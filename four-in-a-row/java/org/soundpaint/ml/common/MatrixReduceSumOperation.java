/*
 * @(#)MatrixReduceSumOperation.java 1.00 20/06/25
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

public class MatrixReduceSumOperation extends Operation<Matrix, Matrix>
{
  public MatrixReduceSumOperation(final Matrix x)
  {
    this(new Variable<Matrix>(x));
  }

  public MatrixReduceSumOperation(final Node<Matrix, Matrix> x)
  {
    super("matrixreducesumop", List.of(x));
  }

  public Matrix performOperation()
  {
    if (inputValues.size() != 1) {
      throw new IllegalArgumentException("require 1 operand, got: " +
                                         inputValues.size());
    }
    return inputValues.get(1).reduceSum();
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
