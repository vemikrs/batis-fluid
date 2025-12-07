/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.exception;

/**
 * エンティティ操作に関する例外クラスです。
 * <p>
 * エンティティのメタデータ取得や値の設定時に発生する例外を表します。
 * </p>
 * 
 * @author H.Kurosawa
 * @version 0.0.2
 * @since 0.0.2
 */
public class EntityException extends FluidException {
    
    /**
     * メッセージを指定して例外を生成します。
     * 
     * @param message エラーメッセージ
     */
    public EntityException(String message) {
        super(message);
    }
    
    /**
     * メッセージと元の例外を指定して例外を生成します。
     * 
     * @param message エラーメッセージ
     * @param cause 元の例外
     */
    public EntityException(String message, Throwable cause) {
        super(message, cause);
    }
}
