# AI Workflow Index

`AGENTS.md` is the mandatory workflow entry. This directory contains detailed rules, canonical templates, and the explanatory tutorial.

## Quick Start

1. Read [`AGENTS.md`](../AGENTS.md).
2. Read the latest task [`state.yaml`](../docs/tasks/) and follow `next_action`.
3. Open the matching rule below.
4. Create task artifacts from the canonical template.
5. Use the [Developer Guide](./tutorial/developer-guide.md) for examples and the five-minute project overview.

## Rules

| Domain | Rules |
|---|---|
| Knowledge | [Collection](./rules/knowledge/collection.md), [Resolution](./rules/knowledge/resolution.md), [Validation](./rules/knowledge/validation.md) |
| Planning | [Planning](./rules/planning/planning.md), [Plan Review](./rules/planning/plan-review.md) |
| Implementation | [Developer](./rules/implementation/developer.md), [Coding](./rules/implementation/coding.md), [Debug](./rules/implementation/debug.md) |
| Verification | [Testing](./rules/verification/testing.md), [Verification](./rules/verification/verification.md) |
| Operations | [Infrastructure](./rules/operations/infrastructure.md), [Investigation](./rules/operations/investigation.md), [Release](./rules/operations/release.md) |
| Governance | [Git](./rules/governance/git.md), [Issue Management](./rules/governance/issue-management.md), [Template Rules](./rules/governance/templates.md) |

## Templates

- `templates/knowledge/`: Collection, Resolution, and Validation artifacts.
- `templates/task/`: Requirement, analysis, plan, plan review, state, and verification artifacts.

## Project Knowledge

- [Architecture](../docs/architecture/overview.md)
- [115 年度土地調查產品規格](../docs/product/land-survey-115/README.md)
- [Design](../docs/design/)
- [API](../docs/api/)
- [Assets](../docs/assets/)
- [Tasks](../docs/tasks/)

## Canonical Structure

```text
ai/
├── README.md
├── rules/
│   ├── knowledge/
│   ├── planning/
│   ├── implementation/
│   ├── verification/
│   ├── operations/
│   └── governance/
├── templates/
│   ├── knowledge/
│   └── task/
└── tutorial/
    ├── developer-guide.md
    └── assets/
```

## Migration Map

| Former location | Canonical location |
|---|---|
| `ai/*-rules.md` | `ai/rules/<domain>/*.md` |
| `ai/templates/*-template.*` | `ai/templates/knowledge/` or `ai/templates/task/` |
| `ai/codexAgentsDeveloperTutorial.md` | `ai/tutorial/developer-guide.md` |
| `ai/architecture.md` | `docs/architecture/overview.md` |
| root `115年度新工處轄管土地調查作業*` | `docs/product/land-survey-115/` |

The migration map is historical guidance only. New references MUST use canonical locations.
