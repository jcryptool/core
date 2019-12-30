// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2020, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.sphincsplus.algorithm;

/**
 * <b>Types:</b>
 * <ol>
 * <li>WOTS+ hash address</li>
 * <li>WOTS+ public key compression address</li>
 * <li>hash tree address</li>
 * <li>FORS tree address</li>
 * <li>FORS tree roots compression address</li>
 * </ol>
 * 
 * @author Lukas Stelzer
 *
 */
public enum ADRSTypes {
    WOTS_HASH, WOTS_PK, TREE, FORS_TREE, FORS_ROOTS
}
