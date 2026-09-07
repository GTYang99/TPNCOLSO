# Implementation Plan

## Goal
- 將專案整理成可快速導航、單一來源且與 AGENTS workflow 一致的目錄結構。

## Scope
- 搬移流程 rules/templates/tutorial、architecture、產品規格及 domain templates；更新全部內部引用。

## Affected Files
- `AGENTS.md`、`ai/**`、`docs/**`、115 年度產品規格與本 Task artifacts。

## Implementation Steps
- 建立 canonical 目錄並按 migration map 搬移現有檔案。
- 建立 `ai/README.md` 導航入口。
- 更新 AGENTS 的路徑、角色路由、Sources of Truth、目錄樹與放置規則。
- 批次更新 rules、templates、Tutorial 與 Task artifact 中的舊路徑。
- 修正產品 README 與章節內部連結。
- 驗證檔案數量、引用、YAML、Markdown 與 legacy paths。

## Test Plan
- 比對搬移前後非 `.DS_Store` 檔案數量。
- 驗證所有 AGENTS／rules 明示路徑存在。
- 掃描舊 canonical 路徑與產品 root 路徑。
- 解析全部 YAML 並檢查 Markdown fences。

## Regression Plan
- 確認 `docs/tasks/` 既有 Task 及檔案數量不減少。
- 確認 Knowledge、Planning、Implementation、Verification、Release 路由語意不變。
- 確認 Tutorial 圖片存在且相對路徑正確。

## Risks
- 外部工具可能依賴舊本機路徑；以 `ai/README.md` migration map 記錄。

## Rollback Plan
- 依 migration map 反向搬移；在無 Git metadata 下不得刪除原始內容。

## Current Behavior 
- Workflow 與 project knowledge 混放，root 與 `ai/` 難以快速理解。

## Expected Behavior
- 從 AGENTS 或 `ai/README.md` 可快速找到每個 lifecycle rule、template、project spec 與 task evidence。

## Open Questions
- 無
