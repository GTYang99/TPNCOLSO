# Verification Report

## Inputs

- Requirement: `docs/tasks/UI-004-parcel-search/requirement.md`
- Plan: `docs/tasks/UI-004-parcel-search/plan.md`
- Completion scope: `development`
- Developer/local validation: `docs/tasks/UI-004-parcel-search/execution-report.md`; current checks ran against committed revision `a4989bc` or the identical source tree immediately before that test-only commit.
- Hosted CI: deferred under the approved pre-release development scope; reactivation remains recorded in `state.yaml`.
- Code review: `APPROVED`, revision `a4989bc` (test-only selector correction); original implementation review remains recorded for `1101ec7`.
- Committed revision evaluated: `a4989bc` on `codex/ui004-parcel-search`.

## Test Environment

- Android Studio JBR `/Applications/Android Studio.app/Contents/jbr/Contents/Home`, OpenJDK `25.0.3`.
- Emulator `Medium_Phone(AVD)` / `emulator-5554`, API 34 (Android 14).
- `minSdk` is configured as 28. No Android 9/API 28 system image is installed locally; API 28 runtime execution is recorded as a limitation.
- ADB and Gradle needed the authorized unsandboxed tool path because the sandbox denied the local ADB listener and Gradle cache lock. The Compose hierarchy was available during the passing test runs.

## Acceptance Criteria

| AC | Result | Evidence |
|---|---|---|
| `AC-UI004-001` | `PASS` | `ParcelSearchHostTest.toolbarSearchSelectsFakeParcelAndShowsSummaryOverMap` opens the top-toolbar search, enters and submits `TEST-KEY-001`, then confirms the active text input retains that value. |
| `AC-UI004-002` | `PASS` | The same host test confirms the map surface and matched parcel remain visible with the bottom summary card. Focused `MapShellViewModelTest.searchContextUpdatesOnlyQueryAndSelectedTarget` passes and verifies unrelated map state is preserved. |
| `AC-UI004-003` | `PASS` | `ParcelSearchHostTest.noMatchKeepsQueryAndMapContainerVisible` confirms the unmatched query remains visible, `查無資料` appears, the existing map surface/parcel remain, and no summary card replaces them. |
| `AC-UI004-004` | `PASS` | The match test confirms the summary card and status are displayed, `land_no` and `site_condition` values are visible, and `key_no` appears in both the retained input and the summary (the test asserts exactly two matching nodes). |
| `AC-UI004-005` | `PASS` | Focused unit tests pass for deterministic matching by parcel key, land number and location, no-match state, and an alternate `ParcelSearchDataSource` implementation using the same ViewModel contract. |
| `AC-UI004-006` | `PASS` | The host test enters through the existing top-toolbar search callback and taps the lower-right location control, observing its loading state. `MapShellScreenTest.topAndLocationControlsForwardExplicitCallbacks` passes and verifies search and location remain separate callbacks. |

## Regression

- `ParcelSearchHostTest`: PASS, 2/2 cases, API 34 emulator.
- `MapShellScreenTest`: PASS, including the top-search/location callback regression.
- `DebugDirectLoginTest`: PASS; the previously affected Compose suite now exposes a hierarchy.
- Focused `ParcelSearch` and `MapShellViewModelTest` unit suite: PASS.
- `:app:assembleDebug`, `:app:assembleRelease`, and `:app:lint`: PASS.
- No production parcel API, DTO, endpoint, persistence or logging behavior was added.

## Issues

- `ENV-UI004-001` is resolved: the configured emulator and Compose hierarchy are available when ADB/Gradle run through the authorized unsandboxed path.
- `VER-UI004-001` is resolved: the original single-node assertion was ambiguous because the active query and summary intentionally display the same key; the test-only correction is committed in `a4989bc`.

## Validation Limitations

- API 28 runtime and an expanded font-scale/screen-size matrix were not run. `minSdk = 28` is declared and Debug/Release builds plus the API 34 Compose checks pass.
- Formal Figma revision approval and pixel parity remain unverified; the plan makes no formal pixel-parity claim.
- Local checks are not hosted CI evidence.

## Deferred Obligations

- Hosted CI: deferred for pre-release development; owner: release/project maintainer; reactivate before release-candidate validation.
- Production parcel API and integration tests: deferred to `INT-002`; owner: INT-002/API owner; reactivate after API contract approval and before release validation.
- Product UAT: deferred unless required by release policy; owner: Product/requester; reactivate at release-candidate acceptance if required.
- Release approval, merge/deployment and monitoring: outside development scope; owner: release approver / deployment owner; reactivate after Development Complete when a release candidate is proposed.
- Release signed-in host integration: deferred until the release session host exists; owner: UI-002 / INT-001 authentication and release-integration owner.
- Formal Figma revision approval and pixel parity: deferred; owner: Design/requester; reactivate before any formal pixel-acceptance claim.

## Completion Decision

- `development_complete`

## Failure Classification

- `不適用` (all task acceptance criteria pass)

## Next Action

- `none`

## Final Result

PASS
