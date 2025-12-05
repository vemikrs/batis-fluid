# BatisFluid 移行作業計画書

## 📋 概要

本ドキュメントは [rename-migration-status.md](rename-migration-status.md) の移行状況に基づき、SeasarBatis → BatisFluid への完全移行を達成するための詳細作業計画を定義します。

**作成日**: 2025年12月5日  
**対象バージョン**: v0.0.2  
**参照ドキュメント**: [NAMING_REFACTOR_PLAN.md](reference/NAMING_REFACTOR_PLAN.md)

---

## 🎯 移行方針

### 基本原則
1. **後方互換性の維持**: 旧API（`SB*`クラス）は`@Deprecated(since="0.0.2")`としてv0.0.2では残存
2. **段階的移行**: delegation方式から徐々に独立実装へ移行
3. **テスト駆動**: 各新クラスに対応するテストクラスを同時作成
4. **旧API削除予定**: v0.0.3以降で`@Deprecated`クラスを削除

### 命名規則
- パッケージ: `jp.vemi.batisfluid.*`
- クラス名から`SB`プレフィックスを削除
- fluent APIには`*Flow`サフィックスを使用
- アノテーションには`@Fluid*`プレフィックスを使用

---

## 📦 フェーズ1: 例外クラスの移行（優先度: 高）

### 理由
他のすべてのクラスが依存する基盤となるため、最初に移行する必要があります。

### 作業一覧

| # | 旧クラス | 新クラス | 新パッケージ | 作業内容 |
|---|---------|---------|-------------|---------|
| 1-1 | `SBException` | `FluidException` | `jp.vemi.batisfluid.exception` | 新規作成（継承元） |
| 1-2 | `SBSQLException` | `FluidSqlException` | `jp.vemi.batisfluid.exception` | 新規作成 |
| 1-3 | `SBNoResultException` | `NoResultException` | `jp.vemi.batisfluid.exception` | 新規作成 |
| 1-4 | `SBNonUniqueResultException` | `NonUniqueResultException` | `jp.vemi.batisfluid.exception` | 新規作成 |
| 1-5 | `SBOptimisticLockException` | `OptimisticLockException` | `jp.vemi.batisfluid.exception` | 新規作成 |
| 1-6 | `SBSqlParseException` | `SqlParseException` | `jp.vemi.batisfluid.exception` | 新規作成 |
| 1-7 | `SBTypeConversionException` | `TypeConversionException` | `jp.vemi.batisfluid.exception` | 新規作成 |
| 1-8 | `SBEntityException` | `EntityException` | `jp.vemi.batisfluid.exception` | 新規作成 |
| 1-9 | `SBIllegalStateException` | `FluidIllegalStateException` | `jp.vemi.batisfluid.exception` | 新規作成 |
| 1-10 | `SBTransactionException` | `TransactionException` | `jp.vemi.batisfluid.exception` | 新規作成 |

### 旧クラス対応
- 各旧クラスに`@Deprecated(since="0.0.2")`を付与
- Javadocに移行先クラスへの参照を追加

### テスト
- `jp.vemi.batisfluid.exception`パッケージにテストクラスを作成

### 推定工数
- 作業時間: 2-3時間
- テスト作成: 1時間

---

## 📦 フェーズ2: アノテーションの移行（優先度: 高）

### 理由
エンティティクラスで使用され、他の移行作業の基盤となります。

### 作業一覧

| # | 旧アノテーション | 新アノテーション | 新パッケージ |
|---|-----------------|-----------------|-------------|
| 2-1 | `@SBTableMeta` | `@FluidTable` | `jp.vemi.batisfluid.meta` |
| 2-2 | `@SBColumnMeta` | `@FluidColumn` | `jp.vemi.batisfluid.meta` |

### 作業詳細
```java
// 新アノテーション例
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FluidTable {
    String name() default "";
    String schema() default "";
}
```

### 旧アノテーション対応
- `@Deprecated(since="0.0.2")`を付与
- 新アノテーションへのエイリアスを検討

### 推定工数
- 作業時間: 1時間
- テスト作成: 30分

---

## 📦 フェーズ3: i18n/メッセージの移行（優先度: 高）

### 理由
例外メッセージ等で使用されるため、早期に移行が必要です。

### 作業一覧

| # | 旧クラス/ファイル | 新クラス/ファイル | 作業内容 |
|---|------------------|------------------|---------|
| 3-1 | `SBLocaleConfig` | `FluidLocale` | 新規作成 |
| 3-2 | `SBMessageManager` | `Messages` | 新規作成 |
| 3-3 | `jp/vemi/seasarbatis/messages.properties` | `jp/vemi/batisfluid/messages.properties` | 新規作成（コピー＆修正） |
| 3-4 | `jp/vemi/seasarbatis/messages_ja.properties` | `jp/vemi/batisfluid/messages_ja.properties` | 新規作成（コピー＆修正） |

### メッセージファイルの内容更新
```properties
# 旧
seasarbatis.error.xxx=...

# 新
batisfluid.error.xxx=...
```

### 推定工数
- 作業時間: 1.5時間
- テスト作成: 30分

---

## 📦 フェーズ4: 設定ファイルの移行（優先度: 中）

### 作業一覧

| # | 旧ファイル | 新ファイル | 作業内容 |
|---|-----------|-----------|---------|
| 4-1 | `seasarbatis-optimistic-lock.properties` | `batisfluid-optimistic-lock.properties` | 新規作成（新プレフィックス対応） |
| 4-2 | `SBMyBatisConfig` | `FluidConfig` | 新規作成 |

### 設定ファイルの内容
```properties
# 新形式
batisfluid.optimistic-lock.enabled=true
batisfluid.optimistic-lock.default-type=NONE
```

### 後方互換性
- `OptimisticLockConfigLoader`は既に両形式をサポート済み

### 推定工数
- 作業時間: 1時間
- テスト作成: 30分

---

## 📦 フェーズ5: エンティティ関連クラスの移行（優先度: 中）

### 作業一覧

| # | 旧クラス | 新クラス | 新パッケージ |
|---|---------|---------|-------------|
| 5-1 | `SBEntityOperations` | `EntityOperations` | `jp.vemi.batisfluid.entity` |
| 5-2 | `SBPrimaryKeyInfo` | `PrimaryKeyInfo` | `jp.vemi.batisfluid.entity` |
| 5-3 | `SBOptimisticLockSupport` | `OptimisticLockSupport` | `jp.vemi.batisfluid.entity` |
| 5-4 | `SBEntityMapper` | `EntityMapper` | `jp.vemi.batisfluid.mapping` |
| 5-5 | `SBMyBatisMapper` | `MyBatisMapper` | `jp.vemi.batisfluid.mapping` |

### 推定工数
- 作業時間: 3時間
- テスト作成: 2時間

---

## 📦 フェーズ6: SQL関連クラスの移行（優先度: 中）

### 作業一覧

| # | 旧クラス | 新クラス | 新パッケージ |
|---|---------|---------|-------------|
| 6-1 | `SBSqlFileLoader` | `SqlFileLoader` | `jp.vemi.batisfluid.sql` |
| 6-2 | `SBSqlParser` | `SqlParser` | `jp.vemi.batisfluid.sql` |
| 6-3 | `SBSqlProcessor` | `SqlProcessor` | `jp.vemi.batisfluid.sql` |
| 6-4 | `SBSqlFormatter` | `SqlFormatter` | `jp.vemi.batisfluid.sql` |
| 6-5 | `SBMyBatisSqlProcessor` | `MyBatisSqlProcessor` | `jp.vemi.batisfluid.sql` |
| 6-6 | `SBDialect` | `Dialect` | `jp.vemi.batisfluid.sql.dialect` |
| 6-7 | `SBQueryExecutor` | - | `SqlRunner`で既に対応（拡張検討） |

### 推定工数
- 作業時間: 4時間
- テスト作成: 3時間

---

## 📦 フェーズ7: クエリビルダーの移行（優先度: 高）

### 理由
ユーザーが最も頻繁に使用するAPIのため、早期に完成させるべきです。

### 作業一覧

| # | 旧クラス | 新クラス | 新パッケージ |
|---|---------|---------|-------------|
| 7-1 | `SBSelectBuilder` | `SelectFlow` | `jp.vemi.batisfluid.query` |
| 7-2 | `SBUpdateBuilder` | `UpdateFlow` | `jp.vemi.batisfluid.query` |
| 7-3 | `SBDeleteBuilder` | `DeleteFlow` | `jp.vemi.batisfluid.query` |
| 7-4 | `SBSelect` | `SelectQuery` | `jp.vemi.batisfluid.query` |
| 7-5 | `SBWhere` | `Where` | `jp.vemi.batisfluid.criteria` |
| 7-6 | `SBOrderByCapable` | `OrderByCapable` | `jp.vemi.batisfluid.criteria` |
| 7-7 | `SBWhereCapable` | `WhereCapable` | `jp.vemi.batisfluid.criteria` |
| 7-8 | `SBSqlBuilder` | `SqlBuilder` | `jp.vemi.batisfluid.query` |

### 注意事項
- `SimpleWhere`, `ComplexWhere`, `AbstractWhere`はリネーム不要（計画通り）
- ただし新パッケージへのコピーを検討

### 推定工数
- 作業時間: 5時間
- テスト作成: 4時間

---

## 📦 フェーズ8: トランザクション関連の移行（優先度: 低）

### 理由
内部実装であり、ユーザーが直接触れる機会が少ないため後回し。

### 作業一覧

| # | 旧クラス | 新クラス | 新パッケージ |
|---|---------|---------|-------------|
| 8-1 | `SBTransactionManager` | `TransactionManager` | `jp.vemi.batisfluid.transaction` |
| 8-2 | `SBTransactionOperation` | `TransactionOperation` | `jp.vemi.batisfluid.transaction` |
| 8-3 | `SBTransactionContext` | `TransactionContext` | `jp.vemi.batisfluid.transaction` |
| 8-4 | `SBTransactionCallback` | `TransactionCallback` | `jp.vemi.batisfluid.transaction` |
| 8-5 | `SBThreadLocalDataSource` | `ThreadLocalDataSource` | `jp.vemi.batisfluid.transaction` |

### 推定工数
- 作業時間: 3時間
- テスト作成: 2時間

---

## 📦 フェーズ9: ファサードの完成（優先度: 中）

### 作業一覧

| # | 旧クラス | 新クラス | 作業内容 |
|---|---------|---------|---------|
| 9-1 | `SBSqlSessionFactory` | `SqlSessionGateway` | 新規作成（オプション） |
| 9-2 | `JdbcFlow` | - | delegation除去、独立実装化 |
| 9-3 | `SqlRunner` | - | 機能拡張（全SQLメソッド対応） |

### 推定工数
- 作業時間: 3時間
- テスト作成: 2時間

---

## 📅 実行スケジュール（推奨順序）

### Week 1: 基盤レイヤー
| 日 | フェーズ | 作業内容 | 推定時間 |
|---|--------|---------|---------|
| Day 1 | 1 | 例外クラスの移行 | 3時間 |
| Day 2 | 2 | アノテーションの移行 | 1.5時間 |
| Day 2 | 3 | i18n/メッセージの移行 | 2時間 |
| Day 3 | 4 | 設定ファイルの移行 | 1.5時間 |

### Week 2: コア機能
| 日 | フェーズ | 作業内容 | 推定時間 |
|---|--------|---------|---------|
| Day 4 | 5 | エンティティ関連の移行 | 5時間 |
| Day 5 | 6 | SQL関連の移行 | 7時間 |

### Week 3: ユーザー向けAPI
| 日 | フェーズ | 作業内容 | 推定時間 |
|---|--------|---------|---------|
| Day 6-7 | 7 | クエリビルダーの移行 | 9時間 |
| Day 8 | 8 | トランザクション関連の移行 | 5時間 |
| Day 9 | 9 | ファサードの完成 | 5時間 |

### Week 4: 統合・検証
| 日 | 作業内容 | 推定時間 |
|---|---------|---------|
| Day 10 | 統合テスト実行 | 4時間 |
| Day 11 | ドキュメント更新 | 3時間 |
| Day 12 | コードレビュー・修正 | 4時間 |

---

## ✅ 完了基準

### 各フェーズの完了条件
1. 新クラスが作成され、コンパイルエラーがないこと
2. 旧クラスに`@Deprecated`アノテーションが付与されていること
3. 対応するユニットテストが作成され、パスすること
4. Javadocが日本語で記述されていること

### 全体の完了条件
1. `./gradlew clean check`がパスすること
2. 統合テスト（H2）がパスすること
3. 移行状況ドキュメントが100%完了に更新されていること
4. CHANGELOGが更新されていること

---

## 📝 注意事項

### パッケージ構造（新）
```
jp.vemi.batisfluid/
├── BatisFluid.java                 # エントリーポイント
├── config/
│   ├── FluidConfig.java
│   ├── OptimisticLockConfig.java   ✅ 作成済み
│   └── OptimisticLockConfigLoader.java ✅ 作成済み
├── core/
│   ├── JdbcFlow.java               ✅ 作成済み
│   └── SqlRunner.java              ✅ 作成済み
├── criteria/
│   ├── Where.java
│   ├── SimpleWhere.java            (コピー)
│   ├── ComplexWhere.java           (コピー)
│   ├── AbstractWhere.java          (コピー)
│   ├── OrderByCapable.java
│   └── WhereCapable.java
├── entity/
│   ├── EntityOperations.java
│   ├── PrimaryKeyInfo.java
│   └── OptimisticLockSupport.java
├── exception/
│   ├── FluidException.java
│   ├── FluidSqlException.java
│   ├── NoResultException.java
│   ├── NonUniqueResultException.java
│   ├── OptimisticLockException.java
│   ├── SqlParseException.java
│   ├── TypeConversionException.java
│   ├── EntityException.java
│   ├── FluidIllegalStateException.java
│   └── TransactionException.java
├── i18n/
│   ├── FluidLocale.java
│   └── Messages.java
├── mapping/
│   ├── EntityMapper.java
│   └── MyBatisMapper.java
├── meta/
│   ├── FluidTable.java             (@interface)
│   └── FluidColumn.java            (@interface)
├── query/
│   ├── SelectFlow.java
│   ├── UpdateFlow.java
│   ├── DeleteFlow.java
│   ├── SelectQuery.java
│   └── SqlBuilder.java
├── sql/
│   ├── SqlFileLoader.java
│   ├── SqlParser.java
│   ├── SqlProcessor.java
│   ├── SqlFormatter.java
│   ├── MyBatisSqlProcessor.java
│   └── dialect/
│       └── Dialect.java
└── transaction/
    ├── TransactionManager.java
    ├── TransactionOperation.java
    ├── TransactionContext.java
    ├── TransactionCallback.java
    └── ThreadLocalDataSource.java
```

### 移行時のコーディング規約
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
