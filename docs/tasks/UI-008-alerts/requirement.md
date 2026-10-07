# Requirement

## Background
- UI-006 already exposes photo deletion and close intents, but the photo action is immediate and closing the form discards the current ViewModel without confirmation.
- Product behavior is defined by parent `FR-014` / `AC-014` and `FR-015` / `AC-015`; visual copy and Android dialog layout come from Figma sections `3184:11543` and `3225:12771`.

## Goal
- Add the approved Android delete-photo and discard-unsaved-edit confirmations to the existing local survey form flow.

## Functional Requirements
- Deleting a photo requires an explicit confirmation. Cancel or dismiss preserves the photo; confirm deletes only the selected photo.
- Leaving a dirty form by the close control or Android Back requires an explicit decision. Continue editing preserves the current form; discard exits and restores the initial local record when reopened.
- Leaving a clean form closes immediately without showing a discard prompt.
- Use the exact copy and Android visual reference recorded in active baseline `KB-UI-008-ALERTS-R1`.

## Non-functional Requirements
- Dialogs remain modal, accessible, and usable with scalable text; controls expose their visible labels and preserve focus behavior.
- Keep the change local-only. Add no API, persistence, or speculative dialog framework.

## Acceptance Criteria
- `AC-001`: Selecting a photo's delete control shows the Android confirmation with title `刪除確認`, body `刪除後無法恢復。`, and actions `取消` and `刪除`; the photo remains until explicit confirmation.
- `AC-002`: Cancel, system Back, or outside dismissal of the delete dialog leaves the selected photo and form values unchanged. Confirm removes only that photo; when the form is below four photos, the existing camera entry is available.
- `AC-003`: With unsaved form or photo changes, either the close control or Android Back shows the discard dialog with title `是否捨棄未儲存的內容？`, body `如果現在離開，剛才編輯的內容將會遺失。`, and actions `繼續編輯` and `捨棄編輯`.
- `AC-004`: Choosing `繼續編輯` or dismissing the discard dialog keeps the form open with all current edits and photos unchanged. Choosing `捨棄編輯` exits; reopening the same record restores its initial values and photos.
- `AC-005`: With no unsaved changes, close and Android Back exit without showing the discard dialog. Both dialogs use the Android visual baseline (402×874 reference frame; 312dp dialog width; 20dp corners; Figma title/body typography and action emphasis) and remain operable at increased system text size with no clipped actions.

## Constraints
- Android only; no iOS alert, API call, submission behavior, or production data storage.
- UI-006 owns form values, dirty calculation and photos; UI-007 owns camera capture; UI-008 adds confirmation at existing UI-006 intents.
- Preserve the user's explicit instruction to work on the current `main` branch. Do not stage unrelated existing UI-006/UI-007 task-document edits.
- Use canonical baseline ID `KB-UI-008-ALERTS-R1`; no separate UI008 `v0.1` artifact is present.

## Open Questions
- None for the approved local UI scope.
