# TP_NCOLSO Android Architecture

## Status

- Initial architecture baseline for a new project, approved by requester scope on 2026-09-07.
- This document supersedes the copied TaoYuanGutter/ViewBinding architecture text that did not describe this repository.

## Product Boundary

`TP_NCOLSO_ANDROID` is a new Android application for the 新工處土地占用調查圖台系統 mobile workflow. The repository starts from a single-activity Jetpack Compose application; no legacy application migration or compatibility layer exists.

## Platform and Stack

- Android 9+ (`minSdk 28` after UI-001)
- Kotlin
- Jetpack Compose with Material 3
- MVVM with immutable UI state and explicit UI events/effects
- Gradle version catalog for dependency ownership
- JUnit and Compose UI tests

Navigation、networking、persistence、maps、camera and dependency injection libraries are selected only by the Task that first requires them; UI-001 must not add them speculatively.

## Module and Package Ownership

The initial repository contains one `app` module. New modules require a reviewed architectural reason.

| Area | Ownership |
|---|---|
| app root | Variant entry, session-driven signed-out/signed-in content boundary |
| `ui.foundation.theme` | Semantic colors, typography, shapes and spacing mapped to approved Figma |
| `ui.foundation.component` | Stateless reusable Compose primitives and accessibility guarantees |
| `ui.foundation.state` | Generic presentation state without API/DTO knowledge |
| `session` | App identity, role and session-state boundary without production Token schema |
| `feature.<name>` | Feature screen, ViewModel, immutable state, events/effects and feature-only assets |
| `data` / `network` | Added by Integration tasks when an approved API contract exists |

## Dependency Direction

```text
MainActivity / AppEntry
        -> AppRoot + session contract
        -> feature screens
feature screens
        -> ui.foundation
        -> feature state/event contracts
data/network implementations
        -> feature-owned ports (when integration is approved)
```

- Foundation never depends on a feature, API DTO, repository or navigation destination.
- Composables render immutable state and emit events; they do not create repositories or fake data sources.
- ViewModels own presentation orchestration; pure validation/state transitions remain independently testable.
- Debug-only fake session code lives in `src/debug`; release code cannot reference it.

## State and Lifecycle

- Screen state is immutable and exposed through lifecycle-aware observable state.
- One-shot navigation/messages are explicit effects or host callbacks, not persistent content state.
- Passwords、captcha、Token and other sensitive values are not logged or placed in durable/saved state without an approved security design.
- Long-running or external operations must survive normal Compose recomposition and expose loading/error/retry behavior.

## Design and Asset Contract

- Formal visual source: Figma file `HRbRsw6HoNBUCtaieX8xUM`, entry node `2905:2680`, approved 2026-09-07.
- Features consume semantic theme values rather than raw colors/dimensions.
- UI-001 owns shared brand tokens/assets; feature-only assets stay with their feature.
- Asset manifests record source node, retrieval date, repository path and usage constraints.

## Build Variants

- `debug`: may include deterministic fake session/direct-login development entry.
- `release`: must exclude debug bypass classes, resources and callable entry points.
- Variant isolation is verified by source inspection, compilation and release artifact checks.

## Testing and Delivery

- Unit tests cover pure validation, reducers/session state and duplicate-event suppression.
- Compose UI tests cover semantics, interaction, loading/disabled behavior and responsive states.
- Both debug and release variants must compile for affected foundation/auth changes.
- Work occurs on a dedicated task branch and Verification targets a committed revision.
- CI provider/remote may be configured after initial local implementation, but CI remains `NOT VERIFIED` and blocks Release until an authoritative run passes.

## Initial Entry Points

- Launcher: `MainActivity`
- Variant application entry: `AppEntry`
- Session/content boundary: `AppRoot`
- First foundation task: `UI-001-foundation`
- First feature consumer: `UI-002-auth`
