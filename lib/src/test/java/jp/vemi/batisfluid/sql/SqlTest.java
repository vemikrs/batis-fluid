/*
 * Copyright (C) 2025 VEMI, All Rights Reserved.
 */
package jp.vemi.batisfluid.sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import jp.vemi.batisfluid.exception.SqlParseException;

/**
 * SQL関連クラスのテストです。
 * 
 * @author H.Kurosawa
 * @version 0.0.2
 */
class SqlTest {
    
    @Nested
    @DisplayName("SqlFileLoader テスト")
    class SqlFileLoaderTest {
        
        @Test
        @DisplayName("SQLファイルを読み込めること")
        void load_readsSqlFile() throws IOException {
            String sql = SqlFileLoader.load("sql/test-select.sql");
            
            assertThat(sql).isNotNull();
            assertThat(sql).contains("SELECT");
        }
        
        @Test
        @DisplayName("存在しないファイルはIOExceptionが発生すること")
        void load_withNonExistentFile_throwsException() {
            assertThatThrownBy(() -> SqlFileLoader.load("non-existent.sql"))
                .isInstanceOf(IOException.class);
        }
    }
    
    @Nested
    @DisplayName("SqlFormatter テスト")
    class SqlFormatterTest {
        
        @Test
        @DisplayName("複数のホワイトスペースを1つに統一すること")
        void simplify_normalizesWhitespace() {
            String sql = "SELECT  *   FROM   users";
            
            String result = SqlFormatter.simplify(sql);
            
            assertThat(result).isEqualTo("SELECT * FROM users");
        }
        
        @Test
        @DisplayName("複数行の改行を1行に統一すること")
        void simplify_normalizesNewlines() {
            String sql = "SELECT *\n\n\nFROM users";
            
            String result = SqlFormatter.simplify(sql);
            
            assertThat(result).isEqualTo("SELECT * FROM users");
        }
        
        @Test
        @DisplayName("nullの場合はnullを返すこと")
        void simplify_withNull_returnsNull() {
            String result = SqlFormatter.simplify(null);
            
            assertThat(result).isNull();
        }
        
        @Test
        @DisplayName("空文字列の場合は空文字列を返すこと")
        void simplify_withEmpty_returnsEmpty() {
            String result = SqlFormatter.simplify("");
            
            assertThat(result).isEmpty();
        }
    }
    
    @Nested
    @DisplayName("SqlParser テスト")
    class SqlParserTest {
        
        @Test
        @DisplayName("単純なバインドパラメータを解析できること")
        void parse_simpleParameter() {
            String sql = "SELECT * FROM users WHERE id = /*id*/1";
            Map<String, Object> params = new HashMap<>();
            params.put("id", 123L);
            
            ParsedSql result = SqlParser.parse(sql, params);
            
            assertThat(result.getSql()).contains("#{id}");
            assertThat(result.getParameterValues()).containsEntry("id", 123L);
        }
        
        @Test
        @DisplayName("IF条件が真の場合はSQL文が含まれること")
        void parse_ifConditionTrue_includesSql() {
            String sql = "SELECT * FROM users WHERE 1=1 /*IF name != null*/AND name = /*name*/'test'/*END*/";
            Map<String, Object> params = new HashMap<>();
            params.put("name", "John");
            
            ParsedSql result = SqlParser.parse(sql, params);
            
            assertThat(result.getSql()).contains("AND name = #{name}");
        }
        
        @Test
        @DisplayName("IF条件が偽の場合はSQL文が除外されること")
        void parse_ifConditionFalse_excludesSql() {
            String sql = "SELECT * FROM users WHERE 1=1 /*IF name != null*/AND name = /*name*/'test'/*END*/";
            Map<String, Object> params = new HashMap<>();
            params.put("name", null);
            
            ParsedSql result = SqlParser.parse(sql, params);
            
            assertThat(result.getSql()).doesNotContain("AND name");
        }
        
        @Test
        @DisplayName("コレクションパラメータをIN句用に展開できること")
        void parse_collectionParameter_expandsForInClause() {
            String sql = "SELECT * FROM users WHERE id IN /*ids*/(1, 2, 3)";
            Map<String, Object> params = new HashMap<>();
            params.put("ids", Arrays.asList(10L, 20L, 30L));
            
            ParsedSql result = SqlParser.parse(sql, params);
            
            assertThat(result.getSql()).contains("#{ids_0}");
            assertThat(result.getSql()).contains("#{ids_1}");
            assertThat(result.getSql()).contains("#{ids_2}");
            assertThat(result.getParameterValues()).containsEntry("ids_0", 10L);
        }
        
        @Test
        @DisplayName("閉じられていないコメントでエラーが発生すること")
        void parse_unclosedComment_throwsException() {
            String sql = "SELECT * FROM users WHERE id = /*id";
            Map<String, Object> params = new HashMap<>();
            
            assertThatThrownBy(() -> SqlParser.parse(sql, params))
                .isInstanceOf(SqlParseException.class)
                .hasMessageContaining("閉じられていません");
        }
        
        @Test
        @DisplayName("ENDがないIF文でエラーが発生すること")
        void parse_missingEnd_throwsException() {
            String sql = "SELECT * FROM users WHERE 1=1 /*IF name != null*/AND name = /*name*/'test'";
            Map<String, Object> params = new HashMap<>();
            
            assertThatThrownBy(() -> SqlParser.parse(sql, params))
                .isInstanceOf(SqlParseException.class)
                .hasMessageContaining("END");
        }
    }
    
    @Nested
    @DisplayName("ParsedSql テスト")
    class ParsedSqlTest {
        
        @Test
        @DisplayName("ビルダーでParsedSqlを作成できること")
        void builder_createsParsedSql() {
            ParsedSql result = ParsedSql.builder()
                .sql("SELECT * FROM users")
                .parameterNames(Arrays.asList("id"))
                .parameterValues(Map.of("id", 1L))
                .build();
            
            assertThat(result.getSql()).isEqualTo("SELECT * FROM users");
            assertThat(result.getParameterNames()).containsExactly("id");
            assertThat(result.getParameterValues()).containsEntry("id", 1L);
        }
    }
}
