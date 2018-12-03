package com.atolkachev.cache.policies;

import com.atolkachev.cache.TwoLevelCache;
import org.junit.After;
import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class LeastFrequentlyUsedCacheTest {
  private static final String KEY = "key";
  private static final String KEY0 = "key0";
  private static final String KEY1 = "key1";
  private static final String KEY2 = "key2";
  private static final String KEY3 = "key3";
  private static final String KEY4 = "key4";
  private static final String KEY5 = "key5";

  private TwoLevelCache<String, Integer> twoLevelCache;

  @After
  public void clearCache() {
    twoLevelCache.clearCache();
  }

  @Test
  public void testReplaceObjectFromCache() {
    twoLevelCache = new TwoLevelCache<>(2, 2, PolicyType.LEAST_FREQUENTLY_USED);

    IntStream.range(0, 5).forEachOrdered(i -> {
      twoLevelCache.putIntoCache(KEY + i, i);
      assertTrue(twoLevelCache.isObjectContained(KEY + i));
      twoLevelCache.getFromCache(KEY + i);
      if (i == 1) return;                                      // Getting an object with key 'key1' from cache only once
      twoLevelCache.getFromCache(KEY + i);
    });

    assertTrue(twoLevelCache.isObjectContained(KEY0));
    assertTrue(twoLevelCache.isObjectContained(KEY2));
    assertTrue(twoLevelCache.isObjectContained(KEY3));
    assertTrue(twoLevelCache.isObjectContained(KEY4));
    assertFalse(twoLevelCache.isObjectContained(KEY1));        // Replaced from 1st level cache as least frequently used

    twoLevelCache.getFromCache(KEY0);
    twoLevelCache.getFromCache(KEY3);
    twoLevelCache.getFromCache(KEY4);
    twoLevelCache.putIntoCache(KEY5, 5);

    assertTrue(twoLevelCache.isObjectContained(KEY0));
    assertTrue(twoLevelCache.isObjectContained(KEY3));
    assertTrue(twoLevelCache.isObjectContained(KEY4));
    assertTrue(twoLevelCache.isObjectContained(KEY5));
    assertFalse(twoLevelCache.isObjectContained(KEY2));        // Replaced from 2nd level cache as least frequently used
  }

  @Test
  public void testDeleteObjectFailedWhenObjectIsNotContained() {
    twoLevelCache = new TwoLevelCache<>(1, 1, PolicyType.LEAST_FREQUENTLY_USED);

    twoLevelCache.putIntoCache(KEY0, 0);
    twoLevelCache.putIntoCache(KEY1, 1);

    assertTrue(twoLevelCache.isObjectContained(KEY0));
    assertTrue(twoLevelCache.isObjectContained(KEY1));
    assertEquals(2, twoLevelCache.getCacheSize());

    twoLevelCache.deleteFromCache(KEY2);

    assertTrue(twoLevelCache.isObjectContained(KEY0));
    assertTrue(twoLevelCache.isObjectContained(KEY1));
    assertEquals(2, twoLevelCache.getCacheSize());
  }
}