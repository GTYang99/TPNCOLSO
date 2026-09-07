# Repository Analysis

## Current Behavior

- APP 是單 Activity 的 Jetpack Compose starter，只顯示 `Hello Android!`。
- 已有 Material 3 theme 基礎，尚無 Navigation、正式畫面、Google Maps、CameraX、Repository 或 domain model。
- 現有 architecture 文件描述的完整舊模組與目前 source tree 不一致。

## Expected Behavior

- 先建立獨立於 API 的 UI 元件、畫面狀態與導覽，再以已核定契約替換 fake data source。

## Affected Modules

- 本 Task 僅更新 `docs/tasks/`。
- 後續 UI Tasks 預期影響 `app/src/main/java/.../ui/`、theme、navigation、feature packages 與 UI tests。

## Dependencies

- UI-001 是全部畫面的基礎。
- UI-003 是 UI-004 的容器；UI-004 是 UI-005 的入口。
- UI-005 是 UI-006 與 UI-008 的入口。
- UI-006 是 UI-007 與 UI-008 編輯狀態的基礎。

## Risks

- 過早將 fake model 當成 API DTO，造成後續大幅重構。
- 每個畫面自行建立重複元件或錯誤狀態。
- 地圖與相機 SDK 的權限／生命週期問題晚到最後才發現。

## Unknowns

- 正式 Navigation library、Google Maps Compose 版本、CameraX 版本與 DI 策略應由各 Task Planning 依 repository 當時狀態決定。
