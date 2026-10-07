# AI Workflow Index

`AGENTS.md` is the mandatory compact contract. Load the rest progressively from the current Task state.

## Minimal Load

```text
AGENTS.md
-> docs/tasks/<task-id>/state.yaml
-> one primary Rule selected by next_action
-> only that Rule's current inputs
-> source/code evidence on demand
```

Never preload every Rule, template, tutorial, source document, or Task artifact.

## Phase Router

| `next_action` | Primary Rule | Normally read from current Task |
|---|---|---|
| `knowledge_collection` | [Collection](./rules/knowledge/collection.md) | requirement/request, current source register if any |
| `knowledge_resolution` | [Resolution](./rules/knowledge/resolution.md) | collection, unresolved claims/conflicts |
| `knowledge_validation` | [Validation](./rules/knowledge/validation.md) | collection, resolution, cited evidence |
| `planning` | [Planning](./rules/planning/planning.md) | requirement, active Knowledge handoff, relevant architecture/code |
| `plan_review` | [Plan Review](./rules/planning/plan-review.md) | requirement, analysis, plan, state |
| `implementation` / `implementation_debug` | [Developer](./rules/implementation/developer.md) | approved requirement, plan/review, state |
| `debug` | [Debug](./rules/implementation/debug.md) | failed AC, Verification, relevant evidence |
| `verification` | [Verification](./rules/verification/verification.md) | requirement, plan, revision/diff, validation/CI evidence |
| `infrastructure` | [Infrastructure](./rules/operations/infrastructure.md) | failing operation and environment evidence |
| `investigation` | [Investigation](./rules/operations/investigation.md) | issue and observations |
| `release` / `human_release` | [Release](./rules/operations/release.md) | verified revision/artifact and release evidence |

## On-demand Policies

Read only when applicable:

- Production coding: [Coding](./rules/implementation/coding.md)
- Test selection/execution: [Testing](./rules/verification/testing.md)
- Gradle/Android JDK execution: [Runtime](./rules/operations/runtime.md)
- Branch/commit work: [Git](./rules/governance/git.md)
- A blocker or defect exists: [Issue Management](./rules/governance/issue-management.md)
- Creating an artifact: [Template Rules](./rules/governance/templates.md) and the matching template

## Canonical Templates

- Knowledge: `templates/knowledge/`
- Task lifecycle and state: `templates/task/`

Templates define shape; Rules define behavior; AGENTS defines global invariants. None should duplicate the others.

## Project Knowledge

- [Architecture](../docs/architecture/overview.md)
- [Product](../docs/product/land-survey-115/README.md)
- [Design](../docs/design/)
- [API](../docs/api/)
- [Assets](../docs/assets/)
- [Task Index](../docs/tasks/README.md)

## Optional Material

[Developer Guide](./tutorial/developer-guide.md) is explanatory and optional. Read only the relevant section when the Rule is insufficient for understanding; it never overrides the contract, Rule, template, or approved Task artifacts.

## Migration Map

Historical path mapping remains documented here only:

| Former location | Canonical location |
|---|---|
| `ai/*-rules.md` | `ai/rules/<domain>/*.md` |
| `ai/templates/*-template.*` | `ai/templates/knowledge/` or `ai/templates/task/` |
| `ai/codexAgentsDeveloperTutorial.md` | `ai/tutorial/developer-guide.md` |
| `ai/architecture.md` | `docs/architecture/overview.md` |
| root product export | `docs/product/land-survey-115/` |
