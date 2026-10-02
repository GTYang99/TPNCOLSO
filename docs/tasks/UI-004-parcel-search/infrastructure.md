# UI-004 Instrumentation Infrastructure Diagnosis

## Scope and symptom

- Originating phase: Implementation developer validation.
- Issue: `INF-UI004-001`.
- Revision: working tree on `codex/ui-004-parcel-search`, based on `b9451b5d7c27b3ed0f04ad174117de5040a9a657`.
- Symptom: UI-004 Compose tests fail on the first node interaction with `IllegalStateException: No compose hierarchies found in the app` before any assertion executes.

## Evidence inspected

| Evidence | Observation |
|---|---|
| Android test runner configuration | `app/build.gradle.kts` selects `androidx.test.runner.AndroidJUnitRunner`. |
| Merged `debugAndroidTest` manifest | Compose test `BootstrapActivity`, `EmptyActivity`, and `EmptyFloatingActivity` are present. |
| Initial awake-independent AVD test attempts | UI-004 and Auth Compose controls failed before assertions while the emulator was asleep. |
| Emulator power state | `dumpsys power` reported `mWakefulness=Asleep`, last sleep reason `power_button`, and sleep duration over 90 minutes; screen-off timeout was `2147483647` ms and `always_finish_activities=0`. |
| Auth recovery control | After `adb -s emulator-5554 shell input keyevent KEYCODE_WAKEUP`, the focused `AuthScreenTest#loginRegisterEntryDispatchesEvent` passed 1/1. |
| UI-004 focused AVD rerun | Command used `ANDROID_SERIAL=emulator-5554 JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.feature.parcelsearch.ParcelSearchScreenTest`; 3/3 tests passed on `Medium_Phone (AVD) - 14`, API 34. |
| UI-003 emulator regression | `MapShellScreenTest` passed 7/7 on the awake AVD. |
| Emulator settings | `settings get global always_finish_activities` returns `0`; this setting does not explain the activity stopping. |
| Captured initial test log | The test `ComponentActivity` transitioned RESUMED -> PAUSED -> STOPPED before the first hierarchy query while the device was asleep. |
| Prior UI-004 physical-device result | The same exception was observed on `XQ-AU52`, Android 12, before the requester asked to stop physical-device testing. No further physical-device testing has been or will be run. |

## Classification and actions

- Classification: environment/device state.
- Diagnosis: the AVD was asleep from a prior power-button sleep state. In that state the test Activity was paused and no Compose hierarchy was available. The `always_finish_activities` setting was not responsible.
- Actions taken: inspected the runner, merged test manifest, test XML/log, and AVD power state; woke the AVD; ran a known Compose control; then reran UI-004 and UI-003 focused tests on the awake AVD. No source, Gradle, manifest, or product behavior was changed to restore the environment.
- Result: restored and verified. Auth control 1/1, UI-004 3/3, UI-003 regression 7/7 all passed after wake-up. Physical-device testing remains stopped per requester direction.

## Return route

Return to `implementation`; the awake AVD restored Compose instrumentation. UI-004 and UI-003 focused device tests now pass. No physical-device testing is needed for this return route.
