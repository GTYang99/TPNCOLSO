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

## Evidence

Record command/check, environment/device, revision, result, failures, and limitation. A test proves only what it asserts against the exact artifact/revision.

## Rules

- Map each AC to executed evidence.
- Include error, empty/loading, permission, lifecycle, duplicate-action, and recovery cases when relevant.
- Do not delete/weaken tests to pass.
- A skipped, stale, flaky, unavailable, or unexecuted required check is `NOT VERIFIED`.
- Classify failures before changing code.
