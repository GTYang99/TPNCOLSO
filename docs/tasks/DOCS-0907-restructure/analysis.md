# Repository Analysis

## Current Behavior
- `ai/` 平鋪 architecture、14 個 rules、Tutorial、images 與 templates。
- 產品規格位於 root，雖然 AGENTS 宣告產品知識應在 `docs/product/`。
- `docs/` 已有 product/design/api/assets/tasks domain，但 template 與正式 artifact 混放。

## Expected Behavior
- `ai/` 僅承載 Agent workflow，規則與範本依職責分組。
- `docs/` 承載 architecture、product、design、api、assets 與 tasks。
- 根目錄只保留入口檔案與必要專案檔。

## Affected Modules
- `AGENTS.md`
- `ai/**`
- `docs/**`
- `docs/product/land-survey-115/`（原 root 產品規格）

## Dependencies
- 所有 Markdown/YAML 中的路徑引用。
- Tutorial 圖片相對路徑。
- 產品規格章節交叉引用。

## Risks
- 搬移造成失效引用或遺漏檔案。
- 歷史 Task 仍記錄舊路徑，需更新或明確視為歷史證據。
- 無 Git rename tracking 可輔助檢查。

## Unknowns
- Repository 外部是否有工具硬編碼舊路徑；以 migration map 降低風險。
