### Theme picker

The application currently only supports 16:9 resolution wallpapers

Example command to resize an image to 3840x2160
```
ffmpeg -i input -vf "scale='if(gt(a,3840/2160),-1,3840)':'if(gt(a,3840/2160),2160,-1)':flags=lanczos,crop=3840:2160" -c:v png output
```
