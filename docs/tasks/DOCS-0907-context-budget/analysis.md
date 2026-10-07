# Analysis

## Current State

| Scope | Size | Lines |
|---|---:|---:|
| `AGENTS.md` | 33,335 bytes | 643 |
| `ai/rules/` | 55,015 bytes | 2,498 |
| Tutorial | 30,425 bytes | 958 |
| UI-001 active folder | 63,570 bytes | 931 |

## Duplication

- Knowledge、Planning、Verification、Release、Issue 與 State 規則同時存在於 AGENTS 與 phase Rules。
- 多份 Rules 重複完整 YAML、全域 gates、禁止事項及其他 phase 的 routing。
- Tutorial 重述完整 workflow，但其用途應只是說明與範例。
- Active Task 可能把 superseded 內容留在 canonical filename，造成 current/historical context 混讀。

## Target Architecture

```text
AGENTS.md                 global invariants + loading/router
ai/README.md              navigation and phase lookup
ai/rules/<domain>/<phase> one phase contract
ai/templates/             artifact/state schema
ai/tutorial/              optional examples only
docs/tasks/<id>/           current task evidence
docs/tasks/<id>/history/   superseded evidence, on demand
```

## Risks

- 過度精簡可能遺失 gate；以語意清單和路徑驗證防止。
- 現有 historical artifacts 可能引用舊段落；保留 canonical file paths，不依 heading 建立流程依賴。
- 工作樹已有其他變更；只修改 `AGENTS.md`、`ai/` 與本 Task folder。
