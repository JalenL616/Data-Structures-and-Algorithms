/**
 * @author Jalen Locke attests that this code is their original work and was written in compliance with the class Academic Integrity and Collaboration Policy found in the syllabus. 
 
  * This time I took my own advice and used as many comments and StdOut lines for 
  debugging as possible. I also got a headstart on the coding for once which meant
  I could focus instead of rush. The most annoying part was getting the indexes
  correct for the vertices. I might've wanted to sketch it all out. Either way, 
  bug fixing comprised the majority of my coding time and that went pretty well
  now that I've learned my lesson more and have tons of debug logs
    
 */

import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;

public class BaseballElimination
{
    private final int numTeams;
    private int mostWins;
    private String bestTeam;
    private final HashMap<String, Integer> teamsToInt;
    private final ArrayList<String> teams;
    private final int[] teamWins;
    private final int[] teamLosses;
    private final int[] teamGamesLeft;
    private final int[][] gamesLeftAgainstOpponent;
    
    private final boolean[] eliminated;
    private final ArrayList<ArrayList<String>> minCut;

    public BaseballElimination(String filename)                    
    // create a baseball division from given filename in format specified below
    {
        teams = new ArrayList<String>();

        In in = new In(filename);
        // Initialize numTeams
        numTeams = in.readInt();

        // Initialize team arrays
        teamWins = new int[numTeams];
        teamLosses = new int[numTeams];
        teamGamesLeft = new int[numTeams];
        gamesLeftAgainstOpponent = new int[numTeams][numTeams];

        teamsToInt = new HashMap<String, Integer>();

        minCut = new ArrayList<ArrayList<String>>();
        eliminated = new boolean[numTeams];

        mostWins = 0;
        bestTeam = "";

        // For each line/team initialize variables
        for (int i = 0; i < numTeams; i++)
        {
            String teamName = in.readString();
            teams.add(teamName);
            // Update Hashmap
            teamsToInt.put(teamName, i);

            int numWins = in.readInt();
            // Update teamWins
            teamWins[i] = numWins;
            // Update mostWins
            if (numWins > mostWins)
            {
                mostWins = numWins;
                bestTeam = teamName;
            }

            int numLosses = in.readInt();
            // Update teamLosses
            teamLosses[i] = numLosses;

            int numRemaining = in.readInt();
            // Update teamGamesLeft
            teamGamesLeft[i] = numRemaining;
            
            for (int j = 0; j < numTeams; j++)
            {
                gamesLeftAgainstOpponent[i][j] = in.readInt();
            }
        }

        // CALCULATE AND INITIALLIZE MINCUT AND ELIMINATED

        // BUILD FLOW NETWORKS AND CACULATE VALS

        for (int team = 0; team < numTeams; team++)
        {
            //StdOut.println("team: " + team);

            // Trivially eliminated?
            if (isTriviallyEliminated(teams.get(team)))
            {
                eliminated[team] = true;
                ArrayList<String> minCutBest = new ArrayList<String>();
                minCutBest.add(bestTeam); 
                minCut.add(minCutBest);
                continue;
            }

            // Calculate numMatchups by the matrix -> Total num minus diagonal, divided by two (since each matchup occurs twice)
            int numMatchups = ( ((numTeams - 1) * (numTeams - 1)) - (numTeams - 1) ) / 2;

            // Calculate numVertices -> S node + numMatchups + possible winners (teams - 1) + N node
            int numVertices = (1 + numMatchups + (numTeams - 1) + 1);

            FlowNetwork flow = new FlowNetwork(numVertices);
            
            // Vertex 0 will correspond to S
            int S = 0;
            // Vertex 1 will correspond to T
            int T = 1;
            // Vertexes for the various matchups are indexes 2 - (1 + numMatchups)
            int teamVerticesStart = 2 + numMatchups;
            // Vertexes 2 - V - 1 are the remaining nodes

            HashMap<Integer, Integer> numMatches = new HashMap<Integer, Integer>();
            HashMap<Integer, int[]> involvedTeams = new HashMap<Integer, int[]>();
            
            int hashIndex = 2;
            int colModifier = 0;
            int rowModifier = 0;

            int team1 = 0;
            int team2 = 1;
            // Map each vertex to matchup
            for (int row = 0; row < numTeams - 1; row++)
            {
                if (row == team)
                {
                    // Once you get to the actual team you'll have to 
                    rowModifier = 1;
                }
                if (rowModifier == 1 && (row + rowModifier) == numTeams - 1)
                {
                    break;
                }
                for (int col = row + 1; col < numTeams; col++)
                {
                    
                    //StdOut.println(row + " --> " + col);
                    if (col == team)
                    {
                        colModifier = 1;
                    }
                    //StdOut.println("Augmented: " + (row + rowModifier) + " --> " + (col + colModifier));
                    
                    if (colModifier == 1 && (col + colModifier) == numTeams)
                    {
                        break;
                    }
                    if (row + rowModifier == col + colModifier)
                    {
                        //StdOut.println("skip");
                        continue;
                    }
                    else
                    {
                        
                        if (team2 == numTeams - 1)
                        {
                            team1++;
                            team2 = team1 + 1;
                        }
                        numMatches.put(hashIndex, gamesLeftAgainstOpponent[row + rowModifier][col + colModifier]);
                        //StdOut.println(teamVerticesStart);

                        //StdOut.println("team1: " + team1 + ", team2: " + team2);
                        involvedTeams.put(hashIndex, new int[]{(teamVerticesStart + team1), (teamVerticesStart + team2)});
                        team2++;
                    }
                    hashIndex++;
                }
                colModifier = 0;
            }

            //StdOut.println(numMatches.entrySet());

            
            // Each matchup
            for (int j = 2; j < teamVerticesStart; j++)
            {
                FlowEdge S_to_matchup = new FlowEdge(S, j, numMatches.get(j));
                flow.addEdge(S_to_matchup);

                FlowEdge matchup_to_team1 = new FlowEdge(j, involvedTeams.get(j)[0], Double.POSITIVE_INFINITY);
                flow.addEdge(matchup_to_team1);

                FlowEdge matchup_to_team2 = new FlowEdge(j, involvedTeams.get(j)[1], Double.POSITIVE_INFINITY);
                flow.addEdge(matchup_to_team2);

            }

            // Each game to sink
            int teamModifier = 0;
            int currentTeamWins = teamWins[team];
            int currentTeamGamesLeft = teamGamesLeft[team];

            for (int k = 0; k < numTeams - 1; k++)
            {
                if (k == team && k != numTeams - 2)
                {
                    teamModifier = 1;
                }
                int flowLimit = currentTeamWins + currentTeamGamesLeft - teamWins[k + teamModifier];
                FlowEdge team_to_T = new FlowEdge((k + teamVerticesStart), T, flowLimit);
                flow.addEdge(team_to_T);
            }
            
            //StdOut.println(flow);

            FordFulkerson ff = new FordFulkerson(flow, S, T);
            //StdOut.println(ff.value());
            ArrayList<String> teamMinCut = new ArrayList<String>();
            eliminated[team] = false;

            int kMod = 0;

            for (int k = 0; k < numTeams - 1; k++)
            {
                if (k == team)
                {
                    kMod = 1;
                }
                //StdOut.println((teamVerticesStart + k) + " in cut? " + ff.inCut((teamVerticesStart + k)) + " with value at: " + ff.value());
                if (ff.inCut(teamVerticesStart + k))
                {
                    teamMinCut.add(teams.get(k + kMod));
                    //StdOut.println(teams.get(k + kMod));
                    
                    // If any team is in the cut then they're eliminated
                    eliminated[team] = true;
                }
            }

            if (teamMinCut.isEmpty())
            {
                minCut.add(null);
            }
            else
            {
                minCut.add(teamMinCut);
            }
        }
    }

    public int numberOfTeams()                        
    // number of teams
    {
        return numTeams;
    }

    public Iterable<String> teams()                                
    // all teams
    {
        return teams;
    }

    public int wins(String team)                      
    // number of wins for given team
    {
        if (teamsToInt.get(team) == null) throw new IllegalArgumentException();
        return teamWins[teamsToInt.get(team)];
    }   

    public int losses(String team)                    
    // number of losses for given team
    {
        if (teamsToInt.get(team) == null) throw new IllegalArgumentException();
        return teamLosses[teamsToInt.get(team)];
    }

    public int remaining(String team)                 
    // number of remaining games for given team
    {
        if (teamsToInt.get(team) == null) throw new IllegalArgumentException();
        return teamGamesLeft[teamsToInt.get(team)];
    }

    public int against(String team1, String team2)    
    // number of remaining games between team1 and team2
    {
        if (teamsToInt.get(team1) == null) throw new IllegalArgumentException();
        if (teamsToInt.get(team2) == null) throw new IllegalArgumentException();
        return gamesLeftAgainstOpponent[teamsToInt.get(team1)][teamsToInt.get(team2)];
    }

    public boolean isEliminated(String team)              
    // is given team eliminated?
    {
        if (teamsToInt.get(team) == null) throw new IllegalArgumentException();
        return eliminated[teamsToInt.get(team)];
    }

    private boolean isTriviallyEliminated(String team)
    {
        if (teamsToInt.get(team) == null) throw new IllegalArgumentException();
        return (mostWins > (wins(team) + remaining(team)));
    }

    public Iterable<String> certificateOfElimination(String team)  
    // subset R of teams that eliminates given team; null if not eliminated
    {
        if (teamsToInt.get(team) == null) throw new IllegalArgumentException();
        return minCut.get(teamsToInt.get(team));
    }


    public static void main(String[] args) {
        
        BaseballElimination division = new BaseballElimination("teams5b.txt");
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                //StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    //StdOut.print(t + " ");
                }
                //StdOut.println("}");
            }
            else {
                //StdOut.println(team + " is not eliminated");
            }
        }
         


        /*
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                //StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    //StdOut.print(t + " ");
                }
                //StdOut.println("}");
            }
            else {
                //StdOut.println(team + " is not eliminated");
            }
        }
        */
    }
}