package org.jruby.dirgra;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class EdgeTypeIterator<T extends ExplicitVertexID, U> implements Iterator<Edge<T, U>> {
    private Edge<T, U>[] edges;
    private int edgesLength;
    private int edgeIteratorIndex = 0;
    private Object type;
    private Edge nextEdge = null;
    private boolean negate;

    public EdgeTypeIterator(Edge<T, U>[] edges, int edgesLength, Object type, boolean negate) {
        this.edges = edges;
        this.edgesLength = edgesLength;
        this.type = type;
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
    public Edge<T, U> next() {
        if (hasNext()) {
            Edge<T, U> tmp = nextEdge;
            nextEdge = null;
            return tmp;
        }

        throw new NoSuchElementException();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported");
    }
}
