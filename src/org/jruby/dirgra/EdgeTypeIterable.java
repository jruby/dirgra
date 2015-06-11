package org.jruby.dirgra;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class EdgeTypeIterable<T extends ExplicitVertexID> implements Iterable<Edge<T>> {
    private Edge<T>[] edges;
    int edgesLength;
    private Object type;
    private boolean negate;

    public EdgeTypeIterable(Edge<T>[] edges, int edgesLength, Object type) {
        this(edges, edgesLength, type, false);

    }

    public EdgeTypeIterable(Edge<T>[] edges, int edgesLength, Object type, boolean negate) {
        this.edges = edges;
        this.edgesLength = edgesLength;
        this.type = type;
        this.negate = negate;
    }

    @Override
    public Iterator<Edge<T>> iterator() {
        return new EdgeTypeIterator<T>(edges, edgesLength, type, negate);
    }
}
