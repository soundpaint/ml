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

import java.awt.Color;
import java.util.List;

public class GraphTest
{
  private void test1() {
    System.out.println("running test #1");
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
    System.out.println("running test #2");
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
    System.out.println("running test #3");
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
    feedDictionary.put(x, Matrix.createRandomUniform(1, nFeatures, 0.0, 1.0));
    final var layerOut = session.run(a);
    System.out.println(layerOut);
    // expected result: array of array of 3 values between 0 and 1
  }

  private void test4() {
    System.out.println("running test #4");
    Graph.getDefaultInstance().clear();
    RandomSingleton.getInstance().setSeed(101);
    final FeedDictionary feedDictionary = new FeedDictionary();

    final var xData0 =
      new Variable<Matrix>(Matrix.createLinearSpace(11, 0.0, 10.0));
    final var xData1 =
      new Variable<Matrix>(Matrix.createRandomUniform(11, -1.5, 1.5));
    final var xData = new MatrixAddOperation(xData0, xData1);

    final var yLabel0 =
      new Variable<Matrix>(Matrix.createLinearSpace(11, 0.0, 10.0));
    final var yLabel1 =
      new Variable<Matrix>(Matrix.createRandomUniform(11, -1.5, 1.5));
    final var yLabel = new MatrixAddOperation(yLabel0, yLabel1);

    final var session = new Session();
    session.run(xData);
    session.run(yLabel);
    final var m = new Variable<Double>(0.44);
    final var b = new Variable<Double>(0.87);
    session.run(m);
    session.run(b);
    final List<StreamUtils.Pair<Double, Double>> zipped =
      StreamUtils.zipToList(xData.getOutputValue().stream(),
                            yLabel.getOutputValue().stream(),
                            new StreamUtils.PairFactory<Double,Double>());
    double error = 0.0;
    for (var pair : zipped) {
      final double yHat = m.getOutputValue() * pair.a + b.getOutputValue();
      error += Math.pow(pair.b - yHat, 2);
    }

    final var finalSlope = 1.0; // TODO: Compute this value.
    final var finalIntercept = -1.0; // TODO: Compute this value.
    final var xTest =
      new Variable<Matrix>(Matrix.createLinearSpace(10, -1.0, 11.0));
    final var yPredPlot =
      new MatrixAddOperation(new MatrixScaleOperation(finalSlope, xTest),
                             finalIntercept);
    session.run(yPredPlot);

    final Plot plot = new Plot("Linear Regression", "Points Before Regression");
    plot.plot(xTest, yPredPlot, Plot.Mode.LINE, Color.RED);
    plot.plot(xData, yLabel, Plot.Mode.DOT, Color.BLUE);
    plot.show();
  }

  public static void main(final String argv[])
  {
    final GraphTest graphTest = new GraphTest();
    System.out.println("running all tests");
    graphTest.test1();
    graphTest.test2();
    graphTest.test3();
    graphTest.test4();
    System.out.println("all tests done");
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
