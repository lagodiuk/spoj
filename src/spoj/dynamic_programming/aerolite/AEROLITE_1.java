package spoj.dynamic_programming.aerolite;

// http://www.spoj.com/problems/AEROLITE/

/**
 * This solution for some reason doesn't work!
 */
public class AEROLITE_1 {

	public static void main(String[] args) {
		System.out.println(Fc(0, 0, 4, 0));
		System.out.println(Fc(0, 0, 4, 1));
		System.out.println(Fc(0, 0, 4, 2));
		System.out.println(Fc(0, 0, 4, 3));
		System.out.println(Fc(0, 0, 4, 4));
		System.out.println(Fc(0, 0, 4, 5));

		System.out.println();
		System.out.println(Fc(0, 0, 6, 3));
	}

	static int Fc(int Nc, int Nb, int Na, int D) {
		if (Nc == 0) {
			return Fb(Nb, Na, D);
		}

		int result = Fc(Nc - 1, Nb, Na, D - 1);

		for (int Nc1 = 0; Nc1 <= Nc; Nc1++) {
			for (int D1 = 0; D1 <= D; D1++) {
				result += Fc(Nc1, Nb, Na, D1) * Fc(Nc - Nc1, Nb, Na, D);
			}
		}
		return result;
	}

	static int Fb(int Nb, int Na, int D) {
		if (Nb == 0) {
			return Fa(Na, D, true);
		}

		int result = Fb(Nb - 1, Na, D - 1);

		for (int Nb1 = 0; Nb1 <= Nb; Nb1++) {
			for (int D1 = 0; D1 <= D; D1++) {
				result += Fb(Nb1, Na, D1) * Fb(Nb - Nb1, Na, D);
			}
		}
		return result;
	}

	static int Fa(int Na, int D, boolean canBeSplitted) {

		if ((Na > 0) && (D == 1)) {
			return 1;
		}

		if ((Na <= 0) || (Na < D)) {
			return 0;
		}

		int result = Fa(Na - 1, D - 1, true);

		if (canBeSplitted) {
			for (int Na1 = 1; Na1 < Na; Na1++) {
				for (int D1 = 1; D1 < D; D1++) {

					int L = Fa(Na1, D1, false);
					int R = Fa(Na - Na1, D, true);
					result += L * R;

					int L1 = Fa(Na1, D, false);
					int R1 = Fa(Na - Na1, D1, true);
					result += L1 * R1;
				}

				int L1 = Fa(Na1, D, false);
				int R1 = Fa(Na - Na1, D, true);
				result += L1 * R1;
			}
		}

		return result;
	}
}
