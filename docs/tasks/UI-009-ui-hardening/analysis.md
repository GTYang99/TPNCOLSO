# 分析

## 基線與範圍

- 使用者核准的知識基線：`KB-UI-009-UI-HARDENING-R1`；目前沒有獨立的 UI-009 `v0.1` 檔案。
- 範圍：UI-002 至 UI-008 現有認證、圖台、查詢、詳情、表單、相機與提醒流程；不得新增畫面、API 或產品行為。
- 設計來源只用於可見畫面觀察；不宣稱 Figma 像素完全相符。
- 目前工作分支為 `developer`。工作目錄有 UI-008、UI-009、UI-010 文件修改；本計畫不得覆蓋或整理那些既有修改。

## 現況盤點

| 畫面／流程 | 目前狀態所有者與既有測試 | UI-009 恢復範圍 |
|---|---|---|
| UI-002 登入／註冊 | `AuthViewModel` 持有記憶體狀態；`AuthScreenTest` 與 `AuthViewModelTest` 覆蓋主要畫面與互動。 | 組態變更沿用記憶體 ViewModel 狀態；程序死亡後密碼與驗證碼依 UI-002 安全規則重設，不把這些值寫入 saved state。其他欄位只依 UI-002 既有規則處理。 |
| UI-003 圖台與側欄 | `MapShellViewModel` 持有底圖、圖層、定位與側欄狀態；`MapShellScreenTest` 覆蓋控制項與回呼。 | 恢復目前底圖、查詢圖層內容及可見路由狀態；外部定位／相機物件不存入 saved state。 |
| UI-004 土地查詢 | `ParcelSearchViewModel` 持有查詢、結果與摘要狀態；`ParcelSearchHostTest` 覆蓋查詢與查無資料。 | 保存查詢字串及結果容器狀態；結果可依查詢從既有 fake source 重算。 |
| UI-005 土地詳情 | `ParcelDetailViewModel` 持有頁籤與期別；`ParcelDetailHostTest` 覆蓋唯讀、退回及返回流程。 | 保存地號、選取頁籤與期別；詳情資料從既有 fake source 重載。 |
| UI-006 調查表單 | `SurveyFormViewModel` 持有欄位、選取、照片、dirty 與送出狀態；`SurveyFormScreenTest` 覆蓋表單、照片與捨棄流程。 | 使用系統暫存 saved state 恢復核准的表單欄位、選取、照片 URI／中繼資料、dirty 與對話框狀態；不新增長期儲存。 |
| UI-007 拍照 | `PhotoCameraViewModel` 持有權限與拍攝狀態；相片由 UI-007 寫入 app-private cache；`PhotoCameraScreenTest` 覆蓋權限與拍攝狀態。 | 相片清單由 UI-006 恢復；程序重建時重新檢查權限並重建 CameraX use case。不可保存 `ImageCapture`、Preview 或進行中的拍攝工作。 |
| UI-008 刪除／捨棄提醒 | 刪除與捨棄對話框位於 `SurveyFormScreen`；`SurveyFormScreenTest` 已覆蓋確認與取消。 | 還原尚未處理的提醒；只有明確確認刪除才移除照片。Back、取消及外部關閉保留表單與照片。 |

## 跨畫面現況與缺口

- `MainActivity` 已啟用 edge-to-edge，Manifest 使用 `adjustResize`；登入與註冊目前套用 `imePadding` 和可捲動內容。其他畫面仍需以裝置矩陣驗證安全區域、鍵盤遮擋及高度不足行為。
- 目前多個 ViewModel 由 Compose `remember { ViewModel(...) }` 建立，沒有 `SavedStateHandle`；UI-006／UI-007 的 ViewModel 狀態因此無法證明會在程序死亡後恢復。
- `SurveyFormScreen` 已使用 `rememberSaveable` 保存刪除與捨棄提醒狀態；`PhotoCameraRoute` 已保存權限請求旗標，但 CameraX 執行物件必須在重建時重新綁定。
- 既有測試涵蓋各功能基本狀態；尚無涵蓋全部 UI-002–UI-008 的尺寸／字級／IME 矩陣，也沒有端到端程序死亡恢復證據。
- UI-002 目前 hosted CI 有 Android SDK／runner 問題，task state 為 `NOT VERIFIED`；其本機執行證據可作為回歸輸入，但不得改報 hosted CI PASS。
- 知識基線要求保留 402×874 參考、繁體中文無障礙名稱、至少 48dp 觸控目標與非顏色狀態線索；目前正式 Figma revision 未知，視覺證據限於 `visual_observation`。

## 計畫假設與邊界

- 測試矩陣採 320dp 窄畫面、360dp 一般手機畫面與 402×874 參考條件；320dp 是本 Task 的測試下限，不改寫產品支援範圍。
- Android 版本採 API 28 與 API 34；字級採 1.0 與 1.3。鍵盤情境只套用於登入、註冊、查詢與表單輸入流程。
- 只使用系統暫存 saved state；表單欄位與照片 URI／中繼資料不可寫入資料庫、額外檔案、log、analytics 或 API。既有 app-private photo cache 依 UI-007 契約保留。
- 相片 URI 若失效，必須保留表單其他內容並呈現可理解的缺圖狀態；不可靜默清除整份表單。
- 開發完成範圍允許延後 hosted CI 至 release candidate；延後責任與重啟里程碑列於 plan。
