# Knowledge Collection

## Question and Scope
- Task question: 如何重新整理專案資料夾並同步更新 AGENTS 與所有文件引用？
- In scope: `ai/`、`docs/`、產品規格、AGENTS、Tutorial、所有 Markdown/YAML 路徑引用。
- Out of scope: production code、產品行為、規格語意變更、歷史 Task 內容刪除。
- Collection date: 2026-09-07
- Collector: Codex

## Source Coverage

| Category | Required | Result | Notes |
|---|---|---|---|
| Authorized decisions / requirement | yes | found | 使用者要求重新整理資料夾與 AGENTS |
| Product rules | yes | found | 根目錄產品索引與六份規格 |
| Design / assets / accessibility | yes | found | `docs/design/`、Tutorial images |
| API / schema / authentication | yes | found | `docs/api/` 與產品 API 規格 |
| Architecture / repository | yes | found | `docs/architecture/overview.md` 與完整目錄盤點 |
| Tests / logs / runtime evidence | yes | found | 文件引用搜尋與 YAML／Markdown 驗證方式 |
| Build / release / operations | no | N/A | 文件結構任務；Git metadata 缺失另列環境限制 |
| External primary sources | no | N/A | 不需要外部來源 |

## Source Register

| Source ID | Title / Location | Type | Domain / Owner | Version / Date | Authority | Freshness | Scope / AC | Access | Notes |
|---|---|---|---|---|---|---|---|---|---|
| SRC-001 | `AGENTS.md` | workflow contract | Process | working copy 2026-09-07 | authoritative | current | all | available | 必須同步更新 |
| SRC-002 | `ai/` | rules/templates/tutorial | Process | working copy 2026-09-07 | authoritative | current | rules | available | 目前平鋪過多 |
| SRC-003 | `docs/` | project knowledge/tasks | Project | working copy 2026-09-07 | authoritative/supporting | current | docs | available | 已有 domain folders |
| SRC-004 | `docs/product/land-survey-115/` | product specs | Product | v1 2026-08-25 | authoritative | current | product | available | 原位於 root，已遷移至 canonical product folder |
| SRC-005 | 全專案 path reference search | observed evidence | Repository | 2026-09-07 | observed | current | migration | available | 用於更新引用 |

## Missing or Inaccessible Sources
- Git metadata unavailable；不影響安全搬移，但阻擋 commit、diff 與 CI。

## Initial Conflicts and Gaps
- `ai/` 同時混放 architecture、rules、templates、tutorial 與 images。
- 產品規格位於 root，與既有 `docs/product/` Source of Truth 約定不一致。
- 產品規格中的部分 Notion 匯出連結指向不存在的帶 ID 檔名。

## Collection Limitations
- 無 Git history 可用於搬移追蹤；必須使用檔案清單、數量與引用檢查補強證據。

## Handoff to Resolution
- Coverage sufficient: yes
- Sources requiring authority or freshness resolution: none
- Questions Resolution must answer: canonical folder taxonomy、檔名、相容與驗證策略
- Blocking items: none for local reorganization
- Recommended next action: knowledge_resolution
