/*
 * Copyright (C) 2011 Clearspring Technologies, Inc. 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.infinispan.stats.topK;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Eric Vlaanderen
 */
public class ScoredItem<T> implements Comparable<ScoredItem> {
   private final AtomicLong error;
   private final AtomicLong count;
   private final AtomicBoolean newItem;
   private final T item;

   public ScoredItem(final T item, final long count, final long error) {
      this.item = item;
      this.error = new AtomicLong(error);
      this.count = new AtomicLong(count);
      this.newItem = new AtomicBoolean(true);
   }

   public ScoredItem(final T item, final long count) {
      this(item, count, 0L);
   }

   public long addAndGetCount(final long delta) {
      return this.count.addAndGet(delta);
   }

   public long getError() {
      return error.get();
   }

   public void setError(final long newError) {
      this.error.set(newError);
   }

   public T getItem() {
      return item;
   }

   public boolean isNewItem() {
      return newItem.get();
   }

   public void setNewItem(final boolean newItem) {
      this.newItem.set(newItem);
   }

   public long getCount() {
      return count.get();
   }

   @Override
   public int compareTo(final ScoredItem o) {
      return Long.valueOf(o.count.get()).compareTo(count.get());
   }

   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("Value: ");
      sb.append(item);
      sb.append(", Count: ");
      sb.append(count);
      sb.append(", Error: ");
      sb.append(error);
      sb.append(", object: ");
      sb.append(super.toString());
      return sb.toString();
   }
}
