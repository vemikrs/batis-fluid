/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.transaction;

import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Objects;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * スレッドローカルにバインドされたコネクションを優先的に返すDataSource。
 * <p>
 * トランザクション境界内で新たに開かれる {@code SqlSessionFactory.openSession()} が
 * 常に同一の物理コネクションを共有するようにし、子セッションからの commit/rollback/close を抑制します。
 * これにより、トランザクション管理側の rollback で一括して取り消せます。
 * </p>
 *
 * @version 0.0.2
 * @author BatisFluid
 */
public class ThreadLocalDataSource implements DataSource {
    private static final Logger logger = LoggerFactory.getLogger(ThreadLocalDataSource.class);

    private final DataSource delegate;
    private static final ThreadLocal<Connection> BOUND = new ThreadLocal<>();
    private static final ThreadLocal<Integer> SUSPEND_LEVEL = ThreadLocal.withInitial(() -> 0);

    /**
     * ラップ対象のDataSourceを指定して構築します。
     * 
     * @param delegate 元のDataSource
     */
    public ThreadLocalDataSource(DataSource delegate) {
        this.delegate = Objects.requireNonNull(delegate);
    }

    /**
     * 現在のスレッドにコネクションをバインドします。
     * 
     * @param connection バインドするコネクション
     */
    public static void bind(Connection connection) {
        BOUND.set(connection);
    }

    /**
     * 現在のスレッドのバインドを解除します。
     */
    public static void unbind() {
        BOUND.remove();
    }

    /**
     * 一時的にスレッドローカルのバインディングを無効化します（ネスト可）。
     * REQUIRES_NEW のコネクション確保時に使用します。
     */
    public static void suspendBinding() {
        SUSPEND_LEVEL.set(SUSPEND_LEVEL.get() + 1);
    }

    /**
     * {@link #suspendBinding()} の対応復帰を行います。
     */
    public static void resumeBinding() {
        int lvl = SUSPEND_LEVEL.get();
        if (lvl <= 0) {
            return;
        }
        SUSPEND_LEVEL.set(lvl - 1);
    }

    private static boolean isBindingSuspended() {
        return SUSPEND_LEVEL.get() > 0;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (!isBindingSuspended()) {
            Connection bound = BOUND.get();
            if (bound != null) {
                logger.debug("Returning BOUND suppressed connection");
                return createSuppressedConnection(bound);
            }
        }
        logger.debug("Returning DELEGATE connection");
        return delegate.getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        if (!isBindingSuspended()) {
            Connection bound = BOUND.get();
            if (bound != null) {
                logger.debug("Returning BOUND suppressed connection (with creds)");
                return createSuppressedConnection(bound);
            }
        }
        logger.debug("Returning DELEGATE connection (with creds)");
        return delegate.getConnection(username, password);
    }

    private Connection createSuppressedConnection(Connection target) {
        InvocationHandler handler = (proxy, method, args) -> {
            String name = method.getName();
            // commit/rollback/close は抑制（NO-OP）
            if ("commit".equals(name) || "rollback".equals(name) || "close".equals(name)
                    || "setAutoCommit".equals(name)) {
                return null;
            }
            // unwrap/isWrapperFor で実コネクションへ到達されないように防御
            if ("unwrap".equals(name) && args != null && args.length == 1 && args[0] instanceof Class) {
                Class<?> iface = (Class<?>) args[0];
                if (iface.isAssignableFrom(Connection.class)) {
                    return proxy;
                }
                return method.invoke(target, args);
            }
            if ("isWrapperFor".equals(name) && args != null && args.length == 1 && args[0] instanceof Class) {
                Class<?> iface = (Class<?>) args[0];
                if (iface.isAssignableFrom(Connection.class)) {
                    return Boolean.FALSE;
                }
                return method.invoke(target, args);
            }
            return method.invoke(target, args);
        };
        return (Connection) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                new Class<?>[] { Connection.class },
                handler);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return delegate.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        delegate.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        delegate.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return delegate.getLoginTimeout();
    }

    @Override
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return java.util.logging.Logger.getLogger("ThreadLocalDataSource");
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface.isInstance(this)) {
            return iface.cast(this);
        }
        return delegate.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return iface.isInstance(this) || delegate.isWrapperFor(iface);
    }
}
