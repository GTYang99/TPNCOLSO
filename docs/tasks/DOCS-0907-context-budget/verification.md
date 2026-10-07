# Verification

## Evidence

- Markdown local-link check: PASS
- YAML parse check: PASS
- Markdown fence check: PASS
- conflict-marker check: PASS
- scoped diff whitespace check: PASS
- production-code scope check: PASS; no `app/` changes introduced

## Acceptance Criteria

| AC | Result | Evidence |
|---|---|---|
| AC-001 | PASS | AGENTS is 175 lines and 7,061 bytes |
| AC-002 | PASS | all Rules total 18,915 bytes |
| AC-003 | PASS | Minimal Context Loading forbids recursive preload |
| AC-004 | PASS | `ai/README.md` maps each normal action to one primary Rule |
| AC-005 | PASS | Rules use phase contracts; state schema remains in template |
| AC-006 | PASS | Tutorial is explicitly optional/on-demand |
| AC-007 | PASS | all checked Markdown local targets resolve |
| AC-008 | PASS | Task changed no `app/` or UI-001/UI-002 artifact |
| AC-009 | PASS | AGENTS limits screenshots to visible UI, requires `visual_observation` classification and explicit unknowns, and forbids hidden-behavior inference |
| AC-010 | PASS | Runtime policy contains the exact Android Studio JBR path and is linked by the router, Developer, Testing, and Infrastructure |
| AC-011 | PASS | Testing policy defines Emulator as default, requires ADB target evidence, and limits mandatory physical-device tests to explicit hardware/device-risk AC |

## Overall

- Local documentation verification: PASS
- Commit/CI verification: NOT VERIFIED because the current branch has unrelated in-progress changes and this Task was not committed
- Next action: infrastructure or an isolated docs worktree/branch before commit
