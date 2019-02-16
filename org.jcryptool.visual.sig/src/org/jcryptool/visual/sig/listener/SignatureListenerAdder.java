//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2019 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.sig.listener;

import java.util.ArrayList;

public class SignatureListenerAdder {
    private static ArrayList<SignatureListener> listeners;

    public static void addSignatureListener(SignatureListener l) {
        if (listeners == null) {
            listeners = new ArrayList<SignatureListener>();
        }
        listeners.add(l);
    }

    public static ArrayList<SignatureListener> getListeners() {
        return listeners;
    }
}
