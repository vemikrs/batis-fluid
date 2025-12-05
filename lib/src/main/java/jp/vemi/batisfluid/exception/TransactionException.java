/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.exception;

/**
 * トランザクション操作時の例外クラスです。
 * <p>
 * トランザクションの開始、コミット、ロールバック時に発生した例外を表します。
 * </p>
 * 
 * @author H.Kurosawa
 * @version 0.0.2
 * @since 0.0.2
 */
public class TransactionException extends FluidException {
    
    /**
     * メッセージを指定して例外を生成します。
     * 
     * @param message エラーメッセージ
     */
    public TransactionException(String message) {
        super(message);
    }
    
    /**
     * メッセージと元の例外を指定して例外を生成します。
     * 
     * @param message エラーメッセージ
     * @param cause 元の例外
     */
    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }
}
