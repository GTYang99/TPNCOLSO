# Investigation

## Purpose

Collect enough evidence to classify an unknown failure without implementing a speculative fix.

## Entry and Load

- `next_action: investigation`
- issue, observed behavior, environment/revision, and available evidence

Inspect only the smallest relevant logs, tests, code, configuration, and sources.

## Output

Record hypotheses, checks, evidence, eliminated causes, final classification/confidence, affected AC, and destination.

## Exit

- requirement/planning/implementation/environment cause: matching canonical route.
- insufficient evidence: remain blocked and name the smallest missing evidence or owner action.

## Restrictions

No production-code change, requirement invention, or issue closure without verification.
