/*
 * @(#)Node.java 1.00 20/03/08
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

public abstract class Node<U, V>
{
  private final String id;
  private List<Operation<V, ?>> operations;
  private V outputValue;

  public Node()
  {
    this("node");
  }

  public Node(final String id)
  {
    if (id == null) {
      throw new NullPointerException("id");
    }
    this.id = id + "-" + Uid.createUniqueId();
    operations = new ArrayList<Operation<V, ?>>();
  }

  public String getId()
  {
    return id;
  }

  public List<Operation<V, ?>> getOperations()
  {
    return operations;
  }

  public void setOutputValue(final V value)
  {
    this.outputValue = value;
  }

  public V getOutputValue()
  {
    return outputValue;
  }

  abstract void update();

  public String toString()
  {
    final StringBuilder s = new StringBuilder();
    s.append(id);
    s.append("(");
    s.append("out=" + getOutputValue());
    s.append(")");
    return s.toString();
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
