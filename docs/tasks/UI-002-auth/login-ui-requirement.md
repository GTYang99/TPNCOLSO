# Login UI Requirement

## Authorized Revision 4 Delta — 2026-10-14
- Authority: current requester decisions, recorded as `SRC-017` / `KD-AUTH-013` and `SRC-018` / `KD-AUTH-014` in the active UI-002 Knowledge artifacts.
- Remove the CAPTCHA input, image, reload affordance, local four-digit validation, CAPTCHA provider/state/fixture, and CAPTCHA-specific accessibility/IME behavior.
- Update the auth failure copy to exactly `帳號或密碼錯誤`; apply error borders only to the account and password fields.
- Remove the CAPTCHA row and its adjacent spacing without leaving a placeholder. Keep the brand, account, and password positions. Move Remember me, registration entry, and Login button upward using the existing UI-001 spacing tokens.
- The R10 composite is the pixel authority only for unchanged regions. Validate the reflowed controls in an R11 runtime capture against their order and existing spacing tokens; do not require old R10 y-coordinates or claim pixel parity for the changed region.
- Other Login and all Register/Logout requirements in revision 3 remain active unless independently changed.
- Revision 4 is the current approved requirement; implementation follows Plan Review iteration 19 under plan revision 18.

## Status and Authority

- Status: approved R11 Login requirement, revision 4; Plan Review iteration 19 approved plan revision 18
- Design owner / approver: Requester
- Approval date: 2026-09-07 (Asia/Taipei)
- Authoritative visual source: `/var/folders/wm/_jpdxgws6mn1jxpy1k6jjmj80000gp/T/codex-clipboard-52d4f92d-7688-4c67-99f9-f6e97ce5f1dc.png`
- Source identity: PNG, 7904 × 2916 px, sRGB, SHA-256 `75cb578c58f3098c270290f033539ebf06c35c8b322efb71526fa4e53f3d3e9c`
- Reference UI canvas: each Login panel represents a 402 × 874 logical-unit screen at approximately 2× display scale.
- The outer gray board and headings `登入頁`／`登入失敗` are documentation chrome and MUST NOT appear in Android UI.
- This revision and source supersede every prior Login screenshot and Figma frame for visual acceptance.

## Source Precedence and State Authority

| Requirement state | Visual authority | Meaning |
|---|---|---|
| `LOGIN_EMPTY` | First Login panel in the current composite | Exact empty composition, background, placement and controls |
| `LOGIN_FILLED` | Second Login panel in the current composite | Filled account/password and selected Remember me; ignore the superseded CAPTCHA region |
| `LOGIN_FIELD_ERROR` | Derived Android/product state | Local validation messages and focus; not supplied by the screenshot |
| `LOGIN_AUTH_ERROR` | Third Login panel under `登入失敗` | Retained values, account/password error borders and one global error message |
| `LOGIN_SUBMITTING` | Derived from foundation behavior | Same resting geometry, repeat submission disabled and progress semantics |

Prior standalone screenshot and Login Figma nodes `2905:2679`, `4922:15312`, `4922:15370` are superseded visual evidence. Candidate vector assets may be reused only after comparison proves they reproduce the current composite.

## Reference Coordinate System

- All layout values below are logical `dp` at the 402 × 874 comparison viewport unless stated otherwise.
- Screenshot measurements are normalized from each white screen panel at approximately 2× scale. Raster-derived positions use a ±3dp comparison tolerance; fixed component sizes and text copy do not use that tolerance.
- Screenshot comparison crops exclude the gray board and headings outside each screen.
- The 402 × 874 baseline contains no rendered Android status or navigation bars. Device validation separately applies system insets.

## Screen Background and Layering

1. Fill the entire 402 × 874 canvas with a vertical gradient: white at the top to the approved pale blue-lavender foundation surface near `#E7E9F6` at the bottom.
2. The gradient is continuous from top to bottom; it is not a white card, centered panel, solid gray screen or Material surface container.
3. Place interactive content above the decorative skyline. The skyline never intercepts pointer or accessibility input.
4. Do not vertically center or evenly distribute the page. R10 anchors remain authoritative for unchanged regions; the three lower controls follow the approved R11 reflow rule.
5. The approved Login reference is light-only. System dark mode must not recolor, invert or replace this background.

## Reference Placement

| Group | Reference placement |
|---|---|
| Main content column | left 41, width 320, horizontally centered |
| Brand header | top 151, height 65, centered within the canvas |
| Account group | label begins around y=251; control begins around y=278 |
| Password group | label begins around y=338; control begins around y=365 |
| Captcha group | Removed by revision 4; these R10 coordinates are historical only |
| Auxiliary row | Reflowed upward after CAPTCHA removal; R10 y=539 is historical. Preserve UI-001 spacing and 48dp minimum interaction height. |
| Login button | Reflowed upward after CAPTCHA removal; R10 y=590 is historical. Preserve width 320 and 44 visual height. |
| Skyline | bottom-anchored and clipped; Taipei 101 tip appears around y=651 |

These anchors prevent implementations that reproduce individual controls but place the whole form too high, too low or inside the wrong background container.

## Brand Header

- Horizontal centered row with 8 spacing.
- Logo is visually 96 × 65 and uses the exact approved asset; do not substitute a text glyph or approximate mark.
- Title contains exactly two lines:
  - `新工處土地占用`
  - `調查圖台系統`
- Title uses Noto Sans TC Bold, 28sp, line height 38sp, black.
- The second line uses the screenshot-approved expanded tracking, approximately 5 logical units.
- Logo and title form one accessibility heading; the decorative logo has no duplicate spoken label.

## Credential Form

Order in revision 4 is account, password; there is no CAPTCHA control.

### Shared field geometry

- Content width is 320.
- Labels use Noto Sans TC Bold 16sp/24sp and `text-color-primary` (`#303133`).
- Label-to-control spacing is 8.
- Account and password controls are 320 × 48 with white fill, 1 border `#DCDFE6`, radius 4, 16 horizontal padding and 12 vertical padding.
- Empty placeholder is exactly `請輸入`, Noto Sans TC Regular 16sp/24sp, `#A8ABB2`.
- Filled text uses Noto Sans TC Regular 16sp/24sp and `#606266`.
- Adjacent credential field groups retain approximately 16 vertical spacing.
- Password is masked by default. The eye glyph is visually about 14 × 14 and sits in a minimum 48 × 48 semantics target without shifting the field geometry.

### Captcha row — removed by revision 4

The dimensions and interactions in this historical R10 subsection no longer apply. Revision 4 has no CAPTCHA input, image, reload action, state or placeholder row.

- The full row is 320 × 48 and uses this fixed left-to-right order: input, image, reload.
- Captcha input is approximately 133 × 48.
- Gap between input and image is 8.
- Captcha image is approximately 139 × 48 and preserves its supplied bitmap aspect without stretch.
- Gap between image and the trailing reload allocation is 8.
- Reload has an approximately 32-wide layout allocation; the visible glyph is approximately 24 × 24 and its effective semantics target is at least 48 × 48 without moving adjacent content.
- Reload requests a new captcha without clearing account, password or Remember me.
- The bitmap visible in the screenshot is a Preview/test fixture only. Production content comes from the later Auth source.

## Auxiliary Action Row

- The row occupies the same 320-wide column. Left and right groups align to opposite edges.
- Left group: unchecked checkbox visual 14 × 14, radius 2, followed by 8 spacing and text `記住我`.
- Checked state uses `color-primary` and the exact check glyph. The complete labeled row is a single selectable target at least 48 high.
- Right group copy is `沒有帳號? ` followed by actionable `註冊`, with 4 spacing.
- Text uses Noto Sans TC Medium 16sp/24sp; normal copy is `#303133`, action is the approved primary blue.
- Only `註冊` navigates. The combined phrase remains understandable to accessibility services.

## Login Button

- In R11, place the button after the auxiliary row using existing UI-001 spacing; preserve this reflowed resting position across submitting state.
- Width 320, visual height 44, approved primary-blue fill, pill radius 999 and white label.
- Label is exactly `登入`, Noto Sans TC Bold 20sp/28sp.
- Effective target is at least 48 high.
- Submitting blocks repeat activation and announces progress without changing resting width, R11 resting position or corner shape.

## Decorative Skyline

- Use the exact blue city-silhouette asset shown in the screenshot: bridge at left, Taipei 101 near left-center and buildings across the bottom.
- Reference asset geometry remains approximately 736 × 246, offset left around -138 and below the canvas around -7, then clipped to 402 width.
- The visible Taipei 101 tip begins around y=651; the base of the silhouette reaches beyond the bottom crop.
- Skyline and logo use the same approved semantic primary-blue appearance. Exact color is verified against the composite's sRGB profile and mapped through the foundation token; do not sample an anti-aliased pixel as a new raw feature color.
- Skyline is decorative, excluded from accessibility traversal and cannot cover the Login button.

## State and Validation Matrix

| Element | `LOGIN_EMPTY` | `LOGIN_FILLED` | `LOGIN_AUTH_ERROR` / derived submitting |
|---|---|---|---|
| Account | Empty, `請輸入` | Fixture `sunrise000` | Local or auth-error border; retain after request error |
| Password | Empty, masked mode | Fixture shown as eight masking glyphs | Never persist or expose; auth-error border |
| Captcha | Not present in revision 4 | Not present in revision 4 | No CAPTCHA state or local validation |
| Remember me | Unselected | Selected | Retained after request error |
| Global error | Hidden | Hidden | Exactly `帳號或密碼錯誤` after auth failure |
| Login button | Enabled subject to local validation | Enabled | Single-submit; re-enabled when request finishes |

For `LOGIN_AUTH_ERROR`, account and password borders use `fieldErrorBorder` (`#C8320A`). The global message uses Noto Sans TC Regular 14sp/22sp and `errorText` (`#E00000`). Validate the reflowed lower controls in an R11 runtime capture using their order and existing UI-001 spacing tokens.

## Responsive Android Adaptation

- 402 × 874 is the visual comparison baseline, not a fixed device requirement.
- On wider screens, keep the content column centered with maximum width 320; preserve unchanged R10 anchors and the approved R11 reflow relationships where height permits.
- On screens too narrow for 320 plus safe margins, keep at least 24 horizontal padding and shrink all full-width controls together.
- Respect status, navigation and IME insets. When usable height is smaller, the complete interactive layer scrolls as one unit while the skyline remains bottom decorative and may crop further.
- At enlarged font scale, preserve content order and readable text even when vertical anchors must expand. Do not clip labels, error copy or button copy.

## Interaction and Accessibility

- Initial focus is not forced unless platform behavior requires it.
- IME order is account → password → Login.
- Password eye content description reflects the action: `顯示密碼` or `隱藏密碼`.
- No CAPTCHA reload action or related content description exists in revision 4.
- Authentication error is associated with the account and password controls and announced once.
- Password values are excluded from logs, persistent/saved state, screenshots and exposed semantics values.

## Asset Requirements

| Asset | Ownership | Current authority and verification rule |
|---|---|---|
| Brand logo | UI-001 shared | Must visually match the current composite; any former export is only a candidate implementation asset. |
| City skyline | UI-002 auth | Must match the current composite's silhouette, scale, offset and clipping. |
| Password eye | UI-001 shared | Match the screenshot's approximately 14 visual size and minimum 48 target. |
| Reload icon | Not applicable in revision 4 | Login has no CAPTCHA reload action. |
| Checkbox check | UI-001 shared | Not visible in the authoritative empty screenshot; checked appearance remains a derived approved state. |
| Captcha sample | Not applicable in revision 4 | Do not retain or consume Login CAPTCHA fixtures. |

Implementation must archive the approved composite unchanged under project evidence, record its SHA-256 and panel crop coordinates, and record each source/runtime asset path and checksum in `docs/assets/app-ui-assets.md`. No logo or skyline may be manually reconstructed when a candidate source asset passes comparison against the composite.

## Acceptance Criteria

- `UIR-LOGIN-001`: At the 402 × 874 comparison viewport, unchanged regions match the current composite's white-to-pale-blue gradient, brand/account/password anchors, centered 320-wide content and bottom-clipped skyline; gray board and headings are absent. The R11 runtime capture establishes placement of reflowed controls.
- `UIR-LOGIN-002`: Brand header matches the screenshot's y-position, 96 × 65 logo, two-line title, typography, line height and expanded second-line tracking.
- `UIR-LOGIN-003`: Account and password groups match the screenshot's order, y-anchors, 320 × 48 controls, labels, borders, padding, typography and empty presentation.
- `UIR-LOGIN-004`: Login has no CAPTCHA input, image or reload control and leaves no empty placeholder row.
- `UIR-LOGIN-005`: Empty, filled and authentication-error comparisons use the composite and its source SHA-256 only for unchanged regions; R11 runtime capture validates the reflowed controls. Field-error and submitting are derived states.
- `UIR-LOGIN-006`: Authentication failure retains allowed values, applies `#C8320A` borders to account/password only and displays exactly `帳號或密碼錯誤` in `#E00000` with accessible semantics.
- `UIR-LOGIN-007`: Remember-me and Registration controls follow the approved R11 upward reflow, retain UI-001 spacing, alignment, copy and colors, and provide effective targets of at least 48.
- `UIR-LOGIN-008`: Login button follows the approved R11 upward reflow, retains 320 × 44 pill geometry, typography and primary-blue appearance, and suppresses duplicate activation while submitting.
- `UIR-LOGIN-009`: Screenshot identity and all Login assets are traceable by path and SHA-256; prior Figma asset exports are reused only when comparison proves they match the current screenshot.
- `UIR-LOGIN-010`: The screen remains usable with Android system insets, IME, narrow widths and enlarged fonts without losing content order, controls, errors or primary action.

## Explicitly Out of Scope

- Registration screen visual details.
- Drawer/logout visual details.
- Real Auth API, Token storage and company data.
- Map or authenticated home implementation.
