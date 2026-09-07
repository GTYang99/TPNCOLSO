# Knowledge Validation Rules

## Objective

Independently verify that collected and resolved Knowledge is authoritative, current, complete, traceable, internally consistent, and safe to hand to Planning.

Validation checks the candidate Knowledge baseline. It does not create missing product decisions or modify production code.

## Required Reading

- `AGENTS.md`, especially Knowledge Governance
- latest task `state.yaml`
- `knowledge-collection.md`
- `knowledge-resolution.md`
- relevant primary sources and decision evidence
- `ai/templates/knowledge/validation.md`

## Entry Criteria

Validation begins only when:

- Collection is completed
- Resolution produced a candidate baseline
- material conflicts are resolved or explicitly marked blocked
- decision and assumption records identify their evidence and owner

## Validation Process

```text
Confirm Scope and Candidate Baseline
-> Sample or Reopen Primary Sources
-> Validate Authority and Freshness
-> Validate Claims and Traceability
-> Validate Conflict Decisions and Approvals
-> Validate Assumption Safety and Expiry
-> Check Completeness and Internal Consistency
-> Assess Downstream Readiness
-> PASS, FAIL, or NOT VERIFIED
```

## Validation Checklist

Validation MUST check:

- every material source has identity, scope, authority, freshness, and version evidence
- material claims link to supporting sources and affected requirements or acceptance criteria
- contradicting evidence is visible and resolved by the correct authority
- product choices have authorized approval rather than inference
- safe assumptions are reversible, owned, time-bounded, and paired with validation triggers
- the candidate baseline identifies included source versions and superseded knowledge
- unresolved items are correctly classified as blocking or non-blocking
- downstream impact and revalidation triggers are complete
- Planning can proceed without inventing material behavior

For material or high-risk work, the validator SHOULD be independent from the resolver. If the same Agent performs both roles, record that fact and use direct primary-source evidence for every critical finding.

## Result Rules

- `PASS`: executed checks demonstrate that the candidate baseline is ready.
- `FAIL`: evidence shows a collection, resolution, authority, freshness, conflict, or traceability defect.
- `NOT VERIFIED`: required evidence or access is unavailable.

`NOT VERIFIED` never becomes Knowledge Ready.

## Required Output

Create or update `docs/tasks/<task-id>/knowledge-validation.md` from `ai/templates/knowledge/validation.md`.

Every finding MUST identify its category, evidence, impact, owner, and route.

## State Updates

When starting:

```yaml
phase: knowledge
status: knowledge_validation_in_progress

knowledge:
  status: validating
  collection: completed
  resolution: completed
  validation: in_progress
  baseline_id: KB-001

next_action: knowledge_validation
```

When validation passes and the baseline becomes active:

```yaml
phase: knowledge
status: knowledge_ready

knowledge:
  status: ready
  collection: completed
  resolution: completed
  validation: passed
  baseline_id: KB-001

blocking: []
next_action: planning
```

When validation fails:

- missing, incomplete, stale, or wrongly classified sources -> `next_action: knowledge_collection`
- unresolved or incorrectly resolved claims, conflicts, decisions, or assumptions -> `next_action: knowledge_resolution`
- missing authorized product decision -> `next_action: requirement_clarification`
- unavailable access, tooling, environment, or external service -> `next_action: infrastructure`

Use `status: knowledge_validation_failed`, set `knowledge.validation: failed`, and record findings in `blocking`.

When evidence is unavailable, use `status: knowledge_not_verified`, set `knowledge.validation: not_verified`, and route to the owner of the missing evidence. Do not mark the baseline active.

## Knowledge Ready Gate

Knowledge Ready is a gate and status, not a separate execution phase. It requires:

- Collection completed
- Resolution completed
- Validation passed
- an active baseline ID
- no material blocker
- an actionable Planning handoff

## Restrictions

Validation MUST NOT:

- resolve its own blocking finding without returning it to the owning stage
- approve missing or inaccessible evidence
- treat confidence as proof
- change requirements, decisions, or production code
- erase failed validation evidence after a rerun
- mark Knowledge Ready when any material item is failed or not verified

## Definition of Done

- every checklist item has PASS, FAIL, NOT VERIFIED, or justified N/A
- findings contain reproducible evidence and a route
- the candidate baseline is activated only on PASS
- the Planning handoff is confirmed or blocked
- `state.yaml` is updated
