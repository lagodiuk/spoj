package spoj.dynamic_programming.aerolite;

// http://www.spoj.com/problems/AEROLITE/

public class AEROLITE_2 {

	public static void main(String[] args) {
		System.out.println(f(8, 5));
	}

	static int f(int n, int depth) {
		return f(0, n, depth, false);
	}

	static int f(int open, int curr, int depth, boolean reached) {
		if (curr == 0) {
			return reached || (open == depth) ? 1 : 0;
		}

		int result = 0;

		if (open == depth) {
			result += f(open - 1, curr, depth, true);
		} else {
			result += f(open + 1, curr - 1, depth, reached);
			if (open > 0) {
				result += f(open - 1, curr, depth, reached);
			}
		}

		return result;
	}
}
