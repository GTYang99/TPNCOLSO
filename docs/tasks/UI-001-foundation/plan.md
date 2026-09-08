# Implementation Plan

Plan revision: 5
Baseline: `KB-UI-001-FOUNDATION-R5`
Planning date: 2026-09-08 (Asia/Taipei)

## Goal
- 建立 UI-first v0.1 的第一個可交付基礎：可重用 Compose foundation contract、debug-only 直接登入、replaceable session／app-root 邊界，以及 release 完全隔離旁路。

## Scope
- 包含 `minSdk 28`、semantic theme、foundation primitives、Android／Material 預設 icon policy、presentation state、Preview/test scaffold、session contract、AppRoot、debug direct-login、release no-bypass entry、asset manifest 與 UI-002/UI-003 handoff。
- 不包含正式登入、API/Token、Navigation graph、Map、parcel/survey/camera 功能或 production persistence。

## Affected Files
- `app/build.gradle.kts`、`gradle/libs.versions.toml`：Android 9+ 與 public `StateFlow` contract 的 direct coroutine dependency。
- `app/src/main/java/com/example/tp_ncolso_android/MainActivity.kt`、`AppRoot.kt`、`session/AppSession.kt`：variant entry、signed-out/signed-in slots 與 session public contract。
- `app/src/main/java/com/example/tp_ncolso_android/ui/foundation/theme/`、`component/`、`state/`、`preview/`：semantic tokens、stateless components、presentation state 與 deterministic previews。
- `app/src/debug/java/com/example/tp_ncolso_android/`：debug-only direct-login UI、`DebugSessionController` 與 in-memory fake session owner。
- `app/src/release/java/com/example/tp_ncolso_android/AppEntry.kt`：release unauthenticated entry with no bypass.
- `app/src/test/`、`app/src/androidTest/`：session、debug direct-login、foundation component semantics 與 variant-boundary tests。
- `docs/assets/app-ui-assets.md`、`docs/tasks/UI-001-foundation/foundation-contract.md`、task lifecycle artifacts：Material/system icon policy、token traceability、consumer contract 與 handoff evidence。

## Implementation Steps
- 1. Confirm the active branch is `feature/UI-001-foundation-compose`, preserve unrelated working-tree changes, and use `KB-UI-001-FOUNDATION-R5` as the only active baseline.
- 2. Set `minSdk` to 28 and directly declare the coroutine dependency needed by the public `StateFlow<AppSessionState>` contract.
- 3. Implement `AppRole`, `AppIdentity`, `AppSessionState` and `AppSessionOwner` without Token, credentials, storage, logging or API DTOs.
- 4. Implement `AppRoot` as a session-driven slot host receiving signed-out and signed-in content callbacks, without feature navigation or fake data ownership.
- 5. Implement `AppTheme` and semantic token accessors from approved visual ownership; keep feature-facing code on semantic values rather than scattered raw visual constants.
- 6. Implement stateless foundation primitives for text, password, select, radio, checkbox row, primary/secondary/icon buttons, status badge, loading, empty, error and read-only content; all icon affordances use Android／Material defaults instead of custom drawable assets.
- 7. Add deterministic fake Preview fixtures for default plus applicable loading, disabled, error and readonly states.
- 8. Implement debug-only `DebugSessionController`, `DebugSessionOwner` and direct-login UI with three roles, single-transition suppression, fake identity display and logout.
- 9. Implement release `AppEntry` as an unauthenticated host with no direct-login strings, fake identity, debug controller or callable bypass.
- 10. Wire `MainActivity` to `AppTheme` and the variant-specific `AppEntry()`.
- 11. Register UI-001 visual inputs and Material/system icon policy in `docs/assets/app-ui-assets.md`, including current composite SHA-256 for token/layout evidence and explicit removal of custom icon crop/scale requirements.
- 12. Add unit and Compose UI tests for session behavior, direct-login flow, component semantics, loading/disabled click suppression, password variants, selection exclusivity, checkbox row target size and release boundary checks.
- 13. Run the smallest available local validation: unit tests, debug build, release build, Android test compilation, release source/artifact inspection and connected tests when a device/runtime exists.
- 14. Record validation results and limitations, create the implementation commit, then route through Code Review, CI and Verification before any Release claim.

## Test Plan
- Unit tests: role mapping, one-time debug session start, duplicate start suppression, clear/logout, no Token/credential fields, process-memory-only owner behavior.
- Compose UI tests: initial debug signed-out state, three role options, direct entry, fake identity, logout return, component semantics and minimum interactive target guarantees.
- Foundation tests: loading/disabled click suppression, Login masked password with eye action, Registration plaintext/no-eye variant, radio zero-or-one/exclusivity, checkbox selectable row and status text-not-color-only behavior.
- Build checks: `assembleDebug`, `assembleRelease`, affected unit tests and Android test APK compilation.
- Release isolation checks: release source/artifact contains no debug direct-login entry point, direct-login strings, `DebugSessionController` usage or fake signed-in identity.
- Visual/asset checks: verify semantic token evidence against current composite SHA-256 `75cb578c58f3098c270290f033539ebf06c35c8b322efb71526fa4e53f3d3e9c`; verify icon affordances by Android／Material dependency/source inspection, semantics, state behavior and 48dp target tests, not custom asset pixel matching.

## Regression Plan
- Re-run foundation/session tests after UI-002 and UI-003 consume the contract.
- Confirm `MainActivity` still launches without crash and remains independent of Login, Map, API, Token, Navigation and persistence choices.
- Verify later consumers do not duplicate foundation primitives or depend on debug-only classes from main/release code.

## Risks
- A debug bypass leaking into release is a blocking security failure and must return to Implementation.
- Persisting fake session data, credentials or Token-like fields would violate the baseline and must be removed before review.
- Java Runtime is currently unavailable in Knowledge Validation evidence; build/test gates remain `NOT VERIFIED` until a Java-enabled environment produces fresh results.
- Remote CI is not yet authoritative; Release remains blocked until CI evidence exists.

## Rollback Plan
- Revert the UI-001 implementation commit to return to the original Compose starter; no migration, persisted data or external state cleanup is required.

## Current Behavior
- The app is a single-module Compose starter and originally displayed only a greeting.
- There is no stable app-root/session boundary for protected UI and no approved reusable foundation contract for UI-002 through UI-010.
- Auth API is not ready, so follow-up UI screens need a debug-only entry that cannot appear in release.

## Expected Behavior
- Debug builds start signed out, show a Traditional Chinese development login entry with three fake roles, allow direct entry once, display fake identity and support logout back to signed out.
- Release builds compile and remain unauthenticated without any direct-login UI, strings, fake session or callable debug controller.
- UI-002 can replace signed-out content and UI-003 can replace signed-in content while reusing `ui.foundation.*`, Android／Material default icon affordances and `session` public contracts.

## Acceptance Criteria Traceability
| AC | Implementation Step | Validation |
|---|---|---|
| AC-UI001-001 | 4, 8, 10 | Debug Compose UI initial-state test for「開發模式」and three role options |
| AC-UI001-002 | 3, 8, 12 | Three-role session tests and direct-login UI test for single fake session creation |
| AC-UI001-003 | 3, 8, 12 | Logout/clear tests and process restart or fresh-owner signed-out check |
| AC-UI001-004 | 2, 9, 13 | `assembleRelease` plus release source/artifact bypass absence inspection |
| AC-UI001-005 | 3, 4 | AppRoot slot contract test and source inspection for DTO/Token/navigation absence |
| AC-UI001-006 | 12, 13 | Unit, Compose UI, Android test compile and available connected-test reports |
| AC-UI001-007 | 2, 13 | Gradle minSdk check plus debug/release build results |
| AC-UI001-008 | 5, 6, 7, 14 | UI-002 handoff review and compile-oriented contract inspection, including no duplicated custom icon assets |
| AC-UI001-009 | 6, 7, 12 | Preview inventory and foundation component semantics/interaction tests |
| AC-UI001-010 | 5, 6, 11, 13 | Asset manifest review, composite SHA/panel evidence for tokens/layout, Material/system icon policy inspection and raw visual value/custom icon asset scan |

## Failure Behavior
- If `startDebugSession` is called while already signed in, it is ignored and does not emit another fake session.
- Debug logout immediately clears the in-memory session and returns to signed out.
- Release auth is not implemented in this task; release remains unauthenticated rather than creating any fallback fake identity.
- Missing Java/device/CI evidence is recorded as `NOT VERIFIED` and blocks later gates rather than lowering acceptance criteria.

## Security and Privacy
- Fake identity is visibly fake and contains only display name plus role; no Token, password, account, credential, analytics, backup, shared preference or DataStore persistence is introduced.
- Debug direct-login code and strings live only in `src/debug`; main/release code cannot reference debug-only implementation.
- Foundation components are stateless and caller-owned for sensitive values, especially password fields.

## Open Questions
- 無產品或實作 blocker。Java Runtime、connected device and CI evidence remain downstream validation requirements before Release.
