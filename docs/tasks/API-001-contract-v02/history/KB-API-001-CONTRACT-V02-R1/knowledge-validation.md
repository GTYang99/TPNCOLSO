# Knowledge Validation

## Inputs
- Collection artifact: `docs/tasks/API-001-contract-v02/knowledge-collection.md`
- Resolution artifact: `docs/tasks/API-001-contract-v02/knowledge-resolution.md`
- Reproducible evidence: `docs/tasks/API-001-contract-v02/evidence/api-v02-validation.txt`
- Candidate baseline ID: `KB-API-001-CONTRACT-V02-R1`
- Validator: Codex (same agent; no independent validator available)
- Validation date: 2026-10-14 (Asia/Taipei)
- Independence note: Validation reproduced the local/external Collection checksum, JSON parse, request inventory, request-field checks and Postman CLI result. The evidence is bound to the current uncommitted working tree; no production code was changed.

## Validation Checklist

| Check | Result | Evidence | Finding / Route |
|---|---|---|---|
| Collection scope and source coverage | PASS | Collection lists AUTH, MAP_AND_LAND and SURVEY_AND_PHOTO with 4 requests each, 12 total; Collection and Resolution cover auth, map/land, survey/photo and explicit exclusions. | No Collection blocker. |
| Source identity and version | PASS | `evidence/api-v02-validation.txt` records `info.name = TP NCOLSO API v0.2` and equal local/source SHA-256 `5f5bd988f36d564743da9180550485ae2f30c5a23f8d10eb8e124c190de8db52`. | Revalidate on a newer Collection. |
| Domain authority | PASS | Current requester decision explicitly assigns API v0.2 authority to the Postman Collection; legacy product/requirements documents are recorded as supporting/conflicting sources. | KD-API001-001 resolves source authority. |
| Freshness and supersession | PASS | Local and external files have identical SHA-256 on 2026-10-14. Collection request names contain no CAPTCHA or `PENDING_CONTRACT` item; legacy conflicting claims are marked overridden for v0.2. | Revalidate on source update. |
| Claim-to-source traceability | PASS | KCL-API001-001～010 each cite the Collection and/or current requester decisions; the evidence reproduces the 12-request inventory, method set, response-example count, forbidden-item check and request-field check. | No traceability gap for the active scope. |
| Requirement / AC traceability | PASS | AC-API001-001～005 are implemented by the source checksum/inventory, Resolution decisions, exclusion record and local validation evidence. | No requirement gap for baseline activation. |
| Conflict resolution and approval | PASS | KCF-API001-001～003 are resolved by KD-API001-001～005; no silent merge was used. | Legacy documents remain supporting/history only. |
| Assumption safety and expiry | PASS | KA-API001-001～002 do not infer hidden schema and explicitly route real execution/schema work to integration follow-up. | Safe for Planning, not proof of production integration. |
| Baseline completeness and consistency | PASS | For the explicitly bounded Collection-defined scope, the baseline defines 12 requests, request shapes, observed response envelopes, auth/token rules, filters, photo/error rules, exclusions, gaps and revalidation triggers. | Group Options and Update Survey have no saved response examples; Upload Photo has no saved 409/422 examples and uses a cloud attachment. These are recorded as bounded follow-up gaps; no hidden schema is inferred. |
| Downstream impact and revalidation triggers | PASS | Resolution maps the baseline to INT-001～INT-004 and lists backend schema, HTTPS and endpoint expansion triggers. | Planning handoff is complete. |
| Planning handoff readiness | PASS | State is routed to `planning` with `next_action: planning`; the Collection-defined baseline is ready for integration planning with the listed artifact limitations. | Planning must preserve the Upload Photo attachment limitation and the `site_condtion` wire name until resolved. |

## Findings

| Finding ID | Category | Severity | Evidence | Impact | Owner | Route | Status |
|---|---|---|---|---|---|---|---|
| KV-API001-001 | collection | P2 | `Group Options` and `Update Survey` have zero saved responses; `Upload Photo` saves only 200 while its description declares 409/422; formal GeoJSON/error schemas are also deferred. | Blocks complete formal DTO/error implementation only; does not invalidate the Collection-defined v0.2 baseline. | API owner | planning / knowledge_collection | open; non-blocking |
| KV-API001-002 | collection | P2 | Upload Photo uses `postman-cloud://...` at Collection line 1501, while `photoFilePath` is declared but not referenced. | The contract shape is observable, but a clean checkout cannot independently reproduce the file upload without the external attachment; resolve before executable integration testing. | API / integration owner | knowledge_collection / infrastructure | open; non-blocking for artifact baseline |
| KV-API001-003 | resolution | P2 | The options route declares `site_condtion`, while survey payloads use `site_condition`; the Collection is authoritative but does not explain the distinction. | Integration may use the wrong wire name if the distinction is normalized without approval. | API owner | knowledge_resolution | open; non-blocking |
| KV-API001-004 | environment | P2 | No real network request was executed; local checks validate artifact integrity, source parity and lint only. | Integration verification remains downstream. | Integration owner | infrastructure / verification | open; non-blocking |

## Baseline Decision
- Result: PASS
- Active baseline ID: `KB-API-001-CONTRACT-V02-R1`
- Supersedes: None
- Activation or rejection reason: The requester-approved Postman Collection is identified, equal to the source by checksum, JSON-valid, inventory-checked and lint-clean with zero reported errors/warnings; all material legacy conflicts are explicitly resolved; the response-example, attachment, naming and runtime gaps are bounded, documented and non-blocking for this Collection-defined baseline. This PASS does not claim clean-checkout Upload Photo execution or complete backend schemas.

## Blocking Items
- None for Knowledge Validation or baseline activation within the Collection-defined scope.
- Before executable integration testing, resolve or explicitly accept the Upload Photo cloud attachment dependency and the `site_condtion` / `site_condition` wire-name distinction.

## Planning Handoff
- Ready for Planning: yes
- Decisions and constraints to cite: KD-API001-001～005; 12-request inventory; no CAPTCHA; role required; Token lifecycle; GET logout; Collection paths and filters; four-photo minimum; PUT without `resubmit`; 409/422 split; excluded PENDING APIs.
- Required follow-up evidence: Group Options response, Update Survey response, Upload Photo 409/422 payloads, formal backend schemas, a reproducible local Upload Photo file binding, explicit option wire-name decision, HTTPS production base URL and real contract-test execution.
- Revalidation triggers: newer Collection, explicit source-authority change, backend contract changes, or activation of excluded APIs.
- Next action: planning
