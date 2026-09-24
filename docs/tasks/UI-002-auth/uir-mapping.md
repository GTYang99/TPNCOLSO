# UI-002 UIR mapping

Mapping is against `login-ui-requirement.md` and the approved Figma Login frame `2905:2679`. `PASS` means code/test/evidence is present; `PARTIAL` means implementation exists but the required visual or device-matrix evidence is incomplete.

| Requirement | Implementation/evidence | Status |
|---|---|---|
| UIR-LOGIN-001 | `LoginScreen.kt` gradient, responsive auth column, composite-compatible skyline; five-state normalized evidence in `figma-export-2026-09-24-534bcb2`. | PASS |
| UIR-LOGIN-002 | `LoginScreen.kt` 96×65 logo/title row; `login_brand_logo.png` checksum in asset manifest. | PASS |
| UIR-LOGIN-003 | Account/password `AppTextField`/`AppPasswordField`; `AuthScreenTest.loginRendersRequiredEntryPoints`; geometry covered by current normalized capture. | PASS |
| UIR-LOGIN-004 | Captcha input/image/reload row and `DebugCaptchaProvider`; connected auth tests and current normalized capture pass. | PASS |
| UIR-LOGIN-005 | Empty/filled/error runtime captures and normalized composite comparisons are complete in `figma-export-2026-09-24-534bcb2/numeric-diff-534bcb2.json`. | PASS |
| UIR-LOGIN-006 | `LoginScreen.kt` maps field/request errors to all credential controls and accessible supporting text; current runtime error capture shows retained values and global error. | PASS |
| UIR-LOGIN-007 | Remember-me row and register action exist; `AuthScreenTest.loginRegisterEntryDispatchesEvent` and current five-state capture pass. | PASS |
| UIR-LOGIN-008 | `AppPrimaryButton` and `submitting` guard; connected tests pass and exact button geometry is included in current composite diff. | PASS |
| UIR-LOGIN-009 | Supporting Figma export, composite-compatible skyline asset, logo, captcha and SHA-256 records are in asset/evidence files; Figma-only skyline candidate was rejected by silhouette comparison. | PASS |
| UIR-LOGIN-010 | Scroll container is implemented; the full current-revision narrow/IME/font-scale matrix is not re-executed. | NOT VERIFIED |
| UIR-REG-001 | `RegisterScreen.kt` uses the approved content origin/rhythm and scroll; current empty/filled normalized comparisons are in `figma-export-2026-09-24-534bcb2/numeric-diff-534bcb2.json`. | PASS |
| UIR-REG-002 | Balanced 48dp top-bar slots, 80dp illustration slot, and approved left-facing back arrow inside the 48dp control are covered by current empty/filled evidence. | PASS |
| UIR-REG-003 | Six controls and labels implemented; `AuthScreenTest.registerRendersSixFieldsAndUnselectedWorkType`. | PASS |
| UIR-REG-004 | Plaintext fields are implemented; current empty/filled captures and strict numeric comparison are recorded. | PASS |
| UIR-REG-005 | `AppSelectField` receives injected vendor options; selection behavior and current filled evidence pass. | PASS |
| UIR-REG-006 | Null/required work-type error and radio group implemented; `registerShowsWorkTypeValidationMessage`. | PASS |
| UIR-REG-007 | Cancel/complete buttons and submit guard implemented; connected auth tests pass. | PASS |
| UIR-REG-008 | Back/cancel/success events are implemented in auth coordinator; local connected contract evidence passes. | PASS |
| UIR-REG-009 | Validation and plaintext boundary unit tests exist; no persistence/logging path is used by the feature. | PASS |
| UIR-REG-010 | Illustration asset is runtime-consumed and asset traceability plus current composite comparison are recorded in `figma-export-2026-09-24-534bcb2/numeric-diff-534bcb2.json`. | PASS |
| UIR-REG-011 | Scroll support exists and current filled evidence confirms fields/actions are usable without horizontal clipping; full current responsive matrix remains unexecuted. | NOT VERIFIED |

## Current revision comparison addendum — 2026-09-23

The five current-revision state comparisons are now complete against the approved composite panels using the recorded crop coordinates, system-inset normalization and RGB threshold in `docs/design/evidence/auth/figma-export-2026-09-23/composite-panel-comparison-842e290.json`. The evidence closes the numeric-comparison collection gap but does not pass the visual contract: Login remains geometrically different from the reference, and Registration's work-type options are vertically arranged in runtime while the approved panel is horizontal. Formal Verification therefore marks `AC-UI002-009` and `AC-UI002-010` as `FAIL` and routes the task to Debug.

## Current revision comparison addendum — 2026-09-24

The reviewed implementation revision is `db7dfbe782d3537b5f618af2735964913ce580b7`. The five current normalized state captures and their reproducible metrics are recorded in `docs/design/evidence/auth/figma-export-2026-09-24-db7dfbe/numeric-diff-db7dfbe.json`. Login empty/filled/auth-error measure MAE `16.9289`/`17.5848`/`26.9049`; Register empty/filled measure MAE `10.4778`/`11.5888` (RGB threshold `20`). The values improve on the prior round, but direct visual review still finds residual Login header/form/action/skyline placement differences and residual Registration top-content/form/action vertical placement differences. `AC-UI002-009` and `AC-UI002-010` therefore remain `FAIL` for this revision and route to `debug`; the numeric comparison is not a PASS threshold.

## Login numeric diff revision

Reference: Figma MCP export `2905:2679`, normalized to `402×874`.

Runtime: `runtime-login-normalized-exact-skyline-402x874.png`, SHA-256 `8b0f4ee8dce1869c06130ef3bcb5ba6083f4189cab8e051d872173e79c6e7311`.

Mask: `login-content-only-mask.json`; Android system chrome removed during crop, debug-only region `(0,0,175,48)` excluded.

RGB threshold `20`, `1,028,844` compared channel samples: MAE `28.1656`, RMSE `68.1859`, over-threshold ratio `0.566433`. This is the post-exact-skyline revision result; it remains visual `PARTIAL`, not PASS.
