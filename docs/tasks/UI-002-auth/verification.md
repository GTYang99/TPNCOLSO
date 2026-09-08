# UI-002 Verification Report

## Inputs

- Requirement baseline: `KB-UI-002-AUTH-R10`
- Plan Review: revision 16, `APPROVED`
- Branch: `UI-002feat`
- Reviewed implementation head: `bba6e33`
- Runtime: Android Studio JBR, OpenJDK 25.0.3

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
| AC-UI002-010 | NOT VERIFIED | Registration UIR mapping and composite pixel/asset comparison are not complete. |
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
| `connectedDebugAndroidTest` | NOT VERIFIED; targeted `AuthScreenTest` execution reports `No connected devices` |
| Authoritative CI | NOT VERIFIED; repository has no configured CI workflow/provider |

## Visual Evidence

- Authoritative composite: SHA-256 `75cb578c58f3098c270290f033539ebf06c35c8b322efb71526fa4e53f3d3e9c`.
- Login/Register screens currently implement light surfaces, gradient/scroll behavior and interactive field structure.
- Exact logo, skyline, captcha fixture, registration illustration, geometry comparison and crop-based evidence remain pending.

## Overall Result

`NOT VERIFIED`. Implementation is incomplete for the visual and connected-runtime gates; this report must not be interpreted as UI-002 PASS or Done.

## Next Action

Complete feature-owned visual assets and UIR-LOGIN/UIR-REG implementation mappings, then rerun connected Compose tests and update this report with exact device/runtime evidence.
