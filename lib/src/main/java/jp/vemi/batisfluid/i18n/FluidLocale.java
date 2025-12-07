/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.i18n;

import java.util.Locale;

/**
 * BatisFluidの国際化設定を管理するクラスです。
 * <p>
 * ロケールの設定や国際化に関する設定を一元管理します。
 * アプリケーション全体で共通のロケール設定を提供します。
 * </p>
 * 
 * @author H.Kurosawa
 * @version 0.0.2
 * @since 0.0.2
 */
public class FluidLocale {
    
    private static final FluidLocale INSTANCE = new FluidLocale();
    
    /**
     * FluidLocaleのシングルトンインスタンスを取得します。
     * 
     * @return FluidLocaleのインスタンス
     */
    public static FluidLocale getInstance() {
        return INSTANCE;
    }
    
    /**
     * FluidLocaleを構築します。
     */
    private FluidLocale() {
    }
    
    /**
     * アプリケーション全体のロケールを設定します。
     * 
     * @param locale 設定するロケール
     */
    public void setLocale(Locale locale) {
        Messages.getInstance().setLocale(locale);
    }
    
    /**
     * 現在のロケールを取得します。
     * 
     * @return 現在のロケール
     */
    public Locale getCurrentLocale() {
        return Messages.getInstance().getCurrentLocale();
    }
    
    /**
     * ロケールを日本語に設定します。
     */
    public void setJapanese() {
        setLocale(Locale.JAPANESE);
    }
    
    /**
     * ロケールを英語に設定します。
     */
    public void setEnglish() {
        setLocale(Locale.ENGLISH);
    }
    
    /**
     * システムのデフォルトロケールに設定します。
     */
    public void setDefault() {
        setLocale(Locale.getDefault());
    }
}
