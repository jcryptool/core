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
package org.jcryptool.crypto.flexiprovider.keystore;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.crypto.SecretKey;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.filesystem.URIUtil;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.jcryptool.core.logging.utils.LogUtil;

import codec.asn1.ASN1Exception;
import codec.asn1.DERDecoder;
import codec.pkcs12.PFX;

public class ImportManager {
	private static ImportManager instance;

	private ImportManager() {}

	public synchronized static ImportManager getInstance() {
		if (instance==null) instance = new ImportManager();
		return instance;
	}

	public SecretKey importSecretKey(IPath path) {
		ObjectInputStream ois;
		try {
			IFileStore fileStore = EFS.getStore(URIUtil.toURI(path));
			ois = new ObjectInputStream(
					new BufferedInputStream(
							fileStore.openInputStream(EFS.NONE, null)));
			SecretKey key = (SecretKey)ois.readObject();
			ois.close();
			return key;
		} catch (CoreException e) {
			LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID, "CoreException while accessing a file store", e, true);
		} catch (IOException e) {
			LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID, "IOException while initializing the object input stream", e, false);
		} catch (ClassNotFoundException e) {
			LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID, "ClassNotFoundException while initializing the object input stream", e, true);
		}
		return null;
	}

	public Certificate importCertificate(IPath path) {
		BufferedInputStream is;
		try {
			IFileStore fileStore = EFS.getStore(URIUtil.toURI(path));
			is = new BufferedInputStream(fileStore.openInputStream(EFS.NONE, null));
			CertificateFactory factory = CertificateFactory.getInstance("X.509"); //$NON-NLS-1$
			return factory.generateCertificate(is);
		} catch (CoreException e) {
			LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID, "CoreException while accessing a file store", e, true);
		} catch (CertificateException e) {
			LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID, "CertificateException while importing a certificate", e, true);
		}
		return null;
	}

	public PFX importPFX(IPath path) {
		BufferedInputStream is;
		try {
			IFileStore fileStore = EFS.getStore(URIUtil.toURI(path));
			is = new BufferedInputStream(fileStore.openInputStream(EFS.NONE, null));
			PFX pfx = new PFX();
			DERDecoder decoder = new DERDecoder(is);
			pfx.decode(decoder);
			decoder.close();
			return pfx;
		} catch (CoreException e) {
			LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID, "CoreException while accessing a file store", e, true);
		} catch (ASN1Exception e) {
			LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID, "ASN1Exception while decoding a pfx", e, true);
		} catch (IOException e) {
			LogUtil.logError(FlexiProviderKeystorePlugin.PLUGIN_ID, "IOException while decoding a pfx", e, false);
		}
		return null;
	}

}
