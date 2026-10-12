# Root Cause — UI010-IMP-001

## Reproduction
- Revision under validation: `1a11dff4d8c081235b090aaa6a6e8549016721ca` plus the current uncommitted UI-010 worktree.
- Command: `JAVA_HOME='/Applications/Android Studio.app/Contents/jbr/Contents/Home' ./gradlew :app:assembleDebug :app:compileDebugUnitTestKotlin :app:compileDebugAndroidTestKotlin --no-daemon`.
- Result: `:app:compileDebugKotlin` fails at `NotificationsScreen.kt:178:17`: `Unresolved reference 'role'`.

## Mapping
- Cause category: implementation regression.
- Affected source: `app/src/main/java/com/example/tp_ncolso_android/feature/notifications/NotificationsScreen.kt`.
- The returned-notification row sets the Compose semantics `role` property but does not import the `androidx.compose.ui.semantics.role` extension. The compiler therefore cannot resolve the property.
- All three acceptance criteria are blocked from validation until the main source set compiles.

## Scope
- This is a source-level import omission. No requirement, architecture, data, or behavior change is needed.

## UI010-IMP-002 — Compose host assertion import
- Reproduction: `:app:compileDebugAndroidTestKotlin` fails at `ReturnFlowHostTest.kt:3:33` with `Unresolved reference 'assertDoesNotExist'`.
- Cause category: implementation regression in the new test source.
- The Compose test assertion is available as a member on the node interaction used by existing tests; it is not a top-level symbol exported by `androidx.compose.ui.test`.
- Scope: remove only the invalid import. Keep the negative assertions and acceptance coverage intact.
- Resolution evidence: the final `:app:compileDebugAndroidTestKotlin` task passed.

## UI010-IMP-003 — Return-flow host test failures
- Reproduction: the focused `ReturnFlowHostTest` run on AVD `Medium_Phone`, serial `emulator-5554`, API 36 reports three failures in the generated XML and per-test logcat files.
- AC-001 failure: the selected UI010 fixture uses the same value for internal `keyNo` and display `landNo`, so the detail has two matching text nodes. The test incorrectly assumed `onNodeWithText(keyNo)` was unique.
- AC-002 failure: the submit action is fixed at the bottom of the form and is outside the scrollable form content. Calling `performScrollTo()` on it has no scrollable ancestor.
- AC-003 failure: the test queries the custom-constructor `SurveyFormViewModel` through the Activity's default factory without first asserting the form route has been composed. The factory exception establishes that the expected instance was not available at lookup time; it does not establish a product-flow failure.
- Classification: implementation regression in the new test harness. The evidence does not yet show a production-code defect.
