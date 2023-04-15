/*
 * @(#)MatrixOperationBuilder.java 1.00 23/04/15
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

public class MatrixOperationBuilder extends OperationBuilder<Matrix, Matrix>
{
  private final Node<Matrix, Matrix> operand;

  private MatrixOperationBuilder(final Node<Matrix, Matrix> operand)
  {
    this.operand = operand;
  }

  @Override
  public Node<Matrix, Matrix> getOperand() {
    return operand;
  }

  public static MatrixOperationBuilder fromNode(final Node<Matrix, Matrix> operand)
  {
    return new MatrixOperationBuilder(operand);
  }

  public MatrixOperationBuilder reduceSum()
  {
    return fromNode(new MatrixReduceSumOperation(operand));
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
