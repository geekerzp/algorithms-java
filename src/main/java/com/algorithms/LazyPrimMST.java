package com.algorithms;

public class LazyPrimMST {
  private static final double FLOATING_POINT_EPSILON = 1E-12;

  private double weight; // total weight of MST
  private Queue<Edge> mst; // edges in the MST
  private boolean[] marked; // marked[v] = true if v on tree
}
