# Repository Analysis

## Current Behavior
- `MainActivity` 只在 Material 3 `Scaffold` 顯示 `Hello Android!`，沒有認證畫面、screen state 或導覽。
- theme 仍使用 Android Studio 預設紫色與 dynamic color；沒有品牌 token、Noto Sans TC 或正式 asset。
- 只有 template unit／instrumented tests；Gradle 尚無 Navigation Compose、ViewModel Compose 或認證相關依賴。

## Expected Behavior
- App host 能呈現可獨立 Preview／測試的 Login 與 Register routes，畫面依 immutable UI state 呈現並以 event callbacks 回報操作。
- fake auth source 集中管理 deterministic success／failure／loading 結果；Composable 不持有 API contract 或硬編碼 fake list。
- 登入成功與註冊成功只輸出 navigation effect；後續 UI-003／INT-001 可接線而不改寫畫面 state contract。

## Affected Modules
- `app/src/main/java/com/example/tp_ncolso_android/feature/auth/`：screen、route、state、validation、fake source。
- `app/src/main/java/com/example/tp_ncolso_android/MainActivity.kt`：僅在 UI-001 app host contract 已核定後接入 auth entry。
- `app/src/androidTest/java/com/example/tp_ncolso_android/feature/auth/`：Compose UI tests。
- `app/src/test/java/com/example/tp_ncolso_android/feature/auth/`：validation／state reducer unit tests。
- `app/build.gradle.kts`、`gradle/libs.versions.toml`：只在選定 state holder 需要現有 catalog 以外依賴時更新。

## Dependencies
- 必要前置：`UI-001-foundation` 的 AppRoot／session contract、debug direct-login、theme／component ownership 與 Preview/test scaffold。
- Product：註冊欄位與本機可確定規則；Login／Register screen inventory。
- Downstream：`UI-003-map-shell` 消費 `LoginSucceeded` 並提供 drawer logout event；`INT-001-auth-api` 替換 fake source、處理 Token 與 401。
- 不在本 Task 加入完整 navigation graph；以 host callbacks 維持低耦合。

## Risks
- UI-001 尚未存在，過早指定元件名稱可能與 foundation output 不符；Implementation 前必須重讀 UI-001 artifacts／source。
- 將 fake models 命名為 API DTO 或散落在 Composable，會讓 integration 產生不必要重構。
- Compose `rememberSaveable` 若保存密碼／驗證碼，可能讓敏感資料超出必要生命週期。
- Figma exact asset URLs 為短效連結；Implementation 必須依核定 node 重新取得 exact bytes 並納入 asset catalog，避免失去可重現來源。
- 正式 Figma 已核定；若檔案內容在 2026-09-07 核定快照後變更，必須先重新驗證差異，不得靜默更新 acceptance baseline。

## Unknowns
- 無 visual authority unknown；shared brand assets 由 UI-001 owns，auth-only logo／skyline／registration illustrations 由 UI-002 owns，全部登錄於 asset manifest。
- App navigation 是否採 Navigation Compose、單一 host state 或其他架構；UI-002 callback 不預先決定。
- fake captcha 是靜態 fixture、可測試 generator 或 image provider，由 UI-001 asset/test policy 決定。
