package org.jruby.dirgra;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class DataIterator<T extends ExplicitVertexID, U> implements Iterator<T> {
    private Edge<T, U>[] edges;
    private int edgesLength;
    private int edgeIteratorIndex;
    private Object type;
    private Edge nextEdge = null;
    private boolean source;
    private boolean negate;

    public DataIterator(Edge<T, U>[] edges, int edgesLength, Object type, boolean source, boolean negate) {
        this.edges = edges;
        this.edgesLength = edgesLength;
        this.edgeIteratorIndex = 0;
        this.type = type;
        this.source = source;
        this.negate = negate;
    }

    @Override
    public boolean hasNext() {
        // Multiple hasNext calls with no next...hasNext still true
        if (nextEdge != null) return true;

        for (int i = edgeIteratorIndex; i < edgesLength; i++) {
            Edge edge = edges[i];
            Object edgeType = edge.getType();

            if (negate) {
                // When edgeType or type is null compare them directly. Otherwise compare them using equals
                if ((edgeType != null && !edgeType.equals(type)) || (edgeType == null && edgeType != type)) {
                    nextEdge = edge;
                    edgeIteratorIndex = i + 1;
                    return true;
                }
                // When edgeType or type is null compare them directly. Otherwise compare them using equals
            } else if ((edgeType != null && edgeType.equals(type)) || (edgeType == null && edgeType == type)) {
                nextEdge = edge;
                edgeIteratorIndex = i + 1;
                return true;
            }
        }

        edgeIteratorIndex = edgesLength;
        return false;
    }

    @Override
    public T next() {
        if (hasNext()) {
            Edge<T, U> tmp = nextEdge;
            nextEdge = null;
            return source ? tmp.getSource().getData() : tmp.getDestination().getData();
        }

        throw new NoSuchElementException();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported");
    }
}
