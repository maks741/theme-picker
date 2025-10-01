### Theme picker

In order for the application to work correctly, it is preferrable to have all wallpapers of the same size

Please use the following command to resize the image to 3840x2160 (16:9) with 'scale and crop' strategy:  
ffmpeg -i input -vf "scale='if(gt(a,3840/2160),-1,3840)':'if(gt(a,3840/2160),2160,-1)':flags=lanczos,crop=3840:2160" -c:v png output
