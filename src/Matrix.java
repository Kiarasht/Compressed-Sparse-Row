import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Matrix {
	private int[] val;                              // Non zero values
	private int[] col;                              // Column indexes
	private int[] ptr;                              // Row pointers
	private int numRows;                            // Number of rows
	private int numCols;                            // Number of columns

	/**
	 * Empty constructor. If you use this you will be responsible of initializing the values or you might
	 * get a null exception.
	 */
	public Matrix() {

	}

	/**
	 * Constructor to set up the values for the arrays and numRows, numCols. Call this constructor
	 * if you already have all the values at hand.
	 *
	 * @param val     Array holding the non zero values.
	 * @param col     Array holding the column indexes.
	 * @param ptr     Array holding the row pointer values.
	 * @param numRows Numbers of rows the CSR matrix has.
	 * @param numCols Number of columns the CSR matrix has.
	 */
	public Matrix(int[] val, int[] col, int[] ptr, int numRows, int numCols) {
		this.val = new int[val.length];
		this.col = new int[col.length];
		this.ptr = new int[ptr.length];
		System.arraycopy(val, 0, this.val, 0, val.length);
		System.arraycopy(col, 0, this.col, 0, col.length);
		System.arraycopy(ptr, 0, this.ptr, 0, ptr.length);
		this.numRows = numRows;
		this.numCols = numCols;
	}

	/**
	 * Copy constructor
	 *
	 * @param matrix A matrix object that will be copied over to a new Matrix class.
	 */
	public Matrix(Matrix matrix) {
		this.val = new int[matrix.val.length];
		this.col = new int[matrix.col.length];
		this.ptr = new int[matrix.ptr.length];
		System.arraycopy(val, 0, this.val, 0, matrix.val.length);
		System.arraycopy(col, 0, this.col, 0, matrix.col.length);
		System.arraycopy(ptr, 0, this.ptr, 0, matrix.ptr.length);
		this.numRows = matrix.numRows;
		this.numCols = matrix.numCols;
	}

	/**
	 * Function that reads a matrix of any size from a text file and turns it into a
	 * compressed sparse row. The matrix is read from a file called "matrix.txt" This
	 * should be called if user doesn't have arrays to pass using the constructor.
	 *
	 * @param filename Filename where a matrix data will be read
	 * @throws IOException IO errors will be thrown back
	 */
	public void matrix_to_CSR(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line = br.readLine();
		int loop = 0, loop2 = 0, loop3 = 0, numRows = 0, numCols = 0;
		int[][] CSR = new int[3][100];
		CSR[2][loop3++] = 0;

		while (line != null) {
			numRows++;
			String[] values = line.split(" ");
			numCols = values.length;
			int count = 0;
			for (int i = 0; i < values.length; i++) {
				String value = values[i];
				if (Integer.parseInt(value) != 0) {
					CSR[0][loop++] = Integer.parseInt(value);
					CSR[1][loop2++] = i;
					++count;
					if (loop % 100 == 0) {
						int[][] temp = new int[3][CSR[0].length + 100];
						for (int j = 0; j < CSR.length; ++j) {
							System.arraycopy(CSR[j], 0, temp[j], 0, CSR[j].length);
						}
						CSR = temp;
					}
				}
			}
			CSR[2][loop3++] = CSR[2][loop3 - 2] + count;
			line = br.readLine();
		}
		CSR[2][loop3] = CSR[2][loop3 - 1] + 1;
		int[] pointer = remove_duplicate(CSR[2]);

		this.numRows = numRows;
		this.numCols = numCols;
		val = new int[loop];
		col = new int[loop2];
		ptr = new int[pointer.length];
		System.arraycopy(CSR[0], 0, this.val, 0, loop);
		System.arraycopy(CSR[1], 0, this.col, 0, loop2);
		System.arraycopy(pointer, 0, this.ptr, 0, pointer.length);
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
			throw new MatrixException("Columns of Matrix A doesn't match rows of Vector's.\n" +
					numCols + " Vs. " + vector.length);
		}

		int[] result = new int[vector.length];
		for (int i = 0; i < numRows; ++i) {
			for (int k = ptr[i]; k < ptr[i + 1]; k++) {
				result[i] += val[k] * vector[col[k]];
			}
		}
		return result;
	}

	/**
	 * Function will multiply a CSR matrix with a vector and return a result as if the transpose of that
	 * matrix was multiplied.
	 *
	 * @param vector A vector that will be multiplied by this object
	 * @return Returns the results from the multiplication
	 * @throws MatrixException If N of Matrix A doesn't match M of Vector
	 */
	public int[] CSRT_mult_vector(int[] vector) throws MatrixException {
		if (numRows != vector.length) {
			throw new MatrixException("Rows of Matrix A_T doesn't match rows of Vector's.\n" +
					numRows + " + Vs. " + vector.length);
		}

		int[] result = new int[vector.length];
		for (int i = 0; i < numRows; ++i) {
			for (int k = ptr[i]; k < ptr[i + 1]; k++) {
				result[col[k]] += val[k] * vector[i];
			}
		}
		return result;
	}

	/**
	 * Function will transpose a compressed matrix directly.
	 *
	 * @return Returns a new matrix object which a transpose of the calling.
	 */
	public Matrix CSR_to_transpose() {
		Matrix transpose = new Matrix();
		int k, index;
		int[] count = new int[100];

		for (int i = 0; i < numCols; i++) {
			for (int j = ptr[i]; j < ptr[i + 1]; j++) {
				k = col[j];
				count[k]++;
			}
		}

		int hold = 0;
		for (int j = 0; j < numRows; j++) {
			transpose.ptr[j + 1] = transpose.ptr[j] + count[j];
			hold = j;
		}
		transpose.ptr[hold + 2] = transpose.ptr[hold + 1] + 1;

		for (int j = 0; j < numRows; j++) {
			count[j] = 0;
		}

		for (int i = 0; i < numCols; i++) {
			for (int j = ptr[i]; j < ptr[i + 1]; j++) {
				k = col[j];
				index = transpose.ptr[k] + count[k];
				transpose.col[index] = i;
				transpose.val[index] = val[j];
				count[k]++;
			}
		}
		transpose.numCols = numRows;
		transpose.numRows = numCols;
		return transpose;
	}

	/**
	 * Remove any duplicates from an array and remove any leading zeros after we are done. We need this for
	 * the row_ptr array to work as needed.
	 *
	 * @param input An array that needs to be cleaned, in this case for ptr
	 * @return A new array that should be treated as the new ptr array
	 */
	int[] remove_duplicate(int[] input) {
		if (input.length == 0 || input.length == 1) {
			return input;
		}

		int[] result = new int[input.length];
		int current = input[0];
		boolean found = false;

		for (int i = 0, j = 0; i < input.length; ++i) {
			int anInput = input[i];

			if (current == anInput && !found) {
				found = true;
			} else if (current != anInput) {
				result[j++] = current;
				current = anInput;
				found = false;
			}
		}

		int size = 1;
		for (int i = 1; i < result.length; ++i) {
			if (result[i] == 0) {
				break;
			}
			++size;
		}
		int[] results = new int[size];
		System.arraycopy(result, 0, results, 0, size);
		return results;
	}

	/**
	 * Getter function for the val array.
	 *
	 * @return The val array in this object.
	 */
	int[] getval() {
		return val;
	}

	/**
	 * Getter function for the col array.
	 *
	 * @return The col array in this object.
	 */
	int[] getcol() {
		return col;
	}

	/**
	 * Getter function for the ptr array.
	 *
	 * @return The ptr array in this object.
	 */
	int[] getptr() {
		return ptr;
	}

	/**
	 * Getter function for the numRows int.
	 *
	 * @return The numbRows int in this object.
	 */
	int getNumRows() {
		return numRows;
	}

	/**
	 * Getter function for the numCols int.
	 *
	 * @return The numbCols int in this object.
	 */
	int getNumCols() {
		return numCols;
	}
}
