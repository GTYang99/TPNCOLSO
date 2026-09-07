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
- Status: open
- Impact: 無法建立 task branch、提交 implementation revision、執行 commit-based review 或留下 CI traceability。
- Evidence: 在 supplied workspace 執行 Git repository inspection 回報不是 Git repository。
- Infrastructure diagnosis: Project files are present, `.git` is absent, Git root／remote／log checks all fail, and only unrelated sibling repositories were found；詳 `infrastructure.md`。
- Resolution: 尚未解決；需提供 authoritative repository／完整 checkout，或由 repository owner 明確授權目前資料夾建立新的 canonical history與 remote／CI ownership。
- Next action: `requirement_clarification`
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
