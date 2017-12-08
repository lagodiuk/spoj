package spoj.dynamic_programming;

// http://www.spoj.com/problems/PHIDIAS/

public class PHIDIAS {

	public static void main(String[] args) {
		System.out.println(getMinWastedArea(new Plate[]{
				new Plate(10, 4),
				new Plate(6, 2),
				new Plate(7, 5),
				new Plate(15, 10)
		}, 21, 11));
	}

	// TODO: add memoization
	static int getMinWastedArea(Plate[] plates, int width, int height) {

		int wasted = width * height;

		for (Plate p : plates) {

			if (wasted == 0) {
				break;
			}

			if ((p.width > width) || (p.height > height)) {
				continue;
			}

			if ((p.width == width) && (p.height == height)) {
				wasted = 0;
				break;
			}

			wasted = Math.min(wasted, Math.min(
					getMinWastedArea(plates, width - p.width, height) + getMinWastedArea(plates, p.width, height - p.height),
					getMinWastedArea(plates, width, height - p.height) + getMinWastedArea(plates, width - p.width, p.height)));
		}

		return wasted;
	}

	static class Plate {
		int width;
		int height;
		Plate(int width, int height) {
			this.width = width;
			this.height = height;
		}
	}
}
