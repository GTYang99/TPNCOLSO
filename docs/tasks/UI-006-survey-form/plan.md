# Implementation Plan

## Goal
- 建立可由 UI-005 進入的本機 UI006 調查填報流程，完成欄位編輯、條件驗證、dirty state、四張照片管理與假送出狀態。

## Scope
- 新增 `feature.surveyform` 的 contract、pure validation、ViewModel、fake source、Compose screen 與測試。
- 只在 debug host 接入 UI005 callback；保留 UI007 camera 與 UI008 dialogs 的替換邊界。

## Affected Files
- `app/src/main/java/com/example/tp_ncolso_android/feature/surveyform/SurveyFormContract.kt` — immutable model, events/effects and state.
- `app/src/main/java/com/example/tp_ncolso_android/feature/surveyform/SurveyFormValidation.kt` — pure field/photo validation.
- `app/src/main/java/com/example/tp_ncolso_android/feature/surveyform/SurveyFormViewModel.kt` — state transitions, dirty tracking and duplicate-submit suppression.
- `app/src/main/java/com/example/tp_ncolso_android/feature/surveyform/SurveyFormScreen.kt` — Compose route and Figma-sized form/photo layout.
- `app/src/main/java/com/example/tp_ncolso_android/feature/surveyform/data/SurveyFormDataSource.kt` and `FakeSurveyFormDataSource.kt` — replaceable local source and deterministic fixtures.
- `app/src/debug/java/com/example/tp_ncolso_android/AppEntry.kt` — open UI006 from UI005 and return to the detail/search context.
- `app/src/test/java/com/example/tp_ncolso_android/feature/surveyform/*Test.kt` — validation and ViewModel behavior.
- `app/src/androidTest/java/com/example/tp_ncolso_android/feature/surveyform/SurveyFormScreenTest.kt` — Compose interaction and accessibility semantics.
- `docs/tasks/UI-006-survey-form/` — current requirement, analysis, review and execution evidence.

## Implementation Steps
1. Define survey form state, field values, photo model, submit state, events/effects and a source port without API/DTO types.
2. Implement pure validation for required fields, `無占用` clearing, integer household count and exactly-four 4:3 photos.
3. Implement the ViewModel reducer and fake source so every edit updates dirty state, capture/delete is deterministic, submit is single-flight, and success/error preserve local values.
4. Build the scrollable Compose form using existing foundation components, explicit test tags, 48dp semantics and the recorded Figma dimensions.
5. Wire the debug UI005 edit callback to UI006 and close/save intent callbacks without changing UI005 detail ownership.
6. Add unit and Compose tests for all task acceptance criteria and the UI005-to-UI006 handoff.

## Test Plan
- `:app:testDebugUnitTest` focused on `SurveyFormValidationTest` and `SurveyFormViewModelTest`.
- `:app:compileDebugAndroidTestKotlin` to verify Compose test sources.
- Connected `SurveyFormScreenTest` on the configured API 34 AVD for interaction, semantics, font/scroll smoke and UI005 entry.
- `:app:assembleDebug :app:assembleRelease :app:lint` and `git diff --check` after implementation.

## Regression Plan
- Run existing UI005, UI004 and UI003 connected tests to confirm map/search/detail overlays and the edit callback remain stable.
- Run existing unit tests and both debug/release compilation; ensure release source does not reference debug-only fake host code.
- Confirm closing UI006 returns to the prior detail/search context and does not alter read-only UI005 state.

## Risks
- Compose form height and keyboard insets can obscure the submit action; use a single scroll container and test tags that can be scrolled into view.
- Camera and confirmation dialogs are downstream; use explicit callback ports and avoid hidden local behavior that later conflicts with UI007/UI008.
- Figma visual evidence is partial; record visual limitation instead of claiming pixel parity.

## Rollback Plan
- Remove the feature package and debug host wiring as one revision; UI005 remains usable because its edit callback is optional.

## Current Behavior
- UI005 exposes an optional edit callback but no UI006 destination or editable survey state exists.
- Existing survey records are read-only and the repository has no survey form source, validation or photo state.

## Expected Behavior
- Selecting UI005's edit action opens UI006 with deterministic local form state and no production integration.
- Field edits, conditional occupation fields, four-photo rules, dirty state and fake submit/error states are observable and testable.

## Acceptance Criteria Traceability
| AC | Implementation Step | Validation |
|---|---|---|
| AC-UI006-001 | 1, 4, 5 | Compose field/read-only assertions and UI005 handoff test |
| AC-UI006-002 | 1, 3, 5 | ViewModel dirty/effect unit tests and close callback test |
| AC-UI006-003 | 2, 3, 4 | Reducer and Compose conditional visibility/clearing tests |
| AC-UI006-004 | 2, 3, 4 | Validation unit tests and field error Compose test |
| AC-UI006-005 | 1, 3, 4 | Photo count/aspect/delete/retake Compose and ViewModel tests |
| AC-UI006-006 | 2, 3, 4 | Submit validation and fake success state tests |
| AC-UI006-007 | 3, 4 | Single-flight and fake failure preservation tests |
| AC-UI006-008 | 4, 6 | Semantics/touch target/scroll smoke test on AVD |

## Failure Behavior
- Invalid fields or photos keep the form open, show field/photo-local errors and do not clear user input.
- Fake submit failure keeps all values/photos and exposes a retryable error state; no HTTP mapping is added.
- Close requests emit the current dirty state for UI008/host handling; the feature does not silently discard data.

## Security and Privacy
- No credentials, tokens, production payloads or unrestricted personal data are added. Local photo fixtures are synthetic and remain in fake state only.
- System-provided survey fields are read-only; no client-side authorization or production status transition is inferred.

## Open Questions
- None for this UI-only scope. API/upload and formal pixel-parity obligations remain deferred with the active baseline.
