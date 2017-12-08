package spoj.dynamic_programming.mreplbrc;

// http://www.spoj.com/problems/MREPLBRC/

import java.util.Stack;
import java.util.function.Function;

public class MREPLBRC_Brute_Force {

	public static void testManyCases(Function<String, Long> f) {
		test("()()()", f);
		test("()()?(", f);
		test("()()?)", f);
		test("()()??", f);
		test("(?([?)]?}?", f);
		test("(??)", f);
		test("????", f);
		test("??(?", f);
		test("?](?", f);
		test("{](?", f);
		test("((?)???)()", f);
		test("((?)???)?)", f);
		test("[(?)???)?)??", f);
		test("([{??}])", f);
		test("(((((??)))))", f);
		test("(((?((??))???)", f);
		test("?((?((???]})???)", f);
		test("?((?((???]})???)??())?", f);
	}

	static void test(String s, Function<String, Long> f) {
		long bruteForceResult = solve(s);
		long fResult = f.apply(s);

		System.out.println(s.length());
		System.out.println(s);
		System.out.println(bruteForceResult);
		System.out.println(fResult);
		System.out.println();

		if (bruteForceResult != fResult) {
			throw new RuntimeException("Incorrect result");
		}
	}

	static long solve(String s) {
		return solve(s, 0, new Stack<>());
	}

	static long solve(String s, int i, Stack<Character> stack) {
		if (i == s.length()) {
			return stack.isEmpty() ? 1 : 0;
		}

		long result = 0;
		switch (s.charAt(i)) {
			case '(' :
				stack.push('(');
				result = solve(s, i + 1, stack);
				stack.pop();
				break;

			case ')' :
				if ((stack.size() > 0) && (stack.peek() == '(')) {
					stack.pop();
					result = solve(s, i + 1, stack);
					stack.push('(');
				}
				break;

			case '[' :
				stack.push('[');
				result = solve(s, i + 1, stack);
				stack.pop();
				break;

			case ']' :
				if ((stack.size() > 0) && (stack.peek() == '[')) {
					stack.pop();
					result = solve(s, i + 1, stack);
					stack.push('[');
				}
				break;

			case '{' :
				stack.push('{');
				result = solve(s, i + 1, stack);
				stack.pop();
				break;

			case '}' :
				if ((stack.size() > 0) && (stack.peek() == '{')) {
					stack.pop();
					result = solve(s, i + 1, stack);
					stack.push('{');
				}
				break;

			default :
				result = 0;

				stack.push('(');
				result += solve(s, i + 1, stack);
				stack.pop();

				stack.push('[');
				result += solve(s, i + 1, stack);
				stack.pop();

				stack.push('{');
				result += solve(s, i + 1, stack);
				stack.pop();

				if (stack.size() > 0) {
					char top = stack.pop();
					result += solve(s, i + 1, stack);
					stack.push(top);
				}
				break;
		}

		return result;
	}

}
