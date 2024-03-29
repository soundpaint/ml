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

import java.util.List;
import java.util.stream.Collectors;

public class Session
{
  public Object run(final Node<?, ?> targetNode,
                    final FeedDictionary feedDictionary)
  {
    return run(List.of(targetNode), feedDictionary).get(0);
  }

  public List<Object> run(final List<Node<?, ?>> targetNodes,
                          final FeedDictionary feedDictionary)
  {
    final List<Node<?, ?>> nodesPostOrder = Node.traversePostOrder(targetNodes);
    for (final Node<?, ?> node : nodesPostOrder) {
      node.update(feedDictionary);
      /*
      if (node.getOutputValue() instanceof List) {
        // TODO: convert list into array:
        // node.setOutputValue(list2array((List<?>)node.getOuputValue()));
      }
      */
    }
    return
      targetNodes.stream().
      map(Node<?, ?>::getOutputValue).collect(Collectors.toList());
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
