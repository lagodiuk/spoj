package spoj.dynamic_programming.vocv;

public class Result {

	int minLightsCount;
	int variantsCount;

	public Result(int minLightsCount, int variantsCount) {
		this.minLightsCount = minLightsCount;
		this.variantsCount = variantsCount;
	}

	@Override
	public String toString() {
		return "lights: " + this.minLightsCount + ", variants: " + this.variantsCount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + this.minLightsCount;
		result = (prime * result) + this.variantsCount;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		Result other = (Result) obj;
		if (this.minLightsCount != other.minLightsCount) {
			return false;
		}
		if (this.variantsCount != other.variantsCount) {
			return false;
		}
		return true;
	}
}
