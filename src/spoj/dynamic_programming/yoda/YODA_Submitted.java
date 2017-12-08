package spoj.dynamic_programming.yoda;

// http://www.spoj.com/problems/YODA/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

public class YODA_Submitted {

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String s;
		while ((s = br.readLine()) != null) {
			System.out.println(calculatePalindroms(s));
		}
	}

	static int[] charCount = new int[32];
	static int[] numCount = new int[10000];

	static long calculatePalindroms(String s) {
		Arrays.fill(charCount, 0);
		Arrays.fill(numCount, 0);

		char c;
		for (int i = 0; i < s.length(); i++) {
			c = s.charAt(i);
			if ((c >= 'a') && (c <= 'z')) {
				charCount[c - 'a']++;
			} else if ((c >= 'A') && (c <= 'Z')) {
				charCount[c - 'A']++;
			}
		}

		int oddCount = 0;
		for (int i = 0; i < charCount.length; i++) {
			if ((charCount[i] % 2) == 1) {
				oddCount++;
			}
		}

		if (oddCount > 1) {
			return 0;
		}

		for (int i = 0; i < charCount.length; i++) {
			charCount[i] /= 2;
		}

		int sum = 0;
		for (int i = 0; i < charCount.length; i++) {
			sum += charCount[i];
		}

		int maxNum = -1;

		for (int j = 2; j <= sum; j++) {
			numCount[j]++;
			maxNum = Math.max(maxNum, j);
		}

		for (int i = 0; i < charCount.length; i++) {
			for (int j = 2; j <= charCount[i]; j++) {
				numCount[j]--;
				maxNum = Math.max(maxNum, j);
			}
		}

		double tmp = 0;
		for (int i = 0; i <= maxNum; i++) {
			if (numCount[i] != 0) {
				tmp += numCount[i] * Math.log(i);
			}
		}

		return Math.round(Math.exp(tmp));
	}
}
