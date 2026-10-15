# Knowledge Resolution

## Question and Scope
- Task question: 以核定的 Postman Collection 建立 API v0.2 的 candidate Knowledge baseline，並處理舊 API 文件衝突。
- In scope: Collection-defined endpoint inventory、request/response examples、auth/token、filters、survey/photo、error rules 與 exclusions。
- Out of scope: 後端實作、Android integration、真實 request 執行、WMTS 與未列入 Collection 的 API。

## Candidate Knowledge Baseline
- Baseline ID: `KB-API-001-CONTRACT-V02-R2`
- Created date: 2026-10-14 (Asia/Taipei)
- Supersedes: None; this is the first API v0.2 baseline artifact in the repository.
- Status: validated

## Source Register

| Source ID | Title / Location | Type | Domain / Owner | Version / Date | Authority | Freshness | Scope / AC | Notes |
|---|---|---|---|---|---|---|---|---|
| SRC-API001-001 | `docs/api/postman/tp-ncolso-api-v0.2.postman_collection.json` | API contract | API / requester + engineering | v0.2; SHA-256 `0178ea57e953cc2094a03d546d2a78b44f7ddfa0984b015c66f624d9f781342f`; 2026-10-15 | authoritative | current | AC-API001-001～005 | Sole contract authority for v0.2 by requester decision. |
| SRC-API001-005 | Current requester message in this task | API contract change | API / requester | 2026-10-15 | authoritative for login update | current | INT-001 login request/response | The update requires `platform=app` and shows `data.user_id`; credential and token literals are excluded from repository evidence. |
| SRC-API001-002 | `docs/product/land-survey-115/5 API規格.md` | product API specification | Product / API owner | current repository source | supporting | current but conflicting | context only | Not used to override the v0.2 Collection. |
| SRC-API001-003 | `/Users/a10362/Desktop/markdown file/TP/tp_req_api.md` | requester requirements | Product / requester | reviewed 2026-10-14 | supporting historical | stale/conflicting for v0.2 | OQ history | Postman Collection supersedes conflicting v0.2 contract claims. |
| SRC-API001-004 | Current requester decisions in this thread | authorized decision record | Product / requester | 2026-10-14 | authoritative for conflict resolution | current | OQ-017, OQ-019～OQ-034 | Establishes Postman authority and final API decisions. |

## Material Claims and Traceability

| Claim ID | Statement | Supporting Sources | Contradicting Sources | Authority / Confidence | Requirement / AC | Status |
|---|---|---|---|---|---|---|
| KCL-API001-001 | v0.2 contract consists of exactly 12 requests in AUTH, MAP_AND_LAND and SURVEY_AND_PHOTO. | SRC-API001-001 | None | Collection / high | AC-API001-001, 002 | resolved |
| KCL-API001-002 | Login and register are JSON POST requests; login requires `platform`, `account` and `password` with current platform value `app`; login has no CAPTCHA; the observed success response contains `token`, `user_id`, `name`, `group_id`, `company` and `role`; register requires `role` with `內業` or `外業`; logout is GET. | SRC-API001-001, SRC-API001-004, SRC-API001-005 | SRC-API001-002, 003 for legacy differences | requester + Collection / high | AC-API001-003, 004; INT-001 | resolved |
| KCL-API001-003 | Token has no expiry limit; re-login immediately invalidates the old Token; logout invalidates the current Token. | SRC-API001-001, 004 | None in active v0.2 source | requester + Collection / high | AC-API001-003 | resolved |
| KCL-API001-004 | Map scope search returns a GeoJSON example under `data.geojson`; coordinates are WGS84-style longitude/latitude pairs; full formal schema remains a follow-up. | SRC-API001-001, 004 | None | Collection example / high for observed shape | AC-API001-003 | resolved with documented gap |
| KCL-API001-005 | Land list pagination uses caller-supplied `page` and `perPage`; response pagination uses Int `total` and `total_page` with `item`. | SRC-API001-001, 004 | Older wording in SRC-API001-002 | requester + Collection / high | AC-API001-003 | resolved |
| KCL-API001-006 | Map filters are repeatable/stackable: key word may be scalar; county/dist/status/user/batch are arrays; survey date uses start/end datetime; GIS occupy flag is boolean. | SRC-API001-001, 004 | None | requester + Collection / high | AC-API001-003 | resolved |
| KCL-API001-007 | Create and update survey use `occupy` arrays; each occupy item has at least four `img_ids`, and the survey top level has four `img_ids`; update is PUT without `resubmit`. | SRC-API001-001, 004 | SRC-API001-002, 003 for `resubmit` | requester + Collection / high | AC-API001-003, 004 | resolved |
| KCL-API001-008 | Upload photo uses horizontal 4:3 images; ownership/claim conflicts return 409; other validation/format errors return 422. | SRC-API001-001, 004 | None | requester + Collection / high | AC-API001-003 | resolved |
| KCL-API001-009 | Survey option type is limited to `note` or `site_condtion` in the active Collection. | SRC-API001-001, 004 | Older `/code/{code_type}` source | requester + Collection / high | AC-API001-003, 004 | resolved |
| KCL-API001-010 | History, approve, reject, batch, export, import and dashboard endpoints are excluded from v0.2 Collection until their contracts are complete. | SRC-API001-001, 004 | SRC-API001-002 lists older/PENDING intent | requester + Collection / high | AC-API001-002, 004 | resolved |

## Conflicts and Gaps

| Conflict ID | Claims / Sources | Domain | Impact / Severity | Options | Decision Owner | Status |
|---|---|---|---|---|---|---|
| KCF-API001-001 | Postman v0.2 versus product API spec for paths, logout method, `resubmit`, code API and pending endpoints. | API contract | P1; wrong source would generate incorrect client contract. | Use Postman; use product spec; merge silently. | Requester | resolved by KD-API001-001 |
| KCF-API001-002 | Postman v0.2 versus older `tp_req_api.md` for CAPTCHA and excluded APIs. | API contract | P1; affects auth and integration scope. | Use Postman; retain legacy requirements. | Requester | resolved by KD-API001-001 |
| KCF-API001-003 | Update Survey has no saved response example; full GeoJSON/error schemas are incomplete. | API schema | P2; limits formal client DTO/error completeness. | Invent schema; mark follow-up; block all baseline work. | API owner | resolved as non-blocking follow-up by KD-API001-005 |

## Decision Records

### KD-API001-001

- Decision: `docs/api/postman/tp-ncolso-api-v0.2.postman_collection.json` is the sole authoritative API contract source for v0.2. Conflicting legacy documents are supporting/history only and must not override it.
- Alternatives: Keep product API spec authoritative; merge conflicting fields; block baseline until every legacy document is rewritten.
- Source / Conflict IDs: SRC-API001-001, SRC-API001-002, SRC-API001-003, SRC-API001-004; KCF-API001-001, KCF-API001-002.
- Rationale: The requester explicitly directed the agent to use the Postman Collection, and the Collection contains the currently resolved API scope and examples.
- Approver and date: Requester, 2026-10-14.
- Affected requirements / AC / artifacts: API-001 requirement, all INT-001～INT-004 integration planning inputs.
- Supersedes: Legacy v0.2 interpretations in `tp_req_api.md` and the conflicting portions of `5 API規格.md`.
- Revalidation trigger: requester publishes a newer Postman Collection or explicitly changes API source authority.

### KD-API001-002

- Decision: Use the Collection's auth contract: no CAPTCHA; `role` required as `內業` or `外業`; GET logout; Token no-expiry statement with immediate invalidation on re-login/logout.
- Alternatives: Retain CAPTCHA; use POST logout; infer token expiry.
- Source / Conflict IDs: SRC-API001-001, SRC-API001-004; KCL-API001-002, KCL-API001-003.
- Rationale: These values are explicitly resolved and represented in the active Collection.
- Approver and date: Requester, 2026-10-14.
- Affected requirements / AC / artifacts: INT-001 auth integration planning.

### KD-API001-006

- Decision: Update the v0.2 Login request to require `platform`, `account`, and `password`; use `app` as the current platform value. Record the observed success response field `data.user_id` alongside `token`, `name`, `group_id`, `company`, and `role`.
- Alternatives: Keep the previous two-field request; infer a different platform value; omit `user_id` because it was absent from the previous example.
- Source / Conflict IDs: SRC-API001-001, SRC-API001-005.
- Rationale: The requester supplied the newer login contract directly; the Postman Collection is the active API authority.
- Approver and date: Requester, 2026-10-15.
- Affected requirements / AC / artifacts: INT-001 login serialization, response mapping, and session integration planning.
- Supersedes: The prior login example in `KB-API-001-CONTRACT-V02-R1`.
- Revalidation trigger: A newer requester/API-owner login schema or backend response example.
- Supersedes: Older auth contract variants.
- Revalidation trigger: auth Postman request or backend auth contract changes.

### KD-API001-003

- Decision: Use Collection land/map paths, repeatable filter encoding, GeoJSON example, and `total`/`total_page` pagination metadata exactly as represented; do not invent omitted schema.
- Alternatives: Use `/parcel` paths from the older product spec; flatten arrays; infer a complete GeoJSON DTO.
- Source / Conflict IDs: SRC-API001-001, SRC-API001-004; KCL-API001-004～006.
- Rationale: Requester resolved the parameter types and Postman preserves the wire representation.
- Approver and date: Requester, 2026-10-14.
- Affected requirements / AC / artifacts: INT-002 map/parcel integration planning.
- Supersedes: Conflicting legacy path/filter interpretations.
- Revalidation trigger: updated Collection or formal backend schema.

### KD-API001-004

- Decision: Use `occupy` arrays, minimum four photos per occupy item and four survey-level photos; correction submission remains PUT without `resubmit`; use 409 only for photo ownership/claim conflict and 422 for other validation errors.
- Alternatives: Four photos only at survey level; add resubmit; use one generic error status.
- Source / Conflict IDs: SRC-API001-001, SRC-API001-004; KCL-API001-007, KCL-API001-008.
- Rationale: These are explicit requester resolutions and are represented in request examples/descriptions.
- Approver and date: Requester, 2026-10-14.
- Affected requirements / AC / artifacts: INT-003 survey/photo integration planning; UI-005/UI-006 handoff.
- Supersedes: Legacy `resubmit` and single-level photo interpretations.
- Revalidation trigger: survey/photo request examples or server validation contract changes.

### KD-API001-005

- Decision: Missing formal schemas and missing Update Survey response example are recorded as non-blocking follow-up gaps. The active baseline is limited to what the Collection observes and declares; no DTO or error field is inferred.
- Alternatives: Invent fields; block activation of the entire baseline; omit the gap.
- Source / Conflict IDs: SRC-API001-001; KCF-API001-003.
- Rationale: The user requested baseline activation from the Collection while allowing later API contract supplementation.
- Approver and date: Requester decision to defer API additions, 2026-10-14.
- Affected requirements / AC / artifacts: INT-001～INT-004 planning and later API validation.
- Supersedes: None.
- Revalidation trigger: backend schema supplement or newer Collection response examples.

## Assumptions

| Assumption ID | Statement / Evidence | Confidence | Impact if Wrong | Owner | Validation Method | Expiry / Trigger | Safe for Planning |
|---|---|---|---|---|---|---|---|
| KA-API001-001 | The Collection response examples document observed representative envelopes, but fields not present in an example are unknown rather than optional. | high | Client DTO and error mapping may need revision. | API owner | Compare against backend schema or updated Collection. | API schema supplement | yes |
| KA-API001-002 | No real request execution is required to activate this artifact-level Knowledge baseline. | high | Environment/API reachability must be added before integration verification. | Integration owner | Execute contract tests when environment is available. | INT implementation/verification | yes |

## Resolved Items
- Postman Collection is the sole v0.2 API authority.
- v0.2 contains 12 requests in three active folders.
- CAPTCHA is removed; login `platform=app` and `data.user_id` are part of the updated observed contract; role is required; auth Token lifecycle is defined by the Collection.
- Map/land paths, repeatable filters, GeoJSON example and pagination metadata follow the Collection.
- Survey/photo structure, minimum four photos, PUT correction flow and 409/422 split follow the Collection and requester resolutions.
- PENDING/unfinished API endpoints are excluded from v0.2.

## Unresolved Non-blocking Items
- Complete formal GeoJSON schema, Update Survey response schema and exhaustive error payloads require later backend/Collection supplementation.
- Real environment execution and integration tests remain downstream work.

## Blocking Items
- None for Knowledge Validation or activation of the Collection-defined v0.2 baseline.

## Downstream Impact
- Requirements / AC: INT-001～INT-004 must cite `KB-API-001-CONTRACT-V02-R2` and must not resurrect excluded endpoints or legacy `resubmit`/CAPTCHA behavior.
- Plan / design: Integration plans must identify known response gaps and avoid speculative DTO fields.
- Code / data / API: No production code is changed by this Knowledge task; API clients must use the active Collection paths and request shapes.
- Tests / verification: Add authenticated contract tests, schema tests and error mapping once a test environment and complete responses are available.
- Release / operations: HTTPS and production base URL remain deployment configuration work; current Collection base URL is internal HTTP test only.

## Revalidation Triggers
- A newer Postman Collection or explicit requester source-authority change.
- Backend supplies formal response/error schemas or changes status, field, path, role, token, photo or filter behavior.
- Internal HTTP test base URL is replaced by the production HTTPS contract.
- New API endpoints leave the currently excluded/PENDING state.

## Validation Handoff
- Candidate baseline: `KB-API-001-CONTRACT-V02-R2`
- Decisions and constraints Validation must check: KD-API001-001～006; exact Collection checksum; 12-request inventory; login platform and `user_id`; no CAPTCHA/PENDING requests; no `resubmit`; photo/error/filter rules.
- Safe assumptions: KA-API001-001～002.
- Required follow-up evidence: backend formal schemas, Update Survey response, exhaustive error payloads and real contract-test execution.
- Material blockers: no.
- Ready for Validation: yes.
