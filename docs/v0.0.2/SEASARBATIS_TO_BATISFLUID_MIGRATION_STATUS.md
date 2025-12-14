# SeasarBatis → BatisFluid 移行状況ドキュメント

> **作成日**: 2025年12月14日  
> **バージョン**: v0.0.2  
> **Powered by GitHub Copilot**

## 概要

このドキュメントは、`jp.vemi.seasarbatis` パッケージから `jp.vemi.batisfluid` パッケージへの機能移行状況を整理したものです。

## 移行状況サマリー

| カテゴリ | 移行済み | 未移行 | 部分移行 |
|---------|---------|-------|---------|
| コアAPI | ○ | - | - |
| SQL処理 | ○ | - | ○ |
| トランザクション | ○ | - | - |
| エンティティ操作 | - | ○ | - |
| Criteria API | ○ | - | - |
| 設定管理 | ○ | - | - |
| 例外クラス | ○ | - | - |
| メタデータ | ○ | - | - |
| i18n (国際化) | ○ | - | - |
| マッパー | - | ○ | - |
| Dialect (方言) | - | ○ | - |
| Generator | - | ○ | - |
| Scripting | - | ○ | - |

---

## 1. 移行済み機能

### 1.1 コアAPI

| SeasarBatis クラス | BatisFluid クラス | 状態 | 備考 |
|-------------------|------------------|------|------|
| `SBJdbcManager` | `JdbcFlow` | ✅ 移行済み | デリゲートパターンで実装 |
| - | `SqlRunner` | ✅ 新規追加 | 外部化SQLの実行専用 |
| - | `BatisFluid` | ✅ 新規追加 | エントリーポイント |

### 1.2 クエリビルダー

| SeasarBatis クラス | BatisFluid クラス | 状態 | 備考 |
|-------------------|------------------|------|------|
| `SBSelectBuilder` | `SelectBuilder` | ✅ 移行済み | |
| `SBUpdateBuilder` | `UpdateBuilder` | ✅ 移行済み | |
| `SBDeleteBuilder` | `DeleteBuilder` | ✅ 移行済み | |
| `SBSqlBuilder` | `SqlBuilder` | ✅ 移行済み | |
| `SBOrderByCapable` | `OrderByCapable` | ✅ 移行済み | |
| `SBWhereCapable` | `WhereCapable` | ✅ 移行済み | |

### 1.3 Criteria API

| SeasarBatis クラス | BatisFluid クラス | 状態 | 備考 |
|-------------------|------------------|------|------|
| `SimpleWhere` | `SimpleWhere` | ✅ 移行済み | Seasar2互換名を維持 |
| `ComplexWhere` | `ComplexWhere` | ✅ 移行済み | Seasar2互換名を維持 |
| `AbstractWhere` | `AbstractWhere` | ✅ 移行済み | |
| `SBWhere` | `Where` | ✅ 移行済み | |
| `OrderDirection` | `OrderDirection` | ✅ 移行済み | |

### 1.4 トランザクション管理

| SeasarBatis クラス | BatisFluid クラス | 状態 | 備考 |
|-------------------|------------------|------|------|
| `SBTransactionManager` | `TransactionManager` | ✅ 移行済み | |
| `SBTransactionCallback` | `TransactionCallback` | ✅ 移行済み | |
| `SBTransactionContext` | `TransactionContext` | ✅ 移行済み | |
| `SBTransactionOperation` | `TransactionOperation` | ✅ 移行済み | |
| `SBThreadLocalDataSource` | `ThreadLocalDataSource` | ✅ 移行済み | |
| `SBTransactionManager.PropagationType` | `PropagationType` | ✅ 移行済み | 独立クラスに変更 |

### 1.5 SQL処理

| SeasarBatis クラス | BatisFluid クラス | 状態 | 備考 |
|-------------------|------------------|------|------|
| `SBSqlFileLoader` | `SqlFileLoader` | ✅ 移行済み | |
| `SBSqlFormatter` | `SqlFormatter` | ✅ 移行済み | |
| `SBSqlParser` | `SqlParser` | ✅ 移行済み | |
| `ParsedSql` | `ParsedSql` | ✅ 移行済み | |

### 1.6 設定管理

| SeasarBatis クラス | BatisFluid クラス | 状態 | 備考 |
|-------------------|------------------|------|------|
| `SBOptimisticLockConfig` | `OptimisticLockConfig` | ✅ 移行済み | |
| `SBOptimisticLockConfigLoader` | `OptimisticLockConfigLoader` | ✅ 移行済み | |
| - | `FluidConfig` | ✅ 新規追加 | 全体設定クラス |

### 1.7 エンティティ操作

| SeasarBatis クラス | BatisFluid クラス | 状態 | 備考 |
|-------------------|------------------|------|------|
| `SBEntityOperations` | `EntityOperations` | ✅ 移行済み | |
| `SBOptimisticLockSupport` | `OptimisticLockSupport` | ✅ 移行済み | |
| `SBPrimaryKeyInfo` | `PrimaryKeyInfo` | ✅ 移行済み | |

### 1.8 メタデータ

| SeasarBatis クラス | BatisFluid クラス | 状態 | 備考 |
|-------------------|------------------|------|------|
| `SBTableMeta` | `FluidTable` | ✅ 移行済み | アノテーション |
| `SBColumnMeta` | `FluidColumn` | ✅ 移行済み | アノテーション |

### 1.9 例外クラス

| SeasarBatis クラス | BatisFluid クラス | 状態 | 備考 |
|-------------------|------------------|------|------|
| `SBException` | `FluidException` | ✅ 移行済み | 基底例外 |
| `SBSQLException` | `FluidSqlException` | ✅ 移行済み | |
| `SBEntityException` | `EntityException` | ✅ 移行済み | |
| `SBIllegalStateException` | `FluidIllegalStateException` | ✅ 移行済み | |
| `SBNoResultException` | `NoResultException` | ✅ 移行済み | |
| `SBNonUniqueResultException` | `NonUniqueResultException` | ✅ 移行済み | |
| `SBOptimisticLockException` | `OptimisticLockException` | ✅ 移行済み | |
| `SBSqlParseException` | `SqlParseException` | ✅ 移行済み | |
| `SBTransactionException` | `TransactionException` | ✅ 移行済み | |
| `SBTypeConversionException` | `TypeConversionException` | ✅ 移行済み | |

### 1.10 i18n（国際化）

| SeasarBatis クラス | BatisFluid クラス | 状態 | 備考 |
|-------------------|------------------|------|------|
| `SBLocaleConfig` | `FluidLocale` | ✅ 移行済み | |
| `SBMessageManager` | `Messages` | ✅ 移行済み | |

---

## 2. 未移行機能（移行優先度順）

### 2.1 高優先度

#### 2.1.1 Dialect（データベース方言）サポート

**現在のseasarbatisの実装:**
- `jp.vemi.seasarbatis.core.sql.dialect.SBDialect` - インターフェース
- `jp.vemi.seasarbatis.core.sql.dialect.PostgresDialect` - PostgreSQL/H2対応
- `jp.vemi.seasarbatis.core.sql.dialect.OracleDialect` - Oracle対応

**機能内容:**
- SQL リテラル生成（文字列、日付、タイムスタンプ、配列）
- データベース固有のフォーマット処理
- H2、PostgreSQL、MySQL、Oracle、SQL Server対応が必要

**移行推奨:**
```
batisfluid/
└── sql/
    └── dialect/
        ├── Dialect.java           # インターフェース
        ├── PostgresDialect.java   # PostgreSQL/H2
        ├── OracleDialect.java     # Oracle
        ├── MySqlDialect.java      # MySQL (新規追加推奨)
        └── SqlServerDialect.java  # SQL Server (新規追加推奨)
```

#### 2.1.2 SQL処理の内部クラス

**現在のseasarbatisの実装:**
- `SBSqlProcessor` - SQL解析・処理プロセッサ
- `SBMyBatisSqlProcessor` - MyBatis機能を使用したSQL処理
- `SBQueryExecutor` - SQLクエリ実行
- `ProcessedSql` - 処理済みSQL情報
- `CommandType` - SQLコマンドタイプ（SELECT/INSERT/UPDATE/DELETE）

**現状:**
- BatisFluidでは `JdbcFlow` と `SqlRunner` が内部的に `SBJdbcManager` に委譲
- 独立した実装への移行が必要

### 2.2 中優先度

#### 2.2.1 マッパー関連

**現在のseasarbatisの実装:**
- `SBEntityMapper<T>` - エンティティ基本CRUD操作
- `SBMyBatisMapper<T>` - MyBatisマッパーインターフェース管理

**用途:**
- 簡易的なCRUD操作
- MyBatis Generatorを使用しない小規模実装
- プロトタイプ開発

**移行推奨:**
```
batisfluid/
└── mapper/
    ├── EntityMapper.java
    └── MyBatisMapper.java
```

#### 2.2.2 セッション・設定管理

**現在のseasarbatisの実装:**
- `SBSqlSessionFactory` - SqlSessionFactory生成ファクトリ
- `SBMyBatisConfig` - MyBatis設定管理
- `SBJdbcManagerFactory` - JdbcManager生成ファクトリ

**移行推奨:**
```
batisfluid/
└── config/
    ├── SessionFactory.java
    └── MyBatisConfig.java
```

### 2.3 低優先度

#### 2.3.1 Generator（コード生成）

**現在のseasarbatisの実装:**
- `SBEntityMetaPlugin` - MyBatis Generator用プラグイン

**機能内容:**
- テーブルメタデータ生成（@SBTableMeta）
- カラムメタデータ生成（@SBColumnMeta）
- 主キー情報の自動付与

**移行推奨:**
```
batisfluid/
└── generator/
    └── EntityMetaPlugin.java  # @FluidTable, @FluidColumn 用
```

#### 2.3.2 Scripting（スクリプティング）

**現在のseasarbatisの実装:**
- `SBScriptLanguageDriver` - カスタムSQL言語ドライバ
- `SBDeferredSqlSource` - 遅延SQL生成

**機能内容:**
- 実行時にパラメータ `_sql` からSQLを取得
- 動的SQLソース

**移行推奨:**
```
batisfluid/
└── scripting/
    └── FluidLanguageDriver.java
```

#### 2.3.3 ユーティリティクラス

**現在のseasarbatisの実装:**
- `SBTypeConverterUtils` - 型変換ユーティリティ
- `SBEntityClassUtils` - エンティティクラスユーティリティ（@Deprecated）

**備考:**
- `SBEntityClassUtils` は廃止予定（`SBTypeConverterUtils` を使用）
- 内部実装として残す可能性あり

---

## 3. 現在の委譲関係（v0.0.2時点）

```
BatisFluid
    ├── JdbcFlow ──委譲──→ SBJdbcManager
    │       └── TransactionManager
    │
    └── SqlRunner ──委譲──→ SBJdbcManager
            └── TransactionManager


SBJdbcManager（内部で使用）
    ├── SBQueryExecutor
    │       ├── SBSqlProcessor
    │       │       └── SBMyBatisSqlProcessor
    │       │               └── SBDialect (PostgresDialect/OracleDialect)
    │       └── SBSqlFileLoader
    │
    ├── SBTransactionManager
    │       └── SBTransactionOperation
    │
    └── SBOptimisticLockSupport
```

---

## 4. 移行ロードマップ（推奨）

### Phase 1: v0.0.3（Dialect移行）
- [ ] `SBDialect` → `Dialect` インターフェース移行
- [ ] `PostgresDialect` 移行
- [ ] `OracleDialect` 移行  
- [ ] `MySqlDialect` 新規追加
- [ ] `SqlServerDialect` 新規追加

### Phase 2: v0.0.4（内部実装の独立）
- [ ] `SBSqlProcessor` → `SqlProcessor` 移行
- [ ] `SBMyBatisSqlProcessor` 移行
- [ ] `SBQueryExecutor` → `QueryExecutor` 移行
- [ ] `JdbcFlow` の委譲解消

### Phase 3: v0.0.5（マッパー・設定）
- [ ] `SBEntityMapper` → `EntityMapper` 移行
- [ ] `SBMyBatisMapper` → `MyBatisMapper` 移行
- [ ] `SBMyBatisConfig` → `MyBatisConfig` 移行

### Phase 4: v0.1.0（Generator・Scripting）
- [ ] `SBEntityMetaPlugin` → `EntityMetaPlugin` 移行
- [ ] `SBScriptLanguageDriver` → `FluidLanguageDriver` 移行
- [ ] `SBJdbcManager` 完全廃止

---

## 5. パッケージ構造比較

### SeasarBatis（旧）
```
jp.vemi.seasarbatis/
├── core/
│   ├── builder/        # クエリビルダー
│   ├── command/        # コマンドクラス（未実装）
│   ├── config/         # 設定
│   ├── criteria/       # Criteria API
│   ├── entity/         # エンティティ操作
│   ├── i18n/           # 国際化
│   ├── mapping/        # マッパー ★未移行
│   ├── meta/           # メタデータアノテーション
│   ├── query/          # クエリAPI
│   ├── session/        # セッション管理 ★未移行
│   ├── sql/
│   │   ├── dialect/    # DB方言 ★未移行
│   │   ├── executor/   # 実行エンジン ★未移行
│   │   ├── loader/     # SQLファイルローダー
│   │   └── processor/  # SQL処理 ★未移行
│   ├── transaction/    # トランザクション
│   └── util/           # ユーティリティ ★一部未移行
├── exception/          # 例外クラス
├── generator/          # コード生成 ★未移行
├── jdbc/               # JdbcManager ★非推奨
└── scripting/          # スクリプティング ★未移行
```

### BatisFluid（新）
```
jp.vemi.batisfluid/
├── BatisFluid.java     # エントリーポイント
├── config/             # 設定
├── core/               # コアAPI
├── entity/             # エンティティ操作
├── exception/          # 例外クラス
├── i18n/               # 国際化
├── meta/               # メタデータアノテーション
├── query/              # Criteria API
├── sql/                # SQL処理
└── transaction/        # トランザクション
```

---

## 6. 移行時の注意点

### 6.1 クラス名の変更規則
- `SB` プレフィックスを削除
- より直感的な名前に変更（例: `SBWhere` → `Where`）
- Seasar2互換名は維持（`SimpleWhere`, `ComplexWhere`）

### 6.2 パッケージ構造の変更
- フラットな構造を維持
- 機能ごとにパッケージを分割
- 内部実装クラスは非公開パッケージに配置

### 6.3 API互換性
- v0.0.2: `JdbcFlow` は内部的に `SBJdbcManager` に委譲
- v0.0.3以降: 段階的に独立実装に移行
- v0.0.3以降: `SBJdbcManager` は `@Deprecated` のまま維持し、v1.0.0で削除予定

---

## 7. 参考リンク

- [MIGRATION_GUIDE_v0.0.1_to_v0.0.2.md](../v0.0.2/reference/MIGRATION_GUIDE_v0.0.1_to_v0.0.2.md)
- [NAMING_REFACTOR_PLAN.md](../v0.0.2/reference/NAMING_REFACTOR_PLAN.md)
- [dialect-support-matrix.md](../v0.0.1/dialect-support-matrix.md)
