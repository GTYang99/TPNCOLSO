# Issue Log

| ID | Title | Category | Priority | Status | Affected AC | Route |
|---|---|---|---|---|---|---|
| ENV-UI005-001 | No connected Android device for Compose host validation | environment | P1 | open | AC-UI005-001 through AC-UI005-006 | infrastructure |

## ENV-UI005-001

- Expected: Run `:app:connectedDebugAndroidTest` for `ParcelDetailHostTest` on the configured emulator.
- Actual: `adb devices` returned no device and Gradle failed with `com.android.builder.testing.api.DeviceException: No connected devices!`.
- Reproduction: Android Studio JBR `/Applications/Android Studio.app/Contents/jbr/Contents/Home`; command `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.feature.parceldetail.ParcelDetailHostTest`.
- Impact: Compose/device evidence for AC-UI005-001 through AC-UI005-006 is `NOT VERIFIED`; source compilation remains verified.
- Owner: local Android validation environment / task owner.
- Route: infrastructure; restore or attach an Android emulator, then rerun the focused host test and affected UI regression tests.
- Resolution: pending external device availability.
