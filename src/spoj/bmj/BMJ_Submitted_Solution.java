package spoj.bmj;

import java.io.BufferedReader;
import java.io.InputStreamReader;

// http://www.spoj.com/problems/BMJ/

public class BMJ_Submitted_Solution {

	public static void main(String[] args) throws Exception {

		int x;
		int y;
		int stepIndex;
		int prevLevel;
		int currLevel;
		int delt;

		String s;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while ((s = br.readLine()) != null) {

			s = s.trim();
			stepIndex = Integer.parseInt(s);

			stepIndex--;
			prevLevel = (int) Math.floor((-4.0 + Math.sqrt(4 + (12 * stepIndex))) / 6);
			delt = stepIndex - ((3 * prevLevel * prevLevel) + (4 * prevLevel) + 1);

			currLevel = prevLevel + 1;

			if (delt == 0) {

				x = 0;
				y = currLevel;

			} else if (delt <= currLevel) {

				x = -delt;
				y = currLevel;

			} else if (delt <= (2 * currLevel)) {

				x = -currLevel;
				y = currLevel - (delt - currLevel);

			} else if (delt <= (3 * currLevel)) {

				x = -currLevel + (delt - (2 * currLevel));
				y = 0 - (delt - (2 * currLevel));

			} else if (delt <= (4 * currLevel)) {

				x = 0 + (delt - (3 * currLevel));
				y = -currLevel;

			} else if (delt <= ((5 * currLevel) + 1)) {

				x = currLevel;
				y = -currLevel + (delt - (4 * currLevel));

			} else {

				x = currLevel - (delt - ((5 * currLevel) + 1));
				y = 1 + (delt - ((5 * currLevel) + 1));
			}

			System.out.print(x);
			System.out.print(' ');
			System.out.println(y);
		}
	}
}
