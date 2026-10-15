# Knowledge Collection

## Question and Scope
- Task question: 是否可依核定的 Postman Collection 建立並啟用 API v0.2 Knowledge baseline？
- In scope: API v0.2 的可用 endpoint inventory、request/response examples、認證、參數、照片與錯誤規則，以及明確排除的 API。
- Out of scope: 後端實作、Android API client、真實 request 執行、部署與未完成 API 的推測合約。
- Collection date: 2026-10-14 (Asia/Taipei)
- Collector: Codex Knowledge Collection and Resolution Agent

## Source Coverage

| Category | Required | Result | Notes |
|---|---|---|---|
| Authorized decisions / requirement | yes | found | 本次 requester decision 指定以 Postman Collection 作為 API v0.2 唯一依據，並已確認 OQ-017、OQ-019～OQ-034。 |
| Product rules | yes | found | `docs/product/land-survey-115/5 API規格.md` 提供背景與原始 API 意圖；與 v0.2 Collection 的差異交由 Resolution 處理。 |
| Design / assets / accessibility | no | N/A | API contract baseline 不包含 UI 視覺或 Figma 尺寸。 |
| API / schema / authentication | yes | found | Postman Collection 直接提供 12 支 request、body、query、可見 response examples 與描述。 |
| Architecture / repository | yes | found | `AGENTS.md`、`docs/api/` 與 integration queue 定義 API baseline 與後續 integration 邊界。 |
| Tests / logs / runtime evidence | yes | found | `jq empty` 與 Postman CLI lint 可在本地重現；未執行真實網路 request。 |
| Build / release / operations | no | N/A | 本 Task 只建立 Knowledge baseline，不進入 build、release 或部署。 |
| External primary sources | no | N/A | v0.2 以 requester 提供並同步至專案的 Postman Collection 為主要 API 來源。 |

## Source Register

| Source ID | Title / Location | Type | Domain / Owner | Version / Date | Authority | Freshness | Scope / AC | Access | Notes |
|---|---|---|---|---|---|---|---|---|---|
| SRC-API001-001 | `docs/api/postman/tp-ncolso-api-v0.2.postman_collection.json` | API contract / Postman Collection | API / requester + engineering | `TP NCOLSO API v0.2`; SHA-256 `5f5bd988f36d564743da9180550485ae2f30c5a23f8d10eb8e124c190de8db52`; 2026-10-14 | authoritative by explicit requester decision | current | AC-API001-001～005 | available | 同步自 `/Volumes/Elements/下載項目_WD/TP NCOLSO API v0.2.postman_collection.json`；包含 AUTH、MAP_AND_LAND、SURVEY_AND_PHOTO，共 12 requests。 |
| SRC-API001-002 | `docs/product/land-survey-115/5 API規格.md` | product API specification | Product / API owner | working tree; date not declared | supporting; not authoritative for v0.2 conflicts | current repository source | background only | available | Contains older paths, POST logout, `resubmit`, code and PENDING endpoint intent; conflicts are recorded and resolved in Resolution. |
| SRC-API001-003 | `/Users/a10362/Desktop/markdown file/TP/tp_req_api.md` | requester API requirements | Product / requester | 2026-10-14 review | supporting historical source; overridden for v0.2 contract | stale/conflicting for v0.2 | OQ-017～OQ-034 | available | Still contains older CAPTCHA / `resubmit` / pending material; requester explicitly selected Postman Collection as source of truth. |
| SRC-API001-004 | `docs/tasks/README.md` deferred integration queue | task boundary | Process / repository | current working tree | authoritative for downstream task split | current | INT-001～INT-004 handoff | available | API baseline precedes integration tasks; it does not implement them. |

## Missing or Inaccessible Sources
- No missing source blocks the v0.2 baseline within the Collection-defined scope.
- No backend environment or credentials were required or stored.

## Initial Conflicts and Gaps
- Product API specification and the older `tp_req_api.md` do not fully match the requester-approved Postman Collection. The conflict includes CAPTCHA, logout method, endpoint naming, `resubmit`, and PENDING APIs.
- The Collection has no formal response example for `Update Survey`, and the full GeoJSON schema beyond the captured example is not separately formalized.
- Real network execution was not performed; local validation proves artifact integrity and lint only.

## Collection Limitations
- The Collection is the contract baseline, but an example is not a substitute for a complete backend schema. Unshown nullability, error payloads, auth middleware behavior and server-side validation remain follow-up work.
- No secrets, credentials, production payloads or unrestricted personal data are stored in this task.

## Handoff to Resolution
- Coverage sufficient: yes
- Sources requiring authority or freshness resolution: SRC-API001-001 versus SRC-API001-002/SRC-API001-003.
- Questions Resolution must answer: Which source governs v0.2? Which legacy endpoints are excluded? Which confirmed OQ decisions must be encoded in the active contract?
- Blocking items: none for the Collection-defined baseline.
- Recommended next action: knowledge_resolution
