# Issue Log

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
- Actual: API ownership、semantic component、AppRoot/session 與 consumer handoff contract 已文件化；實際 implementation／tests 仍受 `ENV-AUTH-001` 阻擋。
- Resolution: foundation contract 已交付；剩餘 implementation readiness 改由 environment issue 追蹤。
- Next action: `infrastructure`
- Owner: UI-001 owner / Planning

## ENV-AUTH-001 — UI-001 前置任務受 Git metadata 環境阻擋

- Category: `environment`
- Priority: `P1`
- Status: open
- Impact: UI-001 無法建立 task branch、committed implementation revision 或 CI evidence，連帶阻擋 UI-002 使用 foundation code／tests 開始實作。
- Evidence: `docs/tasks/UI-001-foundation/state.yaml` 已標示 `phase: infrastructure`、`status: blocked`、`reason: environment_missing_git_metadata`；目前 workspace 的 Git root 檢查失敗。
- Expected: 提供可用且可安全隔離的 Git repository metadata，讓 UI-001 完成 implementation、validation 與 commit traceability。
- Actual: workspace 不是可識別的 Git repository。
- Next action: `infrastructure`
- Owner: Infrastructure
