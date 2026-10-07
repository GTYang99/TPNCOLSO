# Knowledge Collection

## Question and Scope
- Task question: 建立 `UI-009-ui-hardening` 跨畫面品質 Knowledge baseline。
- In scope: Android 9+、多尺寸、字級放大、IME、安全區域、無障礙、旋轉與狀態恢復。
- Out of scope: 新增產品畫面、API、資料行為與 Figma 新設計。
- Collection date: 2026-09-08 (Asia/Taipei)
- Collector: Codex Knowledge Collection and Resolution Agent

## Source Register
| Source ID | Location | Authority / Scope |
|---|---|---|
| SRC-UI009-001 | `docs/design/app-ui-requirements.md` | Responsive/accessibility rules, `402 x 874` reference, 48dp targets. |
| SRC-UI009-002 | `docs/tasks/README.md` and roadmap | Android 9+, text/IME/accessibility/rotation scope. |
| SRC-UI009-003 | Figma MCP inspections UI-002–UI-008 | Screen and component dimensions used as regression references. |

## Missing or Inaccessible Sources
- No single Figma node represents cross-screen hardening; evidence is distributed across screen nodes.

## Initial Conflicts and Gaps
- Figma uses a `402 x 874` reference canvas while Android devices vary; responsive rules must govern runtime.

## Handoff to Resolution
- Coverage sufficient: yes
- Recommended next action: knowledge_resolution
