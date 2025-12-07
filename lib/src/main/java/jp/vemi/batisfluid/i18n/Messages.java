/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * BatisFluidの国際化メッセージを管理するクラスです。
 * <p>
 * リソースバンドルを使用してロケールに応じたメッセージを提供します。
 * デフォルトのロケールはシステムのロケールが使用され、
 * 対応していないロケールの場合は英語メッセージが返されます。
 * </p>
 * 
 * @author H.Kurosawa
 * @version 0.0.2
 * @since 0.0.2
 */
public class Messages {
    
    private static final String BUNDLE_NAME = "jp.vemi.batisfluid.messages";
    private static final String LEGACY_BUNDLE_NAME = "jp.vemi.seasarbatis.messages";
    private static final Messages INSTANCE = new Messages();
    
    private Locale currentLocale;
    private ResourceBundle bundle;
    
    /**
     * Messagesのシングルトンインスタンスを取得します。
     * 
     * @return Messagesのインスタンス
     */
    public static Messages getInstance() {
        return INSTANCE;
    }
    
    /**
     * Messagesを構築します。
     * デフォルトロケールで初期化されます。
     */
    private Messages() {
        this.currentLocale = Locale.getDefault();
        loadBundle();
    }
    
    /**
     * 現在のロケールを設定します。
     * 
     * @param locale 設定するロケール
     */
    public void setLocale(Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        if (!this.currentLocale.equals(locale)) {
            this.currentLocale = locale;
            loadBundle();
        }
    }
    
    /**
     * 現在のロケールを取得します。
     * 
     * @return 現在のロケール
     */
    public Locale getCurrentLocale() {
        return currentLocale;
    }
    
    /**
     * 指定されたキーに対応するメッセージを取得します。
     * 
     * @param key メッセージキー
     * @return ローカライズされたメッセージ
     */
    public String getMessage(String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return "Message not found: " + key;
        }
    }
    
    /**
     * 指定されたキーに対応するメッセージを取得し、パラメータで置換します。
     * 
     * @param key メッセージキー
     * @param args 置換パラメータ
     * @return ローカライズされ、パラメータが置換されたメッセージ
     */
    public String getMessage(String key, Object... args) {
        try {
            String message = bundle.getString(key);
            return MessageFormat.format(message, args);
        } catch (MissingResourceException e) {
            return "Message not found: " + key;
        }
    }
    
    /**
     * リソースバンドルを読み込みます。
     * 新パッケージのリソースバンドルを優先的に読み込み、
     * 存在しない場合は旧パッケージのリソースバンドルにフォールバックします。
     */
    private void loadBundle() {
        try {
            // 新パッケージのバンドルを優先
            ResourceBundle.Control noDefaultFallback = ResourceBundle.Control
                    .getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES);
            bundle = ResourceBundle.getBundle(BUNDLE_NAME, currentLocale, noDefaultFallback);
        } catch (MissingResourceException e) {
            try {
                // 旧パッケージのバンドルにフォールバック
                ResourceBundle.Control noDefaultFallback = ResourceBundle.Control
                        .getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES);
                bundle = ResourceBundle.getBundle(LEGACY_BUNDLE_NAME, currentLocale, noDefaultFallback);
            } catch (MissingResourceException e2) {
                // 最終フォールバックとして英語の旧バンドルを使用
                bundle = ResourceBundle.getBundle(LEGACY_BUNDLE_NAME, Locale.ENGLISH, 
                        ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES));
            }
        }
    }
}
