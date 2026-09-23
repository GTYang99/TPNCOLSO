# UI-002 Implementation Execution Report

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

Implementation is in progress. The task must not be declared PASS or Done until the visual UIR mappings, refreshed content-only numeric evidence, full logout contract evidence and required handoff artifacts are complete.
