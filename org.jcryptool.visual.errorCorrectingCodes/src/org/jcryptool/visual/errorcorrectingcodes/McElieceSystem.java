package org.jcryptool.visual.errorcorrectingcodes;

public class McElieceSystem {
    
    private int[][] g = {
            { 1, 0, 0, 0, 1, 1, 0 },
            { 0, 1, 0, 0, 1, 0, 1 },
            { 0, 0, 1, 0, 0, 1, 1 },
            { 0, 0, 0, 1, 1, 1, 1 }
    };

    private int[][] s = {
            { 1, 1, 0, 1 },
            { 1, 0, 0, 1 },
            { 0, 1, 1, 1 },
            { 1, 1, 0, 0 }
    };
    
    private int[][] p = {
            {0, 1, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 1, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 1}, 
            {1, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 1, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 1, 0}, 
            {0, 0, 0, 0, 1, 0, 0} 
    };
    
    public McElieceSystem() {
    }


    public int[][] getSGP() {
        int[][] sg = binaryMult(s, g);
        int[][] sgp = binaryMult(sg, p);
        return sgp;
    }
    
    private int[][] binaryMult(int[][] first, int[][] second) {
        
        if (first[0].length != second.length) {
            throw new IllegalArgumentException("Number of columns of first matrix must be equal to number of rows of second matrix!");
        }
        
        int[][] result = new int[first.length][second[0].length];

        for (int row = 0; row < result.length; row++) {
            for (int col = 0; col < result[row].length; col++) {
                for (int i = 0; i < second.length; i++) {
                    result[row][col] ^= (first[row][i] & second[i][col]);
                }
            }
        }
        return result;
    }
    

    public String printMatrix(int[][] m) {
        String s = "";
        for (int col = 0; col < m.length; col++) {
            for (int row = 0; row < m[col].length; row++) {
                s += m[col][row] + " ";
            }
            if (col < m.length-1)
                s += "\n";
        }
        return s;
    }

    public int[][] getG() {
        return g;
    }

    public void setG(int[][] g) {
        this.g = g;
    }

    public int[][] getS() {
        return s;
    }

    public void setS(int[][] s) {
        this.s = s;
    }

    public int[][] getP() {
        return p;
    }

    public void setP(int[][] p) {
        this.p = p;
    }

}
