# UI-003 Implementation Execution Report

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
- AC-UI003-005 proves UI-003 exactly-once effect behavior and the debug receiver path; the final cross-task UI-002 verification remains a downstream handoff gate.
- AC-UI003-009 responsive matrix (402×874, narrow, font scale 1.3 and IME-visible) has not yet been captured for the map shell; it remains `NOT VERIFIED` until those captures are collected.
- Real basemap tiles, WMTS parameters, attribution, caching/offline behavior and parcel overlays remain intentionally out of scope.

## Next action

Code review / CI for `UI-003feat`, followed by responsive visual validation and cross-task logout verification.

