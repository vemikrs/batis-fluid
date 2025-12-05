/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.exception;

/**
 * BatisFluidの基底例外クラスです。
 * <p>
 * BatisFluidで発生する全ての例外はこのクラスを継承します。
 * </p>
 * 
 * @author H.Kurosawa
 * @version 0.0.2
 * @since 0.0.2
 */
public class FluidException extends RuntimeException {

    /**
     * 例外を生成します。
     * 
     * @param message エラーメッセージ
     */
    public FluidException(String message) {
        super(message);
    }

    /**
     * 元の例外を保持した例外を生成します。
     * 
     * @param message エラーメッセージ
     * @param cause 元の例外
     */
    public FluidException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 元の例外を保持した例外を生成します。
     * 
     * @param cause 元の例外
     */
    public FluidException(Throwable cause) {
        super(cause);
    }
}
