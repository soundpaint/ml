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
  protected List<U> inputValues;
  private List<? extends Node<?, U>> inputNodes;
  private Node<U, V> outputNode;

  private Operation()
  {
    throw new UnsupportedOperationException("unsupported default constructor");
  }

  public Operation(final String id, final List<? extends Node<?, U>> inputNodes)
  {
    super(id);
    if (inputNodes == null) {
      throw new NullPointerException("inputNodes");
    }
    this.inputNodes = inputNodes;
    outputNode = null;
    for (final Node<?, U> inputNode : inputNodes) {
      inputNode.getOperations().add(this);
    }
    inputValues = new ArrayList<U>();
    Graph.getDefaultInstance().add(this);
  }

  public List<? extends Node<?, U>> getInputNodes()
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

  abstract public V performOperation();

  @Override
  public void update(final FeedDictionary feedDictionary)
  {
    inputValues.clear();
    for (final Node<?, U> inputNode : inputNodes) {
      inputValues.add(inputNode.getOutputValue());
    }
    setOutputValue(performOperation());
  }

  private String inputNodesToString()
  {
    final StringBuilder s = new StringBuilder();
    for (final Node<?, U> inputNode : getInputNodes()) {
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
    for (final U inputValue : inputValues) {
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
