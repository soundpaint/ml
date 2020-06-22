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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.function.Function;

public class GameControl
{
  private static final boolean DEBUG = false;
  private final String dataSetsPath;
  private final GameModel game;
  private final PlayPolicy playerRed, playerBlue;

  private GameControl()
  {
    throw new UnsupportedOperationException("unsupported default constructor");
  }

  public GameControl(final String dataSetsPath)
  {
    if (dataSetsPath == null) {
      throw new NullPointerException("dataSetsPath");
    }
    this.dataSetsPath = dataSetsPath;
    game = new GameModel("random-play-policy-data-set-generator");
    playerRed = new RandomPlayPolicy(game, 0);
    playerBlue = new RandomPlayPolicy(game, 1);
  }

  private Player playNewGame()
  {
    game.clear();
    Player winner = null;
    PlayPolicy player1 = playerRed;
    PlayPolicy player2 = playerBlue;
    while (winner == null) {
      final int column = player1.chooseColumn();
      winner = game.insertChip(column);
      final PlayPolicy playerSwap = player1;
      player1 = player2;
      player2 = playerSwap;
    }
    if (DEBUG) {
      final HashSet<Integer> winningDraws = game.getWinningDraws();
      System.out.println(game.toString(winningDraws));
    }
    return winner;
  }

  private void printGameStatistics(final Player winner)
  {
    System.out.println("winner is: " + winner);
    final HashSet<Integer> winningDraws = game.getWinningDraws();
    System.out.println(game.toString(winningDraws));
    System.out.println(game.toString());
  }

  private void removeOldDataSet(final File dataSetDir)
    throws IOException
  {
    System.out.print("clearing data set \"" + dataSetDir.getName() +
                     "\"… ");
    final File[] dataRecords =
      dataSetDir.listFiles(DataRecordFilter.getDefaultInstance());
    for (final File dataRecord : dataRecords) {
      dataRecord.delete();
    }
    System.out.println("done");
  }

  private void createDataRecord(final File dataSetDir,
                                final int recordId)
    throws IOException
  {
    final File dataRecordFile =
      new File(dataSetDir, "record_" + recordId + ".json");
    final Player winner = playNewGame();
    if (DEBUG) printGameStatistics(winner);
    GameSerializer.serializeToFile(game, dataRecordFile);
  }

  private void createNewDataSet(final File dataSetDir, final int size)
    throws IOException
  {
    System.out.print("creating data set \"" + dataSetDir.getName() +
                     "\"… ");
    for (int recordId = 0; recordId < size; recordId++) {
      createDataRecord(dataSetDir, recordId);
    }
    System.out.println("done");
  }

  private void createDataSet(final String dataSetId, final int size)
    throws IOException
  {
    final Path dataSetPath = Paths.get(dataSetsPath, dataSetId);
    final File dataSetDir = dataSetPath.toFile();
    dataSetDir.mkdirs();
    removeOldDataSet(dataSetDir);
    createNewDataSet(dataSetDir, size);
  }

  private Function<File, Void> checkDataRecord =
    file ->
  {
    try {
      final GameModel savedGame = GameSerializer.deserializeFromFile(file);
      if (DEBUG) {
        System.out.println(savedGame.toString());
        final HashSet<Integer> winningDraws = game.getWinningDraws();
        System.out.println(savedGame.toString(winningDraws));
      }
    } catch (final FileNotFoundException e) {
      throw new RuntimeException(e);
    }
    return null;
  };

  private void checkDataSet(final String dataSetId)
  {
    System.out.print("checking data set \"" + dataSetId + "\"… ");
    final Path dirPath = Paths.get(dataSetsPath, dataSetId);
    final File[] dataRecords =
      dirPath.toFile().listFiles(DataRecordFilter.getDefaultInstance());
    for (final File dataRecord : dataRecords) {
      checkDataRecord.apply(dataRecord);
    }
    System.out.println("done");
  }

  public void run() throws IOException
  {
    createDataSet("training-set", 10);
    checkDataSet("training-set");
    createDataSet("testing-set", 10);
    checkDataSet("testing-set");
    createDataSet("hold-out-set", 10);
    checkDataSet("hold-out-set");
  }

  public static void main(String argv[]) throws IOException
  {
    new GameControl("data-sets").run();
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
