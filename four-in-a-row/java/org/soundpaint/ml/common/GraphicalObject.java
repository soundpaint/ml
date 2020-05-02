/*
 * @(#)GraphicalObject.java 1.00 20/04/26
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
import java.awt.geom.Point2D;
import java.awt.geom.AffineTransform;
import java.util.Objects;

public abstract class GraphicalObject
{
  public static final Point2D ZERO_POINT = new Point2D.Double(0.0, 0.0);
  private final Point2D position;
  private final Point2D displayOffset;
  private final Color color;
  private final Stroke stroke;

  private GraphicalObject()
  {
    throw new UnsupportedOperationException("unsupported default constructor");
  }

  public GraphicalObject(final Point2D position,
                         final Color color,
                         final Stroke stroke)
  {
    this(position, ZERO_POINT, color, stroke);
  }

  public GraphicalObject(final Point2D position,
                         final Point2D displayOffset,
                         final Color color,
                         final Stroke stroke)
  {
    Objects.requireNonNull(position);
    this.position = position;
    Objects.requireNonNull(displayOffset);
    this.displayOffset = displayOffset;
    this.color = color;
    this.stroke = stroke;
  }

  public Point2D getPosition()
  {
    return position;
  }

  public Point2D getDisplayOffset()
  {
    return displayOffset;
  }

  abstract double getLowerBoundX();

  abstract double getUpperBoundX();

  abstract double getLowerBoundY();

  abstract double getUpperBoundY();

  public Point2D getDisplayPosition(final AffineTransform affineTransform)
  {
    return affineTransform.transform(position, null);
  }

  public void draw(final AffineTransform affineTransform,
                   final Graphics g,
                   final Color defaultColor,
                   final Stroke defaultStroke)
  {
    final Graphics2D g2d = (Graphics2D)g;
    g2d.setColor(color != null ? color : defaultColor);
    g2d.setStroke(stroke != null ? stroke : defaultStroke);
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
