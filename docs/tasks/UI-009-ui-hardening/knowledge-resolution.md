# Knowledge Resolution

## Question and Scope
- Task question: 建立跨畫面品質與 Android 相容性 candidate baseline。
- In scope: layout, text scaling, IME, accessibility, rotation and state restoration regression。
- Out of scope: product behavior/API changes。

## Candidate Knowledge Baseline
- Baseline ID: `KB-UI-009-UI-HARDENING-R1`
- Created date: 2026-09-08 (Asia/Taipei)
- Status: draft

## Material Claims and Traceability
| Claim ID | Statement | Sources | Status |
|---|---|---|---|
| KCL-UI009-001 | All UI must support Android 9+, safe areas, scalable text and IME without clipping/overlap. | SRC-UI009-001, 002 | resolved |
| KCL-UI009-002 | Icon actions need content descriptions and interactive targets at least 48dp. | SRC-UI009-001 | resolved |
| KCL-UI009-003 | UI-009 is a cross-screen regression task, not a new Figma screen. | SRC-UI009-002, 003 | resolved |

## Conflicts and Gaps
| Conflict ID | Description | Decision | Status |
|---|---|---|---|
| KCF-UI009-001 | No single Figma frame covers hardening. | Validate against each existing screen baseline and common rules. | resolved |

## Decision Records
### KD-UI009-001
- Decision: Use the existing UI-002–UI-008 Figma dimensions and design accessibility rules as cross-screen regression evidence; do not create a new product screen.
- Source / Conflict IDs: SRC-UI009-001–003; KCF-UI009-001
- Rationale: Hardening concerns runtime quality across all screens.
- Approver and date: Roadmap and design specification, 2026-09-08.
- Revalidation trigger: Android target or common design rule changes.

## Unresolved Non-blocking Items
- Device matrix and exact test coverage to be finalized in Planning.

## Blocking Items
- None for Knowledge Validation.

## Validation Handoff
- Candidate baseline: `KB-UI-009-UI-HARDENING-R1`
- Required follow-up evidence: multi-device screenshot and accessibility tests.
- Material blockers: no.
- Ready for Validation: yes
