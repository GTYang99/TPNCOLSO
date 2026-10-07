# Verification

## Purpose

Independently evaluate the approved acceptance criteria against the reviewed, committed revision and evidence required by the selected completion scope.

## Entry

- `next_action: verification`
- implementation/developer validation complete
- revision, diff, review, and all evidence required for the current development or release scope are available
- hosted CI is PASS, or is explicitly deferred for pre-release development with reason, owner, and reactivation milestone

## Load

- current requirement/AC, plan, and state
- implementation revision/diff and execution report
- code-review, local validation, and applicable CI results
- only tests/source evidence needed per AC
- [Testing](./testing.md)

## Output

Create/update `verification.md` from its template with revision, environment, per-AC evidence/result, regression/non-functional results, findings, limitations, and overall result.

## Decision

- PASS only when every required AC and gate has reproducible passing evidence.
- FAIL when evidence proves nonconformance; classify cause.
- NOT VERIFIED when required evidence cannot be obtained.
- A deferred hosted-CI gate is outside a Development Task's completion scope and is neither PASS nor NOT VERIFIED for task-scoped AC; it becomes required again at the recorded release milestone.
- Existing CI failures remain evidence. A failure indicating a product defect must be investigated and resolved before Development Task completion.
- Local validation must identify revision, environment, commands, and results and must never be labeled hosted CI PASS.

Include applicable Android compatibility, lifecycle, permission, accessibility, localization, security/privacy, performance, persistence/API compatibility, retry, and recovery checks.

## Exit

- PASS with `completion.scope: development`: set `completion.status: development_complete`, preserve deferred obligations, and use `next_action: none`.
- PASS with `completion.scope: release`: `release`.
- requirement/planning/implementation/environment/unknown failure: canonical route.
- NOT VERIFIED for evidence required by the selected scope: remain blocked.

## Restrictions

No production-code fixes, requirement edits, lowered AC, merge, signing, publishing, or deployment. Development Verification PASS authorizes downstream development only.
