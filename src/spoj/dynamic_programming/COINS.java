package spoj.dynamic_programming;

// http://www.spoj.com/problems/COINS/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class COINS {

	static Map<Integer, Long> MEMOIZED = new HashMap<>();

	static Long calculate(int x) {
		if (x == 0) {
			return 0L;
		}

		Long result = MEMOIZED.get(x);

		if (result != null) {
			return result;
		}

		result = calculate(x / 2) + calculate(x / 3) + calculate(x / 4);
		result = (result > x) ? result : x;
		MEMOIZED.put(x, result);

		return result;
	}

	public static void main(String[] args) throws Exception {
		String s;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while ((s = br.readLine()) != null) {
			System.out.println(calculate(Integer.parseInt(s)));
		}
	}
}
