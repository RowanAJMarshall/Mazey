package maze.ai.heuristics;

import maze.ai.core.BestFirstHeuristic;
import maze.core.MazeExplorer;

public class NoSQRT implements BestFirstHeuristic<MazeExplorer> {

	@Override
	public int getDistance(MazeExplorer node, MazeExplorer goal) {
		
		double triangleX = Math.abs(node.getLocation().X() - goal.getLocation().X());
		double triangleY = Math.abs(node.getLocation().Y() - goal.getLocation().Y());
		
		return (int) ((triangleX*triangleX)*(triangleY*triangleY));
	}
}
