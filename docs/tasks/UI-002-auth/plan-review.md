# Plan Review

Task: UI-002-auth
Reviewer: Codex Plan Critic
Review Iteration: 6
Review Date: 2026-09-07

---

# Summary

## Review Result

- [ ] APPROVED
- [ ] REQUEST_CHANGES
- [x] BLOCKED

## Summary

需求、Acceptance Criteria、Repository analysis、架構邊界、實作步驟、測試計畫、回歸計畫與 rollback strategy 整體具備可追溯性，且登入／註冊 UI 與側欄登出、Session API 的責任切分合理。

本輪重新核對後，requester 已核定目前 Figma 就是正式版 UI，`KD-AUTH-006`／`KB-UI-002-AUTH-R5` 已建立，因此 design-source blocker 已解除。`foundation-contract.md` 提供可讀的 API ownership 與 consumer handoff；但 `UI-001-foundation` 仍因 workspace 缺少 Git metadata 而處於 `infrastructure / blocked`，尚無 implementation、測試或 committed revision 交付，故 implementation readiness 仍未滿足。

# Review Checklist

| Item | Result | Notes |
|------|--------|------|
| Requirement understood | PASS | UI-only Compose、fake state、local validation 與跨 Task callback boundary 已明確。 |
| Acceptance Criteria complete | PASS | AC-UI002-001–008 均可對應實作步驟與驗證方法；parent AC 的跨 Task 範圍已標明。 |
| Repository analysis complete | PASS | 已描述目前 starter 行為、缺少 auth feature、受影響模組與現有測試基線。 |
| Architecture impact reasonable | PASS | 保持 auth feature 與 API／Token／map shell 分離，不提前引入完整 navigation graph。 |
| Affected modules identified | PASS | screen、contract、validation、fake source、host、tests 與必要接線位置均已列出；具體 component API 需待 UI-001 對齊。 |
| Dependencies identified | PASS | `foundation-contract.md` 已可供設計對齊；但 UI-001 code/test/asset delivery 仍需在 implementation 前完成。 |
| Risks evaluated | PASS | UI-001 變動、Figma revision、敏感 state、fake model 污染與 asset 可重現性均已評估。 |
| Test Plan complete | PASS | 覆蓋 validation、semantics、single-submit、state matrix、responsive、IME、font scale、accessibility 與 regression。 |
| Regression Plan complete | PASS | 已涵蓋 UI-001、cold start、back、insets、敏感資料及下游接線回歸。 |
| Open Questions documented | PASS | Figma authority 已解決；UI-001 implementation delivery 與 asset manifest evidence 已列為必要追蹤項目。 |
| Implementation steps actionable | PASS | 步驟順序與 fake／production boundary 清楚；第 1、5 步需以前置條件解除為准入門檻。 |
| Task size appropriate | PASS | 範圍集中於兩個認證畫面與可替換 fake state，未混入 API、Token 或地圖。 |
| Rollback strategy (if applicable) | PASS | package／host entry 可獨立回退，無資料遷移或 API rollback 風險。 |

# Findings

## Finding 1

Severity:
- [ ] Critical
- [x] Major
- [ ] Minor
- [ ] Suggestion

Category: Dependencies / Architecture

Description:

計畫依賴 `UI-001-foundation` 的 tokens、fields、buttons、icons、assets 與 test scaffold。`foundation-contract.md` 已解決 API ownership 與 semantic component 邊界，但 UI-001 目前因 Git metadata 缺失而 blocked，尚無 code/test/asset delivery evidence 可確認 UI-002 能直接編譯、重用與回歸驗證。

Recommendation:

在 Implementation 前完成 `UI-001-foundation`，並將其實際 component／token／asset／test contract 加入本任務的 revalidation evidence；依實際 contract 更新 affected files 與第 1、5 步，必要時重新送 Plan Review。

Planning Response:
Re-review 6：foundation contract 與正式 visual baseline 已可讀；但 UI-001 的 environment blocker 尚未解除，仍必須完成 implementation、測試與交付 evidence。

Status:
- [x] Open
- [ ] Resolved

## Finding 2

Severity:
- [ ] Critical
- [x] Major
- [ ] Minor
- [ ] Suggestion

Category: Design Source / Verification Readiness

Description:

原本 Figma Copy `HRbRsw6HoNBUCtaieX8xUM` node `2905:2680` 只能作 Planning reference。Requester 已於 2026-09-07 明確核定目前內容即正式版 UI，並表示正式提供時不會有差異。

Recommendation:

以 file key、入口 node、已取得的 auth 子節點與核定日期建立正式 baseline；Implementation 重新取得 context／exact assets並記錄 retrieval evidence。後續 Figma 內容變更時重新驗證。

Planning Response:
Re-review 5：已建立 `KD-AUTH-006`／`KB-UI-002-AUTH-R5`，design-source blocker 解除。

Status:
- [ ] Open
- [x] Resolved

## Finding 3

Severity:
- [ ] Critical
- [ ] Major
- [x] Minor
- [ ] Suggestion

Category: Process Artifact

Description:

Plan Review 規則列出 task-local `plan-critic-rules.md` 為必要輸入，但該檔案不存在；目前已使用 repository 中的 `ai/rules/planning/plan-review.md` 與 `ai/templates/task/plan-review.md` 完成審查。

Recommendation:

由流程維護者補齊或修正規則中的引用，避免後續 Plan Review 對必要輸入產生歧義。本次不以此項阻擋審查結果。

Planning Response:
以現行 authoritative repository rule 與 canonical template 作為審查依據。

Status:
- [ ] Open
- [x] Resolved

# Additional Finding

## Finding 4

Severity:
- [ ] Critical
- [ ] Major
- [ ] Minor
- [x] Suggestion

Category: Test Coverage

Description:

計畫的 Failure Behavior 已定義 captcha refresh failure 應保留表單並提供重試，但 Test Plan 與 AC traceability 尚未明確列出 refresh failure 的測試案例。

Recommendation:

在 fake data source／captcha provider 可注入後，補一個 refresh failure、錯誤文案與 retry 成功的 Compose 或 state test；不改變現有 Acceptance Criteria。

Planning Response:
Re-review 4：列為非阻擋性建議，待 Implementation planning re-entry 時補入測試矩陣。

Status:
- [ ] Open
- [x] Resolved

## Re-review Evidence

- `docs/tasks/UI-001-foundation/state.yaml`：`phase: infrastructure`、`status: blocked`、`reason: environment_missing_git_metadata`，`implementation.status: pending`；前置任務尚未完成。
- `docs/tasks/UI-001-foundation/foundation-contract.md`：contract 已提供，故 Finding 1 的「無可讀 contract」部分已解除，但 implementation readiness 仍未解除。
- Requester 於 2026-09-07 核定目前 Figma 為正式 UI；`docs/design/app-ui-requirements.md`、`KD-AUTH-006` 與 `KB-UI-002-AUTH-R5` 已同步。
- `git rev-parse --show-toplevel` 仍回報 workspace 不是 Git repository；此環境問題已在 UI-001 state 以 `ENV-UI001-001` 記錄。
- 本輪未發現新的 requirement、architecture 或 test-plan blocker；剩餘問題屬 environment route。

# Blocking Issues

- UI-001 因 Git metadata 缺失無法產生 task branch、committed implementation revision 與 CI evidence；此為下游 UI-002 的 environment blocker。
- UI-001 的 foundation code、asset catalog 與測試交付尚未完成，因此 UI-002 尚不能開始依賴其 implementation。

# Improvement Suggestions

- 在 UI-001 前置條件解除後，將實際 foundation contract 與核定 Figma asset identifiers 寫入本任務的 implementation evidence。
- 流程維護者應補齊或修正 `plan-critic-rules.md` 的規則引用。

# Decision

## BLOCKED

Planning cannot continue to implementation readiness until the external prerequisite is available. The design-source revalidation has passed; UI-001 implementation dependency delivery remains blocked by the Git environment.

# Next Action

- [ ] Planning
- [ ] Implementation
- [ ] Requirement Clarification
- [ ] Human Review
- Infrastructure：恢復 Git metadata，讓 UI-001 完成 implementation／validation／commit delivery。

# state.yaml Update

```yaml
phase: plan_review
status: blocked
reason: environment_missing_git_metadata
next_action: infrastructure
```

# Review Notes

- 本輪依 requester 的正式設計核定與 `KB-UI-002-AUTH-R5` 重新審查；未修改 production code、需求或降低 Acceptance Criteria。
- Git metadata unavailable 是 UI-001 未完成並連帶阻擋 UI-002 的剩餘 environment blocker；Figma 核定 blocker 已解除。
- 登出畫面與行為的責任切分保持：UI-003 負責側欄入口，INT-001 負責 Session logout／Token／401，UI-002 只提供乾淨登入入口 contract。
- 本輪不修改 `requirement.md` 或 Acceptance Criteria；解除 blocker 後需重新確認 `foundation-contract.md` 與實際 code 一致。

# Definition of Done

- [x] All checklist items reviewed
- [x] Findings documented
- [x] Blocking Issues identified
- [x] Improvement Suggestions separated
- [x] Decision recorded
- [x] state.yaml updated
