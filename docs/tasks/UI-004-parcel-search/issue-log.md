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
- Status: open
- Expected behavior: UI-004 Compose interactions and affected map-shell regressions execute on the configured Android emulator.
- Actual behavior: Focused UI-004 `ParcelSearchHostTest`, existing `MapShellScreenTest`, and existing `DebugDirectLoginTest` fail before their UI assertions with `IllegalStateException: No compose hierarchies found in the app`. The non-Compose `ExampleInstrumentedTest` passes on the same emulator.
- Affected AC: `AC-UI004-001`, `AC-UI004-002`, `AC-UI004-003`, `AC-UI004-004`, `AC-UI004-006` (Compose/runtime interaction evidence remains unavailable).
- Evidence: Local `:app:connectedDebugAndroidTest` filtered runs on `Medium_Phone(AVD)` / `emulator-5554`, API 34; UI-004 report at `app/build/reports/androidTests/connected/debug/com.example.tp_ncolso_android.feature.parcelsearch.ParcelSearchHostTest.html`, existing map-shell report at `app/build/reports/androidTests/connected/debug/com.example.tp_ncolso_android.feature.mapshell.MapShellScreenTest.html`, and pre-existing debug-login report at `app/build/reports/androidTests/connected/debug/com.example.tp_ncolso_android.DebugDirectLoginTest.html`. `:app:testDebugUnitTest`, `:app:assembleDebug`, `:app:assembleRelease`, `:app:lint`, and `ExampleInstrumentedTest` pass.
- Impact: Compose/runtime behavior cannot be marked PASS in this local environment; no product assertion ran in the failing Compose tests.
- Owner: Android test environment / project maintainer
- Route: `infrastructure`
- Resolution: Pending restoration of a local Compose test hierarchy or provision of a working Android Compose test environment. Then rerun the focused UI-004 and map-shell Compose tests.
- Verification: `NOT VERIFIED`; environment classification is supported by the same failure in unchanged baseline Compose tests and the passing non-Compose instrumentation smoke test.
