/*
 * Copyright 2014 Amund Elstad. Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License
 * at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package com.github.aelstad.keccakj.spi;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;

import com.github.aelstad.keccakj.keyak.LakeKeyak;

public final class LakeKeyakCipher extends AbstractCipher {
    LakeKeyak lakeKeyak;

    private enum State {
        HEADER, BODY, TAG
    };

    private State state;

    private int TAG_SIZE = 16;

    @Override
    protected void init() throws InvalidKeyException {
        if (lakeKeyak == null)
            lakeKeyak = new LakeKeyak();

        lakeKeyak.init(getKey(), getNonce());
        state = State.HEADER;
    }

    @Override
    protected int engineUpdate(byte[] input, int inputOff, int len, byte[] output, int outputOff)
            throws ShortBufferException {
        if (state == State.HEADER) {
            lakeKeyak.endHeader(true);
            state = State.BODY;
        }
        if (getMode() == Cipher.ENCRYPT_MODE || getMode() == Cipher.WRAP_MODE)
            lakeKeyak.bodyWrap(input, inputOff, output, outputOff, len);
        else
            lakeKeyak.bodyUnwrap(input, inputOff, output, outputOff, len);

        return len;
    }

    @Override
    public void updateAAD(byte[] src, int offset, int len) {
        if (state != State.HEADER)
            throw new RuntimeException("LakeKeyak not in header mode");

        lakeKeyak.header(src, offset, len);
    }

    @Override
    protected int engineDoFinal(byte[] input, int inputOff, int len, byte[] output, int outputOff)
            throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        boolean encrypt = getMode() == Cipher.ENCRYPT_MODE || getMode() == Cipher.WRAP_MODE;

        int bodyLen = len;
        if (!encrypt)
            bodyLen -= TAG_SIZE;

        if (bodyLen == 0 && state == State.HEADER) {
            lakeKeyak.endHeader(false);
        } else if (bodyLen > 0) {
            engineUpdate(input, inputOff, bodyLen, output, outputOff);
        }
        if (state == State.BODY) {
            lakeKeyak.endBody();
        }
        if (encrypt) {
            lakeKeyak.getTag(output, outputOff + len, TAG_SIZE);
        } else {
            lakeKeyak.validateTag(input, inputOff + bodyLen, TAG_SIZE);
        }

        state = State.HEADER;

        return engineGetOutputSize(len);
    }

    @Override
    protected int engineGetOutputSize(int inputLen) {
        if (getMode() == Cipher.ENCRYPT_MODE || getMode() == Cipher.WRAP_MODE)
            return inputLen + TAG_SIZE;
        else
            return inputLen - TAG_SIZE;
    }

    public void forget() {
        lakeKeyak.forget();
    }

    @Override
    public void updateAAD(byte[] src) {
        this.updateAAD(src, 0, src.length);
    }

    @Override
    public void updateAAD(ByteBuffer src) {
        int pos = src.position() + src.arrayOffset();
        int len = (src.limit() - src.position());
        updateAAD(src.array(), pos, len);
    }

}
