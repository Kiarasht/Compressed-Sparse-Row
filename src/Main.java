import java.io.IOException;

public class Main {

	/**
	 * Java program that plays around with matrices and perform operations on them.
	 * The matrices are in compressed sparse row.
	 *
	 * @param args Extra arguments. We don't need them here.
	 */
	public static void main(String[] args) throws InterruptedException {
		if (args.length != 0) {
			System.exit(0);
		}

		try {
			Matrix matrix = new Matrix();
			matrix.matrix_to_CSR("matrix.txt");
			matrix.CSR_mult_vector();
		} catch (MatrixException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(2);
		}
	}
}

