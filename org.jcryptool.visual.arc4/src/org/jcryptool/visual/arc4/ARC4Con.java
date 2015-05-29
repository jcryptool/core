package org.jcryptool.visual.arc4;

import org.eclipse.swt.graphics.Color;
import org.jcryptool.core.util.colors.ColorService;

/**
 * Holds constants for the plug-in to allow to change them in a systematical fashion
 * 
 * @author Luca Rupp
 */
public abstract class ARC4Con {

    // Identifies a key data vector, as well as a wizard for key changing
    public static final int KEY = 1;

    // Identifies a ciphertext data vector
    public static final int ENC = 2;

    // Identifies a plain text data vector, as well as a wizard for changing the plain text
    public static final int PLAIN = 3;

    // Identifies a pseudo random data vector
    public static final int RAND = 4;

    // The color of row and column names
    public static final Color VH_COLOR = ColorService.GRAY;

    // optional prefix for hexadecimal strings e.g. "0x"
    public static final String HEX_MARK = "";

    // width of the labels of the byte vector
    public static final int VECTOR_VISUAL_LABEL_WIDTH = 40;

    // height of the labels of the byte vector
    public static final int VECTOR_VISUAL_LABEL_HEIGHT = 20;

    // number of bytes of the datavector
    public static final int DATAVECTOR_VISUAL_LENGTH = 16;

    // height of labels and buttons of the datavector (except those labels which display actual
    // data)
    public static final int DATAVECTOR_VISUAL_L_B_HEIGHT = 30;

    // width of labels and buttons of the datavector (except those labels which display actual data)
    public static final int DATAVECTOR_VISUAL_L_B_WIDTH = 140;

    // height of the plug-in as a whole (this is important for the scrolling of the plug-in)
    public static final int PLUGIN_HEIGTH = 1100;

    // width of the plug-in as a whole (this is important for the scrolling of the plug-in)
    public static final int PLUGIN_WIDTH = 1800;

    // height of one row in the main layout of the plug-in
    public static final int PLUGIN_ROW_HEIGHT = 70;

    // the relative path to the XOR image
    public static final String PATH_TO_XOR_IMAGE = "/icons/xor.png";
    
    // the relative path to the arrow image
    public static final String PATH_TO_ARROW_IMAGE = "/icons/arrow.png";

    // the color which is used to highlight changes
    public static final Color VECTOR_HIGHLIGHT = ColorService.GREEN;

    // this is used for the layout of the plug-in and has to be even, because the xor is placed at
    // HSPANLEFT / 2
    public static final int H_SPAN_LEFT = 6;

    // the layout of the plug-in is 3 to one -> 6 / 3
    public static final int H_SPAN_RIGHT = 2;

    // the span of the whole plug-in: left + right
    public static final int H_SPAN_MAIN = H_SPAN_LEFT + H_SPAN_RIGHT;

    // default number of steps to perform
    public static final int DEFAULT_STEPS = 1;

    // the minimum width for the input field in instruction visual
    public static final int INST_MIN_IN_WIDTH = 50;

    // Number of elements in the s-box
    public static final int S_BOX_LEN = 256;
    
    // Uses in the algorithm as modulus
    public static final int TWO_FIFE_SIX = 256;

    // Height of the s-box in rows
    public static final int S_BOX_HEIGTH = 4;

    // number of columns in the variable display section of the GUI
    public static final int VAR_COL = 3;

    // Height of the description of the plug-in in rows
    public static final int DESC_HEIGHT = 4;
    
    // Default value of Spritz parameter w; offset one in the list is equal to the value three
    public static final int DEFAULT_W_INDEX = 1;
    
    // Default value of Spritz parameter w; ATTENTION there is a dependency between 
    // this variable and DEFAULT_W_INDEX: THEY HAVE TO BE THE SAME!
    // These really shouldn't be two separate variables, but checkstyle wown't shut up otherwise
    public static final int DEFAULT_W_VALUE = 3;
    
    // number of characters in 16 byte hexadecimal
    public static final int HEXLEN = 32;

}