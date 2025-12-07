/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.query;

import static jp.vemi.batisfluid.entity.EntityOperations.getTableName;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import jp.vemi.seasarbatis.jdbc.SBJdbcManager;

/**
 * UPDATE文を構築するビルダークラス。
 * <p>
 * Fluent interfaceパターンでUPDATE文を組み立てます。
 * </p>
 *
 * <pre>
 * 使用例:
 * int updatedCount = updateBuilder
 *     .set("status", "INACTIVE")
 *     .set("updated_at", LocalDateTime.now())
 *     .where(w -&gt; w.eq("id", userId))
 *     .execute();
 * </pre>
 *
 * @param <E> エンティティの型
 * @version 0.0.2
 * @author BatisFluid
 */
public class UpdateBuilder<E> implements WhereCapable<UpdateBuilder<E>> {

    private final SBJdbcManager jdbcManager;
    private final Class<E> entityClass;
    private final Map<String, Object> setValues = new LinkedHashMap<>();
    private final Map<String, Object> parameters = new HashMap<>();
    private Where where;

    /**
     * コンストラクタ
     *
     * @param jdbcManager JDBCマネージャー
     * @param entityClass エンティティのクラス
     */
    public UpdateBuilder(SBJdbcManager jdbcManager, Class<E> entityClass) {
        this.jdbcManager = jdbcManager;
        this.entityClass = entityClass;
    }

    /**
     * 更新するカラムと値を設定します。
     *
     * @param column カラム名
     * @param value  設定値
     * @return このビルダーインスタンス
     */
    public UpdateBuilder<E> set(String column, Object value) {
        setValues.put(column, value);
        return this;
    }

    @Override
    public String build() {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ")
                .append(getTableName(entityClass))
                .append(" SET ");

        // SET句の構築
        setValues.forEach((column, value) -> {
            sql.append(column)
                    .append(" = /*")
                    .append(column)
                    .append("*/?, ");
            parameters.put(column, value);
        });

        // 最後のカンマを削除
        sql.setLength(sql.length() - 2);

        // WHERE句の追加
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
    public UpdateBuilder<E> where(Consumer<Where> consumer) {
        Where newWhere = new SimpleWhere();
        consumer.accept(newWhere);
        return where(newWhere);
    }

    @Override
    public UpdateBuilder<E> where(Where where) {
        this.where = where;
        return this;
    }

    /**
     * UPDATE文を実行します。
     *
     * @return 更新された行数
     */
    public int execute() {
        return jdbcManager.update(build(), getParameters());
    }
}
