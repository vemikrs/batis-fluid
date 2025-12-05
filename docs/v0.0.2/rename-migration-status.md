## SeasarBatis → Batis Fluid 移行状況レポート

### 📋 移行計画の概要
NAMING_REFACTOR_PLAN.md に基づき、v0.0.2で**SeasarBatis**から**BatisFluid**へのリブランディングが進行中です。

---

## ✅ 完了済みの移行項目

### 1. アーティファクト名の変更
| 対象 | 旧 | 新 | 状態 |
|------|-----|-----|------|
| libモジュール | `jp.vemi:seasar-batis` | `jp.vemi:batis-fluid-core` | ✅ 完了 |
| springモジュール | `jp.vemi:seasar-batis-spring` | `jp.vemi:batis-fluid-spring` | ✅ 完了 |

### 2. 新パッケージ・クラスの作成（`jp.vemi.batisfluid`）

#### lib モジュール
| 新クラス | 場所 | 状態 |
|---------|------|------|
| `BatisFluid` | BatisFluid.java | ✅ 完了 |
| `JdbcFlow` | JdbcFlow.java | ✅ 完了 |
| `SqlRunner` | SqlRunner.java | ✅ 完了 |
| `OptimisticLockConfig` | OptimisticLockConfig.java | ✅ 完了 |
| `OptimisticLockConfigLoader` | OptimisticLockConfigLoader.java | ✅ 完了 |

#### spring モジュール
| 新クラス | 場所 | 状態 |
|---------|------|------|
| `BatisFluidAutoConfiguration` | BatisFluidAutoConfiguration.java | ✅ 完了 |
| `SpringJdbcFlow` | SpringJdbcFlow.java | ✅ 完了 |

### 3. テストの作成（新API用）
| テストクラス | 場所 | 状態 |
|-------------|------|------|
| `BatisFluidTest` | BatisFluidTest.java | ✅ 完了 |
| `OptimisticLockConfigLoaderTest` | OptimisticLockConfigLoaderTest.java | ✅ 完了 |

### 4. Spring Boot AutoConfiguration 設定
- spring.factories: 新旧両方を登録 ✅
- AutoConfiguration.imports: 新旧両方を登録 ✅

### 5. `@Deprecated` マーキング（後方互換レイヤー）
| クラス | 状態 |
|--------|------|
| `SBJdbcManager` | ✅ `@Deprecated(since = "0.0.2")` |
| `SBOptimisticLockConfig` | ✅ `@Deprecated(since = "0.0.2")` |
| `SBOptimisticLockConfigLoader` | ✅ `@Deprecated(since = "0.0.2")` |
| `SeasarBatisAutoConfiguration` | ✅ `@Deprecated(since = "0.0.2")` |
| `SpringJdbcManager` | ✅ `@Deprecated(since = "0.0.2")` |

---

## ⚠️ 未完了・課題のある移行項目

### 1. クラス名リネーム（大部分が未完了）

計画された全リネームに対し、**新クラスの作成は一部のみ**で、残りは旧クラスへのdelegation形式です。

| 計画 | 旧 | 新 | 状態 |
|-----|-----|-----|------|
| Facade | `SBJdbcManager` | `JdbcFlow` | ✅ （delegation形式） |
| Facade | `SBJdbcManagerFactory` | `BatisFluid` | ✅ |
| Session | `SBSqlSessionFactory` | `SqlSessionGateway` | ❌ 未作成 |
| Query | `SBSelectBuilder` | `SelectFlow` | ❌ 未作成 |
| Query | `SBUpdateBuilder` | `UpdateFlow` | ❌ 未作成 |
| Query | `SBDeleteBuilder` | `DeleteFlow` | ❌ 未作成 |
| Query | `SBSelect` | `SelectQuery` | ❌ 未作成 |
| Criteria | `SBWhere` | `Where` | ❌ 未作成 |
| SQL | `SBQueryExecutor` | `SqlRunner` | ✅（機能限定版） |
| SQL | `SBSqlFileLoader` | `SqlFileLoader` | ❌ 未作成 |
| SQL | `SBSqlParser` | `SqlParser` | ❌ 未作成 |
| SQL | `SBSqlProcessor` | `SqlProcessor` | ❌ 未作成 |
| SQL | `SBSqlFormatter` | `SqlFormatter` | ❌ 未作成 |
| SQL | `SBMyBatisSqlProcessor` | `MyBatisSqlProcessor` | ❌ 未作成 |
| SQL | `SBDialect` | `Dialect` | ❌ 未作成 |
| Mapping | `SBEntityMapper` | `EntityMapper` | ❌ 未作成 |
| Mapping | `SBMyBatisMapper` | `MyBatisMapper` | ❌ 未作成 |
| Entity | `SBEntityOperations` | `EntityOperations` | ❌ 未作成 |
| Entity | `SBPrimaryKeyInfo` | `PrimaryKeyInfo` | ❌ 未作成 |
| Entity | `SBOptimisticLockSupport` | `OptimisticLockSupport` | ❌ 未作成 |
| Meta | `@SBTableMeta` | `@FluidTable` | ❌ 未作成 |
| Meta | `@SBColumnMeta` | `@FluidColumn` | ❌ 未作成 |
| Config | `SBMyBatisConfig` | `FluidConfig` | ❌ 未作成 |
| Config | `SBOptimisticLockConfig` | `OptimisticLockConfig` | ✅ |
| Config | `SBOptimisticLockConfigLoader` | `OptimisticLockConfigLoader` | ✅ |
| i18n | `SBLocaleConfig` | `FluidLocale` | ❌ 未作成 |
| i18n | `SBMessageManager` | `Messages` | ❌ 未作成 |
| Transaction | `SBTransactionManager` | `TransactionManager` | ❌ 未作成 |
| Transaction | `SBTransactionOperation` | `TransactionOperation` | ❌ 未作成 |
| Transaction | `SBTransactionContext` | `TransactionContext` | ❌ 未作成 |
| Transaction | `SBTransactionCallback` | `TransactionCallback` | ❌ 未作成 |
| Transaction | `SBThreadLocalDataSource` | `ThreadLocalDataSource` | ❌ 未作成 |
| Exception | `SBException` 等 | `FluidException` 等 | ❌ 未作成 |

### 2. 設定ファイルのリネーム

| 旧ファイル名 | 新ファイル名 | 状態 |
|-------------|--------------|------|
| seasarbatis-optimistic-lock.properties | `batisfluid-optimistic-lock.properties` | ⚠️ 旧ファイルのみ存在（後方互換サポート有り） |
| messages.properties | messages.properties | ❌ 未移行 |
| messages_ja.properties | messages_ja.properties | ❌ 未移行 |

### 3. 設定プレフィックスの変更
| 旧プレフィックス | 新プレフィックス | 状態 |
|-----------------|-----------------|------|
| `seasarbatis.optimistic-lock.*` | `batisfluid.optimistic-lock.*` | ⚠️ 旧形式のみサポート（コードでは両対応） |

---

## 📊 移行進捗サマリー

| カテゴリ | 完了数 | 計画数 | 進捗率 |
|---------|--------|--------|--------|
| **ファサードAPI** | 2 | 3 | 67% |
| **クエリビルダー** | 0 | 8 | 0% |
| **SQL実行** | 1 | 8 | 13% |
| **エンティティ/マッピング** | 0 | 6 | 0% |
| **設定/i18n** | 2 | 6 | 33% |
| **トランザクション** | 0 | 5 | 0% |
| **例外** | 0 | 10 | 0% |
| **アノテーション** | 0 | 2 | 0% |
| **Springモジュール** | 2 | 2 | 100% |
| **設定ファイル** | 0 | 3 | 0% |
| **全体** | **7** | **53** | **約13%** |

---

## 🔄 現在のアーキテクチャ

```
新API (jp.vemi.batisfluid)          旧API (jp.vemi.seasarbatis)
┌─────────────────────┐             ┌─────────────────────┐
│ BatisFluid          │             │ SBJdbcManagerFactory│
│   ├─ jdbcFlow()     │───delegate──│                     │
│   └─ sqlRunner()    │             │ SBJdbcManager       │
├─────────────────────┤             │   ├─ from()         │
│ JdbcFlow            │───delegate──│   ├─ select()       │
│   ├─ from()         │             │   ├─ insert()       │
│   ├─ insert()       │             │   ├─ update()       │
│   └─ ...            │             │   └─ delete()       │
├─────────────────────┤             ├─────────────────────┤
│ SqlRunner           │───delegate──│ SBQueryExecutor     │
│   ├─ select()       │             │ SBSqlFileLoader     │
│   └─ ...            │             │ SBSqlParser         │
├─────────────────────┤             └─────────────────────┘
│ OptimisticLockConfig│
│ OptimisticLockConfigLoader        ← 独立実装（旧API非依存）
└─────────────────────┘
```

---

## 📝 推奨される次のステップ

1. **優先度高**: クエリビルダー系の新API作成（`SelectFlow`, `UpdateFlow`, `DeleteFlow`）
2. **優先度高**: メッセージファイルの新パッケージへの移行
3. **優先度中**: エンティティ関連クラスのリネーム（`@FluidTable`, `@FluidColumn`）
4. **優先度中**: 例外クラスの新パッケージへの移行
5. **優先度低**: トランザクション関連クラスのリネーム
6. **メンテナンス**: 旧APIへのdelegationを実装ベースに置き換え（v0.0.3以降）

