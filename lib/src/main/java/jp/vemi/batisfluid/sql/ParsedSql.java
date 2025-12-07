/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.sql;

import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 解析済みSQLの結果を保持するクラスです。
 * <p>
 * SQLパーサーが解析したSQLとパラメータ情報を保持します。
 * </p>
 *
 * @author H.Kurosawa
 * @version 0.0.2
 */
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ParsedSql {
    
    /**
     * 解析後のSQL文。
     */
    private String sql;
    
    /**
     * パラメータ名のリスト。
     */
    private List<String> parameterNames;
    
    /**
     * パラメータ名と値のマップ。
     */
    private Map<String, Object> parameterValues;
}
