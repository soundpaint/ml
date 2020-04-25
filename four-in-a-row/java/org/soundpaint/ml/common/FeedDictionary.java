/*
 * @(#)FeedDictionary.java 1.00 20/04/25
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

import java.util.HashMap;

public class FeedDictionary
{
  final HashMap<Object, Object> dictionary;

  public FeedDictionary()
  {
    dictionary = new HashMap<>();
  }

  public <T>T put(final Placeholder<T> key,
                  final T value)
  {
    return (T)dictionary.put(key, value);
  }

  public <T>T get(final Placeholder<T> key)
  {
    return (T)dictionary.get(key);
  }

  public <T>void remove(final Placeholder<T> key)
  {
    dictionary.remove(key);
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
