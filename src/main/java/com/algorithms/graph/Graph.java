package com.algorithms.graph;

import com.algorithms.util.In;

import java.util.Iterator;

public class Graph
{
    private int V;              // number of vertices
    private int E;              // number of edges
    private Bag<Integer>[] adj; // adjacency lists

    public Graph(int V)
    {
        this.V = V;
        this.E = 0;
        adj = (Bag<Integer>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<>();
        }
    }

    public Graph(In in)
    {
        this(in.readInt());
        int E = in.readInt();
        for (int i = 0; i < E; i++) {
            int v = in.readInt();
            int w = in.readInt();
            addEdge(v, w);
        }
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    public void addEdge(int v, int w)
    {
        adj[v].add(w);
        adj[w].add(v);
        E++;
    }

    public Iterable<Integer> adj(int v)
    {
        return adj[v];
    }
}
