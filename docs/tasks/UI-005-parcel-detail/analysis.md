# Repository Analysis

## Current Behavior
- UI-004 的 fake parcel search 可在 map 上顯示 bottom summary，但 summary 目前沒有進入 detail 的 UI-005 callback。
- Map shell、parcel search、theme 與 read-only field/status primitives 已存在；沒有 detail/history state、data source 或 detail screen。
- UI-005 task 只有 Knowledge artifacts，沒有 requirement、plan、tests 或 implementation evidence。

## Expected Behavior
- Parcel summary 提供「查看詳情」入口；debug signed-in host 保持 map shell mounted 並顯示 UI-005 overlay。
- UI-005 ViewModel 載入 fake detail，管理 land/survey tab、latest/history selection 與 UI-006 handoff effect。
- 詳情畫面以固定摘要＋可捲動內容呈現土地資料和調查現況，所有 UI-005 欄位唯讀；編輯由 callback 交給 UI-006。

## Affected Modules
- `feature/parcelsearch/ParcelSearchContract.kt` / `ParcelSearchScreen.kt` — summary handoff callback 與詳情入口。
- `feature/parceldetail/` — detail contract, ViewModel, fake data source and Compose screen.
- `debug/AppEntry.kt` — keep map/search host and open/close detail overlay; bridge UI-006 entry callback for test evidence.
- `feature/parcelsearch` and `feature/parceldetail` unit/host tests — detail state, read-only semantics, history and handoff coverage.
- `docs/tasks/UI-005-parcel-detail/` — requirement, plan, review and execution evidence.

## Dependencies
- Active baseline `KB-UI-005-PARCEL-DETAIL-R1` and its decisions KD-UI005-001 through KD-UI005-003.
- Existing `ParcelSearchRecord`, `ParcelSearchViewModel`, `MapShellRoute`, `AppReadOnlyField`, `AppStatusBadge` and `AppSelectField`.
- UI-004 completed handoff in current `main` history; UI-006 remains a callback owner only.

## Risks
- Adding a detail button to the UI-004 summary changes its host contract; keep it as a narrow optional callback and preserve existing tests/behavior.
- Ten survey fields include the photo field, but UI-005 must show only a read-only summary and not invent camera/photo behavior.
- A common-template layout may diverge from a future approved Figma detail frame; visual claim remains observation-only.
- The debug host is the only signed-in host in this development scope; release integration remains deferred.

## Unknowns
- Formal Figma revision approval and production detail/history API contract are not available and remain outside this task.
