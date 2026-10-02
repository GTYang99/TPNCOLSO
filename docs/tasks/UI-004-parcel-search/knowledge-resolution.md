# Knowledge Resolution

## Question and Scope
- Task question: 建立 `UI-004-parcel-search` 土地查詢與摘要的 candidate Knowledge baseline。
- In scope: 查詢輸入、查詢入口、結果定位、查無資料、土地摘要 Sheet 與 UI-003 handoff。
- Out of scope: production API、土地詳情、調查表單、WMTS、通知已讀／分頁與後端錯誤合約。

## Candidate Knowledge Baseline
- Baseline ID: `KB-UI-004-PARCEL-SEARCH-R2`
- Created date: 2026-10-02 (Asia/Taipei)
- Supersedes: `KB-UI-004-PARCEL-SEARCH-R1` for entry placement and successful-result presentation
- Status: validated

## Source Register
| Source ID | Title / Location | Type | Domain / Owner | Version / Date | Authority | Freshness | Scope / AC | Notes |
|---|---|---|---|---|---|---|---|---|
| SRC-UI004-001 | `/Users/a10362/Desktop/markdown file/tp_req_ui.md` | requester UI source index | Product / Design / requester | 2026-09-08 | authoritative for source inventory | current | parcel search Figma location | Node `4952:14932`. |
| SRC-UI004-002 | Figma node `4952:14932` | design source | Design / requester | linked 2026-09-08 | authoritative for visible UI | current | entered, result sheet, no-result states | MCP confirmed three `402 x 874` Android screen frames on 2026-09-08. |
| SRC-UI004-003 | `docs/design/app-ui-requirements.md` | design specification | Design | 2026-09-08 | authoritative | current | search screen inventory and accessibility | Defines `4952:14932` and search behavior context. |
| SRC-UI004-004 | `docs/tasks/UI-0907-app-ui-requirements/requirement.md` | parent requirement | Product / Design | 2026-09-08 | authoritative | current | FR-005 | Search inputs, result summary and no-result behavior. |
| SRC-UI004-005 | `docs/tasks/README.md` and `docs/tasks/UI-ROADMAP-0907/plan.md` | task sequencing | Process | 2026-09-08 | authoritative | current | UI-004 fake data boundary | API deferred to INT-002. |
| SRC-UI004-006 | `docs/product/land-survey-115/5 API規格.md` | API specification | Product / API | 2026-09-08 | supporting / incomplete | current | future integration only | No production contract is fixed in this baseline. |
| SRC-UI004-007 | Requester clarification in current task conversation | explicit product decision | Product / requester | 2026-10-02 | authoritative for UI-004 entry placement and matched-result presentation | current | top-toolbar search/filter entry, lower-right location-only action, bottom card after successful search | Direct requester instruction. |

Figma MCP inspection on 2026-09-08 confirmed node `4952:14932` contains three child screen frames, each `402 x 874` (`4952:14938`, `4952:14933`, `4987:4490`).

## Material Claims and Traceability
| Claim ID | Statement | Supporting Sources | Contradicting Sources | Authority / Confidence | Requirement / AC | Status |
|---|---|---|---|---|---|---|
| KCL-UI004-001 | UI-004 is entered from the full-map page's top-toolbar search/filter callback and presents result/summary UI. | SRC-UI004-003, 004, 007 | Parent FR-003 legacy placement wording | Product + requester / high | FR-005, parent AC-006/007, AC-UI004-006 | resolved by explicit requester clarification |
| KCL-UI004-002 | Search accepts land number, location or land-number keyword; successful result keeps context, locates parcel and opens a card-style page from the bottom. | SRC-UI004-004, 007 | None | Product + requester / high | FR-005, AC-UI004-002 | resolved for UI-only flow |
| KCL-UI004-003 | No-result state keeps the entered keyword and displays a no-result state without silently clearing the current container. | SRC-UI004-004 | None | Product / high | FR-005 | resolved |
| KCL-UI004-004 | UI-004 uses fake parcel data; production query／detail API remains a later integration task. | SRC-UI004-005, 006; requester decision 2026-09-08 | None | Process / high | UI-004 API policy | resolved |
| KCL-UI004-005 | The lower-right map-shell control is location only; search/filter entry is in the top toolbar. | SRC-UI004-007 | Parent FR-003 legacy placement wording | Requester / high | AC-UI004-006 | resolved by current requester direction |

## Conflicts and Gaps
| Conflict ID | Claims / Sources | Domain | Impact / Severity | Options | Decision Owner | Status |
|---|---|---|---|---|---|---|
| KCF-UI004-001 | Figma visible search states exist; API contract remains absent. | Design / API | P2 for integration validation | Use confirmed Figma dimensions for visible UI, fake data for UI behavior, defer API | Requester / Planning | resolved for visual dimensions; API remains deferred |
| KCF-UI004-002 | Search affordance is visible in UI-003 while search/result content belongs to UI-004. | Task boundary | P1 if duplicated | UI-003 emits entry callback; UI-004 owns search/result container | Product / Architecture | resolved |
| KCF-UI004-003 | Parent FR-003 can be read as placing a search/filter entry at lower right; the requester specifies top-toolbar entry and lower-right location only. | Product behavior / task boundary | Material placement mismatch | Follow the explicit current requester direction for UI-004 and record it in the child Requirement; UI-003 retains shell control ownership. | Requester | resolved 2026-10-02 |

## Decision Records
### KD-UI004-001
- Decision: Establish UI-004 as a UI-only parcel search flow using the Figma search states and fake parcel data. Keep production API contract, errors and DTOs deferred to INT-002.
- Alternatives: Wait for API; invent provisional production DTOs; implement search inside UI-003.
- Source / Conflict IDs: SRC-UI004-002, 004, 005, 006; KCF-UI004-001, 002
- Rationale: Matches the roadmap and requester decision that API contracts are added later.
- Approver and date: Requester and roadmap, 2026-09-08.
- Affected requirements / AC / artifacts: UI-004 requirement, fake parcel fixtures, UI-003 callback handoff.
- Supersedes: None.
- Revalidation trigger: Figma or API contract changes.

### KD-UI004-002
- Decision: Use Figma MCP-confirmed `402 x 874` screen frames as the UI-004 visual baseline for entered, result and no-result states.
- Source / Conflict IDs: SRC-UI004-002; KCF-UI004-001
- Rationale: Confirms the Android reference frame without converting deferred API behavior into UI requirements.
- Approver and date: Requester and Figma MCP inspection, 2026-09-08.
- Revalidation trigger: Figma node `4952:14932` changes.

### KD-UI004-003
- Decision: For UI-004, the search/filter entry is in the full-map page's top toolbar; the lower-right map control is location only; a successful search opens a card-style page from the bottom.
- Alternatives: Interpret the older parent FR-003 wording as a second lower-right search/filter entry.
- Source / Conflict IDs: SRC-UI004-007; KCF-UI004-003.
- Rationale: This is the requester's explicit clarification of the intended UI-004 interaction and supersedes the prior lower-right interpretation for this child task.
- Approver and date: Requester, 2026-10-02.
- Affected requirements / AC / artifacts: UI-004 Requirement, AC-UI004-002 / 006, Plan, UI-003 callback wiring boundary.
- Supersedes: Prior UI-004 planning interpretation of the lower-right location control as a search/filter entry.
- Revalidation trigger: Requester changes entry placement or result presentation, or parent requirement is revised to reconcile FR-003 wording.

## Assumptions
| Assumption ID | Statement / Evidence | Confidence | Impact if Wrong | Owner | Validation Method | Expiry / Trigger | Safe for Planning |
|---|---|---|---|---|---|---|---|
| KA-UI004-001 | Figma node `4952:14932` remains the current visual source for parcel search. | medium | Visual criteria may change. | Design | MCP retrieval before Plan Review. | Figma revision | yes |

## Resolved Items
- UI-004 owns search input, result state, no-result state and parcel summary entry using fake data.
- UI-003 owns the map-shell search affordance and handoff callback.
- Production API contract remains deferred.

## Unresolved Non-blocking Items
- Production API schema, error mapping and pagination remain deferred by decision.
- Exact production API schema, error mapping and pagination remain deferred by decision.

## Blocking Items
- None for Knowledge Validation.
- UI-003 prerequisite and later API integration block implementation integration, not this UI baseline.

## Downstream Impact
- Requirements / AC: Define entered, result, no-result and summary-sheet criteria.
- Plan / design: Retrieve Figma MCP structure and use fake parcel fixtures.
- Code / data / API: No production DTO or endpoint; keep source replaceable.
- Tests / verification: Cover keyword retention, result location, no-result display and summary handoff.
- Release / operations: No API integration or release claim from this UI task.

## Revalidation Triggers
- Figma node `4952:14932` changes; requester changes page ownership; INT-002 publishes API contract.

## Validation Handoff
- Candidate baseline: `KB-UI-004-PARCEL-SEARCH-R2`
- Decisions and constraints Validation must check: KD-UI004-001 through KD-UI004-003, fake-data boundary, UI-003 handoff and deferred API scope.
- Safe assumptions: KA-UI004-001.
- Required follow-up evidence: structured Figma MCP capture.
- Material blockers: no.
- Ready for Validation: yes
