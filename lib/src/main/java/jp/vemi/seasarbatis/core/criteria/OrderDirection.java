/*
 * Copyright(c) 2025 VEMI, All rights reserved.
 */
package jp.vemi.seasarbatis.core.criteria;

/**
 * ORDER BY句のソート方向を表す列挙型。
 * 
 * @deprecated この列挙型は将来のバージョンで削除予定です。
 *             代わりに {@link jp.vemi.batisfluid.query.OrderDirection} を使用してください。
 */
@Deprecated(since = "0.0.2", forRemoval = true)
public enum OrderDirection {
    /** 昇順 */
    ASC("ASC"),
    /** 降順 */
    DESC("DESC");

    private final String sql;

    OrderDirection(String sql) {
        this.sql = sql;
    }

    /**
     * SQL文で使用する文字列表現を取得します。
     * 
     * @return SQL文で使用する文字列
     */
    public String toSql() {
        return sql;
    }
}