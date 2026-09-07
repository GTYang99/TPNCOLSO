# Issue Log

## REQ-001 — Token 效期規格衝突

- Category: `requirement_gap`
- Priority: `P1`
- Status: resolved
- Impact: 無法固定 Session 逾期、刷新、401 與登入驗證案例。
- Evidence: `1 系統概述與角色權限.md`、`5 API規格.md` 定義半小時且操作刷新；`3 系統行為規則.md` 定義永久。
- Expected: 只有一個由 Product 與 API/security owner 核定的 Token lifecycle。
- Actual: 兩個互斥定義。
- Resolution: Requester 於 2026-09-07 核定 Token 效期永久；相關產品與 API 規格已同步。
- Next action: `verification`
- Owner: Product + API/security owner

## REQ-002 — 登入失敗文案不一致

- Category: `requirement_gap`
- Priority: `P2`
- Status: resolved
- Impact: AC-002 無法固定精確錯誤文案與驗證碼錯誤分類。
- Evidence: 行為規格為「帳號或密碼錯誤」；Figma 為「帳號、密碼或驗證碼錯誤」。
- Expected: 產品與設計共用單一文案及錯誤條件。
- Actual: 文字規格與設計不同。
- Resolution: Requester 於 2026-09-07 核定「帳號、密碼或驗證碼錯誤」。
- Next action: `verification`
- Owner: Product + Design

## REQ-003 — APP 編輯角色與狀態矩陣不完整

- Category: `requirement_gap`
- Priority: `P1`
- Status: open
- Impact: 可能允許未授權修改或漏掉核定功能。
- Evidence: 功能表、角色矩陣、狀態規則、PUT survey 說明與 Figma「編輯提醒」未形成一致矩陣。
- Expected: 每個角色在五種狀態下的檢視、編輯、送出與鎖定規則一致。
- Actual: 僅退回修正與已查核限制較明確。
- Next action: `knowledge_resolution`
- Owner: Product + API/security owner

## REQ-004 — 退回通知缺少正式合約

- Category: `requirement_gap`
- Priority: `P1`
- Status: open
- Impact: 無法實作 Figma 的通知清單、已讀與錯誤／空狀態。
- Evidence: Figma node `3327:13251` 顯示退回通知清單；Sitemap、介面規格與 API 清單未定義對應能力。
- Expected: 正式定義通知來源、欄位、排序、分頁、已讀與 API failure contract，或明確移出範圍。
- Actual: 只有畫面證據。
- Next action: `knowledge_resolution`
- Owner: Product + API owner

## ENV-001 — Git metadata 不可用

- Category: `environment`
- Priority: `P1`
- Status: open
- Impact: 可分析現有 Compose starter，但無法建立 task branch、提交、CI 或 committed-revision Verification。
- Evidence: `/Users/a10362/Desktop/TP_NCOLSO` 無 `.git`；目前已有 Gradle 與 Android Compose source tree。
- Expected: 完整 Android Git checkout 與可用 build/test 設定。
- Actual: Android source tree 可用，但 Git metadata 缺失。
- Next action: `infrastructure`
- Owner: Infrastructure
