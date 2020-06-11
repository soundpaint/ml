/*
 * @(#)MatrixInitializer.java 1.00 20/06/11
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

public class MatrixInitializer implements Initializer<Matrix>
{
  private final Matrix matrix;
  private final Initializer<Double> elementInitializer;

  private MatrixInitializer()
  {
    throw new UnsupportedOperationException("unsupported default constructor");
  }

  public MatrixInitializer(final Matrix matrix,
                           final Initializer<Double> elementInitializer)
  {
    this.matrix = matrix;
    this.elementInitializer = elementInitializer;
  }

  public Matrix createInitialValue()
  {
    return matrix.apply(new SampleFunction() {
        public Double apply(final Void __) {
          return elementInitializer.createInitialValue();
        }
      });
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
