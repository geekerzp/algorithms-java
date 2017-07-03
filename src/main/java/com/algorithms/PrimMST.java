package com.algorithms;

public class PrimMST {
  private Edge[] edgeTo; // shortest edge from tree vertex
  private double[] distTo; // distTo[w] = edgeTo[w].weight()
  private boolean[] marked; // true if v on tree
  private IndexMinPQ<Double> pq; // eligible crossing edges

  public PrimMST(EdgeWeightedGraph G) {
    edgeTo = new Edge[G.V()];
    distTo = new double[G.V()];
    marked = new boolean[G.V()];
    for (int v = 0; v < G.V(); v++) {
      distTo[v] = Double.POSITIVE_INFINITY;
    }
    pq = new IndexMinPQ<>(G.V());
    // Initialize pq with 0, weight 0.
    distTo[0] = 0.0;
    pq.insert(0, 0.0);
    while (!pq.isEmpty()) {
      // Add closest vertex to tree.
      visit(G, pq.delMin());
    }
  }

  private void visit(EdgeWeightedGraph G, int v) {
    // Add v to tree;
    // update data structures.
    marked[v] = true;
    for (Edge e : G.adj(v)) {
      int w = e.other(v);
      // v-w is ineligible.
      if (marked[w]) {
        continue;
      }
      // Edge e is new best connection from tree to w.
      if (e.weight() < distTo[w]) {
        edgeTo[w] = e;
        distTo[w] = e.weight();
        if (pq.contains(w)) {
          pq.changeKey(w, distTo[w]);
        } else {
          pq.insert(w, distTo[w]);
        }
      }
    }
  }
}
