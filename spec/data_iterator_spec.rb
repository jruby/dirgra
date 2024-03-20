require 'dirgra-0.5-SNAPSHOT.jar'
java_import 'org.jruby.dirgra.DirectedGraph'
java_import 'org.jruby.dirgra.DataIterator'
java_import 'java.util.NoSuchElementException'

require_relative 'helpers/vertex_id_helper'

describe "DataIterator" do
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

  # hasNext method doesn't use the source or destination of iterator at all
  # So specs are not written for iterator having source set to false
  describe "hasNext" do

    context "edges of given type" do

      it "returns true if the iterator contains an edge of given type" do
        iterator = DataIterator.new(*edges, "foo", true, false)
        expect(iterator.hasNext).to eq true
      end

      it "returns false if the iterator does not contain any edge of given type" do
        iterator = DataIterator.new(*edges, "bar", true, false)
        expect(iterator.hasNext).to eq false
      end

    end

    context "edges not of given type" do

      it "returns true if the iterator contains an edge not of given type" do
        iterator = DataIterator.new(*edges, "bar", true, true)
        expect(iterator.hasNext).to eq true
      end

      it "returns false if the iterator contains an edge of given type" do
        iterator = DataIterator.new(*edges, "foo", true, true)
        expect(iterator.hasNext).to eq false
      end

    end

    context "when iterator type is null" do

      context "edges of given type" do

        it "returns true if the iterator contains an edge of type nil" do
          # add an edge of type nil
          add_edge(four,one,nil)
          iterator = DataIterator.new(*edges, nil, true, false)
          expect(iterator.hasNext).to eq true
        end

        it "returns false if the iterator does not contain any edge of type nil" do
          iterator = DataIterator.new(*edges, nil, true, false)
          expect(iterator.hasNext).to eq false
        end

      end

      context "edges not of given type" do

        it "returns true if the iterator contains an edge not of type nil" do
          iterator = DataIterator.new(*edges, nil, true, true)
          expect(iterator.hasNext).to eq true
        end

        it "returns false if the iterator contains all edges of type nil" do
          # remove existing edges not of type nil
          remove_edge(one,two)
          remove_edge(two,three)
          # add an edge of type nil
          add_edge(four,one,nil)
          iterator = DataIterator.new(*edges, nil, true, true)
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
        iterator = DataIterator.new(*edges, "foo", true, true)
        expect(iterator.hasNext).to eq true
      end

      it "returns false if the iterator contains all edges not of type nil" do
        iterator = DataIterator.new(*edges, "foo", true, true)
        expect(iterator.hasNext).to eq false
      end
    end

  end

  describe "next" do

    context "when the iterator has next edge" do

      context "when asked for data of source vertex" do
        it "returns the data of the source of the edge" do
          iterator = DataIterator.new(*edges, "foo", true, false)
          expect([one, two]).to include iterator.next
        end
      end

      context "when asked for data of destination vertex" do
        it "returns the data of the destination of the edge" do
          iterator = DataIterator.new(*edges, "foo", false, false)
          expect([two, three]).to include iterator.next
        end
      end
    end

    context "when the iterator does not have next edge" do

      before do
        @empty_graph = DirectedGraph.new
      end

      it "throws NoSuchElementException for source data" do
        iterator = DataIterator.new(@empty_graph.edges.to_a, 0, "foo", true, false)
        expect { iterator.next }.to raise_error NoSuchElementException
      end

      it "throws NoSuchElementException for destination data" do
        iterator = DataIterator.new(@empty_graph.edges.to_a, 0, "foo", false, false)
        expect { iterator.next }.to raise_error NoSuchElementException
      end
    end
  end

  describe "remove" do

    it "throws UnsupportedOperationException exception" do
      iterator = DataIterator.new(graph.edges.to_a, 0, "foo", true, false)
      expect { iterator.remove }.to raise_error Java::JavaLang::UnsupportedOperationException
    end
  end

end
