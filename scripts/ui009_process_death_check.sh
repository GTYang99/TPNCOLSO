#!/usr/bin/env bash
set -euo pipefail

APP_ID="com.example.tp_ncolso_android"
ACTIVITY="${APP_ID}/.Ui009StateRestoreActivity"
DRAFT_MARKER="UI009_PROCESS_DEATH_DRAFT"
PHOTO_MARKER="UI009_PROCESS_DEATH_PHOTO"
WINDOW_DUMP="/sdcard/ui009-process-death-window.xml"
DISCARD_DIALOG_DUMP="/sdcard/ui009-discard-dialog-process-death.xml"
EXPECTED_API="${UI009_EXPECTED_API:-34}"

ADB=(adb)
if [[ -n "${UI009_ADB_SERIAL:-}" ]]; then
  ADB+=( -s "$UI009_ADB_SERIAL" )
fi

api_level="$("${ADB[@]}" shell getprop ro.build.version.sdk | tr -d '\r')"
if [[ "$api_level" != "$EXPECTED_API" ]]; then
  printf '預期 API %s，實際連線裝置為 API %s。\n' "$EXPECTED_API" "$api_level" >&2
  exit 2
fi
display_size="$("${ADB[@]}" shell wm size | tr -d '\r' | tail -1 | sed 's/.*: //')"
read -r display_width display_height <<<"${display_size/x/ }"
swipe_x=$((display_width / 2))
swipe_start_y=$((display_height * 83 / 100))
swipe_end_y=$((display_height * 18 / 100))

"${ADB[@]}" shell rm -f "$WINDOW_DUMP"
"${ADB[@]}" shell am start --activity-multiple-task -n "$ACTIVITY" \
  --ez ui009.seed-state true \
  --es ui009.draft-marker "$DRAFT_MARKER"
sleep 2

wait_for_markers() {
  local attempt dump
  for attempt in 1 2 3 4 5; do
    for _ in 1 2 3 4 5; do
      "${ADB[@]}" shell input swipe "$swipe_x" "$swipe_start_y" "$swipe_x" "$swipe_end_y" 350 >/dev/null
    done
    "${ADB[@]}" shell uiautomator dump "$WINDOW_DUMP" >/dev/null 2>&1 || true
    dump="$("${ADB[@]}" shell cat "$WINDOW_DUMP" 2>/dev/null | tr -d '\r' || true)"
    if [[ "$dump" == *"調查填報"* && "$dump" == *"$DRAFT_MARKER"* && "$dump" == *"$PHOTO_MARKER"* && "$dump" == *"有未儲存變更"* ]]; then
      return 0
    fi
    sleep 1
  done
  printf '畫面沒有顯示預期的路由、草稿、dirty 狀態與照片標記。\n' >&2
  printf '%s\n' "$dump" >&2
  return 1
}

wait_for_markers

tap_text() {
  local label="$1" dump coordinates x y
  "${ADB[@]}" shell uiautomator dump "$WINDOW_DUMP" >/dev/null 2>&1 || true
  dump="$("${ADB[@]}" shell cat "$WINDOW_DUMP" 2>/dev/null | tr -d '\r' || true)"
  coordinates="$(python3 -c '
import sys,xml.etree.ElementTree as ET
label=sys.argv[1]
root=ET.fromstring(sys.stdin.read())
for node in root.iter("node"):
    if node.attrib.get("text") == label or node.attrib.get("content-desc") == label:
        import re
        match=re.fullmatch(r"\[(\d+),(\d+)\]\[(\d+),(\d+)\]",node.attrib.get("bounds", ""))
        if match:
            left,top,right,bottom=map(int,match.groups())
            print((left+right)//2,(top+bottom)//2)
            raise SystemExit(0)
raise SystemExit("找不到可點選文字："+label)
' "$label" <<<"$dump")"
  read -r x y <<<"$coordinates"
  "${ADB[@]}" shell input tap "$x" "$y"
}

wait_for_discard_dialog() {
  local attempt dump
  for attempt in 1 2 3 4 5; do
    "${ADB[@]}" shell uiautomator dump "$WINDOW_DUMP" >/dev/null 2>&1 || true
    dump="$("${ADB[@]}" shell cat "$WINDOW_DUMP" 2>/dev/null | tr -d '\r' || true)"
    if [[ "$dump" == *"是否捨棄未儲存的內容？"* && "$dump" == *"如果現在離開，剛才編輯的內容將會遺失。"* && "$dump" == *"繼續編輯"* && "$dump" == *"捨棄編輯"* ]]; then
      return 0
    fi
    sleep 1
  done
  printf '未顯示預期的捨棄編輯對話框及兩個操作。\n' >&2
  printf '%s\n' "$dump" >&2
  return 1
}

wait_for_delete_dialog() {
  local attempt dump
  for attempt in 1 2 3 4 5; do
    "${ADB[@]}" shell uiautomator dump "$WINDOW_DUMP" >/dev/null 2>&1 || true
    dump="$("${ADB[@]}" shell cat "$WINDOW_DUMP" 2>/dev/null | tr -d '\r' || true)"
    if [[ "$dump" == *"刪除確認"* && "$dump" == *"刪除後無法恢復。"* && "$dump" == *"取消"* && "$dump" == *"刪除"* ]]; then
      return 0
    fi
    sleep 1
  done
  printf '未顯示預期的刪除照片對話框及兩個操作。\n' >&2
  printf '%s\n' "$dump" >&2
  return 1
}

tap_text "關閉填報"
wait_for_discard_dialog
"${ADB[@]}" shell input keyevent KEYCODE_HOME
sleep 2

recents="$("${ADB[@]}" shell dumpsys activity recents | tr -d '\r')"
if [[ "$recents" != *"$APP_ID"* ]]; then
  printf '系統沒有保留應用 task，無法進行程序死亡檢查。\n' >&2
  exit 3
fi

"${ADB[@]}" shell am kill "$APP_ID"
sleep 2
"${ADB[@]}" shell input keyevent KEYCODE_APP_SWITCH
sleep 2
"${ADB[@]}" shell uiautomator dump "$WINDOW_DUMP" >/dev/null
recents_window="$("${ADB[@]}" shell cat "$WINDOW_DUMP" | tr -d '\r')"
coordinates="$(python3 -c '
import re,sys,xml.etree.ElementTree as ET
width,height=map(int,sys.argv[1:3])
root=ET.fromstring(sys.stdin.read())
pattern=re.compile(r"\[(\d+),(\d+)\]\[(\d+),(\d+)\]")
cards=[]
for node in root.iter("node"):
    if node.attrib.get("clickable") != "true":
        continue
    match=pattern.fullmatch(node.attrib.get("bounds", ""))
    if not match:
        continue
    left,top,right,bottom=map(int,match.groups())
    if right-left > 0.55*width and bottom-top > 0.45*height:
        cards.append((left,top,right,bottom))
if not cards:
    raise SystemExit("找不到 Recents 前景 task 卡片")
left,top,right,bottom=max(cards,key=lambda b:(b[2]-b[0])*(b[3]-b[1]))
print((left+right)//2,(top+bottom)//2)
' "$display_width" "$display_height" <<<"$recents_window")"
read -r tap_x tap_y <<<"$coordinates"
"${ADB[@]}" shell input tap "$tap_x" "$tap_y"
sleep 2
wait_for_discard_dialog
"${ADB[@]}" shell uiautomator dump "$DISCARD_DIALOG_DUMP" >/dev/null
if [[ -n "${UI009_EVIDENCE_DIR:-}" ]]; then
  mkdir -p "$UI009_EVIDENCE_DIR"
  "${ADB[@]}" pull "$DISCARD_DIALOG_DUMP" "$UI009_EVIDENCE_DIR/discard-dialog-process-death-api${api_level}.xml" >/dev/null
fi
tap_text "繼續編輯"
sleep 1
wait_for_markers
tap_text "關閉填報"
wait_for_discard_dialog
tap_text "捨棄編輯"
sleep 2
activity_state="$("${ADB[@]}" shell dumpsys activity activities | tr -d '\r')"
if [[ "$activity_state" == *"Ui009StateRestoreActivity"* ]]; then
  printf '選擇捨棄編輯後，UI009 表單 Activity 仍存在。\n' >&2
  printf '%s\n' "$activity_state" | rg -n -C 1 'Ui009StateRestoreActivity' >&2 || true
  exit 4
fi
"${ADB[@]}" shell uiautomator dump "$WINDOW_DUMP" >/dev/null 2>&1 || true
final_window="$("${ADB[@]}" shell cat "$WINDOW_DUMP" 2>/dev/null | tr -d '\r' || true)"
if [[ "$final_window" == *"調查填報"* || "$final_window" == *"是否捨棄未儲存的內容？"* ]]; then
  printf '選擇捨棄編輯後，填報畫面或對話框仍顯示。\n' >&2
  printf '%s\n' "$final_window" >&2
  exit 5
fi
if [[ -n "${UI009_EVIDENCE_DIR:-}" ]]; then
  "${ADB[@]}" pull "$WINDOW_DUMP" "$UI009_EVIDENCE_DIR/discard-dialog-discard-result-api${api_level}.xml" >/dev/null
fi

DELETE_DIALOG_DUMP="/sdcard/ui009-delete-photo-dialog-process-death.xml"
"${ADB[@]}" shell am start --activity-multiple-task -n "$ACTIVITY" \
  --ez ui009.seed-state true \
  --es ui009.draft-marker "$DRAFT_MARKER"
sleep 2
wait_for_markers
tap_text "刪除照片 ui009-process-marker"
wait_for_delete_dialog
"${ADB[@]}" shell input keyevent KEYCODE_HOME
sleep 2
recents="$(${ADB[@]} shell dumpsys activity recents | tr -d '\r')"
if [[ "$recents" != *"$APP_ID"* ]]; then
  printf '系統沒有保留照片刪除對話框所在的 task。\n' >&2
  exit 6
fi
"${ADB[@]}" shell am kill "$APP_ID"
sleep 2
"${ADB[@]}" shell input keyevent KEYCODE_APP_SWITCH
sleep 2
"${ADB[@]}" shell uiautomator dump "$WINDOW_DUMP" >/dev/null
recents_window="$(${ADB[@]} shell cat "$WINDOW_DUMP" | tr -d '\r')"
coordinates="$(python3 -c '
import re,sys,xml.etree.ElementTree as ET
width,height=map(int,sys.argv[1:3])
root=ET.fromstring(sys.stdin.read())
pattern=re.compile(r"\[(\d+),(\d+)\]\[(\d+),(\d+)\]")
cards=[]
for node in root.iter("node"):
    if node.attrib.get("clickable") != "true":
        continue
    match=pattern.fullmatch(node.attrib.get("bounds", ""))
    if not match:
        continue
    left,top,right,bottom=map(int,match.groups())
    if right-left > 0.55*width and bottom-top > 0.45*height:
        cards.append((left,top,right,bottom))
if not cards:
    raise SystemExit("找不到 Recents 前景 task 卡片")
left,top,right,bottom=max(cards,key=lambda b:(b[2]-b[0])*(b[3]-b[1]))
print((left+right)//2,(top+bottom)//2)
' "$display_width" "$display_height" <<<"$recents_window")"
read -r tap_x tap_y <<<"$coordinates"
"${ADB[@]}" shell input tap "$tap_x" "$tap_y"
sleep 2
wait_for_delete_dialog
"${ADB[@]}" shell uiautomator dump "$DELETE_DIALOG_DUMP" >/dev/null
if [[ -n "${UI009_EVIDENCE_DIR:-}" ]]; then
  "${ADB[@]}" pull "$DELETE_DIALOG_DUMP" "$UI009_EVIDENCE_DIR/delete-photo-dialog-process-death-api${api_level}.xml" >/dev/null
fi
tap_text "取消"
sleep 1
wait_for_markers
tap_text "刪除照片 ui009-process-marker"
wait_for_delete_dialog
tap_text "刪除"
sleep 1
"${ADB[@]}" shell uiautomator dump "$WINDOW_DUMP" >/dev/null 2>&1 || true
delete_result="$(${ADB[@]} shell cat "$WINDOW_DUMP" 2>/dev/null | tr -d '\r' || true)"
if [[ "$delete_result" != *"調查填報"* || "$delete_result" != *"$DRAFT_MARKER"* || "$delete_result" != *"有未儲存變更"* || "$delete_result" == *"$PHOTO_MARKER"* || "$delete_result" == *"刪除照片 ui009-process-marker"* ]]; then
  printf '確認刪除照片後，表單／草稿狀態不符，或照片標記仍存在。\n' >&2
  printf '%s\n' "$delete_result" >&2
  exit 7
fi
if [[ -n "${UI009_EVIDENCE_DIR:-}" ]]; then
  "${ADB[@]}" pull "$WINDOW_DUMP" "$UI009_EVIDENCE_DIR/delete-photo-confirm-result-api${api_level}.xml" >/dev/null
fi

printf 'PASS：API %s 的表單捨棄與照片刪除對話框皆可在程序恢復後保留；取消保留照片，確認刪除只移除照片並保留 dirty 表單。\n' "$api_level"
