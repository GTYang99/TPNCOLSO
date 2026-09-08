# Knowledge Resolution

## Question and Scope
- Task question: 如何將正式 Figma UI 轉為穩定 foundation contract，同時保留 debug direct-login 與 production auth 的隔離？
- In scope: semantic design mapping、asset ownership、public component/session contracts、implementation entry conditions。
- Out of scope: production API／Token、map UI、feature navigation implementation。

## Candidate Knowledge Baseline
- Baseline ID: `KB-UI-001-FOUNDATION-R5`
- Created date: 2026-09-08 (Asia/Taipei)
- Supersedes: `KB-UI-001-FOUNDATION-R3` (rejected after REQ-UI001-002 contract mismatch)
- Status: draft

> Re-resolution update, 2026-09-08: `REQ-UI001-002` contract fix is present in the working tree; compile/test evidence remains pending because Java Runtime is unavailable.

> Re-resolution update, 2026-09-08: requester selected Android／Material system icons and platform controls for UI-001 v0.1; custom icon asset extraction and pixel matching are out of scope.

## Source Register

| Source ID | Title / Location | Type | Domain / Owner | Version / Date | Authority | Freshness | Scope / AC | Notes |
|---|---|---|---|---|---|---|---|---|
| SRC-UI001-001 | Current requester decisions | authorized decision | Product／Design / requester | 2026-09-07 | authoritative | current | all UI-001 AC | Current Figma is the formal UI; debug direct login is required while API is unavailable. |
| SRC-UI001-002 | Figma `HRbRsw6HoNBUCtaieX8xUM`, entry `2905:2680`, indexed nodes | approved design source | Design / requester | retrieved and approved 2026-09-07 | authoritative | current | AC-UI001-009–010 | Formal visual and interaction baseline. |
| SRC-UI001-003 | `docs/design/app-ui-requirements.md` | approved design specification | Design / requester | 2026-09-07 working tree | authoritative | current | component/accessibility | Normalized source index and requirements. |
| SRC-UI001-004 | Product specification set | product specification | Product / requester | 2026-09-07 working tree | authoritative by domain | current | platform/roles/boundaries | Android 9+ and role names. |
| SRC-UI001-005 | Current Android repository | implementation evidence | Repository | bootstrap `5221898`; branch `feature/UI-001-foundation-compose` | authoritative for current behavior | current | current state | New Compose starter; baseline unit test/debug build PASS. |
| SRC-UI001-006 | UI roadmap baseline | process decision | Process / repository | 2026-09-07 | authoritative for sequencing | current | consumer handoff | UI-001 precedes auth and map shell. |
| SRC-UI001-007 | `docs/architecture/overview.md` | initial architecture baseline | Architecture / requester + Planning | 2026-09-07 | authoritative | current | all implementation AC | New single-module Compose MVVM project; supersedes unrelated copied architecture. |
| SRC-UI001-008 | `app/src/debug/java/com/example/tp_ncolso_android/DebugSessionController.kt` and updated `DebugSessionOwner.kt` | implementation evidence | Repository / UI-001 | working tree 2026-09-08 | current | current | debug-only contract | Interface now matches foundation contract; compile evidence not verified. |
| SRC-UI001-009 | `docs/tasks/UI-001-foundation/foundation-contract.md` | foundation contract | Architecture / UI-001 | working tree 2026-09-08 | authoritative candidate | current | tokens, components, session and AppRoot handoff | Defines consumer-facing boundaries; validation remains pending. |
| SRC-UI001-010 | Current requester decision | authorized design/implementation decision | Product／Design / requester | 2026-09-08 | authoritative | current | icon policy, AC-UI001-010 | UI-001 v0.1 uses Android／Material defaults; no custom icon asset matching. |

## Material Claims and Traceability

| Claim ID | Statement | Supporting Sources | Contradicting Sources | Authority / Confidence | Requirement / AC | Status |
|---|---|---|---|---|---|---|
| KCL-UI001-001 | Current Figma content is the approved formal UI baseline. | SRC-UI001-001–003 | Historical development label | Design / high | FR-UI001-006, AC-UI001-009–010 | resolved |
| KCL-UI001-002 | UI-001 must expose reusable semantic tokens/components without feature-owned state or raw values. | SRC-UI001-002–003, SRC-UI001-006 | None | high | AC-UI001-008–010 | resolved |
| KCL-UI001-003 | Debug direct login may create only an in-memory fake identity and must be absent from release. | SRC-UI001-001, SRC-UI001-004 | None | high | AC-UI001-001–006 | resolved |
| KCL-UI001-004 | New-project authorization permits a canonical bootstrap commit and dedicated task branch without recovering legacy history. | SRC-UI001-001, 005 | Earlier unknown provenance | repository / high | implementation readiness | resolved by Infrastructure |
| KCL-UI001-005 | This is a new Compose MVVM project with no legacy architecture to preserve. | SRC-UI001-001, 005, 007 | Superseded TaoYuanGutter architecture text | Product/Architecture / high | plan architecture and affected files | resolved |
| KCL-UI001-006 | Debug source exposes `DebugSessionController : AppSessionOwner`, and `DebugSessionOwner` implements it with `override startDebugSession`. | SRC-UI001-008 | Previous working-tree implementation | Architecture / high | AC-UI001-005, 008 | resolved in source; compile evidence pending |
| KCL-UI001-007 | UI-001 v0.1 icon affordances use Android／Material defaults; custom icon assets are out of scope. | SRC-UI001-009–010 | Prior custom icon asset plan | Product/Design / high | FR-UI001-006, AC-UI001-009–010 | resolved |

## Conflicts and Gaps

| Conflict ID | Claims / Sources | Domain | Impact / Severity | Options | Decision Owner | Status |
|---|---|---|---|---|---|---|
| KCF-UI001-001 | Historical development-only Figma label vs current formal approval | Design authority | P1 visual implementation | wait for duplicate revision / approve current snapshot | Requester | resolved by SRC-UI001-001 |
| KCF-UI001-002 | Shared vs feature-only asset ownership | Architecture | P2 duplicate assets | foundation owns all / consumer owns all / split by reuse | Planning | resolved by KD-UI001-003 |
| KCF-UI001-003 | Copied TaoYuanGutter/ViewBinding architecture vs new Compose MVVM repository | Architecture | P1 wrong implementation direction | preserve legacy text / establish TP_NCOLSO baseline | Requester + Planning | resolved by KD-UI001-004 |
| KCF-UI001-004 | Foundation contract required a named debug interface absent from prior source. | Implementation contract | P1 UI-002 handoff | Add interface and implement it; verify compile/tests | UI-001 / Planning | resolved in working tree; verification pending |

## Decision Records

### KD-UI001-001

- Decision: UI-001 provides a replaceable AppRoot/session contract and debug-only direct login without production Token or API behavior.
- Alternatives: wait for API; embed fake auth in feature screens.
- Source / Conflict IDs: SRC-UI001-001, 004, 006
- Rationale: Unblocks protected-screen development while preserving production auth ownership.
- Approver and date: Requester, 2026-09-07。
- Affected requirements / AC / artifacts: FR-UI001-001–005, AC-UI001-001–008, `foundation-contract.md`。
- Supersedes: None.
- Revalidation trigger: production session/navigation contract changes.

### KD-UI001-002

- Decision: Figma file `HRbRsw6HoNBUCtaieX8xUM`, entry node `2905:2680` and the indexed nodes retrieved on 2026-09-07 are the formal UI implementation and visual Verification baseline.
- Alternatives: retain provisional Material values; wait for another identical revision.
- Source / Conflict IDs: SRC-UI001-001–003, KCF-UI001-001
- Rationale: Requester explicitly confirmed the current content is the formal UI and will not differ from a later delivery.
- Approver and date: Requester, 2026-09-07。
- Affected requirements / AC / artifacts: FR-UI001-006, AC-UI001-009–010, shared design specification, UI-002 baseline.
- Supersedes: provisional/development-only Figma classification.
- Revalidation trigger: Figma content, node index, or approval scope changes.

### KD-UI001-003

- Decision: UI-001 owns semantic tokens and reusable brand assets; consumer tasks own feature-only illustrations/assets, with all files registered by Figma node and retrieval date.
- Alternatives: UI-001 owns every asset; each feature duplicates shared assets.
- Source / Conflict IDs: SRC-UI001-002–003, 006, KCF-UI001-002
- Rationale: Preserves reuse without turning foundation into a feature asset dump.
- Approver and date: Planning, 2026-09-07。
- Affected requirements / AC / artifacts: AC-UI001-008–010, `foundation-contract.md`, `docs/assets/app-ui-assets.md`, UI-002 plan.
- Supersedes: unresolved asset ownership in UI-002 R4.
- Revalidation trigger: an asset is used by multiple features or design ownership changes.

### KD-UI001-004

- Decision: TP_NCOLSO is a new single-module Kotlin/Compose Material 3 application using MVVM and feature/foundation ownership; legacy TaoYuanGutter classes and libraries are not migration constraints.
- Alternatives: Preserve copied ViewBinding/controller architecture; infer a legacy migration.
- Source / Conflict IDs: SRC-UI001-001, 005, 007, KCF-UI001-003
- Rationale: Requester explicitly confirmed the project starts from zero, matching the current Compose starter repository.
- Approver and date: Requester, 2026-09-07。
- Affected requirements / AC / artifacts: architecture overview, UI-001 plan, UI-002 dependency assumptions, all implementation paths.
- Supersedes: previous `docs/architecture/overview.md` content.
- Revalidation trigger: approved module or architecture strategy changes.

### KD-UI001-005

- Decision: Accept the explicit debug-only `DebugSessionController : AppSessionOwner` interface and `DebugSessionOwner` implementation as the corrected R4 contract evidence. Compile readiness remains unverified until Java-enabled build/test passes.
- Source / Conflict IDs: SRC-UI001-008; KCF-UI001-004
- Rationale: Source now matches the documented debug boundary while preserving main/release isolation.
- Approver and date: Requester correction, 2026-09-08.
- Revalidation trigger: interface signature, source-set placement or session ownership changes.

### KD-UI001-006

- Decision: UI-001 v0.1 uses Android／Material default icons and platform controls; custom icon asset extraction and pixel matching are not part of UI-001.
- Source / Conflict IDs: SRC-UI001-009–010, KCL-UI001-007
- Rationale: Requester explicitly selected system-provided iconography while retaining semantics, state behavior and 48dp target requirements.
- Approver and date: Requester, 2026-09-08.
- Affected requirements / AC / artifacts: FR-UI001-006, AC-UI001-009–010, requirement, plan revision 5, foundation contract and asset manifest.
- Revalidation trigger: requester/design decision to require custom iconography or a breaking component API change.

## Assumptions

| Assumption ID | Statement / Evidence | Confidence | Impact if Wrong | Owner | Validation Method | Expiry / Trigger | Safe for Planning |
|---|---|---|---|---|---|---|---|
| KA-UI001-001 | Figma asset bytes remain retrievable until Implementation captures them. | medium | Asset extraction may require design-owner re-export; visual contract remains unchanged. | Design owner | Reopen node and save exact bytes before coding asset consumers. | Implementation start | yes |

## Resolved Items
- Formal design authority, semantic-token mapping, asset ownership and new-project architecture are resolved.
- Debug/release session boundary is resolved.

## Unresolved Non-blocking Items
- None for product or design intent.

## Blocking Items
- None for Implementation entry after bootstrap branch/commit verification; CI remote execution remains a later Verification/Release gate.

## Downstream Impact
- Requirements / AC: AC-UI001-010 adds design-source traceability; no prior AC is reduced.
- Plan / design: provisional token work is replaced by approved Figma mapping and asset manifest work.
- Code / data / API: no production API or Token scope is added.
- Tests / verification: representative visual evidence must cite approved nodes/retrieval date.
- Release / operations: release bypass isolation remains mandatory; Git/CI evidence remains required.

## Revalidation Triggers
- Approved Figma content or node index changes.
- UI-001 public component/session contract changes.
- Repository branch, architecture, minSdk or dependency state changes.

## Validation Handoff
- Candidate baseline: `KB-UI-001-FOUNDATION-R5`
- Decisions and constraints Validation must check: KD-UI001-001–004, formal Figma authority, new-project Compose MVVM architecture, debug/release isolation, asset ownership.
- Safe assumptions: KA-UI001-001.
- Required follow-up evidence: exact asset retrieval, implementation tests, Git branch/commit/CI evidence.
- Material blockers: no Knowledge claim blocker; compile/test evidence remains `NOT VERIFIED` because Java Runtime is unavailable.
- Ready for Validation: yes
