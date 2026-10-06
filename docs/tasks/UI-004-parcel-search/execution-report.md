# Execution Report

## Task and Scope

- Task: `UI-004-parcel-search`
- Knowledge baseline: `KB-UI-004-PARCEL-SEARCH-R2`
- Plan Review: `APPROVED`, iteration 1; approved Plan SHA-256 `c53c1b051435c847d0043d13e5507328b2e6e3bbbd94a2b09a74c754b6fa5cf1`
- Scope implemented: fake-data keyword search, retained query/no-result state, bottom parcel summary, generic map-state handoff and debug signed-in host wiring.
- Out of scope: production parcel API, release signed-in host, real map movement, formal Figma pixel parity, release actions.
- Branch: `codex/ui004-parcel-search`
- Base revision: `b9451b5` (contains UI-003 prerequisite commit `00d5d1e`)
- Implementation commit: `1101ec7` (`feat(ui-004): implement parcel search and summary`).

## Changes

- Added immutable parcel-search state/events/effects and a replaceable `ParcelSearchDataSource` with deterministic synthetic fixtures.
- Added Compose query/no-result UI and a bottom summary showing status, parcel key, land number and site condition.
- Wired the existing top-toolbar callback in the debug host to UI-004, and bridged only `(query, selectedTarget)` to UI-003's generic `UpdateSearchContext` event.
- Added unit tests for fake matching, state transitions, source replacement and preservation of unrelated map state; added host Compose tests for match, no-match, map retention, summary dismissal and location-only behavior.

## Local Validation

- Environment: Android Studio JBR `/Applications/Android Studio.app/Contents/jbr/Contents/Home`, OpenJDK `25.0.3`; emulator `Medium_Phone(AVD)` / `emulator-5554`, API 34.
- `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:testDebugUnitTest --tests 'com.example.tp_ncolso_android.feature.parcelsearch.*' --tests 'com.example.tp_ncolso_android.feature.mapshell.MapShellViewModelTest'` — PASS.
- `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:assembleDebug :app:assembleRelease :app:lint` — PASS.
- `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.feature.parcelsearch.ParcelSearchHostTest` — NOT VERIFIED; both cases stopped before UI assertions with `IllegalStateException: No compose hierarchies found in the app`.
- `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.feature.mapshell.MapShellScreenTest` — NOT VERIFIED; unchanged map-shell Compose cases report the same missing-hierarchy error.
- `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.DebugDirectLoginTest` — NOT VERIFIED; pre-existing Compose cases report the same missing-hierarchy error.
- `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.ExampleInstrumentedTest` — PASS on the same emulator.
- `git diff --cached --check` — PASS before commit `1101ec7`.

## Limitations and Routing

- The local emulator runs non-Compose instrumentation, but Compose test rules do not expose a Compose hierarchy. The same failure occurs in unchanged baseline Compose tests, so no product assertion executed in those runs. `ENV-UI004-001` is recorded in `issue-log.md`; route: `infrastructure`.
- Compose runtime behavior for `AC-UI004-001` through `AC-UI004-004` and `AC-UI004-006` remains `NOT VERIFIED` until the local Compose test environment is restored and the focused tests pass.
- Hosted CI remains deferred for this pre-release development scope under the approved plan and current state.
- Production API integration, release signed-in host integration, product UAT and formal Figma revision approval remain deferred as recorded in the Task plan/state.

## Review and Revision

- Code review: `APPROVED` for committed revision `1101ec7`; reviewer: Codex (same-agent review; independent reviewer unavailable). The diff follows the approved ownership split, keeps parcel types out of UI-003, updates only `activeQuery` and `selectedTarget`, and includes the state-preservation tests. No blocking findings; no code changes followed this review.
- Verification: `NOT VERIFIED` for committed revision `1101ec7`; AC-level results are in `verification.md`. Local results above are not hosted CI evidence.
