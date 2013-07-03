package org.jcryptool.visual.jctca.CertificateClasses;

import java.util.Date;

import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;

/**
 * An entry representing a CSR that goes from the user to the RA and from the RA to the CA.
 * 
 * @author mmacala
 * 
 */
public class CSR {

    /**
     * first name of requestor
     */
    private String first;

    /**
     * last name of requestor
     */
    private String last;

    /**
     * street of the requestor
     */
    private String street;

    /**
     * zip of the requestor
     */
    private String zip;

    /**
     * town of the requestor
     */
    private String town;

    /**
     * country of the requestor
     */
    private String country;

    /**
     * E-Mail address of the requestor
     */
    private String mail;

    /**
     * path to the identity proof of the requestor. has to be an image
     */
    private String proof;

    /**
     * the alias for the private key that is stored in the keystore
     */
    private KeyStoreAlias privAlias;

    /**
     * the alias for the public key that is stored in the keystore
     */
    private KeyStoreAlias pubAlias;

    /**
     * describes if the forward CSR button in the RA-view should be enabled or not
     */
    private boolean forwardenabled;

    /**
     * describes if the reject CSR button in the RA-View should be enabled or not
     */
    private boolean rejectenabled;

    private Date created;

    /**
     * the standard constructor for creating a valid CSR
     * 
     * @param first - first name
     * @param last - last name
     * @param street - street of the requestor
     * @param zip - zip of the requestor
     * @param town - town of the requestor
     * @param country - country of the requestor
     * @param mail - E-Mail of the requestor
     * @param proof - path to the file containing the proof of identity. has to be an image
     * @param pubAlias - the alias for the public key in the keystore
     * @param privAlias - the alias for the private key in the keystore
     */
    public CSR(String first, String last, String street, String zip, String town, String country, String mail,
            String proof, KeyStoreAlias pubAlias, KeyStoreAlias privAlias, Date created) {
        super();
        this.first = first;
        this.last = last;
        this.street = street;
        this.zip = zip;
        this.town = town;
        this.country = country;
        this.mail = mail;
        this.proof = proof;
        this.privAlias = privAlias;
        this.pubAlias = pubAlias;
        this.forwardenabled = false;
        this.rejectenabled = false;
        this.created = created;
    }

    public Date getCreated() {
        return created;
    }

    /**
     * gets the first name of the requestor
     * 
     * @return the first name
     */
    public String getFirst() {
        return first;
    }

    /**
     * sets the first name of the requestor
     * 
     * @param first - the new first name
     */
    public void setFirst(String first) {
        this.first = first;
    }

    /**
     * gets the last name of the requestor
     * 
     * @return the last name
     */
    public String getLast() {
        return last;
    }

    /**
     * sets the last name of the requestor
     * 
     * @param last - the new last name
     */
    public void setLast(String last) {
        this.last = last;
    }

    /**
     * gets the street name of the requestor
     * 
     * @return the street name
     */
    public String getStreet() {
        return street;
    }

    /**
     * sets the street name of the requestor
     * 
     * @param street - the new street name
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * gets the zip code of the requestor
     * 
     * @return the zip code
     */
    public String getZip() {
        return zip;
    }

    /**
     * sets the zip code of the requestor
     * 
     * @param zip - the new zip code
     */
    public void setZip(String zip) {
        this.zip = zip;
    }

    /**
     * gets the town name of the requestor
     * 
     * @return the town name
     */
    public String getTown() {
        return town;
    }

    /**
     * sets the town name of the requestor
     * 
     * @param town - the new town name
     */
    public void setTown(String town) {
        this.town = town;
    }

    /**
     * gets the country name of the requestor
     * 
     * @return the country name
     */
    public String getCountry() {
        return country;
    }

    /**
     * sets the country name of the requestor
     * 
     * @param country - the new country name
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * gets the e-mail address of the requestor
     * 
     * @return the e-mail address
     */
    public String getMail() {
        return mail;
    }

    /**
     * sets the e-mail address of the requestor
     * 
     * @param mail - the new mail-address
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * gets the path to the proof of the identity
     * 
     * @return the file path
     */
    public String getProof() {
        return proof;
    }

    /**
     * sets the file path to the proof of the identity
     * 
     * @param proof - the path of new proof
     */
    public void setProof(String proof) {
        this.proof = proof;
    }

    /**
     * gets the private key alias of the key stored in the keystore
     * 
     * @return the private key alias
     */
    public KeyStoreAlias getPrivAlias() {
        return privAlias;
    }

    /**
     * sets the private key alias of the key stored in the keystore
     * 
     * @param privAlias - the new private key alias
     */
    public void setPrivAlias(KeyStoreAlias privAlias) {
        this.privAlias = privAlias;
    }

    /**
     * gets the public key alias of the key stored in the keystore
     * 
     * @return thes public key alias
     */
    public KeyStoreAlias getPubAlias() {
        return pubAlias;
    }

    /**
     * sets the public key alias of the key stored in the keystore
     * 
     * @param pubAlias - the new public key alias
     */
    public void setPubAlias(KeyStoreAlias pubAlias) {
        this.pubAlias = pubAlias;
    }

    /**
     * describes if the forward CSR button in the ra view should be enabled
     * 
     * @return true if the button should be enabled
     */
    public boolean isForwardenabled() {
        return forwardenabled;
    }

    /**
     * sets the boolean describing if the forwad csr button in the ra view should be enabled
     * 
     * @param forwardenabled - the new value
     */
    public void setForwardenabled(boolean forwardenabled) {
        this.forwardenabled = forwardenabled;
    }

    /**
     * describes if the forward CSR button in the ra view should be enabled
     * 
     * @return true if the button should be enabled
     */
    public boolean isRejectenabled() {
        return rejectenabled;
    }

    /**
     * sets the boolean describing if the reject csr button in the ra view should be enabled
     * 
     * @param rejectenabled - the new value
     */
    public void setRejectenabled(boolean rejectenabled) {
        this.rejectenabled = rejectenabled;
    }

}
