/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import jp.vemi.batisfluid.exception.EntityException;
import jp.vemi.batisfluid.meta.FluidColumn;
import jp.vemi.batisfluid.meta.FluidTable;

/**
 * {@link PrimaryKeyInfo} のテストクラスです。
 * 
 * @author H.Kurosawa
 * @version 0.0.2
 */
class PrimaryKeyInfoTest {
    
    @Nested
    @DisplayName("コンストラクタのテスト")
    class ConstructorTest {
        
        @Test
        @DisplayName("フィールドとカラム名のリストでPrimaryKeyInfoを生成できること")
        void constructor_createsInstance() throws NoSuchFieldException {
            Field idField = TestEntity.class.getDeclaredField("id");
            List<Field> fields = Arrays.asList(idField);
            List<String> columnNames = Arrays.asList("id");
            
            PrimaryKeyInfo pkInfo = new PrimaryKeyInfo(fields, columnNames);
            
            assertThat(pkInfo).isNotNull();
            assertThat(pkInfo.getFields()).hasSize(1);
            assertThat(pkInfo.getColumnNames()).containsExactly("id");
        }
        
        @Test
        @DisplayName("複合主キーでPrimaryKeyInfoを生成できること")
        void constructor_withCompositeKey_createsInstance() throws NoSuchFieldException {
            Field keyPart1 = CompositeKeyEntity.class.getDeclaredField("keyPart1");
            Field keyPart2 = CompositeKeyEntity.class.getDeclaredField("keyPart2");
            List<Field> fields = Arrays.asList(keyPart1, keyPart2);
            List<String> columnNames = Arrays.asList("key_part1", "key_part2");
            
            PrimaryKeyInfo pkInfo = new PrimaryKeyInfo(fields, columnNames);
            
            assertThat(pkInfo.getFields()).hasSize(2);
            assertThat(pkInfo.getColumnNames()).containsExactly("key_part1", "key_part2");
        }
    }
    
    @Nested
    @DisplayName("getFields() メソッドのテスト")
    class GetFieldsTest {
        
        @Test
        @DisplayName("主キーフィールドのリストを取得できること")
        void getFields_returnsFieldList() throws NoSuchFieldException {
            Field idField = TestEntity.class.getDeclaredField("id");
            PrimaryKeyInfo pkInfo = new PrimaryKeyInfo(
                Arrays.asList(idField), 
                Arrays.asList("id")
            );
            
            List<Field> fields = pkInfo.getFields();
            
            assertThat(fields).hasSize(1);
            assertThat(fields.get(0).getName()).isEqualTo("id");
        }
    }
    
    @Nested
    @DisplayName("getColumnNames() メソッドのテスト")
    class GetColumnNamesTest {
        
        @Test
        @DisplayName("主キーカラム名のリストを取得できること")
        void getColumnNames_returnsColumnNameList() throws NoSuchFieldException {
            Field idField = TestEntity.class.getDeclaredField("id");
            PrimaryKeyInfo pkInfo = new PrimaryKeyInfo(
                Arrays.asList(idField), 
                Arrays.asList("user_id")
            );
            
            List<String> columnNames = pkInfo.getColumnNames();
            
            assertThat(columnNames).containsExactly("user_id");
        }
        
        @Test
        @DisplayName("複合主キーのカラム名リストを取得できること")
        void getColumnNames_withCompositeKey_returnsAllColumnNames() throws NoSuchFieldException {
            Field keyPart1 = CompositeKeyEntity.class.getDeclaredField("keyPart1");
            Field keyPart2 = CompositeKeyEntity.class.getDeclaredField("keyPart2");
            PrimaryKeyInfo pkInfo = new PrimaryKeyInfo(
                Arrays.asList(keyPart1, keyPart2), 
                Arrays.asList("key_part1", "key_part2")
            );
            
            List<String> columnNames = pkInfo.getColumnNames();
            
            assertThat(columnNames).containsExactly("key_part1", "key_part2");
        }
    }
    
    @Nested
    @DisplayName("getPrimaryKeyValues() メソッドのテスト")
    class GetPrimaryKeyValuesTest {
        
        @Test
        @DisplayName("エンティティから主キーの値を取得できること")
        void getPrimaryKeyValues_returnsKeyValues() throws NoSuchFieldException {
            TestEntity entity = new TestEntity();
            entity.setId(100L);
            
            Field idField = TestEntity.class.getDeclaredField("id");
            PrimaryKeyInfo pkInfo = new PrimaryKeyInfo(
                Arrays.asList(idField), 
                Arrays.asList("id")
            );
            
            Map<String, Object> pkValues = pkInfo.getPrimaryKeyValues(entity);
            
            assertThat(pkValues).containsEntry("id", 100L);
        }
        
        @Test
        @DisplayName("複合主キーの値を取得できること")
        void getPrimaryKeyValues_withCompositeKey_returnsAllKeyValues() throws NoSuchFieldException {
            CompositeKeyEntity entity = new CompositeKeyEntity();
            entity.setKeyPart1("A");
            entity.setKeyPart2(1L);
            
            Field keyPart1 = CompositeKeyEntity.class.getDeclaredField("keyPart1");
            Field keyPart2 = CompositeKeyEntity.class.getDeclaredField("keyPart2");
            PrimaryKeyInfo pkInfo = new PrimaryKeyInfo(
                Arrays.asList(keyPart1, keyPart2), 
                Arrays.asList("key_part1", "key_part2")
            );
            
            Map<String, Object> pkValues = pkInfo.getPrimaryKeyValues(entity);
            
            assertThat(pkValues)
                .containsEntry("key_part1", "A")
                .containsEntry("key_part2", 1L);
        }
        
        @Test
        @DisplayName("主キーがnullの場合もnullとして取得できること")
        void getPrimaryKeyValues_withNullValue_returnsNullValue() throws NoSuchFieldException {
            TestEntity entity = new TestEntity();
            // idは設定しない（null）
            
            Field idField = TestEntity.class.getDeclaredField("id");
            PrimaryKeyInfo pkInfo = new PrimaryKeyInfo(
                Arrays.asList(idField), 
                Arrays.asList("id")
            );
            
            Map<String, Object> pkValues = pkInfo.getPrimaryKeyValues(entity);
            
            assertThat(pkValues).containsEntry("id", null);
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
    
    @FluidTable(name = "composite_key_table")
    static class CompositeKeyEntity {
        @FluidColumn(name = "key_part1", primaryKey = true)
        private String keyPart1;
        
        @FluidColumn(name = "key_part2", primaryKey = true)
        private Long keyPart2;
        
        @FluidColumn(name = "value")
        private String value;
        
        public String getKeyPart1() { return keyPart1; }
        public void setKeyPart1(String keyPart1) { this.keyPart1 = keyPart1; }
        public Long getKeyPart2() { return keyPart2; }
        public void setKeyPart2(Long keyPart2) { this.keyPart2 = keyPart2; }
        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
    }
}
