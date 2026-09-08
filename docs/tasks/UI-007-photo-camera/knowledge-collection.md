# Knowledge Collection

## Question and Scope
- Task question: 建立 `UI-007-photo-camera` 相機／照片 Knowledge baseline，並確認 Figma 元件內容與尺寸。
- In scope: 相機入口、4:3 現場照片、四張限制、縮圖、拍攝時間、刪除重拍與本機 URI。
- Out of scope: 相簿、照片上傳 API、伺服器儲存、WMTS。
- Collection date: 2026-09-08 (Asia/Taipei)
- Collector: Codex Knowledge Collection and Resolution Agent

## Source Coverage
| Category | Result | Notes |
|---|---|---|
| Requirement / product | found | Parent FR-010/AC-010 and data specification define camera-only, four 4:3 photos. |
| Design / accessibility | found | Figma MCP node `2938:193` inspected. |
| API / schema | N/A | Upload API deferred to INT-003. |
| Architecture / sequencing | found | Roadmap assigns local photo handling to UI-007. |

## Source Register
| Source ID | Location | Authority / Scope | Notes |
|---|---|---|---|
| SRC-UI007-001 | Figma `2938:193`, page `UI` | Design / photo UI | Screen `402 x 874`; photo group `354 x 260`; photo tile `168 x 126`; FAB `56 x 56`; upload description `123 x 22`. |
| SRC-UI007-002 | `docs/design/app-ui-requirements.md` | Design | `CMP-PHOTO-TILE`, `CMP-CAMERA-TILE`, four-photo states. |
| SRC-UI007-003 | `docs/tasks/UI-0907-app-ui-requirements/requirement.md` | Product | FR-010, AC-010. |
| SRC-UI007-004 | `docs/product/land-survey-115/2 資料規格.md` | Product / Data | Photo count/aspect rules. |
| SRC-UI007-005 | Roadmap and task index | Process | Local-only UI; INT-003 later. |

## Missing or Inaccessible Sources
- Production photo-upload API and error contract are deferred.

## Initial Conflicts and Gaps
- Figma upload-like visuals do not override the camera-only product rule.

## Handoff to Resolution
- Coverage sufficient: yes
- Questions: local photo states, four-photo rule and deferred upload boundary.
- Recommended next action: knowledge_resolution
