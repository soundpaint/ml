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
    final FeedDictionary feedDictionary = new FeedDictionary();

    // z = ax + b
    final var a = new Variable<Double>(Math.PI);
    final var b = new Variable<Double>(Math.E);
    final var x = new Placeholder<Double>(feedDictionary);
    final var y = new MultiplyOperation(a, x);
    final var z = new AddOperation(y, b);
    final var session = new Session();
    feedDictionary.put(x, 100.0);
    System.out.println(session.run(z));
  }

  private void test2() {
    Graph.getDefaultInstance().clear();
    final FeedDictionary feedDictionary = new FeedDictionary();

    // z = Ax + b
    final var a =
      new Variable<Matrix>(new Matrix(new double[][] {{1.0, 2.0}, {3.0, 4.0}}));
    final var b =
      new Variable<Matrix>(new Matrix(new double[][] {{400.0, 300.0}, {200.0, 100.0}}));
    final var x = new Placeholder<Matrix>(feedDictionary);
    final var y = new MatrixMultiplyOperation(a, x);
    final var z = new MatrixAddOperation(y, b);
    final var session = new Session();
    feedDictionary.put(x, new Matrix(-1.0));
    System.out.println(session.run(z));
  }

  private void test3() {
    Graph.getDefaultInstance().clear();
    RandomSingleton.getInstance().setSeed(101);
    final FeedDictionary feedDictionary = new FeedDictionary();

    final var nFeatures = 10;
    final var nDenseNeurons = 3;
    final var x = new Placeholder<Matrix>(feedDictionary);
    final var w =
      new Variable<Matrix>(Matrix.createRandomNormal(nFeatures, nDenseNeurons, 1.0, 0.0));
    final var b = new Variable<Matrix>(Matrix.createOnes(1, nDenseNeurons));
    final var wx = new MatrixMultiplyOperation(w, x);
    final var z = new MatrixAddOperation(wx, b);
    final var a =
      new ActivationOperation<Matrix>(ActivationFunction.Standard.SIGMOID, z);
    final var session = new Session();
    feedDictionary.put(x, Matrix.createRandomUniform(1, nFeatures));
    final var layerOut = session.run(a);
    System.out.println(layerOut);
    // expected result: array of array of 3 values between 0 and 1
  }

  public static void main(final String argv[])
  {
    final GraphTest graphTest = new GraphTest();
    graphTest.test1();
    graphTest.test2();
    graphTest.test3();
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
