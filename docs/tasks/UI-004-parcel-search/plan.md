# Implementation Plan

## Goal
- Implement the UI-only parcel keyword search and summary flow on the map shell using replaceable fake data.

## Completion Scope
- `completion.scope: development` — complete UI-004 through approved requirements and plan, implementation, local developer validation, commit, code review and task-AC Verification PASS. This scope does not authorize release, merge, signing, publishing or deployment.
- Deferred obligation: Hosted CI for the UI-004 release candidate. Reason: this is pre-release development scope and the active Project Contract permits hosted CI deferral for Development Complete. Owner: release/project maintainer. Reactivation milestone: before selecting or approving a release candidate.
- Deferred obligation: Production parcel API integration and integration tests. Reason: endpoint/schema/error contract is deferred to `INT-002`. Owner: INT-002/API owner. Reactivation milestone: after the API contract is approved and before release validation.
- Deferred obligation: Product UAT. Reason: not required to establish this UI-only Development Complete scope. Owner: Product/requester. Reactivation milestone: release-candidate acceptance, if required by release policy.
- Deferred obligation: release approval, authorized merge/deployment, post-deployment verification and monitoring. Reason: outside development scope and requires separate explicit authorization. Owner: release approver / deployment owner. Reactivation milestone: after Development Complete when a release candidate is proposed.
- Deferred obligation: Formal Figma revision approval and pixel-parity acceptance. Reason: current frames are usable only as visual observations and UI-004's development AC verify visible content/behavior without asserting formal pixel parity. Owner: Design/requester. Reactivation milestone: before any formal pixel-acceptance claim or if the approved requirement adds pixel-level acceptance.
- Deferred obligation: Release signed-in host integration. Reason: `app/src/release/java/com/example/tp_ncolso_android/AppEntry.kt` currently supplies an empty `signedInContent`, and the release authentication/session host is not implemented; this UI-004 development scope wires the feature in the debug signed-in host only. Owner: UI-002 / INT-001 authentication and release-integration owner. Reactivation milestone: when the release signed-in host is implemented and before release-candidate validation.

## Scope
- Query entry for land number, land location and `key_no`; matching parcel location/selection; retained-query no-result state; visible summary header.
- Update the existing UI-003 map overlay's selected target and active query on match, without introducing parcel-specific API types into the map-shell contract.
- Wire the existing top-toolbar search/filter callback to UI-004 in the signed-in host.
- Preserve the lower-right location-only control; UI-004 does not create or restyle map-shell controls.
- Plan Review iteration 1 approved the generic UI-003 map-state handoff described below; implementation must retain that reviewed boundary.

## Affected Files
- `app/src/main/java/com/example/tp_ncolso_android/feature/parcelsearch/ParcelSearchContract.kt` — immutable state, events and effects.
- `app/src/main/java/com/example/tp_ncolso_android/feature/parcelsearch/ParcelSearchViewModel.kt` — query flow and deterministic state transitions.
- `app/src/main/java/com/example/tp_ncolso_android/feature/parcelsearch/ParcelSearchScreen.kt` — Compose search and result presentation using foundation components/theme.
- `app/src/main/java/com/example/tp_ncolso_android/feature/parcelsearch/data/FakeParcelSearchDataSource.kt` — centralized replaceable fake records and matching.
- `app/src/main/java/com/example/tp_ncolso_android/feature/mapshell/MapShellContract.kt` and `MapShellViewModel.kt` — UI-003-owned generic `UpdateSearchContext(query, selectedTarget)` event that updates only existing overlay fields `activeQuery` and `selectedTarget`.
- `app/src/test/java/com/example/tp_ncolso_android/feature/mapshell/MapShellViewModelTest.kt` and `app/src/androidTest/java/com/example/tp_ncolso_android/feature/mapshell/MapShellScreenTest.kt` — verify the state transition, preservation of unrelated state, and route/callback behavior.
- `app/src/debug/java/com/example/tp_ncolso_android/AppEntry.kt` — retain one `MapShellViewModel` instance, host UI-004 over the existing map surface, and bridge a generic `(query, selectedTarget)` result callback to the UI-003 event without parcel types crossing the shell contract.
- `app/src/release/java/com/example/tp_ncolso_android/AppEntry.kt` — inspected as a release-host dependency; its signed-in content is currently empty and is explicitly deferred from this task's development scope.
- `app/src/main/java/com/example/tp_ncolso_android/feature/mapshell/MapShellScreen.kt` — reuse the existing top-toolbar search callback; retain the lower-right location-only control as UI-003-owned.
- `app/src/test/java/com/example/tp_ncolso_android/feature/parcelsearch/` — matching and state tests.
- `app/src/androidTest/java/com/example/tp_ncolso_android/feature/parcelsearch/` — Compose semantics and interaction coverage.
- `docs/tasks/UI-004-parcel-search/execution-report.md` — record implementation and developer validation evidence.

## Dependencies
- Active UI-004 Knowledge baseline `KB-UI-004-PARCEL-SEARCH-R2`; parent keyword behavior FR-005 / AC-006; requester placement/result decision `KD-UI004-003`.
- Existing UI-003 top-toolbar `onSearch` callback and lower-right `LocationClicked` action; UI-004 adds no map-shell controls.
- Plan Review iteration 1 approved the generic `UpdateSearchContext(query, selectedTarget)` handoff before any implementation changes to the UI-003 contract/ViewModel.
- Existing foundation text field, card/sheet, status, empty/error and theme components where behavior matches; deterministic fake parcel records owned by UI-004.
- UI-003 prerequisite verification passed on commit `00d5d1e` (hosted run `36948553870`). Production parcel API/DTO/error contract is deferred to INT-002.

## Technical Design
- Keep one `MapShellViewModel` and one `ParcelSearchViewModel` alive in the signed-in host so opening/dismissing the summary does not recreate map-shell state.
- UI-004 owns a small parcel-search source contract and synthetic fixture model; the Composable sends events and renders immutable state only.
- The signed-in composition root owns the shared `MapShellViewModel`, passes that same instance into `MapShellRoute`, and provides UI-004 a generic `onParcelSelected(query: String, selectedTarget: String)` callback. The host maps that callback to UI-003's proposed `MapShellEvent.UpdateSearchContext(query, selectedTarget)`; UI-004 parcel models never enter the map-shell contract.
- `UpdateSearchContext` copies only `overlay.activeQuery` and `overlay.selectedTarget`. Tests assert the remaining `MapOverlayFixture` fields, basemap, location state, drawer state and identity are unchanged.
- In this UI-only implementation, “locate” is evidenced by selecting/highlighting the matching synthetic map target; do not invent coordinates, map panning, real tiles or location API behavior.
- UI-003 owns map-shell affordances and entry callbacks; UI-004 owns query/result/card presentation. Search/filter entry is the top-toolbar callback; the lower-right control is location only.
- A successful match shows the card-style parcel summary from the bottom over the still-mounted map surface. A no-result response retains the keyword and shows “查無資料” without replacing the map.
- Use existing shared field, status, empty/error and theme primitives where their behavior matches.

## Plan Review Gate
- **Cross-task interface decision — PLN-UI004-003: APPROVED in Plan Review iteration 1.** Proposed generic callback/event path: UI-004 emits `(query, selectedTarget)` to the signed-in composition root; the root dispatches UI-003's `UpdateSearchContext`; UI-003 updates only the two existing overlay strings while preserving all other state. No parcel-specific type is added to UI-003. Plan Review approved this interface before implementation changes to the UI-003 contract/ViewModel.
- The requester clarified on 2026-10-02 that search/filter is in the full-map top toolbar, the lower-right control is location only, and a successful search shows a bottom card-style page. This supersedes the prior interpretation recorded under PLN-UI004-001. The product decision and generic map-state interface are approved by Plan Review iteration 1.

## Implementation Steps
- **Gate 0 — complete:** Plan Review iteration 1 approved the generic UI-003 map-state handoff. Keep implementation within the reviewed boundary; do not broaden the handoff to parcel-specific shell types or unrelated map-state changes.
- Add a small feature-owned data-source contract and deterministic fake data; avoid DTOs, repositories tied to network, or production API assumptions.
- Define immutable query/result UI state and events for entering, submitting, selecting a match, dismissing the summary and no-result display.
- Add the approved generic UI-003 update event and host callback bridge; prove a selection updates only active query/target while preserving the rest of shell state.
- Build the search and summary UI from existing foundation controls and theme tokens; keep the map container mounted underneath the result presentation.
- Wire the existing top-toolbar `onSearch` callback in the host and keep the lower-right location callback bound to location only; UI-004 adds no shell control.
- Add unit and Compose tests for AC-UI004-001 through AC-UI004-006 and the relevant UI-003 regressions.
- Capture current-revision visual evidence against the approved Figma revision if one is supplied; otherwise label comparisons as observations and retain that limitation.

## Test Plan
- Unit tests: accepted query fields, deterministic matches, no-match retention, selection and state transitions.
- Compose tests: accessible keyword entry, successful summary display, no-result copy/retained value, sheet/map context and summary dismissal.
- Run affected map-shell regression tests, relevant unit/connected tests, lint and debug/release builds per the active Developer and Testing rules after Plan Review and Implementation authorization.
- Verify the top-toolbar callback opens UI-004 and the lower-right control still invokes location only; verify no duplicate map-shell controls are added by UI-004.
- Verify the host forwards `(query, selectedTarget)` to `UpdateSearchContext`, and `MapShellViewModelTest` asserts only the two intended overlay values change.
- Verification must use the committed reviewed revision. Task-scope local checks must pass; hosted CI is deferred for this development scope and must be reactivated before release-candidate validation.

## Regression Plan
- Confirm the map shell still renders, basemap/location/drawer controls remain unchanged, and search callback is emitted once.
- Confirm selecting a search result updates only active query/selected target in the map overlay and preserves user location and other map-shell state.
- Confirm logout/session behavior and release isolation remain unchanged; UI-004 adds no release authentication/session capability.
- Confirm query/no-result states do not clear existing map context or selected overlay.

## Risks
- The requester clarified that only location is in the lower-right area and search/filter entry is in the top toolbar. The older UI-003/parent wording was interpreted differently in the previous draft; this plan now records the requester's clarification as the active UI-004 direction.
- The current Figma Copy revision is not formally approved; pixel-level acceptance cannot be claimed until its authority is established.
- The release `AppEntry` has no signed-in content yet. UI-004's host wiring is limited to the debug signed-in host for this development task; release-host integration must resume when the release session host exists, before release-candidate validation.
- UI-003 verification and prerequisite handoff now pass on hosted run `36948553870` at revision `00d5d1e`. The current worktree remains on `UI-003feat` with unrelated modifications, so UI-004 implementation still requires safe task isolation and its own Plan Review.
- Fake matching rules must not be described as production search semantics.

## Rollback Plan
- Revert only the UI-004 feature, debug-host wiring and task-scoped generic UI-003 handoff commits together; preserve pre-existing user changes and unrelated UI-003 history.

## Current Behavior
- `MapShellScreen` renders the top keyword-search action and lower-right location action. `MapShellRoute` exposes `onSearch`; the debug `AppEntry` currently binds only logout, so search has no UI-004 destination. There is no parcel-search screen, feature state or parcel source.
- The release `AppEntry` currently supplies empty signed-in content, so there is no release map-shell host to receive UI-004 yet.

## Expected Behavior
- The top-toolbar search/filter entry opens UI-004. A matching fake record is selected on the map and a card-style summary appears from the bottom; a no-match retains the query and shows “查無資料”. The lower-right control invokes location only.

## Acceptance Criteria Traceability
| AC | Implementation Step | Validation |
|---|---|---|
| AC-UI004-001 | Feature state and search field | Unit and Compose tests for input and retained query |
| AC-UI004-002 | Fake matching, map selection and bottom card-style summary | Unit match test, MapShellViewModel state test for selected target/query and preserved overlay fields, and Compose result/card test |
| AC-UI004-003 | No-match state | Unit no-match transition and Compose retained-query assertion |
| AC-UI004-004 | Summary header | Compose semantics/content assertions and visual comparison when approved source exists |
| AC-UI004-005 | Replaceable fake data source | Unit tests against source contract; source inspection confirms fixtures are outside Composables |
| AC-UI004-006 | UI-003 top-toolbar callback; lower-right remains location only | Compose callback assertion opens UI-004 from the top toolbar; location callback assertion; source ownership review |

## Failure Behavior
- No matching fake record displays “查無資料” and retains the keyword. API errors, retries and server timeouts are out of scope because no production API is used.

## Security and Privacy
- Use synthetic fixture values only. Do not log or persist user-entered query text; do not add credentials, parcel payloads or production data.

## Open Questions
- No unresolved product-behavior questions. Formal Figma approval remains deferred; do not claim pixel-parity acceptance without it.
