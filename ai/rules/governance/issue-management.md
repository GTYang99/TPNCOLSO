# Issue Management

Read this policy when a blocker, regression, mismatch, or enhancement is detected.

## Record

Use `issue-log.md`; keep `state.yaml` for the main Task route. Record:

- stable ID, title, category, priority, status
- expected/actual behavior and affected AC
- reproducible evidence and impact
- owner, route, resolution, and verification

## Categories and Routes

| Category | Route |
|---|---|
| `requirement_gap` | Knowledge or Planning by cause |
| `planning_gap` | Planning |
| `implementation_regression` | Debug |
| `verification_failure` | classify root cause first |
| `environment` | Infrastructure |
| `unknown` | Investigation |
| `enhancement_request` | backlog/separate Task |

Priority: P0 data/security/core-flow risk; P1 major blocker; P2 local/edge degradation; P3 non-blocking improvement.

Resolve only with evidence; close only after the resolution is verified. Priority expresses impact; category determines route.
