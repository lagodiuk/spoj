package spoj.dynamic_programming;

// http://www.spoj.com/problems/GNYR09F/

/**
 *
 *
 * Input:
 * 5 2
 * 20 8
 * 30 17
 * 40 24
 * 50 37
 * 60 52
 * 70 59
 * 80 73
 * 90 84
 * 100 90
 *
 * Output:
 * 6
 * 63426
 * 1861225
 * 168212501
 * 44874764
 * 160916
 * 22937308
 * 99167
 * 15476
 * 23076518
 */

public class GNYR09F {

	public static void main(String... args) {
		System.out.println(solve(5, 2));
		System.out.println(solve(20, 8));
		System.out.println(solve(30, 17));
		System.out.println(solve(40, 24));
		System.out.println(solve(50, 37));
		System.out.println(solve(60, 52));
		System.out.println(solve(70, 59));
		System.out.println(solve(80, 73));
		System.out.println(solve(90, 84));
		System.out.println(solve(100, 90));
	}

	static long solve(int bitStringLen, int adjBitsCount) {
		Result r = f(bitStringLen, adjBitsCount);
		return r.e0 + r.e1;
	}

	static final int MAX_BIT_STRING_LEN = 100;

	static Result[][] MEMOIZED = new Result[MAX_BIT_STRING_LEN + 1][MAX_BIT_STRING_LEN + 1];

	static Result f(int bitStringLen, int adjBitsCount) {

		if ((adjBitsCount >= bitStringLen) || (adjBitsCount < 0)) {
			Result impossible = new Result();
			impossible.e0 = 0L;
			impossible.e1 = 0L;
			return impossible;
		}

		if (MEMOIZED[bitStringLen][adjBitsCount] != null) {
			return MEMOIZED[bitStringLen][adjBitsCount];
		}

		if (bitStringLen == 1) {
			Result smallestBitStr = new Result();
			smallestBitStr.e0 = 1L;
			smallestBitStr.e1 = 1L;
			MEMOIZED[bitStringLen][adjBitsCount] = smallestBitStr;
			return MEMOIZED[bitStringLen][adjBitsCount];
		}

		Result a = f(bitStringLen - 1, adjBitsCount - 1);
		Result b = f(bitStringLen - 1, adjBitsCount);

		Result result = new Result();
		result.e1 = a.e1 + b.e0;
		result.e0 = b.e0 + b.e1;

		MEMOIZED[bitStringLen][adjBitsCount] = result;
		return MEMOIZED[bitStringLen][adjBitsCount];
	}

	static class Result {
		// count of bit strings which ends with '1'
		Long e1;
		// count of bit strings which ends with '0'
		Long e0;
	}
}