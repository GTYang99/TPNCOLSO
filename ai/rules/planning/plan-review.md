# Plan Review

## Purpose

Decide whether the current plan and technical design can safely enter Implementation.

## Entry

- `next_action: plan_review`
- current requirement, analysis, plan, and state exist

## Load

- `requirement.md`, `analysis.md`, `plan.md`, `state.yaml`
- active Knowledge handoff when a reviewed decision depends on it
- exact architecture/API/design evidence needed for a finding

## Output

Create/update `plan-review.md` from its template. Record checklist, blocking findings, suggestions, responses, iteration, reviewed baseline/plan identity, and one result:

- `APPROVED`
- `REQUEST_CHANGES`
- `BLOCKED`

## Review

Check requirement/AC completeness, repository analysis, architecture ownership, dependencies, implementation steps, test/regression strategy, risks, security/data/lifecycle concerns, scope, rollback, and unresolved questions.

Confirm that `completion.scope` is explicit and that each deferred gate has a valid reason, owner, and release reactivation milestone. Reject any plan that relabels a required current-scope gate as deferred or not applicable.

Do not seek perfection; block only material implementation-readiness gaps. Suggestions do not block.

## Exit

- APPROVED: `implementation`
- REQUEST_CHANGES: `planning`
- BLOCKED by missing authority/source: matching Knowledge or clarification route
- Environment blocker: `infrastructure`

Approval applies only to the identified baseline and plan revision; later material changes invalidate it.

## Restrictions

No production code or requirement modification.
