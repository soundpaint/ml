/*
 * @(#)Operation.java 1.00 20/03/08
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

public abstract class Operation<U, V> extends Node<U, V>
{
  private List<U> inputValues;
  private List<Node<U, V>> inputNodes;
  private Node<U, V> outputNode;

  private Operation()
  {
    throw new UnsupportedOperationException("unsupported default constructor");
  }

  public Operation(final String id, final List<Node<U, V>> inputNodes)
  {
    super(id);
    if (inputNodes == null) {
      throw new NullPointerException("inputNodes");
    }
    this.inputNodes = inputNodes;
    outputNode = null;
    for (final Node<U, V> node : inputNodes) {
      node.getOperations().add(this);
    }
    inputValues = new ArrayList<U>();
    Graph.getDefaultInstance().add(this);
  }

  private void recurse(final Node<U, V> node,
                       final List<Node<U, V>> nodesPostOrder)
  {
    if (node instanceof Operation) {
      final List<Node<U, V>> inputNodes =
        ((Operation<U, V>)node).getInputNodes();
      for (final Node<U, V> inputNode : inputNodes) {
        recurse(inputNode, nodesPostOrder);
      }
    }
    nodesPostOrder.add(node);
  }

  public List<Node<U, V>> traversePostOrder()
  {
    final List<Node<U, V>> nodesPostOrder = new ArrayList<Node<U, V>>();
    recurse(this, nodesPostOrder);
    return nodesPostOrder;
  }

  public List<Node<U, V>> getInputNodes()
  {
    return inputNodes;
  }

  public void setOutputNode(final Node<U, V> outputNode)
  {
    this.outputNode = outputNode;
  }

  public Node<U, V> getOutputNode()
  {
    return outputNode;
  }

  public void setInputValues(final List<U> inputValues)
  {
    this.inputValues.clear();
    this.inputValues.addAll(inputValues);
  }

  public List<U> getInputValues()
  {
    return inputValues;
  }

  abstract public V compute(final List<U> operands);

  private String inputNodesToString()
  {
    final StringBuilder s = new StringBuilder();
    for (final Node<U, V> inputNode : getInputNodes()) {
      if (s.length() > 0) {
        s.append(", ");
      }
      s.append(inputNode.getId());
    }
    return "(" + s + ")";
  }

  private String outputNodeToString()
  {
    final StringBuilder s = new StringBuilder();
    final Node<U, V> outputNode = getOutputNode();
    s.append(outputNode != null ? getOutputNode().getId() : outputNode);
    return "(" + s + ")";
  }

  private String inputValuesToString()
  {
    final StringBuilder s = new StringBuilder();
    for (final U inputValue : getInputValues()) {
      if (s.length() > 0) {
        s.append(", ");
      }
      s.append(inputValue);
    }
    return "(" + s + ")";
  }

  private String outputValueToString()
  {
    final StringBuilder s = new StringBuilder();
    final V outputValue = getOutputValue();
    s.append(outputValue);
    return "(" + s + ")";
  }

  public String toString()
  {
    final StringBuilder s = new StringBuilder();
    s.append(getId());
    s.append("(");
    s.append("inputNodes=" + inputNodesToString());
    s.append(", outputNode=" + outputNodeToString());
    s.append(", inputs=" + inputValuesToString());
    s.append(", output=" + outputValueToString());
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
