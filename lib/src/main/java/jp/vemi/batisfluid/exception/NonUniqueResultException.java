/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.exception;

/**
 * 検索結果が複数件の場合にスローされる例外です。
 * <p>
 * 1件のみの結果が期待される検索で複数件の結果が返された場合にスローされます。
 * </p>
 * 
 * @author H.Kurosawa
 * @version 0.0.2
 * @since 0.0.2
 */
public class NonUniqueResultException extends FluidException {
    
    /**
     * デフォルトメッセージで例外を生成します。
     */
    public NonUniqueResultException() {
        super("一意でない結果です");
    }
    
    /**
     * メッセージを指定して例外を生成します。
     * 
     * @param message エラーメッセージ
     */
    public NonUniqueResultException(String message) {
        super(message);
    }

    /**
     * メッセージと元の例外を指定して例外を生成します。
     * 
     * @param message エラーメッセージ
     * @param cause 元の例外
     */
    public NonUniqueResultException(String message, Throwable cause) {
        super(message, cause);
    }
}
