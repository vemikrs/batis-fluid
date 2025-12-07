/*
 * Copyright(c) 2025 VEMIDaS, All rights reserved.
 */
package jp.vemi.seasarbatis.core.sql.loader;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;

import jp.vemi.batisfluid.sql.SqlFileLoader;

/**
 * SQLファイルを読み込むためのユーティリティクラスです。
 *
 * @deprecated v0.0.2以降は {@link SqlFileLoader} を使用してください。
 */
@Deprecated(since = "0.0.2", forRemoval = true)
public class SBSqlFileLoader {
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
