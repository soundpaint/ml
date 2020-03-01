/*
 * @(#)Player.java 1.00 20/02/28
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

public enum Player
{
  RED('O'),
  BLUE('X');

  private final char mnemonic;
  private final String strMnemonic;

  Player(final char mnemonic)
  {
    this.mnemonic = mnemonic;
    this.strMnemonic = String.valueOf(mnemonic);
  }

  public char getMnemonic()
  {
    return mnemonic;
  }

  public String toString()
  {
    return strMnemonic;
  }

  public static Player lookup(final char mnemonic)
  {
    for (final Player player : Player.values()) {
      if (player.mnemonic == mnemonic) {
        return player;
      }
    }
    throw new IllegalArgumentException("no such player: " + mnemonic);
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
