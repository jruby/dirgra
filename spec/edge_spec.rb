require_relative 'helpers/vertex_id_helper'

java_import 'org.jruby.dirgra.Edge'
java_import 'org.jruby.dirgra.Vertex'
java_import 'org.jruby.dirgra.DirectedGraph'

describe "Edge" do

  let(:graph) { DirectedGraph.new }
  let(:foo) { VertexID.new(1) }
  let(:bar) { VertexID.new(2) }

  describe "toString" do
    context "When edge type is not null" do
      it "represents edge with type" do
        edge  = Edge.new(Vertex.new(graph, foo, 1), Vertex.new(graph, bar, 2), "baz")
        expect(edge.toString).to eq "<1 --> 2> (baz)"
      end
    end

    context "When edge type is null" do
      it "represents edge without type" do
        edge  = Edge.new(Vertex.new(graph, foo, 1), Vertex.new(graph, bar, 2), nil)
        expect(edge.toString).to eq "<1 --> 2>"
      end
    end
  end
end
