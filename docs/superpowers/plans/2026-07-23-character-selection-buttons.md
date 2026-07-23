# Character Selection Buttons Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Generate 6 high-definition arcade fighting game portrait buttons for character selection (`play1.gif` to `play6.gif`) and verify their rendering in the Java Swing interface.

**Architecture:** AI image generation via `generate_image`, Python Pillow image processing script for exact cropping (110x244 px) and format conversion, and Java Swing build verification.

**Tech Stack:** Java, Swing, Python 3 (Pillow), Antigravity Image Generation Tool.

## Global Constraints
- Target button dimensions: 110 x 244 pixels.
- Asset output paths: `src/assets/play1.gif`, `src/assets/play2.gif`, `src/assets/play3.gif`, `src/assets/play4.gif`, `src/assets/play5.gif`, `src/assets/play6.gif`.
- Must pass `mvn test-compile` / `mvn compile` without errors.

---

### Task 1: Generate and Process Character Button Images

**Files:**
- Create/Modify: `src/assets/play1.gif` (Chun-Li)
- Create/Modify: `src/assets/play2.gif` (Sheeva)
- Create/Modify: `src/assets/play3.gif` (Akuma)
- Create/Modify: `src/assets/play4.gif` (Cable)
- Create/Modify: `src/assets/play5.gif` (Spider-Man)
- Create/Modify: `src/assets/play6.gif` (Doctor Doom)
- Create/Scratch: `.gemini/antigravity-cli/brain/b94d305d-9977-45a0-9de4-b618cb791c83/scratch/resize_buttons.py`

**Interfaces:**
- Consumes: `generate_image` tool outputs.
- Produces: 110x244 pixel GIF images placed in `src/assets/`.

- [ ] **Step 1: Generate high quality raw images using `generate_image` tool**

  Generate 6 portrait images with vertical aspect ratio (9:16) for:
  1. `chun_button` (Chun-Li)
  2. `sheeva_button` (Sheeva)
  3. `akuma_button` (Akuma)
  4. `cable_button` (Cable)
  5. `spider_button` (Spider-Man)
  6. `doom_button` (Doctor Doom)

- [ ] **Step 2: Create Python processing script `resize_buttons.py`**

  Write a script to crop and resize each generated image to exactly 110x244 pixels and save to `src/assets/playN.gif`.

```python
import os
from PIL import Image

output_dir = "src/assets"
target_size = (110, 244)

mapping = {
    "chun_button": "play1.gif",
    "sheeva_button": "play2.gif",
    "akuma_button": "play3.gif",
    "cable_button": "play4.gif",
    "spider_button": "play5.gif",
    "doom_button": "play6.gif",
}

# Image processing logic to crop-fit center and resize to target_size
```

- [ ] **Step 3: Execute Python script to update `src/assets/play*.gif`**

  Run: `python3 resize_buttons.py`
  Expected: 6 GIF files updated in `src/assets/` with resolution 110x244.

- [ ] **Step 4: Commit assets**

```bash
git add src/assets/play*.gif
git commit -m "feat: update character selection button images"
```

---

### Task 2: Compile & Verify Java Application

**Files:**
- Modify: `src/visao/Selecao.java` (if needed for background / alignment check)
- Modify: `src/modelo/CharacterData.java`

- [ ] **Step 1: Compile the Java project using Maven**

  Run: `mvn compile`
  Expected: `BUILD SUCCESS`

- [ ] **Step 2: Verify asset presence and image dimensions via shell**

  Run: `file src/assets/play*.gif`
  Expected: All 6 files showing GIF image data, 110 x 244 (or matching bounds).

- [ ] **Step 3: Commit build verification**

```bash
git add .
git commit -m "build: verify character selection asset integration"
```
