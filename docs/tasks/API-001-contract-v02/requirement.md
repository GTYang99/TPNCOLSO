# API-001 Contract v0.2 Requirement

## Task Scope

建立並啟用 API v0.2 的 Knowledge baseline，並將後續 Android integration 拆成可分別規劃的工作包。API 合約以目前核定的 Postman Collection 為唯一權威來源；本 Task 不實作後端或 Android API client。

## In Scope

- `TP NCOLSO API v0.2` Postman Collection 的 12 支可用 API。
- Request path、method、參數、body、可見 response example、認證與錯誤規則。
- OQ-017、OQ-019～OQ-034 的已確認決策與 API 範圍排除。
- Collection 的來源版本、checksum、JSON 與 Postman lint 驗證。
- 對 INT-001～INT-004 提供獨立的候選 plan 邊界、依賴、啟動條件及缺口交接；尚無 API 合約的功能不得視為可實作。

## Out of Scope

- 後端實作、正式環境部署、真實 API request 執行與服務可用性保證。
- 尚未完成或暫不開發的 history、approve、reject、batch、export、import、dashboard API。
- WMTS 圖磚參數與非 Collection 內的 API。

## Acceptance Criteria

| ID | Observable acceptance criterion |
|---|---|
| AC-API001-001 | 專案內 Collection 為 `docs/api/postman/tp-ncolso-api-v0.2.postman_collection.json`，版本名稱為 `TP NCOLSO API v0.2`，並記錄 SHA-256。 |
| AC-API001-002 | Collection 僅包含 AUTH、MAP_AND_LAND、SURVEY_AND_PHOTO 三個資料夾，共 12 支 request；未把 PENDING API 或 CAPTCHA request 當作 v0.2 合約。 |
| AC-API001-003 | Resolution 明確記錄 Token lifecycle、role、日期時間、GeoJSON、分頁、filters、照片張數、PUT 修正送出與 409/422 規則。 |
| AC-API001-004 | 舊產品 API 規格與 `tp_req_api.md` 的衝突，均記錄為已由需求方決定以 Postman Collection 優先，不做未授權的靜默合併。 |
| AC-API001-005 | `jq empty` 與 Postman CLI lint 通過；Knowledge Validation 為 PASS，state 啟用 `KB-API-001-CONTRACT-V02-R1` 並 handoff 至 Planning。 |
| AC-API001-006 | 規劃交接列出 INT-001～INT-004 各自的 API 範圍、既有 UI 接點、依賴、啟動條件與驗證方向，並標明 Collection 未涵蓋的通知及歷史期別等功能仍需後續權威合約。 |

## Constraints and Dependencies

- Planning 只修改文件，不修改 production code，也不把候選 integration plan 當成已通過 Plan Review 的實作授權。
- 候選 plan 均依賴 `KB-API-001-CONTRACT-V02-R1`；正式建立 Integration Task 時須依當時有效 Collection 和 UI 狀態重新確認需求、AC、風險及測試環境。
- 不得自行補完缺少的 response/error schema、照片附件綁定、`site_condtion` 與 `site_condition` 的關係，或 Collection 外的 API。

## Risks and Open Decisions

- Group Options 與 Update Survey 缺少 response example；Upload Photo 缺少 409/422 example，且使用 Postman cloud attachment。這些缺口限制可執行的整合驗證。
- INT-004 的通知與已讀 API、INT-002 的歷史期別 API 不在此 12-request Collection 中；需要 API owner 提供新合約並重新進入 Knowledge 流程。
- 候選 plan 的順序和啟動條件由後續各 Integration Task 的 Plan Review 核定；本 Task 不決定未提供的產品或 API 行為。

## Material Decisions

- 登入不使用 CAPTCHA。
- `role` 必填，正式值為 `內業` 或 `外業`。
- Token 無時效限制；再次登入使舊 Token 立即失效；登出使目前 Token 失效。
- 修正送出仍使用一般 `PUT /api/survey/{surveyId}`，不使用 `resubmit`。
- 每個 `occupy` 項目至少 4 張照片；survey 頂層亦提供 4 張照片。
- Filters 可重複、可疊加；日期含 `YYYY-MM-DD HH:mm:ss`；`total`、`total_page` 為 Int。
