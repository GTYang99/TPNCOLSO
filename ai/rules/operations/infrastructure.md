# Infrastructure

## Purpose

Diagnose and restore tools, repository, SDK, device, build, CI, credential access, or environment capability without changing product behavior.

## Entry and Load

- `next_action: infrastructure`
- failing command/operation, environment details, originating phase, and available logs

Read only the configuration and evidence needed to reproduce the environment failure.

For Gradle, Android JDK, or toolchain work, read and follow [Runtime](./runtime.md).

## Output

Record symptom, reproduction, classification evidence, affected gates, action taken, result, limitations, and return route in `infrastructure.md` or the issue log.

## Exit

- Restored: return to the phase that was blocked.
- Product change caused failure: route to Implementation/Debug.
- Cause unknown: `investigation`.
- External owner/action required: remain blocked with the exact request.

## Restrictions

No production behavior changes, destructive cleanup, secret exposure, bypassed gates, or false PASS. Environment recovery does not prove product correctness.
