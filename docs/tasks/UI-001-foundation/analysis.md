# Repository Analysis

## Current Behavior
- `MainActivity` 直接顯示 `Greeting("Android")`，沒有 app root、session state 或 feature navigation boundary。
- 專案只有 `main`、`test`、`androidTest` 既有內容，尚未使用 debug／release Kotlin source-set 分流。
- `minSdk` 目前為 24，低於產品要求的 Android 9（API 28）。

## Expected Behavior
- `MainActivity` 只啟動 variant-specific app entry；debug entry 可建立記憶體 fake session，release entry 無旁路。
- 共用 app root 只依賴 session state 與 content callbacks，不依賴 API DTO、Token 或特定 map/auth screen。
- `ui.foundation` 提供可重用 semantic theme、stateless components、presentation states、semantics 與 Preview／test guarantees。
- 後續 UI-002／UI-003 可分別替換未登入與已登入插槽。

## Affected Modules
- `app/src/main/java/com/example/tp_ncolso_android/`：app-root/session contract 與 `MainActivity`。
- `app/src/main/java/com/example/tp_ncolso_android/ui/foundation/`：theme、components、presentation state 與 preview ownership。
- `app/src/debug/java/com/example/tp_ncolso_android/`：開發登入、角色選擇與 fake session owner。
- `app/src/release/java/com/example/tp_ncolso_android/`：無 bypass 的 release app entry。
- `app/src/test/`、`app/src/androidTest/`：session 與 debug direct-login tests。
- `app/build.gradle.kts`、`gradle/libs.versions.toml`：Android 9 minSdk、direct coroutine dependency 與必要 build/test 設定。

## Dependencies
- 使用現有 Kotlin、Activity Compose、Material 3 與 Compose UI test dependencies。
- `StateFlow` 是 public session contract，必須直接宣告 `kotlinx-coroutines-core`，不得只依賴 lifecycle 帶入的 transitive dependency。
- 角色名稱取自已核定產品角色表；不建立正式權限矩陣。
- UI-002 消費 unauthenticated slot；UI-003 消費 authenticated slot；INT-001 取代 debug session creation。
- `docs/tasks/UI-001-foundation/foundation-contract.md` 是後續 UI Tasks 的 public contract source。
- `docs/design/app-ui-requirements.md` 與其中核定 Figma node index 是正式 visual source；UI-001 owns shared semantic tokens／brand assets，feature-only assets 由 consumer task owns。

## Risks
- debug/release source-set 邊界若設計錯誤，可能讓 bypass 出現在 release，屬 blocking security finding。
- 將 fake session 持久化或加入 Token 字段會讓開發捷徑污染正式認證架構。
- Git 已依 requester 的 new-project 決策建立 bootstrap commit 與 task branch；remote CI 尚未設定，Release 前仍須補齊 authoritative CI evidence。

## Unknowns
- 無 Implementation-blocking unknown。Remote／CI provider 在 Verification/Release 前必須設定並產生 authoritative run evidence。
- 正式 Navigation library 與 DI 留給後續 Task，不影響此基礎 contract。正式 visual source 已核定。
