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
| KCL-UI008-004 | Figma MCP confirms Android reference frame `402 x 874`. | SRC-UI008-001, 002 | resolved |

## Conflicts and Gaps
| Conflict ID | Description | Decision | Status |
|---|---|---|---|
| KCF-UI008-001 | iOS and Android references coexist. | Implement Android only. | resolved |
| KCF-UI008-002 | Final copy per Alert variant is not yet captured. | Verify with implementation screenshots. | open, non-blocking |

## Decision Records
### KD-UI008-001
- Decision: Establish Android delete-photo and discard-edit Alerts as local UI with Figma MCP dimensions as visual reference.
- Source / Conflict IDs: SRC-UI008-001–004; KCF-UI008-001, 002
- Rationale: Matches roadmap and Android-only design rule.
- Approver and date: Requester and product/design specifications, 2026-09-08.
- Revalidation trigger: Figma copy or Alert behavior changes.

## Unresolved Non-blocking Items
- Final Alert text copy per state.

## Blocking Items
- None for Knowledge Validation.

## Validation Handoff
- Candidate baseline: `KB-UI-008-ALERTS-R1`
- Required follow-up evidence: implementation screenshots and final copy check.
- Material blockers: no.
- Ready for Validation: yes
