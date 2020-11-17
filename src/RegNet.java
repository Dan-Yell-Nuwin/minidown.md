import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class RegNet
{
    //creates a regional network
    //G: the original graph
    //max: the budget
    public static Graph run(Graph G, int max)
    {
        //To be implemented
        //kruskals
        UnionFind uf = new UnionFind(G.V());
        Graph res = new Graph(G.V());
        ArrayList<Edge> edges = G.sortedEdges();
        res.setCodes(G.getCodes());
        for (int i = 0; i < edges.size(); i++) {
            Edge edge = edges.get(i);
            int x = uf.find(edge.ui());
            int y = uf.find(edge.vi());
            if (x != y) {
                res.addEdge(edge);
                uf.union(x,y);
            }
        }
        while (res.totalWeight() > max) {
            ArrayList<Edge> addedEdges = res.sortedEdges();
            res = res.connGraph();
            for (int i = addedEdges.size() - 1; i >=0; i--) {
                Graph temp = res;
                Edge e = addedEdges.get(i);
                temp.removeEdge(e);
                temp = temp.connGraph();
                if (isConnected(temp)) {
                    res = temp;
                    break;
                }
                else {
                    temp.addEdge(e);
                    res  = temp;
                }
            }
        }
        //start to add edges
        ArrayList<int[]> list = new ArrayList<>();
        for (int i = 0; i < res.V(); i++) {
            int [] stop_arr = new int[res.V()];
            boolean [] visited = new boolean[res.V()];
            visited[i] = true;
            stop_arr[i] = 0;
            dfs(res,i,0,stop_arr,visited);
            for (int j = i + 1; j < res.V();j++) {
                int[] arr = new int[4];
                arr[0] = i;
                arr[1] = j;
                arr[2] = stop_arr[j];
                Edge e = G.getEdge(i,j);
                arr[3] = e.w;
                if (arr[2] != 0) list.add(arr);
            }
        }
        int [][] stop_result = new int[list.size()][3];
        for (int i = 0; i < list.size(); i++) {
            stop_result[i] = list.get(i);
        }
        Arrays.sort(stop_result,(a,b)->b[2] == a[2] ? Integer.compare(b[3],a[3]) : Integer.compare(a[2],b[2]));
        for (int i = stop_result.length - 1; i >=0; i--) {
            int [] arr = stop_result[i];
            Edge e = G.getEdge(arr[0],arr[1]);
            if (e.w + res.totalWeight() > max) {
                continue;
            }
            else {
                res.addEdge(e);
            }
        }
        return res;
    }
    public static void DFS(int source, ArrayList<Integer> []adjlist, boolean [] visited) {
        visited[source] = true;
        for (int i = 0; i < adjlist[source].size(); i++) {
            int neighbor = adjlist[source].get(i);
            if (!visited[neighbor]) {
                DFS(neighbor, adjlist, visited);
            }
        }
    }
    public static boolean isConnected(Graph graph) {
        ArrayList<Integer>[] adjlist = graph.getAdjList();
        boolean[] visited = new boolean [graph.V()];
        DFS(0,adjlist,visited);
        int count = 0;
        for (int i = 0; i < visited.length; i++) {
            if (visited[i]) {
                count++;
            }
        }
        return count == graph.V();
    }
    public static void dfs(Graph g, int start, int num_stop, int [] stop_arr, boolean [] visited) {
        ArrayList<Integer> list = g.getAdjList()[start];
        for (int i: list) {
            if (!visited[i]) {
                stop_arr[i] = num_stop;
                visited[i] = true;
                dfs(g, i,num_stop + 1, stop_arr, visited);
            }
        }
    }

}