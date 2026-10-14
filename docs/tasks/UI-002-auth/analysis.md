# Repository Analysis

## Current Behavior
- UI-001 已建立 Compose foundation、AppRoot、in-memory debug session 與 debug direct-login。UI-002 R10 auth feature/tests 已存在於目前 checkout；其 Login implementation and tests include CAPTCHA-dependent code and are the R11 change surface. R10 CI failed during runner/Android SDK setup and connected tests, and does not establish R11 compliance.
- 現有 foundation 使用通用 Material 3 `OutlinedTextField`、文字式密碼顯示按鈕與舊 theme，不符合 current composite 的外置標籤、48 高控制、圖示、漸層、垂直錨點，以及 Register plaintext＋no-eye／initial-null radio variants。
- Existing auth tests cover R10 Login/Register states and include CAPTCHA behavior. R11 must update those tests, compare the composite only for unchanged regions, and capture the changed lower-control layout at runtime.

## Expected Behavior
- App host 能呈現可獨立 Preview／測試的 Login 與 Register routes，畫面依 `AuthViewModel` 所有的 immutable `StateFlow` 呈現並以 one-shot effect 回報操作。
- main auth contract 只定義 replaceable interfaces；deterministic auth implementation 與 Preview fixtures 位於 debug，unit/instrumented tests 使用各自 source-set doubles。R11 移除 Login CAPTCHA implementation/fixture；Composable 不持有 API contract 或硬編碼 fake list。
- `AuthViewModel` 只擁有 Auth form state並輸出 one-shot effects；`AuthHost` 只轉送 callbacks。debug `AppEntry` 的單一 coordinator 才能透過 UI-001 debug-only `DebugSessionController` 修改 session；正式 Login fake success 與既有 direct-login 都走同一 guarded `login(role)`，Logout 對同一 controller 依序 clear → reset Auth state。
- Debug `AppEntry` 預設顯示 AuthHost，並以 debug-only wrapper affordance 切換到現有 direct-login；開發 affordance 不進入 AuthHost 或 screenshot baseline。Release `AppEntry` 保留現有 unauthenticated placeholder 直到 INT-001 提供 production source，且永遠不引用 debug／fake 實作。

## Affected Modules
- `app/src/main/java/com/example/tp_ncolso_android/feature/auth/`：contract、`AuthViewModel`、route、Login／Register screens、validation 與 replaceable source interfaces。
- `app/src/debug/java/com/example/tp_ncolso_android/`：`DebugSignedOutHost`、`DebugAuthSessionCoordinator`、debug source factory/implementations、Preview fixtures、AuthHost／direct-login 模式切換與唯一 session transition wiring。
- `app/src/release/java/com/example/tp_ncolso_android/AppEntry.kt`：作為 release isolation 驗證目標；UI-002 不將 fake source 或 direct-login 接入 release。
- `docs/assets/app-ui-assets.md` 與規劃中 source/runtime paths：Login／Registration asset ownership 與 checksum evidence。
- `app/src/androidTest/java/com/example/tp_ncolso_android/feature/auth/`：Compose UI tests。
- `app/src/test/java/com/example/tp_ncolso_android/feature/auth/`：validation／state reducer unit tests。
- `app/src/testDebug/java/com/example/tp_ncolso_android/`：debug coordinator/session ownership tests。
- `app/build.gradle.kts`、`gradle/libs.versions.toml`：只在選定 state holder 需要現有 catalog 以外依賴時更新。

## Dependencies
- 必要前置：`UI-001-foundation` 交付已核准且與 code 一致的 foundation contract、shared asset manifest、committed revision 與 component／visual test evidence；UI-002 需記錄該 handoff revision 後才能修改 production code。
- Product：註冊欄位與本機可確定規則；Login／Register screen inventory。
- Downstream：debug composition root 消費 `LoginSucceeded` 並建立 fake session；`UI-003-map-shell` 只發出 drawer `LogoutRequested`；`INT-001-auth-api` 日後提供 production data/session adapter、Token、remote logout 與 401。
- 不在本 Task 加入完整 navigation graph；以 host callbacks 維持低耦合。

## Risks
- UI-001 已存在但與詳細 Login UI requirement 有明確落差；必須先回到 UI-001 Planning 更新 token／field／password／checkbox／button／asset contract 與 regression，不可在 UI-002 偷做重複 primitive。
- 將 fake models 命名為 API DTO 或散落在 Composable，會讓 integration 產生不必要重構。
- 把 fake implementation／Preview fixture 放在 `main` 會編入 release；所有可執行 fake data sources 必須限定在 `debug`／test source sets，並以 release DEX/resource inspection 驗證。
- Compose `rememberSaveable` 若保存密碼，可能讓敏感資料超出必要生命週期。
- Prior Figma exact asset URLs 為短效連結；Implementation 只能把其 export 當 candidate，保存 exact bytes 並以 current composite comparison 證明相符。
- Login／Registration 必須使用 current composite path／SHA-256 與 panel crops；任一來源變更都必須先重新驗證差異，不得靜默更新 acceptance baseline。
- 若 AuthHost 直接取代 debug signed-out slot，會移除 UI-001 核准的 direct-login；因此需使用 debug-only wrapper 切換模式，不得在 Login／Registration canvas 內加開發控制。
- 若把 password／confirmPassword 放入 `SavedStateHandle` 或 `rememberSaveable`，會違反敏感資料邊界；這些值只存在 ViewModel process memory，離開 flow 時清除。
- 若 ViewModel、AuthHost 或 screens 直接呼叫 `AppSessionOwner`，會產生第二個 transition owner；session mutation 僅能發生在 composition-root coordinator，且 Login／Logout 必須有 exactly-once／冪等與雙 state observation tests。

## Unknowns
- R11 keeps the brand/account/password positions and removes the CAPTCHA row plus adjacent spacing. Remember me, registration entry, and Login move upward using UI-001 spacing tokens. R10 implementation, verification, and CI do not establish R11 compliance. Other composite authority, Register requirements, immediate Logout, ownership, MVVM and host boundaries remain intact.
# R11 Planning Re-entry — 2026-10-14
- Requester decision removes Login CAPTCHA, its local validation/provider/fixture and CAPTCHA-specific error copy. Auth failure copy is now `帳號或密碼錯誤`.
- R10 implementation and verification do not establish R11 compliance. Plan revision 17 covers removal and regression evidence; implementation is blocked pending Plan Review.
- Requester decision `KD-AUTH-014` resolves the changed-region rule: preserve unchanged anchors, reflow the three lower controls with existing UI-001 spacing, and validate them with an R11 runtime capture rather than old y-coordinates.
