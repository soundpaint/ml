/*
 * @(#)ActivationOperation.java 1.00 20/03/09
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

import java.util.ArrayList;
import java.util.List;

public class ActivationOperation extends Operation<Double, Double>
{
  private final ActivationFunction function;
  private final List<Double> inputValues;

  private static String getFunctionId(final ActivationFunction function)
  {
    if (function == null) {
      throw new NullPointerException("function");
    }
    return function.getId() + "op";
  }

  public ActivationOperation(final ActivationFunction function,
                             final Node<Double, Double> x)
  {
    super(getFunctionId(function),
          new ArrayList<Node<Double, Double>>(List.of(x)));
    this.function = function;
    inputValues = new ArrayList<Double>();
  }

  public Double compute(final List<Double> operands)
  {
    if (operands == null) {
      throw new NullPointerException("operands");
    }
    if (operands.size() != 1) {
      throw new IllegalArgumentException("require 1 operand, got: " +
                                         operands.size());
    }
    inputValues.clear();
    inputValues.add(operands.get(0));
    return function.apply(operands.get(0));
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
