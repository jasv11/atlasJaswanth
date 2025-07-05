public class task10 {
    static class Edge {
        int src;    
        int dest;   

        Edge(int src, int dest) {
            this.src = src;
            this.dest = dest;
        }
    }

    static class Graph {
        int vertices;
        int edges;
        Edge[] edgeArray;

        Graph(int vertices, int edges) {
            this.vertices = vertices;
            this.edges = edges;
            edgeArray = new Edge[edges];
            System.out.println("Graph created with " + vertices + 
                             " vertices and " + edges + " edges\n");
        }

        void addEdge(int index, int src, int dest) {
            edgeArray[index] = new Edge(src, dest);
        }

        void displayEdges() {
            System.out.println("Graph Edges:");
            for(int i = 0; i < edges; i++) {
                System.out.println(edgeArray[i].src + " - " + 
                                 edgeArray[i].dest);
            }
        }
    }

    public static void main(String[] args) {
        Graph graph = new Graph(5, 8);

        graph.addEdge(0, 1, 2);
        graph.addEdge(1, 1, 3);
        graph.addEdge(2, 1, 4);
        graph.addEdge(3, 2, 4);
        graph.addEdge(4, 2, 5);
        graph.addEdge(5, 3, 4);
        graph.addEdge(6, 3, 5);
        graph.addEdge(7, 4, 5);

        graph.displayEdges();
    }
}