import java.util.*;
import java.io.*;
// The On-Line Encyclopedia of Integer Sequences : https://oeis.org/

public class B_Rooks_Defenders {

	static Scanner sc = new Scanner(System.in);
	static PrintWriter pw = new PrintWriter(System.out);
	static StringBuilder sb = new StringBuilder();

	public static void main(String[] args) throws IOException {
		int n =sc.nextInt(), q = sc.nextInt();
		int N = 1;
		while (N < n) { // setting N to n's nearest power of 2
			N *= 2;
		}
        boolean[] preRows = new boolean[N + 1], preColumns = new boolean[N+1];
        for(int i = n + 1; i<N+1; i++){
            preColumns[i] = preRows[i] = true;
        }
        pw.println(Arrays.toString(preColumns));
		SegmentTree rows = new SegmentTree(preRows), columns = new SegmentTree(preColumns);
		for(int i = 0; i<q; i++){
			int type = sc.nextInt();
			if(type == 1){
				int x = sc.nextInt(), y = sc.nextInt();
				rows.updateNode(x, true);
				columns.updateNode(y, true);
			}
			else if(type == 2){
				int x = sc.nextInt(), y = sc.nextInt();
				rows.updateNode(x, false);
				columns.updateNode(y, false);
			}
			else{
				int x1 = sc.nextInt(), y1 = sc.nextInt(), x2 = sc.nextInt(), y2 = sc.nextInt();
				if(rows.query(x1, x2) || columns.query(y1, y2))
					pw.println("Yes");
				else 
					pw.println("No");
			}
		}

		pw.flush();
		pw.close();
	}

    static class SegmentTree {
		boolean[] arr, tree;
		int N;
		
		public SegmentTree(boolean[] arr) {
            this.arr = arr;
            this.N = arr.length-1;
			this.tree = new boolean[2 * N];
			buildTree(1, 1, N);
		}

		public SegmentTree(int N) { // there is no input array , initially empty
			this.N = N;
			this.tree = new boolean[N << 1];
		}
		
		public void buildTree(int idx, int left, int right) { // O(n)
			if (left == right) {
				tree[idx] = arr[left];
				return;
			}
			int mid = left + (right - left) / 2;
			buildTree(idx * 2, left, mid);
			buildTree(idx * 2 + 1, mid + 1, right);
			tree[idx] = tree[idx * 2] && tree[idx * 2 + 1];

		}
		
		public boolean query(int left, int right) { // O(logn)
			return query(left, right, 1, N, 1);
		}
		
		public boolean query(int queryLeft, int queryRight, int currLeft, int currRight, int idx) {
			// Current range is FULLY INCLUDED in the query range
			if(queryLeft <= currLeft && queryRight >= currRight) {
				return tree[idx];
			}
			// Currant range is FULLY EXCLUDED in the query range
			if(queryRight < currLeft || queryLeft > currRight) {
				return true; // or what ever the problem's identity is
			}
			// Current range PARTIALLY INCLUDED in the query range
			int mid = currLeft + (currRight - currLeft) / 2;
			boolean leftChild = query(queryLeft, queryRight, currLeft, mid, idx*2);
			boolean rightChild = query(queryLeft, queryRight, mid + 1, currRight, idx*2+1);
			return leftChild && rightChild;
			
		}
		
		public void updateNode(int Arrayindx, boolean value) { // O(logn)
			int treeIndx = Arrayindx + N - 1;
			tree[treeIndx] = value;
			while(treeIndx>1) {
				treeIndx /= 2;
				tree[treeIndx] = tree[treeIndx*2] && tree[treeIndx*2 + 1];
			}
		}
	}

	static class DSU {
		int[] parent;
		int[] rank;

		public DSU(int len) {
			parent = new int[len + 1];
			rank = new int[len + 1];
			for (int i = 0; i <= len; i++)
				parent[i] = i;
		}

		public int find(int x) {
			if (parent[x] == x)
				return x;
			return parent[x] = find(parent[x]);
		}

		public void union(int x, int y) {
			int a = find(x);
			int b = find(y);

			if (rank[a] <= rank[b]) {
				parent[a] = b;
				if (rank[a] == rank[b])
					rank[b]++;
			} else
				parent[b] = a;
		}
	}

	static class Pair implements Comparable<Pair> {

		public int compareTo(Pair p) {
			return 0;
		}

	}
	
	

	public static void swap(int[] arr, int i, int j) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}

	// O(log min(a, b) )
	public static long gcd(long a, long b) {
		if (b == 0)
			return a;
		return gcd(b, a % b);

	}

	public static long lcm(long a, long b) {
		return a * b / gcd(a, b);
	}

}

class Scanner {
	StringTokenizer st;
	BufferedReader br;

	public Scanner(InputStream s) {
		br = new BufferedReader(new InputStreamReader(s));
	}

	public Scanner(String s) throws FileNotFoundException {
		br = new BufferedReader(new FileReader(s));
	}

	public Integer[] nextIArray(int size) throws IOException {
		Integer[] res = new Integer[size];
		for (int i = 0; i < size; i++)
			res[i] = this.nextInt();
		return res;
	}

	public Long[] nextLArray(int size) throws IOException {
		Long[] res = new Long[size];
		for (int i = 0; i < size; i++)
			res[i] = this.nextLong();
		return res;
	}

	public String next() throws IOException {
		while (st == null || !st.hasMoreTokens())
			st = new StringTokenizer(br.readLine());
		return st.nextToken();
	}

	public int nextInt() throws IOException {
		return Integer.parseInt(next());
	}

	public long nextLong() throws IOException {
		return Long.parseLong(next());
	}

	public String nextLine() throws IOException {
		return br.readLine();
	}

	public double nextDouble() throws IOException {
		String x = next();
		StringBuilder sb = new StringBuilder("0");
		double res = 0, f = 1;
		boolean dec = false, neg = false;
		int start = 0;
		if (x.charAt(0) == '-') {
			neg = true;
			start++;
		}
		for (int i = start; i < x.length(); i++)
			if (x.charAt(i) == '.') {
				res = Long.parseLong(sb.toString());
				sb = new StringBuilder("0");
				dec = true;
			} else {
				sb.append(x.charAt(i));
				if (dec)
					f *= 10;
			}
		res += Long.parseLong(sb.toString()) / f;
		return res * (neg ? -1 : 1);
	}

	public boolean ready() throws IOException {
		return br.ready();
	}
}
