/*
 * @(#)Perceptron.java 1.00 20/03/04
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
import java.util.Random;
import java.util.function.Function;

public class Perceptron
{
  private final Random random;
  private final int size;
  private final double[] input;
  private final double[] weight;
  private final double[] bias;
  private ActivationFunction activationFunction;

  public Perceptron(final int size,
                    final ActivationFunction activationFunction)
  {
    this(size, 0, activationFunction);
  }

  public Perceptron(final int size, final int seed,
                    final ActivationFunction activationFunction)
  {
    if (size <= 0) {
      throw new IllegalArgumentException("size must be greater than 0");
    }
    random = new Random(seed);
    random.setSeed(seed);
    this.size = size;
    input = new double[size];
    weight = new double[size];
    bias = new double[size];
    generateRandomBias();
    setActivationFunction(activationFunction);
  }

  public void setInput(final int index, final double value)
  {
    if ((index < 0) || (index >= size)) {
      throw new IllegalArgumentException("got index " + index +
                                         ", but valid range is [0," +
                                         (size - 1) + "]");
    }
    input[index] = value;
  }

  public void setInputs(final List<Double> values)
  {
    if (values.size() != size) {
      throw new IllegalArgumentException("expected exactly " + size +
                                         " values, but got " + values.size());
    }
    for (int index = 0; index < size; index++) {
      final Double value = values.get(index);
      if (value == null) {
        throw new NullPointerException("value #" + index);
      }
      setInput(index, value);
    }
  }

  public void setWeight(final int index, final double value)
  {
    if ((index < 0) || (index >= size)) {
      throw new IllegalArgumentException("got index " + index +
                                         ", but valid range is [0," +
                                         (size - 1) + "]");
    }
    weight[index] = value;
  }

  public void setWeights(final List<Double> values)
  {
    if (values.size() != size) {
      throw new IllegalArgumentException("expected exactly " + size +
                                         " values, but got " + values.size());
    }
    for (int index = 0; index < size; index++) {
      final Double value = values.get(index);
      if (value == null) {
        throw new NullPointerException("value #" + index);
      }
      setWeight(index, value);
    }
  }

  public void setBias(final int index, final double value)
  {
    if ((index < 0) || (index >= size)) {
      throw new IllegalArgumentException("got index " + index +
                                         ", but valid range is [0," +
                                         (size - 1) + "]");
    }
    bias[index] = value;
  }

  public void generateRandomBias()
  {
    for (int index = 0; index < size; index++) {
      setBias(index, random.nextDouble());
    }
  }

  public void
    setActivationFunction(final ActivationFunction activationFunction)
  {
    if (activationFunction == null) {
      throw new NullPointerException("activationFunction");
    }
    this.activationFunction = activationFunction;
  }

  public double getOutput()
  {
    double weightedSum = 0.0;
    for (int i = 0; i < size; i++) {
      weightedSum = input[i] * weight[i] + bias[i];
    }
    return activationFunction.apply(weightedSum);
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
