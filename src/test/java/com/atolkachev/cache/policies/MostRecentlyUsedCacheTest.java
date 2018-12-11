package com.atolkachev.cache.policies;

import com.atolkachev.cache.TwoLevelCache;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MostRecentlyUsedCacheTest {
  private static final String KEY = "key";
  private static final String KEY0 = "key0";
  private static final String KEY1 = "key1";
  private static final String KEY2 = "key2";
  private static final String KEY3 = "key3";
  private static final String KEY4 = "key4";
  private static final Integer VALUE4 = 4;

  private TwoLevelCache<String, Integer> twoLevelCache;

  @Before
  public void setTwoLevelCache() {
    twoLevelCache = new TwoLevelCache<>(2, 2, PolicyType.MOST_RECENTLY_USED);
  }

  @After
  public void clearCache() {
    twoLevelCache.clearCache();
  }

  @Test
  public void testReplaceObjectFromCache() {
    IntStream.range(0, 4).forEachOrdered(i -> {
      twoLevelCache.putIntoCache(KEY + i, i);
      assertTrue(twoLevelCache.isObjectContained(KEY + i));
      twoLevelCache.getFromCache(KEY + i);
    });

    twoLevelCache.putIntoCache(KEY4, VALUE4);

    assertTrue(twoLevelCache.isObjectContained(KEY0));
    assertTrue(twoLevelCache.isObjectContained(KEY1));
    assertTrue(twoLevelCache.isObjectContained(KEY2));
    assertTrue(twoLevelCache.isObjectContained(KEY4));
    assertFalse(twoLevelCache.isObjectContained(KEY3)); // Replaced from 2nd level cache as most recently used
  }
}
