# Plan Review

Task: UI-009-ui-hardening
Reviewer: Codex Plan Review（同一工作階段；修訂後再次檢視）
Review Iteration: 2
Review Date: 2026-10-08

---

# Summary

## Review Result

- [x] APPROVED
- [ ] REQUEST_CHANGES
- [ ] BLOCKED

## Summary

計畫已對應核准基線 `KB-UI-009-UI-HARDENING-R1` 與 AC-001 至 AC-007。第一輪發現程序死亡測試與 debug session 記憶體狀態不相容；計畫已加入 AndroidTest 專用流程宿主、組態重建與實際程序死亡測試步驟，並明定受限證據不得當作 PASS。範圍、安全界線、回歸、回復方式及 hosted CI 延後責任均已記錄，可進入 Implementation。

## Reviewed Identity

- Knowledge baseline: `KB-UI-009-UI-HARDENING-R1`
- Requirement: `requirement.md`（核准版本）
- Plan: `plan.md`（本次修訂版；程序死亡測試宿主與執行步驟已具體化）
- Branch: `developer`

---

# Review Checklist

| Item | Result | Notes |
|------|------|------|
| Requirement understood | PASS | 涵蓋 UI-002–UI-008，維持既有 Task 行為及 ownership。 |
| Acceptance Criteria complete | PASS | AC-001 至 AC-007 均有實作與驗證追蹤。 |
| Repository analysis complete | PASS | 列出目前 state owner、測試及 `remember`／saved-state 缺口。 |
| Architecture impact reasonable | PASS | 沿用 Compose、MVVM、fake source；不新增 DI、navigation 或資料層。 |
| Affected modules identified | PASS | 列出相關 ViewModel、畫面、測試及必要依賴。 |
| Dependencies identified | PASS | lifecycle saved-state 支援僅在現有 API 不足時補齊。 |
| Risks evaluated | PASS | 覆蓋 Bundle 容量、失效 URI、CameraX 重建及 API 差異。 |
| Test Plan complete | PASS | 包含 viewport、字級、IME、saved-state、組態與程序死亡測試。 |
| Regression Plan complete | PASS | 覆蓋 UI-002–UI-008 已有行為與 source-set 邊界。 |
| Open Questions documented | PASS | Figma revision／像素 parity 限制已清楚排除於本 Task。 |
| Implementation steps actionable | PASS | 逐流程檢視、狀態接線、缺口修正及證據記錄皆有步驟。 |
| Task size appropriate | PASS | 跨畫面工作以矩陣和逐 AC 證據限制範圍。 |
| Rollback strategy (if applicable) | PASS | 僅回復 UI-009 變更至核准 revision，保留其他工作目錄修改。 |

---

# Findings

## Finding 1

Severity:
- [ ] Critical
- [x] Major
- [ ] Minor
- [ ] Suggestion

Category: 程序生命週期／驗證可行性

Description: 第一輪計畫要求程序死亡後從原路由驗證，但目前 `AppEntry` 以一般 `remember` 建立 `DebugSessionOwner`；程序重啟會回到 signed-out，無法直接證明 UI-003–UI-008 的路由狀態恢復。

Recommendation: 不把測試綁在 debug session 身分上；增加 AndroidTest 專用宿主直接掛載 Route，明確列出系統保存 Task、終止 app process、從 Recents 恢復及核對狀態的操作。旋轉或 `ActivityScenario.recreate()` 不可代替程序死亡證據。

Planning Response: 已於 `plan.md` 修訂影響檔案、實作步驟與測試計畫：新增 androidTest source set 專用流程宿主，以測試身分直接掛載 Route；補入組態重建及 emulator 程序死亡／Recents 恢復的分開步驟。若系統未保留 Task，證據記為受限，不得報 PASS。

Status:
- [ ] Open
- [x] Resolved

---

# Blocking Issues

None.

# Improvement Suggestions

- 執行時為每個 API／viewport／字級組合記錄實際裝置設定與截圖或測試輸出，讓 AC 證據可追溯。

# Decision

## APPROVED

計畫已符合 Implementation 進場條件。

# Next Action

- [ ] Planning
- [x] Implementation
- [ ] Requirement Clarification
- [ ] Human Review

# state.yaml Update

```yaml
phase: planning
status: plan_in_progress
planning:
  status: completed
plan_review:
  status: approved
  result: APPROVED
  iteration: 2
implementation:
  status: pending
next_action: implementation
```

# Review Notes

本次審查與計畫修訂由同一工作階段完成；專案規則未要求獨立審查者。正式 Figma revision 未知仍是已登錄限制，本 Task 不主張正式像素 parity。Hosted CI 依 development completion scope 延後至 release candidate，owner 與重啟里程碑已列入 state 與 plan。

---

# Implementation Re-entry Review — Iteration 3

- Review date: 2026-10-08
- Reviewer: same-agent implementation review; independent reviewer unavailable.
- Result: `CHANGES_REQUESTED` for the next implementation entry.
- Reviewed inputs: current `plan.md`, `StateRestorationHostTest`, and partial execution evidence at source revision `06092e8566887def8363418e8dee092e40364e54` plus uncommitted changes.

| Finding | Severity | Evidence | Required response |
|---|---|---|---|
| PR-UI009-003 | P1 | The implemented AndroidTest host uses `ActivityScenario.recreate()` and is not an independently launchable persistent task. It cannot perform the planned background → `am kill` → Recents restoration sequence while keeping an observer available. The approved plan specifies the sequence but does not define a launchable test route or an observer that survives the target app process. | Revise the test architecture and exact launch/observe procedure; keep process-death and configuration-recreation claims separate. Re-review before resuming Implementation. |
| PR-UI009-004 | P1 | The available emulator image used for evidence is API 34; the current SDK has no API 28 system image/AVD. | Record API 28 as an environment dependency and keep AC-002 NOT VERIFIED until API 28 evidence exists. |

The original iteration 2 approval applies to its reviewed plan version. It does not approve an amended process-death test design. Planning response is pending.

## Planning Response and Re-review — Iteration 3

- Plan revision: `plan.md` updated 2026-10-08 with a debug-only launchable state-restoration Activity and a separate UiAutomator runner procedure.
- Review result: `APPROVED` for implementation re-entry.
- Finding PR-UI009-003: resolved at planning level. The proposed target Activity runs in the app process while the AndroidTest instrumentation runner remains in its separate test process; the plan now directs UiAutomator to background the task, kill the target package process, restore from Recents, and inspect restored UI state. Execution evidence is still required for AC-006.
- Finding PR-UI009-004: accepted as an environment limitation. API 28 is explicitly required by AC-002; absence of its SDK image remains an open Infrastructure issue and must not be treated as PASS.
- Scope, saved-state security boundaries, and existing AC values are unchanged.

Implementation may resume for the approved plan. Do not mark API 28 or process-death criteria as passed until device evidence is recorded.

Subsequent execution disproved the process-death runner assumption above: `am kill` terminated the app process hosting AndroidJUnitRunner, so no post-restart assertion could run. The iteration 3 process-death method is superseded below.

## Implementation Re-entry Review — Iteration 4

- Review date: 2026-10-08
- Result: `APPROVED` for the revised external-host validation design.
- Evidence prompting revision: API 34 instrumentation output at `app/build/outputs/androidTest-results/connected/debug/TEST-Medium_Phone(AVD) - 14.xml` failed while the test Activity/runner was terminated by `am kill`; the log shows `Ui009StateRestoreActivity` was launched by AndroidJUnitRunner in the target process.
- Revised plan: `plan.md` assigns process termination and post-Recents observation to `scripts/ui009_process_death_check.sh`, run by the developer host through ADB. The script will launch a debug-only task with nonsensitive test markers, kill only the target process, restore its task from Recents, and inspect the accessibility dump. AndroidTest remains responsible for unit/Activity recreation paths only.
- Finding PR-UI009-003: resolved at planning level. The test observer now lives outside the target app process.
- API 28 image availability remains an open Infrastructure issue. AC-002 and AC-006 remain NOT VERIFIED until runtime evidence exists.
- Requirement values and saved-state security boundaries are unchanged.

Implementation may resume under this revised test design; successful compilation is not process-death verification.

## External Host Procedure Review — Iteration 5

- Review date: 2026-10-08
- Result: `APPROVED` for the revised Recents interaction.
- Finding PR-UI009-005: API 34 rejected `am start --activity-from-recents <id>` with `Unknown option: --activity-from-recents`. The plan now returns to the system Recents screen using `KEYCODE_APP_SWITCH`, identifies the most recent task card with an accessibility dump, and selects it by screen bounds. This is a system UI restoration path and keeps the observer in the developer host.
- API 28 remains an Infrastructure blocker; the plan still requires API 28 and API 34 runs.
- The requirement and approved restoration/security boundaries are unchanged.

Implementation may continue with the host script corrected to use the Recents UI. Do not claim AC-006 PASS until the restored accessibility dump has been checked on both required APIs.

# Definition of Done

- [x] All checklist items reviewed
- [x] Findings documented
- [x] Blocking Issues identified (if any)
- [x] Improvement Suggestions separated
- [x] Decision recorded
- [x] state.yaml updated
