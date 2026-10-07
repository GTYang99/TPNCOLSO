# Knowledge Resolution

## Question and Scope
- Task question: 建立 Android 刪除提醒／編輯提醒 Alert candidate baseline。
- In scope: delete-photo and discard-unsaved-edit confirmations。
- Out of scope: iOS, API and business submission。

## Candidate Knowledge Baseline
- Baseline ID: `KB-UI-008-ALERTS-R1`
- Created date: 2026-09-08 (Asia/Taipei)
- Status: draft

## Material Claims and Traceability
| Claim ID | Statement | Sources | Status |
|---|---|---|---|
| KCL-UI008-001 | Android Alert is used for delete photo and discard edit; iOS is excluded. | SRC-UI008-001–004 | resolved |
| KCL-UI008-002 | Delete offers cancel/delete; only confirm removes the photo and enables retake. | SRC-UI008-003, 004 | resolved |
| KCL-UI008-003 | Discard offers continue/discard; continue preserves state and discard restores prior state. | SRC-UI008-003, 004 | resolved |
| KCL-UI008-004 | Figma MCP confirms Android reference frame `402 x 874` and the exact copy/action labels for both Android dialog variants. | SRC-UI008-001, 002; Figma MCP nodes `2997:4930`, `3225:12775` checked 2026-10-07 | resolved |

## Conflicts and Gaps
| Conflict ID | Description | Decision | Status |
|---|---|---|---|
| KCF-UI008-001 | iOS and Android references coexist. | Implement Android only. | resolved |
| KCF-UI008-002 | Initial Collection did not capture final copy per Android Alert variant. | Figma MCP returned the copy and action labels from Android dialog instances `2997:4930` and `3225:12775` on 2026-10-07. | resolved |

## Decision Records
### KD-UI008-001
- Decision: Establish Android delete-photo and discard-edit Alerts as local UI with Figma MCP dimensions as visual reference.
- Source / Conflict IDs: SRC-UI008-001–004; KCF-UI008-001, 002
- Rationale: Matches roadmap and Android-only design rule.
- Approver and date: Requester and product/design specifications, 2026-09-08.
- Revalidation trigger: Figma copy or Alert behavior changes.

### KD-UI008-002
- Decision: Use the exact Android dialog copy reproduced from the approved Figma nodes: delete `刪除確認` / `刪除後無法恢復。` / `取消` / `刪除`; discard `是否捨棄未儲存的內容？` / `如果現在離開，剛才編輯的內容將會遺失。` / `繼續編輯` / `捨棄編輯`.
- Source / Conflict IDs: SRC-UI008-001–002; KCF-UI008-002
- Rationale: Figma owns visible copy and both Android Basic dialog instances were reproduced through Figma MCP.
- Approver and date: Product/design authority is the approved Figma Copy; evidence retrieved 2026-10-07.
- Revalidation trigger: Any change to the referenced Figma nodes or product action semantics.

## Unresolved Non-blocking Items
- None.

## Blocking Items
- None for Knowledge Validation.

## Validation Handoff
- Candidate baseline: `KB-UI-008-ALERTS-R1`
- Required follow-up evidence: implementation screenshots and final copy check.
- Material blockers: no.
- Ready for Validation: yes
- Baseline naming note: Requester referred to a `v0.1` baseline, but no separate UI008 v0.1 artifact exists in the repository. Retain the traceable canonical candidate ID `KB-UI-008-ALERTS-R1`; do not invent a v0.1 ID.
