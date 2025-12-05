/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.seasarbatis.core.transaction;

import jp.vemi.seasarbatis.jdbc.SBJdbcManager;

/**
 * トランザクション内で実行する処理を定義するコールバックインターフェースです。
 * <p>
 * トランザクション境界内で実行する処理をラムダ式や無名クラスとして定義することができます。
 * </p>
 *
 * @author H.Kurosawa
 * @version 1.0.0
 * @since 2025/01/01
 * @deprecated このインターフェースは将来のバージョンで削除予定です。
 *             代わりに {@link jp.vemi.batisfluid.transaction.TransactionCallback} を使用してください。
 */
@Deprecated(since = "0.0.2", forRemoval = true)
@FunctionalInterface
public interface SBTransactionCallback {

    /**
     * トランザクション内で実行する処理を定義します。
     *
     * @param manager JDBCマネージャー
     * @throws Exception 処理中に例外が発生した場合
     */
    void execute(SBJdbcManager manager) throws Exception;
}