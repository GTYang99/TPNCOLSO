# UI-002 Verification Failure Root Cause

## Scope

This debug re-entry addresses the source-proven Verification findings for reviewed revision `0815e6b5c22dc14bd2478a7b66bbb1aa58e7dd08` on branch `UI-002feat`.

## Reproducible findings

1. `LoginScreen.kt` assigns `contentDescription = "品牌標誌"` to the decorative logo, conflicting with the approved Login accessibility contract that the logo must not create a duplicate spoken label.
2. The captcha reload action is a `32.dp × 48.dp` Foundation clickable region with description `重整`; the approved contract requires `重新產生驗證碼` and an effective `48 × 48` target while retaining the `32dp` layout allocation.
3. `RegisterScreen.kt` draws the Back chevron with `Canvas` lines instead of consuming the approved Back asset required by the Registration contract.

## Classification

These are implementation failures. The requirements are approved and unambiguous; no requirement reduction or design reinterpretation is needed. The unavailable current ADB runtime and missing hosted CI remain separate environment/evidence limitations.

## Evidence boundary

Figma file `HRbRsw6HoNBUCtaieX8xUM`, nodes `2905:2680`, `2905:2679`, and `2997:11541`, was inspected on 2026-09-23 as supporting visual evidence. The requester-approved composite PNG remains the primary visual authority. The findings above are established from the reviewed source revision and do not depend on screenshot inference.

The Registration reference frames `4922:17227` and `4922:17493` expose a 48dp Back slot containing a 32dp `chevron-right 2` vector (`I4922:17286;4922:15635` / `I4922:17494;4922:17424`) and an 80dp illustration frame. This bounds the approved asset target for the planned fix.

## Debug exit condition

The minimum fix scope and regression checks are recorded in `fix-plan.md`. Production code remains unchanged during this debug analysis.
