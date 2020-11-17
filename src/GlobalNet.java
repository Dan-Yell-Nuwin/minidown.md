import java.util.ArrayList;

public class GlobalNet
{
    //creates a global network 
    //O : the original graph
    //regions: the regional graphs
    public static Graph run(Graph O, Graph[] regions) 
    {
	    //start
        // add each edge from regions to new graph.
        Graph g  = new Graph(O.V());
        g.setCodes(O.getCodes());
        for (Graph greph: regions) {
            Edge [] edges = greph.sortedEdges().toArray(new Edge[0]);
            for (Edge e : edges) {
                g.addEdge(e);
            }
        }
        System.out.println(g);
        // run  djikstras on every node in  each  region
        for (int i = 0; i < regions.length; i++) {
            Graph a = regions[i];
            System.out.println(a);
            boolean [] reg_a = a.getStray();
            // connect it to all the other regions
            for (int j = i + 1; j < regions.length; j++) {
                Graph b = regions[j];
                System.out.println(b);
                boolean [] reg_b = b.getStray();
                System.out.println("hi");
                System.out.println(b.V());
                for (int num = 0; num < reg_b.length; num++) {
                    if (!reg_b[num]) System.out.println(num);
                }
                System.out.println("lol");
                int [] a_to_b = new int[2];//a to b.
                int shortest_distance = Integer.MAX_VALUE;
                // get the shortest distance path from region a to region b
                for (int u = 0; u < reg_a.length; u++) {
                    if (!reg_a[u]) {
                        System.out.print(u  + " ");
                        int [] distances = new int [O.V()];
                        int [] prev = new int [O.V()];
                        dijkstrasShortestPath(O, u, distances, prev);
                        for (int v = 0; v < reg_b.length; v++) {
                            if (!reg_b[v]) {
                                System.out.print(v + " ");
                                if (distances[v] > 0 && distances[v] < shortest_distance) {
                                    shortest_distance = distances[v];
                                    a_to_b[0] = u;
                                    a_to_b[1] = v;
                                }
                            }
                        }
                    }
                }
                System.out.println();
                // build the path from  a to  b onto G.
                System.out.println(a_to_b[0] + " " + a_to_b[1]);
                int [] dist = new int[O.V()];
                int [] prev = new int[O.V()];
                dijkstrasShortestPath(O, a_to_b[0],dist, prev);
                // now dfs throughout the path.
                ArrayList<Integer> v_list = new ArrayList<>();
                int previous = a_to_b[1];
                System.out.println(previous);
                v_list.add(a_to_b[1]);
                while (previous != a_to_b[0]) {
                    Edge e = O.getEdge(previous, prev[previous]);
                    previous = prev[previous];
                    System.out.println(previous);
                    g.addEdge(e);
                }
                System.out.println(g);
                // now have list of the path
            }
        }
        //g = g.connGraph();
        System.out.println(regions.length);
        System.out.println(g);
        System.out.println(O);
        return g;
    }
    public static void dijkstrasShortestPath(Graph G, int start, int [] dist, int [] prev) {
        DistQueue distQueue = new DistQueue(G.V());
        dist[start] = 0;
        for (int i = 0; i < G.V(); i++) {
            if (i != start) dist[i] = Integer.MAX_VALUE;
            prev[i] = -1;
            distQueue.insert(i, dist[i]);
        }
        while (!distQueue.isEmpty()) {
            int u =distQueue.delMin();
            for (int v :  G.getAdjList()[u]) {
                if (distQueue.inQueue(v)) {
                    int alt = dist[u] + G.getEdge(u,v).w;
                    if (alt < dist[v]) {
                        dist[v] = alt;
                        prev[v] = u;
                        distQueue.set(v,alt);
                    }
                }
            }
        }
    }
    static int minDistance(Graph O, int dist[], boolean sptSet[]) {
        int min = Integer.MAX_VALUE, min_index = -1;
        for (int v = 0; v < O.V(); v++) {
            if (sptSet[v] == false && dist[v] <= min) {
                min  = dist[v];
                min_index = v;
            }
        }
        return min_index;
    }
    static int [] djikstra_min_path(Graph O, int start) {// get shortest distance
        int dist[] = new int[O.V()];
        boolean sptSet[] = new boolean[O.V()];
        for (int i  = 0 ; i < O.V(); i++) {
            dist[i] = Integer.MAX_VALUE;
            sptSet[i] = false;
        }
        dist[start] = 0;
        // PERFORM DJIKSTRAS
        for (int i = 0; i < O.V(); i++) {
            int u = minDistance(O, dist, sptSet);
            sptSet[u] = true;
            for (int v = 0; v< O.V(); v++) {
                if (!sptSet[v] && O.getAdjList()[u].contains(v) && dist[u] != Integer.MAX_VALUE && dist[u] + O.getEdge(u,v).w < dist[v]) {
                    dist[v] = dist[u] + O.getEdge(u,v).w;
                }
            }
        }
        return dist;
    }
    //find shortest
    static int [] djikstpath(Graph G, ArrayList<Integer>[] adj_list, int start) {
        int V = adj_list.length;
        int smallest_dist[] = new int [V];
        boolean []add = new boolean[V];
        for (int i = 0; i < V; i++) {
            smallest_dist[i] = Integer.MAX_VALUE;
            add[i] = false;
        }
        smallest_dist[start] = -1;
        int [] parent = new int[V];
        parent[start] = -1;

        for (int i = 0; i < V; i++) {
            int nearest = -1;
            int shortest = Integer.MAX_VALUE;
            for (int v = 0; v < V; v++) {
                if (!add[v] && smallest_dist[v] < shortest) {
                    nearest = v;
                    shortest = smallest_dist[v];
                }
            }
            add[nearest] = true;
            // update dist values for nearest_vertex
            for (int index=  0; index < V; index++) {
                if (adj_list[nearest].contains(index)) {
                    Edge e = G.getEdge(nearest,index);
                    parent[index] = nearest;
                    smallest_dist[index] = shortest + e.w;
                }
            }
        }
        return parent;
    }
}
    
    
    