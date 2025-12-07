/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jp.vemi.batisfluid.exception.NonUniqueResultException;
import jp.vemi.batisfluid.meta.FluidColumn;
import jp.vemi.batisfluid.meta.FluidTable;
import jp.vemi.seasarbatis.core.query.SBSelect;
import jp.vemi.seasarbatis.jdbc.SBJdbcManager;

/**
 * {@link SelectBuilder} のテストクラスです。
 * 
 * @author H.Kurosawa
 * @version 0.0.2
 */
@ExtendWith(MockitoExtension.class)
class SelectBuilderTest {
    
    @Mock
    private SBJdbcManager jdbcManager;
    
    @Mock
    private SBSelect<TestEntity> selectQuery;
    
    private SelectBuilder<TestEntity> selectBuilder;
    
    @BeforeEach
    void setUp() {
        selectBuilder = new SelectBuilder<>(jdbcManager, TestEntity.class);
    }
    
    @Nested
    @DisplayName("build() メソッドのテスト")
    class BuildTest {
        
        @Test
        @DisplayName("条件なしでSELECT文を生成できること")
        void build_withoutConditions_generatesSimpleSelect() {
            String sql = selectBuilder.build();
            
            assertThat(sql).contains("SELECT * FROM test_table");
        }
        
        @Test
        @DisplayName("WHERE条件付きでSELECT文を生成できること")
        void build_withWhere_generatesSelectWithWhere() {
            selectBuilder.where(w -> w.eq("name", "test"));
            
            String sql = selectBuilder.build();
            
            assertThat(sql).contains("SELECT * FROM test_table");
            assertThat(sql).contains("WHERE");
            assertThat(sql).contains("name = ");
        }
        
        @Test
        @DisplayName("ORDER BY句付きでSELECT文を生成できること")
        void build_withOrderBy_generatesSelectWithOrderBy() {
            selectBuilder.orderBy("created_at", OrderDirection.DESC);
            
            String sql = selectBuilder.build();
            
            assertThat(sql).contains("SELECT * FROM test_table");
            assertThat(sql).contains("ORDER BY");
            assertThat(sql).contains("created_at DESC");
        }
        
        @Test
        @DisplayName("複数のORDER BY条件を追加できること")
        void build_withMultipleOrderBy_generatesSelectWithMultipleOrderBy() {
            selectBuilder
                .orderBy("status", OrderDirection.ASC)
                .orderBy("created_at", OrderDirection.DESC);
            
            String sql = selectBuilder.build();
            
            assertThat(sql).contains("ORDER BY");
            assertThat(sql).contains("status ASC");
            assertThat(sql).contains("created_at DESC");
        }
        
        @Test
        @DisplayName("デフォルトのORDER BY方向はASCであること")
        void build_withDefaultOrderDirection_usesAsc() {
            selectBuilder.orderBy("name");
            
            String sql = selectBuilder.build();
            
            assertThat(sql).contains("name ASC");
        }
        
        @Test
        @DisplayName("WHERE条件とORDER BYの組み合わせ")
        void build_withWhereAndOrderBy_generatesCombinedSql() {
            selectBuilder
                .where(w -> w.eq("status", "ACTIVE"))
                .orderBy("name");
            
            String sql = selectBuilder.build();
            
            assertThat(sql).contains("WHERE");
            assertThat(sql).contains("ORDER BY");
        }
    }
    
    @Nested
    @DisplayName("where() メソッドのテスト")
    class WhereTest {
        
        @Test
        @DisplayName("Consumer形式でwhere条件を設定できること")
        void where_withConsumer_setsCondition() {
            SelectBuilder<TestEntity> result = selectBuilder.where(w -> w.eq("id", 1L));
            
            assertThat(result).isSameAs(selectBuilder);
            assertThat(selectBuilder.build()).contains("WHERE");
        }
        
        @Test
        @DisplayName("Where オブジェクト形式でwhere条件を設定できること")
        void where_withWhereObject_setsCondition() {
            SimpleWhere simpleWhere = new SimpleWhere();
            simpleWhere.eq("status", "ACTIVE");
            
            SelectBuilder<TestEntity> result = selectBuilder.where(simpleWhere);
            
            assertThat(result).isSameAs(selectBuilder);
            assertThat(selectBuilder.build()).contains("WHERE");
        }
        
        @Test
        @DisplayName("複数条件を設定できること")
        void where_withMultipleConditions_setsAllConditions() {
            selectBuilder.where(w -> w.eq("name", "test").gt("age", 20));
            
            String sql = selectBuilder.build();
            
            assertThat(sql).contains("name = ");
            assertThat(sql).contains("age > ");
        }
    }
    
    @Nested
    @DisplayName("getParameters() メソッドのテスト")
    class GetParametersTest {
        
        @Test
        @DisplayName("条件なしでは空のパラメータマップを返すこと")
        void getParameters_withoutConditions_returnsEmptyMap() {
            selectBuilder.build();
            
            assertThat(selectBuilder.getParameters()).isEmpty();
        }
        
        @Test
        @DisplayName("WHERE条件のパラメータが含まれること")
        void getParameters_withWhere_containsParameters() {
            selectBuilder.where(w -> w.eq("name", "test"));
            selectBuilder.build();
            
            assertThat(selectBuilder.getParameters()).containsValue("test");
        }
    }
    
    @Nested
    @DisplayName("getResultList() メソッドのテスト")
    class GetResultListTest {
        
        @Test
        @DisplayName("クエリ結果のリストを取得できること")
        @SuppressWarnings("unchecked")
        void getResultList_returnsEntities() {
            TestEntity entity1 = new TestEntity();
            entity1.setId(1L);
            TestEntity entity2 = new TestEntity();
            entity2.setId(2L);
            List<TestEntity> expected = Arrays.asList(entity1, entity2);
            
            when(jdbcManager.selectBySql(anyString(), anyMap(), eq(TestEntity.class)))
                .thenReturn(selectQuery);
            when(selectQuery.getResultList()).thenReturn(expected);
            
            List<TestEntity> result = selectBuilder.getResultList();
            
            assertThat(result).hasSize(2);
            assertThat(result).containsExactlyElementsOf(expected);
        }
    }
    
    @Nested
    @DisplayName("getSingleResult() メソッドのテスト")
    class GetSingleResultTest {
        
        @Test
        @DisplayName("単一結果を取得できること")
        @SuppressWarnings("unchecked")
        void getSingleResult_returnsSingleEntity() {
            TestEntity entity = new TestEntity();
            entity.setId(1L);
            
            when(jdbcManager.selectBySql(anyString(), anyMap(), eq(TestEntity.class)))
                .thenReturn(selectQuery);
            when(selectQuery.getResultList()).thenReturn(Collections.singletonList(entity));
            
            TestEntity result = selectBuilder.getSingleResult();
            
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
        }
        
        @Test
        @DisplayName("結果が存在しない場合はnullを返すこと")
        @SuppressWarnings("unchecked")
        void getSingleResult_withNoResult_returnsNull() {
            when(jdbcManager.selectBySql(anyString(), anyMap(), eq(TestEntity.class)))
                .thenReturn(selectQuery);
            when(selectQuery.getResultList()).thenReturn(Collections.emptyList());
            
            TestEntity result = selectBuilder.getSingleResult();
            
            assertThat(result).isNull();
        }
        
        @Test
        @DisplayName("複数結果がある場合はNonUniqueResultExceptionをスローすること")
        @SuppressWarnings("unchecked")
        void getSingleResult_withMultipleResults_throwsException() {
            TestEntity entity1 = new TestEntity();
            TestEntity entity2 = new TestEntity();
            
            when(jdbcManager.selectBySql(anyString(), anyMap(), eq(TestEntity.class)))
                .thenReturn(selectQuery);
            when(selectQuery.getResultList()).thenReturn(Arrays.asList(entity1, entity2));
            
            assertThatThrownBy(() -> selectBuilder.getSingleResult())
                .isInstanceOf(NonUniqueResultException.class)
                .hasMessageContaining("2件");
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
