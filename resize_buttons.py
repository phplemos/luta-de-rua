import os
import glob
from PIL import Image, ImageDraw, ImageFont, ImageFilter, ImageSequence

output_dir = "src/assets"
target_size = (110, 244)
target_aspect = target_size[0] / target_size[1]

brain_dir = "/home/phplemos/.gemini/antigravity-cli/brain/95640fcd-8992-4dfd-85ae-298588d0f963"

mapping = [
    ("chun_button", "play1.gif", "Chun-Li", (0, 100, 255)),
    ("sheeva_button", "play2.gif", "Sheeva", (255, 200, 0)),
    ("akuma_button", "play3.gif", "Akuma", (255, 30, 30)),
    ("cable_button", "play4.gif", "Cable", (255, 140, 0)),
    ("spider_button", "play5.gif", "Spider-Man", (255, 0, 50), "src/assets/SpiderP1/spider_pd.gif"),
    ("doom_button", "play6.gif", "Doctor Doom", (0, 255, 100), "src/assets/DoomP1/doom_pd.gif"),
]

def crop_and_resize(img, target_size=(110, 244)):
    w, h = img.size
    img_aspect = w / h
    if img_aspect > target_aspect:
        new_w = int(h * target_aspect)
        left = (w - new_w) // 2
        crop_box = (left, 0, left + new_w, h)
    else:
        new_h = int(w / target_aspect)
        top = (h - new_h) // 2
        crop_box = (0, top, w, top + new_h)
    
    cropped = img.crop(crop_box)
    resized = cropped.resize(target_size, Image.Resampling.LANCZOS)
    return resized

def generate_fallback_portrait(name, border_color, sprite_gif_path):
    # High resolution portrait canvas (576 x 1024, 9:16 vertical aspect ratio)
    W, H = 576, 1024
    img = Image.new("RGB", (W, H), (15, 15, 25))
    draw = ImageDraw.Draw(img)

    # Gradient dark background
    for y in range(H):
        r = int(12 + (y / H) * 20)
        g = int(12 + (y / H) * 15)
        b = int(25 + (y / H) * 35)
        draw.line([(0, y), (W, y)], fill=(r, g, b))

    # Add background grid lines / arcade details
    grid_color = (35, 40, 60)
    for x in range(0, W, 40):
        draw.line([(x, 0), (x, H)], fill=grid_color, width=1)
    for y in range(0, H, 40):
        draw.line([(0, y), (W, y)], fill=grid_color, width=1)

    # Load character sprite from GIF
    if os.path.exists(sprite_gif_path):
        sprite_gif = Image.open(sprite_gif_path)
        # Take first frame or middle frame
        frame = sprite_gif.convert("RGBA")
        
        # Remove background (assuming top-left corner color or transparency)
        bg_pixel = frame.getpixel((0, 0))
        datas = frame.getdata()
        new_data = []
        for item in datas:
            if item[3] == 0:
                new_data.append((0, 0, 0, 0))
            elif abs(item[0] - bg_pixel[0]) < 15 and abs(item[1] - bg_pixel[1]) < 15 and abs(item[2] - bg_pixel[2]) < 15:
                new_data.append((0, 0, 0, 0))
            else:
                new_data.append(item)
        frame.putdata(new_data)

        # Scale up sprite
        sw, sh = frame.size
        scale = min((W * 0.75) / sw, (H * 0.65) / sh)
        target_sw, target_sh = int(sw * scale), int(sh * scale)
        scaled_sprite = frame.resize((target_sw, target_sh), Image.Resampling.NEAREST)

        # Center sprite on canvas
        sx = (W - target_sw) // 2
        sy = (H - target_sh) // 2 + 30

        # Create glow shadow behind sprite
        shadow = Image.new("RGBA", (target_sw + 40, target_sh + 40), (0, 0, 0, 0))
        shadow_draw = ImageDraw.Draw(shadow)
        shadow_draw.ellipse([0, 0, target_sw + 40, target_sh + 40], fill=border_color + (120,))
        shadow = shadow.filter(ImageFilter.GaussianBlur(25))
        img.paste(shadow, (sx - 20, sy - 20), shadow)

        # Paste sprite
        img.paste(scaled_sprite, (sx, sy), scaled_sprite)

    # Outer glowing border
    border_margin = 20
    border_rect = [border_margin, border_margin, W - border_margin, H - border_margin]
    
    # Multi-layer glowing border
    glow_img = Image.new("RGBA", (W, H), (0, 0, 0, 0))
    glow_draw = ImageDraw.Draw(glow_img)
    for t in range(12, 0, -2):
        alpha = int(180 * (1.0 - t / 12.0))
        glow_draw.rectangle([border_margin - t, border_margin - t, W - border_margin + t, H - border_margin + t], outline=border_color + (alpha,), width=2)
    glow_img = glow_img.filter(ImageFilter.GaussianBlur(4))
    img.paste(glow_img, (0, 0), glow_img)

    draw.rectangle(border_rect, outline=border_color, width=6)
    draw.rectangle([border_margin + 6, border_margin + 6, W - border_margin - 6, H - border_margin - 6], outline=(255, 255, 255), width=2)

    # Header title banner
    banner_h = 70
    draw.rectangle([border_margin + 6, border_margin + 6, W - border_margin - 6, border_margin + 6 + banner_h], fill=(10, 15, 30))
    draw.line([(border_margin + 6, border_margin + 6 + banner_h), (W - border_margin - 6, border_margin + 6 + banner_h)], fill=border_color, width=3)
    
    # Title text
    try:
        font = ImageFont.truetype("/usr/share/fonts/TTF/DejaVuSans-Bold.ttf", 36)
    except:
        font = ImageFont.load_default()
    
    bbox = draw.textbbox((0, 0), name.upper(), font=font)
    tw, th = bbox[2] - bbox[0], bbox[3] - bbox[1]
    tx = (W - tw) // 2
    ty = border_margin + 6 + (banner_h - th) // 2
    draw.text((tx + 2, ty + 2), name.upper(), fill=(0, 0, 0), font=font)
    draw.text((tx, ty), name.upper(), fill=border_color, font=font)

    return img

def main():
    os.makedirs(output_dir, exist_ok=True)
    
    for prefix, out_file, name, color, *extra in mapping:
        pattern = os.path.join(brain_dir, f"{prefix}_*.jpg")
        matches = glob.glob(pattern)
        
        out_path = os.path.join(output_dir, out_file)
        
        if matches:
            src_file = matches[0]
            print(f"Processing generated image {src_file} -> {out_path}")
            img = Image.open(src_file)
        else:
            print(f"No generated image for {prefix}. Building fallback high-def portrait card -> {out_path}")
            sprite_path = extra[0] if extra else ""
            img = generate_fallback_portrait(name, color, sprite_path)
        
        final_img = crop_and_resize(img, target_size)
        
        # Convert to GIF format
        # Convert RGB to P mode with palette
        gif_img = final_img.convert("P", palette=Image.Palette.ADAPTIVE, colors=256)
        gif_img.save(out_path, format="GIF")
        
        print(f"Successfully saved {out_path} ({final_img.size[0]}x{final_img.size[1]})")

if __name__ == "__main__":
    main()
