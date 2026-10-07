# Issue Log

## IMP-AUTH-021 — Register narrow-width work-type label is clipped

- Category: `implementation_failure`
- Priority: `P1`
- Status: resolved
- Revision: `b193a4a` failed; fixed and committed in `48670ffe9e33b19aea23709abf2f47cc5ed85ff1`; current Verification round `2026-09-27`
- Evidence: the prior `b193a4a` failure is retained above. The fixed 720×2400 runtime `runtime-register-narrow-b193a4a-fixed-720x2400.png` (SHA-256 `b98d526febc2b37a15ed12ce22364eb8773e0b19a1a42b3e9a3df12d62058809`) and hierarchy `ui-register-narrow-b193a4a-fixed.xml` (SHA-256 `46a2d8f33c53cf2fd5a7663b7c0448a73f41ab9550d5ead17ec2f224ef76e560`) expose both `外業人員` and `內業人員`; font scale 1.3 and real IME re-checks also pass. The focused Compose assertion and 29/29 connected suite pass on `emulator-5554`.
- Requirement: `UIR-REG-011` requires the screen to remain usable at narrow widths without losing field order or actions; `AC-UI002-007` requires the form-level responsive matrix and `AC-UI002-010` requires every Registration UIR item to pass.
- Impact: The narrow-width implementation failure is closed. AC-UI002-006/011 and hosted CI remain separate NOT VERIFIED limitations.
- Route: `verification`
- Owner: UI-002 implementation
- Root cause: The horizontal `AppRadioGroup` row is unconditional and non-wrapping; at 720×2400 its two labeled options plus spacing exceed the available width, so the second label is clipped. See `root-cause.md` and `fix-plan.md` for the bounded fix scope.

## VER-AUTH-019 — Authoritative CI runner failure after local UI-003 logout integration

- Category: `environment`
- Priority: `P1`
- Status: open
- Revision: UI-002 reviewed `48670ff`; downstream UI-003 implementation `c8120ef`, callback tests `37d957e`, current checkout `0e0b609`; current Verification round `2026-09-29`
- Evidence: `ui003LogoutRequestedFixtureClearsBothStatesAndIsIdempotent`, `MapShellViewModelTest.logoutEffectIsEmittedOnceForRepeatedEvents`, `MapShellScreenTest.drawerLogoutEmitsOneCallbackAndExposesAccessibleAction`, and `DebugDirectLoginTest.directLoginAndLogoutReturnToSignedOut` prove the local UI-002/UI-003 drawer-to-Login path. The latest API 34 connected evidence is `docs/tasks/UI-003-map-shell/connected-tests-emulator-5554-2026-09-28T23-39-39.xml` with 33/33 tests, 0 failures/errors/skips, SHA-256 `298f3feb01e302b8b3e54231780ba93171428d7e942fefb319fd26e3b4a2abf4`. Authoritative GitHub Actions run `36442276066` for `48670ff` exists but is `failure`: job `unit-and-build` fails at `Set up Android SDK`, and job `connected` fails at `Connected Android tests`; no usable hosted build/test result was produced. The public API exposes failed step names but the logs endpoint returns `403 Must have admin rights`, so the exact runner error is not available from this verification context. Remote heads currently contain `main` and `UI-002feat`; `UI-003feat` is not published.
- Requirement: `AC-UI002-006` and `AC-UI002-011` are satisfied at local UI-002/UI-003 contract scope; `UIR-LOGOUT-005` production remote behavior remains INT-001-owned, and authoritative CI must complete successfully before release.
- Impact: The local cross-task integration gap is resolved. The hosted gate is now an environment/CI failure, so the task must not advance to Release until the runner/SDK setup and connected job are repaired and rerun.
- Route: `infrastructure`
- Owner: CI / Android runtime infrastructure
- Recheck `2026-09-29` (Asia/Taipei): remote heads still contain only `main` and `UI-002feat`; GitHub Actions still reports only run `36442276066`, with no new rerun or UI-003 hosted run.

## INF-AUTH-010 — Emulator-authoritative connected validation is unavailable

- Category: `infrastructure`
- Priority: `P1`
- Status: resolved
- Reproduction: the previously recorded `XQ-AU52 - 12` target has no `ro.boot.qemu` property, reports `qcom`, and is excluded under the no-physical-device constraint. A fresh `Medium_Phone(AVD) - 14` run at `2026-09-24T07:26:12Z` executed only on `emulator-5558` but failed 23/24 tests with `No compose hierarchies found in the app`.
- Impact: the initial mixed-device run and headless/profile run could not provide valid connected evidence. The failure was environmental and did not identify a production assertion regression.
- Safe action taken: stopped mixed-device runs, launched a local AVD, isolated ADB with `--one-device emulator-5558`/mDNS disabled, used the SwiftShader profile, and reran the suite.
- Resolution: `24/24` connected tests passed on `Medium_Phone(AVD) - 14`, `emulator-5558`, at `2026-09-24T07:48:37Z`; XML is committed under the current evidence directory. Visual five-state recapture remains a separate open evidence item.
- Route: `infrastructure`
- Owner: Android runtime / environment

## IMP-AUTH-017 — Residual viewport anchor mismatch after `534bcb2`

- Category: `implementation_failure`
- Priority: `P1`
- Status: resolved
- Revision: `f83eaa0` (`fix(UI-002): align auth viewport anchors`)
- Reproduction: current emulator-root normalized captures showed the Login skyline ending 67dp above the viewport, Login content/button shifted downward, and the Register action row clipped at the 402×874 bottom edge.
- Root cause: Login used a negative skyline offset and an oversized top/button rhythm; Register used a 40dp top origin and the action row remained below the approved viewport after the corrected horizontal radio layout.
- Fix: skyline offset is restored to the viewport edge, Login top/button anchors are tightened, Register top origin is 24dp, and the action row is placed 12dp upward without changing its 48dp controls or events.
- Evidence: `numeric-diff-f83eaa0.json`; five emulator-only states on `emulator-5558` at API 34; MAE Login empty/filled/error `10.5298/10.7950/22.9243`, Register empty/filled `5.8778/6.3757`; direct review confirms the bottom skyline and action row are visible and aligned.
- Route: `verification`
- Owner: UI-002 implementation

## IMP-AUTH-016 — Current revision still fails the approved Login/Register visual contract

- Category: `implementation_failure`
- Priority: `P1`
- Status: resolved
- Revision: `db7dfbe782d3537b5f618af2735964913ce580b7` (fixed in `14dc640`, responsive hardening in `534bcb2`)
- Evidence: `docs/design/evidence/auth/figma-export-2026-09-24-db7dfbe/numeric-diff-db7dfbe.json` contains all five normalized state comparisons against the approved composite. Direct review of the current captures proves residual Login header/form/action/skyline placement differences and residual Registration top-content/form/action vertical placement differences. Metrics are Login empty/filled/error MAE `16.9289`/`17.5848`/`26.9049` and Register empty/filled MAE `10.4778`/`11.5888`; RGB threshold `20`.
- Requirement: `AC-UI002-009` and `AC-UI002-010`, including `UIR-LOGIN-001–010` and `UIR-REG-001–011`, require conformance to the approved composite visual contract.
- Impact: The five-state visual mismatch and the narrow-width clipping/captcha overlap are resolved in the focused implementation fixes. Hosted CI and other cross-task gates remain separate `NOT VERIFIED` limitations.
- Route: `verification`
- Owner: UI-002 implementation

## IMP-AUTH-015 — Current revision fails the approved Login/Register visual contract

- Category: `implementation_failure`
- Priority: `P1`
- Status: resolved
- Revision: `842e29061238527d21ee51e29a61126ba9cbb32` (fixed in `db7dfbe782d3537b5f618af2735964913ce580b7`)
- Evidence: `docs/design/evidence/auth/figma-export-2026-09-23/composite-panel-comparison-842e290.json` records reproducible five-state comparisons against the approved composite. The current Register runtime places `外業人員` and `內業人員` vertically while the approved panel places them horizontally; the normalized runtime also does not preserve the approved bottom action-row composition. Login comparisons retain visible logo-treatment and field/captcha geometry differences. Current metrics are Login empty/filled/error MAE `21.3681`/`21.6469`/`31.6095` and Register empty/filled MAE `12.0194`/`12.5398` at RGB threshold `20`.
- Requirement: `AC-UI002-009` and `AC-UI002-010`, `UIR-LOGIN-001–010`, and `UIR-REG-001–011` require the implementation to match the approved current composite and provide executed visual evidence.
- Impact: The specific 842e290 visual mismatch is superseded by the current db7dfbe verification finding `IMP-AUTH-016`; hosted CI remains an additional unresolved gate.
- Route: `verification`
- Owner: UI-002 implementation

## IMP-AUTH-014 — Post-fix Registration accessibility and Back asset orientation remain nonconforming

- Category: `implementation_failure`
- Priority: `P1`
- Status: resolved
- Revision: `63a23794e70edb5cd00203a617afca7715be57b9` (fixed in `842e290`)
- Evidence: prior revision `RegisterScreen.kt:36` exposed the decorative illustration as `註冊插圖`; `ic_register_back_chevron.xml` used `M12,8 L20,16 L12,24` without rotation, so the committed vector pointed right. Revision `842e290` removes the label and applies 180° rotation.
- Requirement: `registration-ui-requirement.md` requires the illustration to remain decorative and the exact approved Back chevron to be rotated toward the left inside the `48 × 48` target.
- Impact: Source-level mismatches are fixed in `842e290`; the current visual-composition failure is tracked separately as `IMP-AUTH-016`.
- Route: `verification`
- Owner: UI-002 implementation

## IMP-AUTH-013 — Reviewed revision violates approved auth accessibility and asset clauses

- Category: `implementation_failure`
- Priority: `P1`
- Status: resolved
- Revision: `0815e6b5c22dc14bd2478a7b66bbb1aa58e7dd08`
- Evidence: `LoginScreen.kt:78` exposes the decorative logo as `品牌標誌`; `LoginScreen.kt:106–112` gives captcha reload a `32dp × 48dp` clickable bounds and description `重整`; `RegisterScreen.kt:42–47` draws a Canvas chevron instead of consuming the approved Back asset.
- Requirement: `login-ui-requirement.md` requires no duplicate spoken logo label, reload description `重新產生驗證碼`, and an effective `48 × 48` reload target; `registration-ui-requirement.md` requires the exact approved Back asset in the `48 × 48` target.
- Impact: The reviewed revision failed approved accessibility/asset requirements; the Login logo/reload and custom-Canvas Back findings were fixed by `63a2379`. Current visual captures and hosted CI evidence are separately incomplete.
- Route: `debug`
- Owner: UI-002 implementation

## PLN-AUTH-007 — UI-001 implementation entry gate lacks authoritative completion evidence

- Category: `planning_gap`
- Priority: `P1`
- Status: open
- Impact: UI-002 may begin against an unvalidated UI-001 foundation and cannot produce traceable reuse or visual verification evidence.
- Evidence: UI-002 `plan.md` revision 14 asserted the prerequisite as complete. Current UI-001 `state.yaml` is `phase: verification`, `status: not_verified`, `verification.result: NOT VERIFIED`, with committed revision `1482f7d`; Code Review／CI remain pending. UI-001 `execution-report.md` records `ec6a1a3`／`1482f7d` history, so the reviewed／verified handoff SHA is not yet normalized in the authoritative state.
- Expected: Treat UI-001 Verification and reviewed handoff as a verifiable precondition. Complete the remaining UI-001 gates, normalize the exact reviewed／verified artifact and commit SHA in UI-001 state/evidence, then record that evidence in the UI-002 entry gate before implementation.
- Next action: `planning`
- Owner: UI-001 / UI-002 Planning

## REQ-AUTH-005 — Auth composite 與 Register／Logout 決策取代 R9

- Category: `requirement_gap`
- Priority: `P1`
- Status: resolved
- Impact: R9 只將先前單張 Login screenshot 設為 authority，仍把 Register 綁定 Figma、密碼遮罩、作業性質預設外業，且未定義立即登出；直接實作會違反最新核定畫面與行為。
- Evidence: Requester 提供 PNG 7904×2916、sRGB、SHA-256 `75cb578c58f3098c270290f033539ebf06c35c8b322efb71526fa4e53f3d3e9c`，並確認其取代所有舊來源、外層 gray board/headings 排除、Register initial work type 為 null 且 required、密碼明文、drawer 屬 UI-003、Logout 無確認且立即清除並回 Login。
- Expected: 單一可追溯 visual baseline 與不跨越 UI-003／INT-001 ownership 的可測 Logout contract。
- Actual: 建立 `KB-UI-002-AUTH-R10`、三份 detailed requirements 與 plan revision 13；舊 source／decision 明確標記 superseded。
- Resolution: `KD-AUTH-012`；等待 iteration 12 Plan Review，不沿用 iteration 11 approval。
- Next action: `plan_review`
- Owner: Planning / Design

## VER-AUTH-012 — Skyline offset wording and runtime asset coordinate space conflict

- Category: `verification_failure`
- Priority: `P1`
- Status: resolved
- Impact: Applying the literal `-138dp` offset to the current 736×246 runtime asset clips Taipei 101 at the left edge and visibly diverges from the approved composite, even though the numeric ratio changes.
- Evidence: Emulator capture `runtime-login-empty-skyline-anchor-minus138.png` shows the clipped tower; the visually aligned implementation uses the centered-canvas `29dp` offset. Connected regression remains 31/31 PASS.
- Expected: Resolve the coordinate-space definition against the authoritative composite/Figma export, then rerun all Login state pixel comparisons with the resolved anchor.
- Resolution: Figma MCP node `2997:11541` confirms `left=-138`, `bottom=-7`; replaced the mismatched runtime PNG with the exact Figma raster and reran the Login empty comparison. The remaining diff is a separate visual mismatch, not an unresolved coordinate-source conflict.
- Route: `verification`
- Owner: UI-002 Planning / Design

## IMP-AUTH-008 — Approved auth visual assets absent from repository

- Category: `knowledge_collection`
- Priority: `P1`
- Status: resolved for asset collection; runtime visual verification remains open
- Evidence: approved Figma exports are now recorded in `docs/assets/app-ui-assets.md`, with source files under `docs/assets/source/`, debug captcha fixture at `app/src/debug/res/drawable-nodpi/login_captcha_fixture.png`, and SHA-256 evidence. The debug Login route now renders the captcha fixture through an injected visual slot.
- Impact: asset identity and source provenance are no longer blocked. UIR-LOGIN-001/002/004/009 and UIR-REG-002/010 still require runtime rendering and comparison evidence before PASS.
- Safe action taken: integrated the captcha fixture without inventing replacement artwork; retained `NOT VERIFIED` for device/runtime and pixel comparison gates.
- Required resolution: complete SVG runtime treatment for the approved logo/skyline/registration illustration as applicable, then run the approved visual/runtime verification path.
- Owner: UI-002 / Design asset handoff

## DBG-AUTH-010 — Approved SVG assets are collected but not runtime-consumed

- Category: `implementation_failure`
- Priority: `P1`
- Status: resolved
- Evidence: Verification reports AC-UI002-009 and AC-UI002-010 as `NOT VERIFIED`; `docs/assets/source/auth/login-city-skyline.svg` and `register-user-illustration.svg` exist, but `LoginScreen` and `RegisterScreen` have no runtime asset slots. Only the debug captcha PNG is currently injected.
- Root cause: asset collection was completed after the initial screen implementation, so the screens retained structural placeholders instead of consuming the approved visual source.
- Fix plan: `root-cause.md` and `fix-plan.md`; approved registration illustration runtime resource is integrated and local regression evidence is green.
- Limitation: connected runtime and pixel comparison remain separately blocked by `INF-AUTH-009` until an approved emulator is available.
- Owner: UI-002 implementation

## DBG-AUTH-011 — Four emulator tests failed on verification round 2

- Category: `implementation_failure`
- Priority: `P1`
- Status: resolved
- Reproduction: `connectedDebugAndroidTest` on `Medium_Phone (AVD) - 14` initially failed 4/17 tests: two direct-login tests assumed role controls before entering Debug mode, and two Login tests could not see/click the registration entry.
- Fix: direct-login tests now toggle `開發模式` before selecting a role; Login keeps the remember/register action row outside the scroll viewport and gives the checkbox a bounded width.
- Evidence: the same command now reports `BUILD SUCCESSFUL`, with 17/17 tests passed.

## INF-AUTH-009 — ADB runtime unavailable in current environment

- Category: `infrastructure`
- Priority: `P1`
- Status: open
- Evidence: `adb devices` could not start the daemon; ADB reported `could not install smartsocket listener: Operation not permitted` and `cannot connect to daemon`.
- Impact: Connected Compose tests cannot execute, so UI-002 runtime semantics, layout, IME and accessibility evidence remain `NOT VERIFIED`.
- Safe action taken: retained `assembleDebugAndroidTest` as APK compile evidence; no runtime PASS claimed.
- Required resolution: restore a permitted ADB daemon/device or provide equivalent approved Android test execution evidence.
- Owner: Android runtime / environment

## REQ-AUTH-001 — 認證 Figma 核定狀態

- Category: `requirement_gap`
- Priority: `P1`
- Status: resolved
- Impact: UI-only plan review 可進行，但 production visual implementation 與 pixel-level Verification 沒有核定設計基準。
- Evidence: Requester 於 2026-09-07 明確確認 Figma Copy `HRbRsw6HoNBUCtaieX8xUM` node `2905:2680` 目前內容就是正式版 UI，正式提供時不會有差異。
- Expected: Requester 提供可識別 file key、node ID 與 revision／核定日期的正式 Figma source。
- Actual: Structured context 已取得，且 requester 已授權作為正式 implementation／visual Verification baseline。
- Resolution: 以 file key、入口 node、已取得子節點與 2026-09-07 核定日期建立 `KD-AUTH-006`／`KB-UI-002-AUTH-R5`；後續內容變更觸發 revalidation。
- Next action: `plan_review`
- Owner: Requester / Design owner

## PLAN-AUTH-001 — UI-001 foundation contract 尚未交付

- Category: `planning_gap`
- Priority: `P1`
- Status: resolved
- Impact: 原本無法確認 UI-002 可重用的 token、共用元件與 session/app-root 邊界。
- Evidence: UI-002 re-review 4 已確認 `docs/tasks/UI-001-foundation/foundation-contract.md` 存在並可讀。
- Expected: UI-001 提供可識別的 component／token／asset／test contract。
- Actual: API ownership、semantic component、AppRoot/session 與 consumer handoff contract 已文件化，且已有 implementation commit；新發現的 exact visual mismatch 由 `REQ-AUTH-002`／`PLN-UI001-005` 另行追蹤。
- Resolution: 「完全沒有 foundation contract」的舊問題已解決；不代表現有 contract 已通過詳細 Login UI requirement。
- Next action: `planning`
- Owner: UI-001 owner / Planning

## ENV-AUTH-001 — UI-001 前置任務受 Git metadata 環境阻擋

- Category: `environment`
- Priority: `P1`
- Status: resolved
- Impact: 新專案原本尚未初始化 Git，無法建立 task branch、committed revision、CI 與 release traceability。
- Evidence: 後續 Infrastructure 已建立 Git repository、bootstrap `5221898` 與 branch `feature/UI-001-foundation-compose`；UI-001 implementation commit 為 `4950758`。
- Expected: 提供可用且可安全隔離的 Git repository metadata，讓 UI-001 完成 implementation、validation 與 commit traceability。
- Actual: Git repository 與 task branch 現已可用；當前 blocker 是正式 Login requirement 造成的 planning／implementation mismatch。
- Resolution: environment issue 關閉；不得將 `REQ-AUTH-002`／`PLN-UI001-005` 誤路由回 Infrastructure。
- Next action: `planning`
- Owner: Infrastructure

## REQ-AUTH-002 — Login UI requirement 過度概括

- Category: `requirement_gap`
- Priority: `P1`
- Status: resolved
- Impact: Generic Material login form could satisfy the old content AC while failing the approved 320-wide composition、captcha geometry、brand header、skyline and three-state visual behavior。
- Evidence: Requester 於 2026-09-07 指出實作方向錯誤；Figma Login frames `2905:2679`、`4922:15312`、`4922:15370` 已逐一重新取得 detailed context／screenshots／variables。
- Resolution: 建立 `login-ui-requirement.md`、KD-AUTH-007／008、`KB-UI-002-AUTH-R7`、FR-UI002-007 與 AC-UI002-009；包含分離的錯誤欄框／文字色與逐項驗收 mapping。
- Next action: `plan_review`
- Owner: Planning

## ASSET-AUTH-001 — Login asset ownership/path 未明確分流

- Category: `planning_gap`
- Priority: `P1`
- Status: resolved
- Impact: UI-002 可能重複保存 UI-001 shared logo／icons，或錯誤納入不屬於 Login requirement 的註冊圖示，造成 asset ownership、manifest 與回歸責任不一致。
- Evidence: `docs/tasks/UI-002-auth/plan.md` 的 `app/src/main/res/drawable/` 清單仍混合 logo、城市剪影、註冊圖示與 icons；`login-ui-requirement.md` 與 `foundation-contract.md` 已分別定義 UI-001 shared assets、UI-002 skyline／captcha fixture ownership。
- Expected: Affected paths、owner、Figma node、retrieval date 與 checksum 逐項明確，UI-002 不重複保存 shared assets。
- Actual: plan 與 `docs/assets/app-ui-assets.md` 已逐項列出 owner、source state node、source/runtime path、SHA-256 evidence gate；UI-002 不再保存 shared assets。
- Resolution: Login logo／eye／reload／check 與 Registration back／dropdown／radio-check 由 UI-001 所有；Login city skyline／debug captcha fixture 與 Registration user illustration 由 UI-002 所有，全部均已列入 asset manifest 及 detailed UI requirements。
- Next action: `plan_review`
- Owner: UI-002 Planning / UI-001 owner

## REQ-AUTH-003 — Login error border/text colors 被靜默統一

- Category: `requirement_gap`
- Priority: `P1`
- Status: resolved
- Impact: 三欄錯誤邊框可能正確，但全局錯誤文字會與核定畫面不一致，且 visual test 無法建立單一預期值。
- Evidence: Figma rendered error state；requester-provided `/Users/a10362/Desktop/tp_req_ui_login.md`；R6 `login-ui-requirement.md` 曾將兩者統一為 `#C8320A`。
- Resolution: `KD-AUTH-008`／`KB-UI-002-AUTH-R7` 確定 field borders 為 `#C8320A`、global error text 為 `#E00000`；foundation 使用 `fieldErrorBorder` 與 `errorText` 分離 tokens。
- Next action: `plan_review`
- Owner: Planning / Design

## PLN-AUTH-002 — Registration 缺少正式 visual/state/asset contract

- Category: `planning_gap`
- Priority: `P1`
- Status: resolved
- Impact: UI-002 仍可以 generic Material form 實作 Registration 並通過目前 content AC，重複 Login 的偏差。
- Evidence: plan step 6 含 Registration；`login-ui-requirement.md` 明確排除 Registration；Figma frames `4922:17227`、`4922:17493` 已知但未建立 normalized requirement；asset manifest 延後 registration illustration。
- Expected: 建立 detailed Registration UI requirement 與 AC mapping，或正式將 Registration 分拆為另一 task。
- Resolution: 已詳讀 frames `4922:17227`、`4922:17493`，建立 `registration-ui-requirement.md`、FR-UI002-008、AC-UI002-010、`UIR-REG-001–011` 與 plan detailed traceability；明文密碼 fixture 以安全規則遮罩。
- Next action: `plan_review`
- Owner: UI-002 Planning

## PLN-AUTH-003 — AuthHost 與 debug direct-login 接線未定義

- Category: `planning_gap`
- Priority: `P1`
- Status: resolved
- Impact: 可能刪除 debug bypass、誤把 bypass 帶入 release，或只有單一 variant 顯示正式 Login。
- Evidence: `MainActivity` 已只呼叫 variant-specific `AppEntry()`；debug/release AppEntry 分別持有 direct-login 與 signed-out placeholder，但 plan Affected Files 未列兩檔也未定義共存模式。
- Expected: 定義 debug/release signed-out composition、debug-only bypass 入口、release isolation 與 variant tests。
- Resolution: plan revision 11 要求 debug `AppEntry` 預設顯示 AuthHost，以畫面外 debug-only affordance 切換至 direct-login；release 保留 placeholder 且禁止 fake/bypass，並新增 initial-entry／mode-switch／release isolation tests。
- Next action: `plan_review`
- Owner: UI-002 Planning / UI-001 owner

## PLN-AUTH-004 — MVVM state ownership 未落到可執行設計

- Category: `planning_gap`
- Priority: `P1`
- Status: resolved
- Impact: 開發者需自行推測 ViewModel、coroutine、lifecycle、one-shot effect 與 sensitive state 邊界，可能違反 MVVM 與安全需求。
- Evidence: plan 只列 `AuthContract.kt`、screens 與 fake source，未列 ViewModel/state-owner file、lifecycle collection 或 SavedStateHandle policy。
- Expected: 指定 immutable `StateFlow` owner、effect 處理、lifecycle-aware collection、fake source injection 與密碼／captcha 不進 saved state。
- Resolution: plan revision 11 新增 `AuthViewModel.kt`、`AuthDataSource.kt`、`AuthRoute.kt`、ViewModel tests 與 lifecycle dependencies；規定 private MutableStateFlow/public StateFlow、buffered Channel + `receiveAsFlow()` one-shot effect、`collectAsStateWithLifecycle`、injected source 與 sensitive-state clearing。
- Next action: `plan_review`
- Owner: UI-002 Planning

## PLN-AUTH-005 — Fake source source-set 與 release isolation 矛盾

- Category: `planning_gap`
- Priority: `P1`
- Status: resolved
- Impact: `FakeAuthDataSource.kt` 若留在 main source set，release artifact 可能包含 fake source，與 release 不得引用或包含 fake authentication 的要求衝突。
- Evidence: `docs/tasks/UI-002-auth/plan.md` 將 `FakeAuthDataSource.kt` 列在 `app/src/main/...`，但 step 6、release isolation tests 與 requirement.md 均要求 release 不引用 fake source。
- Expected: 明確把 fake 實作放入 debug/test source sets，或定義並驗證 release-safe replacement；Affected Files、wiring 與 release checks 一致。
- Actual: plan revision 14 將 interfaces/models/ViewModel/screens 留在 main；debug data source/captcha/Previews 移到 `src/debug`，JVM／instrumented doubles 分置 `src/test`／`src/androidTest`，coordinator test 位於 `src/testDebug`。
- Resolution: Affected Files、step 6、tests 與 release DEX/resource inspection 使用一致 source-set boundary；release 不建立 AuthHost 或 fake source。
- Next action: `plan_review`
- Owner: UI-002 Planning

## PLN-AUTH-006 — Session owner transition boundary 未明確

- Category: `planning_gap`
- Priority: `P1`
- Status: resolved
- Impact: Login success 與 LogoutRequested 可能同時更新 AuthViewModel、AuthHost 與 UI-001 AppSessionOwner，導致重複 session transition、狀態分裂或 screen layer 越界。
- Evidence: plan step 9 同時描述 fake success 啟動 in-memory session、UI-002 清除 local session/auth state，但 Affected Files 未指定 AppSessionOwner injection／adapter 與唯一 transition owner。
- Expected: 明確定義 AuthViewModel emits effects、debug AppEntry adapter controls UI-001 session owner、Logout clears through one idempotent boundary，並以 tests 證明 exactly-once。
- Actual: plan revision 14 指定 `AuthViewModel` 只擁有 Auth state/effects、`AuthHost` 只轉送 callback、debug `DebugAuthSessionCoordinator` 是唯一 session transition owner；它以同一 `DebugSessionOwner` 處理 guarded Login，Logout 依序 clear → reset。
- Resolution: 新增明確 files、direction-of-control、禁止依賴規則與 `testDebug` exactly-once／ordering／idempotency evidence。
- Next action: `plan_review`
- Owner: UI-002 Planning / UI-001 owner

## REQ-AUTH-004 — Login 背景與整頁視覺來源已更換

- Category: `requirement_gap`
- Priority: `P1`
- Status: resolved
- Impact: Iteration 11 仍把舊 Figma Login frames 當成 empty／filled／error 整頁 visual authority；直接實作會重現 requester 指出的錯誤背景，且舊 Plan Review 核准失效。
- Evidence: Requester 於 2026-09-07 表示「背景頁面看起來不是目前版本」並提供 `/Users/a10362/Desktop/截圖 2026-09-07 下午5.39.20.png`；PNG 568×1202、Display P3、SHA-256 `aa77c69ba2b8a20b6245155b48863cc8cf9d02884e2fe00b7a55e3ff3676cf86`。
- Expected: Login 以新 screenshot 重建 background、vertical anchors、state authority 與 visual evidence；紫色 editor outline 不進 UI。
- Resolution: `KD-AUTH-011`、`KB-UI-002-AUTH-R9`、`login-ui-requirement.md` revision 2 與 plan revision 12 將 screenshot 設為 sole current Login empty visual source；舊 Figma Login frames 降為 asset／behavior supporting evidence，derived states 不宣告舊版 pixel match。
- Next action: `plan_review`
- Owner: Planning / Design

## IMP-AUTH-018 — IME 開啟後 Login action 被覆蓋

- Category: `implementation_regression`
- Priority: `P1`
- Status: resolved
- Impact: 真實 Android keyboard 開啟後，Login root 未消費 IME inset；ScrollView semantics 為 `scrollable=false`，登入按鈕停在 keyboard 覆蓋區，無法完成登入。
- Evidence: `emulator-5554` pre-fix UI hierarchy `ui-ime-scrolled.xml`；`mInputShown=true`；Login button bounds `[488,1560][592,1635]`，keyboard 覆蓋下方區域。
- Root cause: Login/Register root layout 使用 edge-to-edge，但沒有 `imePadding()`；既有 `verticalScroll` 因內容未受 IME 後的可視高度限制而沒有形成可滾動 viewport。
- Resolution: Revision `b193a4a` 對 LoginScreen/RegisterScreen root 加入 `imePadding()`。Post-fix hierarchy reports `scrollable=true`; after swipe the Login button is `[488,1417][592,1492]`, above the keyboard. Full connected suite is 28/28 PASS on API 34 emulator-5554.
- Next action: `debug` — the Login IME regression is resolved; the current Register narrow-width failure is tracked in IMP-AUTH-021. The full AC-UI002-007 criterion is therefore FAIL, while hosted CI and cross-task logout evidence remain open.
- Owner: UI-002 Implementation / Verification
