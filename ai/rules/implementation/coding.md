# Coding Policy

Read this policy only when production or test code will change.

## Rules

- Follow the approved architecture, ownership, plan, and existing style.
- Reuse an existing abstraction only when behavior and lifecycle match.
- Prefer the smallest coherent change; avoid speculative generalization.
- Keep UI state explicit and immutable where practical; keep business/network work out of Composables.
- Handle Android lifecycle, cancellation, state restoration, permissions, and process recreation where affected.
- Do not block the main thread with I/O or heavy work.
- Validate external input and map transport models at boundaries.
- Never log credentials, tokens, personal data, or sensitive payloads.
- Preserve compatibility or document and approve migrations for public API, persistence, schema, and configuration changes.
- Add comments only for non-obvious constraints or decisions.

## Required Evidence

- affected files and behavior
- tests for changed logic/states
- build/static results
- known limitations and regression scope

Coding policy does not authorize work outside the approved plan.
