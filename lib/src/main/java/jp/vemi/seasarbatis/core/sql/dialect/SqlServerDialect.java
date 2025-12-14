/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.seasarbatis.core.sql.dialect;

/**
 * Microsoft SQL Server 2022 向けの Dialect 実装。
 * <p>
 * SeasarBatis のSQL置換処理（プレースホルダの値をリテラルに変換）において、
 * SQL Server 固有のリテラル表現を提供します。
 * 特に文字列は Unicode リテラル（N'...'）として扱わないと、
 * 日本語などの非ASCII文字を含む LIKE 条件で一致しないことがあります。
 * </p>
 *
 * @author VEMI
 * @version 0.0.2
 * @since 2025/12/14
 */
public class SqlServerDialect implements SBDialect {

    @Override
    public String formatString(String value) {
        if (value == null) {
            return "NULL";
        }
        String escaped = value.replace("'", "''");
        return "N'" + escaped + "'";
    }

    @Override
    public String formatDate(String value) {
        if (value == null) {
            return "NULL";
        }
        // SQL Server は文字列から暗黙/明示変換できるため、DATETIME2 へ変換する。
        return "CONVERT(DATETIME2, '" + value + "', 120)";
    }

    @Override
    public String formatTimestamp(String value) {
        if (value == null) {
            return "NULL";
        }
        return "CONVERT(DATETIME2, '" + value + "', 120)";
    }

    @Override
    public String formatArray(String formattedElements) {
        if (formattedElements == null || formattedElements.isEmpty()) {
            return "(NULL)";
        }
        // IN (...) 想定のため、配列相当は括弧付きのリストとして返す。
        return "(" + formattedElements + ")";
    }

    @Override
    public String getDatabaseProductName() {
        return "Microsoft SQL Server";
    }
}
