/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jp.vemi.batisfluid.meta.FluidColumn;
import jp.vemi.batisfluid.meta.FluidTable;
import jp.vemi.seasarbatis.jdbc.SBJdbcManager;

/**
 * {@link UpdateBuilder} のテストクラスです。
 * 
 * @author H.Kurosawa
 * @version 0.0.2
 */
@ExtendWith(MockitoExtension.class)
class UpdateBuilderTest {
    
    @Mock
    private SBJdbcManager jdbcManager;
    
    private UpdateBuilder<TestEntity> updateBuilder;
    
    @BeforeEach
    void setUp() {
        updateBuilder = new UpdateBuilder<>(jdbcManager, TestEntity.class);
    }
    
    @Nested
    @DisplayName("build() メソッドのテスト")
    class BuildTest {
        
        @Test
        @DisplayName("単一カラムのUPDATE文を生成できること")
        void build_withSingleColumn_generatesUpdateSql() {
            updateBuilder.set("name", "newName");
            
            String sql = updateBuilder.build();
            
            assertThat(sql).contains("UPDATE test_table SET");
            assertThat(sql).contains("name = ");
        }
        
        @Test
        @DisplayName("複数カラムのUPDATE文を生成できること")
        void build_withMultipleColumns_generatesUpdateSql() {
            updateBuilder
                .set("name", "newName")
                .set("status", "ACTIVE");
            
            String sql = updateBuilder.build();
            
            assertThat(sql).contains("UPDATE test_table SET");
            assertThat(sql).contains("name = ");
            assertThat(sql).contains("status = ");
        }
        
        @Test
        @DisplayName("WHERE条件付きでUPDATE文を生成できること")
        void build_withWhere_generatesUpdateWithWhere() {
            updateBuilder
                .set("status", "INACTIVE")
                .where(w -> w.eq("id", 1L));
            
            String sql = updateBuilder.build();
            
            assertThat(sql).contains("UPDATE test_table SET");
            assertThat(sql).contains("WHERE");
            assertThat(sql).contains("id = ");
        }
        
        @Test
        @DisplayName("複数WHERE条件付きでUPDATE文を生成できること")
        void build_withMultipleWhereConditions_generatesUpdateWithMultipleWhere() {
            updateBuilder
                .set("status", "INACTIVE")
                .where(w -> w.eq("status", "ACTIVE").gt("created_at", "2025-01-01"));
            
            String sql = updateBuilder.build();
            
            assertThat(sql).contains("UPDATE test_table SET");
            assertThat(sql).contains("WHERE");
        }
    }
    
    @Nested
    @DisplayName("set() メソッドのテスト")
    class SetTest {
        
        @Test
        @DisplayName("メソッドチェーンで複数カラムを設定できること")
        void set_supportsMethodChaining() {
            UpdateBuilder<TestEntity> result = updateBuilder
                .set("name", "test")
                .set("status", "ACTIVE")
                .set("age", 25);
            
            assertThat(result).isSameAs(updateBuilder);
        }
        
        @Test
        @DisplayName("null値も設定できること")
        void set_withNullValue_setsNull() {
            updateBuilder.set("name", null);
            updateBuilder.build();
            
            assertThat(updateBuilder.getParameters()).containsEntry("name", null);
        }
    }
    
    @Nested
    @DisplayName("where() メソッドのテスト")
    class WhereTest {
        
        @Test
        @DisplayName("Consumer形式でwhere条件を設定できること")
        void where_withConsumer_setsCondition() {
            updateBuilder.set("name", "test");
            UpdateBuilder<TestEntity> result = updateBuilder.where(w -> w.eq("id", 1L));
            
            assertThat(result).isSameAs(updateBuilder);
            assertThat(updateBuilder.build()).contains("WHERE");
        }
        
        @Test
        @DisplayName("Where オブジェクト形式でwhere条件を設定できること")
        void where_withWhereObject_setsCondition() {
            SimpleWhere simpleWhere = new SimpleWhere();
            simpleWhere.eq("status", "ACTIVE");
            
            updateBuilder.set("name", "test");
            UpdateBuilder<TestEntity> result = updateBuilder.where(simpleWhere);
            
            assertThat(result).isSameAs(updateBuilder);
            assertThat(updateBuilder.build()).contains("WHERE");
        }
    }
    
    @Nested
    @DisplayName("getParameters() メソッドのテスト")
    class GetParametersTest {
        
        @Test
        @DisplayName("SET句のパラメータが含まれること")
        void getParameters_withSet_containsSetParameters() {
            updateBuilder.set("name", "testName").set("age", 30);
            updateBuilder.build();
            
            assertThat(updateBuilder.getParameters())
                .containsEntry("name", "testName")
                .containsEntry("age", 30);
        }
        
        @Test
        @DisplayName("WHERE句のパラメータも含まれること")
        void getParameters_withWhereAndSet_containsBothParameters() {
            updateBuilder
                .set("name", "testName")
                .where(w -> w.eq("id", 100L));
            updateBuilder.build();
            
            assertThat(updateBuilder.getParameters())
                .containsEntry("name", "testName")
                .containsValue(100L);
        }
    }
    
    @Nested
    @DisplayName("execute() メソッドのテスト")
    class ExecuteTest {
        
        @Test
        @DisplayName("UPDATE文を実行して影響行数を返すこと")
        void execute_returnsAffectedRowCount() {
            when(jdbcManager.update(anyString(), anyMap())).thenReturn(5);
            
            updateBuilder.set("status", "INACTIVE");
            int result = updateBuilder.execute();
            
            assertThat(result).isEqualTo(5);
        }
        
        @Test
        @DisplayName("JdbcManagerのupdateメソッドが呼び出されること")
        void execute_callsJdbcManagerUpdate() {
            when(jdbcManager.update(anyString(), anyMap())).thenReturn(1);
            
            updateBuilder
                .set("name", "updated")
                .where(w -> w.eq("id", 1L));
            updateBuilder.execute();
            
            verify(jdbcManager).update(anyString(), anyMap());
        }
    }
    
    // テスト用エンティティ
    @FluidTable(name = "test_table")
    static class TestEntity {
        @FluidColumn(name = "id", primaryKey = true)
        private Long id;
        
        @FluidColumn(name = "name")
        private String name;
        
        @FluidColumn(name = "status")
        private String status;
        
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
