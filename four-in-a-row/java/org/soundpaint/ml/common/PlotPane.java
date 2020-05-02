/*
 * @(#)PlotPane.java 1.00 20/04/26
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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class PlotPane extends JPanel
{
  private static final long serialVersionUID = -4356794143603790043L;

  public static interface TickLabelRenderer
  {
    String renderTickLabel(final double value);
  }

  /**
   * Displays limited amount of mantissa to keep the resulting String
   * short.
   *
   * Caveat / FIXME: This renderer currently works only for values in
   * the range of float values.
   */
  public static final TickLabelRenderer DEFAULT_TICK_LABEL_RENDERER =
    new TickLabelRenderer()
    {
      public String renderTickLabel(final double value)
      {
        return Float.toString((float)value);
      }
    };

  public static final Stroke DEFAULT_STROKE = new BasicStroke();
  public static final Stroke DEFAULT_LINE_STROKE =
    new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
  public static final Stroke DEFAULT_THIN_LINE_STROKE =
    new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

  private final String label;
  private final Dimension dimension;
  private final Graphic graphic;
  private TickLabelRenderer tickLabelRenderer;

  public PlotPane()
  {
    throw new UnsupportedOperationException("unsupported default constructor");
  }

  public PlotPane(final String label)
  {
    this.label = label;
    dimension = new Dimension(450, 440);
    graphic = new Graphic();
    tickLabelRenderer = DEFAULT_TICK_LABEL_RENDERER;
    setBorder(BorderFactory.createLineBorder(Color.black));
  }

  public void addPoints(final Iterable<Point2D> points, final Stroke stroke)
  {
    for (final Point2D point : points) {
      graphic.addPoint(point, 2, true, Color.RED, stroke);
    }
  }

  public void addLine(final double x1, final double y1,
                      final double x2, final double y2,
                      final Color color, final Stroke stroke)
  {
    graphic.addLine(new Point2D.Double(x1, y1),
                    new Point2D.Double(x2, y2),
                    color, stroke);
  }

  public void setTickLabelRenderer(final TickLabelRenderer tickLabelRenderer)
  {
    Objects.requireNonNull(tickLabelRenderer);
    this.tickLabelRenderer = tickLabelRenderer;
  }

  private static final double tickXIdealCount = 5.0;
  private static final double tickYIdealCount = 5.0;
  private static final double tickXCap = 10.0;
  private static final double tickYCap = 10.0;

  /**
   * Return a number d from the set (..., 0.001, 0.01, 0.1, 1.0, 10.0,
   * 100.0, 1000.0, ...) such that d * extent is in the range [0.0,
   * 10.0) for any positive value of extent.  If extent is 0.0, return
   * 1.0.
   */
  private double getUnitScaleFactor(final double extent)
  {
    return
      extent > 0.0 ?
      Math.exp((1.0 + Math.floor(Math.log(extent) / Math.log(10.0))) *
               Math.log(10.0)) :
      1.0;
  }

  private static final List<Double> SCALE_UNITS =
    Arrays.asList(0.1, 0.2, 0.5, 1.0);

  private static final List<StreamUtils.Pair<Double, Double>>
    SCALE_LOG_SCALE_UNITS;

  static {
    final Stream<Double> logScaleUnits =
      SCALE_UNITS.stream().map(x -> Math.log(x));
    SCALE_LOG_SCALE_UNITS =
      StreamUtils.zip(SCALE_UNITS.stream(), logScaleUnits,
                      new StreamUtils.PairFactory<Double,Double>()).
      collect(Collectors.toList());
  }

  private double getNearestScaleUnit(final double scaleFactor)
  {
    assert (scaleFactor >= 0.1) && (scaleFactor <= 1.0);
    final double logScaleFactor = Math.log(scaleFactor);
    double previousDistance = 100.0;
    double previousScaleUnit = 0.0;
    for (final StreamUtils.Pair<Double, Double>
           scaleLogScaleUnit : SCALE_LOG_SCALE_UNITS) {
      final double distance = Math.abs(logScaleFactor - scaleLogScaleUnit.b);
      if (distance > previousDistance) {
        return previousScaleUnit;
      }
      previousScaleUnit = scaleLogScaleUnit.a;
      previousDistance = distance;
    }
    assert previousScaleUnit != 0.0;
    return previousScaleUnit;
  }

  private enum Direction { HORIZONTAL, VERTICAL };
  private static final double LABEL_PADDING = 5.0;

  // Assuming screen (or any other rendering device) with width and
  // height of not more than 1000000 pixels
  private static final double EPSILON = 1.0 / 1000000;

  private void addTick(final double tickX, final double tickY,
                       final double tickCapX, final double tickCapY,
                       final double textDisplayOffsetX,
                       final double textDisplayOffsetY,
                       final Text.AlignmentX textAlignmentX,
                       final Text.AlignmentY textAlignmentY,
                       final String label)
  {
    graphic.addLine(new Point2D.Double(tickX, tickY),
                    new Point2D.Double(-tickCapX, -tickCapY),
                    new Point2D.Double(tickX, tickY),
                    new Point2D.Double(tickCapX, tickCapY),
                    Color.BLACK, DEFAULT_THIN_LINE_STROKE);
    graphic.addText(new Point2D.Double(tickX, tickY),
                    new Point2D.Double(textDisplayOffsetX, textDisplayOffsetY),
                    label, Color.BLACK, textAlignmentX, textAlignmentY);
  }

  private void addTick(final double tick,
                       final double tickCap,
                       final double extent,
                       final Direction direction)
  {
    final boolean isOrigin = Math.abs(tick) < EPSILON * extent;
    if (!isOrigin) {
      final String label = tickLabelRenderer.renderTickLabel(tick);
      switch (direction) {
      case HORIZONTAL:
        addTick(0.0, tick,
                tickCap, 0.0,
                -tickCap - LABEL_PADDING, 0.0,
                Text.AlignmentX.RIGHT, Text.AlignmentY.MIDDLE,
                label);
        break;
      case VERTICAL:
        addTick(tick, 0.0,
                0.0, tickCap,
                0.0, tickCap + LABEL_PADDING,
                Text.AlignmentX.CENTER, Text.AlignmentY.TOP,
                label);
        break;
      }
    }
  }

  private void addTicks(final double min, final double max,
                        final double tickIdealCount, final double tickCap,
                        final Direction direction)
  {
    final double extent = max - min;
    final double tickIdealExtent = extent / tickIdealCount;
    final double unitScale = getUnitScaleFactor(tickIdealExtent);
    final double normalizedTickIdealExtent = tickIdealExtent / unitScale;
    final double normalizedScaleUnit =
      getNearestScaleUnit(normalizedTickIdealExtent);
    final double deltaTick = normalizedScaleUnit * unitScale;
    final int ticksCount = (int)(extent / deltaTick);
    final double tick0 = deltaTick * (int)Math.ceil(min / deltaTick);

    double tick;
    for (int tickIndex = 0;
         (tick = tick0 + tickIndex * deltaTick) < max;
         tickIndex++)
    {
      addTick(tick, tickCap, extent, direction);
    }
  }

  private void addTicks(final double minX,
                        final double maxX,
                        final double minY,
                        final double maxY)
  {
    addTicks(minX, maxX, tickXIdealCount, tickXCap, Direction.VERTICAL);
    addTicks(minY, maxY, tickYIdealCount, tickYCap, Direction.HORIZONTAL);
  }

  private static final double CAP_RATIO = 0.1; // 10% axis overshoot

  public void addAxes()
  {
    double
      minX = 0.0,
      maxX = 0.0,
      minY = 0.0,
      maxY = 0.0;
    for (final GraphicalObject object : graphic) {
      minX = Math.min(minX, object.getLowerBoundX());
      maxX = Math.max(maxX, object.getUpperBoundX());
      minY = Math.min(minY, object.getLowerBoundY());
      maxY = Math.max(maxY, object.getUpperBoundY());
    }
    final double extentX = maxX - minX;
    final double extentY = maxY - minY;
    minX -= CAP_RATIO * extentX;
    maxX += CAP_RATIO * extentX;
    minY -= CAP_RATIO * extentY;
    maxY += CAP_RATIO * extentY;
    graphic.addLine(new Point2D.Double(minX, 0.0),
                    new Point2D.Double(maxX, 0.0),
                    Color.BLACK, DEFAULT_LINE_STROKE);
    graphic.addLine(new Point2D.Double(0.0, minY),
                    new Point2D.Double(0.0, maxY),
                    Color.BLACK, DEFAULT_LINE_STROKE);
    addTicks(minX, maxX, minY, maxY);
  }

  public Dimension getPreferredSize() {
    return dimension;
  }

  public void paintComponent(final Graphics g)
  {
    super.paintComponent(g);
    graphic.draw(g, getWidth(), getHeight(), getForeground(), DEFAULT_STROKE);
    final String text = label != null ? label : "Plot";
    g.drawString(text, 2, getFont().getSize() + 1);
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
