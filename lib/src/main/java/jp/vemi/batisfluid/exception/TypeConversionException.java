/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.exception;

/**
 * 型変換時の例外クラスです。
 * <p>
 * エンティティへのマッピング時や値の型変換時に発生するエラーを表します。
 * 主に数値や日付などの型変換失敗時にスローされます。
 * </p>
 *
 * @author H.Kurosawa
 * @version 0.0.2
 * @since 0.0.2
 */
public class TypeConversionException extends FluidException {
    
    /**
     * メッセージを指定して例外を生成します。
     * 
     * @param message エラーメッセージ
     */
    public TypeConversionException(String message) {
        super(message);
    }

    /**
     * メッセージと元の例外を指定して例外を生成します。
     * 
     * @param message エラーメッセージ
     * @param cause 元の例外
     */
    public TypeConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
