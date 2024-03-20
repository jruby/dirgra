package org.jruby.dirgra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DirectedGraph<T extends ExplicitVertexID, U> {
    private static final int INITIAL_SIZE = 4;

    private final Map<T, Vertex<T, U>> vertices = new HashMap<>();
    private Edge<T, U>[] edges = new Edge[INITIAL_SIZE];
    private int edgeLength = 0;
    private ArrayList inOrderVerticeData = new ArrayList();
    int vertexIDCounter = 0;

    protected Edge<T, U>[] growEdges(Edge<T, U>[] array, int realLength) {
        int newLength = array.length == 0 ? 2 : array.length * 2;
        Edge<T, U>[] newEdges = new Edge[newLength];

        System.arraycopy(array, 0, newEdges, 0, realLength);
        return newEdges;
    }

    protected Edge<T, U>[] getEdges() {
        return edges;
    }

    protected Edge<T, U> addEdge(Edge<T, U> newEdge) {
        for (int i = 0; i < edgeLength; i++) {
            // Edge already added.  No repeated edge support.
            if (edges[i].equals(newEdge)) return newEdge;
        }

        if (edgeLength >= edges.length) edges = growEdges(edges, edgeLength);

        edges[edgeLength++] = newEdge;
        return newEdge;
    }

    public void removeEdge(Edge<T, U> edge) {
        int splitPoint = -1;

        for (int i = 0; i < edgeLength; i++) {
            if (edges[i].equals(edge)) {
                splitPoint = i;
                break;
            }
        }

        if (splitPoint != -1) {
            Edge<T, U> edgeToRemove = edges[splitPoint];
            if (splitPoint < edgeLength - 1) { // somewhere between index 0 and edgeLength-2
                System.arraycopy(edges, splitPoint + 1, edges, splitPoint, edgeLength - 1 - splitPoint);
            }
            edges[edgeLength - 1] = null;         // do not pin left over edge after shifting
            edgeLength--;

            // Remove knowledge of edge from each vertex
            edge.getSource().removeOutgoingEdge(edgeToRemove);
            edge.getDestination().removeIncomingEdge(edgeToRemove);
        }
    }

    public Collection<Vertex<T, U>> vertices() {
        return vertices.values();
    }

    public Collection<Edge<T, U>> edges() {
        return Arrays.asList(Arrays.copyOf(edges, edgeLength));
    }

    public Iterable<Edge<T, U>> edgesOfType(U type) {
        return new EdgeTypeIterable<T, U>(edges, edgeLength, type);
    }

    public Collection<T> allData() {
        return vertices.keySet();
    }

    /**
     * @return data in the order it was added to this graph.
     */
    public Collection<T> getInorderData() {
        return inOrderVerticeData;
    }

    public void addEdge(T source, T destination, U type) {
        findOrCreateVertexFor(source).addEdgeTo(destination, type);
    }

    public void removeEdge(T source, T destination) {
        if (findVertexFor(source) != null) {
            for (Edge edge: findOrCreateVertexFor(source).getOutgoingEdges()) {
                if (edge.getDestination().getData() == destination) {
                    findOrCreateVertexFor(source).removeEdgeTo(edge.getDestination());
                    return;
                }
            }
        }
    }

    public Vertex<T, U> findVertexFor(T data) {
        return vertices.get(data);
    }

    /**
     * Find existing vertex and if it is not present create it.
     * 
     * @param data to find a vertex for
     * @return vertex for given data. If vertex is not present it creates vertex and returns it.
     */
    public Vertex<T, U> findOrCreateVertexFor(T data) {
        Vertex vertex = vertices.get(data);

        if (vertex != null) return vertex;

        vertex = new Vertex(this, data, vertexIDCounter++);
        inOrderVerticeData.add(data);

        vertices.put(data, vertex);

        return vertex;
    }

    public void removeVertexFor(T data) {
        if (findVertexFor(data) != null) {
            Vertex vertex = findOrCreateVertexFor(data);
            vertices.remove(data);
            inOrderVerticeData.remove(data);
            vertex.removeAllEdges();
        }
    }

    /**
     * @return the number of vertices in the graph.
     */
    public int size() {
        return allData().size();
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();

        ArrayList<Vertex<T, U>> verts = new ArrayList<>(vertices.values());
        Collections.sort(verts);
        for (Vertex<T, U> vertex: verts) {
            buf.append(vertex);
        }

        return buf.toString();
    }
}

