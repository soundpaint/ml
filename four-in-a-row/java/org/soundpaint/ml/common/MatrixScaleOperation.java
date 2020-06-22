/*
 * @(#)MatrixScaleOperation.java 1.00 20/05/03
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

public class MatrixScaleOperation extends Operation<Matrix, Matrix>
{
  public MatrixScaleOperation(final double scale,
                              final Matrix x)
  {
    this(scale, new Variable<Matrix>(x));
  }

  public MatrixScaleOperation(final double scale,
                              final Node<Matrix, Matrix> x)
  {
    super("matrixscaleop",
          List.of(new Variable<Matrix>(Matrix.fromArray(new double[][] {{scale}})), x));
  }

  public Matrix performOperation()
  {
    if (inputValues.size() != 2) {
      throw new IllegalArgumentException("require 2 operands, got: " +
                                         inputValues.size());
    }
    return inputValues.get(1).scale(inputValues.get(0).getElementAt(0, 0));
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
