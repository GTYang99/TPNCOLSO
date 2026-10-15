# UI-010 Fix Plan

## UI010-IMP-004 — Correction confirmation leaves stale saved flag

1. In `FakeReturnFlowStore.confirmCorrection()`, set `correctionSaved = false` together with `status = "已修正"`, `returnReason = null`, and `editActionLabel = null`.
2. Keep the guard and active returned-notification filtering unchanged.
3. Re-run `:app:testDebugUnitTest --tests com.example.tp_ncolso_android.feature.notifications.data.FakeReturnFlowStoreTest --no-daemon`; require all 3 tests to pass.
4. Run `git diff --check` and record the exact source revision and environment.

This is an internal state correction within approved UI-010 `AC-002`; it does not change the approved design or scope and requires no new Plan Review.

### Validation Result

- Focused fake-store JVM test: PASS.
- Full JVM suite: PASS, 51 tests, 0 failures.
- UI-010 connected host suite: PASS, 3 tests, 0 failures on the approved API 36 AVD.
- `git diff --check`: PASS.

1. Add the missing `androidx.compose.ui.semantics.role` import in `NotificationsScreen.kt`.
2. Re-run debug assembly plus unit/instrumented test-source compilation with the required Android Studio JBR; do not execute tests while no Android test machine is connected.
3. When the user-designated test machine is connected, run the plan's focused unit and Compose tests and record device identity, API level, commands, and results.
4. Close the import issue after the compile tasks succeed; keep acceptance-criteria test execution pending until the Android test machine is connected.

The fix stays within the approved UI-010 implementation scope and does not require a new Plan Review.

## UI010-IMP-002
1. Remove the unresolved `androidx.compose.ui.test.assertDoesNotExist` import from `ReturnFlowHostTest.kt`; existing host tests use this assertion without importing it.
2. Re-run `:app:compileDebugAndroidTestKotlin` as a compile-only check. Run the Compose tests only after the user-designated Android test machine is connected.

## UI010-IMP-003
1. Assert the duplicated detail key with a collection count instead of requiring a unique text node.
2. Click the fixed submit action directly; do not request scrolling when it has no scroll semantics parent.
3. Wait for and assert `survey-form-overlay` before retrieving the form ViewModel in the dirty-form test. If lookup still fails, capture the actual composition key/owner and fix the test access path without changing product behavior.
4. Re-run the focused host class on `Medium_Phone`; then run the relevant map, detail, and UI-008 regression host tests on the same AVD.
