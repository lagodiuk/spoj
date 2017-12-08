package spoj.dynamic_programming.inumber;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

// http://www.spoj.com/problems/INUMBER/

public class INUMBER_BFS_Submitted {

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int testsNum = Integer.parseInt(br.readLine());
		while (testsNum-- != 0) {
			solve(Integer.parseInt(br.readLine()));
		}
	}

	static final int MAX_NUM = 1001;
	static final int INF = 1000000;

	static int[][] dist = new int[MAX_NUM][MAX_NUM];

	static void solve(int num) {
		for (int[] x : dist) {
			Arrays.fill(x, INF);
		}

		List<State> queue = new LinkedList<>();
		State start = new State(num, 0);
		dist[start.sum][start.mod] = 0;
		queue.add(start);

		State curr = null;
		while (!queue.isEmpty()) {
			curr = queue.remove(0);
			if ((curr.sum == 0) && (curr.mod == 0)) {
				break;
			}

			for (int i = 0; i <= 9; i++) {
				State s = new State(curr.sum - i, ((curr.mod * 10) + i) % num);
				if ((s.sum >= 0) && (dist[s.sum][s.mod] > (dist[curr.sum][curr.mod] + 1))) {
					dist[s.sum][s.mod] = dist[curr.sum][curr.mod] + 1;
					s.prev = curr;
					queue.add(s);
				}
			}
		}

		StringBuilder sb = new StringBuilder();
		while (curr.prev != null) {
			sb.append(curr.prev.sum - curr.sum);
			curr = curr.prev;
		}
		System.out.println(sb.reverse().toString());
	}

	static class State {
		int sum;
		int mod;
		State prev;
		public State(int sum, int mod) {
			this.sum = sum;
			this.mod = mod;
			this.prev = null;
		}
	}
}
