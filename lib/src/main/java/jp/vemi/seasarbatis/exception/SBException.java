/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.seasarbatis.exception;

/**
 * SeasarBatisの基底例外クラスです。
 * <p>
 * SeasarBatisで発生する全ての例外はこのクラスを継承します。
 * </p>
 * <p>
 * <strong>非推奨：</strong> このクラスはv0.0.2で非推奨となりました。
 * 代わりに{@link jp.vemi.batisfluid.exception.FluidException}を使用してください。
 * </p>
 * 
 * @author H.Kurosawa
 * @version 0.0.2
 * @since 2025/01/01
 * @deprecated v0.0.2以降は{@link jp.vemi.batisfluid.exception.FluidException}を使用してください。
 *             このクラスはv0.0.3以降で削除される予定です。
 */
@Deprecated(since = "0.0.2")
public class SBException extends RuntimeException {

    /**
     * 例外を生成します。
     * 
     * @param message エラーメッセージ
     */
    public SBException(String message) {
        super(message);
    }

    /**
     * 元の例外を保持した例外を生成します。
     * 
     * @param message エラーメッセージ
     * @param cause 元の例外
     */
    public SBException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 元の例外を保持した例外を生成します。
     * 
     * @param cause 元の例外
     */
    public SBException(Throwable cause) {
        super(cause);
    }
}