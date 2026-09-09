# UI-002 Verification Report

## Inputs

- Requirement baseline: `KB-UI-002-AUTH-R10`
- Plan Review: revision 16, `APPROVED`
- Branch: `UI-002feat`
- Reviewed implementation/evidence revision: `a5f19d8` (`verify(UI-002): complete normalized diff and UIR mapping`)
- Runtime: Android Studio JBR, OpenJDK 25.0.3

## Verification Round 3 — 2026-09-08

- Device: `Medium_Phone (AVD) - 14`, ADB serial `emulator-5554`
- Command: `JAVA_HOME='/Applications/Android Studio.app/Contents/jbr/Contents/Home' ./gradlew connectedDebugAndroidTest`
- Result after fix: `PASS`; 17 tests executed, 0 failed, 0 errors, 0 skipped.
- Passing groups: `FoundationComponentTest` 9/9; `AuthScreenTest` 5/5; `DebugDirectLoginTest` 2/2; app context 1/1.
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
| AC-UI002-007 | NOT VERIFIED | Scroll containers exist, but narrow-width, font-scale and IME behavior has no executed device evidence. |
| AC-UI002-008 | PASS (executed test scope) | Preview matrix and connected auth screen tests are present; full visual state matrix evidence remains pending. |
| AC-UI002-009 | PARTIAL | Composite-based Login empty/filled/error normalized comparisons are now recorded; measured diff remains above visual PASS and auth-error implementation/state evidence still needs resolution. |
| AC-UI002-010 | PARTIAL | UIR mapping is complete and Register empty has a post-layout-fix normalized comparison; Register filled strict comparison and remaining accessibility/privacy/device evidence remain incomplete. |
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
| `connectedDebugAndroidTest` | PASS; executed on `Medium_Phone (AVD) - 14` / API 34; 17 of 17 tests passed |
| Authoritative CI | NOT VERIFIED; repository has no configured CI workflow/provider |

## Visual Evidence

- Authoritative composite: SHA-256 `75cb578c58f3098c270290f033539ebf06c35c8b322efb71526fa4e53f3d3e9c`.
- Login/Register screens currently implement light surfaces, gradient/scroll behavior and interactive field structure.
- Figma node `2997:11541` was re-read through Figma MCP. The exact exported logo and skyline are now runtime-consumed at the approved 96×65 header and 736×246 bottom placement; runtime asset SHA-256 values are recorded in the asset manifest. Pixel comparison evidence remains pending.

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
- Latest `b368f6c` typography revision evidence: Login empty `runtime-login-empty-after-typography-fix-normalized-402x874.png` measures MAE `36.5524`, RMSE `78.0503`, over-threshold ratio `0.240123`; Register empty measures MAE `16.0203`, RMSE `48.9439`, over-threshold ratio `0.118302`; Register filled measures MAE `17.1337`, RMSE `51.3162`, over-threshold ratio `0.122850`. All use archived composite crops, RGB threshold `20`, and the normalized content-only comparison process.
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
