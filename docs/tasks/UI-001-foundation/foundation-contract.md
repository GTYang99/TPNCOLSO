# UI-001 Reusable Foundation Contract

## Purpose

- 定義 UI-002 至 UI-009 可直接依賴的 Compose theme、元件、狀態、app-root、session、semantics 與測試邊界。
- 本 contract 固定 API ownership 與行為；semantic token 與共用品牌 asset 值須追溯至 requester 於 2026-09-07 核定的正式 Figma UI。

## Package Ownership

| Package | Owns | Must not own |
|---|---|---|
| `ui.foundation.theme` | `AppTheme`、semantic colors、typography、shape、spacing accessors | feature-specific colors、raw Figma values |
| `ui.foundation.component` | 共用輸入、按鈕、選項、狀態與只讀元件 | Login、Parcel、Survey 等業務流程 |
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

Token values MUST map to the approved Figma source through semantic roles. A later approved design change MAY replace token values but MUST NOT change feature component signatures unless a new Plan Review approves a breaking contract change.

## Component Contract

Every component accepts `modifier` and exposes state through parameters; components MUST NOT own a ViewModel, repository, navigation, coroutine scope, or fake data source.

| Component | Required inputs | Required states / behavior |
|---|---|---|
| `AppTextField` | value, onValueChange, label, placeholder, required, enabled, readOnly, isError, supportingText, keyboardOptions, keyboardActions, singleLine | default, focused, disabled, readonly, required, error |
| `AppPasswordField` | value, onValueChange, label, placeholder, visible, onVisibilityChange, visibilityActionEnabled, isError, supportingText, keyboardOptions, keyboardActions, singleLine | masked by default; optional visibility action has content description; Registration disables the action because its approved states show no eye control |
| `AppSelectField<T>` | selected, options, itemLabel, label, onSelect, enabled, isError | closed, open, selected, disabled, error |
| `AppRadioGroup<T>` | options, selected, onSelect, label, enabled, isError | exactly zero or one selected; full row is clickable |
| `AppCheckboxRow` | checked, onCheckedChange, label, enabled | visual checkbox may be smaller, but the full labeled row is a single selectable target of at least 48dp |
| `AppPrimaryButton` | text, onClick, enabled, loading | blocks repeat action and exposes progress semantics while loading |
| `AppSecondaryButton` | text, onClick, enabled | enabled, pressed, disabled |
| `AppIconButton` | icon painter, contentDescription, onClick, enabled | mandatory nonblank content description |
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
- Debug source set may extend the owner with `startDebugSession(role)`; release source set cannot reference that extension.
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
- Icon-only actions require a nonblank Traditional Chinese content description.
- Loading, error, selection and status cannot be color-only.
- Focus order follows visual reading order; modal ownership remains feature-specific.
- Tests prefer visible labels, roles and content descriptions; test tags are allowed only when semantics cannot identify a stable element.

## Preview and Test Contract

- Every foundation component has previews for default plus applicable loading／disabled／error／readonly states.
- Preview data is deterministic and visibly fake; it contains no password, Token or personal data.
- Foundation tests cover component semantics, disabled/loading click suppression, password masking, password visibility action enabled/disabled variants, radio exclusivity and minimum interactive sizing.
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
- Shared brand assets are owned by UI-001; feature-only illustrations remain feature-owned. Both MUST be registered with their Figma node and retrieval date rather than recreated from screenshots.
- UI-001 owns the Login brand logo, password-visibility, captcha-reload and checkbox-check icons plus Registration back, dropdown and selected-radio icons at the exact source/runtime paths in `docs/assets/app-ui-assets.md`; UI-002 owns the Login city skyline, debug captcha fixture and Registration user illustration.
