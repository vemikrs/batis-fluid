/*
 * Copyright(c) 2025 VEMI, All rights reserved.
 */
package jp.vemi.seasarbatis.core.builder;

import jp.vemi.seasarbatis.core.criteria.OrderDirection;

/**
 * ORDER BY句を持つSQLビルダーの基底インターフェース
 * 
 * @param <T> 自身の型（流暢なAPIのため）
 * @deprecated このインターフェースは将来のバージョンで削除予定です。
 *             代わりに {@link jp.vemi.batisfluid.query.OrderByCapable} を使用してください。
 */
@Deprecated(since = "0.0.2", forRemoval = true)
interface SBOrderByCapable<T extends SBOrderByCapable<T>> extends SBSqlBuilder<T> {
    T orderBy(String column);
    T orderBy(String column, OrderDirection direction);
}