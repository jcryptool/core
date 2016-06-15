package org.jcryptool.visual.merkletree.ui;

/**
 * Holds constants for the plug-in to allow to change them in a systematical
 * fashion
 * 
 * @author Kevin Muehlboeck
 * @author <i>revised by</i>
 * @author Maximilian Lindpointner
 */
public abstract class MerkleConst {
	
	// height of the plug-in as a whole (this is important for the scrolling of
	// the plug-in)
	public static final int PLUGIN_HEIGTH = 600;
	
	// width of the plug-in as a whole (this is important for the scrolling of
	// the plug-in)
	public static final int PLUGIN_WIDTH = 400;
	
	// this is used for the layout of the plug-in and has to be even, because
	// the xor is placed at
	// HSPANLEFT / 2
	public static final int H_SPAN_LEFT = 6;
	
	// the layout of the plug-in is 3 to one -> 6 / 3
	public static final int H_SPAN_RIGHT = 2;
	
	// the span of the whole plug-in: left + right
	public static final int H_SPAN_MAIN = H_SPAN_LEFT + H_SPAN_RIGHT;
	
	// Height of the description of the plug-in in rows
	public static final int DESC_HEIGHT = 4;

	// enum to marke the aktual usesd suit
	public enum SUIT{
		XMSS, XMSS_MT, MSS
	}
}