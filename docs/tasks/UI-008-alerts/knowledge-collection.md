# Knowledge Collection

## Question and Scope
- Task question: 建立 `UI-008-alerts` 刪除提醒／編輯提醒 Android Alert baseline。
- In scope: 刪除照片確認、取消／刪除、未儲存編輯確認、繼續編輯／捨棄編輯。
- Out of scope: iOS Alert、API、資料送出與退回通知。
- Collection date: 2026-09-08 (Asia/Taipei)
- Collector: Codex Knowledge Collection and Resolution Agent

## Source Register
| Source ID | Location | Authority / Scope | Notes |
|---|---|---|---|
| SRC-UI008-001 | Figma `3184:11543`, page `UI` | Delete Alert | Four Android/iOS flow frames; Android frames are `402 x 874`. |
| SRC-UI008-002 | Figma `3225:12771`, page `UI` | Edit/discard Alert | Android discard-edit flow; reference frames `402 x 874` where applicable. |
| SRC-UI008-003 | `docs/design/app-ui-requirements.md` | `DLG-DELETE-01`, `DLG-DISCARD-01`, Android-only | iOS is excluded from implementation. |
| SRC-UI008-004 | Parent requirement FR-014/015, AC-014/015 | Product behavior | Confirm/cancel and state restore rules. |

## Missing or Inaccessible Sources
- Final copy for every Alert variant requires implementation screenshot verification.

## Initial Conflicts and Gaps
- iOS and Android references coexist; Android is authoritative for this task.

## Handoff to Resolution
- Coverage sufficient: yes
- Recommended next action: knowledge_resolution
