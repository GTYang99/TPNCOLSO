# UI-002 Verification Report

## Inputs

- Requirement baseline: `KB-UI-002-AUTH-R10`
- Plan Review: revision 16, `APPROVED`
- Branch: `UI-002feat`
- Reviewed implementation/evidence revision: `2d5d6a7` (`fix(UI-002): match Login captcha row geometry`)
- Runtime: Android Studio JBR, OpenJDK 25.0.3

## Verification Round 3 — 2026-09-08

- Device: `Medium_Phone (AVD) - 14`, ADB serial `emulator-5554`
- Command: `JAVA_HOME='/Applications/Android Studio.app/Contents/jbr/Contents/Home' ./gradlew connectedDebugAndroidTest`
- Result after fix: `PASS`; 31 tests executed, 0 failed, 0 errors, 0 skipped.
- Passing groups: `FoundationComponentTest` 8/8; `AuthScreenTest` 20/20; `DebugDirectLoginTest` 2/2; app context 1/1.
- Previous four failures were reproduced and fixed: direct-login tests now enter the existing Debug mode first; Login keeps the remember/register action row visible in the viewport and uses a width-safe checkbox layout.
- Evidence: [connected test report](../../app/build/reports/androidTests/connected/debug/index.html) and `app/build/outputs/androidTest-results/connected/debug/TEST-Medium_Phone(AVD) - 14.xml`.

## Acceptance Criteria

| AC | Current result | Evidence / limitation |
|---|---|---|
| AC-UI002-001 | PASS (connected scope) | Login entry points and semantics pass `AuthScreenTest` on API 34; visual comparison remains pending. |
| AC-UI002-002 | PASS (unit scope) | `AuthValidationTest` and `AuthViewModelTest` cover required fields, captcha format and submit rejection. |
| AC-UI002-003 | PASS (unit scope) | ViewModel tests cover success effect, sensitive clearing and auth-error retention; debug coordinator guards duplicate session start. |
| AC-UI002-004 | PASS (connected scope) | Register route, six fields, Back and Cancel events pass `AuthScreenTest` on API 34. |
| AC-UI002-005 | PASS (unit scope) | Pure validation tests cover account, password, confirmation, vendor, work type and Chinese name rules. |
| AC-UI002-006 | PARTIAL | Debug coordinator and direct-login/logout tests pass on API 34; UI-003 drawer integration remains out of scope. |
| AC-UI002-007 | PARTIAL | Emulator evidence now covers 720×2400 narrow width, font scale 1.3, and IME-open state; the header was corrected to constrain the title with weighted remaining width. Evidence remains emulator-only and needs the full approved device matrix before PASS. |
| AC-UI002-008 | PASS (executed test scope) | Preview matrix and connected auth screen tests are present; full visual state matrix evidence remains pending. |
| AC-UI002-009 | PARTIAL | Composite-based Login empty/filled/error normalized comparisons are recorded; measured diff remains above visual PASS. Auth-error retention, accessible global error, and current runtime evidence are complete. |
| AC-UI002-010 | PARTIAL | UIR mapping is complete; Register empty and filled normalized comparisons are recorded. Remaining device-matrix evidence is incomplete. |
| AC-UI002-011 | PARTIAL | Logout coordinator/idempotency unit evidence exists; UI-003 drawer contract integration is not executed. |

## Build and Test Evidence

| Check | Result |
|---|---|
| `testDebugUnitTest` | PASS |
| `assembleDebug` | PASS |
| `assembleRelease` | PASS |
| `assembleDebugAndroidTest` | PASS |
| Current full local regression (`testDebugUnitTest assembleDebug assembleRelease assembleDebugAndroidTest`) | PASS; `BUILD SUCCESSFUL` on 2026-09-08 |
| Release isolation scan | PASS |
| `connectedDebugAndroidTest` | PASS; executed on `Medium_Phone (AVD) - 14` / API 34; 31 of 31 tests passed |
| Compose Register filled capture test | PASS; `AuthScreenTest.captureRegisterFilledStateForVisualEvidence` rendered the filled Register state and wrote a non-empty PNG plus an emulator screenshot to `/sdcard` |
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
- Latest `b368f6c` typography revision evidence: Login empty `runtime-login-empty-after-typography-fix-normalized-402x874.png` measures MAE `36.5524`, RMSE `78.0503`, over-threshold ratio `0.240123`; Register empty measures MAE `16.0203`, RMSE `48.9439`, over-threshold ratio `0.118302`; Register filled measures MAE `17.1337`, RMSE `51.3162`, over-threshold ratio `0.122850`. All use archived composite crops, RGB threshold `20`, and the normalized content-only comparison process.
- Responsive evidence after the current Login implementation: narrow 720×2400 capture `runtime-login-narrow-fixed-720x2400.png` SHA-256 `a39b9f8fdb6b06688073dd4b3d3e58919fcd3faf0b0035c1b41b41ad1655aff8`; font-scale 1.3 capture `runtime-login-fontscale-1.3.png` SHA-256 `8c81bcaf0d2f87f26840e8974a5d725790f1a533718ddd499e33f3d74c9fb229`; IME-open capture `runtime-login-ime.png` SHA-256 `a0d22d44c937b42f3c45a814f59148cabebf5793cf15fd44e8b51baadb664939`. Emulator settings were restored to 1080×2400 and font scale 1.0 after capture.
- After Register back-control geometry fix `db8aca6`, current Register empty runtime evidence is `runtime-register-empty-db8aca6.png` SHA-256 `95eb89187e1e236657d8aae33d98605d082b816a160bbffebfadcbdb9fb5b763`; normalized crop SHA-256 `2b3012ea87e1b647a8150acfac2bbcdce7efb51e3604ee34374f05bef4b8df46`.
- The semantics-based Compose capture for Register filled after `db8aca6` is retained as `runtime-register-filled-compose-db8aca6.png` SHA-256 `62290f8d1629f53c7a2dba53749b38f696c0ad2e7e25a50afc33896aa4c2179e`; normalized crop SHA-256 `ef0796b96eb47bf5c010704127f234958da20b4fae19c34d0fbd3228535f15ac`. Content-only strict RGB diff at threshold `20`: MAE `15.7032`, RMSE `48.3374`, over-threshold ratio `0.115161` across `1,028,844` channels. Visual result remains `PARTIAL` pending approved device matrix.
- Register filled responsive capture at 720×2400 is `runtime-register-filled-narrow-720x2400.png`, SHA-256 `39afbe962ede93c84ba3599e569d216185c9ef84482dd8d9d0a003d4b8273f8f`; all fields, vendor selection, work type, name, and action buttons remain visible without horizontal clipping. Emulator size was restored to 1080×2400.
- The reviewed revision `173b2ec` includes the semantics-based Compose capture coverage for Register filled state and the latest regression evidence binding. The connected suite remains PASS (31/31); the capture is retained as repository evidence below.
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

`PARTIAL`; the exact-skyline Login revision has complete normalized numeric evidence and complete UIR traceability, but the measured diff and several state/device-matrix requirements remain below PASS.

## Next Action

Resolve the remaining pixel-diff deltas and complete responsive／IME／logout evidence; UIR-LOGIN/UIR-REG mapping is now recorded. CI remains intentionally deferred by requester decision. Physical-device testing is out of scope.
