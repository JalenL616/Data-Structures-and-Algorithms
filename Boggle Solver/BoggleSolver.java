/**
 * @author Jalen Locke attests that this code is their original work and was written in compliance with the class Academic Integrity and Collaboration Policy found in the syllabus. 
    The most fun part about this assignment was making the DFS in conjunction with
    the TST. The concept made sense with the implementation, so I was able to code
    everything with minimal bugs. One thing, hilariously, that tripped me up, was 
    that I used the Algs4 TrieST intead of TST. The former had the 26 child nodes
    versus the latter's three directional nodes. When I switched to TST all my timing
    issues dissapeared
    
 */

 import edu.princeton.cs.algs4.TST;
 import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
 
 import java.util.ArrayList;
 
public class BoggleSolver
{
    private final TST<Integer> TST;
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary)
    {
        TST = new TST<Integer>();

        for (int i = 0; i < dictionary.length; i++)
        {
            TST.put(dictionary[i], dictionary[i].length());
        }
    }
    

    private void gridDFS(Node currentNode, ArrayList<Node> visitedNodes, String wordSoFar, ArrayList<String> wordList)
    {
        Node current = currentNode;
        ArrayList<Node> neighborNodes = current.getNeighborNodes();
        for (Node n : neighborNodes)
        {
            // No backtracking
            if (visitedNodes.contains(n))
            {
                continue;
            }
            String newWord = wordSoFar + n.letter();

            // Add if scorable word
            if (TST.get(newWord) != null)
            {
                wordList.add(newWord);
            }

            // Check the Node's neighbors if there are words with this prefix
            if (TST.keysWithPrefix(newWord).iterator().hasNext())
            {
                // Update visited nodes for this branch
                visitedNodes.add(n);
                // DFS
                gridDFS(n, visitedNodes, newWord, wordList);
                // When retracing remove this from visited
                visitedNodes.remove(n);
            }
        }

        // Will continue recurrsively until no more possible prefixes
    }

    
    private class Node
    {
        private String letter;
        private ArrayList<Node> neighborNodes;

        private Node(String letter)
        {
            this.letter = letter;
            neighborNodes = new ArrayList<Node>();
        }

        private String letter() {return letter;}
        private ArrayList<Node> getNeighborNodes()
        {
            return neighborNodes;
        }
        private void addNeighborNode(Node neighbor)
        {
            neighborNodes.add(neighbor);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board)
    {

        ArrayList<ArrayList<Node>> boardNodes = new ArrayList<ArrayList<Node>>();

        // POPULATE BOARDNODE 2D ARRAYLIST
        for (int row = 0; row < board.rows(); row++)
        {
            ArrayList<Node> rowArr = new ArrayList<Node>();
            for (int col = 0; col < board.cols(); col++)
            {
                String letter = "" + board.getLetter(row, col);
                if (letter.equals("Q"))
                {
                    letter = "QU";
                }
                Node letterNode = new Node(letter);
                rowArr.add(letterNode);
            }
            boardNodes.add(rowArr);
        }

        // UPDATE NEIGHBORS IN BOARDNODE NODES
        for (int row = 0; row < board.rows(); row++)
        {
            for (int col = 0; col < board.cols(); col++)
            {
                Node letterNode = boardNodes.get(row).get(col);
                // UP
                if (row != 0)
                {
                    if (col != 0)
                    {
                        // UP LEFT
                        letterNode.addNeighborNode(boardNodes.get(row - 1).get(col - 1));
                    }
                    if (col != boardNodes.get(row).size() - 1)
                    {
                        // UP RIGHT
                        letterNode.addNeighborNode(boardNodes.get(row - 1).get(col + 1));
                    }
                    // UP
                    letterNode.addNeighborNode(boardNodes.get(row - 1).get(col));
                }
                // DOWN
                if (row != boardNodes.size() - 1)
                {
                    if (col != 0)
                    {
                        // DOWN LEFT
                        letterNode.addNeighborNode(boardNodes.get(row + 1).get(col - 1));
                    }
                    if (col != boardNodes.get(row).size() - 1)
                    {
                        // DOWN RIGHT
                        letterNode.addNeighborNode(boardNodes.get(row + 1).get(col + 1));
                    }
                    // DOWN
                    letterNode.addNeighborNode(boardNodes.get(row + 1).get(col));
                }
                if (col != 0)
                {
                    // LEFT
                    letterNode.addNeighborNode(boardNodes.get(row).get(col - 1));
                }
                if (col != boardNodes.get(row).size() - 1)
                {
                    // RIGHT
                    letterNode.addNeighborNode(boardNodes.get(row).get(col + 1));
                }
            }
        }

        ArrayList<String> scorableWords = new ArrayList<String>();
        // PERFORM DFS FOR EACH NODE
        for (int row = 0; row < board.rows(); row++)
        {
            for (int col = 0; col < board.cols(); col++)
            {
                Node letter = boardNodes.get(row).get(col);

                ArrayList<Node> visitedLetters = new ArrayList<Node>();
                visitedLetters.add(letter);

                gridDFS(letter, visitedLetters, ("" + letter.letter()), scorableWords);
            }
        }

        ArrayList<String> uniqueScoreableWords = new ArrayList<String>();

        // Remove duplicate and too short words
        for (int i = scorableWords.size() - 1; i >= 0; i--)
        {
            // Ignore words size 2
            if (scorableWords.get(i).length() <= 2)
            {
                continue;
            }
            // Only add words that are unique
            if (!uniqueScoreableWords.contains(scorableWords.get(i)))
            {
                uniqueScoreableWords.add(scorableWords.get(i));
            }
        }

        return uniqueScoreableWords;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word)
    {
        if (TST.get(word) == null)
        {
            return 0;
        }
        int rawScore = TST.get(word);
        if (rawScore <= 2)
        {
            return 0;
        }
        else if (rawScore <= 4)
        {
            return 1;
        }
        else if (rawScore == 5)
        {
            return 2;
        }
        else if (rawScore == 6)
        {
            return 3;
        }
        else if (rawScore == 7)
        {
            return 5;
        }
        else
        {
            return 11;
        }
    }

    public static void main(String[] args) {

        In in = new In("dictionary-yawl.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard("board-points5.txt");
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
        

    }    
}

