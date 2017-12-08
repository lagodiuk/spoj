package spoj.dynamic_programming;

// Solution of: http://www.spoj.com/problems/FPOLICE/

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FPOLICE {

	static class CityRisk {
		long currRisk;

		int cityIdx;
		int currTime;

		int heapIndex = -1;

		@Override
		public String toString() {
			return "[city=" + this.cityIdx + ", time=" + this.currTime + ", risk=" + this.currRisk + ", heap=" + this.heapIndex + "]";
		}
	}

	static class City {
		int[] timesToOther;
		int[] risksToOther;
		public City(int[] times, int[] risks) {
			this.timesToOther = times;
			this.risksToOther = risks;
		}
	}

	static final int INF = 100000;
	static final int MAX_CITIES = 105;
	static final int MAX_TIMES = 255;

	static PriorityQueue pq = new PriorityQueue();

	static City[] targets = new City[MAX_CITIES];
	static CityRisk[][] dp = new CityRisk[MAX_CITIES][MAX_TIMES];
	static {
		for (int i = 0; i < MAX_CITIES; i++) {
			targets[i] = new City(new int[MAX_CITIES], new int[MAX_CITIES]);
		}
		for (int i = 0; i < MAX_CITIES; i++) {
			for (int j = 0; j < MAX_TIMES; j++) {
				dp[i][j] = new CityRisk();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);

		int testsCount = sc.nextInt();

		while (testsCount > 0) {

			int citiesCount = sc.nextInt();
			int maxTime = sc.nextInt();

			for (int i = 0; i < citiesCount; i++) {
				for (int j = 0; j < citiesCount; j++) {
					targets[i].timesToOther[j] = sc.nextInt();
				}
			}

			for (int i = 0; i < citiesCount; i++) {
				for (int j = 0; j < citiesCount; j++) {
					targets[i].risksToOther[j] = sc.nextInt();
				}
			}

			solve(maxTime, citiesCount);

			testsCount--;
		}
	}

	static void solve(int timeLimit, int citiesCount) {
		findMinimalRisk(timeLimit, citiesCount);

		long minRisk = INF;
		long time = INF;
		for (int i = timeLimit; i >= 0; i--) {
			if (dp[citiesCount - 1][i].currRisk <= minRisk) {
				minRisk = dp[citiesCount - 1][i].currRisk;
				time = i;
			}
		}

		if (minRisk == INF) {
			System.out.println("-1");
		} else {
			System.out.println(minRisk + " " + time);
		}
	}

	static void findMinimalRisk(int timeLimit, int citiesCount) {

		for (int i = 0; i < citiesCount; i++) {
			for (int j = 0; j <= timeLimit; j++) {
				dp[i][j].currRisk = INF;
				dp[i][j].heapIndex = -1;
				dp[i][j].cityIdx = i;
				dp[i][j].currTime = j;
			}
		}
		dp[0][0].currRisk = 0;

		pq.reset();
		pq.update(dp[0][0]);

		while (!pq.isEmpty()) {
			CityRisk curr = pq.getNearest();

			for (int i = 0; i < citiesCount; i++) {
				int newTime = curr.currTime + targets[curr.cityIdx].timesToOther[i];
				long newRisk = curr.currRisk + targets[curr.cityIdx].risksToOther[i];
				if ((newTime <= timeLimit) && (newRisk < dp[i][newTime].currRisk)) {
					dp[i][newTime].currRisk = newRisk;
					pq.update(dp[i][newTime]);
				}
			}
		}
	}

	static class PriorityQueue {

		List<CityRisk> list = new ArrayList<>((MAX_CITIES * MAX_TIMES) + 1);

		CityRisk getNearest() {
			CityRisk res = this.list.get(0);
			this.list.set(0, this.list.get(this.list.size() - 1));
			this.list.get(0).heapIndex = 0;
			this.list.remove(this.list.size() - 1);
			this.pushDown(0);
			return res;
		}

		void update(CityRisk tgt) {
			if (tgt.heapIndex == -1) {
				this.list.add(tgt);
				tgt.heapIndex = this.list.size() - 1;
			}
			this.pushUp(this.list.size() - 1);
			// System.out.println(this.list);
		}

		void pushDown(int index) {
			int min = this.left(index);
			if (min >= this.list.size()) {
				return;
			}

			int right = this.right(index);
			if ((right < this.list.size()) && (this.list.get(right).currRisk < this.list.get(min).currRisk)) {
				min = right;
			}

			if (this.list.get(min).currRisk < this.list.get(index).currRisk) {
				this.swap(min, index);
				this.pushDown(min);
			}
		}

		void pushUp(int index) {
			if (index == 0) {
				return;
			}

			int p = this.parent(index);
			if (this.list.get(index).currRisk < this.list.get(p).currRisk) {
				this.swap(index, p);
				this.pushUp(p);
			}
		}

		void swap(int x, int y) {
			CityRisk tmp = this.list.get(x);
			this.list.set(x, this.list.get(y));
			this.list.set(y, tmp);
			this.list.get(x).heapIndex = x;
			this.list.get(y).heapIndex = y;
		}

		int left(int x) {
			return (2 * x) + 1;
		}

		int right(int x) {
			return (2 * x) + 2;
		}

		int parent(int x) {
			return (x - 1) / 2;
		}

		boolean isEmpty() {
			return this.list.isEmpty();
		}

		void reset() {
			this.list.clear();
		}
	}
}
