/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.query;

/**
 * ORDER BY句を持つSQLビルダーのインターフェース。
 * <p>
 * SELECT文等のORDER BY句をサポートするビルダーが実装します。
 * </p>
 *
 * @param <T> 自身の型（流暢なAPIのため）
 * @version 0.0.2
 * @author BatisFluid
 */
public interface OrderByCapable<T extends OrderByCapable<T>> extends SqlBuilder<T> {

    /**
     * ORDER BY句を追加します（昇順）。
     * 
     * @param column カラム名
     * @return このインスタンス
     */
    T orderBy(String column);

    /**
     * ORDER BY句を追加します。
     * 
     * @param column    カラム名
     * @param direction ソート方向
     * @return このインスタンス
     */
    T orderBy(String column, OrderDirection direction);
}
