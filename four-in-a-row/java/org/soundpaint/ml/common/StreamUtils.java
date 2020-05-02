/*
 * @(#)StreamUtils.java 1.00 20/05/01
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

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamUtils
{
  /**
   * Zips two streams into one, using the user specified zipper
   * function.
   *
   * Implementation of this method has been inspired by an early
   * version of the java streams specification.
   *
   * @param a The first stream.
   * @param b The second stream.
   * @param zipper Function.  A BiFunction that zips a single element
   * x of stream a of type A and a single given element y of stream b
   * of type B into some object that encapsulates x and y e.g. into a
   * Pair object.
   */
  public static <A, B, C> Stream<C>
    zip(final Stream<? extends A> a,
        final Stream<? extends B> b,
        final BiFunction<? super A, ? super B, ? extends C> zipper)
  {
    Objects.requireNonNull(zipper);
    final Spliterator<? extends A> aSpliterator =
      Objects.requireNonNull(a).spliterator();
    final Spliterator<? extends B> bSpliterator =
      Objects.requireNonNull(b).spliterator();

    // Zipping looses DISTINCT and SORTED characteristics
    final int characteristics =
      aSpliterator.characteristics() & bSpliterator.characteristics() &
      ~(Spliterator.DISTINCT | Spliterator.SORTED);

    final long zipSize = ((characteristics & Spliterator.SIZED) != 0)
      ? Math.min(aSpliterator.getExactSizeIfKnown(),
                 bSpliterator.getExactSizeIfKnown())
      : -1;

    final Iterator<A> aIterator = Spliterators.iterator(aSpliterator);
    final Iterator<B> bIterator = Spliterators.iterator(bSpliterator);
    final Iterator<C> cIterator = new Iterator<C>() {
        @Override
        public boolean hasNext() {
          return aIterator.hasNext() && bIterator.hasNext();
        }

        @Override
        public C next() {
          return zipper.apply(aIterator.next(), bIterator.next());
        }
      };

    final Spliterator<C> split =
      Spliterators.spliterator(cIterator, zipSize, characteristics);
    return StreamSupport.stream(split, a.isParallel() || b.isParallel());
  }

  /**
   * Zips two streams into one, using the user specified zipper
   * function.
   *
   * Implementation of this method has been inspired by an early
   * version of the java streams specification.
   *
   * @param a The first stream.
   * @param b The second stream.
   * @param zipper Function.  A BiFunction that zips a single element
   * x of stream a of type A and a single given element y of stream b
   * of type B into some object that encapsulates x and y e.g. into a
   * Pair object.
   */
  public static <A, B, C> List<C>
    zipToList(final Stream<? extends A> a,
              final Stream<? extends B> b,
              final BiFunction<? super A, ? super B, ? extends C> zipper)
  {
    //return zip(a, b, zipper).toArray(size -> new C[size]);
    return zip(a, b, zipper).collect(Collectors.toList());
  }

  public static class Pair<A, B>
  {
    public A a;
    public B b;

    public Pair(final A a, final B b)
    {
      this.a = a;
      this.b = b;
    }
  }

  public static class PairFactory<A, B>
    implements BiFunction<A, B, Pair<A, B>>
  {
    public Pair<A, B> apply(final A a, final B b)
    {
      return new Pair<A, B>(a, b);
    }
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
