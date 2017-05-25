package com.algorithms;

public class Topological {
  private Iterable<Integer> order;

  public Topological(Digraph G) {
    DirectedCycle cyclefinder = new DirectedCycle(G);
    if (!cyclefinder.hasCycle()) {
      DepthFirstOrder dfs = new DepthFirstOrder(G);
      order = dfs.reversePost();
    }
  }

  public Iterable<Integer> order() {
    return order;
  }

  public boolean isDAG() {
    return order != null;
  }

  public static void main(String[] args) {
    String filename = args[0];
    String separator = args[1];
  }
}
