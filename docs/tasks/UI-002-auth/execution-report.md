# UI-002 Implementation Execution Report

## Latest debug-fix implementation and validation — 2026-09-24

- Reviewed implementation revision: `f83eaa0` (`fix(UI-002): align auth viewport anchors`) on branch `UI-002feat`, based on `534bcb2`.
- Production changes: Login content origin moved to the approved anchor, the skyline baseline now reaches the approved clipped edge without the bottom white block, Login button spacing was tightened, Register top origin was aligned, and the Register action row was moved into the 402×874 viewport.
- Local validation: `testDebugUnitTest`, `assembleDebug`, and `assembleDebugAndroidTest` PASS; 13/13 unit tests passed. Isolated `Medium_Phone(AVD) - 14` on API 34 / `emulator-5558` ran 25/25 connected tests PASS at `2026-09-24T08:07:26Z`; XML is `connected-tests-emulator-5558-2026-09-24T08-07-26.xml`.
- Five-state evidence: `numeric-diff-f83eaa0.json` contains emulator-authoritative Login empty/filled/error and Register empty/filled captures. Metrics are Login `10.5298 / 10.7950 / 22.9243` MAE and Register `5.8778 / 6.3757` MAE; direct review confirms skyline baseline and Register action-row visibility. AC-UI002-009/010 are PASS at local emulator visual scope.
- Responsive evidence from the prior run remains observational; narrow/font-scale/IME captures must be refreshed on the isolated emulator after `f83eaa0` before AC-UI002-007 can advance. Physical-device testing remains out of scope.
- Current formal verification result: `NOT VERIFIED` for AC-UI002-006/007/008/011 and hosted CI; no `PARTIAL` result is used as the current result.

## Revision

- Task: `UI-002-auth`
- Branch: `UI-002feat`
- Current implementation commits: `7dcde3f`, `e0f1d3a`, `7afc677`, `955ab7f`, `1d986fa`, `29464a1`, `d76cc43`, `5581b96`, `20a2342`, `73434c9`, `0894cd0`, `e9bac82`, `afcd107`, `c98291b`, `088e54d`, `a46b9f2`, `7bfd75c`, `52d0524`, `df5339d`, `08f9cce`, `c0b9ed0`
- Date: 2026-09-08 (Asia/Taipei)
- Runtime: Android Studio JBR, OpenJDK 25.0.3

## Latest Implementation Closure

- Revision: `0f7cbe8` on `UI-002feat` (implementation `f19c53f`).
- Register vendor control now uses an external label, `請選擇` placeholder, 48dp control geometry, and vendor error supporting text/semantics.
- Shared 48dp text/password controls use Material 3 `OutlinedTextFieldDefaults.DecorationBox` with explicit horizontal padding, preventing filled-value clipping while retaining existing tokens and accessibility behavior.
- `testDebugUnitTest` and `connectedDebugAndroidTest` both PASS on `Medium_Phone` API 34 in the final run.
- Developer validation is complete. Verification remains `PARTIAL` because current-revision Register root screenshot/numeric diff evidence and the existing visual acceptance gaps are not all closed.
- Subsequent Figma-alignment revisions through `322ba84` also cover the Register dropdown asset, radio indicator/row geometry, white surface and text tokens, work-type label token, and the root-level action-row spacing contract. Each revision passed the same unit and API 34 emulator validation.

## Verification-Failure Debug Fix — 2026-09-23

## Visual composition fix — 2026-09-24

- Revision: `db7dfbe782d3537b5f618af2735964913ce580b7` on `UI-002feat`.
- Restored the composite-compatible 402×239 skyline asset (`706d8030…`) and bottom-anchored it without the mismatched 736×246 Figma-only silhouette.
- Replaced Login global centering/spacing with explicit approved vertical anchors while retaining scrolling for constrained heights.
- Added a horizontal Registration work-type arrangement while preserving 48dp selection targets.
- Direct normalized numeric evidence: Login empty `16.9289 / 51.5348 / 0.128929`, filled `17.5848 / 52.6788 / 0.132727`, auth-error `26.9049 / 65.9684 / 0.189490`; Register empty `10.4778 / 40.7509 / 0.092564`, filled `11.5888 / 43.3090 / 0.096830` (MAE / RMSE / over-threshold ratio).
- All five current-revision state captures are now present. Formal Verification is `FAIL` because the captures still visibly differ from the approved composite; hosted CI remains an additional `NOT VERIFIED` limitation.

- Scope: `IMP-AUTH-013` and `IMP-AUTH-014` only; no requirement, visual authority, API, persistence, navigation, or drawer contract changes.
- Login logo is now decorative (`contentDescription = null`) and the brand row declares one heading semantics node.
- Captcha reload retains the approved `32dp` row allocation while exposing the exact description `重新產生驗證碼` through a required `48dp × 48dp` interaction target.
- Registration illustration is decorative (`contentDescription = null`), and Registration Back now consumes the approved `register-back-chevron.svg` geometry through `ic_register_back_chevron.xml`, rotated 180° toward the left inside the existing `48dp` target; the custom Canvas drawing was removed.
- Added Compose assertions for the decorative logo/illustration, reload description/target, and Register Back target/description.
- The first connected attempt exposed a test/layout constraint mismatch: a nested `size(48.dp)` was constrained to the outer `32dp` allocation, and merged TextButton semantics reported the 48dp Back target. The implementation was corrected with `requiredSize(48.dp)` and target-level assertions.

### Debug-fix validation

| Check | Result | Evidence |
|---|---|---|
| `:app:testDebugUnitTest :app:lintDebug :app:assembleDebug :app:assembleRelease :app:assembleDebugAndroidTest` | PASS | `BUILD SUCCESSFUL` with Android Studio JBR OpenJDK 25.0.3. |
| `:app:connectedDebugAndroidTest` | PASS | 24/24 tests, 0 failures/errors/skips on `Medium_Phone (AVD) - 14`, API 34, serial `emulator-5554`; rerun after the constraint fix. |
| Focused semantics/asset assertions | PASS | Login logo has no `品牌標誌` node; reload exposes `重新產生驗證碼` at 48dp × 48dp while the row tag remains 32dp × 48dp; Register Back target remains 48dp and uses the approved drawable. |

The preceding accessibility/asset fix is committed as `842e290`; the current visual-composition revision is evaluated separately below. Hosted CI remains unavailable and physical-device testing remains out of scope.

## Current verification handoff — 2026-09-24

- Reviewed implementation revision: `db7dfbe782d3537b5f618af2735964913ce580b7`.
- Five-state normalized evidence is complete in `figma-export-2026-09-24-db7dfbe/`; local unit and connected validation pass at 13/13 and 24/24.
- Formal Verification result: `FAIL` for `AC-UI002-009` and `AC-UI002-010` because the current captures still visibly differ from the approved composite. Route: `debug`.
- Hosted CI, complete current responsive/state matrix, and cross-task logout integration remain unverified.

## Implemented Scope

- Source-neutral auth contract, immutable Login/Register state, events and one-shot effects.
- `AuthViewModel` with local validation, source injection, single-submit guard, sensitive-state clearing and logout reset.
- Stateless Login/Register Compose screens and `AuthHost`/`AuthRoute` routing.
- Debug-only deterministic auth/captcha source and centralized `DebugAuthSessionCoordinator`.
- Debug Preview matrix for Login empty/filled/error/submitting and Register empty/filled/validation-error/submitting.
- JVM validation/ViewModel/coordinator tests and Compose instrumentation tests.

## Executed Checks

| Check | Result | Evidence |
|---|---|---|
| `testDebugUnitTest` | PASS | Validation, ViewModel state/effect and coordinator tests passed. |
| `assembleDebug` | PASS | Debug APK assembled with auth flow. |
| `assembleRelease` | PASS | Release APK assembled without debug auth source wiring. |
| `assembleDebugAndroidTest` | PASS | Auth Compose test APK compiled. |
| `connectedDebugAndroidTest` | PASS | Full local run completed on `Medium_Phone` API 34 after the Login action-row correction and content-capture coverage; 34/34 connected tests pass. |
| Compose content capture coverage | PASS | Login empty, filled, and auth-error tests now write the semantics-root PNG to app cache and attempt a `run-as` export path, keeping the comparison source separate from system-chrome screenshots. |
| Figma action-row alignment | PASS | Login action row now preserves the complete semantic text contract while rendering black `沒有帳號?` and blue `註冊` with the Figma 16sp/Medium composition. |
| Figma input-field tokens | PASS | Runtime Login frame confirms white field containers, `#DCDFE6`-equivalent unfocused borders, primary focused borders, and `#A8ABB2` placeholder color are explicitly mapped in the shared field components. |
| Figma skyline crop alignment | PASS | Runtime crop was tuned from `29dp` to `-4dp` against the 402×874 Figma MCP export; full unit/connected regression remains green. |
| Figma form/captcha geometry | PASS | Shared fields now use the Figma 8dp label-control gap; captcha callback receives an explicit 139×48dp visual bound and the row is 48dp high. |
| Figma skyline vertical crop | PASS | Skyline y offset was tuned from `-7dp` to `7dp` against the direct frame export; full unit/connected regression remains green. |
| Figma header composition | PASS | Header now matches the Figma `justify-center` + shrink-to-content structure; the centered natural-width revision has the lowest measured Login empty diff. |
| Figma captcha bounds contract | PASS | `AuthScreenTest.loginCaptchaVisualUsesFigmaBounds` asserts captcha image 139×48dp and reload slot 32×48dp on the emulator. |
| Auth-error color separation | PASS | Login credential borders retain `#C8320A`; the global request error now uses the approved `#E00000` `errorText` token. |
| Current auth-error runtime capture | PASS | API 34 emulator frame retains the wrong account/password/captcha values, shows all three error borders and the global error message after the latest token/layout revision. |
| Release isolation scan | PASS | No debug auth source, direct-login fixture, coordinator, or fixture credential symbols found in `app/src/main` or `app/src/release`. |

## Known Gaps

- Figma asset exports and SHA-256 evidence are now recorded in `docs/assets/app-ui-assets.md`; the debug captcha fixture is wired into Login through an injected visual slot. Composite PNG comparison, SVG runtime treatment, and connected runtime evidence remain incomplete.
- ADB screenshot retrieval outside the instrumentation runner is environment-sensitive; the latest standalone pull returned a blank system-chrome frame, so it is not used as visual evidence.
- UI-003 drawer fixture and INT-001 production token/remote logout integration are outside this task.
- Authoritative CI provider is not configured; local Gradle results are not CI evidence.

## Status

The reviewed implementation revision is committed, but formal Verification is `FAIL` on the current Login/Register visual contract. The task must return to `debug`; it must not be declared PASS or Done until the approved visual correction and the remaining required gates are complete.
