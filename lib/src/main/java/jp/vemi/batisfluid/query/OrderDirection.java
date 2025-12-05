/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.query;

/**
 * ORDER BY句のソート方向を表す列挙型。
 * <p>
 * SQL文でのソート順序（昇順/降順）を定義します。
 * </p>
 *
 * @version 0.0.2
 * @author BatisFluid
 */
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
     * @return SQL文で使用する文字列（"ASC" または "DESC"）
     */
    public String toSql() {
        return sql;
    }
}
