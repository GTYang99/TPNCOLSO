# UI-004 Implementation Execution Report

## Revision and scope

- Task branch: `codex/ui-004-parcel-search`.
- Base revision: `b9451b5d7c27b3ed0f04ad174117de5040a9a657`.
- Tested source: implementation working tree based on that revision; changes are not committed, so validation evidence is for the current working tree only.
- Scope: UI-004 parcel keyword search, deterministic replaceable fake source, bottom summary/no-result states, generic UI-003 map-search context handoff, debug signed-in host wiring, and task-scoped unit/Compose tests.
- No requirement or acceptance criterion was changed. Figma pixel parity, production parcel API integration, and release signed-in host integration remain deferred as recorded in `state.yaml` and `plan.md`.

## Implementation summary

- Added immutable parcel search state/events, a replaceable data-source contract, and synthetic fixture matching for parcel identifier, land number, and location.
- Added search, result summary and no-result Compose presentation while keeping the map shell under the feature.
- Added the reviewed generic `UpdateSearchContext(query, selectedTarget)` map-shell event and wired the top-toolbar search callback in the debug host. The lower-right callback remains the location action.
- Added five parcel-search ViewModel unit tests and three Compose interaction tests.
- Corrected stale approval-status wording in the copied plan/review artifacts; the approved scope and AC did not change.

## Developer validation

Environment: Android Studio bundled JBR `25.0.3`; Android Gradle project; UI test device `Medium_Phone (AVD) - 14` (API 34, `emulator-5554`). A connected `XQ-AU52` (Android 12) was also used before the requester asked to stop physical-device testing; it will not be used again for this task unless requested.

| Check | Result | Evidence |
|---|---|---|
| `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:testDebugUnitTest --tests 'com.example.tp_ncolso_android.feature.parcelsearch.ParcelSearchViewModelTest' --tests 'com.example.tp_ncolso_android.feature.mapshell.MapShellViewModelTest'` | PASS | 9/9 tests: parcel search 5/5, map shell state preservation 4/4. |
| `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:lintDebug` | PASS | Gradle completed successfully on the implementation working tree. |
| `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:assembleDebug :app:assembleRelease` | PASS | Both variants compiled successfully; Gradle emitted only the existing unstripped native-library packaging warning. |
| `ANDROID_SERIAL=emulator-5554 JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.feature.parcelsearch.ParcelSearchScreenTest` on awake AVD | PASS | 3/3 UI-004 Compose tests. |
| `ANDROID_SERIAL=emulator-5554 JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.feature.mapshell.MapShellScreenTest` on awake AVD | PASS | 7/7 UI-003 Compose regression tests. |
| `ANDROID_SERIAL=emulator-5554 JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.feature.auth.AuthScreenTest#loginRegisterEntryDispatchesEvent` on awake AVD | PASS | 1/1 existing Compose control after waking the emulator. |
| UI-004 `ParcelSearchScreenTest` on `XQ-AU52` Android 12 | NOT VERIFIED | Same pre-assertion hierarchy error. No physical-device testing will be repeated per requester direction. |
| `git diff --check` | PASS | No whitespace errors in the current tracked diff. |

Infrastructure cause and recovery: `Medium_Phone` had `mWakefulness=Asleep`, `mLastSleepReason=power_button`, and had been asleep for over 90 minutes; `always_finish_activities=0` and screen-off timeout was `2147483647` ms. Waking `emulator-5554` restored the Compose test host. The first awake UI-004 run passed two cases and revealed that the successful result contains `KS-10001` in both the editable query and summary. The test now checks the editable query and the two matching text nodes separately; the corrected run passed all three cases. No product or Gradle configuration change was needed for the infrastructure issue.

## Acceptance-criteria evidence boundary

- `AC-UI004-001` through `AC-UI004-006`: PASS at local developer-validation scope. Evidence combines five parcel-search unit tests, four map-shell state tests, and three UI-004 Compose tests on the awake API 34 AVD.
- UI-003 Compose regression passed 7/7 on the same AVD; the existing Auth Compose control passed 1/1 after emulator wake-up and isolated the original hierarchy failures to the sleeping AVD.

## Git and completion status

- Implementation files and evidence are ready to commit on `codex/ui-004-parcel-search`.
- Code review, task-scope Verification PASS, and Development Complete have not been reached.
- Hosted CI remains deferred for pre-release development and must be reactivated before release-candidate validation.
- No merge, signing, publishing, release configuration, or deployment was performed.

## Next action

Implementation and local developer validation are complete. Next action: commit the task scope, then request code review. Hosted CI remains deferred for this pre-release development scope; release-candidate CI obligations remain active as recorded in `state.yaml`.
