# UI-002 Verification Failure Root Cause

## Scope

This debug re-entry addresses the Verification findings for the committed UI-002 revision `bce091b`.

## Findings

- The local logic/build evidence passes, but the approved Login skyline and Register illustration exist only as source SVG files under `docs/assets/source/`; production Compose screens do not consume them.
- The debug route consumes only the captcha PNG through an injected slot. The remaining approved visual assets therefore cannot produce runtime evidence.
- Connected Compose, narrow-width, font-scale, IME, accessibility, and pixel comparison findings are evidence gaps. They are not converted into PASS by build or unit-test results.
- Physical-device testing remains out of scope by user instruction. Emulator validation is still the approved alternative when the Android runtime is available.

## Classification

The missing runtime asset consumption is an implementation gap. The unavailable ADB runtime is an infrastructure limitation and remains separately tracked as `INF-AUTH-009`.
