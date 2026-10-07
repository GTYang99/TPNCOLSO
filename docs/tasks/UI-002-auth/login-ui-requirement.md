# Login UI Requirement

## Status and Authority

- Status: approved requirement baseline, revision 3
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
| `LOGIN_FILLED` | Second Login panel in the current composite | Filled account/password/captcha and selected Remember me |
| `LOGIN_FIELD_ERROR` | Derived Android/product state | Local validation messages and focus; not supplied by the screenshot |
| `LOGIN_AUTH_ERROR` | Third Login panel under `登入失敗` | Retained values, three error borders and one global error message |
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
4. Do not vertically center or evenly distribute the page. The screenshot's explicit vertical anchors are part of the requirement.
5. The approved Login reference is light-only. System dark mode must not recolor, invert or replace this background.

## Reference Placement

| Group | Reference placement |
|---|---|
| Main content column | left 41, width 320, horizontally centered |
| Brand header | top 151, height 65, centered within the canvas |
| Account group | label begins around y=251; control begins around y=278 |
| Password group | label begins around y=338; control begins around y=365 |
| Captcha group | label begins around y=428; row begins around y=454 |
| Auxiliary row | visual content around y=539; effective interaction row at least 48 high |
| Login button | top around y=590, width 320, visual height 44 |
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

Order is fixed: account, password, captcha.

### Shared field geometry

- Content width is 320.
- Labels use Noto Sans TC Bold 16sp/24sp and `text-color-primary` (`#303133`).
- Label-to-control spacing is 8.
- Account and password controls are 320 × 48 with white fill, 1 border `#DCDFE6`, radius 4, 16 horizontal padding and 12 vertical padding.
- Empty placeholder is exactly `請輸入`, Noto Sans TC Regular 16sp/24sp, `#A8ABB2`.
- Filled text uses Noto Sans TC Regular 16sp/24sp and `#606266`.
- Adjacent credential field groups retain approximately 16 vertical spacing.
- Password is masked by default. The eye glyph is visually about 14 × 14 and sits in a minimum 48 × 48 semantics target without shifting the field geometry.

### Captcha row

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

- Position follows the reference anchor; do not push the button to the bottom of the available screen.
- Width 320, visual height 44, approved primary-blue fill, pill radius 999 and white label.
- Label is exactly `登入`, Noto Sans TC Bold 20sp/28sp.
- Effective target is at least 48 high.
- Submitting blocks repeat activation and announces progress without changing resting width, vertical anchor or corner shape.

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
| Captcha | Empty input plus fixture image | Fixture `0926` | Local or auth-error border; refresh independent of submit |
| Remember me | Unselected | Selected | Retained after request error |
| Global error | Hidden | Hidden | Exactly `帳號、密碼或驗證碼錯誤` after auth failure |
| Login button | Enabled subject to local validation | Enabled | Single-submit; re-enabled when request finishes |

For `LOGIN_AUTH_ERROR`, all three credential borders use `fieldErrorBorder` (`#C8320A`). The global message uses Noto Sans TC Regular 14sp/22sp and `errorText` (`#E00000`). It appears below the captcha row as shown in the third authoritative panel and shifts the auxiliary row and button downward without overlapping them.

## Responsive Android Adaptation

- 402 × 874 is the visual comparison baseline, not a fixed device requirement.
- On wider screens, keep the content column centered with maximum width 320 and preserve the reference vertical anchors where height permits.
- On screens too narrow for 320 plus safe margins, keep at least 24 horizontal padding and shrink all full-width controls together; do not independently distort the captcha image.
- Respect status, navigation and IME insets. When usable height is smaller, the complete interactive layer scrolls as one unit while the skyline remains bottom decorative and may crop further.
- At enlarged font scale, preserve content order and readable text even when vertical anchors must expand. Do not clip labels, error copy or button copy.

## Interaction and Accessibility

- Initial focus is not forced unless platform behavior requires it.
- IME order is account → password → captcha → Login.
- Password eye content description reflects the action: `顯示密碼` or `隱藏密碼`.
- Reload content description is `重新產生驗證碼`.
- Authentication error is associated with all three credential controls and announced once.
- Password and captcha values are excluded from logs, persistent/saved state, screenshots and exposed semantics values.

## Asset Requirements

| Asset | Ownership | Current authority and verification rule |
|---|---|---|
| Brand logo | UI-001 shared | Must visually match the current composite; any former export is only a candidate implementation asset. |
| City skyline | UI-002 auth | Must match the current composite's silhouette, scale, offset and clipping. |
| Password eye | UI-001 shared | Match the screenshot's approximately 14 visual size and minimum 48 target. |
| Reload icon | UI-001 shared | Match the screenshot's approximately 24 visual size in the 32-wide trailing allocation and minimum 48 target. |
| Checkbox check | UI-001 shared | Not visible in the authoritative empty screenshot; checked appearance remains a derived approved state. |
| Captcha sample | UI-002 debug/test | A deterministic fixture may be archived for Preview/test only and must never be treated as production captcha data. |

Implementation must archive the approved composite unchanged under project evidence, record its SHA-256 and panel crop coordinates, and record each source/runtime asset path and checksum in `docs/assets/app-ui-assets.md`. No logo or skyline may be manually reconstructed when a candidate source asset passes comparison against the composite.

## Acceptance Criteria

- `UIR-LOGIN-001`: At the 402 × 874 comparison viewport, each Login crop matches the current composite's white-to-pale-blue gradient, explicit vertical anchors, centered 320-wide content and bottom-clipped skyline; gray board and headings are absent.
- `UIR-LOGIN-002`: Brand header matches the screenshot's y-position, 96 × 65 logo, two-line title, typography, line height and expanded second-line tracking.
- `UIR-LOGIN-003`: Account and password groups match the screenshot's order, y-anchors, 320 × 48 controls, labels, borders, padding, typography and empty presentation.
- `UIR-LOGIN-004`: Captcha input, image and reload preserve the screenshot's order, 320 × 48 row, 133/139/32 allocations, spacing, image aspect and visible reload size.
- `UIR-LOGIN-005`: Empty, filled and authentication-error pixel comparisons use only the three current composite panels and source SHA-256; field-error and submitting are explicitly derived states.
- `UIR-LOGIN-006`: Authentication failure retains allowed values, applies `#C8320A` borders to all three credential controls and displays exactly `帳號、密碼或驗證碼錯誤` in `#E00000` with accessible semantics.
- `UIR-LOGIN-007`: Remember-me and Registration controls match the screenshot's y-position, alignment, copy and colors, with effective targets of at least 48.
- `UIR-LOGIN-008`: Login button matches the screenshot's y-position, 320 × 44 pill geometry, typography and primary-blue appearance, and suppresses duplicate activation while submitting.
- `UIR-LOGIN-009`: Screenshot identity and all Login assets are traceable by path and SHA-256; prior Figma asset exports are reused only when comparison proves they match the current screenshot.
- `UIR-LOGIN-010`: The screen remains usable with Android system insets, IME, narrow widths and enlarged fonts without losing content order, controls, errors or primary action.

## Explicitly Out of Scope

- Registration screen visual details.
- Drawer/logout visual details.
- Real Auth API, Token storage, production captcha endpoint and company data.
- Map or authenticated home implementation.
