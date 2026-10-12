# Implementation Plan

## Goal
- Connect the map's notification action to a local returned-parcel list and a complete, state-consistent correction flow.

## Scope
- Add the Android notification overlay, six fake returned records, matching parcel/detail/form state, the correction save-and-confirm flow, and route-level handoff.
- Preserve UI-006 form validation/photo behavior and UI-008 dirty-exit behavior; do not add API or notification read/pagination behavior.

## Affected Files
- `app/src/main/java/com/example/tp_ncolso_android/feature/notifications/NotificationsContract.kt` — notification item/state/event/effect contracts.
- `app/src/main/java/com/example/tp_ncolso_android/feature/notifications/NotificationsViewModel.kt` and `NotificationsScreen.kt` — list presentation and interactions.
- `app/src/main/java/com/example/tp_ncolso_android/feature/notifications/data/NotificationsDataSource.kt` and a fake return-flow store/adapter — replaceable six-row data and shared in-memory state.
- `feature/mapshell/MapShellScreen.kt` / `MapShellContract.kt` only as needed to expose the existing notification callback; `app/src/debug/java/com/example/tp_ncolso_android/AppEntry.kt` to mount/dismiss the list and route its parcel key to detail.
- `feature/parceldetail/ParcelDetailContract.kt`, `ParcelDetailViewModel.kt`, and `ParcelDetailScreen.kt` — saved-correction action, exact Android confirmation, confirmed status and return-banner/edit visibility.
- `feature/surveyform/SurveyFormContract.kt`, `SurveyFormViewModel.kt`, `SurveyFormScreen.kt`, and existing data-source interface/adapter as needed — pass the full return reason, retain saved values/photos, show correction-specific completion, and notify the host of a successful local save.
- Fake parcel/detail/survey data adapters and the focused `app/src/test/...` and `app/src/androidTest/...` files for notification state, row routing, confirmation cancel/confirm, and dirty exit.

## Implementation Steps
1. Define six notification fixtures with stable internal parcel keys and matching display land numbers/reasons; back notification, detail, and form ports with one app-session fake store.
2. Add the notification list screen/ViewModel using existing theme components, status chips, accessible row actions, scroll behavior, and a close action.
3. Wire the map notification callback in the debug host to open the overlay; selecting a row closes it and opens detail for that row's key.
4. Extend the returned detail/form handoff so the full reason remains visible during correction and saved edits update the same local parcel record without changing `退回` status.
5. Add the `確認修正` CTA only after successful local save; show the Figma-confirmed Android dialog and update status to `已修正` only from its confirm action. Preserve cancel, Back, and outside-dismiss outcomes.
6. On correction confirmation, hide the return banner and active edit entry, update the local returned list consistently, and show correction-success feedback.
7. Add focused unit and Compose host coverage for the six-row list, correct parcel routing, dialog transitions, retained edits, status update, and UI-008 dirty-exit regression.
8. Capture implementation screenshots for the notification list, returned detail, correction form, confirmation dialog, and corrected state; compare only the requested UI and record any Figma/product-authority differences.

## Test Plan
- Unit: notifications fixture/list state and selection; fake return-flow save/confirm transitions; existing survey form and detail ViewModel tests.
- Compose: notification list contents, semantics, close and row selection; map bell callback; return detail/form handoff; correction dialog cancel/confirm/dismiss; dirty-form continue/discard and clean exit.
- Local checks for Developer Validation: `:app:testDebugUnitTest`, `:app:compileDebugAndroidTestKotlin`, focused connected Compose tests on the available emulator, and a debug build/install if available. Record exact environment and results; do not report them as hosted CI.

## Regression Plan
- Preserve map search and mounted-map behavior, ordinary parcel detail, non-returned UI-006 submission, UI-008 delete/discard dialogs, camera return path, and current saved-state restoration.
- Confirm a corrected parcel is read-only for the investigator and does not reappear in the active returned-only list.

## Risks
- Inconsistent fake keys or separately constructed stores could lose edits or route to another parcel; create one store per signed-in debug host and pass stable keys through callbacks.
- Figma's corrected-state frame (`4950:14447`) visually shows an edit icon. The approved APP role model says corrected records are read-only, so no active edit action will be exposed after confirmation; this visual difference must be recorded.
- The Figma file's formal revision is unavailable; use the cited node screenshots/dimensions and avoid claiming formal revision parity.
- The current branch has two commits ahead of `origin/developer` and pre-existing UI008/UI010 documentation edits. Preserve them and stage only UI010 implementation artifacts if a later commit is authorized.

## Rollback Plan
- Revert only the focused UI-010 implementation files and UI-010 artifacts; leave existing branch history and pre-existing UI008/UI010 working-tree changes untouched.

## Current Behavior
- The map bell is a callback with no opened list. Returned detail can show a reason and open the shared form, but the form save is not connected to a shared local record or UI-010 confirmation/status transition.

## Expected Behavior
- A six-item local returned list opens the matching parcel and full reason; correction edits save locally, and the status remains `退回` until explicit Android confirmation changes it to `已修正`.
- UI-008 dirty-exit behavior remains intact, while API/read/pagination/backend semantics remain deferred.

## Acceptance Criteria Traceability
| AC | Implementation Step | Validation |
|---|---|---|
| AC-001 | 1–3 | Notification ViewModel/Compose test verifies six rows, labels, close and correct parcel-key routing; host test verifies full matching return reason. |
| AC-002 | 1, 4–6 | ViewModel and Compose host tests verify saved edits survive cancel, status changes only on confirm, and corrected state removes the return context/edit path. |
| AC-003 | 4, 7 | Existing UI-008 tests plus correction-route host tests verify continue/discard/clean-exit outcomes and restoration of values/photos. |

## Failure Behavior
- With deterministic local fixtures, the list is immediately available. A missing parcel key shows the existing detail error/retry UI; a failed local save keeps the form values and status `退回`. No network or backend error behavior is introduced.

## Security and Privacy
- No credentials, personal data, permissions, network calls, or durable storage are added. Fixtures remain deterministic test data in memory.

## Open Questions
- None for this baseline. Revisit API, read/pagination, or notification-retention behavior in the authorized integration task.
