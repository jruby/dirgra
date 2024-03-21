require_relative 'helpers/vertex_id_helper'
require_relative 'helpers/edge_helpers'

java_import 'org.jruby.dirgra.DirectedGraph'
java_import 'org.jruby.dirgra.EdgeTypeIterator'
java_import 'java.util.NoSuchElementException'

describe "EdgeTypeIterable" do

  let(:graph) { DirectedGraph.new }
  let(:one) { VertexID.new(1) }
  let(:two) { VertexID.new(2) }
  let(:three) { VertexID.new(3) }
  let(:four) { VertexID.new(4) }

  before do
    @edge_count = 0
    add_edge(one, two, "foo")
    add_edge(two, three, "foo")
  end

  describe "hasNext" do

    context "edges of given type" do

      it "returns true if the iterator contains an edge of given type" do
        iterator = EdgeTypeIterator.new(*edges, "foo", false)
        expect(iterator.hasNext).to eq true
      end

      it "returns false if the iterator does not contain any edge of given type" do
        iterator = EdgeTypeIterator.new(*edges, "bar", false)
        expect(iterator.hasNext).to eq false
      end

    end

    context "edges not of given type" do

      it "returns true if the iterator contains an edge not of given type" do
        iterator = EdgeTypeIterator.new(*edges, "bar", true)
        expect(iterator.hasNext).to eq true
      end

      it "returns false if the iterator contains an edge of given type" do
        iterator = EdgeTypeIterator.new(*edges, "foo", true)
        expect(iterator.hasNext).to eq false
      end

    end

    context "when iterator type is null" do

      context "edges of given type" do

        it "returns true if the iterator contains an edge of type nil" do
          # add an edge of type nil
          add_edge(four,one,nil)
          iterator = EdgeTypeIterator.new(*edges, nil, false)
          expect(iterator.hasNext).to eq true
        end

        it "returns false if the iterator does not contain any edge of type nil" do
          iterator = EdgeTypeIterator.new(*edges, nil, false)
          expect(iterator.hasNext).to eq false
        end

      end

      context "edges not of given type" do

        it "returns true if the iterator contains an edge not of type nil" do
          iterator = EdgeTypeIterator.new(*edges, nil, true)
          expect(iterator.hasNext).to eq true
        end

        it "returns false if the iterator contains all edges of type nil" do
          # remove existing edges not of type nil
          remove_edge(one,two)
          remove_edge(two,three)
          # add an edge of type nil
          add_edge(four,one,nil)
          iterator = EdgeTypeIterator.new(*edges, nil, true)
          expect(iterator.hasNext).to eq false
        end

      end

    end

    context "when edge type is nil and iterator type is not nil" do

      it "returns true if the iterator contains an edge not of type nil" do
        # remove existing edges not of type nil
        remove_edge(one,two)
        remove_edge(two,three)
        # add an edge of type nil
        add_edge(four,one,nil)
        iterator = EdgeTypeIterator.new(*edges, "foo", true)
        expect(iterator.hasNext).to eq true
      end

      it "returns false if the iterator contains all edges not of type nil" do
        iterator = EdgeTypeIterator.new(*edges, "foo", true)
        expect(iterator.hasNext).to eq false
      end
    end

  end

  describe "next" do

    context "when the iterator has next edge" do

      it "returns the next edge" do
        iterator = EdgeTypeIterator.new(*edges, "foo", false)
        expect(iterator.next).to have_type("foo")
      end
    end

    context "when the iterator does not have next edge" do
      it "throws NoSuchElementException" do
        empty_graph = DirectedGraph.new
        iterator = EdgeTypeIterator.new(empty_graph.edges.to_a, 0, "foo", false)
        expect { iterator.next }.to raise_error NoSuchElementException
      end
    end
  end

  describe "remove" do

    it "throws UnsupportedOperationException exception" do
      iterator = EdgeTypeIterator.new(*edges, "foo", false)
      expect { iterator.remove }.to raise_error Java::JavaLang::UnsupportedOperationException
    end
  end

end
