# UI-002 Verification Failure Root Cause

## Scope

This debug re-entry addresses `IMP-AUTH-015`, the visual-comparison failure for reviewed revision `842e29061238527d21ee51e29a61126ba9cbb32` on branch `UI-002feat`.

## Reproducible findings

1. The earlier Login and Registration semantics findings are fixed: logo/illustration are decorative, reload has the exact description/effective target, and the Back glyph is rotated left.
2. `LoginScreen.kt:60–68` renders `login_city_skyline.png`, whose silhouette contains the pagoda/ferris-wheel composition visible in the runtime capture rather than the approved composite's bridge/Taipei-101 skyline. The asset/checksum handoff was treated as approved without revalidating its silhouette against the current composite authority; offset and blend mode cannot repair an asset-content mismatch.
3. `LoginScreen.kt:69–75` centers a scrollable 320dp column and applies one global `Arrangement.spacedBy(24.dp)`, while the approved Login contract defines explicit header, field, auxiliary-row and button anchors. This parent layout model causes the repeated logo/form/button placement and field/captcha geometry deltas across all three Login states.
4. `AppRadioGroup` (`FoundationComponents.kt:280–333`) places every option in a full-width 48dp child inside a `Column`. `RegisterScreen.kt:59` uses that default directly, so `外業人員` and `內業人員` render vertically instead of the approved horizontal work-type row.
5. The Register action row is horizontally implemented, but the vertical radio layout adds a full 48dp option row plus spacing. That upstream height expansion pushes the action row below the 874dp comparison viewport, producing the observed bottom composition/clipping mismatch; the action buttons are a downstream symptom, not the primary cause.

## Classification

These are implementation failures. The requirements and visual authority are approved and unambiguous; no requirement reduction or design reinterpretation is needed. Missing hosted CI remains a separate evidence limitation and is not the cause of the visual FAIL.

## Evidence boundary

Figma file `HRbRsw6HoNBUCtaieX8xUM`, nodes `2905:2680`, `2905:2679`, and `2997:11541`, was inspected on 2026-09-23 as supporting visual evidence. The requester-approved composite PNG remains the primary visual authority. The five-state comparison is bound by `composite-panel-comparison-842e290.json`, with system chrome and debug-only chrome excluded; the findings above combine that numeric evidence with source inspection.

The Registration reference frames `4922:17227` and `4922:17493` expose a horizontal work-type row and an action row that remains visible in the 402 × 874 panel. The Login requirement defines explicit vertical anchors and the approved skyline silhouette. These bounds define the planned visual correction without changing product behavior or authority.

## Debug exit condition

The minimum fix scope and regression checks are recorded in `fix-plan.md`. Production code remains unchanged during this debug analysis.
