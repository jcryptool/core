/*
 * Copyright 2014 Amund Elstad. Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License
 * at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package com.github.aelstad.keccakj.spi;

import java.security.SecureRandomSpi;

import com.github.aelstad.keccakj.core.DuplexRandom;

/**
 * A cryptographic random implementation providing 256-bit security suitable for generating long
 * term keys.
 * 
 * Forgets the previous state after every call to nextBytes.
 */
public final class KeccakRnd256 extends SecureRandomSpi {
    private final DuplexRandom dr = new DuplexRandom(509);

    @Override
    protected byte[] engineGenerateSeed(int len) {
        byte[] rv = new byte[len];

        DuplexRandom.getSeedBytes(rv, 0, len);

        return rv;

    }

    @Override
    protected void engineNextBytes(byte[] buf) {
        dr.getBytes(buf, 0, buf.length);
        dr.forget();
    }

    @Override
    protected void engineSetSeed(byte[] seed) {
        dr.seed(seed, 0, seed.length);
    }

}
