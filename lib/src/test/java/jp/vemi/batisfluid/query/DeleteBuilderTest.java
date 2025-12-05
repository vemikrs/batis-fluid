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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jp.vemi.batisfluid.meta.FluidColumn;
import jp.vemi.batisfluid.meta.FluidTable;
import jp.vemi.seasarbatis.jdbc.SBJdbcManager;

/**
 * {@link DeleteBuilder} のテストクラスです。
 * 
 * @author H.Kurosawa
 * @version 0.0.2
 */
@ExtendWith(MockitoExtension.class)
class DeleteBuilderTest {
    
    @Mock
    private SBJdbcManager jdbcManager;
    
    private DeleteBuilder<TestEntity> deleteBuilder;
    
    @BeforeEach
    void setUp() {
        deleteBuilder = new DeleteBuilder<>(jdbcManager, TestEntity.class);
    }
    
    @Nested
    @DisplayName("build() メソッドのテスト")
    class BuildTest {
        
        @Test
        @DisplayName("条件なしでDELETE文を生成できること")
        void build_withoutConditions_generatesSimpleDelete() {
            String sql = deleteBuilder.build();
            
            assertThat(sql).contains("DELETE FROM test_table");
            assertThat(sql).doesNotContain("WHERE");
        }
        
        @Test
        @DisplayName("WHERE条件付きでDELETE文を生成できること")
        void build_withWhere_generatesDeleteWithWhere() {
            deleteBuilder.where(w -> w.eq("status", "DELETED"));
            
            String sql = deleteBuilder.build();
            
            assertThat(sql).contains("DELETE FROM test_table");
            assertThat(sql).contains("WHERE");
            assertThat(sql).contains("status = ");
        }
        
        @Test
        @DisplayName("複数WHERE条件付きでDELETE文を生成できること")
        void build_withMultipleWhereConditions_generatesDeleteWithMultipleWhere() {
            deleteBuilder.where(w -> w.eq("status", "DELETED").lt("created_at", "2024-01-01"));
            
            String sql = deleteBuilder.build();
            
            assertThat(sql).contains("DELETE FROM test_table");
            assertThat(sql).contains("WHERE");
        }
        
        @Test
        @DisplayName("IN句を使ったDELETE文を生成できること")
        void build_withInClause_generatesDeleteWithIn() {
            deleteBuilder.where(w -> w.in("id", 1L, 2L, 3L));
            
            String sql = deleteBuilder.build();
            
            assertThat(sql).contains("DELETE FROM test_table");
            assertThat(sql).contains("IN");
        }
    }
    
    @Nested
    @DisplayName("where() メソッドのテスト")
    class WhereTest {
        
        @Test
        @DisplayName("Consumer形式でwhere条件を設定できること")
        void where_withConsumer_setsCondition() {
            DeleteBuilder<TestEntity> result = deleteBuilder.where(w -> w.eq("id", 1L));
            
            assertThat(result).isSameAs(deleteBuilder);
            assertThat(deleteBuilder.build()).contains("WHERE");
        }
        
        @Test
        @DisplayName("Where オブジェクト形式でwhere条件を設定できること")
        void where_withWhereObject_setsCondition() {
            SimpleWhere simpleWhere = new SimpleWhere();
            simpleWhere.eq("status", "INACTIVE");
            
            DeleteBuilder<TestEntity> result = deleteBuilder.where(simpleWhere);
            
            assertThat(result).isSameAs(deleteBuilder);
            assertThat(deleteBuilder.build()).contains("WHERE");
        }
        
        @Test
        @DisplayName("IS NULL条件を設定できること")
        void where_withIsNull_setsCondition() {
            deleteBuilder.where(w -> w.isNull("deleted_at"));
            
            String sql = deleteBuilder.build();
            
            assertThat(sql).contains("IS NULL");
        }
        
        @Test
        @DisplayName("IS NOT NULL条件を設定できること")
        void where_withIsNotNull_setsCondition() {
            deleteBuilder.where(w -> w.isNotNull("created_at"));
            
            String sql = deleteBuilder.build();
            
            assertThat(sql).contains("IS NOT NULL");
        }
    }
    
    @Nested
    @DisplayName("getParameters() メソッドのテスト")
    class GetParametersTest {
        
        @Test
        @DisplayName("条件なしでは空のパラメータマップを返すこと")
        void getParameters_withoutConditions_returnsEmptyMap() {
            deleteBuilder.build();
            
            assertThat(deleteBuilder.getParameters()).isEmpty();
        }
        
        @Test
        @DisplayName("WHERE条件のパラメータが含まれること")
        void getParameters_withWhere_containsParameters() {
            deleteBuilder.where(w -> w.eq("status", "DELETED"));
            deleteBuilder.build();
            
            assertThat(deleteBuilder.getParameters()).containsValue("DELETED");
        }
        
        @Test
        @DisplayName("複数WHERE条件のパラメータが含まれること")
        void getParameters_withMultipleWhere_containsAllParameters() {
            deleteBuilder.where(w -> w.eq("status", "DELETED").eq("type", "TEMP"));
            deleteBuilder.build();
            
            assertThat(deleteBuilder.getParameters())
                .containsValue("DELETED")
                .containsValue("TEMP");
        }
    }
    
    @Nested
    @DisplayName("execute() メソッドのテスト")
    class ExecuteTest {
        
        @Test
        @DisplayName("DELETE文を実行して削除行数を返すこと")
        void execute_returnsDeletedRowCount() {
            when(jdbcManager.delete(anyString(), anyMap())).thenReturn(10);
            
            deleteBuilder.where(w -> w.eq("status", "DELETED"));
            int result = deleteBuilder.execute();
            
            assertThat(result).isEqualTo(10);
        }
        
        @Test
        @DisplayName("JdbcManagerのdeleteメソッドが呼び出されること")
        void execute_callsJdbcManagerDelete() {
            when(jdbcManager.delete(anyString(), anyMap())).thenReturn(1);
            
            deleteBuilder.where(w -> w.eq("id", 1L));
            deleteBuilder.execute();
            
            verify(jdbcManager).delete(anyString(), anyMap());
        }
        
        @Test
        @DisplayName("条件なしでも削除を実行できること")
        void execute_withoutConditions_deletesAll() {
            when(jdbcManager.delete(anyString(), anyMap())).thenReturn(100);
            
            int result = deleteBuilder.execute();
            
            assertThat(result).isEqualTo(100);
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
