# Issue Log

## Debug handoff — 2026-09-29

- Root cause analysis: `root-cause.md`.
- Minimum fix plan: `fix-plan.md`.
- All five findings remain open pending authorized implementation and a new review revision.
- Classification is `implementation_failure`; no requirement, planning or environment re-route was identified.

## Implementation fix — revision `51cb024`

- `CR-UI003-001` through `CR-UI003-005` have implementation fixes in the committed revision `51cb024`.
- Focused and connected validation passed after the fixes; findings remain `fixed_pending_review` until Code Review confirms the new semantics, component usage, callback ports, icon/token path and hygiene.
- Verification remains `NOT VERIFIED`; no hosted CI run exists for `UI-003feat`.

## CR-UI003-001 — Basemap selected semantics are not exposed

- Category: `implementation_failure`
- Priority: `P1`
- Status: open
- Revision: `0e0b60995a73252ea6610b513c8c512baba6bd92`
- Evidence: `MapShellScreen.kt:167-178` uses `clickable(role = Role.RadioButton)` but never declares `selectable(selected = ...)` or a selected state. The captured hierarchy `ui-map-shell-normal.xml` reports all three radio nodes with `selected="false"`, including the initial `電子地圖`.
- Requirement: `AC-UI003-002` and the `CMP-BASEMAP-SWITCHER` contract require exactly one selected basemap; the accessibility rules require selection state not to be conveyed by color alone.
- Impact: The visual selected state is present, but assistive technology cannot identify which basemap is selected. The current test only checks labels and does not catch this regression.
- Route: `debug`
- Owner: UI-003 implementation

## CR-UI003-002 — Location states do not use the common state components

- Category: `implementation_failure`
- Priority: `P1`
- Status: open
- Revision: `0e0b60995a73252ea6610b513c8c512baba6bd92`
- Evidence: `MapShellScreen.kt:116-122` renders loading and denied states with plain `Text`; the existing `AppLoadingContent` and `AppErrorContent` common components are not used. The Compose test covers only the denied state and does not assert loading semantics.
- Requirement: `AC-UI003-007` requires loading and permission-denied states to use the common UI template; `AC-UI003-008` requires state rendering coverage.
- Impact: Loading lacks the common live-region/loading semantics, and denied state lacks the common error semantics. The execution report currently overstates AC-UI003-007 coverage.
- Route: `debug`
- Owner: UI-003 implementation

## CR-UI003-003 — Drawer navigation rows are non-actionable

- Category: `implementation_failure`
- Priority: `P1`
- Status: open
- Revision: `0e0b60995a73252ea6610b513c8c512baba6bd92`
- Evidence: `MapShellScreen.kt:197-202` renders `圖台` and `儀錶板` as plain `Text`. `ui-map-shell-drawer-fontscale-1.3.xml` reports both nodes as `clickable="false"` and `focusable="false"`.
- Requirement: `AC-UI003-004` requires the approved identity/navigation/logout drawer composition; `OVL-NAV-01` lists 圖台、儀表板、登出, and actionable rows must expose Traditional Chinese semantics and at least 48dp targets.
- Impact: The drawer shows the labels but does not provide navigation actions or accessibility targets. The existing test verifies logout only.
- Route: `debug`
- Owner: UI-003 implementation

## CR-UI003-004 — Feature UI bypasses the approved icon/token path

- Category: `implementation_failure`
- Priority: `P2`
- Status: open
- Revision: `0e0b60995a73252ea6610b513c8c512baba6bd92`
- Evidence: `MapShellScreen.kt:108-109` contains raw overlay colors, and `ShellAction` at `:208-217` renders Unicode strings instead of the approved Material/system icon path described by the plan and component catalog.
- Requirement: The design baseline requires semantic theme tokens and the plan specifies Material/system icons for shell controls.
- Impact: The implementation can drift from the shared foundation and approved visual/icon behavior. This is not a production API or security issue.
- Route: `debug`
- Owner: UI-003 implementation

## CR-UI003-005 — Review revision contains whitespace defects

- Category: `implementation_failure`
- Priority: `P2`
- Status: open
- Revision: `0e0b60995a73252ea6610b513c8c512baba6bd92`
- Evidence: `git diff --check 48670ff HEAD` reports new blank lines at EOF in `MapShellContract.kt`, `MapShellViewModel.kt` and the current execution report.
- Impact: No runtime impact, but the committed review revision does not pass the repository whitespace check.
- Route: `debug`
- Owner: UI-003 implementation
