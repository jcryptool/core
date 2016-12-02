package org.jcryptool.visual.merkletree.algorithm;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import org.jcryptool.visual.merkletree.files.ByteUtils;
import org.jcryptool.visual.merkletree.files.Converter;
import org.jcryptool.visual.merkletree.files.MathUtils;

public abstract class MultiTree implements ISimpleMerkleTree  {
	// nr of trees for the hypertree klickibunti

	int d; // layers of the tree
	int h; // overall tree height
	int l; // height of a single tree
	int n;
	public String xPrivKey;
	public String xPubKey;
	public byte[] seed;
	OTS otsAlgo;
	MessageDigest mDigest;
	static int id;
	Node Parent;

	public MultiTree(int d, int h, int w, String message) {
		this.otsAlgo = new WOTSPlus(w, message, seed); // wots+ has the length
														// from message and not
														// from hash
		if (h % d != 1) {
			/*
			 * throw hDevidesDException(){
			 * 
			 * }
			 */
		}
		this.l = h / d;
		int i = 0;
		while (i < d) {
			// TODO
		}
	}

	/*
	 * void setParameters(int n, int h, int d, int w){
	 * 
	 * 
	 * }
	 */
	// make visible which tree signs which one--> parent

	void setLayers(int d) {
		this.d = d;
	}

	/**
	 * returns number of trees on a certain layer
	 * 
	 * @param h
	 *            overall height of the tree
	 * @param d
	 *            layer on which the number of trees is searched
	 * @return
	 */
	public double getXMSSTreeCount(int h, int d) {
		if (l == (this.d - 1))
			return 1;
		else {
			double s = Math.pow(2, (h / d));
			return s;
		}
	}

	public void xmssmt_public_key() {
		byte[] publicRoot = this.getMerkleRoot();
		byte[] publicSeed = this.getSeed();
	}

	public void visualiseSig() {
		// TODO
	}

	
	public void chooseAlgorihm() {

		// eig. klassenvariable, aber for reasons private static final String[]
		// cipherSuites={"xmssmt_sha2-256_w16_h20_d2",
		

		final String[] cipherSuites = { "xmssmt_sha2-256_w16_h20_d2", "xmssmt_sha2-256_w16_h20_d4",
				"xmssmt_sha2-256_w16_h40_d2", "xmssmt_sha2-256_w16_h40_d4", "xmssmt_sha2-256_w16_h40_d8",
				"case xmssmt_sha2-256_w16_h60_d3", "xmssmt_sha2-256_w16_h60_d6", "xmssmt_sha2-256_w16_h60_d12",
				"xmssmt_shake128_w16_h20_d2", "xmssmt_shake128_w16_h20_d4", "xmssmt_shake128_w16_h40_d2",
				"xmssmt_shake128_w16_h40_d4", "xmssmt_shake128_w16_h40_d8", "xmssmt_shake128_w16_h60_d3",
				"xmssmt_shake128_w16_h60_d6", "xmssmt_shake128_w16_h60_d12", "xmssmt_sha2-512_w16_h20_d2",
				"xmssmt_sha2-512_w16_h20_d4", "xmssmt_sha2-512_w16_h40_d2", "xmssmt_sha2-512_w16_h40_d4",
				"xmssmt_sha2-512_w16_h40_d8", "xmssmt_sha2-512_w16_h60_d3", "xmssmt_sha2-512_w16_h60_d6",
				"xmssmt_sha2-512_w16_h60_d12", "xmssmt_shake256_w16_h20_d2", "xmssmt_shake256_w16_h20_d4",
				"xmssmt_shake256_w16_h40_d2", "xmssmt_shake256_w16_h40_d4", "xmssmt_shake256_w16_h40_d8",
				"xmssmt_shake256_w16_h60_d3", "xmssmt_shake256_w16_h60_d6", "xmssmt_shake256_w16_h60_d12" };

//		 Display display = new Display();
	//	    Shell shell = new Shell(display);
	//	    shell.setLayout(new FillLayout());

		    // Create a dropdown Combo
		    Combo combo = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
		    readOnly.setItems(cipherSuites);
		    
		    shell.open();
		    while (!shell.isDisposed()) {
		      if (!display.readAndDispatch()) {
		        display.sleep();
		      }
		    }
		    display.dispose();
		
	}

	@Override
	public byte[] getSeed() {
		return seed;
	}

}
