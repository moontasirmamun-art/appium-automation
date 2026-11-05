import subprocess
import time
import os
import xml.etree.ElementTree as ET
import hashlib
from datetime import datetime

OUTPUT_DIR = "observed_session"
os.makedirs(OUTPUT_DIR, exist_ok=True)
SUMMARY_FILE = os.path.join(OUTPUT_DIR, "session_summary.txt")

print(f"[{datetime.now().strftime('%H:%M:%S')}] Mobile Session Observer started.")
print(f"Monitoring connected emulator for screen transitions and user interactions...")
print(f"Saving artifacts to: {os.path.abspath(OUTPUT_DIR)}")

def run_adb(cmd):
    try:
        result = subprocess.run(["adb"] + cmd, capture_output=True, text=True, encoding="utf-8", errors="ignore")
        return result.stdout.strip()
    except Exception as e:
        return ""

def get_current_focus():
    output = run_adb(["shell", "dumpsys", "window"])
    for line in output.splitlines():
        if "mCurrentFocus" in line:
            return line.strip()
    return "UnknownFocus"

def capture_ui_dump():
    run_adb(["shell", "uiautomator", "dump", "/sdcard/live_dump.xml"])
    xml_content = run_adb(["shell", "cat", "/sdcard/live_dump.xml"])
    return xml_content

def capture_screenshot(filepath):
    try:
        with open(filepath, "wb") as f:
            subprocess.run(["adb", "exec-out", "screencap", "-p"], stdout=f)
    except Exception as e:
        pass

def parse_elements(xml_str):
    if not xml_str or not xml_str.startswith("<?xml"):
        return []
    try:
        root = ET.fromstring(xml_str)
    except Exception:
        return []

    elements = []
    for node in root.iter("node"):
        attrib = node.attrib
        res_id = attrib.get("resource-id", "")
        text = attrib.get("text", "")
        content_desc = attrib.get("content-desc", "")
        cls = attrib.get("class", "")
        clickable = attrib.get("clickable", "false") == "true"
        bounds = attrib.get("bounds", "")

        if text or res_id or content_desc:
            elements.append({
                "class": cls,
                "id": res_id,
                "text": text,
                "desc": content_desc,
                "clickable": clickable,
                "bounds": bounds
            })
    return elements

def get_screen_hash(elements, focus):
    content = focus + "|" + "|".join(f"{e['id']}:{e['text']}:{e['desc']}" for e in elements)
    return hashlib.md5(content.encode("utf-8")).hexdigest()

step_count = 0
last_hash = None

with open(SUMMARY_FILE, "w", encoding="utf-8") as f:
    f.write(f"=== Observed Mobile Session Log - Started {datetime.now().strftime('%Y-%m-%d %H:%M:%S')} ===\n\n")

try:
    while True:
        focus = get_current_focus()
        xml_str = capture_ui_dump()
        elements = parse_elements(xml_str)

        if not elements:
            time.sleep(1.2)
            continue

        screen_hash = get_screen_hash(elements, focus)

        if screen_hash != last_hash:
            last_hash = screen_hash
            step_count += 1
            timestamp = datetime.now().strftime("%H:%M:%S")

            img_path = os.path.join(OUTPUT_DIR, f"step_{step_count:02d}.png")
            xml_path = os.path.join(OUTPUT_DIR, f"step_{step_count:02d}.xml")

            capture_screenshot(img_path)
            with open(xml_path, "w", encoding="utf-8") as xf:
                xf.write(xml_str)

            # Log to console & summary file
            log_lines = []
            log_lines.append(f"--- [Step {step_count:02d}] {timestamp} ---")
            log_lines.append(f"Focus: {focus}")
            log_lines.append("Key Elements Detected:")

            for el in elements:
                # Filter noise
                if el["text"] or el["desc"] or (el["id"] and "net.therap.app" in el["id"]):
                    click_tag = "[Clickable] " if el["clickable"] else ""
                    desc_tag = f" desc='{el['desc']}'" if el["desc"] else ""
                    text_tag = f" text='{el['text']}'" if el["text"] else ""
                    id_tag = f" id='{el['id']}'" if el["id"] else ""
                    cls_tag = f" ({el['class'].split('.')[-1]})" if el['class'] else ""
                    log_lines.append(f"  * {click_tag}{text_tag}{desc_tag}{id_tag}{cls_tag} bounds={el['bounds']}")

            log_lines.append("")
            summary_block = "\n".join(log_lines) + "\n"

            print(summary_block, flush=True)
            with open(SUMMARY_FILE, "a", encoding="utf-8") as f:
                f.write(summary_block)
                f.flush()

        time.sleep(1.2)
except KeyboardInterrupt:
    print("\nObserver stopped.")
