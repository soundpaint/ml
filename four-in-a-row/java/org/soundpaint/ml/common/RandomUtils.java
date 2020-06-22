/*
 * @(#)RandomUtils.java 1.00 20/04/22
 *
 * Copyright (C) 2020 Jürgen Reuter
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.soundpaint.ml.common;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class RandomUtils
{
  private static final Random RANDOM_INSTANCE = new Random();

  /**
   * Return singleton instance of java.util.Random object
   * to ensure global uniform distribution.
   */
  public static Random getRandom() { return RANDOM_INSTANCE; }

  /**
   * Select a set of #<code>count</code> integers with uniform
   * distribution from the range between 0 (inclusive) and
   * <code>bound</code> (exclusive), ensuring that there are no
   * duplicates in the result.
   */
  public static Set<Integer> createSelection(final int bound, final int count)
  {
    if (count > bound) {
      throw new IllegalArgumentException("count may not be greater than bound");
    }
    final Random random = getRandom();
    final HashMap<Integer, Integer> selected = new HashMap<Integer, Integer>();
    for (int select = 0; select < count; select++) {
      int value = random.nextInt(bound - select);
      while (selected.containsKey(value)) {
        value = selected.get(value);
      }
      selected.put(value, bound - select - 1);
    }
    return selected.keySet();
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
