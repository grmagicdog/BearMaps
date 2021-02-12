package bearmaps.test;

import bearmaps.proj2d.utils.HashTrieMap;
import bearmaps.proj2d.utils.TrieMap;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Set;

import static org.junit.Assert.*;

public class TestHashTrieMap {

    @Test
    public void testSanity() {
        TrieMap<Integer> trie = new HashTrieMap<>();
        String[] wordsIn = {"cat","dog", "doge", "apple", "application",
                "magicdog", "magic", "at", "yes", "apply"};
        String[] wordsOut = {"ca", "og", "oge", "appl"};
        for (String w : wordsIn) {
            trie.put(w, w.hashCode());
        }
        assertEquals(wordsIn.length, trie.size());
        Set<String> keySet = trie.keySet();
        for (String w : wordsIn) {
            assertTrue(trie.containsKey(w));
            assertTrue(keySet.contains(w));
            assertEquals((Object) w.hashCode(), trie.get(w));
        }
        for (String w : wordsOut) {
            assertFalse(trie.containsKey(w));
            assertFalse(keySet.contains(w));
        }
        trie.get("dog");
        trie.get("dog");
        trie.get("doge");
        assertEquals((Object) "dog".hashCode(), trie.remove("dog"));
        trie.get("apple");
        trie.get("application");
        trie.get("application");
        LinkedList<String> appPrefix = new LinkedList<>();
        appPrefix.addFirst("apply");
        appPrefix.addFirst("apple");
        appPrefix.addFirst("application");
        assertEquals(appPrefix, trie.keysWithPrefix("app"));
    }
}
