### Theme picker

For best experience:

- Config file in ~/.config/theme-picker/config.yaml
- All wallpapers with the same aspect ratio  
  Example command to resize an image to 3840x2160:  
  ```
  ffmpeg -i input -vf "scale='if(gt(a,3840/2160),-1,3840)':'if(gt(a,3840/2160),2160,-1)':flags=lanczos,crop=3840:2160" -c:v png output
  ```
- Aspect ratios used in config file match aspect ratio of all wallpapers
