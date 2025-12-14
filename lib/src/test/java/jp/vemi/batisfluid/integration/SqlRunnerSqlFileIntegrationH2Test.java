/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import jp.vemi.batisfluid.BatisFluid;
import jp.vemi.batisfluid.core.SqlRunner;

/**
 * H2 データベース上で BatisFluid の SqlRunner を利用した統合テストを実施します。
 * <p>
 * externalized SQL（.sql ファイル）を読み込み、動的SQLの生成と実行結果を検証します。
 * </p>
 *
 * @author VEMI
 * @version 0.0.2
 */
@Tag("integration")
class SqlRunnerSqlFileIntegrationH2Test {

    private static DataSource h2DataSource;
    private static SqlRunner sqlRunner;

    /**
     * H2 の組み立てと初期データ投入を行います。
     *
     * @throws Exception H2 初期化に失敗した場合
     */
    @BeforeAll
    static void setUpDatabase() throws Exception {
        h2DataSource = createH2DataSource();
        BatisFluidSqlIntegrationTestSupport.runScripts(h2DataSource,
                "ddl/01_create_h2_schema.sql",
                "ddl/02_insert_h2_data.sql");

        var factory = BatisFluidSqlIntegrationTestSupport.createSqlSessionFactory(h2DataSource);
        sqlRunner = BatisFluid.of(factory).sqlRunner();
    }

    /**
     * H2 上で SQL ファイルを実行し、結果件数とデータ整合性を検証します。
     */
    @Test
    void testSelectBySqlFileOnH2() {
        Map<String, Object> params = BatisFluidSqlIntegrationTestSupport.buildParameterMap();
        List<?> rows = sqlRunner.selectBySqlFile("sql/complex-users-query.sql", params, Map.class);

        assertEquals(2, rows.size());
        assertFalse(rows.isEmpty());
        assertTrue(rows.get(0) instanceof Map);
    }

    private static JdbcDataSource createH2DataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:sbtest;MODE=MySQL;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1");
        dataSource.setUser("sa");
        dataSource.setPassword("");
        return dataSource;
    }
}
