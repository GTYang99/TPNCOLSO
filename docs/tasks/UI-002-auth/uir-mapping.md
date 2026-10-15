# UI-002 UIR mapping — R11

Reviewed source/test revision: `9513ac3402ee6950fa3ed10fafd79a52d6e0ea27`.
The mapping is against the active `login-ui-requirement.md`,
`registration-ui-requirement.md`, and `logout-interface-requirement.md`. A
local PASS is scoped to the evidence named in the row; it is not a task-level
Verification PASS. Any visual row marked `NOT VERIFIED` keeps the related
acceptance criterion open.

## Login

| Requirement | Current implementation and evidence | Result |
|---|---|---|
| UIR-LOGIN-001 | `LoginScreen.kt` owns the gradient, centered auth column, brand/account/password anchors, and skyline. The exact 402dp×874dp capture in `numeric-diff-r11-unchanged.json` compares unchanged upper/skyline regions with MAE `4.9777`; placement is within the requirement's approximately 3dp tolerance, and the artifact is `PASS` for unchanged regions. | PASS (unchanged visual scope) |
| UIR-LOGIN-002 | `LoginScreen.kt` renders the shared logo/title header; `runtime-login-r11-reference-viewport-402x874.png` places the header within the approved anchor tolerance in the exact logical viewport. | PASS (local visual scope) |
| UIR-LOGIN-003 | `LoginScreen.kt` renders account and password fields; the exact logical viewport confirms their order/anchors, while `AuthScreenTest.loginRendersRequiredEntryPoints` and `captureLoginR11ReflowGeometryForEvidence` cover presence and bounds. | PASS (local visual/Compose scope) |
| UIR-LOGIN-004 | CAPTCHA state, provider, fixture, UI and validation are removed from main/debug surfaces. `AuthScreenTest.loginRendersRequiredEntryPoints` asserts no CAPTCHA text/tags; release APK scan has no `Captcha` or `login_captcha` entries. | PASS (local implementation scope) |
| UIR-LOGIN-005 | Empty/auth-error/submitting Compose states are exercised by `AuthScreenTest`; the exact empty-state visual comparison and R11 geometry artifact cover the unchanged and reflowed regions. Filled/error images are not retained because they contain credential-bearing fixture values. | NOT VERIFIED (filled/error visual evidence) |
| UIR-LOGIN-006 | `AuthScreenTest.loginAuthErrorKeepsValuesAndShowsAccessibleGlobalMessage` covers retained account, exact `帳號或密碼錯誤`, and accessible request-error semantics; `AuthViewModelTest.failedLoginRetainsAccountAndRememberMe` covers state retention. | PASS (local behavior/accessibility scope) |
| UIR-LOGIN-007 | `login-r11-reflow-geometry.json` and `AuthScreenTest.loginActionsReflowAfterPasswordAndUseFoundationSpacing` prove order and 16dp spacing for Remember me, registration, and Login. | PASS (local geometry/Compose scope) |
| UIR-LOGIN-008 | `AuthScreenTest.loginSubmittingStateDisablesSubmit` covers duplicate-action suppression; the same R11 geometry artifact records the reflowed button bounds. | PASS (local behavior/geometry scope) |
| UIR-LOGIN-009 | Current composite SHA-256, exact logical runtime capture, reflow artifact, and source/runtime checksums are recorded under `docs/design/evidence/auth/figma-export-2026-10-15-r11/`; the unchanged-region artifact is `PASS` within its explicit scope. | PASS (traceability/local visual scope) |
| UIR-LOGIN-010 | Current API 36 AuthScreenTest covers Login narrow width, font scale, and IME action reachability; the current emulator-only full connected suite is 68/68 PASS. A complete current-R11 runtime visual matrix is still unavailable. | NOT VERIFIED (device matrix) |

## Registration

The R11 change does not alter Registration visual composition. Existing local
evidence remains applicable to the reviewed source/test revision; the latest
Auth connected run includes the Registration assertions below.

| Requirement | Current implementation and evidence | Result |
|---|---|---|
| UIR-REG-001 | `RegisterScreen.kt` scrollable content column and current empty-state Compose capture. | PASS (local scope) |
| UIR-REG-002 | 48dp back target and accessible `返回登入頁` semantics are asserted by `AuthScreenTest`. | PASS (local Compose scope) |
| UIR-REG-003 | Six required fields and labels are asserted by `registerRendersSixFieldsAndUnselectedWorkType`. | PASS (local Compose scope) |
| UIR-REG-004 | Plaintext password and confirmation are asserted by `registerFilledStateUsesPlaintextFieldsWithoutVisibilityAction`. | PASS (local Compose scope) |
| UIR-REG-005 | Vendor options are injected through `RegisterScreen` and covered by the filled/empty tests. | PASS (local scope) |
| UIR-REG-006 | Work-type labels, selectable targets, and required-field error are covered by the current AuthScreenTest suite. | PASS (local Compose scope) |
| UIR-REG-007 | Cancel/Complete controls and the submitting guard are asserted by `registerSubmittingStateDisablesSubmit`; ViewModel tests cover exactly-once valid submission. | PASS (local behavior scope) |
| UIR-REG-008 | Back, Cancel, and successful registration transitions are covered by `AuthViewModelTest` and the screen event test. | PASS (local behavior scope) |
| UIR-REG-009 | Boundary validation and sensitive-state reset are covered by `AuthValidationTest` and `AuthViewModelTest`; no persistence path is introduced. | PASS (local unit scope) |
| UIR-REG-010 | Existing local Registration runtime/asset evidence remains applicable; hosted CI is deferred separately. | PASS (local scope) |
| UIR-REG-011 | Narrow-width labels and action targets are asserted by `registerNarrowWidthKeepsBothWorkTypeLabelsAndTargets`; prior responsive fix evidence is archived. | PASS (local Compose scope) |

## Logout contract

UI-002 owns the destination contract; drawer visuals remain UI-003-owned and
remote Token/API behavior remains INT-001-owned.

| Requirement | Current implementation and evidence | Result |
|---|---|---|
| UIR-LOGOUT-001 | Debug `AppEntry` wires `MapShellCallbacks.onLogoutRequested` to `DebugAuthSessionCoordinator.logout()`. | PASS (local contract scope) |
| UIR-LOGOUT-002 | `DebugDirectLoginTest.directLoginAndLogoutReturnToSignedOut` covers the drawer callback and clean Login entry. | PASS (local integration scope) |
| UIR-LOGOUT-003 | Coordinator logout clears session and resets Auth state; coordinator tests cover transient-state clearing. | PASS (local state scope) |
| UIR-LOGOUT-004 | UI-003 callback and ViewModel duplicate-event tests cover exactly-once logout dispatch. | PASS (local contract scope) |
| UIR-LOGOUT-005 | Remote logout and Token/API behavior are INT-001-owned and not implemented by UI-002. | DELEGATED / NOT VERIFIED |
| UIR-LOGOUT-006 | Drawer visual implementation remains UI-003-owned; UI-002 consumes only the destination contract. | PASS (ownership scope) |

## Overall mapping result

Auth implementation and task-scoped local checks pass, and the current
emulator-only full connected suite is green. UIR-LOGIN-005 and UIR-LOGIN-010
remain `NOT VERIFIED` because filled/error visual artifacts are intentionally not
retained and a complete R11 device visual matrix is unavailable. Formal
Verification and Code Review therefore remain pending.
