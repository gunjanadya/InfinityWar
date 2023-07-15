package avengers;
import java.util.*;
/**
 * 
 * Using the Adjacency Matrix of n vertices and starting from Earth (vertex 0), 
 * modify the edge weights using the functionality values of the vertices that each edge 
 * connects, and then determine the minimum cost to reach Titan (vertex n-1) from Earth (vertex 0).
 * 
 * Steps to implement this class main method:
 * 
 * Step 1:
 * LocateTitanInputFile name is passed through the command line as args[0]
 * Read from LocateTitanInputFile with the format:
 *    1. g (int): number of generators (vertices in the graph)
 *    2. g lines, each with 2 values, (int) generator number, (double) funcionality value
 *    3. g lines, each with g (int) edge values, referring to the energy cost to travel from 
 *       one generator to another 
 * Create an adjacency matrix for g generators.
 * 
 * Populate the adjacency matrix with edge values (the energy cost to travel from one 
 * generator to another).
 * 
 * Step 2:
 * Update the adjacency matrix to change EVERY edge weight (energy cost) by DIVIDING it 
 * by the functionality of BOTH vertices (generators) that the edge points to. Then, 
 * typecast this number to an integer (this is done to avoid precision errors). The result 
 * is an adjacency matrix representing the TOTAL COSTS to travel from one generator to another.
 * 
 * Step 3:
 * LocateTitanOutputFile name is passed through the command line as args[1]
 * Use Dijkstra’s Algorithm to find the path of minimum cost between Earth and Titan. 
 * Output this number into your output file!
 * 
 * Note: use the StdIn/StdOut libraries to read/write from/to file.
 * 
 *   To read from a file use StdIn:
 *     StdIn.setFile(inputfilename);
 *     StdIn.readInt();
 *     StdIn.readDouble();
 * 
 *   To write to a file use StdOut (here, minCost represents the minimum cost to 
 *   travel from Earth to Titan):
 *     StdOut.setFile(outputfilename);
 *     StdOut.print(minCost);
 *  
 * Compiling and executing:
 *    1. Make sure you are in the ../InfinityWar directory
 *    2. javac -d bin src/avengers/*.java
 *    3. java -cp bin avengers/LocateTitan locatetitan.in locatetitan.out
 * 
 * @author Yashas Ravi
 * 
 */

public class LocateTitan {
	
    public static void main (String [] args) {
        if ( args.length < 2 ) {
            StdOut.println("Execute: java LocateTitan <INput file> <OUTput file>");
            return;
        }

        StdIn.setFile(args[0]);
        StdOut.setFile(args[1]);
        //number of generators
        int l = StdIn.readInt();

        //store functionality values
        double [] func = new double [l];
        for(int i = 0; i < l; i++){
            func[StdIn.readInt()] = StdIn.readDouble();

        }

        //store given adjacency matrix
        int[][] adj = new int[l][l];
        for(int i = 0; i < l; i++){
            for(int j = 0; j < l; j++){
                adj[i][j] = StdIn.readInt();
            }
        }

        //update given adjacency matrix to 
        //include functionality of generators
        for(int i = 0; i < l; i++){
            for(int j = 0; j < l; j++){
                double combinedFunc = func[i]*func[j];
                adj[i][j] = (int)((double)adj[i][j]/combinedFunc);
            }
        }
        int [][] adj2 = new int[adj.length][adj.length];
        for(int i = 0; i < adj.length; i++){
            for(int j = 0; j < adj.length; j++){
                if(adj[i][j] == 0 && i != j){
                    adj2[i][j] = -1;
                }else{
                    adj2[i][j] = adj[i][j];
                }
            }
        }

        //initialize dijkstra alg
        int []     minCost      = new int[l];
        boolean [] DijkstraSet  = new boolean[l];
        boolean [] settledNodes = new boolean[l];
        for(int i = 0; i < minCost.length; i++){
            if(i == 0){
                minCost[i] = 0;
                DijkstraSet[i] = true;
                settledNodes[i] = false;
            }
            else{
                minCost[i] = Integer.MAX_VALUE;
                DijkstraSet[i]  = false;
                settledNodes[i] = false;
            }
        }
        int currentSource = 0;

        while(hasNodes(DijkstraSet)){

            //take current source out of nodes to be explored, set the cost to be added to
            int currentCost = minCost[currentSource];
            DijkstraSet[currentSource]  = false;
            settledNodes[currentSource] = true;

            //set neighbors' min costs
            for(int i = 0; i < l; i++){
                if(adj[currentSource][i] != 0 && !settledNodes[i]){
                    DijkstraSet[i] = true;
                    if(minCost[i] > currentCost + adj[currentSource][i]){
                        minCost[i] = currentCost + adj[currentSource][i];
                    }
                }
            }

            //select next currentSource based on minCost in unsettled set
            currentSource = -1;
            for(int i = 0; i < l; i ++){
                if(DijkstraSet[i]){
                    if(currentSource == -1){
                        currentSource = i;
                    }else if (minCost[i] < minCost[currentSource]){
                        currentSource = i;
                    }
                }
            }
        }
        StdOut.print(minCost[l-1]);
    }
    public static boolean hasNodes(boolean[] a){
        for(int i = 0; i < a.length; i++){
            if(a[i]){
                return true;
            }
        }
        return false;
    }
}