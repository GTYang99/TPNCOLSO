# Knowledge Validation

## Inputs

- Collection artifact: `docs/tasks/UI-003-map-shell/knowledge-collection.md`
- Resolution artifact: `docs/tasks/UI-003-map-shell/knowledge-resolution.md`
- Candidate baseline ID: `KB-UI-003-MAP-SHELL-R1`
- Validator: Codex (same agent; no independent validator available)
- Validation date: 2026-09-28 (Asia/Taipei)
- Independence note: Validation reproduced the cited local product, design, architecture and task-boundary evidence. No production code or candidate-baseline content was modified during Validation.

## Validation Checklist

| Check | Result | Evidence | Finding / Route |
|---|---|---|---|
| Collection scope and source coverage | PASS | The Collection covers the requester source index and visual source, structured Figma reference, product/interface rules, task roadmap, foundation contract, UI-002 logout handoff, and the explicit API/design limitations. It separately records missing implementation/build evidence as downstream work. | No Knowledge collection blocker. |
| Source identity and version | PASS | `SRC-UI003-001` records the requester PNG dimensions and SHA-256; `SRC-UI003-002` identifies Figma file `HRbRsw6HoNBUCtaieX8xUM`, node `4952:18205`; product and task documents are bound to the current working tree and dated source register. | Exact WMTS layer parameters remain intentionally unversioned and are not invented by this baseline. |
| Domain authority | PASS | Product interface rules own basemap services and map behavior; design specification owns visible component composition; the roadmap owns task boundaries; `logout-interface-requirement.md` owns the UI-002/UI-003/INT-001 logout split. | No authority conflict remains for the UI shell baseline. |
| Freshness and supersession | PASS | Resolution was re-collected against `tp_req_ui.md` and records the requester source index as current. The terrain screenshot is explicitly superseded for tile-content authority by the product WMTS rule. | Revalidate if the Figma node, basemap list, WMTS source, or task ownership changes. |
| Claim-to-source traceability | PASS | `KCL-UI003-001–010` cite the source register; direct source inspection confirms the three-basemap/default rule, overlay preservation, UI-003 drawer ownership, UI-002 clean Login destination, and INT-001 remote logout boundary. | Screenshot claims remain limited to visible UI observations. |
| Requirement / AC traceability | PASS | The resolution maps the candidate claims to the approved UI-003 requirement and numbered ACs covering map shell, top affordances, basemap state, overlay preservation, drawer/logout event and location states. | Real WMTS/API behavior remains out of scope. |
| Conflict resolution and approval | PASS | Terrain placeholder versus WMTS authority, logout ownership, search/result ownership, and missing location-state visuals are resolved in `KD-UI003-001–008`; unresolved WMTS parameters are retained as an explicit integration gap. | No requirement clarification is needed for Knowledge entry. |
| Assumption safety and expiry | PASS | `KA-UI003-001–003` are limited to visual-source correspondence, fake/static overlay validation, and a planning-stage map SDK spike. Each has an owner, validation method, and expiry/revalidation trigger. | Assumptions are safe for Planning, not for production API integration. |
| Baseline completeness and consistency | PASS | `KB-UI-003-MAP-SHELL-R1` defines scope, authority, ownership, exclusions, visible states, fake-data boundary, WMTS gap, downstream impact, and revalidation triggers. | UI-001/UI-002 readiness and implementation evidence are downstream prerequisites, not defects in this Knowledge baseline. |
| Downstream impact and revalidation triggers | PASS | Resolution explicitly requires structured/archived visual evidence, a map SDK/tile-support spike, UI-001/UI-002 handoff checks, and later WMTS parameter confirmation before implementation/integration claims. | Planning must carry these follow-ups forward. |
| Planning handoff readiness | PASS | The baseline is coherent for Planning, while the missing child requirement/plan and implementation evidence are correctly routed to Planning and later Implementation/Verification. | Route to `planning`. |

## Findings

| Finding ID | Category | Severity | Evidence | Impact | Owner | Route | Status |
|---|---|---|---|---|---|---|---|
| KV-UI003-001 | planning | P1 | Child `requirement.md` and `plan.md` are now present and Plan Review iteration 1 is approved. | No remaining entry gap for the approved UI-only implementation scope. | UI-003 Planning | implementation | resolved |
| KV-UI003-002 | environment / dependency | P1 | `docs/tasks/UI-001-foundation/state.yaml` is at Verification with Release pending, and `docs/tasks/UI-002-auth/state.yaml` remains `NOT VERIFIED`; Resolution records both as authenticated-shell prerequisites. | UI-003 implementation and the real UI-002 logout integration remain blocked until the prerequisite handoffs are complete. | UI-001 / UI-002 owners | planning / verification | open |
| KV-UI003-003 | integration | P2 | WMTS endpoint is recorded, but layer, matrix-set, attribution, caching and offline parameters are not specified. | Blocks real tile integration only; it does not block the UI-only Knowledge baseline or fake-overlay planning. | Product / INT-002 | planning / knowledge_collection | open |

## Baseline Decision

- Result: PASS
- Active baseline ID: `KB-UI-003-MAP-SHELL-R1`
- Supersedes: None
- Activation or rejection reason: Source authority, freshness, claim traceability, conflict decisions, assumption safety, baseline completeness and downstream handoff are validated. The open items are explicitly downstream planning/integration work and do not invalidate the UI-only Knowledge baseline.

## Blocking Items

- None for Knowledge Validation or the approved UI-only implementation entry.
- UI-001 release/handoff and UI-002 verification remain prerequisites for closing the real cross-task logout integration and downstream UI-002 AC-UI002-006/011.
- Missing WMTS request parameters block real map-tile integration, not the UI shell baseline.

## Planning Handoff

- Ready for Planning: completed; implementation entered under the approved UI-only scope.
- Decisions and constraints to cite: `KD-UI003-001–008`; `KCL-UI003-001–010`; UI-002 logout ownership requirement revision 1; electronic map default; three mutually exclusive basemap choices; overlay preservation; screenshot/Figma visible-UI-only authority; no invented production DTOs or WMTS parameters.
- Required follow-up evidence: child requirement with numbered observable AC, structured or archived Figma evidence for node `4952:18205`, map SDK/tile-support spike, fake overlay fixtures, accessibility/state tests, and prerequisite UI-001/UI-002 handoff status.
- Revalidation triggers: Figma/source approval changes, product basemap/WMTS changes, AppRoot/session contract changes, UI-002 logout contract changes, or SDK limitations.
- Next action: implementation
