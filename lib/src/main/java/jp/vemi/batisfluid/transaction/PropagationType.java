/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.transaction;

/**
 * トランザクション伝播タイプを表す列挙型。
 * <p>
 * トランザクションの振る舞いを制御するための伝播設定を定義します。
 * </p>
 *
 * @version 0.0.2
 * @author BatisFluid
 */
public enum PropagationType {
    /**
     * 既存のトランザクションを使用。存在しなければ新規作成。
     */
    REQUIRED,

    /**
     * 常に新規トランザクションを作成。既存のトランザクションは一時停止。
     */
    REQUIRES_NEW,

    /**
     * ネストされたトランザクションを作成。セーブポイントを使用。
     */
    NESTED
}
