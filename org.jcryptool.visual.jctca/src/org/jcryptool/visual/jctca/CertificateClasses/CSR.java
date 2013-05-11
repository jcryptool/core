package org.jcryptool.visual.jctca.CertificateClasses;

import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;

public class CSR {

	private String first;
	private String last;
	private String street;
	private String zip;
	private String town;
	private String country;
	private String mail;
	private String proof;
	private KeyStoreAlias privAlias;
	private KeyStoreAlias pubAlias;
	private boolean forwardenabled;
	private boolean rejectenabled;

	public CSR(String first, String last, String street, String zip,
			String town, String country, String mail, String proof,
			KeyStoreAlias pubAlias, KeyStoreAlias privAlias) {
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
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getProof() {
		return proof;
	}

	public void setProof(String proof) {
		this.proof = proof;
	}

	public KeyStoreAlias getPrivAlias() {
		return privAlias;
	}

	public void setPrivAlias(KeyStoreAlias privAlias) {
		this.privAlias = privAlias;
	}

	public KeyStoreAlias getPubAlias() {
		return pubAlias;
	}

	public void setPubAlias(KeyStoreAlias pubAlias) {
		this.pubAlias = pubAlias;
	}

	public boolean isForwardenabled() {
		return forwardenabled;
	}

	public void setForwardenabled(boolean forwardenabled) {
		this.forwardenabled = forwardenabled;
	}

	public boolean isRejectenabled() {
		return rejectenabled;
	}

	public void setRejectenabled(boolean rejectenabled) {
		this.rejectenabled = rejectenabled;
	}

}
