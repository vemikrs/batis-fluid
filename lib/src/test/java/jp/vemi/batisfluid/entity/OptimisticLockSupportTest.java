/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import jp.vemi.batisfluid.config.OptimisticLockConfig;
import jp.vemi.batisfluid.config.OptimisticLockConfig.LockType;
import jp.vemi.batisfluid.meta.FluidTable;
import jp.vemi.batisfluid.meta.FluidColumn;

/**
 * {@link OptimisticLockSupport} のテストクラスです。
 * 
 * @author H.Kurosawa
 * @version 0.0.2
 */
class OptimisticLockSupportTest {
    
    @Nested
    @DisplayName("getVersionColumnInfo テスト")
    class GetVersionColumnInfoTest {
        
        @Test
        @DisplayName("バージョンカラム情報を取得できること")
        void getVersionColumnInfo_returnsVersionInfo() {
            VersionEntity entity = new VersionEntity();
            entity.setVersion(1L);
            
            Optional<OptimisticLockSupport.VersionColumnInfo> info = 
                OptimisticLockSupport.getVersionColumnInfo(entity);
            
            assertThat(info).isPresent();
            assertThat(info.get().getColumnName()).isEqualTo("version");
            assertThat(info.get().getValue()).isEqualTo(1L);
        }
        
        @Test
        @DisplayName("バージョンカラムがない場合はEmptyを返すこと")
        void getVersionColumnInfo_withoutVersionColumn_returnsEmpty() {
            NoVersionEntity entity = new NoVersionEntity();
            
            Optional<OptimisticLockSupport.VersionColumnInfo> info = 
                OptimisticLockSupport.getVersionColumnInfo(entity);
            
            assertThat(info).isEmpty();
        }
    }
    
    @Nested
    @DisplayName("getLastModifiedColumnInfo テスト")
    class GetLastModifiedColumnInfoTest {
        
        @Test
        @DisplayName("最終更新日時カラム情報を取得できること")
        void getLastModifiedColumnInfo_returnsLastModifiedInfo() {
            LastModifiedEntity entity = new LastModifiedEntity();
            LocalDateTime now = LocalDateTime.now();
            entity.setUpdatedAt(now);
            
            Optional<OptimisticLockSupport.LastModifiedColumnInfo> info = 
                OptimisticLockSupport.getLastModifiedColumnInfo(entity);
            
            assertThat(info).isPresent();
            assertThat(info.get().getColumnName()).isEqualTo("updated_at");
            assertThat(info.get().getValue()).isEqualTo(now);
        }
    }
    
    @Nested
    @DisplayName("getOptimisticLockInfo テスト")
    class GetOptimisticLockInfoTest {
        
        @Test
        @DisplayName("楽観的排他制御が無効な場合はNONEタイプを返すこと")
        void getOptimisticLockInfo_whenDisabled_returnsNone() {
            VersionEntity entity = new VersionEntity();
            OptimisticLockConfig config = new OptimisticLockConfig().setEnabled(false);
            
            OptimisticLockSupport.OptimisticLockInfo info = 
                OptimisticLockSupport.getOptimisticLockInfo(entity, config);
            
            assertThat(info.getLockType()).isEqualTo(LockType.NONE);
            assertThat(info.isEnabled()).isFalse();
        }
        
        @Test
        @DisplayName("デフォルトがNONEの場合は楽観的排他制御情報なしを返すこと")
        void getOptimisticLockInfo_withDefaultNone_returnsNone() {
            VersionEntity entity = new VersionEntity();
            OptimisticLockConfig config = new OptimisticLockConfig()
                .setDefaultLockType(LockType.NONE);
            
            OptimisticLockSupport.OptimisticLockInfo info = 
                OptimisticLockSupport.getOptimisticLockInfo(entity, config);
            
            assertThat(info.getLockType()).isEqualTo(LockType.NONE);
        }
    }
    
    @Nested
    @DisplayName("updateOptimisticLockValue テスト")
    class UpdateOptimisticLockValueTest {
        
        @Test
        @DisplayName("バージョン値をインクリメントできること")
        void updateOptimisticLockValue_incrementsVersion() {
            VersionEntity entity = new VersionEntity();
            entity.setVersion(1L);
            
            OptimisticLockSupport.VersionColumnInfo versionInfo = 
                OptimisticLockSupport.getVersionColumnInfo(entity).get();
            OptimisticLockSupport.OptimisticLockInfo lockInfo = 
                new OptimisticLockSupport.OptimisticLockInfo(
                    LockType.VERSION, 
                    versionInfo.getColumnName(), 
                    versionInfo.getValue(), 
                    versionInfo.getField());
            
            Object newValue = OptimisticLockSupport.updateOptimisticLockValue(entity, lockInfo);
            
            assertThat(newValue).isEqualTo(2L);
            assertThat(entity.getVersion()).isEqualTo(2L);
        }
        
        @Test
        @DisplayName("初期バージョン値が1に設定されること")
        void updateOptimisticLockValue_withNullVersion_setsInitialValue() {
            VersionEntity entity = new VersionEntity();
            
            OptimisticLockSupport.VersionColumnInfo versionInfo = 
                OptimisticLockSupport.getVersionColumnInfo(entity).get();
            OptimisticLockSupport.OptimisticLockInfo lockInfo = 
                new OptimisticLockSupport.OptimisticLockInfo(
                    LockType.VERSION, 
                    versionInfo.getColumnName(), 
                    null, 
                    versionInfo.getField());
            
            Object newValue = OptimisticLockSupport.updateOptimisticLockValue(entity, lockInfo);
            
            assertThat(newValue).isEqualTo(1L);
        }
    }
    
    // テスト用エンティティ
    
    @FluidTable(name = "version_table")
    static class VersionEntity {
        @FluidColumn(name = "id", primaryKey = true)
        private Long id;
        
        @FluidColumn(name = "version", versionColumn = true)
        private Long version;
        
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getVersion() { return version; }
        public void setVersion(Long version) { this.version = version; }
    }
    
    @FluidTable(name = "no_version_table")
    static class NoVersionEntity {
        @FluidColumn(name = "id", primaryKey = true)
        private Long id;
        
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
    }
    
    @FluidTable(name = "last_modified_table")
    static class LastModifiedEntity {
        @FluidColumn(name = "id", primaryKey = true)
        private Long id;
        
        @FluidColumn(name = "updated_at", lastModifiedColumn = true)
        private LocalDateTime updatedAt;
        
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public LocalDateTime getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    }
}
