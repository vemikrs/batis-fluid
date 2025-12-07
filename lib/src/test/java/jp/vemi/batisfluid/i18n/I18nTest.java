/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.i18n;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * {@link FluidLocale} および {@link Messages} のテストクラスです。
 * <p>
 * i18n関連クラスの動作を検証します。
 * </p>
 * 
 * @author H.Kurosawa
 * @version 0.0.2
 */
class I18nTest {
    
    private Locale originalLocale;
    
    @BeforeEach
    void setUp() {
        originalLocale = Locale.getDefault();
    }
    
    @AfterEach
    void tearDown() {
        // テスト後にロケールを復元
        FluidLocale.getInstance().setLocale(originalLocale);
    }
    
    @Nested
    @DisplayName("FluidLocale テスト")
    class FluidLocaleTest {
        
        @Test
        @DisplayName("シングルトンインスタンスが取得できること")
        void getInstance_returnsSingleton() {
            FluidLocale instance1 = FluidLocale.getInstance();
            FluidLocale instance2 = FluidLocale.getInstance();
            
            assertThat(instance1).isSameAs(instance2);
        }
        
        @Test
        @DisplayName("日本語ロケールに設定できること")
        void setJapanese_setsJapaneseLocale() {
            FluidLocale.getInstance().setJapanese();
            
            assertThat(FluidLocale.getInstance().getCurrentLocale())
                .isEqualTo(Locale.JAPANESE);
        }
        
        @Test
        @DisplayName("英語ロケールに設定できること")
        void setEnglish_setsEnglishLocale() {
            FluidLocale.getInstance().setEnglish();
            
            assertThat(FluidLocale.getInstance().getCurrentLocale())
                .isEqualTo(Locale.ENGLISH);
        }
        
        @Test
        @DisplayName("任意のロケールを設定できること")
        void setLocale_setsSpecifiedLocale() {
            FluidLocale.getInstance().setLocale(Locale.GERMAN);
            
            assertThat(FluidLocale.getInstance().getCurrentLocale())
                .isEqualTo(Locale.GERMAN);
        }
    }
    
    @Nested
    @DisplayName("Messages テスト")
    class MessagesTest {
        
        @Test
        @DisplayName("シングルトンインスタンスが取得できること")
        void getInstance_returnsSingleton() {
            Messages instance1 = Messages.getInstance();
            Messages instance2 = Messages.getInstance();
            
            assertThat(instance1).isSameAs(instance2);
        }
        
        @Test
        @DisplayName("存在しないキーではデフォルトメッセージが返されること")
        void getMessage_returnsDefaultForMissingKey() {
            String result = Messages.getInstance().getMessage("non.existent.key");
            
            assertThat(result).contains("non.existent.key");
        }
        
        @Test
        @DisplayName("ロケールを変更できること")
        void setLocale_changesLocale() {
            Messages.getInstance().setLocale(Locale.JAPANESE);
            
            assertThat(Messages.getInstance().getCurrentLocale())
                .isEqualTo(Locale.JAPANESE);
        }
        
        @Test
        @DisplayName("nullロケールの場合はデフォルトロケールが使用されること")
        void setLocale_withNull_usesDefault() {
            Messages.getInstance().setLocale(null);
            
            assertThat(Messages.getInstance().getCurrentLocale())
                .isEqualTo(Locale.getDefault());
        }
        
        @Test
        @DisplayName("メッセージキーでメッセージを取得できること")
        void getMessage_returnsMessage() {
            Messages.getInstance().setLocale(Locale.ENGLISH);
            
            String result = Messages.getInstance().getMessage("transaction.error.execution");
            
            assertThat(result).isEqualTo("Transaction execution error");
        }
        
        @Test
        @DisplayName("日本語ロケールで日本語メッセージが取得できること")
        void getMessage_withJapaneseLocale_returnsJapaneseMessage() {
            Messages.getInstance().setLocale(Locale.JAPANESE);
            
            String result = Messages.getInstance().getMessage("transaction.error.execution");
            
            assertThat(result).isEqualTo("トランザクション実行エラー");
        }
        
        @Test
        @DisplayName("パラメータ付きメッセージが取得できること")
        void getMessage_withArgs_formatsMessage() {
            Messages.getInstance().setLocale(Locale.ENGLISH);
            
            String result = Messages.getInstance().getMessage(
                "transaction.error.unsupported.propagation", "TEST_TYPE");
            
            assertThat(result).isEqualTo("Unsupported propagation type: TEST_TYPE");
        }
    }
}
