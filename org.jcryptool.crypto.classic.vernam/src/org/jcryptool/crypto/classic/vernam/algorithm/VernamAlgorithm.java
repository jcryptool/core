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
package org.jcryptool.crypto.classic.vernam.algorithm;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.OperationsPlugin;
import org.jcryptool.core.operations.algorithm.classic.AbstractClassicAlgorithm;
import org.jcryptool.core.operations.alphabets.AlphaConverter;
import org.jcryptool.core.operations.dataobject.IDataObject;
import org.jcryptool.core.operations.dataobject.classic.ClassicDataObject;
import org.jcryptool.crypto.classic.vernam.ui.VernamWizard;
/**
 * The Vernam Algorithm 
 * 
 * @author Michael Sommer (M1S)
 * @version 0.0.1
 *
 */
public class VernamAlgorithm extends AbstractClassicAlgorithm 
{
	private VernamEngine engine;
	private VernamWizard vernamWizard;
	
	/** Data object implementation must be the classic implementation. */
    private ClassicDataObject dataObject;
	
	public static final VernamAlgorithmSpecification specification = new VernamAlgorithmSpecification();
	
	public VernamAlgorithm() 
	{
		engine = new VernamEngine();
	}

	@Override
	protected int[] generateKey(char[] keyData) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public IDataObject execute()
	{
        return dataObject;
	}
	
	public void init( int opmode, InputStream input, char[] key )
	{
		this.dataObject = new ClassicDataObject();
        this.dataObject.setInputStream(input);
        this.dataObject.setKey(key);
        this.dataObject.setOpmode(opmode);
	}
	
	@Override
	public String getAlgorithmName() {
		// TODO Auto-generated method stub
		return "Vernam";
	}
	
}
