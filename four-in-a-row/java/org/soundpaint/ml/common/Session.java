/*
 * @(#)Session.java 1.00 20/03/08
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
import java.util.HashMap;
import java.util.List;

public class Session
{
  public Object run(final Node<?, ?> targetNode)
  {
    final var targetNodes = new ArrayList<Node<?, ?>>();
    targetNodes.add(targetNode);
    return run(targetNodes).get(0);
  }

  public List<Object> run(final List<Node<?, ?>> targetNodes)
  {
    final List<Node<?, ?>> nodesPostOrder = Node.traversePostOrder(targetNodes);
    for (final Node<?, ?> node : nodesPostOrder) {
      node.update();
      /*
      if (node.getOutputValue() instanceof List) {
        // TODO: convert list into array:
        // node.setOutputValue(list2array((List<?>)node.getOuputValue()));
      }
      */
    }
    final List<Object> result = new ArrayList<Object>();
    for (final Node<?, ?> targetNode : targetNodes) {
      result.add(targetNode.getOutputValue());
    }
    return result;
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
