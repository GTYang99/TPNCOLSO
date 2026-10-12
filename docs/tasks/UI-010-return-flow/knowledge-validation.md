# Knowledge Validation

## Inputs
- Requirement artifact: parent `docs/tasks/UI-0907-app-ui-requirements/requirement.md` (`FR-012`–`FR-014`, `AC-012`–`AC-014`); UI-010 has no separate task requirement artifact.
- Collection artifact: `knowledge-collection.md`
- Resolution artifact: `knowledge-resolution.md`
- Candidate baseline ID: `KB-UI-010-RETURN-FLOW-R1`
- Validator: Codex
- Validation date: 2026-10-08 (Asia/Taipei)
- Independence note: The same agent performed Collection, Resolution and Validation; independent review was not available.

## Validation Checklist

| Check | Result | Evidence | Finding / Route |
|---|---|---|---|
| Collection scope and source coverage | PASS | The current Collection registers the UI-010 Figma section `3327:13251`, the exact correction dialog node `4990:8053`, the design component rules, parent FR/AC and requester/API-scope decisions. | None. |
| Source identity and version | PASS with limitation | Figma MCP inspection on 2026-10-08 identified `3327:13251` as the UI-010 return-flow section, its Android reference frames as `402 x 874`, and `4990:8053` as the correction dialog (`312 x 204`). | Formal Figma file revision metadata is unavailable; formal revision/pixel-parity claims are not made. Revalidate if formal design approval becomes required. |
| Domain authority | PASS | Product behavior is traced to the parent requirement; visible return-flow and dialog geometry are traced to Figma/design sources; requester decisions own UI-only scope and API/read/pagination deferrals. | None. |
| Freshness and supersession | PASS | Collection and Resolution were updated 2026-10-08 after fresh Figma MCP inspection. The previously unresolved correction-confirmation copy is no longer active in Resolution. | Revalidate when the cited Figma nodes, copy or API scope changes. |
| Claim-to-source traceability | PASS | `KCL-UI010-001`–`KCL-UI010-005` map to the registered Figma nodes, design catalog, parent FR/AC and requester decisions. The exact title, body, actions and dialog bounds are directly observed at `4990:8053`. | None. |
| Requirement / AC traceability | PASS | `FR-012`/`AC-012` cover the returned notification list, land entry and full reason; `FR-013`/`AC-013` cover correction and confirmation; `FR-014`/`AC-014` cover discard editing behavior. | Runtime implementation evidence belongs to Planning/Developer Validation. |
| Conflict resolution and approval | PASS | `KCF-UI010-001` resolves the broader notification API scope as fake/static UI for this baseline; `KCF-UI010-002` assigns return context to UI-010 and shared editing to UI-006. `KD-UI010-002` records the confirmed correction dialog. | None for the UI-only baseline. |
| Assumption safety and expiry | PASS with limitation | Static/fake notification and parcel data are explicitly bounded; read status, pagination, API, server synchronization and backend error mapping remain deferred and are not claimed. | Revalidate before `INT-004-return-notification-api` or production synchronization. |
| Baseline completeness and consistency | PASS | Scope, out-of-scope behavior, Figma dimensions, return reason presentation, UI-006 handoff, correction confirmation, constraints and revalidation triggers are present and internally consistent. | API/read/pagination/error contracts are intentionally outside this baseline. |
| Downstream impact and revalidation triggers | PASS | Planning must preserve UI-006 form ownership, use UI-008 Android dialog behavior where applicable, and keep fake data replaceable. API, Figma revision, copy or state-transition changes trigger revalidation. | None. |
| Planning handoff readiness | PASS | The candidate contains enough approved and bounded information to plan the fake notification list, return banner/detail context, correction handoff and confirmation flow. | Planning must add implementation screenshots and state/interaction tests. |

## Findings

| Finding ID | Category | Severity | Evidence | Impact | Owner | Route | Status |
|---|---|---|---|---|---|---|---|
| KV-UI010-001 | resolution | P1 | The prior Resolution listed correction Alert/copy as unresolved. Figma MCP node `4990:8053` directly confirms the title, body, actions and `312 x 204` Android dialog; the current Collection/Resolution now records `SRC-UI010-005`, `KCL-UI010-005` and `KD-UI010-002`. | The candidate is complete for correction-confirmation visual and copy scope. | UI-010 task owner / requester | knowledge_resolution | resolved 2026-10-08 before Validation |
| KV-UI010-002 | decision | P1 | The requester deferred read, pagination, API contract and backend errors; `KCF-UI010-001` and `KD-UI010-001` bound the UI-only baseline to fake/static data. | No blocker for v0.1 UI baseline; production notification integration remains a separate obligation. | INT-004/API owner | planning | resolved as deferred |

## Baseline Decision
- Result: PASS
- Active baseline ID: `KB-UI-010-RETURN-FLOW-R1`
- Supersedes: none
- Activation or rejection reason: The current Collection and Resolution are traceable to the parent FR/AC, Figma MCP evidence and requester decisions. The correction-confirmation dialog is now explicitly evidenced; deferred notification/API behavior is bounded and not claimed as part of the v0.1 UI baseline.

## Blocking Items
- None for Knowledge Validation or Planning handoff.

## Planning Handoff
- Ready for Planning: yes
- Decisions and constraints to cite: `KD-UI010-001`, `KD-UI010-002`; UI-006 owns shared editing/validation/photo/submit state; UI-010 owns return context; use fake notification/parcel data; Android-only correction confirmation; no read/pagination/API/error implementation in this baseline.
- Required follow-up evidence: implementation screenshots for notification list, return reason/banner, correction entry, Android confirmation and corrected state; Compose state/interaction tests; later `INT-004-return-notification-api` contract.
- Revalidation triggers: Figma node/revision or copy changes, correction state-transition changes, notification/read/pagination/error/API decisions, or production synchronization scope.
- Next action: planning
