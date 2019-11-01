/*
 * @author Daniel Hofmann
 */
package org.jcryptool.visual.errorcorrectingcodes.algorithm;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jcryptool.visual.errorcorrectingcodes.data.BitArray;
import org.jcryptool.visual.errorcorrectingcodes.data.EccData;
import org.jcryptool.visual.errorcorrectingcodes.data.Matrix2D;
import org.jcryptool.visual.errorcorrectingcodes.data.MatrixException;
import org.jcryptool.visual.errorcorrectingcodes.data.HammingData;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

// TODO: Auto-generated Javadoc
/**
 * Implementation of a simplified "McEliece Cryptosystem" with Hamming(7,4) instead of Goppa code.
 * 
 * @author dhofmann
 *
 */
public class HammingCrypto {
    
    /** The data model. */
    HammingData data;

  
    /**
     * Instantiates a new HammingCrypto object.
     *
     * @param data containing algorithm specific and additional view data
     */
    public HammingCrypto(HammingData data) {
        this.data = data;
        this.data.setMatrixG(new Matrix2D(new int[][] {
                { 1, 0, 0, 0, 1, 1, 1 },
                { 0, 1, 0, 0, 1, 1, 0 },
                { 0, 0, 1, 0, 0, 1, 1 },
                { 0, 0, 0, 1, 1, 0, 1 }
        }));
        
        this.data.setMatrixH(new Matrix2D(new int[][] {
            { 1, 1, 0, 1, 1, 0, 0 },
            { 1, 1, 1, 0, 0, 1, 0 },
            { 1, 0, 1, 1, 0, 0, 1 }
        }));     
        
        ArrayList<Integer> maskH = new ArrayList<Integer>();
        maskH.add(6);
        maskH.add(5);
        maskH.add(2);
        maskH.add(4);
        maskH.add(3);
        maskH.add(1);
        maskH.add(0);
        this.data.setMaskH(maskH);
    }
    
    /**
     * Store a random binary matrix to {@link HammingData#matrixS S}.
     *
     * @param rows the rows
     * @param columns the columns
     */
    public void randomMatrix(int rows, int columns) {
        SecureRandom rand = new SecureRandom();
        rand.nextBytes(new byte[20]);
        
        Matrix2D s, sInvert;
        do {
            int[][] arr = new int[rows][columns];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    arr[i][j] = rand.nextInt(2);
                }
            }  
            s = new Matrix2D(arr);
            
            sInvert = s.invert();
        } while (sInvert == null || s.equals(sInvert));
              
        data.setMatrixS(s);
        data.setMatrixSInv(sInvert);
    }
    
    /**
     * Store a Random permutation matrix {@link HammingData#matrixP P}, i.e. every row and column contains only a single 1.
     *
     * @param size the size of the square matrix
     */
    public void randomPermutationMatrix(int size) {
        Matrix2D p, pInverse;
        do {
            ArrayList<int[]> bitArray = new ArrayList<int[]>(size);

            for (int i = 0; i < size; i++) {
                int[] arr = new int[size];
                arr[i] = 1;
                bitArray.add(arr);
            }

            Collections.shuffle(bitArray);
            int[][] temp = bitArray.toArray(new int[size][size]);
            p = new Matrix2D(temp);
            pInverse = p.invert();
        } while (p.equals(pInverse));

        data.setMatrixP(p);
        data.setMatrixPInv(pInverse);
    }


    /**
     * Compute the public key and store it in matrix {@link HammingData#matrixSGP SGP}.
     */
    public void computePublicKey() {
        Matrix2D sg = data.getMatrixS().multBinary(data.getMatrixG());
        data.setMatrixSGP(sg.multBinary(data.getMatrixP()));
    }
    
   
    /*
     * Converts a text string to a list of arrays, containing the ASCII encoding as integers of 1 and 0.
     * The data needs to be split into groups of 4 to apply Hamming code with 4 data bits and 3 parity bits.
     */
    public void stringToArray() {
        String s = data.getOriginalString();
        if (s != null) {
            BitArray binary = new BitArray(s.length() * 8);
           
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
            
            data.setBinary(binary);
        }
    }
    
  
    /*
     * Encrypt the Hamming encoded data by adding a single bit error to each segment and multiplying it with the public Key (SGP).
     * The result is stored in the field {@link McElieceData#encrypted encrypted}. 
     */
    public void encrypt() {
        int n = data.getBinary().size();
        BitArray error = new BitArray(n);
        BitArray encrypted = new BitArray(n);

        for (int i = 0; i < n; i++) {
            List<Integer> temp = Arrays.asList(new Integer[] { 0, 0, 0, 0, 0, 0, 1 });
            Collections.shuffle(temp);
            int[] tempArray = temp.stream().mapToInt(j->j).toArray();
            error.add(tempArray);
            
            Matrix2D partial = new Matrix2D(new int[][] {data.getBinary().get(i) });
            Matrix2D encodedPart = partial.multBinary(data.getMatrixSGP());
            int[] result = binaryAdd(tempArray, encodedPart.getRow(0));
            encrypted.add(result);
        }
        
        data.setError(error);
        data.setEncrypted(encrypted);
    }
    
    /**
     * Decryption of the data in the field {@link data.encrypted} by correcting the error in the bit stream and multiplying it with the inverse of the matrices of the private key.
     * The result is stored in the according fields in {@link HammingData}.
     */
    public void decrypt () {
        BitArray decrypted = new BitArray(data.getEncrypted().size());
        if (data.getEncrypted() == null) {
            throw new RuntimeException("Need to perform encryption first.");
        }
        
        data.getEncrypted().forEach(e ->{
             Matrix2D x = new Matrix2D(new int[][] { e });
             Matrix2D y = x.multBinary(data.getMatrixPInv());
             Matrix2D yT = y.getTranspose();
             Matrix2D syndrome = data.getMatrixH().multBinary(yT);
             int error=0;
             
             for (int i = 0; i < syndrome.getRowCount(); i++) {
                 error += ( syndrome.get(i, 0) * Math.pow(2, syndrome.getRowCount()-i-1) );
             }
                
             if (error != 0) {
                 y.flip(0, data.getMaskH().get(error-1));
             }
             
             Matrix2D c = new Matrix2D(new int[][] {Arrays.copyOfRange(y.getRow(0), 0, 4) });
             decrypted.add(c.multBinary(data.getMatrixSInv()).getRow(0));
        });
        
        data.setDecrypted(decrypted);
        data.setBinaryDecoded(decrypted.toString());
        data.setDecodedString(decrypted.toAscii());
    }

    /**
     * Binary addition (without carry) of 2 arrays containing integers of 1 and 0.
     *
     * @param first the value 1
     * @param second the value 2
     * @return the int[]
     */
    private int[] binaryAdd(int[] first, int[] second) {
        
        
        
        if (first.length < second.length) {
            throw new IllegalArgumentException("First array needs to be of equal or greater size.");
        }

        int[] result = new int[first.length];

        for (int i = 0; i < first.length; i++) {
            result[i] = first[i] ^ second[i];
        }
        return result;
    }
  
    /**
     * Gets the data.
     *
     * @return the data
     */
    public HammingData getData() {
        return data;
    }


    /**
     * Sets the data.
     *
     * @param data the new data
     */
    public void setData(HammingData data) {
        this.data = data;
    }
}
