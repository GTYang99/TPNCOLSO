# UI-001 Reusable Foundation Contract

## Purpose

- 定義 UI-002 至 UI-009 可直接依賴的 Compose theme、元件、狀態、app-root、session、semantics 與測試邊界。
- 本 contract 固定 API ownership 與行為；auth semantic tokens 與 component variants 須追溯至 current composite，iconography 改用 Android／Material 預設 icon，不以自有 icon asset 比對 current composite。

## Package Ownership

| Package | Owns | Must not own |
|---|---|---|
| `ui.foundation.theme` | `AppTheme`、semantic colors、typography、shape、spacing accessors | feature-specific colors、raw Figma values |
| `ui.foundation.component` | 共用輸入、按鈕、選項、狀態、只讀元件與 Material/system icon slots | Login、Parcel、Survey 等業務流程或自有 icon asset catalog |
| `ui.foundation.state` | 通用 loading/content/empty/error presentation state | HTTP status、API DTO |
| `ui.foundation.preview` | deterministic preview fixtures／containers | production data、credentials |
| `session` | `AppSessionState`、`AppIdentity`、`AppRole`、session owner interface | Token schema、Retrofit、persistent storage |
| app root | signed-out／signed-in content slots | feature navigation graph、debug-only implementation |
| `src/debug` | direct-login UI、in-memory fake session | production auth behavior |
| `src/release` | no-bypass app entry | debug class、fake authenticated identity |

## Theme Contract

`AppTheme` MUST be the only feature-facing theme entry. Feature code MUST use semantic tokens rather than raw visual values.

### Semantic colors

- `brandPrimary`
- `onBrandPrimary`
- `textPrimary`
- `textSecondary`
- `borderDefault`
- `surface`
- `surfaceMuted`
- `error`
- `onError`
- `fieldErrorBorder` = `#C8320A`
- `errorText` = `#E00000`
- `scrim`

### Typography roles

- `screenTitle`
- `sectionTitle`
- `fieldLabel`
- `body`
- `supporting`
- `buttonLabel`

### Shape and spacing roles

- Shapes: `small`, `medium`, `pill`.
- Spacing: `xs`, `sm`, `md`, `lg`, `xl`.
- Interactive size: `minimumTouchTarget` MUST be at least 48dp.

Token values MUST map to the approved owning visual source through semantic roles. A later approved design change MAY replace token values but MUST NOT change feature component signatures unless a new Plan Review approves a breaking contract change.

For Login, `surface` and `surfaceMuted` must reproduce the current composite's continuous white-to-pale-blue background when composed by UI-002. Former screenshots/Figma cannot override it; exact rules live in `login-ui-requirement.md` revision 3.

## Component Contract

Every component accepts `modifier` and exposes state through parameters; components MUST NOT own a ViewModel, repository, navigation, coroutine scope, or fake data source.

| Component | Required inputs | Required states / behavior |
|---|---|---|
| `AppTextField` | value, onValueChange, label, placeholder, required, enabled, readOnly, isError, supportingText, keyboardOptions, keyboardActions, singleLine | default, focused, disabled, readonly, required, error |
| `AppPasswordField` | value, onValueChange, label, placeholder, visualTransformation, visible, onVisibilityChange, visibilityActionEnabled, required, enabled, readOnly, isError, supportingText, keyboardOptions, keyboardActions, singleLine | Login supports masked＋eye; Registration supports plaintext＋no-eye; required, disabled, readonly and error semantics match `AppTextField`; sensitive value is caller-owned and never saved by the component |
| `AppSelectField<T>` | selected, options, itemLabel, label, onSelect, enabled, isError | closed, open, selected, disabled, error |
| `AppRadioGroup<T>` | options, selected, onSelect, label, enabled, isError | exactly zero or one selected; full row is clickable |
| `AppCheckboxRow` | checked, onCheckedChange, label, enabled | visual checkbox may be smaller, but the full labeled row is a single selectable target of at least 48dp |
| `AppPrimaryButton` | text, onClick, enabled, loading | blocks repeat action and exposes progress semantics while loading |
| `AppSecondaryButton` | text, onClick, enabled | enabled, pressed, disabled |
| `AppIconButton` | Material/system icon imageVector, contentDescription, onClick, enabled | mandatory nonblank content description; no custom drawable source is required |
| `AppStatusBadge` | label, tone | status is conveyed by text, never color alone |
| `AppLoadingContent` | message | announces loading without duplicate announcements |
| `AppEmptyContent` | title, optional body/action | readable empty state |
| `AppErrorContent` | message, optional retry | readable error and optional retry action |
| `AppReadOnlyField` | label, value | missing value presentation is supplied by caller policy |

Feature-specific wrappers MAY compose these primitives but MUST NOT change their accessibility or loading guarantees.

## Presentation State Contract

```kotlin
sealed interface ContentState<out T> {
    data object Idle : ContentState<Nothing>
    data object Loading : ContentState<Nothing>
    data class Content<T>(val value: T) : ContentState<T>
    data object Empty : ContentState<Nothing>
    data class Error(val message: String, val retryable: Boolean) : ContentState<Nothing>
}
```

- Domain-specific UI states may contain `ContentState`, but foundation MUST NOT contain HTTP codes or API payloads.
- One-shot navigation or message events remain feature-owned and MUST NOT be embedded in persistent `ContentState`.

## Session and App-Root Contract

```kotlin
enum class AppRole { INVESTIGATOR, INTERNAL_STAFF, ADMINISTRATOR }

data class AppIdentity(
    val displayName: String,
    val role: AppRole,
)

sealed interface AppSessionState {
    data object SignedOut : AppSessionState
    data class SignedIn(val identity: AppIdentity) : AppSessionState
}

interface AppSessionOwner {
    val state: StateFlow<AppSessionState>
    fun clear()
}
```

- `StateFlow` is part of the public contract, so `kotlinx-coroutines-core` MUST be declared directly rather than supplied only by a transitive dependency.
- Main contract has no `login()` method: production login belongs to Auth integration.
- Debug source set exposes the exact extension boundary below; release/main source sets cannot reference it:

```kotlin
interface DebugSessionController : AppSessionOwner {
    fun startDebugSession(role: AppRole)
}
```

- `DebugSessionOwner` implements `DebugSessionController`. UI-002 composition-root coordinator may depend on this debug-only interface; AuthViewModel, AuthHost and screens may not.
- `AppRoot` receives `sessionState`, `signedOutContent`, and `signedInContent(identity)` slots.
- `AppRoot` does not know Login, Map, Navigation Compose, Retrofit, Token, or persistence.

## Debug Direct-Login Contract

- Initial state is `SignedOut`.
- Fake roles are selectable: investigator, internal staff, administrator.
- `startDebugSession` creates `AppIdentity(displayName = "開發測試人員", role = selectedRole)` in memory only.
- Repeated taps while already signed in do not create additional state transitions.
- Clear/logout returns to `SignedOut`.
- Process restart returns to `SignedOut`.
- Direct login code and strings live under `src/debug`; release has no callable bypass.

## Accessibility and Semantics Contract

- Interactive targets are at least 48dp even when the visual glyph is smaller.
- Text fields expose their visible label; errors expose supporting text and error semantics.
- Password fields expose the same required, enabled, read-only, and error semantics as `AppTextField`.
- Icon-only actions require a nonblank Traditional Chinese content description.
- Loading and error content expose announcement/error semantics; selection and status cannot be color-only.
- Focus order follows visual reading order; modal ownership remains feature-specific.
- Tests prefer visible labels, roles and content descriptions; test tags are allowed only when semantics cannot identify a stable element.

## Preview and Test Contract

- Every foundation component has previews for default plus applicable loading／disabled／error／readonly states.
- Preview data is deterministic and visibly fake; it contains no password, Token or personal data.
- Foundation tests cover component semantics, disabled/loading click suppression, Login masked＋eye and Register plaintext＋no-eye password variants, radio zero-or-one/exclusivity and minimum interactive sizing.
- Debug direct-login tests cover all three roles, single transition and clear.
- Release checks compile the release variant and demonstrate that debug entry symbols／strings are absent.

## Consumer Handoff

| Consumer | Uses | Must supply |
|---|---|---|
| `UI-002-auth` | theme, fields, password, select, radio, buttons, AppRoot signed-out slot | auth state, validation, fake／production auth source |
| `UI-003-map-shell` | theme, icon button, status states, AppRoot signed-in slot | map shell, drawer, logout request |
| `UI-004`–`UI-008` | generic states and foundation primitives | feature-specific state and content |
| `UI-009` | accessibility／responsive contract | cross-screen device and restoration verification |
| `INT-001` | session boundary | Token storage, API login/logout, 401 mapping |

## Change Rules

- Changing token values after approved design is compatible when signatures and semantic roles remain unchanged.
- Removing or renaming a component／token, changing loading click behavior, altering session state shape, or adding Token／API types is a breaking contract change and requires Planning plus regression updates for every consumer.
- Feature code MUST NOT bypass this contract with duplicate primitives unless Plan Review records a documented mismatch.
- UI-001 does not own custom icon/logo drawable assets for v0.1. Password visibility, reload, checkbox, back, dropdown and selected-radio affordances MUST use Android／Material defaults through stable foundation component APIs with Traditional Chinese semantics and at least 48dp targets.
- Feature-specific illustrative media, if later approved, remains feature-owned and must be planned by that feature. UI-002 owns auth composite evidence and any non-icon decorative media it still requires; UI-001 does not require crop/scale proof for custom icon assets because those assets are out of scope.
