# Repository Analysis

## Current Behavior

- `KB-API-001-CONTRACT-V02-R2` 已通過 Knowledge Validation；權威 Collection 的 SHA-256 為 `0178ea57e953cc2094a03d546d2a78b44f7ddfa0984b015c66f624d9f781342f`，包含三個資料夾、12 支 request。
- UI-001～UI-010 已建立 Compose 畫面與 fake/local 資料接點；`docs/tasks/README.md` 已列出 INT-001～INT-004，API-001 目錄已有四個候選工作包的切分文件。
- `app` 是單一 module，架構要求 integration 實作依 feature-owned ports 接入；目前 API-001 只擁有文件和 Collection，沒有 production code 變更。
- Git 目前只有 `developer` 工作樹（HEAD `f16f7fa9194f6330f2df0b5f8ab796fc34e29a8a`）：Collection 與 API-001 目錄尚未追蹤，Task Index 有 API-001 改動，同時 `UI-008-alerts` 的兩份文件另有未提交改動。API-001 必須在獨立 task worktree 建立可審查的提交。

## Expected Behavior

- API-001 的文件規劃可追溯 AC-API001-001～006，並把四個 integration 工作包切開，供日後逐一建立 Requirement、Plan 和 Plan Review。
- 每個工作包明示 Collection 已定義的路徑，以及尚待 API owner 補充的 response、錯誤、權限和執行環境證據。
- Collection 外的歷史期別與通知流程維持未授權狀態，不能由 UI fake 資料推導 endpoint。

## Affected Modules

- 本 Task 的權威交付：`docs/api/postman/tp-ncolso-api-v0.2.postman_collection.json` 與 `docs/tasks/README.md` 中 API-001 的索引與候選切分連結。
- 本 Task 的證據及規劃：`docs/tasks/API-001-contract-v02/` 內的 Knowledge、requirement、analysis、plan、integration-plan-split、issue-log、state、review 與驗證證據；交付前依檔案清單逐一核對。
- 下游預計接點：`app/src/main/java/com/example/tp_ncolso_android/session/`、`feature/auth/`、`feature/mapshell/`、`feature/parcelsearch/`、`feature/parceldetail/`、`feature/surveyform/`、`feature/photocamera/`、`feature/notifications/`；此處只列規劃邊界，不授權修改。

## Dependencies

- 權威 API：`docs/api/postman/tp-ncolso-api-v0.2.postman_collection.json` 和 Knowledge Validation handoff。
- Git 交付：從執行時確認的 `developer` HEAD 建立 `codex/api-001-contract-v02` 獨立 worktree，只移入 API-001 檔案及 Task Index 的 API-001 hunk；原工作樹中的 UI-008 改動保持原狀。
- 下游 Task 必須遵守 `docs/architecture/overview.md` 的 feature-owned port、session、生命週期及敏感資料限制。
- 真實整合驗證需要可用測試環境、測試帳號、完整 schema 和可重現的上傳檔案綁定；不得將憑證寫入 Task artifact。

## Risks

- Collection 範例無法證明未顯示的欄位、nullability 或完整錯誤格式；過早定義 DTO 可能固化錯誤契約。
- `site_condtion` 是 options 路徑值，`site_condition` 是 survey payload 欄位；若自行正規化，可能打破 wire contract。
- INT-004 需要的通知/已讀 endpoint 全部缺席；INT-002 的歷史期別亦缺席。這些部分須重新 Collection/Resolution，不得列為 v0.2 已準備實作。
- 若直接在混合工作樹切分支、整批 stage 或複製整份 Task Index，可能夾帶 UI-008 或同時期其他變更；須在獨立 worktree 檢查來源 hash、變更清單和 staged diff。

## Unknowns

- Group Options、Update Survey 的 response，Upload Photo 的 409/422 response、正式 GeoJSON/error schema 與照片本機附件綁定方式。
- 正式 HTTPS base URL、可用測試環境、完整 401/權限矩陣，以及未列入 Collection 的通知和歷史期別合約。
