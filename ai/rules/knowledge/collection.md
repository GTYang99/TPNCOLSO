# Knowledge Collection Rules

## Objective

Discover and register the material sources required to understand a task before claims or conflicts are resolved.

Collection gathers evidence; it does not decide product behavior, resolve conflicts, or modify production code.

## Required Reading

- `AGENTS.md`, especially Knowledge Governance and Domain Authority
- latest task `state.yaml`
- current user request
- existing task `requirement.md`, when present
- `ai/templates/knowledge/collection.md`

## Entry Criteria

Use Collection when Knowledge work is required and:

- the relevant sources have not been inventoried
- a required source, version, owner, or authority is unknown
- a downstream phase discovers missing, stale, or superseded evidence
- the active Knowledge baseline is invalidated by a source change

## Collection Process

```text
Define Collection Scope
-> Search Approved Project Locations
-> Inspect Repository and Test Evidence
-> Obtain Applicable External Primary Sources
-> Register Source Metadata
-> Classify Domain, Authority, and Freshness
-> Identify Missing or Inaccessible Sources
-> Check Coverage Against Task Questions
-> Handoff to Resolution or Block
```

## Required Source Categories

Check each applicable category and record either its sources or why it is not applicable:

- authorized user decisions and task requirement
- product and business rules
- design, interaction, copy, accessibility, and assets
- API, schema, error, authentication, and compatibility contracts
- architecture and repository implementation
- tests, logs, runtime observations, and prior defects
- build, environment, release, and operational evidence
- current official external platform, dependency, security, or regulatory sources

## Source Registration Rules

Every material source MUST include the metadata required by `AGENTS.md`. Use stable source IDs such as `SRC-001` and record exact locations or immutable identifiers when available.

- Prefer primary sources over summaries.
- Bind repository evidence to a commit, branch, or explicit working-tree state.
- Record inaccessible sources without attempting to bypass access controls.
- Record missing metadata as `unknown`; do not infer authority or freshness.
- Do not copy secrets, personal data, confidential payloads, or unrestricted production data.
- Do not collect unrelated documents merely because they are available.

## Required Output

Create or update `docs/tasks/<task-id>/knowledge-collection.md` from `ai/templates/knowledge/collection.md`.

The artifact MUST include:

- collection question and scope
- source coverage checklist
- source register
- missing or inaccessible sources
- initial conflicts and gaps without resolving them
- collection limitations
- handoff recommendation

## State Updates

When starting:

```yaml
phase: knowledge
status: knowledge_collection_in_progress

knowledge:
  status: collecting
  collection: in_progress
  resolution: pending
  validation: pending
  baseline_id: null

next_action: knowledge_collection
```

When collection is sufficient:

```yaml
phase: knowledge
status: knowledge_collected

knowledge:
  status: resolving
  collection: completed
  resolution: pending
  validation: pending
  baseline_id: null

next_action: knowledge_resolution
```

When a material source is unavailable or cannot be identified:

```yaml
phase: knowledge
status: knowledge_collection_blocked

knowledge:
  status: blocked
  collection: blocked
  resolution: pending
  validation: pending
  baseline_id: null

blocking:
  - Required authoritative source is unavailable

next_action: requirement_clarification
```

Use `next_action: infrastructure` instead when the source exists but access, tooling, credentials, or environment prevents retrieval.

## Handoff to Resolution

The handoff MUST identify:

- collection scope and coverage
- registered sources and unresolved authority or freshness
- missing sources and whether they block progress
- initial contradictions or gaps
- questions Resolution must answer

Collection completion means the evidence set is sufficient to begin Resolution; it does not mean the Knowledge is correct or ready for Planning.

## Restrictions

Collection MUST NOT:

- select a preferred product outcome
- resolve a conflict by convenience
- treat a summary, example, or current code as approved intent
- label unknown authority or freshness as current
- modify requirements or production code
- bulk-copy unrelated or sensitive material into task artifacts

## Definition of Done

- scope and task questions are explicit
- applicable source categories were checked
- material sources have stable IDs and required metadata
- missing, inaccessible, stale, and superseded sources are visible
- initial gaps and conflicts are recorded
- the Resolution handoff is actionable or the task is explicitly blocked
- `state.yaml` is updated
