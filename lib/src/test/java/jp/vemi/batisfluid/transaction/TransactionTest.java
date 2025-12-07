/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Transaction関連クラスのテスト。
 *
 * @version 0.0.2
 */
class TransactionTest {

    @Nested
    @DisplayName("PropagationType列挙型のテスト")
    class PropagationTypeTest {

        @Test
        @DisplayName("全ての伝播タイプが定義されている")
        void testAllPropagationTypes() {
            assertThat(PropagationType.values())
                    .containsExactlyInAnyOrder(
                            PropagationType.REQUIRED,
                            PropagationType.REQUIRES_NEW,
                            PropagationType.NESTED);
        }
    }

    @Nested
    @DisplayName("TransactionContextのテスト")
    class TransactionContextTest {

        @Test
        @DisplayName("操作を設定・取得・クリアできる")
        void testSetGetClearOperation() {
            TransactionContext.clearCurrentOperation();
            assertThat(TransactionContext.getCurrentOperation()).isNull();

            TransactionOperation mockOp = mock(TransactionOperation.class);
            TransactionContext.setCurrentOperation(mockOp);
            assertThat(TransactionContext.getCurrentOperation()).isSameAs(mockOp);

            TransactionContext.clearCurrentOperation();
            assertThat(TransactionContext.getCurrentOperation()).isNull();
        }

        @Test
        @DisplayName("withOperationでコンテキストが設定される")
        void testWithOperation() throws Exception {
            TransactionContext.clearCurrentOperation();
            TransactionOperation mockOp = mock(TransactionOperation.class);

            AtomicBoolean wasSet = new AtomicBoolean(false);
            String result = TransactionContext.withOperation(mockOp, () -> {
                wasSet.set(TransactionContext.getCurrentOperation() == mockOp);
                return "done";
            });

            assertThat(wasSet.get()).isTrue();
            assertThat(result).isEqualTo("done");
            assertThat(TransactionContext.getCurrentOperation()).isNull();
        }

        @Test
        @DisplayName("withOperationでネストしても前のコンテキストが復帰する")
        void testWithOperationNested() throws Exception {
            TransactionContext.clearCurrentOperation();
            TransactionOperation mockOp1 = mock(TransactionOperation.class);
            TransactionOperation mockOp2 = mock(TransactionOperation.class);

            TransactionContext.setCurrentOperation(mockOp1);

            TransactionContext.withOperation(mockOp2, () -> {
                assertThat(TransactionContext.getCurrentOperation()).isSameAs(mockOp2);
                return null;
            });

            assertThat(TransactionContext.getCurrentOperation()).isSameAs(mockOp1);
            TransactionContext.clearCurrentOperation();
        }
    }

    @Nested
    @DisplayName("ThreadLocalDataSourceのテスト")
    class ThreadLocalDataSourceTest {

        @Test
        @DisplayName("バインドされたコネクションが返される")
        void testBoundConnectionReturned() throws SQLException {
            DataSource mockDelegate = mock(DataSource.class);
            Connection mockConnection = mock(Connection.class);
            Connection mockNewConnection = mock(Connection.class);

            when(mockDelegate.getConnection()).thenReturn(mockNewConnection);

            ThreadLocalDataSource ds = new ThreadLocalDataSource(mockDelegate);

            // バインドなしの場合
            ThreadLocalDataSource.unbind();
            Connection conn1 = ds.getConnection();
            assertThat(conn1).isSameAs(mockNewConnection);

            // バインドありの場合（抑制プロキシが返される）
            ThreadLocalDataSource.bind(mockConnection);
            Connection conn2 = ds.getConnection();
            // プロキシなので同一ではないが、Connectionインターフェースを実装
            assertThat(conn2).isNotNull();
            assertThat(conn2).isInstanceOf(Connection.class);

            ThreadLocalDataSource.unbind();
        }

        @Test
        @DisplayName("suspendBindingでバインドが一時停止される")
        void testSuspendBinding() throws SQLException {
            DataSource mockDelegate = mock(DataSource.class);
            Connection mockConnection = mock(Connection.class);
            Connection mockNewConnection = mock(Connection.class);

            when(mockDelegate.getConnection()).thenReturn(mockNewConnection);

            ThreadLocalDataSource ds = new ThreadLocalDataSource(mockDelegate);
            ThreadLocalDataSource.bind(mockConnection);

            // 通常時はバインドされたコネクション
            Connection bound = ds.getConnection();
            assertThat(bound).isNotSameAs(mockNewConnection);

            // suspendするとデリゲートコネクション
            ThreadLocalDataSource.suspendBinding();
            Connection suspended = ds.getConnection();
            assertThat(suspended).isSameAs(mockNewConnection);

            // resumeするとバインドされたコネクション
            ThreadLocalDataSource.resumeBinding();
            Connection resumed = ds.getConnection();
            assertThat(resumed).isNotSameAs(mockNewConnection);

            ThreadLocalDataSource.unbind();
        }
    }

    @Nested
    @DisplayName("TransactionOperationのテスト")
    class TransactionOperationTest {

        private SqlSessionFactory mockFactory;
        private SqlSession mockSession;
        private Connection mockConnection;

        @BeforeEach
        void setUp() throws SQLException {
            mockFactory = mock(SqlSessionFactory.class);
            mockSession = mock(SqlSession.class);
            mockConnection = mock(Connection.class);

            when(mockFactory.openSession(false)).thenReturn(mockSession);
            when(mockSession.getConnection()).thenReturn(mockConnection);
        }

        @Test
        @DisplayName("アクティブ状態を正しく追跡する")
        void testIsActive() {
            TransactionOperation op = new TransactionOperation(mockFactory);

            assertThat(op.isActive()).isFalse();

            op.begin();
            assertThat(op.isActive()).isTrue();

            op.end();
            assertThat(op.isActive()).isFalse();
        }
    }
}
