# CHANGELOG

## [0.0.2] - 2025-12-06
### Added
- Rebrand to **BatisFluid** (`jp.vemi:batis-fluid-core`, `jp.vemi:batis-fluid-spring`)
- New entrypoint **BatisFluid**, fluent API **JdbcFlow**, and SQL facade **SqlRunner**
- New annotations **@FluidTable**, **@FluidColumn**
- New config **FluidConfig**, **OptimisticLockConfig(+Loader)**
- New i18n **FluidLocale / Messages**
- New query builders: **SelectBuilder**, **UpdateBuilder**, **DeleteBuilder**, **SqlBuilder**
- New criteria: **Where**, **SimpleWhere**, **ComplexWhere**, **AbstractWhere**, **WhereCapable**, **OrderByCapable**, **OrderDirection**
- New SQL processing: **SqlFileLoader**, **SqlParser**, **SqlFormatter**, **ParsedSql**
- New entity support: **EntityOperations**, **PrimaryKeyInfo**, **OptimisticLockSupport**
- New transaction: **TransactionManager**, **TransactionOperation**, **TransactionContext**, **TransactionCallback**, **ThreadLocalDataSource**, **PropagationType**
- New exceptions: **FluidException**, **FluidSqlException**, **FluidIllegalStateException**, **EntityException**, **TransactionException**, **OptimisticLockException**, **SqlParseException**, **NoResultException**, **NonUniqueResultException**, **TypeConversionException**
- Comprehensive test coverage: 18 BatisFluid test classes, 78 total test classes

### Changed
- Package moved: `jp.vemi.seasarbatis` -> `jp.vemi.batisfluid`
- Massive class renames (see `NAMING_REFACTOR_PLAN.md`)
- Message files now available in both packages for backward compatibility

### Deprecated
- All legacy `SB*` classes kept as thin adapters with `@Deprecated(since = "0.0.2")` (planned removal in >= `0.0.3`)
- Legacy properties filename still accepted (`seasarbatis-optimistic-lock.properties`)

### Fixed
- Minor i18n keys and SQL parser edge-cases from 0.0.1

---

## [0.0.1] - 2025-10-06
- Initial pre-release as **SeasarBatis**: MyBatis wrapper with S2-like `JdbcManager` ops,
  SQL parser/processor, optimistic locking, and Spring integration (baseline).