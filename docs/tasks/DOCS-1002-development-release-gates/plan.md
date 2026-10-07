# Implementation Plan

## Goal

Add scope-aware development/release gates.

## Affected Files

- `AGENTS.md`
- `ai/rules/implementation/developer.md`
- `ai/rules/verification/testing.md`
- `ai/rules/verification/verification.md`
- `ai/rules/operations/release.md`
- `ai/templates/task/state.yaml`

## Steps

1. Split global required gates into development and release scope.
2. Define local-versus-hosted validation evidence.
3. Allow Verification PASS to finish a development Task without entering Release.
4. Reactivate deferred obligations at release-candidate entry.
5. Add canonical state fields and legal values.
6. Validate links, YAML, Markdown, required semantics, and production-code scope.

## Test and Regression

- Parse all Task YAML.
- Check Markdown links/fences and scoped diff.
- Confirm existing lifecycle failure routing and release authorization remain intact.

## Open Questions

- None; the requester explicitly authorized pre-release hosted-CI deferral.
