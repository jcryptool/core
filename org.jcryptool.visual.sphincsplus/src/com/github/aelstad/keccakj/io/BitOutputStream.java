/*
 * Copyright 2014 Amund Elstad. Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License
 * at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package com.github.aelstad.keccakj.io;

import java.io.OutputStream;

public abstract class BitOutputStream extends OutputStream {

    @Override
    public abstract void close();

    @Override
    public void write(byte[] b, int off, int len) {
        writeBits(b, ((long) (off)) << 3, ((long) len) << 3);
    }

    @Override
    public void write(byte[] b) {
        write(b, 0, b.length);
    }

    @Override
    public void write(int b) {
        writeBits(new byte[] { (byte) b }, 0, 8);
    }

    public abstract void writeBits(byte[] arg, long bitOff, long bitLen);

}
