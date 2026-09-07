# Repository Analysis

## Current Behavior
- `AGENTS.md` 已涵蓋 Knowledge Resolution、Planning、Plan Review、Implementation、Verification、Debug、Infrastructure、Investigation 與 Release。
- Coding、Testing、Release 規則已存在，但 Code Review、CI 執行契約、UAT 與部署後營運檢查主要只以 gate 名稱出現。
- 現有 State Contract 要求不得任意新增 phase 或 status。
- Knowledge Resolution 目前只有最小來源與衝突規則，缺少來源 metadata、領域權威、時效、決策與假設生命週期。
- `knowledge-resolution.md` 是必要產物，但 `ai/templates/` 尚無對應 canonical template。
- Collection 與 Validation 目前仍被包在 Resolution 內，無法分別呈現來源完整性與獨立驗證結果。
- Tutorial 尚未提供從專案入口、規格、目前 Task 到 Knowledge baseline 的快速閱讀順序。

## Expected Behavior
- 在不擴增 state machine 的前提下，讓每個必要 SDLC 檢查都有歸屬階段、輸入、證據、核准條件與失敗路由。
- Requirement、Plan、Design、Implementation、Test、Release 之間具備追蹤關係。
- Knowledge 可以被登錄、驗證、引用、更新、失效與交接，不會由摘要、範例或既有程式碼靜默取代核准需求。
- Knowledge 使用單一 `knowledge` phase，依序經過 collection、resolution、validation，最後以 ready gate 交接 Planning。

## Affected Modules
- `AGENTS.md`
- `ai/rules/knowledge/resolution.md`
- `ai/rules/knowledge/collection.md`
- `ai/rules/knowledge/validation.md`
- `ai/templates/knowledge/collection.md`
- `ai/templates/knowledge/resolution.md`
- `ai/templates/knowledge/validation.md`
- `ai/templates/task/state.yaml`
- `ai/rules/planning/planning.md`
- `ai/rules/verification/verification.md`
- `ai/rules/governance/issue-management.md`
- `ai/rules/operations/investigation.md`
- `ai/tutorial/developer-guide.md`
- `docs/tasks/DOCS-0907-sdlc-workflow/`

## Dependencies
- `ai/rules/planning/planning.md`
- `ai/rules/planning/plan-review.md`
- `ai/rules/implementation/developer.md`
- `ai/rules/verification/testing.md`
- `ai/rules/verification/verification.md`
- `ai/rules/operations/release.md`
- `ai/templates/task/state.yaml`
- `ai/rules/governance/templates.md`

## Risks
- 若直接新增 phase，會與 canonical state template 及既有角色規則衝突。
- 若把 Code Review、CI 或 UAT 寫成可選但未定義適用條件，可能被錯誤跳過。
- 目前不是 Git repository，無法完成 commit gate。
- 過度僵化的全域來源優先序可能讓不同領域文件互相覆蓋，應改採領域權威與決策核准。
- 新 state contract 與舊任務 artifact 命名不同，必須明確記錄 legacy normalization 規則。

## Unknowns
- 無
