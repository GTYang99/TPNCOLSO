# Knowledge Validation

## Inputs
- Collection artifact: `docs/tasks/UI-006-survey-form/knowledge-collection.md`
- Resolution artifact: `docs/tasks/UI-006-survey-form/knowledge-resolution.md`
- Candidate baseline ID: `KB-UI-006-SURVEY-FORM-R1`
- Validator: Codex task agent
- Validation date: 2026-10-07 (Asia/Taipei)
- Independence note: Validation reproduced the cited repository evidence in the current checkout. It is not an independent human approval; requester decisions recorded in Resolution remain the authority for product scope.

## Validation Checklist

| Check | Result | Evidence | Finding / Route |
|---|---|---|---|
| Collection scope and source coverage | PASS | Collection registers requester/design, product/data, architecture/repository and sequencing sources; API/upload evidence is explicitly out of scope. | Scope is sufficient for UI-only implementation. |
| Source identity and version | PASS | Cited paths exist in the current checkout; UI-005 state and execution evidence identify the completed prerequisite revision `cb81272`. | No missing primary file was found. |
| Domain authority | PASS | Product field rules come from `2 資料規格.md`; visible component structure comes from `docs/design/app-ui-requirements.md` and the recorded Figma inspection; ownership comes from requester decisions in Resolution. | Authority is separated by domain. |
| Freshness and supersession | PASS | UI-006 Resolution is the current active task artifact; UI-005 is completed and its handoff is current. No superseding UI006 artifact is registered. | Revalidate if product rules, Figma node, ownership or API contract changes. |
| Claim-to-source traceability | PASS | KCL-UI006-001..006 each identify supporting sources; field, conditional occupation and four-photo claims reproduce in the parent requirement and data specification. | Traceability is adequate for Planning. |
| Requirement / AC traceability | PASS | Parent requirement maps FR-008..FR-011 and AC-008..AC-011; AC-014 and AC-015 add dirty/discard and photo-delete behavior covered by the design specification. | Plan must include all task-scoped behavior, including local-only submit/error states. |
| Conflict resolution and approval | PASS | KD-UI006-001 assigns shared edit/validation/photo/submit ownership to UI006; KD-UI006-002 selects the Figma dimensions and product rules; UI-005 state confirms the handoff is complete. | Stale UI-005 prerequisite blocker is cleared. |
| Assumption safety and expiry | PASS | KA-UI006-001 limits local/fake photo fixtures to UI verification and names API contract availability as the expiry trigger. | No production DTO, endpoint or upload protocol may be inferred. |
| Baseline completeness and consistency | PASS | Resolution defines scope, exclusions, six material claims, decisions, assumptions, downstream impact and revalidation triggers; data specification defines the ten-field form and camera-only 4:3 photos. | Candidate is complete for UI-only Planning. |
| Downstream impact and revalidation triggers | PASS | Resolution records impact on requirements, plan/design, code/data/API, tests/verification and release; triggers include Figma, field rules, ownership and API changes. | Follow-up evidence remains deferred to API/integration work. |
| Planning handoff readiness | PASS | UI-005 prerequisite is complete; UI006 can use local/fake state and no production API. | Route to `planning`. |

## Findings

| Finding ID | Category | Severity | Evidence | Impact | Owner | Route | Status |
|---|---|---|---|---|---|---|---|
| KV-UI006-001 | resolution | P2 | User referred to a “v0.1 baseline”, but no separate UI006 v0.1 artifact exists in the repository; the current candidate is `KB-UI-006-SURVEY-FORM-R1`. | Naming only; inventing a v0.1 identifier would weaken traceability. | Task owner / requester | planning | resolved by retaining R1 as the canonical baseline and recording the limitation |
| KV-UI006-002 | environment | P2 | Figma evidence is recorded as structured inspection, but per-state pixel captures are incomplete. | Formal pixel-parity claim remains deferred; UI implementation can proceed against the recorded dimensions and component states. | Design / requester | planning | recorded |

## Baseline Decision
- Result: PASS
- Active baseline ID: `KB-UI-006-SURVEY-FORM-R1`
- Supersedes: None
- Activation or rejection reason: The scoped UI-only survey form behavior is authoritative, traceable and internally consistent. UI-005 handoff is complete. API, upload and production integration remain explicitly deferred and do not block this development scope.

## Blocking Items
- None.

## Planning Handoff
- Ready for Planning: yes
- Decisions and constraints to cite:
  - UI006 is the single owner of shared field editing, conditional validation, photo management and submit UI; UI005 owns detail display and entry only.
  - The form contains ten survey fields; the three system fields are read-only and fields 4–10 follow the data specification.
  - `無占用` hides and clears occupation type, household count and address; otherwise those fields are required and household count is an integer at least 1.
  - Photos are camera-only, exactly four, 4:3, show capture time, and support delete/retake.
  - Use the recorded Figma dimensions: 402×874 screen, 354×964 inner form, 354×42 tabs, 354×56 standard rows, 168×126 photo tiles and 56×56 FAB.
  - Use local/fake state only; do not add production API, DTO or upload protocol.
- Required follow-up evidence:
  - Per-state Figma captures for formal visual parity.
  - Approved production API/upload contract before integration work.
- Revalidation triggers: Figma node `2938:193` or approved revision changes; product field rules change; UI005/UI006 ownership changes; API/upload contract is approved.
- Next action: `planning`
