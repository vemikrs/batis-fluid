/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.exception;

/**
 * SQL解析時の例外クラスです。
 * <p>
 * SQLの構文解析やパラメータ解決時に発生する例外を表します。
 * 主にSQLコメントの形式不正や条件式の構文エラーなどで発生します。
 * </p>
 * 
 * @author H.Kurosawa
 * @version 0.0.2
 * @since 0.0.2
 */
public class SqlParseException extends FluidException {
    
    /**
     * メッセージを指定して例外を生成します。
     * 
     * @param message エラーメッセージ
     */
    public SqlParseException(String message) {
        super(message);
    }

    /**
     * メッセージと元の例外を指定して例外を生成します。
     * 
     * @param message エラーメッセージ
     * @param cause 元の例外
     */
    public SqlParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
