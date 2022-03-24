# Art-Gen

As a new developer and lover of art, I decided to develop a generative art program to explore the depths of procedural art as well as sharpen my newbie developer skills. I chose to write this in Clojure for a few reasons. First, very little boilerplate was required to get started. This was ideal for experimentation and development of generative art tools. Likewise, the Quil library is lightweight and easy to implement and utilize. Second, I wanted to experiment with functional programming and immutable data structures. Designing the program this way should make multithreading and concurrency easier to tackle.  

This project is nowhere close to being done given that I have only worked on it during school breaks, however, I have managed to put together a handful of interesting tools for generative art. Here are some of the results: 

## Triangle Mapping:

The triangle mapping feature first divides the canvas into two triangles. It then recursively divides each triangle to a given depth. To keep the triangles interesting and visually appealing, the function divides each triangle by its longest edge. This creates a more balanced look and avoids long, skinny triangles. The function call specifies the max depth to be achieved and allows for random variables to halt the recursive division prematurely, creating interesting larger triangle "holes" the triangle map. Once the final depth triangle is reached, the function uses a barycentric algorithm to collect all of the pixels within the triangle. The returned result is a map of triangle pixel collections that can be further modified. 

![recursive-triangle-map](https://user-images.githubusercontent.com/86869080/159971176-31106039-a95f-4012-a955-ec9400cd5eb6.jpg)

![recursive-triangle-map-2](https://user-images.githubusercontent.com/86869080/159977898-20dded57-1a44-4419-b7a9-a8a16ac3768f.jpg)

![recursive-triangle-map-3](https://user-images.githubusercontent.com/86869080/159978199-318ec80b-8b9e-4034-ba00-e81ae92976cc.jpg)

In the following example outputs, I experimented with inputting an image, recursively dividing the image into triangles and averaging the RGB values of each triangle to produce a triangular-pixelated effect:

![recursive-triangle-map-4](https://user-images.githubusercontent.com/86869080/159978847-2fb64b1a-3d7b-4e14-a595-92d051ebfcf1.jpg)

![recursive-triangle-map-5](https://user-images.githubusercontent.com/86869080/159984542-3559d78f-7e7f-4ae4-b302-33d5fdb941df.jpg)

![recursive-triangle-map-6](https://user-images.githubusercontent.com/86869080/159984568-7423cbf2-dd0b-4097-8b7c-027f268d611a.jpg)


## Usage

LightTable - open `core.clj` and press `Ctrl+Shift+Enter` to evaluate the file.

Emacs - run cider, open `core.clj` and press `C-c C-k` to evaluate the file.

REPL - run `(require 'quil-project.core)`.

## License

Copyright © 2016 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
"# Art-Gen" 
