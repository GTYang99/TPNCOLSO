# Knowledge Resolution

## Question and Scope
- Task question: 如何以 requester 最新 composite 重建 Login／Register 視覺權威，並定義不包含 drawer UI 的立即 Logout 契約？
- In scope: `SCR-AUTH-01`、`SCR-AUTH-02`、本機表單狀態、可替換事件邊界、與 `UI-003`／`INT-001` 的 handoff。
- Out of scope: `OVL-NAV-01` 視覺實作、地圖內容、Token persistence／expiry、HTTP integration。

## Candidate Knowledge Baseline
- Baseline ID: `KB-UI-002-AUTH-R10`
- Created date: 2026-09-07 (Asia/Taipei)
- Supersedes: `KB-UI-002-AUTH-R9`（只以 prior standalone screenshot 定義 Login，且仍以 Figma／安全推導定義 Register）
- Status: validated

## Source Register

| Source ID | Title / Location | Type | Domain / Owner | Version / Date | Authority | Freshness | Scope / AC | Notes |
|---|---|---|---|---|---|---|---|---|
| SRC-001 | Current user request, Figma URL, auth decisions, and visual approval | authorized decision | Product / requester | 2026-09-07 | authoritative | current | task scope / auth behavior / visual authority | 核定永久 Token、登入錯誤文案、註冊成功返回登入、帳號最多 30 字元，並核定目前 Figma 即正式 UI。 |
| SRC-002 | Figma `HRbRsw6HoNBUCtaieX8xUM` and prior auth nodes | prior design source | Design / requester | retrieved 2026-09-07 | supporting/candidate asset only | superseded for auth visual acceptance | historical behavior/assets | SRC-015 supersedes Login／Register／Logout visual authority. |
| SRC-003 | `docs/design/app-ui-requirements.md` | approved design specification | Design / requester | 2026-09-07 working tree | authoritative | current | parent AC-001–004, AC-016 | 畫面、元件、可及性 inventory 與正式 source index。 |
| SRC-011 | `docs/tasks/UI-002-auth/login-ui-requirement.md` | normalized UI requirement | Product／Design / requester | revision 3, 2026-09-07 | authoritative derived baseline | current | FR-UI002-001–003, 007; AC-UI002-001–003, 007–009 | Normalizes SRC-015 Login empty／filled／auth-error panels. |
| SRC-012 | `/Users/a10362/Desktop/tp_req_ui_login.md` plus current requester correction request | preliminary requirement plus authorized correction | Product／Design / requester | v0.1 and request, 2026-09-07 | supporting document; current request authoritative for correction | current | Login error state | Preserves separate `#C8320A` border and `#E00000` text values; unresolved draft items do not supersede later decisions. |
| SRC-013 | `docs/tasks/UI-002-auth/registration-ui-requirement.md` | normalized UI requirement | Product／Design / requester | revision 2, 2026-09-07 | authoritative derived baseline | current | Registration visual/state/security/assets | Binds SRC-015 Register panels, initial null required work type and plaintext/no-eye passwords. |
| SRC-014 | Prior standalone Login screenshot | requester-provided screenshot | Design / requester | SHA-256 `aa77c69b…cf86`, 2026-09-07 | historical | superseded | prior Login empty | Replaced in full by SRC-015. |
| SRC-015 | Current composite PNG | requester-provided composite | Product／Design / requester | 7904×2916, sRGB, SHA-256 `75cb578c58f3098c270290f033539ebf06c35c8b322efb71526fa4e53f3d3e9c`, 2026-09-07 | authoritative | current | Login／Register visuals; Logout interface context | Sole current auth visual source; outer gray board/headings excluded; drawer visual remains UI-003-owned. |

Figma MCP inspection on 2026-09-08 confirmed auth source node `2905:2680` contains six Android screen frames at `402 x 874`; the drawer frame is `2926:282` and the home frame is `2908:2792`.
| SRC-016 | `logout-interface-requirement.md` | normalized interface requirement | Product／Architecture / requester | revision 1, 2026-09-07 | authoritative derived baseline | current | Logout boundary / AC-UI002-006, 011 | Immediate local clear, no confirmation, return to clean Login without waiting remote. |
| SRC-004–008 | Product chapters 1–5 | product/data/behavior/interface/API specifications | Product/API / unknown | 2026-09-07 working tree | authoritative by domain | current | auth and navigation | 已依 SRC-001 統一 Token、copy、註冊導覽與帳號長度；詳細 API schema 仍待補。 |
| SRC-009 | `KB-UI-ROADMAP-0907-R1` | task-split baseline | Process / repository | 2026-09-07 | authoritative for sequencing | current | boundaries | UI-002 不包含 map drawer implementation 或 API integration。 |
| SRC-010 | Current Compose repository | implementation evidence | Repository | working tree 2026-09-07 | authoritative | current | current behavior | UI-001 foundation/debug entry 已有 committed implementation，但需針對詳細 Login baseline re-plan／re-implementation。 |

## Material Claims and Traceability

| Claim ID | Statement | Supporting Sources | Contradicting Sources | Authority / Confidence | Requirement / AC | Status |
|---|---|---|---|---|---|---|
| KCL-AUTH-001 | 登入 UI 包含帳號、遮罩密碼、4 碼驗證碼、重整、記住我、登入與註冊入口。 | SRC-002, SRC-003, SRC-005, SRC-006 | None | high | FR-UI002-001 / AC-UI002-001, 002 | resolved |
| KCL-AUTH-002 | 登入成功後進入圖台；UI-only Task 可用 callback 驗證目的事件而不建立地圖。 | SRC-003, SRC-004, SRC-006, SRC-009 | None | high | FR-UI002-003 / AC-UI002-003 | resolved |
| KCL-AUTH-003 | 註冊有帳號、密碼、確認密碼、廠商、作業性質、真實姓名六組 UI 欄位。 | SRC-002, SRC-003, SRC-005 | None | high | FR-UI002-004 / AC-UI002-004 | resolved |
| KCL-AUTH-004 | 登出入口位於主畫面側欄，觸發後應進入認證流程。 | SRC-002, SRC-003, SRC-004, SRC-007 | SRC-006 寫頂部右上角 | medium-high | FR-UI002-006 / AC-UI002-006 | resolved by task boundary |
| KCL-AUTH-005 | 真正的登出 API、Token 清除與 401 不屬 UI-002。 | SRC-008, SRC-009 | None | high | Constraints | resolved |
| KCL-AUTH-006 | Figma 402×874 為視覺參考，不是固定 Android 尺寸。 | SRC-003 | None | high | NFR / AC-UI002-007 | resolved |
| KCL-AUTH-007 | 登入內容寬 320、註冊頁 padding 24、登入錯誤色與邊框、Noto Sans TC 字級、品牌／文字／邊框 tokens 及 exact logo／城市剪影／icon assets 可由 Figma 取得。 | SRC-002, SRC-003 | None | design / high | NFR / AC-UI002-001, 007, 008 | resolved |
| KCL-AUTH-008 | Current Login reference is a 402×874 continuous white-to-pale-blue gradient canvas with explicit vertical anchors, centered 320-wide content and a clipped 736×246 bottom skyline; the composite's outer gray board/headings are excluded. | SRC-001, SRC-011, SRC-015 | SRC-002/SRC-014 prior full-page visuals | design / high | FR-UI002-007 / AC-UI002-009 | resolved by supersession |
| KCL-AUTH-009 | Login empty, filled and auth-error each have an authoritative SRC-015 panel; only field-error and submitting are derived. Auth-error retains values, marks all three credential borders and shows one exact global message. | SRC-001, SRC-011, SRC-012, SRC-015 | prior R9 empty-only authority | product/design / high | FR-UI002-002, 007 / AC-UI002-003, 008, 009 | resolved by supersession |
| KCL-AUTH-010 | Login auth-error uses separate visual tokens: field borders `#C8320A`, global error text `#E00000`. | SRC-002, SRC-011, SRC-012 | Earlier R6 normalization used `#C8320A` for both | product/design / high | UIR-LOGIN-006 / AC-UI002-009 | resolved |
| KCL-AUTH-011 | Registration uses a 402×874 white/light-only canvas, 24 padding, 36 main-group gaps, 354-wide form/actions, 80 user illustration and exact empty/filled states. | SRC-002, SRC-013 | Earlier generic six-field description | design / high | FR-UI002-008 / AC-UI002-010 | resolved |
| KCL-AUTH-012 | Registration password and confirmation display plaintext without an eye action; initial work type is null and completion treats it as required. Sensitive values still must not be logged, persisted or saved. | SRC-001, SRC-013, SRC-015 | KD-AUTH-009 masked behavior and old default-external plan | product/design / high | FR-UI002-004, 005, 008 / UIR-REG-004, 006, 009 | resolved by KD-AUTH-012 |
| KCL-AUTH-013 | UI-002 owns Auth MVVM state/effects and debug AuthHost integration; debug direct-login remains available, while release fake/bypass wiring is deferred and prohibited. | SRC-009, SRC-010, SRC-013 | Earlier unspecified host/state design | architecture / high | AC-UI002-006, 008 | resolved |
| KCL-AUTH-014 | Each auth screen panel in the current composite represents a 402×874 canvas at approximately 2× display scale; raster-derived anchors use ±3dp tolerance while fixed sizes/copy do not. | SRC-015, SRC-011, SRC-013 | None | design / high | UIR-LOGIN-001–005, UIR-REG-001–003 | resolved |
| KCL-AUTH-015 | UI-003 owns the drawer UI; UI-002 owns a callable Logout destination contract that immediately clears local state and returns clean Login without confirmation or remote wait. | SRC-001, SRC-015, SRC-016 | prior INT-001-only session split | product/architecture / high | FR-UI002-006, 009 / AC-UI002-006, 011 | resolved by KD-AUTH-012 |

## Conflicts and Gaps

| Conflict ID | Claims / Sources | Domain | Impact / Severity | Options | Decision Owner | Status |
|---|---|---|---|---|---|---|
| KCF-AUTH-001 | SRC-006 永久 Token；SRC-004／008 半小時滑動 Token | Security/API | P1；影響 INT-001 | 採永久 Token | Requester | resolved by SRC-001 |
| KCF-AUTH-002 | 行為規格「帳號或密碼錯誤」；Figma node `4922:15370` 為「帳號、密碼或驗證碼錯誤」 | Copy/validation | P2；影響 production copy | 採「帳號、密碼或驗證碼錯誤」 | Requester | resolved by SRC-001 |
| KCF-AUTH-003 | 註冊成功後自動登入或返回登入未定義 | Navigation | P2；影響 integration | 返回登入頁，由使用者登入 | Requester | resolved by SRC-001 |
| KCF-AUTH-004 | `varchar(30)` 與「字數不限」 | Data validation | P2；影響帳號最大長度 | 僅限英數，最多 30 字元 | Requester | resolved by SRC-001 |
| KCF-AUTH-005 | 登出在右上角或側欄 | Navigation UI | P2；影響元件 ownership | APP 採 Figma／sitemap 的側欄；`UI-003` 畫側欄，`INT-001` 執行 Session 登出 | Product + Design | resolved for task split |
| KCF-AUTH-006 | Existing generic Login requirement vs exact three-frame Figma composition | UI specification | P1；generic Material form could pass old AC while visibly wrong | keep generic / normalize exact requirement | Requester + Planning | resolved by KD-AUTH-007 |
| KCF-AUTH-007 | R6 unified error border/text to `#C8320A`; Figma rendered text and SRC-012 specify `#E00000` text | UI token | P1；pixel comparison and token implementation would be wrong | unify / preserve separate tokens | Requester + Design | resolved by KD-AUTH-008 |
| KCF-AUTH-008 | Registration filled frame shows plaintext fixture passwords while approved security rules require masking | Security/UI | P1；runtime credential exposure | reproduce fixture / mask runtime values | Requester + Product security | resolved by KD-AUTH-009 |
| KCF-AUTH-009 | SRC-002 Login frames were treated as full-page visual authority; requester says that background is not the current version and supplies SRC-014 | UI source authority | P1；implementation would reproduce the rejected page | retain Figma / use screenshot / wait for another file | Requester | resolved by KD-AUTH-011: current screenshot supersedes Login page visual |
| KCF-AUTH-010 | SRC-015 composite vs all previous auth screenshots/Figma | UI source authority | P1; mixed acceptance baselines would produce mismatched screens | replace all / merge / wait | Requester | resolved by KD-AUTH-012: replace all; gray board/headings excluded |
| KCF-AUTH-011 | Register default external vs no initial selection | Product/UI state | P1; wrong initial state and validation | default / null required | Requester | resolved by KD-AUTH-012: null and required |
| KCF-AUTH-012 | Register masked runtime vs plaintext current panel | Product/security UI | P1; visible behavior differs | masked / plaintext | Requester | resolved by KD-AUTH-012: plaintext/no eye; retain no-persistence controls |
| KCF-AUTH-013 | Drawer/logout responsibility split | Architecture/navigation | P1; duplicate drawer or delayed logout | UI-002 owns all / UI-003 owns visual with UI-002 destination contract | Requester | resolved by KD-AUTH-012 |

## Decision Records

### KD-AUTH-001

- Decision: `UI-002-auth` 只實作登入／註冊畫面與可替換 UI state／event contract；資料來源使用集中 fake implementation。
- Alternatives: 直接建立 Retrofit DTO；將所有狀態硬寫在 Composable。
- Source / Conflict IDs: SRC-008–010, KCF-AUTH-001–004
- Rationale: 可先驗收畫面與互動，同時避免將未完成 API schema 固化為 production contract。
- Approver and date: `KB-UI-ROADMAP-0907-R1`, 2026-09-07。
- Affected requirements / AC / artifacts: 全部 UI-002 AC；parent AC-001–003 的 UI portion。
- Supersedes: None
- Revalidation trigger: auth schema 或 UI roadmap 更新。

### KD-AUTH-002

- Decision: 登入成功、註冊成功與登入入口顯示均以 callback／effect 表達，不在 UI-002 引入完整 App Navigation 或假畫面。
- Alternatives: 立即加入 Navigation Compose；登入後顯示硬編碼 map placeholder。
- Source / Conflict IDs: SRC-009, SRC-010, KCF-AUTH-003
- Rationale: UI-003 尚未建立，callback 可由 Compose test 驗證且不預先決定 app graph。
- Approver and date: Planning boundary, 2026-09-07。
- Affected requirements / AC / artifacts: FR-UI002-003, FR-UI002-006, AC-UI002-003, 006。
- Supersedes: None
- Revalidation trigger: UI-001 或 UI-003 確立 navigation architecture。

### KD-AUTH-003

- Decision: 側欄與登出列的視覺屬 `UI-003-map-shell`；正式登出、Token 清除、401 屬 `INT-001-auth-api`；UI-002 定義兩者返回登入所需的 auth-entry contract。
- Alternatives: 將側欄複製進 UI-002；在 UI-003 直接持有 Token。
- Source / Conflict IDs: SRC-002–004, SRC-007–009, KCF-AUTH-005
- Rationale: 保留單一元件 ownership 並符合 roadmap 的 API separation。
- Approver and date: `KB-UI-ROADMAP-0907-R1`, 2026-09-07。
- Affected requirements / AC / artifacts: parent AC-004, AC-016；UI-002/003/INT-001 handoff。
- Supersedes: None
- Revalidation trigger: roadmap 或正式 navigation/session architecture 改變。

### KD-AUTH-004

- Decision: 品牌色、字型、間距與 asset 必須對應至 UI-001 token／asset catalog，UI-002 不散落 raw values；active visual source 由後續決策指定。
- Alternatives: 從 screenshot 估算所有像素並直接硬編碼。
- Source / Conflict IDs: SRC-002, SRC-003, SRC-009, SRC-010
- Rationale: UI-001 是既定 prerequisite；requester 已核定目前 Figma 為正式 UI。
- Approver and date: repository design governance, 2026-09-07。
- Affected requirements / AC / artifacts: NFR, AC-UI002-007。
- Supersedes: None
- Revalidation trigger: 核定 Figma 內容變更或 UI-001 tokens／asset catalog 完成。

### KD-AUTH-006

- Decision: Figma Copy `HRbRsw6HoNBUCtaieX8xUM`、入口 node `2905:2680` 與已取得的 auth 子節點，依 requester 2026-09-07 決策升格為正式 UI baseline。
- Alternatives: 繼續等待另一份無差異 revision；僅作 Planning reference。
- Source / Conflict IDs: SRC-001–003, KV-AUTH-003, REQ-AUTH-001
- Rationale: Requester 明確表示正式 UI 不會與目前版本有差別，且目前版本就是正式版 UI。
- Approver and date: Requester, 2026-09-07。
- Affected requirements / AC / artifacts: UI-001 visual tokens/assets；UI-002 AC-UI002-001–008；design specification；visual Verification baseline。
- Supersedes: KD-AUTH-004 中「設計尚未核定」的限制與 `KB-UI-002-AUTH-R4` 對應假設。
- Revalidation trigger: Figma file/node 內容、source index 或 requester approval scope 變更。
- Status update: Superseded by `KD-AUTH-012` for all Login／Register／Logout visual acceptance；Figma 僅保留 historical／candidate asset value。

### KD-AUTH-005

- Decision: Token 效期永久；登入失敗統一顯示「帳號、密碼或驗證碼錯誤」；註冊成功返回登入頁；帳號僅限英數且最多 30 字元。
- Alternatives: 半小時滑動 Token；不含驗證碼的錯誤文案；註冊後自動登入；帳號不限制長度。
- Source / Conflict IDs: SRC-001, KCF-AUTH-001–004
- Rationale: requester 以產品權責明確核定。
- Approver and date: Requester, 2026-09-07。
- Affected requirements / AC / artifacts: FR-UI002-002, 004, 005；AC-UI002-003–005；INT-001 session contract；product chapters 1, 2, 3, 5。
- Supersedes: 相關 provisional assumptions 與 deferred decisions。
- Revalidation trigger: requester 或 API/security owner 核定新的 auth contract。

### KD-AUTH-007

- Decision: `login-ui-requirement.md` is the authoritative normalized requirement for Login composition and the three visual states. Figma-observed geometry/tokens/assets are mandatory at the 402×874 baseline; Android responsive/accessibility adaptations are separately labeled and must not erase the reference composition.
- Alternatives: Continue using the generic content inventory; implement a standard Material login form and compare only copy/fields.
- Source / Conflict IDs: SRC-001–003, SRC-011, KCF-AUTH-006
- Rationale: Requester identified the prior implementation direction as incorrect and requested a detailed page-derived UI requirement.
- Approver and date: Requester, 2026-09-07。
- Affected requirements / AC / artifacts: FR-UI002-001–003, 007; AC-UI002-001–003, 007–009; UI-001 checkbox contract; UI-002 plan/review/tests.
- Supersedes: Generic Login visual interpretation in `KB-UI-002-AUTH-R5`.
- Revalidation trigger: Any change to Login nodes `2905:2679`, `4922:15312`, `4922:15370` or requester visual intent.
- Status update: Superseded by `KD-AUTH-011`. Revision 2 of `login-ui-requirement.md` uses the current screenshot for `LOGIN_EMPTY`; former Figma states are supporting/derived evidence only.

### KD-AUTH-008

- Decision: Login authentication-error borders use `#C8320A`; the global error message uses `#E00000`. Foundation exposes separate semantic roles and must not collapse them into one generic error color.
- Alternatives: Use `#C8320A` for both; use `#E00000` for both.
- Source / Conflict IDs: SRC-001, SRC-002, SRC-011, SRC-012, KCF-AUTH-007
- Rationale: This preserves the rendered Figma state and the requester-provided UI requirement instead of silently resolving differing visual values.
- Approver and date: Requester, 2026-09-07。
- Affected requirements / AC / artifacts: UIR-LOGIN-006, AC-UI002-009, UI-001 semantic token contract, Login screenshot tests.
- Supersedes: R6 statement that both borders and message use `#C8320A`.
- Revalidation trigger: Approved Figma error-state or requester visual decision changes.

### KD-AUTH-009

- Decision: `registration-ui-requirement.md` is authoritative for Registration composition and empty/filled states. Runtime password and confirmation values are masked, Figma plaintext values remain fixtures only, and no eye action is added because the approved Registration frames do not contain one.
- Alternatives: Implement a generic Registration form; reproduce plaintext fixture behavior; add a non-designed visibility control.
- Source / Conflict IDs: SRC-001, SRC-002, SRC-012, SRC-013, KCF-AUTH-008
- Rationale: Preserves exact approved composition while applying the higher-authority security requirement to sensitive runtime values.
- Approver and date: Requester, 2026-09-07。
- Affected requirements / AC / artifacts: FR-UI002-004, 005, 008; AC-UI002-004, 005, 010; UI-001 password component contract and Registration visual tests.
- Supersedes: Generic Registration visual interpretation in R7 and the filled-frame plaintext fixture as runtime behavior.
- Revalidation trigger: Registration frames, password security decision or registration product flow changes.
- Status update: Superseded by `KD-AUTH-012` for visual source, password presentation and initial work-type state.

### KD-AUTH-010

- Decision: `AuthViewModel` owns in-memory Auth state and one-shot effects. Debug `AppEntry` defaults to AuthHost and preserves direct-login behind a debug-only outer-wrapper switch; release `AppEntry` retains its unauthenticated placeholder and never references fake/bypass code until INT-001 supplies production wiring.
- Alternatives: Put requests/state in Composables; replace debug direct-login; ship fake authentication in release; add Navigation Compose now.
- Source / Conflict IDs: SRC-009, SRC-010, PLN-AUTH-003, PLN-AUTH-004
- Rationale: Matches MVVM, preserves UI-001's approved debug bypass and prevents unapproved fake authentication from entering release.
- Approver and date: Architecture/Planning, 2026-09-07。
- Affected requirements / AC / artifacts: AC-UI002-003, 006, 008; AuthRoute/ViewModel; debug/release AppEntry tests; INT-001 handoff.
- Supersedes: Unspecified state owner and MainActivity-centered host wiring in plan revision 10.
- Revalidation trigger: UI-001 AppRoot/session contract, navigation architecture or INT-001 production source changes.

### KD-AUTH-011

- Decision: Screenshot SRC-014 is the sole authoritative full-page visual source for the current Login empty state. It supersedes former Login Figma frames for background, vertical placement and pixel comparison. Filled, field-error, auth-error and submitting states are derived from the screenshot composition plus approved behavior; the purple selection outline and editor margin are excluded from UI.
- Alternatives: Retain the former Figma frames; combine old background with the new screenshot; block until another Figma revision is supplied.
- Source / Conflict IDs: SRC-001, SRC-002, SRC-011, SRC-014, KCF-AUTH-009, REQ-AUTH-004
- Rationale: The requester explicitly rejected the prior background/version and provided the replacement source. Keeping the old frames as equal visual authority would recreate the reported mismatch.
- Approver and date: Requester, 2026-09-07.
- Affected requirements / AC / artifacts: FR-UI002-007; AC-UI002-001, 007–009; `login-ui-requirement.md` revision 2; UI-001 gradient/icon contract; Login screenshot tests; plan revision 12.
- Supersedes: `KD-AUTH-006` and `KD-AUTH-007` only for Login full-page visual authority; `KB-UI-002-AUTH-R8`.
- Revalidation trigger: Screenshot bytes/SHA-256, requester visual intent, or a later explicitly approved Login source changes.
- Status update: Superseded in full by `KD-AUTH-012` and SRC-015.

### KD-AUTH-012

- Decision: SRC-015 is the sole formal visual source for Login, Register and Logout context; every earlier screenshot and Figma frame is superseded for auth visual acceptance. The outer gray board and headings are not App UI. Login empty／filled／auth-error and Register empty／filled use their current panels. Register begins with no work type selected, requires a selection on completion, and shows password/confirmation plaintext without an eye action. UI-003 owns the complete drawer visual; UI-002 exposes only a Logout destination contract. Activating Logout clears local login/session and all Auth／Registration transient state immediately, without confirmation, then returns the formal empty Login; remote logout cannot delay or reverse that transition and remains INT-001-owned.
- Alternatives: Merge old and new visual sources; keep default external role; override plaintext with masking; implement the drawer in UI-002; wait for remote logout before navigation.
- Source / Conflict IDs: SRC-001, SRC-002, SRC-011, SRC-013–016, KCF-AUTH-009–013, REQ-AUTH-005.
- Rationale: These are the requester's explicit answers to every material open question and define observable behavior without expanding UI-002 into the drawer or API tasks.
- Approver and date: Requester, 2026-09-07.
- Affected requirements / AC / artifacts: FR-UI002-004–009; AC-UI002-004–011; all three detailed UI/interface requirements; UI-001 component variants; plan revision 13; UI-003 and INT-001 handoffs.
- Supersedes: KD-AUTH-006/007/011 for auth visual authority; KD-AUTH-009 for Register password and work-type behavior; KD-AUTH-003 only where it assigned all local logout behavior to INT-001; `KB-UI-002-AUTH-R9`.
- Revalidation trigger: Composite bytes/approval scope, Register privacy decision, Logout behavior, or task ownership changes.

## Assumptions

| Assumption ID | Statement / Evidence | Confidence | Impact if Wrong | Owner | Validation Method | Expiry / Trigger | Safe for Planning |
|---|---|---|---|---|---|---|---|
| KA-AUTH-001 | UI-001 會提供文字欄位、密碼欄位、主要／次要按鈕、icon button 與 theme tokens。來源 SRC-009。 | medium | UI-002 affected files／元件名稱需更新 | UI-001 owner | Plan Review 前讀取 UI-001 output；Implementation 前確認 | UI-001 完成時 | yes |
| KA-AUTH-002 | 註冊廠商選項可用 deterministic fake list 驗收，但不得成為 production code table。 | high | fixture 更換，不改 UI contract | UI-002 owner | Compose test 注入不同 options | API code table available | yes |
| KA-AUTH-003 | 登入頁底部城市剪影與品牌 logo 應由核定 asset 提供；在 asset 未進 catalog 前不可自行重畫。Logo 由 UI-001 owns，skyline 由 UI-002 owns。 | high | Preview 暫缺裝飾 asset | UI-001 / UI-002 owners | asset manifest node/path/SHA-256 check | asset handoff | yes |

## Resolved Items
- 登入與註冊欄位、主要狀態、local validation、callback boundary 已足以 Planning。
- 登出流程已切分為 UI-003 drawer/action UI、UI-002 immediate local-clear／Login destination contract、INT-001 remote logout／production Token behavior。
- UI-001 prerequisite 保持；current composite values 必須透過 semantic token／asset ownership 實作，不得散落 hardcoded visual values。
- Login three-panel and Registration two-panel composition/state mappings are normalized in their dedicated requirements；gray board/headings excluded。
- MVVM state/effect ownership and debug/release host boundaries are resolved without introducing Navigation Compose or release fake authentication.

## Unresolved Non-blocking Items
- Exact reusable vectors may still come from former sources, but Implementation must prove they visually match SRC-015 before reuse.

## Blocking Items
- None for UI-only Planning。
- Implementation entry condition: `UI-001-foundation` 完成且元件／token／asset implementation 可讀、可驗證。

## Downstream Impact
- Requirements / AC: FR-UI002-007–009 and AC-UI002-009–011 bind `UIR-LOGIN-001–010`, `UIR-REG-001–011` and `UIR-LOGOUT-001–006`; UI-002 只能完成 parent AC-001–003／016 的 owned portion。
- Plan / design: Login maps SRC-015 three panels；Registration maps SRC-015 two panels；Logout uses a UI-003 callback fixture and implements no drawer. Components/shared assets reuse UI-001 only after matching SRC-015。
- Code / data / API: fake model 不得命名或結構化為 production DTO。
- Tests / verification: 驗證 state matrix、焦點／IME、密碼不洩漏、重複提交抑制與 callback 次數。
- Release / operations: 無；本階段不修改 production code。

## Revalidation Triggers
- UI-001 完成後的元件／token／asset contract 與本假設不符。
- Composite bytes／approval intent、產品 Login／Register／Logout behavior、auth API schema 或 navigation architecture 更新。
- 後續提供新 Figma 或 screenshot 時，必須明確比對並記錄是否 supersede SRC-015。

## Validation Handoff
- Candidate baseline: `KB-UI-002-AUTH-R10`
- Decisions and constraints Validation must check: KD-AUTH-001–012；SRC-015 SHA-256／outer-chrome exclusion／panel normalization；`UIR-LOGIN-001–010`；`UIR-REG-001–011`；`UIR-LOGOUT-001–006`；Register null-required work type／plaintext no-eye；immediate Logout；MVVM and variant-host ownership；UI/API/task ownership 不重疊。
- Safe assumptions: KA-AUTH-001–003。
- Required follow-up evidence: UI-001 output；exact asset inventory；詳細 auth API schema。
- Material blockers: no for UI-only Planning
- Ready for Validation: yes
