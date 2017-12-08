package spoj.dynamic_programming.mcirgame;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;

// http://www.spoj.com/problems/MCIRGAME/en/

// Actually, the solution is Catalan numbers sequence: https://oeis.org/A000108

public class MCIRGAME_With_Memoization {

	public static void main(String[] args) throws Exception {
		BigInteger[] memoized = calculateCatalanNumbers(150);

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String s;
		while ((s = br.readLine()) != null) {
			if (s.isEmpty()) {
				continue;
			}
			int x = Integer.parseInt(s);
			if (x == -1) {
				break;
			}
			System.out.println(memoized[x * 2]);
		}
	}

	private static BigInteger[] calculateCatalanNumbers(int amount) {
		BigInteger[] memoized = new BigInteger[(amount * 2) + 1];
		memoized[0] = BigInteger.ONE;
		memoized[2] = BigInteger.ONE;
		for (int N = 4; N < memoized.length; N += 2) {
			BigInteger result = BigInteger.ZERO;
			for (int k = 2; k <= N; k += 2) {
				result = result.add(memoized[k - 2].multiply(memoized[N - k]));
			}
			memoized[N] = result;
		}
		return memoized;
	}
}
