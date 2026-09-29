# UI-003 Debug Fix Plan

## Current debug re-entry

- Findings: `CR-UI003-001` through `CR-UI003-005`
- Reviewed revision: `0e0b60995a73252ea6610b513c8c512baba6bd92`
- Fix type: implementation-debug re-entry within the approved UI-only map-shell scope
- Authorization boundary: production code must remain unchanged until this plan is approved for implementation

## Minimum safe fix

1. **Expose basemap selection semantics.** Replace the manual radio-role `clickable` path with `selectable(selected = selected, role = Role.RadioButton, ...)` (or an equivalent semantics-preserving implementation) inside a selectable group. Keep the three labels, default `電子地圖`, mutual exclusion, visual selected state and reducer unchanged.
2. **Use common location-state components.** Render `LocationState.LOADING` with `AppLoadingContent(message = "定位中…")`. Render `LocationState.PERMISSION_DENIED` with `AppErrorContent(message = "需要定位權限才能顯示目前位置", retry = ...)`, preserving the existing retry event and UI-only behavior. Keep normal state unchanged.
3. **Make drawer navigation rows actionable through callback ports.** Extend `MapShellCallbacks` with explicit `onMapPlatform` and `onDashboard` callbacks (default no-op), route corresponding drawer events without adding navigation destinations or production API behavior, and render both rows as 48dp-or-larger selectable/button targets with Traditional Chinese descriptions. Preserve logout exactly-once behavior and close-drawer behavior.
4. **Use approved visual foundations.** Replace raw overlay colors with the nearest existing `AppThemeTokens` colors. Replace Unicode shell/location glyphs with the existing Material/system icon path or the project-approved icon component, preserving labels, target sizes and callback tags. Do not add a new icon library or alter map/API scope.
5. **Fix review-revision hygiene.** Remove the reported trailing blank lines from `MapShellContract.kt`, `MapShellViewModel.kt` and `execution-report.md`, then run `git diff --check` against the review base before committing.

## Focused regression checks

- Unit tests: initial `電子地圖`, mutual exclusion, overlay preservation and repeated logout idempotency remain green.
- Compose semantics tests:
  - exactly one basemap node exposes `selected = true` initially and after each selection;
  - loading exposes the common polite live-region/loading semantics;
  - denied exposes common error semantics and keeps retry callback behavior;
  - `圖台` and `儀錶板` each expose a click action, Traditional Chinese label and at least a 48dp target;
  - search, notification, map navigation, location, drawer and logout callback ports remain observable;
  - repeated logout still emits one callback and removes the drawer through the existing coordinator path.
- Build checks: `:app:testDebugUnitTest`, `:app:lintDebug`, `:app:assembleDebug`, `:app:assembleRelease`, and `:app:assembleDebugAndroidTest`.
- Connected checks: `:app:connectedDebugAndroidTest` on the approved API 34 emulator, including the existing direct-login/logout regression and the new location/drawer/selection semantics coverage.
- Responsive checks: rerun standard, narrow-width, font-scale 1.3 and drawer evidence; confirm no primary control or callback is clipped.
- Hygiene and isolation: `git diff --check`; release isolation scan remains free of debug-only direct-login/fake-auth entry points.

## Scope and plan-review decision

This fix preserves the approved requirement, visual authority, UI-001 foundation ownership, UI-002 logout boundary and UI-only map/API deferral. It does not add real map tiles, WMTS parameters, persistence, navigation destinations or Auth state mutation. A new Plan Review is not required for this minimum fix. If navigation rows are changed from callback ports into real destinations, or if the icon/component correction requires a material visual-baseline change, stop and route back to Plan Review before implementation.

## Exit condition

After authorized implementation and developer validation, commit a new revision, update the issue log and execution report, request Code Review again, and keep Verification as `NOT VERIFIED` until the new review and required CI evidence are complete.
