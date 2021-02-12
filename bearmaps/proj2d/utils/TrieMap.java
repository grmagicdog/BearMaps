package bearmaps.proj2d.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public interface TrieMap<V> extends Iterable<String>{
    void put(String key, V value);
    V get(String key);
    boolean containsKey(String key);
    V remove(String key);
    void clear();
    int size();
    Set<String> keySet();
    List<String> keysWithPrefix(String prefix, int n);
    default List<String> keysWithPrefix(String prefix) {
        return keysWithPrefix(prefix, Integer.MAX_VALUE);
    }

    @Override
    default Iterator<String> iterator() {
        return keySet().iterator();
    }
}
