/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.sql;

/**
 * SQL文を整形するユーティリティクラスです。
 * <p>
 * 生成されたSQLを見やすく整形するための機能を提供します。
 * </p>
 *
 * @author H.Kurosawa
 * @version 0.0.2
 */
public class SqlFormatter {

    /**
     * SQL文をシンプルに整形します。
     * <ul>
     * <li>複数のホワイトスペースを1つに統一</li>
     * <li>複数行の改行を1行に統一</li>
     * <li>ホワイトスペースのみの行を削除</li>
     * </ul>
     *
     * @param sql 整形前のSQL文
     * @return 整形後のSQL文
     */
    public static String simplify(String sql) {
        if (sql == null || sql.isEmpty()) {
            return sql;
        }

        return sql
                // 複数行の改行を1行に
                .replaceAll("\\n{2,}", "\n")
                // ホワイトスペースのみの行を削除
                .replaceAll("(?m)^[ \t]*\r?\n", "")
                // 複数のホワイトスペースを1つに
                .replaceAll("\\s+", " ")
                .trim();
    }
}
