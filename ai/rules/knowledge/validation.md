# Knowledge Validation

## Purpose

Check that a candidate baseline is authoritative, current, traceable, complete, internally consistent, and safe for Planning.

## Entry

- `next_action: knowledge_validation`
- Candidate baseline and Resolution handoff exist.

## Load

- current Collection and Resolution artifacts
- candidate baseline
- only the primary evidence cited by material claims

## Output

Create/update `knowledge-validation.md` from `ai/templates/knowledge/validation.md`. Record checks, findings, baseline decision, blockers, Planning handoff, and independence limitation when applicable.

## Checks

- source coverage, identity, authority, version, freshness
- claim/source and requirement/AC traceability
- conflict resolution and approval
- assumption safety and expiry
- baseline completeness and downstream/revalidation impact

Validation must reproduce evidence; it cannot merely restate Resolution.

## Exit

- PASS: activate baseline, set Knowledge Ready, route to `planning`.
- Collection defect: `knowledge_collection`.
- Claim/decision defect: `knowledge_resolution`.
- Missing authorized decision: `requirement_clarification`.
- Evidence access failure: `infrastructure`.

`NOT VERIFIED` is not PASS.

## Restrictions

No production code or candidate-baseline repair during Validation.
