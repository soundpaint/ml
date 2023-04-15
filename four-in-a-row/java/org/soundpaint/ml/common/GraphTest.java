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
  private void test0()
  {
    System.out.println("running test #0");
    Graph.getDefaultInstance().clear();
    RandomUtils.getRandom().setSeed(101);
    final FeedDictionary feedDictionary = new FeedDictionary();
    final var randA = Variable.createRandomUniform(0.0, 100.0, 5, 5);
    final var randB = Variable.createRandomUniform(0.0, 100.0, 5, 1);
    final var a2 = new Placeholder<Matrix>();
    final var b2 = new Placeholder<Matrix>();
    feedDictionary.put(a2, randA);
    feedDictionary.put(b2, randB);
    final var a = new Placeholder<Double>();
    final var b = new Placeholder<Double>();
    feedDictionary.put(a, 10.0);
    feedDictionary.put(b, 20.0);
    final var addOp = new AddOperation(a, b);
    final var mulOp = new MatrixMultiplyOperation(b2, a2);
    final var session = new Session();
    final var addResult = session.run(addOp, feedDictionary);
    System.out.println(addResult);
    final var mulResult = session.run(mulOp, feedDictionary);
    System.out.println(mulResult);
  }

  private void test1()
  {
    System.out.println("running test #1");
    Graph.getDefaultInstance().clear();
    final FeedDictionary feedDictionary = new FeedDictionary();

    // z = ax + b
    final var a = Variable.create(Math.PI);
    final var b = Variable.create(Math.E);
    final var x = new Placeholder<Double>();
    final var y = new MultiplyOperation(a, x);
    final var z = new AddOperation(y, b);
    final var session = new Session();
    feedDictionary.put(x, 100.0);
    System.out.println(session.run(z, feedDictionary));
  }

  private void test2()
  {
    System.out.println("running test #2");
    Graph.getDefaultInstance().clear();
    final FeedDictionary feedDictionary = new FeedDictionary();

    // z = Ax + b
    final var a =
      Variable.createMatrix(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
    final var b =
      Variable.createMatrix(new double[][] {{400.0, 300.0}, {200.0, 100.0}});
    final var x = new Placeholder<Matrix>();
    final var y = new MatrixMultiplyOperation(a, x);
    final var z = new MatrixAddOperation(y, b);
    final var session = new Session();
    feedDictionary.put(x, new Matrix(-1.0));
    System.out.println(session.run(z, feedDictionary));
  }

  private void test3()
  {
    System.out.println("running test #3");
    Graph.getDefaultInstance().clear();
    RandomUtils.getRandom().setSeed(101);
    final FeedDictionary feedDictionary = new FeedDictionary();

    final var nFeatures = 10;
    final var nDenseNeurons = 3;
    final var x = new Placeholder<Matrix>();
    final var w =
      Variable.createRandomNormal(1.0, 0.0, nFeatures, nDenseNeurons);
    final var b = Variable.createOnes(1, nDenseNeurons);
    final var wx = new MatrixMultiplyOperation(w, x);
    final var z = new MatrixAddOperation(wx, b);
    final var a =
      new ActivationOperation<Matrix>(ActivationFunction.Standard.SIGMOID, z);
    final var session = new Session();
    feedDictionary.put(x, Matrix.createRandomUniform(1, nFeatures, 0.0, 1.0));
    final var layerOut = session.run(a, feedDictionary);
    System.out.println(layerOut);
    // expected result: array of array of 3 values between 0 and 1
  }

  private void test4()
  {
    System.out.println("running test #4");
    Graph.getDefaultInstance().clear();
    RandomUtils.getRandom().setSeed(101);
    final FeedDictionary feedDictionary = new FeedDictionary();

    final var xData0 = Variable.createLinearSpace(0.0, 10.0, 11);
    final var xData1 = Variable.createRandomUniform(-1.5, 1.5, 11);
    final var xData = new MatrixAddOperation(xData0, xData1);

    final var yLabel0 = Variable.createLinearSpace(0.0, 10.0, 11);
    final var yLabel1 = Variable.createRandomUniform(-1.5, 1.5, 11);
    final var yLabel = new MatrixAddOperation(yLabel0, yLabel1);

    final var m = Variable.create(0.44);
    final var b = Variable.create(0.87);

    final var session = new Session();
    session.run(List.of(xData, yLabel, m, b), feedDictionary);

    var error =
      new Operation<Matrix, Double>("errorop", List.of()) {
        public Double performOperation()
        {
          final List<StreamUtils.Pair<Double, Double>> zipped =
          StreamUtils.zipToList(xData.getOutputValue().stream(),
                                yLabel.getOutputValue().stream(),
                                new StreamUtils.PairFactory<Double,Double>());
          double error = 0.0;
          for (var pair : zipped) {
            final double yHat = m.getOutputValue() * pair.a + b.getOutputValue();
            error += Math.pow(pair.b - yHat, 2);
          }
          return error;
        }
      };
    final var init =
      Graph.getDefaultInstance().createGlobalVariablesInitializer();
    session.run(List.of(init, error), feedDictionary);
    System.out.println("error=" + error.getOutputValue());

    final List<Object> finalResult = session.run(List.of(m, b), feedDictionary);
    final var finalSlope = (Double)finalResult.get(0);
    final var finalIntercept = (Double)finalResult.get(1);
    final var xTest = Variable.createLinearSpace(-1.0, 11.0, 10);

    // # y = mx + b
    final var yPredPlot =
      new MatrixAddOperation(new MatrixScaleOperation(finalSlope, xTest),
                             finalIntercept);
    session.run(yPredPlot, feedDictionary);

    final Plot plot = new Plot("Linear Regression", "Points Before Regression");
    plot.plot(xTest, yPredPlot, Plot.Mode.LINE, Color.RED);
    plot.plot(xData, yLabel, Plot.Mode.DOT, Color.BLUE);
    plot.show();
  }

  private void test5()
  {
    System.out.println("running test #5");
    Graph.getDefaultInstance().clear();
    RandomUtils.getRandom().setSeed(101);
    final FeedDictionary feedDictionary = new FeedDictionary();

    final var xData = Matrix.createLinearSpace(1001, 0.0, 10.0);
    System.out.println("xData=" + xData);
    final var noise = Matrix.createRandomNormal(1001);
    System.out.println("noise=" + noise);
    final var yTrue = xData.scale(0.5).add(5.0).add(noise);
    final var myData =
      Matrix.concat(xData, yTrue, Matrix.Direction.VERTICAL).transpose();
    final var mySampleData = myData.sample(Matrix.Direction.VERTICAL, 250);
    System.out.println(mySampleData);
    mySampleData.plot(0, 1, Matrix.Direction.VERTICAL);
    final var batchSize = 8;
    final var m = Variable.create(0.81);
    final var b = Variable.create(0.17);

    // Note: We currently do *not* support specifying batch sizes by
    // abusing a Placeholder constructor's shape argument, as commonly
    // done in TensorFlow, but still support a batch size argument as
    // dummy value for compatibility and maybe future extension.  That
    // is, for now, we create placeholders xph and yph as standard
    // scalar value containers, without passing a pseudo shape
    // argument into the constructor.
    final var xph = new Placeholder<Double>(batchSize);
    final var yph = new Placeholder<Double>(batchSize);

    final var y_model = new AddOperation(new MultiplyOperation(m, xph), b);
    final var error =
      OperationBuilder
      .fromDoubleNode(yph)
      .sub(OperationBuilder.fromDoubleNode(y_model))
      .square()
      .asMatrix()
      .reduceSum();
  }

  public static void main(final String argv[])
  {
    final GraphTest graphTest = new GraphTest();
    System.out.println("running all tests");
    graphTest.test0();
    graphTest.test1();
    graphTest.test2();
    graphTest.test3();
    graphTest.test4();
    graphTest.test5();
    System.out.println("all tests done");
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
