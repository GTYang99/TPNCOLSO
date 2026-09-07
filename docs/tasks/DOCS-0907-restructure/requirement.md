# Requirement

## Background
- 專案規則、範本、Tutorial、架構與產品規格目前分散於 root 與平鋪的 `ai/`，不易快速導航。

## Goal
- 建立清楚的 canonical folder structure，並同步更新 AGENTS、Tutorial、規則與所有內部引用。

## Functional Requirements
- `ai/` 依 rules、templates、tutorial 分層，rules 依 lifecycle domain 分組。
- 專案 architecture 與產品規格歸入 `docs/` 對應 domain。
- 歷史 Task 完整保留。
- 提供 `ai/README.md` 快速索引與 migration map。
- 更新 AGENTS 的 Sources of Truth、Role Routing、State Contract 與 Directory Structure。
- 修正搬移後的 Markdown、YAML 與產品規格內部連結。

## Non-functional Requirements
- 不修改產品語意、不遺失檔案、不建立重複 Source of Truth。
- 新結構必須可由角色、artifact 或產品 domain 快速找到資料。

## Acceptance Criteria
- AC-001：所有 AI rules 依 Knowledge、Planning、Implementation、Verification、Operations、Governance 分組。
- AC-002：Architecture 與 115 年度產品規格位於 `docs/` 下的 canonical domain。
- AC-003：`docs/tasks/` 既有任務與檔案完整保留。
- AC-004：所有 repository 內部舊路徑引用已更新，僅 migration map 可保留舊路徑字樣。
- AC-005：AGENTS 明確呈現 canonical directory tree 與新增檔案放置規則。
- AC-006：Tutorial 的快速導覽、rules tree、artifact tree 與圖片連結符合新路徑。
- AC-007：產品索引及章節交叉引用可解析至實際檔案。
- AC-008：YAML 可解析、Markdown fence 成對、changed files 無 conflict marker。
- AC-009：Git metadata 缺失被記錄為 NOT VERIFIED，不宣告 commit／CI PASS。

## Constraints
- 不刪除歷史 Task 或產品內容。
- 不修改產品規格語意。
- 目前無 Git metadata。

## Open Questions
- 無
