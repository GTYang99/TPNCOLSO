# Knowledge Resolution

## Question and Scope
- Task question: 建立易於導航且與 SDLC／Knowledge contract 一致的 canonical folder layout。
- In scope: 規則分類、範本分類、Tutorial assets、專案知識與所有引用。
- Out of scope: 產品規格語意、production code、歷史 Task 刪除。

## Candidate Knowledge Baseline
- Baseline ID: KB-DOCS-0907-R1
- Created date: 2026-09-07
- Supersedes: current flat `ai/` and root product-spec layout
- Status: draft

## Source Register

| Source ID | Title / Location | Type | Domain / Owner | Version / Date | Authority | Freshness | Scope / AC | Notes |
|---|---|---|---|---|---|---|---|---|
| SRC-001 | AGENTS | workflow | Process | working copy | authoritative | current | all | defines paths |
| SRC-002 | ai inventory | repository | Process | working copy | observed | current | migration | all files inventoried |
| SRC-003 | docs inventory | repository | Project | working copy | observed | current | migration | tasks preserved |
| SRC-004 | land survey specs | product | Product | v1 | authoritative | current | product | move without semantic changes |

## Material Claims and Traceability

| Claim ID | Statement | Supporting Sources | Contradicting Sources | Authority / Confidence | Requirement / AC | Status |
|---|---|---|---|---|---|---|
| KCL-001 | AI workflow assets belong under `ai/` grouped by purpose | SRC-001, SRC-002 | none | high | AC-001 | resolved |
| KCL-002 | Project knowledge belongs under `docs/` grouped by domain | SRC-001, SRC-003, SRC-004 | current root layout | high | AC-002 | resolved |
| KCL-003 | Historical tasks must remain intact | user scope, SRC-003 | none | authoritative | AC-003 | resolved |

## Conflicts and Gaps

| Conflict ID | Claims / Sources | Domain | Impact / Severity | Options | Decision Owner | Status |
|---|---|---|---|---|---|---|
| KCF-001 | root product specs conflict with `docs/product/` convention | Product | navigation / Major | move under named product folder | User / workflow owner | resolved |

## Decision Records

### KD-001

- Decision: `ai/` 分為 `rules/`、`templates/`、`tutorial/`；規則按生命週期 domain 分組。
- Alternatives: 保持平鋪；僅增加 README。
- Source / Conflict IDs: SRC-001, SRC-002
- Rationale: 降低入口噪音並讓角色規則可快速定位。
- Approver and date: user request, 2026-09-07
- Affected requirements / AC / artifacts: AGENTS、所有 rules/templates/tutorial references。
- Supersedes: flat ai layout
- Revalidation trigger: 新增 lifecycle domain 或 canonical artifact 類型。

### KD-002

- Decision: architecture 與產品規格移入 `docs/architecture/`、`docs/product/land-survey-115/`。
- Alternatives: 保留 root compatibility copies。
- Source / Conflict IDs: SRC-003, SRC-004, KCF-001
- Rationale: 單一 Source of Truth，避免重複副本漂移。
- Approver and date: user request, 2026-09-07
- Affected requirements / AC / artifacts: AGENTS、Tutorial、產品內部連結。
- Supersedes: root product layout
- Revalidation trigger: 外部工具硬編碼舊路徑。

## Assumptions

| Assumption ID | Statement / Evidence | Confidence | Impact if Wrong | Owner | Validation Method | Expiry / Trigger | Safe for Planning |
|---|---|---|---|---|---|---|---|
| KA-001 | 沒有外部系統依賴本機舊路徑 | medium | 外部連結需後續更新 | Project owner | 搜尋 repository 引用；保留 migration map | 發現外部引用 | yes |

## Resolved Items
- Canonical taxonomy、搬移清單、引用更新與驗證方式已確定。

## Unresolved Non-blocking Items
- Git metadata 缺失，無法提供 rename-aware diff。

## Blocking Items
- None for local implementation.

## Downstream Impact
- Requirements / AC: 文件結構 AC。
- Plan / design: 更新所有路徑與檔案樹。
- Code / data / API: 無語意變更。
- Tests / verification: 檔案數量、引用、YAML、Markdown、legacy-path scan。
- Release / operations: Git/CI 維持 NOT VERIFIED。

## Revalidation Triggers
- 搬移後缺檔、失效引用、canonical path 不一致或產品索引無法導航。

## Validation Handoff
- Candidate baseline: KB-DOCS-0907-R1
- Decisions and constraints Validation must check: 分層清晰、單一來源、歷史 Task 保留、路徑完整。
- Safe assumptions: KA-001
- Required follow-up evidence: pre/post inventory and reference scan
- Material blockers: no
- Ready for Validation: yes
