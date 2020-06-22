/*
 * @(#)BasicSampleFunction.java 1.00 20/05/03
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

import java.util.Random;
import java.util.function.Function;

public enum BasicSampleFunction implements SampleFunction
{
  ONES("ones")
  {
    public Double apply(final Void __)
    {
      return 1.0;
    }
  },

  RANDOM_UNIFORM("random uniform")
  {
    public Double apply(final Void __)
    {
      final Random random = RandomUtils.getRandom();
      return random.nextDouble();
    }
  },

  RANDOM_NORMAL("random normal")
  {
    static final double TWO_PI = 2.0 * Math.PI;
    private double z1 = 0.0;
    private boolean needDraw = false;

    /**
     * Box-Müller Transform
     */
    public Double apply(final Void __)
    {
      final Random random = RandomUtils.getRandom();

      needDraw = !needDraw;
      if (!needDraw)
        return z1;

      final double u1 = 1.0 - random.nextDouble(); // exclude 0.0
      final double u2 = random.nextDouble();

      final double z0 = Math.sqrt(-2.0 * Math.log(u1)) * Math.cos(TWO_PI * u2);
      z1 = Math.sqrt(-2.0 * Math.log(u1)) * Math.sin(TWO_PI * u2);

      return z0;
    }
  };

  private final String id;

  private BasicSampleFunction() {
    throw new UnsupportedOperationException("unsupported default constructor");
  }

  private BasicSampleFunction(final String id) {
    this.id = id;
  }

  public String getId()
  {
    return id;
  }

  public String toString()
  {
    return "basic sample function " + id;
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
