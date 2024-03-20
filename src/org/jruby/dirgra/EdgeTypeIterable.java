package org.jruby.dirgra;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class EdgeTypeIterable<T extends ExplicitVertexID, U> implements Iterable<Edge<T, U>> {
    private Edge<T, U>[] edges;
    int edgesLength;
    private Object type;
    private boolean negate;

    public EdgeTypeIterable(Edge<T, U>[] edges, int edgesLength, U type) {
        this(edges, edgesLength, type, false);

    }

    public EdgeTypeIterable(Edge<T, U>[] edges, int edgesLength, U type, boolean negate) {
        this.edges = edges;
        this.edgesLength = edgesLength;
        this.type = type;
        this.negate = negate;
    }

    @Override
    public Iterator<Edge<T, U>> iterator() {
        return new EdgeTypeIterator<T, U>(edges, edgesLength, type, negate);
    }
}
