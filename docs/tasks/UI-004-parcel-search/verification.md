# Verification Report

## Inputs

- Requirement: `docs/tasks/UI-004-parcel-search/requirement.md`
- Plan: `docs/tasks/UI-004-parcel-search/plan.md`
- Completion scope: `development`
- Developer/local validation: `docs/tasks/UI-004-parcel-search/execution-report.md`
- Hosted CI: deferred under the approved pre-release development scope; release-candidate reactivation remains recorded in `state.yaml`.
- Code review: `APPROVED`, revision `1101ec7`.
- Committed revision evaluated: `1101ec7` on `codex/ui004-parcel-search`.

## Acceptance Criteria

| AC | Result | Evidence |
|---|---|---|
| `AC-UI004-001` | `NOT VERIFIED` | Unit state test confirms the ViewModel retains a submitted keyword. The Compose test for actual entry/visible retention stopped before UI assertions because the local test runner reported no Compose hierarchy. |
| `AC-UI004-002` | `NOT VERIFIED` | Fake match and generic map-state transition unit tests pass. The host Compose test for toolbar entry, selected map target and bottom summary did not execute its assertions because no Compose hierarchy was available. |
| `AC-UI004-003` | `NOT VERIFIED` | Unit test confirms an unmatched keyword remains in state. Runtime display of “查無資料” with the map still mounted was not verified because the Compose test stopped before assertions. |
| `AC-UI004-004` | `NOT VERIFIED` | The summary screen renders status, `key_no`, `land_no` and `site_condition` from the selected synthetic record. The Compose content assertion could not run in the local environment. |
| `AC-UI004-005` | `PASS` | Focused unit tests passed for deterministic matching by key number, land number and location, plus an alternate `ParcelSearchDataSource` implementation using the same ViewModel state contract. |
| `AC-UI004-006` | `NOT VERIFIED` | The existing top-search and location-only callback Compose regression test and the UI-004 host Compose test both stopped before assertions with the same missing-hierarchy error. The source retains UI-003 ownership of both controls and wires only the top-search callback to UI-004. |

## Regression

- `MapShellViewModelTest.searchContextUpdatesOnlyQueryAndSelectedTarget` passed; it asserts preservation of basemap, location state, drawer state, other overlay values and identity.
- Existing `MapShellScreenTest` Compose checks are `NOT VERIFIED` in this local environment; they fail before assertions with the same missing-hierarchy error as UI-004.
- The non-Compose `ExampleInstrumentedTest` passed on the same API 34 emulator.
- `:app:assembleDebug`, `:app:assembleRelease` and `:app:lint` passed.
- No parcel API, DTO, endpoint, persistence or logging behavior was added.

## Issues

- `ENV-UI004-001`: local Compose instrumentation cannot obtain a Compose hierarchy. The same failure occurs in unchanged baseline Compose tests; no product assertion ran in the failing cases. Route: `infrastructure`.

## Validation Limitations

- UI runtime evidence for `AC-UI004-001` through `AC-UI004-004` and `AC-UI004-006` remains unavailable until the local Compose instrumentation environment is restored and the focused tests pass.
- Android 9 runtime behavior and formal Figma pixel parity were not established in this task run. The app still declares `minSdk 28`; formal Figma approval is deferred.
- Local checks are not hosted CI evidence.

## Deferred Obligations

- Hosted CI: deferred for pre-release development; owner: release/project maintainer; reactivate before release-candidate validation.
- Production parcel API and integration tests: deferred to `INT-002`; owner: INT-002/API owner; reactivate after API contract approval and before release validation.
- Product UAT: deferred unless required by release policy; owner: Product/requester; reactivate at release-candidate acceptance if required.
- Release approval, merge/deployment and monitoring: outside development scope; owner: release approver / deployment owner; reactivate after Development Complete when a release candidate is proposed.
- Release signed-in host integration: deferred until the release session host exists; owner: UI-002 / INT-001 authentication and release-integration owner.
- Formal Figma revision approval and pixel parity: deferred; owner: Design/requester; reactivate before any formal pixel-acceptance claim.

## Completion Decision

- `blocked`

## Failure Classification

- `environment`

## Next Action

- `infrastructure`

## Final Result

NOT VERIFIED
