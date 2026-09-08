# UI-002 Verification Report

## Inputs

- Requirement baseline: `KB-UI-002-AUTH-R10`
- Plan Review: revision 16, `APPROVED`
- Branch: `UI-002feat`
- Reviewed implementation head: `aa4b364` (`chore(ui-002): record figma visual integration`)
- Runtime: Android Studio JBR, OpenJDK 25.0.3

## Verification Round 3 — 2026-09-08

- Device: `Medium_Phone (AVD) - 14`, ADB serial `emulator-5554`
- Command: `JAVA_HOME='/Applications/Android Studio.app/Contents/jbr/Contents/Home' ./gradlew connectedDebugAndroidTest`
- Result after fix: `PASS`; 17 tests executed, 0 failed, 0 errors, 0 skipped.
- Passing groups: `FoundationComponentTest` 9/9; `AuthScreenTest` 5/5; `DebugDirectLoginTest` 2/2; app context 1/1.
- Previous four failures were reproduced and fixed: direct-login tests now enter the existing Debug mode first; Login keeps the remember/register action row visible in the viewport and uses a width-safe checkbox layout.
- Evidence: [connected test report](../../app/build/reports/androidTests/connected/debug/index.html) and `app/build/outputs/androidTest-results/connected/debug/TEST-Medium_Phone(AVD) - 14.xml`.

## Acceptance Criteria

| AC | Current result | Evidence / limitation |
|---|---|---|
| AC-UI002-001 | PASS (connected scope) | Login entry points and semantics pass `AuthScreenTest` on API 34; visual comparison remains pending. |
| AC-UI002-002 | PASS (unit scope) | `AuthValidationTest` and `AuthViewModelTest` cover required fields, captcha format and submit rejection. |
| AC-UI002-003 | PASS (unit scope) | ViewModel tests cover success effect, sensitive clearing and auth-error retention; debug coordinator guards duplicate session start. |
| AC-UI002-004 | PASS (connected scope) | Register route, six fields, Back and Cancel events pass `AuthScreenTest` on API 34. |
| AC-UI002-005 | PASS (unit scope) | Pure validation tests cover account, password, confirmation, vendor, work type and Chinese name rules. |
| AC-UI002-006 | PARTIAL | Debug coordinator and direct-login/logout tests pass on API 34; UI-003 drawer integration remains out of scope. |
| AC-UI002-007 | NOT VERIFIED | Scroll containers exist, but narrow-width, font-scale and IME behavior has no executed device evidence. |
| AC-UI002-008 | PASS (executed test scope) | Preview matrix and connected auth screen tests are present; full visual state matrix evidence remains pending. |
| AC-UI002-009 | FAIL | Figma Login empty comparison shows material geometry/composition mismatch; full UIR mapping is also incomplete. Route to Debug. |
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

## Visual Comparison — Figma vs Emulator

- Reference: Figma file `HRbRsw6HoNBUCtaieX8xUM`, section `2905:2680`, Login empty frame `2905:2679`, natural size `402×874`.
- Runtime capture: API 34 `Medium_Phone (AVD)`, serial `emulator-5554`, screenshot `1080×2400`, captured after installing the current debug APK from `2eab95a`; evidence file `docs/design/evidence/auth/figma-export-2026-09-08/runtime-login-after-composition.png`, SHA-256 `6f7309921aa90159bd863c6869694464d151e3ba8cb61c34ace9258813b8cb86`.
- Method: cropped emulator capture from system chrome and resized to the Figma reference frame `402×874`; normalized evidence: `docs/design/evidence/auth/figma-export-2026-09-08/runtime-login-normalized-402x874.png`, SHA-256 `38d9c9f0914b53fc88d3d9c6055a3101b9c28071ddba5db7e0d265897a0d6e3b`.
- Observed differences: Android system chrome is removed, but debug-only `開發模式` remains inside the app canvas at the top-left and is explicitly excluded from product comparison. The logo, centered 320-wide form, action row, button and skyline are now visibly aligned to the Figma composition; the skyline crop still requires a content-only mask for strict pixel scoring.
- Result: `PARTIAL`. Normalized crop evidence is now available. A numeric strict pixel diff is not claimed because the debug-only affordance is inside the captured app canvas and no authoritative content-only mask has been applied.

## Overall Result

`NOT VERIFIED` pending Debug re-entry and complete visual evidence; the Login empty visual comparison has a confirmed nonconformance and must not be interpreted as PASS or Done.

## Next Action

Route the confirmed Login empty visual mismatch to Debug, correct the approved composition without changing requirements, then capture normalized Login/Register emulator renders and rerun the visual comparison. CI remains intentionally deferred by requester decision. Physical-device testing is out of scope.
