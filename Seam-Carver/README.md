# Seam Carving for Content-Aware Image Resizing

This project, completed for my high school data structures and algorithms course, is an implementation of the seam carving algorithm for content-aware image resizing. This program is based on the project from Princeton University's "Algorithms, Part 2" course on Coursera.
Unlike standard image scaling which distorts the entire image, seam carving intelligently identifies and removes the least important "seams" of pixels **through dynamic programming**, preserving the most significant content.

**Project Overview**

The goal of this project is to create a data type, SeamCarver, that can resize a given image without distorting its important features. Standard resizing (stretching or compressing) treats all pixels equally, leading to undesirable results where objects of interest are squashed or stretched.
Seam carving works by finding a path of pixels—a "seam"—from one edge of the image to the other that has the lowest total "energy." Energy is a measure of a pixel's importance, typically defined by the color gradient at that pixel. By repeatedly finding and removing these low-energy seams, the image's dimensions can be reduced while keeping the high-energy, important content intact.

**The Algorithm Explained**

The process can be broken down into three main steps, which are repeated until the image reaches the desired dimensions.
1. Energy Calculation
First, we must quantify the "importance" of each pixel. This is done by calculating its energy. This project uses the dual-gradient energy function. The energy of a pixel is determined by the rate of color change (gradient) in both the x and y directions. High gradients occur at the edges between different objects, which are typically the most important parts of an image. Pixels in areas of flat color (like a blue sky) will have low energy.
2. Seam Identification (Dynamic Programming)
The goal is to find the seam with the minimum possible total energy.
To find the lowest-energy vertical seam:
We compute a cumulative energy matrix M that stores the minimum total energy of any seam ending at that pixel.
This calculation is performed row by row, from top to bottom.
Once the entire matrix M is computed, the minimum value in the last row corresponds to the end of the minimum-energy seam.
We then backtrack from this position to the top of the image, following the path of minimum cumulative energies, to reconstruct the seam.
3. Seam Removal
Once the lowest-energy seam is identified, the pixels in that seam are removed from the image. This reduces the image's width (for a vertical seam) or height (for a horizontal seam) by one pixel. The process is then repeated to remove more seams.

**Performance and Optimization**

The course assignment places strict performance requirements on the implementation.
The time to find a seam must be proportional to width × height.
The time to remove a seam must be proportional to width + height (or faster).
The energy() method must take constant time.
A key challenge is that after removing a seam, the energy values of adjacent pixels change. An optimized implementation should only re-calculate the energy for the pixels that are affected, rather than recomputing the energy for the entire image after each seam removal.

**Dependencies**

This project requires the algs4.jar library from the Princeton Algorithms, Part 2 course. This library provides essential classes for the project, including Picture for image manipulation and StdDraw for visualization.
