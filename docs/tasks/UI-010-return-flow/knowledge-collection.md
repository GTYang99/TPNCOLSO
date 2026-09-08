# Knowledge Collection

## Question and Scope
- Task question: 建立 `UI-010-return-flow` 退回通知／修正流程 Knowledge baseline，並確認 Figma 內容與尺寸。
- In scope: 退回通知清單、退回原因、修正入口、修正資料確認與回到 UI-006。
- Out of scope: 已讀、分頁、API 合約與後端錯誤處理，依 requester decision 後續補充。
- Collection date: 2026-09-08 (Asia/Taipei)
- Collector: Codex Knowledge Collection and Resolution Agent

## Source Register
| Source ID | Location | Authority / Scope | Notes |
|---|---|---|---|
| SRC-UI010-001 | Figma `3327:13251`, page `UI` | Design / return flow | Figma MCP found return-flow section and screen frames; Android reference frames use `402 x 874` where shown. |
| SRC-UI010-002 | `docs/design/app-ui-requirements.md` | `OVL-NOTIFY-01`, `CMP-RETURN-BANNER` | Notification and return-reason presentation. |
| SRC-UI010-003 | Parent requirement FR-012–014, AC-012–014 | Product | Return list, banner, correction and confirmation. |
| SRC-UI010-004 | Roadmap and requester decisions | Process | UI-010 ownership; no read/pagination/API for now. |

## Missing or Inaccessible Sources
- Exact notification data/API contract and read/pagination rules are deferred.

## Initial Conflicts and Gaps
- Return correction reuses UI-006 form behavior; UI-010 owns notification and return context.

## Handoff to Resolution
- Coverage sufficient: yes
- Recommended next action: knowledge_resolution
