# Repository Analysis

## Current Behavior
- `SurveyFormScreen` 以 `FloatingActionButton` 觸發 `SurveyFormCallbacks.onCaptureRequested`；debug host 直接建立 `local://debug/...` synthetic `SurveyFormPhoto`。
- UI-006 photo tile 只顯示文字與拍攝時間，沒有 CameraX preview、相機權限、實際 local file 或圖片縮圖。
- app 是單 module Jetpack Compose MVVM；目前沒有 CameraX dependency，但 Gradle cache 已有 CameraX `1.4.1` artifacts。

## Expected Behavior
- 新增 feature-owned CameraX route、immutable state/events/effects 與 ViewModel；成功拍攝寫入 app-private cache，將 URI 與 timestamp 回傳給 UI-006。
- UI-006 photo section 改成 Figma 對應的 168×126dp camera/photo tile，支援 local file thumbnail、時間 overlay、四張上限與既有刪除 callback。
- debug host 以 UI-007 route 取代 synthetic capture；release 維持原有 signed-out entry，不新增未授權的 production flow。

## Affected Modules
- `app/src/main/java/.../feature/photocamera/`：CameraX contract、ViewModel、route、preview/capture lifecycle 與 local file aspect check。
- `app/src/main/java/.../feature/surveyform/SurveyFormScreen.kt`：photo tile/camera tile/thumbnail rendering，保留 UI-006 state ownership。
- `app/src/debug/java/.../AppEntry.kt`：open/close UI-007 route，將 captured photo map 成 UI-006 `SurveyFormPhoto`。
- `app/build.gradle.kts`, `gradle/libs.versions.toml`, `app/src/main/AndroidManifest.xml`：CameraX dependencies、permission 與 optional camera feature。
- `app/src/main/res/drawable-nodpi/ic_photo_camera.png`, `ic_photo_delete.png`：由 Figma 匯出、供 Android resource 使用的 camera/delete assets。
- `app/src/test/.../feature/photocamera/`, `app/src/androidTest/.../feature/photocamera/`, `app/src/androidTest/.../feature/surveyform/`：state and semantics coverage。

## Dependencies
- Active Knowledge baseline `KB-UI-007-PHOTO-CAMERA-R1` and UI-006 committed handoff `80ed4ee`.
- AndroidX CameraX core/camera2/lifecycle/view `1.4.1`; Android camera runtime permission.
- Existing `SurveyFormCallbacks.onCaptureRequested`, `SurveyFormEvent.CapturePhoto`, `AppThemeTokens` and Compose foundation components.
- Figma file `HRbRsw6HoNBUCtaieX8xUM`, node `2938:193`, photo states `4947:26040`, `4947:26547`, `4947:27279`.

## Risks
- Camera permission, emulator hardware and lifecycle rebinding can make connected hardware capture nondeterministic; pure route/state tests must cover deterministic behavior and an emulator smoke should be recorded separately.
- CameraX output orientation can expose portrait dimensions despite a 4:3 target; aspect validation must accept orientation-equivalent 4:3 and reject other ratios.
- Existing UI-006 tests rely on the `survey-camera` semantics tag; preserve it while changing the visual tile.
- Figma has no dedicated preview frame and the photo bitmap is dynamic; visual evidence will claim parity only for the photo-area tile geometry, not the preview surface.

## Unknowns
- No approved production upload contract or OEM-specific permission settings route is available; both remain out of scope and explicitly deferred.
