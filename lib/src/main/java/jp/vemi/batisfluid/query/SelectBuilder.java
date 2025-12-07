/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.query;

import static jp.vemi.batisfluid.entity.EntityOperations.getTableName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import jp.vemi.batisfluid.exception.NonUniqueResultException;
import jp.vemi.batisfluid.sql.SqlFormatter;
import jp.vemi.seasarbatis.jdbc.SBJdbcManager;

/**
 * SELECT文を構築するビルダークラス。
 * <p>
 * Fluent interfaceパターンでSELECT文を組み立てます。
 * </p>
 *
 * <pre>
 * 使用例:
 * List&lt;User&gt; users = selectBuilder
 *     .where(w -&gt; w.eq("status", "ACTIVE"))
 *     .orderBy("created_at", OrderDirection.DESC)
 *     .getResultList();
 * </pre>
 *
 * @param <E> エンティティの型
 * @version 0.0.2
 * @author BatisFluid
 */
public class SelectBuilder<E> implements WhereCapable<SelectBuilder<E>>, OrderByCapable<SelectBuilder<E>> {

    private final SBJdbcManager jdbcManager;
    private final Class<E> entityClass;
    private Where where;
    private final List<String> orderByList = new ArrayList<>();
    private final Map<String, Object> parameters = new HashMap<>();

    /**
     * コンストラクタ
     *
     * @param jdbcManager JDBCマネージャー
     * @param entityClass エンティティのクラス
     */
    public SelectBuilder(SBJdbcManager jdbcManager, Class<E> entityClass) {
        this.jdbcManager = jdbcManager;
        this.entityClass = entityClass;
    }

    @Override
    public String build() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ").append(getTableName(entityClass));

        if (where != null && !where.build().isEmpty()) {
            sql.append(where.build());
            parameters.putAll(where.getParameters());
        }

        if (!orderByList.isEmpty()) {
            sql.append(" ORDER BY ").append(String.join(", ", orderByList));
        }

        return SqlFormatter.simplify(sql.toString());
    }

    @Override
    public Map<String, Object> getParameters() {
        return parameters;
    }

    @Override
    public SelectBuilder<E> where(Consumer<Where> consumer) {
        Where newWhere = new SimpleWhere();
        consumer.accept(newWhere);
        return where(newWhere);
    }

    @Override
    public SelectBuilder<E> where(Where where) {
        this.where = where;
        return this;
    }

    @Override
    public SelectBuilder<E> orderBy(String column) {
        return orderBy(column, OrderDirection.ASC);
    }

    @Override
    public SelectBuilder<E> orderBy(String column, OrderDirection direction) {
        orderByList.add(column + " " + direction.name());
        return this;
    }

    /**
     * クエリを実行し、結果のリストを返します。
     *
     * @return エンティティのリスト
     */
    public List<E> getResultList() {
        return jdbcManager.selectBySql(build(), getParameters(), entityClass).getResultList();
    }

    /**
     * クエリを実行し、単一の結果を返します。
     *
     * @return エンティティ。結果が存在しない場合はnull
     * @throws NonUniqueResultException 複数の結果が存在する場合
     */
    public E getSingleResult() {
        List<E> results = getResultList();
        if (results.isEmpty()) {
            return null;
        }
        if (results.size() > 1) {
            throw new NonUniqueResultException("複数の結果が見つかりました。" + results.size() + "件");
        }
        return results.get(0);
    }
}
