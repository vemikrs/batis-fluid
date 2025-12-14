/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.seasarbatis.core.sql.dialect;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

import javax.sql.DataSource;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;

/**
 * データソースやMyBatis設定から、適切な{@link SBDialect}を判定するユーティリティです。
 * <p>
 * DB製品名（{@link DatabaseMetaData#getDatabaseProductName()}）を元にDialectを推定します。
 * 判定できない場合は互換性重視で{@link PostgresDialect}を返します。
 * </p>
 *
 * @author VEMI
 * @version 0.0.2
 * @since 2025/01/01
 */
public final class SBDialectResolver {

    private SBDialectResolver() {
    }

    /**
     * {@link DataSource}からDialectを判定します。
     *
     * @param dataSource データソース
     * @return 判定されたDialect（判定不能時は{@link PostgresDialect}）
     */
    public static SBDialect resolve(DataSource dataSource) {
        if (dataSource == null) {
            return new PostgresDialect();
        }

        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            String productName = metaData != null ? metaData.getDatabaseProductName() : null;
            return resolveFromProductName(productName);
        } catch (Exception ignored) {
            return new PostgresDialect();
        }
    }

    /**
     * {@link Configuration}からDialectを判定します。
     *
     * @param configuration MyBatis設定
     * @return 判定されたDialect（判定不能時は{@link PostgresDialect}）
     */
    public static SBDialect resolve(Configuration configuration) {
        if (configuration == null) {
            return new PostgresDialect();
        }

        Environment environment = configuration.getEnvironment();
        if (environment == null) {
            return new PostgresDialect();
        }

        return resolve(environment.getDataSource());
    }

    private static SBDialect resolveFromProductName(String productName) {
        if (productName == null || productName.isBlank()) {
            return new PostgresDialect();
        }

        String normalized = productName.toLowerCase();
        if (normalized.contains("microsoft") && normalized.contains("sql") && normalized.contains("server")) {
            return new SqlServerDialect();
        }
        if (normalized.contains("sql server")) {
            return new SqlServerDialect();
        }
        if (normalized.contains("oracle")) {
            return new OracleDialect();
        }
        if (normalized.contains("postgres")) {
            return new PostgresDialect();
        }

        return new PostgresDialect();
    }
}
