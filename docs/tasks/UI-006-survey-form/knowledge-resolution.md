# Knowledge Resolution

## Question and Scope
- Task question: 建立 `UI-006-survey-form` 調查填報 candidate baseline。
- In scope: 表單欄位、條件驗證、照片與送出 UI、Figma 元件尺寸、UI-005 handoff。
- Out of scope: production API／照片上傳、WMTS、通知、其他角色流程。

## Candidate Knowledge Baseline
- Baseline ID: `KB-UI-006-SURVEY-FORM-R1`
- Created date: 2026-09-08 (Asia/Taipei)
- Supersedes: None
- Status: validated

## Material Claims and Traceability
| Claim ID | Statement | Supporting Sources | Contradicting Sources | Authority / Confidence | Requirement / AC | Status |
|---|---|---|---|---|---|---|
| KCL-UI006-001 | UI-006 owns survey form editing, validation, photo management and submit UI shared with UI-005 entry. | SRC-UI006-004, 006; requester decision 2026-09-08 | None | Product / high | FR-008–FR-011 | resolved |
| KCL-UI006-002 | The form has 10 survey fields; system-provided fields remain read-only and user fields follow the data specification. | SRC-UI006-004, 005 | None | Product / high | FR-008, AC-008 | resolved |
| KCL-UI006-003 | Selecting 無占用 hides and clears occupation type, household count and address; otherwise those fields are required and count is an integer >= 1. | SRC-UI006-004, 005 | None | Product / high | FR-009, AC-009 | resolved |
| KCL-UI006-004 | Photos are camera-only, exactly four 4:3 photos are required for submit, thumbnails show capture time, and deletion enables retake. | SRC-UI006-003, 004, 005 | None | Product + Design / high | FR-010, AC-010 | resolved |
| KCL-UI006-005 | Figma MCP confirms a `402 x 874` Android screen, `354 x 964` inner form, `354 x 42` tabs, `354 x 56` standard radio rows, `168 x 126` photo tiles and `56 x 56` FAB. | SRC-UI006-002 | None | Design / high | visual baseline | resolved |
| KCL-UI006-006 | API interfaces, parameters, upload protocol and error contracts remain deferred. | Requester decision 2026-09-08 | None | Integration / high | deferred scope | resolved as deferred |

## Conflicts and Gaps
| Conflict ID | Claims / Sources | Domain | Impact / Severity | Options | Decision Owner | Status |
|---|---|---|---|---|---|---|
| KCF-UI006-001 | API and upload contracts are not available. | API | P2 for integration only | Use local/fake source and defer API | Requester / Integration | resolved as deferred |
| KCF-UI006-002 | UI-005 and UI-006 share field functions. | Task boundary | P1 if duplicated | Put shared implementation in UI-006; UI-005 owns detail and return reason | Requester | resolved |

## Decision Records
### KD-UI006-001
- Decision: UI-006 is the single owner of shared field editing, validation, photo and submit functions. UI-005 only displays detail and return-reason category／explanation and enters UI-006.
- Alternatives: Duplicate behavior in UI-005; wait for API; move editing into UI-005.
- Source / Conflict IDs: requester decision 2026-09-08; KCF-UI006-001, 002
- Rationale: Provides one consistent form behavior and preserves the UI-first API boundary.
- Approver and date: Requester, 2026-09-08.
- Revalidation trigger: ownership or API contract changes.

### KD-UI006-002
- Decision: Use Figma MCP node `2938:193` as the visual source. Implement the confirmed dimensions and component hierarchy; use product data rules for behavior and state transitions.
- Source / Conflict IDs: SRC-UI006-002, 003, 004, 005
- Rationale: Figma supplies visible structure and sizes while product/data specifications own field semantics.
- Approver and date: Requester instruction and MCP inspection, 2026-09-08.
- Revalidation trigger: Figma or product field-rule changes.

## Assumptions
| Assumption ID | Statement / Evidence | Confidence | Impact if Wrong | Owner | Validation Method | Expiry / Trigger | Safe for Planning |
|---|---|---|---|---|---|---|---|
| KA-UI006-001 | Local/fake photo references can verify four-photo UI without upload API. | high | Integration wiring changes later. | Planning | UI tests with local URI fixtures. | API contract supplied | yes |

## Resolved Items
- UI-006 owns shared editing, validation, photos and submit.
- Figma MCP dimensions and component hierarchy are recorded.
- API and upload behavior remain deferred.

## Unresolved Non-blocking Items
- Exact API parameters, upload protocol and error mapping.
- Pixel evidence for every Figma state variant beyond the inspected component tree.

## Blocking Items
- None for Knowledge Validation.
- UI-005 handoff blocks implementation sequencing.

## Downstream Impact
- Requirements / AC: Cover form states, conditional clearing, four-photo rule and submit retention.
- Plan / design: Use Figma-confirmed sizes and local/fake data.
- Code / data / API: No production DTO, endpoint or upload protocol.
- Tests / verification: Cover field validation, conditional visibility, photo count/aspect, delete/retake and submit states.
- Release / operations: No integration or release claim.

## Revalidation Triggers
- Figma node `2938:193`, product field rules, UI-005 ownership or future API contracts change.

## Validation Handoff
- Candidate baseline: `KB-UI-006-SURVEY-FORM-R1`
- Decisions and constraints Validation must check: KD-UI006-001, KD-UI006-002, four-photo rule and deferred API scope.
- Safe assumptions: KA-UI006-001.
- Required follow-up evidence: per-state Figma captures and later API contract.
- Material blockers: no.
- Ready for Validation: yes
