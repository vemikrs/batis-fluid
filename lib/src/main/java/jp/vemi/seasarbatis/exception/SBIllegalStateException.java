/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.seasarbatis.exception;

/**
 * 不正な状態を表す例外クラスです。
 * <p>
 * メソッドの実行に必要な前提条件が満たされていない場合などにスローされます。
 * </p>
 * <p>
 * <strong>非推奨：</strong> このクラスはv0.0.2で非推奨となりました。
 * 代わりに{@link jp.vemi.batisfluid.exception.FluidIllegalStateException}を使用してください。
 * </p>
 * 
 * @author H.Kurosawa
 * @version 0.0.2
 * @since 2025/01/01
 * @deprecated v0.0.2以降は{@link jp.vemi.batisfluid.exception.FluidIllegalStateException}を使用してください。
 *             このクラスはv0.0.3以降で削除される予定です。
 */
@Deprecated(since = "0.0.2")
public class SBIllegalStateException extends SBException {
    public SBIllegalStateException(String message) {
        super(message);
    }

    public SBIllegalStateException(String message, Throwable cause) {
        super(message, cause);
    }
}