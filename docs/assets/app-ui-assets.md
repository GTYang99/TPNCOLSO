# App UI Asset Manifest

## Scope

- Task: `UI-001-foundation`
- Formal visual sources: Login／Register／Logout context use composite PNG SHA-256 `75cb578c58f3098c270290f033539ebf06c35c8b322efb71526fa4e53f3d3e9c` for layout, state and semantic token evidence; v0.1 iconography uses Android／Material defaults instead of custom visual asset matching. Remaining UI uses Figma file `HRbRsw6HoNBUCtaieX8xUM`, entry node `2905:2680`.
- Approval date: 2026-09-07
- Retrieval date: 2026-09-07

## Shared Foundation Visual Inputs

| Asset | Owner | Source node | Source archive path | Runtime path | Checksum evidence | Status |
|---|---|---|---|---|---|---|
| Semantic color and typography roles | UI-001 | Current composite auth panels | `docs/design/evidence/auth/auth-reference-2026-09-07.png` | `app/src/main/java/com/example/tp_ncolso_android/ui/foundation/theme/AppTheme.kt` | composite checksum, token mapping and committed implementation revision | Implemented |
| Material/system icon policy | UI-001 | Android／Material defaults | N/A | Material Icons / platform controls used by foundation components | dependency/source inspection plus semantics and 48dp target tests | Implemented |
| Password visibility affordance | UI-001 | Material default visibility icon | N/A | `AppPasswordField` icon slot | no custom asset checksum; verify masked Login behavior, content description and click target | Planned |
| Captcha reload affordance | UI-001 | Material default refresh icon | N/A | `AppIconButton` / consumer icon slot | no custom asset checksum; verify content description and click target | Planned |
| Checkbox checked affordance | UI-001 | Android／Material checkbox default | N/A | `AppCheckboxRow` | no custom asset checksum; verify selectable row semantics and checked state | Planned |
| Back affordance | UI-001 | Material default back icon | N/A | `AppIconButton` / consumer icon slot | no custom asset checksum; verify content description and click target | Planned |
| Dropdown affordance | UI-001 | Material default dropdown indicator | N/A | `AppSelectField` | no custom asset checksum; verify expanded/collapsed semantics | Planned |
| Radio selected affordance | UI-001 | Android／Material radio default | N/A | `AppRadioGroup` | no custom asset checksum; verify zero-or-one selection and exclusivity | Planned |

## Feature-owned Assets

| Consumer | Asset type | Ownership note |
|---|---|---|
| `UI-002-auth` | Auth composite evidence | Archive original bytes at `docs/design/evidence/auth/auth-reference-2026-09-07.png`; checksum must equal `75cb578c58f3098c270290f033539ebf06c35c8b322efb71526fa4e53f3d3e9c`. Gray board and headings are excluded from panel comparison. |
| `UI-002-auth` | City skyline / decorative media | Deferred from UI-001. If required by UI-002, plan as feature-owned decorative media or replace with approved Android/Material/system-default treatment; do not block UI-001 foundation on custom skyline asset matching. |
| `UI-002-auth` | Captcha Preview/test fixture | Current screenshot supplies visual evidence; runtime `app/src/debug/res/drawable/login_captcha_fixture.png`; never shipped or treated as production captcha data. |
| `UI-002-auth` | Registration user illustration / decorative media | Deferred from UI-001. If required by UI-002, plan as feature-owned decorative media or replace with approved Android/Material/system-default treatment. |
| `UI-003-map-shell` | Map shell and drawer-specific icons | Must be exported or implemented by UI-003; not provided by UI-001. |

## Constraints

- Do not recreate logos, captcha images, custom icons, or city illustrations from screenshots for UI-001.
- UI-001 v0.1 uses Android／Material default icons and platform controls; custom icon checksum/crop/scale evidence is not required for UI-001 acceptance.
- Feature code must consume semantic foundation tokens instead of raw visual values.
- Any approved composite or remaining Figma source change invalidates its affected visual evidence and requires revalidation before release.
