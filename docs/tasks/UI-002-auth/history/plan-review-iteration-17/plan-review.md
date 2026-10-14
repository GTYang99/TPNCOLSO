# Plan Review

Task: UI-002-auth  
Reviewer: Codex  
Review Iteration: 17  
Review Date: 2026-10-14 (Asia/Taipei)

---

## Review Result

- [ ] APPROVED
- [x] REQUEST_CHANGES
- [ ] BLOCKED

The R11 scope and most implementation boundaries are explicit, but revision 17 has material inconsistencies with its active Login requirement and current R10 implementation. Correct the requirement/plan references, make post-CAPTCHA visual acceptance internally consistent, refresh current behavior and rollback, and declare development scope/deferred obligations before implementation.

Reviewed identity: baseline `KB-UI-002-AUTH-R11`; requirement revision 4; plan revision 17; branch `UI-002feat`; latest committed revision `48670ffe9e33b19aea23709abf2f47cc5ed85ff1`. These artifacts are in the current uncommitted working tree; review applies to the hashes below, not a committed R11 revision.

- `requirement.md` SHA-256: `9AF3AD0CFCD06286887F8F4880BF68995DC6C15AE6209BBFBFE487A5C47A4DCB`
- `analysis.md` SHA-256: `E7CD052FAE9846757FED1A63864BAF8384AF1328F0CCB0DDD472FF7358270B4C`
- `plan.md` SHA-256: `46C8C7B09EE5ADDAF2D307CECC963DC4EF09AD40C9AB9B651CAA1B1B2A94AC89`

## Review Checklist

| Item | Result | Notes |
|---|---|---|
| Requirement understood | PASS | R11 removes Login CAPTCHA and changes the auth error copy; unaffected Register/Logout behavior remains in scope. |
| Acceptance Criteria complete | FAIL | R11 removes the row, but some active visual criteria still require old composite vertical anchors. |
| Repository analysis complete | FAIL | Plan says UI-002 auth feature/tests do not exist, while R10 auth code and tests are present and CAPTCHA-dependent. |
| Architecture impact reasonable | PASS | Main/debug/release separation, ViewModel ownership, and session coordinator boundaries are explicit. |
| Affected modules identified | PASS | Auth, test, preview, debug host, release boundary and requirements are listed. |
| Dependencies identified | PASS | UI-001 handoff, UI-003 logout callback, and INT-001 API/session ownership are described. |
| Risks evaluated | PASS | Sensitive state, source-set leakage, visual identity, duplicate transitions and rollback are considered. |
| Test Plan complete | FAIL | R11 visual tests need an explicit rule for unaffected screenshot regions versus the reflowed post-CAPTCHA composition. |
| Regression Plan complete | PASS | Existing auth/session behavior, CAPTCHA removal, debug/release isolation and UI-003 callbacks are covered. |
| Open Questions documented | PASS | No product question is claimed open; implementation gates are stated separately. |
| Implementation steps actionable | FAIL | Step 7 names Login requirement revision 3 instead of active revision 4; logout interface also points to revision 3. |
| Task size appropriate | PASS | Scope stays within auth UI/session boundary and excludes map/API implementation. |
| Rollback strategy | FAIL | Current plan says reverting this task restores the UI-001 shell, although the committed R10 UI-002 implementation already exists; rollback must preserve the active CAPTCHA-removal requirement. |
| Security, data and lifecycle | PASS | Password and fake-data restrictions plus immediate local logout are described. |
| Completion scope and deferred gates | FAIL | Plan does not declare `completion.scope` or list the deferred hosted-CI gate with reason, owner and reactivation milestone. |

## Findings

### Finding 1 — R11 visual criteria conflict with old screenshot anchors

- Severity: Major
- Category: requirement_gap / planning_gap
- Description: `login-ui-requirement.md` revision 4 says post-removal vertical anchors are unknown and the R10 composite cannot establish them. However UIR-LOGIN-001/007/008 still require screenshot vertical anchors, while plan revision 17 also promises composite-authoritative comparisons. R11 cannot satisfy both instructions after deleting the 48dp CAPTCHA row.
- Recommendation: In Planning, retain the composite as authority only for unchanged regions; define reflow of remaining controls using the existing spacing tokens, explicitly remove old downstream y-anchor claims, and validate the resulting R11 layout without claiming R10 pixel parity.
- Status: Open

### Finding 2 — Stale Login requirement revisions and border count

- Severity: Major
- Category: planning_gap / traceability
- Description: Plan step 7 cites `login-ui-requirement.md` revision 3; active R11 is revision 4. The Logout contract also points to Login revision 3. The Login traceability row for UIR-LOGIN-006 expects three error borders, while revision 4 specifies only account and password borders.
- Recommendation: Update the plan and affected Logout interface reference to revision 4, and align the error-border test with the two approved fields.
- Status: Open

### Finding 3 — Current behavior and rollback do not match the repository

- Severity: Major
- Category: planning_gap / repository analysis
- Description: Plan says no UI-002 auth feature, Login/Register state holder or auth tests exist. Current source and tests contain the R10 implementation, including CAPTCHA state/provider/UI/tests, and task state records committed revision `48670ff`. Reverting the entire task to the UI-001 shell would discard unaffected R10 behavior and does not describe a safe R11 rollback.
- Recommendation: Describe R10 as the current behavior. Scope rollback to the R11 change and keep Release on its signed-out placeholder; do not restore CAPTCHA as an accepted active behavior.
- Status: Open

### Finding 4 — Completion scope and deferred hosted-CI gate are absent from the plan

- Severity: Major
- Category: planning_gap / completion scope
- Description: `state.yaml` sets development scope and defers hosted CI, but `plan.md` does not declare the scope or identify the deferred gate, reason, owner and reactivation milestone as required before Plan Review.
- Recommendation: Add `completion.scope: development` and a deferred-obligations table consistent with state and `AGENTS.md`; preserve the recorded R10 CI failure and do not report it as R11 PASS.
- Status: Open

## Blocking Issues

- Active visual acceptance is contradictory for the changed Login composition.
- Requirement version references and error-border mapping are stale.
- Current behavior/rollback descriptions are inaccurate for the committed R10 implementation.
- Completion scope/deferred hosted-CI obligation are missing from the plan.

## Improvement Suggestions

- Keep the pre-release CI state recorded as `not_verified`/deferred; the project contract allows hosted CI deferral for development scope when owner and reactivation milestone are explicit.

## Decision

### REQUEST_CHANGES

Return to Planning. This review does not authorize implementation. No production code or requirement was modified during this review.

## Next Action

- [x] Planning
- [ ] Implementation
- [ ] Requirement Clarification
- [ ] Human Review

## State Update

```yaml
phase: planning
status: plan_in_progress
plan_review:
  status: changes_requested
  result: REQUEST_CHANGES
  iteration: 17
next_action: planning
```

## Definition of Done

- [x] All checklist items reviewed
- [x] Findings documented
- [x] Blocking issues identified
- [x] Suggestions separated
- [x] Decision recorded
- [x] State updated
