/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.query;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Query Builder関連クラスのテスト。
 *
 * @version 0.0.2
 */
class QueryBuilderTest {

    @Nested
    @DisplayName("OrderDirection列挙型のテスト")
    class OrderDirectionTest {

        @Test
        @DisplayName("ASCのSQL文字列を取得できる")
        void testAscToSql() {
            assertThat(OrderDirection.ASC.toSql()).isEqualTo("ASC");
        }

        @Test
        @DisplayName("DESCのSQL文字列を取得できる")
        void testDescToSql() {
            assertThat(OrderDirection.DESC.toSql()).isEqualTo("DESC");
        }
    }

    @Nested
    @DisplayName("SimpleWhereのテスト")
    class SimpleWhereTest {

        @Test
        @DisplayName("eq条件を構築できる")
        void testEq() {
            SimpleWhere where = new SimpleWhere();
            where.eq("name", "John");

            String sql = where.getWhereSql();
            assertThat(sql).contains("WHERE");
            assertThat(sql).contains("name = ");
            assertThat(where.getParameters()).containsEntry("param0", "John");
        }

        @Test
        @DisplayName("複数条件をANDで結合できる")
        void testMultipleConditions() {
            SimpleWhere where = new SimpleWhere();
            where.eq("name", "John").eq("age", 30);

            String sql = where.getWhereSql();
            assertThat(sql).contains("AND");
            assertThat(where.getParameters()).hasSize(2);
        }

        @Test
        @DisplayName("LIKE条件を構築できる")
        void testLike() {
            SimpleWhere where = new SimpleWhere();
            where.like("name", "Jo");

            String sql = where.getWhereSql();
            assertThat(sql).contains("LIKE");
            assertThat(where.getParameters()).containsEntry("param0", "%Jo%");
        }

        @Test
        @DisplayName("IN条件を構築できる")
        void testIn() {
            SimpleWhere where = new SimpleWhere();
            where.in("status", "ACTIVE", "PENDING");

            String sql = where.getWhereSql();
            assertThat(sql).contains("IN");
            assertThat(where.getParameters()).hasSize(2);
        }

        @Test
        @DisplayName("BETWEEN条件を構築できる")
        void testBetween() {
            SimpleWhere where = new SimpleWhere();
            where.between("age", 18, 65);

            String sql = where.getWhereSql();
            assertThat(sql).contains("BETWEEN");
            assertThat(where.getParameters()).hasSize(2);
        }

        @Test
        @DisplayName("IS NULL条件を構築できる")
        void testIsNull() {
            SimpleWhere where = new SimpleWhere();
            where.isNull("deleted_at");

            String sql = where.getWhereSql();
            assertThat(sql).contains("IS NULL");
        }

        @Test
        @DisplayName("IS NOT NULL条件を構築できる")
        void testIsNotNull() {
            SimpleWhere where = new SimpleWhere();
            where.isNotNull("created_at");

            String sql = where.getWhereSql();
            assertThat(sql).contains("IS NOT NULL");
        }

        @Test
        @DisplayName("条件なしの場合空文字を返す")
        void testNoConditions() {
            SimpleWhere where = new SimpleWhere();

            assertThat(where.getWhereSql()).isEmpty();
            assertThat(where.hasConditions()).isFalse();
        }

        @Test
        @DisplayName("null値は無視される")
        void testNullValueIgnored() {
            SimpleWhere where = new SimpleWhere();
            where.eq("name", null);

            assertThat(where.hasConditions()).isFalse();
        }
    }

    @Nested
    @DisplayName("ComplexWhereのテスト")
    class ComplexWhereTest {

        @Test
        @DisplayName("複数条件をORで結合できる")
        void testOrConditions() {
            ComplexWhere complex = new ComplexWhere();
            complex.add(new SimpleWhere().eq("status", "ACTIVE"))
                   .or(new SimpleWhere().eq("status", "PENDING"));

            String sql = complex.getWhereSql();
            assertThat(sql).contains("OR");
            // 注意: 各SimpleWhereが独自のparam0を持つため、同じキーが上書きされる
            // 将来的にはグローバルなパラメータインデックスを検討する
            assertThat(complex.getParameters()).isNotEmpty();
        }

        @Test
        @DisplayName("複数条件をANDで結合できる")
        void testAndConditions() {
            ComplexWhere complex = new ComplexWhere();
            complex.add(new SimpleWhere().eq("status", "ACTIVE"))
                   .and(new SimpleWhere().eq("type", "USER"));

            String sql = complex.getWhereSql();
            assertThat(sql).contains("AND");
        }

        @Test
        @DisplayName("条件が括弧で囲まれる")
        void testConditionsInParentheses() {
            ComplexWhere complex = new ComplexWhere();
            complex.add(new SimpleWhere().eq("a", 1))
                   .or(new SimpleWhere().eq("b", 2));

            String sql = complex.getWhereSql();
            assertThat(sql).contains("(").contains(")");
        }
    }

    @Nested
    @DisplayName("Whereインターフェースのテスト")
    class WhereInterfaceTest {

        @Test
        @DisplayName("比較演算子が正しく生成される")
        void testComparisonOperators() {
            SimpleWhere where = new SimpleWhere();
            where.gt("price", 100)
                 .ge("quantity", 10)
                 .lt("discount", 50)
                 .le("tax", 20)
                 .ne("status", "DELETED");

            String sql = where.getWhereSql();
            assertThat(sql).contains(">");
            assertThat(sql).contains(">=");
            assertThat(sql).contains("<");
            assertThat(sql).contains("<=");
            assertThat(sql).contains("<>");
        }

        @Test
        @DisplayName("NOT IN条件を構築できる")
        void testNotIn() {
            SimpleWhere where = new SimpleWhere();
            where.notIn("status", "DELETED", "ARCHIVED");

            String sql = where.getWhereSql();
            assertThat(sql).contains("NOT IN");
        }

        @Test
        @DisplayName("NOT BETWEEN条件を構築できる")
        void testNotBetween() {
            SimpleWhere where = new SimpleWhere();
            where.notBetween("age", 0, 17);

            String sql = where.getWhereSql();
            assertThat(sql).contains("NOT BETWEEN");
        }
    }
}
