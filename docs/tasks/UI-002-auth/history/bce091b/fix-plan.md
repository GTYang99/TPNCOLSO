# UI-002 Verification Fix Plan

## Approved scope

1. Preserve the approved Figma-exported asset bytes and ownership recorded in `docs/assets/app-ui-assets.md`.
2. Add runtime-consumable resources for the approved Login skyline and Register illustration without recreating or approximating the artwork.
3. Inject debug-only captcha rendering as already established; do not ship the fixture as production data.
4. Add/adjust source-level and Compose test coverage only where it proves the asset slot and existing semantics.
5. Re-run local unit/build/Android-test compilation evidence. Keep connected runtime and pixel comparison `NOT VERIFIED` until an approved emulator execution is available.

## Regression checks

- `testDebugUnitTest`
- `assembleDebug`
- `assembleRelease`
- `assembleDebugAndroidTest`
- release isolation scan

## Exit

After the runtime asset integration and local checks pass, return to Verification and update the per-AC evidence. Do not claim connected-runtime or pixel-comparison PASS without execution evidence.
