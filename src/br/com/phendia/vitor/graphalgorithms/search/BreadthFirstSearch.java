package br.com.phendia.vitor.graphalgorithms.search;

import java.io.IOException;
import java.util.LinkedList;

import br.com.phendia.vitor.graphalgorithms.graph.Edge;
import br.com.phendia.vitor.graphalgorithms.graph.Graph;
import br.com.phendia.vitor.graphalgorithms.graph.PseudoDigraph;

public class BreadthFirstSearch extends GraphSearch {

	// A Java LinkedList can act as a queue
	private LinkedList<Integer> queue;

	private int[] color;
	private int[] distance;
	private Integer[] antecessor;
	private int initialNode;

	private Boolean hasLoop;
	private Boolean isSymmetric;

	private Graph transposedGraph;

	public BreadthFirstSearch(Graph graph) {
		super(graph);
		initialNode = 0;
	}

	@Override
	public void search(int vertice) {
		this.initialNode = vertice;
		this.queue = new LinkedList<Integer>();
		this.color = new int[getGraph().getNumNodes()];
		this.distance = new int[getGraph().getNumNodes()];
		this.antecessor = new Integer[getGraph().getNumNodes()];

		this.hasLoop = false;
		this.isSymmetric = true;
		this.transposedGraph = new PseudoDigraph(getGraph().getNumNodes());

		/*
		 * Color is already set to WHITE since starting value for all elements
		 * of the array is 0. Likewise, Predecessor is already set to null
		 */
		for (int node : getNodes()) {
			this.distance[node] = Integer.MAX_VALUE;
		}

		this.color[vertice] = GRAY;
		this.distance[vertice] = 0;
		this.queue.add(vertice);

		while (!this.queue.isEmpty()) {
			int u = this.queue.poll();
			for (Edge e : getGraph().getAdjacentNodes(u)) {
				int v = e.getNodeTwo();
				if (this.color[v] == WHITE) {
					this.color[v] = GRAY;
					this.distance[v] = this.distance[u] + 1;
					this.antecessor[v] = u;
					this.queue.add(v);
				}
				this.transposedGraph.addEdge(v, u);
				if (v == u) {
					this.hasLoop = true;
				}
				if (!getGraph().existsEdgeBetween(v, u)) {
					this.isSymmetric = false;
				}
			}
			this.color[u] = BLACK;
		}
	}

	public Boolean getHasLoop() {
		return hasLoop;
	}

	public Boolean isSymmetric() {
		return isSymmetric;
	}

	public int getInitialNode() {
		return initialNode;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\n");
		sb.append("\t\"initial_node\" : " + this.initialNode + ",\n");
		sb.append("\t\"has_loop\" : " + this.hasLoop + ",\n");
		sb.append("\t\"is_symmetric\" : " + this.isSymmetric + ",\n");
		sb.append("\t\"transposed_graph\" : "
				+ addCharsBetweenLines(this.transposedGraph.toString(), "\t")
				+ ",\n");
		sb.append("\t\"nodes\" : " + "[\n");
		for (int i = 0;;) {
			sb.append("\t\t" + i + " : {\n");
			switch (this.color[i]) {
			case GRAY:
				sb.append("\t\t\t\"color\" : \"gray\"\n");
				break;
			case BLACK:
				sb.append("\t\t\t\"color\" : \"black\"\n");
				break;
			case WHITE:
			default:
				sb.append("\t\t\t\"color\" : \"white\"\n");
				break;
			}
			sb.append("\t\t\t\"distance\" : " + this.distance[i] + "\n");
			sb.append("\t\t\t\"antecessor\" : " + this.antecessor[i] + "\n");
			sb.append("\t\t}");
			if (++i >= getGraph().getNumNodes()) {
				sb.append("\n");
				break;
			} else {
				sb.append(",\n");
				continue;
			}
		}
		sb.append("\t]\n");
		sb.append("}");
		return sb.toString();
	}

	private String addCharsBetweenLines(String string, String chars) {
		String[] lines = string.split("\n");
		String newString = lines[0];
		for (int i = 1; i < lines.length; i++) {
			newString += "\n" + (chars + lines[i]);
		}
		return newString;

	}

	public static void main(String[] args) {
		try {
			Graph g = PseudoDigraph.readFromFile("resources/graph1.gdf");
			System.out.println(g);
			BreadthFirstSearch bfs = new BreadthFirstSearch(g);
			System.out.println(bfs);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}