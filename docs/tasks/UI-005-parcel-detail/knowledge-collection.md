# Knowledge Collection

## Question and Scope
- Task question: 建立 `UI-005-parcel-detail` 土地詳情畫面的 Knowledge baseline。
- In scope: 固定摘要、土地資料／實地勘查土地現況雙頁籤、唯讀／可編輯顯示、歷史期別唯讀、返回與進入 UI-006。
- Out of scope: 查詢入口、調查表單實作、照片／相機、通知、production API、WMTS。
- Collection date: 2026-09-08 (Asia/Taipei)
- Collector: Codex Knowledge Collection and Resolution Agent

## Source Coverage
| Category | Required | Result | Notes |
|---|---|---|---|
| Authorized decisions / requirement | yes | found | Requester confirmed `tp_req_ui.md` UI pages are baseline and API later補充。 |
| Product rules | yes | found | Product role and data specifications define detail sections and field rules. |
| Design / assets / accessibility | yes | found | Figma MCP located parcel-detail components and confirmed hierarchy and dimensions. |
| API / schema / authentication | no | N/A | UI uses fake parcel detail; production contract deferred. |
| Architecture / repository | yes | found | Roadmap assigns detail after parcel search and before survey form. |
| Tests / logs / runtime evidence | yes | missing | No UI-005 implementation exists. |
| Build / release / operations | yes | missing | No UI-005 branch or build evidence exists. |
| External primary sources | no | N/A | Not required for UI baseline. |

## Source Register
| Source ID | Title / Location | Type | Domain / Owner | Version / Date | Authority | Freshness | Scope / AC | Access | Notes |
|---|---|---|---|---|---|---|---|---|---|
| SRC-UI005-001 | `/Users/a10362/Desktop/markdown file/tp_req_ui.md` | requester UI source index | Product / Design / requester | 2026-09-08 | authoritative for source inventory | current | related UI page index | available | Lists survey, returned data and parcel search, but no dedicated parcel-detail node. |
| SRC-UI005-002 | `docs/design/app-ui-requirements.md` | approved design specification | Design | 2026-09-08 | authoritative | current | `SCR-PARCEL-01`, header and tabs | available | Defines detail states and components. |
| SRC-UI005-003 | `docs/tasks/UI-0907-app-ui-requirements/requirement.md` | parent UI requirement | Product / Design | 2026-09-08 | authoritative | current | FR-006, FR-007 and AC-007 | available | Defines fixed summary, two tabs and ten read-only land-data fields. |
| SRC-UI005-004 | `docs/product/land-survey-115/2 資料規格.md` | data specification | Product / Data | 2026-09-08 | authoritative by field domain | current | tab fields, display rules, history | available | Tab 1 has 8 land-marking + 2 GIS read-only fields; tab 2 has survey fields and system-readonly fields. |
| SRC-UI005-005 | `docs/product/land-survey-115/1 系統概述與角色權限.md` | product / role specification | Product | 2026-09-08 | authoritative | current | detail entry and APP user | available | APP is for field investigators; detail view and survey flow are separate. |
| SRC-UI005-006 | `docs/tasks/README.md` and `docs/tasks/UI-ROADMAP-0907/plan.md` | task sequencing | Process | 2026-09-08 | authoritative | current | UI-005 boundary and fake-data policy | available | Detail follows search; survey is UI-006; API deferred. |
| SRC-UI005-007 | Figma MCP inspection, file `HRbRsw6HoNBUCtaieX8xUM`, page `UI` | structured design evidence | Design / requester | 2026-09-08 | authoritative for visible component dimensions | current | parcel detail hierarchy and variants | available | `土地資料tab` `2960:2914` = `402 x 1004`, inner `4949:12667` = `354 x 560`; `實地勘查土地現況tab` `4943:22836` = `402 x 1412`, inner `4949:13109` = `354 x 964`; 未調查／編輯 variants found. |

## Missing or Inaccessible Sources
- `tp_req_ui.md` has no dedicated parcel-detail link, but Figma MCP has confirmed the underlying parcel-detail components and dimensions.
- Production parcel detail／history API contract is intentionally deferred.

## Initial Conflicts and Gaps
- Product data spec says the land-data tab is always read-only, while the survey tab is viewable and may enter editing through a later survey task.
- The APP user scope is field investigator; role-specific edit differences are not needed for this UI baseline.

## Collection Limitations
- Figma source index cannot prove detailed parcel-detail layout because no dedicated node is provided.
- API payload, history retrieval, error mapping and pagination are not part of this UI baseline.

## Handoff to Resolution
- Coverage sufficient: yes
- Sources requiring authority or freshness resolution: detail UI ownership versus later survey task; missing dedicated Figma node.
- Questions Resolution must answer: Which detail sections are UI-005-owned? Which fields are read-only? What remains deferred?
- Blocking items: None for Knowledge; UI-003/UI-004 handoff blocks implementation sequencing.
- Recommended next action: knowledge_resolution
