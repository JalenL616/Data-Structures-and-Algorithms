# Percolation

This project, completed for my high school data structures and algorithms course, is an implementation of a solution to the Percolation problem, as presented in Princeton University's "Algorithms, Part 1" course on Coursera. The primary goal is to **estimate the percolation threshold** of an N-by-N grid system **using a Monte Carlo simulation**.

https://www.coursera.org/learn/algorithms-part1/programming/Lhp5z/percolation

**Project Description**

This program models a percolation system on an N-by-N grid of sites. Each site can be either "open" or "blocked." A system is said to "percolate" if there is a path of connected open sites from the top row to the bottom row of the grid. This concept models real-world phenomena, such as determining the fraction of metallic materials needed for a composite system to conduct electricity or the conditions under which water can drain through a porous landscape.
This project uses a **Monte Carlo simulation** to estimate the percolation threshold. The program repeatedly opens sites at random until the system percolates and uses the fraction of open sites as an estimate for the threshold.

**The Percolation Problem**

We model the system as an N-by-N grid. Initially, all sites are blocked. We then open sites one by one. The system is said to percolate if a "full" site exists in the bottom row. A site is considered "full" if it is open and can be connected to an open site in the top row through a chain of adjacent (up, down, left, right) open sites.
The central question is: what is the threshold probability p** such that if sites are opened randomly with a probability p > p**, the system will almost certainly percolate, and if p < p**, it almost certainly will not?

**Solution Approach**
The core of the percolation detection algorithm is the **Union-Find data structure** (also known as a disjoint-set). This data structure is highly efficient for tracking the connectivity of sites. Each site in the grid is an element in a set. When a site is opened, it is "unioned" with any of its adjacent open neighbors.
To efficiently check for percolation, two virtual sites are used: a virtual top site connected to every site in the top row and a virtual bottom site connected to every site in the bottom row. The system percolates if and only if the virtual top site is connected to the virtual bottom site.
This project utilizes a Weighted Quick-Union with Path Compression implementation of the Union-Find algorithm. This is a highly optimized version that ensures that the union, find, and connected operations are performed in nearly constant time on average.

**Monte Carlo Simulation**

To estimate the percolation threshold, the following experiment is conducted multiple times:
Initialize an N-by-N grid with all sites blocked.
Choose a site uniformly at random from the blocked sites and open it.
Check if the system percolates.
Repeat step 2 until the system percolates.
The fraction of open sites (number of open sites / N*N) is recorded as one trial's threshold estimate.
This process is repeated for a specified number of trials (e.g., 30 or more). The final estimated percolation threshold is the sample mean of the results from all trials. We also calculate the sample standard deviation to measure the sharpness of the threshold and can compute a 95% confidence interval for the estimate.

**Key Challenges**

A notable challenge in this problem is avoiding **backwash**. This occurs when, after the system percolates, opening a site at the bottom could incorrectly mark sites as "full" that are not actually connected to the top row (but are connected to the virtual bottom site). This is typically solved by using a second Union-Find data structure that does not have a virtual bottom site, used exclusively for the isFull() check.

**Dependencies**

This project requires the algs4.jar library from the Princeton Algorithms, Part I course. This library provides essential classes for the project, including WeightedQuickUnionUF, StdRandom, and StdStats.
