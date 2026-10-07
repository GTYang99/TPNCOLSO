# Requirement

## Background
- UI-004 已提供 fake parcel search 與土地摘要；UI-005 需要承接摘要中的土地資料並呈現完整土地詳情。
- `KB-UI-005-PARCEL-DETAIL-R1` 定義本 task 的 detail container、雙頁籤、歷史檢視與 UI-006 handoff 邊界。

## Goal
- 在現有 map/search host 上提供可驗證的土地詳情檢視容器，使用 fake detail/history data，讓使用者查看土地資料與實地勘查現況。

## Functional Requirements
- 由土地摘要進入土地詳情，顯示固定摘要：調查狀態、土地編號、地號與現場勘查土地情形。
- 詳情預設選取「土地資料」頁籤，呈現土地標示 8 欄與套匯圖資 2 欄，全部唯讀。
- 「實地勘查土地現況」頁籤預設顯示最新一期；期別選單可切換歷史紀錄，歷史紀錄全部唯讀。
- 狀態為退回時顯示退回原因分類 Tag 與退回原因說明。
- 未調查或可編輯的狀態顯示進入 UI-006 的入口；入口只發出 callback，不在 UI-005 實作編輯、驗證、照片或送出。
- 詳情資料由 feature-owned fake data source 提供，production API、DTO、endpoint 與錯誤 mapping deferred。

## Non-functional Requirements
- 支援 Android 9+、Compose responsive layout、可捲動內容與至少 48dp 操作區。
- Composable 只渲染 immutable state 並發出事件；資料查找與頁籤／期別狀態由 ViewModel 管理。
- 所有互動控制與唯讀資料區塊提供可測試 semantics/test tags。

## Acceptance Criteria
- `AC-UI005-001`：由 fake parcel summary 點擊「查看詳情」後，map context 保持 mounted，詳情顯示固定摘要中的調查狀態、土地編號、地號與現場勘查土地情形。
- `AC-UI005-002`：詳情初始顯示「土地資料」頁籤，且可看到土地標示 8 欄與套匯圖資 2 欄；所有欄位皆為唯讀，不提供可輸入控制。
- `AC-UI005-003`：切換至「實地勘查土地現況」頁籤時預設顯示最新一期，並以唯讀方式呈現 10 個調查欄位的值或照片摘要；頁籤內不直接修改資料。
- `AC-UI005-004`：選取歷史期別後，畫面顯示所選期別資料、標示為歷史檢視且所有欄位維持唯讀；切回最新期別後恢復最新資料。
- `AC-UI005-005`：退回資料顯示退回原因分類 Tag 與完整退回原因說明；非退回資料不顯示退回原因區塊。
- `AC-UI005-006`：點擊 UI-005 的「進入調查」／「編輯調查」入口只觸發 UI-006 handoff callback，UI-005 不建立 dirty state、欄位驗證、照片、送出或 API 行為。
- `AC-UI005-007`：ViewModel 可使用替代的 detail data source；fake fixtures 不位於 Composable，且查無 detail 時保留容器並呈現可理解的錯誤狀態。

## Constraints
- 只使用 `KB-UI-005-PARCEL-DETAIL-R1`；repository 中沒有獨立、可驗證的 `v0.1` UI-005 artifact，不得宣稱該版本存在。
- 使用現有 UI-001 theme/foundation 與 UI-004 summary；不新增 production API、DTO、persistence、WMTS、photo capture 或 UI-006 form behavior。
- Figma detail 尺寸是 visible design evidence；在正式 revision 核定前不得宣稱 pixel parity。
- 直接在 requester 指定的目前 `main` branch 開發，保留不相關檔案與變更。

## Open Questions
- 正式 Figma detail revision 尚未核定；本 task 以 observable behavior 與 common-template composition 驗證，pixel parity deferred。
