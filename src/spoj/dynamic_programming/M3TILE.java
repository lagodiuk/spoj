package spoj.dynamic_programming;

// http://www.spoj.com/problems/M3TILE/
// Problem: M3TILE - LATGACH3

public class M3TILE {

	enum Mask {
		_111, _011, _001
	}

	static long combinations(int length, Mask border) {
		if ((length == 0) && (border == Mask._111)) {
			return 1;
		}

		if (length <= 0) {
			return 0;
		}

		switch (border) {
			case _111 :
				return (2 * combinations(length, Mask._001) /* + combinations(length - 1, Mask._011) */) + combinations(length - 2, Mask._111);

			case _011 :
				return combinations(length - 1, Mask._111) + combinations(length - 1, Mask._001);

			case _001 :
				return combinations(length - 1, Mask._011);

			default :
				throw new RuntimeException();
		}
	}

	public static void main(String[] args) {
		System.out.println(combinations(2, Mask._111));
		System.out.println(combinations(8, Mask._111));
		System.out.println(combinations(12, Mask._111));
		System.out.println();
		System.out.println(combinations(3, Mask._111));
		System.out.println(combinations(5, Mask._111));
		System.out.println(combinations(7, Mask._111));
	}

}
