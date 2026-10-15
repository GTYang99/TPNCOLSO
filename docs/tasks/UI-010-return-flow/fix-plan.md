# UI-010 Fix Plan — UI010-IMP-004

## Scope

Fix the stale saved-correction marker in UI-010 `AC-002` without changing the approved correction flow.

## Change

Set `correctionSaved = false` in `FakeReturnFlowStore.confirmCorrection()` with the transition to `已修正`. Preserve the confirmation guard, metadata clearing, edit-action removal, and returned-notification filtering.

## Validation Results

- Focused `FakeReturnFlowStoreTest`: PASS.
- Full `:app:testDebugUnitTest --no-daemon --rerun-tasks`: PASS, 51 tests, 0 failures/errors.
- Focused `ReturnFlowHostTest` connected suite: PASS, 3 tests, 0 failures/errors/skips on the approved API 36 emulator.
- `git show --check 60c26c1eb67cbdb8be62b99210da21647522fc2a`: PASS.

No new Plan Review was needed because the fix is an internal state reset already asserted by the approved UI-010 test and does not alter design or scope.
