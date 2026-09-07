# Knowledge Collection

## Scope

- 將 Android APP 工作依 UI 介面分類並排定可執行順序。
- API 尚未完成，因此本次不定義 request／response DTO，也不把串接列入 UI Task 完成條件。

## Source Register

| Source ID | Source | Authority | Scope | Freshness |
|---|---|---|---|---|
| SRC-001 | Current user instruction, 2026-09-07 | authoritative | UI-first order and API deferral | current |
| SRC-002 | `docs/design/app-ui-requirements.md` | supporting draft | screen/component inventory | current |
| SRC-003 | `docs/product/land-survey-115/4 介面規格.md` | authoritative | screen structure and behavior | current |
| SRC-004 | `docs/product/land-survey-115/2 資料規格.md` | authoritative | field/display rules | current |
| SRC-005 | `docs/product/land-survey-115/3 系統行為規則.md` | authoritative with known conflicts | state and validation behavior | current |
| SRC-006 | `docs/product/land-survey-115/5 API規格.md` | incomplete | endpoint summary and missing schemas | draft |
| SRC-007 | `app/src/main/` and Gradle configuration | authoritative current implementation | Compose baseline and available dependencies | working tree 2026-09-07 |

## Coverage

- Covered: 共用元件、認證、圖台、查詢、摘要、詳情、調查表單、照片、退回流程、responsive/accessibility。
- Deferred: 實際 API DTO、Token、通知、上傳、後端錯誤與狀態同步。
- Environment limitation: `.git` metadata unavailable。
