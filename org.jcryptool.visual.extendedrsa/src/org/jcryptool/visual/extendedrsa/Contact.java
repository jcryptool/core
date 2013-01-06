//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2012 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.extendedrsa;

import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.descriptors.interfaces.IContactDescriptor;

/**
 * represents a contact/identity
 * @author Christoph Schnepf, Patrick Zillner
 *
 */
public class Contact implements IContactDescriptor{

	private String name;
	private String firstName;
	private String lastName;
	private String organisation;
	private String region;
	
	
	public Contact(String name, String firstName, String lastName, String organisation, String region) {
		this.name = name;
		this.firstName = firstName;
		this.lastName = lastName;
		this.organisation = organisation;
		this.region = region;
	}

	@Override
	public String getName(){
		return name;
	}

	@Override
	public String getFirstname() {
		return firstName;
	}

	@Override
	public String getLastname() {
		return lastName;
	}

	@Override
	public String getOrganization() {
		return organisation;
	}

	@Override
	public String getRegion() {
		return region;
	}

	@Override
	public void addSecretKey(KeyStoreAlias alias) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addCertificate(KeyStoreAlias alias) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addKeyPair(KeyStoreAlias privateKey, KeyStoreAlias publicKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeSecretKey(KeyStoreAlias alias) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeCertificate(KeyStoreAlias alias) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeKeyPair(KeyStoreAlias privateKey) {
		// TODO Auto-generated method stub
		
	}

}
