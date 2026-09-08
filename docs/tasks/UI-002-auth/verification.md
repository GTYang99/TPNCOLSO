# UI-002 Verification Report

## Inputs

- Requirement baseline: `KB-UI-002-AUTH-R10`
- Plan Review: revision 16, `APPROVED`
- Branch: `UI-002feat`
- Reviewed implementation head: pending Figma visual integration commit
- Runtime: Android Studio JBR, OpenJDK 25.0.3

## Verification Round 2 — 2026-09-08

- Device: `Medium_Phone (AVD) - 14`, ADB serial `emulator-5554`
- Command: `JAVA_HOME='/Applications/Android Studio.app/Contents/jbr/Contents/Home' ./gradlew connectedDebugAndroidTest`
- Result after fix: `PASS`; 17 tests executed, 0 failed, 0 errors, 0 skipped.
- Passing groups: `FoundationComponentTest` 9/9; `AuthScreenTest` 5/5; `DebugDirectLoginTest` 2/2; app context 1/1.
- Previous four failures were reproduced and fixed: direct-login tests now enter the existing Debug mode first; Login keeps the remember/register action row visible in the viewport and uses a width-safe checkbox layout.
- Evidence: [connected test report](../../app/build/reports/androidTests/connected/debug/index.html) and `app/build/outputs/androidTest-results/connected/debug/TEST-Medium_Phone(AVD) - 14.xml`.

## Acceptance Criteria

| AC | Current result | Evidence / limitation |
|---|---|---|
| AC-UI002-001 | PARTIAL | Login screen contains account, masked password, captcha input/reload, remember-me, submit and registration entry; connected semantics runtime is not verified. |
| AC-UI002-002 | PASS (unit scope) | `AuthValidationTest` and `AuthViewModelTest` cover required fields, captcha format and submit rejection. |
| AC-UI002-003 | PASS (unit scope) | ViewModel tests cover success effect, sensitive clearing and auth-error retention; debug coordinator guards duplicate session start. |
| AC-UI002-004 | PARTIAL | Register route, six fields, Back and Cancel events are implemented and APK tests compile; connected runtime is not verified. |
| AC-UI002-005 | PASS (unit scope) | Pure validation tests cover account, password, confirmation, vendor, work type and Chinese name rules. |
| AC-UI002-006 | PARTIAL | Coordinator clears session then auth state; contract test exists, but UI-003 integration is out of scope and connected runtime is not verified. |
| AC-UI002-007 | NOT VERIFIED | Scroll containers exist, but narrow-width, font-scale and IME behavior has no executed device evidence. |
| AC-UI002-008 | PARTIAL | Debug Preview matrix and Compose test sources exist; connected execution was started but produced no result and was terminated. |
| AC-UI002-009 | NOT VERIFIED | Login UIR mapping and composite pixel/asset comparison are not complete. |
| AC-UI002-010 | PARTIAL | Approved registration illustration is now a runtime VectorDrawable consumed by RegisterScreen; connected rendering and composite pixel comparison remain unverified. |
| AC-UI002-011 | PARTIAL | Logout coordinator/idempotency unit evidence exists; UI-003 drawer contract integration is not executed. |

## Build and Test Evidence

| Check | Result |
|---|---|
| `testDebugUnitTest` | PASS |
| `assembleDebug` | PASS |
| `assembleRelease` | PASS |
| `assembleDebugAndroidTest` | PASS |
| Current full local regression (`testDebugUnitTest assembleDebug assembleRelease assembleDebugAndroidTest`) | PASS; `BUILD SUCCESSFUL` on 2026-09-08 |
| Release isolation scan | PASS |
| `connectedDebugAndroidTest` | PASS; executed on `Medium_Phone (AVD) - 14` / API 34; 17 of 17 tests passed |
| Authoritative CI | NOT VERIFIED; repository has no configured CI workflow/provider |

## Visual Evidence

- Authoritative composite: SHA-256 `75cb578c58f3098c270290f033539ebf06c35c8b322efb71526fa4e53f3d3e9c`.
- Login/Register screens currently implement light surfaces, gradient/scroll behavior and interactive field structure.
- Figma node `2997:11541` was re-read through Figma MCP. The exact exported logo and skyline are now runtime-consumed at the approved 96×65 header and 736×246 bottom placement; runtime asset SHA-256 values are recorded in the asset manifest. Pixel comparison evidence remains pending.

## Overall Result

`NOT VERIFIED`. Implementation is incomplete for the visual and connected-runtime gates; this report must not be interpreted as UI-002 PASS or Done.

## Next Action

The four reproducible instrumented failures are fixed and the emulator suite is green. Capture and compare the emulator Login empty/filled/error renders against the approved composite to close the pixel-comparison gate; CI remains intentionally deferred by requester decision. Physical-device testing is out of scope.
