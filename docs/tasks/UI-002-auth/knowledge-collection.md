# Knowledge Collection

## Question and Scope
- Task question: 如何在不推測 API／Token 契約的前提下，將登入、註冊與登出入口設計拆成可由 fake state 驗收的 Android Compose UI Task？
- In scope: 登入與註冊畫面、欄位與本機驗證、loading／error 狀態、認證成功與登出事件邊界、與圖台側欄的責任切分。
- Out of scope: 正式 API DTO／endpoint 串接、Token 儲存與刷新、401 處理、圖台與側欄的 Compose 實作、發佈。
- Collection date: 2026-09-07 (Asia/Taipei)
- Collector: Codex Planning

## Source Coverage

| Category | Required | Result | Notes |
|---|---|---|---|
| Authorized decisions / requirement | yes | found | 使用者要求 Planning，並於 2026-09-07 核定永久 Token、錯誤文案、註冊導覽與帳號長度。 |
| Product rules | yes | found | 角色、資料、行為與介面規格已檢視。 |
| Design / assets / accessibility | yes | found | 指定 Figma Copy node 與登入失敗、註冊填寫、側欄登出子節點均已取得 structured design context、screenshots、tokens 與 asset URLs。 |
| API / schema / authentication | yes | found | endpoint 摘要存在；Token 已核定為永久，詳細 request／response schema 仍不完整。 |
| Architecture / repository | yes | found | Compose starter、theme 與 Gradle 設定已檢視。 |
| Tests / logs / runtime evidence | yes | found | 僅有 template unit／instrumented tests，無產品行為 evidence。 |
| Build / release / operations | no | N/A | 本次僅 Planning，不修改 production code。 |
| External primary sources | no | N/A | 無需外部平台規格即可完成 UI-only Planning。 |

## Source Register

| Source ID | Title / Location | Type | Domain / Owner | Version / Date | Authority | Freshness | Scope / AC | Access | Notes |
|---|---|---|---|---|---|---|---|---|---|
| SRC-001 | Current user request, Figma URL, and auth decisions | authorized decision | Product / requester | 2026-09-07 | authoritative | current | task scope | available | 核定永久 Token、錯誤文案、註冊返回登入與帳號最多 30 字元。 |
| SRC-002 | Figma Copy file `HRbRsw6HoNBUCtaieX8xUM`, node `2905:2680`；子節點 `4922:15370`, `4922:17493`, `2926:282` | approved design source | Design / requester | retrieved and approved 2026-09-07 | authoritative | current | AC-UI002-001–008 | available | Structured context 已取得；requester 核定目前內容即正式 UI。 |
| SRC-003 | `docs/design/app-ui-requirements.md` | design specification | Design / requester | working tree 2026-09-07 | authoritative visual baseline | current | parent AC-001–004, AC-016 | available | 包含 screen、component、state、responsive inventory 與核定 source index。 |
| SRC-004 | `docs/product/land-survey-115/1 系統概述與角色權限.md` | product specification | Product / unknown | working tree 2026-09-07 | authoritative | current | auth roles, navigation | available | 定義可自行註冊角色、單裝置登入與 APP sitemap。 |
| SRC-005 | `docs/product/land-survey-115/2 資料規格.md` | data specification | Data / unknown | working tree 2026-09-07 | authoritative | current | registration fields | available | 定義帳號、密碼、廠商、作業性質、姓名。 |
| SRC-006 | `docs/product/land-survey-115/3 系統行為規則.md` | behavior specification | Product / unknown | working tree 2026-09-07 | authoritative | current | login/logout/error | available | 已同步 requester 核定的 auth behavior。 |
| SRC-007 | `docs/product/land-survey-115/4 介面規格.md` | interface specification | Design / unknown | working tree 2026-09-07 | authoritative | current | navigation/logout | available | 定義帳號名稱、圖台、儀表板、登出入口。 |
| SRC-008 | `docs/product/land-survey-115/5 API規格.md` | API summary | API / unknown | working tree 2026-09-07 | authoritative | current | deferred integration | available | 有 endpoint 摘要，缺 request／response schema；Token 已同步為永久。 |
| SRC-009 | `docs/tasks/UI-ROADMAP-0907/*` | validated planning baseline | Process / repository | baseline `KB-UI-ROADMAP-0907-R1` | authoritative for task split | current | task boundary | available | 指定 UI-001 → UI-002 → UI-003 順序與 Integration deferral。 |
| SRC-010 | `app/src/main`, `app/build.gradle.kts`, `gradle/libs.versions.toml` | repository evidence | Implementation / repository | working tree 2026-09-07 | authoritative | current | current behavior | available | 目前為單 Activity Compose starter，無 Navigation、ViewModel 或 auth feature。 |

## Missing or Inaccessible Sources
- 正式登入／註冊 request／response schema、錯誤 payload、廠商代碼表 schema 尚未提供。

## Initial Conflicts and Gaps
- `KCF-AUTH-001`: Token 為「永久」或「半小時且操作刷新」互相衝突。
- `KCF-AUTH-002`: 登入失敗文案是否包含驗證碼錯誤互相衝突。
- `KCF-AUTH-003`: 註冊成功後自動登入或返回登入頁未定義。
- `KCF-AUTH-004`: 帳號欄位同時標示 `varchar(30)` 與「字數不限」。
- 登出位置文字規格寫頂部右上，Figma 與 APP sitemap 顯示在漢堡側欄；UI responsibility 需切分。
- Subsequent resolution: `KCF-AUTH-001–004` 已由 requester 於 2026-09-07 核定；詳 `knowledge-resolution.md` 的 `KD-AUTH-005`。

## Collection Limitations
- `.git` metadata 不可用，repository evidence 綁定 2026-09-07 working tree 而非 commit。
- Figma asset URLs 約 7 日後到期；Implementation 若使用，必須下載 exact bytes 並依 asset ownership 納入 repository，不得依 screenshot 自行重畫。

## Handoff to Resolution
- Coverage sufficient: yes，限 UI-only Planning。
- Sources requiring authority or freshness resolution: 無；SRC-002 已由 requester 核定為正式 UI。後續檔案內容變更時需重新檢查 freshness。
- Questions Resolution must answer: 哪些行為可用可替換 callback／fake state 安全規劃；登入、註冊、側欄與 Token 各由哪個 Task 負責。
- Blocking items: 無 for Planning；正式視覺來源已核定。
- Recommended next action: knowledge_resolution
