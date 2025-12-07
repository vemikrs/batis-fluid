/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.query;

import java.util.function.Consumer;

/**
 * WHERE句を持つSQLビルダーのインターフェース。
 * <p>
 * SELECT, UPDATE, DELETE等のWHERE句をサポートするビルダーが実装します。
 * </p>
 *
 * @param <T> 自身の型（流暢なAPIのため）
 * @version 0.0.2
 * @author BatisFluid
 */
public interface WhereCapable<T extends WhereCapable<T>> extends SqlBuilder<T> {

    /**
     * WHERE条件を設定します。
     * 
     * @param where WHERE条件
     * @return このインスタンス
     */
    T where(Where where);

    /**
     * WHERE条件をコンシューマで設定します。
     * 
     * @param whereConsumer WHERE条件を構築するコンシューマ
     * @return このインスタンス
     */
    T where(Consumer<Where> whereConsumer);
}
