/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.seasarbatis.exception;

/**
 * SQL解析時の例外クラスです。
 * <p>
 * SQLの構文解析やパラメータ解決時に発生する例外を表します。
 * 主にSQLコメントの形式不正や条件式の構文エラーなどで発生します。
 * </p>
 * <p>
 * <strong>非推奨：</strong> このクラスはv0.0.2で非推奨となりました。
 * 代わりに{@link jp.vemi.batisfluid.exception.SqlParseException}を使用してください。
 * </p>
 * 
 * @author H.Kurosawa
 * @version 0.0.2
 * @since 2025/01/01
 * @deprecated v0.0.2以降は{@link jp.vemi.batisfluid.exception.SqlParseException}を使用してください。
 *             このクラスはv0.0.3以降で削除される予定です。
 */
@Deprecated(since = "0.0.2")
public class SBSqlParseException extends SBException {
    public SBSqlParseException(String message) {
        super(message);
    }

    public SBSqlParseException(String message, Throwable cause) {
        super(message, cause);
    }
}