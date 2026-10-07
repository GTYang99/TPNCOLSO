# Execution Report

## Result

- `AGENTS.md` 已改為 compact constitution/router。
- Phase Rules 已改為單一階段契約。
- Coding、Testing、Git、Issue、Template 已改為 on-demand policies。
- Tutorial 已改為 132 行短版；原長版移至 `ai/tutorial/history/`。
- Canonical Rule/Template paths 均保留。
- AGENTS 已加入 Figma MCP 不完整／不可信時的 screenshot evidence 降級邊界。
- 新增共用 Runtime policy；Gradle 統一使用 Android Studio JBR，避免各 phase 複製 JDK 路徑。

## Size Comparison

| Scope | Before | After | Reduction |
|---|---:|---:|---:|
| `AGENTS.md` | 33,335 B / 643 lines | 7,061 B / 175 lines | 78.8% bytes |
| `ai/rules/` | 55,015 B / 2,498 lines | 18,915 B / 552 lines | 65.6% bytes |
| canonical Tutorial | 30,425 B / 958 lines | 4,433 B / 132 lines | 85.4% bytes |
| mandatory entry + router | 36,087 B | 10,476 B | 71.0% bytes |

## Loading Model

```text
AGENTS.md
-> current state.yaml
-> one primary Rule
-> current Rule inputs
-> exact evidence on demand
```

The Tutorial, supporting policies, source domains, history, and unrelated Task artifacts are not preloaded.

## Scope Protection

- No file under `app/` was changed by this Task.
- Existing UI-001/UI-002 working-tree changes were not edited by this Task.
- No commit was created because the current feature branch contains unrelated in-progress UI changes.
