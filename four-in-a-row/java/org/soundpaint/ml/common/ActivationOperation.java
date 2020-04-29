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

import java.util.List;
import java.util.function.Function;

// Matrices currently work only for base type Double.  Therefore,
// ActivationFunction currently needs to have base type Double as
// well.  This will change when the Matrices will have a variable base
// type.
public class ActivationOperation<T extends Matrix> extends Operation<T, T>
{
  private final ActivationFunction<Double> function;

  private static <T> String getFunctionId(final ActivationFunction<T> function)
  {
    if (function == null) {
      throw new NullPointerException("function");
    }
    return function.getId() + "op";
  }

  public ActivationOperation(final ActivationFunction<Double> function,
                             final Node<T, T> x)
  {
    super(getFunctionId(function), List.of(x));
    this.function = function;
  }

  public T performOperation()
  {
    if (inputValues.size() != 1) {
      throw new IllegalArgumentException("require 1 operand, got: " +
                                         inputValues.size());
    }
    final T inputValue = inputValues.get(0);
    for (int row = 0; row < inputValue.getRows(); row++) {
      for (int column = 0; column < inputValue.getColumns(); column++) {
        final Double elementBefore = inputValue.getElementAt(row, column);
        final Double elementAfter = function.apply(elementBefore);
        inputValue.setElementAt(row, column, elementAfter);
      }
    }
    return inputValue;
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
