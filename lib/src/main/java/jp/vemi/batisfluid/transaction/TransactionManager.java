/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.transaction;

import java.util.concurrent.Callable;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import jp.vemi.batisfluid.exception.TransactionException;

/**
 * トランザクション管理の高レベルAPIを提供するクラス。
 * <p>
 * トランザクションの境界制御や伝播制御を行い、アプリケーションに
 * より使いやすいトランザクション管理機能を提供します。
 * TransactionOperationを内部で使用し、より高度な制御を実現します。
 * </p>
 *
 * <pre>
 * 使用例:
 * TransactionManager txManager = new TransactionManager(sqlSessionFactory);
 * txManager.execute(PropagationType.REQUIRED, () -&gt; {
 *     // トランザクション内の処理
 *     return result;
 * });
 * </pre>
 *
 * @version 0.0.2
 * @author BatisFluid
 */
public class TransactionManager {
    private final SqlSessionFactory sqlSessionFactory;
    private final TransactionOperation txOperation;

    /**
     * TransactionManagerを構築します。
     *
     * @param sqlSessionFactory SQLセッションファクトリ
     */
    public TransactionManager(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
        // DataSource をスレッドローカル対応のラッパに差し替え
        try {
            Configuration conf = this.sqlSessionFactory.getConfiguration();
            Environment env = conf.getEnvironment();
            if (env != null && !(env.getDataSource() instanceof ThreadLocalDataSource)) {
                Environment wrapped = new Environment(env.getId(), env.getTransactionFactory(),
                        new ThreadLocalDataSource(env.getDataSource()));
                conf.setEnvironment(wrapped);
            }
        } catch (Exception ignore) {
            // 失敗しても致命的ではない（テスト環境等での安全策）
        }
        this.txOperation = new TransactionOperation(sqlSessionFactory);
    }

    /**
     * 指定された伝播タイプでトランザクションを実行します。
     *
     * @param <T>             戻り値の型
     * @param propagationType トランザクション伝播タイプ
     * @param operation       実行する操作
     * @return 操作の実行結果
     */
    public <T> T execute(PropagationType propagationType, Callable<T> operation) {
        switch (propagationType) {
            case REQUIRED:
                return executeRequired(operation);
            case REQUIRES_NEW:
                return executeRequiresNew(operation);
            case NESTED:
                return executeNested(operation);
            default:
                throw new TransactionException("Unsupported propagation type: " + propagationType);
        }
    }

    /**
     * トランザクション内で処理を実行します。
     *
     * @param <T>                      戻り値の型
     * @param isIndependentTransaction 独立したトランザクションで実行するかどうか
     * @param operation                実行する操作
     * @return 操作の実行結果
     */
    public <T> T executeWithTransaction(boolean isIndependentTransaction, Callable<T> operation) {
        if (isIndependentTransaction) {
            TransactionOperation independentTxOperation = new TransactionOperation(sqlSessionFactory);
            SqlSession session = sqlSessionFactory.openSession(false);
            independentTxOperation.beginIndependent(session);
            try {
                T result = TransactionContext.withOperation(independentTxOperation, operation);
                independentTxOperation.commit();
                return result;
            } catch (Exception e) {
                independentTxOperation.rollback();
                throw new TransactionException("transaction.error.execution", e);
            } finally {
                independentTxOperation.end();
            }
        }

        TransactionOperation op = TransactionContext.getCurrentOperation();
        if (op == null) {
            op = txOperation;
        }

        boolean isNewTransaction = !op.isActive();
        if (isNewTransaction) {
            op.begin(sqlSessionFactory.openSession(false));
        }

        try {
            T result = TransactionContext.withOperation(op, operation);
            if (isNewTransaction) {
                op.commit();
            }
            return result;
        } catch (Exception e) {
            if (isNewTransaction) {
                op.rollback();
            }
            throw new TransactionException("transaction.error.execution", e);
        } finally {
            if (isNewTransaction) {
                op.end();
            }
        }
    }

    /**
     * トランザクションが存在する場合はそれを使用し、
     * 存在しない場合は新規トランザクションを作成します。
     *
     * @param <T>       戻り値の型
     * @param operation 実行する操作
     * @return 操作の実行結果
     */
    private <T> T executeRequired(Callable<T> operation) {
        return executeWithTransaction(false, operation);
    }

    /**
     * 常に新規トランザクションを作成して実行します。
     *
     * @param <T>       戻り値の型
     * @param operation 実行する操作
     * @return 操作の実行結果
     */
    private <T> T executeRequiresNew(Callable<T> operation) {
        return executeWithTransaction(true, operation);
    }

    /**
     * ネストされたトランザクションを作成して実行します。
     * 現在のトランザクションがある場合はそのスコープ内で
     * セーブポイントを作成します。
     *
     * @param <T>       戻り値の型
     * @param operation 実行する操作
     * @return 操作の実行結果
     */
    private <T> T executeNested(Callable<T> operation) {
        if (!txOperation.isActive()) {
            return executeRequired(operation);
        }

        String savepoint = txOperation.createSavepoint();
        try {
            T result = operation.call();
            txOperation.releaseSavepoint(savepoint);
            return result;
        } catch (Exception e) {
            txOperation.rollbackToSavepoint(savepoint);
            throw new TransactionException("transaction.error.nested.execution", e);
        }
    }

    /**
     * トランザクションが活性状態かどうかを返します。
     *
     * @return トランザクションが活性状態の場合true
     */
    public boolean isActive() {
        TransactionOperation current = TransactionContext.getCurrentOperation();
        TransactionOperation op = (current != null) ? current : txOperation;
        return op.isActive();
    }

    /**
     * 現在のトランザクションをコミットします。
     */
    public void commit() {
        TransactionOperation current = TransactionContext.getCurrentOperation();
        TransactionOperation op = (current != null) ? current : txOperation;
        op.commit();
    }

    /**
     * 現在のトランザクションをロールバックします。
     */
    public void rollback() {
        TransactionOperation current = TransactionContext.getCurrentOperation();
        TransactionOperation op = (current != null) ? current : txOperation;
        op.rollback();
    }

    /**
     * 内部で使用されるトランザクション操作を取得します。
     * 
     * @return トランザクション操作
     */
    public TransactionOperation getTransactionOperation() {
        TransactionOperation current = TransactionContext.getCurrentOperation();
        return current != null ? current : txOperation;
    }
}
