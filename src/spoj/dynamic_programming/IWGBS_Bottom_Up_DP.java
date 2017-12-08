package spoj.dynamic_programming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.StringTokenizer;

// http://www.spoj.com/problems/IWGBS/

public class IWGBS_Bottom_Up_DP {

	public static BigInteger calculatePossibleCombinations(int n) {
		BigInteger endWithZeroCount = BigInteger.ONE;
		BigInteger endWithOneCount = BigInteger.ONE;

		BigInteger tmp = null;

		for (int i = 1; i < n; i++) {
			tmp = endWithOneCount;
			endWithOneCount = endWithOneCount.add(endWithZeroCount);
			endWithZeroCount = tmp;
		}

		return endWithOneCount.add(endWithZeroCount);
	}

	public static void main(String[] args) throws Exception {
		// System.out.println(calculatePossibleCombinations(1));
		// System.out.println(calculatePossibleCombinations(2));
		// System.out.println(calculatePossibleCombinations(3));
		// System.out.println(calculatePossibleCombinations(4));
		// System.out.println(calculatePossibleCombinations(5));
		FastScanner in = new FastScanner(new InputStreamReader(System.in));
		int binaruDigitsCount = in.nextInt();
		System.out.println(calculatePossibleCombinations(binaruDigitsCount));
	}

	// http://neerc.ifmo.ru/trains/information/Template.java
	static final class FastScanner {
		BufferedReader br;
		StringTokenizer st;

		FastScanner(InputStreamReader in) throws Exception {
			this.br = new BufferedReader(in);
		}

		String next() {
			while ((this.st == null) || !this.st.hasMoreTokens()) {
				try {
					this.st = new StringTokenizer(this.br.readLine());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return this.st.nextToken();
		}

		int nextInt() {
			return Integer.parseInt(this.next());
		}
	}
}
