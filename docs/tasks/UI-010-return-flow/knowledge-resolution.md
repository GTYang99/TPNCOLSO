# Knowledge Resolution

## Question and Scope
- Task question: 建立 `UI-010-return-flow` 退回通知／修正 candidate baseline。
- In scope: notification list, return reason banner, correction entry, confirmation and UI-006 handoff。
- Out of scope: read, pagination, production API and server synchronization。

## Candidate Knowledge Baseline
- Baseline ID: `KB-UI-010-RETURN-FLOW-R1`
- Created date: 2026-09-08 (Asia/Taipei)
- Status: draft

## Material Claims and Traceability
| Claim ID | Statement | Sources | Status |
|---|---|---|---|
| KCL-UI010-001 | UI-010 displays returned-land notifications, return reason and correction entry. | SRC-UI010-001–003 | resolved |
| KCL-UI010-002 | Correction reuses UI-006 editing/validation/photo/submit UI and adds return context. | SRC-UI010-003, 004; requester decision 2026-09-08 | resolved |
| KCL-UI010-003 | Read status, pagination, API contract and error handling are deferred. | SRC-UI010-004; requester decision 2026-09-08 | resolved as deferred |
| KCL-UI010-004 | Figma MCP confirms Android screen references at `402 x 874` where present. | SRC-UI010-001 | resolved |

## Conflicts and Gaps
| Conflict ID | Description | Decision | Status |
|---|---|---|---|
| KCF-UI010-001 | Notification API features were listed in broader requirements but requester defers read/pagination/API. | Build static/fake list and return context only. | resolved as deferred |
| KCF-UI010-002 | Correction form overlaps UI-006. | UI-010 owns return context; UI-006 owns shared form behavior. | resolved |

## Decision Records
### KD-UI010-001
- Decision: Establish UI-010 as the returned-notification and correction-context task. Use fake notification/parcel data, show return reason, and hand correction editing to UI-006. Do not implement read, pagination or API behavior yet.
- Source / Conflict IDs: SRC-UI010-001–004; KCF-UI010-001, 002
- Rationale: Matches requester scope and separates return context from shared survey form behavior.
- Approver and date: Requester, 2026-09-08.
- Revalidation trigger: API or notification feature decision changes.

## Unresolved Non-blocking Items
- Final API, read/pagination and error contracts.
- Final Alert/copy details for correction confirmation.

## Blocking Items
- None for Knowledge Validation.

## Validation Handoff
- Candidate baseline: `KB-UI-010-RETURN-FLOW-R1`
- Required follow-up evidence: implementation screenshots and later notification API contract.
- Material blockers: no.
- Ready for Validation: yes
