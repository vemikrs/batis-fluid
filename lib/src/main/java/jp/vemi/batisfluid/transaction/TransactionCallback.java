/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.transaction;

import jp.vemi.batisfluid.core.JdbcFlow;

/**
 * トランザクション内で実行する処理を定義するコールバックインターフェース。
 * <p>
 * トランザクション境界内で実行する処理をラムダ式や無名クラスとして定義できます。
 * </p>
 *
 * <pre>
 * 使用例:
 * transactionManager.execute(PropagationType.REQUIRED, (jdbcFlow) -&gt; {
 *     jdbcFlow.insert(entity);
 * });
 * </pre>
 *
 * @version 0.0.2
 * @author BatisFluid
 */
@FunctionalInterface
public interface TransactionCallback {

    /**
     * トランザクション内で実行する処理を定義します。
     *
     * @param jdbcFlow JdbcFlowインスタンス
     * @throws Exception 処理中に例外が発生した場合
     */
    void execute(JdbcFlow jdbcFlow) throws Exception;
}
