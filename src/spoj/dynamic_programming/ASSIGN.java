package spoj.dynamic_programming;

// http://www.spoj.com/problems/ASSIGN/

import java.util.BitSet;

public class ASSIGN {

	public static void main(String[] args) {

		System.out.println(getAssignmentsNumber(new int[][]{
				{1, 1, 1},
				{1, 1, 1},
				{1, 1, 1}
		}));

		System.out.println(getAssignmentsNumber(new int[][]{
				{1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1},
				{1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 0},
				{1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 0},
				{1, 0, 1, 1, 1, 0, 1, 1, 0, 1, 1},
				{0, 1, 1, 1, 0, 1, 0, 0, 1, 1, 1},
				{1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1},
				{1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1},
				{0, 0, 1, 0, 1, 1, 0, 0, 0, 1, 1},
				{1, 1, 1, 0, 0, 0, 1, 0, 1, 0, 1},
				{1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0}
		}));

		System.out.println(getAssignmentsNumber(new int[][]{
				{0, 1, 1, 1, 0, 1, 0, 0, 0, 1, 0},
				{0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1},
				{1, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0},
				{0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1},
				{1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1},
				{0, 0, 1, 0, 1, 1, 0, 0, 0, 0, 1},
				{1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 0},
				{1, 0, 1, 1, 0, 1, 1, 0, 0, 1, 0},
				{0, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1},
				{0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1},
				{0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 1}
		}));
	}

	static long getAssignmentsNumber(int[][] studentsPreferences) {
		int studentsCount = studentsPreferences.length;
		int tasksCount = studentsPreferences[0].length;

		BitSet[] studentsPreferencesBitSet = new BitSet[studentsCount];
		for (int s = 0; s < studentsCount; s++) {
			studentsPreferencesBitSet[s] = new BitSet(tasksCount);
			for (int t = 0; t < tasksCount; t++) {
				if (studentsPreferences[s][t] == 1) {
					studentsPreferencesBitSet[s].set(t, true);
				}
			}
		}

		BitSet unavailableTasksBitSet = new BitSet(tasksCount);

		return getAssignmentsNumber(studentsPreferencesBitSet, studentsCount - 1, unavailableTasksBitSet);
	}

	// Solution with exponential complexity
	// TODO: memoize[studentIndex][unavailableTasksBitMask]
	// max number of students: 20
	// max number of tasks: 20
	// max value unavailableTasksBitMask is 2^20 = 1'048'576
	static long getAssignmentsNumber(BitSet[] studentsPreferences, int studentIndex, BitSet unavailableTasks) {
		if (studentIndex < 0) {
			return 1;
		}

		long result = 0;
		BitSet currStudentPreferences = studentsPreferences[studentIndex];
		for (int i = 0; i < currStudentPreferences.size(); i++) {
			if (currStudentPreferences.get(i) && !unavailableTasks.get(i)) {
				unavailableTasks.set(i, true);
				result += getAssignmentsNumber(studentsPreferences, studentIndex - 1, unavailableTasks);
				unavailableTasks.set(i, false);
			}
		}

		return result;
	}
}
