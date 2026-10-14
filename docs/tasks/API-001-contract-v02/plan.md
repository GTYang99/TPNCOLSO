# Implementation Plan

## Goal

- 將已驗證的 API v0.2 Knowledge baseline 整理成可審查的文件交接，並提出四個分別啟動的 integration plan 候選。

## Scope

- 交付權威 Postman Collection、Task Index 的 API-001 索引，以及 API-001 的 Knowledge、需求、分析、plan、缺口、候選 INT 切分、review 和狀態證據；不執行 API integration 或後端工作。

## Affected Files

- `docs/api/postman/tp-ncolso-api-v0.2.postman_collection.json`：權威 12-request Collection，需與 Knowledge evidence 所記 SHA-256 相同。
- `docs/tasks/README.md`：只交付 API Knowledge Baseline 區塊及四個候選 INT plan 的索引連結；不得帶入其他任務 hunk。
- `docs/tasks/API-001-contract-v02/knowledge-collection.md`、`knowledge-resolution.md`、`knowledge-validation.md`、`evidence/api-v02-validation.txt`：baseline 來源、決策及驗證證據。
- `docs/tasks/API-001-contract-v02/requirement.md`、`analysis.md`、`plan.md`、`integration-plan-split.md`、`issue-log.md`、`state.yaml`：文件任務的需求、拆分、缺口及狀態。
- `docs/tasks/API-001-contract-v02/plan-review.md`：只保留最新審查；修訂前審查移至 `history/<review-revision>/`，一併交付可追溯紀錄。實作與驗證階段新增的報告也只限本 Task 目錄。

## Delivery Isolation and Git Strategy

- 規劃時的來源是 `developer` HEAD `f16f7fa9194f6330f2df0b5f8ab796fc34e29a8a`，且原工作樹含未提交的 UI-008 文件；執行前重查 HEAD、`git status --short --branch`、`git worktree list --porcelain`、Collection checksum 和 `git diff -- docs/tasks/README.md`。
- Plan Review 核准後，從核對過的 `developer` HEAD 建立獨立的 `codex/api-001-contract-v02` worktree（例如 `/private/tmp/TP_NCOLSO-API-001`）；若 branch 或路徑已存在，先檢查並重用合適 worktree，不覆寫。不要在混合的 `developer` 工作樹切換分支或批次 stage。
- 將上述 API-001 檔案逐一移入獨立 worktree；Task Index 只套用已審查的 API-001 hunk，不複製可能含其他人新變更的整份檔案。移入後比對 Collection checksum、Task 檔案清單與 Index diff；若來源改變，停止並回到 Planning/Knowledge 對照。
- 僅以明列的 API-001 路徑 stage；檢查 `git diff --cached --name-status`、`git diff --cached --check` 和完整 staged diff，確認 Collection、Index、Task 證據均在內，且沒有 `UI-008-alerts` 或其他無關檔案。驗證後才做專注的 API-001 commit，記錄 branch 和完整 commit SHA；Code Review 與 Verification 使用同一 committed revision。

## Implementation Steps

1. 以 Collection checksum、12-request 清單和 Knowledge Validation handoff 固定規劃依據，核對 Collection 與 Task Index 均列入交付。
2. 補齊 API-001 的需求、依賴、風險和 repo/Git 接點分析，不更動 v0.2 API 決策。
3. 將 INT-001～INT-004 分成各自的候選 plan，分開定義 schema/權限、缺席 endpoint 和 runtime contract-test 門檻。
4. 記錄缺失 schema、照片附件、wire name、通知/歷史 API 與環境依賴的 owner/route，重新送交 API-001 Plan Review；此審查不核准任何 INT implementation。
5. API-001 Plan Review 核准後，依「Delivery Isolation and Git Strategy」建立 task worktree、移入精確檔案清單，驗證 Collection、Index 與 Task 證據。
6. 僅 stage API-001 交付檔案，檢查 staged diff，提交並記錄 revision；後續 Code Review/Verification 對同一 commit 執行。

## Test Plan

- 執行 `jq empty`、`shasum -a 256` 與精確的 12-request inventory 檢查，確認規劃仍引用同一份 Collection。
- 在獨立 worktree 核對 Index 連結可到達、Task 檔案清單完整、YAML 可解析，並驗證 staged diff 只含 API-001 交付；記錄當次 HEAD/branch/commit 與命令結果。
- 檢查 AC-API001-001～006 在 Collection、Knowledge artifact、`integration-plan-split.md` 中有對應證據。
- 文件任務不觸及 production code；本階段不需要 Android build。Postman lint 的既有結果須在最終 Verification 對同一 artifact 重驗。

## Regression Plan

- 確認沒有引入 CAPTCHA、`resubmit`、舊 `/parcel`/`/code` 路徑或排除中的 PENDING API。
- 確認 INT-004 的通知 API 和 INT-002 的歷史期別 API 未被寫成已存在；UI-first Task 順序與既有 fake 接點保持原狀。
- 確認原 `developer` 工作樹的 UI-008 變更仍未被 API-001 stage、commit、覆寫或移動。

## Risks

- 缺少 response/error schema 及實際服務測試會限制個別 integration plan 進入實作的時點。
- Plan split 是邊界提案，不取代各 Task 的 requirement approval、Plan Review 或後續 Knowledge revalidation。
- 原工作樹混有 UI-008 改動；若 task worktree 無法安全建立或移入檔案後 diff 不符合 allowlist，停止提交並依 Git/Infrastructure 規則處理。

## Rollback Plan

- 若新的權威 Collection 或 requester 決策推翻此切分，保留舊版於 task history，更新 Knowledge/Requirement，再重作規劃及 Plan Review；若只是 task worktree 移入失敗，保留原 `developer` 工作樹不變，修正隔離工作樹後重驗，無需 reset 或 force 操作。

## Current Behavior

- Knowledge 已 Ready，四個候選 INT 工作包與 Task Index 連結已存在；Plan Review 第 1 輪要求補足 Collection/Index 交付及 Git 隔離，並修正 state 的 schema/test 門檻。原 `developer` 工作樹尚有 API-001 與無關的 UI-008 未提交變更。

## Expected Behavior

- API-001 的規劃明確交接四個候選 plan、正確的前置 gate，以及可隔離提交的完整文件範圍；後續各 INT 仍需獨立 Knowledge、Requirement 和 Plan Review。

## Acceptance Criteria Traceability

| AC | Implementation Step | Validation |
|---|---|---|
| AC-API001-001 | 1、5、6 | Collection 路徑、名稱、SHA-256 與 committed staged file 對照 Knowledge evidence |
| AC-API001-002 | 1、3、5 | 12-request inventory、排除清單與交付檔案 |
| AC-API001-003 | 2、3 | Resolution KD-API001-002～004 與 plan split 對照 |
| AC-API001-004 | 2 | Resolution KD-API001-001 與 legacy conflict 記錄 |
| AC-API001-005 | 1、5、6 | `jq`/Postman lint evidence、Validation PASS、state baseline ID 與提交 revision |
| AC-API001-006 | 3、4、5 | `integration-plan-split.md` 四個工作包、分離 gate 與 Task Index 連結 |

## Failure Behavior

- 若 checksum 或 request inventory 改變，停止依此 baseline 規劃並路由 `knowledge_collection`；若新舊權威決策衝突，路由 `knowledge_resolution`；若檔案隔離或 staged diff 無法證明安全，停止提交並路由 `infrastructure`。

## Security and Privacy

- 不儲存 Token、測試帳號密碼、真實 payload 或照片。後續 INT-001 須處理 Token 保管、登出與重登失效；INT-003 須處理照片檔案生命週期。

## Open Questions

- 第 1 輪 Plan Review 的 API001-ISSUE-007/008 待重新審查本修訂；各候選 INT plan 的 source/環境缺口在 `issue-log.md` 與 `integration-plan-split.md` 追蹤。
