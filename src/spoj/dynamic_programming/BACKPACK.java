package spoj.dynamic_programming;

import java.util.Arrays;

public class BACKPACK {

	static int solve(Item[] items, int availableVolume) {
		int currIdx = 1;
		for (Item item : items) {
			if (item.index == 0) {
				item.index = currIdx;
			}
			currIdx++;
		}
		Arrays.sort(items, (i1, i2) -> Integer.compare(i1.index, i2.index));
		return solve(items, availableVolume, 0);
	}

	static int solve(Item[] items, int availableVolume, int currIdx) {
		if (currIdx == items.length) {
			return (availableVolume < 0) ? -1000000 : 0;
		}

		if (availableVolume < 0) {
			return -1000000;
		}

		int nextIdx = currIdx;
		while ((nextIdx < items.length) && (items[nextIdx].index == items[currIdx].index)) {
			nextIdx++;
		}

		return solve(items, availableVolume, nextIdx);
	}

	static class Item {
		int volume;
		int importance;
		int index;
		public Item(int volume, int importance, int index) {
			this.volume = volume;
			this.importance = importance;
			this.index = index;
		}
	}
}
