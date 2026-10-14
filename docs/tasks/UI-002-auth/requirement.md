# Requirement

## Active Change — R11 (2026-10-14)
- Requester decisions `KD-AUTH-013` and `KD-AUTH-014`: remove the Login CAPTCHA and apply the approved R11 lower-control reflow. These supersede CAPTCHA-related R10 clauses and changed-region y-anchors only; unaffected R10 behavior remains active.
- Login has no CAPTCHA input, image, reload control, CAPTCHA field validation, or CAPTCHA-specific state/error copy. Fake Login submits account/password only; auth failure copy is `帳號或密碼錯誤`. Keep brand/account/password positions; remove the CAPTCHA row and adjacent spacing; move Remember me, registration entry, and Login button upward using UI-001 spacing. R10 pixel checks apply only to unchanged regions, while the R11 capture validates the reflowed controls.
- This is a material requirement change. Plan Review iteration 19 approved plan revision 18; implementation is underway and local validation is recorded in the execution report.

## Background
- 現有 Android 專案已有 UI-001 Compose foundation、AppRoot 與 debug direct-login；Figma 已呈現登入空白／輸入／失敗、註冊空白／輸入，以及圖台側欄登出流程。
- 本 Task 保留 `KB-UI-002-AUTH-R10` 中未受影響的視覺決策；Login CAPTCHA 相關內容依 2026-10-14 requester 決策移除，Login visual acceptance 使用 R11 requirement。

## Goal
- 建立 Android 9+ 的登入與註冊 Compose 畫面、表單狀態與本機驗證，並提供登入成功、註冊成功及登出返回登入所需的可替換事件邊界。
- 登入畫面的正式 composition、state 與 visual acceptance 以 `login-ui-requirement.md` 為準，不得以 generic Material login form 取代。
- 註冊畫面的正式 composition、state 與 visual acceptance 以 `registration-ui-requirement.md` 為準，不得以 generic Material registration form 取代。
- 登出後返回 Login 的責任與 UI-003／INT-001 邊界以 `logout-interface-requirement.md` 為準；UI-002 不實作側欄。

## Functional Requirements
- `FR-UI002-001 登入內容`：登入頁顯示品牌區、帳號、預設遮罩密碼、記住我、登入按鈕及註冊入口；不得顯示驗證碼。
- `FR-UI002-002 登入狀態`：支援 empty、filled、submitting、field-error 與 request-error fake states；登入失敗顯示「帳號或密碼錯誤」，且不得清除帳號與記住我狀態。
- `FR-UI002-003 登入事件`：本機驗證通過才可送出；submitting 期間禁止重複觸發；fake 成功只發出一次 `LoginSucceeded` callback，供後續圖台導覽接線。
- `FR-UI002-004 註冊內容`：註冊頁依序顯示帳號、明文密碼、明文確認密碼、廠商名稱、作業性質、姓名，並提供返回／取消及完成；註冊成功返回登入頁，由使用者登入。
- `FR-UI002-005 註冊驗證`：六組欄位均必填；帳號限英數且最多 30 字元、密碼至少 8 碼、確認密碼一致、姓名須為中文；廠商使用可注入 fake options，作業性質初始不預選且完成時必須外業／內業擇一。
- `FR-UI002-006 跨 Task 認證入口`：UI-003 擁有側欄並發出 `LogoutRequested`；UI-002 提供清除認證表單並呈現空白 Login 的 contract；INT-001 清除 production Token／處理 remote logout，但不得阻擋本機立即登出。
- `FR-UI002-007 登入視覺契約`：登入頁須符合 `login-ui-requirement.md` revision 4；R10 composite 僅作未變更視覺區域參照，不定義 CAPTCHA 移除後的新垂直錨點。
- `FR-UI002-008 註冊視覺契約`：註冊頁必須符合 `registration-ui-requirement.md` revision 2 與 composite 的 402×874 empty／filled panels；empty 作業性質不預選，filled 密碼為明文。
- `FR-UI002-009 登出契約`：點擊側欄「登出」後不顯示確認視窗、不等待遠端回應，立即清除本機 session 與 auth state 並返回空白 Login；側欄視覺不屬 UI-002。

## Non-functional Requirements
- 支援 Android 9+、直向不同寬度、系統字級放大與 IME；表單可捲動且目前欄位與主要操作可觸及。
- 觸控目標至少 48dp；純圖示操作具繁體中文 content description；錯誤與 loading 不得只靠顏色表達。
- Login 密碼依 composite 遮罩；Register 密碼／確認密碼依 composite 明文顯示。密碼不得寫入 log、analytics 或持久化 state；Preview／測試只使用 fake fixtures。Login 不再有驗證碼欄位。
- 視覺使用 UI-001 提供的品牌 token、typography、field/button component 與核定 asset；不得在 feature 內散落 raw color／dimension 或自行重畫 logo 與城市剪影。Login 不使用驗證碼圖像或控制項。

## Acceptance Criteria
- `AC-UI002-001`：未登入入口顯示帳號、遮罩密碼、記住我、登入及註冊入口，不顯示任何驗證碼 UI，且各操作有可讀 semantics。
- `AC-UI002-002`：帳號或密碼缺漏時不觸發送出並顯示對應欄位錯誤；submitting 時登入只能觸發一次；不存在驗證碼格式驗證。
- `AC-UI002-003`：fake 登入成功只呼叫一次 `LoginSucceeded`；fake 失敗維持登入畫面、保留帳號與記住我並顯示「帳號或密碼錯誤」。
- `AC-UI002-004`：由註冊入口可開啟含六組規定欄位的註冊頁；返回與取消均發出回登入事件且不發出註冊請求。
- `AC-UI002-005`：註冊缺漏、作業性質未選、帳號非英數或超過 30 字元、密碼少於 8 碼、確認不一致或姓名非中文時不提交；合法資料只觸發一次 fake register request，成功後返回空白登入頁。
- `AC-UI002-006`：auth host contract 可在 `LogoutRequested` 後立即清除本機 session／auth state 並顯示空白 Login，無確認視窗且不等待 remote response；UI-002 測試不實作側欄。
- `AC-UI002-007`：在最小支援寬度、一般寬度、字級放大與 IME 顯示情境，表單可捲動、主要操作可觸及，且無功能性文字截斷。
- `AC-UI002-008`：Preview／UI tests 覆蓋登入 empty／filled／submitting／error 與註冊 empty／filled／validation-error／submitting states，且 fake data source 可被替換。
- `AC-UI002-009`：`login-ui-requirement.md` 的 `UIR-LOGIN-001–010` 全部有 implementation mapping 與 executed visual／Compose／accessibility evidence；任一項 `NOT VERIFIED` 時不得宣告登入 UI PASS。
- `AC-UI002-010`：`registration-ui-requirement.md` 的 `UIR-REG-001–011` 全部有 implementation mapping 與 executed visual／Compose／accessibility evidence；任一項 `NOT VERIFIED` 時不得宣告註冊 UI PASS。
- `AC-UI002-011`：`logout-interface-requirement.md` 的 `UIR-LOGOUT-001–006` 全部有 contract／state／idempotency 測試證據；側欄視覺只能在 UI-003 驗收。

## Constraints
- 本 Task 類型為 `feature`，僅處理 Compose UI 與 fake state；不建立 Retrofit service、production DTO、Token storage、401 interceptor 或真實廠商代碼表。
- `UI-001-foundation` 是 Implementation prerequisite；UI-002 必須重用其 tokens、components 與 assets。
- Login／Register visual verification 與 Logout interface context 只使用 SHA-256 `75cb578c58f3098c270290f033539ebf06c35c8b322efb71526fa4e53f3d3e9c` 的 composite PNG；所有先前 screenshots 與 Figma 只能作為已通過新 source 比對的 candidate asset，不得作為 auth visual acceptance source。
- UI-002 只在 debug／test source set 接入 fake AuthHost；debug 啟動預設顯示正式 Login，direct-login 由畫面外層的 debug-only wrapper 保留。Release `AppEntry` 在 `INT-001-auth-api` 提供 production source 前維持 unauthenticated placeholder，不得引用 fake source 或 bypass。
- 側欄與登出列外觀屬 `UI-003-map-shell`；UI-002 擁有立即清除本機 session/auth state 並回 Login 的 destination contract；production Token／remote logout 屬 `INT-001-auth-api`。
- Session Token 效期永久；實際儲存、撤銷與 401 行為由 `INT-001-auth-api` 實作及驗證。
- Parent requirement 的 AC-001–003 僅在本 Task 驗證 UI portion；AC-016 必須由 UI-002、UI-003 與 INT-001 合併驗證，不能在本 Task 宣告完整 PASS。

## Open Questions
- 無；requester 已確認 composite 唯一權威、外層 chrome 排除、Register 不預選／明文密碼、側欄 ownership 與立即登出行為。
