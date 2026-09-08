# Knowledge Collection

## Question and Scope
- Task question: Auth visual authority 再更換為 requester composite 後，UI-001 的 semantic tokens、shared assets、password variants、selection components 與已有 implementation evidence 需如何重新驗證？
- In scope: visual source authority、semantic tokens、shared／feature asset ownership、foundation components、AppRoot/session boundary、debug direct login、Android 9+。
- Out of scope: production Auth API、Token persistence、完整 navigation graph、map feature implementation。
- Collection date: 2026-09-07 (Asia/Taipei)
- Collector: Codex Planning

## Source Coverage

| Category | Required | Result | Notes |
|---|---|---|---|
| Authorized decisions / requirement | yes | found | Requester 核定 direct-login 不變，並指定 current composite 取代所有 prior auth visuals；Register plaintext/no-eye、work type initial null/required。 |
| Product rules | yes | found | Android 9+、角色與 auth/navigation responsibility 可用。 |
| Design / assets / accessibility | yes | found | Composite identity/SHA、Login revision 3、Register revision 2 與全部 prior auth visual supersession 已取得。 |
| API / schema / authentication | no | N/A | UI-001 明確不實作 production API／Token。 |
| Architecture / repository | yes | found | Compose starter、Gradle、MainActivity 與 source-set 現況已檢視。 |
| Tests / logs / runtime evidence | yes | found | 僅有 template tests；無 foundation implementation evidence。 |
| Build / release / operations | yes | missing | Workspace 無 Git metadata，無法形成 branch／commit／CI evidence。 |
| External primary sources | no | N/A | 無需外部規格即可完成本 baseline。 |

## Source Register

| Source ID | Title / Location | Type | Domain / Owner | Version / Date | Authority | Freshness | Scope / AC | Access | Notes |
|---|---|---|---|---|---|---|---|---|---|
| SRC-UI001-001 | Current requester decisions | authorized decision | Product／Design / requester | 2026-09-07 | authoritative | current | all UI-001 AC | available | 核定目前 Figma 即正式 UI，並要求 API 未完成時可直接登入。 |
| SRC-UI001-002 | Figma Copy `HRbRsw6HoNBUCtaieX8xUM`, entry `2905:2680`, indexed child nodes | prior design source | Design / requester | retrieved 2026-09-07 | supporting for auth candidate assets; authoritative for unaffected UI | mixed | AC-UI001-009–010 | available | SRC-UI001-008 supersedes auth visual acceptance. |
| SRC-UI001-003 | `docs/design/app-ui-requirements.md` | approved design specification | Design / requester | working tree 2026-09-07 | authoritative | current | visual/component/accessibility | available | 保存 screen、component、state、responsive 與 source index。 |
| SRC-UI001-004 | `docs/product/land-survey-115/` | product specification set | Product / requester | working tree 2026-09-07 | authoritative by domain | current | Android 9+, roles, auth/navigation | available | 角色與介面規格適用；API 細節不屬本 Task。 |
| SRC-UI001-005 | Current Android repository | implementation evidence | Repository | working tree 2026-09-07; commit unavailable | authoritative for current behavior | current | AC-UI001-004, 006–007 | available | Compose starter；無 foundation／session；minSdk 24。 |
| SRC-UI001-006 | `docs/tasks/UI-ROADMAP-0907/` | task sequencing baseline | Process / repository | 2026-09-07 | authoritative for task split | current | UI-001 consumer handoff | available | UI-001 precedes UI-002／UI-003。 |
| SRC-UI001-007 | Prior standalone Login screenshot and revision 2 requirement | prior design source | Design / requester | SHA-256 `aa77c69b…cf86`, 2026-09-07 | historical | superseded | prior Login visual | available | Replaced in full by SRC-UI001-008. |
| SRC-UI001-008 | Current composite; Login revision 3; Register revision 2; `KD-AUTH-012` | authorized composite and normalized requirements | Product／Design / requester | PNG 7904×2916, sRGB, SHA-256 `75cb578c58f3098c270290f033539ebf06c35c8b322efb71526fa4e53f3d3e9c`, 2026-09-07 | authoritative for auth foundation visuals/variants | current | AC-UI001-008–010 | available | Sole auth visual source. Requires masked＋eye Login variant, plaintext＋no-eye Register variant, null-selection RadioGroup and candidate-asset comparison. |
| SRC-UI001-009 | UI-002 Plan Review iteration 12 findings | planning dependency evidence | Architecture / UI-002 Plan Review | 2026-09-07 | authoritative for consumer handoff gap | current | AC-UI001-005, 008 | available | Existing debug class has `startDebugSession`, but the reusable contract lacks an exact debug-only controller interface; UI-002 needs one composition-root transition owner without exposing debug API to main/release. |
| SRC-UI001-010 | `app/src/debug/java/com/example/tp_ncolso_android/DebugSessionController.kt` and updated `DebugSessionOwner.kt` | current implementation evidence | Repository / UI-001 | working tree 2026-09-08 | current | current | debug-only session contract | Interface declares `startDebugSession`; owner implements and overrides it. Compile remains unverified because Java Runtime is unavailable. |

## Missing or Inaccessible Sources
- Git metadata is unavailable; this blocks implementation traceability but not Knowledge resolution.

## Initial Conflicts and Gaps
- Figma 原先被標示為開發版；requester 最新決策將同一內容核定為正式 UI。
- Shared brand asset 與 feature-only asset ownership 需在 foundation contract 明確切分。
- Current SRC-UI001-008 conflicts with the R2 baseline, SRC-UI001-007 and commit `bc78382` evidence; the prior Verification route remains stale.
- UI-002 iteration 12 found that the provisional wording was not a compile-time injection contract; the explicit debug-only interface is now added and must be revalidated by compile/test when Java Runtime is available.

## Collection Limitations
- Repository evidence 只能綁定 2026-09-07 working tree，不能綁定 commit。
- Figma asset URLs 可能失效；Implementation 必須保存 exact bytes 與 node／retrieval evidence。

## Handoff to Resolution
- Coverage sufficient: yes
- Sources requiring authority or freshness resolution: SRC-UI001-008 supersedes SRC-UI001-002／007 for all auth foundation visual evidence.
- Questions Resolution must answer: composite 對 tokens/assets、Login masked＋eye、Register plaintext＋no-eye、initial-null radio validation 與 commit `bc78382` evidence 的影響，以及新增 interface 是否符合 debug/main/release 隔離。
- Blocking items: UI-001 不得繼續 Verification 或交付 UI-002，直到新 Knowledge／Planning／Implementation cycle 完成。
- Recommended next action: knowledge_resolution
