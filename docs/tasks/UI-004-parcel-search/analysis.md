# Repository Analysis

## Current Behavior
- `MapShellRoute` exposes `MapShellCallbacks.onSearch` for the top keyword-search affordance. The debug `AppEntry` currently binds only logout, so this entry has no UI-004 destination.
- UI-003 Knowledge Resolution (`KCF-UI003-003`, `KD-UI003-001`) assigns map-shell search/filter affordances to UI-003 and query/results/summary to UI-004. Current `MapShellScreen` renders a top keyword-search action and a lower-right location affordance; it does not render a lower-right search/filter control. On 2026-10-02 the requester clarified that search/filter entry belongs in the top toolbar, lower-right is location only, and successful search opens a bottom card-style page. `MapNavigationClicked` belongs to the selected map fixture, not a search/filter entry.
- `MapShellViewModel` owns only map-shell state and callback events; no parcel search feature/data source exists in `app/src`.
- Foundation provides shared `AppTextField`, `AppEmptyContent`, `AppErrorContent`, status badge and theme tokens. No existing parcel summary sheet or replaceable parcel source was found.

## Expected Behavior
- Add a parcel-search feature with immutable UI state and explicit events; it receives map-shell callbacks and presents query, match, no-match and summary states.
- Keep fake parcel fixtures behind a feature-owned replaceable source, outside Composables; preserve the existing map shell and its overlay when the query changes.

## Affected Modules
- `app/src/main/java/com/example/tp_ncolso_android/feature/parcelsearch/` — new feature contract, ViewModel, screen and fake data source.
- `app/src/debug/java/com/example/tp_ncolso_android/AppEntry.kt` — connect the approved UI-003 search callback to UI-004 within the debug signed-in host; no release/session behavior expansion.
- `app/src/main/java/com/example/tp_ncolso_android/feature/mapshell/MapShellContract.kt` and `MapShellViewModel.kt` — current state already has `overlay.activeQuery` and `overlay.selectedTarget`, but no event to update them from a search result; the Plan proposes a UI-003-owned generic `UpdateSearchContext(query, selectedTarget)` event for Plan Review approval.
- `app/src/test/java/.../feature/parcelsearch/` — query matching and state transition unit tests.
- `app/src/androidTest/java/.../feature/parcelsearch/` — Compose search, no-result and summary interaction tests.
- Existing `feature/mapshell` contract/screen and tests — UI-003-owned dependency only; any missing affordance/callback must be resolved under UI-003 ownership, not implemented as duplicate UI-004 shell chrome.

## Dependencies
- `KB-UI-004-PARCEL-SEARCH-R2`, parent `FR-005` / `AC-006`, and the current Figma frames as visual observations.
- UI-003 callback contract and the existing foundation components/theme.
- UI-003 map overlay state handoff for the parent AC's locate-and-preserve-context behavior.
- Deterministic fake parcel records with fields needed for search and the summary header; exact values must stay task fixtures, not production records.

## Risks
- The parent FR-003 text can be read as placing both location and parcel search/filter at lower right; the requester's 2026-10-02 clarification establishes the active UI-004 direction and is recorded in `requirement.md` / `issue-log.md`.
- The Figma Copy revision is not formally approved, so pixel-level visual acceptance has a source-authority limitation.
- UI-003 Verification now passes: hosted run `36948553870` completed both jobs successfully on commit `00d5d1e`. The upstream gate is clear; UI-004 remains subject to its Plan Review, own callback/state-handoff design, and safe task isolation.
- The working tree contains unrelated existing changes on `UI-003feat`; Implementation must establish safe task isolation before any production edits.

## Unknowns
- Formally approved Figma revision for visual acceptance; pixel parity remains deferred.
- The eventual implementation branch.

## Plan Return Root-Cause Assessment — 2026-10-02

### Review Record Check
- At this planning analysis pass, `state.yaml` recorded `phase: planning`, `status: plan_in_progress`, `plan_review.status: pending`, and `next_action: planning`; it contained no return reason or reviewer finding.
- `state.yaml` now marks only the minimal UI-003 map-state handoff approval (`PLN-UI004-003`) as blocking. The search-entry placement finding (`PLN-UI004-001`) was resolved by the requester and removed from the blocker list.
- No separate Plan Review artifact is present, and the state does not explicitly record `returned`; therefore reviewer-specific wording cannot be verified. The plan blockers themselves are clear and are analyzed below.

### Root Causes Affecting Plan Readiness
1. **The previous Plan draft put the search/filter entry in the wrong location.** It treated the lower-right search/filter control as unresolved even though the requester clarified that search/filter belongs in the top toolbar and lower-right is location only. `requirement.md`, `plan.md`, and `PLN-UI004-001` now record the clarification; the unnecessary destination question is closed.
2. **The cross-task map-state handoff is not an approved contract yet.** The Plan proposes changing UI-003's contract/ViewModel to update `activeQuery` and `selectedTarget`; `PLN-UI004-003` says that handoff still needs agreement. This leaves ownership and the exact generic event/API shape provisional.
3. **The traceability table duplicates AC-UI004-006.** The duplicate row combines callback validation with selected-target/context handoff, although the handoff is required by AC-UI004-002. This obscures which acceptance criterion owns each implementation and validation obligation. The table has now been consolidated: map-state validation is under AC-UI004-002 and the single AC-UI004-006 row covers callback ownership and entry.
4. **The Plan's Open Questions included a resolved prerequisite.** UI-003 verification was already PASS in the Knowledge Validation addendum, but the Plan still listed it as an open question. That stale item has now been removed; task isolation remains a risk to address before Implementation.

### Non-root-cause Limitation
- Formal Figma revision approval is unknown, but the Plan explicitly limits the task to observable behavior and defers pixel-parity claims. On current evidence this is a recorded limitation, not a blocker to the UI-only development plan, unless the reviewer requires formal pixel acceptance.
- UI-003 Verification is recorded as passed in the current Knowledge Validation addendum; it is not a remaining root cause.
- Release signed-in integration is not available in the current repository: release `AppEntry` supplies empty signed-in content. The Plan limits task wiring to the debug host and records release integration as a deferred obligation with an owner and reactivation milestone; this does not lower UI-004's development AC.

### Readiness Conclusion
- The Plan now places entry in the top toolbar, preserves the lower-right location-only action, and specifies the bottom card-style summary after a match. It defines a proposed generic UI-003 state handoff and maps state-preservation validation to AC-UI004-002. The handoff remains pending Plan Review; PLN-UI004-003 is the only planning blocker.
- The parent FR-003 wording differs from the requester's explicit clarification. The active task requirement records the clarification; no interaction is inferred from Figma.
- Next action: Plan Review. The Plan Agent has not made the reviewer decision for the UI-003 handoff. Formal Figma approval remains a limitation only while pixel parity is explicitly deferred. Release signed-in host integration resumes when the release session host is implemented, before release-candidate validation.
