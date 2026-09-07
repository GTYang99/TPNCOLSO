# Implementation Plan

## Goal
- 以 UI-001 共用元件建立可由 fake state 完整驗收的登入／註冊 Compose UI，並定義與圖台及正式 Session integration 的穩定邊界。

## Scope
- 實作 `SCR-AUTH-01`、`SCR-AUTH-02`、local validation、state/effect、fake auth source、Preview 與自動化測試；不實作側欄、地圖、API 或 Token。

## Affected Files
- `app/src/main/java/com/example/tp_ncolso_android/feature/auth/AuthContract.kt`：Login／Register immutable state、events、effects 與 fake-domain inputs。
- `app/src/main/java/com/example/tp_ncolso_android/feature/auth/AuthValidation.kt`：可單元測試的純本機驗證。
- `app/src/main/java/com/example/tp_ncolso_android/feature/auth/LoginScreen.kt`：登入 route／screen 與 Preview state matrix。
- `app/src/main/java/com/example/tp_ncolso_android/feature/auth/RegisterScreen.kt`：註冊 route／screen 與 Preview state matrix。
- `app/src/main/java/com/example/tp_ncolso_android/feature/auth/FakeAuthDataSource.kt`：集中 deterministic fake results／company options；不得成為 production DTO。
- `app/src/main/java/com/example/tp_ncolso_android/feature/auth/AuthHost.kt`：登入／註冊切換與成功／返回 callbacks；是否保留此檔依 UI-001 host contract 決定。
- `app/src/main/res/drawable/`：保存由 Figma 匯出的 exact logo、城市剪影、註冊圖示與無現成等價元件的 icons；實際 ownership 先與 UI-001 asset catalog 對齊。
- `app/src/main/java/com/example/tp_ncolso_android/MainActivity.kt`：在不破壞 UI-001 shell 的前提下顯示 auth host。
- `app/src/test/java/com/example/tp_ncolso_android/feature/auth/AuthValidationTest.kt`：欄位與敏感 state 規則。
- `app/src/androidTest/java/com/example/tp_ncolso_android/feature/auth/LoginScreenTest.kt`：登入語意、狀態與單次 callback。
- `app/src/androidTest/java/com/example/tp_ncolso_android/feature/auth/RegisterScreenTest.kt`：註冊欄位、驗證、選項與返回。
- `app/build.gradle.kts`、`gradle/libs.versions.toml`：僅加入 Plan Review 核定且 UI-001 未提供的必要 state-holder dependency。

## Implementation Steps
- 1. Implementation 開始前確認 `UI-001-foundation` 的 AppRoot／session contract 與 debug direct-login 已完成，再依核定 Figma 盤點可重用 tokens、fields、buttons、icons、assets 與 test helpers。
- 2. 建立不含 Retrofit／Token 的 auth contract：Login／Register state、user events、one-shot effects、company option UI model 與敏感欄位生命週期規則。
- 3. 以 pure functions 實作必填、4 碼數字驗證碼、英數且最多 30 字元帳號、至少 8 碼密碼、確認一致與中文姓名驗證。
- 4. 建立可注入的 fake auth source，提供 idle、delayed success、login error、register error 與 company options fixtures，並確保 submitting 時忽略重複 request。
- 5. 依 2026-09-07 核定 Figma baseline 重新取得 context、記錄 node／retrieval evidence 並保存 exact assets；實作 Login route／screen，重用 UI-001 元件與 assets，涵蓋核定內容欄尺寸、empty、filled、submitting、field-error、request-error、password visibility、captcha refresh、remember-me 與 register event。
- 6. 實作 Register route／screen，依規定順序呈現六組欄位、可捲動與 IME actions，涵蓋 empty、filled、validation-error、submitting、back/cancel 與 single-submit。
- 7. 建立最小 Auth host／callback 接口：login success、registration success 後返回空白登入頁、return-to-login 與 external require-login；不建立 map placeholder、drawer 或 session implementation。
- 8. 補齊 Preview state matrix、unit tests 與 Compose UI tests，再以 Android 9+ emulator 與較新版本驗證寬度、字級、IME、TalkBack semantics 與 state restoration。
- 9. 記錄 UI-003 handoff（側欄登出觸發）與 INT-001 handoff（API、永久 Token、401），避免下游修改已通過的 screen contract。

## Test Plan
- Unit tests：每項 local validation 的合法／非法 boundary、trim policy、密碼不持久化、submitting duplicate suppression、effect consume-once。
- Login Compose tests：欄位／semantics、預設密碼遮罩、visibility toggle、captcha refresh、remember-me、固定登入錯誤文案、success callback exactly once。
- Register Compose tests：六欄順序、company options injection、job single selection、帳號 30 字元邊界、每項 validation、返回／取消、成功返回空白登入頁且 callback exactly once。
- Layout checks：最小支援寬度與一般寬度、font scale、IME、portrait scroll、48dp target、focus order。
- Build checks：affected unit tests、connected Compose tests、debug build 與既有 UI-001 regression suite。

## Regression Plan
- 重跑 UI-001 component semantics、theme／dynamic-color policy、Preview 與 screenshot／golden tests（若 foundation 建立）。
- 確認 App cold start、back behavior 與 edge-to-edge inset 不因 auth host 接入而失效。
- 確認 Preview／test source 不輸出或保存 password、confirm password、captcha。
- UI-003 與 INT-001 接線時重跑 UI-002 callback／state tests，確保 screen contract 不被 API DTO 污染。

## Risks
- UI-001 output 或 navigation decision 改變可能需要調整 affected files，但不應改變 auth screen acceptance。
- Pixel verification 必須綁定 2026-09-07 核定的 Figma file/node 與 implementation retrieval evidence；若檔案內容改變，原 visual evidence 失效並觸發 revalidation。

## Rollback Plan
- 此 feature 以獨立 package 與 host entry 接線；若驗證失敗，回退該 task commit 即可恢復 UI-001 app shell，不涉及資料遷移或 API rollback。

## Current Behavior 
- App 只顯示 `Hello Android!`，無 auth feature、navigation、state holder、產品 tokens 或 auth tests。

## Expected Behavior
- Login／Register 可在 fake data 下完整互動與測試；有效提交輸出一次性 callbacks，錯誤保留允許保留的輸入，外部 logout／401 可要求 host 回到乾淨登入狀態。
- UI-002 不包含 map drawer 與 Session 行為，但提供 UI-003／INT-001 可接線且不需重寫畫面的 contract。

## Acceptance Criteria Traceability
| AC | Implementation Step | Validation |
|---|---|---|
| AC-UI002-001 | 1, 5 | Login semantics／content Compose tests |
| AC-UI002-002 | 2–5 | Validation unit tests；duplicate-submit UI test |
| AC-UI002-003 | 4, 5, 7 | Fake error retention；success callback exactly-once test |
| AC-UI002-004 | 5–7 | Login-to-register、六欄與 back/cancel UI tests |
| AC-UI002-005 | 2–4, 6 | Validation matrix unit／Compose tests |
| AC-UI002-006 | 7, 9 | Auth host require-login contract test；UI-003/INT-001 handoff review |
| AC-UI002-007 | 5, 6, 8 | Width／font scale／IME／semantics device matrix |
| AC-UI002-008 | 4–8 | Preview inventory review；fake source replacement test |

## Failure Behavior
- Local validation failure：不呼叫 data source，聚焦第一個錯誤欄位並顯示文字錯誤。
- Fake login failure：結束 loading、維持登入畫面、保留帳號／remember-me，顯示「帳號、密碼或驗證碼錯誤」。
- Fake registration success：清除註冊輸入並返回空白登入頁，不自動登入。
- Captcha refresh failure：保留可操作表單，驗證碼區顯示錯誤與重試；不得假裝 refresh 成功。
- Missing asset／UI-001 dependency：停止 Implementation，記錄 infrastructure／planning issue，不以手繪 placeholder 宣告完成。

## Security and Privacy
- Password、confirm password、captcha 不進 log、analytics、saved-state bundle、semantics value 或 screenshot fixture；離開 auth flow 時清除。
- Remember-me 在 UI-002 只代表 checkbox state；是否保存帳號、保存位置與清除時機由 INT-001 的安全設計核定，絕不保存密碼；Session Token 效期依核定規則為永久。
- Fake source 不接受或回傳 production Token；API error payload mapping留給 INT-001。

## Open Questions
- UI-001 reusable API 依 `docs/tasks/UI-001-foundation/foundation-contract.md`；Implementation 前需確認其 code implementation 與 contract 一致。
- 無 Figma authority question；shared／feature-only asset ownership 已由 UI-001 contract 與本 plan 界定，Implementation 時建立 manifest evidence。
