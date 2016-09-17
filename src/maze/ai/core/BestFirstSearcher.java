package maze.ai.core;
import java.util.*;


public class BestFirstSearcher<T extends BestFirstObject<T>> {
    private ArrayList<T> solution;
    private int depth, numNodes;
    private BestFirstHeuristic<T> h;
    private boolean solutionFound;
    private int heu;
    
    private final static boolean debug = false;
    
    public BestFirstSearcher(BestFirstHeuristic<T> bfh) {
        h = bfh; 
        reset();
    }
    
    public void solve(T start, T target) {
        reset();
        
        Set<T> visited = new HashSet<T>();
        SearchNode best = new SearchNode(null, start, target, heu);
        //Changed Queue to PriorityQueue, same with ArrayList
        PriorityQueue<SearchNode> openList = new PriorityQueue<SearchNode>();
        openList.add(best);
        
        solutionFound = false;
        while (openList.size() > 0 && !solutionFound) {
            best = openList.poll();
            if (debug) {System.out.println("best: " + best.getObject());}
            if (!visited.contains(best.getObject())) {
                visited.add(best.getObject());
                if (best.getObject().achieves(target)) {
                    solutionFound = true;
                } else {
                	addSuccessors(best, openList, target);
                }
            }
        }
        
        if (solutionFound) {
            reconstructMoves(best);
        }
    }
    
    private void addSuccessors(SearchNode best, PriorityQueue<SearchNode> openList, T target) {
        for (T p: best.getObject().getSuccessors()) {
        	heu = h.getDistance(p, target);
            numNodes++;
            SearchNode newNode = new SearchNode(best, p, target, heu);
            depth = Math.max(newNode.getDepth(), depth);
            openList.add(newNode);
        }    	
    }
    
    private void reconstructMoves(SearchNode searcher) { 
        while (searcher != null) {
            solution.add(searcher.getObject());
            searcher = searcher.getParent();
        }
        
        for (int i = 0; i < solution.size() / 2; ++i) {
            T temp = solution.get(i);
            int other = solution.size() - i - 1;
            solution.set(i, solution.get(other));
            solution.set(other, temp);
        }
    }
    
    private class SearchNode implements Comparable<SearchNode> {
        private int depth;
        private T node;
        private SearchNode parent;
        private T goal;
        private int f;
        
        public SearchNode(SearchNode parent, T pNode, T goal, int heu) {
            node = pNode;
            this.parent = parent;
            this.goal = goal;
            depth = (parent == null) ? 0 : parent.depth + 1; 
            f = heu + depth;
            
        }
        
        public int getDepth() {return depth;}
        
        public T getObject() {return node;}
        
        public SearchNode getParent() {return parent;}
        
        //Made SearchNode implement Comparable and implemented the 
        @Override
		public int compareTo(SearchNode myNode) {
        	return this.f - myNode.f;
			
			}
			
		}
    
    
    private void reset() {
        solution = new ArrayList<T>();
        depth = -1;
        numNodes = 0;
        solutionFound = false;
    }
    
    // Pre: 0 <= n <= getMaxStep(); success()
    public T get(int n) {
        return solution.get(n);
    }
    
    public boolean success() {return solutionFound;}

    // Pre: success()
    public int numSteps() {return solution.size();}
    
    // Pre: success()
    public int getMaxDepth() {return depth;}
    
    // Pre: success()
    public int getNumNodes() {return numNodes;}
    
    // Pre: success()
    public double getBranchingFactor(double maxError) {
        double lo = 0;
        double hi = (double)numNodes / (double)depth;
        double error = 0;
        double bGuess = 0;
        do {
            bGuess = (lo + hi) / 2;
            error = computeError(bGuess);
            if (error > 0) {
                hi = bGuess;
            } else {
                lo = bGuess;
            }
        } while (Math.abs(error) > maxError);
        return bGuess;
    }
    
    private double computeError(double bGuess) {
        double sum = 0;
        for (int d = 1; d <= depth; ++d) {
            sum += Math.pow(bGuess, d);
        }
        return sum - numNodes;
    }
}
