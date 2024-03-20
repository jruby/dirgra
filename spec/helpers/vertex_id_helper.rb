
class VertexID
  include org.jruby.dirgra.ExplicitVertexID

  def initialize(id)
    @id = id;
  end

  def getID
    @id
  end

  def to_s
    @id
  end
end

def add_edge(vertex1, vertex2, type)
  @edge_count += 1
  graph.addEdge(vertex1, vertex2, type)
end

def remove_edge(vertex1, vertex2)
  @edge_count -= 1
  graph.removeEdge(vertex1, vertex2)
end

def edges
  [graph.edges.to_a, @edge_count]
end
