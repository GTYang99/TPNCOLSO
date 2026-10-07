# App UI Asset Manifest

## Scope

- Task: `UI-001-foundation`
- Formal visual sources: Login／Register／Logout context use composite PNG SHA-256 `75cb578c58f3098c270290f033539ebf06c35c8b322efb71526fa4e53f3d3e9c` for layout, state and semantic token evidence; v0.1 iconography uses Android／Material defaults instead of custom visual asset matching. Remaining UI uses Figma file `HRbRsw6HoNBUCtaieX8xUM`, entry node `2905:2680`.
- Approval date: 2026-09-07
- Retrieval date: 2026-09-08 (Figma MCP asset export)

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
| `UI-001` | Login brand logo | Figma node `I2997:11541;4987:4624`, exported SVG/PNG from approved composite Login instance | `docs/assets/source/shared/login-brand-logo.svg` | `app/src/main/res/drawable-nodpi/login_brand_logo.png` | source `c8584340fbf083f63e1c70d8e13f585d62d82a570e9e77e89676d9d6e4c92e6f`; runtime PNG `ab2f44f0dcc962d7ac87004d0ff8684650ae29f8c23b348397921ce03fb013e3` | Retrieved 2026-09-08 |
| `UI-002-auth` | City skyline / decorative media | Figma node `I2997:11541;4917:14613`, exported SVG/PNG from approved composite Login instance | `docs/assets/source/auth/login-city-skyline.svg` | `app/src/main/res/drawable-nodpi/login_city_skyline.png` | source `64a394cd0278f01708fc7e312ce86044319cb535c411d61248234e43b324818c`; runtime PNG `706d8030f380f99f2eebff389a0a91c9f84c82e142938da621fbe01fb9195213` | Retrieved 2026-09-08 |
| `UI-002-auth` | Captcha Preview/test fixture | Figma Login image layer `螢幕擷取畫面 2026-08-27 135856 1`, exported PNG from approved composite Login instance | `app/src/debug/res/drawable-nodpi/login_captcha_fixture.png` | Debug/test only; never shipped or treated as production captcha data | `bbb0f26fa3a141de5a07ee7bca3216253311ba837b1b5ad3e3f637e2344b6494` | Retrieved 2026-09-08 |
| `UI-002-auth` | Registration user illustration / decorative media | Figma node `4922:15640`, exported SVG from approved composite Registration instance | `docs/assets/source/auth/register-user-illustration.svg` | UI-002 feature-owned source; runtime integration remains Implementation scope | `2991eaf6a40bca7832725ba1f301e8a0806fd5ec78e3c1335e3eebac50731731` | Retrieved 2026-09-08 |
| `UI-001` | Registration back chevron | Figma node `4922:15635` / `chevron-right 2`, candidate export only | `docs/assets/source/shared/register-back-chevron.svg` | Do not use if UI-001 Material default treatment remains authoritative | `1ed4c0f1ac8c0b041c28c741b5903691d16135ecebaff66db73d3420446dda9c` | Candidate, not required |
| `UI-001` | Registration dropdown arrow | Figma node `4922:16850`, candidate export only | `docs/assets/source/shared/register-dropdown-arrow.svg` | Do not use if UI-001 Material default treatment remains authoritative | `c67678e68e10a1a2c878451bdea64e3189aee00f84f08b49c2dcb304b8d49b32` | Candidate, not required |
| `UI-001` | Registration radio glyph | No independent Figma asset layer; rendered as radio geometry | N/A | Use `AppRadioGroup` / Material default; no custom source required | N/A | Material default selected |
| `UI-003-map-shell` | Map shell and drawer-specific icons | Must be exported or implemented by UI-003; not provided by UI-001. |

## Constraints

- Do not recreate logos, captcha images, custom icons, or city illustrations from screenshots for UI-001.
- UI-001 v0.1 uses Android／Material default icons and platform controls; custom icon checksum/crop/scale evidence is not required for UI-001 acceptance.
- Feature code must consume semantic foundation tokens instead of raw visual values.
- Any approved composite or remaining Figma source change invalidates its affected visual evidence and requires revalidation before release.
