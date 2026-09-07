# Requirement

## Background
- 現有 Android 專案僅有 Compose starter；Figma 已呈現登入空白／輸入／失敗、註冊空白／輸入，以及圖台側欄登出流程。
- 本 Task 依 `KB-UI-002-AUTH-R5` 與 requester 於 2026-09-07 核定的正式 Figma UI，建立可用 fake state 驗收的認證 UI，不等待尚未完成的 API schema。

## Goal
- 建立 Android 9+ 的登入與註冊 Compose 畫面、表單狀態與本機驗證，並提供登入成功、註冊成功及登出返回登入所需的可替換事件邊界。

## Functional Requirements
- `FR-UI002-001 登入內容`：登入頁顯示品牌區、帳號、預設遮罩密碼、4 碼數字驗證碼、驗證碼圖／重整、記住我、登入按鈕及註冊入口。
- `FR-UI002-002 登入狀態`：支援 empty、filled、submitting、field-error 與 request-error fake states；登入失敗顯示「帳號、密碼或驗證碼錯誤」，且不得清除帳號與記住我狀態。
- `FR-UI002-003 登入事件`：本機驗證通過才可送出；submitting 期間禁止重複觸發；fake 成功只發出一次 `LoginSucceeded` callback，供後續圖台導覽接線。
- `FR-UI002-004 註冊內容`：註冊頁依序顯示帳號、密碼、確認密碼、廠商名稱、作業性質、姓名，並提供返回／取消及完成；註冊成功返回登入頁，由使用者登入。
- `FR-UI002-005 註冊驗證`：六組欄位均必填；帳號限英數且最多 30 字元、密碼至少 8 碼、確認密碼一致、姓名須為中文；廠商使用可注入 fake options，作業性質提供外業／內業單選。
- `FR-UI002-006 跨 Task 認證入口`：UI-002 提供可由 App host 顯示登入頁的 contract；側欄登出列由 UI-003 實作並觸發 logout request，真正 Token 清除、API logout 與 401 導回由 INT-001 實作。

## Non-functional Requirements
- 支援 Android 9+、直向不同寬度、系統字級放大與 IME；表單可捲動且目前欄位與主要操作可觸及。
- 觸控目標至少 48dp；純圖示操作具繁體中文 content description；錯誤與 loading 不得只靠顏色表達。
- 密碼、確認密碼與驗證碼不得寫入 log、semantics label、screenshot fixture 名稱或持久化 state；Preview／測試使用明顯 fake 值。
- 視覺使用 UI-001 提供的品牌 token、typography、field/button component 與核定 asset；不得在 feature 內散落 raw color／dimension 或自行重畫 logo、驗證碼與城市剪影。

## Acceptance Criteria
- `AC-UI002-001`：未登入入口顯示帳號、遮罩密碼、4 碼數字驗證碼、驗證碼重整、記住我、登入及註冊入口，且各操作有可讀 semantics。
- `AC-UI002-002`：必填缺漏或驗證碼非 4 碼數字時不觸發送出並顯示對應欄位錯誤；submitting 時登入只能觸發一次。
- `AC-UI002-003`：fake 登入成功只呼叫一次 `LoginSucceeded`；fake 失敗維持登入畫面、保留帳號與記住我並顯示「帳號、密碼或驗證碼錯誤」。
- `AC-UI002-004`：由註冊入口可開啟含六組規定欄位的註冊頁；返回與取消均發出回登入事件且不發出註冊請求。
- `AC-UI002-005`：註冊缺漏、帳號非英數或超過 30 字元、密碼少於 8 碼、確認不一致或姓名非中文時不提交；合法資料只觸發一次 fake register request，成功後返回空白登入頁。
- `AC-UI002-006`：auth host contract 可在登出完成或 401 effect 後顯示乾淨登入狀態；UI-002 測試不要求側欄或 Token implementation。
- `AC-UI002-007`：在最小支援寬度、一般寬度、字級放大與 IME 顯示情境，表單可捲動、主要操作可觸及，且無功能性文字截斷。
- `AC-UI002-008`：Preview／UI tests 覆蓋登入 empty／filled／submitting／error 與註冊 empty／filled／validation-error／submitting states，且 fake data source 可被替換。

## Constraints
- 本 Task 類型為 `feature`，僅處理 Compose UI 與 fake state；不建立 Retrofit service、production DTO、Token storage、401 interceptor 或真實廠商代碼表。
- `UI-001-foundation` 是 Implementation prerequisite；UI-002 必須重用其 tokens、components 與 assets。
- 側欄與登出列外觀屬 `UI-003-map-shell`；Session 登出行為屬 `INT-001-auth-api`。
- Session Token 效期永久；實際儲存、撤銷與 401 行為由 `INT-001-auth-api` 實作及驗證。
- Parent requirement 的 AC-001–003 僅在本 Task 驗證 UI portion；AC-016 必須由 UI-002、UI-003 與 INT-001 合併驗證，不能在本 Task 宣告完整 PASS。

## Open Questions
- 無；正式 Figma visual baseline 已核定。API schema 與 UI-001 implementation prerequisite 依各自 Task 追蹤。
