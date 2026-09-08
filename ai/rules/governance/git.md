# Git Policy

Read this policy only for branch, commit, diff, review, or revision evidence.

## Before Work

- Confirm repository root, current branch, status, and unrelated changes.
- Use a task branch unless explicitly directed otherwise.
- Do not overwrite, stage, revert, or commit unrelated user changes.
- Stop if isolation cannot be maintained safely.

## Commit

- Stage only approved Task scope.
- Review the staged diff and secret/generated-noise risk.
- Run required local validation first.
- Use a focused message and record branch, commit SHA, and validation in Task evidence.

## Review and Verification

- Code review, CI, and Verification must reference the same committed revision.
- Later changes invalidate stale approval/evidence.
- Missing Git metadata or commit identity is `NOT VERIFIED`, not PASS.

## Restrictions

No force push, history rewrite, destructive reset, merge, tag, or release action without explicit authorization.
