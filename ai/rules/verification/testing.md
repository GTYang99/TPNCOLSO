# Testing Policy

Read this policy only when selecting, writing, running, or evaluating tests.

Before any Gradle command, read and follow [Runtime](../operations/runtime.md).

## Selection

Choose the smallest set that proves the changed behavior and nearby regression risk:

- unit tests for logic and state transitions
- Compose UI/instrumented tests for interaction, semantics, navigation, lifecycle, and device behavior
- integration tests for repository, persistence, API mapping, retry, and error handling
- build, lint/static, and release-variant checks where affected
- manual smoke/UAT only when automation cannot represent the requirement

## Device Validation

Emulator is the default Android device-validation environment. Physical-device testing is required only for acceptance criteria involving hardware fidelity, OEM-specific behavior, real sensor/camera output, field performance, or another explicitly identified device risk.

Use ADB to identify and target the validation device. Record the device or emulator identifier, API level, test command, result, and relevant runtime conditions. If an acceptance criterion requires a physical device, emulator evidence cannot replace it; unavailable required hardware is `NOT VERIFIED`.

## Evidence

Record command/check, environment/device, revision, result, failures, and limitation. A test proves only what it asserts against the exact artifact/revision.

## Local and Hosted Evidence

During pre-release development, required task checks may run locally against the committed revision. Record the revision, environment, exact commands, and results. Local results must not be reported as hosted CI PASS.

Hosted CI may be `deferred` only when the current completion scope is `development`, with a recorded reason, owner, and reactivation milestone. `deferred` and `not_applicable` are not PASS. Any existing hosted-CI failure remains evidence; failures indicating product defects must be investigated and resolved.

## Rules

- Map each AC to executed evidence.
- Include error, empty/loading, permission, lifecycle, duplicate-action, and recovery cases when relevant.
- Do not delete/weaken tests to pass.
- A skipped, stale, flaky, unavailable, or unexecuted required check is `NOT VERIFIED`.
- Classify failures before changing code.
