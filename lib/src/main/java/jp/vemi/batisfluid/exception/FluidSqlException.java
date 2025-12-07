/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.exception;

/**
 * SQL実行時の例外クラスです。
 * <p>
 * SQL文の実行時に発生するデータベース関連の例外を表します。
 * </p>
 * 
 * @author H.Kurosawa
 * @version 0.0.2
 * @since 0.0.2
 */
public class FluidSqlException extends FluidException {
    
    /**
     * メッセージを指定して例外を生成します。
     * 
     * @param message エラーメッセージ
     */
    public FluidSqlException(String message) {
        super(message);
    }
    
    /**
     * メッセージと元の例外を指定して例外を生成します。
     * 
     * @param message エラーメッセージ
     * @param cause 元の例外
     */
    public FluidSqlException(String message, Throwable cause) {
        super(message, cause);
    }
}
