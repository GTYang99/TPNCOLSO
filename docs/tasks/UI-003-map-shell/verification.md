# Verification Report

## Inputs

- Requirement: `docs/tasks/UI-003-map-shell/requirement.md`
- Plan and approved baseline: `docs/tasks/UI-003-map-shell/plan.md`, `KB-UI-003-MAP-SHELL-R1`
- Candidate source revision: `9cbd82a` on `UI-003feat`
- Current documentation handoff: `b0b4852`
- Code Review Revision 2: `CHANGES_REQUESTED`; Code Review Revision 3 is pending
- Local build and focused validation: JBR 25.0.3; unit/lint/build PASS; UI-003 connected tests 7/7 PASS
- Full connected result: 36/36 PASS on `emulator-5554`; hosted CI unavailable

## Acceptance Criteria

| AC | Result | Evidence |
|----|--------|----------|
| AC-UI003-001 | PASS | Current `MapShellScreenTest.shellRendersControlsAndExactlyThreeBasemaps` passes on API 34 emulator. |
| AC-UI003-002 | PASS | Current UI-003 test plus `MapShellViewModelTest.defaultStateSelectsElectronicMap` cover the three choices and initial selection. |
| AC-UI003-003 | PASS | `MapShellViewModelTest.selectingBasemapPreservesUiOnlyOverlayContext` passes. |
| AC-UI003-004 | PASS | Current `MapShellScreenTest` covers drawer opening, 304dp panel, navigation rows and accessible logout action. |
| AC-UI003-005 | PASS (local emulator) | UI-003 exactly-once logout callback passes, and `DebugDirectLoginTest.directLoginAndLogoutReturnToSignedOut` now completes direct-login, drawer, logout and signed-out assertions on `emulator-5554`. |
| AC-UI003-006 | PASS | Current UI-003 callback tests pass for top controls, map navigation, drawer navigation and location/retry. |
| AC-UI003-007 | PASS | Current loading and permission-denied tests pass with common semantics and readable retry action. |
| AC-UI003-008 | PASS | Current unit suite is 18/18 and current `MapShellScreenTest` is 7/7. |
| AC-UI003-009 | NOT VERIFIED | Current-revision responsive screenshots/hierarchies have not been recaptured after the drawer fixes; prior evidence predates the reviewed fix revision. |

## Regression

- The direct-login regression assertion was updated to the current approved drawer contract; focused and full emulator runs now pass.

## Issues

- `VER-UI003-008`: resolved locally by the test-only assertion update; full connected regression is 36/36 on `emulator-5554`.
- Code Review Revision 3 is still required for the `CR-UI003-006` and `CR-UI003-007` fixes.
- Hosted/authoritative CI is unavailable for the unpublished `UI-003feat` branch.

## Validation Limitations

- Current-revision responsive evidence for AC-UI003-009 is missing.
- The available physical device is excluded because it exposes no Compose hierarchy.
- Hosted CI evidence is unavailable; local emulator evidence cannot substitute for that gate.

## Failure Classification

- `implementation`

## Next Action

- Route to `debug`: repair the direct-login regression coverage or the implementation contract as authorized, rerun the full connected suite, commit the result, then request Code Review Revision 3. Re-enter Verification only after review and current responsive evidence are available.

## Final Result

NOT VERIFIED
