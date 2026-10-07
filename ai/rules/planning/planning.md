# Planning

## Purpose

Turn an approved requirement and active Knowledge baseline into an actionable, testable implementation plan.

## Entry

- `next_action: planning`
- Knowledge is Ready or explicitly `not_required`.
- Requirement intent is sufficiently authoritative.

## Load

- current `requirement.md` and `state.yaml`
- current Knowledge Validation handoff, when used
- relevant architecture and exact repository/test areas
- applicable product/design/API/asset sources on demand

## Output

Create/update:

- `requirement.md` with scope and numbered observable AC
- `analysis.md`
- `plan.md` from `ai/templates/task/plan.md`
- `issue-log.md` when gaps exist
- `state.yaml`

## Plan Contract

Cover current/expected behavior, affected files/ownership, dependencies, implementation steps, technical design, test/regression plan, risks, rollback/recovery, security/data/lifecycle concerns, and open questions proportional to risk.

Classify Task as `feature`, `bugfix`, `debug`, `refactor`, `hotfix`, or `docs`. Debug may use `root-cause.md` and `fix-plan.md` instead of a full feature plan.

## Exit

- Actionable and no material unresolved decision: route to `plan_review`.
- Missing/stale source: Knowledge Collection.
- Conflicting source/decision: Knowledge Resolution.
- Requirement gap: Planning or requirement clarification based on authority needed.
- Environment prevents analysis: Infrastructure.

## Restrictions

Planning never modifies production code, lowers AC, or includes unrelated refactoring.
