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

## ENV-UI004-001 — Local Compose instrumentation hierarchy unavailable
- Category: `environment`
- Priority: P1
- Status: resolved
- Expected behavior: UI-004 Compose interactions and affected map-shell regressions execute on the configured Android emulator.
- Actual behavior: The initial run reported `IllegalStateException: No compose hierarchies found in the app`. In this continuation, sandboxed ADB could not bind its local listener (`Operation not permitted`) and Gradle could not create its wrapper-cache lock. Running ADB and Gradle through the authorized unsandboxed path made the API 34 emulator available and Compose hierarchies were exposed.
- Affected AC: `AC-UI004-001`, `AC-UI004-002`, `AC-UI004-003`, `AC-UI004-004`, `AC-UI004-006` (initially blocked; current Compose evidence is recorded in `verification.md`).
- Evidence: On `a4989bc`, filtered `:app:connectedDebugAndroidTest` runs passed for `ParcelSearchHostTest` (2 tests), `MapShellScreenTest`, and `DebugDirectLoginTest` on `Medium_Phone(AVD)` / `emulator-5554`, API 34. Focused unit tests, Debug/Release builds, and lint also passed. The initial `No compose hierarchies` failure did not recur.
- Impact: Compose/runtime behavior was blocked only while ADB/Gradle access was restricted; the task-scoped Compose checks now execute.
- Owner: Android test environment / project maintainer
- Route: `infrastructure`
- Resolution: Started the configured `Medium_Phone` API 34 AVD and ran the local Android tools outside the restricted sandbox. The Compose test hierarchy became available; reran the affected UI-004, map-shell and debug-login Compose suites successfully.
- Verification: `PASS` for environment restoration and affected Compose test execution. Return route: `verification`.

## VER-UI004-001 — Summary key text matcher was ambiguous
- Category: `verification_failure`
- Priority: P2
- Status: resolved
- Expected behavior: The match test verifies that the active query remains visible and that the parcel summary exposes the same `key_no`.
- Actual behavior: `onNodeWithText("TEST-KEY-001")` matched both the retained search input and the visible summary key, so the single-node assertion failed after the Compose UI had rendered.
- Affected AC: `AC-UI004-004` (the assertion stopped before the remaining summary fields were checked).
- Evidence: The first restored-environment `ParcelSearchHostTest` run failed at the ambiguous text assertion; `noMatchKeepsQueryAndMapContainerVisible` passed. The test-only correction and subsequent host test pass are committed in `a4989bc`.
- Impact: Summary key display could not be asserted with a single-node selector; no product behavior defect was shown.
- Owner: UI-004 task owner
- Route: `verification`
- Resolution: Changed the assertion to require the key text to occur twice, while retaining separate assertions for the active input, visible summary card, status, land number and site condition. No production behavior or acceptance criteria changed.
- Verification: `PASS`; both `ParcelSearchHostTest` cases passed on `a4989bc`.
