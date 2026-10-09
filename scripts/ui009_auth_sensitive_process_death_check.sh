#!/usr/bin/env bash
set -euo pipefail

APP_ID="com.example.tp_ncolso_android"
ACTIVITY="${APP_ID}/.MainActivity"
WINDOW_DUMP="/sdcard/ui009-auth-process-death.xml"
SCREENSHOT_PATH="/sdcard/ui009-auth-process-death.png"
EXPECTED_API="${UI009_EXPECTED_API:-34}"
PROJECT_ROOT="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")/.." && pwd)"
APK="${PROJECT_ROOT}/app/build/outputs/apk/debug/app-debug.apk"
TEST_ACCOUNT="UI009TEST"
TEST_PASSWORD="dummy123"
TEST_CAPTCHA="2468"

ADB=(adb)
if [[ -n "${UI009_ADB_SERIAL:-}" ]]; then
  ADB+=( -s "$UI009_ADB_SERIAL" )
fi

api_level="$("${ADB[@]}" shell getprop ro.build.version.sdk | tr -d '\r')"
if [[ "$api_level" != "$EXPECTED_API" ]]; then
  printf '預期 API %s，實際裝置為 API %s。\n' "$EXPECTED_API" "$api_level" >&2
  exit 2
fi
if [[ -z "$("${ADB[@]}" shell pm path "$APP_ID" 2>/dev/null | tr -d '\r')" ]]; then
  if [[ ! -f "$APK" ]]; then
    printf '找不到已建置的 debug APK：%s\n' "$APK" >&2
    exit 3
  fi
  "${ADB[@]}" install -r "$APK" >/dev/null
fi

dump_window() {
  "${ADB[@]}" shell uiautomator dump "$WINDOW_DUMP" >/dev/null 2>&1 || true
  "${ADB[@]}" shell cat "$WINDOW_DUMP" 2>/dev/null | tr -d '\r' || true
}

wait_for_login() {
  local attempt dump
  for attempt in 1 2 3 4 5 6 7 8; do
    dump="$(dump_window)"
    if [[ "$dump" == *"登入"* && "$dump" == *"帳號"* && "$dump" == *"密碼"* && "$dump" == *"驗證碼"* && "$dump" == *"$APP_ID"* ]]; then
      return 0
    fi
    sleep 1
  done
  printf '登入畫面未在預期時間內顯示。\n' >&2
  return 1
}

type_into_labeled_field() {
  local label="$1" value="$2" dump coordinates x y
  dump="$(dump_window)"
  coordinates="$(python3 -c '
import re,sys,xml.etree.ElementTree as ET
label=sys.argv[1]
root=ET.fromstring(sys.stdin.read())
pattern=re.compile(r"\[(\d+),(\d+)\]\[(\d+),(\d+)\]")
label_nodes=[]
inputs=[]
for node in root.iter("node"):
    match=pattern.fullmatch(node.attrib.get("bounds", ""))
    if match:
        left,top,right,bottom=map(int,match.groups())
        if node.attrib.get("text") == label:
            label_nodes.append((top,bottom))
        if node.attrib.get("class") == "android.widget.EditText":
            inputs.append((top,left,right,bottom))
if not label_nodes:
    raise SystemExit("找不到登入欄位標籤："+label)
label_top,label_bottom=min(label_nodes)
candidates=[item for item in inputs if item[0] >= label_top]
if not candidates:
    raise SystemExit("找不到標籤下方的登入輸入欄位："+label)
top,left,right,bottom=min(candidates,key=lambda item:item[0]-label_bottom)
print((left+right)//2,(top+bottom)//2)
' "$label" <<<"$dump")"
  read -r x y <<<"$coordinates"
  "${ADB[@]}" shell input tap "$x" "$y"
  sleep 1
  "${ADB[@]}" shell input text "$value"
  sleep 1
}

capture_screenshot() {
  local filename="$1" output_path
  "${ADB[@]}" shell screencap -p "$SCREENSHOT_PATH"
  if [[ -n "${UI009_EVIDENCE_DIR:-}" ]]; then
    mkdir -p "$UI009_EVIDENCE_DIR"
    output_path="${UI009_EVIDENCE_DIR}/${filename}"
  else
    output_path="/private/tmp/${filename}"
  fi
  "${ADB[@]}" pull "$SCREENSHOT_PATH" "$output_path" >/dev/null
}

tap_recents_task() {
  local display_size width height recents recents_window coordinates x y
  display_size="$("${ADB[@]}" shell wm size | tr -d '\r' | tail -1 | sed 's/.*: //')"
  read -r width height <<<"${display_size/x/ }"
  recents="$("${ADB[@]}" shell dumpsys activity recents | tr -d '\r')"
  if [[ "$recents" != *"$APP_ID"* ]]; then
    printf '系統沒有保留登入 task。\n' >&2
    return 4
  fi
  "${ADB[@]}" shell am kill "$APP_ID"
  sleep 2
  "${ADB[@]}" shell input keyevent KEYCODE_APP_SWITCH
  sleep 2
  "${ADB[@]}" shell uiautomator dump "$WINDOW_DUMP" >/dev/null 2>&1
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
    match=pattern.fullmatch(node.attrib.get("bounds",""))
    if not match:
        continue
    left,top,right,bottom=map(int,match.groups())
    if right-left > 0.55*width and bottom-top > 0.45*height:
        cards.append((left,top,right,bottom))
if not cards:
    raise SystemExit("找不到 Recents 前景 task 卡片")
left,top,right,bottom=max(cards,key=lambda b:(b[2]-b[0])*(b[3]-b[1]))
print((left+right)//2,(top+bottom)//2)
' "$width" "$height" <<<"$recents_window")"
  read -r x y <<<"$coordinates"
  "${ADB[@]}" shell input tap "$x" "$y"
  sleep 2
}

"${ADB[@]}" shell rm -f "$WINDOW_DUMP"
"${ADB[@]}" shell am force-stop "$APP_ID"
"${ADB[@]}" shell am start -n "$ACTIVITY"
wait_for_login
type_into_labeled_field "帳號" "$TEST_ACCOUNT"
type_into_labeled_field "密碼" "$TEST_PASSWORD"
"${ADB[@]}" shell input keyevent KEYCODE_BACK
sleep 1
type_into_labeled_field "驗證碼" "$TEST_CAPTCHA"
"${ADB[@]}" shell input keyevent KEYCODE_BACK
sleep 1
capture_screenshot "auth-sensitive-prefill-api${api_level}.png"
"${ADB[@]}" shell input keyevent KEYCODE_HOME
sleep 2
tap_recents_task
wait_for_login

capture_screenshot "auth-sensitive-process-death-restored-api${api_level}.png"
"${ADB[@]}" shell rm -f "$WINDOW_DUMP"
"${ADB[@]}" shell rm -f "$SCREENSHOT_PATH"

printf '完成：API %s 登入程序死亡與 Recents 流程；請視覺核對輸入前與恢復後截圖，再判定欄位重設結果。\n' "$api_level"
