package bearmaps.proj2d.utils;

import bearmaps.proj2ab.ArrayHeapMinPQ;
import bearmaps.proj2ab.ExtrinsicMinPQ;

import java.util.*;

/**
 * A Hash Table based implementation of TrieMap.
 *
 * @author Rui Gao
 */
public class HashTrieMap<V> implements TrieMap<V> {

    private class Node {
        private boolean isKey;
        private Map<Character, Node> next;
        private V v;
        private int score;
        private int best;

        private Node() {
            isKey = false;
            next = new HashMap<>();
            v = null;
            score = 0;
            best = 0;
        }

        private Node put(String key, V value, int i) {
            if (i >= key.length()) {
                isKey = true;
                v = value;
            } else {
                char c = key.charAt(i);
                if (next.containsKey(c)) {
                    next.get(c).put(key, value, i + 1);
                } else {
                    next.put(c, new Node().put(key, value, i + 1));
                }
            }
            return this;
        }

        private Node getNode(String key, int i, boolean incrementScore) {
            if (i >= key.length()) {
                if (incrementScore && isKey) {
                    score += 1;
                    updateBest(score);
                }
                return this;
            }
            char c = key.charAt(i);
            if (next.containsKey(c)) {
                Node result = next.get(c).getNode(key, i + 1, incrementScore);
                if (result != null) {
                    updateBest(result.best);
                }
                return result;
            }
            return null;
        }

        private void updateBest(int value) {
            if (best < value) {
                best = value;
            }
        }

        private void updateBest() {
            best = score;
            for (Map.Entry<Character, Node> entry : next.entrySet()) {
                updateBest(entry.getValue().best);
            }
        }

        private Tuple<Node, Boolean> removeNode(String key, int i) {
            if (i >= key.length()) {
                if (isLeaf()) {
                    return new Tuple<>(this, true);
                } else if (isKey) {
                    isKey = false;
                    score = 0;
                    updateBest();
                    return new Tuple<>(this, false);
                }
                return null;
            }
            char c = key.charAt(i);
            if (isLeaf() || !next.containsKey(c)) {
                return null;
            }
            Tuple<Node, Boolean> result = next.get(c).removeNode(key, i + 1);
            if (result != null) {
                updateBest();
                boolean prune = result.getSecond();
                if (prune) {
                    next.remove(c);
                    result.setSecond(!isKey && isLeaf());
                }
            }
            return result;
        }

        private boolean isLeaf() {
            return next.isEmpty();
        }

        private Set<String> collect() {
            return collectHelper("", new HashSet<>());
        }

        private Set<String> collectHelper(String prefix, Set<String> keysSoFar) {
            if (isLeaf()) {
                if (isKey) {
                    keysSoFar.add(prefix);
                }
                return keysSoFar;
            }
            if (isKey) {
                keysSoFar.add(prefix);
            }
            for (Map.Entry<Character, Node> entry : next.entrySet()) {
                entry.getValue().collectHelper(prefix + entry.getKey(), keysSoFar);
            }
            return keysSoFar;
        }

        private List<String> keysWithPrefix(String prefix, int n) {
            ExtrinsicMinPQ<String> keyScore = new ArrayHeapMinPQ<>();
            ExtrinsicMinPQ<Tuple<String, Node>> nextBest = new ArrayHeapMinPQ<>();
            nextBest.add(new Tuple<>(prefix, this), -best);
            while (nextBest.size() != 0) {
                Tuple<String, Node> p = nextBest.removeSmallest();
                String prefixSoFar = p.getFirst();
                Node node = p.getSecond();
                if (keyScore.size() >= n && node.best <= keyScore.getSmallestPriority()) {
                    break;
                }
                if (node.isKey) {
                    keepNAdder(keyScore, prefixSoFar, node.score, n);
                }
                for (Map.Entry<Character, Node> entry : node.next.entrySet()) {
                    String first = prefixSoFar + entry.getKey();
                    Node second = entry.getValue();
                    nextBest.add(new Tuple<>(first, second), -second.best);
                }
            }
            return pqToList(keyScore);
        }

        private <T> List<T> pqToList(ExtrinsicMinPQ<T> pq) {
            LinkedList<T> result = new LinkedList<>();
            while (pq.size() != 0) {
                result.addFirst(pq.removeSmallest());
            }
            return result;
        }

        private <T> void keepNAdder(ExtrinsicMinPQ<T> pq, T item, double priority,  int n) {
            pq.add(item, priority);
            if (pq.size() > n) {
                pq.removeSmallest();
            }
        }
    }

    private int size;
    private Node root;

    public HashTrieMap() {
        size = 0;
        root = new Node();
    }


    @Override
    public void put(String key, V value) {
        root.put(key, value, 0);
        size += 1;
    }


    @Override
    public V get(String key) {
        Node n = root.getNode(key, 0, true);
        if (n == null) {
            return null;
        }
        return n.v;
    }

    @Override
    public boolean containsKey(String key) {
        Node n = root.getNode(key, 0, false);
        return n != null && n.isKey;
    }

    @Override
    public V remove(String key) {
        Tuple<Node, Boolean> result = root.removeNode(key, 0);
        if (result == null) {
            return null;
        }
        return result.getFirst().v;
    }

    @Override
    public void clear() {
        root = new Node();
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Set<String> keySet() {
        return root.collect();
    }

    @Override
    public List<String> keysWithPrefix(String prefix, int n) {
        Node start = root.getNode(prefix, 0, false);
        if (start != null) {
            return start.keysWithPrefix(prefix, n);
        }
        return new LinkedList<>();
    }
}
