# UI009-009 最小修正計畫

## 對應

- Issue：`UI009-009`
- 驗收條件：`AC-004`
- Plan Review：iteration 5；原核准範圍包含依矩陣修正表單 IME／system inset 缺口。

## 修改

1. 在 `SurveyFormScreen` 的根版面套用 Compose `imePadding()`，令可用高度排除系統鍵盤區域。
2. 令 debug-only `Ui009StateRestoreActivity` 使用與主 Activity 相同的 `enableEdgeToEdge()` 與 `adjustResize` 視窗設定，確保直接掛載 Route 的測試宿主代表實際 UI 環境。
3. 保持表單內容的垂直捲動及「送出調查」按鈕在捲動區外的既有語意；鍵盤顯示時按鈕須完整顯示在 IME 上方。
4. 不改動欄位驗證、送出行為、資料來源、導覽、API 或狀態保存規則。

## 驗證

- API 34 模擬機以 UI-006 debug-only 表單宿主點選文字欄位，確認系統 IME 可見，檢查 `送出調查` bounds 完整位於 IME 上緣以上且可點選。
- 執行 `SurveyFormScreenTest`、表單相關 host／state restoration instrumentation 測試與必要 debug 編譯。
- 記錄修正前後的 API、解析度、密度、字級、指令、測試結果與截圖證據。
- 若最小修改無法滿足 AC-004，停止並回 Planning；不降低驗收條件。

## 執行狀態

- 狀態：已完成，Issue `UI009-009` 已解決。
- API 34／Gboard 的 UI-006 表單路徑已確認 IME 顯示時「送出調查」完整位於鍵盤上方，節點 bounds 為 `[436,1354][644,1429]`；截圖：`evidence/api34-ui006-ime-submit-visible.png`。
- 修正後 `SurveyFormScreenTest` 9 項通過；完整 Android instrumentation suite 56 項通過，0 failures、0 errors、0 skipped。
- AC-004 整體仍是 partial / NOT VERIFIED，尚需涵蓋所有文字輸入路徑與 API 28。

## 風險與回復

- `imePadding()` 可能減少可見表單高度；既有垂直捲動須仍能讓欄位與送出操作可達。
- 若發生回歸，僅回復本 Issue 的 UI-006 inset 修改，保留其他 UI009 與工作目錄變更。
