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
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

// TODO: Would like to generify this class as Matrix<T> over elements
// of base type T.  However, we have operations such as matrix
// addition or scaling.  The base type hence would need to implement
// an interface that provides algebraic operations like that of
// algebraic rings of fields.  However, one would need to subclass
// classes like java.lang.Double to implement such an interface with
// algebraic operations to be applicable for a generic Matrix<T> type
// with base type T that implements algebraic operations.
// For now, we assume Double as the only base type of matrices.
public class Matrix implements Iterable<Double>
{
  public enum Direction { HORIZONTAL, VERTICAL };

  private class ElementIterator implements Iterator<Double>
  {
    private final Direction direction;
    private final int rowsBound;
    private final int columnsBound;
    private int row, column;

    private ElementIterator()
    {
      direction = null;
      row = 0;
      column = 0;
      rowsBound = rows;
      columnsBound = columns;
    }

    private ElementIterator(final Direction direction, final int index)
    {
      this.direction = direction;
      switch (direction) {
      case HORIZONTAL:
        row = index;
        column = 0;
        rowsBound = index;
        columnsBound = columns;
        break;
      case VERTICAL:
        row = 0;
        column = index;
        rowsBound = rows;
        columnsBound = index;
        break;
      default:
        throw new IllegalStateException("unexpected case fall-through");
      }
    }

    public boolean hasNext()
    {
      return (row < rows) && (column < columns);
    }

    private void updateIndices()
    {
      if (direction != null) {
        switch (direction) {
        case HORIZONTAL:
          column++;
          break;
        case VERTICAL:
          row++;
          break;
        default:
          throw new IllegalStateException("unexpected case fall-through");
        }
      } else {
        if (column < columns) {
          column++;
        } else if (row < rows) {
          column = 0;
          row++;
        }
      }
    }

    public Double next()
    {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      final Double next = elements[row][column];
      updateIndices();
      return next;
    }
  }

  private final String id;
  private final double[][] elements;
  private final int rows;
  private final int columns;

  /**
   * Creates columns×rows matrix filled with 1 values.
   */
  public static Matrix createOnes(final int columns, final int rows)
  {
    final double[][] elements = new double[rows][columns];
    for (var row = 0; row < rows; row++) {
      for (var column = 0; column < columns; column++) {
        elements[row][column] = BasicSampleFunction.ONES.apply(null);
      }
    }
    return new Matrix(elements);
  }

  public static Matrix createLinearSpace(final int count,
                                         final double firstValue,
                                         final double lastValue)
  {
    return createLinearSpace(count, 1, firstValue, lastValue);
  }

  public static Matrix createLinearSpace(final int columns,
                                         final int rows,
                                         final double firstValue,
                                         final double lastValue)
  {
    final int count = rows * columns;
    if (count <= 1) {
      throw new IllegalArgumentException("got " + count +
                                         " elements, but need at least 2");
    }
    final double space = lastValue - firstValue;
    final double distance = space / (count - 1);
    final double[][] elements = new double[rows][columns];
    int index = 0;
    for (var row = 0; row < rows; row++) {
      for (var column = 0; column < columns; column++) {
        elements[row][column] = firstValue + index++ * distance;
      }
    }
    return new Matrix(elements);
  }

  public static Matrix createRandomUniform(final int columns,
                                           final int rows,
                                           final double minValue,
                                           final double maxValue)
  {
    final double interval = maxValue - minValue;
    final double[][] elements = new double[rows][columns];
    for (var row = 0; row < rows; row++) {
      for (var column = 0; column < columns; column++) {
        elements[row][column] =
          minValue + interval * BasicSampleFunction.RANDOM_UNIFORM.apply(null);
      }
    }
    return new Matrix(elements);
  }

  public static Matrix createRandomNormal(final int count)
  {
    return createRandomNormal(count, 1.0, 0.0);
  }

  public static Matrix createRandomNormal(final int count,
                                          final double σ,
                                          final double µ)
  {
    return createRandomNormal(count, 1, σ, µ);
  }

  public static Matrix createRandomNormal(final int columns,
                                          final int rows,
                                          final double σ,
                                          final double µ)
  {
    final double[][] elements = new double[rows][columns];
    for (var row = 0; row < rows; row++) {
      for (var column = 0; column < columns; column++) {
        elements[row][column] =
          BasicSampleFunction.RANDOM_NORMAL.apply(null) * σ + µ;
      }
    }
    return new Matrix(elements);
  }

  public static Matrix fromArray(final double[][] elements)
  {
    final int rows = elements.length;
    int columns = 0;
    for (int row = 0; row < rows; row++) {
      if (elements[row].length > columns) {
        columns = elements[row].length;
      }
    }
    final double[][] internalElements = new double[rows][columns];
    for (int row = 0; row < rows; row++) {
      for (int column = 0; column < elements[row].length; column++) {
        internalElements[row][column] = elements[row][column];
      }
    }
    return new Matrix(internalElements);
  }

  private Matrix()
  {
    throw new UnsupportedOperationException("unsupported default constructor");
  }

  public Matrix(final double scale)
  {
    this(new double[][] {{scale, 0.0}, {0.0, scale}});
  }

  private Matrix(final double[][] elements)
  {
    assert elements != null : "unexpected null elements";
    id = "matrix-" + Uid.createUniqueId();
    rows = elements.length;
    columns = rows > 0 ? elements[0].length : 0;
    for (int row = 0; row < rows; row++) {
      assert
        elements[row].length == columns : "elements must have square shape";
    }
    this.elements = elements;
  }

  public String getId()
  {
    return id;
  }

  public int getRows()
  {
    return rows;
  }

  public int getColumns()
  {
    return columns;
  }

  public int getSize()
  {
    return rows * columns;
  }

  public Double getElementAt(final int row, final int column)
  {
    return elements[row][column];
  }

  public void setElementAt(final int row, final int column,
                           final Double element)
  {
    elements[row][column] = element;
  }

  private Iterator<Double> rowIterator(final int row)
  {
    return new ElementIterator(Direction.VERTICAL, row);
  }

  private Iterator<Double> columnIterator(final int column)
  {
    return new ElementIterator(Direction.HORIZONTAL, column);
  }

  public Iterator<Double> iterator()
  {
    return new ElementIterator();
  }

  public Stream<Double> stream()
  {
    final Iterable<Double> iterable = () -> iterator();
    return StreamSupport.stream(iterable.spliterator(), false);
  }

  public Matrix apply(final SampleFunction function)
  {
    final double[][] elements = new double[rows][columns];
    for (int row = 0; row < rows; row++) {
      for (int column = 0; column < columns; column++) {
        elements[row][column] = function.apply(null);
      }
    }
    return new Matrix(elements);
  }

  public Matrix add(final Matrix other)
  {
    if (other == null) {
      throw new NullPointerException("other");
    }
    if (other.rows != rows) {
      throw new IllegalArgumentException("rows mismatch: " + other.rows +
                                         " != " + rows +
                                         ", m1=" + fullToString() +
                                         ", m2=" + other.fullToString());
    }
    if (other.columns != columns) {
      throw new IllegalArgumentException("rows mismatch: " + other.columns +
                                         " != " + columns +
                                         ", m1=" + fullToString() +
                                         ", m2=" + other.fullToString());
    }
    final double[][] sum = new double[rows][columns];
    for (int row = 0; row < rows; row++) {
      for (int column = 0; column < columns; column++) {
        sum[row][column] = elements[row][column] + other.elements[row][column];
      }
    }
    return new Matrix(sum);
  }

  public Matrix scale(final Double scale)
  {
    final double[][] scaled = new double[rows][columns];
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
                                         ", m1=" + fullToString() +
                                         ", m2=" + other.fullToString());
    }
    final double[][] product = new double[rows][other.columns];
    for (int otherColumn = 0; otherColumn < other.columns; otherColumn++) {
      for (int row = 0; row < rows; row++) {
        double dotSum = 0.0;
        for (int column = 0; column < columns; column++) {
          dotSum += elements[row][column] * other.elements[column][otherColumn];
        }
        product[row][otherColumn] = dotSum;
      }
    }
    final Matrix result = new Matrix(product);
    return result;
  }

  private final static int MAX_ROWS_PRINT = 10;
  private final static int MAX_COLUMNS_PRINT = 10;

  private String rowToString(final int row)
  {
    final StringBuilder s = new StringBuilder();
    final boolean abbreviate = columns > MAX_COLUMNS_PRINT;
    if (abbreviate) {
      s.append(elements[row][0]);
      s.append(", ");
      s.append(elements[row][1]);
      s.append(", ");
      s.append(elements[row][2]);
      s.append(", …, ");
      s.append(elements[row][columns - 1]);
    } else {
      for (int column = 0; column < columns; column++) {
        if (s.length() > 0) {
          s.append(", ");
        }
        s.append(elements[row][column]);
      }
    }
    return "{" + s + "}";
  }

  public String contentsToString()
  {
    final StringBuilder s = new StringBuilder();
    final boolean abbreviate = rows > MAX_ROWS_PRINT;
    if (abbreviate) {
      s.append(rowToString(0));
      s.append(", ");
      s.append(rowToString(1));
      s.append(", ");
      s.append(rowToString(2));
      s.append(", …, ");
      s.append(rowToString(rows - 1));
    } else {
      for (int row = 0; row < rows; row++) {
        if (s.length() > 0) {
          s.append(", ");
        }
        s.append(rowToString(row));
      }
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
