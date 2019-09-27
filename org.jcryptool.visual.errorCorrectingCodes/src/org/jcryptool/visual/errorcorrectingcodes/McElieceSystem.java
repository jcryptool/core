package org.jcryptool.visual.errorcorrectingcodes;

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

public class McElieceSystem {
    
    EccData data;
    private CodeArray binary;
    private CodeArray error;
    private CodeArray encrypted;
    private CodeArray decrypted;
    private CodeArray decoded;
    
    public McElieceSystem(EccData data) {
        this.data = data;
        data.setMatrixG(new Matrix2D(new int[][] {
                { 1, 0, 0, 0, 1, 1, 0 },
                { 0, 1, 0, 0, 1, 0, 1 },
                { 0, 0, 1, 0, 0, 1, 1 },
                { 0, 0, 0, 1, 1, 1, 1 }
        }));

        
        
        
    }

  //TODO implement matrix echolon form computation
    public void fillPrivateKey() {
        data.setMatrixS( new Matrix2D(new int[][] {
            { 1, 1, 0, 1 },
            { 1, 0, 0, 1 },
            { 0, 1, 1, 1 },
            { 1, 1, 0, 0 }
        }));        
        data.setMatrixP(new Matrix2D(new int[][] {
            {0, 1, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 1, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 1}, 
            {1, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 1, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 1, 0}, 
            {0, 0, 0, 0, 1, 0, 0} 
        }));
        
        data.setMatrixSInv(new Matrix2D(new int[][] {
            { 1, 1, 0, 1 },
            { 1, 1, 0, 0 },
            { 0, 1, 1, 1 },
            { 1, 0, 0, 1 }
        }));
        
        data.setMatrixPInv(new Matrix2D(new int[][] {
                {0,  0,  0,  1,  0,  0,  0},
                {1,  0,  0,  0,  0,  0,  0},
                {0,  0,  0,  0,  1,  0,  0},
                {0,  1,  0,  0,  0,  0,  0},
                {0,  0,  0,  0,  0,  0,  1},
                {0,  0,  0,  0,  0,  1,  0},
                {0,  0,  1,  0,  0,  0,  0}
        }));
    }

    public Matrix2D getSGP() {
        Matrix2D sg = data.getMatrixS().multBinary(data.getMatrixG());
        Matrix2D sgp = sg.multBinary(data.getMatrixP());
        data.setMatrixSGP(sgp);
        return sgp;
    }
    
    public void stringToArray() {
        String s = data.getOriginalString();
        if (s != null) {
            binary = new CodeArray(s.length() * 8);

            for (byte b : s.getBytes()) {
                
                int[] temp = new int[8];
                for (int i = 7; i >= 8; i--) {
                    // set the i-th position to the corresponding bit by shifting the byte to the right
                    temp[i] = ((b >> i) & 0x01);
                }
                binary.add(temp);
            }
        }
    }
        
    public void encrypt() {
        error = new CodeArray(binary.size());
        encrypted = new CodeArray(binary.size());

        for (int i = 0; i < binary.size(); i++) {
            int[] temp = new int[] { 0, 0, 0, 0, 0, 0, 1 };
            Collections.shuffle(Arrays.asList(temp));
            error.add(temp);
            Matrix2D partial = new Matrix2D(new int[][] { temp });
            Matrix2D encryptedPart = partial.multBinary(data.getMatrixSGP());
            int[] result = binaryAdd(error.get(i), encryptedPart.getRow(0));
            encrypted.add(result);
        }
    }
    
    public void decrypt () {
        if (getEncrypted() == null) {
            throw new RuntimeException("Need to perform encryption first.");
        }
        
                
    }

    private int[] binaryAdd(int[] value1, int[] value2) {
        
        if (value1.length < value2.length) {
            throw new IllegalArgumentException("First array needs to be of equal or greater size.");
        }

        int[] result = new int[value1.length];

        for (int i = 0; i < value1.length; i++) {
            value1[i] ^= value2[i];
        }
        return result;
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
}
