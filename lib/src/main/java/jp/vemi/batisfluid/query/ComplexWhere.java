/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.query;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 複雑なWHERE句を構築するクラス。
 * <p>
 * 複数のWhere条件をANDやORで結合し、複雑な条件式を構築できます。
 * </p>
 *
 * <pre>
 * 使用例:
 * ComplexWhere complexWhere = new ComplexWhere()
 *     .add(new SimpleWhere().eq("status", "ACTIVE"))
 *     .or(new SimpleWhere().eq("status", "PENDING"));
 * </pre>
 *
 * @version 0.0.2
 * @author BatisFluid
 * @see SimpleWhere
 */
public class ComplexWhere implements Where {

    /** 結合されたWhere条件のリスト */
    private final List<Where> wheres = new ArrayList<>();

    /** 各条件間の演算子（AND/OR） */
    private final List<String> operators = new ArrayList<>();

    /**
     * WHERE条件を追加します。
     * 
     * @param where 追加する条件
     * @return このインスタンス
     */
    public ComplexWhere add(Where where) {
        wheres.add(where);
        return this;
    }

    /**
     * WHERE条件をANDで追加します。
     * 
     * @param where 追加する条件
     * @return このインスタンス
     */
    @Override
    public ComplexWhere and(Where where) {
        if (!wheres.isEmpty()) {
            operators.add("AND");
        }
        wheres.add(where);
        return this;
    }

    /**
     * WHERE条件をORで追加します。
     * 
     * @param where 追加する条件
     * @return このインスタンス
     */
    @Override
    public ComplexWhere or(Where where) {
        if (!wheres.isEmpty()) {
            operators.add("OR");
        }
        wheres.add(where);
        return this;
    }

    @Override
    public String getWhereSql() {
        if (wheres.isEmpty()) {
            return "";
        }

        StringBuilder sql = new StringBuilder(" WHERE ");
        for (int i = 0; i < wheres.size(); i++) {
            if (i > 0 && i - 1 < operators.size()) {
                sql.append(" ").append(operators.get(i - 1)).append(" ");
            }
            String whereSql = wheres.get(i).getWhereSql();
            if (whereSql.startsWith(" WHERE ")) {
                whereSql = whereSql.substring(7);
            }
            sql.append("(").append(whereSql).append(")");
        }
        return sql.toString();
    }

    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> params = new LinkedHashMap<>();
        for (Where where : wheres) {
            params.putAll(where.getParameters());
        }
        return params;
    }

    @Override
    public boolean hasConditions() {
        return !wheres.isEmpty();
    }

    @Override
    public String build() {
        return getWhereSql();
    }

    // 以下のメソッドはComplexWhereでは使用しない（シンプルな条件追加は内部のSimpleWhereで行う）
    // ただしインターフェースの契約を満たすために実装

    @Override
    public Where eq(String column, Object value) {
        SimpleWhere where = new SimpleWhere();
        where.eq(column, value);
        return add(where);
    }

    @Override
    public Where ne(String column, Object value) {
        SimpleWhere where = new SimpleWhere();
        where.ne(column, value);
        return add(where);
    }

    @Override
    public Where gt(String column, Object value) {
        SimpleWhere where = new SimpleWhere();
        where.gt(column, value);
        return add(where);
    }

    @Override
    public Where ge(String column, Object value) {
        SimpleWhere where = new SimpleWhere();
        where.ge(column, value);
        return add(where);
    }

    @Override
    public Where lt(String column, Object value) {
        SimpleWhere where = new SimpleWhere();
        where.lt(column, value);
        return add(where);
    }

    @Override
    public Where le(String column, Object value) {
        SimpleWhere where = new SimpleWhere();
        where.le(column, value);
        return add(where);
    }

    @Override
    public Where like(String column, Object value) {
        SimpleWhere where = new SimpleWhere();
        where.like(column, value);
        return add(where);
    }

    @Override
    public Where isNull(String column) {
        SimpleWhere where = new SimpleWhere();
        where.isNull(column);
        return add(where);
    }

    @Override
    public Where isNotNull(String column) {
        SimpleWhere where = new SimpleWhere();
        where.isNotNull(column);
        return add(where);
    }

    @Override
    public Where in(String column, Object... values) {
        SimpleWhere where = new SimpleWhere();
        where.in(column, values);
        return add(where);
    }

    @Override
    public Where notIn(String column, Object... values) {
        SimpleWhere where = new SimpleWhere();
        where.notIn(column, values);
        return add(where);
    }

    @Override
    public Where between(String column, Object value1, Object value2) {
        SimpleWhere where = new SimpleWhere();
        where.between(column, value1, value2);
        return add(where);
    }

    @Override
    public Where notBetween(String column, Object value1, Object value2) {
        SimpleWhere where = new SimpleWhere();
        where.notBetween(column, value1, value2);
        return add(where);
    }

    @Override
    public Where and(String column, Object value) {
        SimpleWhere where = new SimpleWhere();
        where.eq(column, value);
        return and(where);
    }

    @Override
    public Where or(String column, Object value) {
        SimpleWhere where = new SimpleWhere();
        where.eq(column, value);
        return or(where);
    }

    @Override
    public Where and(String column, Object value, boolean isAdd) {
        if (!isAdd || value == null) {
            return this;
        }
        return and(column, value);
    }

    @Override
    public Where or(String column, Object value, boolean isAdd) {
        if (!isAdd || value == null) {
            return this;
        }
        return or(column, value);
    }
}
