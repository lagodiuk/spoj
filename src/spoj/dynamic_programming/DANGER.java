package spoj.dynamic_programming;

// http://www.spoj.com/problems/DANGER/

public class DANGER {

	static long getIndex(long amount) {
		long each = 2;
		long firstNumber = 1;
		long difference = 1;
		while (amount > 1) {
			if (((amount % 2) == 0) && ((each % 2) == 0)) {
				each = 2;
				amount = amount / 2;
				difference = difference * 2;
			} else if (((amount % 2) == 1) && ((each % 2) == 0)) {
				each = 1;
				amount = (amount / 2) + 1;
				difference = difference * 2;
			} else if (((amount % 2) == 0) && ((each % 2) == 1)) {
				each = 1;
				amount = amount / 2;
				firstNumber = firstNumber + difference;
				difference = difference * 2;
			} else if (((amount % 2) == 1) && ((each % 2) == 1)) {
				each = 2;
				amount = amount / 2;
				firstNumber = firstNumber + difference;
				difference = difference * 2;
			} else {
				throw new RuntimeException();
			}
		}
		return firstNumber;
	}

	public static void main(String[] args) {
		System.out.println(getIndex(5));
		System.out.println(getIndex(10));
		System.out.println(getIndex(42));
		System.out.println(getIndex(66000000));
		System.out.println();
		System.out.println(getIndex(40));
		System.out.println(getIndex(99000000));
	}

}
