# Execution Report

Task: UI-002-auth
Plan Review: iteration 19, approved
Plan revision: 18
Knowledge baseline: `KB-UI-002-AUTH-R11`
Date: 2026-10-14 (Asia/Taipei)

## Revision and Isolation

- Task branch: `codex/ui-002-auth-r11`
- Base commit: `d74bcd8192e6ea6e2d06306867af74f198c20580`
- Working tree: uncommitted; only UI-002 auth source/test/resource changes are included in this execution scope. Other task documentation changes remain unstaged and are excluded from any future UI-002 commit.
- R10 Auth implementation originated at `48670ffe9e33b19aea23709abf2f47cc5ed85ff1` and is the behavior being updated.

## Changes

- Removed CAPTCHA fields, events, validation, provider, fixture, UI, preview data, and test dependencies from Login.
- Changed the replaceable Login data-source contract and debug fake to accept account/password only.
- Updated authentication failure copy to `帳號或密碼錯誤` and applies auth-error borders only to account/password.
- Removed the CAPTCHA row; the lower controls now reflow with UI-001 spacing tokens. Added a Compose assertion for absence and relative placement/spacing.
- Kept `rememberMe` as UI state. INT-001 owns the approved restart behavior: unchecked does not persist authenticated state across restart; checked restores the session using its approved secure Token store.

## R11 Plan-to-Source Alignment Review

- Read-only comparison against approved plan revision 18 confirms the implementation diff removes the CAPTCHA UI/state/validation/provider/fixture from main/debug/test surfaces, keeps the fake Login request at account/password only, updates the auth error copy, and reflows the three lower controls using UI-001 spacing tokens.
- The updated Compose test contains CAPTCHA absence assertions and relative placement plus 16dp spacing assertions for the lower controls. `AuthScreenTest` passed 17/17 on `Medium_Phone_API_36.0` (emulator-5554), API 36.
- The R11 runtime screenshot is `r11-login-runtime.png` (1080×2400, density 420 dpi, font scale 1.0; SHA-256 `F573401B73D354EDB364A3937ECD14991B75DBD6F2C53099A28E6B96AAE2C2A4`). Visual inspection confirms CAPTCHA absence and the lower controls reflow; pixel comparison of unchanged regions against the R10 source composite remains NOT VERIFIED.
- Cross-Task boundary remains explicit: this UI Task retains `rememberMe` as form state and does not persist Tokens. The current `AuthDataSource.login(account, password)` port does not carry the selected flag; INT-001 must plan the approved session/persistence handoff without adding the flag to the HTTP body. This is recorded in INT-001 Collection (`SRC-INT001-007`, `INT001-ISSUE-006`).

## Developer Validation

| Check | Command / Environment | Result |
|---|---|---|
| Whitespace and patch integrity | `git diff --check`, Windows PowerShell, base `d74bcd8` | PASS; only Git line-ending normalization warnings for YAML/Markdown |
| CAPTCHA source scan | `rg -n "captcha|Captcha|驗證碼" app/src` | PASS for implementation removal; remaining matches are four negative Compose assertions that verify absence |
| JVM auth tests | `testDebugUnitTest --tests '*Auth*'`; JBR OpenJDK 25.0.3, Gradle 9.6.0 | PASS; AuthValidation 3/3, AuthViewModel 4/4, DebugAuthSessionCoordinator 2/2 |
| Compose connected tests | `connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.tp_ncolso_android.feature.auth.AuthScreenTest`; emulator-5554, API 36 | PASS; 17/17 |
| Lint and variants | `lintDebug assembleDebug assembleRelease assembleDebugAndroidTest` | PASS |
| Release source isolation | Scan `app-release-unsigned.apk` entries for `DebugAuth`, `Captcha`, `login_captcha` | PASS; no matching entries |
| R11 runtime screenshot | `r11-login-runtime.png`; emulator-5554, API 36, 1080×2400, density 420 dpi, font scale 1.0 | Capture and visual observation PASS; unchanged-region pixel comparison against source composite NOT VERIFIED |
| Full app JVM suite | `testDebugUnitTest` | FAIL outside Auth scope: `FakeReturnFlowStoreTest.confirmationChangesStatusRemovesReturnContextAndHidesNotification`, assertion at test line 76; filtered Auth suite passes |
| Full app connected suite | `connectedDebugAndroidTest` | Aborted after unrelated `SurveyFormScreenTest.cleanCloseSkipsDiscardDialogAndRouteBackIsDisabledWhenNotForeground` failure; AuthScreenTest was rerun separately and passed 17/17 |

On 2026-10-14 the requester authorized `C:\Program Files\Android\Android Studio1\jbr` as the Windows Gradle JBR. The Runtime Rule now lists this Windows path alongside the macOS path and preserves the no-fallback requirement. Android SDK Platform 37 was installed into the existing SDK at `C:\Users\GT.YANG\AppData\Local\Android\Sdk`; command-local `ANDROID_HOME`/`ANDROID_SDK_ROOT` were used. JBR identity and commands are recorded in `infrastructure.md`.

## Outcome and Next Action

- Auth implementation, task-scoped local tests, lint and variant builds pass. Full app regressions include unrelated failures; unchanged-region pixel comparison and full AC verification remain incomplete.
- R11 verification remains NOT VERIFIED until the outstanding regression and visual comparison evidence are resolved/reviewed.
- Hosted CI remains the previously recorded R10 failure; no R11 CI has run. The hosted gate is deferred for development scope with reactivation before release-candidate validation.
- Current action: `investigation`. Classify the app-wide failures and complete the remaining visual comparison before Code Review and Verification.
- Commit `65daddc1b7ad9ef05d9033fa1e485d901a4d1d8f` on `codex/ui-002-auth-r11` was pushed to `origin`. Code Review and Verification remain pending.
