/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * テーブルのメタデータを定義するアノテーションです。
 * <p>
 * このアノテーションを使用して、エンティティクラスと
 * データベースのテーブルとのマッピング情報を定義します。
 * </p>
 * 
 * @author H.Kurosawa
 * @version 0.0.2
 * @since 0.0.2
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FluidTable {
    /**
     * データベースのテーブル名を指定します。
     * 
     * @return テーブル名
     */
    String name();
    
    /**
     * データベースのスキーマ名を指定します。
     * 
     * @return スキーマ名（未指定の場合は空文字列）
     */
    String schema() default "";
}
