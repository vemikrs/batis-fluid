# Vanniktech Maven Publish Plugin 0.36.0 アップデート

## 概要

2026年1月、`com.vanniktech.maven.publish` プラグインを 0.30.0 から 0.36.0 にアップデートした際の破壊的変更と対応方法について記録します。

## 参照情報

- **プラグインGitHub**: https://github.com/vanniktech/gradle-maven-publish-plugin
- **公式ドキュメント**: https://vanniktech.github.io/gradle-maven-publish-plugin/
- **リリースノート**: https://github.com/vanniktech/gradle-maven-publish-plugin/releases/tag/0.36.0
- **関連PR**: #53

## 破壊的変更 (Breaking Changes)

### 1. 最小サポートバージョンの更新

0.36.0では、以下の最小バージョンが引き上げられました：

| コンポーネント | 最小バージョン |
|---|---|
| JDK | 17 |
| Gradle | 9.0.0 |
| Android Gradle Plugin | 8.13.0 |
| Kotlin Gradle Plugin | 2.2.0 |

**BatisFluid の状況**: 
- JDK 21 使用中 ✅
- Gradle 9.1.0 使用中 ✅
- → 問題なし

### 2. `JavaLibrary` コンストラクタAPIの変更

**旧API (0.30.0以前)**:
```kotlin
configure(JavaLibrary(JavadocJar.Javadoc(), true))
```
- 第2引数: `Boolean` 型で sources jar の有無を指定

**新API (0.36.0)**:
```kotlin
configure(JavaLibrary(
    javadocJar = JavadocJar.Javadoc(), 
    sourcesJar = SourcesJar.Sources()
))
```
- 第2引数: `SourcesJar` 型を使用
- 名前付き引数を推奨

**利用可能なオプション**:

**JavadocJar**:
- `JavadocJar.None()` - javadoc jar を公開しない
- `JavadocJar.Empty()` - 空の jar を公開
- `JavadocJar.Javadoc()` - 標準 javadoc を公開
- `JavadocJar.Dokka("dokkaHtml")` - Kotlin + Dokka を使用

**SourcesJar**:
- `SourcesJar.None()` - sources jar を公開しない
- `SourcesJar.Empty()` - 空の jar を公開
- `SourcesJar.Sources()` - ソースコードを公開

### 3. `publishToMavenCentral()` の引数変更

**旧API (0.30.0以前)**:
```kotlin
publishToMavenCentral(com.vanniktech.maven.publish.SonatypeHost.CENTRAL_PORTAL)
```
- `SonatypeHost` enum を使用して公開先を指定

**新API (0.36.0)**:
```kotlin
publishToMavenCentral()
```
- 引数なしでデフォルトで Central Portal を使用
- `SonatypeHost` クラスは削除された

**オプション引数** (必要に応じて):
```kotlin
publishToMavenCentral(
    automaticRelease = true,  // 自動リリースを有効化
    validateDeployment = DeploymentValidation.VALIDATED  // デプロイ検証レベル
)
```

### 4. Dokka v1 サポート廃止

- Dokka v2 モードの使用が必須になりました
- BatisFluid では Dokka を使用していないため影響なし

### 5. `DeploymentValidation` の型変更

**旧API**:
```kotlin
validateDeployment: Boolean
```

**新API**:
```kotlin
validateDeployment: DeploymentValidation
```

**利用可能な値**:
- `DeploymentValidation.NONE` - 検証なし
- `DeploymentValidation.VALIDATED` - 検証完了まで待機 (デフォルト)
- `DeploymentValidation.PUBLISHED` - 公開完了まで待機

## BatisFluid での修正内容

### 修正ファイル

1. **lib/build.gradle.kts**
2. **spring/build.gradle.kts**

### 修正前

```kotlin
import com.vanniktech.maven.publish.JavaLibrary
import com.vanniktech.maven.publish.JavadocJar

mavenPublishing {
    configure(JavaLibrary(JavadocJar.Javadoc(), true))
    publishToMavenCentral(com.vanniktech.maven.publish.SonatypeHost.CENTRAL_PORTAL)
    
    // ...
}
```

### 修正後

```kotlin
import com.vanniktech.maven.publish.JavaLibrary
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.SourcesJar  // 追加

mavenPublishing {
    configure(JavaLibrary(
        javadocJar = JavadocJar.Javadoc(), 
        sourcesJar = SourcesJar.Sources()
    ))
    publishToMavenCentral()
    
    // ...
}
```

### 変更点まとめ

1. `SourcesJar` を import に追加
2. `JavaLibrary()` の第2引数を `Boolean` から `SourcesJar.Sources()` に変更
3. 名前付き引数 (`javadocJar =`, `sourcesJar =`) を使用
4. `publishToMavenCentral()` を引数なしで呼び出し

## トラブルシューティング

### エラー: Unresolved reference 'SonatypeHost'

**原因**: `SonatypeHost` クラスが 0.36.0 で削除されました

**対処**: `publishToMavenCentral()` を引数なしで呼び出してください

### 警告: 'constructor(javadocJar: JavadocJar = ..., sourcesJar: Boolean): JavaLibrary' is deprecated

**原因**: Boolean 型の `sourcesJar` パラメータが非推奨になりました

**対処**: `SourcesJar` 型を使用してください

```kotlin
// ❌ 非推奨
configure(JavaLibrary(JavadocJar.Javadoc(), true))

// ✅ 推奨
configure(JavaLibrary(
    javadocJar = JavadocJar.Javadoc(),
    sourcesJar = SourcesJar.Sources()
))
```

## 新機能 (0.36.0)

### Android プロジェクトでの Dokka サポート

Android プロジェクトで Dokka プラグインが適用されている場合、自動的に Dokka から javadoc が生成されます。

### Maven Central デプロイ検証の改善

- デプロイ状態の自動監視
- 5秒ごとのポーリング (設定可能)
- 60分のタイムアウト (設定可能)
- `FAILED` 状態時のエラー詳細表示

### isolated projects サポートの改善

Gradle の isolated projects が有効な場合、モジュール/プロジェクト固有の `gradle.properties` ファイルが正しく考慮されるようになりました。

## 推奨事項

1. **常に名前付き引数を使用する**: API の明確性と将来の互換性のため
2. **適切な `SourcesJar` / `JavadocJar` オプションを選択**: プロジェクトの要件に応じて
3. **公式ドキュメントを参照**: https://vanniktech.github.io/gradle-maven-publish-plugin/
4. **自動リリースを検討**: CI/CD パイプラインで `publishAndReleaseToMavenCentral` タスクを使用

## 参考リンク

- [Maven Central 公開ガイド](https://vanniktech.github.io/gradle-maven-publish-plugin/central/)
- [公開内容の設定ガイド](https://vanniktech.github.io/gradle-maven-publish-plugin/what/)
- [Changelog](https://vanniktech.github.io/gradle-maven-publish-plugin/changelog/)

## 更新履歴

| 日付 | 担当者 | 内容 |
|---|---|---|
| 2026-01-20 | GitHub Copilot | 初版作成 (0.30.0 → 0.36.0 移行) |
