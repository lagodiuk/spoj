package spoj.dynamic_programming;

// http://www.spoj.com/problems/GNY07H/

public class GNY07H {

	public static void main(String[] args) {
		System.out.println(coverageVariants(2));
		System.out.println(coverageVariants(3));
		System.out.println(coverageVariants(7));
		System.out.println();
		System.out.println(coverageVariants(4));
		System.out.println(coverageVariants(5));
		System.out.println(coverageVariants(6));
		System.out.println(coverageVariants(7));
	}

	enum Mask {
		_1111,
		_1001,
		_0110,
		_0011
	}

	static int coverageVariants(int width) {
		return coverageVariants(width, Mask._1111);
	}

	static int coverageVariants(int width, Mask mask) {
		if (width < 0) {
			return 0;
		}

		if (width == 0) {
			return (mask == Mask._1111) ? 1 : 0;
		}

		switch (mask) {
			case _1111 :
				return coverageVariants(width - 2, Mask._1111)
						+ coverageVariants(width - 1, Mask._1111)
						+ coverageVariants(width, Mask._1001) +
						(2 * coverageVariants(width - 1, Mask._0011));

			case _1001 :
				return coverageVariants(width - 1, Mask._0110);

			case _0110 :
				return coverageVariants(width - 1, Mask._1111)
						+ coverageVariants(width - 1, Mask._1001);

			case _0011 :
				return coverageVariants(width - 1, Mask._1111)
						+ coverageVariants(width - 1, Mask._0011);
		}

		throw new RuntimeException();
	}
}
