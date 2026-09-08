# Requirement

## Background
- Android 專案目前只有 Compose starter，正式 Auth API 尚未完成，後續 UI 畫面沒有可進入受保護區域的開發入口。
- 本 Task 先建立可替換的 session／app-root 基礎與 debug-only 直接登入，正式登入仍由 `UI-002-auth`／`INT-001-auth-api` 負責。
- 原 `KB-UI-001-FOUNDATION-R2` 已因 auth visual source 更換而失效；專案仍為從零建立的 Compose MVVM app，Login／Registration foundation 現以 current composite 為準。

## Goal
- 建立後續 UI Tasks 可直接依賴的 Compose foundation contract，以及不依賴 API、只存在於 debug build 的直接登入方式與 session／app-root 邊界。

## Functional Requirements
- `FR-UI001-001 Session contract`：以 immutable state 表達未登入及已登入；開發 session 只包含明顯 fake 的使用者名稱與角色，不包含 Token。
- `FR-UI001-002 Debug direct login`：debug build 顯示開發登入入口，允許選擇調查人員、內業人員或管理者後直接進入 authenticated content。
- `FR-UI001-003 Debug logout`：authenticated debug content 提供返回開發登入入口的動作，並清除記憶體內 fake session。
- `FR-UI001-004 Release isolation`：release build 不得顯示、啟用或自動建立 debug session；正式 auth 尚未接入時維持 unauthenticated host。
- `FR-UI001-005 Replaceable boundary`：UI-002 可替換 unauthenticated content，UI-003 可替換 authenticated content，INT-001 可替換 debug session provider，而不修改各 feature screen。
- `FR-UI001-006 Foundation contract`：依 `foundation-contract.md` 提供 semantic theme、共用 field／button／selection／status 元件、通用 presentation state、Preview／test scaffold 與 consumer handoff；Login／Register token 與 password/selection variants 必須追溯至 current composite，iconography 統一使用 Android／Material 預設 icon，不要求自有 icon asset 與 composite pixel matching。

## Non-functional Requirements
- debug 入口使用繁體中文，角色選擇及按鈕具可讀 semantics，觸控目標至少 48dp。
- fake session 僅存在記憶體，不寫入 SharedPreferences、DataStore、log、analytics 或備份。
- release source set 不依賴 debug implementation；debug bypass 不得被 production navigation 或 API code 呼叫。
- 支援 Android 9+；本 Task 應將 `minSdk` 與產品限制對齊為 28。

## Acceptance Criteria
- `AC-UI001-001`：debug build 啟動後顯示「開發模式」及三種角色選項，預設不處於 authenticated state。
- `AC-UI001-002`：選擇任一角色並點擊「直接進入」後，只建立一次對應 fake session 並顯示 authenticated placeholder 與 fake identity。
- `AC-UI001-003`：點擊 debug logout 後清除 session 並返回開發登入入口；重新啟動 process 亦回到未登入。
- `AC-UI001-004`：release build 可成功編譯，且 UI／可呼叫 entry point 不包含直接登入或 fake authenticated session。
- `AC-UI001-005`：App root 以 callbacks／state contract 接收 unauthenticated 與 authenticated content，後續 Tasks 可替換內容而不依賴 fake DTO。
- `AC-UI001-006`：unit／Compose UI tests 覆蓋三角色、單次登入、登出、記憶體 session 與 debug/release 邊界。
- `AC-UI001-007`：`minSdk` 為 28，debug build、release build及既有 tests 均未因 foundation 變更失敗。
- `AC-UI001-008`：UI-002 可只依 `ui.foundation.*` 與 `session` public contract 建立登入／註冊，不需複製 field、button、loading/error state 或 session model。
- `AC-UI001-009`：foundation components 具 default 與適用的 loading／disabled／error／readonly Preview，並通過 semantics、click suppression、Login masked＋eye、Register plaintext＋no-eye、radio zero-or-one/exclusivity、checkbox selectable-row 與最小觸控尺寸測試。
- `AC-UI001-010`：semantic color、typography、shape、spacing 均可追溯至 owning current source；Login／Register 記錄 composite SHA-256／panel crop／scale；password eye、reload、checkbox、back、dropdown、radio 等 affordance 使用 Android／Material 預設 icon／control，並以 semantics、狀態與 48dp 觸控目標測試驗收；feature code 不散落 raw visual values 或自有 icon asset。

## Constraints
- 不呼叫正式 Auth API、不產生或模擬 production Token、不保存帳號密碼。
- 正式視覺來源依 `docs/design/app-ui-requirements.md`：Login／Register 使用 SHA-256 `75cb578c…3d3e9c` 的 requester composite 作為 layout/state/token authority；UI-001 iconography 改用 Android／Material 預設 icon，不從 composite 或 Figma 匯出自有 icon。其他畫面使用 Figma Copy `HRbRsw6HoNBUCtaieX8xUM`。後續變更須重新 Knowledge Validation／Plan Review。
- debug authenticated content 僅是導覽插槽，不是假圖台，不得被當成 UI-003 完成證據。
- 本功能不得出現在 release build。
- `foundation-contract.md` 是 UI-001 public API 的 Planning source；破壞性變更需重新 Plan Review 並更新全部 consumers。

## Open Questions
- 無產品問題；但 UI-001 必須先重新驗證 current composite 對 token／password／selection component evidence 的影響，並確認 Android／Material 預設 icon policy 已反映於 plan、asset manifest 與 tests，不可使用舊 baseline 進入 Verification。
