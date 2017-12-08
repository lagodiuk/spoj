package spoj.dynamic_programming;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;

// http://www.spoj.com/problems/IWGBS/

// This is actually a Fibonnaci sequence!
public class IWGBS_Via_Matrix_Exponentiation {

	public static BigInteger[][] multiply2x2matrices(BigInteger[][] a, BigInteger[][] b) {
		return new BigInteger[][]{
				{a[0][0].multiply(b[0][0]).add(a[0][1].multiply(b[1][0])), a[0][0].multiply(b[0][1]).add(a[0][1].multiply(b[1][1]))},
				{a[1][0].multiply(b[0][0]).add(a[1][1].multiply(b[1][0])), a[1][0].multiply(b[0][1]).add(a[1][1].multiply(b[1][1]))}
		};
	}

	private static BigInteger[][] baseMatrix = {
			{BigInteger.ONE, BigInteger.ONE},
			{BigInteger.ONE, BigInteger.ZERO}
	};

	public static BigInteger[][] pow(int n) {
		if (n == 1) {
			return baseMatrix;
		}

		BigInteger[][] tmp = pow(n / 2);
		if ((n % 2) == 0) {
			return multiply2x2matrices(tmp, tmp);
		} else {
			return multiply2x2matrices(multiply2x2matrices(tmp, tmp), baseMatrix);
		}
	}

	// O(log(N)) solution
	private static BigInteger fibonnaciViaMatrixExponentiation(int binaryDigitsCount) {
		return pow(binaryDigitsCount + 1)[0][0];
	}

	// O(N) solution
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
		// BigInteger[][] a = pow(1001);
		// System.out.println(Arrays.toString(a[0]));
		// System.out.println(Arrays.toString(a[1]));
		// System.out.println(calculatePossibleCombinations(1000));

		// System.out.println(calculatePossibleCombinations(1));
		// System.out.println(calculatePossibleCombinations(2));
		// System.out.println(calculatePossibleCombinations(3));
		// System.out.println(calculatePossibleCombinations(4));
		// System.out.println(calculatePossibleCombinations(5));

		int binaryDigitsCount = Integer.parseInt(new BufferedReader(new InputStreamReader(System.in)).readLine());
		System.out.println(fibonnaciViaMatrixExponentiation(binaryDigitsCount));
	}
}
