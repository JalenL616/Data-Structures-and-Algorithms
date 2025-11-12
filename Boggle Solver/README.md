# High-Performance Boggle Solver
This project, completed for my high school data structures and algorithms course, is an implementation of a high-performance solver for the game of Boggle based on the programming project from Princeton University's "Algorithms, Part 2" course on Coursera.
**The solver uses a Trie (prefix tree) for efficient dictionary storage and a Depth-First Search (DFS) algorithm with backtracking to explore the Boggle board and identify all valid words.**

**Project Overview**

The goal of this project is to write a program that can, given a dictionary and a Boggle board, find every valid word on that board as quickly as possible. This requires an efficient way to store the dictionary and a systematic way to explore all possible paths on the board.
The final implementation is capable of searching through a dictionary of tens of thousands of words and finding all valid words on a standard 4x4 Boggle board in a fraction of a second.

**The Game of Boggle**

Boggle is a word game played on a grid of lettered dice (typically 4x4 or 5x5). The rules are:
 - Words must be formed from adjacent letters. Adjacency includes horizontal, vertical, and diagonal connections.
 - A letter's die may not be used more than once in the same word.
 - Words must be at least 3 letters long.
 - The letter "Q" on a die is almost always treated as "Qu".
 - Words must exist in a pre-defined dictionary.

**Solution Approach**

The Core Challenge: Efficiency
A naive approach, such as generating every possible path on the board and checking if it's in the dictionary, is computationally infeasible. The number of paths is enormous. The key to an efficient solution is to prune the search space as early as possible. We should stop exploring a path on the board the moment it ceases to be a valid prefix of any word in the dictionary.

**Data Structure: The Trie**

To enable efficient prefix lookups, the dictionary is stored in a Trie (also known as a prefix tree). This data structure is perfectly suited for this problem:
 - Each node in the Trie represents a character. A path from the root down to a node represents a prefix.
 - Nodes can be marked to indicate that they represent the end of a complete word.
 - Checking if a string is a word or a valid prefix takes time proportional to the length of the string, not the size of the dictionary.
This allows us to instantly determine if the current path of letters on the board ("CAT", for example) is a prefix to any valid word ("CATCH", "CATALOG"). If it isn't, we can immediately stop exploring that path and backtrack.

**Algorithm: Depth-First Search on the Board**

The Boggle board can be modeled as an implicit graph, where each die is a vertex and edges exist between adjacent dice. Finding all word paths is a graph traversal problem. Depth-First Search (DFS) is the natural choice for this kind of exhaustive path exploration.

The algorithm works as follows:

1. A separate DFS is initiated from every die on the board.
2. The recursive DFS function keeps track of the current path of dice (and the corresponding string prefix).
3. At each step, the algorithm moves to an adjacent, unvisited die.
4. After adding the new letter, it checks the Trie:
   - If the new string is not a valid prefix in the Trie, the function backtracks immediately (this is the crucial pruning step).
   - If the string is a valid word (and meets the length requirement), it is added to a set of found words.
   - If it is a valid prefix, the search continues to its neighbors.
5. To prevent using the same die twice in one word, we mark dice as "visited" for the duration of a recursive path and "un-visit" them when we backtrack. This allows them to be used in different words.

**Scoring**

The program scores words based on their length:
0-2 letters: 0 points
3-4 letters: 1 point
5 letters: 2 points
6 letters: 3 points
7 letters: 5 points
8+ letters: 11 points

**Dependencies**

This project requires the algs4.jar library from the Princeton Algorithms, Part 2 course. This library provides essential classes for the project, including In, StdOut, and the BoggleBoard class itself.
