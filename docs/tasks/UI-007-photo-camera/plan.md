# Implementation Plan

## Goal
- 以 CameraX 接通 UI-006 的相機入口，完成四張 4:3 本機照片、Figma 尺寸縮圖與可恢復的權限／錯誤狀態。

## Scope
- 新增 UI-007 CameraX feature 與 debug host wiring。
- 調整 UI-006 photo section 的 camera/photo tile、local thumbnail、capture time 與四張上限呈現。
- 加入 CameraX dependency/permission、feature tests、debug/release build validation與 task evidence。
- 不含刪除確認 dialog、production upload/persistence/API 或正式 camera preview pixel parity。

## Affected Files
- `app/src/main/java/com/example/tp_ncolso_android/feature/photocamera/PhotoCameraContract.kt` — state, events, effects and callback payload。
- `app/src/main/java/com/example/tp_ncolso_android/feature/photocamera/PhotoCameraViewModel.kt` — permission/capture/error state transitions and duplicate suppression。
- `app/src/main/java/com/example/tp_ncolso_android/feature/photocamera/PhotoCameraScreen.kt` — CameraX preview binding, 4:3 capture, local file output and accessibility UI。
- `app/src/main/java/com/example/tp_ncolso_android/feature/surveyform/SurveyFormScreen.kt` — Figma-aligned camera/photo tiles and local thumbnail rendering。
- `app/src/debug/java/com/example/tp_ncolso_android/AppEntry.kt` — route overlay and captured-photo mapping to UI-006。
- `app/build.gradle.kts`, `gradle/libs.versions.toml`, `app/src/main/AndroidManifest.xml` — CameraX 1.4.1 and camera permission/feature declaration。
- `app/src/main/res/drawable-nodpi/ic_photo_camera.png`, `ic_photo_delete.png` — Figma exports converted to Android-supported PNG resources.
- `app/src/test/java/.../feature/photocamera/PhotoCameraViewModelTest.kt` — deterministic state/effect tests。
- `app/src/androidTest/java/.../feature/photocamera/PhotoCameraScreenTest.kt` — permission/capture/close semantics using pure screen state.
- `app/src/androidTest/java/.../feature/surveyform/SurveyFormScreenTest.kt` — retain and extend camera tile/four-photo semantics.
- `docs/tasks/UI-007-photo-camera/` — execution, verification and current state evidence。

## Implementation Steps
1. Add CameraX version catalog entries, runtime CAMERA permission and optional camera feature declaration.
2. Implement UI-007 immutable contract and ViewModel with permission, ready/capturing/error transitions and one-shot captured-photo effect.
3. Implement lifecycle-safe CameraX preview binding and 4:3 file capture into app-private cache, including orientation-tolerant aspect validation and failure recovery.
4. Replace UI-006's synthetic-looking photo area with fixed 168×126dp Figma tiles, Figma camera/delete assets, asynchronous local thumbnail loading and preserved `survey-camera` semantics.
5. Wire debug host camera open/close/captured callback while keeping UI-006 form state and four-photo validation as the owner.
6. Add unit/Compose tests, run targeted and regression checks, record developer evidence, review staged diff and commit only UI007 scope.

## Test Plan
- Unit: `PhotoCameraViewModelTest` for permission denial/retry, capture duplicate suppression, success effect, failure recovery and close state.
- Compose: `PhotoCameraScreenTest` for camera/permission/error semantics, 4:3 preview slot, disabled shutter while capturing and close action.
- Existing feature: `SurveyFormValidationTest`, `SurveyFormViewModelTest`, `SurveyFormScreenTest` for four-photo limit, delete/retake and preserved callback semantics.
- Build/static: debug unit tests, `compileDebugAndroidTestKotlin`, debug/release assemble, lint and `git diff --check`.
- Device: targeted UI007/SurveyForm connected tests on existing emulator; actual CameraX hardware capture is a smoke limitation if the emulator camera is unavailable.

## Regression Plan
- UI-006 field validation, dirty state, submit and duplicate-submit behavior remain unchanged.
- Existing UI-003/UI-004/UI-005 connected tests and release compilation remain green.
- The debug host still opens UI-006 from parcel detail and synthetic test callbacks remain deterministic.

## Risks
- CameraX binding may fail on an emulator without a camera; show recoverable error and keep form state.
- File decode must remain off the main thread; use local placeholder when a synthetic URI has no readable file.
- UI-008 delete confirmation is intentionally not added; current delete callback remains immediate and traceable.

## Rollback Plan
- Revert the focused UI007 commit, which removes CameraX dependencies, manifest/assets, feature package, debug wiring and photo tile changes without touching UI-006's prior verification artifact.

## Current Behavior
- UI-006 calls a callback and debug host fabricates a local URI; no camera surface or persisted local image exists.

## Expected Behavior
- Tapping the camera tile opens a lifecycle-safe CameraX surface; a successful 4:3 capture returns one app-private URI and timestamp to the same UI-006 ViewModel, while the photo grid displays the local image and retake path.

## Acceptance Criteria Traceability
| AC | Implementation Step | Validation |
|---|---|---|
| AC-001 | 2–5 | Camera route Compose semantics test and debug host smoke; no gallery dependency/entry in source. |
| AC-002 | 2–3 | ViewModel permission tests and denied-state Compose semantics. |
| AC-003 | 3, 5 | Local file/aspect helper test, capture callback mapping and device smoke limitation recorded. |
| AC-004 | 4–5 | SurveyFormScreenTest for 0/4/delete states and fixed tile semantics/dimensions. |
| AC-005 | 2–3, 5 | ViewModel duplicate suppression and capture callback integration tests. |
| AC-006 | 2–3 | Error/retry tests, lifecycle `DisposableEffect` review and connected smoke where camera is available. |
| AC-007 | 6 | Unit/Compose/build/lint/regression results recorded in execution and verification artifacts. |

## Failure Behavior
- Permission denied shows a retryable permission message and does not mutate UI-006 photos.
- Camera bind/capture/file/aspect failure returns an error state with retry/close affordances; the partial file is deleted when safe.
- A duplicate capture callback while `CAPTURING` is ignored; existing photos and form values remain intact.

## Security and Privacy
- Camera output is written only under app-private cache and passed as an in-process local URI; no network or durable server storage is added.
- No camera bytes, URI contents, credentials or tokens are logged. Permission is requested only when the user enters the camera route.

## Open Questions
- Production upload and replacement semantics remain with INT-003; no implementation decision is required for this development scope.
- Formal pixel parity of the camera preview remains unverified because the approved Figma source covers the form/photo tile states, not the preview surface.
