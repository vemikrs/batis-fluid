## SeasarBatis → BatisFluid 移行状況レポート

### 📋 移行計画の概要
NAMING_REFACTOR_PLAN.md に基づき、v0.0.2で**SeasarBatis**から**BatisFluid**へのリブランディングが完了しました。

**最終更新日: 2025年12月6日**

---

## ✅ 完了済みの移行項目

### 1. アーティファクト名の変更
| 対象 | 旧 | 新 | 状態 |
|------|-----|-----|------|
| libモジュール | `jp.vemi:seasar-batis` | `jp.vemi:batis-fluid-core` | ✅ 完了 |
| springモジュール | `jp.vemi:seasar-batis-spring` | `jp.vemi:batis-fluid-spring` | ✅ 完了 |

### 2. 新パッケージ・クラスの作成（`jp.vemi.batisfluid`）

#### 2.1 lib モジュール - コアクラス
| 新クラス | パッケージ | 状態 |
|---------|----------|------|
| `BatisFluid` | `jp.vemi.batisfluid` | ✅ 完了 |
| `JdbcFlow` | `jp.vemi.batisfluid.core` | ✅ 完了 |
| `SqlRunner` | `jp.vemi.batisfluid.core` | ✅ 完了 |

#### 2.2 lib モジュール - 設定クラス
| 新クラス | パッケージ | 状態 |
|---------|----------|------|
| `FluidConfig` | `jp.vemi.batisfluid.config` | ✅ 完了 |
| `OptimisticLockConfig` | `jp.vemi.batisfluid.config` | ✅ 完了 |
| `OptimisticLockConfigLoader` | `jp.vemi.batisfluid.config` | ✅ 完了 |

#### 2.3 lib モジュール - エンティティ関連
| 新クラス | パッケージ | 状態 |
|---------|----------|------|
| `EntityOperations` | `jp.vemi.batisfluid.entity` | ✅ 完了 |
| `OptimisticLockSupport` | `jp.vemi.batisfluid.entity` | ✅ 完了 |
| `PrimaryKeyInfo` | `jp.vemi.batisfluid.entity` | ✅ 完了 |

#### 2.4 lib モジュール - クエリビルダー
| 新クラス | パッケージ | 状態 |
|---------|----------|------|
| `SelectBuilder` | `jp.vemi.batisfluid.query` | ✅ 完了 |
| `UpdateBuilder` | `jp.vemi.batisfluid.query` | ✅ 完了 |
| `DeleteBuilder` | `jp.vemi.batisfluid.query` | ✅ 完了 |
| `SqlBuilder` | `jp.vemi.batisfluid.query` | ✅ 完了 |
| `Where` (インターフェース) | `jp.vemi.batisfluid.query` | ✅ 完了 |
| `SimpleWhere` | `jp.vemi.batisfluid.query` | ✅ 完了 |
| `ComplexWhere` | `jp.vemi.batisfluid.query` | ✅ 完了 |
| `AbstractWhere` | `jp.vemi.batisfluid.query` | ✅ 完了 |
| `WhereCapable` | `jp.vemi.batisfluid.query` | ✅ 完了 |
| `OrderByCapable` | `jp.vemi.batisfluid.query` | ✅ 完了 |
| `OrderDirection` | `jp.vemi.batisfluid.query` | ✅ 完了 |

#### 2.5 lib モジュール - SQL処理
| 新クラス | パッケージ | 状態 |
|---------|----------|------|
| `SqlFileLoader` | `jp.vemi.batisfluid.sql` | ✅ 完了 |
| `SqlParser` | `jp.vemi.batisfluid.sql` | ✅ 完了 |
| `SqlFormatter` | `jp.vemi.batisfluid.sql` | ✅ 完了 |
| `ParsedSql` | `jp.vemi.batisfluid.sql` | ✅ 完了 |

#### 2.6 lib モジュール - トランザクション
| 新クラス | パッケージ | 状態 |
|---------|----------|------|
| `TransactionManager` | `jp.vemi.batisfluid.transaction` | ✅ 完了 |
| `TransactionOperation` | `jp.vemi.batisfluid.transaction` | ✅ 完了 |
| `TransactionContext` | `jp.vemi.batisfluid.transaction` | ✅ 完了 |
| `TransactionCallback` | `jp.vemi.batisfluid.transaction` | ✅ 完了 |
| `ThreadLocalDataSource` | `jp.vemi.batisfluid.transaction` | ✅ 完了 |
| `PropagationType` | `jp.vemi.batisfluid.transaction` | ✅ 完了 |

#### 2.7 lib モジュール - i18n（国際化）
| 新クラス | パッケージ | 状態 |
|---------|----------|------|
| `FluidLocale` | `jp.vemi.batisfluid.i18n` | ✅ 完了 |
| `Messages` | `jp.vemi.batisfluid.i18n` | ✅ 完了 |

#### 2.8 lib モジュール - メタアノテーション
| 新クラス | パッケージ | 状態 |
|---------|----------|------|
| `@FluidTable` | `jp.vemi.batisfluid.meta` | ✅ 完了 |
| `@FluidColumn` | `jp.vemi.batisfluid.meta` | ✅ 完了 |

#### 2.9 lib モジュール - 例外クラス
| 新クラス | パッケージ | 状態 |
|---------|----------|------|
| `FluidException` | `jp.vemi.batisfluid.exception` | ✅ 完了 |
| `FluidSqlException` | `jp.vemi.batisfluid.exception` | ✅ 完了 |
| `FluidIllegalStateException` | `jp.vemi.batisfluid.exception` | ✅ 完了 |
| `EntityException` | `jp.vemi.batisfluid.exception` | ✅ 完了 |
| `TransactionException` | `jp.vemi.batisfluid.exception` | ✅ 完了 |
| `OptimisticLockException` | `jp.vemi.batisfluid.exception` | ✅ 完了 |
| `SqlParseException` | `jp.vemi.batisfluid.exception` | ✅ 完了 |
| `NoResultException` | `jp.vemi.batisfluid.exception` | ✅ 完了 |
| `NonUniqueResultException` | `jp.vemi.batisfluid.exception` | ✅ 完了 |
| `TypeConversionException` | `jp.vemi.batisfluid.exception` | ✅ 完了 |

#### 2.10 spring モジュール
| 新クラス | パッケージ | 状態 |
|---------|----------|------|
| `BatisFluidAutoConfiguration` | `jp.vemi.batisfluid.spring.config` | ✅ 完了 |
| `SpringJdbcFlow` | `jp.vemi.batisfluid.spring.core` | ✅ 完了 |

### 3. テストの作成（新API用）

#### 3.1 BatisFluid パッケージテスト（計18クラス）
| テストクラス | パッケージ | 状態 |
|-------------|----------|------|
| `BatisFluidTest` | `jp.vemi.batisfluid` | ✅ 完了 |
| `FluidConfigTest` | `jp.vemi.batisfluid.config` | ✅ 完了 |
| `OptimisticLockConfigLoaderTest` | `jp.vemi.batisfluid.config` | ✅ 完了 |
| `JdbcFlowTest` | `jp.vemi.batisfluid.core` | ✅ 完了 |
| `SqlRunnerTest` | `jp.vemi.batisfluid.core` | ✅ 完了 |
| `EntityOperationsTest` | `jp.vemi.batisfluid.entity` | ✅ 完了 |
| `OptimisticLockSupportTest` | `jp.vemi.batisfluid.entity` | ✅ 完了 |
| `PrimaryKeyInfoTest` | `jp.vemi.batisfluid.entity` | ✅ 完了 |
| `ExceptionTest` | `jp.vemi.batisfluid.exception` | ✅ 完了 |
| `I18nTest` | `jp.vemi.batisfluid.i18n` | ✅ 完了 |
| `MetaAnnotationTest` | `jp.vemi.batisfluid.meta` | ✅ 完了 |
| `QueryBuilderTest` | `jp.vemi.batisfluid.query` | ✅ 完了 |
| `SelectBuilderTest` | `jp.vemi.batisfluid.query` | ✅ 完了 |
| `UpdateBuilderTest` | `jp.vemi.batisfluid.query` | ✅ 完了 |
| `DeleteBuilderTest` | `jp.vemi.batisfluid.query` | ✅ 完了 |
| `SqlTest` | `jp.vemi.batisfluid.sql` | ✅ 完了 |
| `TransactionTest` | `jp.vemi.batisfluid.transaction` | ✅ 完了 |
| `TransactionManagerTest` | `jp.vemi.batisfluid.transaction` | ✅ 完了 |

### 4. 設定ファイルの移行
| 旧ファイル | 新ファイル | 状態 |
|-----------|-----------|------|
| `seasarbatis-optimistic-lock.properties` | `batisfluid-optimistic-lock.properties` | ✅ 両方存在（後方互換） |
| `jp/vemi/seasarbatis/messages.properties` | `jp/vemi/batisfluid/messages.properties` | ✅ 両方存在（後方互換） |
| `jp/vemi/seasarbatis/messages_ja.properties` | `jp/vemi/batisfluid/messages_ja.properties` | ✅ 両方存在（後方互換） |

### 5. Spring Boot AutoConfiguration 設定
| 設定ファイル | 内容 | 状態 |
|-------------|------|------|
| `spring.factories` | 新旧両方を登録 | ✅ 完了 |
| `AutoConfiguration.imports` | 新旧両方を登録 | ✅ 完了 |

### 6. `@Deprecated` マーキング（後方互換レイヤー）
| クラス | 状態 |
|--------|------|
| `SBJdbcManager` | ✅ `@Deprecated(since = "0.0.2")` |
| `SBOptimisticLockConfig` | ✅ `@Deprecated(since = "0.0.2")` |
| `SBOptimisticLockConfigLoader` | ✅ `@Deprecated(since = "0.0.2")` |
| `SeasarBatisAutoConfiguration` | ✅ `@Deprecated(since = "0.0.2")` |
| `SpringJdbcManager` | ✅ `@Deprecated(since = "0.0.2")` |

---

## 📊 移行進捗サマリー

| カテゴリ | 完了数 | 計画数 | 進捗率 |
|---------|--------|--------|--------|
| **ファサードAPI** | 3 | 3 | 100% |
| **クエリビルダー** | 11 | 11 | 100% |
| **SQL処理** | 4 | 4 | 100% |
| **エンティティ** | 3 | 3 | 100% |
| **設定** | 3 | 3 | 100% |
| **i18n** | 2 | 2 | 100% |
| **トランザクション** | 6 | 6 | 100% |
| **例外** | 10 | 10 | 100% |
| **メタアノテーション** | 2 | 2 | 100% |
| **Springモジュール** | 2 | 2 | 100% |
| **設定ファイル** | 3 | 3 | 100% |
| **テストクラス** | 18 | 18 | 100% |
| **全体** | **67** | **67** | **100%** |

---

## 🔄 現在のアーキテクチャ

```
新API (jp.vemi.batisfluid)          旧API (jp.vemi.seasarbatis) [@Deprecated]
┌─────────────────────────┐         ┌─────────────────────────┐
│ BatisFluid              │         │ SBJdbcManagerFactory    │
│   ├─ jdbcFlow()         │←───────→│                         │
│   └─ sqlRunner()        │         │ SBJdbcManager           │
├─────────────────────────┤         └─────────────────────────┘
│ JdbcFlow                │
│   ├─ from()             │
│   ├─ insert()           │
│   ├─ update()           │
│   └─ delete()           │
├─────────────────────────┤
│ SqlRunner               │
│   ├─ select()           │
│   └─ execute()          │
├─────────────────────────┤
│ Query Builders          │
│   ├─ SelectBuilder      │
│   ├─ UpdateBuilder      │
│   ├─ DeleteBuilder      │
│   ├─ Where / SimpleWhere│
│   └─ ComplexWhere       │
├─────────────────────────┤
│ SQL Processing          │
│   ├─ SqlFileLoader      │
│   ├─ SqlParser          │
│   ├─ SqlFormatter       │
│   └─ ParsedSql          │
├─────────────────────────┤
│ Transaction             │
│   ├─ TransactionManager │
│   ├─ TransactionOperation│
│   └─ TransactionContext │
├─────────────────────────┤
│ Entity                  │
│   ├─ EntityOperations   │
│   ├─ PrimaryKeyInfo     │
│   └─ OptimisticLockSupport│
├─────────────────────────┤
│ Config                  │
│   ├─ FluidConfig        │
│   ├─ OptimisticLockConfig│
│   └─ OptimisticLockConfigLoader│
├─────────────────────────┤
│ i18n                    │
│   ├─ FluidLocale        │
│   └─ Messages           │
├─────────────────────────┤
│ Meta Annotations        │
│   ├─ @FluidTable        │
│   └─ @FluidColumn       │
├─────────────────────────┤
│ Exceptions (10 classes) │
│   ├─ FluidException     │
│   ├─ FluidSqlException  │
│   └─ ...                │
└─────────────────────────┘
```

---

## 📁 パッケージ構成

```
jp.vemi.batisfluid/
├── BatisFluid.java                 # ファクトリクラス
├── config/
│   ├── FluidConfig.java
│   ├── OptimisticLockConfig.java
│   └── OptimisticLockConfigLoader.java
├── core/
│   ├── JdbcFlow.java
│   └── SqlRunner.java
├── entity/
│   ├── EntityOperations.java
│   ├── OptimisticLockSupport.java
│   └── PrimaryKeyInfo.java
├── exception/
│   ├── FluidException.java
│   ├── FluidSqlException.java
│   ├── FluidIllegalStateException.java
│   ├── EntityException.java
│   ├── TransactionException.java
│   ├── OptimisticLockException.java
│   ├── SqlParseException.java
│   ├── NoResultException.java
│   ├── NonUniqueResultException.java
│   └── TypeConversionException.java
├── i18n/
│   ├── FluidLocale.java
│   └── Messages.java
├── meta/
│   ├── FluidTable.java
│   └── FluidColumn.java
├── query/
│   ├── SelectBuilder.java
│   ├── UpdateBuilder.java
│   ├── DeleteBuilder.java
│   ├── SqlBuilder.java
│   ├── Where.java
│   ├── SimpleWhere.java
│   ├── ComplexWhere.java
│   ├── AbstractWhere.java
│   ├── WhereCapable.java
│   ├── OrderByCapable.java
│   └── OrderDirection.java
├── sql/
│   ├── SqlFileLoader.java
│   ├── SqlParser.java
│   ├── SqlFormatter.java
│   └── ParsedSql.java
└── transaction/
    ├── TransactionManager.java
    ├── TransactionOperation.java
    ├── TransactionContext.java
    ├── TransactionCallback.java
    ├── ThreadLocalDataSource.java
    └── PropagationType.java

jp.vemi.batisfluid.spring/
├── config/
│   └── BatisFluidAutoConfiguration.java
└── core/
    └── SpringJdbcFlow.java
```

---

## 📝 v0.0.2 リリースノート

### 新機能
- **BatisFluid** ブランドへの完全移行
- 新しい `jp.vemi.batisfluid` パッケージ階層
- 全ての主要クラスの新API提供
- 18個のテストクラスによる包括的なテストカバレッジ

### 後方互換性
- 旧 `jp.vemi.seasarbatis` パッケージは `@Deprecated` としてマークされ、引き続き利用可能
- 旧設定ファイル（`seasarbatis-*`）も引き続きサポート
- Spring Boot AutoConfiguration は新旧両方のクラスを登録

### 移行ガイド
1. 依存関係を `jp.vemi:batis-fluid-core:0.0.2` に変更
2. import文を `jp.vemi.batisfluid.*` に変更
3. 設定ファイルを `batisfluid-*` 形式に変更（任意）

---

## ⚠️ 今後の計画（v0.0.3以降）

1. **旧APIの削除準備**: 移行期間終了後に `@Deprecated` クラスを削除予定
2. **追加機能**: Dialect サポートの拡充
3. **ドキュメント**: API リファレンスの充実

