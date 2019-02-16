//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2019 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.jctca.CertificateClasses;

import java.util.ArrayList;
import java.util.Date;

import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;

/**
 * Class containing the information that is shown in the ra-view
 * 
 * @author mmacala
 * 
 */
public class RegistrarCSR {

    /**
     * the instance of this class
     */
    private static RegistrarCSR instance = null;

    /**
     * the arraylist containing all the CSRs for the ra
     */
    private ArrayList<CSR> csr;

    /**
     * private constructor for this class. use getInstance
     */
    private RegistrarCSR() {
        csr = new ArrayList<CSR>();
    }

    /**
     * delivers an instance of this class
     * 
     * @return the instance
     */
    public static RegistrarCSR getInstance() {
        if (instance == null) {
            instance = new RegistrarCSR();
        }
        return instance;
    }

    /**
     * gets the size of the list with the CSRs submitted to the RA
     * 
     * @return the amount of CSRs
     */
    public int csrSize() {
        return csr.size();
    }

    /**
     * gets the list containing all CSRs for the RA
     * 
     * @return the CSR-List
     */
    public ArrayList<CSR> getCSR() {
        return csr;
    }

    /**
     * removes the given CSR from the CSR-List
     * 
     * @param c - the csr to be removed from the list
     * @return true if the csr has ben removed
     */
    public boolean removeCSR(CSR c) {
        return csr.remove(c);
    }

    /**
     * Gets the CSR from the CSR-List at the given index
     * 
     * @param i - the index
     * @return the requested CSR, null if there is no CSR at the given index
     */
    public CSR getCSR(int i) {
        if (csr != null && csr.size() > i && i >= 0) {
            return csr.get(i);
        }
        return null;
    }

    /**
     * creates a CSR and adds it to the CSR-List for the RA
     * 
     * @param txt_first_name - first name
     * @param txt_last_name - last name
     * @param txt_street - street of the requestor
     * @param txt_zip - zip of the requestor
     * @param txt_town - town of the requestor
     * @param txt_country - country of the requestor
     * @param txt_mail - E-Mail of the requestor
     * @param path - path to the file containing the proof of identity. has to be an image
     * @param pubAlias - the alias for the public key in the keystore
     * @param privAlias - the alias for the private key in the keystore
     */
    public void addCSR(String txt_first_name, String txt_last_name, String txt_street, String txt_zip, String txt_town,
            String txt_country, String txt_mail, String path, KeyStoreAlias pubAlias, KeyStoreAlias privAlias,
            Date created) {
        csr.add(new CSR(txt_first_name, txt_last_name, txt_street, txt_zip, txt_town, txt_country, txt_mail, path,
                pubAlias, privAlias, created));
    }
}
