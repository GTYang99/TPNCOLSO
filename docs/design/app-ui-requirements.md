# Design Specification

## Status

- Approved visual baseline — Requester 於 2026-09-07 核定目前 Figma Copy 為正式 UI；尚未解決的產品／API 問題仍由各 owning Task 追蹤，不降低 visual baseline authority。

## Related Product Requirement

- [UI-0907 APP UI requirement](../tasks/UI-0907-app-ui-requirements/requirement.md)
- [Knowledge Resolution](../tasks/UI-0907-app-ui-requirements/knowledge-resolution.md)

## Screens and Components

### Figma source index

| Flow | Figma source node | Primary screens / states |
|---|---|---|
| 登入、註冊、登出 | [2905:2680](https://www.figma.com/design/HRbRsw6HoNBUCtaieX8xUM/新工處土地占用調查圖台系統-行動板---Copy-?node-id=2905-2680) | 登入空白／已輸入／失敗、註冊空白／已輸入、首頁、側欄 |
| 底圖切換 | [4952:18205](https://www.figma.com/design/HRbRsw6HoNBUCtaieX8xUM/新工處土地占用調查圖台系統-行動板---Copy-?node-id=4952-18205) | 電子地圖、正射圖、地形圖 |
| 調查填報 | [2938:193](https://www.figma.com/design/HRbRsw6HoNBUCtaieX8xUM/新工處土地占用調查圖台系統-行動板---Copy-?node-id=2938-193) | 地塊、摘要 sheet、土地資料、現況檢視、新增、成功 |
| 修正退回資料 | [3327:13251](https://www.figma.com/design/HRbRsw6HoNBUCtaieX8xUM/新工處土地占用調查圖台系統-行動板---Copy-?node-id=3327-13251) | 通知清單、退回意見、編輯、確認修正、已修正 |
| 土地查詢 | [4952:14932](https://www.figma.com/design/HRbRsw6HoNBUCtaieX8xUM/新工處土地占用調查圖台系統-行動板---Copy-?node-id=4952-14932) | 已輸入、搜尋結果 sheet、查無資料 |
| 刪除提醒 | [3184:11543](https://www.figma.com/design/HRbRsw6HoNBUCtaieX8xUM/新工處土地占用調查圖台系統-行動板---Copy-?node-id=3184-11543) | 刪除前、Android 確認、刪除後 |
| 編輯提醒 | [3225:12771](https://www.figma.com/design/HRbRsw6HoNBUCtaieX8xUM/新工處土地占用調查圖台系統-行動板---Copy-?node-id=3225-12771) | 編輯中、Android 捨棄確認 |

### Information architecture

```text
登入 ─┬─ 註冊
      └─ 圖台首頁 ─┬─ 漢堡選單 ─ 登出
                   ├─ 底圖切換
                   ├─ 土地關鍵字查詢 ─ 土地摘要 Sheet
                   ├─ 點擊地塊 ─────── 土地摘要 Sheet
                   └─ 退回通知 ─────── 退回土地清單
                                          │
土地摘要 Sheet ─ 上滑／檢視 ─ 土地詳情 ─┬─ 土地資料（唯讀）
                                       └─ 實地勘查土地現況
                                           ├─ 未調查：新增與送出
                                           ├─ 退回：修正與確認
                                           └─ 歷史／鎖定：唯讀
```

### Screen inventory

| Screen ID | Screen / container | Entry | Required states | Exit / navigation | AC |
|---|---|---|---|---|---|
| `SCR-AUTH-01` | 登入 | 未登入啟動、登出、401 | empty, filled, submitting, field-error | 成功至 `SCR-MAP-01`；註冊至 `SCR-AUTH-02` | AC-001, AC-002, AC-016 |
| `SCR-AUTH-02` | 註冊 | 登入頁「註冊」 | empty, filled, validation-error, submitting | 取消／返回至登入；完成後依核定流程 | AC-003 |
| `SCR-MAP-01` | 圖台首頁 | 登入成功 | loading, content, map-error, permission-denied | 開啟側欄、查詢、通知、底圖、土地摘要 | AC-004, AC-005, AC-006 |
| `OVL-NAV-01` | 側欄 | 漢堡選單 | open | 圖台、儀表板、登出 | AC-004, AC-016 |
| `OVL-NOTIFY-01` | 退回通知清單 | 通知入口 | loading, content, empty, error | 點擊土地至 `SCR-PARCEL-01` | AC-012 |
| `SHEET-PARCEL-01` | 土地摘要 Sheet | 點擊地塊或查詢結果 | collapsed, expanded, loading, error | 關閉或進入詳情 | AC-006, AC-007 |
| `SCR-PARCEL-01` | 土地詳情 | 摘要或通知 | land-tab, survey-tab, readonly, editable, returned | 返回／關閉；新增或編輯 | AC-007, AC-008, AC-012 |
| `SCR-SURVEY-01` | 調查表單 | 未調查新增、退回修正、允許的既有編輯 | pristine, dirty, field-error, photo-error, submitting, success | 取消、完成、捨棄確認 | AC-008–AC-015 |
| `DLG-DISCARD-01` | 捨棄未儲存變更 | dirty 狀態返回／關閉 | Android modal | 繼續編輯或捨棄離開 | AC-014 |
| `DLG-DELETE-01` | 刪除照片確認 | 點擊縮圖刪除 | Android modal | 取消或刪除 | AC-015 |
| `DLG-RESUBMIT-01` | 確認完成修正 | 退回資料完成編輯 | Android modal | 取消或更新為已修正 | AC-013 |

### Component catalog

| Component ID | Component | Inputs / content | States / behavior | Reuse scope |
|---|---|---|---|---|
| `CMP-TEXT-FIELD` | 標籤文字欄位 | label, value, placeholder, required, helper/error | default, focused, disabled, error, readonly | 登入、註冊、查詢、調查 |
| `CMP-PASSWORD` | 密碼欄位 | value, visibility action | masked by default, error | 登入、註冊 |
| `CMP-CAPTCHA` | 驗證碼列 | 4 碼輸入、圖片、重整 | loading, ready, error | 登入 |
| `CMP-SELECT` | 單選下拉／可輸入選單 | label, options, custom value | closed, open, selected, disabled, error | 註冊、調查 |
| `CMP-RADIO-GROUP` | 單選群組 | options, selected | default, selected, disabled, error | 作業性質、占用型態 |
| `CMP-MULTI-SELECT` | 複選＋自行輸入 | options, values | `無占用` 與其餘值互斥 | 現場勘查土地情形 |
| `CMP-PRIMARY-BUTTON` | 主要膠囊按鈕 | label, action | enabled, pressed, loading, disabled | 登入、註冊、表單 |
| `CMP-SECONDARY-BUTTON` | 次要外框按鈕 | label, action | enabled, pressed, disabled | 取消、返回 |
| `CMP-ICON-BUTTON` | 圖示按鈕 | icon, accessibility label | enabled, pressed, disabled | 選單、搜尋、通知、返回、關閉、定位 |
| `CMP-STATUS-CHIP` | 狀態標籤 | survey_status | 未調查、外業調查完成、退回、已修正、已查核 | 摘要、詳情、通知 |
| `CMP-PARCEL-HEADER` | 土地固定摘要 | status, key_no, land_no, site_condition | loading, content, missing-value | Sheet、詳情 |
| `CMP-TABS` | 兩頁籤 | 土地資料、實地勘查土地現況 | selected, readonly/editable content | 詳情 |
| `CMP-BOTTOM-SHEET` | 土地摘要 Sheet | parcel header | collapsed, expanded, dismissible | 地圖 |
| `CMP-BASEMAP-SWITCHER` | 三底圖圓形選項 | EMAP, PHOTO2, 地形圖 | selected exactly one, loading, error | 圖台 |
| `CMP-PHOTO-TILE` | 照片縮圖 | image, captured_at, delete | loaded, loading, failed | 調查 |
| `CMP-CAMERA-TILE` | 拍攝入口 | camera action | enabled until 4 photos, permission-denied | 調查 |
| `CMP-ANDROID-DIALOG` | Android 確認對話框 | title, body, negative, positive | modal; destructive action uses destructive emphasis | 刪除、捨棄、修正 |
| `CMP-TOAST` | 短暫成功／失敗訊息 | message | visible, dismissed | 新增、編輯、修正、查無資料 |
| `CMP-RETURN-BANNER` | 退回意見區塊 | return_reason | visible only in returned workflow | 詳情、調查 |

### State and permission model

| Status | APP presentation | Investigator | Internal staff | Administrator |
|---|---|---|---|---|
| 未調查 | 顯示新增入口 | 可新增送出 | 唯讀 | 可新增送出 |
| 外業調查完成 | 顯示已完成狀態 | 是否可編輯待 `KCF-003` | 唯讀 | 依既有資料編輯規則，待與 API 核對 |
| 退回 | 顯示退回意見與修正入口 | 可修正並確認重送 | 唯讀 | 可依權限編輯 |
| 已修正 | 顯示已修正、不可由外業再編輯 | 唯讀 | 唯讀（APP） | 可依權限編輯 |
| 已查核 | 顯示已查核並鎖定 | 唯讀 | 唯讀 | 規格允許管理者編輯，但 APP 入口待確認 |

## Behavior

- 導覽：返回鍵回上一層；關閉鈕結束詳情／表單；編輯 dirty 時兩者皆先走 `DLG-DISCARD-01`。
- 地圖：登入後顯示所有土地資料的範圍；縮放 12–20；切換底圖不清除業務圖層或目前查詢。
- 查詢：輸入保留至使用者清除或完成新查詢；結果以地塊定位及摘要 Sheet 表達，404 不關閉目前容器。
- 詳情：檢視預設土地資料頁籤，編輯預設實地勘查頁籤；歷史期別為唯讀。頂部摘要在內容捲動時維持可辨識。
- 表單：只有可輸入欄位進入 enabled；系統帶入與清冊／圖資欄位皆 readonly。驗證錯誤就近顯示，第一次錯誤須可被鍵盤與無障礙焦點定位。
- 照片：未滿 4 張顯示拍攝入口，4 張時停止新增；刪除後立即恢復入口。上傳失敗保留其他照片與表單內容並可重試。
- 送出：loading 期間禁止重複觸發；成功後更新本機畫面狀態，失敗依 HTTP 類型處理且不得靜默丟失輸入。
- 通知：Figma 顯示退回清單與意見摘要，但取得、分頁、已讀與空狀態尚無 API 合約，實作前須處理 `KCF-004`。

## Responsive and Accessibility Rules

- Figma 402×874 僅為基準畫布；內容以可捲動容器、系統安全區域與 IME inset 適配 Android 9+ 直向裝置。
- 以 Noto Sans TC 為觀察到的設計字型；一般內文 16sp／24sp 行高、主要按鈕 20sp／28sp 行高是設計參考，實作須使用可縮放文字單位。
- 所有僅圖示操作需提供繁體中文 content description；裝飾性圖片不重複朗讀。
- 狀態、錯誤、選取與破壞性操作不得只靠顏色表達；對話框焦點進入標題，關閉後返回觸發元件。
- 主要操作觸控區不小於 48dp；Figma 中 14px radio／checkbox 必須由較大的可點區包覆。
- 鍵盤出現時目前欄位及主要操作可捲入可視範圍；字級放大時按鈕文字不得被裁切。

## Fixed Decisions

- Android 實作使用 Figma 的 Android 提醒樣式；iOS 提醒畫面不納入 Android 像素比對。
- 主要品牌色觀察值為 `#0D23AC`，主要文字為 `#303133`，次要文字為 `#606266`，邊框為 `#DCDFE6`；正式 token 名稱須在實作計畫中對應現有 Android Theme，不直接散落 raw color。
- 參考視覺採 Noto Sans TC；主要按鈕及底圖選擇使用膠囊／圓形語彙，卡片與浮動操作帶輕量陰影。
- 既有產品固定規則：三種底圖且電子地圖預設、兩個土地詳情頁籤、正好四張 4:3 現場照片、五種狀態只由指定動作流轉。
- Figma「地形圖（圖片亂放的）」明示為暫放素材，不能視為可交付的實際地形圖內容；實作來源仍依介面規格的臺北市歷史圖資 WMTS。
- 認證決策（2026-09-07 requester）：Token 效期永久；登入失敗文案為「帳號、密碼或驗證碼錯誤」；註冊成功返回登入頁；帳號限英數且最多 30 字元。
- Figma Copy `HRbRsw6HoNBUCtaieX8xUM`、入口 node `2905:2680` 及本文件 source index 所列節點，已由 requester 於 2026-09-07 核定為正式 UI，可作為實作與視覺驗收來源。核定綁定當日已取得的設計內容；後續 Figma 變更須重新確認 freshness 與影響範圍。

## Open Questions

- `KCF-003`：APP 中外業調查完成及已查核資料的可編輯角色／入口。
- `KCF-004`：退回通知清單、已讀、分頁、錯誤與 API 合約。
