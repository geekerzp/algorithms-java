package com.algorithms.search;

import com.algorithms.util.StdIn;
import com.algorithms.util.StdOut;
import com.algorithms.sort.Queue;

import java.util.NoSuchElementException;

public class BinarySearchST<Key extends Comparable<Key>, Value> {
    private static final int INIT_CAPACITY = 2;
    private Key[] keys;
    private Value[] vals;
    private int n = 0;

    public BinarySearchST() {
        this(INIT_CAPACITY);
    }

    public BinarySearchST(int capacity) {
        keys = (Key[]) new Comparable[capacity];
        vals = (Value[]) new Object[capacity];
    }

    private void resize(int capacity) {
        assert capacity >= n;
        Key[] tempk = (Key[]) new Comparable[capacity];
        Value[] tempv = (Value[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            tempk[i] = keys[i];
            tempv[i] = vals[i];
        }
        keys = tempk;
        vals = tempv;
    }

    public int size() {
        return n;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean contains(Key key) {
        if (key == null)
            throw new NullPointerException("argument to contains() is null");
        return get(key) != null;
    }

    public Value get(Key key) {
        if (key == null)
            throw new NullPointerException("argument to get() is null");
        if (isEmpty())
            return null;
        int i = rank(key);
        if (i < n && keys[i].compareTo(key) == 0)
            return vals[i];
        return null;
    }

    /**
     * Returns the number of keys in this symbol table strictly less than {@code key}.
     */
    public int rank(Key key) {
        if (key == null)
            throw new NullPointerException("argument to rank() is null");

        int lo = 0, hi = n - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int cmp = key.compareTo(keys[mid]);
            if (cmp < 0)
                hi = mid - 1;
            else if (cmp > 0)
                lo = mid + 1;
            else
                return mid;
        }
        return lo;
    }

    public void put(Key key, Value val) {
        if (key == null)
            throw new NullPointerException("argument to put() is null");

        if (val == null) {
            delete(key);
            return;
        }

        int i = rank(key);

        // key is already in table
        if (i < n && keys[i].compareTo(key) == 0) {
            vals[i] = val;
            return;
        }

        // insert new key-value pair
        if (n == keys.length)
            resize(2 * keys.length);
        for (int j = n; j > i; j--) {
            keys[j] = keys[j - 1];
            vals[j] = vals[j - 1];
        }
        keys[i] = key;
        vals[i] = val;
        n++;

        assert  check();
    }

    public void delete(Key key) {
        if (key == null)
            throw new NullPointerException("argument to delete() is null");
        if (isEmpty())
            return;

        int i = rank(key);

        // key not in table
        if (i == n || keys[i].compareTo(key) != 0) {
            return;
        }

        for (int j = i; j < n - 1; j++) {
            keys[j] = keys[j + 1];
            vals[j] = vals[j + 1];
        }

        n--;
        keys[n] = null;
        vals[n] = null;

        // resize if 1/4 full
        if (n > 0 && n == keys.length / 4) {
            resize(keys.length / 2);
        }

        assert check();
    }

    public void deleteMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Symbol table underflow error");
        }
        delete(min());
    }

    public void deleteMax() {
        if (isEmpty()) {
            throw new NoSuchElementException("Symbol table underflow error");
        }
        delete(max());
    }

    public Key min() {
        if (isEmpty()) {
            return null;
        }
        return keys[0];
    }

    public Key max() {
        if (isEmpty()) {
            return null;
        }
        return keys[n - 1];
    }

    public Key select(int k) {
        if (k < 0 || k >= n) {
            return null;
        }
        return keys[k];
    }

    /**
     * Returns the largest key in this symbol table less than or equal to {@code key}.
     */
    public Key floor(Key key) {
        if (key == null) {
            throw new NullPointerException("argument to floor() is null");
        }
        int i = rank(key);
        if (i < n && key.compareTo(keys[i]) == 0) {
            return keys[i];
        }
        if (i == 0) {
            return null;
        } else {
            return keys[i - 1];
        }
    }

    /**
     * Returns the smallest key in this symbol table greater than or equal to {@code key}.
     */
    public Key ceiling(Key key) {
        if (key == null) {
            throw new NullPointerException("argument to ceiling() is null");
        }
        int i = rank(key);
        if (i == n) {
            return null;
        } else {
            return keys[i];
        }
    }

    /**
     * Returns the number of keys in this symbol table in the specified rang.
     */
    public int size(Key lo, Key hi) {
        if (lo == null) {
            throw new NullPointerException("first argument to size() is null");
        }
        if (hi == null) {
            throw new NullPointerException("second argument to size() is null");
        }

        if (lo.compareTo(hi) > 0) {
            return 0;
        }

        if (contains(hi)) {
            return rank(hi) - rank(lo) + 1;
        } else {
            return rank(hi) - rank(lo);
        }
    }

    public Iterable<Key> keys() {
        return keys(min(), max());
    }

    public Iterable<Key> keys(Key lo, Key hi) {
        if (lo == null) {
            throw new NullPointerException("first argument to size() is null");
        }
        if (hi == null) {
            throw new NullPointerException("second argument to size() is null");
        }

        Queue<Key> queue = new Queue<Key>();
        if (lo.compareTo(hi) > 0) {
            return queue;
        }
        for (int i = rank(lo); i < rank(hi); i++) {
            queue.enqueue(keys[i]);
        }
        if (contains(hi)) {
            queue.enqueue(keys[rank(hi)]);
        }
        return queue;
    }

    public boolean check() {
        return isSorted() && rankCheck();
    }

    private boolean isSorted() {
        for (int i = 1; i < size(); i++) {
            if (keys[i].compareTo(keys[i - 1]) < 0) {
                return false;
            }
        }
        return true;
    }

    private boolean rankCheck() {
        for (int i = 0; i < size(); i++) {
            if (i != rank(select(i))) {
                return false;
            }
        }
        for (int i = 0; i < size(); i++) {
            if (keys[i].compareTo(select(rank(keys[i]))) != 0) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        BinarySearchST<String, Integer> st = new BinarySearchST<>();
        for (int i = 0; !StdIn.isEmpty(); i++) {
            String key = StdIn.readString();
            st.put(key, i);
        }
        for (String s : st.keys()) {
            StdOut.println(s + " " + st.get(s));
        }
    }
}
