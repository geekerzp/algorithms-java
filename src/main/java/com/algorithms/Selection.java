package com.algorithms;

import com.algorithms.StdOut;

public class Selection {
    public static void sort(Comparable[] a) {
        // Sort a[] into increasing order.
        int N = a.length;

        for (int i = 0; i < N; i++) {
            // Exchange a[i] with smallest entry in a[i+1...N)
            int min = i;
            for (int j = i + 1; j < N; j++) {
                if (less(a[j], a[min])) min = j;
            }
            exch(a, i, min);
        }
    }

    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    private static void exch(Comparable[] a, int i, int j) {
        Comparable t = a[i]; a[i] = a[j]; a[j] = t;
    }

    private static void show(Comparable[] a) {
        // Print the array, on a single line.
        for (Comparable v : a) {
            StdOut.print(v + " ");
        }
        StdOut.println();
    }

    public static boolean isSorted(Comparable[] a) {
        for (int i = 1; i < a.length; i++) {
            if (less(a[i], a[i - 1])) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        // String[] a = In.readStrings();
        String[] a = {"1", "2", "3", "6", "5", "4"};
        sort(a);
        assert isSorted(a);
        show(a);
    }
}
