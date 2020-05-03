/*
 * @(#)Variable.java 1.00 20/03/08
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

public class Variable<T> extends Node<T, T>
{
  private T value;

  public Variable()
  {
    this(null);
  }

  public Variable(final T value)
  {
    super("variable(" + value + ")");
    this.value = value;
    Graph.getDefaultInstance().add(this);
  }

  public static <T> Variable<T> create(final T value)
  {
    return new Variable<T>(value);
  }

  public static Variable<Matrix> createMatrix(final double[][] elements)
  {
    return create(new Matrix(elements));
  }

  public static Variable<Matrix> createOnes(final int count)
  {
    return create(Matrix.createOnes(count, 1));
  }

  public static Variable<Matrix> createOnes(final int columns,
                                            final int rows)
  {
    return create(Matrix.createOnes(columns, rows));
  }

  public static Variable<Matrix> createLinearSpace(final double min,
                                                   final double max,
                                                   final int count)
  {
    return create(Matrix.createLinearSpace(count, 1, min, max));
  }

  public static Variable<Matrix> createLinearSpace(final double min,
                                                   final double max,
                                                   final int columns,
                                                   final int rows)
  {
    return create(Matrix.createLinearSpace(columns, rows, min, max));
  }

  public static Variable<Matrix> createRandomUniform(final double min,
                                                     final double max,
                                                     final int count)
  {
    return create(Matrix.createRandomUniform(count, 1, min, max));
  }

  public static Variable<Matrix> createRandomUniform(final double min,
                                                     final double max,
                                                     final int columns,
                                                     final int rows)
  {
    return create(Matrix.createRandomUniform(columns, rows, min, max));
  }

  public static Variable<Matrix> createRandomNormal(final double σ,
                                                    final double µ,
                                                    final int count)
  {
    return create(Matrix.createRandomNormal(count, 1, σ, µ));
  }

  public static Variable<Matrix> createRandomNormal(final double σ,
                                                    final double µ,
                                                    final int columns,
                                                    final int rows)
  {
    return create(Matrix.createRandomUniform(columns, rows, σ, µ));
  }

  public T getValue()
  {
    return value;
  }

  @Override
  public void update()
  {
    setOutputValue(getValue());
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
