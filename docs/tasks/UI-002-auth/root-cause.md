# UI-002 Verification Failure Root Cause

## IMP-AUTH-016 — Residual composition failure after `db7dfbe`

### Reproduction and evidence

- Reviewed revision: `db7dfbe782d3537b5f618af2735964913ce580b7`.
- All five states are normalized with the approved app-content crop and the debug-only top-left mask in `numeric-diff-db7dfbe.json`.
- The failure is implementation, not CI: the same current captures show stable geometry differences in both Login and Register while local unit, lint, release, and emulator tests pass.

### Root cause analysis

1. Login still uses a manually approximated vertical stack. `LoginScreen.kt` uses a `151.dp` top padding followed by `35.dp`, `15.dp`, and `19.dp` spacers around components whose rendered bounds are not the approved anchor rectangles. This makes the header-to-form gap and lower action/button positions drift independently; changing one global offset cannot align all three Login states.
2. Login's skyline is now the correct bridge/Taipei-101 artwork, but it is rendered as a `402 × 239.dp` canvas bottom-aligned to the runtime viewport. The approved composite's visible skyline baseline is a clipped panel composition; the runtime canvas is therefore at a different visible y/edge crop after system-content normalization. The remaining skyline mismatch is placement/clipping, not asset identity.
3. Register's horizontal radio implementation fixed the previous orientation bug, but the parent `Column` still uses a global `24.dp` outer padding and `36.dp` spacing. The approved Register panel has a different top origin and inter-group rhythm, so the illustration, first field, work-type row, name field and action row are all displaced together. This is a parent geometry problem, not a radio-selection problem.
4. The numeric metrics are improvement signals only; no numeric acceptance threshold is defined in the approved requirements. The visual authority therefore correctly rejects the revision despite lower MAE/ratio values.

### Classification

`implementation_failure` → `debug`. No requirement or authority conflict is present. Hosted CI is a separate `NOT VERIFIED` limitation and does not explain the current FAIL.

### Minimum safe fix direction

- Replace spacer-driven Login placement with a measured anchor layout: fixed content column bounds, explicit child top positions, and a separate adaptive scroll container for constrained heights.
- Keep the composite-compatible skyline bytes, but anchor its visible baseline/crop to the normalized 402×874 panel rather than relying on the viewport's bottom alignment.
- Give Register its own approved vertical rhythm/top origin while preserving the horizontal radio group and 48dp targets; verify the action row against the filled panel after the parent geometry change.

## Scope

The current debug re-entry addresses `IMP-AUTH-016`, the visual-comparison failure for reviewed revision `db7dfbe782d3537b5f618af2735964913ce580b7` on branch `UI-002feat`. The `IMP-AUTH-015` section below is retained as historical analysis for the superseded `842e290` revision.

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
