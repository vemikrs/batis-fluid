/*
 * Copyright(c) 2025 VEMI, All rights reserved.
 */
package jp.vemi.seasarbatis.core.builder;

import java.util.function.Consumer;

import jp.vemi.seasarbatis.core.criteria.SBWhere;

/**
 * WHERE句を持つSQLビルダーの基底インターフェース
 * 
 * @param <T> 自身の型（流暢なAPIのため）
 * @deprecated このインターフェースは将来のバージョンで削除予定です。
 *             代わりに {@link jp.vemi.batisfluid.query.WhereCapable} を使用してください。
 */
@Deprecated(since = "0.0.2", forRemoval = true)
interface SBWhereCapable<T extends SBWhereCapable<T>> extends SBSqlBuilder<T> {
    T where(SBWhere where);
    T where(Consumer<SBWhere> whereConsumer);
}