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
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class PlotPane extends JPanel
{
  private static final long serialVersionUID = -4356794143603790043L;

  public static final Stroke DEFAULT_STROKE = new BasicStroke();
  public static final Stroke DEFAULT_LINE_STROKE =
    new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

  private final String label;
  private final Dimension dimension;
  private final Graphic graphic;

  public PlotPane()
  {
    throw new UnsupportedOperationException("unsupported default constructor");
  }

  public PlotPane(final String label)
  {
    this.label = label;
    dimension = new Dimension(450, 440);
    graphic = new Graphic();
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

  private static final double CAP_RATIO = 0.1;

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
