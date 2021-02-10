package bearmaps.proj2c;

import bearmaps.proj2ab.ArrayHeapMinPQ;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.*;

/**
 * An implementation of SShortestPathsSolver using Memory-Optimizing A* algorithm.
 *
 * @author Rui Gao
 */
public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    private final AStarGraph<Vertex> graph;
    private final Vertex s;
    private final Vertex goal;
    private final Map<Vertex, Double> distTo;
    private final Map<Vertex, Vertex> edgeTo;
    private final ArrayHeapMinPQ<Vertex> fringe;
    private final double elapsedTime;
    private int numDequeue;

    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        Stopwatch sw = new Stopwatch();
        graph = input;
        s = start;
        goal = end;
        distTo = new HashMap<>();
        distTo.put(s, 0.0);
        edgeTo = new HashMap<>();
        fringe = new ArrayHeapMinPQ<>();
        fringe.add(s, heuristic(s));
        numDequeue = 0;
        while (sw.elapsedTime() < timeout && fringe.size() > 0 && !fringe.getSmallest().equals(goal)) {
            Vertex p = fringe.removeSmallest();
            numDequeue += 1;
            for (WeightedEdge<Vertex> e : graph.neighbors(p)) {
                relax(e);
            }
        }
        elapsedTime = sw.elapsedTime();
    }

    private void relax(WeightedEdge<Vertex> e) {
        Vertex p = e.from();
        Vertex q = e.to();
        double w = e.weight();
        double newDist = distTo.get(p) + w;
        if (!distTo.containsKey(q) || newDist < distTo.get(q)) {
            distTo.put(q, newDist);
            edgeTo.put(q, p);
            if (fringe.contains(q)) {
                fringe.changePriority(q, newDist + heuristic(q));
            } else {
                fringe.add(q, newDist + heuristic(q));
            }
        }
    }

    private double heuristic(Vertex v) {
        return graph.estimatedDistanceToGoal(v, goal);
    }

    @Override
    public SolverOutcome outcome() {
        if (fringe.size() == 0) {
            return SolverOutcome.UNSOLVABLE;
        } else if (fringe.getSmallest().equals(goal)) {
            return SolverOutcome.SOLVED;
        } else {
            return SolverOutcome.TIMEOUT;
        }
    }

    @Override
    public List<Vertex> solution() {
        List<Vertex> result = new ArrayList<>();
        if (outcome() == SolverOutcome.SOLVED) {
            for (Vertex p = goal; !p.equals(s); p = edgeTo.get(p)) {
                result.add(p);
            }
            result.add(s);
            Collections.reverse(result);
        }
        return result;
    }

    @Override
    public double solutionWeight() {
        return distTo.get(goal);
    }

    @Override
    public int numStatesExplored() {
        return numDequeue;
    }

    @Override
    public double explorationTime() {
        return elapsedTime;
    }
}
