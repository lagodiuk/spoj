package spoj.dynamic_programming.mnered;

// http://www.spoj.com/problems/MNERED/

import java.util.Arrays;
import java.util.List;

public class MNERED {

	public static void main(String[] args) {

		// example from description of the problem
		// expected result is 2
		solve(4, Arrays.asList(
				new Coordinate(2, 2),
				new Coordinate(4, 4),
				new Coordinate(1, 1)));

		// example from description of the problem
		// expected result is 3
		solve(5, Arrays.asList(
				new Coordinate(2, 2),
				new Coordinate(3, 2),
				new Coordinate(4, 2),
				new Coordinate(2, 4),
				new Coordinate(3, 4),
				new Coordinate(4, 4),
				new Coordinate(2, 3),
				new Coordinate(2, 3)));

		// 9 entities
		// 2 entities - near each other
		// 7 entities on the top of one entity
		// expected result is 7
		solve(10, Arrays.asList(
				new Coordinate(2, 2),
				new Coordinate(2, 2),
				new Coordinate(2, 2),
				new Coordinate(2, 2),
				new Coordinate(2, 2),
				new Coordinate(2, 2),
				new Coordinate(2, 2),
				new Coordinate(2, 2),
				new Coordinate(3, 3)));

		// entities are already aligned into rectangle
		// expected result is 0
		solve(10, Arrays.asList(
				new Coordinate(2, 2),
				new Coordinate(2, 3),
				new Coordinate(2, 4),
				new Coordinate(3, 2),
				new Coordinate(3, 3),
				new Coordinate(3, 4),
				new Coordinate(4, 2),
				new Coordinate(4, 3),
				new Coordinate(4, 4)));
	}

	static void solve(int dimension, List<Coordinate> entitiesCoordinates) {

		// Put entities onto the field
		int[][] field = new int[dimension + 1][dimension + 1];
		for (Coordinate c : entitiesCoordinates) {
			field[c.y][c.x] = 1;
		}

		// Calculate 2D cumulative matrix
		for (int x = 1; x < field.length; x++) {
			for (int y = 0; y < field.length; y++) {
				field[y][x] += field[y][x - 1];
			}
		}
		for (int y = 1; y < field.length; y++) {
			for (int x = 0; x < field.length; x++) {
				field[y][x] += field[y - 1][x];
			}
		}

		// Check all rectangles
		int maxAmountOfNonEmptyCells = -1;
		int amountOfEntitites = entitiesCoordinates.size();
		int w = 1;
		while ((w * w) <= (amountOfEntitites + 1)) {
			if ((amountOfEntitites % w) == 0) {
				int h = amountOfEntitites / w;

				if ((w <= dimension) && (h <= dimension)) {

					// Horizontal sliding window
					for (int y = h; y < field.length; y++) {
						for (int x = w; x < field.length; x++) {
							int nonEmptyCells = (field[y][x]
									- field[y - h][x]
									- field[y][x - w])
									+ field[y - h][x - w];
							maxAmountOfNonEmptyCells = Math.max(maxAmountOfNonEmptyCells, nonEmptyCells);
						}
					}

					// Temporarily swap H and W
					int tmp = w;
					w = h;
					h = tmp;

					// Vertical sliding window
					for (int y = h; y < field.length; y++) {
						for (int x = w; x < field.length; x++) {
							int nonEmptyCells = (field[y][x]
									- field[y - h][x]
									- field[y][x - w])
									+ field[y - h][x - w];
							maxAmountOfNonEmptyCells = Math.max(maxAmountOfNonEmptyCells, nonEmptyCells);
						}
					}

					// Restore original values of H and W
					tmp = w;
					w = h;
					h = tmp;
				}
			}
			w++;
		}

		int result = amountOfEntitites - maxAmountOfNonEmptyCells;
		System.out.println(result);
	}

	/**
	 * x and y starts from 1 (not from 0)
	 */
	static class Coordinate {
		int x;
		int y;
		public Coordinate(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
}
