# Requirement

## Background
- UI-010 is the next UI-first task after UI-009. The approved baseline `KB-UI-010-RETURN-FLOW-R1` bounds it to returned notifications and correction context using local fake data.
- The map already exposes a notification action, and parcel detail can display a return reason, but the end-to-end notification-to-correction flow is not wired.

## Goal
- Provide the Android v0.1 returned-notification list and connect a selected parcel to its return reason, shared survey editor, and explicit correction confirmation.

## Functional Requirements
- Tapping the map notification action opens a dismissible list of six fake returned records. Each item shows its returned status, land number, and a concise return-reason preview.
- Selecting an item opens that same parcel's detail, shows the complete return reason prominently, and offers the existing UI-006 survey editor for correction.
- Saving edits returns to the parcel detail while the record remains `退回`; the user must then choose `確認修正` and confirm the Android dialog before status becomes `已修正`.
- The notification, parcel-detail, and survey-form fixtures share stable parcel identity and in-memory state so edits and confirmed status stay consistent during the app session.
- Reuse UI-006 form validation/photo behavior and UI-008 discard-edit behavior. Keep API, server sync, notification read state, and pagination out of scope.

## Non-functional Requirements
- Use Jetpack Compose, existing semantic theme tokens, accessible labels, and scrollable content that remains usable at supported Android widths and increased text sizes.
- Keep fake data replaceable and outside Composables; do not add network calls or persist fake state across process restarts.

## Acceptance Criteria
- **AC-001 (parent AC-012):** Opening the map notification action shows six returned entries with status, land number, and reason preview. Closing the list returns to the map. Selecting any entry opens the matching land detail and displays that record's complete return reason.
- **AC-002 (parent AC-013):** The returned detail can open the shared editable survey form. Saving valid edits returns to detail with status still `退回` and the `確認修正` action available. The Android dialog shows title `是否確認資料已修正？`, body `送出後將無法再編輯，請確認資料已修正完成。`, and actions `取消`／`確認`. Cancel keeps the status `退回` and saved edits. Only confirm changes the record to `已修正`, removes its return banner/edit entry, and shows correction-success feedback.
- **AC-003 (parent AC-014):** Leaving a dirty correction form through Back or close shows the existing discard dialog. `繼續編輯` and dismissal preserve edits and keep the form open; `捨棄編輯` restores the values/photos from form entry and returns to detail. A clean form exits without a discard prompt.

## Constraints
- Android-only v0.1 UI; use fake/local data and no production API, notification read state, pagination, backend error mapping, or server synchronization.
- UI-010 owns notification and return context. UI-006 remains the owner of shared form fields, validation, photos, and save behavior; UI-008 owns the established Android discard dialog behavior.
- Follow approved product role rules: an investigator cannot edit a record after it becomes `已修正`, even though the Figma corrected-state frame visually includes an edit icon.

## Open Questions
- None for the bounded UI-only baseline. Revalidate before adding notification/API semantics or changing the corrected-state permission rule.
