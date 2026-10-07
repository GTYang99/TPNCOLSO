# Release

## Purpose

Determine release readiness and record the exact approved artifact, risks, authorization, deployment, and monitoring evidence.

## Entry

- `next_action: release`
- Verification PASS on reviewed committed revision
- completion scope is `release` and the release candidate is identified
- every deferred release obligation has been reactivated
- required hosted CI, integration, and UAT evidence is complete

## Load

- state and Verification result
- exact revision/artifact identity
- deferred-obligation owners, reactivation milestones, and completion evidence
- only applicable release configuration, risks, and operational evidence

## Output

Record version/build variant, environment, signing status, checksum/immutable ID, release notes, limitations, migration/config/API/permission changes, rollout, rollback/recovery, owner, authorization, smoke checks, and monitoring results when applicable.

## Exit

- Ready but authorization required: `human_release`.
- Authorized deployment and required monitoring complete: set `completion.status: release_complete` and `next_action: none`.
- Failed signal/gate: open issue and route by cause.

## Restrictions

Release records do not authorize merge, publish, deploy, production configuration, or signing use. Those require explicit human authorization. Never mark `release_complete` with pending, failed, deferred, or `NOT VERIFIED` release-scope gates.
