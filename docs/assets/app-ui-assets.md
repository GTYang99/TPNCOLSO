# App UI Asset Manifest

## Scope

- Task: `UI-001-foundation`
- Formal visual source: Figma file `HRbRsw6HoNBUCtaieX8xUM`, entry node `2905:2680`
- Approval date: 2026-09-07
- Retrieval date: 2026-09-07

## Shared Foundation Assets

| Asset | Owner | Source node | Source archive path | Runtime path | Checksum evidence | Status |
|---|---|---|---|---|---|---|
| Semantic color and typography roles | UI-001 | `2905:2680`; Login states `2905:2679`, `4922:15312`, `4922:15370` | N/A | `app/src/main/java/com/example/tp_ncolso_android/ui/foundation/theme/AppTheme.kt` | committed implementation revision | Requires reconciliation with approved Login tokens |
| Brand logo | UI-001 | Login states `2905:2679`, `4922:15312`, `4922:15370` | `docs/assets/source/login/brand-logo.svg` | `app/src/main/res/drawable/ic_brand_land_survey.xml` | SHA-256 recorded after exact export and conversion | Planned |
| Password visibility icon | UI-001 | Login states `2905:2679`, `4922:15312`, `4922:15370` | `docs/assets/source/login/password-eye.svg` | `app/src/main/res/drawable/ic_password_visibility.xml` | SHA-256 recorded after exact export and conversion | Planned |
| Captcha reload icon | UI-001 | Login states `2905:2679`, `4922:15312`, `4922:15370` | `docs/assets/source/login/captcha-reload.svg` | `app/src/main/res/drawable/ic_captcha_reload.xml` | SHA-256 recorded after exact export and conversion | Planned |
| Checkbox check icon | UI-001 | Filled/error states `4922:15312`, `4922:15370` | `docs/assets/source/login/checkbox-check.svg` | `app/src/main/res/drawable/ic_checkbox_check.xml` | SHA-256 recorded after exact export and conversion | Planned |
| Back chevron | UI-001 | Registration `4922:17227`, `4922:17493` | `docs/assets/source/registration/chevron-back.svg` | `app/src/main/res/drawable/ic_chevron_back.xml` | SHA-256 recorded after exact export and conversion | Planned |
| Dropdown arrow | UI-001 | Registration states; child `4922:16850` | `docs/assets/source/registration/arrow-down.svg` | `app/src/main/res/drawable/ic_arrow_down.xml` | SHA-256 recorded after exact export and conversion | Planned |
| Radio selected glyph | UI-001 | Registration filled state; group `4922:17456` | `docs/assets/source/registration/radio-check.svg` | `app/src/main/res/drawable/ic_radio_check.xml` | SHA-256 recorded after exact export and conversion | Planned |

## Feature-owned Assets

| Consumer | Asset type | Ownership note |
|---|---|---|
| `UI-002-auth` | City skyline | Source archive `docs/assets/source/login/city-skyline.svg`; runtime `app/src/main/res/drawable/bg_login_city_skyline.xml`; SHA-256 and conversion evidence recorded by UI-002. |
| `UI-002-auth` | Captcha Preview/test fixture | Source node is the applicable Login state; runtime `app/src/debug/res/drawable/login_captcha_fixture.png`; never shipped or treated as production captcha data. |
| `UI-002-auth` | Registration user illustration | Source states `4922:17227`, `4922:17493`; source archive `docs/assets/source/registration/user-illustration.svg`; runtime `app/src/main/res/drawable/ic_registration_user.xml`; SHA-256 and conversion evidence recorded by UI-002. |
| `UI-003-map-shell` | Map shell and drawer-specific icons | Must be exported or implemented by UI-003; not provided by UI-001. |

## Constraints

- Do not recreate approved logos, captcha images, or city illustrations from screenshots.
- Feature code must consume semantic foundation tokens instead of raw visual values.
- Any approved Figma change invalidates visual evidence and requires revalidation before release.
