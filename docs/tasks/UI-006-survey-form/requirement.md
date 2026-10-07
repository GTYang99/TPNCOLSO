# Requirement

## Background
- UI-005 已完成土地詳情與進入 UI006 的 callback handoff；UI006 負責共用的調查填報編輯、驗證、照片與送出呈現。
- 本 task 依 `KB-UI-006-SURVEY-FORM-R1` 建立可在無 production API 時驗證的 Android Compose UI。

## Goal
- 建立調查填報畫面，支援 10 個調查欄位的唯讀／可編輯狀態、條件驗證、dirty state、四張照片管理與本機假送出狀態。

## Functional Requirements
- 由 UI-005 進入時顯示固定土地摘要與 10 個調查欄位；勘查時間、勘查人員、提交批次唯讀，其餘欄位依資料規格可編輯。
- 現場勘查土地情形支援多選與自行輸入；「無占用」與其他占用項目互斥。
- 選取「無占用」時清空並隱藏占用型態、占用戶數、占用門牌號；有占用時三欄恢復並在送出前必填。
- 照片區只提供拍攝入口 callback，不提供相簿選擇；最多四張、每張標示拍攝時間與 4:3 狀態，刪除後可補拍。
- 送出前執行本機欄位與照片驗證；送出中禁止重複觸發，成功／失敗均保留可恢復的本機表單狀態。
- 表單有變更時標示 dirty state，關閉請求透過 callback 交給後續 Alert task 處理；本 task 不新增 production API。

## Non-functional Requirements
- Android 9+、Kotlin、Jetpack Compose、MVVM；畫面使用 immutable state、ViewModel events/effects 與既有 foundation theme/components。
- 主要操作觸控區至少 48dp；僅圖示操作需有繁體中文 content description；內容可捲動並可適配鍵盤與字級放大。
- 使用 Figma 記錄的視覺基準：402×874 screen、354×964 inner form、354×42 tabs、354×56 standard rows、168×126 photo tiles、56×56 FAB。

## Acceptance Criteria
- `AC-UI006-001`：從 UI-005 的新增／編輯 callback 開啟 UI006，顯示 10 個調查欄位；系統帶入的勘查時間、勘查人員、提交批次不可輸入，使用者欄位可輸入或選取。
- `AC-UI006-002`：修改任一使用者欄位後 state 標示 dirty；未修改或成功送出後 dirty 清除；關閉請求能回傳目前 dirty 狀態與 key_no。
- `AC-UI006-003`：現場勘查土地情形選取「無占用」時，立即清空並隱藏占用型態、占用戶數、占用門牌號；選取其他占用項目後三欄恢復。
- `AC-UI006-004`：有占用時，土地坐落、現場勘查土地情形、占用型態、占用戶數、占用門牌號依規則驗證；占用戶數只接受大於等於 1 的整數，空白、0、負數、小數與非數字不得送出。
- `AC-UI006-005`：照片只能由拍攝入口 callback 加入；未滿四張可拍攝，達四張後入口停用；每張顯示縮圖、拍攝時間與 4:3 驗證狀態，刪除後可補拍。
- `AC-UI006-006`：照片不是恰好四張或任一照片不是 4:3 時，送出被阻止並就近顯示錯誤；恰好四張且欄位有效時進入 submitting，假資料源回傳成功後顯示成功狀態並保留表單內容。
- `AC-UI006-007`：送出中主要操作停用且重複送出不增加請求次數；本機假資料源回傳失敗時顯示可理解的錯誤並保留欄位與照片。
- `AC-UI006-008`：表單內容在最小支援寬度、字級放大及鍵盤顯示時可捲動；所有 icon-only 操作具繁體中文語意，主要按鈕與照片操作具至少 48dp 觸控區。

## Constraints
- 只使用本機／fake data source；不得新增 production DTO、endpoint、API client、照片上傳或推測的 HTTP error mapping。
- UI007 負責後續 CameraX 實體相機接線；UI006 只提供 camera request/captured-photo port 與可測試的 local/fake photo fixture。
- UI008 負責共用 Android confirmation dialogs；UI006 保留 dirty state、delete/close intent 與可接入的 callbacks，不以未核定的 dialog 行為擴張 scope。
- 不修改 UI-005 的詳情 ownership；UI-005 只負責詳情顯示與進入 UI006。

## Open Questions
- `無`（production API/upload contract 與正式 pixel parity 已依 baseline 明確 deferred，不影響本 task UI implementation）。
