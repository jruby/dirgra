package org.jruby.dirgra;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class EdgeTypeIterable<T> implements Iterable<Edge<T>> {
    private Collection<Edge<T>> edges;
    private Object type;
    private boolean negate;

    public EdgeTypeIterable(Collection<Edge<T>> edges, Object type) {
        this(edges, type, false);

    }

    public EdgeTypeIterable(Collection<Edge<T>> edges, Object type, boolean negate) {
        this.edges = edges;
        this.type = type;
        this.negate = negate;
    }

    @Override
    public Iterator<Edge<T>> iterator() {
        return new EdgeTypeIterator<T>(edges, type, negate);
    }
}
