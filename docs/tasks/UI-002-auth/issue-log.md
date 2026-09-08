# Issue Log

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
