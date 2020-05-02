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
  public enum AlignmentX {
    LEFT, CENTER, RIGHT;

    public double getOffset(final int stringWidth)
    {
      switch (this) {
      case RIGHT:
        return -stringWidth;
      case CENTER:
        return -0.5 * stringWidth;
      case LEFT:
      default:
        return 0.0;
      }
    }
  };

  public enum AlignmentY {
    TOP, MIDDLE, BOTTOM;

    public double getOffset(final int fontHeight)
    {
      switch (this) {
      case TOP:
        return fontHeight;
      case MIDDLE:
        return 0.5 * fontHeight;
      case BOTTOM:
      default:
        return 0.0;
      }
    }
  };

  private final String text;
  private final AlignmentX alignmentX;
  private final AlignmentY alignmentY;

  public Text(final Point2D position, final String text,
              final Color color,
              final AlignmentX alignmentX, final AlignmentY alignmentY)
  {
    this(position, ZERO_POINT, text, color, alignmentX, alignmentY);
  }

  public Text(final Point2D position,
              final Point2D displayOffset,
              final String text,
              final Color color,
              final AlignmentX alignmentX,
              final AlignmentY alignmentY)
  {
    super(position, displayOffset, color, null);
    this.text = text;
    this.alignmentX = alignmentX;
    this.alignmentY = alignmentY;
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
    final Point2D position = getDisplayPosition(affineTransform);
    final Point2D offset = getDisplayOffset();
    final double alignmentXOffset =
      alignmentX.getOffset(g.getFontMetrics().stringWidth(text));
    final double alignmentYOffset =
      alignmentY.getOffset(g.getFontMetrics().getHeight());
    g.drawString(text,
                 (int)(position.getX() + offset.getX() + alignmentXOffset),
                 (int)(position.getY() + offset.getY() + alignmentYOffset));
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
