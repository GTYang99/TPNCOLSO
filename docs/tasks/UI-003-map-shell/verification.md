# Verification Report

## Inputs

- Requirement: `docs/tasks/UI-003-map-shell/requirement.md`
- Plan and approved baseline: `docs/tasks/UI-003-map-shell/plan.md`, `KB-UI-003-MAP-SHELL-R1`
- Candidate reviewed production revision: `9c0d453` on `UI-003feat` (production fix ancestry `9cbd82a`, regression-test fix `a84f848`)
- CI-evaluated descendant: `00d5d1e` on `UI-003feat` (workflow-only hosted-runner compatibility changes after `9c0d453`; no production-code change)
- Current verification run: 2026-10-02, current-revision responsive capture and hosted CI validation
- Code Review Revision 3: `APPROVED` for committed revision `9c0d453`
- Local focused validation: Android Studio JBR 25.0.3; `MapShellScreenTest` connected tests 7/7 PASS
- Full connected result: 36/36 PASS on `emulator-5554`; hosted run `36948553870` unit-and-build PASS, connected PASS

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
- Hosted run `36948553870` is PASS: both `unit-and-build` and `connected` completed successfully on workflow-only revision `00d5d1e`. The connected job created/launched the hosted emulator and completed `./gradlew connectedDebugAndroidTest`.
- Prior hosted infrastructure issues `VER-UI003-010` through `VER-UI003-012` are closed in `issue-log.md`; no production code changed during their resolution.

## Validation Limitations

- The available physical device is excluded because it exposes no Compose hierarchy.
- Hosted connected-test evidence is now available from run `36948553870`; the available physical device remains excluded because it exposes no Compose hierarchy.

## Failure Classification

- `none` — prior hosted failures were infrastructure/tooling issues. The final workflow-only repair was verified by hosted run `36948553870`; no implementation regression remains in the reviewed scope.

## Next Action

- Verification is complete. Proceed to the release-approval gate for UI-003 when release work is explicitly requested; do not merge, deploy, or publish without authorization.

## Final Result

PASS
