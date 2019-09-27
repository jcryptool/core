/*
 * Copyright 2014 Amund Elstad. Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License
 * at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package com.github.aelstad.keccakj.spi;

import javax.crypto.ShortBufferException;

import com.github.aelstad.keccakj.core.KeccackSponge;
import com.github.aelstad.keccakj.io.BitOutputStream;

public abstract class AbstractSpongeStreamCipher extends AbstractCipher {

    @Override
    public void reset() {
        super.reset();
        getSponge().reset();
    }

    @Override
    protected void init() {
        KeccackSponge sponge = getSponge();
        BitOutputStream absorbStream = sponge.getAbsorbStream();
        absorbStream.write(getKey());
        if (getNonce() != null)
            sponge.getAbsorbStream().write(getNonce());

        sponge.getAbsorbStream().close();
    }

    @Override
    protected int engineUpdate(byte[] input, int inputOffset, int len, byte[] output, int outputOffset)
            throws ShortBufferException {
        return getSponge().getSqueezeStream().transform(input, inputOffset, output, outputOffset, len);
    }

    @Override
    protected byte[] engineUpdate(byte[] input, int offset, int len) {
        byte[] rv = new byte[len];
        getSponge().getSqueezeStream().transform(input, offset, rv, 0, len);

        return rv;
    }

    abstract KeccackSponge getSponge();
}
