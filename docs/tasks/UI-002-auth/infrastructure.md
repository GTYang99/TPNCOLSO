# Infrastructure Handoff

Task: UI-002-auth  
Originating phase: Implementation  
Classification: environment / Android build runtime

## Symptom and Reproduction

The Runtime Rule originally required only the macOS Android Studio JBR path:

`/Applications/Android Studio.app/Contents/jbr/Contents/Home`

The active host is Windows PowerShell in `C:\Users\GT.YANG\Documents\TPNCOLSO`. The required macOS path is unavailable, and `JAVA_HOME` was unset. Inspection found Android Studio JBR at `C:\Program Files\Android\Android Studio1\jbr`; `java.exe -version` reports OpenJDK `25.0.3` (2026-04-21). The wrapper is Gradle `9.6.0` and AGP is `9.4.1`. On 2026-10-14 the requester authorized this exact Windows path. `ai/rules/operations/runtime.md` now lists it as the required Windows JBR, keeps the macOS path, and prohibits fallback to other JDKs. Compatibility references checked 2026-10-14: Gradle supports Java 25 for running Gradle from 9.1 onward; AGP 9.4 requires Gradle 9.6 and lists JDK 17. These facts do not substitute for a build run.

## Affected Gates

- UI-002 R11 JVM auth tests
- Compose connected tests
- Debug and release builds and release source/resource isolation
- R11 runtime capture and visual comparison for the reflowed Login controls

At handoff, all affected checks remained `NOT VERIFIED`; no result was reported as PASS. The policy blocker is resolved, and checks will run under the authorized Windows JBR.

## Action and Result

- Requester approved the Windows JBR path; the Runtime Rule was updated before running Gradle. `C:\Program Files\Android\Android Studio1\jbr\bin\java.exe` reports OpenJDK 25.0.3 (build 25.0.3+-15898627-b508.16). The repository wrapper is Gradle 9.6.0; AGP is 9.4.1.
- Android SDK was present at `C:\Users\GT.YANG\AppData\Local\Android\Sdk`; Gradle initially could not locate it because `ANDROID_HOME` was unset. Set `ANDROID_HOME` and `ANDROID_SDK_ROOT` command-locally. Gradle installed requested Android SDK Platform 37.0 (revision 2). No project `local.properties` was created.
- `testDebugUnitTest --tests '*Auth*'`: PASS, 9 tests (AuthValidation 3, AuthViewModel 4, DebugAuthSessionCoordinator 2).
- `lintDebug assembleDebug assembleRelease assembleDebugAndroidTest`: PASS; release APK scan found no `DebugAuth`, CAPTCHA or CAPTCHA fixture entries.
- `connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.feature.auth.AuthScreenTest`: PASS, 17/17 on `Medium_Phone_API_36.0` / `emulator-5554`, API 36.
- R11 Login runtime screenshot: `r11-login-runtime.png`; 1080×2400, density 420 dpi, font scale 1.0, SHA-256 `F573401B73D354EDB364A3937ECD14991B75DBD6F2C53099A28E6B96AAE2C2A4`. Visual observation confirms CAPTCHA removal and lower-control reflow. Pixel comparison against the R10 source composite remains NOT VERIFIED.
- Broad suite findings: full `testDebugUnitTest` had one failure in `FakeReturnFlowStoreTest.confirmationChangesStatusRemovesReturnContextAndHidesNotification` (assertion at line 76). Full connected suite reached an unrelated `SurveyFormScreenTest.cleanCloseSkipsDiscardDialogAndRouteBackIsDisabledWhenNotForeground` failure and was stopped; the filtered Auth test class passed.
- Source-only changes are recorded in `execution-report.md`; static `git diff --check` passed. A source scan found only negative CAPTCHA absence assertions in `app/src`.
- Hosted CI is separate: the recorded R10 run failed; no R11 hosted run exists. The approved development plan defers hosted CI with owner and reactivation milestone.

Compatibility references checked 2026-10-14: [Gradle Java compatibility](https://docs.gradle.org/current/userguide/compatibility.html), [AGP 9.4 release notes](https://developer.android.com/build/releases/agp-9-4-0-release-notes).

## Infrastructure Resolution

Decision: requester approved `C:\Program Files\Android\Android Studio1\jbr` on 2026-10-14. The Runtime Rule now defines platform-specific required paths and retains the no-fallback rule. The environment issue is resolved; implementation validation remains pending until executed.

## Return Route

Return route: `implementation`. JBR/SDK access is restored. Investigate the unrelated suite failures and complete the remaining source-composite visual comparison; do not report task Verification PASS while those required gates remain open.
