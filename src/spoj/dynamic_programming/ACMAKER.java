package spoj.dynamic_programming;

// http://www.spoj.com/problems/ACMAKER/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class ACMAKER {

	private static final String LAST_CASE = "LAST CASE";

	public static void main(String[] args) throws IOException {
		Set<String> stopWords = new HashSet<>(101);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String s;
		int stopWordsCount;

		stopWordsCount = Integer.parseInt(br.readLine());
		while (stopWordsCount > 0) {
			stopWords.clear();
			for (int i = 0; i < stopWordsCount; ++i) {
				stopWords.add(br.readLine());
			}
			while (!(s = br.readLine()).equals(LAST_CASE)) {
				String[] words = s.split(" ");
				String acronym = words[0];
				int result = calculateAcronymVariantsDP(words, stopWords);
				if (result > 0) {
					System.out.println(acronym + " can be formed in " + result + " ways");
				} else {
					System.out.println(acronym + " is not a valid abbreviation");
				}
			}
			stopWordsCount = Integer.parseInt(br.readLine());
		}
	}

	public static void mainTest(String[] args) {
		System.out.println(calculateSubsequenceOccurence("123", "3141592653"));
		System.out.println(calculateSubsequenceOccurence(new StringBuilder("123").reverse().toString(), new StringBuilder("3141592653").reverse().toString()));
		System.out.println(calculateSubsequenceOccurence("123", "12223"));
		System.out.println(calculateSubsequenceOccurence("123", "112_2233_33"));
		System.out.println(calculateSubsequenceOccurence("123", "1221323"));

		System.out.println();

		Set<String> stopWords = new HashSet<>();

		stopWords.add("and");
		stopWords.add("of");
		System.out.println(calculateAcronymVariants("ACM academy of computer makers".split(" "), stopWords) + "\t"
				+ calculateAcronymVariantsDP("ACM academy of computer makers".split(" "), stopWords));
		System.out.println(calculateAcronymVariants("RADAR radio detection and ranging".split(" "), stopWords) + "\t"
				+ calculateAcronymVariantsDP("RADAR radio detection and ranging".split(" "), stopWords));
		System.out.println(calculateAcronymVariants("RADAR radio deatection and arar".split(" "), stopWords) + "\t"
				+ calculateAcronymVariantsDP("RADAR radio deatection and arar".split(" "), stopWords));

		stopWords.clear();
		stopWords.add("a");
		stopWords.add("an");
		System.out.println(calculateAcronymVariants("APPLY an apple a day".split(" "), stopWords) + "\t"
				+ calculateAcronymVariantsDP("APPLY an apple a day".split(" "), stopWords));
	}

	// Bottom-up Dynamic Programming solution
	static int[][] dpAcronym = new int[151][151];
	static int calculateAcronymVariantsDP(String[] words, Set<String> stopWords) {

		String abbr = words[0].toLowerCase();
		int lastWordIdx = collapseStopWords(words, stopWords);
		int fullAbbrLength = abbr.length();

		int prev = 1;
		int curr = 0;
		for (int abbrLength = 1; abbrLength <= fullAbbrLength; ++abbrLength) {
			dpAcronym[curr][abbrLength] = calculateSubsequenceOccurence(abbr.substring(0, abbrLength), words[0]);
		}
		curr ^= 1;
		prev ^= 1;

		for (int wordIdx = 1; wordIdx <= lastWordIdx; ++wordIdx) {
			for (int abbrLength = 1; abbrLength <= fullAbbrLength; ++abbrLength) {
				dpAcronym[curr][abbrLength] = 0;
				for (int split = 1; split < abbrLength; ++split) {
					int subseqOccurence = calculateSubsequenceOccurence(abbr.substring(split, abbrLength), words[wordIdx]);
					dpAcronym[curr][abbrLength] += subseqOccurence * dpAcronym[prev][split];
				}
			}
			curr ^= 1;
			prev ^= 1;
		}

		return dpAcronym[prev][fullAbbrLength];
	}

	private static int collapseStopWords(String[] words, Set<String> stopWords) {
		int insertIdx = 0;
		// '0' position is acronym - so, we start from the 1st position
		int wordIdx = 1;
		while (wordIdx < words.length) {
			while ((wordIdx < words.length) && stopWords.contains(words[wordIdx])) {
				wordIdx++;
			}
			if (wordIdx != words.length) {
				words[insertIdx] = words[wordIdx];
				insertIdx++;
				wordIdx++;
			}
		}
		int lastWordIdx = insertIdx - 1;
		return lastWordIdx;
	}

	// Recursive solution driver
	static int calculateAcronymVariants(String[] words, Set<String> stopWords) {
		String abbr = words[0].toLowerCase();
		int lastWordIdx = collapseStopWords(words, stopWords);
		return calculateAcronymVariants(abbr, words, lastWordIdx, abbr.length());
	}

	// Recursive solution (with exponential complexity)
	static int calculateAcronymVariants(String abbr, String[] words, int wordIdx, int abbrLength) {
		if (wordIdx == 0) {
			return calculateSubsequenceOccurence(abbr.substring(0, abbrLength), words[wordIdx]);
		}

		int result = 0;

		int split = abbrLength - 1;
		int subseqOccurence = calculateSubsequenceOccurence(abbr.substring(split, abbrLength), words[wordIdx]);
		while ((subseqOccurence > 0) && (split > 0)) {
			result += subseqOccurence * calculateAcronymVariants(abbr, words, wordIdx - 1, split);
			split--;
			subseqOccurence = calculateSubsequenceOccurence(abbr.substring(split, abbrLength), words[wordIdx]);
		}

		return result;
	}

	//
	// if s1.charAt(idx1) != s2.charAt(idx2)
	// f(idx1, idx2) = f(idx1, idx2 + 1)
	//
	// if s1.charAt(idx1) == s2.charAt(idx2)
	// f(idx1, idx2) = f(idx1 + 1, idx2 + 1) + f(idx1, idx2 + 1)
	//
	static int[][] dpSubseq = new int[2][151];
	static int calculateSubsequenceOccurence(String subseq, String str) {
		int curr = 0;
		int prev = 1;

		dpSubseq[curr][0] = (subseq.charAt(0) == str.charAt(0)) ? 1 : 0;
		for (int i = 1; i < str.length(); i++) {
			dpSubseq[curr][i] = dpSubseq[curr][i - 1];
			if (subseq.charAt(0) == str.charAt(i)) {
				dpSubseq[curr][i] += 1;
			}
		}
		if (dpSubseq[curr][str.length() - 1] == 0) {
			return 0;
		}

		curr ^= 1;
		prev ^= 1;

		for (int j = 1; j < subseq.length(); j++) {
			dpSubseq[curr][0] = 0;
			for (int i = 1; i < str.length(); i++) {
				dpSubseq[curr][i] = dpSubseq[curr][i - 1];
				if (subseq.charAt(j) == str.charAt(i)) {
					dpSubseq[curr][i] += dpSubseq[prev][i - 1];
				}
			}
			if (dpSubseq[curr][str.length() - 1] == 0) {
				return 0;
			}
			curr ^= 1;
			prev ^= 1;
		}

		return dpSubseq[prev][str.length() - 1];
	}
}
