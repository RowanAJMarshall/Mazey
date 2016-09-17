package maze.ai.heuristics;

import maze.ai.core.BestFirstHeuristic;
import maze.core.MazeExplorer;

public class Manhattan implements BestFirstHeuristic<MazeExplorer> {

	@Override
	public int getDistance(MazeExplorer node, MazeExplorer goal) {
		return node.getLocation().getManhattanDist(goal.getLocation());
	}
}
