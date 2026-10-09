# UI009-009 根因分析

## 問題

- Issue：`UI009-009`
- 分類：`implementation_regression`
- 影響驗收條件：`AC-004`
- 流程：UI-006 調查表單

## 重現證據

- 裝置：`Medium_Phone (AVD) - 14`，Android 14／API 34。
- 條件：1080×2400、420dpi、字級 1.0；使用系統 Gboard。
- 以 debug-only `Ui009StateRestoreActivity` 開啟表單，點選「土地坐落(路名或適當敘明位置)」欄位，確認 `dumpsys input_method` 回報 `mInputShown=true`。
- 螢幕擷取：[`api34-ui006-ime-submit-obscured.png`](evidence/api34-ui006-ime-submit-obscured.png)。鍵盤佔據視窗下方；「送出調查」被遮住。表單內容可捲動，但底部動作仍留在鍵盤後方。

## 根因

`SurveyFormScreen` 的根版面原先沒有套用 IME inset。其內容區雖有 `weight(1f).verticalScroll(...)`，但「送出調查」是內容捲動區外的固定同層元件；若根版面未依 IME 調整高度，固定動作會落在鍵盤覆蓋區。

另確認測試宿主與主 Activity 的視窗設定不一致：主 Activity 使用 `enableEdgeToEdge()` 並在 manifest 設定 `adjustResize`，原 `Ui009StateRestoreActivity` 未設定這兩項。故修正 `imePadding()` 後在舊測試宿主取得的畫面仍被鍵盤覆蓋，不能用來判定主 Activity 的實際行為。根因修正須同時使直接掛載 Route 的測試宿主與主 Activity 視窗條件一致，再以真實系統 IME 驗證。

這是 UI layout 與測試宿主設定缺口，非 API／fake data 或輸入法資料造成；UI009 核准計畫已要求依驗證矩陣修正 IME／inset 缺口。

## 路由

## 修正與驗證結果

- `SurveyFormScreen` 根 `Surface` 已套用 `imePadding()`。
- debug-only `Ui009StateRestoreActivity` 已啟用 `enableEdgeToEdge()`，並在 debug manifest 設定 `adjustResize`，與主 Activity 的視窗條件一致。
- API 34／Gboard 開啟時，`送出調查` 可見且位於鍵盤上方；無障礙節點 bounds 為 `[436,1354][644,1429]`。修正後截圖：`evidence/api34-ui006-ime-submit-visible.png`。
- 修正後 `SurveyFormScreenTest` 9 項通過；完整 `connectedDebugAndroidTest` 56 項通過，0 failures、0 errors、0 skipped。
- UI009-009 已解決。此結果僅涵蓋 API 34 的 UI-006 表單路徑；AC-004 全部文字輸入路徑與 API 28 矩陣仍待驗證。

Debug 修正已完成，返回 `implementation` 繼續 Developer Validation；不可據此宣告 AC-004 整體通過。
