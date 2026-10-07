# Release

## Purpose

Determine release readiness and record the exact approved artifact, risks, authorization, deployment, and monitoring evidence.

## Entry

- `next_action: release`
- Verification PASS on reviewed committed revision
- required CI/UAT evidence complete

## Load

- state and Verification result
- exact revision/artifact identity
- only applicable release configuration, risks, and operational evidence

## Output

Record version/build variant, environment, signing status, checksum/immutable ID, release notes, limitations, migration/config/API/permission changes, rollout, rollback/recovery, owner, authorization, smoke checks, and monitoring results when applicable.

## Exit

- Ready but authorization required: `human_release`.
- Authorized deployment and required monitoring complete: `done`.
- Failed signal/gate: open issue and route by cause.

## Restrictions

Release records do not authorize merge, publish, deploy, production configuration, or signing use. Those require explicit human authorization. Never mark Done with pending/failed/`NOT VERIFIED` gates.
