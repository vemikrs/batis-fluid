/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.transaction;

import jp.vemi.batisfluid.i18n.Messages;

/**
 * トランザクション操作のコンテキストを管理するクラス。
 * <p>
 * ThreadLocalを使用して、現在のスレッドのトランザクション操作を追跡します。
 * 独立トランザクションが実行される際に、一時的に異なるトランザクション操作を
 * 使用できるようにします。
 * </p>
 *
 * @version 0.0.2
 * @author BatisFluid
 */
public class TransactionContext {
    
    private static final ThreadLocal<TransactionOperation> currentOperation = new ThreadLocal<>();
    
    /**
     * プライベートコンストラクタ。
     */
    private TransactionContext() {
        // ユーティリティクラス
    }
    
    /**
     * 現在のトランザクション操作を設定します。
     * 
     * @param operation トランザクション操作
     */
    public static void setCurrentOperation(TransactionOperation operation) {
        currentOperation.set(operation);
    }
    
    /**
     * 現在のトランザクション操作を取得します。
     * 
     * @return 現在のトランザクション操作、設定されていない場合はnull
     */
    public static TransactionOperation getCurrentOperation() {
        return currentOperation.get();
    }
    
    /**
     * 現在のトランザクション操作をクリアします。
     */
    public static void clearCurrentOperation() {
        currentOperation.remove();
    }
    
    /**
     * 指定されたトランザクション操作を一時的に設定して処理を実行します。
     * 
     * @param <T> 戻り値の型
     * @param operation 使用するトランザクション操作
     * @param action 実行する処理
     * @return 処理の結果
     */
    public static <T> T withOperation(TransactionOperation operation, java.util.concurrent.Callable<T> action) {
        TransactionOperation previousOperation = getCurrentOperation();
        try {
            setCurrentOperation(operation);
            return action.call();
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new RuntimeException(Messages.getInstance().getMessage("transaction.error.processing"), e);
        } finally {
            if (previousOperation != null) {
                setCurrentOperation(previousOperation);
            } else {
                clearCurrentOperation();
            }
        }
    }
}
