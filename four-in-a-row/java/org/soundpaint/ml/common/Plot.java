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
  public enum Mode
  {
    LINE, DOT
  };

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

  private final String windowTitle;
  private final PlotPane plotPane;
  private int currentAutoColor;

  private Plot()
  {
    throw new UnsupportedOperationException("unsupported default constructor");
  }

  public Plot(final String windowTitle,
              final String diagramLabel)
  {
    this.windowTitle = windowTitle;
    plotPane = new PlotPane(diagramLabel);
    currentAutoColor = 0;
  }

  private static final float hueShift = 8.77f / 16.0f;
  private static final float int2unitScale = 1.0f / 256.0f;

  private Color nextAutoColor()
  {
    final float hue = hueShift * (currentAutoColor & 0xff);
    final float saturation = 1.0f;
    final float brightness =
      0.25f + 0.75f * int2unitScale *
      ((Integer.reverse((~currentAutoColor) >> 3) >> 24) & 0xff);
    final Color color = Color.getHSBColor(hue, saturation, brightness);
    currentAutoColor++;
    return color;
  }

  public void plot(final Iterable<Double> x,
                   final Iterable<Double> y,
                   final Mode mode,
                   final Color color)
  {
    final var xStream = StreamSupport.stream(x.spliterator(), false);
    final var yStream = StreamSupport.stream(y.spliterator(), false);
    final var points =
      StreamUtils.zipToList(xStream, yStream,
                            Point2DFactory.getDefaultInstance());
    plotPane.addPoints(points, mode,
                       color != null ? color : nextAutoColor(), null);
  }

  public void show()
  {
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
