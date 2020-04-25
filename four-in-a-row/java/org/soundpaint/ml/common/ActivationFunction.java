/*
 * @(#)ActivationFunction.java 1.00 20/03/04
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

import java.util.function.Function;

public interface ActivationFunction<T> extends Function<T, T>
{
  public enum Standard implements ActivationFunction<Double>
  {
    THRESHOLD("threshold")
    {
      public Double apply(final Double input)
      {
        return 1.0 / (1.0 + Math.exp(-input));
      }
    },

    SIGMOID("sigmoid")
    {
      public Double apply(final Double input)
      {
        return 1.0 / (1.0 + Math.exp(-input));
      }
    },

    HYPERBOLIC_TANGENT("hyperbolic tangent")
    {
      public Double apply(final Double input)
      {
        return Math.tanh(input);
      }
    },

    RECTIFIED_LINEAR_UNIT("rectified linear unit")
    {
      public Double apply(final Double input)
      {
        return input < 0.0 ? 0.0 : input;
      }
    };

    private final String id;

    private Standard()
    {
      throw new UnsupportedOperationException("unsupported default constructor");
    }

    private Standard(final String id)
    {
      this.id = id;
    }

    public String getId()
    {
      return id;
    }

    public String toString()
    {
      return "activation function " + id;
    }
  }

  String getId();
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
