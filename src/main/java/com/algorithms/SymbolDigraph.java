package com.algorithms;

public class SymbolDigraph {
  private ST<String, Integer> st; // string -> index
  private String[] keys; // index -> string
  private Digraph graph; // the underlying digraph

  public SymbolDigraph(String filename, String delimiter) {
    st = new ST<>();

    // first pass builds the index by reading strings to associate
    // distinct strings with an index
    In in = new In(filename);
    while (in.hasNextLine()) {
      String[] a = in.readLine().split(delimiter);
      for (int i = 0; i < a.length; i++) {
        if (!st.contains(a[i])) {
          st.put(a[i], st.size());
        }
      }
    }

    // inverted index to get string keys in an array
    keys = new String[st.size()];
    for (String name : st.keys()) {
      keys[st.get(name)] = name;
    }

    // second pass builds the digraph by connecting first vertex on each
    // line to all others
    graph = new Digraph(st.size());
    in = new In(filename);
    while (in.hasNextLine()) {
      String[] a = in.readLine().split(delimiter);
      int v = st.get(a[0]);
      for (int i = 1; i < a.length; i++) {
        int w = st.get(a[i]);
        graph.addEdge(v, w);
      }
    }
  }

  public boolean contains(String s) {
    return st.contains(s);
  }

  @Deprecated
  public int index(String s) {
    return st.get(s);
  }

  public int indexOf(String s) {
    return st.get(s);
  }

  @Deprecated
  public String name(int v) {
    return keys[v];
  }

  public String nameOf(int v) {
    return keys[v];
  }

  public Digraph G() {
    return graph;
  }

  public Digraph digraph() {
    return graph;
  }

  public static void main(String[] args) {
    String filename = args[0];
    String delimiter = args[1];
    SymbolDigraph sg = new SymbolDigraph(filename, delimiter);
    Digraph graph = sg.digraph();
    while (!StdIn.isEmpty()) {
      String t = StdIn.readLine();
      for (int v : graph.adj(sg.index(t))) {
        StdOut.println("  " + sg.name(v));
      }
    }
  }
}
