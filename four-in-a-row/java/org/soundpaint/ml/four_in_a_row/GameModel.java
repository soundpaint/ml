/*
 * @(#)GameModel.java 1.00 20/02/28
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class GameModel
{
  private static final int[][] DIRECTIONS = {
    {+1, +0}, // horizontally
    {+0, +1}, // vertically
    {+1, +1}, // diagonally
    {-1, +1}, // reverse diagonally
  };

  private final String LINE_SEPARATOR;
  private final String id;
  private final Integer[][] board;
  private final int[] height;
  private int fullColumns;
  private final int rows, columns;
  private final int minMatchCount;
  private int drawn;
  private Player nextPlayer;
  private Player winner;
  private int winDrawRow, winDrawColumn;
  private List<GameView> listeners;

  private GameModel()
  {
    throw new UnsupportedOperationException("unsupported default constructor");
  }

  public GameModel(final String id)
  {
    this(id, 6, 7, 4);
  }

  public GameModel(final String id, final int rows, final int columns,
                   final int minMatchCount)
  {
    if (id == null) {
      throw new NullPointerException("id");
    }
    if (rows <= 0) {
      throw new IllegalArgumentException("rows <= 0: " + rows);
    }
    if (columns <= 0) {
      throw new IllegalArgumentException("columns <= 0: " + columns);
    }
    if (minMatchCount <= 3) {
      throw new IllegalArgumentException("minMatchCount <= 3: " +
                                         minMatchCount);
    }
    this.id = id;
    this.rows = rows;
    this.columns = columns;
    this.minMatchCount = minMatchCount;
    this.LINE_SEPARATOR = createLineSeparator();
    board = new Integer[rows][columns];
    height = new int[columns];
    fullColumns = 0;
    drawn = 0;
    nextPlayer = Player.RED;
    winner = null;
    winDrawRow = winDrawColumn = -1;
    listeners = new ArrayList<GameView>();
  }

  public String getId() { return id; }

  public int getRows() { return rows; }

  public int getColumns() { return columns; }

  public int getMinMatchCount() { return minMatchCount; }

  public void addListener(final GameView listener)
  {
    listeners.add(listener);
  }

  public void removeListener(final GameView listener)
  {
    listeners.remove(listener);
  }

  private void boardChanged(final int row, final int column, final Integer draw)
  {
    for (final GameView listener : listeners) {
      listener.boardChanged(row, column, draw);
    }
  }

  public int getHeightOfColumn(final int column)
  {
    return height[column];
  }

  public boolean isFullColumn(final int column)
  {
    return height[column] >= rows;
  }

  public int getFullColumns()
  {
    return fullColumns;
  }

  public Integer getDrawAt(final int row, final int column)
  {
    return board[row][column];
  }

  private Player getPlayerFromDraw(final Integer draw)
  {
    if (draw == null) {
      return null;
    }
    return draw % 2 == 0 ? Player.RED : Player.BLUE;
  }

  private Player getPlayerAt(final int row, final int column)
  {
    final Integer draw = board[row][column];
    return getPlayerFromDraw(draw);
  }

  private void updateNextPlayer()
  {
    if ((winner != null) || (drawn >= rows * columns)) {
      nextPlayer = null;
    } else {
      switch (nextPlayer) {
      case RED: nextPlayer = Player.BLUE; break;
      case BLUE: nextPlayer = Player.RED; break;
      default: throw new IllegalArgumentException("can not draw nothing");
      }
    }
  }

  public Player getNextPlayer()
  {
    return nextPlayer;
  }

  private boolean inRange(final int row, final int column)
  {
    return
      (row >= 0) &&
      (row < rows) &&
      (column >= 0) &&
      (column < columns);
  }

  private boolean tryMatch(final int rowStart, final int columnStart,
                           final int dx, final int dy,
                           final HashSet<Integer> matchingDraws)
  {
    int matchCount = -1;
    final Player toBeMatched = getPlayerAt(rowStart, columnStart);
    if (toBeMatched == null) {
      throw new IllegalArgumentException("useless matching for null player");
    }
    int row = rowStart;
    int column = columnStart;
    boolean tryNext = true;
    do {
      if (matchingDraws != null) {
        matchingDraws.add(board[row][column]);
      }
      matchCount++;
      row += dy;
      column += dx;
      tryNext =
        inRange(row, column) && (getPlayerAt(row, column) == toBeMatched);
    } while (tryNext);
    row = rowStart;
    column = columnStart;
    tryNext = true;
    do {
      if (matchingDraws != null) {
        matchingDraws.add(board[row][column]);
      }
      matchCount++;
      row -= dy;
      column -= dx;
      tryNext =
        inRange(row, column) && (getPlayerAt(row, column) == toBeMatched);
    } while (tryNext);
    return matchCount >= minMatchCount;
  }

  private void updateWinner(final int row, final int column)
  {
    final Player toBeMatched = getPlayerAt(row, column);
    for (int[] direction : DIRECTIONS) {
      if (tryMatch(row, column, direction[0], direction[1], null)) {
        winner = toBeMatched;
        winDrawRow = row;
        winDrawColumn = column;
        return;
      }
    }
  }

  public Player insertChip(final int column)
  {
    if (nextPlayer == null) {
      throw new IllegalStateException("game already finished");
    }
    if ((column < 0) || (column >= columns)) {
      throw new IllegalArgumentException("column out of range: " + column);
    }
    final int row = height[column];
    if (row >= rows) {
      throw new IllegalArgumentException("column overflow");
    }
    board[row][column] = drawn++;
    boardChanged(row, column, drawn);
    height[column]++;
    if (height[column] == rows) {
      fullColumns++;
    }
    updateWinner(row, column);
    updateNextPlayer();
    return winner;
  }

  private String createLineSeparator()
  {
    final StringBuffer s = new StringBuffer();
    s.append("+");
    for (int column = 0; column < columns; column++) {
      s.append("-");
      s.append("+");
    }
    s.append(System.lineSeparator());
    return s.toString();
  }

  private String toString(final Player player)
  {
    return player != null ? player.toString() : " ";
  }

  public String toString()
  {
    return toString((HashSet<Integer>)null);
  }

  public String toString(final HashSet<Integer> markedDraws)
  {
    final StringBuilder s = new StringBuilder();
    s.append(LINE_SEPARATOR);
    for (int row = 0; row < rows; row++) {
      s.append("|");
      for (int column = 0; column < columns; column++) {
        final Integer draw = board[rows - row - 1][column];
        if ((markedDraws != null) && markedDraws.contains(draw)) {
          s.append("*");
        } else {
          s.append(toString(getPlayerFromDraw(draw)));
        }
        s.append("|");
      }
      s.append(System.lineSeparator());
    }
    s.append(LINE_SEPARATOR);
    return s.toString();
  }

  public HashSet<Integer> getWinningDraws()
  {
    if (winner == null) {
      throw new IllegalStateException("no winner available for matching");
    }
    final HashSet<Integer> matchingDraws = new HashSet<Integer>();
    final HashSet<Integer> allMatchingDraws = new HashSet<Integer>();
    for (int[] direction : DIRECTIONS) {
      matchingDraws.clear();
      if (tryMatch(winDrawRow, winDrawColumn,
                   direction[0], direction[1], matchingDraws)) {
        allMatchingDraws.addAll(matchingDraws);
      }
    }
    return allMatchingDraws;
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
