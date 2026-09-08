# Implementation Plan

## Goal

- 建立低 Token、低耦合且可依 phase 漸進載入的 Agent workflow 文件。

## Current Behavior

- Agent 容易同時讀取長篇 AGENTS、Rules、Tutorial 與過期 Task evidence。

## Expected Behavior

- 一般接手只讀 AGENTS、state、單一 primary Rule 與該 Rule 明列的 current artifacts。

## Affected Files

- `AGENTS.md`
- `ai/README.md`
- `ai/rules/**/*.md`
- `ai/tutorial/developer-guide.md`
- `docs/tasks/DOCS-0907-context-budget/*`

## Implementation Steps

1. 將 AGENTS 改寫為 compact constitution/router。
2. 在 `ai/README.md` 建立 phase-to-rule 與最小 context 表。
3. 將各 Rule 改寫成一致的 phase contract，移除重複內容。
4. 將 coding/testing/governance 文件改為 on-demand policy，不列為每次必讀。
5. 在 Tutorial 開頭加入 optional 與載入限制。
6. 加入結構化設計來源失效時的 screenshot evidence 全域邊界。
7. 驗證尺寸、必要語意、連結、YAML、Markdown fences 與 production/UI Task scope。

## Test Plan

- 比較前後 bytes/lines。
- 掃描 routing paths、Markdown local links、YAML 與 conflict markers。
- 確認 required lifecycle、failure categories、gates 與 allowed writes 仍存在。
- 確認 `app/`、UI-001、UI-002 未被本任務修改。

## Regression Plan

- 保留所有 canonical Rule/Template paths。
- 不移動或刪除 historical task artifacts。

## Risks

- 舊 Task 仍可能偏肥；本次只建立 history loading policy，不擅自搬動 active UI artifacts。

## Open Questions

- 無。
