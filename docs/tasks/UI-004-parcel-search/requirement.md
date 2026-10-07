# Requirement

## Background
- UI-004 delivers the parcel search and summary flow after the map shell. UI-003 Knowledge Resolution assigns map-shell controls and callbacks to UI-003 and query/results/summary to UI-004. Its current Knowledge baseline is `KB-UI-004-PARCEL-SEARCH-R2`; the request refers to “v0.1,” but no separate v0.1 artifact is present.
- Product search behavior is defined by parent `FR-005` / `AC-006`. Figma node `4952:14932` supplies visual observations for entered, result-sheet and no-result states.
- Requester clarification on 2026-10-02: search/filter entry is in the full-map page's top toolbar, the lower-right control is location only, and a successful search opens a card-style page from the bottom. This explicit task direction resolves the prior interpretation that placed a search/filter entry at lower right.

## Goal
- Allow a user in the map shell to search parcels by land number, location or parcel identifier and inspect a matching parcel summary, using replaceable fake data.

## Functional Requirements
- Provide a keyword search flow for land number, land location or `key_no`.
- For a matching fake parcel, retain map context, locate the parcel and display its card-style summary from the bottom of the full-map page.
- For no match, retain the keyword and present the “查無資料” state without clearing the current map container.
- Keep fake parcel data behind a replaceable feature data source; do not add production API contracts, DTOs or endpoints.
- The full-map page's top toolbar search/filter entry calls into UI-004; the lower-right map control is for location only. UI-003 owns these map-shell controls and callbacks, while UI-004 owns keyword entry, query results and the summary presentation. UI-004 does not create or restyle map-shell controls.
- “Locate” in this UI-only task means selecting/highlighting the corresponding synthetic map target and retaining the active query; it does not claim geographic pan/zoom or live map integration.

## Non-functional Requirements
- Support Android 9+ and responsive Compose layouts; provide accessible labels for icon-only actions and scalable text.
- Keep query and result presentation state out of Composables and deterministic for unit/Compose testing.

## Acceptance Criteria
- `AC-UI004-001`: The search field accepts a keyword representing a land number, land location or `key_no`, and the entered value remains visible while the query is active.
- `AC-UI004-002`: When a keyword matches the fake parcel source, the map retains its context, the corresponding parcel is selected/located, and a card-style parcel summary appears from the bottom of the full-map page.
- `AC-UI004-003`: When a keyword has no match, the entered value remains visible and “查無資料” is shown without replacing or clearing the map container.
- `AC-UI004-004`: The summary presentation shows the parcel status and the key parcel-identifying/site-condition information defined by the visible summary header; no parcel-detail editing or API payload behavior is introduced.
- `AC-UI004-005`: Search behavior is exercised through deterministic fake data, and the feature can later replace that source without changing the UI state contract.
- `AC-UI004-006`: UI-004 is entered through the top-toolbar search/filter callback from UI-003; it does not duplicate or own map-shell controls, and the lower-right control continues to invoke location only.

## Constraints
- Use `KB-UI-004-PARCEL-SEARCH-R2` as the traceable Knowledge baseline identifier; do not claim an absent v0.1 artifact exists.
- Production parcel API, DTOs, endpoint/error semantics and pagination are deferred to `INT-002`.
- UI-004 does not own map shell controls, full parcel details, survey forms, WMTS tiles or notifications.
- No real map movement, geographic coordinates or tile/API behavior is introduced; the selected target is represented by the existing map-shell fixture state.
- Figma node `4952:14932` is a visual observation until its formal revision/approval is established.
- UI-003 verification and prerequisite handoff must be completed before UI-004 Implementation begins.

## Open Questions
- Which Figma revision is formally approved for pixel-level acceptance?
