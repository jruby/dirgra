package org.jruby.dirgra;

public class Edge<T extends ExplicitVertexID, U> {
    private Vertex<T, U> source;
    private Vertex<T, U> destination;
    private U type;

    public Edge(Vertex<T, U> source, Vertex<T, U> destination, U type) {
        this.source = source;
        this.destination = destination;
        this.type = type;
    }

    public Vertex<T, U> getDestination() {
        return destination;
    }

    public Vertex<T, U> getSource() {
        return source;
    }

    public U getType() {
        return type;
    }

    @Override
    public String toString() {
        return "<" + source.getID() + " --> " +
                destination.getID() + ">" + (type == null ? "" : " (" + type + ")");
    }
}
