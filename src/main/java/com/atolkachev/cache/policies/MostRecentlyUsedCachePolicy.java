package com.atolkachev.cache.policies;

public class MostRecentlyUsedCachePolicy<K> extends CachePolicy<K> {
  @Override
  public void putObject(K key) {
    getRepository().put(key, System.nanoTime());
  }

  @Override
  public K getKeyToReplace() {
    getSortedRepository().clear();
    getSortedRepository().putAll(getRepository());
    return getSortedRepository().lastKey();
  }
}
