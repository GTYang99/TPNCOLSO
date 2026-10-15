# UI-010 Root Cause Analysis — UI010-IMP-004

## Reproduction

- Base revision: `d74bcd8192e6ea6e2d06306867af74f198c20580`.
- Isolated branch: `codex/ui010-correction-state`.
- Command: `:app:testDebugUnitTest --tests com.example.tp_ncolso_android.feature.notifications.data.FakeReturnFlowStoreTest --no-daemon`.
- Result before fix: 3 tests, 1 failure at `FakeReturnFlowStoreTest.kt:76`; the assertion expected `correctionSaved == false` after confirmation.

## Root Cause

`submit()` sets `correctionSaved = true` for a saved correction awaiting confirmation. `confirmCorrection()` changed the record to `已修正` and cleared its return reason/edit action, but omitted resetting the pending marker. The confirmed record therefore carried contradictory state. This is an implementation defect in the local fake return-flow store and affects UI-010 `AC-002`.

## Fix

On committed source revision `60c26c1eb67cbdb8be62b99210da21647522fc2a`, `confirmCorrection()` now sets `correctionSaved = false` in the same detail copy as the corrected status and cleared return context. The guard and active-notification filtering are unchanged. The approved requirement, plan, and Plan Review remain applicable.
