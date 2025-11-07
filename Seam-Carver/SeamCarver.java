/**
 * @author Jalen Locke attests that this code is their original work and was written in compliance with the class Academic Integrity and Collaboration Policy found in the syllabus. 
 
    The hardest part of this project for me stemmed from a greater coding technique issue.
    For SeamCarver, I had to debug a ton despite understanding the concept fairly well.
    The main issue that arose after I'd written all my code, was that the Picture.get(x,y)
    used column, row indexing, which was the opposite of my 2d array. Thus, I had to redo and debug a ton.
    Additionally, a lot of my data was stored differently depending on when I coded it, so as I made fixes,
    other parts became incongruent with my new correct work resulting in more bug fixes.
    Ultimately though, what I really would've benefitted from was following object-oriented design
    I needed to plan out all my methods and DATA STRUCTURES first, and then focus on coding.
    It would've saved tons of time. And then while actually coding, debugging each method in isolation
    would have again saved a lot of rewriting time to fix bugs and incongruities
 */

import edu.princeton.cs.algs4.Picture;
import java.awt.Color;
import java.util.ArrayList;

public class SeamCarver {

    private final Picture pic;
    private final ArrayList<ArrayList<int[]>> newPic;
    private int width;
    private int height;
    private final ArrayList<ArrayList<Double>> energies;
    private final ArrayList<ArrayList<int[]>> rgbs;
    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture)
    {
        pic = new Picture(picture);
        width = pic.width();
        height = pic.height();
        energies = new ArrayList<ArrayList<Double>>();
        rgbs = new ArrayList<ArrayList<int[]>>();
        newPic = new ArrayList<ArrayList<int[]>>();
        //initialize rgbs (and newPic)
        for (int i = 0; i < height; i++)
        {
            ArrayList<int[]> ithRowVals = new ArrayList<int[]>();

            ArrayList<int[]> newPicRow = new ArrayList<int[]>();
            for (int j = 0; j < width; j++)
            {
                Color c = pic.get(j, i);
                int[] colorArr = {c.getRed(), c.getGreen(), c.getBlue()};
                ithRowVals.add(colorArr);

                newPicRow.add(new int[] {j, i});
            }
            rgbs.add(ithRowVals);

            newPic.add(newPicRow);
        }

        initateEnergies();
    }
 
    private void initateEnergies()
    {
        for (int i = 0; i < energies.size(); i++)
        {
            energies.remove(0);
        }
        for (int i = 0; i < height; i++)
        {
            ArrayList<Double> ithRowVals = new ArrayList<Double>();
            for (int j = 0; j < width; j++)
            {
                ithRowVals.add(energy(j, i));
            }
            energies.add(ithRowVals);
        }
        
    }

    // current picture
    public Picture picture()
    {
        Picture outputPic = new Picture(width, height);
        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                // Find where the pixel was at
                int[] oldPicCoordinates = newPic.get(i).get(j);
                // Find the color there
                Color oldPicColor = pic.get(oldPicCoordinates[0], oldPicCoordinates[1]);
                // Put that color in the new coordinate
                outputPic.set(j, i, oldPicColor);
            }
        }
        return outputPic;
        }
 
    // width of current picture
    public int width()
    {
        return width;
    }
 
    // height of current picture
    public int height()
    {
        return height;
    }
 
    // energy of pixel at column x and row y
    public double energy(int x, int y)
    {
        if (x < 0 || y < 0 || x > (width - 1) || y > (height - 1)) throw new IllegalArgumentException();
        //Border
        if (x == 0 || y == 0 || x == (width - 1) || y == (height - 1))
        {
            return 1000;
        }

        
        int[] leftColor = rgbs.get(y).get(x - 1);
        int[] rightColor = rgbs.get(y).get(x + 1);
        int dxSquared = 0;
        int dxRed = leftColor[0] - rightColor[0];
        dxSquared += dxRed * dxRed;
        int dxBlue = leftColor[1] - rightColor[1];
        dxSquared += dxBlue * dxBlue;
        int dxGreen = leftColor[2] - rightColor[2];
        dxSquared += dxGreen * dxGreen;

        int[] upColor = rgbs.get(y - 1).get(x);
        int[] botColor = rgbs.get(y + 1).get(x);
        int dySquared = 0;
        int dyRed = upColor[0] - botColor[0];
        dySquared += dyRed * dyRed;
        int dyBlue = upColor[1] - botColor[1];
        dySquared += dyBlue * dyBlue;
        int dyGreen = upColor[2] - botColor[2];
        dySquared += dyGreen * dyGreen;

        return Math.sqrt(dxSquared + dySquared);
    }

    // Function to rotate the matrix by 90 degrees clockwise
    private void rotateClockwise(ArrayList<ArrayList<Double>> mat) {

        // Initialize the result matrix with zeros
        ArrayList<ArrayList<Double>> res = new ArrayList<ArrayList<Double>>();

        for (int i = 0; i < width; i++) {
            ArrayList<Double> newRow = new ArrayList<Double>();
            for(int j = 0; j < height; j++)
            {
                newRow.add(0.0);
            }
            res.add(newRow);
        }

        // Flip the matrix clockwise using nested loops
        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height; col++) {
                res.get(row).set(col, energies.get(height - 1 - col).get(row));
            }
        }

        // Clear mat
        for (int x = 0; x < height; x++)
        {
            mat.remove(0);
        }

        for (int i = 0; i < width; i++) {
            mat.add(res.get(i));
        }
        //System.out.println(energies.size());
        //System.out.println(energies.get(0).size());
        int temp = height;
        updateHeight(width);
        updateWidth(temp);
    }
    private void updateHeight(int x)
    {
        height = x;
    }
    private void updateWidth(int x)
    {
        width = x;
    }

    // Function to rotate the matrix by 90 degrees counter clockwise
    private void rotateCounterClockwise(ArrayList<ArrayList<Double>> mat) {

        int temp = height;
        updateHeight(width);
        updateWidth(temp);
        // Initialize the result matrix with zeros
        ArrayList<ArrayList<Double>> res = new ArrayList<ArrayList<Double>>();

        for (int i = 0; i < height; i++) {
            ArrayList<Double> newRow = new ArrayList<Double>();
            for(int j = 0; j < width; j++)
            {
                newRow.add(0.0);
            }
            res.add(newRow);
        }

        // Flip the matrix counterclockwise using nested loops
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                res.get(row).set(col, energies.get(col).get(height - 1 - row));
            }
        }

        // Clear mat
        for (int x = 0; x < width; x++)
        {
            mat.remove(0);
        }

        for (int i = 0; i < height; i++) {
            mat.add(res.get(i));
        }
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam()
    {
        
        rotateClockwise(energies);
        int[] result = findVerticalSeam();
        rotateCounterClockwise(energies);
        for (int i = 0; i < result.length; i++)
        {
            result[i] = height - 1 - result[i];
        }
        return result;
    }
 
    // sequence of indices for vertical seam
    public int[] findVerticalSeam()
    {
        ArrayList<ArrayList<int[]>> edgeTo = new ArrayList<ArrayList<int[]>>();
        for (int row = 0; row < height; row++)
        {
            ArrayList<int[]> emptyRow = new ArrayList<int[]>();
            for (int col = 0; col < width; col++)
            {
                emptyRow.add(new int[] {});
            }
            edgeTo.add(emptyRow);
        }
        double[][] distTo = new double[height][width];
        //Topological edge relaxation
        for (int row = 0; row < height - 1; row++)
        {
            for (int col = 0; col < width; col++)
            {
                //Check and update left -> [row + 1][col - 1]
                if (col > 0)
                {
                    double energyTotal = distTo[row][col] + energies.get(row + 1).get(col - 1);
                    if(distTo[row + 1][col - 1] == 0)
                    {
                        // Update energy and edge to location
                        distTo[row + 1][col - 1] = energyTotal;
                        edgeTo.get(row + 1).set(col - 1, new int[] {row, col});
                    }
                    else
                    {
                        if (energyTotal < distTo[row + 1][col - 1])
                        {
                            // Update energy and edge to location
                            distTo[row + 1][col - 1] = energyTotal;
                            edgeTo.get(row + 1).set(col - 1, new int[] {row, col});
                        }
                    }
                }
                //Check and update right -> [row + 1][col + 1]
                if (col < width - 1)
                {
                    double energyTotal = distTo[row][col] + energies.get(row + 1).get(col + 1);
                    if(distTo[row + 1][col + 1] == 0)
                    {
                        // Update energy and edge to location
                        distTo[row + 1][col + 1] = energyTotal;
                        edgeTo.get(row + 1).set(col + 1, new int[] {row, col});
                    }
                    else
                    {
                        if (energyTotal < distTo[row + 1][col + 1])
                        {
                            // Update energy and edge to location
                            distTo[row + 1][col + 1] = energyTotal;
                            edgeTo.get(row + 1).set(col + 1, new int[] {row, col});
                        }
                    }
                }
                
                //Check and update bot -> [row + 1][col]
                double energyTotal = distTo[row][col] + energies.get(row + 1).get(col);
                
                if(distTo[row + 1][col] == 0)
                {
                    // Update energy and edge to location
                    distTo[row + 1][col] = energyTotal;
                    edgeTo.get(row + 1).set(col, new int[] {row, col});
                }
                else
                {
                    if (energyTotal < distTo[row + 1][col])
                    {
                        // Update energy and edge to location
                        distTo[row + 1][col] = energyTotal;
                        //System.out.println(row + ", " + col);
                        edgeTo.get(row + 1).set(col, new int[] {row, col});
                    }
                }
            }
        }
        int leastCol = -1;
        double leastEnergy = Integer.MAX_VALUE;
        for (int i = 0; i < width; i++)
        {
            if (distTo[height - 1][i] < leastEnergy)
            {
                leastEnergy = distTo[height - 1][i];
                leastCol = i;
            }
        }
        int[] seam = new int[height];
        seam[height - 1] = leastCol;
        for (int row = height - 1; row > 0; row--)
        {
            //System.out.println(row + ", " + seam[row]);
            // Get coordinates of from edge of the previous node
            int[] fromEdge = edgeTo.get(row).get(seam[row]);
            seam[row - 1] = fromEdge[1];
        }
        return seam;
    }
 
    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam)
    {
        if (seam == null) throw new IllegalArgumentException();
        if (height <= 1) throw new IllegalArgumentException();
        if (seam.length != width) throw new IllegalArgumentException();
        if (seam[0] < 0)
        {
            throw new IllegalArgumentException();
        }
        if (seam[0] >= height)
        {
            throw new IllegalArgumentException();
        }
        for (int i = 1; i < seam.length; i++)
        {
            if (Math.abs(seam[i] - seam[i - 1]) > 1)
            {
                throw new IllegalArgumentException();
            }
            if (seam[i] < 0)
            {
                throw new IllegalArgumentException();
            }
            if (seam[i] >= height)
            {
                throw new IllegalArgumentException();
            }
        }
        updateHeight(height - 1);
        //System.out.println(height);
        for(int col = 0; col < width; col++)
        {
            // Perform from right to left
            for (int i = seam[col]; i < height; i++)
            {
                rgbs.get(i).set((col), rgbs.get(i + 1).get(col));
                newPic.get(i).set((col), newPic.get(i + 1).get(col));
            }
        }
        rgbs.remove(height);
        newPic.remove(height);
        initateEnergies();
    }
 
    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam)
    {
        if (seam == null) throw new IllegalArgumentException();
        if (seam.length != height) throw new IllegalArgumentException();
        if (width <= 1) throw new IllegalArgumentException();
        if (seam[0] < 0)
        {
            throw new IllegalArgumentException();
        }
        if (seam[0] >= width)
        {
            throw new IllegalArgumentException();
        }
        for (int i = 1; i < seam.length; i++)
        {
            if (Math.abs(seam[i] - seam[i - 1]) > 1)
            {
                throw new IllegalArgumentException();
            }
            if (seam[i] < 0)
            {
                throw new IllegalArgumentException();
            }
            if (seam[i] >= width)
            {
                throw new IllegalArgumentException();
            }
        }
        updateWidth(width - 1);
        for(int row = 0; row < height; row++)
        {
            // Perform from right to left
            rgbs.get(row).remove(seam[row]);
            newPic.get(row).remove(seam[row]);
        }
        initateEnergies();
    }
 
    //  unit testing (optional)
    public static void main(String[] args)
    {
        SeamCarver s = new SeamCarver(new Picture("HJocean.png"));
        
        System.out.println(s.width() + ", " + s.height());
        for (int i = 0; i < s.width(); i++)
        {
            for (int j = 0; j < s.height(); j++)
            {
                System.out.print(s.energy(i,j) + " ");
            }
            System.out.println();
        }
        
        s.removeVerticalSeam(s.findVerticalSeam());
        System.out.println(s.width() + ", " + s.height());
        for (int i = 0; i < s.width(); i++)
        {
            for (int j = 0; j < s.height(); j++)
            {
                System.out.print(s.energy(i,j) + " ");
            }
            System.out.println();
        }
    }

 
 }