# Implementation Plan

## Goal
- Add the approved Android delete-photo and discard-edit alerts to the existing local survey form, with correct cancel, confirm, Back, and state-restore behavior.

## Scope
- Add local Compose dialog presentation and intent handling to the survey form screen.
- Reset the debug host's remembered survey key after form exit so confirmed discard reloads the original local seed.
- Add focused Compose tests for delete and discard decisions, Android Back, and same-record reopen behavior.
- No API, persistence, iOS UI, submission changes, shared dialog framework, or unrelated refactoring.

## Affected Files
- `app/src/main/java/com/example/tp_ncolso_android/feature/surveyform/SurveyFormScreen.kt` — intercept delete/close requests, show the two Android dialogs, and route system Back through close handling when the form is foregrounded (`backEnabled`).
- `app/src/debug/java/com/example/tp_ncolso_android/AppEntry.kt` — clear `surveyKeyNo` on form exit and disable the form BackHandler while the UI-007 camera route is foregrounded.
- `app/src/androidTest/java/com/example/tp_ncolso_android/feature/surveyform/SurveyFormScreenTest.kt` — assert exact copy, cancel/confirm behavior, dirty/clean exits, Android Back, and state restoration on reopen.
- `docs/tasks/UI-008-alerts/` — approved task requirement, analysis, plan, execution evidence and verification.

## Implementation Steps
1. Add route-local pending-delete and pending-discard dialog state; keep UI-006 ViewModel as owner of form values, dirty state and photo mutations.
2. Intercept thumbnail deletion and dispatch the existing `DeletePhoto` event only after confirmation; preserve state for cancel, Back, and dialog dismissal.
3. Route close-control and Android Back intents through one handler: close immediately when clean, otherwise show the discard dialog; preserve current form on continue/dismiss and exit on discard. Enable the route BackHandler only while the form is foregrounded.
4. Pass `backEnabled = !cameraOpen` from the debug host; clear `surveyKeyNo` when the form exits so reopening the same key reloads the source seed rather than reusing discarded edits.
5. Match Figma's 312dp width, 20dp corner radius, 20sp/28sp title, 16sp/24sp body, 24dp content inset, blue cancel/continue actions, and error-colored destructive actions; allow height to grow with scaled text.
6. Add focused Compose tests for all UI008 AC and update task execution evidence without modifying unrelated dirty files.

## Test Plan
- `SurveyFormScreenTest`: delete dialog labels; cancel and dismissal preserve photo; confirm deletes the selected photo and restores camera entry; continue/dismiss preserves dirty form; discard closes and reopening the same key restores seed; clean close exits without a dialog.
- Exercise Android Back from the form and while each dialog is open; assert the same outcomes as the corresponding controls.
- Run focused `:app:connectedDebugAndroidTest` filter for `SurveyFormScreenTest`, plus `:app:testDebugUnitTest` for affected form state, followed by the smallest required build/regression checks under the task verification plan.
- Capture emulator screenshots of both dialogs at standard and increased font scale and compare the dialog geometry, copy, and action colors to the Figma references.

## Regression Plan
- Retain UI-006's existing photo list, dirty calculation, submit flow, and UI-007 camera callback behavior; while the camera is foregrounded, Back must not trigger the underlying form's discard flow.
- Run existing survey form and photo camera tests; smoke the UI005-to-UI006 debug host handoff and confirm Camera entry remains available after a confirmed delete.
- Inspect only UI008 implementation paths and do not stage existing modified UI-006/UI-007 task artifacts.

## Risks
- AlertDialog defaults can clip actions under increased text size; use scalable text and a height-flexible dialog, then validate at larger font scale.
- Dialog cancellation and form Back can compete for system Back; verify Dialog consumes Back while shown and route Back reaches the dirty guard while no dialog is shown.
- UI-006 remains composed under UI-007 camera; verify the form BackHandler is disabled while camera is foregrounded so it cannot close the survey form.
- The current branch is `main` with unrelated dirty task-document changes. The requester explicitly directed use of this branch; commits must include only UI008-owned files and the planned UI006 integration paths.

## Rollback Plan
- Revert the focused UI008 code and test changes, restoring immediate delete/close behavior without changing UI-006 form ownership or UI-007 capture implementation.

## Current Behavior
- Photo deletion is immediate. The form emits a close callback with `dirty`, but the debug host ignores it and closes immediately.
- The host remembers the survey ViewModel by parcel key, so simply closing and reopening the same key can retain edits.

## Expected Behavior
- Android delete confirmation gates the existing photo removal event, and the discard confirmation gates dirty form exit.
- Cancel/continue preserve the active screen state; confirmed delete removes one photo; confirmed discard exits and reloads the original seed on re-entry.

## Acceptance Criteria Traceability
| AC | Implementation Step | Validation |
|---|---|---|
| AC-001 | 1, 2, 5 | Compose test checks exact title/body/actions and that no photo is removed before confirmation. |
| AC-002 | 2 | Compose tests check cancel/dismiss preservation and confirmed one-photo delete/camera-entry restoration. |
| AC-003 | 1, 3, 5 | Compose tests open discard alert from close control and Android Back with exact copy. |
| AC-004 | 3, 4 | Compose test checks continue preservation and discard/reopen seed restoration. |
| AC-005 | 3, 5 | Clean-close/Back tests and emulator screenshot checks at standard and increased font scale. |

## Failure Behavior
- Dialog dismissal is equivalent to cancel/continue and must not mutate photos or discard form state.
- If dialog content cannot fit at increased font scale, the dialog grows within available height and allows content/actions to scroll or remain reachable; form values remain unchanged.

## Security and Privacy
- No new storage, permission, network request, logging, or production payload is introduced. Dialogs display no sensitive data.

## Open Questions
- None.
