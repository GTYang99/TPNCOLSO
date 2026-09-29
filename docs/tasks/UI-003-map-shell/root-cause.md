# UI-003 Debug Root Cause Analysis

## Scope and reviewed revision

- Task: `UI-003-map-shell`
- Reviewed revision: `0e0b60995a73252ea6610b513c8c512baba6bd92`
- Branch: `UI-003feat`
- Classification: `implementation_failure` -> `debug`
- Evidence: Code Review Revision 1 findings `CR-UI003-001` through `CR-UI003-005` in `issue-log.md`.

The local unit, connected, build and lint checks pass, but those checks do not establish that the reviewed accessibility and component-contract requirements are satisfied. The failures are reproducible from source inspection and the captured UI hierarchies; they are not caused by hosted CI or emulator availability.

## Confidence strengthening evidence — 2026-09-29

- `ui-map-shell-normal.xml` directly reports `selected="false"` for `電子地圖`, `正射圖` and `地形圖`, including the initial `電子地圖` state.
- `ui-map-shell-drawer-fontscale-1.3.xml` directly reports `clickable="false"` and `focusable="false"` for the `圖台` and `儀錶板` text nodes, while `登出` has an actionable parent target.
- Source inspection confirms the exact reviewed constructs: `clickable(role = Role.RadioButton)`, plain location `Text`, plain drawer `Text`, raw `Color(...)`, and Unicode shell glyphs.
- `git diff --check 48670ff HEAD` reproduces the three reported EOF whitespace errors.
- Focused verification completed successfully with `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:testDebugUnitTest :app:lintDebug` (`BUILD SUCCESSFUL`). This confirms the implementation compiles and the existing tests/lint pass; it does not negate the uncovered review findings.

Based on independent source, runtime-hierarchy and repository-check evidence, confidence in the five root-cause classifications is **95%+**. The remaining uncertainty is limited to the exact foundation icon replacement and post-fix runtime behavior, which require implementation and re-validation; neither affects the diagnosis of the current revision.

## Requirement mapping

| Finding | Requirement / AC | Current evidence | Result |
|---|---|---|---|
| `CR-UI003-001` | `AC-UI003-002` | `BasemapSwitcher` uses `clickable(role = Role.RadioButton)` and manually assigns `role`, but never exposes `selected`; the normal hierarchy reports all three choices as `selected="false"`. | Fails selected-state semantics. |
| `CR-UI003-002` | `AC-UI003-007`, `AC-UI003-008` | `MapSurface` renders `LOADING` and `PERMISSION_DENIED` with plain `Text`; only denied state is tested. | Fails common loading/error template contract and leaves loading untested. |
| `CR-UI003-003` | `AC-UI003-004` and `OVL-NAV-01` | Drawer rows `圖台` and `儀錶板` are plain `Text`; captured drawer hierarchy marks both `clickable="false"` and `focusable="false"`. | Fails actionable navigation-row semantics. |
| `CR-UI003-004` | Approved plan icon/token constraint | `MapShellScreen.kt` uses raw `Color(...)` values and Unicode glyphs in `ShellAction`; shared theme/icon path is bypassed. | Fails approved foundation usage; no API/security impact. |
| `CR-UI003-005` | Repository review gate | `git diff --check 48670ff HEAD` reports blank lines at EOF in `MapShellContract.kt`, `MapShellViewModel.kt` and `execution-report.md`. | Review revision fails whitespace hygiene. |

## Root causes

### CR-UI003-001 — selected basemap state is only visual

`BasemapSwitcher` derives `selected` for color and text contrast, but the interaction modifier is `clickable`, which has no selected-state parameter. The following `semantics` block overwrites/adds the radio role and label only; it does not publish `SemanticsProperties.Selected`. The implementation therefore has a correct reducer state and an incomplete accessibility projection. The existing test checks labels and visibility, not selected semantics, so the defect passes local tests.

### CR-UI003-002 — feature screen reimplemented status states locally

The implementation treated location states as small inline messages instead of composing the existing `AppLoadingContent` and `AppErrorContent` components. That bypasses the shared live-region and error semantics (`AppLoadingContent` sets a polite live region; `AppErrorContent` sets error and polite live-region semantics). The test covers only the retry action in the denied state and has no loading assertion, allowing both the component-contract regression and coverage gap.

### CR-UI003-003 — drawer composition modeled labels, not navigation actions

The drawer was implemented with `Text` for `圖台` and `儀錶板` because the current UI-only task has no authorized navigation destinations or callback contracts for those rows. That scope constraint was incorrectly translated into non-actionable UI instead of explicit callback ports with no-op/default consumers. As a result, visible labels exist but do not satisfy the approved actionable-row/accessibility contract.

### CR-UI003-004 — feature-local visual approximation bypassed shared foundations

The implementation used direct color literals for the fake overlay and Unicode glyphs for shell actions to finish the static shell quickly. The approved plan explicitly required existing foundation tokens and Material/system icons. The failure is a conformance shortcut in the feature screen, not a missing product requirement or a map/API dependency.

### CR-UI003-005 — review evidence was committed without the repository hygiene check

The implementation validation documentation and two newly added Kotlin files retain an extra blank line at EOF. The validation commands recorded build/test/lint results but did not include or enforce `git diff --check` before the review revision was committed. This is a process/quality defect in the reviewed revision, independent of runtime behavior.

## Root-cause conclusion

The common underlying cause is that implementation optimized for visible local behavior and callback tests, while the approved contract also requires shared foundation semantics, actionable drawer affordances, and repository hygiene. No requirement, plan, API, WMTS, or environment conflict was found. The minimum correction remains within the approved UI-only scope; no new Plan Review is required unless the fixes add navigation behavior beyond callback ports or alter the approved visual/ownership contract.

## Debug boundary

Production code remains unchanged during this analysis. Re-implementation may start only after the fix plan is authorized. Verification remains blocked until a new committed revision resolves all review findings and is re-reviewed.
