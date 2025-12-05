/*
 * Copyright(c) 2025 VEMI, All rights reserved.
 */
package jp.vemi.seasarbatis.core.builder;

import java.util.Map;

/**
 * SQLビルダーの基底インターフェース
 * 
 * @param <T> 自身の型（流暢なAPIのため）
 * @deprecated このインターフェースは将来のバージョンで削除予定です。
 *             代わりに {@link jp.vemi.batisfluid.query.SqlBuilder} を使用してください。
 */
@Deprecated(since = "0.0.2", forRemoval = true)
interface SBSqlBuilder<T extends SBSqlBuilder<T>> {
    /**
     * SQLをビルドします。
     * @return SQL
     */
    String build();

    /**
     * パラメータを取得します。
     * @return パラメータ
     */
    Map<String, Object> getParameters();
}
