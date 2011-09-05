// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.kegver.csFujisakiOkamoto;

import java.math.BigInteger;

/**
 * Fujisaki-Okamoto commitment scheme:
 *
 * This commitment scheme, introduced in [18], is essentially a variant on the
 * commitment scheme of Pedersen [36], but adjusted for application to groups G
 * of unknown order of the form described above. It should be noted that in
 * contrast to the scheme of Pedersen, which is unconditionally hiding, the
 * Fujisaki-Okamoto scheme is only statistically hiding in a security parameter
 * m.
 *
 * To commit to a value x ∈ Z, the user selects a random commitment factor w ∈U
 * {−2mN + 1, 2m N − 1}. She then computes the commitment C(x, w) = g^x h^w mod
 * N .
 *
 * For further details on this setup, see [7, 18]. Note that this commitment
 * scheme is only certain to be hiding provided that the CA has selected g and h
 * honestly. In particular, it suffices that <g>=<h>. (In fact, weaker
 * requirements suffice, such as g ∈<h> or h having large order in <g>. We
 * adhere to the simpler condition <g>=<h> throughout to avoid confusion.)
 *
 * Thus, to ensure secure hiding, we require that the CA prove knowledge of
 * log_h g and log_g h over ZN . (Such proofs may be published as
 * non-interactive proofs of knowledge by the CA along with its public
 * parameters.) The Fujisaki-Okamoto commitment scheme is binding assuming the
 * hardness of factoring, i.e., that the user cannot factor N .
 *
 * [7] F. Boudot. Efficient proofs that a committed number lies in an interval.
 * In B. Pre- neel, editor, Advances in Cryptology – EUROCRYPT ’00, pages
 * 431–444, 2000. LNCS no. 1807.
 *
 * [18] E. Fujisaki and T. Okamoto. Statistical zero knowledge protocols to
 * prove modular polynomial relations. In B. Kaliski, editor, Advances in
 * Cryptology – CRYPTO ’97, pages 16–30. Springer-Verlag, 1997. LNCS no. 1294.
 *
 * [36] T. Pedersen. Non-interactive and information-theoretic secure verifiable
 * secret sharing. In J. Feigenbaum, editor, Advances in Cryptology - CRYPTO
 * ’91, pages 129–140. Springer-Verlag, 1991. LNCS no. 576.
 *
 *
 *
 * @author two
 *
 */
public class CSFujisakiOkamoto {

	private static int m = 0;

	public int selectMRandomly(BigInteger inN){
		return 0;
		//TODO
	}

	public BigInteger calculateCommitment(int inX, int inW){
		return null;
//		BigInteger C =
	}

}
