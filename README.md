# Art

Various Generative Art algorithms in Kotlin.

## Quad Tree Entropy

This art form recursively partitions an image into quadrants, towards lower entropy.

Steps:
1. Partition the image into four quadrants.  
2. Color each quadrant based on the average color of the pixels in the target image.
3. Compute each quadrant's squared error between the original target image and the generated image.
4. Select the quadrant with the highest error and recur into it. 
5. Repeat from step one, using the current highest error quadrant.

Notes:
* To avoid local minima, and/or force the algorithm to focus on areas of high entropy, there is a randomness variable (0 - 100%) to help the algorithm generate "better" results. The default value is 5%.
* In order to have well defined grid lines, it is recommended to use an image with dimensions that are power of 2. (Squares are not required, 512x256, is also perfectly ok.)
* Algorithm inspired by [Michael Fogleman](https://www.michaelfogleman.com/static/quads/)'s post.

### Examples

My Profile picture. (left: original, middle: without grid borders, right: with grid borders)
<img src="https://github.com/kennycason/art/blob/master/src/main/resources/profile_512px.jpg?raw=true" width="33%"/><img src="https://github.com/kennycason/art/blob/master/output/quad_tree/profile_512px_1537867958530.png?raw=true" width="33%"/><img src="https://github.com/kennycason/art/blob/master/output/quad_tree/profile_512px_1537867958556_borders.png?raw=true" width="33%"/>
<img src="https://github.com/kennycason/art/blob/master/output/quad_tree/profile_512px_1537872534697.png?raw=true" width="50%"/><img src="https://github.com/kennycason/art/blob/master/output/quad_tree/profile_512px_1537872534720_borders.png?raw=true" width="50%"/>
<img src="https://github.com/kennycason/art/blob/master/output/quad_tree/profile_512px_1537867958556_borders.png?raw=true" width="100%"/>
<img src="https://github.com/kennycason/art/blob/master/output/quad_tree/jing_1537868439570_borders.png?raw=true" width="50%"/><img src="https://github.com/kennycason/art/blob/master/output/quad_tree/moon_first_1537868377501_borders.png?raw=true" width="50%"/>
<img src="https://github.com/kennycason/art/blob/master/output/quad_tree/moon_apollo_1537869132610_borders.png?raw=true"/>
<img src="https://github.com/kennycason/art/blob/master/output/quad_tree/space_needle_1537868432376_borders.png?raw=true" width="75%"/><img src="https://github.com/kennycason/art/blob/master/output/quad_tree/bulbasaur_1537794420319.png" width="25%"/>
<img src="https://github.com/kennycason/art/blob/master/output/quad_tree/flower_1537808765438.png?raw=true"/>

### John Conway's Game of Life on Images

A modification of John Conway's Game of Life adapted to images.

<img src="https://github.com/kennycason/art/blob/master/output/image_gol/profile_512px.jpg_gol_40.png?raw=true"/>
<img src="https://github.com/kennycason/art/blob/master/output/image_gol/flower.jpg_gol_150.png?raw=true"/>

## John Conway's Game of Life - Graph 

A modification of John Conways' Game of Life adaptd to a graph where every node is connected to the 8 nearest neighbors.

<img src="https://github.com/kennycason/art/blob/master/output/graph_gol/gol_4_4.png?raw=true"/>

## MandelBulb 2D Slices

<img src="https://github.com/kennycason/art/blob/master/output/mandelbulb/mandelbulb.png?raw=true"/>


## Random Alien Tree

Nothing Pretty, just randomly drawing circles while following random branching vectors.

<img src="https://github.com/kennycason/art/blob/master/output/alien_tree/1537527022883.png?raw=true"/>

