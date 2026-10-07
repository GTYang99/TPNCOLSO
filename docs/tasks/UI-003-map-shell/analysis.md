# Repository Analysis

## Current Behavior

- `AppRoot` already switches between signed-out and signed-in content through `AppSessionState`, but the debug signed-in slot currently renders placeholder text and a debug logout button.
- UI-001 provides shared Compose foundation components, semantic tokens, `AppIdentity`/`AppRole` and the signed-in content slot; UI-002 provides the clean Login destination and logout-clearing coordinator boundary.
- UI-003 now has a UI-only `feature.mapshell` implementation on `UI-003feat`; production map/API/WMTS integration remains intentionally absent.

## Expected Behavior

- Replace the placeholder signed-in content with a UI-003-owned map-shell route that renders the approved visible controls and state fixtures.
- Keep map shell state, callbacks and fake overlays feature-owned and immutable; leave production tile/network/data behavior to later approved integration tasks.
- Emit one stable logout callback that can be consumed by the existing UI-002 coordinator without importing Auth screen internals.

## Affected Modules

- `app/src/main/java/com/example/tp_ncolso_android/feature/mapshell/`: UI-003 state, reducer/ViewModel, callbacks and Compose screens.
- `app/src/debug/java/com/example/tp_ncolso_android/AppEntry.kt`: replace the debug signed-in placeholder with the map-shell host while preserving the debug-only entry boundary.
- `app/src/test/.../feature/mapshell/`: pure state/reducer and idempotency tests.
- `app/src/androidTest/.../feature/mapshell/`: semantics, interaction and responsive runtime tests.
- Existing `session`, `AppRoot` and `ui.foundation` contracts: consume only; breaking changes require a separate approved plan.

## Dependencies

- `AppRoot` signed-in slot and `AppIdentity` from UI-001.
- `LogoutRequested` handoff requirement from `docs/tasks/UI-002-auth/logout-interface-requirement.md`.
- Existing foundation theme, icon/button/status components and 48dp target contract.
- Archived or structured Figma evidence for node `4952:18205` before visual comparison.
- A later map SDK/WMTS decision for real tile integration; not required for UI-only fixtures.

## Risks

- Treating screenshot terrain imagery as production tile content would create a design/data authority conflict.
- Coupling the drawer directly to AuthViewModel or Token/API types would break task ownership and make UI-002 logout verification non-reproducible.
- Unbounded map SDK or navigation dependencies could expand scope before the required API and session contracts exist.
- The real cross-task logout verification remains dependent on UI-002/UI-003 handoff evidence; the approved UI-only shell implementation does not claim that integration gate is complete.

## Unknowns

- Final map SDK, WMTS layer parameters, attribution, caching/offline policy and production overlay source.
- Final approved drawer asset export or Figma revision, if the requester changes the current visible source.
