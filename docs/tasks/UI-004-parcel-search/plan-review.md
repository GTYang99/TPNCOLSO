# Plan Review

Task: `UI-004-parcel-search`
Reviewer: Codex Plan Critic (same agent; independent reviewer unavailable)
Review Iteration: 1
Review Date: 2026-10-02 (Asia/Taipei)
Reviewed Plan: `docs/tasks/UI-004-parcel-search/plan.md` (SHA-256 `c53c1b051435c847d0043d13e5507328b2e6e3bbbd94a2b09a74c754b6fa5cf1`)
Reviewed Baseline: `KB-UI-004-PARCEL-SEARCH-R2`
Reviewed Working State: `UI-003feat` dirty worktree; the reviewed UI-004 plan and related current artifacts were uncommitted. No separate `v0.1` artifact exists; this review applies to R2.

---

# Summary

## Review Result

- [x] APPROVED
- [ ] REQUEST_CHANGES
- [ ] BLOCKED

## Summary

The UI-004 plan is implementation-ready for its UI-only development scope. It defines the clarified top-toolbar entry and lower-right location-only action, keeps fake parcel data replaceable, and specifies a generic host-mediated map-state handoff that updates only the existing active-query and selected-target fields. The handoff is approved for implementation with the planned state-preservation tests. This approval does not authorize release, merge, signing, publishing or deployment.

# Review Checklist

| Item | Result | Notes |
|---|---|---|
| Requirement understood | PASS | Search inputs, match/no-match behavior, bottom card summary, fake-data boundary and UI ownership are explicit in R2. |
| Acceptance Criteria complete | PASS | AC-UI004-001 through AC-UI004-006 are numbered and observable; the summary-header assertion can be made more explicit as a non-blocking improvement. |
| Repository analysis complete | PASS | Existing top search callback, lower-right location control, map state fields, debug host and absent release signed-in host are identified. |
| Architecture impact reasonable | PASS | The composition root owns the shared MapShellViewModel and translates a generic callback to a UI-003 event; parcel models do not cross the map-shell boundary. |
| Affected modules identified | PASS | Feature, debug host, UI-003 state contract/ViewModel, related tests and execution report are listed. |
| Dependencies identified | PASS | R2 baseline, UI-003 callback and prerequisite verification, foundation components and deferred INT-002 API work are accounted for. |
| Risks evaluated | PASS | Worktree isolation, visual authority, release host, fake matching semantics and cross-task state preservation are addressed. |
| Test Plan complete | PASS | Unit, Compose, map-shell state-preservation and callback/location regression checks map to the task ACs. |
| Regression Plan complete | PASS | Basemap, location, drawer, logout/session, map context and duplicate-control behavior are covered. |
| Open Questions documented | PASS | Only formal Figma revision approval remains open and is explicitly outside pixel-parity claims for this scope. |
| Implementation steps actionable | PASS | The approved Gate 0 handoff, feature source/state, UI, host wiring and tests are sequenced. |
| Task size appropriate | PASS | Parcel search remains UI-only; API integration and release signed-in host integration stay deferred to their owning work. |
| Rollback strategy (if applicable) | PASS | Rollback is limited to task-scoped feature, debug wiring and generic handoff commits while preserving unrelated work. |

# Findings

## Finding 1

Severity:
- [ ] Critical
- [x] Major
- [ ] Minor
- [ ] Suggestion

Category: Cross-task architecture / map-state handoff

Description: The current UI-003 contract has `overlay.activeQuery` and `overlay.selectedTarget`, but no event to update them from a search result. The plan proposes a generic host callback carrying `(query, selectedTarget)` and a UI-003-owned `UpdateSearchContext` event that copies only those two fields. The composition root keeps the same MapShellViewModel alive and the plan includes tests for preservation of all other map-shell state.

Recommendation: Approve and implement this boundary as written. Keep parcel-specific models out of UI-003 and retain the planned MapShellViewModel and host callback tests.

Planning Response: Reviewer approves the proposed interface and state ownership for this task. This resolves the Plan Review decision for `PLN-UI004-003`; runtime behavior and state preservation still require implementation validation.

Status:
- [ ] Open
- [x] Resolved

# Blocking Issues

None for Plan Review approval. Implementation must establish safe task isolation before production edits because the current `UI-003feat` worktree contains unrelated changes.

# Improvement Suggestions

- Make the AC-UI004-004 summary assertions enumerate the visible header values (`status`, `key_no`, `land_no`, and `site_condition`) so test expectations are directly visible in the task artifacts.
- Continue labeling Figma-derived comparisons as visual observations unless a formally approved revision is supplied; do not claim pixel parity under this plan.

# Decision

## APPROVED

Implementation may begin for the approved UI-only UI-004 scope after safe task isolation is established. This approval applies to `KB-UI-004-PARCEL-SEARCH-R2` and the reviewed plan hash above. Material changes to the UI ownership split, map-state handoff, scope or acceptance criteria require a new Plan Review.

# Next Action

- [ ] Planning
- [x] Implementation
- [ ] Requirement Clarification
- [ ] Human Review

# State Update

```yaml
phase: implementation
status: ready
planning:
  status: completed
plan_review:
  status: approved
  result: APPROVED
  iteration: 1
blocking: []
next_action: implementation
```

# Definition of Done

- [x] All checklist items reviewed
- [x] Findings documented
- [x] Blocking Issues identified
- [x] Improvement Suggestions separated
- [x] Decision recorded
- [x] state.yaml updated
