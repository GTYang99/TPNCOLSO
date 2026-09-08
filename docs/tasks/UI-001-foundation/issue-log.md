# Issue Log

## PLN-UI001-001 — 缺少可重用 Foundation Contract

- Category: `planning_gap`
- Priority: `P1`
- Status: resolved
- Impact: UI-002 無法確定可重用 theme、component、state、session 與 test APIs，可能重複實作或形成不相容 contract。
- Evidence: Requester 於 2026-09-07 指出 UI-001 尚未提供可重用 foundation contract；原 plan 只具體定義 AppRoot／debug session。
- Expected: 明確列出 package ownership、public APIs、component states、semantics、Preview／test guarantees 與 consumer handoff。
- Actual: 已建立 `foundation-contract.md`，並將 contract 對應至 requirement AC-UI001-008／009 與 implementation steps。
- Resolution: Planning artifacts 已補齊 reusable contract；未使用開發版 Figma 視覺值。
- Next action: `plan_review`
- Owner: Planning

## PLN-UI001-002 — StateFlow 缺少直接依賴規劃

- Category: `planning_gap`
- Priority: `P1`
- Status: resolved
- Impact: foundation public contract 若只依賴 transitive coroutine artifact，建置與依賴所有權不穩定。
- Evidence: `foundation-contract.md` 公開 `StateFlow<AppSessionState>`，原 plan 只說保留既有 dependencies。
- Resolution: plan、analysis 與 contract 已要求在 version catalog／app module 直接宣告 coroutine dependency。
- Next action: `plan_review`
- Owner: Planning

## PLN-UI001-003 — 共用元件 contract 狀態輸入不足

- Category: `planning_gap`
- Priority: `P1`
- Status: resolved
- Impact: UI-002 可能為 required、keyboard action、select label 與 loading semantics 建立不相容 wrapper。
- Evidence: 原 component table 未列出上述必要輸入或行為。
- Resolution: 已擴充 field、password、select 與 primary button contract，並加入驗證範圍。
- Next action: `plan_review`
- Owner: Planning

## ENV-UI001-001 — Git metadata 不存在

- Category: `environment`
- Priority: `P1`
- Status: resolved
- Impact: 無法建立 task branch、提交 implementation revision、執行 commit-based review 或留下 CI traceability。
- Evidence: 在 supplied workspace 執行 Git repository inspection 回報不是 Git repository。
- Infrastructure diagnosis: Project files are present, `.git` is absent, Git root／remote／log checks all fail, and only unrelated sibling repositories were found；詳 `infrastructure.md`。
- Resolution: Requester 確認本案為從零建立的新專案；已建立 `chore/project-bootstrap`、root commit `5221898`、task branch `feature/UI-001-foundation-compose`，且 baseline unit test／debug build PASS。Remote CI 待 Release gate 前設定。
- Next action: `plan_review`
- Owner: Infrastructure

## REQ-UI001-001 — 正式 Figma revision 未提供

- Category: `requirement_gap`
- Priority: `P1`
- Status: resolved
- Impact: 未核定時只能建立 provisional visual foundation，不能宣告正式視覺 PASS。
- Evidence: Requester 於 2026-09-07 確認目前 Figma 內容即正式版 UI，正式提供時不會與目前版本不同。
- Resolution: Figma Copy `HRbRsw6HoNBUCtaieX8xUM`、入口 node `2905:2680` 與 design source index 所列節點升格為正式 visual baseline。
- Next action: `plan_review`
- Owner: Planning / Design

## PLN-UI001-004 — Architecture 文件屬於其他專案

- Category: `planning_gap`
- Priority: `P1`
- Status: resolved
- Impact: 原 `docs/architecture/overview.md` 描述 TaoYuanGutter、ViewBinding、Room、CameraX 與 legacy activities，會讓新專案 UI-001 的 module／dependency 設計錯置。
- Evidence: Requester 確認 TP_NCOLSO 是從零建立的新專案；repository 現況是單 Activity Compose starter。
- Resolution: 以 TP_NCOLSO Android 9+、Kotlin、Compose Material 3、MVVM、feature ownership、debug/release isolation 與 deferred integration dependencies 建立新的 architecture baseline。
- Next action: `plan_review`
- Owner: Planning

## PLN-UI001-005 — 現有 foundation 與正式 Login UI contract 不一致

- Category: `planning_gap`
- Priority: `P1`
- Status: resolved
- Impact: UI-002 直接重用現有 primitive 會產生浮動標籤、文字式密碼動作、錯誤色系，並缺少符合 14×14 視覺／48dp 選取目標的記住我、註冊 radio 與 select 共用能力。
- Evidence: 正式 Figma Login nodes `2905:2679`、`4922:15312`、`4922:15370` 與 Registration nodes `4922:17227`、`4922:17493` 詳讀結果；`login-ui-requirement.md`、`registration-ui-requirement.md`；現有 `AppTheme.kt` 與 `FoundationComponents.kt`。
- Resolution: Implementation 已新增 `AppCheckboxRow`、password visibility action disabled variant、field/global error semantic tokens，並將主要品牌色、文字色與邊框 token 對齊正式 design specification；對應 previews 與 Android UI test APK 編譯已通過。Connected device execution 另由 validation limitation 追蹤。
- Next action: `verification`
- Owner: Planning / UI-001

## REQ-UI001-002 — Auth visual source 更換使 foundation evidence 過期

- Category: `requirement_gap`
- Priority: `P1`
- Status: resolved
- Impact: UI-001 commit `bc78382` 與原 Plan Review 無法證明 tokens/assets、Login masked＋eye、Register plaintext＋no-eye 與 null-selection radio 符合 current composite；R3 曾發現 `DebugSessionController` interface 尚未落實，現已在 working tree 補上，仍待驗證。
- Evidence: Requester 於 2026-09-07 將 composite SHA-256 `75cb578c58f3098c270290f033539ebf06c35c8b322efb71526fa4e53f3d3e9c` 設為全部 auth visual 唯一權威；`KD-AUTH-012`、Login revision 3、Register revision 2。
- Resolution: `KB-UI-001-FOUNDATION-R4` Knowledge Validation PASS；R2 PASS 已取消，R4 已重新確認 current composite、token／asset ownership、password／selection variants、debug-only controller boundary 與 Planning handoff。Java Runtime unavailable 導致 build/test evidence 仍屬後續 gate，不作為 Knowledge PASS 依據。
- Next action: `plan_review`
- Owner: UI-001 Knowledge / Planning
