# Knowledge Validation

## Inputs
- Collection artifact: `docs/tasks/UI-002-auth/knowledge-collection.md`
- Resolution artifact: `docs/tasks/UI-002-auth/knowledge-resolution.md`
- Candidate baseline ID: `KB-UI-002-AUTH-R10`
- Validator: Codex (same Agent; no independent validator available at Planning preparation)
- Validation date: 2026-09-07 (Asia/Taipei)
- Independence note: same-Agent limitation is recorded. The current requester PNG was inspected directly and bound to exact dimensions, color profile and SHA-256; requester answers, normalized requirements and product rules were cross-checked.

## Validation Checklist

| Check | Result | Evidence | Finding / Route |
|---|---|---|---|
| Collection scope and source coverage | PASS | User decisions, composite, product, design, API, repository, roadmap and tests are registered. | None |
| Source identity and version | PASS | SRC-015 is PNG 7904×2916, sRGB, SHA-256 `75cb578c58f3098c270290f033539ebf06c35c8b322efb71526fa4e53f3d3e9c`. | Any byte or approval-scope change triggers revalidation. |
| Domain authority | PASS | Requester owns visual/product decisions; repository/process/API sources remain limited to their domains. | None |
| Freshness and supersession | PASS | Requester explicitly made SRC-015 the replacement source for all Login／Register／Logout visuals. SRC-002 and SRC-014 are marked superseded, not silently deleted. | None |
| Claim-to-source traceability | PASS | KCL-AUTH-001–015 and KD-AUTH-012 connect the composite and six requester decisions to the three detailed requirements. | None |
| Requirement / AC traceability | PASS | UIR-LOGIN-001–010, UIR-REG-001–011 and UIR-LOGOUT-001–006 map to AC-UI002-001–011. | Drawer visual acceptance remains UI-003-only. |
| Conflict resolution and approval | PASS | Composite sole authority, outer-chrome exclusion, Register null-required work type, plaintext/no-eye passwords and immediate Logout are explicit requester decisions. | Prior plan/review approvals are stale. |
| Assumption safety and expiry | PASS | Remaining assumptions concern replaceable component names, fake vendor data and asset handoff only. | None |
| Baseline completeness and consistency | PASS | Three Login panels, two Register panels, derived states, Logout boundary, responsive rules, sensitive-state controls and task ownership are explicit. | Exact candidate assets require composite comparison at Implementation gate. |
| Downstream impact and revalidation triggers | PASS | UI-001 component variants, UI-003 drawer handoff and INT-001 remote logout are identified. | None |
| Planning handoff readiness | PASS | No material product/UI choice remains open for Planning. | Production implementation still waits for UI-001 completion. |

## Findings

| Finding ID | Category | Severity | Evidence | Impact | Owner | Route | Status |
|---|---|---|---|---|---|---|---|
| KV-AUTH-008 | source supersession | Major | SRC-015 plus requester answers | R9, Login requirement revision 2, Registration Figma authority and plan revision 12 cannot authorize implementation. | Requester / Planning | knowledge_resolution → planning | resolved by KD-AUTH-012 and plan revision 13; iteration 12 review required |
| KV-AUTH-009 | behavior decision | Major | Requester answers 2, 3 and 5 | Register and Logout would be observably wrong under the old default/masking/session assumptions. | Requester / Planning | knowledge_resolution → planning | resolved in R10 requirements and plan |
| KV-AUTH-010 | ownership decision | Major | Requester answers 4 and 6 | Drawer must not be duplicated in UI-002. | UI-002 / UI-003 | planning | resolved by Logout interface contract and explicit scope exclusion |

## Baseline Decision
- Result: PASS
- Active baseline ID: `KB-UI-002-AUTH-R10`
- Supersedes: `KB-UI-002-AUTH-R9`
- Activation reason: SRC-015 has immutable identity and current requester authority; every visual and behavior delta is normalized without inventing API or drawer behavior.

## Blocking Items
- None for Planning.
- `UI-001-foundation` must complete its source-change Knowledge Resolution, revised plan/review, implementation and verified handoff before UI-002 production implementation.

## Planning Handoff
- Ready for Planning: yes
- Decisions and constraints to cite: KD-AUTH-001–012; SRC-015 identity and outer-chrome exclusion; all UIR-LOGIN／REG／LOGOUT criteria; Android 9+; Compose MVVM; debug/test-only fake sources; UI-003 drawer ownership; INT-001 remote/API ownership.
- Required follow-up evidence: UI-001 reusable component/token/asset catalog; exact archived composite and panel crops; formal auth schema for INT-001.
- Revalidation triggers: UI-001 output, SRC-015 bytes/approval scope, Register privacy behavior, Logout behavior, auth API schema or repository/navigation architecture change.
- Next action: planning
