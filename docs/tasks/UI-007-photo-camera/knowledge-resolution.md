# Knowledge Resolution

## Question and Scope
- Task question: 建立 `UI-007-photo-camera` candidate baseline。
- In scope: camera-only local capture, four 4:3 photos, timestamped tiles, delete/retake。
- Out of scope: album, upload API and server storage。

## Candidate Knowledge Baseline
- Baseline ID: `KB-UI-007-PHOTO-CAMERA-R1`
- Created date: 2026-09-08 (Asia/Taipei)
- Status: draft

## Material Claims and Traceability
| Claim ID | Statement | Sources | Status |
|---|---|---|---|
| KCL-UI007-001 | UI-007 owns camera-only local photo UI; no album selection. | SRC-UI007-002, 003, 005 | resolved |
| KCL-UI007-002 | Submit requires exactly four 4:3 photos; tiles show capture time and support delete/retake. | SRC-UI007-002–004 | resolved |
| KCL-UI007-003 | Figma MCP confirms screen `402 x 874`, group `354 x 260`, tile `168 x 126`, FAB `56 x 56`. | SRC-UI007-001 | resolved |
| KCL-UI007-004 | Upload API remains deferred. | Requester decision 2026-09-08 | resolved as deferred |

## Conflicts and Gaps
| Conflict ID | Description | Decision | Status |
|---|---|---|---|
| KCF-UI007-001 | Figma upload visuals versus camera-only rule. | Camera-only local capture. | resolved |
| KCF-UI007-002 | Upload contract absent. | Defer INT-003. | resolved as deferred |

## Decision Records
### KD-UI007-001
- Decision: Implement camera-only local capture, exactly four 4:3 photos, timestamped tiles and delete/retake; defer upload API.
- Source / Conflict IDs: SRC-UI007-001–005; KCF-UI007-001, 002
- Rationale: Matches product rules and UI-first roadmap.
- Approver and date: Requester and product specifications, 2026-09-08.
- Revalidation trigger: Camera or upload contract changes.

## Unresolved Non-blocking Items
- Android camera permission denial UI and production upload parameters.

## Blocking Items
- None for Knowledge Validation.

## Validation Handoff
- Candidate baseline: `KB-UI-007-PHOTO-CAMERA-R1`
- Required follow-up evidence: implementation screenshots and upload contract.
- Material blockers: no.
- Ready for Validation: yes
