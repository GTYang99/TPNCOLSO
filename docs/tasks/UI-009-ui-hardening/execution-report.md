# UI-009 Execution Report

## Revision and Environment
- Branch: `developer`.
- Tested source revision: base `06092e8566887def8363418e8dee092e40364e54` plus the UI-009 application/test changes, committed unchanged as `221a160e0a2140d99c81266a482f9215a29b58cc`. No application, test, Gradle, or script source was changed after the final matrix; the later edits were task-document updates only.
- Tested application/test working-tree fingerprint (SHA-256 over `git diff --binary -- app gradle/libs.versions.toml`, followed by each sorted untracked `app/` or `scripts/` path and its file bytes): `fc3d51b4d6e26a0d051ce446418fefa5dd73ea359fedb08ad7a079fc071156eb`.
- Knowledge baseline: `KB-UI-009-UI-HARDENING-R1`; Plan Review iteration 5 is the current approved test design before implementation validation.
- Runtime: Android Studio JBR `/Applications/Android Studio.app/Contents/jbr/Contents/Home`, OpenJDK `25.0.3`.
- Devices: `Medium_Phone (AVD) - 14`, Android 14 / API 34, 1080×2400 at 420 dpi; and `UI009_API28_EXT`, Android 9 / API 28, 1080×1920 at 420 dpi. The API 28 Google APIs ARM64 image, Emulator, platform-tools, and Pixel 2 AVD are installed in a dedicated APFS sparse image at `/Volumes/Elements/Android28SDK.sparseimage` because the internal volume lacks extraction space.
- Scope: implementation, emulator Developer Validation, Code Review, and development-scope Verification are complete for AC-001–AC-007 on revision `221a160e0a2140d99c81266a482f9215a29b58cc`. Hosted CI remains deferred as recorded below.

## Changes
- Added a debug-only `SavedStateHandle` ViewModel factory and connected the existing debug flow ViewModels to lifecycle owners.
- Added transient saved-state restoration for map selection, parcel query/detail selection, and the approved survey fields/photo references. Camera runtime objects and authentication password/captcha values are not saved.
- Made parcel detail content scroll as a unit, and added a visible recovery message for unreadable restored photo URIs.
- Extended focused UI tests for narrow viewports, larger text, invalid photo URI recovery, small ViewModel state round-trips, and UI006 plus map/search/detail Activity-recreation flows using debug-only route hosts.
- No database, API, DTO, permission, or long-term persistence was introduced.

## Validation

| Check | Command / Method | Result |
|---|---|---|
| Unit tests and debug build | `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:testDebugUnitTest :app:assembleDebug` | PASS; latest run succeeded. `SavedStateRestorationTest` reports 5 tests. |
| Release build | `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:assembleRelease` | PASS; latest run succeeded. |
| Android instrumentation suite, normal viewport | `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:connectedDebugAndroidTest` | PASS on API 34; 56 tests, 0 failures, 0 errors, 0 skipped. XML: `app/build/outputs/androidTest-results/connected/debug/TEST-Medium_Phone(AVD) - 14.xml`. |
| Android instrumentation suite, narrow viewport / larger text | Same connected Android test task after setting emulator to 320dp width and font scale 1.3 | PASS on API 34; 56 tests, 0 failures, 0 errors, 0 skipped. Emulator size and font scale were restored to 1080×2400 and 1.0 afterward. |
| Direct survey route Activity recreation | `StateRestorationHostTest.surveyRouteRemainsUsableAndKeepsDraftAcrossActivityRecreation` via connected Android tests | PASS for Activity recreation; retains note, photo URI, dirty state, and usable route. This is not process-death evidence. |
| System process death and Recents restoration | `UI009_EXPECTED_API=34 ./scripts/ui009_process_death_check.sh` on `Medium_Phone (AVD) - 14` | PASS for UI-006 on API 34. The host script backgrounded the app, used `am kill`, reopened the task from system Recents, then found the route, draft marker, dirty marker, and photo marker in the restored accessibility dump. |
| Debug/release source-set boundary | `:app:assembleDebug :app:assembleRelease`; inspect merged release manifest | PASS; both variants build and the release manifest excludes all three UI-009 debug-only Activities. |
| Whitespace check | `git diff --check` | PASS. |

## Acceptance-Criteria Evidence Status
- `AC-001`: PASS for the registered UI-002–UI-008 screen/state matrix. Each route family has focused instrumentation coverage, and the complete suite passed across the current viewport/font matrix below. The approved flow owners and fake-data boundary remain unchanged.
- `AC-002`: PASS for the emulator matrix below. All 62 connected UI tests passed on Android 9 and Android 14 at 320dp, 360dp, and approximately 402×874dp. These flow and scroll assertions found no viewport condition that blocked the registered route actions.
- `AC-003`: PASS for the registered route flows at font scale 1.3 across the same three widths and both Android versions. Each complete suite passed with no functional text/action assertion failure; scrollable route content remains reachable in the tested flows.
- `AC-004`: PASS for the tested text-entry routes on API 28 and API 34. On both emulators, login, registration, parcel search and UI-006 survey form were focused with the real system Gboard open. Login and registration primary actions were brought above Gboard by scrolling; search and survey primary actions remained visible above it. The latest API 34 captures are `evidence/api34-ac004-login-ime-scroll.png`, `evidence/api34-ac004-register-ime-scroll.png`, `evidence/api34-ac004-search-ime.png`, and `evidence/api34-ac004-survey-ime.png`. API 28 captures are `evidence/api28-login-captcha-ime-visible.png`, `evidence/api28-register-complete-ime-visible-after-scroll.png`, `evidence/api28-parcel-search-ime-visible.png`, and `evidence/api28-survey-ime-submit-visible.png`. Visual review found the route content and actions outside the status-bar and navigation-gesture areas in these captures; API 28 `dumpsys input_method` also confirmed the IME was visible. No API/backend was called.
- `AC-005`: PASS for the registered UI-002–UI-008 routes. Tests verify named map/auth/camera controls, 48dp map and photo-delete targets, the 64dp shutter, and 48dp shared icon/primary/secondary controls on API 28 and API 34. Source review confirms text-based actions and error/status messages retain labels; checkbox/radio controls expose checked/selected semantics and visible marks, and the selected map basemap now has a visible check mark. The formal per-AC Verification result is recorded in `verification.md`.
- `AC-006`: PASS for the registered stateful UI flows. Activity recreation and host-side process-death flows cover map selection, parcel-search query/summary, parcel-detail selection, survey draft/photo/dirty state, camera return, login sensitive-value reset, and both discard/photo-delete dialog outcomes on API 28 and API 34. Raw UIAutomator evidence is under `evidence/`.
- `AC-007`: PASS for the scoped source audit. The implementation uses transient `SavedStateHandle` values and debug-only test hosts; the diff introduces no endpoint, API call, DTO, database or long-term form persistence. Release-manifest checks recorded below confirm the UI-009 test Activities stay out of release.

## Limitations and Deferred Gates
- API 28 uses a Google APIs ARM64 image and AVD on a dedicated APFS sparse image on the external volume. An initial full suite encountered a transient System UI ANR; after recovery, the focused form class, subsequent full suites, and final viewport/text-scale matrix passed. The initial failure remains documented under resolved issue UI009-011 with `evidence/api28-systemui-anr.png`.
- An AndroidTest attempt to kill the target package also terminated the AndroidJUnitRunner process and failed before post-restart assertions. It was removed and replaced by the external host ADB script, which passed the API 34 UI-006 scenario.
- Hosted CI remains deferred for pre-release development; owner: release/project maintainer; reactivate at release candidate.
- Revision `221a160e0a2140d99c81266a482f9215a29b58cc` is committed, reviewed, and passed development-scope Verification; see `verification.md`. No hosted CI PASS or release authorization is claimed.
- Existing UI-008/UI-010 changes in the working tree were preserved and are outside this report's implementation scope.

## Developer Validation Update — 2026-10-08

- Tested working-tree identity remains the app/source fingerprint above, on branch `developer` at `HEAD 06092e8566887def8363418e8dee092e40364e54` plus uncommitted UI-009 changes.
- Runtime: `Medium_Phone (AVD) - 14`, Android 14 / API 34, 1080×2400, 420 dpi, font scale 1.0; Android Studio JBR OpenJDK 25.0.3.
- `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:connectedDebugAndroidTest` — PASS, 56 tests, 0 failures, 0 errors, 0 skipped. Result XML: `app/build/outputs/androidTest-results/connected/debug/TEST-Medium_Phone(AVD) - 14.xml` (11:53, 2026-10-08).
- Repeated the same task with emulator override size `840x2400` (320dp at 420dpi) and system font scale `1.3` — PASS, 56 tests, 0 failures, 0 errors, 0 skipped. Result XML at 11:54, 2026-10-08. Restored emulator size, density and font scale to 1080×2400, 420dpi and 1.0; API remains 34.
- Repeated the same task with emulator override size `945x2400` (360dp at 420dpi), font scale `1.0` — PASS, 56 tests, 0 failures, 0 errors, 0 skipped (12:00, 2026-10-08).
- Repeated the same task with emulator override size `1055x2294` (approximately 402×874dp at 420dpi), font scale `1.0` — PASS, 56 tests, 0 failures, 0 errors, 0 skipped (12:04, 2026-10-08). XML result: `app/build/outputs/androidTest-results/connected/debug/TEST-Medium_Phone(AVD) - 14.xml`.
- Re-ran `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:installDebug && UI009_EXPECTED_API=34 ./scripts/ui009_process_death_check.sh`. Debug install PASS. Process-death procedure reached the expected route before termination, but then failed to locate a foreground Recents task card. The AVD uses gesture navigation (`navigation_mode=2`); `KEYCODE_APP_SWITCH` did not expose a task card to the accessibility dump in this run. No post-restart state claim is made from this attempt. Original API-34 process-death evidence remains recorded above; repeatability on the current AVD is NOT VERIFIED.
- Tried switching the AVD navigation mode temporarily to three-button navigation to diagnose Recents; UiAutomator returned a null root after the mode change. Restored the original gesture navigation, display size and font scale. This was an emulator/tooling diagnostic, not an application failure.
- At the time of this entry, the internal SDK had API 34, 36 and 37 images; its API 28 install failed with `No space left on device`. Later follow-up installed an isolated API 28 SDK and AVD on the external APFS volume.

## Implementation Debug Resolution — 2026-10-08

- Issue `UI009-009` was resolved within the approved UI-006 IME scope. `SurveyFormScreen` now applies root `imePadding()`. The debug-only restoration Activity now mirrors MainActivity by enabling edge-to-edge and declaring `adjustResize` in the debug manifest.
- API 34 Gboard manual evidence: login, registration, parcel search and UI-006 form fields displayed the IME; their primary actions were visible/reachable after scrolling where needed. UI-006's `送出調查` action was observed at bounds `[436,1354][644,1429]`, above the keyboard. Before-fix screenshot: `evidence/api34-ui006-ime-submit-obscured.png`; after-fix screenshot: `evidence/api34-ui006-ime-submit-visible.png`.
- The focused `SurveyFormScreenTest` class passed (9 tests). The latest full command `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:connectedDebugAndroidTest` passed on API 34: 56 tests, 0 failures, 0 errors, 0 skipped. XML timestamp: `2026-10-08T04:33:14Z`.
- A temporary Compose assertion that attempted to read system IME visibility in the instrumentation runner was removed because that runner did not expose the system IME state; host-side ADB validation is the evidence for actual Gboard visibility. The focused and full suites passed after removing the non-representative assertion.
- API 28 installation was retried on the internal volume with Google APIs ARM64 at 2.3 GiB and 2.9 GiB free, and with the default ARM64 image at 2.8 GiB free. Those attempts failed with `No space left on device`. An isolated APFS sparse image and AVD were later created on the external volume; existing SDK images and AVDs were preserved.
- `UI009-008` is resolved for API 34 UI-006: two consecutive runs after debug-host alignment passed, restoring the route, draft marker, dirty marker, and photo marker after process kill and Recents selection. AC-006 remains partial / NOT VERIFIED overall until API 28 and remaining stateful routes/dialog outcomes have evidence.
- Current developer validation is still incomplete. Resume `implementation` and continue the task AC matrix; do not enter Verification yet.

## API 34 Follow-up — 2026-10-08

- Device remained `Medium_Phone (AVD) - 14`, Android 14 / API 34, 1080×2400, 420 dpi, font scale 1.0, gesture navigation (`navigation_mode=2`). Settings were checked after execution and remain at these original values.
- `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:installDebug` — PASS; debug APK installed on `emulator-5554`.
- `UI009_EXPECTED_API=34 ./scripts/ui009_process_death_check.sh` — PASS in two consecutive runs. Both runs killed the target process, restored the task through Recents, and found the UI-006 route, draft, dirty state, and photo marker. This resolves UI009-008 for API 34 UI-006; it does not cover other stateful routes or API 28.
- `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.feature.StateRestorationHostTest` — PASS, 1 test, 0 failures/errors/skipped. It verified UI-006 draft retention and usable route after Activity recreation. Result XML timestamp: `2026-10-08T04:41:45`.
- A combined class filter for `StateRestorationHostTest,SurveyFormScreenTest` only selected the first class (1 test); it is not counted as SurveyForm evidence. `SurveyFormScreenTest` was then run separately after the AC-005 change and passed 9 tests, 0 failures/errors/skipped. Result XML timestamp: `2026-10-08T04:45:20`.
- AC-005 review found UI-006 photo delete hit box was 40×40dp. Issue UI009-010 was logged; the clickable box was increased to 48×48dp. The test `deletePhotoRequiresConfirmationAndCancelKeepsPhoto` now asserts 48dp semantics width and height and exercises the confirmation/cancel behavior on the connected API 34 emulator. UI009-010 is resolved; AC-005 remains partial / NOT VERIFIED overall pending the other screen families.
- `git diff --check` — PASS after documentation and source/test changes.
- AC-006 remains partial / NOT VERIFIED overall; process-death coverage for the other stateful routes/dialog outcomes remains outstanding. The API 28 first-run System UI ANR (UI009-011) was resolved after recovery and repeat passes.

## API 28 First Validation — 2026-10-08

- Runtime: `UI009_API28_EXT` Pixel 2 AVD, Android 9 / API 28, 1080×1920 at 420 dpi; SDK and AVD data are on `/Volumes/Android28SDK` (APFS sparse image on `/Volumes/Elements`). Connected device: `emulator-5556`.
- `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:connectedDebugAndroidTest` — FAIL, 56 tests, 4 failures, 0 errors, 0 skipped. XML timestamp: `2026-10-08T05:15:48`. The four failures (`cleanCloseSkipsDiscardDialogAndRouteBackIsDisabledWhenNotForegrounded`, `dirtyCloseAndBackUseDiscardDialogAndContinuePreservesEdits`, `cleanSystemBackClosesWithoutDiscardDialog`, `deletePhotoRequiresConfirmationAndCancelKeepsPhoto`) all reported Espresso `RootViewWithoutFocusException` with `has-window-focus=false` while Android displayed `System UI isn't responding`; screenshot: `evidence/api28-systemui-anr.png`. No product assertion failure was reported.
- Selected `Wait` in the System UI ANR dialog without clearing app data. The system recovered and the focused API 28 command `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.feature.surveyform.SurveyFormScreenTest` then passed 9 tests, 0 failures, 0 errors, 0 skipped. XML timestamp: `2026-10-08T05:19:56`. This includes Back/discard-dialog behavior and the 48×48dp photo-delete semantics bounds assertion.
- The second full API 28 suite passed after System UI recovery: 56 tests, 0 failures, 0 errors, 0 skipped; XML timestamp `2026-10-08T05:31:32`. Issue UI009-011 is resolved as a transient AVD System UI ANR. The AVD was also tested at 320dp with font scale 1.3: 56 tests, 0 failures, 0 errors, 0 skipped; XML timestamp `2026-10-08T05:39:55`. Display size, density and font scale were restored after that run.
- `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:installDebug` — PASS on `emulator-5556`.
- `UI009_EXPECTED_API=28 UI009_ADB_SERIAL=emulator-5556 ./scripts/ui009_process_death_check.sh` — PASS. After process kill and Recents restoration, the UI-006 route, draft, dirty state and photo marker remained visible on Android 9 / API 28.
- Repeated `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:connectedDebugAndroidTest` at 360dp (`wm size 945x2100`, 420dpi, font scale 1.0) — PASS, 56 tests, 0 failures, 0 errors, 0 skipped. Result XML timestamp: `2026-10-08T05:52:15Z`. Restored physical size 1080×1920, density 420dpi, font scale 1.0.
- Repeated the full connected suite at the API 28 reference viewport (`wm size 1055x2294`, approximately 402×874dp at 420dpi) with system font scale 1.3 — PASS, 56 tests, 0 failures, 0 errors, 0 skipped. Result XML timestamp: `2026-10-08T06:05:43Z`. Restored the Pixel 2 AVD to physical size 1080×1920, density 420dpi and font scale 1.0.
- Repeated the full connected suite on Android 14 at the same reference viewport and font scale (`1055x2294`, 420dpi, font scale 1.3) — PASS, 56 tests, 0 failures, 0 errors, 0 skipped. Result XML timestamp: `2026-10-08T06:07:54Z`. Restored the API 34 AVD to physical size 1080×2400, density 420dpi and font scale 1.0.
- After UI009-012, ran `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.feature.auth.AuthScreenTest` — PASS on API 34 and API 28, 18 tests per device, 0 failures/errors/skipped. Result XML timestamps: `2026-10-08T06:09:57Z` (API 34) and `2026-10-08T06:12:26Z` (API 28). UI009-012 is resolved.
- API 28 manual Gboard checks followed the fake-data UI flow on Android 9: login captcha, registration account/name, parcel search, and UI-006 land-location fields were focused; the system input method reported visible. Login, registration, search, and UI-006 submit actions were brought above the rendered keyboard. The final registration capture followed an additional upward scroll until the 「完成」 button and Gboard were both visible; the system dump confirmed IME visibility. No backend/API was used.
- API 28 UI009 AC matrix remains in progress; remaining route/process-death matrix and final route-by-route review still require evidence.

## Focused UI Flow Follow-up — 2026-10-08

- To keep lifecycle checks UI-focused and representative, `StateRestorationHostTest` now launches the debug-only `Ui009StateRestoreActivity`, which builds the route from `onCreate`; it no longer manually reattaches Compose content after `ActivityScenario.recreate()`.
- The single UI flow seeds an unsaved note, uses the visible 「關閉填報」 action, verifies the discard dialog, recreates the Activity, verifies the draft and dialog remain, selects 「繼續編輯」, then opens the dialog again to confirm the route remains usable.
- `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ANDROID_HOME=/Volumes/Android28SDK/AndroidSdk ANDROID_SDK_ROOT=/Volumes/Android28SDK/AndroidSdk ANDROID_SERIAL=emulator-5554 ./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.feature.StateRestorationHostTest` — PASS, 1 test, 0 failures/errors/skipped on `UI009_API28_EXT (AVD)`, Android 9 / API 28. Result XML timestamp: `2026-10-08T06:29:52Z`.
- `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ANDROID_HOME=/Users/a10362/Library/Android/sdk ANDROID_SDK_ROOT=/Users/a10362/Library/Android/sdk ANDROID_SERIAL=emulator-5556 ./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.feature.StateRestorationHostTest` — PASS, 1 test, 0 failures/errors/skipped on `Medium_Phone (AVD)`, Android 14 / API 34. Result XML timestamp: `2026-10-08T06:27:59Z`.
- An earlier exploratory version manually reattached test-owned Compose content after recreation and failed to retain the dialog. That harness did not mirror the app Activity's `onCreate` lifecycle. The final test uses the real debug-only restoration Activity and passes on both APIs; no production dialog change was needed.
- The test focus follows the requester direction to prioritize UI flows. Five small state round-trip tests support saved-state logic; route lifecycle validation uses four focused UI flows (UI006, map, search, and detail).

## Stateful Route UI Flows — 2026-10-08

- Added a debug-only fixed-identity flow Activity whose `onCreate` mounts the existing fake-data map/search/detail flow, so Activity recreation exercises the real saved-state owner and route composition lifecycle. It is declared only in the debug manifest.
- `StatefulUiFlowRecreationTest` drives visible controls without API calls: select the orthophoto basemap; query `TEST-KEY-001` and open/close its summary; query the same fake parcel, open details, select the survey tab and 2025 history period, then close details. Each case recreates the Activity and verifies the route and selection remain usable.
- `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ANDROID_HOME=/Volumes/Android28SDK/AndroidSdk ANDROID_SDK_ROOT=/Volumes/Android28SDK/AndroidSdk ANDROID_SERIAL=emulator-5554 ./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.feature.StatefulUiFlowRecreationTest` — PASS, 3 tests, 0 failures/errors/skipped on `UI009_API28_EXT (AVD)`, Android 9 / API 28. XML timestamp: `2026-10-08T06:43:08Z`.
- `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ANDROID_HOME=/Users/a10362/Library/Android/sdk ANDROID_SDK_ROOT=/Users/a10362/Library/Android/sdk ANDROID_SERIAL=emulator-5556 ./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.feature.StatefulUiFlowRecreationTest` — PASS, 3 tests, 0 failures/errors/skipped on `Medium_Phone (AVD)`, Android 14 / API 34. XML timestamp: `2026-10-08T06:45:20Z`.
- `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ANDROID_HOME=/Users/a10362/Library/Android/sdk ANDROID_SDK_ROOT=/Users/a10362/Library/Android/sdk ./gradlew :app:assembleRelease` — PASS. Merged release manifest contains neither `Ui009StateRestoreActivity` nor `Ui009StatefulFlowTestActivity`; the debug manifest contains both.
- An earlier version mounted `DebugSignedInContent` in a generic test-owned `ComponentActivity` and manually reattached it after recreation; search/detail route flags were lost in that harness. The final test uses a debug-only Activity that creates the content during every `onCreate`, and all three route flows pass on both APIs. The exploratory generic-host results are not treated as application behavior evidence.
- AC-006 remains partial / NOT VERIFIED: API 28 and API 34 process-death flows now cover map selection, search query/summary, detail history selection, UI-006 draft/photo and UI-007 camera route. Dialog process-death outcomes and the final route/state review remain outstanding.

## Follow-up
- Extend process-death evidence to the remaining stateful routes where AC-006 applies.
- Finish the remaining route/state, viewport and accessibility review.
- Resume implementation and complete all task-scoped developer validation before commit and Verification.

## API 28 Registration UI Flow — 2026-10-08

- Tested revision: `06092e8566887def8363418e8dee092e40364e54` plus the UI-009 working tree identified above; branch `developer`.
- Device: `UI009_API28_EXT (AVD)`, Android 9 / API 28, 1080×1920, 420 dpi, font scale 1.0; Google Gboard.
- Manual flow: open the debug app, tap 「註冊」, focus the account field, then scroll the registration form upward until its lower fields and 「完成」 action are above the keyboard.
- `adb shell dumpsys input_method` — observed `mImeWindowVis=Active|Visible`, `mShowRequested=true`, and `mInputShown=true` while the final screenshot showed both the complete 「完成」 button and Gboard. Screenshot: `evidence/api28-register-complete-ime-visible-after-scroll.png` (1080×1920).
- This confirms the registration primary action can be brought into the visible area while the system IME remains visible on API 28. It does not complete the other route/viewport safe-area review; AC-004 remains partial / NOT VERIFIED overall.

## Camera Screen UI Flow — 2026-10-08

- Tested source revision: `06092e8566887def8363418e8dee092e40364e54` plus the existing UI-009 working tree; branch `developer`.
- Device: `Medium_Phone (AVD) - 14`, Android 14 / API 34, 1080×2400 at 420 dpi, font scale 1.0.
- `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ANDROID_HOME=/Users/a10362/Library/Android/sdk ANDROID_SDK_ROOT=/Users/a10362/Library/Android/sdk ANDROID_SERIAL=emulator-5556 ./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.feature.photocamera.PhotoCameraScreenTest` — PASS, 3 tests, 0 failures/errors/skipped. Result XML timestamp: `2026-10-08T06:48:00`.
- Visible UI interactions verified: denied permission message and retry action; granted-state preview slot, shutter callback and 「取消拍攝」 action; capture-in-progress state disables the shutter.
- The granted-state preview uses a Compose placeholder. This screen-only check does not exercise Android permission dialogs, a physical camera, CameraX binding or photo capture. Camera route Activity recreation is covered separately below; camera process-death restoration remains NOT VERIFIED.
- `git diff --check` — PASS after recording this result.

## Camera Route Activity Recreation UI Flow — 2026-10-08

- Added `PhotoCameraUiFlowTest` using the debug-only fixed-identity fake-data Activity. The UI flow searches for `TEST-KEY-002`, opens its detail and edit form, opens the camera route, recreates the Activity, then cancels capture and verifies return to the same survey form.
- The test grants the app's camera permission through Android instrumentation before opening the route. It does not interact with the system permission dialog; it exercises the granted UI path and Activity recreation. The flow adds no API/backend calls and no production-screen behavior.
- Android 9 / API 28: `UI009_API28_EXT (AVD)`, 1080×1920 at 420 dpi. `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ANDROID_HOME=/Volumes/Android28SDK/AndroidSdk ANDROID_SDK_ROOT=/Volumes/Android28SDK/AndroidSdk ANDROID_SERIAL=emulator-5554 ./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.feature.PhotoCameraUiFlowTest` — PASS, 1 test, 0 failures/errors/skipped. XML: `evidence/photo-camera-flow-api28.xml`, timestamp `2026-10-08T06:52:08`.
- Android 14 / API 34: `Medium_Phone (AVD)`, 1080×2400 at 420 dpi. `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ANDROID_HOME=/Users/a10362/Library/Android/sdk ANDROID_SDK_ROOT=/Users/a10362/Library/Android/sdk ANDROID_SERIAL=emulator-5556 ./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.feature.PhotoCameraUiFlowTest` — PASS, 1 test, 0 failures/errors/skipped. XML: `evidence/photo-camera-flow-api34.xml`, timestamp `2026-10-08T06:52:48`.
- This verifies the app's camera route remains visible after Activity recreation and returns to its owning form. It does not prove system process-death restoration, CameraX preview/binding, or real photo capture; these remain outside the evidence.
- `git diff --check` — PASS after test and report updates.

## Latest Full UI Regression — 2026-10-08

- The full connected Compose instrumentation suite, including `PhotoCameraUiFlowTest`, passed on both supported runtime endpoints. Each result contains 60 tests, 0 failures, 0 errors, and 0 skipped tests.
- Android 14 / API 34: `Medium_Phone (AVD)`, 1080×2400 at 420 dpi. `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ANDROID_HOME=/Users/a10362/Library/Android/sdk ANDROID_SDK_ROOT=/Users/a10362/Library/Android/sdk ANDROID_SERIAL=emulator-5556 ./gradlew :app:connectedDebugAndroidTest` — PASS. Result: `evidence/ui-suite-api34.xml`, timestamp `2026-10-08T06:58:40`.
- Android 9 / API 28: `UI009_API28_EXT (AVD)`, 1080×1920 at 420 dpi. `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ANDROID_HOME=/Volumes/Android28SDK/AndroidSdk ANDROID_SDK_ROOT=/Volumes/Android28SDK/AndroidSdk ANDROID_SERIAL=emulator-5554 ./gradlew :app:connectedDebugAndroidTest` — PASS. Result: `evidence/ui-suite-api28.xml`, timestamp `2026-10-08T07:01:09`.
- An earlier full API 34 attempt failed in a test-only permission cleanup hook after the test runner had removed the target package. No product assertion failed. The cleanup hook was removed; the targeted route flow then passed on both APIs, and both full suites passed with the finalized test.
- These runs are local Developer Validation evidence, not hosted CI or Verification PASS. AC-001–AC-007 still require the remaining route-by-route viewport, typography, IME, accessibility, process-death, and final-scope reviews described above. No commit or code review is claimed.

## Camera Route Process-Death UI Flow — 2026-10-08

- Added debug-only `Ui009CameraStateRestoreActivity` and `scripts/ui009_camera_process_death_check.sh`. The activity opens the existing camera route over a fake UI-006 form and seeds a harmless draft/photo marker. The host script checks the camera page, backgrounds the app, terminates only the app process, restores its task from system Recents, then selects 「取消拍攝」 and checks that the owning form still exposes the draft, dirty state and photo marker. The script restores the camera permission to its prior grant state.
- Android 14 / API 34: `Medium_Phone (AVD)`. `PATH="/Users/a10362/Library/Android/sdk/platform-tools:$PATH" UI009_EXPECTED_API=34 UI009_ADB_SERIAL=emulator-5556 ./scripts/ui009_camera_process_death_check.sh` — PASS; camera route restored after process termination and form markers remained visible after cancelling the camera route.
- Android 9 / API 28: `UI009_API28_EXT (AVD)`. `PATH="/Volumes/Android28SDK/AndroidSdk/platform-tools:$PATH" UI009_EXPECTED_API=28 UI009_ADB_SERIAL=emulator-5554 ./scripts/ui009_camera_process_death_check.sh` — PASS. Post-restore accessibility dump: `evidence/camera-process-death-api28.xml`; it contains the survey route, draft marker, photo marker and dirty-state cue, with the camera cancel action no longer present after returning to the form.
- `:app:installDebug` succeeded on both devices with Android Studio JBR OpenJDK 25.0.3 before the host checks. `bash -n scripts/ui009_camera_process_death_check.sh` passed. No API/backend was used. CameraX binding and real photo capture are not asserted.
- `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:assembleRelease` — PASS. The merged release manifest omits `Ui009CameraStateRestoreActivity`, `Ui009StateRestoreActivity`, and `Ui009StatefulFlowTestActivity`; the debug manifest declares all three.
- AC-006 remains partial / NOT VERIFIED after map selection, search query/summary and detail history process-death flows passed; dialog outcomes and remaining route/state review are still open. No production-source change was made.

## Map, Search and Detail Process-Death UI Flow — 2026-10-08

- Added debug-only `Ui009StatefulRouteRestoreActivity` and `scripts/ui009_stateful_routes_process_death_check.sh`. The host starts the existing map/search/detail routes with fake local data and a selected orthophoto basemap, a matched `TEST-KEY-001` query/summary and the 2025 history period. It backgrounds the task, kills the app process, restores from Recents, confirms the detail history date and readonly/history content, closes detail to return to the original query/summary, then closes those surfaces and verifies the map still exposes 「底圖 正射圖」. No API or backend is used.
- Android 14 / API 34: `Medium_Phone (AVD)`. `PATH="/Users/a10362/Library/Android/sdk/platform-tools:$PATH" UI009_EXPECTED_API=34 UI009_ADB_SERIAL=emulator-5554 ./scripts/ui009_stateful_routes_process_death_check.sh` — PASS. Dumps: `evidence/routes-detail-process-death-api34.xml`, `evidence/routes-search-process-death-api34.xml`, and `evidence/routes-map-process-death-api34.xml`.
- Android 9 / API 28: `UI009_API28_EXT (AVD)`. `PATH="/Volumes/Android28SDK/AndroidSdk/platform-tools:$PATH" UI009_EXPECTED_API=28 UI009_ADB_SERIAL=emulator-5556 ./scripts/ui009_stateful_routes_process_death_check.sh` — PASS. Dumps: `evidence/routes-detail-process-death-api28.xml`, `evidence/routes-search-process-death-api28.xml`, and `evidence/routes-map-process-death-api28.xml`.
- The saved accessibility XML for both APIs was parsed and matched the history date/content, original query and summary controls, and map content description/text for the restored orthophoto basemap. `bash -n scripts/ui009_stateful_routes_process_death_check.sh` and `git diff --check` passed.
- `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:assembleRelease` — PASS. The release manifest excludes `Ui009StatefulRouteRestoreActivity`; the debug manifest contains it. The host-script startup and text-node matching needed adjustments for Android 14/Android 9 differences; those were test-harness issues, not product assertion failures.
- AC-006 remains partial / NOT VERIFIED: process-death behavior for dialog outcomes and remaining route/state review must still be covered. This change adds debug-only test support; no production-source change was made.


## Discard-Dialog Process-Death UI Flow — 2026-10-08

- Extended `scripts/ui009_process_death_check.sh` to open the form's 「是否捨棄未儲存的內容？」 dialog, background the app, terminate its process, reopen the task from Recents, and verify the dialog text and both actions. It then selects 「繼續編輯」 and checks that the form, draft marker, dirty-state cue, and photo marker remain visible.
- Android 9 / API 28: `UI009_API28_EXT (AVD)`, 1080×1920 at 420 dpi. `PATH="/Volumes/Android28SDK/AndroidSdk/platform-tools:$PATH" UI009_EXPECTED_API=28 UI009_ADB_SERIAL=emulator-5554 UI009_EVIDENCE_DIR="docs/tasks/UI-009-ui-hardening/evidence" ./scripts/ui009_process_death_check.sh` — PASS. Evidence: `evidence/discard-dialog-process-death-api28.xml`.
- Android 14 / API 34: `Medium_Phone (AVD)`, 1080×2400 at 420 dpi. `PATH="/Users/a10362/Library/Android/sdk/platform-tools:$PATH" UI009_EXPECTED_API=34 UI009_ADB_SERIAL=emulator-5556 UI009_EVIDENCE_DIR="docs/tasks/UI-009-ui-hardening/evidence" ./scripts/ui009_process_death_check.sh` — PASS. Evidence: `evidence/discard-dialog-process-death-api34.xml`.
- Both saved accessibility dumps contain the dialog title, explanatory text, 「繼續編輯」, and 「捨棄編輯」. The host flow also tapped 「繼續編輯」 and confirmed the original fake form draft, dirty state, and photo marker remained present. This was UI/OS lifecycle validation with local fake data; no API or backend was used.
- `bash -n scripts/ui009_process_death_check.sh` and `git diff --check -- scripts/ui009_process_death_check.sh` — PASS. AC-006 remains partial / NOT VERIFIED overall; this flow closes only its dialog process-death case, not the remaining route/state and acceptance-criteria review.


## Discard Confirmation UI Flow — 2026-10-08

- Extended `scripts/ui009_process_death_check.sh` to cover both explicit outcomes after restoring the discard dialog: 「繼續編輯」 keeps the seeded draft, dirty cue, and photo marker; reopening the dialog and choosing 「捨棄編輯」 closes `Ui009StateRestoreActivity` and leaves no form or dialog visible.
- Android 14 / API 34 (`Medium_Phone` AVD, 1080×2400 at 420 dpi): `PATH="/Users/a10362/Library/Android/sdk/platform-tools:$PATH" UI009_EXPECTED_API=34 UI009_ADB_SERIAL=emulator-5554 UI009_EVIDENCE_DIR="docs/tasks/UI-009-ui-hardening/evidence" ./scripts/ui009_process_death_check.sh` — PASS. Accessibility dumps: `evidence/discard-dialog-process-death-api34.xml`, `evidence/discard-dialog-discard-result-api34.xml`.
- Android 9 / API 28 (`UI009_API28_EXT` AVD, 1080×1920 at 420 dpi): `PATH="/Volumes/Android28SDK/AndroidSdk/platform-tools:$PATH" UI009_EXPECTED_API=28 UI009_ADB_SERIAL=emulator-5554 UI009_EVIDENCE_DIR="docs/tasks/UI-009-ui-hardening/evidence" ./scripts/ui009_process_death_check.sh` — PASS. Accessibility dumps: `evidence/discard-dialog-process-death-api28.xml`, `evidence/discard-dialog-discard-result-api28.xml`.
- Both APIs passed the post-discard check that the survey form Activity is gone and the accessibility hierarchy contains neither the form nor the discard dialog. XML parsing confirmed both restored-dialog dumps expose the title, message, continue action, and discard action. No API/backend was used.
- `bash -n scripts/ui009_process_death_check.sh` and `git diff --check -- scripts/ui009_process_death_check.sh` — PASS. AC-006 remains partial / NOT VERIFIED overall pending the remaining route/state review.


## Photo-Delete Dialog Process-Death UI Flow — 2026-10-08

- Extended `scripts/ui009_process_death_check.sh` with a second fake form task. It opens the photo-delete dialog, backgrounds the app, kills the app process, restores from Recents, then chooses 「取消」 and verifies the original draft, dirty cue, and photo marker remain. It reopens the dialog, chooses 「刪除」, and verifies only the photo marker/delete action disappear while the form, draft, and dirty cue remain.
- Android 14 / API 34 (`Medium_Phone` AVD, 1080×2400 at 420 dpi): `PATH="/Users/a10362/Library/Android/sdk/platform-tools:$PATH" UI009_EXPECTED_API=34 UI009_ADB_SERIAL=emulator-5554 UI009_EVIDENCE_DIR="docs/tasks/UI-009-ui-hardening/evidence" ./scripts/ui009_process_death_check.sh` — PASS. Evidence: `evidence/delete-photo-dialog-process-death-api34.xml`, `evidence/delete-photo-confirm-result-api34.xml`.
- Android 9 / API 28 (`UI009_API28_EXT` AVD, 1080×1920 at 420 dpi): `PATH="/Volumes/Android28SDK/AndroidSdk/platform-tools:$PATH" UI009_EXPECTED_API=28 UI009_ADB_SERIAL=emulator-5556 UI009_EVIDENCE_DIR="docs/tasks/UI-009-ui-hardening/evidence" ./scripts/ui009_process_death_check.sh` — PASS. Evidence: `evidence/delete-photo-dialog-process-death-api28.xml`, `evidence/delete-photo-confirm-result-api28.xml`.
- XML parsing confirms both restored dialogs expose 「刪除確認」、「刪除後無法恢復。」、「取消」 and 「刪除」. After confirmation both APIs retain the survey form, seeded draft and dirty cue, while the photo marker and delete control are gone. The flow uses fake local data and makes no API/backend calls.
- `bash -n scripts/ui009_process_death_check.sh` and `git diff --check -- scripts/ui009_process_death_check.sh` — PASS. AC-006 remains partial / NOT VERIFIED overall pending review of remaining stateful routes.


## Map Icon Accessibility Bounds — 2026-10-08

- Extended `MapShellScreenTest.shellRendersControlsAndExactlyThreeBasemaps` to assert that 「開啟選單」、「關鍵字搜尋」、「通知」 and 「定位目前位置」 each expose a click action and a 48×48dp Compose semantics target. The same flow continues to check the three basemap labels and selected/not-selected state.
- Android 14 / API 34: `Medium_Phone (AVD)`, 1080×2400 at 420 dpi. JBR OpenJDK 25.0.3. `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ANDROID_HOME=/Users/a10362/Library/Android/sdk ANDROID_SDK_ROOT=/Users/a10362/Library/Android/sdk ANDROID_SERIAL=emulator-5554 ./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.feature.mapshell.MapShellScreenTest` — PASS, 7 tests, 0 failures/errors/skipped. Result: `evidence/mapshell-icons-api34.xml`, timestamp `2026-10-08T07:57:42`.
- Android 9 / API 28: `UI009_API28_EXT (AVD)`, 1080×1920 at 420 dpi. Same command with `ANDROID_HOME=/Volumes/Android28SDK/AndroidSdk`, `ANDROID_SDK_ROOT=/Volumes/Android28SDK/AndroidSdk`, `ANDROID_SERIAL=emulator-5556` — PASS, 7 tests, 0 failures/errors/skipped. Result: `evidence/mapshell-icons-api28.xml`, timestamp `2026-10-08T07:56:37`.
- A first API 34 cold-start attempt left Android's active instrumentation record without a live app PID and produced no result XML for about five minutes. After closing and normally restarting the AVD, the same targeted class completed successfully. The initial attempt was an emulator startup/tooling stall; it is not counted as a test pass or product assertion failure.
- `bash -n scripts/ui009_process_death_check.sh` and `git diff --check` — PASS. AC-005 remains partial / NOT VERIFIED pending the remaining route-wide target, semantics and non-color cue audit.


## Camera Screen Accessibility Target — 2026-10-08

- Extended `PhotoCameraScreenTest.grantedCameraExposesFourByThreePreviewAndCloseAction` to locate the shutter by its Traditional Chinese accessibility name 「拍攝現況照片」, assert its semantics bounds are 64×64dp, and then tap it. The test class also covers permission-denied retry, cancel/return, and disabling the shutter while a capture is in progress.
- Android 14 / API 34: `Medium_Phone (AVD)`, 1080×2400 at 420 dpi. JBR OpenJDK 25.0.3. `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ANDROID_HOME=/Users/a10362/Library/Android/sdk ANDROID_SDK_ROOT=/Users/a10362/Library/Android/sdk ANDROID_SERIAL=emulator-5554 ./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.feature.photocamera.PhotoCameraScreenTest` — PASS, 3 tests, 0 failures/errors/skipped. Result: `evidence/camera-screen-target-api34.xml`, timestamp `2026-10-08T08:03:25`.
- Android 9 / API 28: `UI009_API28_EXT (AVD)`, 1080×1920 at 420 dpi. Same command with `ANDROID_HOME=/Volumes/Android28SDK/AndroidSdk`, `ANDROID_SDK_ROOT=/Volumes/Android28SDK/AndroidSdk`, `ANDROID_SERIAL=emulator-5554` — PASS, 3 tests, 0 failures/errors/skipped. Result: `evidence/camera-screen-target-api28.xml`, timestamp `2026-10-08T08:04:47`.
- The first API 34 headless cold-start attempt left instrumentation active with a zero-PID app process. After stopping that AVD and restarting in normal window mode, the same targeted tests passed. The initial attempt has no result XML and is not counted as PASS.
- `git diff --check` — PASS. AC-005 remains partial / NOT VERIFIED pending review of all affected routes and non-color state cues.


## Login Captcha Field Geometry — 2026-10-08

- Corrected the wide login row to bottom-align the 48dp captcha image and refresh control with the 48dp input box, avoiding alignment against the label plus input as one combined column.
- Added `AuthScreenTest.enteringCaptchaDoesNotResizeOrMoveFieldOrImage`, which checks the field and image geometry before and after the captcha changes from empty to four digits, and checks their bottom alignment at the wide reference viewport.
- Android 9 / API 28 (`UI009_API28_EXT`, 1080×1920 at 420 dpi): `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class='com.example.tp_ncolso_android.feature.auth.AuthScreenTest#enteringCaptchaDoesNotResizeOrMoveFieldOrImage'` — PASS, 1 test, 0 failures/errors/skipped.
- Android 9 / API 28, full auth class: `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.feature.auth.AuthScreenTest` — PASS, 19 tests, 0 failures/errors/skipped. Result XML: `app/build/outputs/androidTest-results/connected/debug/TEST-UI009_API28_EXT(AVD) - 9.xml`.
- The first fresh Android 14 / API 34 launch was blocked at 1.9 GiB host free space. After the requester cleared space, 6.4 GiB was available and the emulator started normally.
- Android 14 / API 34, full auth class: `ANDROID_HOME=/Users/a10362/Library/Android/sdk ANDROID_SDK_ROOT=/Users/a10362/Library/Android/sdk ANDROID_SERIAL=emulator-5554 JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.feature.auth.AuthScreenTest` — PASS, 19 tests, 0 failures/errors/skipped. Result XML: `app/build/outputs/androidTest-results/connected/debug/TEST-Medium_Phone(AVD) - 14.xml`.
- The `enteringCaptchaDoesNotResizeOrMoveFieldOrImage` test is included in both 19-test class runs and passed on API 28 and API 34.
- `git diff --check` — PASS. No API/backend behavior is involved.
- `git diff --check` — PASS. No API/backend behavior is involved.


## Authentication Sensitive-Value Restoration — 2026-10-08

- Updated `scripts/ui009_auth_sensitive_process_death_check.sh` after discovering that Android 9 UIAutomator reports empty `text` attributes for Compose `BasicTextField` nodes even when the rendered screen visibly contains text. The script now captures the filled screen and the post-Recents screen; screenshot assertions are reviewed visually instead of relying on the unavailable accessibility text values.
- Android 9 / API 28 (`UI009_API28_EXT`, 1080×1920 at 420 dpi): `PATH="/Volumes/Android28SDK/AndroidSdk/platform-tools:$PATH" UI009_EXPECTED_API=28 UI009_ADB_SERIAL=emulator-5556 UI009_EVIDENCE_DIR="docs/tasks/UI-009-ui-hardening/evidence" ./scripts/ui009_auth_sensitive_process_death_check.sh` — PASS after visual review. Before process death, the fake account and captcha are visible and the password is masked. After `am kill` and Recents restoration, all three fields show empty placeholders. Screenshots: `evidence/auth-sensitive-prefill-api28.png`, `evidence/auth-sensitive-process-death-restored-api28.png`.
- Android 14 / API 34 (`Medium_Phone`, 1080×2400 at 420 dpi): `PATH="/Users/a10362/Library/Android/sdk/platform-tools:$PATH" UI009_EXPECTED_API=34 UI009_ADB_SERIAL=emulator-5554 UI009_EVIDENCE_DIR="docs/tasks/UI-009-ui-hardening/evidence" ./scripts/ui009_auth_sensitive_process_death_check.sh` — PASS after visual review. The before/after screenshots show the same input/reset behavior: `evidence/auth-sensitive-prefill-api34.png`, `evidence/auth-sensitive-process-death-restored-api34.png`.
- The earlier tree-based API 28 attempts failed before killing the process and are recorded as test-harness/environment failures under UI009-013; they are not product assertion failures. After the requester cleared disk space, API 34 test and host flow both ran successfully.
- No real credentials or API/backend behavior were used.


## Shared Action Touch Targets — 2026-10-08

- Added Compose bounds checks for the shared icon button, primary button, and secondary button. The first API 28 run exposed `AppIconButton` at 40×48dp. Updated its minimum width and height to use the shared 48dp token; issue UI009-014 records the discovered defect and resolution.
- Android 9 / API 28 (`UI009_API28_EXT`, API 28): `ANDROID_HOME=/Volumes/Android28SDK/AndroidSdk ANDROID_SDK_ROOT=/Volumes/Android28SDK/AndroidSdk ANDROID_SERIAL=emulator-5556 JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.ui.foundation.FoundationComponentTest` — PASS, 10 tests, 0 failures/errors/skipped.
- Android 14 / API 34 (`Medium_Phone`, API 34): `ANDROID_HOME=/Users/a10362/Library/Android/sdk ANDROID_SDK_ROOT=/Users/a10362/Library/Android/sdk ANDROID_SERIAL=emulator-5554 JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.ui.foundation.FoundationComponentTest` — PASS, 10 tests, 0 failures/errors/skipped.
- The route source audit also confirmed map icon names, auth refresh/back names, survey photo actions, and camera shutter/cancel names are Traditional Chinese. Existing map and camera bounds tests passed on both APIs. AC-005 remains partial pending the remaining non-color state-cue review.


## Map Selection Non-Color Cue — 2026-10-08

- Source review found that basemap selection was indicated visually only through the blue background and white text. Added a visible check mark to the selected option while retaining the existing selected radio semantics; issue UI009-015 records the finding and fix.
- Android 9 / API 28 (`UI009_API28_EXT`, API 28): `ANDROID_HOME=/Volumes/Android28SDK/AndroidSdk ANDROID_SDK_ROOT=/Volumes/Android28SDK/AndroidSdk ANDROID_SERIAL=emulator-5556 JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.feature.mapshell.MapShellScreenTest` — PASS, 7 tests, 0 failures/errors/skipped.
- Android 14 / API 34 (`Medium_Phone`, API 34): `ANDROID_HOME=/Users/a10362/Library/Android/sdk ANDROID_SDK_ROOT=/Users/a10362/Library/Android/sdk ANDROID_SERIAL=emulator-5554 JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.feature.mapshell.MapShellScreenTest` — PASS, 7 tests, 0 failures/errors/skipped.
- The test confirms the marker remains visible when selection changes, while semantics report the correct selected option. Both API runs include the existing 48×48dp icon target checks.


## Final Full UI Regression — 2026-10-08

- Re-ran the complete connected UI instrumentation suite against the final application/test source fingerprint above on both Android 9 / API 28 and Android 14 / API 34. Each run passed 62 tests, with 0 failures, 0 errors and 0 skipped tests.
- API 34 command: `ANDROID_HOME=/Users/a10362/Library/Android/sdk ANDROID_SDK_ROOT=/Users/a10362/Library/Android/sdk ANDROID_SERIAL=emulator-5554 JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:connectedDebugAndroidTest` — PASS. Raw result: `evidence/ui-suite-api34.xml`.
- API 28 command: `ANDROID_HOME=/Volumes/Android28SDK/AndroidSdk ANDROID_SDK_ROOT=/Volumes/Android28SDK/AndroidSdk ANDROID_SERIAL=emulator-5556 JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:connectedDebugAndroidTest` — PASS. Raw result: `evidence/ui-suite-api28.xml`.
- `git diff --check` and `bash -n scripts/*.sh` — PASS after the test runs. These are local Developer Validation results; hosted CI, commit, code review, and Verification remain outstanding. Existing AC-level limitations above still apply, so this does not mark UI-009 Development Complete.


## Final Emulator Viewport and Text-Scale Matrix — 2026-10-08

- Re-ran the complete connected UI instrumentation suite against the tested fingerprint at every planned width and font scale on Android 9 / API 28 and Android 14 / API 34. Each of the 12 combinations passed all 62 tests (0 failures, 0 errors, 0 skipped): 320dp, 360dp, and approximately 402×874dp, each at font scale 1.0 and 1.3.
- Raw XML files: `evidence/ui-suite-api28-320dp-font100.xml`, `evidence/ui-suite-api28-320dp-font130.xml`, `evidence/ui-suite-api28-360dp-font100.xml`, `evidence/ui-suite-api28-360dp-font130.xml`, `evidence/ui-suite-api28-reference402-font100.xml`, `evidence/ui-suite-api28-reference402-font130.xml`; matching API 34 files use the `ui-suite-api34-` prefix. Each XML was parsed and its test/failure/error/skipped counts verified.
- `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:testDebugUnitTest` — PASS on the current working tree (`BUILD SUCCESSFUL`; 24 actionable tasks, 3 executed).
- Physical overrides used 420 dpi: API 28 `840×1920` (320dp), `945×2100` (360×800dp), and `1055×2294` (about 402×874dp); API 34 used matching widths and its corresponding height. Both emulators were restored and verified at physical resolution with system font scale 1.0.
- AC-001–AC-007 have local Developer Validation evidence recorded above, including the API 28/34 viewport, text-scale, IME, and process-state flows. This completes the emulator Developer Validation scope. Commit, code review, and formal Verification remain subsequent workflow gates; no hosted CI PASS or release authorization is claimed.

## Post-commit Code Review — 2026-10-09

- Reviewed revision: `221a160e0a2140d99c81266a482f9215a29b58cc` on branch `developer`.
- Review scope: committed UI-009 production changes, lifecycle/test hosts, UI tests, saved-state unit tests, process-death scripts, release manifest evidence, and the UI-009 task diff.
- Checks: changes follow the approved cross-screen UI hardening plan; saved state contains only the approved local UI values and photo references; login password/captcha are not added to saved state; no API, DTO, database, durable form storage, or permission change is introduced; test Activities remain in the debug source set and are absent from the merged release manifest.
- Findings: none. `git show --check` and the staged `git diff --cached --check` passed before commit. This review was performed by Codex in the task session; it is not an additional human review.
- Review result: approved for task-scoped Verification.
