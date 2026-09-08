# Task Index

本索引依實際執行順序排列。現階段採 UI-first：先完成可獨立驗證的 Compose 畫面與互動，再於 API schema 核定後建立 Integration Tasks。

## Active Roadmap

- [UI-first Task Roadmap](./UI-ROADMAP-0907/plan.md)
- Roadmap state: [state.yaml](./UI-ROADMAP-0907/state.yaml)

## UI Implementation Order

| Order | Task ID | UI category | Deliverable | API policy |
|---:|---|---|---|---|
| 1 | [`UI-001-foundation`](./UI-001-foundation/plan.md) | Design system／開發入口 | AppRoot、debug-only 直接登入、session contract、Theme／Preview/Test scaffold | Plan ready；不依賴 API |
| 2 | [`UI-002-auth`](./UI-002-auth/plan.md) | 登入／註冊 | 登入、註冊、欄位驗證、載入與錯誤狀態 | Plan ready；使用 fake state，登入契約後補 |
| 3 | `UI-003-map-shell` | 圖台與導覽 | 圖台容器、頂部操作、側欄、底圖切換、定位權限狀態 | 不呼叫業務 API |
| 4 | `UI-004-parcel-search` | 土地查詢／摘要 | 搜尋、查無資料、結果定位狀態、土地摘要 Sheet | 使用 fake parcel list |
| 5 | `UI-005-parcel-detail` | 土地詳情 | 固定摘要、雙頁籤、唯讀欄位、狀態顯示 | 使用 fake parcel detail |
| 6 | `UI-006-survey-form` | 調查填報 | 表單、條件欄位、驗證、dirty state、送出狀態 | 本機驗證；不送 API |
| 7 | `UI-007-photo-camera` | 相機／照片 | CameraX 入口、4:3 拍攝、四張限制、縮圖 | 本機 URI；不上傳 |
| 8 | `UI-008-alerts` | 刪除提醒／編輯提醒 Alert | Android Alert、取消／確認、刪除與捨棄編輯 | 本機狀態；不呼叫 API |
| 9 | `UI-009-ui-hardening` | 跨畫面品質 | Android 9+、字級、IME、無障礙、旋轉與狀態恢復 | 不依賴 API |
| 10 | `UI-010-return-flow` | 退回通知／修正 | 通知清單、退回 banner、修正表單、確認修正 | 使用 fake notification/parcel |

## Deferred Integration Queue

下列項目不得混入前述 UI Tasks；等 API request／response schema、錯誤與權限契約核定後另立 Task：

1. `INT-001-auth-api`：註冊、登入、登出、Token lifecycle 與 401。
2. `INT-002-map-parcel-api`：圖台範圍、搜尋、詳情與歷史期別。
3. `INT-003-survey-photo-api`：首次送出、編輯、照片上傳、重試與錯誤映射。
4. `INT-004-return-notification-api`：退回清單、已讀、修正重送與狀態同步。

## Start Rule

- 同一時間只啟動序列中最前面且未完成的 UI Task。
- 啟動時建立 `docs/tasks/<task-id>/`，從 canonical templates 產生 Knowledge、Requirement、Plan、Review、State 與 Verification artifacts。
- API 未完成不得用推測的 DTO 或 endpoint 當作 UI Task 的完成條件。
- Fake data 必須集中在可替換的資料來源，不得散落在 Composable。
- 前一 Task 的共用元件與 UI tests 通過後，下一 Task 才能開始。

## Historical Tasks

- `UI-0907-app-ui-requirements`：UI requirement 與 Figma／產品衝突盤點。
- `DOCS-0907-sdlc-workflow`：SDLC 與 Knowledge workflow 文件。
- `DOCS-0907-restructure`：canonical folder restructure。
