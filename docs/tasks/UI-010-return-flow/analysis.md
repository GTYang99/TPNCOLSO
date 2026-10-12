# Repository Analysis

## Current Behavior
- `MapShellEvent.NotificationsClicked` is emitted by the map shell, but its ViewModel treats it as a no-op and the debug host does not open a notification surface.
- `ParcelDetailScreen` and `FakeParcelDetailDataSource` already support a returned parcel and a full-reason banner. The debug host can hand the detail edit action to `SurveyFormRoute`.
- `SurveyFormRoute` already uses UI-008's dirty-edit confirmation and `SurveyFormViewModel` supports local form editing. It does not carry return-reason context or expose UI-010's save-then-confirm-correction transition; its fake sources are recreated in the debug host, so updates are not shared across the flow.

## Expected Behavior
- Add a Compose notification overlay backed by six deterministic return fixtures. Each row routes by stable parcel key into the matching existing detail/form experience.
- Keep one app-session fake return-flow store behind feature data-source interfaces so notification, detail, and form state remain consistent. Save correction values locally first; update status only after the explicit Android confirmation.
- Retain the existing UI-006 form ownership and UI-008 discard semantics while adding only the return-context presentation and confirmation transition required by UI-010.

## Affected Modules
- New `feature.notifications` contract, ViewModel, screen, data-source port, and fake fixture adapter.
- `feature.mapshell` callback wiring and `DebugSignedInContent` overlay/navigation state in `app/src/debug/.../AppEntry.kt`.
- `feature.parceldetail` returned-correction CTA, confirmation state/dialog, and local record refresh/update.
- `feature.surveyform` return-reason seed/state presentation and a correction-specific save label/callback; preserve current non-returned submission behavior.
- Fake notification/detail/form data sources and focused unit/Compose host test files for UI-010.

## Dependencies
- Active Knowledge baseline: `KB-UI-010-RETURN-FLOW-R1`; parent requirement `FR-012`–`FR-014` / `AC-012`–`AC-014`.
- Design nodes: Figma section `3327:13251`; notification list `3327:13261`; returned detail `3327:13256`; edit/save/confirm/corrected states `4950:14192`, `4964:4571`, `4990:7894`, `4950:14447`; confirmation component `4990:8053`.
- Existing UI-006 survey form and UI-008 discard dialog; existing MapShell, ParcelDetail and fake-source ports.

## Risks
- The notification, detail, and form screens can show stale or mismatched records unless they share one stable fake key/store and only confirmation advances status.
- The corrected-state Figma frame visually contains an edit icon, while approved APP role rules make `已修正` read-only for investigators. Product behavior takes precedence; implementation should not expose an active edit action after confirmation.
- Figma MCP does not expose formal revision metadata; pixel-parity claims must remain limited to the observed node renders and dimensions.

## Unknowns
- Notification read state, paging, API contracts, and backend errors are intentionally deferred and do not block this baseline.
