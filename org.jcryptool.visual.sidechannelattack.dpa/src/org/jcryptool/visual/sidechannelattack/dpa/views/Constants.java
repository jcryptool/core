/**
 * This interface is used to define all Constants in Class DPAView.java.
 *
 * @author Biqiang Jiang
 *
 * @version 1.0, 01/09/09
 *
 * @since JDK1.5.7
 */

package org.jcryptool.visual.sidechannelattack.dpa.views;

import org.jcryptool.visual.sidechannelattack.DPAPlugIn;

public interface Constants {

    static String pluginRootDirectory = DPAPlugIn.getDefault().getBundle().getLocation().substring(16);

    String IMGADDRESSE_TEST_ENG = "icons/explanation_4.jpg";
    String IMGADDRESSE_DA_ENG = "icons/doubleAndAdd.jpg";
    String IMGADDRESSE_D_ENG = "icons/double2.jpg";
    String IMGADDRESSE_D_0_ENG = "icons/doubleAndAdd_0.jpg";

    String VIEW_ID = "org.jcryptool.visual.sidechannelattack.dpaview"; // 96-47
    String MAIN_GROUP_TITLE = "DPA against ECC";
    String ECC_ALG_GROUP_TITLE = "Information";
    String ECC_ALG_TEXT =
            "Note: the scalar multiplication operation of ECCPoints above the elliptic curve is normally realized with \"Double and Add\" algorithm, the following introduced algorithm is based on \"Double and Add Always\", which is proved to be secure to SPA but still insecure to DPA.\nThe concrete attack process will be given on the right side in visual form and after all the corresponding countermeasures. ";
    String EXPLANATION_OF_ALG = "Part 2. Explanation of Algorithm: ";
    // String UNSECURE_DOUBLE_ADD_TEXT =
    // "Insecurity of \"Double and Add\" to SPA:\nsince the private key can be recovered through comparison the difference of power traces between ECAdd and ECDouble operations, the original \"Double and Add\" algorithm is insecure to both SPA and DPA.\n\nInput:\n ECPoint P;\n scalar parameter k;\n\nOutput:\n Q = kP;\n\nProcess:\n Q = P;\n for(i = n-2 down to 0){\n Q = 2Q;\n if(di == 1)\n Q = Q + P; }";
    String UNSECURE_DOUBLE_ADD_ALWAYS_TEXT =
            "Note: the vulnerability to SPA attack can be solved by adding a dummy \"add\" operation in case of \"0\". It is named \"Double and Add Always\". However such countermeasure is still nonresistant to DPA attack, since the power consumption of \"Double and Add always\" method for each input looks same, but is slightly different. With statistic method like sampling analysis and differential analysis we can amplify the slight differences and recover the private key \'k\'.\n\n Q = kP with Double and Add Always:\n   Q[0] = P;\n   for(i = n-2 down to 0){\n   Q[0] = 2Q[0]; Q[1] = Q[0] + P;\n   Q[0] = Q[d\u1d62]; \n\n}";
    String RANDOMIZED_SCALAR_MULTIPLIER_TEXT =
            "Parameter k Randomization:\nif the scalar multiplier k is randomized in each turn of execution, the attacker could not recover the private key per comparison the differences between the power traces analysis of \"double and add\" operations.\n\n The idea and principle are like so:\nFirst Choose a random factor in group [1,p-1], then a new randomized scalar multiplier k'=k+r*order[P] will be calculated,\nsince order[P]*P is the infinity point(0) of the chosen elliptic curve, we get:\n G'=(k+r*order[P])*P=k*P+r*(oder[P]*P)\n    =kP+r*0=G\n With such method even for the same private key k, the power traces are still ensured to be different. The relation between certain input parameter K and corresponding power trace during operation process will be broken.";
    String DOUBLE_ADD_ALWAYS = "Double and Add always";
    String RANDOMIZED_INITIAL_POINT_TEXT =
            "Initial Point P Randomization:\nif the initial point P is randomized in each turn of execution, it is also difficult to recover the private key through analysis of the correlation between a certain kP and its power trace.\n\nBasic idea and principle:\nFirst we choose randomly another ECPoint R on the same EC which is different from the initial ECPoint. Then we calculate R+P = P' as the new initial ECPoint for EC cryptography. After \"Double and Add Always\" algorithm we get Q' = kP' = k( P + R ) = kP + kR = Q + kR as the result. \nAnd for an ECPoint satisfies S = (x,y) then -S = (x, -y), thus Q = Q' - kR = Q' + (-kR), with this method we can calculate the result Q but concealing the real initial point P, in this way the correlation between power traces and the initial input P is also concealed.";
    String RANDOMIZED_ISOMORPHIC_CURVE_TEXT =
            "Isomorphic Curve Randomization:\nwe use a random isomorphic curve to the original curve, and after \"Double and Add always\" we recover the original result Q from Q'. The random isomorphic curve could also be understand as a DPA countermeasure with the method of randomizing the initial point P.\n\nIdea and process:\nFirst we choose randomly a number r in prime field [1, p-1], and then we calculate a' = r\u2074a, b' = r\u2076b P' = (r\u00b2Xp, r\u00b3Yp). With new a', b', P' we get a new isomorphic curve: E': y\u00b2 = x\u00b3 + a'x + b' After that we compute Q' = kP' on the new isomorphic curve. Q' = (Xq', Yq'). Finally we recover Q =(r\u207b\u00b2Xq',r\u207b\u00b3Yq'). Actually the method \"isomorphic curve\" breaks the correlation between ECPoint and power traces of different operations through randomizing the initial point.";
    String COUTNERMEASURES_CCOMBO_RANDOMIZED_SCALAR_MULTIPLIER = "Randomize Parameter k";
    String COUNTERMEASURES_CCOMBO_RANDOMIZED_INITIAL_POINT = "Randomize initial ECPoint P";
    String COUNTERMEASURES_CCOMBO_RANDOMIZED_ISOMORPHIC_CURVE = "Random Isomorphic Curve";
    String TEXT_OF_PARAMETEROFCOUNTERMEASURESTEXT =
            "1. Q = KP at step j the processed point Q depends only on the first bits (kn-2,kn-3; : : : ; kj ) of K.\n\n2. Power consumption will be correlated to specific bit of Q, no correlation will be observed with a point not computed.\n\n3. The 2th most significant bit kn-2 of K can be recovered by computing the correlation between power consumption and any specific bit of the binary representation of 6P.\n\n 4. If kn-1 = 1, 6P is computed as Q[0] = 2*3P = 6P, otherwise by kn-1 = 0, 2*2P will be carried out during the Double operation.\n\n5. We gather many power consumption of computing 6P, and let si be any specific bit of 6P. We use the correlation function: g(t) = Power(si = 0) - Power(si = 1).\n\n6. If 6P is related to simulated correlation function g(t), a peak is observed corresponding to the computation of 6P, otherwise if there is no peak, the second significant bit is 0.\n\n7. The following bits kn-3, kn-4,... kj of K can be recursively recovered in the same way.\n\n8. The countermeasures against DPA are randomizing either the Initial Point P or the scalar multiplier K.";
    String INSECURE_ALG_LABEL_TEXT = "Double and Add Always";
    String TITLE_OF_PARAMETEROFCOUNTERMEASURESGROUP = "DPA against Double and Add:";
    String TOOLTIPTEXT_OF_PARAMETERACOMBO = "choose the parameter a of the EC here";
    String TOOLTIPTEXT_OF_PARAMETERBCOMBO = "choose the parameter b of the EC here";
    String TOOLTIPTEXT_OF_ORDEROFCURVETEXT =
            "order of the selected EC, that means the number of ECPoints on the curve plus the infinity point O";
    String TOOLTIPTEXT_OF_SCALARPARAMETERCOMBO = "select the scalar multiplier k for Q = kP here";
    String TOOLTIPTEXT_OF_ECPOINTSCOMBO = "choose an ECPoint P as initial Point of ECC, except the infinity point O ";
    String TEXT_OF_EXECUTEBUTTON = "Execute";
    String TOOLTIPTEXT_OF_ORDEROFECPOINTTEXT =
            "the order of selected initial point P, which means the minimum number k in GF(p) which satisfied kP = infinity point";
    String TOOLTIPTEXT_OF_PRIMEFIELDSELECTCOMBO = "choose the prime field GF(p) here";
    String TEXT_OF_PARAMETEROFCOUNTERMEASURESTEXT_1 =
            "1. Q = KP at step j the processed point Q depends only on the first bits (kn-2,kn-3; : : : ; kj ) of K.\n\n2. Power consumption will be correlated to specific bit of Q, no correlation will be observed with a point not computed.\n\n3. The 2th most significant bit kn-2 of K can be recovered by computing the correlation between power consumption and any specific bit of the binary representation of 6P.\n\n 4. If kn-1 = 1, 6P is computed as Q[0] = 2*3P = 6P, otherwise by kn-1 = 0, 2*2P will be carried out during the Double operation.\n\n5. We gather many power consumption of computing 6P, and let si be any specific bit of 6P. We use the correlation function: g(t) = Power(si = 0) - Power(si = 1)\n\n6. If 6P is related to simulated correlation function g(t), a peak is observed corresponding to the computation of 6P, otherwise if there is no peak, the second significant bit is 0.\n\n7. The following bits kn-3, kn-4,... kj of K can be recursively recovered in the same way.";
    String SECURE_ALG_LABEL_TEXT = "Secure countermeasures against SPA/DPA:";
    String TEXT_OF_GLABEL = "   Q = ";
    String TEXT_OF_ALABEL = "a =";
    String TEXT_OF_BLABEL = "b = ";
    String TOOLTIPTEXT_OF_ECCURVETEXT = "the EC has been selected with parameter a, b and GF(p)";
    String TEXT_OF_DOUBLE_FORMEL = "Q[0]' = 2Q[0]'";
    String TEXT_OF_ADD_FORMEL = "Q[1]' = Q[0]' + P";
    String TEXT_OF_K_IN_BINARYFORM = "k in binary form: ";
    String PARAM_OF_ECC_GROUP_TITEL = "Parameters";
    String PARAM_OF_COUNTERMEASURES_GROUP_TITEL = "Parameter of Countermeasures";
    String CALCULATION_TABLE_GROUP_TITEL = "Part 4. Double and Add (from left highest to right lowest valuable bit)";
    String FIRST_COLUMN_IN_TABLE = "Round Counter(left to right)";
    String SECOND_COLUMN_IN_TABLE = "Result after Double";
    String THIRD_COLUMN_IN_TABLE = "Result after Add";
    String CHOOSE_AN_ECPOINT_LABEL_TEXT = "Choose a Point (no infinity point):";
    String PRIME_FIELD_LABEL_TEXT = "GF(p) =";
    String ECCURVE_TEXT_PART1 = "y\u00b2 = x\u00b3 + ";
    String ECCURVE_TEXT_PART2 = "x + ";
    String ECCURVE_TEXT_PART3 = ", GF(";
    String CUE_LABEL_TEXT_1 = "a, b must be set in GF(p).";
    String CUE_LABEL_TEXT_2 = "4a\u00b3 + 27b\u00b2 = 0. Change please.";
    String CUE_LABEL_TEXT_3 = "Give correct number please.";
    String CUE_LABEL_TEXT_4 = "a, b must be set in GF(p).";
    String CUE_LABEL_TEXT_5 = "4a\u00b3 + 27b\u00b2 = 0. Change please.";
    String ORDER_OF_SELECTED_POINT_TEXT = "Order of selected ECPoint: ";
    String ORDER_OF_CURVE_TEXT = "Order of Curve: ";
    String INITIAL_TABLE_ITEM_PART_1 = "ECPoint(P): ";
    String INITIAL_TABLE_ITEM_PART_2 = "Parameter k: ";
    String DECIMAL_ABBR = " (dec.)";
    String INITIAL_TABLE_ITEM_BINARY = "Binary: ";
    String BINARY_ABBR = " (bin.)";
    String INITIAL_TABLE_ITEM_PROCESS = "Process: ";
    String INITIAL_TABLE_ITEM_INPUT = "Input:";
    String INITIAL_TABLE_ITEM_DOUBLE = "Q[0] = 2Q[0]";
    String INITIAL_TABLE_ITEM_ADD = "Q[1] = Q[0] + P";
    String RANDOMIZED_K_TEXT_PART1 = "1. Countermeasure against DPA:\n   (Parameter k Randomization)\n\n";
    String RANDOMIZED_K_TEXT_PART2 = "a).Original Input:\n   Elliptic Curve: y\u00b2 = x\u00b3 + ";
    String RANDOMIZED_K_TEXT_PART3 = "\n   GF(";
    String RANDOMIZED_K_TEXT_PART4 = "), a = ";
    String RANDOMIZED_K_TEXT_PART5 = ", b = ";
    String RANDOMIZED_K_TEXT_PART6 = "\n   Initial ECPoint P: ";
    String RANDOMIZED_K_TEXT_PART7 = "\n   Parameter k: ";
    String RANDOMIZED_K_TEXT_PART8 = "\n   k in Binary: ";
    String RANDOMIZED_K_TEXT_PART9 = "\n\nb).Randomization:\n   Random Factor r: ";
    String RANDOMIZED_K_TEXT_PART10 = "\n   Order of P o(P): ";
    String RANDOMIZED_K_TEXT_PART11 = "\n   k' = k + o(P)*r = ";
    String RANDOMIZED_K_TEXT_PART12 = "\n   k' in Binary: ";
    String RANDOMIZED_ECPOINT_TEXT_PART1 = "Random ECPoint R: (";
    String RANDOMIZED_ECPOINT_TEXT_PART2 = "S = k*R = ";
    String RANDOMIZED_ECPOINT_TEXT_PART3 = "R = ";
    String RANDOMIZED_ECPOINT_TEXT_PART4 = "2. Countermeasure against DPA:\n   (Initial Point P Randomization)\n\n";
    String RANDOMIZED_ECPOINT_TEXT_PART5 = "a).Original Input:\n   Elliptic Curve: y\u00b2 = x\u00b3 + ";
    String RANDOMIZED_ECPOINT_TEXT_PART6 = "x + ";
    String RANDOMIZED_ECPOINT_TEXT_PART7 = "\n   GF(";
    String RANDOMIZED_ECPOINT_TEXT_PART8 = "), a = ";
    String RANODMIZED_ECPOINT_TEXT_PART9 = ", b = ";
    String RANODMIZED_ECPOINT_TEXT_PART10 = "\n   Initial ECPoint P: ";
    String RANODMIZED_ECPOINT_TEXT_PART11 = "\n   Parameter k: ";
    String RANODMIZED_ECPOINT_TEXT_PART12 = "\n   k in Binary: ";
    String RANODMIZED_ECPOINT_TEXT_PART13 = "\n\nb).Randomization:\n   ";
    String RANODMIZED_ECPOINT_TEXT_PART14 = "Random ECPoint R: (";
    String RANODMIZED_ECPOINT_TEXT_PART15 = "\n   P + R = ";
    String RANODMIZED_ECPOINT_TEXT_PART16 = "\n   S = k*R = ";
    String RANODMIZED_ECPOINT_TEXT_PART17 = "R = ";
    String RANODMIZED_ECPOINT_TEXT_PART18 = "\n   Q' = k*(P+R) = Q + S";
    String RANODMIZED_ECPOINT_TEXT_PART19 = "\n   if S = (x, y) then -S = (x, -y)";
    String RANODMIZED_ECPOINT_TEXT_PART20 = "\n   Q = Q' - S = Q' + (-S)";
    String RANDOMIZED_ISOMORPHIC_TEXT_PART1 = "3. Countermeasure against DPA:\n   (Isomorphic Curve Random.)\n\n";
    String RANDOMIZED_ISOMORPHIC_TEXT_PART2 = "a).Original Input:\n   Elliptic Curve: y\u00b2 = x\u00b3 + ";
    String RANDOMIZED_ISOMORPHIC_TEXT_PART3 = "x + ";
    String RANDOMIZED_ISOMORPHIC_TEXT_PART4 = "\n   GF(";
    String RANDOMIZED_ISOMORPHIC_TEXT_PART5 = "), a = ";
    String RANDOMIZED_ISOMORPHIC_TEXT_PART6 = ", b = ";
    String RANDOMIZED_ISOMORPHIC_TEXT_PART7 = "\n   Initial ECPoint P: ";
    String RANDOMIZED_ISOMORPHIC_TEXT_PART8 = "\n   Parameter k: ";
    String RANDOMIZED_ISOMORPHIC_TEXT_PART9 = "\n   k in Binary: ";
    String RANDOMIZED_ISOMORPHIC_TEXT_PART10 = "\n\nb).Randomization:\n   ";
    String RANDOMIZED_ISOMORPHIC_TEXT_PART11 = "Random Factor r = ";
    String RANDOMIZED_ISOMORPHIC_TEXT_PART12 = "\n   a' = a*r\u2074 = ";
    String RANDOMIZED_ISOMORPHIC_TEXT_PART13 = ", b' = b*r\u2076 = ";
    String RANDOMIZED_ISOMORPHIC_TEXT_PART14 = "\n   P' = (r\u00b2*Xp, r\u00b3*Yp) = (";
    String RANDOMIZED_ISOMORPHIC_TEXT_PART15 = "\n   EC': y\u00b2 = x\u00b3 + a'x + b' ";
    String RANDOMIZED_ISOMORPHIC_TEXT_PART16 = "\n            = x\u00b3 + ";
    String RANDOMIZED_ISOMORPHIC_TEXT_PART17 = "x + ";
    String INITIAL_TABLE_ITEM_HIGHEST_BIT = ". highest bit = ";
    String TABLE_ITEM_DOUBLE = "Q = 2Q = ";
    String TABLE_ITEM_P_EQUALS = "P = (";
    String TABLE_ITEM_ADD = "Q = Q + P = ";
    String TABLE_ITEM_WRONG_POINT_TEXT1 =
            "The denominator 2Y\u2081 in ECDBL Formula is \"0\", please choose another point.";
    String TABLE_ITEM_Q0_DOUBLE = "Q[0] = 2Q[0] = ";
    String INITIALTABLEITEM_INPUT = "Input:";
    String INITIALTABLEITEM_DOUBLE = "Q = 2Q";
    String INITIALTABLEITEM_ADD = "Q = Q + P";
    String TABLE_ITEM_Q1_DOUBLE = "Q[1]  = Q[0] + P = ";
    String TABLE_ITEM_Q0_DOUBLE_NEW = "Q[0]' = 2Q[0]' = ";
    String TABLE_ITEM_P_PLUS_R = "(P + R) = (";
    String TABLE_ITEM_Q1_ADD_NEW1 = "Q[1]'  = Q[0]' + (P + R) = ";
    String TABLE_ITEM_P_NEW_EQUALS = "P' = (";
    String TABLE_ITEM_Q1_ADD_NEW2 = "Q[1]'  = Q[0]' + P' = ";
    String TABLE_ITEM_OUTPUT = "Output:";
    String TABLE_ITEM_NEW_G_PART1 = "Q' = k*(P+R) = Q + S = ";
    String TABLE_ITEM_NEW_G_PART2 = "S = (x, y) then -S = (x, -y) = ";
    String TABLE_ITEM_NEW_G_PART3 = "Q = Q' - S = Q' + (-S)";
    String TABLE_ITEM_Q_EQUALS = "Q = (";
    String TABLE_ITEM_NEW_Q = "Q' = (Xq*r\u00b2,Yq*r\u00b3) = ";
    String UNICODE_1 = "\u0304";
    String UNICODE_2 = "\u00b2"; // superscript 2
    String UNICODE_3 = "\u00b3"; // superscript 3
    String UNICODE_5 = "\u2081";
    String UNICODE_6 = "\u2074";
    String UNICODE_7 = "\u2076";
    String UNICODE_8 = "\u2082";
    String UNICODE_9 = "\u1d62"; // subscript i
    String UNICODE_10 = "\u2074"; // superscript 4
    String UNICODE_11 = "\u2076"; // superscript 6
    String UNICODE_12 = "\u207b"; // superscript -

    String TABLE_ITEM_Xq_EQUALS = "Xq = ";
    String TABLE_ITEM_Yq_EQUALS = "Yq = ";
    String DPA_PLUGIN_ID = "org.jcryptool.visual.sidechannelattack.dpa.dpaview";

}
