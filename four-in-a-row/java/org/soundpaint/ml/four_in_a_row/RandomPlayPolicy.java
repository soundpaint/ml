/*
 * @(#)RandomPlayPolicy.java 1.00 20/02/28
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
package org.soundpaint.ml.four_in_a_row;

import java.util.Random;

public class RandomPlayPolicy implements PlayPolicy
{
  private final GameModel game;
  private final Random random;

  public RandomPlayPolicy(final GameModel game, final long seed)
  {
    if (game == null) {
      throw new NullPointerException("game");
    }
    this.game = game;
    random = new Random(seed);
    random.setSeed(seed);
  }

  public int chooseColumn()
  {
    final int columns = game.getColumns();
    final int availableColumns = columns - game.getFullColumns();
    int availableColumn = random.nextInt(availableColumns);
    for (int column = 0; column < columns; column++) {
      if (!game.isFullColumn(column)) {
        availableColumn--;
        if (availableColumn == -1) {
          return column;
        }
      }
    }
    throw new IllegalStateException("corrupted available columns data");
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
