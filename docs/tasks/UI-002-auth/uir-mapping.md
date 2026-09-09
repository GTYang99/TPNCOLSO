# UI-002 UIR mapping

Mapping is against `login-ui-requirement.md` and the approved Figma Login frame `2905:2679`. `PASS` means code/test/evidence is present; `PARTIAL` means implementation exists but the required visual or device-matrix evidence is incomplete.

| Requirement | Implementation/evidence | Status |
|---|---|---|
| UIR-LOGIN-001 | `LoginScreen.kt` gradient, centered 320dp column, exact 736×246 skyline; normalized evidence and masked RGB diff recorded in `verification.md`. | PARTIAL |
| UIR-LOGIN-002 | `LoginScreen.kt` 96×65 logo/title row; `login_brand_logo.png` checksum in asset manifest. | PASS |
| UIR-LOGIN-003 | Account/password `AppTextField`/`AppPasswordField`; `AuthScreenTest.loginRendersRequiredEntryPoints`; geometry covered by normalized capture. | PARTIAL |
| UIR-LOGIN-004 | Captcha input/image/reload row and `DebugCaptchaProvider`; connected auth tests pass. | PARTIAL |
| UIR-LOGIN-005 | Empty/filled/error runtime captures exist; normalized composite comparison is complete for Login empty, filled and error. | PARTIAL |
| UIR-LOGIN-006 | `LoginScreen.kt` maps field/request errors to all credential controls and accessible supporting text; current runtime error capture shows retained values and global error. | PARTIAL |
| UIR-LOGIN-007 | Remember-me row and register action exist; `AuthScreenTest.loginRegisterEntryDispatchesEvent`; target-size/device evidence incomplete. | PARTIAL |
| UIR-LOGIN-008 | `AppPrimaryButton` and `submitting` guard; connected tests pass; exact button pixel evidence is included only in composite diff. | PARTIAL |
| UIR-LOGIN-009 | Figma export, exact skyline asset, logo, captcha and SHA-256 records are in asset/evidence files. | PASS |
| UIR-LOGIN-010 | Scroll container is implemented; narrow/insets/IME/font-scale matrix is not executed. | PARTIAL |
| UIR-REG-001 | `RegisterScreen.kt` now uses 24dp padding, 36dp main group spacing and scroll; post-layout-fix empty normalized comparison MAE `15.5036`, RMSE `47.7196`, over-20 ratio `0.340352`. | PARTIAL |
| UIR-REG-002 | Balanced 48dp top-bar slots and 80dp illustration slot implemented; exact back asset and filled-state comparison remain incomplete. | PARTIAL |
| UIR-REG-003 | Six controls and labels implemented; `AuthScreenTest.registerRendersSixFieldsAndUnselectedWorkType`. | PASS |
| UIR-REG-004 | Plaintext fields are implemented; post-layout-fix empty capture is recorded, while filled-state strict comparison remains pending. | PARTIAL |
| UIR-REG-005 | `AppSelectField` receives injected vendor options; selection behavior is covered by ViewModel validation tests. | PARTIAL |
| UIR-REG-006 | Null/required work-type error and radio group implemented; `registerShowsWorkTypeValidationMessage`. | PASS |
| UIR-REG-007 | Cancel/complete buttons and submit guard implemented; full navigation-race evidence pending. | PARTIAL |
| UIR-REG-008 | Back/cancel/success events are implemented in auth coordinator; full state-clearing runtime evidence pending. | PARTIAL |
| UIR-REG-009 | Validation and plaintext boundary unit tests exist; log/SavedState/rememberSaveable audit evidence pending. | PARTIAL |
| UIR-REG-010 | Illustration asset is runtime-consumed and asset traceability exists; strict composite comparison pending. | PARTIAL |
| UIR-REG-011 | Scroll support exists; narrow/insets/IME/font-scale matrix is not executed. | PARTIAL |

## Login numeric diff revision

Reference: Figma MCP export `2905:2679`, normalized to `402×874`.

Runtime: `runtime-login-normalized-exact-skyline-402x874.png`, SHA-256 `8b0f4ee8dce1869c06130ef3bcb5ba6083f4189cab8e051d872173e79c6e7311`.

Mask: `login-content-only-mask.json`; Android system chrome removed during crop, debug-only region `(0,0,175,48)` excluded.

RGB threshold `20`, `1,028,844` compared channel samples: MAE `28.1656`, RMSE `68.1859`, over-threshold ratio `0.566433`. This is the post-exact-skyline revision result; it remains visual `PARTIAL`, not PASS.
