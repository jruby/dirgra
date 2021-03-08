require 'dirgra-0.3.jar'
import 'org.jruby.dirgra.DirectedGraph'

require 'vertex_id_helper'

# This is spec for Directed Graph Library

describe "Directed Graph Utility" do

  let(:graph) { DirectedGraph.new }
  let(:one) { VertexID.new(1) }
  let(:two) { VertexID.new(2) }
  let(:three) { VertexID.new(3) }
  let(:four) { VertexID.new(4) }
  let(:five) { VertexID.new(5) }
  let(:six) { VertexID.new(6) }
  let(:hundred) { VertexID.new(100) }
  let(:foo) { VertexID.new("foo") }
  let(:bar) { VertexID.new("bar") }

  before do
    @edge_count = 0
  end

  it "adds an edge to newly created graph" do
    expect(graph.edges.size).to eq 0
    add_edge(one,two,'foo')
    add_edge(four,five,'bar')
    expect(graph.edges.size).to eq 2
  end

  it "removes an existing edge from a graph" do
    add_edge(one,two,'foo')
    add_edge(four,five,'bar')
    remove_edge(four,five)
    expect(graph.edges.size).to eq 1
    graph.removeEdge(graph.edges.to_a.last)
    expect(graph.edges.size).to eq 0
  end

  it "does not delete a non-existent edge from the graph" do
    remove_edge(two,one)
    expect(graph.edges.size).to eq 0
  end

  it "removes a vertex and its associated edges" do
    graph.removeVertexFor(three)
    expect(graph.vertices.size).to eq 0
    add_edge(one,two,'foo')
    add_edge(four,five,'bar')
    graph.removeVertexFor(two)
    expect(graph.vertices.size).to eq 3
    expect(graph.edges.size).to eq 1
  end

  it "gives vertex for given data" do
    add_edge(one,two,'foo')
    expect(graph.findOrCreateVertexFor(two).getData()).to eq two
  end

  it "creates a new vertex if it is not present" do
    expect(graph.findOrCreateVertexFor(hundred).getData()).to eq hundred
  end

  it "finds already existing vertex" do
    expect(graph.findVertexFor(hundred)).to eq nil
    add_edge(one,two,'foo')
    expect(graph.findVertexFor(one).getData()).to eq one
  end

  it "gives correct size of graph" do
    remove_edge(one,two)
    expect(graph.size).to eq 0
    add_edge(five,six,'baz')
    expect(graph.size).to eq 2
    add_edge(foo,bar,'baz')
    expect(graph.size).to eq 4
  end

  it "gives all data in the graph" do
    expect(graph.allData.size).to eq 0
    add_edge(one,two,'baz')
    graph.allData.each do |key|
      expect(graph.findVertexFor(key)).to_not eq nil
    end
    graph.removeVertexFor(one)
    graph.allData.each do |key|
      expect(graph.findVertexFor(key)).to_not eq nil
    end
  end

  it "gives data in the graph in the order in which it was inserted" do
    expect(graph.getInorderData.to_a.size).to eq 0
    graph.findOrCreateVertexFor(one)
    expect(graph.getInorderData.to_a).to eq [one]
    add_edge(foo,bar,'baz')
    expect(graph.getInorderData.to_a).to eq [one,foo,bar]
    graph.removeVertexFor(foo)
    expect(graph.getInorderData.to_a).to eq [one,bar]
  end
end
