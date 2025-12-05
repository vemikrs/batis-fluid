/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.config;

import java.util.Locale;
import java.util.Objects;

import jp.vemi.batisfluid.i18n.FluidLocale;
import jp.vemi.batisfluid.i18n.Messages;

/**
 * BatisFluidのグローバル設定を管理するクラスです。
 * <p>
 * アプリケーション全体で使用するBatisFluidの設定を一元管理します。
 * ロケール設定、楽観的排他制御設定などを含みます。
 * </p>
 * 
 * @author H.Kurosawa
 * @version 0.0.2
 */
public class FluidConfig {
    
    private static final FluidConfig INSTANCE = new FluidConfig();
    
    private OptimisticLockConfig optimisticLockConfig;
    
    /**
     * FluidConfigを構築します。
     */
    private FluidConfig() {
        this.optimisticLockConfig = OptimisticLockConfigLoader.loadDefault();
    }
    
    /**
     * FluidConfigのシングルトンインスタンスを取得します。
     * 
     * @return FluidConfigのインスタンス
     */
    public static FluidConfig getInstance() {
        return INSTANCE;
    }
    
    /**
     * 楽観的排他制御設定を取得します。
     * 
     * @return 楽観的排他制御設定
     */
    public OptimisticLockConfig getOptimisticLockConfig() {
        return optimisticLockConfig;
    }
    
    /**
     * 楽観的排他制御設定を設定します。
     * 
     * @param config 楽観的排他制御設定
     * @return このインスタンス（メソッドチェーン用）
     */
    public FluidConfig setOptimisticLockConfig(OptimisticLockConfig config) {
        this.optimisticLockConfig = Objects.requireNonNull(config, "config must not be null");
        return this;
    }
    
    /**
     * ロケールを設定します。
     * 
     * @param locale 設定するロケール
     * @return このインスタンス（メソッドチェーン用）
     */
    public FluidConfig setLocale(Locale locale) {
        FluidLocale.getInstance().setLocale(locale);
        Messages.getInstance().setLocale(locale);
        return this;
    }
    
    /**
     * 現在のロケールを取得します。
     * 
     * @return 現在のロケール
     */
    public Locale getCurrentLocale() {
        return FluidLocale.getInstance().getCurrentLocale();
    }
    
    /**
     * ロケールを日本語に設定します。
     * 
     * @return このインスタンス（メソッドチェーン用）
     */
    public FluidConfig setJapanese() {
        setLocale(Locale.JAPANESE);
        return this;
    }
    
    /**
     * ロケールを英語に設定します。
     * 
     * @return このインスタンス（メソッドチェーン用）
     */
    public FluidConfig setEnglish() {
        setLocale(Locale.ENGLISH);
        return this;
    }
    
    /**
     * システムのデフォルトロケールに設定します。
     * 
     * @return このインスタンス（メソッドチェーン用）
     */
    public FluidConfig setDefaultLocale() {
        setLocale(Locale.getDefault());
        return this;
    }
}
