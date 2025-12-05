/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.exception;

/**
 * 不正な状態を表す例外クラスです。
 * <p>
 * メソッドの実行に必要な前提条件が満たされていない場合などにスローされます。
 * </p>
 * 
 * @author H.Kurosawa
 * @version 0.0.2
 * @since 0.0.2
 */
public class FluidIllegalStateException extends FluidException {
    
    /**
     * メッセージを指定して例外を生成します。
     * 
     * @param message エラーメッセージ
     */
    public FluidIllegalStateException(String message) {
        super(message);
    }

    /**
     * メッセージと元の例外を指定して例外を生成します。
     * 
     * @param message エラーメッセージ
     * @param cause 元の例外
     */
    public FluidIllegalStateException(String message, Throwable cause) {
        super(message, cause);
    }
}
