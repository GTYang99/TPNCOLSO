# Issue Log

## Debug handoff — 2026-09-29 (historical)

- Root cause analysis: `root-cause.md`.
- Minimum fix plan: `fix-plan.md`.
- At handoff, all five findings were open pending implementation and re-review.
- Classification is `implementation_failure`; no requirement, planning or environment re-route was identified.

## Implementation fix — revision `51cb024`

- `CR-UI003-001` through `CR-UI003-005` have implementation fixes in the committed revision `51cb024`.
- Focused and connected validation passed after the fixes. Code Review Revision 2 below confirms these five findings are resolved.
- Verification remains `NOT VERIFIED`; no hosted CI run exists for `UI-003feat`.

## CR-UI003-001 — Basemap selected semantics are not exposed

- Category: `implementation_failure`
- Priority: `P1`
- Status: resolved
- Revision: `0e0b60995a73252ea6610b513c8c512baba6bd92`
- Evidence: `MapShellScreen.kt:167-178` uses `clickable(role = Role.RadioButton)` but never declares `selectable(selected = ...)` or a selected state. The captured hierarchy `ui-map-shell-normal.xml` reports all three radio nodes with `selected="false"`, including the initial `電子地圖`.
- Requirement: `AC-UI003-002` and the `CMP-BASEMAP-SWITCHER` contract require exactly one selected basemap; the accessibility rules require selection state not to be conveyed by color alone.
- Impact: The visual selected state is present, but assistive technology cannot identify which basemap is selected. The current test only checks labels and does not catch this regression.
- Route: `debug`
- Owner: UI-003 implementation
- Resolution: `51cb024` uses `selectable(selected = ..., role = Role.RadioButton)` in a selectable group.
- Verification: Code Review Revision 2 confirmed selected semantics and the initial/changed state assertions in `MapShellScreenTest`.

## CR-UI003-002 — Location states do not use the common state components

- Category: `implementation_failure`
- Priority: `P1`
- Status: resolved
- Revision: `0e0b60995a73252ea6610b513c8c512baba6bd92`
- Evidence: `MapShellScreen.kt:116-122` renders loading and denied states with plain `Text`; the existing `AppLoadingContent` and `AppErrorContent` common components are not used. The Compose test covers only the denied state and does not assert loading semantics.
- Requirement: `AC-UI003-007` requires loading and permission-denied states to use the common UI template; `AC-UI003-008` requires state rendering coverage.
- Impact: Loading lacks the common live-region/loading semantics, and denied state lacks the common error semantics. The execution report currently overstates AC-UI003-007 coverage.
- Route: `debug`
- Owner: UI-003 implementation
- Resolution: `51cb024` renders loading and denied states through `AppLoadingContent` and `AppErrorContent`.
- Verification: Code Review Revision 2 confirmed common-component usage and Compose assertions for loading semantics and denied retry presentation.

## CR-UI003-003 — Drawer navigation rows are non-actionable

- Category: `implementation_failure`
- Priority: `P1`
- Status: resolved
- Revision: `0e0b60995a73252ea6610b513c8c512baba6bd92`
- Evidence: `MapShellScreen.kt:197-202` renders `圖台` and `儀錶板` as plain `Text`. `ui-map-shell-drawer-fontscale-1.3.xml` reports both nodes as `clickable="false"` and `focusable="false"`.
- Requirement: `AC-UI003-004` requires the approved identity/navigation/logout drawer composition; `OVL-NAV-01` lists 圖台、儀表板、登出, and actionable rows must expose Traditional Chinese semantics and at least 48dp targets.
- Impact: The drawer shows the labels but does not provide navigation actions or accessibility targets. The existing test verifies logout only.
- Route: `debug`
- Owner: UI-003 implementation
- Resolution: `51cb024` replaces both labels with 48dp-minimum `AppSecondaryButton` actions and explicit callback ports.
- Verification: Code Review Revision 2 confirmed both callback routes and the shared button minimum target contract.

## CR-UI003-004 — Feature UI bypasses the approved icon/token path

- Category: `implementation_failure`
- Priority: `P2`
- Status: resolved
- Revision: `0e0b60995a73252ea6610b513c8c512baba6bd92`
- Evidence: `MapShellScreen.kt:108-109` contains raw overlay colors, and `ShellAction` at `:208-217` renders Unicode strings instead of the approved Material/system icon path described by the plan and component catalog.
- Requirement: The design baseline requires semantic theme tokens and the plan specifies Material/system icons for shell controls.
- Impact: The implementation can drift from the shared foundation and approved visual/icon behavior. This is not a production API or security issue.
- Route: `debug`
- Owner: UI-003 implementation
- Resolution: `51cb024` uses theme tokens for map fixture colors, Material3 `Icon`/`IconButton`, and project vector resources.
- Verification: Code Review Revision 2 confirmed the relevant source changes; the current committed diff has no raw map-surface color literals or Unicode shell glyphs.

## CR-UI003-005 — Review revision contains whitespace defects

- Category: `implementation_failure`
- Priority: `P2`
- Status: resolved
- Revision: `0e0b60995a73252ea6610b513c8c512baba6bd92`
- Evidence: `git diff --check 48670ff HEAD` reports new blank lines at EOF in `MapShellContract.kt`, `MapShellViewModel.kt` and the current execution report.
- Impact: No runtime impact, but the committed review revision does not pass the repository whitespace check.
- Route: `debug`
- Owner: UI-003 implementation
- Resolution: whitespace defects were removed in `51cb024`.
- Verification: `git diff --check 48670ff..51cb024` completed without diagnostics.

## Code Review Revision 2 — 2026-09-30

- Result: `CHANGES_REQUESTED`.
- Reviewed implementation revision: `51cb024fe907b2047750162ed857900fe126abbd` on `UI-003feat`; review base: `0e0b60995a73252ea6610b513c8c512baba6bd92`.
- Review covered the approved requirement, drawer/logout contract, implementation source, tests, and prior review fixes. No source changes were made during review.
- `CR-UI003-001` through `CR-UI003-005` are resolved in this revision.
- New findings: `CR-UI003-006` and `CR-UI003-007`; both route to `debug`.
- Validation boundary: existing unit/connected results are recorded in `execution-report.md`; no tests were run during this review. The available responsive screenshots and hierarchies predate `51cb024` and do not establish AC-UI003-009 for this revision. Hosted CI is unavailable.

## CR-UI003-006 — Drawer occupies the whole screen and misses approved composition

- Category: `implementation_regression`
- Priority: `P1`
- Status: resolved
- Revision: `51cb024fe907b2047750162ed857900fe126abbd`
- Evidence: `MapShellScreen.kt:215-218` applies `fillMaxSize()` before `width(304.dp)`, so the width modifier receives tight full-screen constraints and the drawer remains full width. The runtime capture `runtime-map-shell-drawer-fontscale-1.3-1080x2400.png` shows the prior revision's matching modifier chain covering the complete viewport; the outer drawer modifiers are unchanged by `51cb024`. The approved `logout-interface-requirement.md` specifies a blue side panel, avatar, navigation icons, white Logout row, and a visible map scrim; the current `Drawer` uses a surface-colored panel, text-only header/actions, and a filled primary logout button.
- Requirement: `AC-UI003-004`; approved drawer composition in `docs/tasks/UI-002-auth/logout-interface-requirement.md`.
- Impact: Opening the drawer hides the map entirely and the visible composition does not match the approved drawer, including its identity/avatar, navigation icon, logout-row, and scrim treatment.
- Owner: UI-003 implementation
- Route: `debug`
- Resolution: `9cbd82a` uses a 304dp full-height panel, theme-token blue surface, scrim, identity/avatar, icon-backed rows, and a light logout row.
- Verification: Code Review Revision 3 confirmed the source fix and 304dp width assertion. Current-revision responsive runtime evidence remains a Verification limitation for AC-UI003-009.

## CR-UI003-007 — Location retry does not reach a callback consumer

- Category: `implementation_regression`
- Priority: `P1`
- Status: resolved
- Revision: `51cb024fe907b2047750162ed857900fe126abbd`
- Evidence: `MapShellScreen.kt:134-137` maps the Retry action to `MapShellEvent.RetryLocation`. `MapShellRoute` forwards `LocationClicked` to `callbacks.onLocation` at `:72-82`, but does not forward `RetryLocation`; `MapShellViewModel` only changes the state to `LOADING` at `MapShellViewModel.kt:29`.
- Requirement: `AC-UI003-006` location callback boundary and `AC-UI003-007` denied-state recovery action.
- Impact: Activating `重試` displays an indefinite loading state because no location owner is notified to retry or return a result.
- Owner: UI-003 implementation
- Route: `debug`
- Resolution: `9cbd82a` forwards `RetryLocation` through the location callback contract, with a route-level regression test asserting one callback.
- Verification: Code Review Revision 3 confirmed the route forwarding and focused retry callback assertion.

## Code Review Revision 3 — 2026-10-01

- Result: `APPROVED`.
- Reviewed committed revision: `9c0d45378dafb1095ff7c03a1c7da16beb597e3` on `UI-003feat`; source fixes are in its ancestry at `9cbd82a`, with the direct-login regression assertion fixed in `a84f848`.
- Review covered the approved requirement, drawer/logout contract, production source, UI-003 tests, direct-login regression test, prior findings, and committed validation evidence.
- `CR-UI003-001` through `CR-UI003-005` remain resolved. `CR-UI003-006` and `CR-UI003-007` are resolved and verified by source inspection plus the current focused UI test evidence.
- No remaining implementation findings were identified in the approved UI-003 scope.
- Review checks were static; no tests were run during this review. `git diff --check 51cb024..9c0d453` completed without diagnostics.
- Verification remains `NOT VERIFIED`: current-revision responsive screenshot/hierarchy evidence for AC-UI003-009 is missing, and hosted CI is unavailable for the unpublished branch.

## Debug Fix Revision 3 — 2026-10-01

- Scope: `CR-UI003-006` and `CR-UI003-007` only; no requirement or plan change.
- Focused validation: `:app:testDebugUnitTest :app:lintDebug` passed.
- Connected validation: UI-003 `MapShellScreenTest` passed 7/7 on `Medium_Phone(AVD) - 14` / `emulator-5554`.
- The test runner was explicitly limited to the approved emulator; the unrelated physical device was not used as evidence because it exposes no Compose hierarchy.
- Result boundary: fixes are reviewed and approved; Verification remains `NOT VERIFIED` until current-revision responsive/CI evidence.

## Verification attempt — 2026-10-01 (superseded by direct-login fix)

- Candidate source revision: `9cbd82a` (`fix(UI-003): repair drawer and location retry`); documentation handoff is `b0b4852`.
- Full connected validation command: `ANDROID_SERIAL=emulator-5554 JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:connectedDebugAndroidTest`.
- Result: `35/36` passed, `1` failed, `0` errors, `0` skipped on `Medium_Phone (AVD) - 14`, API 34, `emulator-5554`; XML SHA-256 `afb20aa45094e4d107b3ef4cc007783252aff853cde8893e6e8c735029cb3f9f`.
- `DebugDirectLoginTest.directLoginAndLogoutReturnToSignedOut` fails at `DebugDirectLoginTest.kt:39` because it expects `開發測試人員 · ADMINISTRATOR`, while the current approved drawer composition renders the display name and role as separate nodes. This is a current-revision implementation/test contract regression, not an environment failure.
- UI-003 focused connected evidence remains `MapShellScreenTest` `7/7` PASS, including drawer width, navigation callbacks, retry callback forwarding and exactly-once logout callback.
- Historical route: `debug`; the direct-login assertion was updated in `a84f848`, and the full connected suite subsequently passed. Do not claim Verification PASS until Code Review Revision 3, current responsive evidence and required CI gates are restored.

## Direct-login regression fix — 2026-10-01

- Updated `DebugDirectLoginTest.directLoginAndLogoutReturnToSignedOut` to assert the current approved drawer contract: `開發測試人員` and `ADMINISTRATOR` are separate accessible nodes.
- The test still covers the complete debug direct-login → authenticated map shell → drawer → logout → signed-out Login flow.
- Focused result: `DebugDirectLoginTest` PASS on `emulator-5554`.
- Full connected result: 36/36 PASS on `Medium_Phone(AVD) - 14`, API 34, `emulator-5554`; no physical device was used.
- This resolves `VER-UI003-008` / the direct-login test-contract regression. The remaining Verification limitation is hosted CI.

## Current-revision responsive verification — 2026-10-01

- Current responsive screenshot and hierarchy evidence was captured from revision `9c0d453` on API 34 `emulator-5554` at standard 1080x2400, narrow 720x2400, font scale 1.3, and drawer/font scale 1.3.
- `AC-UI003-009` is PASS at local emulator scope. Checksums and paths are recorded in `execution-report.md`.
- The only remaining Verification limitation is unavailable hosted CI for the unpublished branch; this does not change the local acceptance result.

## Verification rerun — focused device test — 2026-10-01

- Only `MapShellScreenTest` was executed, explicitly targeted to `emulator-5554`; physical devices and the full suite were excluded by scope.
- Result: `7/7 PASS` on `Medium_Phone (AVD) - 14`, API 34, with XML SHA-256 `4ffbd5d8addbda942cf8904692caea6b82632f7e0eb8cf58dc6905d88e12b9a5`.
- No implementation failure was observed in this focused run. The current-revision responsive evidence is recorded above; the overall gate remains `NOT VERIFIED` only because hosted CI is unavailable.

## Hosted CI attempt — 2026-10-01

- Authorization: requester explicitly authorized publishing `UI-003feat` and running hosted CI.
- Published branch: `origin/UI-003feat`, head `c60c605`.
- Workflow run: [Android CI run 36828347279](https://github.com/GTYang99/TPNCOLSO/actions/runs/36828347279).
- `unit-and-build`: `failure` at `Set up Android SDK`; the Gradle build/test step was skipped.
- `connected`: `failure`; the `Connected Android tests` step exited with code `1`, and no usable hosted test result was produced.
- Classification: `infrastructure`; local JBR/emulator evidence remains PASS and is not invalidated by the hosted runner failure.
- Verification remains `NOT VERIFIED`; no merge, deploy or release action was taken.

## VER-UI003-010 — hosted connected job failed without accessible diagnostic output

- Category: `infrastructure` / `unknown`
- Priority: `P1`
- Status: resolved
- Revision: `8f0decf4caa82d66f7acf6be680ada14febb4915`
- Evidence: hosted run `36841914119` has `unit-and-build` PASS and `connected` FAIL; the `Connected Android tests` step exited with code `1`. The check annotations expose no test failure detail, and the job log endpoint returns HTTP 403.
- Impact: required hosted connected-test PASS evidence is unavailable; UI-003 Verification cannot advance beyond `NOT VERIFIED` and UI-004 Implementation cannot start.
- Route: `infrastructure` / `investigation`
- Resolution: Android SDK setup was repaired in workflow commit `8f0decf`; remaining connected failure requires accessible hosted log output or an authorized rerun with diagnostics.

## VER-UI003-011 — hosted connected failure persists after diagnostics reruns

- Category: `investigation`
- Priority: `P1`
- Status: resolved
- Revision: `471d40c`
- Evidence: runs `36947001582`, `36947353256`, and `36947657986` all have hosted `unit-and-build` PASS and hosted `connected` FAIL. In the latest run, Android SDK setup, KVM setup, post-failure diagnostics, and artifact upload all passed; artifact `11202647109` exists, but authenticated download is unavailable. Check annotations expose only bounded runner identity and generic shell exit code `1`.
- Impact: required hosted connected-test PASS evidence remains unavailable; UI-003 Verification cannot advance and UI-004 Implementation cannot start.
- Route: `investigation` / external hosted-runner access
- Resolution: workflow-only diagnostics were added without changing product behavior. Exact emulator/test failure still requires authenticated artifact/log access or an authorized rerun under an accessible hosted-runner account.

## VER-UI003-012 — Arc confirms failure before Gradle connected tests

- Category: `infrastructure`
- Priority: `P1`
- Status: resolved
- Revision: `471d40c`
- Evidence: authenticated Arc inspection of hosted run `36947001582` shows `reactivecircus/android-emulator-runner@v2` entering `Configure emulator`, `Install Android SDK`, `Create AVD`, and `Terminate Emulator`, then exiting with code `1`. The configured `adb devices` and `./gradlew connectedDebugAndroidTest` script was not reached.
- Impact: hosted connected-test PASS evidence is unavailable; UI-003 Verification remains `NOT VERIFIED`, and UI-004 Implementation remains blocked.
- Route: `infrastructure` / hosted emulator runner
- Resolution: SDK setup and KVM preparation already pass. The remaining failure is external runner AVD creation/termination and requires hosted runner remediation or a supported emulator configuration.

## Verification infrastructure issue closure — 2026-10-02

- `VER-UI003-010`, `VER-UI003-011` and `VER-UI003-012` are resolved; their historical evidence remains above.
- Root cause and repair: the hosted emulator profile `Medium_Phone` was not a supported hosted `avdmanager` profile. Workflow commit `cf5c207` changed it to the official `pixel_7_pro` profile, allowing AVD creation and launch.
- Follow-up root cause and repair: the emulator runner executes the script with `/usr/bin/sh`, where `set -o pipefail` is illegal. Workflow commit `00d5d1e` removed that incompatible shell option while retaining `adb devices -l` and `./gradlew connectedDebugAndroidTest`.
- Final evidence: hosted run `36948553870` passed both `unit-and-build` and `connected` on `00d5d1e`; no production code changed.
- Impact closure: the hosted CI gate is satisfied, UI-003 Verification is `PASS`, and the UI-003 prerequisite handoff is complete. No merge, deploy or release action was performed.
