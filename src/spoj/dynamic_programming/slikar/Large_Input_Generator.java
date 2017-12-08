package spoj.dynamic_programming.slikar;

public class Large_Input_Generator {

	public static void main(String[] args) {
		int size = 512; // should be a power of 2
		System.out.println(size);
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				System.out.print(Math.random() < 0.5 ? "0" : "1");
			}
			System.out.println();
		}
	}
}
