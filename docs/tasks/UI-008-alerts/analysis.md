# Repository Analysis

## Current Behavior
- `SurveyFormScreen` sends `DeletePhoto` immediately from the thumbnail control.
- `SurveyFormViewModel` reports close intent and its dirty flag; `AppEntry` closes the route regardless of the flag.
- `AppEntry` remembers the survey ViewModel by identity and parcel key. Reopening the same key reuses the prior state unless the key is cleared.

## Expected Behavior
- Keep photo deletion behind an Android confirmation; only confirmation dispatches the existing `DeletePhoto` event.
- Keep form close handling in the existing screen route: clean close exits, dirty close opens the discard confirmation, and system Back uses the same path.
- On confirmed discard, close the route and clear its remembered parcel key so a future open reloads the data source's initial record.

## Affected Modules
- `feature.surveyform`: dialog state and presentation, photo-delete request interception, close/Back handling, and Compose tests.
- Debug host: reset the survey key after confirmed/clean exit so the local form reloads its seed.
- `docs/tasks/UI-008-alerts/`: requirement, analysis, plan, execution and verification evidence.

## Dependencies
- Active Knowledge baseline `KB-UI-008-ALERTS-R1` and parent UI requirement `FR-014` / `AC-014`, `FR-015` / `AC-015`.
- Existing Compose Material 3, `AppThemeTokens`, UI-006 dirty state/events, and UI-007's existing photo delete/retake state behavior.

## Risks
- Reusing the remembered ViewModel after discard would make edits appear to persist; clearing `surveyKeyNo` at exit and testing reopen behavior addresses this.
- Material 3 defaults may differ from Figma; set dialog shape/width, text styles, and action colors to the measured reference, then inspect emulator screenshots.
- Current branch is `main`, and the working tree already contains unrelated edits in UI-006/UI-007 verification and state artifacts. The requester explicitly authorized work on this branch; keep those paths untouched and stage only UI-008 scope.

## Unknowns
- None affecting implementation. Figma confirms exact dialog copy, dimensions and destructive action emphasis.
