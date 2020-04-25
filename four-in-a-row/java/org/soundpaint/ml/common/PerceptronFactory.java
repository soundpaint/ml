/*
 * @(#)PerceptronFactory.java 1.00 20/03/06
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

public class PerceptronFactory
{
  private static final ActivationFunction<Double> DEFAULT_ACTIVATION_FUNCTION =
    ActivationFunction.Standard.HYPERBOLIC_TANGENT;

  private final ActivationFunction<Double> activationFunction;

  public PerceptronFactory()
  {
    this(DEFAULT_ACTIVATION_FUNCTION);
  }

  public PerceptronFactory(final ActivationFunction<Double> activationFunction)
  {
    if (activationFunction == null) {
      throw new NullPointerException("activationFunction");
    }
    this.activationFunction = activationFunction;
  }

  public Perceptron createPerceptron(final int size)
  {
    return new Perceptron(size, activationFunction);
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
