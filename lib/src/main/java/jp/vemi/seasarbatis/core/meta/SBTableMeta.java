/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.seasarbatis.core.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * テーブルのメタデータを定義するアノテーションです。
 * <p>
 * <strong>非推奨：</strong> このアノテーションはv0.0.2で非推奨となりました。
 * 代わりに{@link jp.vemi.batisfluid.meta.FluidTable}を使用してください。
 * </p>
 * 
 * @author H.Kurosawa
 * @version 0.0.2
 * @since 2025/01/01
 * @deprecated v0.0.2以降は{@link jp.vemi.batisfluid.meta.FluidTable}を使用してください。
 *             このアノテーションはv0.0.3以降で削除される予定です。
 */
@Deprecated(since = "0.0.2")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SBTableMeta {
    String name();
    String schema() default "";
}