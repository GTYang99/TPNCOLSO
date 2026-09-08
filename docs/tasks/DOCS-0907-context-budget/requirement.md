# Requirement

## Goal

- 以降低每次 Agent 接手的 Token 消耗為目的，降低 `AGENTS.md`、Rules、Templates 與 Tutorial 之間的重複與耦合。

## Requirements

- `AGENTS.md` 只保留全域不變條件、載入政策與 phase routing。
- 每個 phase Rule 只定義該階段的 entry、inputs、outputs、allowed writes、procedure、exit 與 failure route。
- State schema 只由 `ai/templates/task/state.yaml` 定義，Rules 不複製完整 YAML。
- Tutorial 必須明確為選讀，不能列入一般 Required Reading。
- Active Task 只讀 current artifacts；歷史與 superseded artifacts 僅在需要追溯時讀取。
- 保留既有 canonical paths，避免造成大量引用遷移。
- 當 Figma MCP 等結構化設計來源不完整或不可信時，AGENTS 必須定義 UI 截圖 evidence 的使用邊界。
- Gradle runtime 必須有單一 on-demand policy，固定使用 Android Studio JBR。

## Acceptance Criteria

- AC-001：`AGENTS.md` 不超過 220 行且小於 14 KB。
- AC-002：全部 Rules 合計小於 30 KB。
- AC-003：AGENTS 提供明確的最小載入順序與禁止遞迴讀取規則。
- AC-004：Role Routing 每個正常 phase 只指定一份 primary Rule。
- AC-005：Rules 不再重複完整 lifecycle、state YAML 或其他 phase 的詳細流程。
- AC-006：Tutorial 標示 optional/on-demand。
- AC-007：現有 Rule、Template 與產品文件連結仍可解析。
- AC-008：不修改 `app/` 與既有 UI-001／UI-002 Task artifacts。
- AC-009：AGENTS 允許截圖作為可見 UI 的主要 Collection evidence，並要求 Resolution 標記 `visual_observation`、列出不可確認資訊且禁止推論隱藏行為。
- AC-010：所有 Gradle 指令由共用 Runtime policy 固定使用 `/Applications/Android Studio.app/Contents/jbr/Contents/Home`，Implementation、Testing 與 Infrastructure 均可按需找到此規則。

## Constraints

- 保留現有 lifecycle、gates、failure categories 與 side-effect restrictions 的語意。
- 不覆寫目前 working tree 中的使用者變更。
- 本文件任務不與目前 UI feature branch 的變更一起提交。
