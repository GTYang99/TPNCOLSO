# Knowledge Collection

## Question and Scope
- Task question: 建立 `UI-006-survey-form` 調查填報 Knowledge baseline，並以 Figma MCP 確認元件內容與尺寸。
- In scope: 調查現況表單、欄位編輯與驗證、無占用條件欄位、照片四張限制、刪除／重拍、送出狀態、UI-005 handoff。
- Out of scope: production API、照片上傳 API、WMTS、通知已讀／分頁、角色權限擴充。
- Collection date: 2026-09-08 (Asia/Taipei)
- Collector: Codex Knowledge Collection and Resolution Agent

## Source Coverage
| Category | Required | Result | Notes |
|---|---|---|---|
| Authorized decisions / requirement | yes | found | Requester confirms shared editing／validation／photo／submit functions live in UI-006 and APIs later補充。 |
| Product rules | yes | found | Product and data specs define 10 survey fields, conditional rules and four photos. |
| Design / assets / accessibility | yes | found | Figma MCP node `2938:193` inspected. |
| API / schema / authentication | no | N/A | Production contracts deferred. |
| Architecture / repository | yes | found | Roadmap assigns UI-006 after UI-005. |
| Tests / logs / runtime evidence | yes | missing | No implementation exists. |
| Build / release / operations | yes | missing | No branch/build evidence exists. |
| External primary sources | no | N/A | Not required. |

## Source Register
| Source ID | Title / Location | Type | Domain / Owner | Version / Date | Authority | Freshness | Scope / AC | Access | Notes |
|---|---|---|---|---|---|---|---|---|---|
| SRC-UI006-001 | `/Users/a10362/Desktop/markdown file/tp_req_ui.md` | requester UI source index | Product / Design | 2026-09-08 | authoritative for source inventory | current | survey Figma node `2938:193` | available | Identifies 調查填報 screen. |
| SRC-UI006-002 | Figma node `2938:193`, page `UI` | structured design evidence | Design / requester | MCP inspected 2026-09-08 | authoritative for visible UI | current | form states and components | available | Section `4432 x 2095`; screen frame `4947:27279` = `402 x 874`; inner form `4949:13109` = `354 x 964`; tabs `354 x 42`; radio rows mostly `354 x 56`, long row `354 x 80`; photo tiles `168 x 126`; FAB `56 x 56`. |
| SRC-UI006-003 | `docs/design/app-ui-requirements.md` | approved design specification | Design | 2026-09-08 | authoritative | current | survey states/components | available | Defines `SCR-SURVEY-01`, photo tile, camera tile and dialogs. |
| SRC-UI006-004 | `docs/tasks/UI-0907-app-ui-requirements/requirement.md` | parent requirement | Product / Design | 2026-09-08 | authoritative | current | FR-008–FR-011, AC-008–AC-011 | available | Defines fields, conditional validation, four photos and submit behavior. |
| SRC-UI006-005 | `docs/product/land-survey-115/2 資料規格.md` | data specification | Product / Data | 2026-09-08 | authoritative by field domain | current | survey field definitions | available | 10 fields; system fields read-only; conditional occupation fields. |
| SRC-UI006-006 | `docs/tasks/README.md` and `docs/tasks/UI-ROADMAP-0907/plan.md` | task sequencing | Process | 2026-09-08 | authoritative | current | UI-006 ownership and fake/local policy | available | UI-006 uses local validation and no API. |

## Missing or Inaccessible Sources
- Production API and photo-upload contracts are intentionally deferred.
- Figma MCP confirms component dimensions, but some screen variants need separate visual capture for pixel comparison.

## Initial Conflicts and Gaps
- UI-005 enters edit; UI-006 owns the shared editing／validation／photo／submit functions.
- Figma includes both filled and unfilled／editing variants; state mapping must follow product field rules, not infer behavior from names alone.

## Collection Limitations
- No API payload, error mapping or upload protocol is established.

## Handoff to Resolution
- Coverage sufficient: yes
- Sources requiring authority or freshness resolution: UI-005/UI-006 boundary and Figma variants versus product field rules.
- Questions Resolution must answer: Which states and fields are in the UI-only baseline? What API behavior remains deferred?
- Blocking items: None for Knowledge; UI-005 handoff blocks implementation sequencing.
- Recommended next action: knowledge_resolution
