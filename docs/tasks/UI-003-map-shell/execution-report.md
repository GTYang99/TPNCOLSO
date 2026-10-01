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

- Implementation fix revision: `51cb024` (`fix(UI-003): resolve map shell review findings`).
- Fixed `CR-UI003-001`: basemap options now use `selectable(selected = ..., role = Role.RadioButton)` within a selectable group; the Compose test verifies the initial and changed selected states.
- Fixed `CR-UI003-002`: loading and permission-denied states now use `AppLoadingContent` and `AppErrorContent`; tests verify loading semantics and denied retry behavior.
- Fixed `CR-UI003-003`: `圖台` and `儀錶板` now expose 48dp-or-larger button targets and callback ports (`onMapPlatform`, `onDashboard`) without adding navigation destinations.
- Fixed `CR-UI003-004`: map overlay colors use `AppThemeTokens`; shell and location controls use Material3 `Icon`/`IconButton` with project vector resources instead of Unicode glyphs.
- Fixed `CR-UI003-005`: the affected tracked files pass the working-tree `git diff --check`.
- Focused validation: `:app:testDebugUnitTest`, `:app:lintDebug`, `:app:assembleDebug`, `:app:assembleRelease`, and `:app:assembleDebugAndroidTest` PASS with Android Studio JBR 25.0.3.
- Connected validation: `:app:connectedDebugAndroidTest` PASS, 35/35 tests, 0 failures/errors/skips on `Medium_Phone (AVD) - 14`, API 34, `emulator-5554`, timestamp `2026-09-29T01:38:01`.
- Release isolation scan: PASS; no `DebugDirectLogin`, `DebugAuthSessionCoordinator`, or debug direct-login labels found in `app-release.apk`.
- The first combined connected run exposed a test-harness defect (the screen test used a no-op event lambda); the test was corrected to exercise `MapShellRoute`/`MapShellViewModel`, and the full connected suite was rerun successfully.

## Next action after fix (before Code Review Revision 2)

Request Code Review for committed revision `51cb024`, then retain Verification as `NOT VERIFIED` until the new review and authoritative CI evidence are available.

## Code Review Revision 2 — 2026-09-30 (historical)

- Reviewed revision: `51cb024fe907b2047750162ed857900fe126abbd` (`fix(UI-003): resolve map shell review findings`), branch `UI-003feat`.
- Review base: `0e0b60995a73252ea6610b513c8c512baba6bd92`.
- Result: `CHANGES_REQUESTED`.
- `CR-UI003-001` through `CR-UI003-005` are resolved: basemap selected semantics, common location-state components, actionable drawer rows, theme/icon usage, and whitespace checks were confirmed in the committed source and tests.
- New findings `CR-UI003-006` and `CR-UI003-007` are recorded in `issue-log.md`: the drawer fills the viewport and misses its approved composition; the denied-state retry does not call a location callback and can remain loading indefinitely.
- Review was static; no tests were run during this review. The developer-reported `:app:testDebugUnitTest`, lint/build, and 35-test connected run are existing local evidence, not reruns for this review.
- The available responsive screenshots and hierarchy dumps predate `51cb024`; AC-UI003-009 remains `NOT VERIFIED` on the reviewed revision until fresh evidence is captured after the fixes.
- Hosted/authoritative CI remains unavailable. No Verification or Release approval is implied by this review.
- Next action: `debug` for `CR-UI003-006` and `CR-UI003-007`, followed by a new committed review revision.

## Code Review Revision 3 — 2026-10-01

- Reviewed committed revision: `9c0d45378dafb1095ff7c03a1c7da16beb597e3` on `UI-003feat`; source fix ancestry `9cbd82a`, direct-login test fix `a84f848`.
- Result: `APPROVED`.
- `CR-UI003-006` and `CR-UI003-007` are resolved: the drawer is a 304dp side panel with the approved scrim/composition, and denied-state retry reaches `MapShellCallbacks.onLocation`.
- No remaining implementation findings were identified in the approved UI-003 scope.
- Review was static; no tests were run during this review. Existing evidence records `MapShellScreenTest` 7/7 and the full connected suite 36/36 on the approved emulator.
- AC-UI003-009 remains `NOT VERIFIED` because current-revision responsive screenshots/hierarchies were not captured. Hosted CI remains unavailable.
- Next action: `verification` after current-revision responsive evidence and the required CI gate are available.

## Debug Fix Revision 3 — 2026-10-01

- Fixed `CR-UI003-006`: the drawer is now a 304dp full-height themed side panel with full-screen scrim, dynamic identity/avatar, icon-backed navigation rows, and a light logout row.
- Fixed `CR-UI003-007`: denied-state retry now reaches `MapShellCallbacks.onLocation` through `MapShellRoute`; the ViewModel's existing loading transition is unchanged.
- Focused developer validation: `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:testDebugUnitTest :app:lintDebug` — PASS.
- Focused connected validation: `ANDROID_SERIAL=emulator-5554 JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.feature.mapshell.MapShellScreenTest` — PASS, 7/7 on `Medium_Phone(AVD) - 14`.
- The physical device with no Compose hierarchy was excluded from this focused evidence; it is an environment limitation, not a UI-003 assertion failure.
- `git diff --check` is required before commit. Verification remains `NOT VERIFIED` pending Code Review Revision 3, fresh responsive evidence, and hosted CI.

## Direct-login regression coverage — 2026-10-01

- Root cause: `DebugDirectLoginTest` asserted the pre-fix combined identity string, while the approved drawer renders display name and role as separate nodes.
- Fix scope: test-only assertion update; no production code or acceptance criteria changed.
- Runtime: Android Studio JBR 25.0.3; `Medium_Phone(AVD) - 14`, API 34, `emulator-5554`.
- Focused command: `ANDROID_SERIAL=emulator-5554 JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.DebugDirectLoginTest` — PASS.
- Full command: `ANDROID_SERIAL=emulator-5554 JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:connectedDebugAndroidTest` — PASS, 36/36.
- Physical devices were excluded by explicit serial targeting. This evidence proves the direct-login regression flow on the approved test machine only.

## Current-revision responsive evidence — 2026-10-01

- Revision under test: `9c0d453` on `UI-003feat`; package installed from the current debug APK on `emulator-5554`.
- Device state: API 34 `Medium_Phone(AVD)`, standard 1080x2400, restored to font scale 1.0 after capture.
- Standard: [`runtime-map-shell-standard-9c0d453-1080x2400.png`](runtime-map-shell-standard-9c0d453-1080x2400.png), SHA-256 `4d8c8e4396d5ffdfc6b4958ae02ee0663c0c1c517f73c2b6ff2ea552cffc3dd2`; hierarchy [`ui-map-shell-standard-9c0d453.xml`](ui-map-shell-standard-9c0d453.xml), SHA-256 `99d9a0fd7a0c021d8a876c3de09fcffc4c7842ff288e220cd3607758cfe52634`.
- Narrow: [`runtime-map-shell-narrow-9c0d453-720x2400.png`](runtime-map-shell-narrow-9c0d453-720x2400.png), SHA-256 `2459264a262fcda03b3094c55186322d216ea2d6f1f3caa160fac5d27a983fe7`; hierarchy [`ui-map-shell-narrow-9c0d453.xml`](ui-map-shell-narrow-9c0d453.xml), SHA-256 `a7d35c476d5975c1fc49db89cbff0abddc5b2a8f6f3a32f98e8204b70f78f7de`.
- Font scale 1.3: [`runtime-map-shell-fontscale-1.3-9c0d453-1080x2400.png`](runtime-map-shell-fontscale-1.3-9c0d453-1080x2400.png), SHA-256 `87b340f580273da9c298844a74ce0972ecbdeae2a2d70f30266d40b9276b303c`; hierarchy [`ui-map-shell-fontscale-1.3-9c0d453.xml`](ui-map-shell-fontscale-1.3-9c0d453.xml), SHA-256 `b7dae2f67da84fc818918d1edd0fee5f5a89099b86316617ecab9c76448bb170`.
- Drawer at standard: [`runtime-map-shell-drawer-9c0d453-1080x2400.png`](runtime-map-shell-drawer-9c0d453-1080x2400.png), SHA-256 `a138a85f00c581c69e84f1a55850d3bb650e31514fed25b262f8117fd6f2a55d`; hierarchy [`ui-map-shell-drawer-9c0d453.xml`](ui-map-shell-drawer-9c0d453.xml), SHA-256 `66c24f3222a036d64eeda29dd2e53a4f722ab6d96d073570fa4a899b1f0cd391`.
- Drawer at font scale 1.3: [`runtime-map-shell-drawer-fontscale-1.3-9c0d453-1080x2400.png`](runtime-map-shell-drawer-fontscale-1.3-9c0d453-1080x2400.png), SHA-256 `e364bf6a01402ca8a935e6741d66074963a6734ae80f04d1ac8239edc4432e38`; hierarchy [`ui-map-shell-drawer-fontscale-1.3-9c0d453.xml`](ui-map-shell-drawer-fontscale-1.3-9c0d453.xml), SHA-256 `79ec9313b39facfe8885807142d3516a52a1304705ee948f7d80abbcc8febc73`.
- Visual observation: all requested shell controls, exactly three basemap choices, location affordance, drawer identity, `圖台`, `儀錶板`, `登出`, and `關閉選單` remain visible; no primary control clipping was observed. This evidence does not infer hidden API, permission or persistence behavior.

## Verification attempt — 2026-10-01 (superseded by direct-login fix)

- Candidate source revision: `9cbd82a`; documentation handoff revision: `b0b4852`.
- Full connected command: `ANDROID_SERIAL=emulator-5554 JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:connectedDebugAndroidTest`.
- Result: `35/36` tests passed, `1` failed, `0` errors and `0` skipped on `Medium_Phone (AVD) - 14`, API 34, serial `emulator-5554`. Result XML: `app/build/outputs/androidTest-results/connected/debug/TEST-Medium_Phone(AVD) - 14.xml`, SHA-256 `afb20aa45094e4d107b3ef4cc007783252aff853cde8893e6e8c735029cb3f9f`.
- Failure: `DebugDirectLoginTest.directLoginAndLogoutReturnToSignedOut` cannot find `開發測試人員 · ADMINISTRATOR` at line 39. The current drawer renders `開發測試人員` and `ADMINISTRATOR` as separate nodes after the approved drawer composition fix.
- UI-003 focused evidence remains PASS: `MapShellScreenTest` `7/7`, including current-revision drawer, callback, retry and logout assertions.
- Historical result: `NOT VERIFIED`; the direct-login assertion failure was fixed in `a84f848` and the full suite subsequently passed. Code Review Revision 3, fresh current-revision responsive evidence, and hosted CI remain pending.

## Verification rerun — focused device test — 2026-10-01

- Scope: only the UI-003 Compose/device test class; no full connected suite, physical device, screenshot recapture or production-code change was performed.
- Revision: `9c0d453` (`UI-003feat`); source fix ancestry `9cbd82a`, direct-login test fix `a84f848`.
- Environment: Android Studio JBR 25.0.3; `Medium_Phone (AVD) - 14`, API 34, serial `emulator-5554`.
- Command: `ANDROID_SERIAL=emulator-5554 GRADLE_USER_HOME="/private/tmp/tp-ncolso-gradle-verification" JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.feature.mapshell.MapShellScreenTest`.
- Result: `BUILD SUCCESSFUL`; `MapShellScreenTest` `7/7` passed, `0` failures, `0` errors and `0` skipped. XML timestamp `2026-10-01T06:37:22`, SHA-256 `4ffbd5d8addbda942cf8904692caea6b82632f7e0eb8cf58dc6905d88e12b9a5`.
- Covered tests: shell controls/basemaps, drawer navigation callbacks, loading semantics, permission-denied retry callback, top/location callbacks and exactly-once logout callback.
- Historical verification boundary: focused evidence PASS; current-revision responsive evidence was captured below. Overall Verification remains `NOT VERIFIED` only because hosted CI is unavailable.
