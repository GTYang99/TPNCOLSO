# Implementation Plan

Plan revision: 16 (reconciled UI-001 prerequisite gate; submitted for Plan Review)

## Goal
- 以 UI-001 共用元件建立可由 fake state 完整驗收的登入／註冊 Compose UI，並定義 UI-003 可呼叫的立即登出／回到乾淨 Login 契約，以及正式 Session integration 邊界。

## Scope
- 實作 `SCR-AUTH-01`、`SCR-AUTH-02`、local validation、state/effect、fake auth source、Preview、自動化測試及 Logout destination contract；不實作側欄本體、登出列外觀、地圖、API 或 production Token。

## Affected Files
- `app/src/main/java/com/example/tp_ncolso_android/feature/auth/AuthContract.kt`：Login／Register immutable state、events、effects 與 source-neutral UI/domain inputs；不得出現 fake implementation 或 production DTO。
- `app/src/main/java/com/example/tp_ncolso_android/feature/auth/AuthViewModel.kt`：唯一 Auth flow state owner，提供 immutable `StateFlow<AuthUiState>` 與由 buffered `Channel` 轉出的 one-shot `Flow<AuthEffect>`。
- `app/src/main/java/com/example/tp_ncolso_android/feature/auth/AuthDataSource.kt`：可替換 login／register／vendor／captcha interfaces，不含 Retrofit DTO。
- `app/src/main/java/com/example/tp_ncolso_android/feature/auth/AuthRoute.kt`：lifecycle-aware state/effect collection、ViewModel event dispatch 與 stateless screen routing。
- `app/src/main/java/com/example/tp_ncolso_android/feature/auth/AuthValidation.kt`：可單元測試的純本機驗證。
- `app/src/main/java/com/example/tp_ncolso_android/feature/auth/LoginScreen.kt`：production-safe stateless Login route／screen；不包含 Preview fixture 或 fake source。
- `app/src/main/java/com/example/tp_ncolso_android/feature/auth/RegisterScreen.kt`：production-safe stateless Register route／screen；不包含 Preview fixture 或 fake source。
- `app/src/main/java/com/example/tp_ncolso_android/feature/auth/AuthHost.kt`：建立登入／註冊切換並把 ViewModel one-shot effects 轉送為 callbacks；不得依賴、保存或修改 `AppSessionOwner`。
- `app/src/debug/java/com/example/tp_ncolso_android/feature/auth/DebugAuthDataSource.kt`：debug-only deterministic login／register／vendor fake implementation；不編入 release。
- `app/src/debug/java/com/example/tp_ncolso_android/feature/auth/DebugCaptchaProvider.kt`：debug-only deterministic captcha source；不編入 release。
- `app/src/debug/java/com/example/tp_ncolso_android/feature/auth/AuthPreviews.kt`：Login／Register Preview state matrix 與明顯 fake fixtures；避免把 fixture/data source 放入 main/release artifact。
- `app/src/debug/java/com/example/tp_ncolso_android/DebugAuthSessionCoordinator.kt`：debug composition-root adapter，也是唯一可同時呼叫 `DebugSessionOwner` 與 `AuthViewModel.resetToLogin()` 的類別。
- `app/src/test/java/com/example/tp_ncolso_android/feature/auth/TestAuthDataSource.kt`：JVM tests 自有可程式化 test double，不依賴 debug source set。
- `app/src/testDebug/java/com/example/tp_ncolso_android/DebugAuthSessionCoordinatorTest.kt`：驗證 Login／Logout exactly-once、clear/reset 順序與冪等性。
- `app/src/androidTest/java/com/example/tp_ncolso_android/feature/auth/TestAuthFixtures.kt`：instrumented tests 自有 screen states／callbacks，不依賴 debug data source。
- `docs/tasks/UI-002-auth/login-ui-requirement.md`：current composite 中三個 Login panel 的 exact composition、state authority、assets 與 visual acceptance source。
- `docs/tasks/UI-002-auth/registration-ui-requirement.md`：current composite 中兩個 Registration panel 的 exact composition、empty/filled states、plaintext password、required work type、assets 與 visual acceptance source。
- `docs/tasks/UI-002-auth/logout-interface-requirement.md`：UI-003 `LogoutRequested` 到 UI-002 清除狀態／返回 Login 的界面契約；不擁有側欄視覺。
- `docs/design/evidence/auth/auth-reference-2026-09-07.png`：Implementation 開始前封存 composite 原始 bytes，SHA-256 必須為 `75cb578c58f3098c270290f033539ebf06c35c8b322efb71526fa4e53f3d3e9c`。
- `docs/assets/app-ui-assets.md`：讀取 UI-001 Android／Material 預設 icon policy、semantic token evidence 與 component affordance handoff；不等待 UI-001 自有 logo／icon source/runtime asset。
- UI-002 若仍需要 city skyline 或其他 decorative media，必須在 UI-002 scope 內另行規劃 feature-owned evidence；不得阻塞 UI-001 foundation，也不得自行從 screenshot 重畫素材。
- `app/src/debug/res/drawable/login_captcha_fixture.png`：UI-002 的 Preview／test-only 驗證碼 fixture，不得進入 release source set。
- UI-002 若仍需要 registration user illustration，必須在 UI-002 scope 內另行規劃 feature-owned evidence，或改用核定的 Android／Material/system-default treatment。
- `app/src/debug/java/com/example/tp_ncolso_android/AppEntry.kt`：composition root；持有同一個 activity-scoped `AuthViewModel`、`DebugSessionOwner`、`DebugAuthSessionCoordinator`、debug source wiring 與 non-sensitive `selectedDebugRole`（預設 investigator），在 direct-login 與 AuthHost 間切換；兩種登入都只呼叫 coordinator。
- `app/src/release/java/com/example/tp_ncolso_android/AppEntry.kt`：保留無 bypass/fake 的 unauthenticated placeholder，作為 release isolation 測試目標；等 INT-001 才注入 production Auth source。
- `app/src/test/java/com/example/tp_ncolso_android/feature/auth/AuthValidationTest.kt`：欄位與敏感 state 規則。
- `app/src/test/java/com/example/tp_ncolso_android/feature/auth/AuthViewModelTest.kt`：StateFlow transition、effect exactly-once、single-submit、source replacement 與 sensitive-state clearing。
- `app/src/androidTest/java/com/example/tp_ncolso_android/feature/auth/LoginScreenTest.kt`：登入語意、狀態與單次 callback。
- `app/src/androidTest/java/com/example/tp_ncolso_android/feature/auth/RegisterScreenTest.kt`：註冊欄位、驗證、選項與返回。
- `app/build.gradle.kts`、`gradle/libs.versions.toml`：加入 `lifecycle-viewmodel-compose`、`lifecycle-runtime-compose` 與 ViewModel coroutine test 所需的最小直接依賴，不引入 Navigation Compose 或 DI framework。

## Implementation Steps
- 1. 執行 UI-001 completion gate：確認 `foundation-contract.md` 已依 current composite 重新審查，field／password（含 Login masked＋eye 與 Register plaintext＋no-eye variants）／checkbox／select／radio／button／gradient／token implementation 與 contract 一致；password visibility、reload、checkbox、back、dropdown、radio 等 affordance 全部使用 Android／Material 預設 icon／control，並通過 semantics、state behavior 與 48dp 觸控目標測試；debug source set 另提供 `DebugSessionController : AppSessionOwner` 與唯一 `startDebugSession(role)` boundary；component／session／visual tests 已執行，且 handoff commit SHA 已記錄。任一項未滿足時不得開始 UI-002 production implementation。
- 2. 建立不含 Retrofit／Token／fake class 的 main auth contract：Login／Register state、user events、`LoginSucceeded`／`RegistrationSucceeded` one-shot effects、company option UI model、`AuthDataSource`／`CaptchaProvider` interfaces 與敏感欄位生命週期規則。Contract 不公開 `AppSessionOwner`，也不建立第二份 signed-in state。
- 3. 建立 `AuthViewModel` 作為 Auth form/state 的唯一 owner：以 private `MutableStateFlow` 更新單一 `AuthUiState`、對外只暴露 `StateFlow`；以 buffered `Channel` 與 `receiveAsFlow()` 發布一次性 effect；ViewModel scope 執行 injected source。成功時先清除敏感 Auth state，再發出一次 `LoginSucceeded`，但 ViewModel 不注入、不呼叫 `AppSessionOwner`。另提供同步且冪等的 `resetToLogin()`，只負責清除 Login／Register state，不改 session。
- 4. `AuthRoute` 使用 `collectAsStateWithLifecycle()` 收集 state，以 `LaunchedEffect(viewModel)` 建立單一 effect collector；`AuthHost` 只把 `LoginSucceeded` 轉送至 `onLoginSucceeded` callback，把 screen events 回傳 ViewModel。Route／Host／stateless screens 均不持有 source 或 `AppSessionOwner`，不建立 signed-in state。
- 5. 以 pure functions 實作必填、4 碼數字驗證碼、英數且最多 30 字元帳號、至少 8 碼密碼、確認一致與中文姓名驗證。
- 6. 依 source set 隔離替身：`src/debug` 實作 `DebugAuthDataSource`／`DebugCaptchaProvider` 並保存 Preview fixtures；`src/test` 與 `src/androidTest` 各自提供 test doubles/fixtures。`src/main` 只包含 interfaces、source-neutral models、ViewModel 與 screens；release `AppEntry` 不建立 AuthHost，且 release compile/runtime classpath、DEX/resources 不得含 `DebugAuthDataSource`、`DebugCaptchaProvider`、`FakeAuthDataSource`、debug fixture credentials 或 captcha bitmap。
- 7. 依 `login-ui-requirement.md` revision 3 實作 Login：封存並校驗 composite bytes，以第一、二、三個 Login panels 分別作為 empty／filled／auth-error authority；field-error／submitting 才由同一 composition 派生。舊 screenshot 與 Figma frames 不得作為 visual acceptance source。
- 8. 依 `registration-ui-requirement.md` revision 2 實作 Registration 402×874 reference composition、top bar、user illustration、six controls、vendor select、initially-unselected required work type、action row及 empty／filled/derived error/submitting states；密碼與確認密碼顯示明文且不增加 eye action，但仍不得保存、記錄或暴露到非必要語意／測試證據。
- 9. 在 debug `AppEntry` 建立唯一 `DebugAuthSessionCoordinator`：同一 composition root 持有 activity-scoped `AuthViewModel` 與 UI-001 debug-only `DebugSessionController`，`DebugSignedOutHost` 持有 non-sensitive `selectedDebugRole`（預設 investigator）。AuthHost 收到 `LoginSucceeded` effect 後只回呼 `coordinator.login(selectedDebugRole)`；direct-login 也透過相同方法登入，不得直接呼叫 controller。Coordinator 以 `controller.state.value is SignedOut` guard，且只在這一處呼叫 `startDebugSession(role)`。UI-003 fixture 的 `LogoutRequested` 也只回到同一 coordinator；coordinator 在同一主執行緒 callback 先呼叫同一 `controller.clear()`，緊接 `authViewModel.resetToLogin()`，兩者皆冪等，於下一次 composition 前形成 `SignedOut + LOGIN_EMPTY`。AuthViewModel、AuthHost、LoginScreen、RegisterScreen、direct-login composable 與 UI-003 不直接修改 session。Remote logout 只能在 local transition 之後 fire-and-forget，失敗不得逆轉。debug signed-out default 為 AuthHost，outer debug affordance 仍可切換 direct-login；release `AppEntry` 保留 placeholder，不建立 coordinator/fake source，等 INT-001 提供 production adapter。
- 10. 補齊 debug-source Preview state matrix、ViewModel/unit tests、Compose UI tests、coordinator/session transition tests、debug host regression 與 release isolation checks，再以 Android 9+ emulator 與較新版本驗證寬度、字級、IME、TalkBack semantics 與 state clearing。
- 11. 記錄 UI-003 handoff（LoginSucceeded、drawer-owned Logout action 呼叫 `LogoutRequested`）與 INT-001 handoff（production source、永久 Token、remote logout／401、release AppEntry replacement）；UI-002 不實作側欄，也不預選 Navigation Compose。

## Source-set Isolation

| Source set | Allowed auth content | Forbidden dependency |
|---|---|---|
| `src/main` | source-neutral contract/models, `AuthViewModel`, validation, routes and stateless screens | debug/fake implementations, fixture credentials/captcha, `DebugSessionController` |
| `src/debug` | `DebugAuthDataSource`, `DebugCaptchaProvider`, Previews, `DebugAuthSessionCoordinator`, debug AppEntry wiring | production Token/DTO/API implementation |
| `src/release` | existing unauthenticated placeholder until INT-001 supplies production wiring | debug/fake classes, fixtures, direct-login and `DebugSessionController` |
| `src/test` | JVM-only test doubles for main contracts | dependency on `src/debug` implementations |
| `src/testDebug` | coordinator tests against the debug-only session contract | release implementation assumptions |
| `src/androidTest` | screen state fixtures and callback probes | production credentials or debug data-source dependency |

## State and Transition Ownership

| Layer | Owns | May mutate `AppSessionState` |
|---|---|---|
| `AuthViewModel` | Login/Register form state, validation, requests and one-shot effects | No; it never receives `AppSessionOwner` |
| `AuthHost` / `AuthRoute` | lifecycle collection and callback forwarding only | No |
| Login/Register screens | stateless rendering and user-event callbacks | No |
| `DebugAuthSessionCoordinator` | ordering/guard for debug Login and Logout transitions | Yes, exclusively through one `DebugSessionController` |
| `DebugSessionController` / `DebugSessionOwner` | canonical debug `AppSessionState` | Yes; `startDebugSession` and idempotent `clear` |
| UI-003 drawer | emits `LogoutRequested` | No |
| INT-001 future adapter | production Token/remote logout after its own approved plan | Must preserve immediate local signed-out transition |

Transition order is fixed:

1. Login: screen event → ViewModel validation/source → ViewModel clears sensitive form state → one `LoginSucceeded` effect → AuthHost callback → coordinator guarded `startDebugSession(role)` → AppRoot observes `SignedIn`.
2. Logout: UI-003 `LogoutRequested` → coordinator `controller.clear()` → `authViewModel.resetToLogin()` in the same main-thread callback → AppRoot observes `SignedOut` and AuthHost renders `LOGIN_EMPTY` → optional remote cleanup runs afterward.
3. Registration success: ViewModel clears Registration state → one `RegistrationSucceeded` effect → AuthHost returns to `LOGIN_EMPTY`; no session mutation occurs.

## Implementation Entry Gate

- **UI-001 Verification 已通過；Implementation entry gate 仍須完成其餘必要 gate 核對。** UI-002 不得進入 Implementation，直到 UI-001 的權威 `state.yaml`、review、Verification 與必要 CI 狀態均已明確記錄。
- UI-001 正式 reviewed／verified handoff commit 為 `1482f7d756330705ac31c60321403c68c6bf4786`，對應 `feature/UI-001-foundation-compose`；此 exact SHA 是 UI-002 重用 foundation contract、component signatures、session boundary 與 shared assets 的唯一 handoff revision。
- UI-001 `state.yaml` 與 `verification.md` 已記錄 Verification `PASS` 及 AC-UI001-001–010 全部 PASS；CI build/test 仍為 `not_verified`，不得誤標為 PASS，需依後續 gate 完成。
- Composite 原始 bytes 已封存至 `docs/design/evidence/auth/auth-reference-2026-09-07.png`，SHA-256 與 current source 完全一致，Login／Register／Logout panel crop／scale 規則已記錄。
- `docs/assets/app-ui-assets.md` 已記錄 UI-001 semantic token evidence 與 Android／Material 預設 icon policy；不要求 Login logo／eye／reload／check 或 Registration back／dropdown／radio-check 的自有 source/runtime asset、source node 或 SHA-256。
- UI-001 field／password／checkbox／select／radio／button semantics／visual tests、debug／release build 與 single-device runtime evidence 已完成並由 Verification report 記錄；CI 狀態仍以 `not_verified` 保留。
- UI-002 execution report 必須引用上述 UI-001 handoff artifact 與 exact SHA；若必要 CI gate 尚未完成，仍不得開始 production implementation，也不得新增重複 primitives 或 placeholder assets。

## Test Plan
- Unit tests：每項 local validation boundary／trim policy；`AuthViewModel` state transition、source injection、single-submit、`LoginSucceeded` effect exactly-once、success-before-effect sensitive clearing、`resetToLogin()` 冪等性、SavedStateHandle 不含 password／confirmPassword／captcha，並證明 ViewModel 不依賴或修改 `AppSessionOwner`。
- Login Compose tests：`UIR-LOGIN-001–010` 的 geometry/state/semantics mapping、預設密碼遮罩、visibility toggle、captcha refresh、remember-me、全三欄 auth-error treatment、固定錯誤文案、success callback exactly once。
- Captcha failure test：refresh failure 保留帳號／密碼／remember-me，顯示可理解錯誤，retry 成功後更換 fixture 且不發出登入請求。
- Login visual evidence：從 current composite 裁切三個 402×874 Login panels，分別驗證 empty／filled／auth-error，標示 composite SHA-256、panel crop／scale、device density、font scale、system inset policy 與差異結果；field-error／submitting 只驗證衍生狀態，不對舊來源宣告 pixel match。
- Register Compose tests：`UIR-REG-001–011` 的 geometry/state/semantics mapping、六欄順序、company options injection、初始未選／未選時必填錯誤／exactly-one selection、帳號 30 字元邊界、plaintext passwords/no eye action、每項 validation、返回／取消、成功返回空白登入頁且 effect exactly once；另驗證明文值不進 log、analytics、SavedStateHandle、`rememberSaveable` 或非必要語意輸出。
- Registration visual evidence：從 current composite 裁切兩個 402×874 Registration panels，驗證 empty／filled、初始未選與填寫後外業選取、明文密碼呈現，並記錄 composite SHA-256、density、font scale 與 inset policy。
- Logout/coordinator tests：以 UI-003 callback fixture 觸發 `LogoutRequested`，驗證不顯示確認、`DebugAuthSessionCoordinator` 對同一 `DebugSessionOwner` 依序執行 `clear()` → `AuthViewModel.resetToLogin()`，下一個可觀察 UI state 為 `SignedOut + LOGIN_EMPTY`；重複事件只形成一次有效 session transition，remote failure/latency 不阻塞或逆轉，debug direct-login 亦回正式 Login。另驗證 AuthHost／screens 不取得 session owner。
- Host/variant tests：debug 啟動預設 AuthHost、debug-only affordance 可開 direct-login／Back 返回 AuthHost，兩種 Login 都經同一 coordinator 且每次 signed-out cycle 只建立一次 in-memory session；release compile/runtime classpath、DEX/resource inspection 無 `DebugAuthDataSource`、`DebugCaptchaProvider`、`FakeAuthDataSource`、debug fixture credential/captcha、debug action 或 bypass symbol，且保留 unauthenticated placeholder。JVM/instrumented tests 各使用自身 source-set test double，不從 `src/debug` 洩漏依賴。
- Layout checks：最小支援寬度與一般寬度、font scale、IME、portrait scroll、48dp target、focus order。
- Build checks：affected unit tests、connected Compose tests、debug build 與既有 UI-001 regression suite。

## Regression Plan
- 重跑 UI-001 component semantics、theme／dynamic-color policy、Preview 與 screenshot／golden tests（若 foundation 建立）。
- 確認 App cold start、back behavior 與 edge-to-edge inset 不因 auth host 接入而失效。
- 確認 Preview／test source 不輸出或保存 password、confirm password、captcha。
- UI-003 與 INT-001 接線時重跑 UI-002 callback／state tests，確保 screen contract 不被 API DTO 污染。
- Debug `AppEntry` 預設顯示 AuthHost，仍可透過畫面外層 debug-only affordance 切換至三角色 direct-login；AuthHost 不把 debug action 混入 Figma canvas，release `AppEntry` 無任何 bypass/fake 參照。
- Login 與 Registration 切換不重建多個 effect collectors，離開註冊與登出／401 require-login 會清除 password、confirmation 與 captcha。
- `src/main`／`src/release` dependency scan 不得引用 `src/debug`／`src/test` symbols；assembleRelease 後重跑 class／DEX／resource inspection，防止 source-set 調整把 fake fixtures 帶回 release。
- Login/logout wiring regression 必須同時觀察 `AppSessionState` 與 `AuthUiState`，禁止 AuthViewModel/AuthHost/screens 直接 mutation session，避免後續 UI-003 接線產生第二個 state owner。

## Risks
- UI-001 output 或 navigation decision 改變可能需要調整 affected files，但不應改變 auth screen acceptance。
- Login／Registration visual verification 必須綁定 current composite path／SHA-256 與明確 panel crop／scale；任一 source bytes 或核定範圍改變時原 visual evidence 失效並觸發 revalidation。
- Registration 明文密碼是 requester 核定的 UI 行為，必須如實呈現；風險以禁止 log、analytics、持久化、saved state、真實 credential fixture 與非必要語意暴露控制，不能擅自改回遮罩。
- Logout 必須先完成不可逆的本機 signed-out transition；若把 remote logout 放在畫面轉移前，會違反立即返回 Login 契約並造成弱網路阻塞。
- Debug AuthHost 與 direct-login 共用 session owner 時若未單次化 callback，可能重複進入；ViewModel 與 session owner 均需 duplicate suppression tests。
- `test` 無法直接依賴 `debug` classes；因此共用 contract 測試用 source-neutral interface，debug coordinator 行為放在 `testDebug`，instrumented screen fixtures放在 `androidTest`，不可為了測試便利把 fake 搬回 `main`。

## Rollback Plan
- 此 feature 以獨立 package 與 host entry 接線；若驗證失敗，回退該 task commit 即可恢復 UI-001 app shell，不涉及資料遷移或 API rollback。

## Current Behavior 
- App 已有 UI-001 foundation、AppRoot 與 debug direct-login，但無 UI-002 auth feature、Login／Register state holder 或 auth tests。
- UI-001 foundation 已有對應元件與 contract，且其權威 Verification 已通過（AC-UI001-001–010 全部 PASS）；UI-001 的 CI build/test 仍為 `not_verified`，屬後續 gate，不改變 Verification 結果。UI-002 尚未開始 production implementation，仍須依 Entry Gate 核對必要 gate 後才能進入實作。

## Expected Behavior
- Login／Register 可在 fake data 下完整互動與測試；有效提交輸出一次性 callbacks，錯誤保留允許保留的輸入，外部 logout／401 可要求 host 回到乾淨登入狀態。
- UI-002 不包含 map drawer 或 remote/production Session 行為，但擁有立即本機 sign-out／清除 Auth state／返回 Login 的 destination contract，供 UI-003／INT-001 接線且不需重寫畫面。

## Acceptance Criteria Traceability
| AC | Implementation Step | Validation |
|---|---|---|
| AC-UI002-001 | 1, 7, 9, 10 | Login semantics／content and debug initial-entry Compose tests |
| AC-UI002-002 | 2–7, 10 | Validation/ViewModel tests；duplicate-submit UI test |
| AC-UI002-003 | 3, 6, 7, 9, 10 | Fake error retention；success effect/session transition exactly-once test |
| AC-UI002-004 | 3, 4, 8–10 | Login-to-register、六欄、back/cancel 與 host UI tests |
| AC-UI002-005 | 2–6, 8, 10 | Validation matrix unit／ViewModel／Compose tests |
| AC-UI002-006 | 3, 9–11 | `UIR-LOGOUT-001–006` contract/state/idempotency tests；UI-003/INT-001 handoff review |
| AC-UI002-007 | 7, 8, 10 | Width／font scale／IME／semantics device matrix |
| AC-UI002-008 | 3, 6–10 | Preview inventory；fake source replacement；variant isolation tests |
| AC-UI002-009 | 1, 7, 10 | `UIR-LOGIN-001–010` traceability；composite-authoritative empty／filled／auth-error plus derived-state Compose／accessibility evidence |
| AC-UI002-010 | 1, 8, 10 | `UIR-REG-001–011` traceability；two-state visual／Compose／accessibility evidence |
| AC-UI002-011 | 3, 9–11 | Logout boundary／immediate clear／clean Login／remote-independence tests；drawer visual marked UI-003-only |

### Detailed Login Traceability

| Login AC | Implementation Step | Validation |
|---|---|---|
| UIR-LOGIN-001 | 1, 7, 10 | 402×874 light-only screenshot comparison for gradient／column geometry；custom skyline geometry is not a UI-001 prerequisite |
| UIR-LOGIN-002 | 1, 7, 10 | Material/system icon policy check where an icon is used；title typography and two-line screenshot evidence |
| UIR-LOGIN-003 | 1, 7, 10 | account／password 320×48 geometry, external-label and state Compose tests |
| UIR-LOGIN-004 | 1, 6, 7, 10 | captcha row dimensions／aspect test；reload success and failure tests |
| UIR-LOGIN-005 | 6, 7, 10 | composite-authoritative empty／filled／auth-error comparisons；derived field-error／submitting state matrix |
| UIR-LOGIN-006 | 6, 7, 10 | three `#C8320A` borders, `#E00000` exact-copy text and error semantics test |
| UIR-LOGIN-007 | 1, 7, 10 | checkbox semantics／48dp target and register-link action test |
| UIR-LOGIN-008 | 1, 3, 6, 7, 10 | pill-button screenshot；loading and duplicate-click suppression test |
| UIR-LOGIN-009 | 1, 7, 10 | composite checksum review, Material/system icon policy inspection and release fixture exclusion check |
| UIR-LOGIN-010 | 7, 10 | narrow width／insets／IME／font-scale device matrix |

### Detailed Registration Traceability

| Registration AC | Implementation Step | Validation |
|---|---|---|
| UIR-REG-001 | 8, 10 | 402×874 white/light-only screenshot and padding/group geometry assertions |
| UIR-REG-002 | 1, 8, 10 | Material default back affordance and any approved feature-owned decorative media checks；top-bar screenshot |
| UIR-REG-003 | 8, 10 | six-control order／label／354×48 geometry Compose tests |
| UIR-REG-004 | 3, 8, 10 | empty/filled Preview matrix；plaintext/no-eye rendering and password-leak boundary checks |
| UIR-REG-005 | 2, 3, 6, 8, 10 | injected vendor options and accessible selection tests |
| UIR-REG-006 | 1, 3, 8, 10 | initial null／required error／exactly-one selection／48dp target tests |
| UIR-REG-007 | 3, 8, 10 | equal-button geometry／single-submit／navigation-race tests |
| UIR-REG-008 | 3, 4, 8–10 | Back/Cancel/success effect, state clearing and empty-Login return tests |
| UIR-REG-009 | 3–5, 8, 10 | validation semantics, plaintext/no-eye and log／SavedStateHandle／rememberSaveable exclusion tests |
| UIR-REG-010 | 1, 8, 10 | composite checksum review, Material/system icon policy inspection and any separately approved feature-owned media review |
| UIR-REG-011 | 8, 10 | narrow width／insets／IME／font-scale device matrix |

### Detailed Logout Traceability

| Logout AC | Implementation Step | Validation |
|---|---|---|
| UIR-LOGOUT-001 | 2–4, 9, 11 | UI-003 callback fixture invokes coordinator only; Host/screens have no session-owner dependency |
| UIR-LOGOUT-002 | 3, 9, 10 | coordinator orders one `clear()` → `resetToLogin()` and next observable UI is `LOGIN_EMPTY` without confirmation |
| UIR-LOGOUT-003 | 3, 9, 10 | same-owner session clear plus account/password/captcha/remember/register state clearing tests |
| UIR-LOGOUT-004 | 3, 9, 10 | effect/coordinator/repeated-event idempotency and single effective transition test |
| UIR-LOGOUT-005 | 6, 9–11 | delayed/failing remote fixture cannot block or reverse local logout |
| UIR-LOGOUT-006 | 9–11 | scope review proves no drawer implementation; UI-003 handoff recorded |

## Failure Behavior
- Local validation failure：不呼叫 data source，聚焦第一個錯誤欄位並顯示文字錯誤。
- Fake login failure：結束 loading、維持登入畫面、保留帳號／remember-me，顯示「帳號、密碼或驗證碼錯誤」。
- Fake registration success：清除註冊輸入並返回空白登入頁，不自動登入。
- Captcha refresh failure：保留可操作表單，驗證碼區顯示錯誤與重試；不得假裝 refresh 成功。
- Missing asset／UI-001 dependency：停止 Implementation，記錄 infrastructure／planning issue，不以手繪 placeholder 宣告完成。

## Security and Privacy
- Login password、Registration password／confirm password、captcha 不進 log、analytics、saved-state bundle、`rememberSaveable`、非必要 semantics value 或含真實 credential 的 screenshot fixture；Registration 僅在畫面欄位依核定需求顯示明文，離開 auth flow 或登出時立即清除。
- Remember-me 在 UI-002 只代表 checkbox state；是否保存帳號、保存位置與清除時機由 INT-001 的安全設計核定，絕不保存密碼；Session Token 效期依核定規則為永久。
- Fake source 不接受或回傳 production Token；API error payload mapping留給 INT-001。

## Open Questions
- None for Planning. UI-001 handoff 是 implementation entry gate；current composite authority、Register 初始未選／必填與明文密碼、UI-003 drawer ownership、UI-002 immediate-logout destination contract、asset ownership、debug/release host composition、MVVM state ownership、repository paths 與 error colors 均已確定。
