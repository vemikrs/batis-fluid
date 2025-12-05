/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.entity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.vemi.batisfluid.meta.FluidTable;
import jp.vemi.batisfluid.meta.FluidColumn;
import jp.vemi.batisfluid.exception.FluidException;
import jp.vemi.batisfluid.exception.FluidIllegalStateException;

/**
 * エンティティ操作に関する共通機能を提供するユーティリティクラスです。
 * <p>
 * テーブル名の解決や主キー情報の取得など、エンティティに関連する
 * 操作の共通実装を提供します。
 * </p>
 * 
 * @author H.Kurosawa
 * @version 0.0.2
 */
public class EntityOperations {
    
    private static final Logger logger = LoggerFactory.getLogger(EntityOperations.class);

    /**
     * エンティティクラスからテーブル名を取得します。
     * <p>
     * 新しい {@link FluidTable} アノテーションを優先的に使用し、
     * 存在しない場合は旧 {@link jp.vemi.seasarbatis.core.meta.SBTableMeta} を確認します。
     * </p>
     *
     * @param <T> エンティティの型
     * @param entityClass エンティティクラス
     * @return テーブル名
     */
    public static <T> String getTableName(Class<T> entityClass) {
        // 新アノテーションを優先
        FluidTable fluidTable = entityClass.getAnnotation(FluidTable.class);
        if (fluidTable != null) {
            String schema = fluidTable.schema();
            String tableName = fluidTable.name();
            return schema.isEmpty() ? tableName : schema + "." + tableName;
        }
        
        // 旧アノテーションにフォールバック
        jp.vemi.seasarbatis.core.meta.SBTableMeta tableMeta = 
            entityClass.getAnnotation(jp.vemi.seasarbatis.core.meta.SBTableMeta.class);
        if (tableMeta != null) {
            String schema = tableMeta.schema();
            String tableName = tableMeta.name();
            return schema.isEmpty() ? tableName : schema + "." + tableName;
        }
        
        logger.warn("テーブルアノテーションが見つかりません: {}", entityClass.getName());
        return entityClass.getSimpleName().toLowerCase();
    }

    /**
     * エンティティからパラメータマップを取得します。
     * <p>
     * 新しい {@link FluidColumn} アノテーションを優先的に使用し、
     * 存在しない場合は旧 {@link jp.vemi.seasarbatis.core.meta.SBColumnMeta} を確認します。
     * </p>
     *
     * @param <T> エンティティの型
     * @param entity エンティティ
     * @return パラメータマップ
     */
    public static <T> Map<String, Object> getEntityParams(T entity) {
        Map<String, Object> params = new HashMap<>();
        Arrays.stream(entity.getClass().getDeclaredFields()).forEach(field -> {
            try {
                field.setAccessible(true);
                
                String columnName = null;
                
                // 新アノテーションを優先
                FluidColumn fluidColumn = field.getAnnotation(FluidColumn.class);
                if (fluidColumn != null) {
                    columnName = fluidColumn.name();
                } else {
                    // 旧アノテーションにフォールバック
                    jp.vemi.seasarbatis.core.meta.SBColumnMeta columnMeta = 
                        field.getAnnotation(jp.vemi.seasarbatis.core.meta.SBColumnMeta.class);
                    if (columnMeta != null) {
                        columnName = columnMeta.name();
                    }
                }
                
                if (columnName == null) {
                    throw new FluidException("カラムメタ情報が不明です: " + field.getName());
                }

                Object value = null;

                // boolean型のフィールドの場合、isXxx形式のgetterメソッドを試す
                if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                    try {
                        String getterName = "is" + Character.toUpperCase(field.getName().charAt(0))
                                + field.getName().substring(1);
                        java.lang.reflect.Method getter = entity.getClass().getMethod(getterName);
                        value = getter.invoke(entity);
                    } catch (NoSuchMethodException e) {
                        // isXxx形式のgetterメソッドがない場合は、そのままfield.get()を試す
                        value = field.get(entity);
                    }
                } else {
                    value = field.get(entity);
                }
                params.put(columnName, value);
            } catch (Exception e) {
                throw new FluidException("パラメータの取得に失敗しました", e);
            }
        });
        return params;
    }

    /**
     * エンティティクラスから主キー情報を取得します。
     *
     * @param <T> エンティティの型
     * @param entityClass エンティティクラス
     * @return 主キー情報
     */
    public static <T> PrimaryKeyInfo getPrimaryKeyInfo(Class<T> entityClass) {
        List<java.lang.reflect.Field> pkFields = Arrays.stream(entityClass.getDeclaredFields()).filter(field -> {
            // 新アノテーションを優先
            FluidColumn fluidColumn = field.getAnnotation(FluidColumn.class);
            if (fluidColumn != null) {
                return fluidColumn.primaryKey();
            }
            
            // 旧アノテーションにフォールバック
            jp.vemi.seasarbatis.core.meta.SBColumnMeta columnMeta = 
                field.getAnnotation(jp.vemi.seasarbatis.core.meta.SBColumnMeta.class);
            return columnMeta != null && columnMeta.primaryKey();
        }).collect(Collectors.toList());

        if (pkFields.isEmpty()) {
            throw new FluidIllegalStateException("主キーが見つかりません: " + entityClass.getName());
        }

        List<String> pkColumnNames = pkFields.stream().map(field -> {
            FluidColumn fluidColumn = field.getAnnotation(FluidColumn.class);
            if (fluidColumn != null) {
                return fluidColumn.name();
            }
            jp.vemi.seasarbatis.core.meta.SBColumnMeta columnMeta = 
                field.getAnnotation(jp.vemi.seasarbatis.core.meta.SBColumnMeta.class);
            return columnMeta != null ? columnMeta.name() : field.getName();
        }).collect(Collectors.toList());

        return new PrimaryKeyInfo(pkFields, pkColumnNames);
    }

    /**
     * エンティティから主キーの値を取得します。
     *
     * @param <T> エンティティの型
     * @param entity エンティティ
     * @return 主キーの値
     */
    public static <T> Map<String, Object> getPrimaryKeyValues(T entity) {
        PrimaryKeyInfo pkInfo = getPrimaryKeyInfo(entity.getClass());
        return pkInfo.getPrimaryKeyValues(entity);
    }
}
