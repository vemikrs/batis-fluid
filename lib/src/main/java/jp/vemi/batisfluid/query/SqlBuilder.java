/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.query;

import java.util.Map;

/**
 * SQLビルダーの基底インターフェース。
 * <p>
 * 全てのSQLビルダーが実装すべき基本メソッドを定義します。
 * </p>
 *
 * @param <T> 自身の型（流暢なAPIのため）
 * @version 0.0.2
 * @author BatisFluid
 */
public interface SqlBuilder<T extends SqlBuilder<T>> {

    /**
     * SQLをビルドします。
     * 
     * @return 構築されたSQL文
     */
    String build();

    /**
     * パラメータを取得します。
     * 
     * @return バインドパラメータのマップ
     */
    Map<String, Object> getParameters();
}
