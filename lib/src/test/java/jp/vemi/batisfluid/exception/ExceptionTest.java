/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * 例外クラスのテストクラス。
 *
 * @author H.Kurosawa
 * @version 0.0.2
 */
class ExceptionTest {
    
    @Test
    void testFluidException() {
        // メッセージのみ
        FluidException e1 = new FluidException("テストエラー");
        assertEquals("テストエラー", e1.getMessage());
        assertNull(e1.getCause());
        
        // メッセージと原因
        RuntimeException cause = new RuntimeException("原因");
        FluidException e2 = new FluidException("テストエラー", cause);
        assertEquals("テストエラー", e2.getMessage());
        assertEquals(cause, e2.getCause());
        
        // 原因のみ
        FluidException e3 = new FluidException(cause);
        assertEquals(cause, e3.getCause());
    }
    
    @Test
    void testFluidSqlException() {
        FluidSqlException e1 = new FluidSqlException("SQL実行エラー");
        assertEquals("SQL実行エラー", e1.getMessage());
        assertTrue(e1 instanceof FluidException);
        
        RuntimeException cause = new RuntimeException("DB接続エラー");
        FluidSqlException e2 = new FluidSqlException("SQL実行エラー", cause);
        assertEquals(cause, e2.getCause());
    }
    
    @Test
    void testNoResultException() {
        // デフォルトメッセージ
        NoResultException e1 = new NoResultException();
        assertNotNull(e1.getMessage());
        assertTrue(e1 instanceof FluidException);
        
        // カスタムメッセージ
        NoResultException e2 = new NoResultException("カスタムメッセージ");
        assertEquals("カスタムメッセージ", e2.getMessage());
    }
    
    @Test
    void testNonUniqueResultException() {
        // デフォルトメッセージ
        NonUniqueResultException e1 = new NonUniqueResultException();
        assertNotNull(e1.getMessage());
        assertTrue(e1 instanceof FluidException);
        
        // カスタムメッセージ
        NonUniqueResultException e2 = new NonUniqueResultException("複数件検出");
        assertEquals("複数件検出", e2.getMessage());
    }
    
    @Test
    void testOptimisticLockException() {
        Object entity = new Object();
        OptimisticLockException e = new OptimisticLockException(
            "楽観ロックエラー", entity, "version", "updatedAt");
        
        assertEquals("楽観ロックエラー", e.getMessage());
        assertEquals(entity, e.getEntity());
        assertArrayEquals(new String[]{"version", "updatedAt"}, e.getProperties());
        assertTrue(e instanceof FluidException);
    }
    
    @Test
    void testSqlParseException() {
        SqlParseException e1 = new SqlParseException("SQL解析エラー");
        assertEquals("SQL解析エラー", e1.getMessage());
        assertTrue(e1 instanceof FluidException);
        
        RuntimeException cause = new RuntimeException("構文エラー");
        SqlParseException e2 = new SqlParseException("SQL解析エラー", cause);
        assertEquals(cause, e2.getCause());
    }
    
    @Test
    void testTypeConversionException() {
        TypeConversionException e1 = new TypeConversionException("型変換エラー");
        assertEquals("型変換エラー", e1.getMessage());
        assertTrue(e1 instanceof FluidException);
    }
    
    @Test
    void testEntityException() {
        EntityException e1 = new EntityException("エンティティエラー");
        assertEquals("エンティティエラー", e1.getMessage());
        assertTrue(e1 instanceof FluidException);
    }
    
    @Test
    void testFluidIllegalStateException() {
        FluidIllegalStateException e1 = new FluidIllegalStateException("不正状態");
        assertEquals("不正状態", e1.getMessage());
        assertTrue(e1 instanceof FluidException);
    }
    
    @Test
    void testTransactionException() {
        TransactionException e1 = new TransactionException("トランザクションエラー");
        assertEquals("トランザクションエラー", e1.getMessage());
        assertTrue(e1 instanceof FluidException);
        
        RuntimeException cause = new RuntimeException("コミット失敗");
        TransactionException e2 = new TransactionException("トランザクションエラー", cause);
        assertEquals(cause, e2.getCause());
    }
    
    @Test
    void testExceptionHierarchy() {
        // 全ての例外がFluidExceptionを継承していることを確認
        assertTrue(FluidException.class.isAssignableFrom(FluidSqlException.class));
        assertTrue(FluidException.class.isAssignableFrom(NoResultException.class));
        assertTrue(FluidException.class.isAssignableFrom(NonUniqueResultException.class));
        assertTrue(FluidException.class.isAssignableFrom(OptimisticLockException.class));
        assertTrue(FluidException.class.isAssignableFrom(SqlParseException.class));
        assertTrue(FluidException.class.isAssignableFrom(TypeConversionException.class));
        assertTrue(FluidException.class.isAssignableFrom(EntityException.class));
        assertTrue(FluidException.class.isAssignableFrom(FluidIllegalStateException.class));
        assertTrue(FluidException.class.isAssignableFrom(TransactionException.class));
        
        // FluidExceptionはRuntimeExceptionを継承
        assertTrue(RuntimeException.class.isAssignableFrom(FluidException.class));
    }
}
