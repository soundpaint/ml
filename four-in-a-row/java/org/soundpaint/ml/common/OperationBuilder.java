/*
 * @(#)OperationBuilder.java 1.00 23/04/15
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

public abstract class OperationBuilder<U, V>
{
  abstract Node<U, V> getOperand();

  public static MatrixOperationBuilder fromMatrixNode(final Node<Matrix, Matrix> operand)
  {
    return MatrixOperationBuilder.fromNode(operand);
  }

  public static DoubleOperationBuilder fromDoubleNode(final Node<Double, Double> operand)
  {
    return DoubleOperationBuilder.fromNode(operand);
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
