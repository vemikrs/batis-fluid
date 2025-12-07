/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.sql;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;

/**
 * SQLファイルを読み込むためのユーティリティクラスです。
 * <p>
 * クラスパスからSQLファイルを読み込み、文字列として返します。
 * </p>
 *
 * @author H.Kurosawa
 * @version 0.0.2
 */
public class SqlFileLoader {
    
    /**
     * 指定されたパスのSQLファイルを読み込みます。
     *
     * @param filePath SQLファイルのパス（クラスパスからの相対パス）
     * @return 読み込まれたSQL文字列
     * @throws IOException ファイルの読み込みに失敗した場合
     */
    public static String load(String filePath) throws IOException {
        try (Reader reader = Resources.getResourceAsReader(filePath)) {
            StringBuilder sql = new StringBuilder();
            char[] buffer = new char[1024];
            int bytesRead;
            while ((bytesRead = reader.read(buffer)) != -1) {
                sql.append(buffer, 0, bytesRead);
            }
            return sql.toString();
        }
    }
}
