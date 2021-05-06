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
package org.jcryptool.crypto.flexiprovider.engines.securerandom;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.core.filesystem.URIUtil;
import org.eclipse.ui.PartInitException;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.editors.AbstractEditorService;
import org.jcryptool.core.operations.editors.EditorsManager;
import org.jcryptool.core.operations.util.PathEditorInput;
import org.jcryptool.crypto.flexiprovider.descriptors.IFlexiProviderOperation;
import org.jcryptool.crypto.flexiprovider.descriptors.algorithms.SecureRandomDescriptor;
import org.jcryptool.crypto.flexiprovider.engines.FlexiProviderEngine;
import org.jcryptool.crypto.flexiprovider.engines.FlexiProviderEnginesPlugin;

import de.flexiprovider.api.Registry;
import de.flexiprovider.api.SecureRandom;
import de.flexiprovider.api.exceptions.NoSuchAlgorithmException;

public class SecureRandomEngine extends FlexiProviderEngine {
	private SecureRandom random;
	private byte[] value;

	public SecureRandomEngine() {
	}

	@Override
	public KeyObject init(IFlexiProviderOperation operation) {
		LogUtil.logInfo("initializing message digest engine"); //$NON-NLS-1$
		this.operation = operation;
		try {
			random = Registry.getSecureRandom(operation.getAlgorithmDescriptor().getAlgorithmName());
			byte[] seed = random.generateSeed(((SecureRandomDescriptor)operation.getAlgorithmDescriptor()).getLength());
			random.setSeed(seed);
			value = new byte[seed.length];
			initialized = true;
		} catch (NoSuchAlgorithmException e) {
		    LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID, "NoSuchAlgorithmException while initializing a  secure random"); //$NON-NLS-1$
		}
		return null;
	}

	@Override
	public void perform(KeyObject usedKey) {
		if (initialized) {
			LogUtil.logInfo("performing a secure random"); //$NON-NLS-1$
			OutputStream outputStream = initOutput(operation.getOutput());
			try {
				if (operation.getOutput().equals("<Editor>")) { //$NON-NLS-1$
					byte[][] alphabet = ((SecureRandomDescriptor)operation.getAlgorithmDescriptor()).getAlphabet();
					if(alphabet == null){
						random.nextBytes(value);
						outputStream.write(value);
						outputStream.close();
						EditorsManager.getInstance().openNewHexEditor(AbstractEditorService.createOutputFile(new FileInputStream(new File(getOutputURI()))));
					}
					else{
						value = new byte[1];
						int log = (int) Math.pow(2, Math.ceil((Math.log(alphabet.length)/Math.log(2))));
						for(int i=0;i<((SecureRandomDescriptor)operation.getAlgorithmDescriptor()).getLength();i++){
							boolean found = false;
							while(!found){
								random.nextBytes(value);
								int r = (int) ((int) value[0] & (log-1)); // modulo
								if(r<0)
									r+=log;

								if(r<alphabet.length){
									outputStream.write(alphabet[r]);
									found = true;

									if((i+1)%20 == 0)
										outputStream.write("\n".getBytes()); //$NON-NLS-1$
								}
							}
						}
						outputStream.close();
						EditorsManager.getInstance().openNewTextEditor(new PathEditorInput(URIUtil.toPath(getOutputURI())));
					}
				}
			} catch (IOException e) {
			    LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID, "IOException while performing a secure random", e, false);
			} catch (PartInitException e) {
                LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID, "Failed to open the Hexeditor", e, true);
			}
		}
	}

}
