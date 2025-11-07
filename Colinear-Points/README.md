# Colinear-Points

This project, completed as part of my high school data structures and algorithms course, implements two algorithms to identify all line segments containing 4 or more collinear points within a given set of points. The programming project is from Princeton University's "Algorithms, Part 1" course on Coursera.
The project **contrasts a brute-force approach with a more efficient, sorting-based algorithm**, demonstrating the profound impact of algorithm design on performance.

https://www.coursera.org/learn/algorithms-part1/programming/prXiW/collinear-points

**Project Overview**

Given a set of n distinct points in a two-dimensional plane, the goal is to find every (maximal) line segment that connects 4 or more of these points. This project implements and compares two distinct algorithms for solving this geometric problem.
BruteCollinearPoints.java: A brute-force method that examines every combination of 4 points.
FastCollinearPoints.java: A significantly faster method that uses sorting as its core component.
A point is represented by its (x, y) coordinates. A set of points is collinear if they all lie on the same straight line. The task is to develop a program that can systematically identify these collinear sets from a file of input points and represent them as line segments.

**Brute-Force Algorithm**

The BruteCollinearPoints.java class implements the straightforward, brute-force approach.
Algorithm: It **iterates through every distinct combination of 4 points** from the input set.
Check: For each combination (p, q, r, s), it checks if the slopes between p and the other three points are all equal (slope(p, q) == slope(p, r) == slope(p, s)).
Performance: This method is simple to conceptualize but computationally expensive, with a time complexity of O(n⁴), making it infeasible for large inputs.

**Fast Sorting-Based Algorithm**

The FastCollinearPoints.java class provides **a much more efficient solution by leveraging a clever sorting technique.**
Algorithm: The algorithm iterates through each point p in the set and uses it as an origin.
For each point p, it treats p as the origin.
It calculates the slope from p to every other point in the set.
It sorts all the other points based on the slope they make with p, using the slopeOrder() comparator.
It then iterates through this newly sorted array. A sequence of 3 or more adjacent points with an equal slope to p indicates a collinear set including p.
**Duplicate Handling**: To avoid adding permutations of the same line segment (e.g., segment A-B-C-D and D-C-B-A), a segment is only added if the origin point p is the smallest point (according to its natural order) in the discovered collinear set.
Performance: This algorithm has a time complexity of O(n² log n), a dramatic improvement over the brute-force method. The dominant operation is sorting the other n-1 points for each of the n origin points.

**Dependencies**
This project requires the algs4.jar library from the Princeton Algorithms, Part I course, which provides the necessary libraries for drawing and I/O (StdDraw, StdOut, In).
