# UI-002 Verification Fix Plan

## Reviewed failure

Fix only the approved-requirement mismatches recorded as `IMP-AUTH-015` for revision `842e29061238527d21ee51e29a61126ba9cbb32`.

## Minimum fix scope

1. Make the Login logo decorative in semantics while preserving the combined brand/title heading behavior.
2. Change captcha reload semantics to the exact approved description `重新產生驗證碼` and provide an effective `48 × 48` interaction target without changing the approved `32dp` layout allocation or shifting adjacent content.
3. Replace the custom Registration Back Canvas drawing with the approved Back asset inside the existing `48 × 48` target. Figma supporting metadata identifies the reference as `I4922:17286;4922:15634` / `I4922:17286;4922:15635` (and the filled-state equivalent `I4922:17494;4922:17423` / `I4922:17494;4922:17424`), with a 32dp chevron inside the 48dp slot.
4. Add focused Compose/accessibility assertions for the logo semantics, reload description/bounds, and Back asset/target behavior.
5. Make the Registration illustration decorative in semantics (`contentDescription = null`) while preserving its approved 80dp visual geometry.
6. Rotate the approved Registration Back vector toward the left without changing the existing 48dp target or visual slot geometry; bind orientation to the source-level `rotationZ = 180f` evidence and the formal visual comparison. A bitmap capture assertion is not used because the connected Compose runner cannot capture this graphics-layer node reliably.
7. Replace or correct the runtime skyline asset only with bytes that reproduce the current approved composite silhouette; update the asset manifest/checksum evidence and preserve the 736 × 246 clipping contract.
8. Replace Login's centered/global-spacing composition with explicit approved vertical anchors while retaining adaptive scrolling for narrow/IME conditions; align the shared field/captcha geometry against the five-state comparison.
9. Add a Registration-specific horizontal work-type arrangement to `AppRadioGroup` without weakening its 48dp semantics targets, and verify that the action row remains visible in the approved 402 × 874 composition.

No API, token, persistence, navigation, drawer, or visual-baseline changes are authorized by this fix plan.

The Registration reference confirms an 80dp decorative illustration frame at `I4922:17286;4922:15640` (filled-state equivalent `I4922:17494;4922:17429`) and a 32dp chevron inside the 48dp Back slot. The fix must preserve these visual bounds while correcting semantics and orientation.

## Regression checks

- `testDebugUnitTest`
- `lintDebug`
- `assembleDebug`
- `assembleRelease`
- `assembleDebugAndroidTest`
- `connectedDebugAndroidTest` when an approved emulator is available
- current-revision Login empty/filled/auth-error and Registration empty/filled visual captures normalized against the requester-approved composite
- five-state numeric comparison must show the skyline silhouette, Login anchors/geometry, horizontal work-type row and visible action row corrected before formal re-verification
- release isolation scan

## Exit

After the authorized implementation fix and developer validation, return to Verification with a new committed revision. Re-evaluate every affected AC and retain `NOT VERIFIED` for any visual, runtime, or hosted-CI evidence that cannot be reproduced.
