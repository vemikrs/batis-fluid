plugins {
    // Central Portal 推奨: Vanniktech Maven Publish Plugin を採用
    id("com.vanniktech.maven.publish") version "0.30.0" apply false
}

// ルートプロジェクトのグループIDを定義します（モジュールと揃えるため）。
group = "jp.vemi"

// Central Portal への公開は各モジュールで本プラグインを適用し設定します。

// SeasarBatis の Maven Central 公開フローを補助するタスク群です。
// シークレット名はこのリポジトリの既存運用（OSSRH_*, SIGNING_*）を前提としつつ、
// Gradle には ORG_GRADLE_PROJECT_* 経由で渡されることを想定します。

tasks.register("validateCredentials") {
    group = "publishing"
    description = "Central Portal 向け Maven Central / 署名クレデンシャル設定を検証します。"

    doLast {
        val mcUser = System.getenv("ORG_GRADLE_PROJECT_mavenCentralUsername")
        val mcPass = System.getenv("ORG_GRADLE_PROJECT_mavenCentralPassword")
        val signingKey = System.getenv("ORG_GRADLE_PROJECT_signingInMemoryKey")
        val signingPass = System.getenv("ORG_GRADLE_PROJECT_signingInMemoryKeyPassword")

        println("=== Maven Central Credentials (Central Portal) ===")
        println("  Username : " + if (mcUser.isNullOrBlank()) "MISSING" else "AVAILABLE")
        println("  Password : " + if (mcPass.isNullOrBlank()) "MISSING" else "AVAILABLE")
        println("=== GPG Signing (in-memory) ===")
        println("  Key      : " + if (signingKey.isNullOrBlank()) "MISSING" else "AVAILABLE")
        println("  Password : " + if (signingPass.isNullOrBlank()) "MISSING" else "AVAILABLE")

        if (mcUser.isNullOrBlank() || mcPass.isNullOrBlank()) {
            error(
                "Maven Central credentials are not configured. " +
                    "Set ORG_GRADLE_PROJECT_mavenCentralUsername / ORG_GRADLE_PROJECT_mavenCentralPassword."
            )
        }

        if (signingKey.isNullOrBlank() || signingPass.isNullOrBlank()) {
            error(
                "GPG signing credentials are not configured. " +
                    "Set ORG_GRADLE_PROJECT_signingInMemoryKey / ORG_GRADLE_PROJECT_signingInMemoryKeyPassword."
            )
        }
    }
}

tasks.register("publishLocalOnly") {
    group = "publishing"
    description = "すべての公開物をローカル Maven リポジトリのみに公開します。"
    dependsOn("publishToMavenLocal")
}

