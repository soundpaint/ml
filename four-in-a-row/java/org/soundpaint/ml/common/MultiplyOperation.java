/*
 * @(#)MultiplyOperation.java 1.00 20/03/08
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

public class MultiplyOperation extends Operation<Double, Double>
{
  private final List<Double> inputValues;

  public MultiplyOperation(final Node<Double, Double> x,
                           final Node<Double, Double> y)
  {
    super("mulop", new ArrayList<Node<Double, Double>>(List.of(x, y)));
    inputValues = new ArrayList<Double>();
  }

  public Double compute(final List<Double> operands)
  {
    if (operands == null) {
      throw new NullPointerException("operands");
    }
    if (operands.size() != 2) {
      throw new IllegalArgumentException("require 2 operands, got: " +
                                         operands.size());
    }
    inputValues.clear();
    inputValues.add(operands.get(0));
    inputValues.add(operands.get(1));
    return operands.get(0) * operands.get(1);
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
