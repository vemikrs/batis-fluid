/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.exception;

/**
 * 楽観的排他制御に失敗した場合にスローされる例外です。
 * <p>
 * バージョン番号やタイムスタンプによる楽観的排他制御で、
 * 更新対象のレコードが他のトランザクションによって既に更新されていた場合にスローされます。
 * </p>
 * 
 * @author H.Kurosawa
 * @version 0.0.2
 * @since 0.0.2
 */
public class OptimisticLockException extends FluidException {
    
    private final Object entity;
    private final String[] properties;

    /**
     * 楽観的排他制御の例外を生成します。
     * 
     * @param message エラーメッセージ
     * @param entity 対象エンティティ
     * @param properties 更新対象のプロパティ名
     */
    public OptimisticLockException(String message, Object entity, String... properties) {
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
