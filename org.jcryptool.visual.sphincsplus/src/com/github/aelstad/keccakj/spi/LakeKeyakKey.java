/*
 * Copyright 2014 Amund Elstad. Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License
 * at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package com.github.aelstad.keccakj.spi;

import java.security.InvalidKeyException;

import com.github.aelstad.keccakj.provider.Constants;

public final class LakeKeyakKey extends RawKey {
    public LakeKeyakKey() {
        super();
    }

    public LakeKeyakKey(byte[] rawKey) throws InvalidKeyException {
        super(rawKey);
    }

    @Override
    public String getAlgorithm() {
        return Constants.LAKEKEYAK_AUTHENTICATING_STREAM_CIPHER;
    }

    @Override
    public int getMaxKeyLength() {
        return 30;
    }

}
