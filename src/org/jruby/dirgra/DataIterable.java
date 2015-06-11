package org.jruby.dirgra;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class DataIterable<T extends ExplicitVertexID> implements Iterable<T> {
    private Edge<T>[] edges;
    private int edgesLength;
    private Object type;
    private boolean negate;
    private boolean source;

    public DataIterable(Edge<T>[] edges, int edgesLength, Object type, boolean source, boolean negate) {
        this.edges = edges;
        this.edgesLength = edgesLength;
        this.type = type;
        this.negate = negate;
        this.source = source;
    }

    @Override
    public Iterator<T> iterator() {
        return new DataIterator<T>(edges, edgesLength, type, source, negate);
    }
}
