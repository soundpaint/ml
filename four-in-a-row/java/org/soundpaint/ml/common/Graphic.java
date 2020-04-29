/*
 * @(#)Graphic.java 1.00 20/04/26
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
import java.awt.Graphics;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Graphic implements Iterable<GraphicalObject>
{
  private final List<GraphicalObject> objects;
  private final AffineTransform data2display;
  private boolean boundsDirty;

  public Graphic()
  {
    objects = new ArrayList<GraphicalObject>();
    data2display = AffineTransform.getTranslateInstance(0.0, 0.0);
    boundsDirty = true;
  }

  public void addPoint(final Point2D point,
                       final int radius,
                       final boolean fill,
                       final Color color,
                       final Stroke stroke)
  {
    objects.add(new DataPoint(point, radius, fill, color, stroke));
    boundsDirty = true;
  }

  public void addLine(final Point2D p1, final Point2D p2,
                      final Color color, final Stroke stroke)
  {
    objects.add(new Line(p1, p2, color, stroke));
    boundsDirty = true;
  }

  public void addText(final Point2D pos, final String text,
                      final Color color)
  {
    objects.add(new Text(pos, text, color));
    boundsDirty = true;
  }

  public Iterator<GraphicalObject> iterator()
  {
    return objects.iterator();
  }

  private static final double BORDER_FRACTION = 0.1; // 10% of panel as border

  private void updateTransform(final int displayWidth, final int displayHeight)
  {
    double
      minX = Double.POSITIVE_INFINITY,
      maxX = Double.NEGATIVE_INFINITY,
      minY = Double.POSITIVE_INFINITY,
      maxY = Double.NEGATIVE_INFINITY;
    for (final GraphicalObject object : objects) {
      minX = Math.min(minX, object.getLowerBoundX());
      maxX = Math.max(maxX, object.getUpperBoundX());
      minY = Math.min(minY, object.getLowerBoundY());
      maxY = Math.max(maxY, object.getUpperBoundY());
    }
    final double tx = BORDER_FRACTION * displayWidth;
    final double ty = (1.0 - BORDER_FRACTION) * displayHeight;
    data2display.setToTranslation(tx, ty);
    final double plotAreaWidth = displayWidth * (1.0 - 2.0 * BORDER_FRACTION);
    final double plotAreaHeight = displayHeight * (1.0 - 2.0 * BORDER_FRACTION);
    final double extentX = minX != maxX ? maxX - minX : 1;
    final double extentY = minY != maxY ? maxY - minY : 1;
    final double sx = plotAreaWidth / extentX;
    final double sy = plotAreaHeight / extentY;
    data2display.scale(sx, -sy);
    data2display.translate(-minX, -minY);
    boundsDirty = false;
  }

  public void draw(final Graphics g, final int width, final int height,
                   final Color defaultColor, final Stroke defaultStroke)
  {
    if (boundsDirty) {
      updateTransform(width, height);
    }
    for (final GraphicalObject object : objects) {
      object.draw(data2display, g, defaultColor, defaultStroke);
    }
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
