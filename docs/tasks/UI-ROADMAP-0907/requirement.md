# Requirement

## Goal

- 依 UI 介面分類 Android 開發 Task，並按共用元件及畫面依賴排定先後順序。

## Requirements

- Task 必須以畫面或跨畫面 UI 能力分類，不以未完成的 API endpoint 分類。
- 每個 UI Task 都要有可使用 fake data 驗收的明確邊界。
- API 串接必須獨立列為 deferred Integration Tasks。
- 排序須避免後續畫面重複建立共用元件或狀態模型。
- 現有 requirement、畫面 ID、元件 ID 與 AC 必須可追溯。

## Acceptance Criteria

- AC-001：所有 APP 畫面均被一個 UI Task 覆蓋。
- AC-002：UI Tasks 有唯一、明確且依賴合理的執行順序。
- AC-003：每個 UI Task 標明 API boundary 與 fake-data 策略。
- AC-004：API integration 另列，不阻塞可獨立完成的 UI。
- AC-005：Task index 可由 `docs/tasks/README.md` 快速閱讀。
- AC-006：不修改 production code。

## Constraints

- Android 9+、Kotlin、Jetpack Compose。
- 不從未完成 API 規格推測 production contract。
- `.git` metadata unavailable。
