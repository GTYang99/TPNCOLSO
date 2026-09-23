# UI-002 Implementation Execution Report

## Revision

- Task: `UI-002-auth`
- Branch: `UI-002feat`
- Current implementation commits: `7dcde3f`, `e0f1d3a`, `7afc677`, `955ab7f`, `1d986fa`, `29464a1`, `d76cc43`, `5581b96`, `20a2342`, `73434c9`, `0894cd0`
- Date: 2026-09-08 (Asia/Taipei)
- Runtime: Android Studio JBR, OpenJDK 25.0.3

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
| Release isolation scan | PASS | No debug auth source, direct-login fixture, coordinator, or fixture credential symbols found in `app/src/main` or `app/src/release`. |

## Known Gaps

- Figma asset exports and SHA-256 evidence are now recorded in `docs/assets/app-ui-assets.md`; the debug captcha fixture is wired into Login through an injected visual slot. Composite PNG comparison, SVG runtime treatment, and connected runtime evidence remain incomplete.
- ADB screenshot retrieval outside the instrumentation runner is environment-sensitive; the latest standalone pull returned a blank system-chrome frame, so it is not used as visual evidence.
- UI-003 drawer fixture and INT-001 production token/remote logout integration are outside this task.
- Authoritative CI provider is not configured; local Gradle results are not CI evidence.

## Status

Implementation is in progress. The task must not be declared PASS or Done until the visual UIR mappings, refreshed content-only numeric evidence, full logout contract evidence and required handoff artifacts are complete.
