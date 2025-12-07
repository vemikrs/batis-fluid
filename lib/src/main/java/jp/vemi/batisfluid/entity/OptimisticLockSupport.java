/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.entity;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import jp.vemi.batisfluid.config.OptimisticLockConfig;
import jp.vemi.batisfluid.config.OptimisticLockConfig.EntityLockConfig;
import jp.vemi.batisfluid.config.OptimisticLockConfig.LockType;
import jp.vemi.batisfluid.meta.FluidColumn;
import jp.vemi.batisfluid.exception.EntityException;
import jp.vemi.batisfluid.exception.FluidIllegalStateException;

/**
 * 楽観的排他制御に関するエンティティ操作を提供するユーティリティクラスです。
 * <p>
 * バージョンカラムや最終更新日時カラムの検出、値の更新、
 * WHERE句への条件追加などの機能を提供します。
 * </p>
 * 
 * @author H.Kurosawa
 * @version 0.0.2
 */
public class OptimisticLockSupport {
    
    /**
     * エンティティからバージョンカラムの情報を取得します。
     * <p>
     * 新しい {@link FluidColumn} アノテーションを優先的に使用し、
     * 存在しない場合は旧アノテーションを確認します。
     * </p>
     * 
     * @param entity エンティティインスタンス
     * @return バージョンカラムの情報、存在しない場合はEmpty
     */
    public static Optional<VersionColumnInfo> getVersionColumnInfo(Object entity) {
        Class<?> entityClass = entity.getClass();
        
        for (Field field : entityClass.getDeclaredFields()) {
            // 新アノテーションを優先
            FluidColumn fluidColumn = field.getAnnotation(FluidColumn.class);
            if (fluidColumn != null && fluidColumn.versionColumn()) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(entity);
                    return Optional.of(new VersionColumnInfo(field, fluidColumn.name(), value));
                } catch (IllegalAccessException e) {
                    throw new EntityException("entity.error.metadata", e);
                }
            }
            
            // 旧アノテーションにフォールバック
            jp.vemi.seasarbatis.core.meta.SBColumnMeta columnMeta = 
                field.getAnnotation(jp.vemi.seasarbatis.core.meta.SBColumnMeta.class);
            if (columnMeta != null && columnMeta.versionColumn()) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(entity);
                    return Optional.of(new VersionColumnInfo(field, columnMeta.name(), value));
                } catch (IllegalAccessException e) {
                    throw new EntityException("entity.error.metadata", e);
                }
            }
        }
        
        return Optional.empty();
    }
    
    /**
     * エンティティから最終更新日時カラムの情報を取得します。
     * 
     * @param entity エンティティインスタンス
     * @return 最終更新日時カラムの情報、存在しない場合はEmpty
     */
    public static Optional<LastModifiedColumnInfo> getLastModifiedColumnInfo(Object entity) {
        Class<?> entityClass = entity.getClass();
        
        for (Field field : entityClass.getDeclaredFields()) {
            // 新アノテーションを優先
            FluidColumn fluidColumn = field.getAnnotation(FluidColumn.class);
            if (fluidColumn != null && fluidColumn.lastModifiedColumn()) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(entity);
                    return Optional.of(new LastModifiedColumnInfo(field, fluidColumn.name(), value));
                } catch (IllegalAccessException e) {
                    throw new EntityException("entity.error.metadata", e);
                }
            }
            
            // 旧アノテーションにフォールバック
            jp.vemi.seasarbatis.core.meta.SBColumnMeta columnMeta = 
                field.getAnnotation(jp.vemi.seasarbatis.core.meta.SBColumnMeta.class);
            if (columnMeta != null && columnMeta.lastModifiedColumn()) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(entity);
                    return Optional.of(new LastModifiedColumnInfo(field, columnMeta.name(), value));
                } catch (IllegalAccessException e) {
                    throw new EntityException("entity.error.metadata", e);
                }
            }
        }
        
        return Optional.empty();
    }
    
    /**
     * エンティティの楽観的排他制御情報を取得します。
     * 
     * @param entity エンティティインスタンス
     * @param config 楽観的排他制御設定
     * @return 楽観的排他制御情報
     */
    public static OptimisticLockInfo getOptimisticLockInfo(Object entity, OptimisticLockConfig config) {
        if (!config.isEnabled()) {
            return new OptimisticLockInfo(LockType.NONE, null, null, null);
        }
        
        Class<?> entityClass = entity.getClass();
        Optional<EntityLockConfig> entityConfig = config.getEntityConfig(entityClass);
        
        LockType lockType;
        if (entityConfig.isPresent()) {
            lockType = entityConfig.get().getLockType();
        } else {
            lockType = config.getDefaultLockType();
        }
        
        switch (lockType) {
            case VERSION:
                Optional<VersionColumnInfo> versionInfo = getVersionColumnInfo(entity);
                if (versionInfo.isPresent()) {
                    return new OptimisticLockInfo(LockType.VERSION, versionInfo.get().getColumnName(), 
                                                  versionInfo.get().getValue(), versionInfo.get().getField());
                }
                break;
                
            case LAST_MODIFIED:
                Optional<LastModifiedColumnInfo> lastModifiedInfo = getLastModifiedColumnInfo(entity);
                if (lastModifiedInfo.isPresent()) {
                    return new OptimisticLockInfo(LockType.LAST_MODIFIED, lastModifiedInfo.get().getColumnName(),
                                                  lastModifiedInfo.get().getValue(), lastModifiedInfo.get().getField());
                }
                break;
                
            default:
                break;
        }
        
        return new OptimisticLockInfo(LockType.NONE, null, null, null);
    }
    
    /**
     * エンティティの楽観的排他制御用カラムの値を更新します。
     * 
     * @param entity エンティティインスタンス
     * @param lockInfo 楽観的排他制御情報
     * @return 更新後の値
     */
    public static Object updateOptimisticLockValue(Object entity, OptimisticLockInfo lockInfo) {
        if (lockInfo.getLockType() == LockType.NONE || lockInfo.getField() == null) {
            return null;
        }
        
        Field field = lockInfo.getField();
        Object newValue;
        
        try {
            field.setAccessible(true);
            
            if (lockInfo.getLockType() == LockType.VERSION) {
                Object currentValue = field.get(entity);
                if (currentValue == null) {
                    newValue = 1L;
                } else if (currentValue instanceof Number) {
                    newValue = ((Number) currentValue).longValue() + 1;
                } else {
                    throw new FluidIllegalStateException("バージョンカラムの型が数値型ではありません: " + field.getType());
                }
            } else if (lockInfo.getLockType() == LockType.LAST_MODIFIED) {
                Class<?> fieldType = field.getType();
                if (fieldType == Date.class) {
                    newValue = new Date();
                } else if (fieldType == LocalDateTime.class) {
                    newValue = LocalDateTime.now();
                } else if (Temporal.class.isAssignableFrom(fieldType)) {
                    newValue = LocalDateTime.now();
                } else {
                    throw new FluidIllegalStateException("最終更新日時カラムの型が日時型ではありません: " + fieldType);
                }
            } else {
                return null;
            }
            
            field.set(entity, newValue);
            return newValue;
            
        } catch (IllegalAccessException e) {
            throw new EntityException("entity.error.value.setting", e);
        }
    }
    
    /**
     * 楽観的排他制御用のWHERE句条件を生成します。
     * 
     * @param lockInfo 楽観的排他制御情報
     * @param params SQLパラメータマップ
     * @return WHERE句の条件文字列
     */
    public static String buildOptimisticLockCondition(OptimisticLockInfo lockInfo, Map<String, Object> params) {
        if (lockInfo.getLockType() == LockType.NONE || lockInfo.getColumnName() == null) {
            return "";
        }
        
        String paramName = "optimisticLockValue";
        params.put(paramName, lockInfo.getCurrentValue());
        
        return " AND " + lockInfo.getColumnName() + " = /*" + paramName + "*/null";
    }
    
    /**
     * バージョンカラムの情報を保持するクラスです。
     */
    public static class VersionColumnInfo {
        private final Field field;
        private final String columnName;
        private final Object value;
        
        /**
         * バージョンカラム情報を構築します。
         *
         * @param field フィールド
         * @param columnName カラム名
         * @param value 値
         */
        public VersionColumnInfo(Field field, String columnName, Object value) {
            this.field = field;
            this.columnName = columnName;
            this.value = value;
        }
        
        /**
         * フィールドを取得します。
         *
         * @return フィールド
         */
        public Field getField() { 
            return field; 
        }
        
        /**
         * カラム名を取得します。
         *
         * @return カラム名
         */
        public String getColumnName() { 
            return columnName; 
        }
        
        /**
         * 値を取得します。
         *
         * @return 値
         */
        public Object getValue() { 
            return value; 
        }
    }
    
    /**
     * 最終更新日時カラムの情報を保持するクラスです。
     */
    public static class LastModifiedColumnInfo {
        private final Field field;
        private final String columnName;
        private final Object value;
        
        /**
         * 最終更新日時カラム情報を構築します。
         *
         * @param field フィールド
         * @param columnName カラム名
         * @param value 値
         */
        public LastModifiedColumnInfo(Field field, String columnName, Object value) {
            this.field = field;
            this.columnName = columnName;
            this.value = value;
        }
        
        /**
         * フィールドを取得します。
         *
         * @return フィールド
         */
        public Field getField() { 
            return field; 
        }
        
        /**
         * カラム名を取得します。
         *
         * @return カラム名
         */
        public String getColumnName() { 
            return columnName; 
        }
        
        /**
         * 値を取得します。
         *
         * @return 値
         */
        public Object getValue() { 
            return value; 
        }
    }
    
    /**
     * 楽観的排他制御の情報を保持するクラスです。
     */
    public static class OptimisticLockInfo {
        private final LockType lockType;
        private final String columnName;
        private final Object currentValue;
        private final Field field;
        
        /**
         * 楽観的排他制御情報を構築します。
         *
         * @param lockType ロックタイプ
         * @param columnName カラム名
         * @param currentValue 現在の値
         * @param field フィールド
         */
        public OptimisticLockInfo(LockType lockType, String columnName, Object currentValue, Field field) {
            this.lockType = lockType;
            this.columnName = columnName;
            this.currentValue = currentValue;
            this.field = field;
        }
        
        /**
         * ロックタイプを取得します。
         *
         * @return ロックタイプ
         */
        public LockType getLockType() { 
            return lockType; 
        }
        
        /**
         * カラム名を取得します。
         *
         * @return カラム名
         */
        public String getColumnName() { 
            return columnName; 
        }
        
        /**
         * 現在の値を取得します。
         *
         * @return 現在の値
         */
        public Object getCurrentValue() { 
            return currentValue; 
        }
        
        /**
         * フィールドを取得します。
         *
         * @return フィールド
         */
        public Field getField() { 
            return field; 
        }
        
        /**
         * 楽観的排他制御が有効かどうかを取得します。
         *
         * @return 有効な場合true
         */
        public boolean isEnabled() {
            return lockType != LockType.NONE;
        }
    }
}
