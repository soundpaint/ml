/*
 * @(#)Plot.java 1.00 20/04/25
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
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.StreamSupport;
import javax.swing.JOptionPane;

public class Plot
{
  private static class Point2DFactory
    implements BiFunction<Double, Double, Point2D>
  {
    private static final Point2DFactory defaultInstance = new Point2DFactory();

    public static Point2DFactory getDefaultInstance() {
      return defaultInstance;
    }

    public Point2D apply(final Double x, final Double y)
    {
      return new Point2D.Double(x, y);
    }
  }

  public static void show(final String windowTitle,
                          final String diagramLabel,
                          final Iterable<Double> x,
                          final Iterable<Double> y)
  {
    final var xStream = StreamSupport.stream(x.spliterator(), false);
    final var yStream = StreamSupport.stream(y.spliterator(), false);
    final var points =
      StreamUtils.zipToList(xStream, yStream,
                            Point2DFactory.getDefaultInstance());
    final PlotPane plotPane = new PlotPane(diagramLabel);
    plotPane.addPoints(points, null);
    /*
    plotPane.addLine(-7.0, -7.0, 10.0, 10.0,
                     Color.BLUE, PlotPane.DEFAULT_LINE_STROKE); // DEBUG
    */
    plotPane.addAxes();
    JOptionPane.
      showMessageDialog(null,
                        plotPane, windowTitle,
                        JOptionPane.INFORMATION_MESSAGE);
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
