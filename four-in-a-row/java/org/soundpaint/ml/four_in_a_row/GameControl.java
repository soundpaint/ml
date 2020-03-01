/*
 * @(#)GameControl.java 1.00 20/02/28
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

import java.io.IOException;
import java.util.HashSet;

public class GameControl
{
  private final GameModel game;
  private final PlayPolicy playerRed, playerBlue;

  public GameControl()
  {
    game = new GameModel("demogame");
    playerRed = new RandomPlayPolicy(game, 0);
    playerBlue = new RandomPlayPolicy(game, 1);
  }

  private static final String FILENAME = "game.tmp.json";

  public void run() throws IOException
  {
    Player winner = null;
    PlayPolicy player1 = playerRed;
    PlayPolicy player2 = playerBlue;
    while (winner == null) {
      final int column = player1.chooseColumn();
      winner = game.insertChip(column);
      System.out.println(game.toString());
      final PlayPolicy playerSwap = player1;
      player1 = player2;
      player2 = playerSwap;
    }
    System.out.println("winner is: " + winner);
    final HashSet<Integer> winningDraws = game.getWinningDraws();
    System.out.println(game.toString(winningDraws));
    System.out.println(game.toString());
    System.out.println("[write to file & re-read from file...]");
    GameSerializer.serializeToFile(game, FILENAME);
    final GameModel savedGame = GameSerializer.deserializeFromFile(FILENAME);
    System.out.println(savedGame.toString());
    System.out.println(savedGame.toString(winningDraws));
  }

  public static void main(String argv[]) throws IOException
  {
    new GameControl().run();
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
