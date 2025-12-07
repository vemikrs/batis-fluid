/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * {@link FluidConfig} のテストクラスです。
 * 
 * @author H.Kurosawa
 * @version 0.0.2
 */
class FluidConfigTest {
    
    private Locale originalLocale;
    
    @BeforeEach
    void setUp() {
        originalLocale = Locale.getDefault();
    }
    
    @AfterEach
    void tearDown() {
        FluidConfig.getInstance().setLocale(originalLocale);
    }
    
    @Nested
    @DisplayName("シングルトン テスト")
    class SingletonTest {
        
        @Test
        @DisplayName("シングルトンインスタンスが取得できること")
        void getInstance_returnsSingleton() {
            FluidConfig instance1 = FluidConfig.getInstance();
            FluidConfig instance2 = FluidConfig.getInstance();
            
            assertThat(instance1).isSameAs(instance2);
        }
    }
    
    @Nested
    @DisplayName("ロケール設定 テスト")
    class LocaleTest {
        
        @Test
        @DisplayName("日本語ロケールに設定できること")
        void setJapanese_setsJapaneseLocale() {
            FluidConfig.getInstance().setJapanese();
            
            assertThat(FluidConfig.getInstance().getCurrentLocale())
                .isEqualTo(Locale.JAPANESE);
        }
        
        @Test
        @DisplayName("英語ロケールに設定できること")
        void setEnglish_setsEnglishLocale() {
            FluidConfig.getInstance().setEnglish();
            
            assertThat(FluidConfig.getInstance().getCurrentLocale())
                .isEqualTo(Locale.ENGLISH);
        }
        
        @Test
        @DisplayName("任意のロケールを設定できること")
        void setLocale_setsSpecifiedLocale() {
            FluidConfig.getInstance().setLocale(Locale.GERMAN);
            
            assertThat(FluidConfig.getInstance().getCurrentLocale())
                .isEqualTo(Locale.GERMAN);
        }
        
        @Test
        @DisplayName("メソッドチェーンで設定できること")
        void methodChaining_works() {
            FluidConfig config = FluidConfig.getInstance()
                .setJapanese()
                .setEnglish();
            
            assertThat(config).isSameAs(FluidConfig.getInstance());
            assertThat(config.getCurrentLocale()).isEqualTo(Locale.ENGLISH);
        }
    }
    
    @Nested
    @DisplayName("楽観的排他制御設定 テスト")
    class OptimisticLockConfigTest {
        
        @Test
        @DisplayName("デフォルトの楽観的排他制御設定が取得できること")
        void getOptimisticLockConfig_returnsDefaultConfig() {
            OptimisticLockConfig config = FluidConfig.getInstance().getOptimisticLockConfig();
            
            assertThat(config).isNotNull();
            assertThat(config.isEnabled()).isTrue();
        }
        
        @Test
        @DisplayName("楽観的排他制御設定を変更できること")
        void setOptimisticLockConfig_updatesConfig() {
            OptimisticLockConfig newConfig = new OptimisticLockConfig()
                .setEnabled(false)
                .setDefaultLockType(OptimisticLockConfig.LockType.VERSION);
            
            FluidConfig.getInstance().setOptimisticLockConfig(newConfig);
            
            assertThat(FluidConfig.getInstance().getOptimisticLockConfig())
                .isSameAs(newConfig);
        }
        
        @Test
        @DisplayName("nullを設定するとNullPointerExceptionが発生すること")
        void setOptimisticLockConfig_withNull_throwsException() {
            assertThatThrownBy(() -> FluidConfig.getInstance().setOptimisticLockConfig(null))
                .isInstanceOf(NullPointerException.class);
        }
    }
}
