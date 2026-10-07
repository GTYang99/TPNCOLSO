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
- Added test-only commit `a4989bc` to disambiguate the summary key assertion: the same key is intentionally visible in both the retained query field and summary card.

## Local Validation

- Environment: Android Studio JBR `/Applications/Android Studio.app/Contents/jbr/Contents/Home`, OpenJDK `25.0.3`; emulator `Medium_Phone(AVD)` / `emulator-5554`, API 34.
- `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:testDebugUnitTest --tests 'com.example.tp_ncolso_android.feature.parcelsearch.*' --tests 'com.example.tp_ncolso_android.feature.mapshell.MapShellViewModelTest'` — PASS.
- `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:assembleDebug :app:assembleRelease :app:lint` — PASS.
- Initial `1101ec7` Compose runs for `ParcelSearchHostTest`, `MapShellScreenTest` and `DebugDirectLoginTest` — NOT VERIFIED at the time; they reported no Compose hierarchy. These results were superseded by the successful revalidation below after restoring local ADB/Gradle access.
- `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.ExampleInstrumentedTest` — PASS on the same emulator.

### Revalidation at committed revision `a4989bc`

- Environment: Android Studio JBR `/Applications/Android Studio.app/Contents/jbr/Contents/Home`, OpenJDK `25.0.3`; `Medium_Phone(AVD)` / `emulator-5554`, API 34 (Android 14).
- `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.feature.parcelsearch.ParcelSearchHostTest` — PASS (2/2 tests).
- `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.feature.mapshell.MapShellScreenTest` — PASS.
- `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.DebugDirectLoginTest` — PASS.
- `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:testDebugUnitTest --tests 'com.example.tp_ncolso_android.feature.parcelsearch.*' --tests 'com.example.tp_ncolso_android.feature.mapshell.MapShellViewModelTest' --rerun-tasks` — PASS.
- `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:assembleDebug :app:assembleRelease :app:lint` — PASS.
- `git diff --cached --check` — PASS before test-only commit `a4989bc`.
- Environment recovery: the sandbox denied the ADB local listener and Gradle wrapper-cache lock. Running ADB/Gradle through the authorized unsandboxed tool path exposed the configured emulator and Compose hierarchies; the earlier hierarchy error did not recur.

## Limitations and Routing

- `ENV-UI004-001` is resolved; the task-scoped Compose tests now execute and pass. The first restored-environment run exposed only the ambiguous test selector recorded as `VER-UI004-001`, which was corrected without changing product code or acceptance criteria.
- `minSdk` is configured as 28 and API 34 runtime checks pass. No Android 9/API 28 system image is installed locally, and no separate expanded font-scale/screen-size matrix was run; those runtime variants remain `NOT VERIFIED`.
- Hosted CI remains deferred for this pre-release development scope under the approved plan and current state.
- Production API integration, release signed-in host integration, product UAT and formal Figma revision approval remain deferred as recorded in the Task plan/state.

## Review and Revision

- Code review: `APPROVED` for implementation revision `1101ec7`; reviewer: Codex (same-agent review; independent reviewer unavailable). The implementation follows the approved ownership split, keeps parcel types out of UI-003, updates only `activeQuery` and `selectedTarget`, and includes state-preservation tests.
- Code review: `APPROVED` for committed revision `a4989bc`; reviewer: Codex (same-agent review; independent reviewer unavailable). Reviewed the test-only matcher correction; it now accounts for the key appearing in both the query field and summary. No product behavior or AC changed; no blocking findings.
- Verification: `PASS` for committed revision `a4989bc`; all six task ACs have local evidence in `verification.md`. Local results are not hosted CI evidence.
