# Debug

## Purpose

Identify the root cause of a classified implementation failure and define the minimum safe fix.

## Entry

- `next_action: debug`
- failed AC and reproducible evidence exist
- failure category is implementation

## Load

- failed requirement/AC
- Verification finding and relevant test/log/stack trace
- plan and changed diff for the failing revision
- related issue

## Output

- `root-cause.md`
- `fix-plan.md`
- updated issue/execution evidence and `state.yaml`

## Procedure

1. Reproduce or bound the failure.
2. Map it to AC, code, tests, and revision.
3. Separate root cause from symptoms.
4. Define the minimum fix and regression checks.
5. Obtain Plan Review when the fix changes approved design or scope.

## Exit

- Root cause and fix scope established: `implementation_debug`.
- Requirement/planning cause discovered: route there instead.
- Environment cause: `infrastructure`.
- Still unclassified: `investigation`.

## Restrictions

Debug analysis does not modify production code. Re-implementation starts only after the fix plan is sufficient and authorized.
