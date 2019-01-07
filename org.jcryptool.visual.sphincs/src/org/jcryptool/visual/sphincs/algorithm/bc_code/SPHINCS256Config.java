package org.jcryptool.visual.sphincs.algorithm.bc_code;

public class SPHINCS256Config
{
	public static final int SUBTREE_HEIGHT = 5;
	public static final int TOTALTREE_HEIGHT = 60;
	public static final int N_LEVELS = (TOTALTREE_HEIGHT / SUBTREE_HEIGHT);
	public static final int SEED_BYTES = 32;

	public static final int SK_RAND_SEED_BYTES = 32;
	public static final int MESSAGE_HASH_SEED_BYTES = 32;

	public static final int HASH_BYTES = 32; // Has to be log(HORST_T)*HORST_K/8
	public static final int MSGHASH_BYTES = 64;

	public static final int CRYPTO_PUBLICKEYBYTES = ((Horst.N_MASKS + 1) * HASH_BYTES);
	public static final int CRYPTO_SECRETKEYBYTES = (SEED_BYTES + CRYPTO_PUBLICKEYBYTES - HASH_BYTES + SK_RAND_SEED_BYTES);
	public static final int CRYPTO_BYTES = (MESSAGE_HASH_SEED_BYTES + (TOTALTREE_HEIGHT + 7) / 8 + Horst.HORST_SIGBYTES + (TOTALTREE_HEIGHT / SUBTREE_HEIGHT) * Wots.WOTS_SIGBYTES + TOTALTREE_HEIGHT * HASH_BYTES);
}
