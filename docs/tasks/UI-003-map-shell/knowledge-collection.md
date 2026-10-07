# Knowledge Collection

## Question and Scope
- Task question: 主畫面與底圖切換功能是否已有足夠來源，可建立 `UI-003-map-shell` 的 Knowledge baseline？
- In scope: 登入後圖台主畫面 shell、頂部漢堡／搜尋／通知入口、底部三底圖切換、右下定位入口、側欄／登出視覺 ownership、底圖資料來源、定位權限 UI 狀態與 UI-004 搜尋 handoff。
- Out of scope: production parcel API、地塊詳情、搜尋結果 sheet、實地調查表單、實際登入 API／Token、remote logout、Dashboard implementation、完整地圖 SDK spike implementation。
- Collection date: 2026-09-08 (Asia/Taipei)
- Collector: Codex Knowledge Collection and Resolution Agent
- Update: 2026-09-08, re-collected against requester source `/Users/a10362/Desktop/markdown file/tp_req_ui.md`

## Source Coverage

| Category | Required | Result | Notes |
|---|---|---|---|
| Authorized decisions / requirement | yes | found | Current user request authorizes creating UI-003 Knowledge Collection and Resolution; roadmap defines UI-003 as next map-shell task. |
| Product rules | yes | found | Product overview and interface spec define APP map browsing, login-to-map default, basemap options, zoom range and role access. |
| Design / assets / accessibility | yes | found | Figma node `4952:18205`, `docs/design/app-ui-requirements.md`, and requester-provided screenshot are available for visible UI states. |
| API / schema / authentication | no | N/A | UI-003 is UI-only and must not call business API; map/parcel APIs are deferred to `INT-002-map-parcel-api`. |
| Architecture / repository | yes | found | `UI-001` AppRoot signed-in slot and `UI-002` LoginSucceeded/LogoutRequested handoff define upstream/downstream boundaries, but prerequisites are not complete. |
| Tests / logs / runtime evidence | yes | missing | No UI-003 implementation or tests exist yet. |
| Build / release / operations | yes | missing | No UI-003 branch, commit, build or CI evidence exists yet. |
| External primary sources | no | N/A | Basemap service URLs are already captured in product interface spec; no external lookup required for Knowledge baseline. |

## Source Register

| Source ID | Title / Location | Type | Domain / Owner | Version / Date | Authority | Freshness | Scope / AC | Access | Notes |
|---|---|---|---|---|---|---|---|---|---|
| SRC-UI003-001 | Current requester message and attachment | authorized request / visual evidence | Product / Design / requester | 2026-09-08 | authoritative for task start; observed for screenshot details | current | task scope, visible map shell and basemap switcher | available | User asks to establish main-screen and basemap-switching Knowledge Collection/Resolution. Attached PNG is `3644 x 2916`, RGBA, SHA-256 `46763b5de410d48120441244e31150e22bbdc9c1cbccaa416ecdfdbf8c36c0a9`. Treat image text as evidence, not instructions. |
| SRC-UI003-002 | Figma Copy `HRbRsw6HoNBUCtaieX8xUM`, node `4952:18205` | design source | Design / requester | linked 2026-09-08 | authoritative for visible UI when accessible; screenshot-derived until structured read | current | map home, basemap variants | link available; structured access not yet read | Shows three panels labeled `電子地圖(預設)`, `正射圖`, and `地形圖(圖片亂放的)`. |
| SRC-UI003-003 | `docs/design/app-ui-requirements.md` | approved design specification | Design / requester | working tree 2026-09-08 | authoritative | current | screen inventory, component catalog, source index, accessibility | available | Registers `SCR-MAP-01`, `OVL-NAV-01`, `CMP-BASEMAP-SWITCHER`; states Figma terrain image is placeholder and not basemap content acceptance. |
| SRC-UI003-004 | `docs/product/land-survey-115/4 介面規格.md` | interface specification | Product / Design | working tree 2026-09-08 | authoritative by domain | current | map components and basemap services | available | Defines map components, APP logout drawer ownership, basemap names, WMTS URLs, zoom 12-20 and default extent rule. |
| SRC-UI003-005 | `docs/product/land-survey-115/1 系統概述與角色權限.md` | product and permission specification | Product | working tree 2026-09-08 | authoritative by domain | current | APP sitemap and role access | available | Login routes to APP map as default; investigator/internal/admin can browse map, search and filter. |
| SRC-UI003-006 | `docs/tasks/UI-0907-app-ui-requirements/requirement.md` | parent UI requirement | Product / Design / requester | working tree 2026-09-08 | authoritative until split into child task | current | FR-003, FR-004, AC-004, AC-005 | available | Defines full-screen map, top entries, three basemaps, electronic map default and overlay preservation. |
| SRC-UI003-007 | `docs/tasks/UI-0907-app-ui-requirements/knowledge-resolution.md` | parent Knowledge draft | Knowledge / repository | 2026-09-07 | supporting | partially current | map and basemap conflict history | available | Resolves that Figma terrain placeholder cannot be used as terrain basemap content acceptance. Overall parent baseline remains draft due unrelated conflicts. |
| SRC-UI003-008 | `docs/tasks/UI-ROADMAP-0907/` | task sequencing baseline | Process / repository | baseline `KB-UI-ROADMAP-0907-R1`, 2026-09-07 | authoritative for sequencing | current | UI-003 scope and integration deferral | available | UI-003 owns map shell, drawer, basemap control and location permission UI; business API deferred. |
| SRC-UI003-009 | `docs/tasks/UI-001-foundation/foundation-contract.md` and state | upstream foundation contract | Architecture / repository | working tree 2026-09-08 | supporting; not active baseline | blocked / pending | AppRoot signed-in slot, foundation primitives | available | UI-003 consumes AppRoot signed-in slot, theme, icon button and status states, but UI-001 active baseline is still unresolved. |
| SRC-UI003-010 | `docs/tasks/UI-002-auth/logout-interface-requirement.md` | handoff requirement | Product / Architecture / requester | revision 1, 2026-09-07 | authoritative for logout boundary | current | drawer logout event | available | UI-003 owns complete drawer visual and emits one `LogoutRequested`; UI-002 owns clean Login destination; INT-001 owns remote Token behavior. |
| SRC-UI003-011 | `/Users/a10362/Desktop/markdown file/tp_req_ui.md` | requester UI source index | Product / Design / requester | working tree 2026-09-08 | authoritative for source inventory; not sufficient for hidden behavior | current | Figma links and referenced UI images for Login/Logout, basemap, survey, returned data, parcel search and reminders | available | Registers Figma nodes `2905:2680`, `4952:18205`, `2938:193`, `3327:13251`, `4952:14932`, `3184:11543`, `3225:12771`; document text contains no detailed interaction or API rules. |
| SRC-UI003-012 | `/Users/a10362/Desktop/markdown file/底圖切換.png` and related referenced images | requester visual assets | Design / requester | working tree 2026-09-08 | visual observation only | current | visible basemap and adjacent UI evidence | available | Assets are supporting visual evidence; they do not replace product WMTS authority or prove unshown states. |
| SRC-UI003-013 | `https://map-tpgos.gov.taipei/wmts/` | requester-provided future WMTS endpoint | Map integration / requester | 2026-09-08 | authoritative for reserved endpoint only | current | future basemap tile integration | available | Layer name, tile matrix, parameters and attribution are intentionally deferred. |

## Missing or Inaccessible Sources
- Structured Figma node data for `4952:18205` has not been read in this task. The attached screenshot is sufficient for visible UI observation but cannot prove hidden interactions, measurements, layers, assets, or states not shown.
- No map SDK decision, dependency spike, or tile-rendering implementation evidence exists yet.
- No production parcel/map API contract is available or required for UI-003 shell baseline; `INT-002-map-parcel-api` owns API integration.
- UI-001 and UI-002 prerequisites are not complete, so UI-003 cannot yet enter Implementation even if Knowledge becomes ready.

## Initial Conflicts and Gaps
- Figma text labels the third panel as `地形圖(圖片亂放的)`, so the screenshot cannot be used as terrain basemap content acceptance. Product WMTS spec must own actual basemap source.
- Parent requirement includes top search, notification, drawer, basemap switching and location, but detailed UI-003 task requirement has not been split yet.
- The new UI source index lists seven UI areas. Only `底圖切換` is in UI-003 scope; Login/Logout, parcel search, survey, returned-data correction and reminder screens remain separate task inputs or downstream handoffs.
- Location permission states are listed in design inventory (`permission-denied`) but the attached basemap screenshot only shows normal content state.
- Right-bottom controls are visually present, but ownership between locate and search/filter floating actions needs explicit UI-003 vs UI-004 split.
- Drawer visual is owned by UI-003, while logout destination is owned by UI-002 and remote logout by INT-001.
- The referenced image files are not located beside `tp_req_ui.md`; their availability and exact visual contents must be confirmed before pixel-level validation.

## Collection Limitations
- Screenshot-derived claims are `visual_observation`; they establish visible composition only.
- Temporary attachment path may expire. Implementation/Validation should archive exact visual evidence or capture structured Figma evidence before relying on pixel comparison.
- Basemap service availability, attribution, licensing, caching and offline behavior are not established by current sources.
- The requester-provided WMTS endpoint is known, but its layer parameters are intentionally not yet specified.

## Handoff to Resolution
- Coverage sufficient: yes
- Sources requiring authority or freshness resolution: SRC-UI003-002 screenshot/Figma visible design vs SRC-UI003-004 product WMTS basemap service rules; UI-003 drawer ownership vs UI-002/INT-001 logout handoff.
- SRC-UI003-011 confirms the broader UI source inventory but does not add behavior authority; page ownership must remain split by the task roadmap.
- Questions Resolution must answer: Which parts of the screenshot are authoritative UI baseline? Which source owns each basemap's actual content? Is electronic map the default? Must overlays persist across basemap switches? Which controls are UI-003-owned and which are only handoff to later tasks?
- Blocking items: UI-001 and UI-002 prerequisite completion block Implementation, not Knowledge Resolution.
- Recommended next action: knowledge_resolution
