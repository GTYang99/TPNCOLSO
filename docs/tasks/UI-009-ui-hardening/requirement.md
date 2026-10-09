# Requirement

## Status
- Approved by requester on 2026-10-08 (Asia/Taipei) for Knowledge baseline `KB-UI-009-UI-HARDENING-R1`.
- Approval authorizes downstream Planning only; it does not authorize production-code changes before an approved plan.

## Background
- `UI-009-ui-hardening` is the cross-screen hardening task after UI-002 through UI-008.
- The parent UI requirement already defines the Android 9+, responsive, accessibility, IME and scalable-text rules. This task makes those rules observable across the existing screen set without adding product behavior.

## Goal
- Validate and correct runtime quality across the existing UI-002–UI-008 routes while preserving each task's approved behavior and ownership boundary.

## Functional Requirements
- `FR-UI009-001` Screen coverage: the regression set includes the existing authentication, map shell, parcel search, parcel detail, survey form, photo and alert surfaces owned by UI-002–UI-008.
- `FR-UI009-002` Responsive layout: existing content must adapt to supported Android widths and the `402 x 874` Figma canvas must remain a reference, not a fixed-pixel device assumption.
- `FR-UI009-003` Input and insets: text fields and their primary actions remain usable with the system IME visible and system safe-area insets applied.
- `FR-UI009-004` Accessible interaction: icon-only actions expose Traditional Chinese content descriptions, primary interactive targets are at least `48dp`, and state/error/selection is not communicated by color alone.
- `FR-UI009-005` State restoration: rotation/configuration change must not crash a route or silently discard the local UI state that the owning task already defines as restorable.

## Non-functional Requirements
- Target Android 9+ (`minSdk 28`) using the existing Kotlin/Jetpack Compose architecture.
- Text must remain scalable; increased system text size must not clip button labels or hide a required action.
- Content must remain scrollable or otherwise reachable when the available height is reduced by the IME or system insets.
- This task uses local/fake data and existing task boundaries. It does not add API calls, DTOs, persistence, permissions, or new product screens.

## Acceptance Criteria
- `AC-001`: A reviewed screen/state matrix identifies the UI-002–UI-008 surfaces under test and records runtime evidence for each affected route; no new screen or feature behavior is introduced by UI-009.
- `AC-002`: On Android 9 and one newer Android version, using a minimum supported width, a normal supported width and the `402 x 874` reference condition, primary content remains reachable and no clipping or overlap blocks the existing task flow.
- `AC-003`: With increased system text size, every tested screen keeps required labels, values, buttons and dialog actions visible or reachable; no text truncation causes functional blockage.
- `AC-004`: With the system keyboard visible on every tested text-entry route, the focused field and its primary action can be brought into the visible area, and content does not enter the system safe areas.
- `AC-005`: Tested icon-only actions expose meaningful Traditional Chinese accessibility names, primary interactive targets meet the `48dp` minimum, and error/selection/destructive states have a non-color cue.
- `AC-006`: After rotation/configuration change or process death/recreation during each tested stateful flow, the route remains usable and the owning task's local input, selection, dirty state, photo state or dialog outcome is not silently reset unless that task explicitly defines the reset.
- `AC-007`: Existing UI-002–UI-008 acceptance criteria and ownership boundaries remain unchanged; UI-009 produces no production API or integration claim.

## Constraints
- Parent traceability: the responsive behavior derives from parent `AC-017` and the parent non-functional requirements in `docs/tasks/UI-0907-app-ui-requirements/requirement.md`.
- Design constraints derive from `docs/design/app-ui-requirements.md`, especially the responsive/accessibility rules and the source index.
- Visual evidence is limited to the registered source for each child task. Formal pixel parity is not claimed while the Figma file revision/approval is unknown.
- UI-002's requester-approved composite visual source remains authoritative for auth visuals; older auth Figma nodes are not reactivated.
- UI-005 has no dedicated Figma screen node in the source register; its visible claims use the registered design specification and component inspection evidence.

## Dependencies
- UI-002–UI-008 current requirements, Knowledge baselines and completed development evidence.
- A reproducible source register for each screen family, recorded in `knowledge-collection.md`.
- The owning task must retain behavior ownership for its existing fields, navigation, dialogs and local state.

## Risks
- A changed Figma node or unrecorded visual revision can make a screenshot comparison stale; revalidate the affected source before claiming visual parity.
- Process-death restoration requires explicit lifecycle and test evidence in Planning/Developer Validation; it must not be inferred from a rotation-only test.

## Open Questions
- Is formal Figma revision approval required for this hardening task, or are the registered per-task sources sufficient while visual claims remain `visual_observation`?
