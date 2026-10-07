# Implementation Plan

## Goal
- Implement the UI-005 read-only parcel detail container and its fake latest/history data flow on the existing map/search host.

## Completion Scope
- `completion.scope: development` — complete through approved requirement/plan, implementation, local developer validation, commit, code review and task-AC Verification PASS.
- Deferred: hosted CI, production API integration, release signed-in host integration, product UAT, formal Figma pixel parity, and release approval/merge/deployment. Each is outside this pre-release UI-only scope and must be reactivated before release-candidate validation or a formal visual claim.

## Scope
- Add a feature-owned detail contract, fake source, ViewModel and Compose screen.
- Add the smallest UI-004 summary callback needed to enter and close detail.
- Wire only the debug signed-in host; keep map context mounted and expose UI-006 handoff callback without implementing UI-006.
- Add unit and Compose tests for all seven task ACs and the affected UI-004 regression.

## Affected Files
- `app/src/main/java/com/example/tp_ncolso_android/feature/parcelsearch/ParcelSearchContract.kt` — optional detail-open callback contract.
- `app/src/main/java/com/example/tp_ncolso_android/feature/parcelsearch/ParcelSearchScreen.kt` — detail action in the existing summary.
- `app/src/main/java/com/example/tp_ncolso_android/feature/parceldetail/ParcelDetailContract.kt` — immutable detail models, UI state/events/effects.
- `app/src/main/java/com/example/tp_ncolso_android/feature/parceldetail/ParcelDetailViewModel.kt` — fake-source lookup and tab/history/handoff orchestration.
- `app/src/main/java/com/example/tp_ncolso_android/feature/parceldetail/ParcelDetailScreen.kt` — fixed header, tabs, read-only fields, return reason and UI-006 action.
- `app/src/main/java/com/example/tp_ncolso_android/feature/parceldetail/data/*` — replaceable source and deterministic fixtures.
- `app/src/debug/java/com/example/tp_ncolso_android/AppEntry.kt` — open detail overlay and close/edit callbacks.
- `app/src/test/java/.../feature/parceldetail/*` and `app/src/androidTest/java/...` — state and UI evidence.
- `docs/tasks/UI-005-parcel-detail/execution-report.md` — revision, commands, results and limitations.

## Technical Design
- Keep detail models feature-owned; do not add parcel-detail DTOs to `data/network` or `MapShell`.
- `ParcelDetailViewModel` exposes `StateFlow<ParcelDetailUiState>` and buffered effects for `EditRequested` and `Closed`.
- `ParcelDetailUiState` keeps fixed `ParcelDetailRecord`, selected tab, selected history period, loading/error state and `isHistoryReadOnly`; the ViewModel always initializes to latest history record and Land tab.
- The screen uses existing theme/foundation components and plain read-only rows. The edit action is a button with an explicit callback/effect; it does not mutate field state.
- The debug root opens detail from the selected parcel key and leaves `MapShellRoute` mounted underneath. Search summary can close when detail opens, and detail close returns to the existing map/search context.

## Implementation Steps
1. Add the approved requirement/plan handoff and feature contracts with explicit fake-data and UI-006 boundaries.
2. Add deterministic detail/history models and a replaceable `ParcelDetailDataSource` keyed by `key_no`.
3. Implement ViewModel load, tab selection, history selection and UI-006 handoff effect without editable state.
4. Implement the detail overlay with fixed summary, land-data tab, survey tab, history selector, return reason and accessible close/edit actions.
5. Add the summary-to-detail callback and debug host wiring while keeping map context mounted.
6. Add focused unit and Compose tests, run targeted checks, then build/lint affected variants.

## Test Plan
- Unit: fake source lookup, latest default, tab/history transitions, historical read-only flag, return reason and edit effect.
- Compose: summary opens detail, fixed header/land fields, survey latest/history switch, read-only semantics, returned banner, close and UI-006 callback.
- Regression: existing `ParcelSearchHostTest`, `MapShellScreenTest`, focused parcel-search unit tests, debug/release assemble and lint.

## Regression Plan
- Search match/no-match and retained query behavior remain unchanged.
- Map surface, location-only control, basemap state and map overlay remain visible under detail.
- Summary dismissal still works when detail is not opened; logout/session and release source isolation stay unchanged.

## Risks
- Visual composition is based on common-template evidence; formal pixel parity is deferred.
- UI-006 entry may be tested only as a callback because no UI-006 screen exists yet.
- Android API 28 and expanded font-scale/device matrix may be unavailable locally.

## Rollback Plan
- Revert only the UI-005 feature, summary callback and debug host wiring commits; preserve existing UI-003/UI-004 history and unrelated files.

## Current Behavior
- Search opens a summary card with no detail navigation; no parcel detail feature exists.

## Expected Behavior
- A matched summary can open a read-only detail overlay containing fixed summary, two tabs, latest/history survey data and a UI-006 handoff action while the map remains mounted.

## Acceptance Criteria Traceability
| AC | Implementation Step | Validation |
|---|---|---|
| AC-UI005-001 | 3–5 | Host test opens detail and asserts fixed header plus mounted map. |
| AC-UI005-002 | 3–4 | Compose test asserts land tab, 10 fields and no editable nodes. |
| AC-UI005-003 | 3–4 | ViewModel and host tests assert survey tab defaults to latest and shows 10 read-only values. |
| AC-UI005-004 | 3–4 | Unit/Compose tests switch history period and assert selected values/read-only state. |
| AC-UI005-005 | 2–4 | Host test asserts returned Tag/explanation and absence for non-returned fixture. |
| AC-UI005-006 | 3–5 | Unit/Compose callback test asserts UI-006 effect only; no dirty/edit state exists. |
| AC-UI005-007 | 2–3, 6 | Alternate source unit test, missing-key error test and source inspection. |

## Failure Behavior
- Blank/missing key or absent fixture shows a detail error state with a close action and does not replace the map. Fake source has no network timeout or API error behavior.

## Security and Privacy
- Fixtures are synthetic; no credentials, tokens, personal data, production payloads, persistence or logging are added. Query/key values stay in in-memory UI state only.

## Open Questions
- Formal Figma revision approval remains deferred; no implementation decision depends on it for this development scope.
