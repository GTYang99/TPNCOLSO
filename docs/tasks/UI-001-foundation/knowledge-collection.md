# Knowledge Collection

## Question and Scope
- Task question: 如何以已核定正式 Figma UI 建立可重用 Compose foundation，並在 API 未完成時提供只存在於 debug build 的直接登入？
- In scope: visual source authority、semantic tokens、shared／feature asset ownership、foundation components、AppRoot/session boundary、debug direct login、Android 9+。
- Out of scope: production Auth API、Token persistence、完整 navigation graph、map feature implementation。
- Collection date: 2026-09-07 (Asia/Taipei)
- Collector: Codex Planning

## Source Coverage

| Category | Required | Result | Notes |
|---|---|---|---|
| Authorized decisions / requirement | yes | found | Requester 核定 direct-login需求、認證行為與目前 Figma 即正式 UI。 |
| Product rules | yes | found | Android 9+、角色與 auth/navigation responsibility 可用。 |
| Design / assets / accessibility | yes | found | Figma source index、structured context 摘要、tokens、components 與 accessibility 規格已文件化。 |
| API / schema / authentication | no | N/A | UI-001 明確不實作 production API／Token。 |
| Architecture / repository | yes | found | Compose starter、Gradle、MainActivity 與 source-set 現況已檢視。 |
| Tests / logs / runtime evidence | yes | found | 僅有 template tests；無 foundation implementation evidence。 |
| Build / release / operations | yes | missing | Workspace 無 Git metadata，無法形成 branch／commit／CI evidence。 |
| External primary sources | no | N/A | 無需外部規格即可完成本 baseline。 |

## Source Register

| Source ID | Title / Location | Type | Domain / Owner | Version / Date | Authority | Freshness | Scope / AC | Access | Notes |
|---|---|---|---|---|---|---|---|---|---|
| SRC-UI001-001 | Current requester decisions | authorized decision | Product／Design / requester | 2026-09-07 | authoritative | current | all UI-001 AC | available | 核定目前 Figma 即正式 UI，並要求 API 未完成時可直接登入。 |
| SRC-UI001-002 | Figma Copy `HRbRsw6HoNBUCtaieX8xUM`, entry `2905:2680`, indexed child nodes | approved design source | Design / requester | retrieved and approved 2026-09-07 | authoritative | current | AC-UI001-009–010 | available | 先前已取得 structured context；後續內容變更需 revalidate。 |
| SRC-UI001-003 | `docs/design/app-ui-requirements.md` | approved design specification | Design / requester | working tree 2026-09-07 | authoritative | current | visual/component/accessibility | available | 保存 screen、component、state、responsive 與 source index。 |
| SRC-UI001-004 | `docs/product/land-survey-115/` | product specification set | Product / requester | working tree 2026-09-07 | authoritative by domain | current | Android 9+, roles, auth/navigation | available | 角色與介面規格適用；API 細節不屬本 Task。 |
| SRC-UI001-005 | Current Android repository | implementation evidence | Repository | working tree 2026-09-07; commit unavailable | authoritative for current behavior | current | AC-UI001-004, 006–007 | available | Compose starter；無 foundation／session；minSdk 24。 |
| SRC-UI001-006 | `docs/tasks/UI-ROADMAP-0907/` | task sequencing baseline | Process / repository | 2026-09-07 | authoritative for task split | current | UI-001 consumer handoff | available | UI-001 precedes UI-002／UI-003。 |

## Missing or Inaccessible Sources
- Git metadata is unavailable; this blocks implementation traceability but not Knowledge resolution.

## Initial Conflicts and Gaps
- Figma 原先被標示為開發版；requester 最新決策將同一內容核定為正式 UI。
- Shared brand asset 與 feature-only asset ownership 需在 foundation contract 明確切分。

## Collection Limitations
- Repository evidence 只能綁定 2026-09-07 working tree，不能綁定 commit。
- Figma asset URLs 可能失效；Implementation 必須保存 exact bytes 與 node／retrieval evidence。

## Handoff to Resolution
- Coverage sufficient: yes
- Sources requiring authority or freshness resolution: SRC-UI001-002 的歷史 development 標籤須由最新 requester decision supersede。
- Questions Resolution must answer: 正式 design authority、token／asset ownership、後續變更的 revalidation trigger。
- Blocking items: none for Knowledge；Git metadata blocks Implementation。
- Recommended next action: knowledge_resolution
