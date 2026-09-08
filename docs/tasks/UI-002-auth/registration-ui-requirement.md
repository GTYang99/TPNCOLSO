# Registration UI Requirement

## Status and Authority

- Status: approved requirement baseline, revision 2
- Design owner / approver: Requester
- Approval date: 2026-09-07 (Asia/Taipei)
- Authoritative visual source: `/var/folders/wm/_jpdxgws6mn1jxpy1k6jjmj80000gp/T/codex-clipboard-52d4f92d-7688-4c67-99f9-f6e97ce5f1dc.png`
- Source identity: PNG, 7904 × 2916 px, sRGB, SHA-256 `75cb578c58f3098c270290f033539ebf06c35c8b322efb71526fa4e53f3d3e9c`
- Reference canvas: 402 × 874
- Geometry values are normalized from the two Registration panels at approximately 2× scale and map one-to-one to Compose `dp` at the comparison baseline. The outer gray board and heading `註冊` are not App UI.
- This source supersedes all prior Registration screenshots and Figma nodes for visual acceptance.

## Authoritative State Panels

| Requirement state | Composite panel | Meaning |
|---|---|---|
| `REGISTER_EMPTY` | First Registration panel | Initial form with placeholders and no work type selected |
| `REGISTER_FILLED` | Second Registration panel | Demonstration values filled; external work type selected |

The values `sunrise1234`, `sfk;wfj1~`, `日陞` and `劉大君` are screenshot fixtures only. They must not become runtime defaults, credentials, production data or persistent state.

## Screen Composition

- Reference frame is 402 × 874 with a white background; content reaching the bottom remains inside the panel crop.
- Root content uses 24 padding on all sides and 36 vertical spacing between the top bar, user illustration, form and action row.
- Top bar, form and action row occupy the available 354 width.
- On a constrained-height Android device or while IME is visible, the whole interactive content scrolls; the top bar must remain reachable through normal scrolling and system Back must remain supported.
- The approved Registration reference is light-only and must not be automatically recolored by system dark mode until an authoritative dark design is approved.

## Top Bar and Illustration

- Top bar uses a three-slot row: 48 × 48 Back target, centered `註冊` title and an empty 48 × 48 balancing slot.
- Back glyph is visually 32 × 32 and uses the exact approved chevron asset rotated toward the left. Content description is `返回登入頁`.
- Title uses Noto Sans TC Bold 20/28 and `#303133`.
- User illustration is visually 80 × 80 and must reproduce the current composite. A prior export may be reused only after comparison proves it matches. It is decorative unless future product copy gives it independent meaning.

## Form Fields

Order is fixed:

1. `帳號`
2. `密碼`
3. `確認密碼`
4. `廠商名稱`
5. `作業性質`
6. `姓名(請輸入真實姓名)`

Shared geometry:

- Field groups have 16 vertical spacing; label-to-control spacing is 8.
- Labels use Noto Sans TC Medium 16/24 and `#303133`.
- Text/select controls are 354 × 48 at the reference canvas, with white fill, 1 border `#DCDFE6`, radius 4 and 16 horizontal / 12 vertical padding.
- Empty text placeholder is `請輸入`, Noto Sans TC Regular 16/24, `#A8ABB2`.
- Filled text is Noto Sans TC Regular 16/24, `#606266`.
- Account accepts only the approved alphanumeric policy and maximum 30 characters.
- Password and confirmation display their typed values as plaintext, matching the current authoritative filled panel.
- Registration does not add a password visibility action because the approved panels show plaintext without an eye control.
- Plaintext presentation does not authorize logging, analytics, persistence, autofill export or production fixtures containing real credentials.

## Vendor Selection

- Label is `廠商名稱`; empty value is `請選擇`.
- Select control uses the shared 354 × 48 geometry and a visually 14 × 14 downward-arrow asset aligned to the trailing edge.
- Options are supplied by an injected source; the composite value `日陞` is a fixture and not a built-in production option.
- Selection semantics expose current value, expanded state and disabled/error state. The full control is a target of at least 48 high.

## Work Type

- Label is `作業性質`; options are `外業人員` and `內業人員` in that order.
- `REGISTER_EMPTY` shows neither option selected. `REGISTER_FILLED` shows `外業人員` selected.
- Option group may wrap with 24 horizontal and 16 vertical gaps. Each visual row is 40 high with 8 between indicator and label.
- Radio indicator is visually 14 × 14, selected fill `#0D23AC`, unselected white with `#CDD0D6` border. The selected inner glyph is visually 10 × 10.
- Each complete label/indicator row is one selectable semantics target of at least 48 high. Initial selection is `null`; once selected, exactly one option is active. Completion treats no selection as a required-field error.

## Action Row

- The row is 354 wide with a 16 gap and two equal-width buttons, approximately 169 × 44 each.
- `取消`: white fill, 1 border `#DCDFE6`, pill radius 999, Noto Sans TC Bold 20/28, text `#303133`.
- `完成`: fill `#0D23AC`, pill radius 999, Noto Sans TC Bold 20/28, white text.
- Both have an effective target of at least 48 high. Submitting disables both repeated completion and destructive navigation races.
- Back and Cancel return to an empty Login screen without issuing a register request. Successful registration also clears Registration inputs and returns to empty Login; it does not automatically authenticate.

## State and Validation Matrix

| Element | `REGISTER_EMPTY` | `REGISTER_FILLED` | Derived validation/submitting behavior |
|---|---|---|---|
| Account | Empty, `請輸入` | Fixture value | Required, alphanumeric, max 30; invalid control and text error |
| Password | Empty, plaintext input | Plaintext fixture | Required, at least 8 characters; never persisted or logged |
| Confirm password | Empty, plaintext input | Plaintext fixture | Required and must match password; never persisted or logged |
| Vendor | `請選擇` | Fixture option | Required; options injected |
| Work type | No selection | External selected fixture | Required; exactly one selection after choice |
| Name | Empty, `請輸入` | Fixture value | Required and Chinese-only per approved product rule |
| Actions | Enabled subject to local validation | Enabled | Completion single-submit; loading semantics derived from foundation |

The composite supplies empty and filled visual states. Validation-error and submitting behavior are Android/product-derived states and must not be presented as pixel-matched source panels.

## Responsive and Accessibility Rules

- At widths above the baseline, center content with a maximum width of 354. At narrower widths, keep at least 24 horizontal padding and shrink all full-width controls together.
- Respect status/navigation/IME insets and support enlarged fonts without clipping labels, errors or action text.
- Focus/IME order follows the six controls, then Complete. Back/Cancel remain reachable with IME open.
- Errors use text plus semantics, not color alone. Focus moves to the first invalid control after attempted submission.
- Password and confirmation are intentionally visible on screen but remain excluded from logs, analytics, SavedStateHandle, `rememberSaveable` and non-fixture test evidence.

## Asset Requirements

| Asset | Owner | Source evidence | Planned path |
|---|---|---|---|
| Back chevron | UI-001 shared | Current composite Registration panels | `docs/assets/source/registration/chevron-back.svg` → `app/src/main/res/drawable/ic_chevron_back.xml` |
| Dropdown arrow | UI-001 shared | Current composite Registration panels | `docs/assets/source/registration/arrow-down.svg` → `app/src/main/res/drawable/ic_arrow_down.xml` |
| Selected radio glyph | UI-001 shared | Current composite filled Registration panel | `docs/assets/source/registration/radio-check.svg` → `app/src/main/res/drawable/ic_radio_check.xml` |
| User illustration | UI-002 Registration | Current composite Registration panels | `docs/assets/source/registration/user-illustration.svg` → `app/src/main/res/drawable/ic_registration_user.xml` |

Implementation records composite SHA-256/panel crops, candidate asset bytes, conversion result and runtime visual comparison in `docs/assets/app-ui-assets.md`. Prior Figma exports may be used only as candidate implementation assets that pass comparison against the current composite.

## Acceptance Criteria

- `UIR-REG-001`: At 402 × 874, Registration matches the white light-only canvas, 24 padding, 36 main-group gaps and centered 354-wide content.
- `UIR-REG-002`: Top bar matches the 48/center/48 structure, exact Back asset, `註冊` typography and 80 × 80 user illustration.
- `UIR-REG-003`: Six controls appear in the exact approved order with matching labels, 16 group spacing, 8 label gap and 354 × 48 input geometry.
- `UIR-REG-004`: Empty and filled states map to the two current composite panels without using fixture values as runtime defaults or production credentials.
- `UIR-REG-005`: Vendor selection matches placeholder/value, arrow asset, injected options and accessible selection behavior.
- `UIR-REG-006`: Work type initially has no selection, is required on completion, enforces exactly one selection after choice and matches the approved radio geometry/colors with a 48dp target.
- `UIR-REG-007`: Cancel and Complete match the equal-width 354-row composition, 16 gap, pill styling, typography and single-submit behavior.
- `UIR-REG-008`: Back/Cancel and successful registration clear sensitive form state and return to empty Login; success does not authenticate automatically.
- `UIR-REG-009`: All validation rules have text/semantics evidence; password and confirmation render plaintext as approved but remain excluded from logs and persistent/saved state.
- `UIR-REG-010`: Composite identity and Registration assets are traceable to source/runtime paths and SHA-256 evidence; candidate vector reuse must pass visual comparison.
- `UIR-REG-011`: The screen remains usable with Android insets, IME, narrow widths and enlarged fonts without losing field order or actions.

## Explicitly Out of Scope

- Registration API schema, vendor production data and approval workflow.
- SMS, email or administrator verification.
- Login visual details, map, drawer and logout implementation.
