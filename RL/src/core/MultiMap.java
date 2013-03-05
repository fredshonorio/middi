package core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * HashMap that allows multiple values with the same key. Nulls are not allowed
 * has key, but are possible has value.
 */

public class MultiMap<K, V> {

    private final HashMap<K, LinkedList<V>> map;

    public MultiMap() {
	map = new HashMap<K, LinkedList<V>>();
    }

    public MultiMap(int initialCapacity) {
	map = new HashMap<K, LinkedList<V>>(initialCapacity);
    }

    /**
     * Associates the value with the given key, if the key already has values,
     * it is appended.
     * 
     * @param key
     *            The key
     * @param value
     *            The value
     * @return true if the value was the first associated with the key
     */
    public boolean put(K key, V value) {
	assert key != null;

	LinkedList<V> list = map.get(key);

	boolean first;
	if (list == null) {
	    list = new LinkedList<V>();
	    list.add(value);

	    map.put(key, list);

	    first = true;
	} else {
	    list.add(value);
	    first = false;
	}

	assert map.containsKey(key);

	return first;
    }

    /**
     * Places the key in the map, without any elements.
     * 
     * @param key
     *            The key
     * @return false.
     */
    public boolean put(K key) {
	assert key != null;
	assert !map.containsKey(key);

	LinkedList<V> list = map.get(key);

	if (list == null) {
	    list = new LinkedList<V>();
	    map.put(key, list);
	}

	assert map.containsKey(key);

	return false;
    }

    /**
     * Associates the values with the given key.
     * 
     * @param key
     *            The key
     * @param values
     *            The list of values
     * @return true if the first value was the first associated with the key.
     */
    public boolean put(K key, List<V> values) {
	assert key != null;
	assert values != null;
	assert !values.isEmpty();

	LinkedList<V> list = map.get(key);
	boolean first;

	if (list == null) {
	    list = new LinkedList<V>();
	    map.put(key, list);
	    append(key, values);
	    first = true;
	} else {
	    append(key, values);
	    first = false;
	}

	assert map.containsKey(key);

	return first;
    }

    /**
     * Appends values to a key.
     * 
     * @param key
     *            The key.
     * @param values
     *            The values
     */
    private void append(K key, List<V> values) {
	assert map.containsKey(key);

	LinkedList<V> list = map.get(key);

	for (V v : values) {
	    list.add(v);
	}
    }

    /**
     * Checks if a key exists in the map.
     * 
     * @param key
     *            The key.
     * @return True if the key exists, false otherwise.
     */
    public boolean containsKey(K key) {
	return map.containsKey(key);
    }

    /**
     * Returns the set of keys added to the map.
     * 
     * @return The set of keys.
     */
    public Set<K> keySet() {
	return map.keySet();
    }

    /**
     * Clears the key and the values associated with it.
     * 
     * @param key
     *            The key.
     */
    public void clear(K key) {
	assert map.containsKey(key);

	map.remove(key);

	assert !map.containsKey(key);
    }

    /**
     * Returns the values associated with the key.
     * 
     * @param key
     *            The key.
     * @return The values.
     */
    public LinkedList<V> get(K key) {
	assert map.containsKey(key);

	return map.get(key);
    }

    /**
     * Clears all the keys and values from the map.
     */
    public void clear() {
	map.clear();
    }
}
