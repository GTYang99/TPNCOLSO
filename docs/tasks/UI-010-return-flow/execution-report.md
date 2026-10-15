# UI-010 Execution Report

## Debug Re-entry — UI010-IMP-004

- Base revision: `d74bcd8192e6ea6e2d06306867af74f198c20580`; isolated branch: `codex/ui010-correction-state`.
- Change: `FakeReturnFlowStore.confirmCorrection()` now clears `correctionSaved` in the same transition that marks the record `已修正` and removes its return context.
- Environment: Windows; Android Studio JBR OpenJDK `25.0.3`; Gradle `9.6.0`; Android SDK `C:\Users\GT.YANG\AppData\Local\Android\Sdk`.
- Reproduction before fix: `:app:testDebugUnitTest --tests com.example.tp_ncolso_android.feature.notifications.data.FakeReturnFlowStoreTest --no-daemon` failed 1/3 at `FakeReturnFlowStoreTest.kt:76`.
- Focused JVM after fix: `:app:testDebugUnitTest --tests com.example.tp_ncolso_android.feature.notifications.data.FakeReturnFlowStoreTest --no-daemon` — PASS; Gradle `BUILD SUCCESSFUL`.
- Full JVM after fix: `:app:testDebugUnitTest --no-daemon` — PASS; 51 tests, 0 failures.
- Connected host after fix: `:app:connectedDebugAndroidTest --no-daemon '-Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.feature.notifications.ReturnFlowHostTest'` — PASS; 3 tests, 0 failures on `Medium_Phone_API_36.0(AVD) - 16`, API 36, serial `emulator-5554`.
- `git diff --check` — PASS.
- The first build attempt lacked SDK discovery because the isolated worktree had no ignored `local.properties`; rerunning with command-local `ANDROID_HOME` succeeded. No project configuration file was added or modified.
- The failed unquoted PowerShell Gradle-property attempt stopped during task selection and ran no tests; the quoted command above is the successful connected run.
- Validation is local evidence on the current branch/worktree, not hosted CI. No physical device was used.

## Revision and Environment
- Branch: `developer`.
- Implementation commit: `01a9a10200001ff572bc7a7f89d192a94f58b656` on `developer`. The focused AVD checks ran against the same UI-010 source content immediately before this commit; unrelated UI-008 working-tree changes remain uncommitted.
- Gradle JDK: `/Applications/Android Studio.app/Contents/jbr/Contents/Home`, OpenJDK `25.0.3` (2026-04-21).
- Scope: Android v0.1 implementation against `KB-UI-010-RETURN-FLOW-R1`, approved Plan Review iteration 1.
- Test-target authorization: the user explicitly approved `Medium_Phone` AVD after clarifying that the available target was an emulator. Runtime validation is authorized on serial `emulator-5554`, API 36; no physical-device requirement is listed in the approved ACs.
- Design references: Figma nodes `3327:13261`, `3327:13256`, `4950:14192`, `4964:4571`, `4990:8053`, `4990:7894`, and `4950:14447`. The Figma file does not expose formal revision metadata.

## Changes
- Added the Figma-style floating notification card over the map, with six returned rows, a scrollable list, outside-tap / Back dismissal, and map-bell routing.
- Added one app-session fake store shared by the notification list, parcel detail, and existing survey form; saved edits update local detail fields without changing the returned status.
- Scoped return-flow ViewModel keys and route state to a login-session generation so signing in again as the same role receives a fresh in-memory store.
- Added prominent return-reason context to detail and the shared correction form. A successful form save returns to detail, shows `編輯成功`, and reveals the Figma-style success `確認修正` action.
- Added local Figma color tokens and exact button / toast icon assets, recorded in `docs/assets/app-ui-assets.md`.
- Added the Figma confirmation dialog copy and actions. Cancel preserves saved correction data and returned status; confirm updates the status to `已修正`, removes the return banner/edit action, removes the row from the returned-only list, and shows `修正成功` feedback.
- Kept the existing UI-008 dirty-exit dialog and investigator read-only rule for corrected records.
- Followed the approved read-only rule after confirmation; the Figma corrected-state frame visually includes an edit icon, which remains an explicit visual difference from the product baseline.
- No API, server synchronization, persistent fake storage, read state, or pagination was added.

## Acceptance-Criteria Evidence Status
- `AC-001`: focused UI-010 host test passed 3/3 on the user-approved `Medium_Phone` AVD, API 36, serial `emulator-5554`.
- `AC-002`: focused UI-010 host test passed 3/3 on the user-approved `Medium_Phone` AVD, API 36, serial `emulator-5554`.
- `AC-003`: focused UI-010 host test passed 3/3, and the affected `SurveyFormScreenTest` class passed 9/9 on the user-approved AVD. The initial full suite's intermittent focus failure remains recorded as an environment event; its exact method also passed in isolation.

## Checks

| Check | Method | Result |
|---|---|---|
| Source review | Inspected changed feature contracts, view models, Compose routes, fake store, debug host wiring, tests, and local Figma assets. | Completed; not a substitute for runtime execution. |
| Diff whitespace | `git diff --check` and whitespace scan on new UI-010 test/source files | PASS. |
| Debug build and test-source compilation | `JAVA_HOME='/Applications/Android Studio.app/Contents/jbr/Contents/Home' ./gradlew :app:assembleDebug :app:compileDebugUnitTestKotlin :app:compileDebugAndroidTestKotlin :app:lintDebug --no-daemon` | PASS: `BUILD SUCCESSFUL`; main, unit-test, and Android-test sources compile and debug APK assembles. |
| AC-003 test-source update | `JAVA_HOME='/Applications/Android Studio.app/Contents/jbr/Contents/Home' ./gradlew :app:compileDebugAndroidTestKotlin --no-daemon` | PASS after adding discard/reopen checks for restoration of original values and all three original photos; compile only, no tests executed. |
| Android lint | `:app:lintDebug` as part of the command above; inspected `app/build/reports/lint-results-debug.sarif`. | PASS; report contains 31 non-blocking lint findings, including style/modernization suggestions. No lint task failure. |
| Initial compile failures | Same command before the two import fixes. | Failed first on missing Compose `role` import, then on invalid test assertion import; both issues are resolved and recorded in `issue-log.md`. |
| Focused UI-010 instrumentation | `ANDROID_SERIAL='emulator-5554' JAVA_HOME='/Applications/Android Studio.app/Contents/jbr/Contents/Home' ./gradlew :app:connectedDebugAndroidTest --no-daemon -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.feature.notifications.ReturnFlowHostTest` | 3/3 PASS on the user-approved `Medium_Phone(AVD) - 16`, API 36, serial `emulator-5554` (latest rerun after explicit test authorization). |
| Affected survey-form regression class | `ANDROID_SERIAL='emulator-5554' JAVA_HOME='/Applications/Android Studio.app/Contents/jbr/Contents/Home' ./gradlew :app:connectedDebugAndroidTest --no-daemon '-Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.feature.surveyform.SurveyFormScreenTest'` | 9/9 PASS, 0 failures/errors/skips on `Medium_Phone(AVD) - 16`, API 36, serial `emulator-5554`; XML reports `tests=9`, `failures=0`, `errors=0`, `skipped=0`. |
| Full connected Android regression suite (initial run) | `ANDROID_SERIAL='emulator-5554' JAVA_HOME='/Applications/Android Studio.app/Contents/jbr/Contents/Home' ./gradlew :app:connectedDebugAndroidTest --no-daemon` | FAIL, 64/65 tests on the approved `Medium_Phone(AVD) - 16`, serial `emulator-5554`, API 36. The existing `SurveyFormScreenTest.dirtyCloseAndBackUseDiscardDialogAndContinuePreservesEdits` failed before Back dispatch because Espresso's root window did not have focus. See `UI010-VAL-001`. |
| Full connected Android regression suite (reruns) | Same command, explicitly pinned to `ANDROID_SERIAL=emulator-5554`. | Two attempts were INTERRUPTED (exit 130) after separate worktrees began connected instrumentation on the same serial; a third attempt was stopped by the user's request (exit 130). None produced a complete XML result; all are excluded from pass/fail counts. See `UI010-VAL-002` for the two shared-AVD interruptions. |
| Isolated rerun of the failed regression method | `ANDROID_SERIAL='emulator-5554' JAVA_HOME='/Applications/Android Studio.app/Contents/jbr/Contents/Home' ./gradlew :app:connectedDebugAndroidTest --no-daemon '-Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.feature.surveyform.SurveyFormScreenTest#dirtyCloseAndBackUseDiscardDialogAndContinuePreservesEdits'` | 1/1 PASS on the approved `Medium_Phone` AVD, serial `emulator-5554`, 29.405s. |
| Unit tests | `:app:compileDebugUnitTestKotlin` was included in the compile/build command; JVM unit-test execution was not started. | Source compilation PASS; JVM test execution NOT VERIFIED. |
| Runtime screenshots | Not captured. | NOT VERIFIED. |
| Figma context | Inspected the approved list, returned detail, correction, dialog, and corrected-state nodes; downloaded two local Figma icon exports. | Source reviewed; no implementation screenshot comparison. |

## Limitations and Deferred Gates
- Debug buildability, test-source compilation, lint, focused UI-010 instrumentation, and the affected survey-form regression class are verified for implementation commit `01a9a10200001ff572bc7a7f89d192a94f58b656`. JVM unit-test execution and runtime screenshots remain NOT VERIFIED. The initial full suite failed 64/65; two reruns were interrupted by concurrent instrumentation, and a third was stopped at the user's request. No full-suite rerun produced a complete result.
- Approved AVD results: focused UI-010 host class 3/3 PASS; affected `SurveyFormScreenTest` class 9/9 PASS; initial full connected suite 64/65 with one existing survey-form focus failure, while the exact method passed in isolation. The full-suite result remains a recorded regression limitation and is not reported as PASS.
- UI010-IMP-001, UI010-IMP-002, UI010-IMP-003, and the isolated UI010-VAL-001 focus incident are resolved as recorded. UI010-VAL-002 records both concurrent-AVD interruptions.
- Task-scoped developer validation is complete for AC-001–AC-003; JVM unit-test execution and runtime screenshots remain NOT VERIFIED limitations. Code review and Verification remain pending. Hosted CI remains deferred to the release candidate per the approved plan.
- Existing unrelated UI-008/UI-010 working-tree changes were preserved and left unstaged.

## Code Review Result
- Reviewed revision: `01a9a10200001ff572bc7a7f89d192a94f58b656` (`feat(ui010): add returned parcel correction flow`) on branch `developer`.
- Result: `APPROVED`; same-agent review because no independent reviewer was available.
- Reviewed the shared in-memory return-flow store, stable parcel-key routing, notification overlay semantics and dismissal paths, save-before-confirm status transition, correction confirmation cancel/confirm behavior, dirty-form discard/continue behavior, login-session store generation, read-only behavior after `已修正`, and task-scoped test coverage.
- Confirmed the change stays within the approved local Android UI scope: no API calls, persistence, permissions, credentials, or unrelated production refactoring were introduced.
- `git show --check 01a9a10200001ff572bc7a7f89d192a94f58b656` — PASS.
- Findings: no blocking defects. JVM unit-test execution, runtime screenshots, and a complete full-suite PASS remain documented limitations; they do not replace the focused AC evidence.
