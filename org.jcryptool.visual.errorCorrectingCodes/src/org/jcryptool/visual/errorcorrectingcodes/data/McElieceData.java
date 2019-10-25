package org.jcryptool.visual.errorcorrectingcodes.data;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * McElieceData is a model class to store various information needed for the McEliece view and algorithm.
 * 
 * @author dhofmann
 *
 */
public class McElieceData {
    private String originalString;
    private String decodedString;
    private String correctedString;
    private String binaryAsString;
    private String codeAsString;
    private String binaryDecoded;
    private String codeStringWithErrors;

    private ArrayList<Integer> maskH;
    private Matrix2D matrixG;
    private Matrix2D matrixH;
    private Matrix2D matrixP;
    private Matrix2D matrixS;
    private Matrix2D matrixSInv;
    private Matrix2D matrixPInv;
    private Matrix2D matrixSGP;
    private BitArray binary;
    private BitArray error;
    private BitArray encrypted;
    private BitArray decrypted;
    private PropertyChangeSupport pcs;

    public McElieceData() {
        super();
        pcs = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(propertyName, listener);
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        pcs.firePropertyChange(propertyName, oldValue, newValue);
    }

    public String getOriginalString() {
        return originalString;
    }

    public void setOriginalString(String originalString) {
        String oldText = this.originalString;
        this.originalString = originalString;
        pcs.firePropertyChange("originalString", oldText, originalString);
    }

    public String getDecodedString() {
        return decodedString;
    }

    public void setDecodedString(String string) {
        String oldText = this.decodedString;
        this.decodedString = string;
        pcs.firePropertyChange("decodedString", oldText, decodedString);
    }

    public String getBinaryAsString() {
        return binaryAsString;
    }

    public void setBinaryAsString(String binaryAsString) {
        String oldText = this.binaryAsString;
        this.binaryAsString = binaryAsString;
        pcs.firePropertyChange("binaryAsString", oldText, binaryAsString);
    }

    public String getCodeAsString() {
        return codeAsString;
    }

    public void setCodeAsString(String codeAsString) {
        String oldText = this.codeAsString;
        this.codeAsString = codeAsString;
        pcs.firePropertyChange("codeAsString", oldText, codeAsString);
    }

    public String getCodeStringWithErrors() {
        return codeStringWithErrors;
    }

    public void setCodeStringWithErrors(String codeStringWithErrors) {
        String oldText = this.codeStringWithErrors;
        this.codeStringWithErrors = codeStringWithErrors;
        pcs.firePropertyChange("codeStringWithErrors", oldText, codeStringWithErrors);
    }

    public void setCorrectedString(String correctedString) {
        String oldText = this.correctedString;
        this.correctedString = correctedString;
        pcs.firePropertyChange("correctedString", oldText, correctedString);
    }

    public String getBinaryDecoded() {
        return binaryDecoded;
    }

    public void setBinaryDecoded(String binaryDecoded) {
        String oldText = this.binaryDecoded;
        this.binaryDecoded = binaryDecoded;
        pcs.firePropertyChange("binaryDecoded", oldText, binaryDecoded);

    }

    public ArrayList<Integer> getMaskH() {
        return maskH;
    }

    public void setMaskH(ArrayList<Integer> maskH) {
        this.maskH = maskH;
    }

    public Matrix2D getMatrixG() {
        return matrixG;
    }

    public void setMatrixG(Matrix2D matrixG) {
        Matrix2D old = this.matrixG;
        this.matrixG = matrixG;
        pcs.firePropertyChange("matrixG", old, matrixG);
    }

    public Matrix2D getMatrixH() {
        return matrixH;
    }

    public void setMatrixH(Matrix2D matrixH) {
        Matrix2D old = this.matrixH;
        this.matrixH = matrixH;
        pcs.firePropertyChange("matrixH", old, matrixH);
    }

    public Matrix2D getMatrixP() {
       return this.matrixP;
    }

    public void setMatrixP(Matrix2D matrixP) {
        Matrix2D old = this.matrixP;
        this.matrixP = matrixP;
        pcs.firePropertyChange("matrixP", old, matrixP);    }

    public Matrix2D getMatrixS() {
        return matrixS;
    }

    public void setMatrixS(Matrix2D matrixS) {
        Matrix2D old = this.matrixS;
        this.matrixS = matrixS;
        pcs.firePropertyChange("matrixS", old, matrixS);
    }

    public Matrix2D getMatrixSInv() {
        return matrixSInv;
    }

    public void setMatrixSInv(Matrix2D matrixSInv) {
        Matrix2D old = this.matrixSInv;
        this.matrixSInv = matrixSInv;
        pcs.firePropertyChange("matrixSInv", old, matrixSInv);
    }

    public Matrix2D getMatrixPInv() {
        return matrixPInv;
    }

    public void setMatrixPInv(Matrix2D matrixPInv) {
        Matrix2D old = this.matrixPInv;
        this.matrixPInv = matrixPInv;
        pcs.firePropertyChange("matrixPInv", old, matrixPInv);
    }

    public Matrix2D getMatrixSGP() {
        return matrixSGP;
    }

    public void setMatrixSGP(Matrix2D matrixSGP) {
        Matrix2D old = this.matrixSGP;
        this.matrixSGP = matrixSGP;
        pcs.firePropertyChange("matrixSGP", old, matrixSGP);
    }

    public BitArray getBinary() {
        return binary;
    }

    public void setBinary(BitArray binary) {
        this.binary = binary;
    }

    public BitArray getError() {
        return error;
    }

    public void setError(BitArray error) {
        this.error = error;
    }

    public BitArray getEncrypted() {
        return encrypted;
    }

    public void setEncrypted(BitArray encrypted) {
        this.encrypted = encrypted;
    }

    public BitArray getDecrypted() {
        return decrypted;
    }

    public void setDecrypted(BitArray decrypted) {
        this.decrypted = decrypted;
    }
}
