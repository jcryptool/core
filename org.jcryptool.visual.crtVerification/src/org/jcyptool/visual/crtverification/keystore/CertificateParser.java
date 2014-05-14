package org.jcyptool.visual.crtverification.keystore;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.cert.CertPath;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.keys.IKeyStoreAlias;
import org.jcryptool.visual.crtverification.Activator;

/**
 * Class to load and parse X.509 certificate files to {@link java.security.cert.Certificate} objects <br>
 * requires .pem certificate files
 * 
 * @author Clemens Elst
 * 
 */
public class CertificateParser {

    File tornezeder = null;
    File google = null;
    File revolutions = null;
    File cv = null;

    File gRoot = null;
    File gCA = null;
    File gTN = null;

    public Certificate loadCertificate() {

        try {
            tornezeder = new File("/Users/clemenselst/Desktop/cert/tornetezeder.pem");
            google = new File("/Users/clemenselst/Desktop/cert/google.pem");
            revolutions = new File("/Users/clemenselst/Desktop/cert/revolutions.pem");
            cv = new File("/Users/clemenselst/Desktop/cert/cv.pem");

            gTN = new File("/Users/clemenselst/Desktop/cert/google/tn.pem");
            gCA = new File("/Users/clemenselst/Desktop/cert/google/ca.pem");
            gRoot = new File("/Users/clemenselst/Desktop/cert/google/root.pem");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        // return loadCertificate(tornezeder);
        // return loadCertificate(revolutions);
        File[] files = {gTN, gCA, gRoot};
        //loadCertificatePath(files);
        
        KeystoreConnector mKeystoreConnector = new KeystoreConnector();
        ArrayList<Certificate> certs = mKeystoreConnector.getAllCertificates();
        for(Certificate cert : certs){
            System.out.println(cert.toString());
        }
        
        //addCertificate();
        
        return null;
    }

    public Certificate loadCertificate(File filename) {
        Certificate cert = null;

        FileInputStream fis;
        try {
            fis = new FileInputStream(filename);
            BufferedInputStream bis = new BufferedInputStream(fis);

            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            while (bis.available() > 0) {
                cert = cf.generateCertificate(bis);
            }
        } catch (IOException | CertificateException e) {
            LogUtil.logError(Activator.PLUGIN_ID, e);
        }
        
        System.out.println(cert.toString());
        return cert;
    }

    public CertPath loadCertificatePath(File[] filenames) {
        CertPath mCertPath = null;
        List<Certificate> certList = null;
        Certificate[] certs = new Certificate[filenames.length];

        FileInputStream fis;
        BufferedInputStream bis;

        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            for (int i =0; i<filenames.length; i++) {

                fis = new FileInputStream(filenames[i]);
                bis = new BufferedInputStream(fis);

                while (bis.available() > 0) {
                    certs[i] = cf.generateCertificate(bis);
                }
            }
            
            certList = Arrays.asList(certs);
            mCertPath = cf.generateCertPath(certList);

        } catch (IOException | CertificateException e) {
            LogUtil.logError(Activator.PLUGIN_ID, e);
        }

        System.out.println(mCertPath.toString());
        return mCertPath;
    }
    
    public void addCertificate(){
        KeystoreConnector mKeystoreConnector = new KeystoreConnector();
        IKeyStoreAlias alias = mKeystoreConnector.getAliasByContactName("ific");
        
        Certificate cert = loadCertificate(gRoot);
        mKeystoreConnector.addCertificate(cert, alias);
    }
}
