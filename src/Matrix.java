import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Matrix {
	private static int[][] CSR = new int[3][30];        // Three one dimensional arrays holding a CSR format of a matrix
	private static int[] nxm = new int[2];              // N and M of the matrix
	private static int loop = 0;                        // Number of filled elements in first array of CSR
	private static int loop2 = 0;                       // for second array
	private static int loop3 = 0;                       // for third array


	/**
	 * Function that reads a matrix of any size from a text file and turns it into a
	 * compressed sparse row. The matrix is read from a file called "matrix.txt"
	 */
	public void matrix_to_CSR(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line = br.readLine();
		CSR[2][loop3++] = 0;

		while (line != null) {
			++nxm[0];
			int count = 0;
			String[] values = line.split(" ");
			nxm[1] = values.length;
			for (int i = 0; i < values.length; i++) {
				String value = values[i];
				if (Integer.parseInt(value) != 0) {
					CSR[0][loop++] = Integer.parseInt(value);
					CSR[1][loop2++] = i;
					++count;
				}
			}
			CSR[2][loop3++] = CSR[2][loop3 - 2] + count;
			line = br.readLine();
		}
		printarray(CSR[0], loop, true);
		printarray(CSR[1], loop2, true);
		printarray(CSR[2], loop3, true);
	}

	/**
	 * Function will multiply a CSR matrix with a vector.
	 */
	public void CSR_mult_vector() throws MatrixException {
		int[] result = new int[8];
		int[] vector = {1, 4, 4, 2, 0, 2, 3, 5};

		if (nxm[1] != result.length) {
			throw new MatrixException("Columns of Matrix A doesn't match height of Vertor's.");
		}

		for (int i = 0; i < nxm[0]; ++i) {
			for (int k = CSR[2][i]; k < CSR[2][i + 1]; k = k + 1) {
				result[i] = result[i] + CSR[0][k] * vector[CSR[1][k]];
			}
		}
		printarray(result, nxm[0], true);
	}

	/**
	 * printarray is responsible of either just printing the array values or pretty
	 * printing it by adding additional commas, brackets, etc...
	 *
	 * @param array       The array that needs to be printed.
	 * @param length      The length of the array. Excluding the trailing zeros.
	 * @param prettyprint Simple print or pretty print?
	 */
	public void printarray(int[] array, int length, boolean prettyprint) {
		if (!prettyprint) {
			for (int i = 0; i < length; ++i) {
				System.out.print(array[i] + " ");
			}
			System.out.println();
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append('[');
			for (int i = 0; i < length; ++i) {
				if (i < length - 1) {
					sb.append(array[i]).append(',').append(' ');
				} else {
					sb.append(array[i]);
				}
			}
			sb.append(']');
			System.out.println(sb.toString());
		}
	}
}
