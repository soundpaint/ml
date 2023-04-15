/*
 * @(#)DoubleToMatrixWrapper.java 1.00 23/03/26
 *
 * Copyright (C) 2023 Jürgen Reuter
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

public class DoubleToMatrixWrapper extends Operation<Matrix, Matrix>
{
  public DoubleToMatrixWrapper(final Node<Double, Double> x)
  {
    super("doubletomatrixwrapper", List.of(new WrapperNode(x)));
  }

  public Matrix performOperation()
  {
    if (inputValues.size() != 1) {
      throw new IllegalArgumentException("require 1 operand, got: " +
                                         inputValues.size());
    }
    final Node<?, Matrix> node = getInputNodes().get(0);
    final WrapperNode wrapperNode = (WrapperNode)node;
    return wrapperNode.getOutputValue();
  }

  private static class WrapperNode extends Node<Matrix, Matrix>
  {
    private final Node<Double, Double> x;

    private WrapperNode(final Node<Double, Double> x)
    {
      this.x = x;
    }

    @Override
    public void update(final FeedDictionary feedDictionary)
    {
      setOutputValue(new Matrix(x.getOutputValue()));
    }
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
