/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.query;

import java.util.Map;

/**
 * SQL WHERE句を構築するためのインターフェース。
 * <p>
 * 条件式の生成と結合をサポートし、流暢なAPIでクエリ条件を構築できます。
 * </p>
 *
 * @version 0.0.2
 * @author BatisFluid
 */
public interface Where {

    /**
     * WHERE句のSQL文を構築します。
     * 
     * @return 構築されたWHERE句のSQL文
     */
    String build();

    /**
     * 等価条件（=）を追加します。
     * 
     * @param column カラム名
     * @param value  比較値
     * @return このインスタンス
     */
    Where eq(String column, Object value);

    /**
     * 不等価条件（&lt;&gt;）を追加します。
     * 
     * @param column カラム名
     * @param value  比較値
     * @return このインスタンス
     */
    Where ne(String column, Object value);

    /**
     * より大きい条件（&gt;）を追加します。
     * 
     * @param column カラム名
     * @param value  比較値
     * @return このインスタンス
     */
    Where gt(String column, Object value);

    /**
     * 以上条件（&gt;=）を追加します。
     * 
     * @param column カラム名
     * @param value  比較値
     * @return このインスタンス
     */
    Where ge(String column, Object value);

    /**
     * より小さい条件（&lt;）を追加します。
     * 
     * @param column カラム名
     * @param value  比較値
     * @return このインスタンス
     */
    Where lt(String column, Object value);

    /**
     * 以下条件（&lt;=）を追加します。
     * 
     * @param column カラム名
     * @param value  比較値
     * @return このインスタンス
     */
    Where le(String column, Object value);

    /**
     * LIKE条件を追加します。
     * 
     * @param column カラム名
     * @param value  検索パターン
     * @return このインスタンス
     */
    Where like(String column, Object value);

    /**
     * IS NULL条件を追加します。
     * 
     * @param column カラム名
     * @return このインスタンス
     */
    Where isNull(String column);

    /**
     * IS NOT NULL条件を追加します。
     * 
     * @param column カラム名
     * @return このインスタンス
     */
    Where isNotNull(String column);

    /**
     * IN条件を追加します。
     * 
     * @param column カラム名
     * @param values 比較値の配列
     * @return このインスタンス
     */
    Where in(String column, Object... values);

    /**
     * NOT IN条件を追加します。
     * 
     * @param column カラム名
     * @param values 比較値の配列
     * @return このインスタンス
     */
    Where notIn(String column, Object... values);

    /**
     * BETWEEN条件を追加します。
     * 
     * @param column カラム名
     * @param value1 開始値
     * @param value2 終了値
     * @return このインスタンス
     */
    Where between(String column, Object value1, Object value2);

    /**
     * NOT BETWEEN条件を追加します。
     * 
     * @param column カラム名
     * @param value1 開始値
     * @param value2 終了値
     * @return このインスタンス
     */
    Where notBetween(String column, Object value1, Object value2);

    /**
     * 別のWhere条件をANDで結合します。
     * 
     * @param where 結合する条件
     * @return このインスタンス
     */
    Where and(Where where);

    /**
     * 別のWhere条件をORで結合します。
     * 
     * @param where 結合する条件
     * @return このインスタンス
     */
    Where or(Where where);

    /**
     * 等価条件をANDで追加します。
     * 
     * @param column カラム名
     * @param value  比較値
     * @return このインスタンス
     */
    Where and(String column, Object value);

    /**
     * 等価条件をORで追加します。
     * 
     * @param column カラム名
     * @param value  比較値
     * @return このインスタンス
     */
    Where or(String column, Object value);

    /**
     * 等価条件をANDで追加します。
     * 
     * @param column カラム名
     * @param value  比較値
     * @param isAdd  条件を追加するかどうか
     * @return このインスタンス
     */
    Where and(String column, Object value, boolean isAdd);

    /**
     * 等価条件をORで追加します。
     * 
     * @param column カラム名
     * @param value  比較値
     * @param isAdd  条件を追加するかどうか
     * @return このインスタンス
     */
    Where or(String column, Object value, boolean isAdd);

    /**
     * 構築されたWHERE句のSQL文を取得します。
     * 
     * @return WHERE句のSQL文
     */
    String getWhereSql();

    /**
     * バインドパラメータを取得します。
     * 
     * @return パラメータのマップ
     */
    Map<String, Object> getParameters();

    /**
     * 条件が存在するかを確認します。
     * 
     * @return 条件が1つ以上存在する場合はtrue
     */
    boolean hasConditions();
}
