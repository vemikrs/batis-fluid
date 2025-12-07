/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.seasarbatis.exception;

/**
 * 型変換時の例外クラスです。
 * <p>
 * エンティティへのマッピング時や値の型変換時に発生するエラーを表します。
 * 主に数値や日付などの型変換失敗時にスローされます。
 * </p>
 * <p>
 * <strong>非推奨：</strong> このクラスはv0.0.2で非推奨となりました。
 * 代わりに{@link jp.vemi.batisfluid.exception.TypeConversionException}を使用してください。
 * </p>
 *
 * @author H.Kurosawa
 * @version 0.0.2
 * @since 2025/01/01
 * @deprecated v0.0.2以降は{@link jp.vemi.batisfluid.exception.TypeConversionException}を使用してください。
 *             このクラスはv0.0.3以降で削除される予定です。
 */
@Deprecated(since = "0.0.2")
public class SBTypeConversionException extends SBException {
    
    public SBTypeConversionException(String message) {
        super(message);
    }

    public SBTypeConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}