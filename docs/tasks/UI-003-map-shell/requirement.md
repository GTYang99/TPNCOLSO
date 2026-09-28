# Requirement

## Background

- `UI-003-map-shell` implements the authenticated map-shell UI that consumes the `AppRoot` signed-in slot after UI-001 and UI-002 handoff.
- The Knowledge baseline is `KB-UI-003-MAP-SHELL-R1`; the product WMTS specification owns real tile content while this task establishes the UI and testable state contract.

## Goal

- Provide an Android 9+ Compose map shell with the approved visible navigation controls, three-state basemap switcher, drawer/logout event boundary, and location permission states without inventing production map or parcel API behavior.

## Functional Requirements

- Render a full-screen authenticated map-shell surface with top hamburger, keyword-search and notification affordances, bottom basemap switcher, and right-bottom location affordance.
- Provide exactly three mutually exclusive basemap choices: electronic map, orthophoto and terrain; electronic map is selected initially.
- Preserve UI-only overlay fixtures, selected target, user-location marker and active query context when the basemap selection changes.
- Own the drawer visual and expose an accessible `登出` action that emits one `LogoutRequested` event; UI-002 owns the clean Login destination and INT-001 owns Token/remote logout behavior.
- Expose callback boundaries for search, notification, map navigation and location actions without creating production repositories, DTOs, WMTS parameters or API calls.
- Render normal, location-loading and permission-denied states using the project common UI template; permission states are not Figma pixel-match acceptance states.

## Non-functional Requirements

- Support Android 9+ and Compose MVVM conventions in the existing single app module.
- Every actionable icon/row has Traditional Chinese semantics and a touch target of at least 48dp.
- UI state is immutable and lifecycle-safe; one-shot callbacks are explicit events and repeated logout activation is idempotent at the receiving boundary.
- Do not persist or log Token, credentials or unrestricted personal data; do not add production networking, map SDK or parcel DTO dependencies in this UI task.
- The layout must remain usable at the approved 402×874 reference frame, a narrow supported width, font scale 1.3 and IME-visible conditions where applicable.

## Acceptance Criteria

- `AC-UI003-001`: When an authenticated identity is supplied to the signed-in slot, the screen renders a full-screen map-shell surface with hamburger, keyword-search and notification entry points, a three-choice bottom basemap switcher and a right-bottom location affordance.
- `AC-UI003-002`: The basemap switcher exposes exactly three mutually exclusive choices labeled `電子地圖`, `正射圖` and `地形圖`; `電子地圖` is selected on initial state.
- `AC-UI003-003`: Selecting any basemap updates only the selected basemap state and preserves the UI-only overlay fixture, selected target, user-location marker and active query context.
- `AC-UI003-004`: The drawer opens from the hamburger action, presents the approved identity/navigation/logout composition, and exposes `登出` as an actionable semantics target of at least 48dp.
- `AC-UI003-005`: One `登出` activation emits exactly one `LogoutRequested` callback; repeated activations after the signed-out boundary is applied do not create duplicate signed-in transitions or duplicate logout effects.
- `AC-UI003-006`: Search, notification, map navigation and location controls emit their documented callbacks without requiring production API responses or inventing API payloads.
- `AC-UI003-007`: The shell renders normal, location-loading and permission-denied states with readable Traditional Chinese status/action text; denied/loading states use the common UI template and are not claimed as Figma pixel parity.
- `AC-UI003-008`: Compose/unit tests cover basemap default and mutual exclusion, overlay preservation, callback emission, drawer/logout semantics, repeated-event idempotency and location state rendering.
- `AC-UI003-009`: Local validation at the reference frame plus narrow width, font scale 1.3 and IME-visible conditions shows no functional clipping of the shell controls or loss of the primary callbacks.

## Constraints

- UI-003 owns map-shell/drawer visuals and UI callbacks only. Clean Login return remains UI-002-owned; Token deletion and remote logout remain INT-001-owned.
- The Figma/screenshot source establishes visible composition and selected-state affordances only. The terrain screenshot is a placeholder and is not tile-content acceptance.
- The current WMTS endpoint is documented but layer, tile-matrix, attribution, caching and offline parameters are not to be invented in this task.
- Search results, parcel details, live parcel data and production map integration are out of scope and belong to downstream/integration tasks.
- UI-003 implementation must wait for the prerequisite UI-001/UI-002 handoff and an approved Plan Review.

## Open Questions

- Which map SDK and WMTS request parameters will be approved for real tile integration? This is a later integration decision and does not block the UI-only shell plan.
- Are additional approved drawer assets or final Figma revisions supplied before visual Verification? If so, revalidate the visual source and screenshots.
