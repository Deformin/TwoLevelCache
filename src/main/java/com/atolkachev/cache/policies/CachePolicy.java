package com.atolkachev.cache.policies;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public abstract class CachePolicy<K> {
  private final Map<K, Long> repository;
  private final TreeMap<K, Long> sortedRepository;

  CachePolicy() {
    this.repository = new HashMap<>();
    this.sortedRepository = new TreeMap<>(new MyComparator<>(repository));
  }

  Map<K, Long> getRepository() {
    return repository;
  }

  TreeMap<K, Long> getSortedRepository() {
    return sortedRepository;
  }

  public abstract void putObject(K key);

  public boolean isObjectContained(K key) {
    return repository.containsKey(key);
  }

  public void deleteObject(K key) {
    if (isObjectContained(key)) {
      repository.remove(key);
    }
  }

  public K getKeyToReplace() {
    sortedRepository.clear();
    sortedRepository.putAll(repository);
    return sortedRepository.firstKey();
  }

  public void clear() {
    repository.clear();
    sortedRepository.clear();
  }
}
