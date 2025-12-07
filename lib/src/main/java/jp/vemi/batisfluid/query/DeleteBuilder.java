/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.query;

import static jp.vemi.batisfluid.entity.EntityOperations.getTableName;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import jp.vemi.seasarbatis.jdbc.SBJdbcManager;

/**
 * DELETE文を構築するビルダークラス。
 * <p>
 * Fluent interfaceパターンでDELETE文を組み立てます。
 * </p>
 *
 * <pre>
 * 使用例:
 * int deletedCount = deleteBuilder
 *     .where(w -&gt; w.eq("status", "DELETED"))
 *     .execute();
 * </pre>
 *
 * @param <E> エンティティの型
 * @version 0.0.2
 * @author BatisFluid
 */
public class DeleteBuilder<E> implements WhereCapable<DeleteBuilder<E>> {

    private final SBJdbcManager jdbcManager;
    private final Class<E> entityClass;
    private final Map<String, Object> parameters = new HashMap<>();
    private Where where;

    /**
     * コンストラクタ
     *
     * @param jdbcManager JDBCマネージャー
     * @param entityClass エンティティのクラス
     */
    public DeleteBuilder(SBJdbcManager jdbcManager, Class<E> entityClass) {
        this.jdbcManager = jdbcManager;
        this.entityClass = entityClass;
    }

    @Override
    public String build() {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ").append(getTableName(entityClass));

        if (where != null) {
            sql.append(" WHERE ").append(where.build());
            parameters.putAll(where.getParameters());
        }

        return sql.toString();
    }

    @Override
    public Map<String, Object> getParameters() {
        return parameters;
    }

    @Override
    public DeleteBuilder<E> where(Consumer<Where> consumer) {
        Where newWhere = new SimpleWhere();
        consumer.accept(newWhere);
        return where(newWhere);
    }

    @Override
    public DeleteBuilder<E> where(Where where) {
        this.where = where;
        return this;
    }

    /**
     * DELETE文を実行します。
     *
     * @return 削除された行数
     */
    public int execute() {
        return jdbcManager.delete(build(), getParameters());
    }
}
