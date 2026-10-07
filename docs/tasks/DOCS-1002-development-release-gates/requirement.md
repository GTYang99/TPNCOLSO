# Requirement

## Goal

Separate development-task completion from release completion so hosted CI may be explicitly deferred during pre-release development without being misreported as PASS.

## Acceptance Criteria

- AC-001: AGENTS defines separate development and release gates.
- AC-002: Development Tasks may complete after approved requirements/plan, developer validation, commit, code review, and Verification of task-scoped AC.
- AC-003: Local evidence identifies revision, environment, commands, and results and is never reported as hosted CI PASS.
- AC-004: Existing CI failures remain recorded; product-defect failures block completion until resolved.
- AC-005: Development Verification PASS does not authorize release, merge, signing, publishing, or deployment.
- AC-006: Release reactivates required CI, integration, UAT, approval, and deployment gates against the release candidate.
- AC-007: State template defines completion scope/status and legal deferred/not-applicable gate values with owner and reactivation milestone.

## Constraints

- Preserve current Task artifacts and unrelated working-tree changes.
- Do not modify production code.
