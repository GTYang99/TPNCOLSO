# Repository Analysis

## Current Behavior
- UI-005 已有 `ParcelDetailCallbacks.onEditRequested(keyNo)`，但 debug host 目前只把 key_no 傳給外部 callback，沒有 UI006 畫面。
- `ParcelSurveyRecord` 已提供 10 個欄位的唯讀展示模型；既有 foundation 提供文字欄位、單選群組、下拉、按鈕、錯誤與 loading 元件。
- Repository 使用單一 app module；feature 以 immutable Compose state、ViewModel、fake data source 與 unit/Compose tests 分層。

## Expected Behavior
- UI005 的 edit callback 開啟可捲動的 UI006 表單，保留土地摘要與系統帶入欄位，並管理使用者欄位、條件顯示、照片及 dirty/submit state。
- 所有驗證與狀態轉移在 feature-owned pure validation/ViewModel 中完成；Composable 只 render state 與發送 event。
- debug host 可使用 deterministic fake form/photo source 完成 UI 流程；release 不依賴 debug-only entry 或 production API。

## Affected Modules
- `app/src/main/java/.../feature/surveyform/`：新增 contract、validation、ViewModel、screen、fake/local data source。
- `app/src/debug/java/.../AppEntry.kt`：接上 UI-005 edit callback、UI006 route 與關閉返回行為。
- `app/src/test/java/.../feature/surveyform/`：純驗證、reducer/ViewModel、submit duplicate suppression 測試。
- `app/src/androidTest/java/.../feature/surveyform/`：Compose semantics、條件欄位、照片、dirty、submit/error 與可捲動畫面測試。
- `docs/tasks/UI-006-survey-form/`：requirement、analysis、plan、plan-review、issue-log、validation/evidence。

## Dependencies
- Active baseline `KB-UI-006-SURVEY-FORM-R1` and parent AC-008–AC-011.
- UI-005 completed handoff at commit `cb81272` and `ParcelDetailCallbacks.onEditRequested`.
- Existing `AppThemeTokens` and foundation components; no new dependency is needed.
- UI007 camera port and UI008 confirmation dialog are downstream integration points, not implementations in this task.

## Risks
- UI005 host currently assumes edit callback is terminal; wiring a new overlay must preserve search/detail close behavior and not mutate the read-only detail state.
- The Figma per-state pixel evidence is incomplete; visual implementation can claim only the recorded dimensions and component hierarchy.
- Photo and submit APIs are intentionally absent; fake ports must remain replaceable and must not resemble production DTOs or endpoints.
- Large form content may expose IME, touch-target or font-scale regressions; Compose tests and a connected smoke test are required.

## Unknowns
- Production upload protocol, HTTP error mapping and role/state edit matrix remain deferred by the active baseline and are not needed for this UI-only task.
