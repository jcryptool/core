package org.jcryptool.visual.sig.listener;

import java.util.ArrayList;

public class SignatureListenerAdder {
    private static ArrayList<SignatureListener> listeners;

    public static void addSignatureListener(SignatureListener l) {
        if (listeners == null) {
            listeners = new ArrayList<SignatureListener>();
        }
        listeners.add(l);
    }// end addSignatureListener

    public static ArrayList<SignatureListener> getListeners() {
        return listeners;
    }
}
