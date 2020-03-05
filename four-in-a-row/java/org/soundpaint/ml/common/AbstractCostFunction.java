/*
 * @(#)AbstractCostFunction.java 1.00 20/03/06
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

import java.util.List;

public abstract class AbstractCostFunction implements CostFunction
{
  private final String id;
  protected final List<Double> targetValues;
  protected final int size;
  protected final double reverseSize;

  private AbstractCostFunction()
  {
    throw new UnsupportedOperationException("unsupported default constructor");
  }

  public AbstractCostFunction(final String id, final List<Double> targetValues)
  {
    if (id == null) { throw new NullPointerException("id"); }
    if (targetValues == null) {
      throw new NullPointerException("targetValues");
    }
    if (targetValues.size() == 0) {
      throw new IllegalArgumentException("empty targetValues");
    }
    for (final Double targetValue : targetValues) {
      if (targetValue == null) {
        throw new IllegalArgumentException("list of target values contains null");
      }
    }
    this.id = id;
    this.targetValues = targetValues;
    size = targetValues.size();
    reverseSize = 1.0 / size;
  }

  public String getId()
  {
    return id;
  }

  public String toString()
  {
    return "cost function " + id;
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
