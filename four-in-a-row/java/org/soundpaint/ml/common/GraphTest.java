/*
 * @(#)GraphTest.java 1.00 20/03/08
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

import java.util.HashMap;

public class GraphTest
{
  private void test1() {
    Graph.getDefaultInstance().clear();

    // z = ax + b
    final var a = new Variable<Double, Double>(Math.PI);
    final var b = new Variable<Double, Double>(Math.E);
    final var x = new Placeholder<Double, Double>();
    final var y = new MultiplyOperation(a, x);
    final var z = new AddOperation(y, b);
    final var session = new Session<Double>();
    final var feedDictionary =
      new HashMap<Placeholder<Double, Double>, Double>();
    feedDictionary.put(x, 100.0);
    System.out.println(session.run(z, feedDictionary));
  }

  private void test2() {
    Graph.getDefaultInstance().clear();

    // z = Ax + b
    final var a =
      new Variable<Matrix, Matrix>(new Matrix(new double[][] {{1.0, 2.0}, {3.0, 4.0}}));
    final var b =
      new Variable<Matrix, Matrix>(new Matrix(new double[][] {{400.0, 300.0}, {200.0, 100.0}}));
    final var x = new Placeholder<Matrix, Matrix>();
    final var y = new MatrixMultiplyOperation(a, x);
    final var z = new MatrixAddOperation(y, b);
    final var session = new Session<Matrix>();
    final var feedDictionary =
      new HashMap<Placeholder<Matrix, Matrix>, Matrix>();
    feedDictionary.put(x, new Matrix(-1.0));
    System.out.println(session.run(z, feedDictionary));
  }

  public static void main(final String argv[])
  {
    final GraphTest graphTest = new GraphTest();
    graphTest.test1();
    graphTest.test2();
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
