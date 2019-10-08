package org.jcryptool.visual.sphincsplus.interfaces;

import org.jcryptool.visual.sphincsplus.algorithm.Key;

public interface ISphincsPlus {
    public Key spx_keygen();

    public byte[] spx_sign(byte[] message);

    public boolean spx_verify(byte[] message, byte[] SIG);

}
