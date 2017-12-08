package spoj.dynamic_programming.mcirgame;

// http://www.spoj.com/problems/MCIRGAME/en/

// Actually, the solution is Catalan numbers sequence: https://oeis.org/A000108

public class MCIRGAME {

	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			System.out.print(f(i * 2) + ", ");
		}
	}

	// TODO: add memoization
	static long f(int N) {
		if ((N == 2) || (N == 0)) {
			return 1;
		}

		long result = 0;
		for (int k = 2; k <= N; k += 2) {
			result += f(k - 2) * f(2) * f(N - k);
		}
		return result;
	}
}
