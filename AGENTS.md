# Project Contract

Android Kotlin application using MVVM and Jetpack Compose.

- Target: Android 9+ (`minSdk 28`)
- Architecture: `docs/architecture/overview.md`
- Product index: `docs/product/land-survey-115/README.md`
- Workflow index: `ai/README.md`
- Task index: `docs/tasks/README.md`

# Instruction Precedence

1. Current user request and explicit approvals
2. Approved Task requirement and acceptance criteria
3. This file
4. The active phase Rule
5. Canonical templates
6. Tutorial and examples

Never resolve a conflict silently when it changes product behavior, public contracts, stored data, security, privacy, acceptance criteria, or release risk. Record it and request the smallest authoritative decision.

# Minimal Context Loading

For every turn:

1. Read this file once.
2. Read `docs/tasks/<task-id>/state.yaml`.
3. Follow `next_action` and read only its primary Rule from `ai/README.md`.
4. Read only the current artifacts listed by that Rule.
5. Open source specifications or code on demand for the exact claim or change.

Do not recursively read all of `ai/`, `docs/`, or a Task folder. Do not read `ai/tutorial/` unless an example is needed. Do not read `history/` or superseded evidence unless the current artifact explicitly references it or provenance is under review.

Canonical filenames contain only the current version. Move superseded task evidence to `docs/tasks/<task-id>/history/<baseline-or-revision>/`; never leave stale approval in an active artifact.

# Sources of Truth

| Scope | Canonical source |
|---|---|
| Workflow and global constraints | `AGENTS.md` |
| Phase execution | `ai/rules/` |
| Artifact and state shape | `ai/templates/` |
| Architecture | `docs/architecture/` |
| Product, design, API, assets | matching `docs/` domain |
| Current task state and evidence | `docs/tasks/<task-id>/` |
| Examples | `ai/tutorial/` (non-authoritative) |

Authority is domain-specific. Design cannot redefine an API contract; code cannot redefine approved product intent; tests cannot prove a requirement is correct.

When structured design access, including Figma MCP, is incomplete or untrusted, UI screenshots may be primary Collection evidence only for visible UI. Resolution MUST label screenshot-derived claims as `visual_observation`, distinguish `observed`, `declared`, `inferred`, and `unknown`, and list interactions, unshown states, data rules, permissions, API behavior, and accessibility information that the screenshots cannot establish. Validation decides whether those gaps block Knowledge Ready; never infer hidden behavior from screenshots.

# Task Lifecycle

```text
Intake
-> Knowledge (when required)
-> Requirement Baseline
-> Planning
-> Plan Review
-> Implementation
-> Developer Validation
-> Git Commit
-> Code Review
-> CI
-> Verification
-> UAT (when required)
-> Release Approval
-> Authorized Merge or Deployment
-> Post-deployment Verification / Monitoring (when applicable)
-> Done
```

Knowledge is required for multiple, stale, unknown, inaccessible, or conflicting sources. A small task with one clear authoritative source may set Knowledge to `not_required`.

Knowledge uses one phase with ordered stages:

```text
Collection -> Resolution -> Validation -> Ready -> Planning
```

Collection registers evidence. Resolution decides claims, conflicts, safe assumptions, and candidate baseline. Validation activates the baseline. `Ready` is a gate, not a separate phase.

# Phase Routing

The canonical phase-to-Rule table is `ai/README.md`. Each normal phase has one primary Rule. Supporting policies such as coding, testing, Git, issues, and templates are read only when the primary Rule or current work requires them.

Every phase must define:

- entry conditions and required current inputs
- allowed writes and prohibited side effects
- required outputs and evidence
- exit status and `next_action`
- failure classification and route

# Required Gates

```text
Sources identified
-> Knowledge Ready or justified not_required
-> Requirements resolved and approved
-> Plan and technical design approved
-> Implementation complete
-> Developer validation complete or limitations recorded
-> Commit and code review complete
-> CI PASS
-> Verification PASS
-> UAT PASS when required
-> Release approval
-> Authorized merge/deploy
-> Required monitoring complete
-> Done
```

- `NOT VERIFIED` is never PASS.
- Verification evaluates a reviewed, committed revision and does not modify production code.
- Missing required CI, UAT, or release evidence blocks advancement.
- Merge, publish, deploy, production configuration, and signing require explicit human authorization.

# Requirement and Change Control

Before Implementation, the approved baseline must contain scope, out-of-scope behavior, observable numbered acceptance criteria, constraints, dependencies, risks, and material decisions.

Do not modify requirements or lower acceptance criteria to make code pass. A material change returns to Knowledge Resolution or Planning, updates affected artifacts and tests, and requires a new Plan Review. Editorial corrections may proceed only with their reason recorded.

# Failure Routing

Classify before changing code:

| Cause | Route |
|---|---|
| missing/stale source | `knowledge_collection` |
| conflicting claim/decision | `knowledge_resolution` |
| requirement gap | Knowledge or `planning`, based on cause |
| planning gap | `planning` |
| implementation regression | `debug` |
| environment/tooling | `infrastructure` |
| unknown | `investigation` |

Only an implementation failure enters Debug automatically. Record blockers, regressions, and mismatches in `issue-log.md`; keep `state.yaml` focused on the main task route.

# State Contract

`ai/templates/task/state.yaml` is the only state schema. Use a mapping for `task`, one scalar for `next_action`, and only statuses defined by the template or owning Rule.

On every handoff update:

- `phase`, `status`, and `next_action`
- phase completion/result fields
- baseline, branch, commit, CI, and Verification identifiers when available
- blockers and `NOT VERIFIED` limitations

Historical non-canonical state remains readable but must be normalized on its next transition.

# Side-effect Boundaries

| Work | Production code |
|---|---|
| Knowledge, Planning, Plan Review | forbidden |
| Verification, Investigation, Infrastructure | forbidden |
| Implementation / approved Debug re-entry | only approved scope |
| Release | records only; no merge/deploy without authorization |

All work preserves unrelated user changes. Stop when required artifacts are missing, material authority conflicts remain, scope does not authorize a necessary change, isolation is unsafe, or required validation has no sufficient alternative.

# Engineering Invariants

- Reuse existing components only when behavior, ownership, lifecycle, and design constraints match.
- Do not introduce speculative abstractions, DTOs, endpoints, or product behavior.
- Do not perform unrelated refactoring.
- Do not skip required tests.
- Never store secrets, credentials, personal data, production payloads, or unrestricted sensitive data in task artifacts or logs.
- Bind evidence to source version, working-tree state, branch, commit, build, or retrieval time when available.
- Run the smallest relevant build, test, static, regression, and smoke checks; record commands, results, and limitations.

# Completion Report

Report the outcome first, then changed files, validation, unresolved issues, and next action. Do not claim Done while any required gate is failed, pending, blocked, or `NOT VERIFIED`.
