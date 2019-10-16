package org.jcryptool.visual.errorcorrectingcodes;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jcryptool.visual.errorcorrectingcodes.data.CodeArray;
import org.jcryptool.visual.errorcorrectingcodes.data.EccData;
import org.jcryptool.visual.errorcorrectingcodes.data.Matrix2D;
import org.jcryptool.visual.errorcorrectingcodes.data.MatrixException;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class McElieceSystem {
    
    EccData data;
    private ArrayList<Integer> maskH;
    private Matrix2D matrixG;
    private Matrix2D matrixH;
    private Matrix2D matrixP;
    private Matrix2D matrixS;
    private Matrix2D matrixSInv;
    private Matrix2D matrixPInv;
    private Matrix2D matrixSGP;
    private CodeArray binary;
    private CodeArray error;
    private CodeArray encrypted;
    private CodeArray decrypted;
    private CodeArray decoded;
    
    public McElieceSystem(EccData data) {
        this.data = data;
        matrixG = new Matrix2D(new int[][] {
                { 1, 0, 0, 0, 1, 1, 1 },
                { 0, 1, 0, 0, 1, 1, 0 },
                { 0, 0, 1, 0, 0, 1, 1 },
                { 0, 0, 0, 1, 1, 0, 1 }
        });
        
        matrixH = new Matrix2D(new int[][] {
            { 1, 1, 0, 1, 1, 0, 0 },
            { 1, 1, 1, 0, 0, 1, 0 },
            { 1, 0, 1, 1, 0, 0, 1 }
        });     
        
        maskH = new ArrayList<Integer>();
        maskH.add(6);
        maskH.add(5);
        maskH.add(2);
        maskH.add(4);
        maskH.add(3);
        maskH.add(1);
        maskH.add(0);
    }
    
    public Matrix2D randomMatrix(int rows, int columns) {
        SecureRandom rand = new SecureRandom();
        int[][] data = new int[rows][columns];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    data[i][j] = rand.nextInt(2);
                }
            }        
        return new Matrix2D(data);
    }
    
    public Matrix2D randomPermutationMatrix(int size) {
       
        ArrayList<int[]> data = new ArrayList<int[]>();
        
        for (int i = 0; i < size; i++) {
            int[] arr = new int[size];
            arr[i] = 1;
            data.add(arr);
        }
        
        Collections.shuffle(data);
        int[][] temp = data.toArray(new int[size][size]);
        return new Matrix2D(temp);
    }


    public void computePublicKey() {
        Matrix2D sg = matrixS.multBinary(matrixG);
        matrixSGP = sg.multBinary(matrixP);
    }
    
    public void stringToArray() {
        String s = data.getOriginalString();
        if (s != null) {
            binary = new CodeArray(s.length() * 8);
           
          
            for (byte b : s.getBytes()) {
                int[] high = new int[4];
                int[] low = new int[4];
                for (int i = 0; i < 7; i++) {
                    if (i > 3)
                        high[i-4] = ((b >> i) & 1);
                    else
                        low[i] = ((b >> i) & 1);

                }
                binary.add(high);
                binary.add(low);
            }
        }
    }
        
    public void encrypt() {
        error = new CodeArray(binary.size());
        encrypted = new CodeArray(binary.size());

        for (int i = 0; i < binary.size(); i++) {
            List<Integer> temp = Arrays.asList(new Integer[] { 0, 0, 0, 0, 0, 0, 1 });
            Collections.shuffle(temp);
            int[] tempArray = temp.stream().mapToInt(j->j).toArray();
            error.add(tempArray);
            
            Matrix2D partial = new Matrix2D(new int[][] { binary.get(i) });
            Matrix2D encodedPart = partial.multBinary(matrixSGP);
            int[] result = binaryAdd(tempArray, encodedPart.getRow(0));
            encrypted.add(result);
        }
    }
    
    public void decrypt () {
        decoded = new CodeArray(encrypted.size());
        if (getEncrypted() == null) {
            throw new RuntimeException("Need to perform encryption first.");
        }
        
        encrypted.forEach(e ->{
             Matrix2D x = new Matrix2D(new int[][] { e });
             Matrix2D y = x.multBinary(matrixPInv);
             Matrix2D yT = y.getTranspose();
             Matrix2D syndrome = matrixH.multBinary(yT);
             int error=0;
             
             for (int i = 0; i < syndrome.getRowCount(); i++) {
                 error += ( syndrome.get(i, 0) * Math.pow(2, syndrome.getRowCount()-i-1) );
             }
                
             if (error != 0) {
                 y.flip(0, maskH.get(error-1));
             }
             
             Matrix2D c = new Matrix2D(new int[][] {Arrays.copyOfRange(y.getRow(0), 0, 4) });
             decoded.add(c.multBinary(matrixSInv).getRow(0));
        });
        
        data.setBinaryDecoded(decoded.toString());
        data.setDecodedString(toAscii(decoded));
    }

    private int[] binaryAdd(int[] value1, int[] value2) {
        
        if (value1.length < value2.length) {
            throw new IllegalArgumentException("First array needs to be of equal or greater size.");
        }

        int[] result = new int[value1.length];

        for (int i = 0; i < value1.length; i++) {
            result[i] = value1[i] ^ value2[i];
        }
        return result;
    }
    
    public String toAscii(CodeArray arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.size(); i+=2) {
          
            int[] high = arr.get(i);
            int[] low = arr.get(i+1);
            int ascii = 0;
            
            for (int j = 7; j >= 0; j--) {
                if (j > 3)
                    ascii += high[j-4] * (1 << j);
                else
                    ascii += low[j] * (1 << j);
            }
            sb.append((char) ascii);
        }
        
        return sb.toString();
    }
    
    
    public CodeArray getBinary() {
        return binary;
    }


    public void setVector(CodeArray arr) {
        this.binary = arr;
    }


    public EccData getData() {
        return data;
    }


    public void setData(EccData data) {
        this.data = data;
    }

    public CodeArray getError() {
        return error;
    }

    public void setError(CodeArray error) {
        this.error = error;
    }

    public CodeArray getEncrypted() {
        return encrypted;
    }

    public void setEncrypted(CodeArray encrypted) {
        this.encrypted = encrypted;
    }

    public CodeArray getDecrypted() {
        return decrypted;
    }

    public void setDecrypted(CodeArray decrypted) {
        this.decrypted = decrypted;
    }

    public CodeArray getDecoded() {
        return decoded;
    }

    public void setDecoded(CodeArray decoded) {
        this.decoded = decoded;
    }

    public void setBinary(CodeArray binary) {
        this.binary = binary;
    } 
    
    public Matrix2D getMatrixG() {
      return matrixG;
    }

    public void setMatrixG(Matrix2D matrixG) {
        this.matrixG = matrixG;
    }

    public Matrix2D getMatrixH() {
        return matrixH;
    }

    public void setMatrixH(Matrix2D matrixH) {
        this.matrixH = matrixH;
    }

    public Matrix2D getMatrixP() {
        return matrixP;
    }

    public void setMatrixP(Matrix2D matrixP) {
        this.matrixP = matrixP;
    }

    public Matrix2D getMatrixS() {
        return matrixS;
    }

    public void setMatrixS(Matrix2D matrixS) {
        this.matrixS = matrixS;
    }

    public Matrix2D getMatrixPInv() {
        return matrixPInv;
    }

    public void setMatrixPInv(Matrix2D matrixPInv) {
        this.matrixPInv = matrixPInv;
    }

    public Matrix2D getMatrixSInv() {
        return matrixSInv;
    }

    public void setMatrixSInv(Matrix2D matrixSInv) {
        this.matrixSInv = matrixSInv;
    }

    public Matrix2D getMatrixSGP() {
        return matrixSGP;
    }

    public void setMatrixSGP(Matrix2D matrixSGP) {
        this.matrixSGP = matrixSGP;
    }

    
}
