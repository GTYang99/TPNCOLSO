# Knowledge Resolution

## Question and Scope
- Task question: 建立跨畫面品質與 Android 相容性 candidate baseline。
- In scope: layout, text scaling, IME, accessibility, rotation and state restoration regression。
- Out of scope: product behavior/API changes。
- Current requirement input: `requirement.md` (requester-approved clarification, 2026-10-08)。

## Candidate Knowledge Baseline
- Baseline ID: `KB-UI-009-UI-HARDENING-R1`
- Created date: 2026-09-08 (Asia/Taipei)
- Status: validated_candidate

## Material Claims and Traceability
| Claim ID | Statement | Sources | Status |
|---|---|---|---|
| KCL-UI009-001 | All UI must support Android 9+, safe areas, scalable text and IME without clipping/overlap. | SRC-UI009-001, 002 | resolved |
| KCL-UI009-002 | Icon actions need content descriptions and interactive targets at least 48dp. | SRC-UI009-001 | resolved |
| KCL-UI009-003 | UI-009 is a cross-screen regression task, not a new Figma screen. | SRC-UI009-002, 003 | resolved |
| KCL-UI009-004 | The approved numbered AC set is derived from parent `AC-017`, the design rules and existing task boundaries; it does not add product behavior. | `requirement.md`, SRC-UI009-001–003 | resolved |
| KCL-UI009-005 | UI-002–UI-008 source identity is reproducible through child node IDs or an authoritative repository alternative; formal Figma revision metadata remains unknown. | SRC-UI009-004–011 | resolved_with_limitation |

## Conflicts and Gaps
| Conflict ID | Description | Decision | Status |
|---|---|---|---|
| KCF-UI009-001 | No single Figma frame covers hardening. | Validate against each existing screen baseline and common rules. | resolved |
| KCF-UI009-002 | Auth visual authority is a requester-approved composite, while UI-003–UI-008 retain per-task Figma or component sources; UI-005 has no dedicated screen node. | Keep each child task's current authority and use the registered repository/component alternative where a dedicated node does not exist. Do not claim formal pixel parity. | resolved_with_limitation |
| KCF-UI009-003 | The roadmap names rotation and state restoration but does not define process-death restoration. | Approved AC-006 includes rotation/configuration change and process-death/recreation restoration. Planning must provide lifecycle evidence for both. | resolved |
| KCF-UI009-004 | AC-006 requires process-death restoration of local UI inputs and photo state, while the task NFR excludes new persistence and architecture prohibits sensitive saved state without an approved security design. | Requester explicitly authorizes Android transient saved state for UI-006 form data and UI-007 photo state after process death, without long-term storage. UI-002 password and captcha remain excluded and reset as their approved requirements specify. | resolved_with_constraint |

## Decision Records
### KD-UI009-001
- Decision: Use the existing UI-002–UI-008 Figma dimensions and design accessibility rules as cross-screen regression evidence; do not create a new product screen.
- Source / Conflict IDs: SRC-UI009-001–003; KCF-UI009-001
- Rationale: Hardening concerns runtime quality across all screens.
- Authority: requester-approved requirement clarification on 2026-10-08; the roadmap and design specification remain the cited domain sources.
- Revalidation trigger: Android target or common design rule changes.

### KD-UI009-002
- Decision: Use the source matrix in `knowledge-collection.md` as the UI-009 source boundary. UI-002 uses the requester-approved composite; UI-003, UI-004, UI-006, UI-007 and UI-008 use the registered Figma nodes/child frames; UI-005 uses the registered design/component evidence as its authoritative alternative.
- Source / Conflict IDs: SRC-UI009-004–011; KCF-UI009-002.
- Rationale: The source set is reproducible at task/screen/component level without inventing a Figma revision or reactivating superseded auth visual sources.
- Approval status: requester-approved on 2026-10-08; formal Figma revision approval is not claimed.
- Revalidation trigger: child source node, approved visual artifact, or ownership boundary changes.

### KD-UI009-003
- Decision: Approved AC-006 covers rotation/configuration change and process-death/recreation restoration.
- Source / Conflict IDs: `requirement.md`; KCF-UI009-003.
- Approval status: requester-approved on 2026-10-08.

### KD-UI009-004
- Decision: While the survey route is active, if the user does not explicitly confirm photo deletion, no current form data, selection, photo, or dirty-state value may be cleared. Cancel, system Back, outside dismissal, and any non-confirmation path preserve the complete dirty form as-is; only explicit `刪除` confirmation removes the selected photo.
- Source / Conflict IDs: requester decision 2026-10-08; UI-008 `AC-001`–`AC-004`; KCF-UI009-004.
- Scope limitation: This decision resolves active dialog and dirty-state mutation behavior only. It does not authorize storage or define which state may be restored after process death/recreation.
- Approval status: requester-approved on 2026-10-08.
- Revalidation trigger: delete/discard behavior or process-death restoration policy changes.

### KD-UI009-005
- Decision: Android transient saved state may restore UI-006 form data and UI-007 photo state after process death/recreation. Do not add long-term persistence. UI-002 password and captcha remain excluded and reset under their owning requirement.
- Source / Conflict IDs: requester authorization on 2026-10-08; KCF-UI009-004; UI-009 `AC-006`.
- Security boundary: use only system transient saved-state mechanisms needed by AC-006; do not write these values to a database, files, logs, analytics, or production API. Preserve photo references only as needed to restore the existing local photo state.
- Approval status: requester-approved on 2026-10-08.
- Revalidation trigger: any change from transient system saved state to durable persistence or any expansion to authentication credentials/captcha.

## Unresolved Items
- Device matrix and exact test coverage are to be finalized in Planning under the approved requirement.
- Formal Figma revision/approval remains unknown; this is non-blocking only while UI-009 does not claim formal pixel parity.

## Blocking Items
- None for Knowledge Validation or Planning handoff. The process-death restoration boundary is resolved by `KD-UI009-005`; Authentication password and captcha remain excluded.
- Formal Figma revision/approval remains a non-blocking limitation because pixel parity is out of scope.

## Validation Handoff
- Candidate baseline: `KB-UI-009-UI-HARDENING-R1`
- Required follow-up evidence: multi-device, accessibility and rotation/process-death regression evidence in Planning and Developer Validation.
- Material blockers: none.
- Ready for Validation: yes
