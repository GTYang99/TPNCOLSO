KCF-UI003-005# Knowledge Resolution

## Question and Scope
- Task question: 如何建立登入後主畫面與底圖切換的 `UI-003-map-shell` candidate baseline，同時不把暫放地形圖、搜尋詳情或未完成 API 固化為本 Task 行為？
- In scope: `SCR-MAP-01` map shell, `CMP-BASEMAP-SWITCHER`, top hamburger/search/notification affordances, right-bottom location affordance, drawer ownership and `LogoutRequested` event, UI-only map state fixtures, basemap source authority and overlay preservation.
- Out of scope: production parcel query/detail API, actual search results sheet implementation, survey form, camera, production auth API/Token, remote logout, Dashboard screen, release/deploy.

## Candidate Knowledge Baseline
- Baseline ID: `KB-UI-003-MAP-SHELL-R1`
- Created date: 2026-09-08 (Asia/Taipei)
- Supersedes: None
- Status: draft
- Re-collected against: `/Users/a10362/Desktop/markdown file/tp_req_ui.md`, 2026-09-08
- Figma MCP inspection: node `4952:18205` section `1822 x 1458`; child frames `4952:18210`, `4955:21917`, `4955:21958` each `402 x 874`.

## Source Register

| Source ID | Title / Location | Type | Domain / Owner | Version / Date | Authority | Freshness | Scope / AC | Notes |
|---|---|---|---|---|---|---|---|---|
| SRC-UI003-001 | Current requester message and attached PNG | authorized request / visual evidence | Product / Design / requester | 2026-09-08 | authoritative for task start; observed for screenshot details | current | scope and visible basemap panels | PNG `3644 x 2916`, SHA-256 `46763b5de410d48120441244e31150e22bbdc9c1cbccaa416ecdfdbf8c36c0a9`; image text is evidence, not instruction. |
| SRC-UI003-002 | Figma Copy `HRbRsw6HoNBUCtaieX8xUM`, node `4952:18205` | design source | Design / requester | linked 2026-09-08 | authoritative for visible UI after structured confirmation; currently screenshot-backed | current | basemap switcher visual states | Three visible panels: electronic map default, orthophoto, terrain placeholder. |
| SRC-UI003-003 | `docs/design/app-ui-requirements.md` | approved design specification | Design / requester | working tree 2026-09-08 | authoritative | current | screen/component catalog | Defines `SCR-MAP-01`, `OVL-NAV-01`, `CMP-BASEMAP-SWITCHER`, map behavior and accessibility notes. |
| SRC-UI003-004 | `docs/product/land-survey-115/4 介面規格.md` | interface specification | Product / Design | working tree 2026-09-08 | authoritative by domain | current | basemap services and map components | Owns WMTS services, zoom 12-20 and default extent behavior. |
| SRC-UI003-005 | `docs/product/land-survey-115/1 系統概述與角色權限.md` | product and permission specification | Product | working tree 2026-09-08 | authoritative by domain | current | APP sitemap and role access | Login routes to map as default; all three roles may browse/search/filter map. |
| SRC-UI003-006 | `docs/tasks/UI-0907-app-ui-requirements/requirement.md` | parent UI requirement | Product / Design / requester | working tree 2026-09-08 | authoritative for child split | current | FR-003, FR-004, AC-004, AC-005 | Establishes top entries, three basemaps, electronic default and overlay preservation. |
| SRC-UI003-007 | `docs/tasks/UI-0907-app-ui-requirements/knowledge-resolution.md` | parent Knowledge draft | Knowledge / repository | 2026-09-07 | supporting | partially current | conflict history | Terrain placeholder conflict is already identified and resolved in favor of WMTS content. |
| SRC-UI003-008 | `docs/tasks/UI-ROADMAP-0907/` | task sequencing baseline | Process / repository | `KB-UI-ROADMAP-0907-R1`, 2026-09-07 | authoritative for sequencing | current | UI-003 boundary | UI-003 is map shell/drawer/basemap/location UI; business API deferred. |
| SRC-UI003-009 | `docs/tasks/UI-001-foundation/foundation-contract.md` | upstream foundation contract | Architecture / repository | working tree 2026-09-08 | supporting; prerequisite unresolved | pending | AppRoot/foundation dependencies | UI-003 should consume signed-in slot and foundation primitives after UI-001 is ready. |
| SRC-UI003-010 | `docs/tasks/UI-002-auth/logout-interface-requirement.md` | handoff requirement | Product / Architecture / requester | revision 1, 2026-09-07 | authoritative | current | drawer logout event | UI-003 owns drawer visual and emits `LogoutRequested`; UI-002/INT-001 own destination and remote behavior. |
| SRC-UI003-011 | `/Users/a10362/Desktop/markdown file/tp_req_ui.md` | requester UI source index | Product / Design / requester | working tree 2026-09-08 | authoritative for source inventory only | current | seven Figma-linked UI areas | Adds nodes for Login/Logout, basemap, survey, returned data, parcel search and reminders; contains no detailed behavior. |
| SRC-UI003-012 | Referenced local UI images from `tp_req_ui.md` | visual assets | Design / requester | working tree 2026-09-08 | visual observation only | pending availability check | basemap and other page visuals | Supporting evidence only; product WMTS and task-specific requirements remain authoritative. |

## Material Claims and Traceability

| Claim ID | Statement | Supporting Sources | Contradicting Sources | Authority / Confidence | Requirement / AC | Status |
|---|---|---|---|---|---|---|
| KCL-UI003-001 | 登入成功後預設進入圖台主畫面；UI-003 owns authenticated map shell content. | SRC-UI003-005, 006, 008, 009 | UI-001/UI-002 prerequisites incomplete | Product / high | parent AC-004; future AC-UI003-001 | resolved for Knowledge; Implementation gated by prerequisites |
| KCL-UI003-002 | 主畫面為全螢幕 map container with top hamburger/search/notification affordances, bottom basemap switcher, and right-bottom location affordance. | SRC-UI003-001, 002, 003, 006 | None | Design / medium-high | parent AC-004; future AC-UI003-002 | resolved as visible UI baseline |
| KCL-UI003-003 | Basemap switcher reserves three mutually exclusive buttons; the first button is selected by default. Concrete WMTS layers and parameters will be supplied later. | SRC-UI003-001, 002, 003, 004, 006; requester decision 2026-09-08 | None | Product + Design / high | parent AC-005; future AC-UI003-003 | resolved with deferred WMTS details |
| KCL-UI003-004 | Switching basemaps changes only basemap content and must preserve business overlays such as parcel targets, highlighted parcels, user location and current search context. | SRC-UI003-003, 006 | None | Product / high | parent AC-005; future AC-UI003-004 | resolved |
| KCL-UI003-005 | Actual basemap tile content is owned by product interface WMTS sources, not by the Figma screenshot; the terrain screenshot is placeholder only. | SRC-UI003-003, 004, 007 | SRC-UI003-001/002 show placeholder terrain panel | Product / high | future AC-UI003-005 | resolved |
| KCL-UI003-006 | UI-003 owns drawer visual and accessible logout row activation, but does not own clean Login destination, Token deletion or remote logout. | SRC-UI003-003, 004, 010 | None | Product / Architecture / high | parent AC-004, AC-016 handoff; future AC-UI003-006 | resolved |
| KCL-UI003-007 | UI-003 may provide fake/static map overlay fixtures for UI validation, but must not invent production parcel DTOs or business API behavior. | SRC-UI003-006, 008 | None | Process / high | future AC-UI003-007 | resolved |
| KCL-UI003-008 | Location permission UI states are in scope, but Figma does not provide dedicated `permission-denied` or location-loading screens; these states will follow the project's common UI template. | SRC-UI003-003, 006, requester decision 2026-09-08 | SRC-UI003-001 does not show permission states | Design / high | future AC-UI003-008 | resolved; common-template implementation, not Figma pixel acceptance |
| KCL-UI003-009 | `tp_req_ui.md` is a source index covering seven UI areas, but it does not define hidden interaction, API, permission or acceptance behavior. | SRC-UI003-011 | None | Requester / high | UI-003 scope and downstream handoffs | resolved |
| KCL-UI003-010 | Within the new source index, only the `底圖切換` node `4952:18205` belongs to UI-003; other listed pages remain separate task scope. | SRC-UI003-011, 008, 006 | None | Process + Product / high | future UI-003 scope AC | resolved |

## Conflicts and Gaps

| Conflict ID | Claims / Sources | Domain | Impact / Severity | Options | Decision Owner | Status |
|---|---|---|---|---|---|---|
| KCF-UI003-001 | Figma labels terrain as `地形圖(圖片亂放的)` while product requires Taipei historical WMTS terrain source. | Design asset / Map data | P1 if screenshot is used as map-content acceptance | Use Figma only for control visual state; use product WMTS for basemap content | Product + Design | resolved |
| KCF-UI003-002 | UI-003 owns drawer visual/logout activation; UI-002 owns clean Login return; INT-001 owns remote Token deletion. | Architecture / Auth boundary | P1 if one task owns all logout behavior | Split by visual/action/destination/remote ownership | Product + Architecture | resolved |
| KCF-UI003-003 | Search field and right-bottom search/filter affordance are visible in map shell, but query results and parcel summary belong to later UI-004. | Task boundary | P2; could duplicate search implementation | UI-003 owns affordance and callback only; UI-004 owns query/results/sheet | Planning | resolved |
| KCF-UI003-004 | Location permission denied and loading states are listed but not visible in Figma or the attached screenshot. | UI state evidence | P2; exact pixel design is unavailable | Use the project's common UI template and Android platform behavior; do not claim Figma pixel parity for these states | Requester / Planning | resolved by requester decision 2026-09-08 |
| KCF-UI003-005 | Structured Figma node details were previously unread. | Design evidence | P2; exact measurements/assets may be incomplete | Figma MCP inspection completed for section and screen frame dimensions | Design / Planning | resolved 2026-09-08 |
| KCF-UI003-006 | UI-001 and UI-002 prerequisites are incomplete. | Sequencing | P1 for Implementation entry | Finish prerequisite handoffs before UI-003 implementation | Project owner | open; blocks Implementation, not Knowledge |
| KCF-UI003-007 | New source index references image files that are not present in its directory. | Design evidence | P2 for pixel-level validation | Confirm/attach the referenced assets or rely on accessible Figma evidence before visual validation | Design / requester | open; does not change current behavioral baseline |
| KCF-UI003-008 | WMTS endpoint `https://map-tpgos.gov.taipei/wmts/` is retained but its layer parameters are not yet specified. | Map API / integration | P2; prevents real tile integration only | Add layer, matrix set, attribution and request parameters in a later WMTS update | Product / Engineering | open; does not block UI shell baseline |

## Decision Records

### KD-UI003-001

- Decision: `UI-003-map-shell` owns the authenticated map shell UI: full-screen map container, top hamburger/search/notification affordances, bottom basemap switcher, right-bottom locate affordance, drawer visual and UI-only callbacks.
- Alternatives: Put map shell in UI-002 after login; wait for production parcel API before building shell.
- Source / Conflict IDs: SRC-UI003-003, 005, 006, 008; KCF-UI003-003
- Rationale: Roadmap separates auth, map shell and parcel search so protected UI can be built with fake/static state before API integration.
- Approver and date: Roadmap baseline and current requester task, 2026-09-08.
- Affected requirements / AC / artifacts: future `UI-003-map-shell/requirement.md`, plan, UI-004 handoff.
- Supersedes: None.
- Revalidation trigger: roadmap task split, AppRoot/session contract or authenticated navigation ownership changes.

### KD-UI003-002

- Decision: `CMP-BASEMAP-SWITCHER` exposes exactly three mutually exclusive basemap choices: electronic map / EMAP default, orthophoto / PHOTO2, and terrain / Taipei historical WMTS. Switching basemap preserves parcel overlays, selected/highlighted targets, user location and active query context.
- Alternatives: Treat basemap as a decorative screenshot; clear overlays on switch; defer switcher until API.
- Source / Conflict IDs: SRC-UI003-001, 003, 004, 006; KCF-UI003-001
- Rationale: Product and parent requirement explicitly define three basemaps, default and overlay preservation; UI can verify with fake overlays.
- Approver and date: Product/interface specs and parent requirement, 2026-09-08.
- Affected requirements / AC / artifacts: future AC-UI003 basemap criteria, fake overlay fixtures, map SDK plan.
- Supersedes: None.
- Revalidation trigger: product basemap list, WMTS source, default basemap, zoom range or overlay preservation rule changes.

### KD-UI003-003

- Decision: Figma/screenshot is authoritative for visible shell composition and selected-state affordances, but actual map tile imagery and terrain content acceptance come from product WMTS specifications.
- Alternatives: Pixel-match all basemap backgrounds from the screenshot; ignore Figma and use platform default map controls.
- Source / Conflict IDs: SRC-UI003-001, 002, 003, 004, 007; KCF-UI003-001, KCF-UI003-005
- Rationale: Figma explicitly marks terrain image as placeholder; domain authority makes product interface spec the source for map data services.
- Approver and date: Design specification and product interface spec, 2026-09-08.
- Affected requirements / AC / artifacts: visual Verification evidence, basemap implementation plan, asset manifest.
- Supersedes: Any interpretation that uses the `地形圖(圖片亂放的)` screenshot as deliverable terrain tile content.
- Revalidation trigger: structured Figma retrieval contradicts visible screenshot, or product changes basemap data source.

### KD-UI003-004

- Decision: Drawer/logout is split: UI-003 owns complete drawer visual and emits one accessible `LogoutRequested` event; UI-002 owns immediate clean Login return; INT-001 owns production Token and remote logout.
- Alternatives: UI-003 directly clears auth state; UI-002 implements drawer visual; wait for INT-001 before drawing drawer.
- Source / Conflict IDs: SRC-UI003-003, 004, 010; KCF-UI003-002
- Rationale: Keeps visual/navigation shell, auth destination and remote API behavior in their owning tasks without blocking UI shell work.
- Approver and date: UI-002 logout requirement and interface spec, 2026-09-08.
- Affected requirements / AC / artifacts: future drawer criteria, UI-002 handoff, INT-001 handoff.
- Supersedes: Any UI-003 plan that owns production logout or Token deletion.
- Revalidation trigger: logout destination contract, session owner contract or remote logout requirement changes.

### KD-UI003-005

- Decision: Treat `/Users/a10362/Desktop/markdown file/tp_req_ui.md` as the current requester-approved UI source inventory. It establishes the existence and Figma locations of seven UI areas, but does not by itself establish hidden behavior, API contracts, permissions, accessibility details or acceptance criteria.
- Alternatives: Treat every linked page as part of UI-003; infer interactions from page names and image references; replace product/task specifications with this index.
- Source / Conflict IDs: SRC-UI003-011, SRC-UI003-008; KCF-UI003-007
- Rationale: The task roadmap assigns UI-003 only to the authenticated map shell, navigation drawer, basemap control and location UI. The remaining pages are separate task scopes or downstream handoffs.
- Approver and date: Current requester source and task roadmap, 2026-09-08.
- Affected requirements / AC / artifacts: UI-003 scope, UI-004/UI-006/UI-008 handoffs, future visual validation evidence.
- Supersedes: None.
- Revalidation trigger: requester changes page ownership or promotes a linked page into UI-003 scope.

### KD-UI003-008

- Decision: Use Figma MCP dimensions as the current visual reference for UI-003: three Android screen frames at `402 x 874`; the section container is `1822 x 1458`.
- Source / Conflict IDs: SRC-UI003-002; KCF-UI003-005
- Rationale: MCP confirms the screen canvas size and visible three-state composition without inventing hidden behavior.
- Approver and date: Requester source and MCP inspection, 2026-09-08.
- Revalidation trigger: Figma node or Android target frame changes.

### KD-UI003-006

- Decision: Implement `permission-denied` and location `loading` states with the project's common UI template because these states are not drawn as dedicated Figma screens. They are behaviorally required by `SCR-MAP-01`, but are excluded from Figma pixel-level acceptance.
- Alternatives: Block UI-003 until new Figma screens are supplied; infer a pixel-perfect design from the normal map screenshot.
- Source / Conflict IDs: SRC-UI003-003, SRC-UI003-006; KCF-UI003-004
- Rationale: The requester explicitly confirmed the missing Figma states and authorized common-template development.
- Approver and date: Requester, 2026-09-08.
- Affected requirements / AC / artifacts: future AC-UI003-008, UI-003 requirement, Compose state tests and accessibility verification.
- Supersedes: The open status of KCF-UI003-004.
- Revalidation trigger: Dedicated Figma states are later supplied or the common UI template changes.

### KD-UI003-007

- Decision: Render three reserved basemap buttons in the UI and select the first button by default. Keep the WMTS endpoint `https://map-tpgos.gov.taipei/wmts/` as a documented API gap until layer parameters are provided; do not invent request parameters.
- Alternatives: Implement only one button; hard-code provisional WMTS parameters; block the UI baseline until WMTS is complete.
- Source / Conflict IDs: SRC-UI003-004, requester decisions 2026-09-08; KCF-UI003-008
- Rationale: The visual interaction can be established independently while preserving a traceable integration gap for later WMTS completion.
- Approver and date: Requester, 2026-09-08.
- Affected requirements / AC / artifacts: basemap switcher criteria, fake/static tile state, future WMTS integration plan.
- Supersedes: Any assumption that the current UI baseline must contain complete WMTS parameters.
- Revalidation trigger: WMTS layer and request parameters are supplied or changed.

## Assumptions

| Assumption ID | Statement / Evidence | Confidence | Impact if Wrong | Owner | Validation Method | Expiry / Trigger | Safe for Planning |
|---|---|---|---|---|---|---|---|
| KA-UI003-001 | The attached screenshot accurately represents Figma node `4952:18205` visible basemap-switching panels. | medium | Exact measurements/assets may require revision. | Design owner | Retrieve structured Figma data or archived crop before Plan Review. | Plan Review / source change | yes |
| KA-UI003-002 | UI-003 can validate basemap switching with fake/static overlays before production parcel API exists. | high | If product requires live data for visual acceptance, UI-003 would need to wait for INT-002. | Planning | Requirement must state fake overlay fixtures and API deferral. | Requirement approval | yes |
| KA-UI003-003 | Map SDK choice is not yet a Knowledge blocker if Planning includes a spike and fallback for tile rendering. | medium | Unsupported WMTS/overlay behavior may alter technical plan. | Engineering | Planning spike for SDK/tile support before Implementation. | Planning / dependency selection | yes |

## Resolved Items
- UI-003 owns map shell, basemap switcher, drawer visual and location permission UI.
- Electronic map is the default basemap.
- Three basemap choices are required and mutually exclusive.
- Three basemap buttons are reserved, with the first selected by default; actual WMTS layer parameters remain deferred.
- `https://map-tpgos.gov.taipei/wmts/` is retained as the future WMTS endpoint gap.
- Basemap switching must preserve map overlays and query/location context.
- Logout visual/action ownership is UI-003; clean Login destination is UI-002; remote logout/Token is INT-001.
- Search field/affordance belongs visibly to map shell, while query results and parcel summary are UI-004-owned.
- The new source index confirms related UI pages but does not expand UI-003 scope; each page requires its own task Knowledge baseline or explicit handoff.

## Unresolved Non-blocking Items
- Detailed child-component measurements and asset export list remain implementation-level follow-up; screen frame dimensions are confirmed.
- Exact availability and contents of the image files referenced by `tp_req_ui.md`.
- Figma-specific visual details for location permission denied/loading remain unavailable; implementation follows the common UI template by explicit requester decision.
- WMTS layer parameters, attribution, caching and offline policy.

## Blocking Items
- None for Knowledge Validation.
- UI-001 foundation and UI-002 authenticated handoff must complete before UI-003 Implementation.
- Production map/parcel API absence blocks integration, not UI shell baseline.

## Downstream Impact
- Requirements / AC: `UI-003` requirement should introduce observable AC for default map shell, top entries, drawer, basemap mutual exclusivity/default, overlay preservation, locate permission states and UI-004 handoff.
- Plan / design: Plan must retrieve structured Figma evidence before visual implementation and must include map SDK/tile-support spike.
- Code / data / API: Use fake/static parcel and user-location overlays only; do not invent production DTOs or endpoints.
- Tests / verification: Compose tests should cover selected basemap state, mutual exclusivity, overlay persistence, callback events and accessibility labels; screenshot/visual evidence should separate visible controls from real tile-content verification.
- Release / operations: No merge/release until prerequisite gates, build/test evidence and later integration boundaries pass.

## Revalidation Triggers
- Figma node `4952:18205` content, approval, node hierarchy or screenshot changes.
- Product basemap list, WMTS URL, default basemap, zoom range or overlay preservation rule changes.
- UI-001 AppRoot/session contract, UI-002 LogoutRequested destination contract or task roadmap changes.
- Map SDK choice cannot support required basemap/overlay/accessibility behavior.
- Production INT-002 map/parcel API changes visual state or ownership assumptions.

## Validation Handoff
- Candidate baseline: `KB-UI-003-MAP-SHELL-R1`
- Re-collection source: `SRC-UI003-011` and `SRC-UI003-012`
- Decisions and constraints Validation must check: KD-UI003-001 through KD-UI003-004; screenshot-derived claims labeled as visual observations; terrain placeholder exclusion; WMTS authority; UI-002/INT-001 logout split; UI-004 search handoff.
- Safe assumptions: KA-UI003-001 through KA-UI003-003.
- Required follow-up evidence: structured Figma data or archived crops for node `4952:18205`, map SDK/tile spike evidence, UI-001/UI-002 prerequisite handoff status.
- Material blockers: no for Knowledge Validation; yes for Implementation until UI-001/UI-002 prerequisites complete.
- Ready for Validation: yes
