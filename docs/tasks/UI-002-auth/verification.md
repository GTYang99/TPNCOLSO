# Verification Report

## Inputs

- Requirement: active R11 `docs/tasks/UI-002-auth/requirement.md`
- Plan/review: plan revision 18, Plan Review iteration 19 `APPROVED`
- Completion scope: development
- Reviewed source/test revision: `9d241994d29057a16aa68c4f4c323ed232158b18`
- Code Review: `APPROVED` by same-agent review; no blocking finding
- Hosted CI: deferred for pre-release development; the recorded R10 run is failed and no R11 hosted run exists

## Acceptance Criteria

| AC | Result | Evidence |
|---|---|---|
| AC-UI002-001 | PASS | AuthScreenTest `loginRendersRequiredEntryPoints` and current Auth connected suite 26/26 cover the Login entry points, semantics, and absence of CAPTCHA UI. |
| AC-UI002-002 | PASS | Auth validation/ViewModel tests plus `loginSubmittingStateDisablesSubmit` and current Auth connected 26/26 cover local errors, duplicate suppression, and no CAPTCHA validation. |
| AC-UI002-003 | PASS | Auth ViewModel tests and `loginAuthErrorKeepsValuesAndShowsAccessibleGlobalMessage` cover exactly-once success, retained account/remember state, and `帳號或密碼錯誤`. |
| AC-UI002-004 | PASS | Current Auth connected suite covers Login-to-Register navigation, six fields, Back, and Cancel. |
| AC-UI002-005 | PASS | Auth validation/ViewModel tests cover all stated boundaries, exactly-once registration, and success reset. |
| AC-UI002-006 | PASS | Debug coordinator/session tests cover immediate local logout and clean Login return; drawer remains UI-003-owned. |
| AC-UI002-007 | PASS (emulator scope) | AuthScreenTest passes on API 28 (`emulator-5558`) and API 36 profiles for 320dp, 402dp, font scale 1.3, and IME action reachability. Direct review of the API 28 narrow/font-scale captures found no functional text clipping; physical/OEM and additional API coverage remain outside this development run. |
| AC-UI002-008 | PASS | Debug Preview state inventory, replaceable fake source, JVM tests, current Auth connected tests, and release source-set checks are recorded in the execution report. |
| AC-UI002-009 | PASS (emulator scope) | UIR-LOGIN-001–010 now have implementation mapping plus executed visual/Compose/accessibility evidence across API 28 and API 36 emulator profiles. Physical/OEM coverage is deferred and does not change the local development scope result. |
| AC-UI002-010 | PASS (local scope) | All Registration UIR rows have implementation/test mapping and current local Compose evidence; complete cross-device responsive coverage remains a documented limitation. |
| AC-UI002-011 | NOT VERIFIED | Local logout contract/idempotency evidence passes, while remote logout behavior is explicitly INT-001-owned and remains delegated/not verified. |

## Regression

- Full emulator-only connected run on `emulator-5554`, API 36, completed 72 tests with 1 failure and 0 errors/skips. AuthScreenTest completed 26/26; the only failure was `SurveyFormScreenTest.dirtyCloseAndBackUseDiscardDialogAndContinuePreservesEdits`, outside UI-002 Auth scope. XML SHA-256: `eb4f76ddf898da7424dd1037d009b2b04ed4c0e6dcc0ef2ad194912d98a11c93`.
- The unfiltered JVM suite still reproduces the out-of-scope `FakeReturnFlowStoreTest` failure at line 76. The Auth-filtered JVM suite, lint, release assembly, and Android test APK assembly pass.

## Issues

- Physical/OEM and additional API-level responsive coverage are deferred beyond the API 28/API 36 emulator matrix.
- One owning SurveyForm connected regression and one owning Notifications/Parcel JVM regression remain open outside UI-002.
- Hosted CI has not run for R11.

## Validation Limitations

- No physical device was used, per requester instruction.
- The current responsive visual artifact is an emulator-only slice; the IME Compose root raster does not include the system keyboard.
- API 28 and API 36 emulator profiles were exercised. Physical/OEM behavior and additional API-level combinations remain outside this development run.
- Local results are not hosted CI PASS.

## Deferred Obligations

- `hosted_ci_release_candidate` — deferred for pre-release development; owner: release/project maintainer; reactivate before release-candidate validation.
- Physical/OEM and additional API responsive coverage — owner: UI-002/QA; reactivate when additional authorized emulator coverage is available.
- INT-001 remote logout verification — owner: INT-001; reactivate after the remote Token/logout contract is implemented.

## Completion Decision

- `blocked` pending the `NOT VERIFIED` acceptance criteria and owning regression review.

## Failure Classification

- `environment` for missing complete device/API coverage and the connected-test infrastructure history.
- `implementation` is not assigned to UI-002 for the SurveyForm or FakeReturnFlowStore failures; both are source-classified as owning external regressions.

## Next Action

- `verification`: resolve or formally accept the delegated remote logout and owning regressions before Development Complete; retain the API 28/API 36 emulator matrix as local evidence.

## Final Result

NOT VERIFIED
