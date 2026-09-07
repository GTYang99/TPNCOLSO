# Knowledge Resolution

## Question and Scope
- Task question: 如何將正式 Figma UI 轉為穩定 foundation contract，同時保留 debug direct-login 與 production auth 的隔離？
- In scope: semantic design mapping、asset ownership、public component/session contracts、implementation entry conditions。
- Out of scope: production API／Token、map UI、feature navigation implementation。

## Candidate Knowledge Baseline
- Baseline ID: `KB-UI-001-FOUNDATION-R1`
- Created date: 2026-09-07 (Asia/Taipei)
- Supersedes: `knowledge.not_required` planning assumption and provisional-Figma plan language
- Status: validated

## Source Register

| Source ID | Title / Location | Type | Domain / Owner | Version / Date | Authority | Freshness | Scope / AC | Notes |
|---|---|---|---|---|---|---|---|---|
| SRC-UI001-001 | Current requester decisions | authorized decision | Product／Design / requester | 2026-09-07 | authoritative | current | all UI-001 AC | Current Figma is the formal UI; debug direct login is required while API is unavailable. |
| SRC-UI001-002 | Figma `HRbRsw6HoNBUCtaieX8xUM`, entry `2905:2680`, indexed nodes | approved design source | Design / requester | retrieved and approved 2026-09-07 | authoritative | current | AC-UI001-009–010 | Formal visual and interaction baseline. |
| SRC-UI001-003 | `docs/design/app-ui-requirements.md` | approved design specification | Design / requester | 2026-09-07 working tree | authoritative | current | component/accessibility | Normalized source index and requirements. |
| SRC-UI001-004 | Product specification set | product specification | Product / requester | 2026-09-07 working tree | authoritative by domain | current | platform/roles/boundaries | Android 9+ and role names. |
| SRC-UI001-005 | Current Android working tree | implementation evidence | Repository | 2026-09-07; no commit | authoritative for current behavior | current | current state | Starter only; Git environment unavailable. |
| SRC-UI001-006 | UI roadmap baseline | process decision | Process / repository | 2026-09-07 | authoritative for sequencing | current | consumer handoff | UI-001 precedes auth and map shell. |

## Material Claims and Traceability

| Claim ID | Statement | Supporting Sources | Contradicting Sources | Authority / Confidence | Requirement / AC | Status |
|---|---|---|---|---|---|---|
| KCL-UI001-001 | Current Figma content is the approved formal UI baseline. | SRC-UI001-001–003 | Historical development label | Design / high | FR-UI001-006, AC-UI001-009–010 | resolved |
| KCL-UI001-002 | UI-001 must expose reusable semantic tokens/components without feature-owned state or raw values. | SRC-UI001-002–003, SRC-UI001-006 | None | high | AC-UI001-008–010 | resolved |
| KCL-UI001-003 | Debug direct login may create only an in-memory fake identity and must be absent from release. | SRC-UI001-001, SRC-UI001-004 | None | high | AC-UI001-001–006 | resolved |
| KCL-UI001-004 | Implementation cannot satisfy repository traceability without Git metadata. | SRC-UI001-005 | None | repository / high | implementation readiness | resolved as environment blocker |

## Conflicts and Gaps

| Conflict ID | Claims / Sources | Domain | Impact / Severity | Options | Decision Owner | Status |
|---|---|---|---|---|---|---|
| KCF-UI001-001 | Historical development-only Figma label vs current formal approval | Design authority | P1 visual implementation | wait for duplicate revision / approve current snapshot | Requester | resolved by SRC-UI001-001 |
| KCF-UI001-002 | Shared vs feature-only asset ownership | Architecture | P2 duplicate assets | foundation owns all / consumer owns all / split by reuse | Planning | resolved by KD-UI001-003 |

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

## Assumptions

| Assumption ID | Statement / Evidence | Confidence | Impact if Wrong | Owner | Validation Method | Expiry / Trigger | Safe for Planning |
|---|---|---|---|---|---|---|---|
| KA-UI001-001 | Figma asset bytes remain retrievable until Implementation captures them. | medium | Asset extraction may require design-owner re-export; visual contract remains unchanged. | Design owner | Reopen node and save exact bytes before coding asset consumers. | Implementation start | yes |

## Resolved Items
- Formal design authority, semantic-token mapping and asset ownership are resolved.
- Debug/release session boundary is resolved.

## Unresolved Non-blocking Items
- None for product or design intent.

## Blocking Items
- Git metadata remains an environment blocker for Implementation, commit and CI traceability.

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
- Candidate baseline: `KB-UI-001-FOUNDATION-R1`
- Decisions and constraints Validation must check: KD-UI001-001–003, formal Figma authority, debug/release isolation, asset ownership.
- Safe assumptions: KA-UI001-001.
- Required follow-up evidence: exact asset retrieval, implementation tests, Git branch/commit/CI evidence.
- Material blockers: no for Planning; yes for Implementation environment.
- Ready for Validation: yes
