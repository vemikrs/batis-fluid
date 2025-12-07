/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.seasarbatis.exception;

/**
 * 楽観的排他制御に失敗した場合にスローされる例外です。
 * <p>
 * バージョン番号やタイムスタンプによる楽観的排他制御で、
 * 更新対象のレコードが他のトランザクションによって既に更新されていた場合にスローされます。
 * </p>
 * <p>
 * <strong>非推奨：</strong> このクラスはv0.0.2で非推奨となりました。
 * 代わりに{@link jp.vemi.batisfluid.exception.OptimisticLockException}を使用してください。
 * </p>
 * 
 * @author H.Kurosawa
 * @version 0.0.2
 * @since 2025/01/01
 * @deprecated v0.0.2以降は{@link jp.vemi.batisfluid.exception.OptimisticLockException}を使用してください。
 *             このクラスはv0.0.3以降で削除される予定です。
 */
@Deprecated(since = "0.0.2")
public class SBOptimisticLockException extends SBException {
    
    private final Object entity;
    private final String[] properties;

    /**
     * 楽観的排他制御の例外を生成します。
     * 
     * @param message エラーメッセージ
     * @param entity 対象エンティティ
     * @param properties 更新対象のプロパティ名
     */
    public SBOptimisticLockException(String message, Object entity, String... properties) {
        super(message);
        this.entity = entity;
        this.properties = properties;
    }

    /**
     * 対象のエンティティを取得します。
     * 
     * @return 対象エンティティ
     */
    public Object getEntity() {
        return entity;
    }

    /**
     * 更新対象のプロパティ名を取得します。
     * 
     * @return プロパティ名の配列
     */
    public String[] getProperties() {
        return properties;
    }
}