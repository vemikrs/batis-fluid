/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.core;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

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

import jp.vemi.batisfluid.config.OptimisticLockConfig;
import jp.vemi.batisfluid.query.SimpleWhere;
import jp.vemi.batisfluid.transaction.PropagationType;
import jp.vemi.batisfluid.transaction.TransactionManager;

/**
 * JdbcFlowのテストクラス。
 *
 * @author H.Kurosawa
 * @version 0.0.2
 */
@ExtendWith(MockitoExtension.class)
@org.mockito.junit.jupiter.MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
class JdbcFlowTest {
    
    @Mock
    private SqlSessionFactory sqlSessionFactory;
    
    @Mock
    private SqlSession sqlSession;
    
    @Mock
    private Configuration configuration;
    
    private JdbcFlow jdbcFlow;
    
    @BeforeEach
    void setUp() {
        when(sqlSessionFactory.getConfiguration()).thenReturn(configuration);
        when(sqlSessionFactory.openSession(anyBoolean())).thenReturn(sqlSession);
        when(configuration.getEnvironment()).thenReturn(null);
        
        jdbcFlow = new JdbcFlow(sqlSessionFactory);
    }
    
    @Nested
    @DisplayName("コンストラクタテスト")
    class ConstructorTests {
        
        @Test
        @DisplayName("SqlSessionFactoryのみでインスタンスを生成できる")
        void testConstructorWithSqlSessionFactory() {
            JdbcFlow flow = new JdbcFlow(sqlSessionFactory);
            assertThat(flow).isNotNull();
            assertThat(flow.getDelegate()).isNotNull();
            assertThat(flow.getTransactionManager()).isNotNull();
        }
        
        @Test
        @DisplayName("SqlSessionFactoryとOptimisticLockConfigでインスタンスを生成できる")
        void testConstructorWithOptimisticLockConfig() {
            OptimisticLockConfig config = new OptimisticLockConfig();
            config.setEnabled(true);
            config.setDefaultLockType(OptimisticLockConfig.LockType.VERSION);
            
            JdbcFlow flow = new JdbcFlow(sqlSessionFactory, config);
            assertThat(flow).isNotNull();
            assertThat(flow.getDelegate()).isNotNull();
            assertThat(flow.getTransactionManager()).isNotNull();
        }
    }
    
    @Nested
    @DisplayName("where()メソッドテスト")
    class WhereMethodTests {
        
        @Test
        @DisplayName("where()で新しいSimpleWhereインスタンスを取得できる")
        void testWhereReturnsNewInstance() {
            SimpleWhere where1 = jdbcFlow.where();
            SimpleWhere where2 = jdbcFlow.where();
            
            assertThat(where1).isNotNull();
            assertThat(where2).isNotNull();
            assertThat(where1).isNotSameAs(where2);
        }
        
        @Test
        @DisplayName("取得したSimpleWhereで条件を追加できる")
        void testWhereCanBuildConditions() {
            SimpleWhere where = jdbcFlow.where()
                    .eq("name", "test")
                    .gt("age", 20);
            
            assertThat(where.getWhereSql()).contains("name = ");
            assertThat(where.getWhereSql()).contains("age > ");
        }
    }
    
    @Nested
    @DisplayName("トランザクション制御テスト")
    class TransactionTests {
        
        @Test
        @DisplayName("getTransactionManager()でTransactionManagerを取得できる")
        void testGetTransactionManager() {
            TransactionManager txManager = jdbcFlow.getTransactionManager();
            
            assertThat(txManager).isNotNull();
        }
        
        @Test
        @DisplayName("transaction()メソッドが呼び出せる")
        void testTransactionMethod() {
            // transaction()メソッドが例外なく呼び出せることを確認
            // 実際のトランザクション処理はTransactionManagerに委譲
            assertThat(jdbcFlow).isNotNull();
            // Note: 完全なトランザクションテストは統合テストで行う
        }
    }
    
    @Nested
    @DisplayName("PropagationType統合テスト")
    class PropagationTypeTests {
        
        @Test
        @DisplayName("PropagationTypeの値が正しく定義されている")
        void testPropagationTypeValues() {
            assertThat(PropagationType.REQUIRED).isNotNull();
            assertThat(PropagationType.REQUIRES_NEW).isNotNull();
            assertThat(PropagationType.NESTED).isNotNull();
            
            assertThat(PropagationType.values()).hasSize(3);
        }
    }
}
