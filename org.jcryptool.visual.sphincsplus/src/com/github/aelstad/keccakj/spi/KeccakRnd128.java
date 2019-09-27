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
 * A cryptographic random implementation providing 128-bit security suitable for generating session
 * keys.
 * 
 * Forgets the previous state after 2MB of random data.
 * 
 * Re-seeding with a small static seed also forgets the state.
 * 
 */
public final class KeccakRnd128 extends SecureRandomSpi {
    private final DuplexRandom dr = new DuplexRandom(253);

    private final static int FORGET_INTERVAL = 2 * 1024 * 1024;

    private int forgetCounter;

    public KeccakRnd128() {
        this.forgetCounter = FORGET_INTERVAL;
    }

    @Override
    protected byte[] engineGenerateSeed(int len) {
        byte[] rv = new byte[len];

        DuplexRandom.getSeedBytes(rv, 0, len);

        return rv;

    }

    @Override
    protected void engineNextBytes(byte[] buf) {
        dr.getBytes(buf, 0, buf.length);
        forgetCounter -= buf.length;
        if (forgetCounter <= 0) {
            dr.forget();
            forgetCounter = FORGET_INTERVAL;
        }
    }

    @Override
    protected void engineSetSeed(byte[] seed) {
        dr.seed(seed, 0, seed.length);
    }

}
