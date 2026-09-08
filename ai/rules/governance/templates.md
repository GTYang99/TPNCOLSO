# Template Policy

Read this policy only when creating or changing a Task artifact.

- Use the matching file under `ai/templates/`.
- Templates define the canonical minimum shape; Rules define phase behavior.
- Add sections only when an approved requirement or Rule needs evidence.
- Do not remove required sections or copy full Rule text into artifacts.
- `ai/templates/task/state.yaml` is the only state schema.
- Keep only current evidence in canonical Task filenames; move superseded versions to `history/<baseline-or-revision>/`.
- Examples and Tutorial never override templates, Rules, AGENTS, or approved requirements.
