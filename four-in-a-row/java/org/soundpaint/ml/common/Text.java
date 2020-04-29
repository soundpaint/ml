/*
 * @(#)Text.java 1.00 20/04/26
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

public class Text extends GraphicalObject
{
  private final String text;

  public Text(final Point2D startPosition, final String text,
              final Color color)
  {
    super(startPosition, color, null);
    this.text = text;
  }

  // TODO: Compute bounding box of text.  For now,
  // we just consider the start position.

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
    final Point2D p1 = getDisplayPosition(affineTransform);
    g.drawString(text, (int)p1.getX(), (int)p1.getY());
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
