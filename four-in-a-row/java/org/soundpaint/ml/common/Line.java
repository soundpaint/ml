/*
 * @(#)Line.java 1.00 20/04/26
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
import java.util.Objects;

public class Line extends GraphicalObject
{
  private final Point2D endPosition;
  private final Point2D endDisplayOffset;

  public Line(final Point2D startPosition,
              final Point2D endPosition,
              final Color color, final Stroke stroke)
  {
    this(startPosition, ZERO_POINT,
         endPosition, ZERO_POINT,
         color, stroke);
  }

  public Line(final Point2D startPosition,
              final Point2D startDisplayOffset,
              final Point2D endPosition,
              final Point2D endDisplayOffset,
              final Color color, final Stroke stroke)
  {
    super(startPosition, startDisplayOffset, color, stroke);
    Objects.requireNonNull(endPosition);
    Objects.requireNonNull(endDisplayOffset);
    this.endPosition = endPosition;
    this.endDisplayOffset = endDisplayOffset;
  }

  public Point2D getStartPosition()
  {
    return getPosition();
  }

  public Point2D getStartDisplayOffset()
  {
    return getDisplayOffset();
  }

  public Point2D getEndPosition()
  {
    return endPosition;
  }

  public Point2D getEndDisplayOffset()
  {
    return endDisplayOffset;
  }

  @Override
  public double getLowerBoundX()
  {
    return Math.min(getPosition().getX(), endPosition.getX());
  }

  @Override
  public double getUpperBoundX()
  {
    return Math.max(getPosition().getX(), endPosition.getX());
  }

  @Override
  public double getLowerBoundY()
  {
    return Math.min(getPosition().getY(), endPosition.getY());
  }

  @Override
  public double getUpperBoundY()
  {
    return Math.max(getPosition().getY(), endPosition.getY());
  }

  @Override
  public void draw(final AffineTransform affineTransform,
                   final Graphics g,
                   final Color defaultColor,
                   final Stroke defaultStroke)
  {
    super.draw(affineTransform, g, defaultColor, defaultStroke);
    final Point2D position1 = getDisplayPosition(affineTransform);
    final Point2D offset1 = getStartDisplayOffset();
    final Point2D position2 = affineTransform.transform(endPosition, null);
    final Point2D offset2 = getEndDisplayOffset();
    g.drawLine((int)(position1.getX() + offset1.getX()),
               (int)(position1.getY() + offset1.getY()),
               (int)(position2.getX() + offset2.getX()),
               (int)(position2.getY() + offset2.getY()));
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
