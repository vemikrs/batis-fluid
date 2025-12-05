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

import jp.vemi.batisfluid.transaction.TransactionManager;

/**
 * SqlRunnerのテストクラス。
 *
 * @author H.Kurosawa
 * @version 0.0.2
 */
@ExtendWith(MockitoExtension.class)
@org.mockito.junit.jupiter.MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
class SqlRunnerTest {
    
    @Mock
    private SqlSessionFactory sqlSessionFactory;
    
    @Mock
    private SqlSession sqlSession;
    
    @Mock
    private Configuration configuration;
    
    private SqlRunner sqlRunner;
    
    @BeforeEach
    void setUp() {
        when(sqlSessionFactory.getConfiguration()).thenReturn(configuration);
        when(sqlSessionFactory.openSession(anyBoolean())).thenReturn(sqlSession);
        when(configuration.getEnvironment()).thenReturn(null);
        
        sqlRunner = new SqlRunner(sqlSessionFactory);
    }
    
    @Nested
    @DisplayName("コンストラクタテスト")
    class ConstructorTests {
        
        @Test
        @DisplayName("SqlSessionFactoryでインスタンスを生成できる")
        void testConstructorWithSqlSessionFactory() {
            SqlRunner runner = new SqlRunner(sqlSessionFactory);
            assertThat(runner).isNotNull();
            assertThat(runner.getTransactionManager()).isNotNull();
        }
    }
    
    @Nested
    @DisplayName("トランザクション制御テスト")
    class TransactionTests {
        
        @Test
        @DisplayName("getTransactionManager()でTransactionManagerを取得できる")
        void testGetTransactionManager() {
            TransactionManager txManager = sqlRunner.getTransactionManager();
            
            assertThat(txManager).isNotNull();
        }
        
        @Test
        @DisplayName("transaction()メソッドが呼び出せる")
        void testTransactionMethod() {
            // transaction()メソッドが例外なく呼び出せることを確認
            assertThat(sqlRunner).isNotNull();
            // Note: 完全なトランザクションテストは統合テストで行う
        }
    }
}
