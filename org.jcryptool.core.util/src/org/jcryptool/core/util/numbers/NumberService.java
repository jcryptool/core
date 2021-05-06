// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.util.numbers;

/**
 * Provides different services around numbers in JCrypTool.
 * 
 * 
 * The Miller-Rabin primality test used here is based on the work available at
 * http://en.literateprograms.org/Miller-Rabin_primality_test_(Java)
 * 
 * @author Dominik Schadow
 * @version 0.9.5
 */
public class NumberService {
    /**
     * Copyright (c) 2011 the authors listed at the following URL, and/or the authors of referenced articles or
     * incorporated external code:
     * http://en.literateprograms.org/Miller-Rabin_primality_test_(Java)?action=history&offset=20080201093914
     * 
     * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
     * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
     * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
     * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
     * 
     * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
     * the Software.
     * 
     * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
     * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
     * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
     * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
     * IN THE SOFTWARE.
     * 
     * Retrieved from: http://en.literateprograms.org/Miller-Rabin_primality_test_(Java)?oldid=12469
     */
    public static boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        } else if (n == 2) {
            return true;
        } else if (miller_rabin_pass_32(2, n) && (n <= 7 || miller_rabin_pass_32(7, n))
                && (n <= 61 || miller_rabin_pass_32(61, n))) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean miller_rabin_pass_32(int a, int n) {
        int d = n - 1;
        int s = Integer.numberOfTrailingZeros(d);
        d >>= s;
        int a_to_power = modular_exponent_32(a, d, n);

        if (a_to_power == 1) {
            return true;
        }

        for (int i = 0; i < s - 1; i++) {
            if (a_to_power == n - 1) {
                return true;
            }
            a_to_power = modular_exponent_32(a_to_power, 2, n);
        }

        if (a_to_power == n - 1) {
            return true;
        }

        return false;
    }

    private static int modular_exponent_32(int base, int power, int modulus) {
        long result = 1;

        for (int i = 31; i >= 0; i--) {
            result = (result * result) % modulus;
            if ((power & (1 << i)) != 0) {
                result = (result * base) % modulus;
            }
        }

        return (int) result;
    }
}
