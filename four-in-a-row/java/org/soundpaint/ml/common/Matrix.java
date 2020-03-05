/*
 * @(#)Matrix.java 1.00 20/03/08
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

public class Matrix
{
  private final String id;
  private final double[][] elements;
  private final int rows;
  private final int columns;

  private Matrix()
  {
    throw new UnsupportedOperationException("unsupported default constructor");
  }

  public Matrix(final double scale)
  {
    this(new double[][] {{scale, 0.0}, {0.0, scale}});
  }

  public Matrix(final double[][] elements)
  {
    if (elements == null) {
      throw new NullPointerException("elements");
    }
    id = "matrix-" + Uid.createUniqueId();
    rows = elements.length;
    int columns = 0;
    for (int row = 0; row < rows; row++) {
      if (elements[row].length > columns) {
        columns = elements[row].length;
      }
    }
    this.columns = columns;
    this.elements = new double[rows][columns];
    for (int row = 0; row < rows; row++) {
      for (int column = 0; column < elements[row].length; column++) {
        this.elements[row][column] = elements[row][column];
      }
    }
  }

  public String getId()
  {
    return id;
  }

  public Matrix add(final Matrix other)
  {
    if (other == null) {
      throw new NullPointerException("other");
    }
    if (other.rows != rows) {
      throw new IllegalArgumentException("rows mismatch: " + other.rows +
                                         " != " + rows +
                                         ", m1=" + this +
                                         ", m2=" + other);
    }
    if (other.columns != columns) {
      throw new IllegalArgumentException("rows mismatch: " + other.columns +
                                         " != " + columns +
                                         ", m1=" + this +
                                         ", m2=" + other);
    }
    double[][] sum = new double[rows][columns];
    for (int row = 0; row < rows; row++) {
      for (int column = 0; column < columns; column++) {
        sum[row][column] = elements[row][column] + other.elements[row][column];
      }
    }
    return new Matrix(sum);
  }

  public Matrix scale(final Double scale)
  {
    double[][] scaled = new double[rows][columns];
    for (int row = 0; row < rows; row++) {
      for (int column = 0; column < columns; column++) {
        scaled[row][column] = elements[row][column] * scale;
      }
    }
    return new Matrix(scaled);
  }

  public Matrix dot(final Matrix other)
  {
    if (other == null) {
      throw new NullPointerException("other");
    }
    if (other.rows != columns) {
      throw new IllegalArgumentException("size mismatch: " + other.rows +
                                         " != " + columns +
                                         ", m1=" + this +
                                         ", m2=" + other);
    }
    double[][] product = new double[rows][other.columns];
    for (int otherColumn = 0; otherColumn < other.columns; otherColumn++) {
      for (int row = 0; row < rows; row++) {
        double sum = 0.0;
        for (int column = 0; column < columns; column++) {
          sum += elements[row][column] * other.elements[column][otherColumn];
        }
        product[row][otherColumn] = sum;
      }
    }
    final Matrix result = new Matrix(product);
    return result;
  }

  private String rowToString(final int row)
  {
    final StringBuilder s = new StringBuilder();
    for (int column = 0; column < columns; column++) {
      if (s.length() > 0) {
        s.append(", ");
      }
      s.append(elements[row][column]);
    }
    return "{" + s + "}";
  }

  public String contentsToString()
  {
    final StringBuilder s = new StringBuilder();
    for (int row = 0; row < rows; row++) {
      if (s.length() > 0) {
        s.append(", ");
      }
      s.append(rowToString(row));
    }
    return "{" + s + "}";
  }

  public String fullToString()
  {
    return "Matrix(id=" + id + ", rows=" + rows + ", columns=" + columns +
      ", contents=" + contentsToString() + ")";
  }

  public String toString()
  {
    return contentsToString();
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
