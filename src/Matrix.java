import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Matrix {
	protected int[][] CSR = new int[3][30];        // Three one dimensional arrays holding a CSR format of a matrix
	protected int numRows = 0;                     // Number of rows
	protected int numCols = 0;                     // Number of columns
	protected int loop = 0;                        // Number of filled elements in first array of CSR
	protected int loop2 = 0;                       // For second array
	protected int loop3 = 0;                       // For third array


	/**
	 * Function that reads a matrix of any size from a text file and turns it into a
	 * compressed sparse row. The matrix is read from a file called "matrix.txt"
	 *
	 * @param filename Filename where a matrix data will be read
	 * @return Returns the CSR matrix
	 * @throws IOException IO erros will be thrown back
	 */
	public int[][] matrix_to_CSR(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line = br.readLine();
		CSR[2][loop3++] = 0;

		while (line != null) {
			numRows++;
			int count = 0;
			String[] values = line.split(" ");
			numCols = values.length;
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
		return CSR;
	}

	/**
	 * Function will multiply a CSR matrix with a vector
	 *
	 * @param vector A vector that will be multiplied by this object
	 * @return Returns the results from the multiplication
	 * @throws MatrixException If N of Matrix A doesn't match M of Vector
	 */
	public int[] CSR_mult_vector(int[] vector) throws MatrixException {
		if (numCols != vector.length) {
			throw new MatrixException("Columns of Matrix A doesn't match rows of Vector's.");
		}

		int[] result = new int[vector.length];
		for (int i = 0; i < numRows; ++i) {
			for (int k = CSR[2][i]; k < CSR[2][i + 1]; k = k + 1) {
				result[i] = result[i] + CSR[0][k] * vector[CSR[1][k]];
			}
		}
		return result;
	}

	public Matrix CSR_to_transpose() throws IOException {
		Matrix transpose = new Matrix();
		int i, j, k, index;
		int m = numRows;
		int n = numCols;
		int[] count = new int[100];
		
		for (i = 0; i < n; i++) {
			for (j = CSR[2][i]; j < CSR[2][i + 1]; j++) {
				k = CSR[1][j];
				count[k]++;
			}
		}

		for (j = 0; j < m; j++) {
			transpose.CSR[2][j + 1] = transpose.CSR[2][j] + count[j];
		}

		for (j = 0; j < m; j++) {
			count[j] = 0;
		}

		for (i = 0; i < n; i++) {
			for (j = CSR[2][i]; j < CSR[2][i + 1]; j++) {
				k = CSR[1][j];
				index = transpose.CSR[2][k] + count[k];
				transpose.CSR[1][index] = i;
				transpose.CSR[0][index] = CSR[0][j];
				count[k]++;
			}
		}
		return transpose;
	}
}
