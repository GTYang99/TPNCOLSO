# Knowledge Collection

## Question and Scope
- Task question: 建立 `UI-009-ui-hardening` 跨畫面品質 Knowledge baseline 前，確認 task requirement、AC 與 UI-002–UI-008 的可追溯來源。
- In scope: Android 9+、多尺寸、字級放大、IME、安全區域、無障礙、旋轉與既有 local UI state restoration。
- Out of scope: 新增產品畫面、API／DTO／資料行為、Figma 新設計、正式 pixel-parity acceptance。
- Collection date: 2026-10-08 (Asia/Taipei)
- Collector: Codex Knowledge Collection and Resolution Agent

## Source Coverage

| Category | Required | Result | Notes |
|---|---|---|---|
| Authorized decisions / requirement | yes | approved | Requester approved `requirement.md` and `AC-001`–`AC-007` on 2026-10-08; process-death restoration is included in `AC-006`. |
| Product rules | yes | found | Parent `FR`/`AC` and the task roadmap define the existing UI scope and API boundary. |
| Design / assets / accessibility | yes | found with limitations | The design specification and child-task source registers provide visible UI and accessibility rules; Figma formal revision IDs remain unknown. |
| API / schema / authentication | no | N/A | UI-009 is a local UI hardening task and must not infer or add production contracts. |
| Architecture / repository | yes | found | Existing UI-002–UI-008 task artifacts and current development handoffs define ownership boundaries. |
| Tests / logs / runtime evidence | yes | supporting evidence found | Existing child-task runtime and verification artifacts are inputs for the later regression matrix; UI-009 has not run its own suite. |
| Build / release / operations | no | N/A | Not required to clarify the Knowledge baseline. |
| External primary sources | no | N/A | No external source is required. |

## Source Register

| Source ID | Title / Location | Type | Domain / Owner | Version / Date | Authority | Freshness | Scope / AC | Access | Notes |
|---|---|---|---|---|---|---|---|---|---|
| SRC-UI009-001 | `docs/tasks/UI-0907-app-ui-requirements/requirement.md` | parent UI requirement | Product / Design | working tree 2026-10-08 | authoritative | current | parent NFR and `AC-017`; responsive, IME, scalable text and Android 9+ | available | Owns product-level behavior; UI-009 derives hardening checks without changing child behavior. |
| SRC-UI009-002 | `docs/design/app-ui-requirements.md` | approved design specification | Design / requester | working tree 2026-10-08 | authoritative for design rules | current | `402 x 874` reference, insets, content descriptions, non-color state cues, `48dp` targets, IME visibility | available | Figma canvas is a reference, not a fixed device size. |
| SRC-UI009-003 | `docs/tasks/UI-ROADMAP-0907/plan.md` and `docs/tasks/README.md` | task boundary / sequencing | Process / repository | active roadmap baseline `KB-UI-ROADMAP-0907-R1` | authoritative for task boundary | current | UI-009 scope: Android 9+, multi-size, text, IME, accessibility, rotation and state restoration; no API | available | Assigns UI-009 as the final cross-screen hardening task. |
| SRC-UI009-004 | `docs/tasks/UI-002-auth/knowledge-collection.md` (`SRC-015`) and `docs/design/evidence/auth/` | requester-approved auth composite and archived evidence | Product / Design / requester | composite dated 2026-09-07; repository evidence current | authoritative for auth visuals | current | UI-002 Login/Register/Logout visual surfaces | available | The composite image hash is recorded in the child collection and replaces prior auth Figma visual authority. No Figma node is required for UI-002 visual comparison. |
| SRC-UI009-005 | Figma file `HRbRsw6HoNBUCtaieX8xUM`, node `4952:18205`; `docs/tasks/UI-003-map-shell/knowledge-collection.md` (`SRC-UI003-002`) | structured design source | Design / requester | inspected 2026-09-28; formal revision unknown | authoritative for visible UI observations | current retrieval; revision unknown | UI-003 map shell, drawer and basemap surfaces | link available | Claims are limited to visible UI; product WMTS behavior remains owned by product/interface specifications. |
| SRC-UI009-006 | Figma file `HRbRsw6HoNBUCtaieX8xUM`, node `4952:14932`, child nodes `4952:14938`, `4952:14933`, `4987:4490`; `docs/tasks/UI-004-parcel-search/knowledge-validation.md` | structured design source | Design / requester | retrieved 2026-10-01; formal revision unknown | authoritative for visible UI observations | current retrieval; revision unknown | UI-004 entered query, result summary and no-result states | link available | Child frames are each `402 x 874`; query behavior remains owned by the parent requirement and UI-004 requester clarification. |
| SRC-UI009-007 | `docs/design/app-ui-requirements.md` plus Figma component inspection recorded in `docs/tasks/UI-005-parcel-detail/knowledge-collection.md` (`2960:2914`, `4949:12667`, `4943:22836`, `4949:13109`) | design specification and component evidence | Design / requester | inspected 2026-09-08; no dedicated detail frame; formal revision unknown | authoritative for recorded visible component claims | current repository record; revision unknown | UI-005 detail header, tabs and read-only content surfaces | available | This is the approved alternative source because the source index has no dedicated UI-005 screen node. |
| SRC-UI009-008 | Figma file `HRbRsw6HoNBUCtaieX8xUM`, node `2938:193`, child frame `4947:27279`; `docs/tasks/UI-006-survey-form/knowledge-collection.md` | structured design source | Design / requester | inspected 2026-10-07; formal revision unknown | authoritative for visible UI observations | current retrieval; revision unknown | UI-006 survey form, tabs, rows, photos and form states | link available | Product field behavior remains owned by the parent requirement and data specification. |
| SRC-UI009-009 | Figma file `HRbRsw6HoNBUCtaieX8xUM`, node `2938:193`, child frames `4947:26040`, `4947:26547`, `4947:27279`; `docs/tasks/UI-007-photo-camera/knowledge-validation.md` | structured design source | Design / requester | inspected 2026-10-07; formal revision unknown | authoritative for visible UI observations | current retrieval; revision unknown | UI-007 camera/photo states and four-photo surface | link available | Camera-only, local URI and four-photo rules remain product/task requirements, not screenshot inferences. |
| SRC-UI009-010 | Figma file `HRbRsw6HoNBUCtaieX8xUM`, delete node `3184:11543` with Android frame/dialog `2985:4264`/`2997:4930`, discard node `3225:12771` with Android frame/dialog `3225:12772`/`3225:12775`; `docs/tasks/UI-008-alerts/knowledge-validation.md` | structured design source | Design / requester | inspected 2026-10-07; formal revision unknown | authoritative for Android visible UI observations | current retrieval; revision unknown | UI-008 delete and discard dialogs | link available | iOS variants are excluded; confirmation outcomes remain owned by UI-008 and its parent ACs. |
| SRC-UI009-011 | `docs/tasks/UI-002-auth/state.yaml` through `docs/tasks/UI-008-alerts/state.yaml` | child-task handoff evidence | Task owners / repository | current working tree 2026-10-08 | supporting | current | child baseline IDs, completion state and ownership handoff | available | These states are not substitutes for product/design authority; they bind the regression set to the current task handoffs. |

## Missing or Inaccessible Sources
- Figma formal revision/approval metadata is not available in the current records. The source register records retrieval dates and node IDs, and uses approved repository alternatives where a dedicated node is absent.
- UI-005 has no dedicated Figma screen node; the registered design specification and component inspection are the authoritative alternative for the visible claims listed above.

## Initial Conflicts and Gaps
- UI-002 uses a requester-approved composite image as its visual authority, while UI-003–UI-008 use per-task Figma node evidence. This is intentional source ownership, not a request to reactivate superseded auth nodes.
- UI-005 has component-level evidence rather than a dedicated screen frame; UI-009 must not claim a screen-level pixel baseline for UI-005.
- Figma revision IDs are unknown. This blocks formal pixel-parity claims but does not prevent the approved source boundary for layout/accessibility hardening.
- Process-death restoration is now an approved requirement; visual sources cannot prove it and Planning/Developer Validation must provide lifecycle evidence.

## Collection Limitations
- Figma-derived statements are `visual_observation` unless the cited product/design document explicitly declares the behavior.
- Screenshots and visual nodes cannot establish hidden API behavior, permissions, persistence, process-death restoration or accessibility semantics that are not recorded in the product/design sources.
- UI-009 runtime regression evidence does not yet exist; it belongs to Planning/Implementation/Developer Validation after baseline approval.

## Handoff to Resolution
- Coverage sufficient: yes for Knowledge Validation.
- Sources requiring authority or freshness resolution: formal Figma revision requirement only if pixel parity is later requested.
- Questions Resolution must answer: whether the current per-task source matrix remains valid when a child source node, approved visual artifact or ownership boundary changes.
- Blocking items: none for Knowledge Validation.
- Recommended next action: knowledge_validation
