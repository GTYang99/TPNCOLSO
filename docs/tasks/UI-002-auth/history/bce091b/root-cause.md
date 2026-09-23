# UI-002 Verification Failure Root Cause

## Scope

This debug re-entry addresses the Verification findings for the committed UI-002 revision `bce091b`.

## Findings

- The local logic/build evidence passes, but the approved Login skyline and Register illustration initially existed only as source SVG files under `docs/assets/source/`; production Compose screens did not consume them.
- The debug route consumed only the captcha PNG through an injected slot. The Register illustration is now runtime-consumed through a VectorDrawable; the Login skyline remains unresolved.
- Connected Compose, narrow-width, font-scale, IME, accessibility, and pixel comparison findings are evidence gaps. They are not converted into PASS by build or unit-test results.
- Physical-device testing remains out of scope by user instruction. Emulator validation is still the approved alternative when the Android runtime is available.

## Classification

The missing runtime asset consumption is an implementation gap. The unavailable ADB runtime is an infrastructure limitation and remains separately tracked as `INF-AUTH-009`.

## Verification Round 2 Failure Analysis

The four emulator failures were test-harness precondition defects: the direct-login tests asserted role controls before toggling the existing `開發模式` affordance, and the Login tests asserted/clicked the registration entry without scrolling the existing scroll container to it. Production auth behavior was not changed for these failures; the tests now exercise the intended user path.
