/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import jp.vemi.batisfluid.meta.FluidTable;
import jp.vemi.batisfluid.meta.FluidColumn;
import jp.vemi.batisfluid.exception.FluidIllegalStateException;

/**
 * {@link EntityOperations} のテストクラスです。
 * 
 * @author H.Kurosawa
 * @version 0.0.2
 */
class EntityOperationsTest {
    
    @Nested
    @DisplayName("getTableName テスト")
    class GetTableNameTest {
        
        @Test
        @DisplayName("FluidTableアノテーションからテーブル名を取得できること")
        void getTableName_withFluidTable_returnsTableName() {
            String tableName = EntityOperations.getTableName(TestEntity.class);
            
            assertThat(tableName).isEqualTo("test_table");
        }
        
        @Test
        @DisplayName("スキーマ付きテーブル名を取得できること")
        void getTableName_withSchema_returnsSchemaAndTableName() {
            String tableName = EntityOperations.getTableName(SchemaEntity.class);
            
            assertThat(tableName).isEqualTo("test_schema.schema_table");
        }
        
        @Test
        @DisplayName("アノテーションがない場合はクラス名を小文字で返すこと")
        void getTableName_withoutAnnotation_returnsLowerCaseClassName() {
            String tableName = EntityOperations.getTableName(NoAnnotationEntity.class);
            
            assertThat(tableName).isEqualTo("noannotationentity");
        }
    }
    
    @Nested
    @DisplayName("getEntityParams テスト")
    class GetEntityParamsTest {
        
        @Test
        @DisplayName("エンティティからパラメータマップを取得できること")
        void getEntityParams_returnsParamsMap() {
            TestEntity entity = new TestEntity();
            entity.setId(1L);
            entity.setName("test");
            
            Map<String, Object> params = EntityOperations.getEntityParams(entity);
            
            assertThat(params)
                .containsEntry("id", 1L)
                .containsEntry("name", "test");
        }
    }
    
    @Nested
    @DisplayName("getPrimaryKeyInfo テスト")
    class GetPrimaryKeyInfoTest {
        
        @Test
        @DisplayName("主キー情報を取得できること")
        void getPrimaryKeyInfo_returnsPrimaryKeyInfo() {
            PrimaryKeyInfo pkInfo = EntityOperations.getPrimaryKeyInfo(TestEntity.class);
            
            assertThat(pkInfo.getColumnNames()).containsExactly("id");
        }
        
        @Test
        @DisplayName("主キーがない場合は例外が発生すること")
        void getPrimaryKeyInfo_withoutPrimaryKey_throwsException() {
            assertThatThrownBy(() -> EntityOperations.getPrimaryKeyInfo(NoPrimaryKeyEntity.class))
                .isInstanceOf(FluidIllegalStateException.class);
        }
    }
    
    @Nested
    @DisplayName("getPrimaryKeyValues テスト")
    class GetPrimaryKeyValuesTest {
        
        @Test
        @DisplayName("主キーの値を取得できること")
        void getPrimaryKeyValues_returnsPrimaryKeyValues() {
            TestEntity entity = new TestEntity();
            entity.setId(123L);
            
            Map<String, Object> pkValues = EntityOperations.getPrimaryKeyValues(entity);
            
            assertThat(pkValues).containsEntry("id", 123L);
        }
    }
    
    // テスト用エンティティ
    
    @FluidTable(name = "test_table")
    static class TestEntity {
        @FluidColumn(name = "id", primaryKey = true)
        private Long id;
        
        @FluidColumn(name = "name")
        private String name;
        
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
    
    @FluidTable(name = "schema_table", schema = "test_schema")
    static class SchemaEntity {
        @FluidColumn(name = "id", primaryKey = true)
        private Long id;
        
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
    }
    
    static class NoAnnotationEntity {
        private Long id;
        
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
    }
    
    @FluidTable(name = "no_pk_table")
    static class NoPrimaryKeyEntity {
        @FluidColumn(name = "name")
        private String name;
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
}
