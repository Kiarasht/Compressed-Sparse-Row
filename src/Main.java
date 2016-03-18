import java.io.IOException;

public class Main {

	/**
	 * Java program that plays around with matrices and performs operations on them.
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
			//Matrix transpose = matrix.CSR_to_transpose();

			printarray(matrix.getval());
			printarray(matrix.getcol());
			printarray(matrix.getptr());
/*			System.out.println();
			printarray(transpose.CSR[0], matrix.loop, true);
			printarray(transpose.CSR[1], matrix.loop2, true);
			printarray(transpose.CSR[2], matrix.loop3, true);
			System.out.println();
			printarray(matrix.CSR_mult_vector(new int[]{1, 1, 1, 1, 1, 1, 1, 1}), matrix.numRows, true);
			printarray(matrix.CSRT_mult_vector(new int[]{1, 1, 1, 1, 1, 1, 1, 1}), transpose.numRows, true);
			System.out.println();
			printarray(transpose.CSR_mult_vector(new int[]{1, 1, 1, 1, 1, 1, 1, 1}), matrix.numRows, true);
			printarray(transpose.CSRT_mult_vector(new int[]{1, 1, 1, 1, 1, 1, 1, 1}), transpose.numRows, true);*/
		}/* catch (MatrixException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}*/ catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(2);
		}
	}

	/**
	 * printarray is responsible of either just printing the array values or pretty
	 * printing it by adding additional commas, brackets, etc...
	 *
	 * @param array       The array that needs to be printed.
	 */
	public static void printarray(int[] array) {
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for (int i = 0; i < array.length; ++i) {
			if (i < array.length - 1) {
				sb.append(array[i]).append(',').append(' ');
			} else {
				sb.append(array[i]);
			}
		}
		sb.append(']');
		System.out.println(sb.toString());

	}
}

