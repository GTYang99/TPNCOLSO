# Verification Report

## Inputs

- Requirement: `docs/tasks/UI-003-map-shell/requirement.md`
- Plan and approved baseline: `docs/tasks/UI-003-map-shell/plan.md`, `KB-UI-003-MAP-SHELL-R1`
- Candidate committed revision: `9c0d453` on `UI-003feat` (production fix ancestry `9cbd82a`, regression-test fix `a84f848`)
- Current verification run: 2026-10-01, current-revision responsive capture and connected validation on the approved emulator
- Code Review Revision 3: `APPROVED` for committed revision `9c0d453`
- Local focused validation: Android Studio JBR 25.0.3; `MapShellScreenTest` connected tests 7/7 PASS
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
| AC-UI003-009 | PASS (local emulator) | Current revision `9c0d453` was captured at standard 1080x2400, narrow 720x2400, font scale 1.3, and drawer/font scale 1.3. Screenshots and hierarchies are recorded with SHA-256 checksums in `execution-report.md`; primary controls, basemap choices, location, identity, navigation rows and logout remain exposed without observed clipping. |

## Regression

- The direct-login regression assertion was updated to the current approved drawer contract; focused and full emulator runs now pass.

## Issues

- `VER-UI003-008`: resolved locally by the test-only assertion update; the prior full connected regression is 36/36 on `emulator-5554`.
- Code Review Revision 3 approved the `CR-UI003-006` and `CR-UI003-007` fixes.
- Hosted/authoritative CI is unavailable for the unpublished `UI-003feat` branch.

## Validation Limitations

- The available physical device is excluded because it exposes no Compose hierarchy.
- Hosted CI evidence is unavailable; local emulator evidence cannot substitute for that gate.

## Failure Classification

- `unknown` — no implementation failure occurred in this focused run; overall `NOT VERIFIED` is caused by remaining review/evidence gates.

## Next Action

- Keep Verification at `NOT VERIFIED` because hosted CI is unavailable for the unpublished branch. All locally executable acceptance criteria now have passing evidence; no production code change was made in this verification run.

## Final Result

NOT VERIFIED
