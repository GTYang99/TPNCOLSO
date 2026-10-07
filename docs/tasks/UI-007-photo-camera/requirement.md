# Requirement

## Background
- UI-006 已建立本機表單與照片 callback，但目前 debug host 只用 synthetic local URI 代替拍攝。
- UI-007 依 `KB-UI-007-PHOTO-CAMERA-R1` 接上 CameraX，完成相機入口與本機照片回傳；上傳仍由 INT-003 處理。

## Goal
- 在 Android 9+ Compose app 中提供 camera-only 的現場拍照流程，輸出可由 UI-006 使用的本機照片 URI。
- 讓照片區符合 Figma `2938:193` 的相機 tile、4:3 縮圖與四張上限視覺邊界。

## Functional Requirements
- 從 UI-006 的相機入口開啟 UI-007 CameraX preview；不提供相簿選取或上傳操作。
- CameraX capture 固定使用 4:3 target aspect ratio，寫入 app-private cache 並回傳 local URI 與民國年拍攝時間。
- 相機權限未授予時顯示可理解的拒絕狀態與重新授權入口；關閉或權限拒絕不得清除既有表單照片。
- UI-006 photo section 顯示 168×126dp 的照片 tile；local URI 可顯示縮圖、拍攝時間與刪除入口，未滿四張顯示 168×126dp 的「拍攝照片」入口，滿四張停止新增。
- 單次拍攝完成後只回傳一張照片給 UI-006；相機關閉、重拍與重新進入不新增重複資料。

## Non-functional Requirements
- 支援 Android 9+、不同寬度與字級放大；主要操作觸控區至少 48dp。
- 相機與檔案操作遵守 lifecycle，關閉畫面時解除 CameraX binding，不在主執行緒做縮圖解碼或檔案檢查。
- 所有相機／刪除 icon-only 操作具繁體中文 accessibility semantics；不記錄照片內容、Token 或個資。

## Acceptance Criteria
- `AC-001`：在 UI-006 未滿四張照片時點擊「拍攝照片」，開啟 UI-007 camera route；相機畫面提供 4:3 preview、拍攝與取消操作，且不存在相簿選取入口。
- `AC-002`：未授予相機權限時，畫面顯示「需要相機權限才能拍攝現況照片」與重新授權操作；選擇取消／拒絕時既有照片與表單內容保持不變。
- `AC-003`：成功拍攝後以 app-private local URI 保存一張照片，回傳照片 URI 與非空拍攝時間；CameraX output 經 4:3 aspect 檢查後才回傳給 UI-006。
- `AC-004`：UI-006 photo section 在 0、1–3、4 張狀態分別顯示相機 tile／照片 tile，tile 為 168×126dp、照片時間可讀；達四張後相機入口不存在，刪除後可再次拍攝。
- `AC-005`：拍攝中禁止重複觸發；成功、失敗、取消與重進相機流程均不造成重複 photo event 或遺失既有照片。
- `AC-006`：相機 route 關閉或 lifecycle 遭到 dispose 時解除 CameraX use cases；無法開啟相機或寫檔時顯示可恢復錯誤，不崩潰且不清空 UI-006 狀態。
- `AC-007`：UI-007 的 ViewModel／Compose semantics 測試覆蓋權限、capture state、錯誤恢復與可及性；受影響的 UI-006 unit／connected tests 與 debug/release 編譯通過。

## Constraints
- 僅處理本機 URI；不得新增 production upload API、DTO、server storage、HTTP error mapping 或 persistence schema。
- UI-006 保有 `SurveyFormPhoto` list 與送出驗證；UI-007 透過既有 `onCaptureRequested` callback 回傳 captured photo，不重新建立表單狀態。
- UI-008 保有刪除確認 dialog；UI-007 僅提供現有刪除入口與補拍能力。
- Figma `2938:193`／child frames `4947:26040`, `4947:26547`, `4947:27279` 是照片區視覺來源；camera preview surface 本身沒有已核定的 Figma frame，採 platform-native CameraX presentation 並不宣稱 pixel parity。
- 使用 repository 可取得的 CameraX `1.4.1`；不引入相簿、網路或地圖依賴。

## Open Questions
- Production upload API、伺服器儲存與替換照片規則仍由 INT-003 定義；不阻擋本次 UI-only development scope。
- 相機權限在不同 OEM 的「永久拒絕」文案與設定頁導引，留待 UI hardening／產品決策；本次提供可重試的最小狀態。
