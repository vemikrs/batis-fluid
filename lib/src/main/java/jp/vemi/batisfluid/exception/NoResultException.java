/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.exception;

/**
 * 検索結果が0件の場合にスローされる例外です。
 * <p>
 * 1件以上の結果が期待される検索で結果が0件だった場合にスローされます。
 * </p>
 * 
 * @author H.Kurosawa
 * @version 0.0.2
 * @since 0.0.2
 */
public class NoResultException extends FluidException {
    
    /**
     * デフォルトメッセージで例外を生成します。
     */
    public NoResultException() {
        super("結果が見つかりません");
    }
    
    /**
     * メッセージを指定して例外を生成します。
     * 
     * @param message エラーメッセージ
     */
    public NoResultException(String message) {
        super(message);
    }

    /**
     * メッセージと元の例外を指定して例外を生成します。
     * 
     * @param message エラーメッセージ
     * @param cause 元の例外
     */
    public NoResultException(String message, Throwable cause) {
        super(message, cause);
    }
}
