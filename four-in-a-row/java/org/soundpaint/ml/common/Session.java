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

public class Session<T>
{
  public T run(final Operation<T, T> operation,
               final HashMap<Placeholder<T, T>, T> feedDictionary)
  {
    final List<Node<T, T>> nodesPostOrder = operation.traversePostOrder();
    for (Node<T, T> node : nodesPostOrder) {
      if (node instanceof Placeholder) {
        node.setOutputValue(feedDictionary.get((Placeholder<T, T>)node));
      } else if (node instanceof Variable) {
        node.setOutputValue(((Variable<T, T>)node).getValue());
      } else { // (node instanceof Operation)
        final Operation<T, T> operationNode = (Operation<T, T>)node;
        final List<T> inputValues = new ArrayList<T>();
        for (final Node<T, T> inputNode : operationNode.getInputNodes()) {
          inputValues.add(inputNode.getOutputValue());
        }
        operationNode.setInputValues(inputValues);
        node.setOutputValue(operationNode.compute(inputValues));
      }
      /*
      if (node.getOutputValue() instanceof List) {
        // TODO: convert list into array:
        // node.setOutputValue(list2array((List<T>)node.getOuputValue()));
      }
      */
    }
    return operation.getOutputValue();
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
