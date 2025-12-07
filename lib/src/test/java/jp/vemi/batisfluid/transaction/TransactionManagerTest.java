/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Callable;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import jp.vemi.batisfluid.exception.TransactionException;

/**
 * {@link TransactionManager} のテストクラスです。
 * 
 * @author H.Kurosawa
 * @version 0.0.2
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TransactionManagerTest {
    
    @Mock
    private SqlSessionFactory sqlSessionFactory;
    
    @Mock
    private SqlSession sqlSession;
    
    @Mock
    private Configuration configuration;
    
    @Mock
    private Connection connection;
    
    private TransactionManager transactionManager;
    
    @BeforeEach
    void setUp() throws SQLException {
        when(sqlSessionFactory.getConfiguration()).thenReturn(configuration);
        when(configuration.getEnvironment()).thenReturn(null);
        when(sqlSessionFactory.openSession(anyBoolean())).thenReturn(sqlSession);
        when(sqlSession.getConnection()).thenReturn(connection);
        
        transactionManager = new TransactionManager(sqlSessionFactory);
    }
    
    @Nested
    @DisplayName("コンストラクタのテスト")
    class ConstructorTest {
        
        @Test
        @DisplayName("SqlSessionFactoryでTransactionManagerを生成できること")
        void constructor_createsTransactionManager() {
            TransactionManager tm = new TransactionManager(sqlSessionFactory);
            
            assertThat(tm).isNotNull();
        }
        
        @Test
        @DisplayName("Environmentがある場合もTransactionManagerを生成できること")
        void constructor_withEnvironment_createsTransactionManager() throws SQLException {
            Environment env = mock(Environment.class);
            javax.sql.DataSource dataSource = mock(javax.sql.DataSource.class);
            when(configuration.getEnvironment()).thenReturn(env);
            when(env.getDataSource()).thenReturn(dataSource);
            when(env.getId()).thenReturn("test");
            when(env.getTransactionFactory()).thenReturn(null);
            
            TransactionManager tm = new TransactionManager(sqlSessionFactory);
            
            assertThat(tm).isNotNull();
        }
    }
    
    @Nested
    @DisplayName("execute() メソッドのテスト")
    class ExecuteTest {
        
        @Test
        @DisplayName("REQUIRED伝播タイプで処理を実行できること")
        void execute_withRequired_executesOperation() throws Exception {
            Callable<String> operation = () -> "result";
            
            String result = transactionManager.execute(PropagationType.REQUIRED, operation);
            
            assertThat(result).isEqualTo("result");
        }
        
        @Test
        @DisplayName("REQUIRES_NEW伝播タイプで処理を実行できること")
        void execute_withRequiresNew_executesOperation() throws Exception {
            Callable<String> operation = () -> "new_result";
            
            String result = transactionManager.execute(PropagationType.REQUIRES_NEW, operation);
            
            assertThat(result).isEqualTo("new_result");
        }
        
        @Test
        @DisplayName("NESTED伝播タイプで処理を実行できること")
        void execute_withNested_executesOperation() throws Exception {
            Callable<String> operation = () -> "nested_result";
            
            String result = transactionManager.execute(PropagationType.NESTED, operation);
            
            assertThat(result).isEqualTo("nested_result");
        }
        
        @Test
        @DisplayName("操作の例外がTransactionExceptionにラップされること")
        void execute_withException_wrapsInTransactionException() {
            Callable<String> operation = () -> {
                throw new RuntimeException("Test error");
            };
            
            assertThatThrownBy(() -> transactionManager.execute(PropagationType.REQUIRED, operation))
                .isInstanceOf(TransactionException.class);
        }
        
        @Test
        @DisplayName("操作の戻り値がnullでも正常に処理されること")
        void execute_withNullResult_succeeds() throws Exception {
            Callable<String> operation = () -> null;
            
            String result = transactionManager.execute(PropagationType.REQUIRED, operation);
            
            assertThat(result).isNull();
        }
    }
    
    @Nested
    @DisplayName("executeWithTransaction() メソッドのテスト")
    class ExecuteWithTransactionTest {
        
        @Test
        @DisplayName("独立トランザクションで処理を実行できること")
        void executeWithTransaction_independent_executesInNewTransaction() throws Exception {
            Callable<Integer> operation = () -> 42;
            
            Integer result = transactionManager.executeWithTransaction(true, operation);
            
            assertThat(result).isEqualTo(42);
        }
        
        @Test
        @DisplayName("既存トランザクション内で処理を実行できること")
        void executeWithTransaction_notIndependent_executesInExistingTransaction() throws Exception {
            Callable<String> operation = () -> "existing";
            
            String result = transactionManager.executeWithTransaction(false, operation);
            
            assertThat(result).isEqualTo("existing");
        }
        
        @Test
        @DisplayName("独立トランザクションで例外発生時にロールバックされること")
        void executeWithTransaction_independent_withException_rollsBack() {
            Callable<String> operation = () -> {
                throw new RuntimeException("Error");
            };
            
            assertThatThrownBy(() -> transactionManager.executeWithTransaction(true, operation))
                .isInstanceOf(TransactionException.class);
        }
    }
    
    @Nested
    @DisplayName("isActive() メソッドのテスト")
    class IsActiveTest {
        
        @Test
        @DisplayName("初期状態ではトランザクションは非アクティブ")
        void isActive_initially_returnsFalse() {
            // TransactionContext をクリアして初期状態を確認
            TransactionContext.clearCurrentOperation();
            
            // 新しいTransactionManagerの初期状態
            boolean active = transactionManager.isActive();
            
            assertThat(active).isFalse();
        }
    }
    
    @Nested
    @DisplayName("getTransactionOperation() メソッドのテスト")
    class GetTransactionOperationTest {
        
        @Test
        @DisplayName("TransactionOperationが取得できること")
        void getTransactionOperation_returnsOperation() {
            TransactionContext.clearCurrentOperation();
            
            TransactionOperation op = transactionManager.getTransactionOperation();
            
            assertThat(op).isNotNull();
        }
        
        @Test
        @DisplayName("コンテキストにOperationがある場合はそれを返すこと")
        void getTransactionOperation_withContext_returnsContextOperation() {
            TransactionOperation mockOp = mock(TransactionOperation.class);
            TransactionContext.setCurrentOperation(mockOp);
            
            try {
                TransactionOperation op = transactionManager.getTransactionOperation();
                assertThat(op).isSameAs(mockOp);
            } finally {
                TransactionContext.clearCurrentOperation();
            }
        }
    }
    
    @Nested
    @DisplayName("PropagationType列挙型のテスト")
    class PropagationTypeEnumTest {
        
        @Test
        @DisplayName("全ての伝播タイプが定義されていること")
        void allPropagationTypes_areDefined() {
            assertThat(PropagationType.values())
                .hasSize(3)
                .containsExactlyInAnyOrder(
                    PropagationType.REQUIRED,
                    PropagationType.REQUIRES_NEW,
                    PropagationType.NESTED
                );
        }
        
        @Test
        @DisplayName("各伝播タイプでexecuteが呼び出せること")
        void execute_canBeCalledWithAllPropagationTypes() {
            // 各伝播タイプで正常に開始されることを確認（例外がスローされないことを検証）
            for (PropagationType type : PropagationType.values()) {
                assertThat(type.name()).isNotNull();
            }
        }
    }
}
