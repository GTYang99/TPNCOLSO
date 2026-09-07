# Implementation Plan

## Goal
- 補足 `AGENTS.md` 的端到端 SDLC 治理流程，並維持現有狀態機與規則相容。
- 建立可操作的 Knowledge Governance 與 Knowledge Resolution 產物契約。

## Scope
- 更新 `AGENTS.md`、Knowledge Resolution 規則與範本，以及本任務文件證據。

## Affected Files
- `AGENTS.md`：新增需求、設計、Review、CI、驗收與發布後流程。
- `ai/rules/knowledge/resolution.md`：補充 Knowledge 生命週期與治理規則。
- `ai/templates/knowledge/resolution.md`：新增 canonical 產物格式。
- `docs/tasks/DOCS-0907-sdlc-workflow/*`：保存計畫、審查、執行與驗證證據。

## Implementation Steps
- 補充端到端 SDLC flow，標示條件式 gate 與既有 phase 的歸屬。
- 新增 Requirement Baseline 與 Change Control 規則。
- 擴充 Plan Review，使其涵蓋架構、API、資料遷移、安全與 rollback 設計審查。
- 定義 Code Review、CI、條件式 UAT 與 release evidence。
- 定義部署後 smoke test、監控、事故與 rollback 路由。
- 新增角色責任、traceability、Definition of Ready 與 Definition of Done。
- 定義 Knowledge 類型、來源清冊、領域權威、可信度、時效、衝突、決策與假設規則。
- 定義 Knowledge baseline、失效觸發、跨階段重新解析與 handoff。
- 新增並驗證 `knowledge-resolution.md` canonical template。
- 建立 Collection 與 Validation 的角色規則、artifact template、entry/exit gate 和 failure route。
- 將 Knowledge state 統一為 `phase: knowledge`，以 collection、resolution、validation、ready 表達子階段。
- 同步更新 Planning、Verification、Issue Management、Investigation 與 Tutorial 的路由和範例。
- 在 Tutorial 新增五分鐘掌握專案資訊的閱讀路徑、專案資訊卡與 task 接手步驟。

## Test Plan
- 檢查 Markdown 結構與重複標題。
- 搜尋新增必要關鍵字與 gate。
- 對照現有 Role Routing、State Contract、Testing Rules 與 Release Rules。
- 對照 Knowledge Resolution required outputs 與新增 template 欄位。
- 檢查所有 Knowledge 規則檔、template、state value 與 `next_action` 路由一致。
- 搜尋並審查舊 `knowledge_resolution` state 範例，避免殘留互相衝突的 canonical 說明。

## Regression Plan
- 確認既有 phase、status、next_action、Failure Flow 與禁止事項仍保留且無衝突。
- 確認沒有引用不存在的新規則檔。
- 確認不把 repository current behavior、測試或範例提升為產品需求來源。

## Risks
- 文件過度展開可能讓入口規則難以閱讀，因此細節以 gate contract 表格與短規則呈現。
- Knowledge 來源權威必須依領域判定，避免錯誤使用單一全域優先序。
- Tutorial 篇幅較長，快速導覽必須置於文件前段，細節仍以 AGENTS 與 phase rules 為準。

## Rollback Plan
- 若補強造成矛盾，可移除新增段落並恢復原有 `AGENTS.md` 內容。

## Current Behavior 
- 主要 phase 已存在，但完整 SDLC 的 Review、CI、驗收和上線後治理仍不夠具體。

## Expected Behavior
- Agent 能由 `AGENTS.md` 判斷每個 SDLC 檢查由哪一階段負責、需要何種證據，以及失敗時返回何處。

## Open Questions
- 無
