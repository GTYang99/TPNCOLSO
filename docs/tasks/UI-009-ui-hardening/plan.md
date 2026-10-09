# 實作計畫

## 目標

- 在不改變 UI-002 至 UI-008 已核准行為的前提下，完成跨畫面的窄螢幕、字級、IME、無障礙與狀態恢復硬化。

## 範圍

- 盤點並修正登入／註冊、圖台、查詢、詳情、調查表單、拍照及刪除／捨棄提醒。
- 使用既有 Compose、Material 3、MVVM、ViewModel 與 fake data source；僅在 lifecycle saved state 整合所需時補足 AndroidX lifecycle Compose 依賴。
- 不新增正式登入狀態、API、DTO、資料庫、長期表單儲存、權限或產品畫面；不作正式 Figma 像素驗收。

## 目前行為

- `MainActivity` 已啟用 edge-to-edge 與 `adjustResize`；登入／註冊使用 `imePadding`，表單與相機已有少量 `rememberSaveable` 狀態。
- 功能 ViewModel 多由 `remember` 手動建立，未接上 `SavedStateHandle`；目前沒有跨 UI-002–UI-008 的完整狹窄畫面、字級、IME 與程序死亡測試矩陣。

## 預期行為

- 主要內容在 320dp、360dp 與 402×874 參考條件下可到達；字級 1.3 與 IME 不會遮住必要內容或主要動作。
- 所有核對的圖示動作有繁體中文無障礙名稱，觸控目標至少 48dp，狀態與錯誤不只靠顏色表達。
- 組態變更與程序死亡後，允許的流程狀態依所屬 Task 契約恢復；UI-006 表單與 UI-007 照片狀態僅用系統暫存 saved state；登入密碼、註冊密碼及驗證碼不保存。

## 影響檔案

- `gradle/libs.versions.toml`、`app/build.gradle.kts`：若現有 lifecycle API 不足，加入同版本 AndroidX lifecycle Compose／saved-state 支援。
- `app/src/debug/java/com/example/tp_ncolso_android/AppEntry.kt`：以 lifecycle ViewModel owner／factory 建立現有 fake-flow ViewModel，保持 debug-only 導覽與資料來源邊界。
- `app/src/debug/java/com/example/tp_ncolso_android/Ui009StateRestoreActivity.kt` 與 debug manifest：新增僅 debug 可用、可由 ADB 獨立啟動的流程測試入口，直接掛載 UI-006 Route 與 fake source；以明確的 debug intent extras 注入可辨識的測試草稿與照片狀態；release source set 不包含此入口。
- `app/src/main/java/com/example/tp_ncolso_android/feature/auth/AuthViewModel.kt`：驗證組態變更行為及密碼、驗證碼不寫入 saved state；不保存敏感值。
- `app/src/main/java/com/example/tp_ncolso_android/feature/mapshell/MapShellViewModel.kt`、`feature/parcelsearch/ParcelSearchViewModel.kt`、`feature/parceldetail/ParcelDetailViewModel.kt`：保存最小必要的非敏感選取／路由狀態，資料內容依既有 source 重載。
- `app/src/main/java/com/example/tp_ncolso_android/feature/surveyform/SurveyFormViewModel.kt`、`SurveyFormScreen.kt`：保存已核准表單值、照片 URI／中繼資料、dirty 及未結束提醒狀態；只有明確刪除確認移除照片。
- `app/src/main/java/com/example/tp_ncolso_android/feature/photocamera/PhotoCameraViewModel.kt`、`PhotoCameraScreen.kt`：恢復權限／錯誤顯示所需 UI 狀態，程序重建後重新檢查權限並重建 CameraX；不保存相機執行物件或拍攝工作。
- `feature/auth/LoginScreen.kt`、`RegisterScreen.kt`、`feature/mapshell/MapShellScreen.kt`、`feature/parcelsearch/ParcelSearchScreen.kt`、`feature/parceldetail/ParcelDetailScreen.kt`：只修正矩陣證實的尺寸、insets、IME 或 semantics 缺口。
- 共用元件只有在矩陣證明缺陷為跨功能共用時才修改 `app/src/main/java/com/example/tp_ncolso_android/ui/foundation/component/FoundationComponents.kt`。
- 測試延伸既有 `app/src/androidTest/.../AuthScreenTest.kt`、`MapShellScreenTest.kt`、`ParcelSearchHostTest.kt`、`ParcelDetailHostTest.kt`、`SurveyFormScreenTest.kt`、`PhotoCameraScreenTest.kt`、`FoundationComponentTest.kt`，並在相應 ViewModel 單元測試加入 saved-state 還原案例。
- `app/src/androidTest` 流程測試宿主直接掛載各 Route 與 fake data source，提供 SavedStateRegistry／ViewModelStore，使用測試身分繞過 `DebugSessionOwner`，不把假登入角色寫入 saved state；只驗證狀態 round-trip 及 Activity recreation。
- `scripts/ui009_process_death_check.sh`：由開發主機上的 ADB client 啟動 debug-only Activity、背景化、終止目標 app process、打開系統 Recents 並點選剛加入的前景 task 卡片，最後檢查 UIAutomator accessibility dump。ADB client 不依附 Android app process，因此 `am kill` 不會終止驗證者。
- Task 證據：更新 `analysis.md`、`plan.md`、`execution-report.md`、`verification.md` 與 `state.yaml`；保留 UI-008、UI-010 及其他既有修改。

## 技術設計與畫面／狀態矩陣

| 流程 | 核心狀態／情境 | 還原策略 | 驗證證據 |
|---|---|---|---|
| UI-002 登入／註冊 | 空白、錯誤、輸入、IME、放大字級 | 組態變更由 lifecycle ViewModel 保留記憶體狀態；程序死亡後不保存密碼、確認密碼及驗證碼。 | `AuthScreenTest`、`AuthViewModelTest`；密碼／驗證碼 saved-state 排除檢查。 |
| UI-003 圖台 | 側欄、底圖、查詢圖層、定位狀態 | 保存底圖與非敏感畫面選取；重新取得外部權限／定位狀態，不保存平台物件。 | `MapShellScreenTest` 與 `MapShellViewModelTest`。 |
| UI-004 查詢 | 輸入、查無資料、摘要 | 保存查詢及容器狀態；以 query 從 fake source 重算結果。 | `ParcelSearchHostTest` 與 `ParcelSearchViewModelTest`。 |
| UI-005 詳情 | 頁籤、期別、唯讀／退回狀態 | 保存 key、頁籤及期別；從 fake source 重載內容。 | `ParcelDetailHostTest` 與 `ParcelDetailViewModelTest`。 |
| UI-006 表單 | 欄位、選取、dirty、照片、送出結果 | 依 requester 核准的範圍寫入系統暫存 saved state；不寫入長期儲存。 | `SurveyFormScreenTest`、`SurveyFormViewModelTest` 與重新建構 SavedStateHandle 測試。 |
| UI-007 拍照 | 權限、預覽、拍攝、回傳照片 | 恢復 UI-006 管理的照片 URI／中繼資料；權限重查、CameraX use case 重建、進行中拍攝回到可重試狀態。 | `PhotoCameraScreenTest`、`PhotoCameraViewModelTest`；失效 URI 保留其餘表單狀態。 |
| UI-008 提醒 | 刪除確認、捨棄確認、Back／取消 | 還原尚未確認的對話框；取消、Back、外部關閉不改資料，只有明確確認才刪除。 | `SurveyFormScreenTest` 的刪除／捨棄案例及重建案例。 |

## 實作步驟

1. 依 `analysis.md` 的流程矩陣逐一檢視現有畫面與 test tags，確認可觀察的路由、狀態、窄畫面、字級、IME、insets 與無障礙缺口。
2. 先建立最小 lifecycle saved-state 建立方式，讓既有 fake source ViewModel 由目前 `ViewModelStoreOwner` 建立；不新增 navigation、DI 或資料層架構。AndroidTest 流程宿主直接掛載指定 Route；debug-only launchable Activity 提供可跨程序保存的測試 Task，避免綁在 `AppEntry` 以 `remember` 建立的 debug session 身分上。
3. 按各流程 owner 保存必要 primitive 狀態與識別值；表單值／照片只放入系統暫存 saved state，且相機 runtime object、密碼、驗證碼與 Token 不入 Bundle。
4. 依矩陣修正已證實的 Compose 尺寸、scroll、IME／system bar inset 與 semantics 缺口；共用元件僅在缺陷確為共用時修改。
5. 為 saved-state round-trip、狀態重載、未確認提醒與敏感值排除新增針對性測試，並將 UI 測試延伸到完整 viewport／字級／IME 矩陣。
6. 以 API 28 與 API 34 驗證；先由 AndroidTest 用 `ActivityScenario.recreate()` 覆蓋組態重建。再由開發主機執行 `scripts/ui009_process_death_check.sh`：script 透過 ADB 啟動 debug-only `Ui009StateRestoreActivity` 並傳入唯一草稿／照片標記，將 app 送背景、確認系統保存 Task 後以 `am kill <applicationId>` 結束目標 process，送出 `KEYCODE_APP_SWITCH` 開啟 Recents，再以 UiAutomator dump 取得顯示尺寸並點選最新的前景 task 卡片；確認 app route、草稿、dirty 狀態及照片標記都出現在恢復後的 accessibility dump。不得使用 API 不支援的 `am start --activity-from-recents` 參數。AndroidTest runner 不參與此程序終止序列。至少 API 28 與 API 34 各執行一次；debug Activity 僅使用 fake source，release manifest 不宣告此 Activity。若系統未保留 Task 或 host 無法連接 ADB，記為受限／NOT VERIFIED，不得以旋轉或單純重建冒充程序死亡證據。
7. 完成受影響測試、debug 編譯與回歸後，記錄實際 revision、環境、指令、逐 AC 結果及所有限制。

## 測試計畫

- 裝置／畫布：320dp 窄畫面、360dp 一般畫面、402×874 參考條件；Android 9/API 28 與 Android 14/API 34。
- 字級：1.0 與 1.3；IME：登入、註冊、查詢與表單欄位逐路徑測試。
- UI：所有矩陣路由確認必要內容可見或可捲動到達；量測主要互動區至少 48dp；驗證繁體中文 semantics 及非顏色狀態線索。
- 狀態：ViewModel SavedStateHandle round-trip、`ActivityScenario.recreate()` 組態重建、ADB host script 執行的系統程序死亡／Recents 恢復、表單 dirty、四張照片、URI 無效、刪除與捨棄提醒；測試不依賴 `DebugSessionOwner` 在程序死亡後保留。
- 預定本機檢查：`./gradlew :app:testDebugUnitTest`、受影響的 `:app:connectedDebugAndroidTest`（API 28 與 API 34）、`./gradlew :app:assembleDebug`。本計畫階段尚未執行以上指令。

## 回歸計畫

- 保留 UI-002 原有認證、欄位驗證與敏感值規則；重新執行 auth 已有測試，hosted CI 舊失敗維持原記錄。
- 確認 UI-003 側欄、三底圖、定位狀態；UI-004 查詢／摘要；UI-005 頁籤／唯讀與 UI-006 編輯 handoff 不退化。
- 確認 UI-006 無占用欄位互斥、四張照片限制、送出狀態及 UI-008 刪除／捨棄語意不變。
- 確認 UI-007 camera permission、4:3 capture、錯誤重試及 app-private cache 輸出不變。
- 確認 `release` source set 不引用 debug 假登入或測試資料；不宣稱 production authentication／API 已驗證。

## Acceptance Criteria Traceability

| AC | 實作步驟 | 驗證方式 |
|---|---|---|
| AC-001 | 步驟 1、5、7 | matrix 列出 UI-002–UI-008 畫面／狀態與逐路由 runtime evidence；確認沒有新增功能。 |
| AC-002 | 步驟 1、4、6 | API 28／34 × 320dp／360dp／402×874 viewport 下主要內容可見或可捲動到達且無阻塞重疊。 |
| AC-003 | 步驟 4、5、6 | 字級 1.3 下標籤、值、按鈕與對話框動作可見或可到達，無功能性截斷。 |
| AC-004 | 步驟 1、4、5、6 | 各文字輸入流程於 IME 顯示時可聚焦並將主要動作捲入可視範圍，內容不進入安全區域。 |
| AC-005 | 步驟 1、4、5 | Compose semantics 與 bounds 檢查繁體中文名稱、48dp 觸控目標及非顏色狀態線索。 |
| AC-006 | 步驟 2、3、5、6 | 組態變更及程序重建後路由可用；核准的狀態還原，認證密碼／驗證碼排除，提醒與照片確認語意不變。 |
| AC-007 | 步驟 2–7 | Diff／manifest／dependency 審查確認沒有 API、DTO、長期表單儲存、新畫面或跨 Task ownership 變更。 |

## 失敗行為

- Saved state 缺欄、無法解碼或照片 URI 無效時，採用擁有 Task 定義的安全預設值；保留其他可用表單資料並顯示可理解的照片錯誤，不得靜默清空整份 dirty form 或偽報恢復成功。
- Camera 權限拒絕、CameraX 尚未可用或重新綁定失敗時，維持可重試狀態，不保存 CameraX 物件，不清除 UI-006 表單。
- 窄畫面或 IME 使內容高度不足時，允許捲動到達必要操作；不得縮小 48dp 觸控區。

## 安全與隱私

- UI-006 表單資料與 UI-007 照片 URI／中繼資料依使用者授權僅保存於 Android 暫存 saved state；不得加入資料庫、額外檔案、log、analytics 或 API。
- 登入／註冊密碼、確認密碼、驗證碼與 Token 不可寫入 `SavedStateHandle`、`rememberSaveable`、log 或截圖 fixture；程序死亡後依 UI-002 規則重設。
- 相機執行物件、權限 launcher、Context、View、bitmap 與 image bytes 不得放入 Bundle。Saved state 只存可重建的基本值與本機 URI 字串。
- 超出 saved-state 容量或相片檔案失效時不可截斷欄位或靜默清除資料；記錄可重現的限制並依 AC 回報結果。

## 風險

- 手動 `remember` 建立的 ViewModel 與測試直接建構方式需遷移至 lifecycle owner；需維持 fake data source 與 debug／release source-set 邊界。
- Android saved state 有容量限制，長備註或照片 URI 中繼資料過大可能無法還原；不可加入截斷行為，需以代表性最大輸入驗證。
- app-private cache 的照片檔案引用可能失效；失效時保留其他表單狀態與可辨識錯誤。
- API 28 裝置的 IME 與 inset 行為可能與新系統不同；兩版本皆需實機制式化結果。
- UI-002 hosted CI 目前為環境 `NOT VERIFIED`，不把該狀態改報 PASS；本 Task 仍要求本機完成受影響回歸。

## 回復計畫

- 若修改造成回歸，僅還原 UI-009 此次變更檔案至 Plan Review 核准 revision，保留現有 UI-008、UI-010 與其他工作目錄修改，再依 issue route 修正計畫。

## Deferred Obligations

| Gate | 狀態 | 原因 | 負責人 | 重新啟動里程碑 |
|---|---|---|---|---|
| Hosted CI | deferred | 目前為 pre-release development scope；本機驗證不代表 hosted PASS。 | release／project maintainer | release candidate 驗證前 |

## Open Questions

- 無；Figma 正式 revision／像素等同不在本 Task 的接受範圍。
