package com.algorithms;

public class TransitiveClosure {
  private DirectedDFS[] tc; // tc[v] = reachable from v

  public TransitiveClosure(Digraph G) {
    tc = new DirectedDFS[G.V()];
    for (int v = 0; v < G.V(); v++) {
      tc[v] = new DirectedDFS(G, v);
    }
  }

  /**
   * Is there a directed path from vertex {@code v} to vertex {@code w} in the digraph?
   *
   * @param v the source vertex
   * @param w the source vertex
   * @return {@code true} if there is a directdd path from {@code v} to {@code w}, {@code false}
   *     otherwise
   */
  boolean reachable(int v, int w) {
    return tc[v].marked(w);
  }

  public static void main(String[] args) {
    In in = new In(args[0]);
    Digraph G = new Digraph(in);

    TransitiveClosure tc = new TransitiveClosure(G);

    // print header
    for (int v = 0; v < G.V(); v++) {
      StdOut.printf("%3d", v);
    }
    StdOut.println();
    StdOut.println("------------------------------------------");

    // print transitive closure
    for (int v = 0; v < G.V(); v++) {
      StdOut.printf("%3d: ", v);
      for (int w = 0; w < G.V(); w++) {
        if (tc.reachable(v, w)) StdOut.printf(" T");
        else                    StdOut.printf("  ");
      }
      StdOut.println();
    }
  }
}
