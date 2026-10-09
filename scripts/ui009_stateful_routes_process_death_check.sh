#!/usr/bin/env bash
set -euo pipefail

APP_ID="com.example.tp_ncolso_android"
ACTIVITY="${APP_ID}/.Ui009StatefulRouteRestoreActivity"
WINDOW_DUMP="/sdcard/ui009-routes-process-death-window.xml"
DETAIL_DUMP="/sdcard/ui009-route-detail-restored.xml"
SEARCH_DUMP="/sdcard/ui009-route-search-restored.xml"
MAP_DUMP="/sdcard/ui009-route-map-restored.xml"
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

"${ADB[@]}" shell rm -f "$WINDOW_DUMP" "$DETAIL_DUMP" "$SEARCH_DUMP" "$MAP_DUMP"
"${ADB[@]}" shell am force-stop "$APP_ID"
"${ADB[@]}" shell am start -f 0x10008000 -n "$ACTIVITY" --ez ui009.seed-state true

dump_window() {
  "${ADB[@]}" shell uiautomator dump "$WINDOW_DUMP" >/dev/null 2>&1 || true
  "${ADB[@]}" shell cat "$WINDOW_DUMP" 2>/dev/null | tr -d '\r' || true
}

wait_for_detail() {
  local attempt dump
  for attempt in 1 2 3 4 5 6 7 8 9 10; do
    for _ in 1 2 3 4; do
      "${ADB[@]}" shell input swipe "$swipe_x" "$swipe_start_y" "$swipe_x" "$swipe_end_y" 350 >/dev/null
    done
    dump="$(dump_window)"
    if [[ "$dump" == *"土地詳情"* && "$dump" == *"民國 114 年 9 月 3 日"* ]] \
      && { [[ "$dump" == *"歷史期別（唯讀）"* ]] || [[ "$dump" == *"歷史檢視資料"* ]]; }; then
      return 0
    fi
    sleep 1
  done
  printf '詳情頁未顯示預期的期別選取與唯讀內容。\n' >&2
  printf '%s\n' "$dump" >&2
  return 1
}

wait_for_search() {
  local attempt dump
  for attempt in 1 2 3 4 5 6 7 8; do
    dump="$(dump_window)"
    if [[ "$dump" == *"土地查詢"* && "$dump" == *"TEST-KEY-001"* && "$dump" == *"土地摘要"* && "$dump" == *"關閉摘要"* ]]; then
      return 0
    fi
    sleep 1
  done
  printf '詳情返回後未恢復查詢與摘要內容。\n' >&2
  printf '%s\n' "$dump" >&2
  return 1
}

tap_label() {
  local label="$1" dump coordinates tap_x tap_y
  dump="$(dump_window)"
  coordinates="$(python3 -c '
import re,sys,xml.etree.ElementTree as ET
label=sys.argv[1]
root=ET.fromstring(sys.stdin.read())
for node in root.iter("node"):
    if label not in node.attrib.get("text", "") and label not in node.attrib.get("content-desc", ""):
        continue
    match=re.fullmatch(r"\[(\d+),(\d+)\]\[(\d+),(\d+)\]",node.attrib.get("bounds", ""))
    if match:
        left,top,right,bottom=map(int,match.groups())
        print((left+right)//2,(top+bottom)//2)
        raise SystemExit(0)
raise SystemExit("找不到可操作項目："+label)
' "$label" <<<"$dump")"
  read -r tap_x tap_y <<<"$coordinates"
  "${ADB[@]}" shell input tap "$tap_x" "$tap_y"
}

verify_basemap_selected() {
  local dump
  dump="$(dump_window)"
  python3 -c '
import sys,xml.etree.ElementTree as ET
root=ET.fromstring(sys.stdin.read())
for node in root.iter("node"):
    if node.attrib.get("content-desc")=="地圖區域，底圖 正射圖":
        map_visible=True
    if node.attrib.get("text")=="正射圖示意底圖":
        basemap_text=True
if locals().get("map_visible", False) and locals().get("basemap_text", False):
    raise SystemExit(0)
raise SystemExit("正射圖底圖未保持選取狀態")
' <<<"$dump"
}

wait_for_detail
"${ADB[@]}" shell cp "$WINDOW_DUMP" "$DETAIL_DUMP"
"${ADB[@]}" shell input keyevent KEYCODE_HOME
sleep 2

recents="$("${ADB[@]}" shell dumpsys activity recents | tr -d '\r')"
if [[ "$recents" != *"$APP_ID"* ]]; then
  printf '系統沒有保留地圖／查詢／詳情測試 task。\n' >&2
  exit 3
fi

"${ADB[@]}" shell am kill "$APP_ID"
sleep 2
"${ADB[@]}" shell input keyevent KEYCODE_APP_SWITCH
sleep 2
recents_window="$(dump_window)"
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

wait_for_detail
"${ADB[@]}" shell cp "$WINDOW_DUMP" "$DETAIL_DUMP"
tap_label "關閉詳情"
wait_for_search
"${ADB[@]}" shell cp "$WINDOW_DUMP" "$SEARCH_DUMP"
tap_label "關閉摘要"
tap_label "關閉查詢"
verify_basemap_selected
"${ADB[@]}" shell uiautomator dump "$WINDOW_DUMP" >/dev/null
"${ADB[@]}" shell cp "$WINDOW_DUMP" "$MAP_DUMP"

printf 'PASS：API %s 的詳情期別、查詢摘要與地圖底圖在 app process 結束／Recents 還原後皆可恢復並繼續操作。\n' "$api_level"
