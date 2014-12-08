Real time Voronoi computation for Android
==============

This is an Android implementation of a localised nearest neighbour algorithm to compute and animate Voronoi cells in real time using OpenGL. The algorithm splits the available space into a grid with integer grid lines. It then places two seed points in each grid cell, each in random positions within the cell. An OpenGL texture full of random values is precomputed and used in the shader as a lookup table to generate coordinates for the seed points.

To compute the colour value at each pixel, the algorithm finds the minimum distance to any seed point within the current grid cell and then compares this to the minimum distances to seed points in the neighbouring eight cells. To give classic Worley noise, this final minimum distance can be set as the pixel's grayscale intensity. This example does something similar but slightly more complicated in order to produce three effects - stained glass, random spheres and random snowflakes. These are all animated, i.e. a time variable is sent to the shader which sends each cell on a random (ish) sinusoidal walk.

This algorithm is scalable, as in the number of grid cells in the space can be increased without causing (at least noticeable) fall in performance, since for each pixel the shader still only searches nine grid cells. A seekbar could be introduced to show this in real time.

However, while this algorithm is much faster than the naive one which searches all seed points every pixel, it needs to be optimised as many android devices still cannot handle the processing power required. For reference, the project worked well on the HTC One and HTC One Mini. Possible optimisations could include determining which neighbouring grid cells couldn't possibly contain the minimum distance and simply disregarding these, as this might reduce the number of calculations per pixel significantly.
