# API v0.2 Integration Plan Split

## Status and Source

- Status: Planning candidate; each Integration Task needs its own approved requirement, Knowledge check, plan and Plan Review before code changes.
- Source: `KB-API-001-CONTRACT-V02-R2`, Collection SHA-256 `0178ea57e953cc2094a03d546d2a78b44f7ddfa0984b015c66f624d9f781342f`.
- Boundary: The 12 requests below are the entire active v0.2 Collection. This split allocates ownership, not new API behavior.

| Candidate plan | Collection requests | Existing UI / state touchpoint | Earliest safe outcome |
|---|---|---|---|
| `INT-001-auth-api` | Login `POST /api/auth/login`; Logout `GET /api/auth/logout`; Register `POST /api/auth/register`; Group Options `GET /api/auth/groupOptions` | `session/`, `feature/auth/`, `AppRoot` | Auth/session integration after response, 401 and storage rules are confirmed |
| `INT-002-map-parcel-api` | Map Scope Search `GET /api/map/scopeSearch`; Land List `GET /api/land/list`; Land Filter Options `GET /api/land/filterOptions`; Land Detail `GET /api/land/{keyNo}` | `feature/mapshell/`, `feature/parcelsearch/`, `feature/parceldetail/` | Map/land read paths under the observed v0.2 scope |
| `INT-003-survey-photo-api` | Create Survey `POST /api/survey`; Update Survey `PUT /api/survey/{surveyId}`; Upload Photo `POST /api/survey/photo`; Survey Options `GET /api/survey/options/{optionType}` | `feature/surveyform/`, `feature/photocamera/`, relevant parcel edit state | Survey/photo integration after upload and error contracts can be tested |
| `INT-004-return-notification-api` | None in the active v0.2 Collection | `feature/notifications/`, returned parcel correction UI | Contract acquisition and Knowledge work only until notification/return endpoints are approved |

## Candidate Plan INT-001: Auth and Session

- Scope: Replace the auth fake data boundary with approved login/register/group-options calls; connect login/logout to session state and handle invalidated Token and 401.
- Dependencies: Login/register/group-options response schemas, Group Options empty/error behavior, complete 401 semantics, Token storage/security decision, test environment and test identities. Preserve `role` values `內業`/`外業`, no CAPTCHA, GET logout and the declared no-expiry/re-login invalidation rules.
- Proposed sequence: confirm schemas and 401 matrix; design transport and session ownership; implement the feature-owned auth port; wire UI loading/error/retry; test logout and re-login invalidation with server evidence.
- Validation direction: request serialization, response/error mapping, session transitions, duplicate requests, auth persistence/security, and Android 9+ lifecycle behavior. Mock tests alone cannot prove backend invalidation.
- Start gate: approved INT-001 requirement and plan, usable auth response/error evidence, and a secure test path. Do not infer missing Token fields or storage format.

## Candidate Plan INT-002: Map and Parcel Reads

- Scope: Replace fake map/parcel list/detail/options data with the four Collection read requests; preserve the UI's current loading, empty and error states.
- Dependencies: Confirmed authentication requirement per route, formal/representative GeoJSON and land response shapes, filter encoding, pagination behavior, and map rendering/library decision when needed. Use `page`/`perPage` requests and Int `total`/`total_page` responses as declared.
- Proposed sequence: map UI data ports to the four routes; confirm repeatable filters and WGS84 coordinate handling; implement parsing/transport; wire search, scope and detail states; test paging, filter combinations and empty/error results.
- Validation direction: URL encoding, repeated filters, coordinate order, pagination, stale request cancellation, loading/error recovery and regression against UI-003～UI-005.
- Exclusion: No history-period API is present in v0.2. History data integration needs a new contract and Knowledge pass; a UI tab does not establish its endpoint.
- Start gate: approved INT-002 requirement and plan, sufficient GeoJSON/land schema and access evidence for its selected sub-scope, plus usable test environment.

## Candidate Plan INT-003: Survey and Photo

- Scope: Connect the survey form and camera/photo flow to Create Survey, Update Survey, Upload Photo and Survey Options, including correction through the existing PUT route.
- Dependencies: Complete Update Survey response, Upload Photo 409/422 payloads, reproducible local file binding in place of the Postman cloud attachment, photo ownership rules, upload/submit sequence, and explicit `site_condtion` options path versus `site_condition` payload-field interpretation.
- Proposed sequence: resolve wire-name and missing-schema questions; make upload runnable from a clean checkout; define photo URI/file lifecycle and request ownership; implement options/upload/create/update through feature-owned ports; map 409 versus 422 and retry behavior.
- Validation direction: 4:3 photo request handling, minimum four `img_ids` per `occupy` item and four top-level `img_ids`, create/update serialization, ownership conflict 409, other validation 422, interrupted upload and duplicate-submit behavior.
- Exclusion: Do not add `resubmit` or an unlisted correction endpoint. Do not infer server response fields from UI fake state.
- Start gate: approved INT-003 requirement and plan, resolved wire/response/file gaps, and an executable contract-test approach. Run the actual service tests during INT-003 validation before its Verification.

## Candidate Plan INT-004: Return and Notification

- Scope available now: identify the notification list, read-state, return reason/status and correction synchronization contract required by the existing UI; collect and resolve authoritative sources before implementation planning.
- Dependencies: New approved endpoint inventory with method/path/request/response/error/auth rules and state transitions. The v0.2 Collection supplies only Update Survey for the correction write; it supplies no notification or read-state API.
- Proposed sequence: API owner publishes the missing contract; run Knowledge Collection/Resolution/Validation; set observable INT-004 acceptance criteria; then draft technical design, tests and Plan Review.
- Validation direction after contract approval: returned item visibility, read-state consistency, correction PUT interaction, status refresh, conflict/error handling and cross-device/server reconciliation.
- Start gate: **blocked for API implementation** until the missing contract and its Knowledge baseline are approved. Fake UI behavior is not contract evidence.

## Ordering and Cross-Plan Gates

1. Plan Review for this API-001 document task confirms the split; it does not approve any INT implementation.
2. INT-001 is the first integration candidate because downstream calls may need a valid session. Each downstream plan must confirm its own auth requirement from the API contract.
3. INT-002 and INT-003 may be planned independently after their own source gaps are resolved. Implementation ordering is decided by their approved plans and the available environment; neither plan should silently absorb the other.
4. INT-004 begins with contract acquisition. It cannot be scheduled as an API implementation slice against the current Collection.
5. On any newer Collection or backend contract change, revalidate the affected Knowledge baseline and update each impacted plan before implementation.

## Deferred Gates

| Gate | Reason | Owner | Reactivate at |
|---|---|---|---|
| Response/error schemas and per-route permission evidence | Current Collection omits responses or complete error/authorization semantics for affected calls; DTO and behavior decisions must cite authority | API owner | Before affected INT-001～INT-003 Plan Review; if absent, return to Knowledge instead of approving implementation |
| Upload Photo local file binding and `site_condtion`/`site_condition` decision | Cloud attachment and wire-name ambiguity prevent a reproducible, approved INT-003 request contract | API / integration owner | Before INT-003 Plan Review |
| Notification/history endpoint contracts | No corresponding v0.2 request exists | API owner | Before INT-004 Knowledge Validation or INT-002 history-scope Knowledge Validation |
| Executable runtime contract tests | Artifact lint is not evidence of server response and error behavior; execution environment is not yet supplied | Integration owner | During affected INT-001～INT-003 Developer Validation, before each Task's Verification; INT-004 requires its own test plan after its contract exists |
| Hosted CI, integration tests and UAT for release | API-001 has development scope and no deployable artifact | Release/project maintainer | Release candidate |
