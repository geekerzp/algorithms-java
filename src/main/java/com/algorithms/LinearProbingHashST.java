package com.algorithms;

public class LinearProbingHashST<Key, Value> {
    private static final int INIT_CAPACITY = 4;

    private int n;         // number of key-value pairs in the table
    private int m = 16;    // size of linear-probing table
    private Key[] keys;    // the keys
    private Value[] vals;  // the values

    public LinearProbingHashST() {
        this(INIT_CAPACITY);
    }

    public LinearProbingHashST(int capacity) {
        m = capacity;
        n = 0;
        keys = (Key[])   new Object[m];
        vals = (Value[]) new Object[m];
    }

    public int size() {
        return n;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean contains(Key key) {
        if (key == null) throw new NullPointerException("argument to contains() is null");
        return get(key) != null;
    }

    private int hash(Key key) {
        return (key.hashCode() & 0x7fffffff) % m;
    }

    private void resize(int capacity) {
        LinearProbingHashST<Key, Value> temp = new LinearProbingHashST<>(capacity);
        for (int i = 0; i < m; i++) {
            if (keys[i] != null) {
                temp.put(keys[i], vals[i]);
            }
        }
        keys = temp.keys;
        vals = temp.vals;
        m    = temp.m;
    }

    private boolean check() {
        // check that hash table is at most 50% full
        if (m < 2 * n) {
            System.err.println("Hash table size m = " + m + "; array size n = " + n);
            return false;
        }

        // check that each key in table can be found by get()
        for (int i = 0; i < m; i++) {
            if (keys[i] == null) {
                continue;
            }
            else if (get(keys[i]) != vals[i]) {
                System.err.println("get[" + keys[i] + "] = " + get(keys[i]) + "; vals[i] = " + vals[i]);
                return false;
            }

        }

        return true;
    }

    private void resize() {

    }

    public void put(Key key, Value val) {
        if (n >= m / 2) resize(2 * m);
        int i;
        for (i = hash(key); keys[i] != null; i = (i + 1) % m) {
            if (keys[i].equals(key)) {
                vals[i] = val;
                return;
            }
        }
        keys[i] = key;
        vals[i] = val;
        n++;
    }

    public Value get(Key key) {
        for (int i = hash(key); keys[i] != null; i = (i + 1) % m) {
            if (keys[i].equals(key)) return vals[i];
        }
        return null;
    }

    public void delete(Key key) {
        if (!contains(key)) return;
        int i = hash(key);
        while (!key.equals(keys[i])) {
            i = (i + 1) % m;
        }
        keys[i] = null;
        vals[i] = null;
        i = (i + 1) % m;
        while (keys[i] != null) {
            Key keyToRedo   = keys[i];
            Value valToRedo = vals[i];
            keys[i] = null;
            vals[i] = null;
            n--;
            put(keyToRedo, valToRedo);
            i = (i + 1) % m;
        }
        n--;
        if (n > 0 && n == m / 8) resize(m / 2);

        assert check();
    }

    public Iterable<Key> keys() {
        Queue<Key> queue = new Queue<>();
        for (int i = 0; i < m; i++) {
            if (keys[i] != null) queue.enqueue(keys[i]);
        }
        return queue;
    }
}
