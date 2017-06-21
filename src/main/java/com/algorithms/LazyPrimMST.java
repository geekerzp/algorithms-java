package com.algorithms;

public class LazyPrimMST {
  private static final double FLOATING_POINT_EPSILON = 1E-12;

  private double weight; // total weight of MST
  private Queue<Edge> mst; // edges in the MST
  private boolean[] marked; // marked[v] = true if v on tree
  private MinPQ<Edge> pq; // edges with one endpoint in tree

  public LazyPrimMST(EdgeWeightedGraph G) {
    mst = new Queue<>();
    pq = new MinPQ<>();
    marked = new boolean[G.V()];
    // run Prim from all vertices to
    // get a minimum spanning forest
    for (int v = 0; v < G.V(); v++) {
      if (!marked[v]) prim(G, v);
    }
    // check optimality conditions
    assert check(G);
  }

  // run Prim's algorithm
  private void prim(EdgeWeightedGraph G, int s) {
    scan(G, s);
    while (!pq.isEmpty()) {
      // smallest edge to pq
      Edge e = pq.delMin();
      // two endpoints
      int v = e.either(), w = e.other(v);
      assert marked[v] || marked[w];
      // lazy, both v and w already scanned
      if (marked[v] && marked[w]) continue;
      mst.enqueue(e);
      weight += e.weight();
      // v becomes part of tree
      if (!marked[v]) scan(G, v);
      // w becomes part of tree
      if (!marked[w]) scan(G, w);
    }
  }

  // add all edges e incident to v onto pq if the other endpoint has not yet been scanned
  private void scan(EdgeWeightedGraph G, int v) {
    assert !marked[v];
    marked[v] = true;
    for (Edge e : G.adj(v)) {
      if (!marked[e.other(v)]) pq.insert(e);
    }
  }

  /**
   * Returns the edges in a minimum spanning tree (or forest).
   *
   * @return the edges in a minimum spanning tree (of forest) as an iterable edges.
   */
  public Iterable<Edge> edges() {
    return mst;
  }

  /**
   * Returns the sum of the edge weights in a minimum spanning tree (or forest).
   *
   * @return the sum of the edge weights in a minimum spanning tree (or forest).
   */
  public double weight() {
    return weight;
  }

  // check optimality conditions (takes time proportional to E V lg* V)
  private boolean check(EdgeWeightedGraph G) {
    // check weight
    double totalWeight = 0.0;
    for (Edge e : edges()) {
      totalWeight += e.weight();
    }
    if (Math.abs(totalWeight - weight()) > FLOATING_POINT_EPSILON) {
      System.err.printf(
          "Weight of edges dose not equal weight(): %f vs. %f\n", totalWeight, weight());
      return false;
    }

    // check that it is acyclic
  }
}
