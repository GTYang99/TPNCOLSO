# Knowledge Resolution

## Question and Scope
- Task question: 建立 `UI-005-parcel-detail` 土地詳情與雙頁籤 candidate baseline。
- In scope: 固定摘要、土地資料 tab、實地勘查土地現況 tab、唯讀格式、歷史期別檢視與 UI-006 handoff。
- Out of scope: 搜尋、調查表單編輯行為、照片、通知、production API、WMTS。

## Candidate Knowledge Baseline
- Baseline ID: `KB-UI-005-PARCEL-DETAIL-R1`
- Created date: 2026-09-08 (Asia/Taipei)
- Supersedes: None
- Status: draft

## Material Claims and Traceability
| Claim ID | Statement | Supporting Sources | Contradicting Sources | Authority / Confidence | Requirement / AC | Status |
|---|---|---|---|---|---|---|
| KCL-UI005-001 | 土地詳情由固定摘要與兩個頁籤組成：土地資料、實地勘查土地現況。 | SRC-UI005-002, 003, 004 | None | Product + Design / high | FR-006, AC-007 | resolved |
| KCL-UI005-002 | 土地資料 tab 包含土地標示 8 欄與套匯圖資 2 欄，全部唯讀。 | SRC-UI005-003, 004 | None | Product / high | FR-007, AC-007 | resolved |
| KCL-UI005-003 | 實地勘查土地現況 tab 預設顯示最新一期；歷史期別可檢視但為唯讀。 | SRC-UI005-002, 004 | None | Product / high | FR-006, AC-007 | resolved |
| KCL-UI005-004 | UI-005 顯示詳情並提供進入 UI-006 的入口；表單編輯、送出與 dirty state 不在 UI-005。 | SRC-UI005-005, 006; requester decision 2026-09-08 | None | Process + Product / high | UI-005/UI-006 boundary | resolved |
| KCL-UI005-005 | UI-005 使用 fake parcel detail/history data；production API contract later補充。 | SRC-UI005-006; requester decision 2026-09-08 | None | Process / high | Integration deferral | resolved |
| KCL-UI005-006 | 共用欄位編輯、驗證、照片與送出功能放在 UI-006；UI-005 額外顯示退回原因分類 Tag 與退回原因說明。 | Requester decision 2026-09-08 | None | Product / high | UI-005/UI-006 boundary | resolved |

## Conflicts and Gaps
| Conflict ID | Claims / Sources | Domain | Impact / Severity | Options | Decision Owner | Status |
|---|---|---|---|---|---|---|
| KCF-UI005-001 | `tp_req_ui.md` 沒有獨立土地詳情連結，但 Figma MCP 已找到對應元件與尺寸。 | Design evidence | P2; source index link remains absent | Use MCP-confirmed component hierarchy and dimensions for visual baseline | Requester / Design | resolved 2026-09-08 |
| KCF-UI005-002 | 詳情第二 tab 可編輯入口與 UI-006 表單行為的 ownership boundary。 | Task boundary | P1 if duplicated | UI-005 owns view/tab/entry and return-reason display; UI-006 owns shared edit/validation/photo/submit functions | Product / Planning | resolved by requester decision 2026-09-08 |
| KCF-UI005-003 | 正式 API 接口與參數尚未提供。 | API contract | P2 for integration only | Keep fake detail/history data and defer API interface and parameters | Requester / Integration | resolved as deferred scope 2026-09-08 |

## Decision Records
### KD-UI005-001
- Decision: Establish UI-005 as the read/detail container for fixed parcel summary and two tabs. The land-data tab is read-only; the survey tab shows latest data and historical read-only data, with edit entry handed to UI-006.
- Alternatives: Put all detail and form behavior in UI-005; wait for API; infer a dedicated detail screen from unrelated Figma frames.
- Source / Conflict IDs: SRC-UI005-002, 003, 004, 006; KCF-UI005-001, 002
- Rationale: Matches the product data specification and roadmap task split while keeping the UI independently verifiable.
- Approver and date: Requester scope decision and product specifications, 2026-09-08.
- Affected requirements / AC / artifacts: UI-005 requirement, UI-006 handoff, fake detail/history fixtures.
- Revalidation trigger: Dedicated Figma detail node or API contract changes.

### KD-UI005-002
- Decision: Use the Figma MCP parcel-detail components as the visual and hierarchy baseline: `土地資料tab` `402 x 1004` with inner content `354 x 560`; `實地勘查土地現況tab` `402 x 1412` with inner content `354 x 964`. Include the discovered 未調查 and 編輯 component variants as state evidence.
- Source / Conflict IDs: Figma MCP inspection 2026-09-08; KCF-UI005-001
- Rationale: Structured Figma inspection provides direct component dimensions and parent-child hierarchy even though `tp_req_ui.md` does not link a dedicated detail section.
- Approver and date: Requester instruction to use Figma MCP, 2026-09-08.
- Revalidation trigger: Figma component or variant changes.

### KD-UI005-003
- Decision: Place the shared field editing, validation, photo and submission functions in `UI-006-survey-form`. Keep `UI-005` responsible for detail display and add return-reason category Tag plus return-reason explanation.
- Alternatives: Duplicate shared behavior in UI-005; move all detail display into UI-006; wait for API before defining the boundary.
- Source / Conflict IDs: Requester decision 2026-09-08; KCF-UI005-002, KCF-UI005-003
- Rationale: Keeps the detail screen focused while giving the survey flow one owner for shared editing behavior; API remains replaceable.
- Approver and date: Requester, 2026-09-08.
- Affected requirements / AC / artifacts: UI-005 return display, UI-006 shared form components, future integration handoff.
- Revalidation trigger: API contract or task ownership changes.

## Assumptions
| Assumption ID | Statement / Evidence | Confidence | Impact if Wrong | Owner | Validation Method | Expiry / Trigger | Safe for Planning |
|---|---|---|---|---|---|---|---|
| KA-UI005-001 | Common detail layout can be used until a dedicated Figma node is supplied. | medium | Pixel layout may need revision. | Design / Planning | Confirm Figma node before visual validation. | Dedicated node supplied | yes |

## Resolved Items
- UI-005 owns fixed summary, two tabs, read-only formatting and history view.
- UI-006 owns survey editing, validation, dirty state and submission.
- UI-006 owns shared field editing, validation, photo and submission functions; UI-005 displays return-reason category Tag and explanation.
- UI uses fake detail/history data; API remains deferred.

## Unresolved Non-blocking Items
- No remaining Figma dimension gap for the identified parcel-detail components; screen composition outside these components may require later visual review.
- Production API interface, parameters, history payload and error mapping remain deferred by decision.

## Blocking Items
- None for Knowledge Validation.
- UI-003/UI-004 prerequisite handoffs block implementation sequencing.

## Downstream Impact
- Requirements / AC: Specify fixed summary, two tabs, ten read-only land-data fields, latest/history view, return-reason Tag/explanation and UI-006 entry.
- Plan / design: Use common template pending dedicated Figma evidence.
- Code / data / API: Fake detail/history source only; no production DTO or endpoint; shared edit functions live in UI-006.
- Tests / verification: Cover tab selection, read-only fields, latest/history state and handoff callback.
- Release / operations: No integration or release claim from this UI task.

## Revalidation Triggers
- Dedicated Figma node, product field/display rules, UI-006 ownership or INT-002 API contract changes.

## Validation Handoff
- Candidate baseline: `KB-UI-005-PARCEL-DETAIL-R1`
- Decisions and constraints Validation must check: KD-UI005-001, read-only field scope, UI-006 boundary and deferred API.
- Safe assumptions: KA-UI005-001.
- Required follow-up evidence: dedicated Figma detail node or approved common-template visual reference.
- Material blockers: no.
- Ready for Validation: yes
