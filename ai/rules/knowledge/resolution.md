# Knowledge Resolution Rules

## Objective

Resolve the meaning, authority, scope, freshness, and conflicts of product, design, API, asset, repository, test, operational, and external sources before Planning commits to an implementation direction.

Knowledge Resolution interprets and validates evidence. It does not invent product decisions, convert assumptions into requirements, or modify production code.

## Required Reading

- `AGENTS.md`, especially Knowledge Governance and Sources of Truth
- latest task `state.yaml`
- current user request and approved decisions
- completed task `knowledge-collection.md`
- relevant product, design, API, architecture, asset, repository, test, release, and operational evidence
- existing task `requirement.md` and prior `knowledge-resolution.md`, when present
- `ai/templates/knowledge/resolution.md`

## When Required

Use this phase when:

- multiple source documents or systems affect the task
- authoritative sources conflict across or within domains
- a fixed specification is incomplete, ambiguous, stale, superseded, or of unknown authority
- a material claim depends only on an example, summary, recollection, or current implementation
- an assumption affects product intent, public contracts, stored data, security, privacy, compliance, migration, or release risk
- a downstream phase discovers new evidence that invalidates the current Knowledge baseline
- Planning would otherwise need to invent material behavior

A task with one clear, current, authoritative requirement may record the Knowledge workflow as `not_required` and proceed directly to Planning.

## Required Inputs

- current user request
- available sources under `docs/product/`, `docs/design/`, `docs/api/`, `docs/assets/`, and other documented project locations
- applicable external primary sources or platform documentation
- relevant repository revision or explicit working-tree state
- existing tests, runtime observations, logs, and release evidence
- task `requirement.md`, when present

Missing inputs MUST be recorded; do not silently omit a source that could materially change the result.

## Knowledge Resolution Process

```text
Read Collection Handoff
-> Confirm Scope and Source Coverage
-> Extract Material Claims
-> Map Claims to Requirements and Acceptance Criteria
-> Detect Conflicts, Gaps, and Assumptions
-> Resolve with Evidence or Escalate to Decision Owner
-> Record Decisions and Superseded Claims
-> Build Candidate Knowledge Baseline
-> Handoff to Knowledge Validation
-> Update State
```

## Source Register

Use the source IDs established in `knowledge-collection.md`. Add a source only when Resolution discovers material missing evidence, then update the Collection artifact. Every material source includes:

- stable source ID
- title, type, and location
- domain and owner, when known
- version, revision, branch, node, endpoint, artifact, or retrieval identifier
- modified, published, or retrieved date
- applicable scope and affected acceptance criteria
- authority: `authoritative`, `supporting`, `observed`, `example`, or `unknown`
- freshness: `current`, `stale`, `superseded`, or `unknown`
- accessibility or confidentiality limitation

Prefer primary evidence. If only secondary evidence is available, record that limitation and do not overstate confidence.

## Authority and Scope Resolution

Apply the domain authority rules in `AGENTS.md`.

- Product intent is owned by approved product requirements and authorized decisions.
- Design owns approved presentation and interaction details within product and API constraints.
- API and schema contracts own interface and data-shape obligations within approved product intent.
- Repository and runtime evidence establish current behavior, not necessarily intended behavior.
- Tests establish evidence for their exact revision, environment, inputs, and asserted behavior.
- Tutorials and examples do not override canonical rules or approved requirements.

A source is authoritative only for its applicable domain, version, and scope. Do not resolve cross-domain conflict by applying a source outside its authority.

## Freshness Validation

For every material source:

- identify the version or point in time it represents
- determine whether a later authoritative source supersedes it
- determine whether the information is stable or likely to change
- revalidate unstable external or environment-dependent claims using current primary evidence
- record `unknown` when freshness cannot be demonstrated

Unknown or stale knowledge blocks Planning when it may change acceptance criteria, architecture, public contracts, persistent data, security, privacy, migration, or release behavior.

## Claim and Traceability Rules

Record each material claim with:

- claim ID
- normalized statement
- supporting and contradicting source IDs
- authority and confidence
- affected requirement or acceptance criterion
- resolution status

Do not copy a source without interpreting its relevance. Do not record a conclusion without linking the evidence that supports it.

## Conflict Resolution

For each conflict, assign a stable ID and record:

- conflicting claims and sources
- domain and scope
- affected behavior, data, acceptance criteria, tests, or release risk
- severity
- possible resolutions and tradeoffs
- required decision owner
- final decision, rationale, approver, and date, or blocking status

Use evidence to resolve factual questions. Obtain authorized approval for product or policy choices. Modification date alone does not resolve authority.

## Decision Record

Each material Knowledge decision SHOULD use an identifier such as `KD-001` and include:

- decision statement
- alternatives considered
- source and conflict IDs
- rationale
- approving authority and date
- affected acceptance criteria and downstream artifacts
- superseded decision or claim IDs
- revalidation trigger

Decisions that apply beyond one task SHOULD be promoted to the owning shared document or ADR after approval.

## Assumption Rules

An assumption may be marked safe for Planning only when it is:

- reversible
- low impact if wrong
- outside material product, API, data, security, privacy, compliance, migration, and release decisions
- paired with a validation method and recheck trigger

Record assumption ID, statement, evidence, confidence, impact if wrong, owner, validation method, and expiry or trigger. Unsafe assumptions are blockers. Implementation does not promote an assumption into an approved requirement.

## Knowledge Baseline

For material work, prepare a candidate baseline identifier or dated snapshot that contains:

- included source versions
- resolved claims and decisions
- approved safe assumptions
- known non-blocking gaps
- invalidation and revalidation triggers

The candidate baseline becomes active only after Knowledge Validation passes. Baseline history is immutable as evidence. When Knowledge changes, create a new candidate baseline or revision, identify what it supersedes, and map downstream impact.

## Required Outputs

Create or update `docs/tasks/<task-id>/knowledge-resolution.md` from `ai/templates/knowledge/resolution.md`.

The artifact MUST include:

- question and scope
- source register
- material claims and traceability
- conflicts and gaps
- decision records
- assumptions
- candidate Knowledge baseline
- downstream impact
- revalidation triggers
- resolved and blocked items
- Validation handoff

## State Updates

When starting after Collection:

```yaml
phase: knowledge
status: knowledge_resolution_in_progress

knowledge:
  status: resolving
  collection: completed
  resolution: in_progress
  validation: pending
  baseline_id: null

next_action: knowledge_resolution
```

When Resolution produces a candidate baseline:

```yaml
phase: knowledge
status: knowledge_resolved

knowledge:
  status: validating
  collection: completed
  resolution: completed
  validation: pending
  baseline_id: KB-001

next_action: knowledge_validation
```

When blocked:

```yaml
phase: knowledge
status: blocked

knowledge:
  status: blocked
  collection: completed
  resolution: blocked
  validation: pending
  baseline_id: null

blocking:
  - Material specification conflict requires an authorized decision

next_action: requirement_clarification
```

When a downstream baseline is invalidated, route to Collection if sources are missing, stale, or changed; otherwise return to Resolution. Preserve former decision history and identify impacted task artifacts. Do not overwrite old evidence as if it never applied.

## Handoff to Validation

The handoff MUST identify:

- candidate Knowledge baseline
- resolved decisions and constraints
- safe assumptions
- unresolved non-blocking items
- blockers, if any
- affected requirements and acceptance criteria
- required downstream citations
- events that require re-entry into Collection or Resolution

Knowledge Validation MUST NOT begin when the handoff contains a material blocker. Planning MUST NOT begin until Validation passes and Knowledge status is `ready`.

## Restrictions

Knowledge Resolution MUST NOT:

- modify production code
- invent or approve product behavior without authority
- treat current implementation as intended behavior without evidence
- let design silently redefine API or product requirements
- treat examples, summaries, or prior Agent output as primary authority
- hide stale, missing, inaccessible, or conflicting evidence
- copy secrets, credentials, personal data, or unrestricted production payloads into artifacts
- erase superseded decisions or source history

## Definition of Done

- question and scope are explicit
- material sources are registered with authority and freshness
- claims are traceable to sources and affected acceptance criteria
- conflicts are resolved by evidence or authorized decision, or explicitly blocked
- assumptions are safe, owned, and time-bounded
- candidate Knowledge baseline and invalidation triggers are recorded
- downstream impact is identified
- Validation handoff is actionable without inventing material behavior
- `state.yaml` is updated
