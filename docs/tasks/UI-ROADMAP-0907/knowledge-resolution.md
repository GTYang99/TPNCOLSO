# Knowledge Resolution

## Baseline

- Baseline ID: `KB-UI-ROADMAP-0907-R1`
- Status: candidate

## Resolved Decisions

### KD-UIR-001 — UI-first boundary

- UI Tasks以可呈現、可互動、可做 Compose UI test 的畫面狀態為完成邊界。
- 需要伺服器資料的狀態以集中式 fake data source 提供。
- 不建立推測性的正式 DTO、Repository 或 endpoint implementation。

### KD-UIR-002 — Sequence dependency

- 先建立共用 design system，再建立 screen。
- 土地摘要依賴圖台 shell；土地詳情依賴摘要 navigation；調查表單依賴詳情；照片依賴表單；退回流程重用詳情與表單。
- 最後以獨立 hardening Task 做跨畫面 responsive、accessibility 與 state restoration 驗證。

### KD-UIR-003 — API separation

- API integration 分為 Auth、Map/Parcel、Survey/Photo、Return/Notification 四個後續 Tasks。
- Integration Task 只有在相應 schema、權限、錯誤與狀態契約可驗證時才能進 Planning。

## Known Gaps

- Token lifecycle、登入失敗精確文案、APP 編輯權限與通知契約仍未核定。
- Google Maps／CameraX dependencies 尚未加入；各自於 `UI-003`、`UI-007` Planning 階段確認。
- `docs/architecture/overview.md` 描述舊有完整應用結構，與目前 Compose starter source 不一致；Implementation Planning 應以實際 source tree 為準。

## Candidate Handoff

- 可用於 UI roadmap 與 UI-only Planning。
- 不可用於宣告 API integration ready。
