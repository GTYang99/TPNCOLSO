# Knowledge Collection

## Question and Scope
- Task question: 是否能依目前 UI 來源建立 `UI-004-parcel-search` 土地查詢與摘要的 Knowledge baseline？
- In scope: 地號／土地坐落／土地編號查詢入口、輸入狀態、查詢結果定位、查無資料、土地摘要 Sheet。
- Out of scope: production parcel API、土地詳情、調查填報、WMTS 圖磚、通知已讀／分頁與錯誤 API 合約。
- Collection date: 2026-09-08 (Asia/Taipei)
- Collector: Codex Knowledge Collection and Resolution Agent

## Source Coverage
| Category | Required | Result | Notes |
|---|---|---|---|
| Authorized decisions / requirement | yes | found | Requester confirmed all `tp_req_ui.md` screens are baseline and API later補充。 |
| Product rules | yes | found | Product role/use specification and parent UI requirement define search scope. |
| Design / assets / accessibility | yes | found | Figma node `4952:14932` and design inventory define search states. |
| API / schema / authentication | no | N/A | API contract intentionally deferred; UI uses fake parcel data. |
| Architecture / repository | yes | found | Roadmap assigns search／summary to UI-004 after UI-003. |
| Tests / logs / runtime evidence | yes | missing | No UI-004 implementation exists. |
| Build / release / operations | yes | missing | No UI-004 branch or build evidence exists. |
| External primary sources | no | N/A | No external source required for UI baseline. |

## Source Register
| Source ID | Title / Location | Type | Domain / Owner | Version / Date | Authority | Freshness | Scope / AC | Access | Notes |
|---|---|---|---|---|---|---|---|---|---|
| SRC-UI004-001 | `/Users/a10362/Desktop/markdown file/tp_req_ui.md` | requester UI source index | Product / Design / requester | 2026-09-08 | authoritative for source inventory | current | 土地查詢 Figma node | available | Registers node `4952:14932`; source index itself does not define hidden behavior. |
| SRC-UI004-002 | Figma node `4952:14932` | design source | Design / requester | linked 2026-09-08 | authoritative for visible UI | current | 已輸入、結果 sheet、查無資料 | link available | Structured MCP confirmation remains required before pixel validation. |
| SRC-UI004-003 | `docs/design/app-ui-requirements.md` | approved design specification | Design | working tree 2026-09-08 | authoritative | current | `4952:14932`, search states and accessibility | available | Defines search screen inventory and map handoff. |
| SRC-UI004-004 | `docs/tasks/UI-0907-app-ui-requirements/requirement.md` | parent UI requirement | Product / Design | working tree 2026-09-08 | authoritative | current | FR-005, query and no-result behavior | available | Query accepts land number, location or land number keyword; result opens summary sheet; no result keeps keyword. |
| SRC-UI004-005 | `docs/tasks/README.md` and `docs/tasks/UI-ROADMAP-0907/plan.md` | task sequencing | Process | 2026-09-08 | authoritative | current | UI-004 boundary and fake data policy | available | UI-004 uses fake parcel list; production API is INT-002. |
| SRC-UI004-006 | `docs/product/land-survey-115/5 API規格.md` | product API specification | Product / API | working tree 2026-09-08 | supporting / incomplete for this UI baseline | current | future parcel query contract | available | API details may be extended later; do not invent DTOs or endpoints now. |

## Missing or Inaccessible Sources
- Structured Figma MCP output for node `4952:14932` has not yet been captured.
- Production parcel query／detail API contract is intentionally deferred.

## Initial Conflicts and Gaps
- Figma establishes visible search states, while API behavior is not yet available; UI must use fake data and keep integration replaceable.
- Search result and summary belong to UI-004; map shell search affordance remains UI-003-owned.

## Collection Limitations
- Screenshot or Figma evidence cannot prove API payloads, permissions, pagination or error semantics.

## Handoff to Resolution
- Coverage sufficient: yes
- Sources requiring authority or freshness resolution: Figma visible states versus deferred API behavior; UI-003／UI-004 search ownership.
- Questions Resolution must answer: What is the UI-only search flow? What states are required? Which API details remain deferred?
- Blocking items: None for Knowledge; UI-003 prerequisite blocks implementation sequencing.
- Recommended next action: knowledge_resolution
