package org.jcryptool.visual.merkletree.algorithm;

/**
 * putting the XMSS_MT Parameters in a constructor because it makes the code
 * better readable
 * 
 * @author Lena Heimberger
 *
 */
public class params {
	int d; // layers of the tree
	int h; // overall tree height
	int l; // height of a single tree
	byte[] n;
	int index_len;

	public params(int h, int d, byte[] n) throws Exception {
		if (h % d != 0) {
			throw new Exception(
					"Tree height and overall tree height are different- please pick parameters where h%d==0");
			// do something fancy?
		}
		this.h = h;
		this.d = d;
		this.n = n;
		this.index_len = (h + 7) / 8;
	}

	public int getH(params P) {
		return this.h;
	}

	public int getD(params P) {
		return this.d;
	}

	public int getL(params P) {
		return this.l;
	}

	public byte[] getN(params P) {
		return this.n;
	}

	public int getIndexLength(params P) {
		return this.index_len;
	}
}
