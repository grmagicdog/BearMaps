package bearmaps.proj2ab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * An ArrayHeapMinPQ implementation of ExtrinsicMinPQ.
 *
 * @author Rui Gao
 */
public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    private static class Info {
        private int index;
        private double priority;

        private Info(int i, double p) {
            index = i;
            priority = p;
        }
    }

    private List<T> items;
    private HashMap<T, Info> info;
    private static final int ROOT = 1;

    public ArrayHeapMinPQ() {
        items = new ArrayList<>();
        items.add(null);
        info = new HashMap<>();
        info.put(null, null);
    }

    private int extrinsicCompare(int x, int y) {
        return Double.compare(info.get(items.get(x)).priority, info.get(items.get(y)).priority);
    }

    private int leftChild(int i) {
        return i * 2;
    }

    private int rightChild(int i) {
        return i * 2 + 1;
    }

    private int smallerChild(int i) {
        int left = leftChild(i);
        int right = rightChild(i);
        if (left > size()) {
            return -1;
        } else if (right > size()) {
            return left;
        } else if (extrinsicCompare(left, right) <= 0) {
            return left;
        } else  {
            return right;
        }
    }

    private int parent(int i) {
        return i / 2;
    }

    private int lastItem() {
        return size();
    }

    private void swap(int x, int y) {
        T first = items.get(x);
        T second = items.get(y);
        Info infoT = info.get(first);
        info.replace(first, new Info(info.get(second).index, info.get(first).priority));
        info.replace(second, new Info(infoT.index, info.get(second).priority));
        items.set(x, second);
        items.set(y, first);
    }

    private void swim(int i) {
        if (i <= ROOT) {
            return;
        }
        int parent = parent(i);
        if (extrinsicCompare(i, parent) < 0) {
            swap(i, parent);
            swim(parent);
        }
    }

    private void sink(int i) {
        int smaller = smallerChild(i);
        if (smaller < 0) {
            return;
        }
        if (extrinsicCompare(i, smaller) > 0) {
            swap(i, smaller);
            sink(smaller);
        }
    }

    /**
     * Adds an item with the given priority value. Throws an
     * IllegalArgumentException if item is already present.
     * You may assume that item is never null.
     */
    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException();
        }
        items.add(item);
        info.put(item, new Info(size(), priority));
        swim(size());
    }

    /**
     * Returns true if the PQ contains the given item.
     */
    @Override
    public boolean contains(T item) {
        return info.containsKey(item);
    }

    /**
     * Returns the minimum item. Throws NoSuchElementException if the PQ is empty.
     */
    @Override
    public T getSmallest() {
        if (size() == 0) {
            throw new NoSuchElementException();
        }
        return items.get(ROOT);
    }

    /**
     * Removes and returns the minimum item. Throws NoSuchElementException if the PQ is empty.
     */
    @Override
    public T removeSmallest() {
        T result = getSmallest();
        swap(ROOT, lastItem());
        info.remove(items.remove(lastItem()));
        sink(ROOT);
        return result;
    }

    /**
     * Returns the number of nodes in the PQ.
     */
    @Override
    public int size() {
        return items.size() - 1;
    }

    /**
     * Changes the priority of the given item. Throws NoSuchElementException if the item
     * doesn't exist.
     */
    @Override
    public void changePriority(T item, double priority) {
        int i = info.get(item).index;
        info.replace(item, new Info(i, priority));
        sink(i);
        swim(i);
    }

    void print() {
        printFancyHeapDrawing(items.toArray());
    }

    /** Prints out a drawing of the given array of Objects assuming it
     *  is a heap starting at index 1. You're welcome to copy and paste
     *  code from this method into your code, just make sure to cite
     *  this with the @source tag.
     *
     *  @source Josh Hug
     */
    public static void printFancyHeapDrawing(Object[] items) {
        String drawing = fancyHeapDrawingHelper(items, 1, "");
        System.out.println(drawing);
    }

    /** Recursive helper method for toString.
     *
     *  @source Josh Hug
     */
    private static String fancyHeapDrawingHelper(Object[] items, int index, String soFar) {
        if (index >= items.length || items[index] == null) {
            return "";
        } else {
            String toReturn = "";
            int rightIndex = 2 * index + 1;
            toReturn += fancyHeapDrawingHelper(items, rightIndex, "        " + soFar);
            if (rightIndex < items.length && items[rightIndex] != null) {
                toReturn += soFar + "    /";
            }
            toReturn += "\n" + soFar + items[index] + "\n";
            int leftIndex = 2 * index;
            if (leftIndex < items.length && items[leftIndex] != null) {
                toReturn += soFar + "    \\";
            }
            toReturn += fancyHeapDrawingHelper(items, leftIndex, "        " + soFar);
            return toReturn;
        }
    }
}
