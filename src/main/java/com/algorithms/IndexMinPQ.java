package com.algorithms;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class IndexMinPQ<Key extends Comparable<Key>> implements Iterable<Integer> {
  private int maxN; // maximum number of elements on PQ
  private int n; // number of elements on PQ
  private int[] pq; // binary heap using 1-based indexing
  private int[] qp; // inverse of pq - qp[pq[i]] = pq[qp[i]] = i
  private Key[] keys; // keys[i] = priority of i

  /**
   * Initializes an empty indexed priority queue with indices between {@code 0} and {@code maxN -
   * 1}.
   *
   * @param maxN the keys on this priority queue are index from {@code 0} {@code maxN - 1}
   * @throws IllegalArgumentException if {@code maxN < 0}
   */
  public IndexMinPQ(int maxN) {
    if (maxN < 0) throw new IllegalArgumentException();
    this.maxN = maxN;
    n = 0;
    keys = (Key[]) new Comparable[maxN + 1];
    pq = new int[maxN + 1];
    qp = new int[maxN + 1];
    for (int i = 0; i <= maxN; i++) {
      qp[i] = -1;
    }
  }

  /**
   * Returns true if this priority queue is empty.
   *
   * @return {@code true} if this priority queue is empty; {@code false} otherwise
   */
  public boolean isEmpty() {
    return n == 0;
  }

  /**
   * Is {@code i} an index on this priority queue?
   *
   * @param i an index
   * @return {@code true} if {@code i} is an index on this priority queue; {@code false} otherwise
   * @throws IndexOutOfBoundsException unless {@code 0 <= i < maxN}
   */
  public boolean contains(int i) {
    if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
    return qp[i] != -1;
  }

  /**
   * Returns the number of keys on this priority queue.
   *
   * @return the number of keys on this priority queue
   */
  public int size() {
    return n;
  }

  /**
   * Associates key with index {@code i}.
   *
   * @param i an index
   * @param key the key to associate with index {@code i}
   * @throws IndexOutOfBoundsException unless {@code 0 <= i < maxN}
   * @throws IllegalArgumentException if there already is an item associated with index {@code i}.
   */
  public void insert(int i, Key key) {
    if (i < 0 || i >= maxN) {
      throw new IndexOutOfBoundsException();
    }
    if (contains(i)) {
      throw new IllegalArgumentException("index is already in the priority queue");
    }
    n++;
    qp[i] = n;
    pq[n] = i;
    keys[i] = key;
    swim(n);
  }

  /**
   * Returns an index associated with a minimum key.
   *
   * @return an index associated with a minimum key
   * @throws NoSuchElementException if this priority queue is empty
   */
  public int minIndex() {
    if (n == 0) {
      throw new NoSuchElementException("Priority queue underflow");
    }
    return pq[1];
  }

  /**
   * Returns a minimum key.
   *
   * @return a minimum key
   * @throws NoSuchElementException if this priority queue is empty
   */
  public Key minKey() {
    if (n == 0) {
      throw new NoSuchElementException("Priority queue underflow");
    }
    return keys[pq[1]];
  }

  /**
   * Removes a minimum key and returns its associated index.
   *
   * @return an index associated with a minimum key
   * @throws NoSuchElementException if this priority queue is empty
   */
  public int delMin() {
    if (n == 0) {
      throw new NoSuchElementException("Priority queue underflow");
    }
    int min = pq[1];
    exch(1, n--);
    sink(1);
    assert min == pq[n + 1];
    qp[min] = -1; // delete
    keys[min] = null; // to help with garbage collection
    pq[n + 1] = -1; // not needed
    return min;
  }

  /**
   * Returns the key associated with index {@code i}.
   *
   * @param i the index of the key to return
   * @return the key associated with index {@code i}
   * @throws NoSuchElementException no key is associated with index {@code i}
   */
  public Key keyOf(int i) {
    if (i < 0 || i >= maxN) {
      throw new IndexOutOfBoundsException();
    }
    if (!contains(i)) {
      throw new NoSuchElementException("index is not in the priority queue");
    }
    return keys[i];
  }

  /**
   * Change the associated with index {@code i} to the specified value.
   *
   * @param i the index of the key th change
   * @param key change the key associated with index {@code i} to this key
   * @throws IndexOutOfBoundsException unless {@code 0 <= i < maxN}
   */
  public void changeKey(int i, Key key) {
    if (i < 0 || i >= maxN) {
      throw new IndexOutOfBoundsException();
    }
    if (!contains(i)) {
      throw new NoSuchElementException("index is not in the priority queue");
    }
    keys[i] = key;
    swim(qp[i]);
    sink(qp[i]);
  }

  /**
   * Change the key associated with index {@code i} to the specified value.
   *
   * @param i the index of the key to change
   * @param key change the key associated with index {@code i} to this key
   * @throws IndexOutOfBoundsException unless {@code 0 <= i < maxN}
   * @deprecated Replaced by {@code changeKey(int, Key)}.
   */
  @Deprecated
  public void change(int i, Key key) {
    changeKey(i, key);
  }

  /** Decrease the key associated with index {@code i} to the specified value. */
  public void decreaseKey(int i, Key key) {
    if (i < 0 || i >= maxN) {
      throw new IndexOutOfBoundsException("index is not in the priority queue");
    }
    if (!contains(i)) {
      throw new NoSuchElementException("index is not in the priority queue");
    }
  }
}
