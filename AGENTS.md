# Project Summary

Android Kotlin application using MVVM.

- Primary language: Kotlin
- Target: Android 9+
- Architecture: `docs/architecture/overview.md`
- Product specification index: `docs/product/land-survey-115/README.md`
- Workflow navigation: `ai/README.md`

# Goal

Complete each task from requirement understanding through verified release readiness without changing approved product intent, lowering acceptance criteria, or introducing unrelated work.

# Success Criteria

A task is complete only when:

- its requirements and acceptance criteria are traceable to implementation and evidence
- all required phase artifacts are present under `docs/tasks/<task-id>/`
- relevant validation has passed, or any unavailable validation is explicitly marked `NOT VERIFIED`
- the implementation is committed on a task branch
- CI passes
- independent Verification passes
- Release records the final decision and the task state is `done`

# Instruction Precedence

For repository instructions, use this order:

1. Current user request and explicitly approved decisions
2. Approved `requirement.md` and acceptance criteria
3. This `AGENTS.md`
4. The active phase rule in `ai/rules/`
5. Templates and examples

Do not silently resolve a conflict that would change requirements, acceptance criteria, public API behavior, data handling, or release risk. Record the conflict and request the smallest decision needed.

# Sources of Truth

- `AGENTS.md`: workflow, routing, gates, and global constraints
- `ai/rules/`: phase-specific rules grouped by lifecycle domain
- `ai/templates/`: canonical artifact and state formats
- `ai/tutorial/`: explanatory guidance and examples; never overrides rules
- `docs/architecture/`: system structure, technology, ownership, and entry points
- `docs/product/`: product requirements and fixed product decisions, when present
- `docs/design/`: design specifications and asset guidance, when present
- `docs/api/`: API contracts, when present
- `docs/assets/`: asset inventory and usage constraints, when present
- `docs/tasks/<task-id>/`: task requirement, analysis, plan, state, execution evidence, issues, and verification

Examples never override an approved requirement or a canonical template.

# Canonical Directory Structure

```text
Project/
├── AGENTS.md                       # mandatory workflow entry
├── ai/
│   ├── README.md                   # workflow navigation and migration map
│   ├── rules/
│   │   ├── knowledge/              # Collection, Resolution, Validation
│   │   ├── planning/               # Planning and Plan Review
│   │   ├── implementation/         # Developer, Coding, Debug
│   │   ├── verification/           # Testing and Verification
│   │   ├── operations/             # Infrastructure, Investigation, Release
│   │   └── governance/             # Git, Issues, Template policy
│   ├── templates/
│   │   ├── knowledge/              # Knowledge artifacts
│   │   └── task/                   # Task lifecycle artifacts and state
│   └── tutorial/
│       ├── developer-guide.md
│       └── assets/
└── docs/
    ├── architecture/               # project architecture source
    ├── product/
    │   ├── templates/
    │   └── land-survey-115/        # approved product specification set
    ├── design/                     # approved designs and design requirements
    │   └── templates/
    ├── api/                        # API contracts
    │   └── templates/
    ├── assets/                     # asset manifests and constraints
    │   └── templates/
    └── tasks/<task-id>/            # task state, evidence, and handoffs
```

## Placement Rules

- Keep `AGENTS.md` concise enough to serve as the mandatory entry; detailed execution belongs in `ai/rules/`.
- Put reusable workflow explanations and examples in `ai/tutorial/`.
- Put canonical Knowledge and Task artifact shapes in `ai/templates/`; do not store completed Task artifacts there.
- Put stable project knowledge in the matching `docs/` domain and task-specific interpretations in `docs/tasks/<task-id>/`.
- Put domain templates inside that domain's `templates/` folder; do not mix templates with approved specifications.
- A product specification set with multiple files MUST have its own folder and `README.md` index.
- New rule files MUST be placed in the lifecycle domain that owns their state transition.
- New paths become canonical only after `AGENTS.md`, `ai/README.md`, the tutorial, and affected references are updated together.
- Do not create compatibility copies of moved authoritative files. Record former paths only in the migration map to prevent duplicate sources of truth.

# Knowledge Governance

Knowledge is the evidence and context used to understand a task. It is not automatically an approved requirement or decision.

Use these terms consistently:

| Term | Meaning |
|---|---|
| Source | An identifiable origin such as a user decision, specification, design, API contract, repository revision, test, log, or external standard |
| Knowledge | A validated statement derived from one or more sources, with scope and confidence |
| Requirement | Approved behavior or constraint that implementation and Verification must satisfy |
| Decision | An approved choice that resolves alternatives or a material conflict |
| Assumption | A provisional belief used only when its risk is acceptable and explicitly recorded |
| Evidence | Reproducible support for a statement, decision, test result, or current behavior |

## Knowledge Lifecycle

```text
Discover Sources
-> Register and Classify
-> Check Authority, Scope, Version, and Freshness
-> Extract Claims and Constraints
-> Detect Conflicts and Gaps
-> Resolve or Escalate
-> Record Decisions and Safe Assumptions
-> Establish Knowledge Baseline
-> Handoff to Planning and Downstream Phases
-> Revalidate on Change
-> Supersede or Archive
```

Knowledge Resolution is complete only when the relevant knowledge baseline is sufficient for Planning without inventing material product behavior.

## Knowledge Stage Contract

Knowledge uses one lifecycle phase with four ordered stages:

| Stage | Purpose | Required artifact | Exit gate |
|---|---|---|---|
| Collection | Discover, scope, and register material sources | `knowledge-collection.md` | source coverage is sufficient for Resolution |
| Resolution | Normalize claims and resolve conflicts, gaps, decisions, and assumptions | `knowledge-resolution.md` | candidate baseline is ready for Validation |
| Validation | Independently check authority, freshness, traceability, completeness, and consistency | `knowledge-validation.md` | candidate baseline PASS |
| Ready | Gate that activates the validated baseline and permits Planning | state and validated artifacts | `next_action: planning` |

Knowledge Ready is a status and gate, not a separate execution phase. Collection completion does not imply correctness; Resolution completion does not imply approval; only Validation PASS activates the baseline.

Normal routing:

```text
Knowledge Collection
-> Knowledge Resolution
-> Knowledge Validation
-> Knowledge Ready
-> Planning
```

Failure routing:

```text
missing, incomplete, stale, or changed source -> Knowledge Collection
unresolved claim, conflict, decision, or assumption -> Knowledge Resolution
validation evidence defect -> owning Collection or Resolution stage
missing authorized product decision -> Requirement Clarification
source access or tooling failure -> Infrastructure
Validation PASS -> Knowledge Ready
```

## Source Register

For every material source, `knowledge-collection.md` MUST record when available:

- source identifier, title, type, and location
- owning domain and accountable owner
- version, revision, branch, node, endpoint, or artifact identifier
- publication, modification, or retrieval date
- applicable task scope and affected acceptance criteria
- authority status: authoritative, supporting, observed, example, or unknown
- freshness status: current, stale, superseded, or unknown
- access or confidentiality constraints without copying secrets or sensitive data

Secondary summaries are navigation aids. Material decisions SHOULD cite the primary source or explain why only secondary evidence is available.

## Domain Authority

Authority is determined by subject, not by one universal file order:

| Domain | Normal authority | Supporting evidence |
|---|---|---|
| Product intent, scope, and priority | current authorized user decision and approved product requirement | tickets, meeting notes, examples |
| UI behavior and visual specification | approved product requirement plus approved design source | screenshots and current UI behavior |
| API contract | approved API schema or backend contract | client DTOs, mocks, network traces |
| Data model and migration | approved schema and migration decision | entities, Room code, sample data |
| Current implementation behavior | repository revision and reproducible runtime evidence | comments and historical documents |
| Test and release result | executed evidence for the exact revision and environment | prior runs and developer claims |
| Process and artifact format | `AGENTS.md`, active phase rules, and canonical templates | tutorials and examples |
| External platform or regulatory rule | current primary official source | secondary articles and recollection |

One domain source MUST NOT silently redefine another domain. For example, a design cannot silently change an API contract, current code cannot redefine approved product intent, and a passing test cannot prove that the requirement itself is correct.

When two authoritative sources conflict, do not choose by convenience or modification date alone. Record the conflict, impact, possible resolutions, and the authority required to decide it.

## Freshness and Version Rules

- Bind repository knowledge to a branch, commit, or explicit working-tree state when Git is available.
- Bind design, API, build, test, and release evidence to an identifiable version or retrieval time.
- Recheck information likely to change, including external standards, dependencies, endpoints, credentials policy, environment behavior, and release configuration.
- Mark a source `stale`, `superseded`, or `unknown` instead of silently treating it as current.
- A newer source supersedes an older source only when it has authority for the same domain and scope.
- If freshness cannot be established and the uncertainty can change implementation or acceptance, block and request authoritative clarification.

## Conflict, Decision, and Assumption Rules

Each material conflict MUST have an identifier and record:

- conflicting claims and exact sources
- affected requirements, acceptance criteria, interfaces, or data
- severity and delivery impact
- proposed options and tradeoffs
- decision owner, resolution, rationale, and date
- superseded knowledge and required downstream updates

Each material decision SHOULD use a stable identifier such as `KD-001`. A later decision must reference the decision it supersedes.

Assumptions are allowed only when they are reversible, low risk, and do not alter product intent, public contracts, stored data, security, privacy, compliance, or release gates. Record the assumption, evidence, confidence, impact if wrong, validation method, owner, and expiry or recheck trigger. An assumption never becomes a requirement merely because code was written from it.

## Knowledge Baseline and Invalidation

The Knowledge baseline is the set of source versions, resolved claims, decisions, and approved assumptions used by Planning. Record a baseline identifier or dated snapshot in `knowledge-resolution.md` for material work.

Re-enter the Knowledge workflow when any downstream phase discovers:

- a new or changed authoritative source
- a product, design, API, schema, asset, repository, test, or operational conflict
- stale or unverifiable evidence that affects the task direction
- an assumption that expired, failed validation, or became material
- implementation evidence that contradicts the planned understanding
- a release or production signal that reveals a knowledge gap

When the baseline is invalidated, identify impacted acceptance criteria, plans, code, tests, verification, and release decisions. Route to Collection for missing, stale, superseded, or changed sources; route to Resolution for claim, conflict, decision, or assumption defects. Stale approval does not apply to changed scope or evidence.

## Knowledge Handoff and Storage

- Shared product knowledge belongs in the appropriate versioned product, design, API, architecture, or operational document.
- Task-specific evidence and interpretation belong in `knowledge-collection.md`, `knowledge-resolution.md`, and `knowledge-validation.md` under `docs/tasks/<task-id>/`.
- Reusable decisions SHOULD be promoted to the owning shared document or ADR after approval; do not leave durable project knowledge only in chat history.
- Task artifacts MUST link to sources and decision identifiers instead of copying large documents.
- Never store credentials, tokens, personal data, confidential payloads, or unrestricted production data in Knowledge artifacts.

The Validation handoff to Planning MUST state the active baseline, resolved decisions, constraints, safe assumptions, unresolved non-blocking items, blocked items, affected acceptance criteria, and revalidation triggers. Planning and every downstream phase MUST cite the relevant Knowledge decision when it materially affects implementation or verification.

# Workflow

## Intake Flow

Use the Knowledge workflow when a task depends on multiple specifications, design files, API contracts, assets, stale sources, unknown authority, or conflicting evidence.

```text
Task Intake
    |
    +-- Specifications are clear and consistent --> Planning
    |
    +-- Knowledge work required --> Collection --> Resolution --> Validation
                                                            |
                                                            +-- PASS --> Knowledge Ready --> Planning
                                                            +-- FAIL --> owning Knowledge stage
                                                            +-- Blocked --> Requirement Clarification or Infrastructure
```

A small task with one clear requirement may proceed directly to Planning.

The Knowledge workflow is also required when a single source is stale, lacks authority, or cannot support a material implementation decision.

## Success Flow

```text
Task Intake
-> Knowledge Collection, Resolution, and Validation (when required)
-> Knowledge Ready
-> Requirement Baseline
-> Planning and Technical Design
-> Plan and Design Review
-> Implementation
-> Developer Validation
-> Git Commit
-> Code Review
-> CI Quality Gate
-> Independent Verification
-> UAT (when required)
-> Release Approval
-> Authorized Merge or Deployment
-> Post-Deployment Verification
-> Monitoring and Closure
-> Done
```

Requirement Baseline, Technical Design, Code Review, CI, UAT, Post-Deployment Verification, and Monitoring are lifecycle gates, not additional task phases. They are recorded by the owning Planning, Plan Review, Implementation, Verification, or Release phase unless the canonical state contract and phase rules are explicitly extended.

## Failure Flow

Verification MUST classify failures before any code change:

```text
requirement    -> Knowledge Collection, Knowledge Resolution, or Planning
planning       -> Planning
implementation -> Debug -> Re-Implementation -> Developer Validation -> Git Commit -> Verification
environment    -> Infrastructure -> blocked phase or Verification
unknown        -> Investigation -> classified destination
```

Only an `implementation` failure enters Debug automatically.

Failures can be detected at any gate, not only during Verification. The detecting role MUST preserve evidence, create or update the related issue, classify the cause, and route it to the owning phase before work continues.

# Requirement Baseline and Change Control

Planning MUST establish an approved requirement baseline before implementation. The baseline includes:

- task goal, scope, and explicit out-of-scope behavior
- uniquely numbered, observable acceptance criteria
- functional and non-functional requirements
- constraints, dependencies, assumptions, and unresolved decisions
- product owner or approving authority when human approval is required
- target release or priority when supplied

A requirement is ready only when implementation and verification can proceed without inventing material product behavior. Open questions that affect user behavior, public API contracts, stored data, security, privacy, or release risk block Plan Approval.

After the baseline is approved:

- Developer, Reviewer, and Verifier MUST NOT edit it to make an implementation pass.
- A material change MUST be recorded as a `requirement_gap` or change request and return to Knowledge Resolution or Planning.
- Planning MUST update impacted acceptance criteria, analysis, plan, risks, tests, and traceability, followed by a new Plan Review.
- Editorial corrections that do not change meaning may be made with the reason recorded in the task artifacts.

# Technical Design and Plan Review

Technical design is part of Planning and its approval is part of Plan Review. The level of detail MUST be proportional to risk.

Planning MUST address, when applicable:

- architecture boundaries, ownership, dependencies, and reuse decisions
- API request, response, error, compatibility, and versioning contracts
- database schema, Room migration, data retention, and rollback or recovery
- Android lifecycle, permissions, offline behavior, concurrency, background work, and device compatibility
- UI states, accessibility, localization, and design-source alignment
- authentication, authorization, secrets, sensitive data, logging, and threat boundaries
- performance, observability, rollout, and operational risks

Plan Review MUST reject or block a plan when a material design decision, migration path, API contract, security control, test strategy, or rollback expectation is missing. Significant architectural decisions SHOULD be recorded in `analysis.md`, `plan.md`, or a linked ADR when the project uses ADRs.

# Code Review and CI Quality Gate

Code Review occurs after the implementation commit and before independent Verification. It is a required review gate for production-code changes; documentation-only tasks may use a proportionate document review.

The reviewer MUST check:

- requirement and plan alignment
- correctness, architecture ownership, lifecycle safety, error handling, and maintainability
- security, privacy, data migration, API compatibility, and performance risks when applicable
- test adequacy and regression coverage
- absence of unrelated changes, generated noise, secrets, and hidden behavior changes

Review findings MUST be classified as blocking or non-blocking and recorded in a review artifact, pull request, or task evidence. Blocking findings return to Implementation; a requirement or planning gap follows the canonical failure route. The author MUST NOT self-approve when independent review is required by the repository or release policy.

CI MUST run against the committed revision that will be verified. Required CI checks are defined by the repository and `ai/rules/verification/testing.md`, and SHOULD include applicable build, lint or static analysis, unit tests, integration or UI tests, and artifact generation checks. Record the revision, workflow or run identifier, checks executed, result, and relevant failure evidence.

- Required CI failures block Verification PASS and Release.
- A skipped, cancelled, stale, or unavailable required check is `NOT VERIFIED`, not PASS.
- Flaky tests MUST be recorded as issues; rerunning without diagnosis does not erase the original evidence.
- CI or toolchain failures route to Infrastructure only when evidence shows the product change is not the cause.

# Verification, UAT, and Non-Functional Validation

Independent Verification evaluates the approved requirement baseline against the reviewed, committed revision and CI evidence. Each acceptance criterion MUST map to implementation evidence and at least one executed test or other reproducible verification method.

Verification MUST include applicable non-functional checks for:

- Android 9+ and affected device or OS compatibility
- permissions, lifecycle, offline or weak-network behavior, retries, and duplicate submission
- accessibility and localization
- security and privacy controls
- performance, memory, battery, startup, map rendering, and upload behavior
- Room migrations, API compatibility, and recovery behavior

UAT is required when acceptance depends on business workflow, field operation, stakeholder judgment, production-like data, or an external system that automated verification cannot sufficiently represent. Record the UAT owner, environment, scenario, result, evidence, and approval. Missing required UAT is `NOT VERIFIED` and blocks Release.

# Release, Deployment, and Operations

Release MUST identify the exact approved revision and artifact. Before requesting human release authorization, record when applicable:

- version name and version code
- build variant, environment, signing status, and artifact checksum or immutable identifier
- release notes and known limitations
- schema, API, configuration, permission, certificate, and dependency changes
- rollout strategy, rollback trigger, rollback or recovery procedure, and responsible owner
- monitoring signals and post-deployment validation procedure

Human release authorization is distinct from technical Verification PASS. Release readiness does not authorize an Agent to merge, publish, deploy, change production configuration, or use signing credentials.

After an authorized deployment:

1. Confirm the deployed revision, version, environment, and deployment result.
2. Run the planned smoke tests for critical paths.
3. Check defined health signals such as crash or ANR rate, API errors, authentication, map loading, draft persistence, and upload failures when applicable.
4. Record incidents, user-visible regressions, and rollback decisions.
5. Mark the task `done` only when required deployment evidence and the defined monitoring window are complete.

A failed smoke test or harmful production signal opens an incident or issue and routes by cause. Use rollback or recovery when its approved trigger is met; do not improvise a destructive rollback. Emergency hotfixes may expedite review but MUST preserve traceability, CI or documented evidence, independent Verification, and release authorization.

# Traceability and Responsibility

Every task MUST maintain this minimum evidence chain:

```text
Source or Decision
-> Requirement
-> Acceptance Criterion
-> Plan or Design Decision
-> Implementation Revision
-> Test and CI Evidence
-> Verification Result
-> Release Artifact and Decision
-> Deployment and Monitoring Evidence (when deployed)
```

The same person or Agent may perform multiple roles for low-risk work, but the evidence and gate decisions MUST remain separate. Independent review is required when mandated by project policy, security or data risk, migration risk, release policy, or the user.

| Responsibility | Accountable role |
|---|---|
| Requirement meaning and priority | Product owner or authorized requester |
| Plan and technical design | Planning |
| Plan and design approval | Plan Review |
| Implementation and developer validation | Developer |
| Code review decision | Reviewer |
| CI execution and evidence | CI system or Infrastructure owner |
| Acceptance and regression decision | Verifier |
| Business acceptance when required | UAT owner |
| Merge or deployment authorization | Authorized human release owner |
| Post-deployment health and incident response | Release or Operations owner |

# Task Type Routing

Planning MUST classify the task before producing a plan:

| Task type | Purpose | Review |
|---|---|---|
| `feature` | Add new product behavior | Full Plan Review |
| `bugfix` | Restore intended behavior | Plan Review; mini plan allowed |
| `debug` | Find the cause of an unknown failure | Evidence review before implementation |
| `refactor` | Improve internal structure without changing behavior | Plan Review plus behavior-preservation evidence |
| `hotfix` | Urgent production correction | Expedited review; no skipped verification |
| `docs` | Documentation-only change | Proportionate review and validation |

Detailed requirements are defined in `ai/rules/planning/planning.md`.

# Role Routing

| Phase | Required rule | Primary output | Normal next action |
|---|---|---|---|
| Knowledge / Collection | `ai/rules/knowledge/collection.md` | `knowledge-collection.md` | `knowledge_resolution` |
| Knowledge / Resolution | `ai/rules/knowledge/resolution.md` | `knowledge-resolution.md` | `knowledge_validation` |
| Knowledge / Validation | `ai/rules/knowledge/validation.md` | `knowledge-validation.md` | `planning` on PASS |
| Planning | `ai/rules/planning/planning.md` | `analysis.md`, `plan.md`, `state.yaml` | `plan_review` |
| Plan Review | `ai/rules/planning/plan-review.md` | `plan-review.md` | `implementation` |
| Implementation | `ai/rules/implementation/developer.md`, `ai/rules/implementation/coding.md` | code, tests, execution report | `verification` |
| Verification | `ai/rules/verification/verification.md`, `ai/rules/verification/testing.md` | `verification.md` | `release` or failure route |
| Debug | `ai/rules/implementation/debug.md` | `root-cause.md`, `fix-plan.md` | `implementation_debug` |
| Infrastructure | `ai/rules/operations/infrastructure.md` | environment evidence | blocked phase or `verification` |
| Investigation | `ai/rules/operations/investigation.md` | classification evidence | classified destination |
| Release | `ai/rules/operations/release.md` | release decision | `human_release` or `done` |

Every Agent MUST read the latest `state.yaml`, this file, its active stage or phase rule, and the task artifacts required by that rule before acting.

# Gates

```text
Task Sources Identified
-> Knowledge Collected (when required)
-> Knowledge Resolved (when required)
-> Knowledge Validation PASS (when required)
-> Knowledge Ready
-> Requirements Resolved
-> Requirement Baseline Approved
-> Plan Approved
-> Technical Design Approved (when applicable)
-> Implementation Complete
-> Developer Validation Complete or Limitations Recorded
-> Git Commit
-> Code Review Approved
-> CI PASS
-> Verification PASS
-> UAT PASS (when required)
-> Release Approval
-> Authorized Merge or Deployment
-> Post-Deployment Verification PASS (when deployed)
-> Monitoring Window Complete (when required)
-> Done
```

- `NOT VERIFIED` is not PASS and cannot advance to Release.
- Environment limitations may explain missing evidence but never convert it to PASS.
- Verification reviews a committed revision and does not modify production code.
- Merge is not part of Developer or Verifier responsibility and requires explicit authorization.
- A gate marked “when applicable” or “when required” may be `N/A` only with a recorded reason and accountable owner.
- Approval evidence MUST identify the revision, artifact, and scope it applies to; later changes invalidate stale approval.

# Definition of Ready and Done

Implementation is ready to start only when:

- authoritative requirements are resolved and baselined
- acceptance criteria are testable
- dependencies, risks, affected modules, and required environments are identified
- required technical design is documented
- the implementation, validation, regression, and rollback plans are approved
- no blocking question or P0/P1 issue remains assigned to Planning

A task is done only when:

- all applicable gates have PASS, APPROVED, or justified `N/A` evidence
- every acceptance criterion passed; none remains failed or `NOT VERIFIED`
- the verified revision and release artifact are traceable
- no release-blocking issue remains open
- authorized merge or deployment evidence is recorded when it occurred
- required post-deployment smoke tests and monitoring are complete
- task artifacts and `state.yaml` reflect the final outcome

# Issue Management

Read `ai/rules/governance/issue-management.md` whenever a blocker, regression, requirement mismatch, or verification failure is found.

Task state tracks the main workflow. `issue-log.md` tracks individual problems.

Canonical issue-to-verification mapping:

| Issue category | Verification category | Route |
|---|---|---|
| `requirement_gap` | `requirement` | `knowledge_collection`, `knowledge_resolution`, or `planning` |
| `planning_gap` | `planning` | `planning` |
| `implementation_regression` | `implementation` | `debug` |
| `verification_failure` | classification required | route by classified cause |
| `environment` | `environment` | `infrastructure` |
| `unknown` | `unknown` | `investigation` |
| `enhancement_request` | not a failure | backlog or separate task |

Priority guidance:

- `P0`: core flow broken, data/security risk, or broad regression
- `P1`: major flow blocked; workaround may exist
- `P2`: local flow, edge case, or UI degradation
- `P3`: non-blocking cleanup or improvement

# State Contract

`ai/templates/task/state.yaml` is the canonical state shape. Use a mapping for `task`, never a scalar.

```yaml
task:
  id: TYG-205
  type: feature

phase: planning
status: plan_in_progress
next_action: planning
```

Use one-line scalar values for `next_action`. Do not invent new phases, statuses, categories, or next actions without updating the canonical template and the owning phase rule.

Canonical Knowledge state:

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

On Validation PASS, use `status: knowledge_ready`, set `knowledge.status: ready`, and route to Planning. Existing historical tasks that use `phase: knowledge_resolution` or a `knowledge_resolution` mapping remain readable, but MUST be normalized to the canonical `knowledge` shape on their next Knowledge transition.

# Autonomy and Stop Rules

Continue with reasonable, reversible assumptions when they do not change product behavior or acceptance criteria. Record material assumptions in `analysis.md`.

Stop the active phase and record a blocker when:

- required task artifacts are missing
- requirements or authoritative sources conflict materially
- an action may cause data loss, expose secrets, change a public contract, or require destructive migration
- the approved plan does not cover a necessary production-code change
- the current branch or working tree makes isolation unsafe
- required validation cannot run and no sufficient alternative evidence exists

Ask only for the smallest missing decision. Do not ask for information that can be obtained safely from the repository or available evidence.

# Validation and Evidence

After changes, run the most relevant available validation:

- targeted unit or UI tests for changed behavior
- build and static checks for affected modules
- regression checks for nearby behavior
- a minimal smoke test when full validation is impractical

Record commands, results, failures, and limitations. If a check cannot run, mark it `NOT VERIFIED`, explain why, and name the next best check.

# Allowed Side Effects

- Knowledge Collection, Knowledge Resolution, Knowledge Validation, Planning, Plan Review, Verification, Investigation, and Infrastructure MUST NOT modify production code.
- Implementation, including re-entry from an approved Debug fix plan, may modify only approved source, test, and task artifact scope.
- Release may update release records but MUST NOT merge, publish, deploy, or change production without explicit authorization.
- All phases MUST preserve unrelated user changes.

# Reuse Rules

Before creating a component, API abstraction, or asset wrapper, check for an existing equivalent in the repository.

- Prefer reuse when behavior, ownership, lifecycle, and design constraints match.
- Do not generalize a component solely for hypothetical future reuse.
- Cross-project extraction requires an explicit requirement or separate approved task.
- Refactoring for reuse must preserve behavior and remain separate from unrelated feature or bug-fix commits.

# Final Report

At the end of a phase, report:

- completed work and artifacts
- validation performed and results
- assumptions or unverified items
- blockers, if any
- resulting `phase`, `status`, and `next_action`

# Global Rules

- Do not modify approved requirements outside Planning or an authorized Knowledge decision process.
- Do not lower acceptance criteria.
- Do not skip required validation or report unexecuted checks as PASS.
- Do not perform unrelated refactoring.
- Do not invent evidence, specifications, API behavior, or completion claims.
