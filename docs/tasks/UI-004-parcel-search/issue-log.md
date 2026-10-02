# Issue Log

## PLN-UI004-001 — Search entry location and UI-003 callback interpretation
- Category: `requirement_gap`
- Priority: P2
- Status: resolved
- Expected behavior: The top-toolbar search/filter callback enters UI-004; the lower-right control invokes location only; a successful query displays the bottom card-style parcel summary.
- Actual behavior: Current `MapShellScreen` already has a top `SearchClicked` callback and a lower-right location action, but no parcel-search destination. The previous plan draft misread older parent wording as a separate lower-right search/filter control.
- Affected AC: `AC-UI004-001`, `AC-UI004-002`, `AC-UI004-006`
- Evidence: `docs/tasks/UI-003-map-shell/knowledge-resolution.md`; `docs/tasks/UI-0907-app-ui-requirements/requirement.md` FR-003 / FR-005; `MapShellScreen.kt` and `MapShellContract.kt`.
- Impact: Without clarification the plan assigned search to the wrong map-shell location and required an unnecessary callback decision.
- Owner: Product / requester
- Route: `planning`
- Resolution: Requester clarified on 2026-10-02 that search/filter entry is in the top toolbar, the lower-right control is location only, and a successful search opens a card-style page from the bottom. Updated `requirement.md` and `plan.md`; no lower-right search/filter callback will be added by UI-004.
- Verification: Matches the current `MapShellScreen` top `SearchClicked` entry and lower-right `LocationClicked` action. The successful-match presentation remains UI-004-owned.

## PLN-UI004-003 — Map result selection handoff
- Category: `planning_gap`
- Priority: P1
- Status: resolved
- Expected behavior: A matched parcel is located on the map while active query and unrelated map overlay context are preserved.
- Actual behavior: `MapShellUiState` contains `overlay.activeQuery` and `overlay.selectedTarget`, but `MapShellEvent` / `MapShellViewModel` provide no transition to update them from UI-004. Existing `MapNavigationClicked` has no parcel/query payload and leaves map state unchanged.
- Affected AC: `AC-UI004-002`, `AC-UI004-006`
- Evidence: `app/src/main/java/com/example/tp_ncolso_android/feature/mapshell/MapShellContract.kt` and `MapShellViewModel.kt`; parent `AC-006`; UI-003 Resolution's overlay preservation decision.
- Impact: Without a minimal host/UI-003 state handoff, UI-004 can display a summary but cannot satisfy the map locate/context-preservation requirement.
- Owner: UI-003 / UI-004 planning
- Route: `planning`
- Resolution: Plan Review iteration 1 approved the proposed interface on 2026-10-02: the signed-in composition root retains one `MapShellViewModel` and passes it to `MapShellRoute`; UI-004 reports only `(query: String, selectedTarget: String)` through a host callback; the root dispatches a UI-003-owned `MapShellEvent.UpdateSearchContext`; the event updates only `overlay.activeQuery` and `overlay.selectedTarget`.
- Verification: The planning decision is resolved. State-preservation behavior and callback wiring remain to be verified by implementation tests.

## PLN-UI004-002 — Figma Copy approval/revision
- Category: `requirement_gap`
- Priority: P2
- Status: open
- Expected behavior: Pixel-level visual acceptance uses an approved Figma revision.
- Actual behavior: Current node content and dimensions were retrieved on 2026-10-01, while parent Knowledge records that the Figma Copy revision and approval owner are unknown.
- Affected AC: `AC-UI004-004`
- Evidence: `docs/tasks/UI-0907-app-ui-requirements/knowledge-resolution.md` unresolved Figma Copy note; direct Figma retrieval of `4952:14938`, `4952:14933`, `4987:4490`.
- Impact: Current Figma is usable as `visual_observation`; it cannot alone prove formal visual acceptance.
- Owner: Design / requester
- Route: `knowledge_collection`
- Resolution: Pending approved revision or explicit confirmation.
- Verification: Pending.

## INF-UI004-001 — Compose instrumentation host exposes no hierarchy
- Category: `infrastructure`
- Priority: P1
- Status: resolved
- Revision: UI-004 implementation working tree based on `b9451b5d7c27b3ed0f04ad174117de5040a9a657` on `codex/ui-004-parcel-search`.
- Evidence: On `Medium_Phone (AVD) - 14` (API 34, `emulator-5554`), UI-004 and Auth Compose tests failed before assertions while the device was asleep (`mWakefulness=Asleep`, last sleep reason `power_button`, and `always_finish_activities=0`). After waking the AVD, the existing Auth control passed 1/1 and the UI-004 Compose run reached all three tests; two passed and the successful-result test exposed a duplicate-text selector in the test itself. A prior UI-004 attempt produced the same hierarchy symptom on connected `XQ-AU52` (Android 12); no further physical-device tests will run.
- Impact: The stale-sleep infrastructure condition is resolved by waking `emulator-5554`. UI-004's remaining focused rerun is pending; do not mark Compose AC as PASS until the corrected test executes successfully.
- Route: `infrastructure`
- Resolution: Wake `emulator-5554` before connected Compose tests; verify `mWakefulness=Awake`. Latest focused run reached test assertions, confirming test-host recovery. No source/build configuration changes were needed for the environment fix.

## DEV-UI004-001 — Successful-result test selector matched both query and summary
- Category: `implementation`
- Priority: P2
- Status: resolved
- Revision: UI-004 implementation working tree based on `b9451b5d7c27b3ed0f04ad174117de5040a9a657` on `codex/ui-004-parcel-search`.
- Expected behavior: Verify the retained query field and parcel key in the successful summary.
- Actual behavior: `onNodeWithText("KS-10001")` matched two visible nodes (search field and summary), so the test failed with “Expected at most 1 node but found 2.” The other two UI-004 Compose tests passed on the awake AVD.
- Affected AC: `AC-UI004-001`, `AC-UI004-002`, `AC-UI004-004`.
- Route: `implementation`.
- Resolution: Changed the Compose test to assert the text-field value separately and assert the key appears twice across the field and summary. Added an exact retained-query assertion to the no-result case. UI-004 Compose tests passed 3/3 on awake `Medium_Phone` (API 34) on 2026-10-02.
