# App UI Asset Manifest

## Scope

- Task: `UI-001-foundation`
- Formal visual source: Figma file `HRbRsw6HoNBUCtaieX8xUM`, entry node `2905:2680`
- Approval date: 2026-09-07
- Retrieval date: 2026-09-07

## Shared Foundation Assets

| Asset | Source | Repository path | Usage | Status |
|---|---|---|---|---|
| Semantic color and typography roles | `docs/design/app-ui-requirements.md`; Figma entry node `2905:2680` | `app/src/main/java/com/example/tp_ncolso_android/ui/foundation/theme/AppTheme.kt` | Shared theme contract for UI tasks | Implemented as semantic tokens |

## Feature-owned Assets

| Consumer | Asset type | Ownership note |
|---|---|---|
| `UI-002-auth` | Login logo, city skyline, captcha image, registration illustration | Must be exported from approved Figma nodes by UI-002 and registered with exact node/retrieval evidence. |
| `UI-003-map-shell` | Map shell and drawer-specific icons | Must be exported or implemented by UI-003; not provided by UI-001. |

## Constraints

- Do not recreate approved logos, captcha images, or city illustrations from screenshots.
- Feature code must consume semantic foundation tokens instead of raw visual values.
- Any approved Figma change invalidates visual evidence and requires revalidation before release.
