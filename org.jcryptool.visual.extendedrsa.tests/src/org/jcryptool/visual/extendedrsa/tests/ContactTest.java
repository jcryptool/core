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
package org.jcryptool.visual.extendedrsa.tests;

import static org.junit.Assert.*;

import org.jcryptool.visual.extendedrsa.Contact;
import org.junit.Test;
/**
 * test class for the class 'Contact.java'
 @author Christoph Schnepf, Patrick Zillner
 */
public class ContactTest {
	
	@Test
	public void testContact() {
		Contact contact = new Contact("test1","test1","test1","test1","test1");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testContact_null() {
		Contact contact = new Contact(null,null,null,null,null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testContact_null2() {
		Contact contact = new Contact(null,"firstName","lastName","organsation","region");
	}
	
	@Test
	public void testGetName() {
		Contact contact = new Contact("name","firstName","lastName","organsation","region");
		String name = contact.getName(); 
		assertEquals("name", name);
	}

	@Test
	public void testGetFirstname() {
		Contact contact = new Contact("name","firstName","lastName","organsation","region");
		String firstname = contact.getFirstname(); 
		assertEquals("firstName", firstname);
	}

	@Test
	public void testGetLastname() {
		Contact contact = new Contact("name","firstName","lastName","organsation","region");
		String lname = contact.getLastname(); 
		assertEquals("lastName", lname);
	}

	@Test
	public void testGetOrganization() {
		Contact contact = new Contact("name","firstName","lastName","organsation","region");
		String org = contact.getOrganization(); 
		assertEquals("organsation", org);
	}

	@Test
	public void testGetRegion() {
		Contact contact = new Contact("name","firstName","lastName","organsation","region");
		String region = contact.getRegion(); 
		assertEquals("region", region);
	}
}
