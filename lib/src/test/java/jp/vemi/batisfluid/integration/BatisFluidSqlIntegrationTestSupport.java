/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.integration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

/**
 * BatisFluid 統合テスト向けの共通ユーティリティです。
 * <p>
 * 本クラスは、外部SQL（.sql ファイル）を実データベースで検証するテストの共通処理を提供します。
 * </p>
 *
 * @author VEMI
 * @version 0.0.2
 */
final class BatisFluidSqlIntegrationTestSupport {

    private BatisFluidSqlIntegrationTestSupport() {
    }

    static SqlSessionFactory createSqlSessionFactory(DataSource dataSource) {
        try (var reader = Resources.getResourceAsReader("mybatis-config.xml")) {
            SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(reader);
            factory.getConfiguration().setEnvironment(
                    new Environment("development", new JdbcTransactionFactory(), dataSource));
            return factory;
        } catch (IOException ex) {
            throw new IllegalStateException("MyBatis設定の読み込みに失敗しました", ex);
        }
    }

    static Map<String, Object> buildParameterMap() {
        Map<String, Object> params = new HashMap<>();
        params.put("statuses", List.of("ACTIVE", "VIP"));
        params.put("keyword", "%テスト%");
        params.put("minScore", 80.0);
        params.put("includeInactive", Boolean.FALSE);
        return params;
    }

    static void runScripts(DataSource dataSource, String... resources) throws IOException, SQLException {
        for (String resource : resources) {
            String sql = loadResource(resource);
            executeStatements(dataSource, sql);
        }
    }

    private static String loadResource(String resource) throws IOException {
        ClassLoader loader = BatisFluidSqlIntegrationTestSupport.class.getClassLoader();
        try (InputStream in = loader.getResourceAsStream(resource)) {
            if (in == null) {
                throw new IOException("リソースが見つかりません: " + resource);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        }
    }

    private static void executeStatements(DataSource dataSource, String sql) throws SQLException {
        String[] statements = sql.split(";");
        try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
            for (String raw : statements) {
                String trimmed = raw.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("--")) {
                    continue;
                }
                statement.execute(trimmed);
            }
        }
    }
}
