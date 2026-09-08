# Implementation

## Purpose

Implement only the approved plan, then produce reproducible developer-validation evidence.

## Entry

- `next_action: implementation` or `implementation_debug`
- requirement baseline and Plan Review are approved
- branch/worktree isolation is safe

## Load

- `state.yaml`, current requirement, plan, and Plan Review
- active Knowledge decisions cited by the plan
- only affected code/tests
- [Coding](./coding.md) for production code
- [Testing](../verification/testing.md) when selecting/running checks
- [Runtime](../operations/runtime.md) before any Gradle command
- [Git](../governance/git.md) for commit work

## Work

1. Confirm scope and clean isolation from unrelated changes.
2. Implement the smallest plan-aligned change.
3. Add/update tests for changed behavior.
4. Run targeted checks, then affected regression/build checks.
5. Record commands, results, limitations, diff scope, and decisions in `execution-report.md`.
6. Commit the approved scope when Git is available and record revision evidence.

Any necessary out-of-plan production change stops Implementation and returns to Planning.

## Exit

- Implementation and developer validation complete: commit, code review/CI, then `verification`.
- Implementation defect found locally: remain in Implementation.
- Requirement/planning defect: canonical failure route.
- Environment-only failure: `infrastructure`.

Unavailable validation is `NOT VERIFIED`, never PASS.

## Restrictions

Do not change requirements, lower AC, skip required tests, expose secrets, perform unrelated refactoring, merge, deploy, or modify production configuration.
