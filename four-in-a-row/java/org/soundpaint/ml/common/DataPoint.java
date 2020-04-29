/*
 * @(#)Point.java 1.00 20/04/26
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
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class DataPoint extends GraphicalObject
{
  private final int radius;
  private final int diameter;
  private final boolean fill;

  public DataPoint(final Point2D position,
                   final int radius, final boolean fill,
                   final Color color, final Stroke stroke)
  {
    super(position, color, stroke);
    if (radius < 1) {
      throw new IllegalArgumentException("radius < 1: " + radius);
    }
    this.radius = radius;
    diameter = 2 * radius;
    this.fill = fill;
  }

  @Override
  public double getLowerBoundX() {
    return getPosition().getX();
  }

  @Override
  public double getUpperBoundX() {
    return getPosition().getX();
  }

  @Override
  public double getLowerBoundY() {
    return getPosition().getY();
  }

  @Override
  public double getUpperBoundY() {
    return getPosition().getY();
  }

  @Override
  public void draw(final AffineTransform affineTransform,
                   final Graphics g,
                   final Color defaultColor,
                   final Stroke defaultStroke)
  {
    super.draw(affineTransform, g, defaultColor, defaultStroke);
    final Graphics2D g2d = (Graphics2D)g;
    final Point2D displayPosition = getDisplayPosition(affineTransform);
    final Ellipse2D.Double circle =
      new Ellipse2D.Double((int)displayPosition.getX() - radius,
                           (int)displayPosition.getY() - radius,
                           diameter, diameter);
    if (fill) {
      g2d.fill(circle);
    }
    g2d.draw(circle);
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
