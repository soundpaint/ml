/*
 * @(#)Graph.java 1.00 20/03/08
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

import java.util.ArrayList;
import java.util.List;

public class Graph
{
  private static final Graph defaultInstance = new Graph();

  public static Graph getDefaultInstance()
  {
    return defaultInstance;
  }

  private final List<Operation<?, ?>> operations;
  private final List<Placeholder<?, ?>> placeholders;
  private final List<Variable<?, ?>> variables;

  private Graph()
  {
    operations = new ArrayList<Operation<?, ?>>();
    placeholders = new ArrayList<Placeholder<?, ?>>();
    variables = new ArrayList<Variable<?, ?>>();
  }

  public void add(final Operation<?, ?> operation)
  {
    operations.add(operation);
  }

  public void add(final Placeholder<?, ?> placeholder)
  {
    placeholders.add(placeholder);
  }

  public void add(final Variable<?, ?> variable)
  {
    variables.add(variable);
  }

  public void clear()
  {
    operations.clear();
    placeholders.clear();
    variables.clear();
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
