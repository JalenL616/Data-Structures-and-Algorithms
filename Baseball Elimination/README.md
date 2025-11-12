# Baseball Elimination Problem
This project, completed for my high school data structures and algorithms course, is an implementation of a solution to the classic Baseball Elimination problem based on the programming project from Princeton University's "Algorithms, Part 2" course on Coursera.
The program determines for each team in a baseball division whether it has been mathematically eliminated from winning (or tying for) first place. The solution demonstrates a clever **application of the max-flow min-cut theorem to solve** a problem that is not immediately obvious as **a network flow problem**.

**Project Overview**

In a sports division, a team is considered mathematically eliminated if it cannot possibly finish the season in first place, even if it wins all of its remaining games. This project implements a program that takes the current standings of a baseball division (wins, losses, remaining games) and determines for each team:

 - Whether the team is eliminated.
 - If it is eliminated, a "certificate of elimination": a subset of other teams that, together, make it impossible for the team in question to win.

**The Baseball Elimination Problem**

There are two ways a team can be eliminated:

 - Trivial Elimination: A team is trivially eliminated if the number of games it can possibly win at most (current wins + remaining games) is less than the current number of wins for some other team. This is a simple check.
 - Nontrivial Elimination: A more complex scenario occurs when a team is not trivially eliminated but still cannot win. This happens when the remaining games to be played among other teams guarantee that at least one of them will finish with more wins. Discovering this requires a more sophisticated approach.
Solution Approach

**Trivial Elimination**

For a given team x, we first check for trivial elimination. We iterate through all other teams i in the division. If wins[x] + remaining[x] < wins[i], then team x is eliminated. The certificate of elimination is simply team i.

**Nontrivial Elimination: The Max-Flow Formulation**

If a team x is not trivially eliminated, we must determine if the remaining games can be played out in such a way that team x ends up with the most wins. This is modeled as a maximum flow problem.
We construct a flow network and check if the maximum possible flow "saturates" all the edges originating from the source vertex. If it does, it means all remaining games can be resolved in a way that no team surpasses team x's maximum possible win total. If it does not, the team is eliminated.

**Algorithm Details**

Constructing the Flow Network
To check if team x is eliminated, we build a specific flow network with the following vertices and edges:
A source vertex s and a sink vertex t.
 - Game vertices: One for each pair of other teams {i, j} that still have games to play against each other.
 - Team vertices: One for each team i in the division (other than x).

Edges are added as follows:
1. From the source s to each game vertex {i, j}: An edge with capacity equal to the number of games remaining between teams i and j. This represents the wins from these games that need to be distributed.
2. From each game vertex {i, j} to the two corresponding team vertices i and j: Edges with infinite capacity. This allows the wins from the game to flow to either participating team.
3. From each team vertex i to the sink t: An edge with capacity w_max - w_i, where w_max is the maximum number of games team x can win (wins[x] + remaining[x]) and w_i is the current win total for team i. This capacity represents the maximum number of additional games team i can win without surpassing team x.

**Interpreting the Max-Flow Result**

We calculate the max flow from s to t in this network (using the Ford-Fulkerson algorithm).
 - If the max flow equals the total number of remaining games between all teams (other than x), it means all game-wins could be distributed among the teams without any of them exceeding the win total of team x. In this case, team x is not eliminated.
 - If the max flow is less than the total number of remaining games, it is impossible to distribute the wins without at least one team surpassing x. Therefore, team x is eliminated.

**Finding the Certificate of Elimination**

By the max-flow min-cut theorem, the max flow is equal to the capacity of the min cut. When a team is eliminated, the teams on the source side of the min-cut (excluding s itself) form the certificate of elimination. These are the teams that, collectively, are guaranteed to accumulate enough wins among themselves to eliminate team x. We can find these vertices by running a search (like BFS or DFS) from the source s on the residual graph after the max-flow calculation.

**Dependencies**

This project requires the algs4.jar library from the Princeton Algorithms, Part 2 course. This library provides essential classes for the project, including FlowNetwork, FlowEdge, FordFulkerson, In, and StdOut.
