# UI-010 Root Cause Analysis

## UI010-IMP-004 — Correction confirmation leaves stale saved flag

### Reproduction

- Base revision: `d74bcd8192e6ea6e2d06306867af74f198c20580`; isolated branch: `codex/ui010-correction-state`.
- Environment: Windows, Android Studio JBR OpenJDK `25.0.3`, Gradle `9.6.0`, Android SDK `C:\Users\GT.YANG\AppData\Local\Android\Sdk`.
- Command: `:app:testDebugUnitTest --tests com.example.tp_ncolso_android.feature.notifications.data.FakeReturnFlowStoreTest --no-daemon`.
- Result: FAIL, 3 tests completed; `confirmationChangesStatusRemovesReturnContextAndHidesNotification` failed at `FakeReturnFlowStoreTest.kt:76` because `correctionSaved` remained true after confirmation.

### Root Cause and Scope

`submit()` sets `correctionSaved = true` to represent a saved correction awaiting confirmation. `confirmCorrection()` changes status to `已修正` and clears return metadata, but omits resetting that flag. The test already asserts the expected false value. This is a production fake-store state-transition defect affecting UI-010 `AC-002`, not a test-harness or environment failure.

Minimum fix: set `correctionSaved = false` in the same detail copy that applies the confirmed status. Preserve the existing guard, metadata clearing, edit-action removal, and notification filtering. No requirement, architecture, persistence, or API change is needed; the approved plan and review remain applicable.

### Fix and Validation Result

- Implemented on `codex/ui010-correction-state`: `confirmCorrection()` now resets `correctionSaved` in the confirmed-state copy.
- Focused `FakeReturnFlowStoreTest`: PASS after the fix.
- Full `:app:testDebugUnitTest`: PASS, 51 tests, 0 failures.
- Focused `ReturnFlowHostTest`: PASS, 3 tests, 0 failures on `Medium_Phone_API_36.0(AVD) - 16`, API 36, `emulator-5554`.
- `git diff --check`: PASS. See the current UI-010 `execution-report.md` entry for revision and environment details.

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
