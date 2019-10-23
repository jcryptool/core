package org.jcryptool.visual.errorcorrectingcodes.data;

import java.util.Arrays;

/**
 * Matrix2D holds a 2-Dimensional array of integer values and provides methods for computations in the binary field.   
 * 
 * @author dhofmann
 *
 */
public class Matrix2D {
    int[][] data;

    public Matrix2D() {
    }

    public Matrix2D(int rows, int columns) {
        this.setData(new int[rows][columns]);
    }

    public Matrix2D(int[][] data) {
        this.setData(data);
    }
    
    /**
     * Multiply two binary matrices.
     * 
     * @param other the matrix multiplicand
     * @return the product
     * @throws IllegalArgumentException if the number of rows and columns of the two multiplicands do not match 
     * @throws IllegalArgumentException when one or both matrices contain non-binary values 
     */
    public Matrix2D multBinary(Matrix2D other) {

        if (this.getColCount() != other.getRowCount())
            throw new IllegalArgumentException("Number of columns of first matrix must be equal to number of rows of second matrix.");
        
        if (!this.isBinary() || !other.isBinary())
            throw new IllegalArgumentException("Matrices must contain only binary values.");

        int[][] result = new int[getRowCount()][other.getColCount()];

        for (int row = 0; row < result.length; row++) {
            for (int col = 0; col < result[row].length; col++) {
                for (int i = 0; i < other.getRowCount(); i++) {
                    result[row][col] ^= (this.get(row, i) & other.get(i, col));
                }
            }
        }
        return new Matrix2D(result);
    }
    
    /**
     * XOR the matrix value at (row, col) with 1, i.e. flip a binary value.
     * 
     * @param row
     * @param col
     */
    public void flip(int row, int col) {
        data[row][col] ^= 1;
    }

    public Matrix2D getTranspose() {
        int[][] transpose = new int[getColCount()][getRowCount()];
        for (int row = 0; row < data.length; row++) {
            for (int col = 0; col < data[row].length; col++) {
                transpose[col][row] = data[row][col];
            }
        }

        return new Matrix2D(transpose);
    }
    
    /**
     * Compute inverse of this matrix via lower–upper (LU) decomposition algorithm. 
     * 
     * @return inverted Matrix2D object; null if matrix not invertible
     */
    public Matrix2D invert() {
        if (!isSquare())
            return null;

        int n = data.length;
        int i, j, k, pivot; 
        int[] swap;
        
        //the permutation reference as an 1D array
        int[] P = new int[n];

        //copy the rows to a new array to keep the original data
        int[][] LU = Arrays.stream(data)
                .map((int[] row) -> row.clone())
                .toArray((int length) -> new int[length][]);
                
        for (i = 0; i < n; i++)
            P[i] = i;

        //pivoting and LU Decomposition
        for (i = 0; i < n; i++) {
            pivot = -1;

            for (k = i; k < n; k++)
                if (LU[k][i] == 1) { 
                    pivot = k;
                    break;
                }
            
            if (pivot == -1 ) 
                return null;
            
            if (pivot != i) {
                //pivoting P
                j = P[i];
                P[i] = P[pivot];
                P[pivot] = j;

                //pivoting rows of A
                swap = LU[i];
                LU[i] = LU[pivot];
                LU[pivot] = swap;
            }

            //calculate lower and upper part
            for (j = i + 1; j < n; j++) {
                LU[j][i] &= LU[i][i];

                for (k = i + 1; k < n; k++)
                    LU[j][k] ^= (LU[j][i] & LU[i][k]);
            }
        }  

        //compute the inverse by solving LUX = IA
        int[][] IA = new int[n][n];
        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++) {
                //check if the row was pivoted
                if (P[j] == i) 
                    IA[j][i] = 1;
                else
                    IA[j][i] = 0;

                for (k = 0; k < j; k++)
                    IA[j][i] ^= (LU[j][k] & IA[k][i]);
            }

            for (j = n - 1; j >= 0; j--) {
                for (k = j + 1; k < n; k++)
                    IA[j][i] ^= (LU[j][k] & IA[k][i]);

                IA[j][i] = IA[j][i] & LU[j][j];
            }
        }       
        return new Matrix2D(IA);
    }

    /**
     * Check for squareness of this matrix.
     * 
     * @return true if the number of rows and columns are equal 
     */
    public boolean isSquare() {
        if (getColCount() == getRowCount())
            return true;
        else
            return false;
    }
    
    /**
     * Check if all matrix values are binary.
     * 
     * @return false if any value is not 1 or 0, true otherwise
     */
    public boolean isBinary() {
        for (int row = 0; row < data.length; row++) {
            for (int col = 0; col < data[row].length; col++) {
                if (data[row][col] != 0 && data[row][col] != 1)
                    return false;
            }
        }
        return true;
    }

    public int get(int row, int col) {
        return data[row][col];
    }

    public void set(int row, int col, int val) {
        data[row][col] = val;
    }

    public int[][] getData() {
        return data;
    }

    public void setData(int[][] data) {
        if (data.length != 0 && data[0].length != 0 && (data.length > 1 || data[0].length > 1))
            this.data = data;
        else
            throw new IllegalArgumentException("Not a matrix.");
    }

    public int[] getRow(int idxRow) {
        return data[idxRow];
    }

    /**
     * Get a column of the row oriented data array.
     * 
     * @param idxColumn the column id
     * @return a new array containing the column values
     */
    public int[] getColumn(int idxColumn) {
        int[] column = new int[getRowCount()];
        for (int i = 0; i < data.length; i++) {
            column[i] = data[i][idxColumn];
        }

        return column;
    }

    public int getRowCount() {
        return data.length;
    }

    public int getColCount() {
        return data[0].length;
    }
    
    @Override
    public boolean equals(Object other) {
        if(! (other instanceof Matrix2D))
            return false;
        
        int[][] otherData = ((Matrix2D) other).getData();
        
        for (int i = 0; i < otherData.length; i++) {
            for (int j = 0; j < otherData[i].length; j++) {
                if (data[i][j] != otherData[i][j])
                    return false;
            }
        }
        
        return true;
    }
    
    /**
     * Return a string representation of the Matrix.
     * 
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < data.length; row++) {
            for (int col = 0; col < data[row].length; col++) {
                sb.append(data[row][col]).append(" ");
            }
            if (row < data.length - 1)
                sb.append("\n");
        }
        return sb.toString();
    }
}
