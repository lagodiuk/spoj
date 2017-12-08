package spoj.dynamic_programming;

// http://www.spoj.com/problems/MPILOT/en/

public class MPILOT {

	public static void main(String[] args) {

		/**
		 * 5000 3000
		 * 6000 2000
		 * 8000 1000
		 * 9000 6000
		 *
		 * Expected output: 19000
		 */
		System.out.println(calculateMinSpendings_O_N_2(new Pilot[]{
				new Pilot(5000, 3000),
				new Pilot(6000, 2000),
				new Pilot(8000, 1000),
				new Pilot(9000, 6000)
		}));

		/**
		 * 10000 7000
		 * 9000 3000
		 * 6000 4000
		 * 5000 1000
		 * 9000 3000
		 * 8000 6000
		 *
		 * Expected output: 32000
		 */
		System.out.println(calculateMinSpendings_O_N_2(new Pilot[]{
				new Pilot(10000, 7000),
				new Pilot(9000, 3000),
				new Pilot(6000, 4000),
				new Pilot(5000, 1000),
				new Pilot(9000, 3000),
				new Pilot(8000, 6000)
		}));
	}

	static int calculateMinSpendings(Pilot[] pilots) {
		return calculateMinSpendings(pilots, pilots.length / 2, pilots.length / 2);
	}

	static final int INFINITY = 10000000;

	// TODO: add memoization
	static int calculateMinSpendings(
			Pilot[] pilots, // array of pilots, sorted by increase of age
			int captains, // vacant captain positions
			int helpers // vacant assistant positions
	) {

		if ((captains < helpers) || (captains < 0) || (helpers < 0)) {
			return INFINITY;
		}

		int pilotIndex = pilots.length - captains - helpers;

		if (pilotIndex == pilots.length) {
			return 0;
		}

		return Math.min(
				calculateMinSpendings(pilots, captains - 1, helpers) + pilots[pilotIndex].salaryCaptain,
				calculateMinSpendings(pilots, captains, helpers - 1) + pilots[pilotIndex].salaryHelper);
	}

	// Bottom-up solution with complexity O(N^2)
	static int calculateMinSpendings_O_N_2(Pilot[] pilots) {

		// Actually, instead of entire matrix we need only 2 rows
		int[][] spendings = new int[(pilots.length / 2) + 1][(pilots.length / 2) + 1];

		spendings[0][0] = 0;

		// captain - amount of vacant captain positions
		for (int captain = 1; captain <= (pilots.length / 2); captain++) {
			// helper - amount of vacant helper positions
			for (int helper = 0; helper <= captain; helper++) {

				int pilotIdx = pilots.length - captain - helper;

				int captainSubproblem = (helper <= (captain - 1)) ? spendings[captain - 1][helper] : INFINITY;
				int helperSubproblem = (helper > 0) ? spendings[captain][helper - 1] : INFINITY;

				spendings[captain][helper] = Math.min(
						captainSubproblem + pilots[pilotIdx].salaryCaptain,
						helperSubproblem + pilots[pilotIdx].salaryHelper);
			}
		}

		return spendings[pilots.length / 2][pilots.length / 2];
	}

	static class Pilot {
		int salaryCaptain;
		int salaryHelper;
		public Pilot(int salaryCaptain, int salaryHelper) {
			this.salaryCaptain = salaryCaptain;
			this.salaryHelper = salaryHelper;
		}
	}
}
