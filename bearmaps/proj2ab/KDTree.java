package bearmaps.proj2ab;

import java.util.List;

/**
 * A KDTree implementation of PointSet, which has a fast closest method.
 *
 * @author Rui Gao
 */
public class KDTree implements PointSet {

    private static class Node implements Comparable<Point> {
        private static final int DIMENSION = 2;

        private final Point point;
        private Node left = null;
        private Node right = null;
        private int k = 0;

        private Node(Point point) {
            this.point = point;
        }

        private void setLeft(Node left) {
            left.k = nextK();
            this.left = left;
        }

        private void setRight(Node right) {
            right.k = nextK();
            this.right = right;
        }

        private int nextK() {
            return (k + 1) % DIMENSION;
        }

        @Override
        public int compareTo(Point other) {
            if (k == 0) {
                return Double.compare(point.getX(), other.getX());
            } else {
                return Double.compare(point.getY(), other.getY());
            }
        }

        private double minOtherSide(Point goal) {
            if (k == 0) {
                return Math.pow(point.getX() - goal.getX(), 2);
            } else {
                return Math.pow(point.getY() - goal.getY(), 2);
            }
        }

        private Node sameSide(Point goal) {
            if (compareTo(goal) >= 0) {
                return left;
            } else {
                return right;
            }
        }

        private Node otherSide(Point goal) {
            if (compareTo(goal) >= 0) {
                return right;
            } else {
                return left;
            }
        }
    }

    private Node root = null;

    public KDTree(Iterable<Point> points) {
        for (Point p : points) {
            add(p);
        }
    }

    private void add(Point point) {
        root = addHelper(root, point);
    }

    private Node addHelper(Node node, Point point) {
        if (node == null) {
            return new Node(point);
        } else if (node.point.equals(point)) {
            return node;
        }
        if (node.compareTo(point) >= 0) {
            node.setLeft(addHelper(node.left, point));
        } else {
            node.setRight(addHelper(node.right, point));
        }
        return node;
    }

    @Override
    public Point nearest(double x, double y) {
        return nearestHelper(root, null, new Point(x, y));
    }

    /** Return the nearest point of NODE to the INPUT point which has a distance less than MIN. */
    private Point nearestHelper(Node node, Point nearest, Point goal) {
        if (node == null) {
            return nearest;
        }
        double dist = Point.distance(node.point, goal);
        if (dist < nullSafeDistance(nearest, goal)) {
            nearest = node.point;
        }
        nearest = nearestHelper(node.sameSide(goal), nearest, goal);
        if (node.minOtherSide(goal) < nullSafeDistance(nearest, goal)) {
            nearest = nearestHelper(node.otherSide(goal), nearest, goal);
        }
        return nearest;
    }

    private double nullSafeDistance(Point p1, Point p2) {
        if (p1 == null || p2 == null) {
            return Double.POSITIVE_INFINITY;
        }
        return Point.distance(p1, p2);
    }
}
