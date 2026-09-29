# UI-003 Implementation Execution Report

## Latest verification rerun — 2026-09-28 23:39:39 +0800

- The current `UI-003feat` checkout was revalidated with `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:connectedDebugAndroidTest` on `Medium_Phone (AVD) - 14`, API 34, serial `emulator-5554`.
- The suite reports `33` tests, `0` failures, `0` errors and `0` skipped. Evidence: `connected-tests-emulator-5554-2026-09-28T23-39-39.xml`, SHA-256 `298f3feb01e302b8b3e54231780ba93171428d7e942fefb319fd26e3b4a2abf4`.
- The run includes `DebugDirectLoginTest.directLoginAndLogoutReturnToSignedOut` and `MapShellScreenTest.drawerLogoutEmitsOneCallbackAndExposesAccessibleAction`; the drawer-owned event reaches `DebugAuthSessionCoordinator.logout()`, clears the local session, resets Auth state, and returns to the clean Login destination.
- This closes the UI-002/UI-003 cross-task logout handoff at local emulator scope. Hosted/authoritative CI remains a separate gate.
- Remote heads currently contain `main` and `UI-002feat`; `UI-003feat` is not published, so no hosted CI run exists for the UI-003 revision. The latest hosted run `36442276066` is for UI-002 `48670ff` and failed before usable hosted build/test results.

## Responsive verification evidence — 2026-09-28

- Reference-frame-equivalent run: `1080×2400`, API 34 `emulator-5554`; map shell screenshot `runtime-map-shell-standard-1080x2400.png` (SHA-256 `e0946378f7dac952e020b5953ab97bbca0f6500f17507a79de2162f93aa7f70c`) and hierarchy `ui-map-shell-normal.xml` (SHA-256 `49b5b0aaee707cc737de1f55ad561a61348faa7d6ab7d2c7ff816ca6df82b34b`).
- Narrow-width run: `720×2400`, API 34 `emulator-5554`; screenshot `runtime-map-shell-narrow-720x2400.png` (SHA-256 `486d3ba1cde220b1579e13ce1ef2ee7b24e74b5ca3e89f69fdaf103421d3782d`) and hierarchy `ui-map-shell-narrow.xml` (SHA-256 `d79fe0e7c10994397d4404307d137f1d9857938592327e1574fe5c2920574d92`). Hamburger, search, notification, three basemap choices and location remain present without functional clipping.
- Font-scale run: standard `1080×2400`, system font scale `1.3`; screenshot `runtime-map-shell-fontscale-1.3-1080x2400.png` (SHA-256 `f644e29aeedbd0184ebe273c09e51c47091b762260d2b32c1925bf6a6629e04d`) and hierarchy `ui-map-shell-fontscale-1.3.xml` (SHA-256 `c3a534272e8e8d419e5941d0cf5cd12bf411a401898e5df96f107d7996007006`). The shell controls, basemap labels and location affordance remain available.
- Drawer/font-scale run: screenshot `runtime-map-shell-drawer-fontscale-1.3-1080x2400.png` (SHA-256 `95e6c110320c9f2befa50f63ea3a2f9f1fe2e2f4db4497dae9ed1d9afd15a3d1`) and hierarchy `ui-map-shell-drawer-fontscale-1.3.xml` (SHA-256 `fbb00014dbfb487eac205933ceb2b56662744cb0c147df1728474a9c72d08749`). Identity, `圖台`, `儀錶板`, `登出` and `關閉選單` remain exposed.
- IME applicability: the UI-003 shell exposes no editable control or text-entry destination; the font-scale hierarchy contains no `EditText`/editable node and `dumpsys input_method` reports `mInputShown=false`, `mServedInputConnection=null`. IME-visible validation is therefore `NOT APPLICABLE` to this UI-only shell; auth-screen IME coverage remains owned by UI-002.

## Current implementation revision — 2026-09-28

- Branch: `UI-003feat`
- Implementation revision: `c8120ef` (`feat(UI-003): implement UI-only map shell`)
- Latest test-evidence revision: `37d957e` (`test(UI-003): cover map shell callback ports`)
- Scope: UI-only authenticated map shell; no map SDK, WMTS requests, Token handling, parcel API, DTO or production repository was added.

## Implemented behavior

- Immutable `MapShellUiState`, `MapShellEvent`, `MapShellEffect`, basemap and location-state contracts.
- Default electronic map with exactly three mutually exclusive choices: `電子地圖`, `正射圖`, `地形圖`.
- UI-only overlay fixture and selected target are preserved when the basemap changes.
- Full-screen shell with hamburger, keyword search, notification, map navigation and location controls.
- Drawer with supplied identity, `圖台`, `儀錶板` and accessible `登出` action.
- Exactly-once logout effect at the UI-003 receiving boundary; UI-003 does not clear Auth/session state itself.
- Normal, loading and permission-denied location states with retry action.
- Debug signed-in slot now renders UI-003 and routes logout through the existing UI-002 coordinator.

## Validation

| Check | Result | Evidence |
|---|---|---|
| `:app:testDebugUnitTest` | PASS | 18/18 tests; includes 4 `MapShellViewModelTest` cases |
| `:app:connectedDebugAndroidTest` | PASS | 33/33 tests, 0 failures/errors/skips on `Medium_Phone(AVD) - 14`, API 34, `emulator-5554`, `2026-09-28T15:28:31` |
| UI-003 Compose semantics | PASS | 4 `MapShellScreenTest` cases: shell controls/basemaps, drawer/logout, denied state, callback ports |
| Existing direct-login regression | PASS | Updated `DebugDirectLoginTest` enters map shell, opens drawer, and returns to Login through `登出` |
| `lintDebug` | PASS | `BUILD SUCCESSFUL` |
| `assembleDebug` | PASS | `BUILD SUCCESSFUL` |
| `assembleRelease` | PASS | `BUILD SUCCESSFUL` |
| `assembleDebugAndroidTest` | PASS | `BUILD SUCCESSFUL` |
| Release isolation scan | PASS | Release APK contains no debug direct-login/coordinator symbols or debug-only labels |

## Acceptance scope and limitations

- AC-UI003-001 through AC-UI003-008 are covered at local unit/Compose/emulator scope.
- AC-UI003-005 proves UI-003 exactly-once effect behavior and the debug receiver path; the cross-task UI-002 handoff is now exercised at local emulator scope by the latest connected run.
- AC-UI003-009 is PASS at local emulator scope: reference-frame-equivalent, narrow-width and font-scale 1.3 evidence show no functional clipping or loss of primary callbacks; IME is not applicable because the shell has no editable control.
- Real basemap tiles, WMTS parameters, attribution, caching/offline behavior and parcel overlays remain intentionally out of scope.

## Next action

Implementation-debug re-entry for `CR-UI003-001` through `CR-UI003-005`. Responsive validation and cross-task logout are complete at local emulator scope; hosted/authoritative CI remains pending until the fixes are committed and re-reviewed.

## Code Review Revision 1 Disposition

- Review result: `CHANGES REQUESTED`
- Reviewed revision: `0e0b60995a73252ea6610b513c8c512baba6bd92` (`docs(UI-003): record implementation validation`)
- Review scope: UI-003 production source, debug signed-in wiring, unit/Compose tests, local runtime hierarchy and approved requirement/design contracts.
- Findings: `CR-UI003-001` through `CR-UI003-005` in `issue-log.md`.
- Blocking findings: basemap selected semantics, common location-state semantics, and non-actionable drawer navigation rows.
- Non-blocking quality finding: raw feature colors/Unicode icon path and whitespace defects.
- CI disposition: not evaluated as a pass gate; UI-003 is not published and no authoritative CI run exists for this revision.
- Handoff: route to `debug` for minimum fixes and focused regression checks. Do not enter formal Verification until the findings are resolved and a new committed revision is re-reviewed.

## Debug analysis — 2026-09-29

- Root cause analysis: `root-cause.md`.
- Minimum fix plan: `fix-plan.md`.
- Classification: implementation failure; no requirement, planning or environment cause found.
- Root causes are incomplete accessibility state projection for basemap selection, local replacement of shared location-state components, non-actionable drawer labels caused by missing callback ports, foundation/icon conformance shortcuts, and missing `git diff --check` before review commit.
- Confidence strengthening: source inspection, captured hierarchy evidence, and `git diff --check 48670ff HEAD` independently reproduce all five findings; focused `:app:testDebugUnitTest :app:lintDebug` completed `BUILD SUCCESSFUL` and confirms the findings are review-contract gaps rather than compile/lint failures. Root-cause confidence is 95%+.
- Production code was not modified during debug analysis.
- Exit handoff: `implementation_debug`; authorized re-implementation and a new committed review revision are required before Verification.

## Implementation-debug fix result — 2026-09-29

- Fixed `CR-UI003-001`: basemap options now use `selectable(selected = ..., role = Role.RadioButton)` within a selectable group; the Compose test verifies the initial and changed selected states.
- Fixed `CR-UI003-002`: loading and permission-denied states now use `AppLoadingContent` and `AppErrorContent`; tests verify loading semantics and denied retry behavior.
- Fixed `CR-UI003-003`: `圖台` and `儀錶板` now expose 48dp-or-larger button targets and callback ports (`onMapPlatform`, `onDashboard`) without adding navigation destinations.
- Fixed `CR-UI003-004`: map overlay colors use `AppThemeTokens`; shell and location controls use Material3 `Icon`/`IconButton` with project vector resources instead of Unicode glyphs.
- Fixed `CR-UI003-005`: the affected tracked files pass the working-tree `git diff --check`.
- Focused validation: `:app:testDebugUnitTest`, `:app:lintDebug`, `:app:assembleDebug`, `:app:assembleRelease`, and `:app:assembleDebugAndroidTest` PASS with Android Studio JBR 25.0.3.
- Connected validation: `:app:connectedDebugAndroidTest` PASS, 35/35 tests, 0 failures/errors/skips on `Medium_Phone (AVD) - 14`, API 34, `emulator-5554`, timestamp `2026-09-29T01:38:01`.
- Release isolation scan: PASS; no `DebugDirectLogin`, `DebugAuthSessionCoordinator`, or debug direct-login labels found in `app-release.apk`.
- The first combined connected run exposed a test-harness defect (the screen test used a no-op event lambda); the test was corrected to exercise `MapShellRoute`/`MapShellViewModel`, and the full connected suite was rerun successfully.

## Next action after fix

Create the new committed implementation revision, request Code Review again, and retain Verification as `NOT VERIFIED` until the new review and authoritative CI evidence are available.
