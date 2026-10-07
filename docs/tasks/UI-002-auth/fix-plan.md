# UI-002 Verification Fix Plan

## Current Debug Fix Plan — IMP-AUTH-021 / revision `b193a4a`

### Fix scope

1. Make the Registration work-type group responsive to available width. Keep the approved horizontal row at the 402×874 baseline and wider widths; use a width-aware fallback only when both labeled options cannot fit within the required 24dp side padding.
2. Preserve two labeled options, 48dp effective targets, exactly-one selection, `作業性質` semantics/error behavior, and the current `WorkTypeSelected` events. Do not change the approved composite, field order, action buttons, or IME inset behavior.
3. Add focused narrow-width evidence/assertions proving that both `外業人員` and `內業人員` remain visible and represented in the UI hierarchy. Keep the existing baseline visual comparison to ensure the fallback does not alter the 402×874 horizontal composition.

### Regression checks for this fix

- focused Register Compose semantics test at a narrow width: both work-type labels and both 48dp targets exist;
- `testDebugUnitTest`, `lintDebug`, `assembleDebug`, `assembleRelease`, and `assembleDebugAndroidTest`;
- `connectedDebugAndroidTest` on the approved API 34 emulator;
- current 402×874 Register empty/filled visual captures;
- 720×2400 Register capture, font scale 1.3 capture, and real-IME scroll/recovery capture;
- release isolation scan and existing logout/coordinator regression tests.

### Authorization boundary

This is an implementation-debug re-entry within the approved `UIR-REG-011` responsive requirement. No requirement, visual authority, API, persistence, navigation, or UI-003 ownership change is proposed. Production code remains unchanged until the implementation-debug step is explicitly executed.

## Implementation result — revision `48670ff`

The approved debug scope was executed and committed as `48670ffe9e33b19aea23709abf2f47cc5ed85ff1`. `AppRadioGroup` now uses a width-aware fallback: the approved horizontal row remains in place when the labeled options fit, while narrow content stacks both options without changing their labels, targets, selection semantics, or events. The focused narrow-width assertion, full build/lint/APK validation, and 29/29 API 34 connected regression pass. Runtime captures at 720×2400, font scale 1.3, and real IME conditions confirm both labels and actions remain available; the subsequent current-revision Login responsive matrix also passes locally. Formal Verification is re-entered with the remaining cross-task and hosted-CI evidence gaps preserved.

## Reviewed failure

Fix only the approved-requirement mismatches recorded as `IMP-AUTH-016` for revision `db7dfbe782d3537b5f618af2735964913ce580b7`.

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
10. Replace the new spacer-driven Login geometry with explicit measured anchor placement; retain scrolling only as an adaptive fallback and keep the skyline's composite-compatible artwork while correcting its visible baseline/crop.
11. Replace Register's one-size-fits-all `36dp` rhythm with the approved Register-specific top origin and group spacing so the illustration, fields, work-type row, name field and action row share the reference y-coordinates.

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
- five-state visual comparison must show the skyline baseline/crop, Login anchors/geometry, Register vertical rhythm, horizontal work-type row and visible action row corrected before formal re-verification
- release isolation scan

## Exit

After the authorized implementation fix and developer validation, return to Verification with a new committed revision. Re-evaluate every affected AC and retain `NOT VERIFIED` for any visual, runtime, or hosted-CI evidence that cannot be reproduced.
