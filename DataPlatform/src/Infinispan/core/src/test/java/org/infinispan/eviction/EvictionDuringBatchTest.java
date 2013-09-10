/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other
 * contributors as indicated by the @author tags. All rights reserved.
 * See the copyright.txt in the distribution for a full listing of
 * individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.infinispan.eviction;

import org.infinispan.AdvancedCache;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.test.SingleCacheManagerTest;
import org.infinispan.test.fwk.CleanupAfterMethod;
import org.infinispan.test.fwk.TestCacheManagerFactory;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Mircea Markus
 * @author anistor@redhat.com
 * @since 5.1
 */
@Test (groups = "functional", testName = "eviction.EvictionDuringBatchTest")
@CleanupAfterMethod
public class EvictionDuringBatchTest extends SingleCacheManagerTest {

   protected EmbeddedCacheManager createCacheManager() throws Exception {
      ConfigurationBuilder cfgBuilder = new ConfigurationBuilder();
      cfgBuilder.eviction().strategy(EvictionStrategy.LRU).maxEntries(128) // 128 max entries
            .expiration().wakeUpInterval(100L)
            .locking().useLockStriping(false) // to minimize chances of deadlock in the unit test
            .invocationBatching().enable(true);
      EmbeddedCacheManager cm = TestCacheManagerFactory.createCacheManager(cfgBuilder);
      cache = cm.getCache();
      cache.addListener(new BaseEvictionFunctionalTest.EvictionListener());
      return cm;
   }

   public void testEvictionDuringBatchOperations() throws Exception {
      AdvancedCache<Object,Object> advancedCache = cache.getAdvancedCache();
      for (int i = 0; i < 512; i++) {
         advancedCache.startBatch();
         cache.put("key-" + (i + 1), "value-" + (i + 1), 1, TimeUnit.MINUTES);
         advancedCache.endBatch(true);
      }
      Thread.sleep(1000); // sleep long enough to allow the thread to wake-up

      int cacheSize = cache.size();
      assertTrue("no data in cache! all state lost?", cacheSize != 0);
      assertTrue("cache size too big: " + cacheSize, cacheSize < 512);
   }

   public void testEvictInBatch() throws Exception {
      cache().put("myKey", "myValue");

      cache().getAdvancedCache().startBatch();
      //this should execute non-transactionally despite the batch transaction and should not fail as in https://issues.jboss.org/browse/ISPN-2845
      cache().evict("myKey");
      cache().getAdvancedCache().endBatch(true);

      assertFalse(cache().containsKey("myKey"));
   }
}
