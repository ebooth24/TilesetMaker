<div align="left">
  <img src="/resources/how_it_works.png" alt="idk" style="width: 40%; height: 40%"/>
</div>
  
# TilesetMaker
A tool that makes designing game tilesets significantly easier. <br/>

It greatly reduces the number of images needed to draw from 47 to 8. These images are automatically rotated and combined to create a full tileset of 47 tiles. <br/>

<p float="left">
  <img src="/resources/47tiles.png"     alt="full tileset consisting of 47 tiles"      style="width: 40%; height: 40%"/>
  <img src="/resources/8tiles.png"      alt="reduced tileset consisting of 8 images"   style="width: 40%; height: 40%"/>
</p>

Includes a tile drawing editor and a tileset display with autotiling. <br/>

<img src="/resources/screenshot.png" alt="screenshot">

Video Demo: https://www.youtube.com/watch?v=Eq31r2sk1Pg

The tile drawing editor has the following features:
- draw
- erase (draw over currently selected color)
- various brush sizes
- color selector (top right, close color selector window to confirm color change)
- eyedropper tool (right click)
- fill (double tap)

<br/>

NOTE: 
To save an edit to a tile, you must switch to another tile in the tile editor drawing window. <br/>
To update all tiles after saving an edit, simply make a change in the tileset display window. <br/>


To reset tiles:
1. Delete the images in the "draw" folder
2. Copy and paste images in the "drawDefault" folder to "draw"
3. Follow the steps above to update tiles.

## License
[MIT](https://choosealicense.com/licenses/mit/)
