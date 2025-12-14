/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import com.mysql.cj.jdbc.MysqlDataSource;

import jp.vemi.batisfluid.BatisFluid;
import jp.vemi.batisfluid.core.SqlRunner;

/**
 * MySQL(Testcontainers) 上で BatisFluid の SqlRunner を検証する統合テストです。
 * <p>
 * Docker が利用できる環境に限定して、externalized SQL の実行結果を確認します。
 * </p>
 *
 * @author VEMI
 * @version 0.0.2
 */
@Tag("integration")
class SqlRunnerSqlFileIntegrationMySQLTest {

    private static MySQLContainer<?> mysqlContainer;
    private static SqlRunner sqlRunner;

    /**
     * Docker 利用可否を判定し、MySQL コンテナを初期化します。
     *
     * @throws Exception 初期化時に致命的なエラーが発生した場合
     */
    @BeforeAll
    static void setUpDatabase() throws Exception {
        Assumptions.assumeTrue(DockerClientFactory.instance().isDockerAvailable(),
                "Docker が利用できないため MySQL 統合テストをスキップします。");

        MySQLContainer<?> container = new MySQLContainer<>(DockerImageName.parse("mysql:8.4.6"));
        container.withDatabaseName("sbtest")
                .withUsername("test")
                .withPassword("test")
                .withEnv("MYSQL_ROOT_HOST", "%")
                .withEnv("MYSQL_ROOT_PASSWORD", "test")
                .withConfigurationOverride("mysql-8_4-conf")
                .waitingFor(
                        Wait.forLogMessage(".*mysqld: ready for connections.*port: 3306.*", 1)
                                .withStartupTimeout(Duration.ofMinutes(5)))
                .withStartupTimeout(Duration.ofMinutes(5))
                .withInitScript("ddl/mysql_init.sql");

        try {
            container.start();
            mysqlContainer = container;

            DataSource dataSource = createMysqlDataSource();
            var factory = BatisFluidSqlIntegrationTestSupport.createSqlSessionFactory(dataSource);
            sqlRunner = BatisFluid.of(factory).sqlRunner();
        } catch (Exception ex) {
            Assumptions.assumeTrue(false, "MySQL コンテナを起動できないためテストをスキップします: " + ex.getMessage());
        }
    }

    /**
     * MySQL 上で SQL ファイルを実行し、期待結果が得られるかを検証します。
     */
    @Test
    void testSelectBySqlFileOnMySQL() {
        Assumptions.assumeTrue(sqlRunner != null, "SqlRunner が初期化されていないためスキップします。");
        Map<String, Object> params = BatisFluidSqlIntegrationTestSupport.buildParameterMap();
        List<?> rows = sqlRunner.selectBySqlFile("sql/complex-users-query.sql", params, Map.class);

        assertEquals(2, rows.size());
        assertFalse(rows.isEmpty());
        assertTrue(rows.get(0) instanceof Map);
    }

    /**
     * MySQL コンテナを停止します。
     */
    @AfterAll
    static void tearDown() {
        if (mysqlContainer != null) {
            mysqlContainer.stop();
        }
    }

    private static DataSource createMysqlDataSource() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl(mysqlContainer.getJdbcUrl());
        dataSource.setUser(mysqlContainer.getUsername());
        dataSource.setPassword(mysqlContainer.getPassword());
        return dataSource;
    }
}
