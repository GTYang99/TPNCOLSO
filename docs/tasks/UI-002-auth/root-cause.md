# UI-002 Verification Failure Root Cause

## Scope

This debug re-entry addresses the source-proven Verification findings for reviewed revision `63a23794e70edb5cd00203a617afca7715be57b9` on branch `UI-002feat`.

## Reproducible findings

1. The earlier Login findings were fixed: the logo is now decorative, the brand row exposes one heading, and captcha reload has the exact description/effective target.
2. `RegisterScreen.kt:36` still assigns `contentDescription = "註冊插圖"` to an illustration explicitly defined as decorative.
3. `ic_register_back_chevron.xml` defines a right-facing path (`M12,8 L20,16 L12,24`) and `RegisterScreen.kt` applies no rotation, conflicting with the required left-facing Back asset.

## Classification

These are implementation failures. The requirements are approved and unambiguous; no requirement reduction or design reinterpretation is needed. The unavailable current ADB runtime and missing hosted CI remain separate environment/evidence limitations.

## Evidence boundary

Figma file `HRbRsw6HoNBUCtaieX8xUM`, nodes `2905:2680`, `2905:2679`, and `2997:11541`, was inspected on 2026-09-23 as supporting visual evidence. The requester-approved composite PNG remains the primary visual authority. The findings above are established from the reviewed source revision and do not depend on screenshot inference.

The Registration reference frames `4922:17227` and `4922:17493` expose a 48dp Back slot containing a 32dp `chevron-right 2` vector (`I4922:17286;4922:15635` / `I4922:17494;4922:17424`) that must be rotated toward the left, plus an 80dp decorative illustration frame. This bounds the approved asset and semantics target for the planned fix.

## Debug exit condition

The minimum fix scope and regression checks are recorded in `fix-plan.md`. Production code remains unchanged during this debug analysis.
