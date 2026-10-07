# Implementation Plan

## Goal

- Implement the authenticated UI-only map shell and drawer contract so UI-002 can later verify a real `LogoutRequested` handoff.

## Scope

- Add UI-003 state, fake/static map surface, basemap switcher, drawer, location states, callback boundaries and focused tests; do not add production map/API/Token behavior.

## Affected Files

- `app/src/main/java/com/example/tp_ncolso_android/feature/mapshell/MapShellContract.kt` — immutable state, events, callbacks and fake overlay model.
- `app/src/main/java/com/example/tp_ncolso_android/feature/mapshell/MapShellViewModel.kt` — state transitions, basemap selection, overlay preservation and idempotent event handling.
- `app/src/main/java/com/example/tp_ncolso_android/feature/mapshell/MapShellScreen.kt` — shell, controls, basemap switcher, drawer and location states.
- `app/src/debug/java/com/example/tp_ncolso_android/AppEntry.kt` — wire the signed-in slot to UI-003 and route `LogoutRequested` to the existing boundary.
- `app/src/test/.../feature/mapshell/` — reducer, default selection, overlay preservation and repeated-event tests.
- `app/src/androidTest/.../feature/mapshell/` — semantics, drawer interaction, callback and responsive tests.

## Implementation Steps

- Define the UI-only state/event contract with three basemap values, immutable overlay fixtures, location states and stable callback types; keep production API types out.
- Implement pure reducer/ViewModel transitions for default EMAP, mutually exclusive selection, overlay preservation, location loading/denied states and one-shot/idempotent logout events.
- Build the full-screen Compose shell using existing foundation tokens/components and Material/system icons with Traditional Chinese semantics and 48dp targets.
- Implement the drawer visual and route the accessible `登出` row to exactly one `LogoutRequested` callback without clearing Auth state inside UI-003.
- Wire the debug signed-in content slot to the map-shell host while preserving the existing signed-out/debug direct-login behavior.
- Add focused unit and Compose tests before any visual comparison; record fake-data and screenshot evidence with source/revision identity.

## Test Plan

- Run focused UI-003 unit tests for initial state, mutual exclusion, overlay preservation, location transitions and repeated logout idempotency.
- Run focused Compose/android tests for semantics labels, 48dp targets, drawer open/close, callback emission and all location states.
- Run existing UI-001 foundation, UI-002 auth unit and connected regression suites.
- Run debug/release compilation and lint; verify no production API/Token/map dependency or debug-only symbol leaks into release.
- Execute local responsive checks at the 402×874 reference, narrow supported width, font scale 1.3 and IME-visible conditions.

## Regression Plan

- Confirm UI-002 Login/Register routes and the existing clean logout coordinator remain unchanged and return `LOGIN_EMPTY`.
- Confirm debug direct-login role selection and AppRoot signed-out/signed-in switching still work.
- Confirm release source/artifact inspection contains no debug direct-login or fake authenticated map entry.
- Confirm all existing foundation/auth tests remain green.

## Risks

- Figma visual details may change before Plan Review; bind captures to the approved source hash and revalidate if the source changes.
- Map SDK/WMTS details are intentionally deferred; adding them here would violate task scope.
- UI-003 cannot provide real cross-task logout evidence until the UI-002 prerequisite handoff is complete.

## Rollback Plan

- Revert only the UI-003 implementation commit and restore the debug signed-in placeholder; do not reset unrelated worktree changes.

## Current Behavior

- The signed-in `AppRoot` slot currently shows placeholder authenticated content and a debug-only logout button; there is no UI-003 map shell or drawer integration.

## Expected Behavior

- The signed-in slot renders the UI-003 map shell with observable control/state contracts and emits `LogoutRequested` to the existing auth/session boundary while remaining independent of production API behavior.

## Acceptance Criteria Traceability

| AC | Implementation Step | Validation |
|---|---|---|
| AC-UI003-001 | MapShellScreen shell and AppEntry wiring | Compose hierarchy and connected interaction test |
| AC-UI003-002 | MapShellContract and basemap switcher | Unit default/mutual-exclusion test |
| AC-UI003-003 | Reducer overlay-preservation transition | Unit state fixture test |
| AC-UI003-004 | Drawer visual and semantics row | Compose semantics and interaction test |
| AC-UI003-005 | Logout event reducer/callback boundary | Unit idempotency plus Compose callback test; later UI-002 integration evidence |
| AC-UI003-006 | Explicit callback ports | Unit/Compose callback assertions with no API dependency |
| AC-UI003-007 | Location state rendering | Compose state matrix and accessibility assertions |
| AC-UI003-008 | Focused test suites | `testDebugUnitTest` and `connectedDebugAndroidTest` |
| AC-UI003-009 | Responsive shell layout | Local emulator matrix and hierarchy/screenshot evidence |

## Failure Behavior

- Loading and denied location states remain visible with a retry/action callback; no permission or map-service failure may expose credentials or block the rest of the shell.
- Unknown callback consumers do not mutate production data; UI-only fixtures remain deterministic.
- Repeated logout events are ignored after the signed-out boundary has been requested; UI-003 does not restore signed-in content.

## Security and Privacy

- Do not log, persist or display Token, password, captcha or unrestricted personal data.
- Use only the supplied `AppIdentity.displayName`/role for the UI fixture; do not invent production profile data.
- Keep production release free of debug-only direct-login and fake map content entry points according to the existing source-set contract.

## Open Questions

- Final map SDK/WMTS parameters, attribution, caching and offline behavior remain INT-002/product integration decisions.
- Final approved drawer asset export remains a Plan Review revalidation item if the current visual source changes.
