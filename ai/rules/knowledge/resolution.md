# Knowledge Resolution

## Purpose

Normalize collected claims and resolve authority, conflicts, gaps, decisions, and safe assumptions into a candidate Knowledge baseline.

## Entry

- `next_action: knowledge_resolution`
- Collection is complete enough to evaluate material claims.

## Load

- current `knowledge-collection.md`
- current `knowledge-resolution.md`, only if not superseded
- exact cited primary sources needed for disputed claims
- requirement or affected AC

Do not read unrelated sources or historical baselines by default.

## Output

Create/update `knowledge-resolution.md` from `ai/templates/knowledge/resolution.md` with:

- candidate baseline ID and status
- material claims and traceability
- conflicts/gaps and decision owners
- decision records and safe assumptions
- blockers, downstream impact, and revalidation triggers
- Validation handoff

## Decision Rules

- Authority is determined by domain and scope, not recency alone.
- A design does not redefine API/product behavior; current code does not redefine intent.
- Assumptions must be reversible, low risk, explicit, and time-bounded.
- Security, privacy, data, public contract, permission, or product behavior uncertainty cannot be assumed.
- Superseded evidence moves to Task `history/`; the active file contains only the current candidate.

## Exit

- Candidate complete: route to `knowledge_validation`.
- Missing/stale evidence: `knowledge_collection`.
- Unresolved authoritative conflict: remain in Resolution or `requirement_clarification`.
- Tool/access failure: `infrastructure`.

## Restrictions

No production code, requirement changes, or silent conflict choices.
