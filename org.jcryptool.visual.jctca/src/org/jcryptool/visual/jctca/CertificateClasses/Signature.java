package org.jcryptool.visual.jctca.CertificateClasses;

import java.util.Date;

import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;

public class Signature {
	private byte[] signature;
	
	String path;
	
	String text;
	
	Date time;
	
	KeyStoreAlias privAlias;
	
	KeyStoreAlias pubAlias;
	
	
	public Signature(byte[] signature, String path, String text, Date time, KeyStoreAlias privAlias, KeyStoreAlias pubAlias){
		this.signature = signature;
		this.path = path;
		this.text = text;
		this.time = time;
		this.privAlias = privAlias;
		this.pubAlias = pubAlias;
	}

	/**
	 * @return the signature
	 */
	public byte[] getSignature() {
		return signature;
	}

	/**
	 * @param signature the signature to set
	 */
	public void setSignature(byte[] signature) {
		this.signature = signature;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the time
	 */
	public Date getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(Date time) {
		this.time = time;
	}

	/**
	 * @return the alias
	 */
	public KeyStoreAlias getPrivAlias() {
		return privAlias;
	}

	/**
	 * @param privAlias the Alias to set
	 */
	public void setPrivAlias(KeyStoreAlias privAlias) {
		this.privAlias = privAlias;
	}
	
	/**
	 * @return the alias
	 */
	public KeyStoreAlias getPubAlias() {
		return pubAlias;
	}

	/**
	 * @param pubAlias the Alias to set
	 */
	public void setPubAlias(KeyStoreAlias pubAlias) {
		this.pubAlias = pubAlias;
	}
}
