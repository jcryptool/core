package org.jcyptool.visual.crtverification.keystore;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.security.cert.CertPath;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.keys.IKeyStoreAlias;
import org.jcryptool.visual.crtverification.Activator;
import org.jcryptool.visual.crtverification.models.ShellModelVerifier;

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
    File[] files;

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
         
        // loadCertificatePath(files);

        File[] files = { gTN, gCA, gRoot};
        this.files = files;
        
        
        addCertificate();
        
        KeystoreConnector mKeystoreConnector = new KeystoreConnector();
        ArrayList<Certificate> certs = mKeystoreConnector.getAllCertificates();
        for (Certificate cert : certs) {
            System.out.println(cert.toString());
        }

         

        //verify();

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

        //System.out.println(cert.toString());
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
            for (int i = 0; i < filenames.length; i++) {

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

        //System.out.println(mCertPath.toString());
        return mCertPath;
    }

    public void addCertificate() {
        KeystoreConnector mKeystoreConnector = new KeystoreConnector();
        IKeyStoreAlias alias = mKeystoreConnector.getAliasByContactName("lemens");
        IKeyStoreAlias alias2 = mKeystoreConnector.getAliasByContactName("root");
        if(alias != null){
            System.out.println("found");
        }
        
        Enumeration<String> al = mKeystoreConnector.getAllAliases();
        while (al.hasMoreElements()) {
           System.out.println(al.nextElement() + "\n");
            
        }
        
//System.out.println(alias.getContactName());
        


        Certificate cert = loadCertificate(gTN);
    
        mKeystoreConnector.addCertificate(cert, alias);

         cert = loadCertificate(gCA);
        mKeystoreConnector.addCertificate(cert, alias);
         cert = loadCertificate(gRoot);
        mKeystoreConnector.addCertificate(cert, alias);
        
       
        
        
        
    }

    private void verify() {
        KeystoreConnector ksc = new KeystoreConnector();
        ArrayList<Certificate> certs = ksc.getAllCertificates();

        Certificate root = null;
        Certificate ca = null;
        Certificate tn = null;
        CertPath path= null;
        List<Certificate>  certList =null;
        
        for (Certificate certificate : certs) {
            X509Certificate crt = (X509Certificate) certificate;
            if (crt.toString().contains("SerialNumber: [    753a3bd8 98601282]")) {
                tn = crt;
                System.out.println(tn.toString());
                if(tn==null) {System.out.println("tn null");}
            } else if (crt.toString().contains("SSerialNumber: [    023a69]")) {
                ca = crt;
                System.out.println(ca.toString());
                if(ca==null) {System.out.println("ca null");}
            } else if (crt.toString().contains("SerialNumber: [    023456]")) {
                root = crt;
                System.out.println(root.toString());
                if(root==null) {System.out.println("root null");}
            }
        }
        
         certList = Arrays.asList(new Certificate[] {root, ca, tn});
        
        try {
            Certificate[] foo = (Certificate[]) certList.toArray();
            for (int i = 0; i < certList.size(); i++) {
                if(foo[i] == null){
                    System.out.println(foo[i].toString());;
                }
            }
            
             CertificateFactory cf = CertificateFactory.getInstance("X.509");
             cf.generateCertPath(certList);
             
        } catch (CertificateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        Date date = new Date();
        
        ShellModelVerifier smv = new ShellModelVerifier(path, false, date);
    }
}
