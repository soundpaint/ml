/*
 * @(#)CrossEntropyFunction.java 1.00 20/03/06
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

public class CrossEntropyCostFunction extends AbstractCostFunction
{
  public CrossEntropyCostFunction(final String id,
                                  final List<Double> targetValues)
  {
    super(id, targetValues);
  }

  public Double apply(final List<Perceptron> outputLayerPerceptrons) {
    if (outputLayerPerceptrons.size() != size) {
      throw new IllegalArgumentException("vector size mismatch:" +
                                         size + " != " +
                                         outputLayerPerceptrons.size());
    }
    double cost = 0.0;
    for (int index = 0; index < size; index++) {
      final Perceptron perceptron = outputLayerPerceptrons.get(index);
      final double y = targetValues.get(index);
      final double a = perceptron.getOutput();
      cost += y * Math.log(a) + (1.0 - y) * Math.log(1.0 - a);
    }
    return - cost * reverseSize;
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
