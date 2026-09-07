# Knowledge Resolution

## Question and Scope
- Task question: 如何將附件中的七個 Figma 流程與現有產品、資料、行為、介面及 API 規格整理為 AI 可執行的 Android UI requirement 架構？
- In scope: Android 登入／註冊／登出、圖台與底圖、土地查詢、土地詳情、調查填報、照片、退回修正、Android 提醒、錯誤與權限／狀態顯示。
- Out of scope: WEB 介面、iOS 視覺實作、後端 schema 補完、實際 Android 程式、測試執行、部署。

## Knowledge Baseline
- Baseline ID: `KB-UI-2026-09-07-01`
- Created date: 2026-09-07 (Asia/Taipei)
- Supersedes: None
- Status: draft

## Source Register

| Source ID | Title / Location | Type | Domain / Owner | Version / Date | Authority | Freshness | Scope / AC | Notes |
|---|---|---|---|---|---|---|---|---|
| SRC-001 | Current user request | authorized request | Product / requester | 2026-09-07 | authoritative | current | Task goal / all | Authorizes reading links and creating requirements; does not resolve existing product conflicts. |
| SRC-002 | `/Users/a10362/Desktop/tp_req_ui.md` | UI source index | Design / unknown | modified 2026-09-07 09:39 +0800 | supporting | current | All UI flows | Lists seven node-specific Figma URLs; contains no detailed behavior itself. |
| SRC-003 | Figma file `HRbRsw6HoNBUCtaieX8xUM`, nodes `2905:2680`, `4952:18205`, `2938:193`, `3327:13251`, `4952:14932`, `3184:11543`, `3225:12771` | design source | Design / unknown | retrieved 2026-09-07 | supporting | unknown | AC-001–AC-017 | Metadata, screenshots and representative design context were read; file revision／approval owner unavailable. |
| SRC-004 | `系統規格說明書.md` | system specification | Product / 于詩昱 | v1, 2026-08-25 | authoritative | current | Scope | Defines APP/WEB purpose and field-survey scope. |
| SRC-005 | `1 系統概述與角色權限.md` | product and permission specification | Product / unknown | modified 2026-09-02 | authoritative | current | AC-004, AC-008, AC-012, AC-013, AC-016 | Defines feature list, APP sitemap and role matrix. |
| SRC-006 | `2 資料規格.md` | data and field specification | Data / unknown | modified 2026-09-02 | authoritative | current | AC-003, AC-006–AC-010, AC-012 | Defines identifiers, display formats, form fields and photo rules. |
| SRC-007 | `3 系統行為規則.md` | behavior specification | Product / unknown | modified 2026-09-02 | authoritative | current | AC-001, AC-002, AC-009, AC-011, AC-013, AC-016 | Defines state transitions, validation and error behavior; contains token conflict. |
| SRC-008 | `4 介面規格.md` | interface specification | Design / unknown | modified 2026-09-02 | authoritative | current | AC-004–AC-010, AC-012 | Figma URL is marked pending; UI structure is defined. |
| SRC-009 | `5 API規格.md` | API contract summary | API / unknown | modified 2026-09-02 | authoritative | current | AC-002, AC-006, AC-008–AC-013, AC-016 | Detailed request/response schemas and notification API are missing. |
| SRC-010 | `docs/architecture/overview.md` and current Android source tree | repository architecture / implementation | Architecture / repository | working copy 2026-09-07 | authoritative | current | Planning impact | Current repository is a Compose starter and does not match the older full-module architecture description. |

## Material Claims and Traceability

| Claim ID | Statement | Supporting Sources | Contradicting Sources | Authority / Confidence | Requirement / AC | Status |
|---|---|---|---|---|---|---|
| KCL-001 | APP 為 Android 現場勘查與填報端，登入後預設圖台。 | SRC-004, SRC-005, SRC-003 | None | high | FR-003 / AC-004 | resolved |
| KCL-002 | 登入包含帳號、密碼、4 碼驗證碼，並可重整與註冊。 | SRC-006, SRC-007, SRC-003 | None | high | FR-001 / AC-001 | resolved |
| KCL-003 | 圖台提供電子地圖、正射影像圖、地形圖，電子地圖預設。 | SRC-008, SRC-003 | None | high | FR-004 / AC-005 | resolved |
| KCL-004 | 關鍵字搜尋涵蓋 land_no、land_location、key_no。 | SRC-006 | SRC-003 僅顯示「地號」提示 | authoritative / high | FR-005 / AC-006 | resolved: placeholder 不縮減搜尋能力 |
| KCL-005 | 土地詳情有兩頁籤，清冊／圖資唯讀，現況欄位依狀態編輯。 | SRC-006, SRC-008, SRC-003 | None | high | FR-006–FR-008 / AC-007, AC-008 | resolved |
| KCL-006 | 現況照片僅可現場拍攝、固定 4 張、4:3 並可刪除重拍。 | SRC-005, SRC-006, SRC-008, SRC-009, SRC-003 | None | high | FR-010 / AC-010, AC-015 | resolved |
| KCL-007 | 調查狀態共有未調查、外業調查完成、退回、已修正、已查核五種。 | SRC-007, SRC-003 | SRC-006 §2.3 漏列「已修正」 | authoritative / high | State model / AC-011–AC-013 | resolved by specific state-machine evidence |
| KCL-008 | Token 效期永久。 | SRC-001, SRC-005, SRC-007, SRC-009 | None after requester decision | authoritative / high | FR-016 / AC-016 | resolved |
| KCL-009 | APP 提供退回通知清單並可由通知進入修正。 | SRC-003 | SRC-005, SRC-008, SRC-009 omit notification contract | supporting / medium | FR-012 / AC-012 | blocked |
| KCL-010 | Android 應採 Android 確認對話框處理刪除、捨棄與確認修正。 | SRC-003, SRC-010 | None | high | FR-013–FR-015 / AC-013–AC-015 | resolved |

## Conflicts and Gaps

| Conflict ID | Claims / Sources | Domain | Impact / Severity | Options | Decision Owner | Status |
|---|---|---|---|---|---|---|
| KCF-001 | 原 SRC-005／SRC-009：半小時滑動；原 SRC-007：永久 | Security / API / session | P1；影響逾期、401、儲存與測試 | 採永久 | Requester | resolved 2026-09-07 |
| KCF-002 | 原 SRC-007：「帳號或密碼錯誤」；SRC-003：「帳號、密碼或驗證碼錯誤」 | UI copy / validation | P2；影響錯誤分類與 AC-002 精確文案 | 採「帳號、密碼或驗證碼錯誤」 | Requester | resolved 2026-09-07 |
| KCF-003 | SRC-005 表示 APP 有既有資料編輯；SRC-007 只明確描述退回修正；SRC-009 PUT 允許所有非未調查狀態且依角色處理；SRC-003 顯示外業調查完成可編輯 | Permission / state | P1；可能造成未授權修改或功能缺漏 | 依角色×狀態明列 APP edit matrix | Product + API/security owner | blocked |
| KCF-004 | SRC-003 有退回通知中心；SRC-005 Sitemap、SRC-008 面板、SRC-009 API 均未定義通知資料、已讀與失敗 | Product / API / UI | P1；無法完成可連線實作與驗證 | A 新增通知 API；B 由 parcel list 篩選退回；C 通知移出本期 | Product + API owner | blocked |
| KCF-005 | SRC-003 的地形圖標記為「圖片亂放的」 | Design asset | P2；不可用設計截圖當底圖驗收 | 以 SRC-008 WMTS 服務為資料來源，Figma 僅驗證選取狀態 | Design + Map owner | resolved |
| KCF-006 | 工作區無 `.git` 與 Android production source tree，僅有規格文件 | Environment | P1；無法分析既有元件、建立分支、提交、執行 CI 或獨立驗證 | 提供完整 Git checkout 後重進 Planning | Infrastructure | open |

## Decision Records

### KD-001

- Decision: Figma 僅作為畫面結構、視覺、元件與交互證據；不得覆寫產品、資料、權限或 API 合約。
- Alternatives: 直接以 Figma 推論所有產品行為；完全忽略 Figma 未出現在文字規格的流程。
- Source / Conflict IDs: SRC-001–SRC-009, KCF-002–KCF-005
- Rationale: 符合 domain authority，並保留設計新增流程與正式合約之差異。
- Approver and date: Knowledge Resolution evidence rule, 2026-09-07；非產品決策。
- Affected requirements / AC / artifacts: 全部；`docs/design/app-ui-requirements.md`。
- Supersedes: None
- Revalidation trigger: Figma 被核定或任何正式產品／API 規格更新。

### KD-002

- Decision: 本任務只使用 Android 提醒畫面作為樣式驗收，Figma 的 iOS 畫面不納入 Android 實作。
- Alternatives: 同時建立 iOS requirement。
- Source / Conflict IDs: SRC-001, SRC-003, SRC-010
- Rationale: 專案目標明確為 Android Kotlin，排除 iOS 不改變產品行為。
- Approver and date: Current task scope, 2026-09-07。
- Affected requirements / AC / artifacts: FR-013–FR-015, AC-013–AC-015。
- Supersedes: None
- Revalidation trigger: 產品新增 iOS target。

### KD-003

- Decision: UI 狀態模型使用五種狀態，包含「已修正」。
- Alternatives: 依 SRC-006 §2.3 的四值顯示清單移除「已修正」。
- Source / Conflict IDs: KCL-007, SRC-007, SRC-003
- Rationale: 專門的狀態機與 Figma 均明確使用「已修正」，§2.3 為摘要漏列。
- Approver and date: Evidence resolution, 2026-09-07；若資料庫 enum 不符須重新核定。
- Affected requirements / AC / artifacts: FR-013, AC-013, state and permission model。
- Supersedes: None
- Revalidation trigger: API schema 或資料庫 enum 與五值不符。

### KD-004

- Decision: Token 效期永久；登入失敗文案為「帳號、密碼或驗證碼錯誤」；註冊成功返回登入頁；帳號僅限英數且最多 30 字元。
- Alternatives: 半小時滑動 Token；不含驗證碼的錯誤文案；註冊後自動登入；帳號不限長度。
- Source / Conflict IDs: SRC-001, KCF-001, KCF-002
- Rationale: requester 於 2026-09-07 逐項核定。
- Approver and date: Requester, 2026-09-07。
- Affected requirements / AC / artifacts: FR-001, FR-002, FR-016；AC-002, AC-003, AC-016；產品規格 chapters 1, 2, 3, 5。
- Supersedes: KCF-001、KCF-002 先前互斥 claims 與 KA-003。
- Revalidation trigger: requester 或 API/security owner 核定新的 auth contract。

## Assumptions

| Assumption ID | Statement / Evidence | Confidence | Impact if Wrong | Owner | Validation Method | Expiry / Trigger | Safe for Planning |
|---|---|---|---|---|---|---|---|
| KA-001 | 402×874 是設計基準畫布而非固定裝置尺寸；Android UI 應自適應。來源 SRC-003、SRC-010。 | high | 版面需重新調整，不改產品規則 | Android developer | 在 Android 9+ 多尺寸與字級執行 layout test | 實作取得目標裝置矩陣時 | yes |
| KA-002 | Figma 的短暫成功標示可實作為符合現有 Android 架構的 toast/snackbar，而非要求特定 framework widget。 | medium | 視覺元件需替換 | Design owner | Plan Review 對照現有元件與 screenshot | 元件盤點完成時 | yes |

## Resolved Items
- Android UI 範圍、七組 Figma 節點與主要畫面已建立索引。
- 登入、註冊、三種底圖、土地詳情兩頁籤、調查欄位、條件驗證、四張照片、退回修正及 Android 對話框已映射至 requirement 與 AC。
- 認證四項產品決策已由 requester 核定並同步至共享產品規格。
- 「已修正」已納入五狀態 UI 模型；地形圖使用正式 WMTS 而非暫放設計圖片。

## Unresolved Non-blocking Items
- 動畫、轉場時間、toast 顯示時間與離線快取策略未由現有來源定義；不得在 requirement 中升格為固定產品行為。
- Figma Copy 目前為開發版；正式視覺驗收等待核定 revision。

## Blocking Items
- KCF-003 APP 既有資料編輯權限／狀態矩陣不一致。
- KCF-004 退回通知缺少正式產品與 API 合約。
- KCF-006 缺少 Git metadata，無法建立 task branch、提交或執行 committed-revision Verification。

## Downstream Impact
- Requirements / AC: AC-002、AC-016 的認證決策已更新；AC-012、AC-013 仍受 KCF-003／004 影響。
- Plan / design: Planning 必須先盤點實際 Android 導覽、theme、共用表單、地圖、相機、對話框與狀態保存方式。
- Code / data / API: 通知 API、Token lifecycle、PUT survey 權限與狀態條件需要合約化；不得只靠 UI 判斷授權。
- Tests / verification: 需建立角色×狀態矩陣、錯誤碼、旋轉／背景恢復、IME、字級、Android 9+、重複送出及照片權限測試。
- Release / operations: 尚未進入 release；Git、CI 與可驗證 artifact 均不可用。

## Revalidation Triggers
- 任一 Figma node、系統規格、API contract、角色權限或狀態 enum 更新。
- 提供 Android repository checkout 後發現現有元件、導航或資料模型與本 baseline 不符。
- 通知、照片上傳、相機權限取得新決策，或認證決策再次變更。
- Figma file 確認為已核定版本或被新設計檔 supersede。

## Planning Handoff
- Active baseline: 無；`KB-UI-2026-09-07-01` 為 draft，尚未 active。
- Decisions and constraints Planning must cite: KD-001、KD-002、KD-003；Android 9+、key_no 主鍵、四張 4:3 現場照片、兩頁籤、五狀態流轉。
- Safe assumptions: KA-001–KA-003，僅限其列出的範圍。
- Required follow-up evidence: KCF-003、KCF-004 的權責決策；核定 Figma revision；Git metadata、tests、build 設定與 CI 定義。
- Ready for Planning: no
