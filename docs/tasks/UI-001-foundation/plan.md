# Implementation Plan

Plan revision: 3 (post-infrastructure diagnosis)

## Goal
- 建立可重用 Compose foundation contract、debug-only 直接登入與可替換 app session，讓 API 未完成時仍可開發受保護 UI，且 release 完全隔離旁路。

## Scope
- Reusable foundation contract、semantic theme、共用 components／presentation states、Session contract、AppRoot、debug direct-login、release unauthenticated entry、Android 9 minSdk 與對應 tests；不建立正式 Login、Map 或 API。
- Production-code work begins only after an authoritative Git root、baseline commit and isolated task branch are verified.

## Affected Files
- `app/src/main/java/com/example/tp_ncolso_android/MainActivity.kt`
- `app/src/main/java/com/example/tp_ncolso_android/AppRoot.kt`
- `app/src/main/java/com/example/tp_ncolso_android/session/AppSession.kt`
- `app/src/main/java/com/example/tp_ncolso_android/ui/foundation/theme/`
- `app/src/main/java/com/example/tp_ncolso_android/ui/foundation/component/`
- `app/src/main/java/com/example/tp_ncolso_android/ui/foundation/state/ContentState.kt`
- `app/src/main/java/com/example/tp_ncolso_android/ui/foundation/preview/`
- `app/src/debug/java/com/example/tp_ncolso_android/AppEntry.kt`
- `app/src/release/java/com/example/tp_ncolso_android/AppEntry.kt`
- `app/src/debug/res/values/strings.xml`
- `app/src/main/res/values/strings.xml`
- `app/src/test/java/com/example/tp_ncolso_android/session/AppSessionTest.kt`
- `app/src/androidTest/java/com/example/tp_ncolso_android/DebugDirectLoginTest.kt`
- `app/src/androidTest/java/com/example/tp_ncolso_android/ui/foundation/`
- `app/build.gradle.kts`
- `gradle/libs.versions.toml`
- `docs/assets/app-ui-assets.md`
- `docs/tasks/UI-001-foundation/foundation-contract.md`

## Implementation Steps
- 1. Infrastructure 恢復 authoritative Git checkout/baseline 後，確認 repository root、remote／provenance、clean isolation，並依 Git rule 建立 `feature/UI-001-foundation-compose`；任一檢查未通過即停止。
- 2. 將 minSdk 調整為 28，並在 version catalog 與 app module 直接宣告 public `StateFlow` contract 使用的 coroutine dependency，確認 debug／release source sets 均可編譯。
- 3. 依 `foundation-contract.md` 與核定 Figma 建立 semantic theme roles，記錄 source node／取得日期；consumer 不得存取 raw values。
- 4. 建立共用 fields、buttons、selection、status、loading／empty／error／readonly components 與 `ContentState`，確保全部為 stateless public APIs。
- 5. 建立 `AppRole`、`AppIdentity`、`AppSessionState`、`AppSessionOwner` 與無 Token 的 in-memory debug owner，提供 start／clear 並防止重複登入事件。
- 6. 建立共用 `AppRoot`，只依 session state 選擇 unauthenticated／authenticated content，內容由 caller 注入。
- 7. 在 debug source set 建立角色 selector、「直接進入」按鈕、fake identity 與 authenticated placeholder／登出。
- 8. 在 release source set 建立不含 bypass 的 unauthenticated entry，讓 `MainActivity` 不需要 runtime debug branch。
- 9. 將 `MainActivity` 改為只套用 `AppTheme` 並呼叫 variant-specific `AppEntry()`。
- 10. 建立 shared brand asset manifest，保存可重現的正式 Figma assets；feature-only asset ownership 明確交給 consumer task，不得從 screenshot 重畫。
- 11. 新增 foundation Preview／UI tests、visual comparison evidence 與 session unit／direct-login tests，覆蓋 contract 所列狀態和 semantics。
- 12. 執行 unit tests、debug/release assemble、instrumented tests（環境可用時）與 release source inspection，記錄 limitations。
- 13. 完成 local validation 後提交單一 task revision，記錄 branch／commit／diff，再交 Code Review、CI 與 independent Verification；任何 required check 不可用均維持 `NOT VERIFIED`。
- 14. 更新 execution report、verification、state 與 UI-002 handoff；不得在 Git／CI 不可用時宣告 done。

## Test Plan
- Unit：三角色 session 建立、重複 start、clear、無 Token／credential fields。
- Foundation：component state Previews、semantics、required field、keyboard action／single-line behavior、loading／disabled click suppression、password masking、radio exclusivity、minimum touch target。
- Compose UI：初始未登入、角色切換、直接進入、fake identity 顯示、登出返回。
- Build：`assembleDebug`、`assembleRelease`、affected unit tests。
- Git gate：repository root、authoritative provenance、task branch、commit ID 與 diff scope 均須可重現。
- Release isolation：檢查 release source／artifact 不含 debug direct-login entry 或其字串資源。
- Instrumented：可用 emulator 上執行 `DebugDirectLoginTest`。
- Visual：以核定 Figma node／取得日期為基準，驗證 semantic token mapping、共用品牌 asset bytes 與 representative component states。

## Regression Plan
- 既有 theme Preview 與 template tests 仍可編譯。
- edge-to-edge 與 `MainActivity` 啟動無 crash。
- UI-002／UI-003 接入時不需修改 session data shape或引用 debug source。
- UI-002 不需複製 foundation field、button、state或 semantics implementation。

## Risks
- release 若能建立 fake session或看到「直接進入」，Plan Review／Verification 必須判定 blocking failure。
- 無 Git metadata，commit、CI 與 independent committed-revision Verification 將維持 `NOT VERIFIED`。

## Rollback Plan
- 回退 UI-001 task changes即可恢復原始 `Greeting` starter；無資料遷移或外部狀態。

## Current Behavior 
- App 只顯示 `Hello Android!`，API 未完成時沒有進入後續畫面的方式。

## Expected Behavior
- debug build 可選 fake 角色直接登入並登出；release build 沒有任何直接登入旁路。
- 後續 Auth、Map 與其他 UI 可重用 semantic theme、共用 components／states，並替換 AppRoot 兩側內容而不修改 session foundation。

## Acceptance Criteria Traceability
| AC | Implementation Step | Validation |
|---|---|---|
| AC-UI001-001 | 6, 7, 9 | Debug Compose UI initial-state test |
| AC-UI001-002 | 5, 7, 11 | Three-role／single-start tests |
| AC-UI001-003 | 5, 7, 11 | Logout／relaunch tests |
| AC-UI001-004 | 8, 12 | Release assemble／source and artifact inspection |
| AC-UI001-005 | 5, 6 | AppRoot contract unit／Compose test |
| AC-UI001-006 | 11, 12 | Unit and instrumented test reports |
| AC-UI001-007 | 2, 12 | Gradle config check；debug/release builds |
| AC-UI001-008 | 3–6, 14 | Contract compile fixture／UI-002 handoff review |
| AC-UI001-009 | 3, 4, 11, 12 | Preview inventory and foundation component UI tests |
| AC-UI001-010 | 3, 10, 11, 14 | Token／asset manifest review and visual comparison evidence |

## Failure Behavior
- session start 已在 authenticated state：忽略重複事件，不建立第二份 session。
- debug logout：立即清除 in-memory session並回到開發入口。
- release auth 尚未接入：保持 unauthenticated host，不自動登入、不建立 fake user。

## Security and Privacy
- 直接登入只編譯於 debug source set；release 不引用 debug class／resources。
- fake identity 不使用真實姓名、帳號、密碼或 Token；session 不持久化、不寫 log。
- 若 release artifact 發現 bypass，停止 Verification 並回到 Implementation。

## Open Questions
- Repository provenance：應提供既有 authoritative repository／完整 checkout，或由 repository owner 明確授權目前資料夾建立新的 canonical Git history與 remote／CI ownership。
- 正式 API schema、Navigation 與 DI 為後續 Task inputs；正式 Figma UI 已核定。
