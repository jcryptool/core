/**
 * This interface is used to define all Constants in Class SPAView.java.

 * @author  Biqiang Jiang

 * @version 1.0, 01/09/09

 * @since   JDK1.5.7

 */

package org.jcryptool.visual.sidechannelattack.spa.views;

import org.jcryptool.visual.sidechannelattack.SPAPlugIn;

public interface Constants {
static String pluginRootDirectory = SPAPlugIn.getDefault().getBundle().getLocation().substring(16);

	public final static String IMGADDRESSE_SQ_ENG = "icons/sam_Sq.jpg";
	public final static String IMGADDRESSE_SQMUL_0_ENG = "icons/sam_SqMul_0.jpg";
	public final static String IMGADRESSE_Y_ACHSE_ENG = "icons/achse_y.jpg";
	public final static String IMGADDRESSE_X_ACHSE_ENG = "icons/achse_x.jpg";
	public final static String IMGADDRESSE_SQMUL_ENG = "icons/sam_SqMul.jpg";

	public final static String INPUT_BOLD_ENG = "\n Input: \n";
	public final static String INPUT_ENG = "   ciphertext = c;\r\n   private key = d;\r\n   d in binary form = b with m+1 digits;\r\n   (digit m: first significant bit; digit 0: last bit)\r\n   modul = p*q = n;\r\n\r\n";
	public final static String OUTPUT_BOLD_ENG = " Output: \n";
	public final static String OUTPUT_ENG = "   Res = cd mod n;\r\n\r\n";
	public final static String PROCESS_BOLD_ENG = " Process: \n";
	public final static String PROCESS_ENG = "   int Res = 1;  //initial value: Res\r\n   for(int i = m; i>-1; i--)  {\r\n   Res = Res2 mod n;              //\"Square\"\r\n   if (bi == 1)  {\r\n   \tRes = (Res * c) mod n;       //\"Multiply\"\r\n   } \r\n   else  {\r\n   //do nothing; } }";
	public final static String NOTE_RSA_VULNERABILITY = " Note: the private key could be recovered\n through power trace analysis of the difference\n between two branches of condition statement\n (red marked \"if statement\"). The concrete\n process will be shown in visualized form on\n the right side. Following a SPA-resistant algo-\n rithm will be given in the last part.\n";

	public final static String DEC_ENG = " (dec.)";
	public final static String BIN_ENG = " (bin.)";
	public final static String CUE_LABEL_BAISIS_ENG = "Wrong! Give Ciphertext please.";
	public final static String CUE_LABEL_EXP_ENG = "Wrong! Give private key please.";
	public final static String CUE_LABEL_MOD_ENG = "Wrong! Choose P and Q please.";
	public final static String CUE_LABEL_NaN_ENG = "Wrong! Give correct number please.";
	public final static String CUE_INF_OUT_OF_BOUND = "Wrong! The given c is out of bound.";
	public final static String HIGHEST_BIT_ENG = ". highest bit = ";
	public final static String FINAL_RESULT_ENG = "Final Result: ";
	public final static String INPUT_BASIS_ENG = "Input: ";
	public final static String INPUT_RES_1_ENG = ";  Res: 1;";
	public final static String RES_AFTER_SQUARE_ENG = "Res = Res\u00b2 mod n";
	public final static String RES_AFTER_MUL_ENG = "Res = Res * c mod n";
	public final static String EXP_ENG = "  private key(d): ";
	public final static String MODUL_ENG = "Modul n = p*q = ";
	public final static String RES_EQUAL_ENG = "Res = ";
	public final static String HOCH_2_MOD_ENG = "\u00b2 mod ";
	public final static String RSA_PROCESS_TEXT = " Note: the exponentiation operation in encryption\n and decryption functions (yellow marked) is\n normally realized with Square and Multiply\n algorithm, which is proved vulnerable to SPA.\n\n 1. Choose two distinct prime numbers p and q.\n\n 2. Compute the modulus n = p*q.\n\n 3. Compute the totient: phi(n) = (p-1)*(q-1).\n\n 4. Choose a public key 'e' coprime with phi(n).\n\n 5. Determine private key 'd': d*e \u2261 1 mod {phi(n)}.\n\n 6. Encryption function: c \u2261 me mod{n}.\n\n 7. Decryption function: m \u2261 cd mod{n}.";
	public final static String RSA_ALG_GROUP_TITLE = "Part 1. RSA Algorithm:";
	public final static String MAIN_GROUP_TITLE = "SPA against RSA";
	public final static String SQUARE_MULTI_ALG_GROUP_TITLE = "Part 2. Square and Multiply Algorithm: ";
	public final static String PARAM_OF_RSA_GROUP_TITLE ="Part 3. Parameter of RSA";
	public final static String BASIS_LABEL = "Ciphertext c:";
	public final static String EXPONENT_LABEL = "Private Key d:";
	public final static String CHOOSE_Q_LABEL = "Choose Q:";
	public final static String CHOOSE_P_LABEL = "Choose P:";
	public final static String CUE_LABEL_TIP_1 = "Choose different P and Q please!";
	public final static String POWER_TRACE_VISUAL_GROUP_TITLE = "Part 6. Visualized Power Traces";
	public final static String CALCULATION_TABLE_GROUP_TITLE = "Part 5. Square and Multiply (from left highest to right lowest valuable bit)";
	public final static String FIRST_COLUMN_IN_TABLE = "Round Counter(left to right)";
	public final static String SECOND_COLUMN_IN_TABLE = "Result after Square";
	public final static String THIRD_COLUMN_IN_TABLE = "Result after Multiplication";

	public final static String INITIAL_ITEM_TEXT_1_IN_TABLE = "  Ciphertext(c): ";
	public final static String INITIAL_ITEM_TEXT_2_IN_TABLE = "  Binary: ";
	public final static String INITIAL_ITEM_TEXT_3_IN_TABLE = "Process:";
	public final static String OUTPUT_TABLE_ITEM_TEXT = "Output:";
	public final static String INDICATION_GROUP_SQUARE_MLUTI_TITLE = "Indication(Referring to \"Square and Multiply\")";
	public final static String INDICATION_GROUP_SQUARE_MULTI_ALWAYS_TITLE = "Indication(Referring to \"Square and Multiply Always\")";
	public final static String INDICATION_OF_VULNERABILITY_TEXT = "M: the trace of multiplication operation in Square and Multiply.\nS: the trace of squaring operation in Square and Multiply.\n\nNote: If the current digit is a '0', only a squaring operation will be carried out, while the current digit is a '1', besides the squaring a multiplication operation will be executed follows the squaring. Obviously shown in the diagram above, the power traces between squaring and multiplication operations are quite different. In the decryption process according to the power traces we can distinguish every bit of private key is a binary '0' or a '1'.\nIf the whole secret key digits can be read from the power traces, the RSA is vulnerable. Of course, it should be mentioned that in fact the secret key of RSA is at least 1024 bit long and to ensure the security, it is recommended that n be at least 2048 bits long. However, no matter how long the secret key is, if the power traces of some key operations are obviously distinct, the RSA based cryptograhic system is insecure and weak.";
	public final static String INSECURE_EXECUTION_BUTTON_TEXT = "Insecure execute";
	public final static String CLEAR_BUTTON_TEXT = "Clear ";
	public final static String MODULE_LABEL_TEXT = "Module n:";

	public final static String INITIAL_TABLE_ITEM_SQUARE = "Res_\u2080 = Res\u00b2 mod n";
	public final static String INITIAL_TABLE_ITEM_MULTIPLY = "Res_\u2081 = Res_\u2080 * c mod n";
	public final static String RES_SQUARE_MULTI_ALWAYS_SQUARE = "Res_\u2080 = ";
	public final static String RES_SQUARE_MULTI_ALWAYS_MULTI = "Res_\u2081 = ";
	public final static String INDICATION_SQUARE_MULTI_ALWAYS = "M: the trace of multiplication operation in Square and Multiply.\nS: the trace of squaring operation in Square and Multiply.\n\nNote: As the algorithm \"Square and Multiply Always\" shown above, the squaring and multiplication operations will be constantly executed in each loop, so it is difficult to read out the private key with SPA.\r\nAvoiding these types of conditional statements when implementing these algorithms can eliminate many SPA weaknesses. In algorithms which inherently assume this type of key dependent branching, it may not be possible to remove these statements completely. However, operations with large power characteristics ( e.g. multiplications ) can be moved outside of conditional branches to decrease the size of SPA characteristics. This strategy can be applied to the square-and-multiply algorithm as shown above. \r\nTechniques for resisting power analysis can be implemented at both the hardware and software levels. Countermeasures at the software level seem to be more desirable, from a commercial standpoint at least, since they can be implemented on existing architectures. However countermeasures in software level are always based on sacrifice of the execution performance and the algorithm efficiency. The Hardware countermeasures are generally more costly to implement, but they may be necessary depending on the required level of security.";
	public final static String SQUARE_MULTI_EXECUTION_BUTTON_TEXT = "Secure execute";
	public final static String COUNTERMEASURES_GROUP_TITLE = "Part 4. Countermeasure";
	public final static String SQUARE_MULTI_ALWAYS_ALG_TEXT = "Note: in the modified algorithm \"Square and Multiply Always\", no matter the current bit is '0' or '1', the multiplication operation will be always executed. So it is impossible to recover the private key through comparison of the difference between power traces. So this \"Square and Multiply Always\" algorithm is resistant to SPA. \n\n Process:\n    int Res = 1;  //Initial value: Res\r\n    for(int i = m; i>-1; i--)  {\r\n   Res_0 = Res2 mod n;       //\"Square\"\r\n   Res_1 = Res_0 * c mod n;  //\"Multiply\"\r\n    Res = Res_(bi)}";

	public final static String TOOL_TIP_TEXT_EXPONENT = "choose a number as private key d here";
	public final static String TOOL_TIP_TEXT_BASIS = "give an integer number in field [-2^31, 2^31-1] as ciphertext";
	public final static String TOOL_TIP_TEXT_INSECURE_EXECUTEBUTTON = "\"Square and Multiply\" is insecure to SPA";
	public final static String TOOL_TIP_TEXT_SECURE_EXECUTEBUTTON = "\"Square and Multiply Alway\" is secure to SPA";
	public final static String TOOL_TIP_TEXT_CLEARBUTTON = "Click this button to clear input frames and left table.";
	public final static String TOOL_TIP_TEXT_RESULT_LABEL = "Plaintext: Result R = c\u1d48(mod n)";
	public final static String TOOL_TIP_TEXT_RESULT = "the result R is the decrypted plaintext";
	public final static String TOOL_TIP_TEXT_Q_SELECTION = "determine the prime Q here";
	public final static String TOOL_TIP_TEXT_P_SELECTION = "determine the prime P here";
	public final static String TOOL_TIP_TEXT_MODULE_N = "the module n here is the result of P*Q";


    public final static String UNICODE_1 = "\u00b2";
    public final static String UNICODE_2 = "\u00b3";
    public final static String UNICODE_3 = "\u2081";
    public final static String UNICODE_4 = "\u2080";

}
