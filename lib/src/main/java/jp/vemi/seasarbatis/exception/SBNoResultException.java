/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.seasarbatis.exception;

import jp.vemi.seasarbatis.core.i18n.SBMessageManager;

/**
 * 検索結果が0件の場合にスローされる例外です。
 * <p>
 * 1件以上の結果が期待される検索で結果が0件だった場合にスローされます。
 * 国際化対応のメッセージを提供します。
 * </p>
 * <p>
 * <strong>非推奨：</strong> このクラスはv0.0.2で非推奨となりました。
 * 代わりに{@link jp.vemi.batisfluid.exception.NoResultException}を使用してください。
 * </p>
 * 
 * @author H.Kurosawa
 * @version 0.0.2
 * @since 2025/01/01
 * @deprecated v0.0.2以降は{@link jp.vemi.batisfluid.exception.NoResultException}を使用してください。
 *             このクラスはv0.0.3以降で削除される予定です。
 */
@Deprecated(since = "0.0.2")
public class SBNoResultException extends SBException {
    
    /**
     * デフォルトメッセージで例外を生成します。
     */
    public SBNoResultException() {
        super(SBMessageManager.getInstance().getMessage("error.no.result"));
    }
    
    /**
     * メッセージキーから国際化されたメッセージで例外を生成します。
     * 
     * @param messageKey メッセージキー
     */
    public SBNoResultException(String messageKey) {
        super(SBMessageManager.getInstance().getMessage(messageKey));
    }

    /**
     * メッセージキーと元の例外から国際化されたメッセージで例外を生成します。
     * 
     * @param messageKey メッセージキー
     * @param cause 元の例外
     */
    public SBNoResultException(String messageKey, Throwable cause) {
        super(SBMessageManager.getInstance().getMessage(messageKey), cause);
    }
}