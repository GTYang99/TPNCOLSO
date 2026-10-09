#!/usr/bin/env bash
set -euo pipefail

APP_ID="com.example.tp_ncolso_android"
ACTIVITY="${APP_ID}/.Ui009CameraStateRestoreActivity"
DRAFT_MARKER="UI009_CAMERA_PROCESS_DRAFT"
PHOTO_MARKER="UI009_CAMERA_PROCESS_PHOTO"
WINDOW_DUMP="/sdcard/ui009-camera-process-death-window.xml"
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

package_dump="$("${ADB[@]}" shell dumpsys package "$APP_ID" | tr -d '\r')"
restore_permission=0
if [[ "$package_dump" != *"android.permission.CAMERA: granted=true"* ]]; then
  "${ADB[@]}" shell pm grant "$APP_ID" android.permission.CAMERA
  restore_permission=1
fi
cleanup() {
  if [[ "$restore_permission" == "1" ]]; then
    "${ADB[@]}" shell pm revoke "$APP_ID" android.permission.CAMERA >/dev/null 2>&1 || true
  fi
}
trap cleanup EXIT

display_size="$("${ADB[@]}" shell wm size | tr -d '\r' | tail -1 | sed 's/.*: //')"
read -r display_width display_height <<<"${display_size/x/ }"
swipe_x=$((display_width / 2))
swipe_start_y=$((display_height * 83 / 100))
swipe_end_y=$((display_height * 18 / 100))

"${ADB[@]}" shell rm -f "$WINDOW_DUMP"
"${ADB[@]}" shell am start --activity-multiple-task -n "$ACTIVITY" \
  --ez ui009.seed-state true \
  --es ui009.draft-marker "$DRAFT_MARKER"

wait_for_camera() {
  local attempt dump
  for attempt in 1 2 3 4 5 6 7 8 9 10; do
    "${ADB[@]}" shell uiautomator dump "$WINDOW_DUMP" >/dev/null 2>&1 || true
    dump="$("${ADB[@]}" shell cat "$WINDOW_DUMP" 2>/dev/null | tr -d '\r' || true)"
    if [[ "$dump" == *"拍攝現況照片"* && "$dump" == *"取消拍攝"* ]]; then
      return 0
    fi
    sleep 1
  done
  printf '相機畫面沒有顯示標題與取消拍攝動作。\n' >&2
  printf '%s\n' "$dump" >&2
  return 1
}

wait_for_survey_markers() {
  local attempt dump
  for attempt in 1 2 3 4 5 6; do
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
  printf '返回表單後未顯示預期的草稿、dirty 狀態與照片標記。\n' >&2
  printf '%s\n' "$dump" >&2
  return 1
}

tap_cancel_capture() {
  local dump coordinates tap_x tap_y
  "${ADB[@]}" shell uiautomator dump "$WINDOW_DUMP" >/dev/null
  dump="$("${ADB[@]}" shell cat "$WINDOW_DUMP" | tr -d '\r')"
  coordinates="$(python3 -c '
import re,sys,xml.etree.ElementTree as ET
root=ET.fromstring(sys.stdin.read())
for node in root.iter("node"):
    label=node.attrib.get("content-desc", "") + node.attrib.get("text", "")
    if "取消拍攝" not in label:
        continue
    match=re.fullmatch(r"\[(\d+),(\d+)\]\[(\d+),(\d+)\]",node.attrib.get("bounds", ""))
    if match:
        left,top,right,bottom=map(int,match.groups())
        print((left+right)//2,(top+bottom)//2)
        raise SystemExit(0)
raise SystemExit("找不到取消拍攝動作")
' <<<"$dump")"
  read -r tap_x tap_y <<<"$coordinates"
  "${ADB[@]}" shell input tap "$tap_x" "$tap_y"
}

wait_for_camera
"${ADB[@]}" shell input keyevent KEYCODE_HOME
sleep 2

recents="$("${ADB[@]}" shell dumpsys activity recents | tr -d '\r')"
if [[ "$recents" != *"$APP_ID"* ]]; then
  printf '系統沒有保留相機測試 Activity 的 task。\n' >&2
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
wait_for_camera
tap_cancel_capture
wait_for_survey_markers

printf 'PASS：API %s 在 app process 結束並從 Recents 恢復後仍保留相機路由；取消後表單草稿、dirty 狀態與照片標記仍可見。\n' "$api_level"
