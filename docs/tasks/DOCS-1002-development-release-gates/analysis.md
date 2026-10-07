# Analysis

## Current

- AGENTS requires CI PASS before every Verification/Release path.
- Verification always routes PASS to Release.
- State has no canonical completion scope or deferred-obligation model.

## Target

- Development scope ends at task Verification PASS and `development_complete`.
- Release scope reactivates all deferred release-candidate obligations.
- Deferred is an explicit state with reason, owner, and milestone; it is not PASS.

## Risk Control

- Product failures from any available CI remain blocking.
- Local checks cannot be labeled hosted CI.
- Release and merge authorization remain separate human-controlled gates.
