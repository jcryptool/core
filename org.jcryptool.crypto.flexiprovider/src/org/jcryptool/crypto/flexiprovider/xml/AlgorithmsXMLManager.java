//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2021 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.xml;

import java.util.List;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.flexiprovider.FlexiProviderPlugin;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaAlgorithm;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaKeyGenerator;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaMode;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaOID;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaPaddingScheme;
import org.jcryptool.crypto.flexiprovider.descriptors.reflect.interfaces.IMetaSpec;
import org.jcryptool.crypto.flexiprovider.exception.InvalidAlgorithmsXMLElementException;
import org.jdom.Document;


public class AlgorithmsXMLManager {
	private static AlgorithmsXMLManager instance;

	private static Document xmlDocument;
	private static FlexiProviderRootElement root;

	public synchronized static AlgorithmsXMLManager getInstance() {
		if (instance==null) instance = new AlgorithmsXMLManager();
		return instance;
	}

	private AlgorithmsXMLManager() {
		xmlDocument = de.flexiprovider.FlexiProviderPlugin.getAlgorithmsXML();
		try {
			root = new FlexiProviderRootElement(xmlDocument.getRootElement());
			LogUtil.logInfo("Version: " + root.getVersion()); //$NON-NLS-1$
		} catch (InvalidAlgorithmsXMLElementException e) {
		    LogUtil.logError(FlexiProviderPlugin.PLUGIN_ID, "InvalidAlgorithmsXMLElementException while parsing the xml structure", e, false); //$NON-NLS-1$
		}

	}

	/* ------------------------------ GETTERS BELOW ------------------------------ */

	public List<IMetaAlgorithm> getAsymmetricBlockCiphers() {
		return root.getAsymmetricBlockCiphers();
	}

	public List<IMetaAlgorithm> getAsymmetricHybridCiphers() {
		return root.getAsymmetricHybridCiphers();
	}

	public IMetaAlgorithm getBlockCipher(IMetaOID oid) {
		return root.getBlockCipher(oid);
	}

	public IMetaAlgorithm getBlockCipher(String name) {
		return root.getBlockCipher(name);
	}

	public List<IMetaAlgorithm> getBlockCiphers() {
		return root.getBlockCiphers();
	}

	public List<IMetaAlgorithm> getCiphers() {
		return root.getCiphers();
	}

	public IMetaKeyGenerator getKeyPairGenerator(String name) {
		return root.getKeyPairGenerator(name);
	}

	public List<IMetaKeyGenerator> getKeyPairGenerators() {
		return root.getKeyPairGenerators();
	}

	public List<IMetaAlgorithm> getMacs() {
		return root.getMacs();
	}

	public List<IMetaAlgorithm> getMessageDigests() {
		return root.getMessageDigests();
	}

	public IMetaMode getMode(String name) {
		return root.getMode(name);
	}

	public IMetaMode getModeForID(String id) {
		return root.getModeForID(id);
	}

	public List<IMetaMode> getModes() {
		return root.getModes();
	}

	public IMetaPaddingScheme getPaddingScheme(String name) {
		return root.getPaddingScheme(name);
	}

	public IMetaPaddingScheme getPaddingSchemeForID(String id) {
		return root.getPaddingSchemeForID(id);
	}

	public List<IMetaPaddingScheme> getPaddingSchemes() {
		return root.getPaddingSchemes();
	}

	public IMetaSpec getParameterSpec(String className) {
		return root.getParameterSpec(className);
	}

	public IMetaKeyGenerator getSecretKeyGenerator(String name) {
		return root.getSecretKeyGenerator(name);
	}

	public List<IMetaKeyGenerator> getSecretKeyGenerators() {
		return root.getSecretKeyGenerators();
	}

	public List<IMetaAlgorithm> getSecureRandoms() {
		return root.getSecureRandoms();
	}

	public IMetaAlgorithm getSignature(IMetaOID oid) {
		return root.getSignature(oid);
	}

	public IMetaAlgorithm getSignature(String name) {
		return root.getSignature(name);
	}

	public List<IMetaAlgorithm> getSignatures() {
		return root.getSignatures();
	}

}
