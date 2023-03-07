import java.util.*;
import java.io.*;
// The On-Line Encyclopedia of Integer Sequences : https://oeis.org/
/*
* Test Cases Draft

 */

public class C_Distinct_Characters_Queries {

    static Scanner sc = new Scanner(System.in);
    static PrintWriter pw = new PrintWriter(System.out);
    static StringBuilder sb = new StringBuilder();

    public static void main(String[] args) throws IOException {
        char[] s = sc.nextLine().toCharArray();
        int len = s.length;
        int N = 1;
        while (len > N)
            N *= 2;
        SegmentTree tree = new SegmentTree(N, s);
        int q = sc.nextInt();
        while (q-- > 0) {
            int type = sc.nextInt();
            if (type == 2) {
                int l = sc.nextInt(), r = sc.nextInt();
                pw.println(tree.query(l, r));
            } else {
                int pos = sc.nextInt();
                char c = sc.next().charAt(0);
                tree.updatePoint(pos, c);
            }
        }
        pw.flush();
        pw.close();
    }

    static class SegmentTree {
        int[][] tree;
        char[] arr;
        int N;

        public SegmentTree(int N, char[] arr) { // there is no input array , initially empty
            this.N = N;
            this.arr = arr;
            tree = new int[2 * N][27];
            buildTree(1, 1, arr.length, arr);
        }

        public void buildTree(int idx, int left, int right, char[] arr) { // O(n)
            if (left == right) {
                tree[idx][arr[left-1] - 'a'] = 1 ;
                return;
            }
            int mid = left + (right - left) / 2;
            buildTree(idx * 2, left, mid, arr);
            buildTree(idx * 2 + 1, mid + 1, right, arr);
            oring(tree[idx * 2], tree[idx * 2 + 1], tree[idx]);
    
        }

        public void oring(int[] a1, int[] a2, int[] res){
            for(int i = 0; i<res.length; i++){
                res[i] = a1[i] | a2[i];
            }
        }

        public void updatePoint(int arrIdx, char c) { // update = set
            int tIdx = arrIdx + N - 1;
            tree[tIdx][arr[arrIdx -1] - 'a'] = 0;
            arr[arrIdx - 1] = c;
            tree[tIdx][c  - 'a'] = 1;
            while (tIdx > 1) {
                tIdx /= 2;
                oring(tree[tIdx * 2], tree[tIdx * 2 + 1], tree[tIdx]);
                
            }
        }


        public int query(int l, int r) {
            int res[] = new int[27];
            query(1, 1, N, l, r, res);
            int ans = 0;
            for (int i : res) {
                if (i == 1)
                    ans++;
            }
            return ans;
        }

        public void query(int node, int start, int end, int l, int r, int[] res ) {
            if (start >= l && end <= r) {
                for(int i = 0; i<res.length; i++){
                    res[i] |= tree[node][i]; 
                }
                return;
            }
            if (start > r || end < l)
                return; // value does not affect the answer
            int mid = start + end >> 1;
            int leftChild = node << 1, rightChild = leftChild | 1;
            query(leftChild, start, mid, l, r, res);
            query(rightChild, mid + 1, end, l, r, res);
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
