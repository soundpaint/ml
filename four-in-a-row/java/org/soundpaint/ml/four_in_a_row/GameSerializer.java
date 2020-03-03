/*
 * @(#)GameSerializer.java 1.00 20/02/28
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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;

public class GameSerializer
{
  public static void serializeToFile(final GameModel game,
                                     final File file)
    throws IOException
  {
    final FileWriter writer = new FileWriter(file);
    serializeToWriter(game, writer);
  }

  public static void serializeToFile(final GameModel game,
                                     final String filePath)
    throws IOException
  {
    final FileWriter writer = new FileWriter(filePath);
    serializeToWriter(game, writer);
  }

  public static void serializeToWriter(final GameModel game,
                                       final Writer writer)
    throws IOException
  {
    writer.write(serializeToString(game));
    writer.close();
  }

  public static String serializeToString(final GameModel game)
  {
    final int rows = game.getRows();
    final int columns = game.getColumns();
    final Integer[] draws = new Integer[rows * columns];
    int drawn = 0;
    for (int row = 0; row < rows; row++) {
      for (int column = 0; column < columns; column++) {
        final Integer draw = game.getDrawAt(row, column);
        if (draw != null) {
          if (draws[draw] != null) {
            throw new IllegalStateException("data corrupted: duplicate draw " +
                                            draw);
          }
          draws[draw] = column;
          drawn++;
        }
      }
    }
    final JsonArrayBuilder gameDraws = Json.createArrayBuilder();
    for (int draw = 0; draw < drawn; draw++) {
      final Integer column = draws[draw];
      if (column == null) {
        throw new IllegalStateException("data corrupted: missing draw " + draw);
      }
      gameDraws.add(column);
    }
    final JsonObjectBuilder obj = Json.createObjectBuilder()
      .add("id", game.getId())
      .add("rows", game.getRows())
      .add("columns", game.getColumns())
      .add("min-match-count", game.getMinMatchCount())
      .add("draws", gameDraws);
      return obj.build().toString();
  }

  public static GameModel deserializeFromFile(final File file)
    throws FileNotFoundException
  {
    return deserializeFromReader(new FileReader(file));
  }

  public static GameModel deserializeFromFile(final String filePath)
    throws FileNotFoundException
  {
    return deserializeFromReader(new FileReader(filePath));
  }

  public static GameModel deserializeFromString(final String s)
  {
    return deserializeFromReader(new StringReader(s));
  }

  public static GameModel deserializeFromReader(final Reader reader)
  {
    final JsonReader jsonReader = Json.createReader(reader);
    final JsonObject obj = jsonReader.readObject();
    jsonReader.close();
    final String id = obj.getString("id");
    final int rows = obj.getInt("rows");
    final int columns = obj.getInt("columns");
    final int minMatchCount = obj.getInt("min-match-count");
    final GameModel game = new GameModel(id, rows, columns, minMatchCount);
    final JsonArray gameDraws = obj.getJsonArray("draws");
    for (JsonValue draw : gameDraws) {
      final JsonNumber column = (JsonNumber)draw;
      if (column == null) {
        throw new IllegalStateException("data corrupted: missing draw " + draw);
      } else {
        game.insertChip(column.intValue());
      }
    }
    return game;
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
