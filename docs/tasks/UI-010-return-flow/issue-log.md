# Issue Log

## UI010-IMP-001 — Notifications row semantics import blocks Kotlin compilation

- Category: `implementation_regression`
- Priority: P1
- Status: resolved
- Affected acceptance criteria: AC-001, AC-002, AC-003 (the application main source set does not compile, so none can be validated)
- Expected: `:app:compileDebugKotlin` succeeds for the approved UI-010 implementation.
- Actual: Kotlin compilation fails at `feature/notifications/NotificationsScreen.kt:178:17` with `Unresolved reference 'role'`.
- Reproduction: `JAVA_HOME='/Applications/Android Studio.app/Contents/jbr/Contents/Home' ./gradlew :app:assembleDebug :app:compileDebugUnitTestKotlin :app:compileDebugAndroidTestKotlin --no-daemon`; failed at `:app:compileDebugKotlin` before compiling the test sources.
- Impact: debug app and planned task validation are blocked. No test task was executed.
- Owner: implementation agent
- Route: `debug` -> minimum import fix -> `implementation_debug`
- Resolution: imported `androidx.compose.ui.semantics.role` in `NotificationsScreen.kt`.
- Verification: `:app:compileDebugKotlin` passed as part of the final compile-only validation command. Device tests remain pending under the separate validation gate.

## UI010-IMP-002 — Compose host test imports a non-exported assertion

- Category: `implementation_regression`
- Priority: P1
- Status: resolved
- Affected acceptance criteria: AC-001, AC-002, AC-003 (instrumented host coverage cannot compile)
- Expected: the new UI-010 Compose host tests compile with the existing Compose test API.
- Actual: `:app:compileDebugAndroidTestKotlin` fails at `ReturnFlowHostTest.kt:3:33` with `Unresolved reference 'assertDoesNotExist'`.
- Reproduction: rerun the compile-only command recorded for UI010-IMP-001 after its source import fix; main and unit-test compilation proceed, then Android test-source compilation fails at the unresolved assertion import.
- Impact: planned on-device host tests cannot run until the test source compiles.
- Owner: implementation agent
- Route: `debug` -> remove invalid function import -> `implementation_debug`
- Resolution: removed the invalid top-level assertion import; kept the negative assertions.
- Verification: `:app:compileDebugAndroidTestKotlin` passed as part of the final compile-only validation command. No tests were executed.

## UI010-IMP-003 — Return-flow host tests use invalid selectors and premature ViewModel lookup

- Category: `implementation_regression`
- Priority: P1
- Status: resolved
- Affected acceptance criteria: AC-001, AC-002, AC-003 (the focused Compose host suite fails before completing its assertions)
- Expected: the host suite drives the notification-to-correction flow and asserts the approved behavior.
- Actual: on `Medium_Phone` API 36, AC-001's detail key selector finds two text nodes; AC-002 tries `performScrollTo()` on the fixed submit button, which has no scroll parent; AC-003 retrieves the form ViewModel before asserting that its route is composed, so the default factory attempts an unsupported zero-argument constructor.
- Reproduction: `ANDROID_SERIAL='emulator-5554' JAVA_HOME='/Applications/Android Studio.app/Contents/jbr/Contents/Home' ./gradlew :app:connectedDebugAndroidTest --no-daemon -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.feature.notifications.ReturnFlowHostTest`.
- Impact: the initial focused run could not prove AC-001–AC-003. After the test-harness fixes below, all three host tests passed; no product failure was established by the original stack traces.
- Owner: implementation agent
- Route: `debug` -> focused test-harness fix -> `implementation_debug`
- Resolution: changed AC-001 to assert both expected key-number nodes, clicked the fixed submit button directly, and waited for the form route before fetching its ViewModel after both opens.
- Verification: `ANDROID_SERIAL='emulator-5554' JAVA_HOME='/Applications/Android Studio.app/Contents/jbr/Contents/Home' ./gradlew :app:connectedDebugAndroidTest --no-daemon -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.feature.notifications.ReturnFlowHostTest` passed all 3 tests on `Medium_Phone(AVD)` API 36. The subsequent full Android suite also completed all 3 UI-010 host tests without failure.

## UI010-VAL-001 — Full Android suite lost activity focus during an existing survey-form Back test

- Category: `environment`
- Priority: P2
- Status: resolved
- Affected acceptance criteria: AC-003 (adjacent UI-008 discard-flow regression coverage)
- Expected: `SurveyFormScreenTest.dirtyCloseAndBackUseDiscardDialogAndContinuePreservesEdits` can send Back through the instrumented Compose activity during the connected regression suite.
- Actual: the full connected suite reported 64/65 tests passing. At `SurveyFormScreenTest.kt:185`, Espresso timed out waiting for the app root to have window focus (`RootViewWithoutFocusException`; `has-window-focus=false`) before `pressBack()` could be dispatched. The failure log also shows heavy emulator frame skips while launcher/system processes were active.
- Reproduction: `ANDROID_SERIAL='emulator-5554' JAVA_HOME='/Applications/Android Studio.app/Contents/jbr/Contents/Home' ./gradlew :app:connectedDebugAndroidTest --no-daemon` on `Medium_Phone(AVD)` API 36 exited 1 after 13m17s (65 tests, 1 failure). An isolated rerun of the same method, `ANDROID_SERIAL='emulator-5554' JAVA_HOME='/Applications/Android Studio.app/Contents/jbr/Contents/Home' ./gradlew :app:connectedDebugAndroidTest --no-daemon '-Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.feature.surveyform.SurveyFormScreenTest#dirtyCloseAndBackUseDiscardDialogAndContinuePreservesEdits'`, passed 1/1 in 29.405s on `emulator-5554`.
- Impact: the UI-010 AC-003 focused host test passes. The full regression suite remains recorded as FAIL (64/65); its one failure occurred before Back dispatch and did not reproduce in the isolated rerun. The full suite itself is not being reported as PASS.
- Owner: implementation agent / local Android validation environment
- Route: `infrastructure` -> isolate the focus failure -> return to implementation
- Resolution: isolated execution restored the instrumented activity focus and passed the exact method; failure evidence and the full-suite 64/65 result are retained. No product code change was made for this environment event.
- Verification: focused failure-method rerun PASS (1/1); full-suite result remains FAIL (64/65).

## UI010-VAL-002 — Concurrent instrumentation on the shared approved AVD

- Category: `environment`
- Priority: P2
- Status: resolved
- Affected acceptance criteria: AC-001, AC-002, AC-003 (the full connected regression rerun was interrupted before a complete result)
- Expected: the full connected suite runs on the user-approved `Medium_Phone` AVD without another instrumentation run concurrently using the same serial.
- Actual: on two separate UI-010 full-suite attempts, read-only process inspection found another worktree running `:app:connectedDebugAndroidTest` on ADB serial `emulator-5554`. Both runs targeted the same `Medium_Phone(AVD) - 16` emulator; the competing worktree was `TaoYuanGutter`.
- Reproduction: start `ANDROID_SERIAL='emulator-5554' JAVA_HOME='/Applications/Android Studio.app/Contents/jbr/Contents/Home' ./gradlew :app:connectedDebugAndroidTest --no-daemon`; during the run, inspect active Gradle/AndroidJUnitRunner processes. Two attempts found a second connected test process on `emulator-5554`.
- Impact: both UI-010 reruns were interrupted with exit code 130 before a complete XML result was produced. They are excluded from test counts and pass/fail evidence. The initial 64/65 full-suite failure and isolated 1/1 rerun remain unchanged.
- Owner: local Android validation environment
- Route: `infrastructure` -> wait for shared AVD to be idle -> rerun approved connected suite -> return to `implementation`.
- Resolution: the competing connected test process exited; a read-only process snapshot showed the shared AVD idle. Both overlapping UI-010 attempts remain excluded from result counts. Validation resumed on the same approved AVD without modifying other worktree processes.
