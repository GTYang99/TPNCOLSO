# Developer Guide

> Optional / on-demand. This guide explains the workflow; it is not normal Required Reading and never overrides `AGENTS.md`, Rules, templates, or approved Task artifacts.

## Five-minute Start

1. Read `AGENTS.md`.
2. Find the Task in `docs/tasks/README.md`.
3. Read only its `state.yaml`.
4. Use `next_action` in the router at `ai/README.md`.
5. Read that one primary Rule and its named current inputs.
6. Inspect exact product/design/API/code evidence only when needed.

Never preload all Rules, specifications, Task files, Tutorial, or `history/`.

## Two Levels of Flow

Project sequencing decides which Task comes next:

```text
Product/domain rules
-> UI and API contracts
-> Architecture/foundation
-> Feature Tasks
-> Integration Tasks
-> Release
```

Each individual Task follows:

```text
Knowledge when needed
-> Requirement
-> Planning
-> Plan Review
-> Implementation
-> Validation / Commit / Review / CI
-> Verification
-> Release
```

A Task dependency such as `UI-001 -> UI-002` is not a workflow state. A state such as `next_action: knowledge_resolution` means the current Task must perform that phase action before advancing.

## Knowledge in Plain Language

- Collection: find and register the relevant sources.
- Resolution: reconcile claims, conflicts, decisions, and safe assumptions.
- Validation: independently check the candidate baseline.
- Ready: activate the baseline and allow Planning.

Use Knowledge only when sources are multiple, stale, inaccessible, uncertain, or conflicting. One clear authoritative request may use `knowledge.status: not_required`.

## Task Artifacts

```text
docs/tasks/<task-id>/
├── state.yaml                 current route; read first
├── requirement.md             approved scope and AC
├── knowledge-collection.md    when Knowledge is used
├── knowledge-resolution.md
├── knowledge-validation.md
├── analysis.md
├── plan.md
├── plan-review.md
├── execution-report.md
├── issue-log.md               only when issues exist
├── verification.md
└── history/                   superseded evidence; on demand
```

Canonical filenames contain only current evidence. Move an invalidated baseline, plan, review, or Verification result into a named `history/` folder before creating its replacement.

## How to Continue a Task

Read these state fields:

| Field | Question |
|---|---|
| `phase` / `status` | Where is the Task now? |
| `next_action` | Which primary Rule runs next? |
| `knowledge.baseline_id` | Which understanding is active? |
| `plan_review.status` | Is implementation authorized? |
| `git.commit` | Which revision owns the evidence? |
| `verification.result` | Did acceptance pass, fail, or remain unverified? |
| `blocking` | What prevents advancement? |

Then open only the files required by the selected Rule.

## UI-first Development

When API implementation is incomplete:

```text
Composable
-> immutable UI state / ViewModel
-> repository or use-case contract
-> centralized fake data source
```

Do not scatter fake values in Composables or invent production DTOs/endpoints. After the API contract is approved, an Integration Task supplies production repositories, mappers, errors, authentication, and retry behavior without redefining the UI state contract.

The current UI order is maintained in `docs/tasks/README.md`.

## Failure Routing

| Finding | Route |
|---|---|
| missing/stale source | Collection |
| conflicting claims | Resolution |
| incomplete requirement/plan | Planning |
| code regression | Debug |
| tool/environment failure | Infrastructure |
| unknown cause | Investigation |

Record the issue before changing code. Only an implementation failure goes directly to Debug.

## Evidence Rules

- Tie requirements, plans, code, tests, and Verification to stable IDs.
- Bind evidence to the current baseline and revision.
- `NOT VERIFIED` means evidence is unavailable; it never means PASS.
- A changed baseline invalidates dependent plans and approvals.
- A changed commit invalidates stale review, CI, and Verification evidence.

## On-demand References

- Phase router: `ai/README.md`
- Artifact templates: `ai/templates/`
- Architecture: `docs/architecture/overview.md`
- Product index: `docs/product/land-survey-115/README.md`
- Task index: `docs/tasks/README.md`
- Extended historical tutorial: `ai/tutorial/history/developer-guide-legacy.md`
