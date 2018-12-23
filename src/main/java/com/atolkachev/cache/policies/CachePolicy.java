package com.atolkachev.cache.policies;

import java.util.*;

public abstract class CachePolicy<K> {
  private final Map<K, Long> repository;
  private final SortedMap<K, Long> sortedRepository;

  CachePolicy() {
    this.repository = new HashMap<>();
    this.sortedRepository = new TreeMap<>(new MyComparator<>(repository));
  }

  Map<K, Long> getRepository() {
    return repository;
  }

  SortedMap<K, Long> getSortedRepository() {
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
