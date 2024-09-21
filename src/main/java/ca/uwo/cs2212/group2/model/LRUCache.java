package ca.uwo.cs2212.group2.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Extends a LinkedHashMap to implement a basic LRU Cache
 *
 * @param <K>
 * @param <V>
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {
  private final int maxSize;

  public LRUCache(int maxSize) {
    super(maxSize, 0.75f, true);
    this.maxSize = maxSize;
  }

  @Override
  protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
    return size() > maxSize;
  }
}
