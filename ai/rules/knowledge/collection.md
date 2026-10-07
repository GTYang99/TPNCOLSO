# Knowledge Collection

## Purpose

Find and register the sources needed to understand the Task. Collection does not decide which conflicting claim is correct.

## Entry

- `next_action: knowledge_collection`
- Task scope or request is identifiable.

## Load

- current request or `requirement.md`
- existing `knowledge-collection.md`, if current
- source locations needed for the Task

Do not preload downstream plans, all product documents, or unrelated repository areas.

## Output

Create/update `knowledge-collection.md` from `ai/templates/knowledge/collection.md`. For each material source record identity/location, domain/owner, version/date, authority, freshness, scope, access, and relevant claims.

Record missing sources, access limits, initial conflicts, and coverage. Link sources instead of copying large content.

## Procedure

1. Bound the question and affected acceptance criteria.
2. Search primary sources first.
3. Register only material sources.
4. Mark authority/freshness as unknown when unproven.
5. Record gaps and handoff questions for Resolution.

## Exit

- Coverage sufficient: mark Collection completed and route to `knowledge_resolution`.
- Missing/stale source: remain in Collection.
- Access/tool failure: route to `infrastructure`.
- Required product decision is absent: record issue and route to `requirement_clarification`.

## Restrictions

No production code changes, requirement invention, conflict resolution, or secret/sensitive payload storage.
