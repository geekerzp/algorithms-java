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
    UF uf = new UF(G.V());
    for (Edge e : edges()) {
      int v = e.either(), w = e.other(v);
      if (uf.connected(v, w)) {
        System.err.println("Not a forest");
        return false;
      }
      uf.union(v, w);
    }

    // check that it is a spanning forest
    for (Edge e : G.edges()) {
      int v = e.either(), w = e.other(v);
      if (!uf.connected(v, w)) {
        System.err.println("Not a spanning forest");
        return false;
      }
    }

    // check that it is a minimal spanning forest (cut optimality condition)
    for (Edge e : edges()) {

      // all edges in MST except e
      uf = new UF(G.V());
      for (Edge f : mst) {
        int x = f.either(), y = f.other(x);
        if (f != e) uf.union(x, y);
      }

      // check that e is min weight edge in crossing cut
      for (Edge f : G.edges()) {
        int x = f.either(), y = f.other(x);
        if (!uf.connected(x, y)) {
          if (f.weight() < e.weight()) {
            System.err.println("Edge " + f + " violates cut optimality conditions");
            return false;
          }
        }
      }
    }

    return true;
  }

  public static void main(String[] args) {
    In in = new In(args[0]);
    EdgeWeightedGraph G = new EdgeWeightedGraph(in);
    LazyPrimMST mst = new LazyPrimMST(G);
    for (Edge e : mst.edges()) {
      StdOut.println(e);
    }
    StdOut.printf("%.5f\n", mst.weight());
  }
}
