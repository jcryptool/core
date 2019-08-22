package org.jcryptool.visual.errorcorrectingcodes;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.BitSet;
import java.util.List;

public class EccData {

    private String originalString;
    private String binaryAsString;
    private String codeAsString;
    private String codeWithErrors;

    private List<BitSet> binary;
    private List<BitSet> binaryCode;
    private List<BitSet> flippedCode;

    private PropertyChangeSupport pcs;

    public EccData() {
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

    public void setOriginalString(String text) {
        String oldText = this.originalString;
        this.originalString = text;
        pcs.firePropertyChange("originalString", oldText, text);
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
    public String getCodeWithErrors() {
        return codeWithErrors;
    }
    
    public void setCodeWithErrors(String codeWithErrors) {
        String oldText = this.codeWithErrors;
        this.codeWithErrors = codeWithErrors;
        pcs.firePropertyChange("codeWithErrors", oldText, codeWithErrors);
        
    }

    public List<BitSet> getBinary() {
        return binary;
    }

    public void setBinary(List<BitSet> binary) {
        this.binary = binary;
    }

    public List<BitSet> getBinaryCode() {
        return binaryCode;
    }

    public void setBinaryCode(List<BitSet> binaryCode) {
        this.binaryCode = binaryCode;
    }

   

}
