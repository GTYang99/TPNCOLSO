# Issue Log

| ID | Title | Category | Priority | Status | Affected AC | Route |
|---|---|---|---|---|---|---|
| ENV-UI005-001 | No connected Android device for Compose host validation | environment | P1 | resolved | AC-UI005-001 through AC-UI005-006 | verification |
| TEST-UI005-001 | Host assertions assumed unique or viewport-visible nodes | test | P2 | resolved | AC-UI005-001 through AC-UI005-005 | verification |

## ENV-UI005-001

- Expected: Run `:app:connectedDebugAndroidTest` for `ParcelDetailHostTest` on the configured emulator.
- Actual: `adb devices` returned no device and Gradle failed with `com.android.builder.testing.api.DeviceException: No connected devices!`.
- Reproduction: Android Studio JBR `/Applications/Android Studio.app/Contents/jbr/Contents/Home`; command `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.feature.parceldetail.ParcelDetailHostTest`.
- Impact: Connected evidence was initially unavailable; no product defect was established.
- Owner: local Android validation environment / task owner.
- Route: verification.
- Resolution: `Medium_Phone (AVD) - 14` was started and the focused UI005 plus UI003/UI004 regression suites passed.

## TEST-UI005-001

- Expected: Host assertions prove duplicate summary/detail values, scrollable read-only fields and the non-returned reason absence without weakening the AC.
- Actual: The original test required a unique duplicated land number and required offscreen fields to be immediately displayed.
- Resolution: Test-only selectors now assert the expected duplicate count, scroll to fields before display assertions and cover non-returned reason absence. Final UI005 connected run passed 4/4.
