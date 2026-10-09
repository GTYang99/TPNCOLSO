# Knowledge Validation

## Inputs
- Requirement artifact: `requirement.md` (requester-approved 2026-10-08)
- Collection artifact: `knowledge-collection.md`
- Resolution artifact: `knowledge-resolution.md`
- Candidate baseline ID: `KB-UI-009-UI-HARDENING-R1`
- Validator: Codex
- Validation date: 2026-10-08 (Asia/Taipei)
- Independence note: Same agent performed Collection/Resolution and Validation; independent review was not available.

## Validation Checklist

| Check | Result | Evidence | Finding / Route |
|---|---|---|---|
| Collection scope and source coverage | PASS | Collection registers the approved requirement, design rules, roadmap boundary and a per-task UI-002–UI-008 source matrix. | None. |
| Source identity and version | PASS with limitation | UI-002 uses the requester-approved composite alternative; UI-003–UI-008 have registered node/child IDs or component evidence. No separate UI-009 `v0.1` artifact exists; `KB-UI-009-UI-HARDENING-R1` is the canonical candidate. Formal Figma revision IDs remain unknown. | Formal pixel parity is not claimed; revalidate if an approved revision is required. |
| Domain authority | PASS | `docs/design/app-ui-requirements.md` owns responsive/accessibility design rules; the validated roadmap owns task sequencing and UI-009's broad cross-screen scope. | No route |
| Freshness and supersession | PASS with limitation | Retrieval dates are recorded for the Figma sources and the auth composite is repository-backed. Formal Figma revision/approval metadata remains unknown, but the approved requirement excludes formal pixel parity. | Revalidate if pixel parity becomes an acceptance criterion or a child source changes. |
| Claim-to-source traceability | PASS with limitation | The approved requirement and Resolution map hardening claims to parent/design rules and the per-task source matrix; Figma-derived claims are limited to `visual_observation`. | Process-deep state and accessibility semantics cannot be inferred from visual sources. |
| Requirement / AC traceability | PASS | `requirement.md` contains `AC-001`–`AC-007`; the requester approved the document and AC set on 2026-10-08. `AC-006` explicitly includes process-death/recreation restoration. | Runtime evidence belongs to Planning/Developer Validation. |
| Conflict resolution and approval | PASS | Requester explicitly approved Android transient saved state for UI-006 form data and UI-007 photo state after process death, with no long-term storage; UI-002 password and captcha remain excluded. This is recorded as `KD-UI009-005`. | No open material conflict. |
| Assumption safety and expiry | PASS with limitation | The allowed mechanism is limited to transient system saved state; no database, files, logs, analytics or API storage. Credentials/captcha remain reset. Runtime validation must prove local photo references restore correctly. | Revalidate if durable persistence or additional sensitive fields are proposed. |
| Baseline completeness and consistency | PASS | Requirement/ACs, source matrix, requester decisions, ownership boundaries, explicit saved-state limits and revalidation triggers are consistent. | Runtime restoration mechanics remain a Planning/Developer Validation obligation. |
| Downstream impact and revalidation triggers | PASS | Resolution records source, ownership, Figma revision and Android-rule revalidation triggers. Runtime regression evidence is intentionally downstream. | None for Knowledge handoff. |
| Planning handoff readiness | PASS | UI-008 is development complete; the process-death state boundary is now authorized and bounded. Planning can define the screen/state matrix and test matrix. | Route to Planning. |

## Findings

| Finding ID | Category | Severity | Evidence | Impact | Owner | Route | Status |
|---|---|---|---|---|---|---|---|
| KV-UI009-001 | collection | P2 | The former register named only a Figma section. The current collection maps UI-002–UI-008 to node/child IDs or repository alternatives and records retrieval dates; formal revision IDs remain unknown. | Source identity is clarified. Formal pixel-parity claims remain unavailable until a revision is approved. | Task owner / Design | knowledge_collection | resolved for source identity; limitation recorded |
| KV-UI009-002 | decision | P1 | `requirement.md` defines `AC-001`–`AC-007`; requester approval was recorded on 2026-10-08. | Resolved; the requirement set is authoritative for the UI-009 baseline. | Requester | planning | resolved 2026-10-08 |
| KV-UI009-003 | requirement scope | P1 | `AC-006` now explicitly includes rotation/configuration change and process-death/recreation restoration after requester confirmation on 2026-10-08. | Resolved; Planning must include both lifecycle paths in the regression matrix. | Requester | planning | resolved 2026-10-08 |
| KV-UI009-004 | decision / security lifecycle | P1 | Requester authorized transient Android saved state for UI-006 form data and UI-007 photo state, with no long-term storage; UI-002 password/captcha remain excluded. | Resolved by `KD-UI009-005`; runtime persistence boundaries remain an implementation check. | Requester | planning | resolved 2026-10-08 |

## Baseline Decision
- Result: PASS
- Active baseline ID: `KB-UI-009-UI-HARDENING-R1`
- Supersedes: none
- Activation or rejection reason: Requester decision `KD-UI009-005` resolves the process-death saved-state boundary while limiting it to transient Android saved state for UI-006 form data and UI-007 photo state, excluding long-term storage and UI-002 credentials/captcha. Formal Figma revision/pixel parity remains outside scope.

## Blocking Items
- None for Planning handoff.

## Planning Handoff
- Ready for Planning: yes
- Decisions and constraints to cite: `docs/design/app-ui-requirements.md` responsive/accessibility rules; `KB-UI-ROADMAP-0907-R1`; UI-008 development-complete handoff.
- Required follow-up evidence: UI-009 regression matrix, including rotation/process-death tests and confirmation that restoration uses transient state only.
- Revalidation triggers: baseline approval, restoration-scope change, child source node/revision change, or a request for formal pixel parity.
- Next action: planning
