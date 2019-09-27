/*
 * Copyright 2014 Amund Elstad. Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License
 * at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package com.github.aelstad.keccakj.spi;

import com.github.aelstad.keccakj.core.KeccackSponge;
import com.github.aelstad.keccakj.fips202.Shake256;

public class Shake256StreamCipher extends AbstractSpongeStreamCipher {

    private Shake256 sponge;

    @Override
    KeccackSponge getSponge() {
        if (sponge == null) {
            sponge = new Shake256();
        }
        return sponge;
    }

}
