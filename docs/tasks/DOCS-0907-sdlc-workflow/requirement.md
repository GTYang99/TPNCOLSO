# Requirement

## Background
- 現有 `AGENTS.md` 已定義主要任務流程，但仍需補足可落地的 SDLC 治理細節。

## Goal
- 重新閱讀並補充 `AGENTS.md` 的需求治理、設計審查、程式碼審查、CI、驗收、發布後驗證與責任邊界。
- 補充 Knowledge Governance 與 Knowledge Resolution 的完整生命週期及標準產物。

## Functional Requirements
- 保留既有 Knowledge Resolution、Planning、Implementation、Verification、Release 與失敗分類。
- 補充各 SDLC 關卡的進入條件、證據、核准與失敗路由。
- 不新增缺乏配套規則的任務 phase，維持既有 state contract 相容。
- 定義 Knowledge 的來源登錄、領域權威、時效、衝突、決策、假設、失效與交接規則。

## Non-functional Requirements
- 流程必須可稽核、可追蹤，且不允許以限制或未執行檢查取代 PASS。

## Acceptance Criteria
- AC-001：`AGENTS.md` 明確定義需求基線與需求變更處理。
- AC-002：`AGENTS.md` 明確定義設計審查與 Plan Review 的關係。
- AC-003：`AGENTS.md` 明確定義 Code Review、CI 與 Verification gate。
- AC-004：`AGENTS.md` 明確定義條件式 UAT、安全、效能與相容性驗證。
- AC-005：`AGENTS.md` 明確定義授權發布、部署後 smoke test、監控與 rollback。
- AC-006：新增流程不與既有 phase、status、next_action 及規則路由衝突。
- AC-007：`AGENTS.md` 明確區分 Knowledge、Requirement、Decision、Assumption 與 Evidence。
- AC-008：Knowledge Resolution 規則包含來源清冊、權威判定、時效檢查、衝突處理、決策紀錄與失效機制。
- AC-009：提供 canonical `knowledge-resolution.md` 範本並符合 Template Rules。
- AC-010：Planning、Implementation、Verification 與 Release 都有重新進入 Knowledge Resolution 的觸發條件。
- AC-011：建立獨立 Knowledge Collection 規則與 canonical artifact template。
- AC-012：建立獨立 Knowledge Validation 規則與 canonical artifact template。
- AC-013：Knowledge state 明確支援 Collection、Resolution、Validation、Ready 與對應失敗回路。
- AC-014：Tutorial 提供五分鐘專案導覽、完整 Knowledge 流程、角色閱讀順序、artifact 與 state 範例。
- AC-015：Planning、Verification、Issue Management 與 Investigation 路由使用新的 Knowledge workflow contract。

## Constraints
- 本任務只修改流程文件與任務證據，不修改產品規格或 production code。
- 工作目錄目前沒有 Git metadata，無法建立 task branch 或 commit；必須明確記錄為環境限制。
- 舊任務的 `knowledge_resolution` 欄位不得因 schema 更新而失去可讀性；採用下一次 Knowledge transition 時正規化的相容策略。

## Open Questions
- 無
