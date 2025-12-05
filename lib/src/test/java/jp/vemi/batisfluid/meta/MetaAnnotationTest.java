/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.meta;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

/**
 * メタデータアノテーションのテストクラス。
 *
 * @author H.Kurosawa
 * @version 0.0.2
 */
class MetaAnnotationTest {
    
    @FluidTable(name = "users", schema = "public")
    static class TestEntity {
        @FluidColumn(name = "id", primaryKey = true)
        private Long id;
        
        @FluidColumn(name = "name")
        private String name;
        
        @FluidColumn(name = "version", versionColumn = true)
        private Long version;
        
        @FluidColumn(name = "updated_at", lastModifiedColumn = true)
        private java.time.LocalDateTime updatedAt;
    }
    
    @FluidTable(name = "simple_table")
    static class SimpleEntity {
        @FluidColumn(name = "id")
        private Long id;
    }
    
    @Test
    void testFluidTableAnnotation() {
        FluidTable table = TestEntity.class.getAnnotation(FluidTable.class);
        assertNotNull(table, "FluidTableアノテーションが取得できるべき");
        assertEquals("users", table.name(), "テーブル名が一致するべき");
        assertEquals("public", table.schema(), "スキーマ名が一致するべき");
    }
    
    @Test
    void testFluidTableDefaultSchema() {
        FluidTable table = SimpleEntity.class.getAnnotation(FluidTable.class);
        assertNotNull(table, "FluidTableアノテーションが取得できるべき");
        assertEquals("simple_table", table.name(), "テーブル名が一致するべき");
        assertEquals("", table.schema(), "スキーマのデフォルト値は空文字列");
    }
    
    @Test
    void testFluidColumnPrimaryKey() throws NoSuchFieldException {
        Field idField = TestEntity.class.getDeclaredField("id");
        FluidColumn column = idField.getAnnotation(FluidColumn.class);
        
        assertNotNull(column, "FluidColumnアノテーションが取得できるべき");
        assertEquals("id", column.name(), "カラム名が一致するべき");
        assertTrue(column.primaryKey(), "主キーフラグがtrueであるべき");
        assertFalse(column.versionColumn(), "バージョンカラムフラグがfalseであるべき");
        assertFalse(column.lastModifiedColumn(), "最終更新日時カラムフラグがfalseであるべき");
    }
    
    @Test
    void testFluidColumnVersionColumn() throws NoSuchFieldException {
        Field versionField = TestEntity.class.getDeclaredField("version");
        FluidColumn column = versionField.getAnnotation(FluidColumn.class);
        
        assertNotNull(column, "FluidColumnアノテーションが取得できるべき");
        assertEquals("version", column.name(), "カラム名が一致するべき");
        assertFalse(column.primaryKey(), "主キーフラグがfalseであるべき");
        assertTrue(column.versionColumn(), "バージョンカラムフラグがtrueであるべき");
        assertFalse(column.lastModifiedColumn(), "最終更新日時カラムフラグがfalseであるべき");
    }
    
    @Test
    void testFluidColumnLastModifiedColumn() throws NoSuchFieldException {
        Field updatedAtField = TestEntity.class.getDeclaredField("updatedAt");
        FluidColumn column = updatedAtField.getAnnotation(FluidColumn.class);
        
        assertNotNull(column, "FluidColumnアノテーションが取得できるべき");
        assertEquals("updated_at", column.name(), "カラム名が一致するべき");
        assertFalse(column.primaryKey(), "主キーフラグがfalseであるべき");
        assertFalse(column.versionColumn(), "バージョンカラムフラグがfalseであるべき");
        assertTrue(column.lastModifiedColumn(), "最終更新日時カラムフラグがtrueであるべき");
    }
    
    @Test
    void testFluidColumnDefaultValues() throws NoSuchFieldException {
        Field nameField = TestEntity.class.getDeclaredField("name");
        FluidColumn column = nameField.getAnnotation(FluidColumn.class);
        
        assertNotNull(column, "FluidColumnアノテーションが取得できるべき");
        assertEquals("name", column.name(), "カラム名が一致するべき");
        assertFalse(column.primaryKey(), "主キーフラグのデフォルトはfalse");
        assertFalse(column.versionColumn(), "バージョンカラムフラグのデフォルトはfalse");
        assertFalse(column.lastModifiedColumn(), "最終更新日時カラムフラグのデフォルトはfalse");
    }
    
    @Test
    void testAnnotationRetention() {
        // FluidTableアノテーションのRetentionPolicyを確認
        Retention tableRetention = FluidTable.class.getAnnotation(Retention.class);
        assertNotNull(tableRetention);
        assertEquals(RetentionPolicy.RUNTIME, tableRetention.value(), 
            "FluidTableはRUNTIMEで保持されるべき");
        
        // FluidColumnアノテーションのRetentionPolicyを確認
        Retention columnRetention = FluidColumn.class.getAnnotation(Retention.class);
        assertNotNull(columnRetention);
        assertEquals(RetentionPolicy.RUNTIME, columnRetention.value(), 
            "FluidColumnはRUNTIMEで保持されるべき");
    }
    
    @Test
    void testAnnotationTarget() {
        // FluidTableアノテーションのTargetを確認
        Target tableTarget = FluidTable.class.getAnnotation(Target.class);
        assertNotNull(tableTarget);
        assertArrayEquals(new ElementType[]{ElementType.TYPE}, tableTarget.value(), 
            "FluidTableはTYPEに適用されるべき");
        
        // FluidColumnアノテーションのTargetを確認
        Target columnTarget = FluidColumn.class.getAnnotation(Target.class);
        assertNotNull(columnTarget);
        assertArrayEquals(new ElementType[]{ElementType.FIELD}, columnTarget.value(), 
            "FluidColumnはFIELDに適用されるべき");
    }
}
