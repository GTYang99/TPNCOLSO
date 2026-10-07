# Verification Report

## Inputs
- Requirement
- Plan
- Diff
- Completion scope
- Developer/local validation
- Hosted CI result or recorded deferral
- Code review
- Committed revision

---

## Acceptance Criteria

| AC | Result | Evidence |
|----|--------|----------|
| AC-001 | PASS/FAIL/NOT VERIFIED | 簡短證據 |

---

## Regression
- 只寫確認過的回歸項目

---

## Issues
- 只寫問題與缺口；沒有就寫 `無`

## Validation Limitations
- 無法執行或證據不足的項目；沒有就寫 `無`

## Deferred Obligations
- 每項列出 gate、reason、owner、reactivate_at；沒有就寫 `無`
- Local evidence 不得標示為 hosted CI PASS

## Completion Decision
- `development_complete` / `release_complete` / `blocked`

## Failure Classification
- `requirement` / `planning` / `implementation` / `environment` / `unknown`；PASS 時寫 `不適用`

## Next Action
- Development PASS：`none`，並保留 deferred obligations
- Release-scope PASS：`release`
- Failure／NOT VERIFIED：依 `AGENTS.md` 分類路由

---

## Final Result

PASS

FAIL

NOT VERIFIED

只保留一個最終結果。`NOT VERIFIED` 不得視為 PASS。
