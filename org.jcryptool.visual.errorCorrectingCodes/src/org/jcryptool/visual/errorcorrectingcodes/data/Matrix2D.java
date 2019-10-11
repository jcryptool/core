package org.jcryptool.visual.errorcorrectingcodes.data;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.security.auth.login.AccountExpiredException;

import com.sun.xml.internal.ws.spi.db.DatabindingException;

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

    public Matrix2D multBinary(Matrix2D other) {

        if (this.getColCount() != other.getRowCount()) {
            throw new IllegalArgumentException(
                    "Number of columns of first matrix must be equal to number of rows of second matrix.");
        }

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

    public Matrix2D invert() {
        if (!isSquare())
            throw new RuntimeException("Only square matrices can be inverted!");

        int n = data.length;
        int i, j, k, pivot;
        
        int[] swap;
        int[] P = new int[n];
        int[][] LU = Arrays.stream(data)
                .map((int[] row) -> row.clone())
                .toArray((int length) -> new int[length][]);
        
        int[][] IA = new int[n][n];
              
        for (i = 0; i < n; i++)
            P[i] = i; //Unit permutation "matrix"

        //pivoting and LU Decomposition
        for (i = 0; i < n; i++) {
            pivot = -1;

            for (k = i; k < n; k++)
                if (LU[k][i] == 1) { 
                    pivot = k;
                    break;
                }
            

            if (pivot == -1 ) 
                throw new MatrixException("Matrix is singular, no inverse could be found!");

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

            for (j = i + 1; j < n; j++) {
                LU[j][i] &= LU[i][i];

                for (k = i + 1; k < n; k++)
                    LU[j][k] ^= (LU[j][i] & LU[i][k]);
            }
        }
        
        
        //compute the inverse by solving LUX = IA
        for (j = 0; j < n; j++) {
            for (i = 0; i < n; i++) {
                if (P[i] == j) 
                    IA[i][j] = 1;
                else
                    IA[i][j] = 0;

                for (k = 0; k < i; k++)
                    IA[i][j] ^= (LU[i][k] & IA[k][j]);
            }

            for (i = n - 1; i >= 0; i--) {
                for (k = i + 1; k < n; k++)
                    IA[i][j] ^= (LU[i][k] & IA[k][j]);

                IA[i][j] = IA[i][j] & LU[i][i];
            }
        }
               
        return new Matrix2D(IA);
    }

    public boolean isSquare() {
        if (getColCount() == getRowCount())
            return true;
        else
            return false;
    }

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
