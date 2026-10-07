# Knowledge Collection

## Question and Scope
- Task question: 如何以 requester 最新合成圖重建 Login／Register 視覺需求，並在不納入側欄實作或推測 API 的前提下定義立即登出契約？
- In scope: 登入與註冊畫面、欄位與本機驗證、loading／error 狀態、認證成功與登出事件邊界、與圖台側欄的責任切分。
- Out of scope: 正式 API DTO／endpoint 串接、Token 儲存與刷新、401 處理、圖台與側欄的 Compose 實作、發佈。
- Collection date: 2026-09-07 (Asia/Taipei)
- Collector: Codex Planning

## Source Coverage

| Category | Required | Result | Notes |
|---|---|---|---|
| Authorized decisions / requirement | yes | found | 使用者要求 Planning，並於 2026-09-07 核定永久 Token、錯誤文案、註冊導覽與帳號長度。 |
| Product rules | yes | found | 角色、資料、行為與介面規格已檢視。 |
| Design / assets / accessibility | yes | found | 最新 7904×2916 composite 是 Login／Register／Logout 唯一正式視覺來源；外層灰色畫布與標題排除，所有先前 screenshot／Figma visual authority 均被取代。 |
| API / schema / authentication | yes | found | endpoint 摘要存在；Token 已核定為永久，詳細 request／response schema 仍不完整。 |
| Architecture / repository | yes | found | Compose starter、theme 與 Gradle 設定已檢視。 |
| Tests / logs / runtime evidence | yes | found | 僅有 template unit／instrumented tests，無產品行為 evidence。 |
| Build / release / operations | no | N/A | 本次僅 Planning，不修改 production code。 |
| External primary sources | no | N/A | 無需外部平台規格即可完成 UI-only Planning。 |

## Source Register

| Source ID | Title / Location | Type | Domain / Owner | Version / Date | Authority | Freshness | Scope / AC | Access | Notes |
|---|---|---|---|---|---|---|---|---|---|
| SRC-001 | Current user request, Figma URL, and auth decisions | authorized decision | Product / requester | 2026-09-07 | authoritative | current | task scope | available | 核定永久 Token、錯誤文案、註冊返回登入、帳號最多 30 字元，以及最新六項 visual／Register／Logout 決策。 |
| SRC-002 | Figma Copy file `HRbRsw6HoNBUCtaieX8xUM`, section `2905:2680` and prior auth nodes | prior design source | Design / requester | retrieved 2026-09-07 | supporting/superseded for Login／Register／Logout visual acceptance | superseded | candidate assets and historical behavior only | available | SRC-015 supersedes all auth-screen visual authority; reuse requires comparison against SRC-015. |
| SRC-003 | `docs/design/app-ui-requirements.md` | design specification | Design / requester | working tree 2026-09-07 | authoritative visual baseline | current | parent AC-001–004, AC-016 | available | 包含 screen、component、state、responsive inventory 與核定 source index。 |
| SRC-004 | `docs/product/land-survey-115/1 系統概述與角色權限.md` | product specification | Product / unknown | working tree 2026-09-07 | authoritative | current | auth roles, navigation | available | 定義可自行註冊角色、單裝置登入與 APP sitemap。 |
| SRC-005 | `docs/product/land-survey-115/2 資料規格.md` | data specification | Data / unknown | working tree 2026-09-07 | authoritative | current | registration fields | available | 定義帳號、密碼、廠商、作業性質、姓名。 |
| SRC-006 | `docs/product/land-survey-115/3 系統行為規則.md` | behavior specification | Product / unknown | working tree 2026-09-07 | authoritative | current | login/logout/error | available | 已同步 requester 核定的 auth behavior。 |
| SRC-007 | `docs/product/land-survey-115/4 介面規格.md` | interface specification | Design / unknown | working tree 2026-09-07 | authoritative | current | navigation/logout | available | 定義帳號名稱、圖台、儀表板、登出入口。 |
| SRC-008 | `docs/product/land-survey-115/5 API規格.md` | API summary | API / unknown | working tree 2026-09-07 | authoritative | current | deferred integration | available | 有 endpoint 摘要，缺 request／response schema；Token 已同步為永久。 |
| SRC-009 | `docs/tasks/UI-ROADMAP-0907/*` | validated planning baseline | Process / repository | baseline `KB-UI-ROADMAP-0907-R1` | authoritative for task split | current | task boundary | available | 指定 UI-001 → UI-002 → UI-003 順序與 Integration deferral。 |
| SRC-010 | `app/src/main`, `app/build.gradle.kts`, `gradle/libs.versions.toml` | repository evidence | Implementation / repository | working tree 2026-09-07 | authoritative | current | current behavior | available | UI-001 foundation/debug entry 已存在，但尚無 UI-002 auth feature，且 foundation 未對齊詳細 Login baseline。 |
| SRC-011 | `docs/tasks/UI-002-auth/login-ui-requirement.md` | normalized UI requirement | Product／Design / requester | revision 3, 2026-09-07 | authoritative derived baseline | current | FR-UI002-001–003, 007; AC-UI002-001–003, 007–009 | available | Normalizes SRC-015 Login empty／filled／auth-error panels; field-error／submitting remain derived. |
| SRC-012 | `/Users/a10362/Desktop/tp_req_ui_login.md` | requester-provided preliminary requirement | Product／Design / requester | v0.1, 2026-09-07 | supporting; authoritative where reaffirmed by current request | current | Login comparison and error colors | available | 記錄 Login 錯誤欄框 `#C8320A`、文字 `#E00000`；其他「待確認」項目不得覆蓋已核定決策。 |
| SRC-013 | `docs/tasks/UI-002-auth/registration-ui-requirement.md` | normalized UI requirement | Product／Design / requester | revision 2, 2026-09-07 | authoritative derived baseline | current | FR-UI002-004, 005, 008; AC-UI002-004, 005, 007, 008, 010 | available | Binds SRC-015 two Register panels, initial null required work type, plaintext password and no-eye behavior. |
| SRC-014 | `/Users/a10362/Desktop/截圖 2026-09-07 下午5.39.20.png` | prior requester screenshot | Design / requester | PNG 568×1202, Display P3, SHA-256 `aa77c69b…cf86`, 2026-09-07 | historical | superseded | prior Login base only | available | Replaced in full by SRC-015. |
| SRC-015 | `/var/folders/wm/_jpdxgws6mn1jxpy1k6jjmj80000gp/T/codex-clipboard-52d4f92d-7688-4c67-99f9-f6e97ce5f1dc.png` | requester-provided composite | Product／Design / requester | PNG 7904×2916, sRGB, SHA-256 `75cb578c58f3098c270290f033539ebf06c35c8b322efb71526fa4e53f3d3e9c`, 2026-09-07 | authoritative | current | all Login／Register visual AC and Logout interface context | available; temporary path | Sole formal visual source. Contains three Login panels, two Register panels, home and drawer examples; gray board/headings excluded. Drawer visuals remain UI-003-owned. |
| SRC-016 | `docs/tasks/UI-002-auth/logout-interface-requirement.md` | normalized interface requirement | Product／Architecture / requester | revision 1, 2026-09-07 | authoritative derived baseline | current | FR-UI002-006, 009; AC-UI002-006, 011 | available | Defines immediate local clear and return to Login without confirmation or remote wait; excludes drawer UI. |

## Missing or Inaccessible Sources
- 正式登入／註冊 request／response schema、錯誤 payload、廠商代碼表 schema 尚未提供。

## Initial Conflicts and Gaps
- `KCF-AUTH-001`: Token 為「永久」或「半小時且操作刷新」互相衝突。
- `KCF-AUTH-002`: 登入失敗文案是否包含驗證碼錯誤互相衝突。
- `KCF-AUTH-003`: 註冊成功後自動登入或返回登入頁未定義。
- `KCF-AUTH-004`: 帳號欄位同時標示 `varchar(30)` 與「字數不限」。
- `KCF-AUTH-007`: Figma semantic variable 提供 `#C8320A`，但實際錯誤文字與 requester-provided requirement 記錄 `#E00000`；不可靜默統一。
- `KCF-AUTH-009`: Requester 明確指出舊 Login 背景不是當前版本；SRC-014 與 SRC-002 的 Login 整頁 visual authority 衝突，需依最新 requester 決定記錄 supersession。
- `KCF-AUTH-010`: SRC-015 是否取代全部先前 Login／Register／Logout 視覺來源，以及 gray board／panel titles 是否屬 UI。
- `KCF-AUTH-011`: 舊 Registration requirement 預設外業；requester 決定初始不預選且完成必填。
- `KCF-AUTH-012`: 舊安全推導要求遮罩 Registration 密碼；requester 明確要求依圖顯示明文。
- `KCF-AUTH-013`: 登出位置與行為跨 UI-002／UI-003／INT-001；requester 決定 drawer 不在本次範圍，但點擊後必須立即清除登入狀態並回 Login。
- Subsequent resolution: `KCF-AUTH-001–004` 已由 `KD-AUTH-005` 核定；`KCF-AUTH-007` 由 `KD-AUTH-008` 解決；`KCF-AUTH-009–013` 由最新 requester 決策與 `KD-AUTH-012` 解決。

## Collection Limitations
- Repository 已建立 Git；本輪 requirement work 位於 `feature/UI-001-foundation-compose` working tree。
- Figma asset URLs 約 7 日後到期；Implementation 若使用，必須下載 exact bytes 並依 asset ownership 納入 repository，不得依 screenshot 自行重畫。

## Handoff to Resolution
- Coverage sufficient: yes，限 UI-only Planning。
- Sources requiring authority or freshness resolution: SRC-015 已由 requester 核定為全部 auth visual 的唯一權威；SRC-002／014 均降級為 historical／candidate asset evidence。
- Questions Resolution must answer: composite panel authority、Register initial selection/password presentation、Logout immediate transition，以及 UI-002／UI-003／INT-001 ownership 如何一致落入 requirements 與 plan。
- Blocking items: 無 for Planning；正式視覺來源已核定。
- Recommended next action: knowledge_resolution
