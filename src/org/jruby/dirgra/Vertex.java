package org.jruby.dirgra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public class Vertex<T extends ExplicitVertexID> implements Comparable<Vertex<T>> {
    private static final Edge[] EMPTY_EDGE_LIST = new Edge[0];
    private DirectedGraph graph;
    private T data;
    private Edge<T>[] incoming = EMPTY_EDGE_LIST;
    private int incomingLength = 0;
    private Edge<T>[] outgoing = EMPTY_EDGE_LIST;
    private int outgoingLength = 0;
    int id;

    public Vertex(DirectedGraph graph, T data, int id) {
        this.graph = graph;
        this.data = data;
        this.id = id;
    }

    public void addEdgeTo(Vertex destination) {
        addEdgeTo(destination, null);
    }

    public void addEdgeTo(Vertex destination, Object type) {
        Edge<T> newEdge = graph.addEdge(new Edge<T>(this, destination, type));
        addOutgoingEdge(newEdge);
        destination.addIncomingEdge(newEdge);
    }

    public void addEdgeTo(T destination) {
        addEdgeTo(destination, null);
    }

    public void addEdgeTo(T destination, Object type) {
        Vertex destinationVertex = graph.findOrCreateVertexFor(destination);

        addEdgeTo(destinationVertex, type);
    }

    public boolean removeEdgeTo(Vertex destination) {
        for (int i = 0; i < outgoingLength; i++) {
            Edge edge = outgoing[i];
            if (edge.getDestination() == destination) {
                graph.removeEdge(edge); // This will remove incoming/outgoing as side-effect.
                return true;
            }
        }

        return false;
    }

    protected void addOutgoingEdge(Edge<T> newEdge) {
        for (int i = 0; i < outgoingLength; i++) {
            // Edge already added.  No repeated edge support.
            if (outgoing[i].equals(newEdge)) return;
        }

        if (outgoingLength >= outgoing.length) outgoing = graph.growEdges(outgoing, outgoingLength);

        outgoing[outgoingLength++] = newEdge;
    }

    protected void addIncomingEdge(Edge<T> newEdge) {
        for (int i = 0; i < incomingLength; i++) {
            // Edge already added.  No repeated edge support.
            if (incoming[i] == newEdge) return;
        }

        if (incomingLength >= incoming.length) incoming = graph.growEdges(incoming, incomingLength);

        incoming[incomingLength++] = newEdge;
    }

    protected void removeOutgoingEdge(Edge<T> edge) {
        int splitIndex = -1; // which index we found the edge at

        for (int i = 0; i < outgoingLength; i++) {
            if (outgoing[i].equals(edge)) {
                splitIndex = i;
                break;
            }
        }

        if (splitIndex == -1) return;           // no edge found

        if (splitIndex != outgoingLength-1) {   // need to shift over all elements one
            System.arraycopy(outgoing, splitIndex + 1, outgoing, splitIndex, outgoingLength - 1 - splitIndex);
        }
        outgoing[outgoingLength-1] = null;      // we made list one smaller null last element so it does not leak.
        outgoingLength--;                       // list is one smaller
    }

    protected void removeIncomingEdge(Edge<T> edge) {
        int splitIndex = -1; // which index we found the edge at

        for (int i = 0; i < incomingLength; i++) {
            if (incoming[i].equals(edge)) {
                splitIndex = i;
                break;
            }
        }

        if (splitIndex == -1) return;           // no edge found

        if (splitIndex != incomingLength-1) {   // need to shift over all elements one
            System.arraycopy(incoming, splitIndex + 1, incoming, splitIndex, incomingLength - 1 - splitIndex);
        }
        incoming[incomingLength-1] = null;      // we made list one smaller null last element so it does not leak.
        incomingLength--;                       // list is one smaller
    }

    public void removeAllIncomingEdges() {
        while (incomingLength > 0) {       // Each removal will decrement length until none are left
            graph.removeEdge(incoming[0]); // This will remove incoming/outgoing as side-effect.
        }

        incoming = EMPTY_EDGE_LIST;
    }

    public void removeAllOutgoingEdges() {
        while (outgoingLength > 0) {       // Each removal will decrement length until none are left
            graph.removeEdge(outgoing[0]); // This will remove incoming/outgoing as side-effect.
        }

        outgoing = EMPTY_EDGE_LIST;
    }

    public void removeAllEdges() {
        removeAllIncomingEdges();
        removeAllOutgoingEdges();
    }

    public int inDegree() {
        return incomingLength;
    }

    public int outDegree() {
        return outgoingLength;
    }

    public Iterable<Edge<T>> getIncomingEdgesOfType(Object type) {
        return new EdgeTypeIterable<T>(incoming, incomingLength, type);
    }

    public Iterable<Edge<T>> getIncomingEdgesNotOfType(Object type) {
        return new EdgeTypeIterable<T>(incoming, incomingLength, type, true);
    }

    public Iterable<Edge<T>> getOutgoingEdgesOfType(Object type) {
        return new EdgeTypeIterable<T>(outgoing, outgoingLength, type);
    }

    public T getIncomingSourceData() {
        Edge<T> edge = getFirstEdge(getIncomingEdges().iterator());

        return edge == null ? null : edge.getSource().getData();
    }

    public T getIncomingSourceDataOfType(Object type) {
        Edge<T> edge = getFirstEdge(getIncomingEdgesOfType(type).iterator());

        return edge == null ? null : edge.getSource().getData();
    }

    public Iterable<T> getIncomingSourcesData() {
        return new DataIterable<T>(incoming, incomingLength, null, true, true);
    }

    public Iterable<T> getIncomingSourcesDataOfType(Object type) {
        return new DataIterable<T>(incoming, incomingLength, type, true, false);
    }

    public Iterable<T> getIncomingSourcesDataNotOfType(Object type) {
        return new DataIterable<T>(incoming, incomingLength, type, true, true);
    }

    public Iterable<Edge<T>> getOutgoingEdgesNotOfType(Object type) {
        return new EdgeTypeIterable<T>(outgoing, outgoingLength, type, true);
    }

    public Iterable<T> getOutgoingDestinationsData() {
        return new DataIterable<T>(outgoing, outgoingLength, null, false, true);
    }

    public Iterable<T> getOutgoingDestinationsDataOfType(Object type) {
        return new DataIterable<T>(outgoing, outgoingLength, type, false, false);
    }

    public Iterable<T> getOutgoingDestinationsDataNotOfType(Object type) {
        return new DataIterable<T>(outgoing, outgoingLength, type, false, true);
    }

    public T getOutgoingDestinationData() {
        Edge<T> edge = getFirstEdge(getOutgoingEdges().iterator());

        return edge == null ? null : edge.getDestination().getData();
    }

    public T getOutgoingDestinationDataOfType(Object type) {
        Edge<T> edge = getFirstEdge(getOutgoingEdgesOfType(type).iterator());

        return edge == null ? null : edge.getDestination().getData();
    }

    private Edge<T> getFirstEdge(Iterator<Edge<T>> iterator) {
        return iterator.hasNext() ? iterator.next() : null;
    }

    public Edge<T> getIncomingEdgeOfType(Object type) {
        return getFirstEdge(getIncomingEdgesOfType(type).iterator());
    }

    public Edge<T> getOutgoingEdgeOfType(Object type) {
        return getFirstEdge(getOutgoingEdgesOfType(type).iterator());
    }

    public Edge<T> getIncomingEdge() {
        return getFirstEdge(getIncomingEdgesNotOfType(null).iterator());
    }

    public Edge<T> getOutgoingEdge() {
        return getFirstEdge(getOutgoingEdgesNotOfType(null).iterator());
    }

    public Collection<Edge<T>> getIncomingEdges() {
        return Arrays.asList(Arrays.copyOf(incoming, incomingLength));
    }

    public Collection<Edge<T>> getOutgoingEdges() {
        return Arrays.asList(Arrays.copyOf(outgoing, outgoingLength));
    }

    public T getData() {
        return data;
    }

    public int getID() {
        return data.getID();
    }

    // FIXME: This is pretty ugly...creating massive number of comparators
    @Override
    public String toString() {
        boolean found = false;
        StringBuilder buf = new StringBuilder(data.toString());

        buf.append(":");

        Collection<Edge<T>> edges = getOutgoingEdges();
        int size = edges.size();

        if (size > 0) {
            found = true;
            buf.append(">[");
            List<Edge<T>> e = new ArrayList<Edge<T>>(edges);
            Collections.sort(e, new DestinationCompare());

            for (int i = 0; i < size - 1; i++) {
                buf.append(e.get(i).getDestination().getID()).append(",");
            }
            buf.append(e.get(size - 1).getDestination().getID()).append("]");
        }

        edges = getIncomingEdges();
        size = edges.size();

        if (size > 0) {
            if (found) buf.append(", ");
            buf.append("<[");
            List<Edge<T>> e = new ArrayList<Edge<T>>(edges);
            Collections.sort(e, new SourceCompare());

            for (int i = 0; i < size - 1; i++) {
                buf.append(e.get(i).getSource().getID()).append(",");
            }
            buf.append(e.get(size - 1).getSource().getID()).append("]");
        }
        buf.append("\n");

        return buf.toString();
    }

    @Override
    public int compareTo(Vertex<T> that) {
        if (getID() == that.getID()) return 0;
        if (getID() < that.getID()) return -1;
        return 1;
    }

    class SourceCompare implements Comparator<Edge<T>> {
        @Override
        public int compare(Edge<T> o1, Edge<T> o2) {
            int i1 = o1.getSource().getID();
            int i2 = o2.getSource().getID();

            if (i1 == i2) return 0;
            return i1 < i2 ? -1 : 1;
        }
    }

    class DestinationCompare implements Comparator<Edge<T>> {
        @Override
        public int compare(Edge<T> o1, Edge<T> o2) {
            int i1 = o1.getDestination().getID();
            int i2 = o2.getDestination().getID();

            if (i1 == i2) return 0;
            return i1 < i2 ? -1 : 1;
        }
    }
}