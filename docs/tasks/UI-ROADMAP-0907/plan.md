# Implementation Plan

## Goal

- 建立以 UI 介面分類、可依序執行且不受未完成 API 阻塞的 Android Task roadmap。

## Current Behavior

- UI requirement 已有畫面 inventory，但尚未拆成可執行的 Task 序列。

## Expected Behavior

- 開發者可由 `docs/tasks/README.md` 直接找到下一個 UI Task、依賴、完成邊界與延後的 Integration 工作。

## Affected Files

- `docs/tasks/README.md`
- `docs/tasks/UI-ROADMAP-0907/*`

## Implementation Steps

1. `UI-001-foundation`：建立 Theme tokens 與共用元件 catalog，含 Preview、semantics、loading/error/readonly states。
2. `UI-002-auth`：建立登入與註冊畫面、欄位驗證和 fake submitting/error states。
3. `UI-003-map-shell`：建立主導覽、側欄、圖台容器、底圖控制與定位權限 UI。
4. `UI-004-parcel-search`：建立查詢、空／錯誤／結果狀態及 parcel summary bottom sheet。
5. `UI-005-parcel-detail`：建立固定摘要、土地資料／現況雙頁籤及唯讀格式。
6. `UI-006-survey-form`：建立新增／編輯表單、條件欄位、validation、dirty state 與確認離開。
7. `UI-007-photo-camera`：接入 CameraX UI、四張 4:3 限制與縮圖；只處理本機照片。
8. `UI-008-alerts`：建立刪除提醒與編輯提醒 Android Alert，含取消／確認流程。
9. `UI-009-ui-hardening`：統一處理 Android 9+、多尺寸、字級、IME、accessibility、旋轉與狀態恢復。
10. `UI-010-return-flow`：建立退回通知、意見 banner、修正與確認修正流程。
10. API schema 核定後，依序規劃 `INT-001` 至 `INT-004`，用 production data source 替換 fake source。

## Test Plan

- 每個 UI Task 至少包含 Compose Preview、狀態矩陣與對應 Compose UI tests。
- fake data 覆蓋 loading、content、empty、error 及該畫面特有狀態。
- UI-009 執行跨畫面 Android 9+、字級、IME、無障礙與 state restoration regression suite。

## Regression Plan

- 後續 Task 必須重跑前序共用元件與 navigation tests。
- Integration 替換資料來源時，不得改變已通過的 UI state contract；差異須回到 Knowledge／Planning。

## Risks

- Map 與 Camera 能力仍需提早做 SDK spike；各自包含在 UI-003、UI-007，而非等 API integration。
- 未核定產品衝突只允許以視覺 placeholder 呈現，不得固化成 production behavior。

## Open Questions

- API integration 的開始日期取決於 schema 與權限契約核定，不影響 UI-001 至 UI-009 的排序。
