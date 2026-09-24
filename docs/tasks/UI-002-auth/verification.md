# UI-002 Verification Report

## Current Verification Provenance — 2026-09-24, debug-fix re-entry

This section supersedes the older round summaries for the current formal decision. Historical `PARTIAL` labels are retained as historical progress records only; the current formal result is restricted to `PASS`, `FAIL`, or `NOT VERIFIED`.

Current formal result: `NOT VERIFIED` for `534bcb2`. The visual subresult for AC-UI002-009/010 is `PASS`; the overall result remains `NOT VERIFIED` because AC-UI002-006/007/008/011 and hosted CI do not have current reproducible evidence. The prior `FAIL` decisions for `db7dfbe782d3537b5f618af2735964913ce580b7` and the responsive finding fixed in `534bcb2` are retained as historical debug input.

| Provenance item | Current evidence | Classification |
|---|---|---|
| Requirement baseline | `KB-UI-002-AUTH-R10`; Plan Review iteration 16 `APPROVED` | PASS |
| Previous failed implementation revision | Branch `UI-002feat`; `0815e6b5c22dc14bd2478a7b66bbb1aa58e7dd08`; `docs(UI-002): normalize implementation handoff` | FAIL; superseded by the post-fix revision |
| Previous failed implementation revision | Branch `UI-002feat`; `63a2379`; `fix(UI-002): restore auth accessibility and back asset contracts` | FAIL; superseded by post-fix revision |
| Reviewed implementation revision | Branch `UI-002feat`; `534bcb2`; `fix(UI-002): make login layout responsive` (includes `14dc640`) | PASS; visual and responsive implementation fixes are reviewed below |
| Verification handoff revision | `1c18088`; task state and verification handoff only | PASS; does not change reviewed production revision |
| Unit test evidence | `testDebugUnitTest`; five XML suites, 13 tests, 0 failures/errors/skips; latest run `2026-09-24T07:05:03Z` | PASS |
| Connected test evidence | `connectedDebugAndroidTest`; 24 tests, 0 failures/errors/skips; `XQ-AU52 - 12`, API 34; latest run `2026-09-24T07:05:44Z` | PASS |
| Developer-validation commands | `JAVA_HOME='/Applications/Android Studio.app/Contents/JBR/Contents/Home' ./gradlew :app:testDebugUnitTest :app:lintDebug :app:assembleDebug :app:assembleRelease :app:assembleDebugAndroidTest` and `./gradlew :app:connectedDebugAndroidTest` | PASS |
| Visual primary authority | Requester-approved composite `docs/design/evidence/auth/auth-reference-2026-09-07.png`, SHA-256 `75cb578c58f3098c270290f033539ebf06c35c8b322efb71526fa4e53f3d3e9c` | PASS |
| Figma authority role | Figma file `HRbRsw6HoNBUCtaieX8xUM`, nodes `2905:2679` / `2997:11541`, direct export SHA-256 `bfde10cd297feb639ecc2b3fc4bc4964258bc4541174032e392c0a104e4ed2e5`; supporting design evidence, not the primary auth acceptance authority | PASS |
| Comparison normalization | Composite panels cropped at `804×1748` from the approved source; runtime captures cropped to app content (`1080×2296`, top inset `48`, bottom inset `56`) and resized to `402×874`; debug-only region excluded | PASS |
| Current-revision visual comparison | Evidence is bound in `figma-export-2026-09-24-534bcb2/numeric-diff-534bcb2.json`; all five states are recaptured with system chrome/debug-only region excluded and directly reviewed against the approved composite | PASS; AC-UI002-009 and AC-UI002-010 |
| Hosted CI | No hosted/authoritative CI run is available; local JBR Gradle and emulator evidence only | NOT VERIFIED |
| Physical device | Explicitly out of scope by requester; emulator coverage is the applicable environment | NOT APPLICABLE |

### Current formal decision — revision `534bcb2`

`NOT VERIFIED`

The focused fix moves the Login header/form rhythm to the approved anchors, corrects the skyline visible baseline/crop, moves the Register content origin to the approved vertical rhythm, and makes narrow Login layout responsive by adapting width, header arrangement, captcha arrangement and skyline placement. All five states were recaptured from `534bcb2`; metrics are Login empty `10.6742 / 36.7446 / 0.094471`, filled `11.2702 / 38.2067 / 0.098033`, auth-error `23.1521 / 59.4954 / 0.169826`; Register empty `5.8141 / 27.8220 / 0.062526`, filled `7.2016 / 32.0093 / 0.070590` (MAE / RMSE / over-threshold ratio, RGB threshold `20`). Direct review confirms the five standard-state compositions align with the approved composite after system chrome and debug-only masking. A current 720×2400 narrow emulator capture confirms no horizontal clipping or captcha overlap and is recorded beside the numeric evidence.

This proves `AC-UI002-009` and `AC-UI002-010` for the reviewed revision at visual scope. Additional current Login responsive captures are recorded as `runtime-login-narrow-534bcb2.png` (720×2400), `runtime-login-fontscale-1.3-534bcb2.png`, and `runtime-login-ime-534bcb2.png`, with SHA-256 `973ce40d0be93cdcabcb7db8b7f6404ae73bbb41ac538d08d35acff9505ef2b9`, `234c84959748d6ad9c0377c59f950eb97fa01fa18f84229147bb429753dc2c58`, and `a6c7fedbf6281c8231b7bca9d7a948ea26edc9c29e310239ad900ee30572eb3c`. These support Login responsive behavior but do not by themselves close the full Register/current matrix requirement. Local build, lint, release, unit and connected evidence passes. `AC-UI002-006`, `AC-UI002-007`, `AC-UI002-008`, and `AC-UI002-011` remain `NOT VERIFIED`; hosted CI is unavailable. Under the Verification Rule, the overall result is therefore `NOT VERIFIED`, not PASS.

### Previous formal decision — revision `63a2379`

`FAIL`

The post-fix revision passes local build, unit, lint, and API 34 connected tests, and it fixes the three findings from `0815e6b`. However, source inspection proves two remaining Registration accessibility/asset mismatches: the illustration is exposed as a spoken `註冊插圖` label even though it is decorative, and the committed Back vector points right without the required left rotation. Current-revision visual comparisons and hosted CI evidence also remain incomplete, so the task cannot advance to Release.

### Verified implementation findings — revision `842e290`

| Finding | Reviewed revision evidence | Requirement | Result |
|---|---|---|---|
| Login logo semantics | `LoginScreen.kt:77–82` now declares one heading row and `contentDescription = null`; `AuthScreenTest.loginRendersRequiredEntryPoints` confirms no `品牌標誌` node | Decorative logo has no duplicate spoken label while logo/title form one heading | PASS for source/Compose scope |
| Captcha reload semantics and target | `LoginScreen.kt:109–117` keeps the `32dp × 48dp` allocation and exposes `重新產生驗證碼` through a required `48dp × 48dp` target; focused connected assertions pass | Exact description and effective target of at least `48 × 48` | PASS for source/Compose scope |
| Registration illustration semantics | `RegisterScreen.kt:36` now sets `contentDescription = null`; `AuthScreenTest.registerRendersSixFieldsAndUnselectedWorkType` confirms no `註冊插圖` node | User illustration is decorative unless future product copy gives it independent meaning | PASS for source/Compose scope |
| Registration Back asset orientation | `RegisterScreen.kt:42–44` consumes the approved vector and applies `graphicsLayer { rotationZ = 180f }`; target remains `48 × 48` and glyph remains `32 × 32` | Exact approved chevron is rotated toward the left inside the `48 × 48` target | PASS for source scope; visual comparison NOT VERIFIED |

The source-proven findings from `63a23794e70edb5cd00203a617afca7715be57b9` are fixed in `842e290`. The current composite comparison now proves separate visual implementation mismatches in the reviewed revision; Verification routes to Debug. No production-code fix is made during Verification.

### Historical formal decision — revision `842e290`

`FAIL`

The Registration illustration is now decorative and the approved candidate vector is rotated left in Compose. Unit, lint, and API 34 connected validation pass, but the complete current-revision composite comparison proves visual nonconformance: Login still differs in logo treatment/field geometry, and Registration renders the work-type choices vertically while the approved panel is horizontal; the normalized runtime view also does not preserve the approved action-row composition. This fails `AC-UI002-009` and `AC-UI002-010` and routes to `debug`. Hosted CI remains unavailable as an additional gate.

## Verification Run Evidence — 2026-09-23

- Figma file `HRbRsw6HoNBUCtaieX8xUM` was read through nodes `2905:2680`, `2905:2679`, and `2997:11541`. `2905:2680` contains Login empty/filled/error, Registration empty/filled, and Logout context; `2905:2679` is a `402 × 874` Login frame; `2997:11541` exposes the `320dp` auth column and `736 × 246` skyline at `x=-138`, `y=635`. This is `visual_observation` supporting evidence only; the approved composite PNG remains the primary visual authority.
- Local developer validation: `JAVA_HOME="/Applications/Android Studio.app/Contents/JBR/Contents/Home" ./gradlew :app:testDebugUnitTest :app:lintDebug :app:assembleDebug :app:assembleRelease :app:assembleDebugAndroidTest` — `BUILD SUCCESSFUL`; JBR `25.0.3`; 13 unit tests passed with 0 failures/errors/skips.
- Connected runtime validation: `JAVA_HOME="/Applications/Android Studio.app/Contents/JBR/Contents/Home" ./gradlew :app:connectedDebugAndroidTest` — latest run `BUILD SUCCESSFUL`; 24 tests passed with 0 failures/errors/skips on `Medium_Phone (AVD) - 14`, API 34, serial `emulator-5554`, timestamp `2026-09-23T08:06:23`.
- Isolated `AuthScreenTest.captureRegisterFilledStateForVisualEvidence` also passed 1/1 on `842e290` at `2026-09-23T07:49:41`, confirming the approved filled fixture at the Compose semantics level. Its exported root bitmap was blank apart from system chrome, so it is not used as current visual evidence.
- The connected suite re-executed the focused semantics assertions: no `品牌標誌` node, reload `重新產生驗證碼` at `48dp × 48dp` while the layout slot remains `32dp × 48dp`, Register Back `48dp × 48dp`/`返回登入頁`, and no `註冊插圖` node. The left-facing Back orientation is source-proven by `rotationZ = 180f`; the completed visual comparison below independently identifies remaining implementation mismatches.
- Direct emulator captures from the installed `842e290` debug APK are stored under `docs/design/evidence/auth/figma-export-2026-09-23/`: Login empty (`runtime-login-empty-842e290.png`, 1080×2400, SHA-256 `971209d79b9074c65841efe703ae6f719ef668f67058c5a171cd3e8e376fb7fa`), Login filled (`runtime-login-filled-842e290.png`, SHA-256 `995c3ec243a826ba9d58d0b796ffb450d39291fbd2ef2fcd6cdf6f4be853f26f`), Login auth-error (`runtime-login-error-842e290.png`, SHA-256 `d38c7fa1d1714d28b0c80fcbfc4102123da31ea9c021edf7cd142cb692824cc0`), Register empty (`runtime-register-empty-842e290.png`, SHA-256 `d210d86bac112d07f1008bd5cc26fc647ad111ae42c79dc309f00de8e34d13db`), and Register filled (`runtime-register-filled-842e290.png`, 1080×2400, SHA-256 `d5afecf154ddd08bfa217bf6517df21227804125549e29dbd9c06c4a6462ff10`). The Register filled capture uses the approved fixture `sunrise1234` / `sfk;wfj1~` / `日陞` / `外業人員` / `劉大君`. All five raw captures include Android system chrome and, where applicable, the debug-only `開發模式` region, so they are current raw visual evidence, not normalized PASS evidence.
- The first top-only normalization (`1080×2400 → crop 1080×2344 at top offset 48`) was superseded because it retained the bottom navigation bar. The formal current comparison uses `1080×2400 → crop app content 1080×2296 at y=48` (removing `48px` top status and `56px` bottom navigation chrome) → resize `402×874`; the debug-only region `(0,0,175,48)` is excluded. The five raw/reference hashes and metrics are bound in `docs/design/evidence/auth/figma-export-2026-09-23/composite-panel-comparison-842e290.json`.
- Approved-fixture recaptures were added for Login filled (`runtime-login-filled-842e290-approved-fixture.png`, SHA-256 `ede02aa9f56ada91da2c760528d6d7321477dc1ba9a986a459a5663306d2070f`) and Login auth-error (`runtime-login-error-842e290-approved-fixture.png`, SHA-256 `4928a4febf8976e96eae71ab3ad04bfb86a29b35d7094ad20740d853d85ddc12`); both use `sunrise000` / eight-character password / `0926` / Remember me, with the auth-error copy rendered by the current Activity. The temporary capture test was removed after collection.
- Formal current RGB comparison at threshold `20`, after system-chrome removal and mask exclusion: Login empty MAE/RMSE/over-threshold `21.3681 / 58.7782 / 0.156346`; Login filled `21.6469 / 59.2477 / 0.156870`; Login auth-error `31.6095 / 72.2754 / 0.219775`; Register empty `12.0194 / 44.8400 / 0.100315`; Register filled `12.5398 / 45.9339 / 0.101072` across `1,028,844` channel samples. These are evidence of the remaining visual mismatch, not PASS evidence.

## Verification Re-run Evidence — 2026-09-24

- Reviewed production revision is `db7dfbe782d3537b5f618af2735964913ce580b7`; `1c18088` is the documentation handoff commit that records validation evidence after the reviewed production revision.
- Runtime identity was verified before Gradle execution: Android Studio JBR OpenJDK `25.0.3` at `/Applications/Android Studio.app/Contents/jbr/Contents/Home`.
- `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:testDebugUnitTest :app:lintDebug :app:assembleDebug :app:assembleRelease :app:assembleDebugAndroidTest` — `BUILD SUCCESSFUL` in 26 seconds; all requested unit, lint, Debug, Release and AndroidTest APK checks passed or were up-to-date.
- `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:connectedDebugAndroidTest` — `BUILD SUCCESSFUL`; connected XML reports `24` tests, `0` failures, `0` errors and `0` skipped at `2026-09-24T02:06:56Z`.
- The connected rerun confirms runtime semantics and interaction coverage, but the current five-state comparison still proves visual nonconformance. The Register work-type row is now horizontal, but the normalized Login and Register captures retain residual vertical/placement differences against the approved composite.
- Hosted/authoritative CI evidence remains unavailable; these checks are local JBR/emulator evidence only.

## Inputs

- Requirement baseline: `KB-UI-002-AUTH-R10`
- Plan Review: revision 16, `APPROVED`
- Branch: `UI-002feat`
- Reviewed revision for the current formal round: `db7dfbe` (`fix(UI-002): align auth visual composition`)
- Runtime: Android Studio JBR, OpenJDK 25.0.3

The older round below is retained as historical evidence and is not the reviewed revision for the current formal decision.

## Verification Round 3 — 2026-09-08

- Device: `Medium_Phone (AVD) - 14`, ADB serial `emulator-5554`
- Command: `JAVA_HOME='/Applications/Android Studio.app/Contents/jbr/Contents/Home' ./gradlew connectedDebugAndroidTest`
- Result after fix: `PASS`; 33 tests executed, 0 failed, 0 errors, 0 skipped.
- Passing groups: `FoundationComponentTest` 8/8; `AuthScreenTest` 20/20; `DebugDirectLoginTest` 2/2; app context 1/1.
- Previous four failures were reproduced and fixed: direct-login tests now enter the existing Debug mode first; Login keeps the remember/register action row visible in the viewport and uses a width-safe checkbox layout.
- Evidence: [connected test report](../../app/build/reports/androidTests/connected/debug/index.html) and `app/build/outputs/androidTest-results/connected/debug/TEST-Medium_Phone(AVD) - 14.xml`.

## Acceptance Criteria

| AC | Current result | Evidence / limitation |
|---|---|---|
| AC-UI002-001 | PASS (source/Compose scope) | Current source and focused API 34 Compose assertions cover the required entry points, decorative logo semantics, reload semantics, and effective target. |
| AC-UI002-002 | PASS (unit scope) | `AuthValidationTest` and `AuthViewModelTest` cover required fields, captcha format and submit rejection. |
| AC-UI002-003 | PASS (unit scope) | ViewModel tests cover success effect, sensitive clearing and auth-error retention; debug coordinator guards duplicate session start. |
| AC-UI002-004 | PASS (connected scope) | Register route, six fields, Back and Cancel events pass `AuthScreenTest` on API 34. |
| AC-UI002-005 | PASS (unit scope) | Pure validation tests cover account, password, confirmation, vendor, work type and Chinese name rules. |
| AC-UI002-006 | NOT VERIFIED | Debug coordinator evidence exists, but the complete cross-task logout contract is not executed in the current revision. |
| AC-UI002-007 | NOT VERIFIED | Historical emulator evidence covers narrow width, font scale 1.3 and IME; the complete current-revision responsive matrix is still not executed. |
| AC-UI002-008 | NOT VERIFIED | Preview and test fixtures exist, but the complete current-revision visual state matrix is not evidenced. |
| AC-UI002-009 | PASS | `numeric-diff-534bcb2.json` contains all three current Login states; direct normalized comparison and visual review pass after the anchor/crop and responsive fix. |
| AC-UI002-010 | PASS | `numeric-diff-534bcb2.json` contains both current Register states; direct normalized comparison and visual review pass after the content-origin fix. |
| AC-UI002-011 | NOT VERIFIED | Coordinator/idempotency evidence exists, but UI-003 contract integration is not executed in the current revision. |

## Build and Test Evidence

| Check | Result |
|---|---|
| `testDebugUnitTest` | PASS |
| `assembleDebug` | PASS |
| `assembleRelease` | PASS |
| `assembleDebugAndroidTest` | PASS |
| Current full local regression (`testDebugUnitTest lintDebug assembleDebug assembleRelease assembleDebugAndroidTest`) | PASS; `BUILD SUCCESSFUL` on the reviewed `db7dfbe` validation run; 13/13 unit tests |
| Release isolation scan | PASS |
| `connectedDebugAndroidTest` | PASS; executed on `XQ-AU52 - 12` / API 34; 24 of 24 tests passed in the `534bcb2` run |
| Compose Register filled fixture test | PASS at semantics scope; the current connected suite exercises the approved filled fixture, while the five normalized emulator captures are the visual evidence |
| CI-equivalent local run | PASS; `testDebugUnitTest lintDebug assembleDebug assembleRelease` completed successfully on 2026-09-09 |
| Authoritative CI | NOT VERIFIED; `.github/workflows/android.yml` now defines hosted unit/build/lint and API 34 emulator jobs, but no hosted run evidence is available yet |

## Visual Evidence

- Authoritative composite: SHA-256 `75cb578c58f3098c270290f033539ebf06c35c8b322efb71526fa4e53f3d3e9c`.
- Login/Register screens currently implement light surfaces, gradient/scroll behavior and interactive field structure.
- Figma node `2997:11541` was re-read through Figma MCP as supporting asset evidence. The runtime uses the composite-compatible skyline asset (SHA-256 `706d8030f380f99f2eebff389a0a91c9f84c82e142938da621fbe01fb9195213`) at the approved 736×246 placement; the Figma-only skyline candidate was rejected because its silhouette did not match the authoritative composite.

## Visual Comparison — Figma vs Emulator

- Reference: archived authoritative composite `docs/design/evidence/auth/auth-reference-2026-09-07.png`, SHA-256 `75cb578c58f3098c270290f033539ebf06c35c8b322efb71526fa4e53f3d3e9c`; Figma node `2905:2679` is supporting asset evidence only.
- Runtime capture: API 34 `Medium_Phone (AVD)`, serial `emulator-5554`, screenshot `1080×2400`, captured after installing the current debug APK from `2eab95a`; evidence file `docs/design/evidence/auth/figma-export-2026-09-08/runtime-login-after-composition.png`, SHA-256 `6f7309921aa90159bd863c6869694464d151e3ba8cb61c34ace9258813b8cb86`.
- Method: crop each runtime screenshot from system chrome and resize to the authoritative composite panel frame `402×874`; system chrome is removed before comparison and debug-only region `(0,0,175,48)` is excluded by `login-content-only-mask.json`.
- Observed differences: Android system chrome is removed, but debug-only `開發模式` remains inside the captured app canvas and is excluded from product comparison. The logo, centered form, action row and button are substantially aligned; the lower white block and skyline clipping/placement still differ from Figma.
- Result: `PARTIAL`. The skyline white block was removed with a multiply-blended runtime render and a new Login empty capture confirms the background continues behind the skyline. Runtime captures now exist for Login empty/filled/error and Register empty/filled. A reproducible content-only mask is now defined at `docs/design/evidence/auth/figma-export-2026-09-08/login-content-only-mask.json`: Android system chrome is removed during normalization, and the debug-only `開發模式` region `(x=0,y=0,w=175,h=48)` is excluded. Strict RGB comparison of the normalized Login capture against the 402×874 Figma reference measured MAE `29.2499`, RMSE `70.1947`, and over-threshold ratio `0.580866` at threshold 20 across `1,028,844` channel samples. This is numeric evidence, but not a visual PASS; the remaining composition differences require Debug/implementation follow-up.
- Re-capture after skyline anchor fix: runtime evidence `runtime-login-empty-after-skyline-anchor-fix.png` (SHA-256 `52f82e53e2ffe46e94a805120a158dbf9a8ceb43942724720f716d9863199067`) and normalized evidence `runtime-login-normalized-after-skyline-anchor-fix-402x874.png` (SHA-256 `e2ae5e08aabee38534540b3d0cd33098032ab4881148c253600bf506030be8db`). Using the same content-only mask and threshold `20`, strict RGB comparison measured MAE `28.9438`, RMSE `63.9785`, and over-threshold ratio `0.684666` across `1,028,844` channel samples. MAE and RMSE improved versus the prior capture, but the over-threshold ratio increased; visual verification therefore remains `PARTIAL`, not PASS.
- Figma MCP re-read confirmed the skyline source is the exact `736×246` vector asset (`figma-89efaa36-f383-42a9-8937-bf07022c332d.svg`), replacing the prior `402×239` raster approximation. It was rasterized at native dimensions for Android runtime consumption (SHA-256 `424ba33c8da0a8cdc29e8ee1c4559f459d9ee28bb93137e07ad7ce416f03f`). Post-replacement capture succeeded on API 34 after emulator recovery: `runtime-login-empty-after-exact-skyline.png`, 1080×2400, SHA-256 `8c18cfdc6c3d3fe3d05464e8c5af9305546c11e189bab88fbbcea8291f47362c`. Visual inspection confirms the lower background is continuous and the skyline is present without the prior white block.
- Exact-skyline revision normalized evidence: `runtime-login-normalized-exact-skyline-402x874.png`, SHA-256 `8b0f4ee8dce1869c06130ef3bcb5ba6083f4189cab8e051d872173e79c6e7311`. With the same content-only mask and RGB threshold `20`, the comparison measured MAE `28.1656`, RMSE `68.1859`, and over-threshold ratio `0.566433` across `1,028,844` channel samples. The revision numeric diff is complete; visual acceptance remains `PARTIAL`.
- The Figma-only skyline candidate was rejected because its silhouette did not match the authoritative composite. The composite-compatible skyline asset was restored (SHA-256 `706d8030f380f99f2eebff389a0a91c9f84c82e142938da621fbe01fb9195213`). After the shared external-label field fix, latest Login empty normalized evidence `runtime-login-empty-after-field-fix-normalized-402x874.png` (SHA-256 `5db8f4a30f3c84f6b3f0929260bedadf68f6a8cf263cb5d5b5351ccc67330b7f`) measures MAE `32.5681`, RMSE `70.8088`, and over-threshold ratio `0.236735` against the archived composite crop using threshold `20` and the content-only mask.
- Figma MCP node `2997:11541` was re-read and its skyline geometry confirmed as `736×246`, `left=-138`, `bottom=-7`. The runtime resource now uses the exact Figma raster (SHA-256 `424ba33c8da0a8cdc29e8ee1c4559f459d9ee28bb93137e07ad7ce416f03f`) at that placement. The current valid Login empty capture `runtime-login-empty-exact-skyline-current.png` has SHA-256 `f0bf1d45df3ea5896aa664130bccf87f4902ec48f06d38940784a47ba4c57a51`; normalized crop SHA-256 `4b912b657096ba4cac52dc40f6a61d14efb1ed0c695c38685319c6766541d9a8`. Content-only strict RGB diff at threshold `20`: MAE `35.5466`, RMSE `77.3607`, over-threshold ratio `0.220772` across `1,028,844` channels. The mismatch improved but remains `PARTIAL`.
- Final centered-canvas equivalent capture `runtime-login-empty-exact-skyline-final.png` SHA-256 `af7a0b22495b85c35cd52d88999a8c1473e5e43bd8203cc270a5a896973efb8d`; normalized SHA-256 `942fea52f124e8a5d66d5fe9400e11748473fb782aae1ea0e00734563cdd5d19`. The centered `29dp` offset is mathematically equivalent to Figma absolute `left=-138dp` for a 736dp canvas centered in a 402dp viewport. Strict masked diff is MAE `38.0054`, RMSE `80.5564`, over-threshold ratio `0.238680`; visual acceptance remains `PARTIAL`.
- After aligning Figma typography tokens (title Bold 28sp/38sp, labels Bold 16sp/24sp, button Bold 20sp/28sp, second title line tracking 5sp), the latest Login empty capture `runtime-login-empty-typography-exact.png` has SHA-256 `1dd6f846f95bab95347d6a02c6cfa05042b2dee48da7602d910b3bd1956f5dd6`; normalized SHA-256 `4e6f7fd7cdee2d41de6bf781c7fae1868c3f3ce6e8a30ff4a0300c35e3a623b2`. Strict masked diff improved to MAE `37.4843`, RMSE `79.5649`, over-threshold ratio `0.235615`; visual acceptance remains `PARTIAL`.
- After matching the Figma captcha row widths (`133dp` input, `139dp` image, `32dp` reload allocation), the latest Login empty capture `runtime-login-empty-fixed-row.png` has SHA-256 `90b2d900299f6c0a4302ff93a9091f2a79d87690fcddae20bd8e59525c5139bc`; normalized SHA-256 `c9e971d19d9f55448c1730637f2ae4dea0c89742b4d4a648e74a7323ac0639c8`. Strict masked diff improved to MAE `37.3815`, RMSE `79.4287`, over-threshold ratio `0.234985`; visual acceptance remains `PARTIAL`.
- After replacing the Material checkbox visual with the Figma `14dp` checkbox while retaining a `48dp` selectable semantics row, the latest Login empty capture `runtime-login-empty-checkbox-aligned.png` has SHA-256 `6ea4c1bfffa9f12f1e02772e4211883146238a67e0052fbaffe7756f9252508d`; normalized SHA-256 `e59b537227472e6a4a6389f1fa8e5ef81b4a43aca61452b7f4135d1e1746872f`. Strict masked diff improved to MAE `37.3206`, RMSE `79.3679`, over-threshold ratio `0.234505`; visual acceptance remains `PARTIAL`.
- After integrating the Figma password-eye asset, the latest Login empty capture `runtime-login-empty-eye-asset.png` has SHA-256 `aa6981ef6a0b9ace8b30343722ad467c3abab8421eb6667cb3f37c66254f0f28`; normalized SHA-256 `ae25270bf3dd95713178a363cb5437c5fe480708fcdea4ad294a9b72dc8c2a74`. Strict masked diff is MAE `37.3168`, RMSE `79.3559`, over-threshold ratio `0.234661`; visual acceptance remains `PARTIAL`.
- Latest Compose Login filled capture `runtime-login-filled-compose-eye-asset.png` has SHA-256 `73cb5f74383239c77edf79b5bdbc077df057601d398a7a8bf6f5489291782bc5`; normalized SHA-256 `afca9cda8d4160377d6bfc2d2ad206410358d3f52bb639b89f02569717842e27`; strict diff MAE `42.5147`, RMSE `87.0442`, over-threshold ratio `0.256640`. Latest auth-error capture `runtime-login-error-compose-eye-asset.png` has SHA-256 `b1083096aca706ccf7cd9935154ad49139e05a22f2f12d7ba12e543b332a63f9`; normalized SHA-256 `a6ca58b7e8ea7a8eb9f3797bee445b871f3738fe4d80a896e4c6c37a372fdbb3`; strict diff MAE `45.9906`, RMSE `90.7237`, over-threshold ratio `0.282392`. Both are now recorded and remain visual `PARTIAL`.
- Figma action-row correction runtime capture `runtime-login-empty-action-composition.png` has SHA-256 `148104c5554e6a6df55955c1061e8d805166869daa01403328b869ced6aae7b8` (1080×2400, API 34 `Medium_Phone`, current APK). Visual inspection confirms the action row now renders black `沒有帳號?` and blue `註冊`; the debug-only `開發模式` region remains excluded by the existing content-only mask. The capture is implementation evidence; strict normalized numeric comparison remains pending for this revision.
- Figma input-field token correction runtime capture `runtime-login-empty-field-tokens.png` has SHA-256 `764d8cfae4fe586943054fb5408ecdbbb995014d36e39952ebe013445f9911cc` (1080×2400, API 34 `Medium_Phone`, current APK). Visual inspection confirms white containers, light unfocused borders, and lighter placeholders matching the Figma empty state. The capture is implementation evidence; strict normalized numeric comparison remains pending for this revision.
- Direct Figma MCP export `figma-login-2997-11541-reference-402x874.png` has SHA-256 `bfde10cd297feb639ecc2b3fc4bc4964258bc4541174032e392c0a104e4ed2e5`; latest skyline-offset runtime normalized capture `runtime-login-empty-skyline-offset-minus4-normalized-402x874.png` has SHA-256 `f53e16df9a0fd7d9b60f6ead48f4e2956ca8ebbf31f4b42735d3e99e01d86d81`. With system chrome removed and debug-only `(0,0,175,48)` masked, direct Figma comparison at RGB threshold `20` measures MAE `25.6358`, RMSE `65.5339`, over-threshold ratio `0.178984` across `1,028,844` channel samples. The prior `29dp` skyline placement measured MAE `27.9475`, RMSE `68.8674`, ratio `0.192612`; the `-4dp` implementation is retained as the improved composition, while overall visual verification remains `PARTIAL`.
- Latest form/captcha geometry revision `runtime-login-empty-captcha-contract-normalized-402x874.png` has SHA-256 `a465f3da5d1207d74f7a58ac28edd33808c5c1759bc80979c866e4ad1e6e34dc`; direct masked comparison to the same Figma MCP export measures MAE `18.6097`, RMSE `54.0154`, over-threshold ratio `0.143084` across `1,028,844` channel samples. This improves on the prior field/skyline revision (`25.6358` / `65.5339` / `0.178984`) after applying the 8dp field gap, 48dp captcha row, explicit 139×48dp visual contract, and `-40dp` column offset. Overall verification remains `PARTIAL` pending other state/device gates.
- Latest skyline vertical-crop revision `runtime-login-empty-skyline-y7-normalized-402x874.png` has SHA-256 `e330ef7f66df5c0d3d05e01a169399009526008cbfe904085078dd6a27ef2b16`; direct masked comparison measures MAE `16.7023`, RMSE `50.6566`, over-threshold ratio `0.130918`. The skyline region MAE improved from `29.0437` at `1dp` to `25.7836` at `7dp`; overall verification remains `PARTIAL` pending filled/error/register state refresh and other device gates.
- Latest header-centered revision `runtime-login-empty-header-centered-normalized-402x874.png` has SHA-256 `ad975abff033a4040c96f815925e52928195e3de0fef5dc773e253d9522bfc07`; direct masked comparison measures MAE `14.7699`, RMSE `46.0134`, over-threshold ratio `0.122486`. Header band MAE improved from `40.1542` to `23.6690` after matching Figma's `justify-center` and removing the non-Figma weight expansion. Overall verification remains `PARTIAL` pending state-specific refresh and other gates.
- `AuthScreenTest.loginCaptchaVisualUsesFigmaBounds` now locks the Figma captcha geometry: `captcha-image` is 139×48dp and `captcha-refresh` is 32×48dp. The full connected suite is 35/35 PASS on `Medium_Phone` API 34; this is developer-validation evidence and does not change the overall visual `PARTIAL` result.
- Auth-error implementation now preserves the approved semantic split: credential field borders use `#C8320A`, while `login-request-error` renders with `errorText` `#E00000`. The full connected suite remains 35/35 PASS; state-specific visual comparison remains `PARTIAL` pending refreshed current-revision error evidence.
- Latest auth-error runtime evidence `runtime-login-auth-error-errorText-token.png` has SHA-256 `6b90ca34e8c589b77c17fab53d78e381cf30418543fafa8edd5662c56e924988`; normalized evidence `runtime-login-auth-error-errorText-token-normalized-402x874.png` has SHA-256 `6ded708de4e2076ad8ec3ac7394a5de90d95bf18a4604d8ed707f0f58e19fa48`. API 34 inspection confirms retained values, three error borders and the global error copy. Compared with the prior current-revision normalized error capture, the content-only delta is MAE `23.6279`, RMSE `62.9821`, over-threshold ratio `0.174492`; this is refreshed implementation evidence, not a visual PASS claim.
- Latest `b368f6c` typography revision evidence: Login empty `runtime-login-empty-after-typography-fix-normalized-402x874.png` measures MAE `36.5524`, RMSE `78.0503`, over-threshold ratio `0.240123`; Register empty measures MAE `16.0203`, RMSE `48.9439`, over-threshold ratio `0.118302`; Register filled measures MAE `17.1337`, RMSE `51.3162`, over-threshold ratio `0.122850`. All use archived composite crops, RGB threshold `20`, and the normalized content-only comparison process.
- Responsive evidence after the current Login implementation: narrow 720×2400 capture `runtime-login-narrow-fixed-720x2400.png` SHA-256 `a39b9f8fdb6b06688073dd4b3d3e58919fcd3faf0b0035c1b41b41ad1655aff8`; font-scale 1.3 capture `runtime-login-fontscale-1.3.png` SHA-256 `8c81bcaf0d2f87f26840e8974a5d725790f1a533718ddd499e33f3d74c9fb229`; IME-open capture `runtime-login-ime.png` SHA-256 `a0d22d44c937b42f3c45a814f59148cabebf5793cf15fd44e8b51baadb664939`. Emulator settings were restored to 1080×2400 and font scale 1.0 after capture.
- After Register back-control geometry fix `db8aca6`, current Register empty runtime evidence is `runtime-register-empty-db8aca6.png` SHA-256 `95eb89187e1e236657d8aae33d98605d082b816a160bbffebfadcbdb9fb5b763`; normalized crop SHA-256 `2b3012ea87e1b647a8150acfac2bbcdce7efb51e3604ee34374f05bef4b8df46`.
- The semantics-based Compose capture for Register filled after `db8aca6` is retained as `runtime-register-filled-compose-db8aca6.png` SHA-256 `62290f8d1629f53c7a2dba53749b38f696c0ad2e7e25a50afc33896aa4c2179e`; normalized crop SHA-256 `ef0796b96eb47bf5c010704127f234958da20b4fae19c34d0fbd3228535f15ac`. Content-only strict RGB diff at threshold `20`: MAE `15.7032`, RMSE `48.3374`, over-threshold ratio `0.115161` across `1,028,844` channels. Visual result remains `PARTIAL` pending approved device matrix.
- Register filled responsive capture at 720×2400 is `runtime-register-filled-narrow-720x2400.png`, SHA-256 `39afbe962ede93c84ba3599e569d216185c9ef84482dd8d9d0a003d4b8273f8f`; all fields, vendor selection, work type, name, and action buttons remain visible without horizontal clipping. Emulator size was restored to 1080×2400.
- The reviewed revision includes Compose capture coverage for Register filled and Login filled/error states. The connected suite remains PASS (33/33); the captures are retained as repository evidence below.
- Latest valid emulator Login filled capture was obtained from `com.example.tp_ncolso_android/.MainActivity` after installing the current debug APK. Full capture `runtime-login-filled-current-revision.png` SHA-256 `590d3f875ba1708c6107896c9661f13e3c9c55b15094694ca670c04d5ebd07be`; normalized crop `runtime-login-filled-current-revision-normalized-402x874.png` SHA-256 `86e56b6e42ebf9d2f97f0b6a770bd42d6f1e5d33b8c493e9a3982b3ce4be1b77`. The screenshot confirms retained account/password/captcha values.
- Latest valid Login auth-error capture was obtained with `wronguser` / non-matching credentials and the current debug APK. Full capture `runtime-login-error-current-revision.png` SHA-256 `644d25608cb67cb583c962be4308818ee37bdb5df42e4b4d14ccc2d62c4daba0`; normalized crop `runtime-login-error-current-revision-normalized-402x874.png` SHA-256 `f40b3fe00cdd6d4cc912c78ffdd5c78f7d4b0a8c281a5e125883fe4fe0f3b59f`. With the content-only mask and RGB threshold `20`, strict comparison measured MAE `41.0997`, RMSE `84.0536`, and over-threshold ratio `0.260942` across `1,028,844` channel samples. The runtime state visibly retains all submitted values and shows the global error message; the numeric result remains visual `PARTIAL`, not PASS.
- The `98435c7` typography captures are historical evidence only. The earlier Login empty normalized image from that run was identified as a splash capture and is superseded by the stable post-launch capture below; the Login auth-error result from that run remains historical and `PARTIAL`.
- The earlier `runtime-login-empty-98435c7-normalized-402x874.png` was identified as a splash capture and is superseded. A stable post-launch capture is now recorded as `runtime-login-empty-98435c7-stable.png` SHA-256 `39c9b68085d810c1862264bc25a621b54a23acc3458e0774cac007f194d18f6e`, normalized SHA-256 `3df72e4908efc249a65c407f762b2127875ab0dd0ee4d926e8503520e550d55b`. Masked strict RGB diff: MAE `38.0054`, RMSE `80.5564`, over-threshold ratio `0.238680` across `1,028,844` channels.
- Complete UIR traceability for `UIR-LOGIN-001–010` and `UIR-REG-001–011` is recorded in `docs/tasks/UI-002-auth/uir-mapping.md`, with implementation/test/evidence references and explicit `PASS` or `PARTIAL` status for every requirement.
- Current-revision state comparisons against the archived composite crops are now recorded: Login filled MAE `37.4054`, RMSE `79.9405`, over-threshold ratio `0.702739`; Login auth-error MAE `38.8138`, RMSE `81.2323`, over-threshold ratio `0.737983`; Register empty after layout fix MAE `15.5036`, RMSE `47.7196`, over-threshold ratio `0.340352`; Register filled after layout fix MAE `16.5684`, RMSE `50.0627`, over-threshold ratio `0.353985`. These results are evidence of the remaining visual mismatch, not PASS claims.

## State Comparison Captures

| State | Evidence |
|---|---|
| Login empty | `runtime-login-empty-after-skyline-fix.png` — SHA-256 `e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855` |
| Login filled | `runtime-login-filled-after-exact-skyline-normalized-402x874.png` — SHA-256 `322f7a1def11dfc26080dfb301302f6897df595ff1fa6d8e0563ec9428e5e754` |
| Login error | `runtime-login-error-after-exact-skyline-normalized-402x874.png` — SHA-256 `708fdfa04806b062ae8449b55d342c342d681b305b0243d19737ba046a4b894d` |
| Register empty | `runtime-register-empty-after-layout-fix-normalized-402x874.png` — SHA-256 `b60dadf8566ffcd31a13dd83906c17dba2775b512333db38ab4b66ab40211b8d` |
| Register filled | `runtime-register-filled-after-layout-fix-normalized-402x874.png` — SHA-256 `457b6b1beeef8c172a17e818c77b0b67133bf0669f0438724dfb6406093a647a` |

## Overall Result

`FAIL`

Revision `db7dfbe` passes the local build and test gates, but the completed current-revision five-state comparison proves residual Login/Register visual nonconformance. The failed acceptance criteria are `AC-UI002-009` and `AC-UI002-010`; the formal route is `debug`. Hosted CI and the remaining cross-task/device/state evidence are additional `NOT VERIFIED` limitations.

## Next Action

Route the proven visual mismatches to `debug`, implement only the approved visual correction scope, then rerun the five-state comparison and the developer/hosted CI gates. Physical-device testing remains out of scope.
