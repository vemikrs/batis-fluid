# BatisFluid 移行作業計画書

## 📋 概要

本ドキュメントは [rename-migration-status.md](rename-migration-status.md) の移行状況に基づき、SeasarBatis → BatisFluid への完全移行を達成するための詳細作業計画を定義します。

**作成日**: 2025年12月5日  
**最終更新日**: 2025年12月6日  
**対象バージョン**: v0.0.2  
**参照ドキュメント**: [NAMING_REFACTOR_PLAN.md](reference/NAMING_REFACTOR_PLAN.md)

---

## ✅ 移行完了報告

**全フェーズの移行が完了しました。**

| フェーズ | 内容 | 状態 |
|---------|------|------|
| 1 | 例外クラスの移行 | ✅ 完了 |
| 2 | アノテーションの移行 | ✅ 完了 |
| 3 | i18n/メッセージの移行 | ✅ 完了 |
| 4 | 設定ファイルの移行 | ✅ 完了 |
| 5 | エンティティ関連の移行 | ✅ 完了 |
| 6 | SQL関連の移行 | ✅ 完了 |
| 7 | クエリビルダーの移行 | ✅ 完了 |
| 8 | トランザクション関連の移行 | ✅ 完了 |
| 9 | ファサードの完成 | ✅ 完了 |
| 10 | テストカバレッジ拡充 | ✅ 完了 |

詳細な移行状況は [rename-migration-status.md](rename-migration-status.md) を参照してください。

---

## 🎯 移行方針（完了時の最終仕様）

### 基本原則
1. **後方互換性の維持**: 旧API（`SB*`クラス）は`@Deprecated(since="0.0.2")`としてv0.0.2では残存 ✅
2. **段階的移行**: delegation方式から徐々に独立実装へ移行 ✅
3. **テスト駆動**: 各新クラスに対応するテストクラスを同時作成 ✅
4. **旧API削除予定**: v0.0.3以降で`@Deprecated`クラスを削除

### 命名規則
- パッケージ: `jp.vemi.batisfluid.*` ✅
- クラス名から`SB`プレフィックスを削除 ✅
- アノテーションには`@Fluid*`プレフィックスを使用 ✅

---

## 📦 完了したフェーズ一覧

### フェーズ1: 例外クラスの移行 ✅

| # | 旧クラス | 新クラス | 状態 |
|---|---------|---------|------|
| 1-1 | `SBException` | `FluidException` | ✅ 完了 |
| 1-2 | `SBSQLException` | `FluidSqlException` | ✅ 完了 |
| 1-3 | `SBNoResultException` | `NoResultException` | ✅ 完了 |
| 1-4 | `SBNonUniqueResultException` | `NonUniqueResultException` | ✅ 完了 |
| 1-5 | `SBOptimisticLockException` | `OptimisticLockException` | ✅ 完了 |
| 1-6 | `SBSqlParseException` | `SqlParseException` | ✅ 完了 |
| 1-7 | `SBTypeConversionException` | `TypeConversionException` | ✅ 完了 |
| 1-8 | `SBEntityException` | `EntityException` | ✅ 完了 |
| 1-9 | `SBIllegalStateException` | `FluidIllegalStateException` | ✅ 完了 |
| 1-10 | `SBTransactionException` | `TransactionException` | ✅ 完了 |

### フェーズ2: アノテーションの移行 ✅

| # | 旧アノテーション | 新アノテーション | 状態 |
|---|-----------------|-----------------|------|
| 2-1 | `@SBTableMeta` | `@FluidTable` | ✅ 完了 |
| 2-2 | `@SBColumnMeta` | `@FluidColumn` | ✅ 完了 |

### フェーズ3: i18n/メッセージの移行 ✅

| # | 旧クラス/ファイル | 新クラス/ファイル | 状態 |
|---|------------------|------------------|------|
| 3-1 | `SBLocaleConfig` | `FluidLocale` | ✅ 完了 |
| 3-2 | `SBMessageManager` | `Messages` | ✅ 完了 |
| 3-3 | `jp/vemi/seasarbatis/messages.properties` | `jp/vemi/batisfluid/messages.properties` | ✅ 完了 |
| 3-4 | `jp/vemi/seasarbatis/messages_ja.properties` | `jp/vemi/batisfluid/messages_ja.properties` | ✅ 完了 |

### フェーズ4: 設定ファイルの移行 ✅

| # | 旧ファイル | 新ファイル | 状態 |
|---|-----------|-----------|------|
| 4-1 | `seasarbatis-optimistic-lock.properties` | `batisfluid-optimistic-lock.properties` | ✅ 完了 |
| 4-2 | `SBMyBatisConfig` | `FluidConfig` | ✅ 完了 |
| 4-3 | `SBOptimisticLockConfig` | `OptimisticLockConfig` | ✅ 完了 |
| 4-4 | `SBOptimisticLockConfigLoader` | `OptimisticLockConfigLoader` | ✅ 完了 |

### フェーズ5: エンティティ関連クラスの移行 ✅

| # | 旧クラス | 新クラス | 状態 |
|---|---------|---------|------|
| 5-1 | `SBEntityOperations` | `EntityOperations` | ✅ 完了 |
| 5-2 | `SBPrimaryKeyInfo` | `PrimaryKeyInfo` | ✅ 完了 |
| 5-3 | `SBOptimisticLockSupport` | `OptimisticLockSupport` | ✅ 完了 |

### フェーズ6: SQL関連クラスの移行 ✅

| # | 旧クラス | 新クラス | 状態 |
|---|---------|---------|------|
| 6-1 | `SBSqlFileLoader` | `SqlFileLoader` | ✅ 完了 |
| 6-2 | `SBSqlParser` | `SqlParser` | ✅ 完了 |
| 6-3 | `SBSqlFormatter` | `SqlFormatter` | ✅ 完了 |
| 6-4 | `ParsedSql` | `ParsedSql` | ✅ 完了 |

### フェーズ7: クエリビルダーの移行 ✅

| # | 旧クラス | 新クラス | 状態 |
|---|---------|---------|------|
| 7-1 | `SBSelectBuilder` | `SelectBuilder` | ✅ 完了 |
| 7-2 | `SBUpdateBuilder` | `UpdateBuilder` | ✅ 完了 |
| 7-3 | `SBDeleteBuilder` | `DeleteBuilder` | ✅ 完了 |
| 7-4 | `SBSqlBuilder` | `SqlBuilder` | ✅ 完了 |
| 7-5 | `SBWhere` | `Where` | ✅ 完了 |
| 7-6 | `SimpleWhere` | `SimpleWhere` | ✅ 完了 |
| 7-7 | `ComplexWhere` | `ComplexWhere` | ✅ 完了 |
| 7-8 | `AbstractWhere` | `AbstractWhere` | ✅ 完了 |
| 7-9 | `SBOrderByCapable` | `OrderByCapable` | ✅ 完了 |
| 7-10 | `SBWhereCapable` | `WhereCapable` | ✅ 完了 |
| 7-11 | - | `OrderDirection` | ✅ 完了 |

### フェーズ8: トランザクション関連の移行 ✅

| # | 旧クラス | 新クラス | 状態 |
|---|---------|---------|------|
| 8-1 | `SBTransactionManager` | `TransactionManager` | ✅ 完了 |
| 8-2 | `SBTransactionOperation` | `TransactionOperation` | ✅ 完了 |
| 8-3 | `SBTransactionContext` | `TransactionContext` | ✅ 完了 |
| 8-4 | `SBTransactionCallback` | `TransactionCallback` | ✅ 完了 |
| 8-5 | `SBThreadLocalDataSource` | `ThreadLocalDataSource` | ✅ 完了 |
| 8-6 | - | `PropagationType` | ✅ 完了 |

### フェーズ9: ファサードの完成 ✅

| # | 旧クラス | 新クラス | 状態 |
|---|---------|---------|------|
| 9-1 | `SBJdbcManagerFactory` | `BatisFluid` | ✅ 完了 |
| 9-2 | `SBJdbcManager` | `JdbcFlow` | ✅ 完了 |
| 9-3 | `SBQueryExecutor` | `SqlRunner` | ✅ 完了 |

### フェーズ10: テストカバレッジ拡充 ✅

BatisFluidパッケージに18個のテストクラスを作成：

| テストクラス | 対象 | 状態 |
|-------------|------|------|
| `BatisFluidTest` | ファクトリ | ✅ 完了 |
| `FluidConfigTest` | 設定 | ✅ 完了 |
| `OptimisticLockConfigLoaderTest` | 設定ローダー | ✅ 完了 |
| `JdbcFlowTest` | JDBC操作 | ✅ 完了 |
| `SqlRunnerTest` | SQL実行 | ✅ 完了 |
| `EntityOperationsTest` | エンティティ操作 | ✅ 完了 |
| `OptimisticLockSupportTest` | 楽観的ロック | ✅ 完了 |
| `PrimaryKeyInfoTest` | 主キー情報 | ✅ 完了 |
| `ExceptionTest` | 例外 | ✅ 完了 |
| `I18nTest` | 国際化 | ✅ 完了 |
| `MetaAnnotationTest` | アノテーション | ✅ 完了 |
| `QueryBuilderTest` | クエリビルダー全般 | ✅ 完了 |
| `SelectBuilderTest` | SELECT | ✅ 完了 |
| `UpdateBuilderTest` | UPDATE | ✅ 完了 |
| `DeleteBuilderTest` | DELETE | ✅ 完了 |
| `SqlTest` | SQL処理 | ✅ 完了 |
| `TransactionTest` | トランザクション全般 | ✅ 完了 |
| `TransactionManagerTest` | トランザクション管理 | ✅ 完了 |

---

## ✅ 完了基準（達成済み）

### 各フェーズの完了条件
1. ✅ 新クラスが作成され、コンパイルエラーがないこと
2. ✅ 旧クラスに`@Deprecated`アノテーションが付与されていること
3. ✅ 対応するユニットテストが作成され、パスすること
4. ✅ Javadocが日本語で記述されていること

### 全体の完了条件
1. ✅ `./gradlew clean check`がパスすること
2. ✅ 統合テスト（H2）がパスすること
3. ✅ 移行状況ドキュメントが100%完了に更新されていること
4. ⚠️ CHANGELOGが更新されていること（要確認）

---

## 📁 最終パッケージ構造
---

## 📁 最終パッケージ構造

```
jp.vemi.batisfluid/
├── BatisFluid.java                 # エントリーポイント ✅
├── config/
│   ├── FluidConfig.java            ✅
│   ├── OptimisticLockConfig.java   ✅
│   └── OptimisticLockConfigLoader.java ✅
├── core/
│   ├── JdbcFlow.java               ✅
│   └── SqlRunner.java              ✅
├── entity/
│   ├── EntityOperations.java       ✅
│   ├── PrimaryKeyInfo.java         ✅
│   └── OptimisticLockSupport.java  ✅
├── exception/
│   ├── FluidException.java         ✅
│   ├── FluidSqlException.java      ✅
│   ├── FluidIllegalStateException.java ✅
│   ├── EntityException.java        ✅
│   ├── TransactionException.java   ✅
│   ├── OptimisticLockException.java ✅
│   ├── SqlParseException.java      ✅
│   ├── NoResultException.java      ✅
│   ├── NonUniqueResultException.java ✅
│   └── TypeConversionException.java ✅
├── i18n/
│   ├── FluidLocale.java            ✅
│   └── Messages.java               ✅
├── meta/
│   ├── FluidTable.java             ✅ (@interface)
│   └── FluidColumn.java            ✅ (@interface)
├── query/
│   ├── SelectBuilder.java          ✅
│   ├── UpdateBuilder.java          ✅
│   ├── DeleteBuilder.java          ✅
│   ├── SqlBuilder.java             ✅
│   ├── Where.java                  ✅
│   ├── SimpleWhere.java            ✅
│   ├── ComplexWhere.java           ✅
│   ├── AbstractWhere.java          ✅
│   ├── WhereCapable.java           ✅
│   ├── OrderByCapable.java         ✅
│   └── OrderDirection.java         ✅
├── sql/
│   ├── SqlFileLoader.java          ✅
│   ├── SqlParser.java              ✅
│   ├── SqlFormatter.java           ✅
│   └── ParsedSql.java              ✅
└── transaction/
    ├── TransactionManager.java     ✅
    ├── TransactionOperation.java   ✅
    ├── TransactionContext.java     ✅
    ├── TransactionCallback.java    ✅
    ├── ThreadLocalDataSource.java  ✅
    └── PropagationType.java        ✅

jp.vemi.batisfluid.spring/
├── config/
│   └── BatisFluidAutoConfiguration.java ✅
└── core/
    └── SpringJdbcFlow.java         ✅
```

### 移行時のコーディング規約（適用済み）
- 著作権表記: `Copyright (C) 2025 VEMI, All Rights Reserved.`
- バージョン: `@version 0.0.2`
- Javadoc: 日本語で記述
- クラス名に`SB`プレフィックスを使用しない

---

## 🔗 関連ドキュメント

- [NAMING_REFACTOR_PLAN.md](reference/NAMING_REFACTOR_PLAN.md) - 命名規則計画
- [rename-migration-status.md](rename-migration-status.md) - 移行状況レポート
- [CHANGELOG.md](reference/CHANGELOG.md) - 変更履歴
- [MIGRATION_GUIDE_v0.0.1_to_v0.0.2.md](reference/MIGRATION_GUIDE_v0.0.1_to_v0.0.2.md) - 移行ガイド

---

*Powered by GitHub Copilot*
