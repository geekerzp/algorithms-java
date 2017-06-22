package com.algorithms;

import java.util.ArrayList;
import java.util.List;

public class LazyPrimMST {
  private boolean[] marked; // MST vertices
  private Queue<Edge> mst; // MST edges
  private MinPQ<Edge> pq; // crossing (and ineligible) edges

  public LazyPrimMST(EdgeWeightedGraph G) {
    pq = new MinPQ<>();
    marked = new boolean[G.V()];
    mst = new Queue<>();

    visit(G, 0);
    while (!pq.isEmpty()) {
      // Get lowest-weight edge from pq.
      Edge e = pq.delMin();
      int v = e.either(), w = e.other(v);
      // Skip if ineligible.
      if (marked[v] && marked[w]) continue;
      // Add edge to tree.
      mst.enqueue(e);
      // Add vertex to tree (either v or w).
      if (!marked[v]) visit(G, v);
      if (!marked[w]) visit(G, w);
    }
  }

  private void visit(EdgeWeightedGraph G, int v) {
    marked[v] = true;
    for (Edge e : G.adj(v)) {
      if (!marked[e.other(v)]) pq.insert(e);
    }
  }

  public Iterable<Edge> edges() {
    return mst;
  }

  public double weight() {
    List<Edge> mstList = new ArrayList<>();
    mst.forEach(mstList::add);
    return mstList.stream().mapToDouble(Edge::weight).sum();
  }
}
