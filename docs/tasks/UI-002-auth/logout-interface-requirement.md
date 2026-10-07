# Logout Interface Requirement

## Status and Authority

- Status: approved interface requirement, revision 1
- Approver: Requester
- Approval date: 2026-09-07 (Asia/Taipei)
- Authoritative visual source: `/var/folders/wm/_jpdxgws6mn1jxpy1k6jjmj80000gp/T/codex-clipboard-52d4f92d-7688-4c67-99f9-f6e97ce5f1dc.png`
- Source identity: PNG, 7904 × 2916 px, sRGB, SHA-256 `75cb578c58f3098c270290f033539ebf06c35c8b322efb71526fa4e53f3d3e9c`
- Authoritative state: the final panel under `點開漢堡選單，登出` shows the open drawer and Logout entry.
- The outer gray board and heading are documentation chrome, not App UI.

## Ownership Boundary

- `UI-003-map-shell` owns the complete drawer visual: blue panel, dynamic user identity, avatar, `圖台`, `儀錶板`, icons, bottom white Logout row, map scrim and hamburger interaction.
- `UI-002-auth` owns only the destination contract that presents a clean Login state after logout.
- `INT-001-auth-api` owns production Token deletion and any remote `/auth/logout` integration.
- The complete drawer, map screen and Dashboard destination are explicitly outside UI-002 implementation scope.

## Logout Contract

1. UI-003 emits one `LogoutRequested` event when the user activates the drawer row labeled `登出`.
2. The session owner immediately clears the local signed-in state and any in-memory identity.
3. Auth state clears account, password, captcha, Remember me, Registration values, request errors and transient effects, then renders `LOGIN_EMPTY` from `login-ui-requirement.md` revision 3.
4. No confirmation dialog is shown.
5. The transition does not wait for a remote logout response. INT-001 may perform remote cleanup, but failure must not restore the signed-in UI or block return to Login.
6. Repeated Logout events are idempotent and must not create duplicate navigation/effect delivery.
7. In debug builds, logout returns to the formal Login page, not the debug direct-login screen. The debug-only outer wrapper may still be reached through its separately owned development affordance.

## Accessibility and Failure Behavior

- UI-003 exposes the visible label `登出` as an actionable semantics target of at least 48dp.
- Logout cannot be conveyed only by its icon or color.
- If local clearing throws or cannot complete, remain signed out from protected UI, record a non-sensitive error, and render Login; do not expose Token or identity in logs.
- Remote logout failure is owned by INT-001 and cannot reverse the local signed-out result.

## Acceptance Criteria

- `UIR-LOGOUT-001`: UI-002 publishes a stable `LogoutRequested`/require-login boundary that UI-003 can call without importing Auth screen internals.
- `UIR-LOGOUT-002`: One Logout activation immediately clears local session and renders the clean authoritative Login empty state without confirmation.
- `UIR-LOGOUT-003`: Logout clears all Auth and Registration sensitive/transient state and cannot reveal the previous account after return.
- `UIR-LOGOUT-004`: Repeated Logout events are idempotent and produce one signed-out transition.
- `UIR-LOGOUT-005`: Remote logout success/failure cannot block or reverse local return to Login; Token/API behavior remains INT-001-owned.
- `UIR-LOGOUT-006`: Drawer visuals and destinations remain UI-003-owned; UI-002 tests use a contract fixture rather than implementing the drawer.

## Explicitly Out of Scope

- Drawer, hamburger, map scrim, user profile row, `圖台` and `儀錶板` UI.
- Map and Dashboard destination screens.
- Remote logout DTO, endpoint implementation and Token storage.
