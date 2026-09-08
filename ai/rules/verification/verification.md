# Verification

## Purpose

Independently evaluate the approved acceptance criteria against the reviewed, committed revision and CI evidence.

## Entry

- `next_action: verification`
- implementation/developer validation complete
- revision, diff, review, and required CI evidence available or explicitly `NOT VERIFIED`

## Load

- current requirement/AC, plan, and state
- implementation revision/diff and execution report
- code-review and CI results
- only tests/source evidence needed per AC
- [Testing](./testing.md)

## Output

Create/update `verification.md` from its template with revision, environment, per-AC evidence/result, regression/non-functional results, findings, limitations, and overall result.

## Decision

- PASS only when every required AC and gate has reproducible passing evidence.
- FAIL when evidence proves nonconformance; classify cause.
- NOT VERIFIED when required evidence cannot be obtained.

Include applicable Android compatibility, lifecycle, permission, accessibility, localization, security/privacy, performance, persistence/API compatibility, retry, and recovery checks.

## Exit

- PASS: `release`.
- requirement/planning/implementation/environment/unknown failure: canonical route.
- NOT VERIFIED: remain blocked; never advance to Release.

## Restrictions

No production-code fixes, requirement edits, lowered AC, merge, or deployment.
