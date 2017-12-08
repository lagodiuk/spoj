package spoj.dynamic_programming.yoda;

// http://www.spoj.com/problems/YODA/

import java.util.Arrays;

public class YODA {

	public static void main(String[] args) throws Exception {
		System.out.println(calculatePalindroms("A man, a plan, a canal, Panama!"));
		System.out.println(calculatePalindroms("arD,R!A"));
		System.out.println(calculatePalindroms("B.a.C1/"));
		System.out.println(calculatePalindroms("12[’;. =1"));
	}

	static int[] charCount = new int[32];
	static int[] numCount = new int[10000];

	static long calculatePalindroms(String s) {

		Arrays.fill(charCount, 0);
		Arrays.fill(numCount, 0);

		countLetters(s);

		int oddCount = countLettersWithOddAmount();
		if (oddCount > 1) {
			return 0;
		}

		halveCountOfLetters();

		long result = calculateMultinomialCoefficient();
		return result;
	}

	private static long calculateMultinomialCoefficient() {
		int sum = calculateTotalAmountOfLetters();

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

		long result = Math.round(Math.exp(tmp));
		return result;
	}

	private static int calculateTotalAmountOfLetters() {
		int sum = 0;
		for (int i = 0; i < charCount.length; i++) {
			sum += charCount[i];
		}
		return sum;
	}

	private static void halveCountOfLetters() {
		for (int i = 0; i < charCount.length; i++) {
			charCount[i] /= 2;
		}
	}

	private static int countLettersWithOddAmount() {
		int oddCount = 0;
		for (int i = 0; i < charCount.length; i++) {
			if ((charCount[i] % 2) == 1) {
				oddCount++;
			}
		}
		return oddCount;
	}

	private static void countLetters(String s) {
		char c;
		for (int i = 0; i < s.length(); i++) {
			c = s.charAt(i);
			if ((c >= 'a') && (c <= 'z')) {
				charCount[c - 'a']++;
			} else if ((c >= 'A') && (c <= 'Z')) {
				charCount[c - 'A']++;
			}
		}
	}
}
