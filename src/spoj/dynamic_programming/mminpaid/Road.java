package spoj.dynamic_programming.mminpaid;

public class Road {

	final int src;
	final int tgt;

	final int paymentCityInAdvance;

	final int costInAdvance;
	final int costInTgt;

	public Road(int src, int tgt, int paymentCityInAdvance, int costInAdvance, int costInTgt) {
		this.src = src;
		this.tgt = tgt;
		this.paymentCityInAdvance = paymentCityInAdvance;
		this.costInAdvance = costInAdvance;
		this.costInTgt = costInTgt;
	}

	@Override
	public String toString() {
		return "(s=" + this.src + ", t=" + this.tgt + ", a=" + this.paymentCityInAdvance + ", ca=" + this.costInAdvance
				+ ", ct="
				+ this.costInTgt + ")";
	}
}
