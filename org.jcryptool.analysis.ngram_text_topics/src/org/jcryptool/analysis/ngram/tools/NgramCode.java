package org.jcryptool.analysis.ngram.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jcryptool.analysis.ngram.views.NgramView;

public class NgramCode {
	public static final String ID = "org.jcryptool.analysis.ngram.tools.NgramCode";

	public NgramCode() {
	}

	public void NgramCalculate(NgramView view) {
		String result1 = "";
		String result2 = "";

		String[][] ngramArr;

		ngramArr = new String[30][51];

		// LITERATURE TEXT DEUTSCH ENGLISH
		ngramArr[2][1] = "EN";
		ngramArr[3][1] = "ICH";
		ngramArr[12][1] = "TH";
		ngramArr[13][1] = "THE";
		ngramArr[2][2] = "ER";
		ngramArr[3][2] = "EIN";
		ngramArr[12][2] = "HE";
		ngramArr[13][2] = "AND";
		ngramArr[2][3] = "CH";
		ngramArr[3][3] = "SCH";
		ngramArr[12][3] = "AN";
		ngramArr[13][3] = "ING";
		ngramArr[2][4] = "EI";
		ngramArr[3][4] = "DER";
		ngramArr[12][4] = "IN";
		ngramArr[13][4] = "HER";
		ngramArr[2][5] = "TE";
		ngramArr[3][5] = "CHT";
		ngramArr[12][5] = "ER";
		ngramArr[13][5] = "ERE";
		ngramArr[2][6] = "ND";
		ngramArr[3][6] = "UND";
		ngramArr[12][6] = "ND";
		ngramArr[13][6] = "THA";
		ngramArr[2][7] = "DE";
		ngramArr[3][7] = "NDE";
		ngramArr[12][7] = "ED";
		ngramArr[13][7] = "DTH";
		ngramArr[2][8] = "IN";
		ngramArr[3][8] = "CHE";
		ngramArr[12][8] = "RE";
		ngramArr[13][8] = "NTH";
		ngramArr[2][9] = "IE";
		ngramArr[3][9] = "INE";
		ngramArr[12][9] = "ES";
		ngramArr[13][9] = "HIS";
		ngramArr[2][10] = "ES";
		ngramArr[3][10] = "DIE";
		ngramArr[12][10] = "EA";
		ngramArr[13][10] = "WAS";
		ngramArr[2][11] = "IC";
		ngramArr[3][11] = "END";
		ngramArr[12][11] = "EN";
		ngramArr[13][11] = "HES";
		ngramArr[2][12] = "GE";
		ngramArr[3][12] = "DEN";
		ngramArr[12][12] = "ST";
		ngramArr[13][12] = "ETH";
		ngramArr[2][13] = "NE";
		ngramArr[3][13] = "GEN";
		ngramArr[12][13] = "HA";
		ngramArr[13][13] = "INT";
		ngramArr[2][14] = "UN";
		ngramArr[3][14] = "TEN";
		ngramArr[12][14] = "AT";
		ngramArr[13][14] = "HAT";
		ngramArr[2][15] = "ST";
		ngramArr[3][15] = "ENS";
		ngramArr[12][15] = "NG";
		ngramArr[13][15] = "ITH";
		ngramArr[2][16] = "SE";
		ngramArr[3][16] = "DAS";
		ngramArr[12][16] = "ON";
		ngramArr[13][16] = "OTH";
		ngramArr[2][17] = "HE";
		ngramArr[3][17] = "ERS";
		ngramArr[12][17] = "HI";
		ngramArr[13][17] = "ENT";
		ngramArr[2][18] = "BE";
		ngramArr[3][18] = "SIE";
		ngramArr[12][18] = "NT";
		ngramArr[13][18] = "FOR";
		ngramArr[2][19] = "AN";
		ngramArr[3][19] = "STE";
		ngramArr[12][19] = "TO";
		ngramArr[13][19] = "SHE";
		ngramArr[2][20] = "RE";
		ngramArr[3][20] = "EBE";
		ngramArr[12][20] = "IT";
		ngramArr[13][20] = "WIT";
		ngramArr[2][21] = "SI";
		ngramArr[3][21] = "NIC";
		ngramArr[12][21] = "AS";
		ngramArr[13][21] = "TTH";
		ngramArr[2][22] = "AU";
		ngramArr[3][22] = "TER";
		ngramArr[12][22] = "OU";
		ngramArr[13][22] = "THI";
		ngramArr[2][23] = "SC";
		ngramArr[3][23] = "HEN";
		ngramArr[12][23] = "ET";
		ngramArr[13][23] = "OFT";
		ngramArr[2][24] = "DI";
		ngramArr[3][24] = "ACH";
		ngramArr[12][24] = "OF";
		ngramArr[13][24] = "NDT";
		ngramArr[2][25] = "NS";
		ngramArr[3][25] = "ERE";
		ngramArr[12][25] = "IS";
		ngramArr[13][25] = "EDT";
		ngramArr[2][26] = "SS";
		ngramArr[3][26] = "LIC";
		ngramArr[12][26] = "TE";
		ngramArr[13][26] = "FTH";
		ngramArr[2][27] = "UE";
		ngramArr[3][27] = "BER";
		ngramArr[12][27] = "LE";
		ngramArr[13][27] = "TER";
		ngramArr[2][28] = "AS";
		ngramArr[3][28] = "ERD";
		ngramArr[12][28] = "OR";
		ngramArr[13][28] = "GHT";
		ngramArr[2][29] = "LE";
		ngramArr[3][29] = "NEN";
		ngramArr[12][29] = "AR";
		ngramArr[13][29] = "REA";
		ngramArr[2][30] = "HT";
		ngramArr[3][30] = "SSE";
		ngramArr[12][30] = "ME";
		ngramArr[13][30] = "EST";
		ngramArr[2][31] = "DA";
		ngramArr[3][31] = "NGE";
		ngramArr[12][31] = "TI";
		ngramArr[13][31] = "MAN";
		ngramArr[2][32] = "HA";
		ngramArr[3][32] = "SIC";
		ngramArr[12][32] = "NE";
		ngramArr[13][32] = "VER";
		ngramArr[2][33] = "EL";
		ngramArr[3][33] = "IES";
		ngramArr[12][33] = "WA";
		ngramArr[13][33] = "EDA";
		ngramArr[2][34] = "NI";
		ngramArr[3][34] = "ASS";
		ngramArr[12][34] = "SH";
		ngramArr[13][34] = "HEW";
		ngramArr[2][35] = "RA";
		ngramArr[3][35] = "SEI";
		ngramArr[12][35] = "RO";
		ngramArr[13][35] = "ALL";
		ngramArr[2][36] = "EH";
		ngramArr[3][36] = "IST";
		ngramArr[12][36] = "SE";
		ngramArr[13][36] = "STH";
		ngramArr[2][37] = "IS";
		ngramArr[3][37] = "SEN";
		ngramArr[12][37] = "VE";
		ngramArr[13][37] = "SAN";
		ngramArr[2][38] = "EM";
		ngramArr[3][38] = "BEN";
		ngramArr[12][38] = "NO";
		ngramArr[13][38] = "EAN";
		ngramArr[2][39] = "ME";
		ngramArr[3][39] = "AND";
		ngramArr[12][39] = "SA";
		ngramArr[13][39] = "HEN";
		ngramArr[2][40] = "AL";
		ngramArr[3][40] = "NUN";
		ngramArr[12][40] = "OT";
		ngramArr[13][40] = "RTH";
		ngramArr[2][41] = "NG";
		ngramArr[3][41] = "LLE";
		ngramArr[12][41] = "DE";
		ngramArr[13][41] = "HAD";
		ngramArr[2][42] = "LI";
		ngramArr[3][42] = "IND";
		ngramArr[12][42] = "AL";
		ngramArr[13][42] = "NOT";
		ngramArr[2][43] = "NA";
		ngramArr[3][43] = "REI";
		ngramArr[12][43] = "DT";
		ngramArr[13][43] = "AST";
		ngramArr[2][44] = "NN";
		ngramArr[3][44] = "ENW";
		ngramArr[12][44] = "RA";
		ngramArr[13][44] = "ONE";
		ngramArr[2][45] = "AR";
		ngramArr[3][45] = "ENA";
		ngramArr[12][45] = "EL";
		ngramArr[13][45] = "HEM";
		ngramArr[2][46] = "IT";
		ngramArr[3][46] = "TTE";
		ngramArr[12][46] = "LL";
		ngramArr[13][46] = "EAR";
		ngramArr[2][47] = "ET";
		ngramArr[3][47] = "VER";
		ngramArr[12][47] = "EW";
		ngramArr[13][47] = "ERS";
		ngramArr[2][48] = "WE";
		ngramArr[3][48] = "AUF";
		ngramArr[12][48] = "TT";
		ngramArr[13][48] = "HED";
		ngramArr[2][49] = "ED";
		ngramArr[3][49] = "AUS";
		ngramArr[12][49] = "TA";
		ngramArr[13][49] = "ESS";
		ngramArr[2][50] = "WA";
		ngramArr[3][50] = "RDE";
		ngramArr[12][50] = "LI";
		ngramArr[13][50] = "EAT";

		// GAME RULES TEXT DEUTSCH ENGLISH
		ngramArr[4][1] = "ER";
		ngramArr[5][1] = "IEL";
		ngramArr[14][1] = "TH";
		ngramArr[15][1] = "THE";
		ngramArr[4][2] = "EN";
		ngramArr[5][2] = "PIE";
		ngramArr[14][2] = "HE";
		ngramArr[15][2] = "PLA";
		ngramArr[4][3] = "IE";
		ngramArr[5][3] = "SPI";
		ngramArr[14][3] = "ER";
		ngramArr[15][3] = "LAY";
		ngramArr[4][4] = "DE";
		ngramArr[5][4] = "EIN";
		ngramArr[14][4] = "IN";
		ngramArr[15][4] = "ARD";
		ngramArr[4][5] = "EI";
		ngramArr[5][5] = "DER";
		ngramArr[14][5] = "AR";
		ngramArr[15][5] = "CAR";
		ngramArr[4][6] = "TE";
		ngramArr[5][6] = "DIE";
		ngramArr[14][6] = "LA";
		ngramArr[15][6] = "AYE";
		ngramArr[4][7] = "EL";
		ngramArr[5][7] = "ELE";
		ngramArr[14][7] = "AN";
		ngramArr[15][7] = "ING";
		ngramArr[4][8] = "IN";
		ngramArr[5][8] = "NDE";
		ngramArr[14][8] = "RE";
		ngramArr[15][8] = "AND";
		ngramArr[4][9] = "ND";
		ngramArr[5][9] = "INE";
		ngramArr[14][9] = "ON";
		ngramArr[15][9] = "YER";
		ngramArr[4][10] = "CH";
		ngramArr[5][10] = "TEN";
		ngramArr[14][10] = "ST";
		ngramArr[15][10] = "ERS";
		ngramArr[4][11] = "ES";
		ngramArr[5][11] = "RTE";
		ngramArr[14][11] = "NT";
		ngramArr[15][11] = "ARE";
		ngramArr[4][12] = "SP";
		ngramArr[5][12] = "ERD";
		ngramArr[14][12] = "ES";
		ngramArr[15][12] = "HER";
		ngramArr[4][13] = "NE";
		ngramArr[5][13] = "DEN";
		ngramArr[14][13] = "EA";
		ngramArr[15][13] = "INT";
		ngramArr[4][14] = "GE";
		ngramArr[5][14] = "ENS";
		ngramArr[14][14] = "PL";
		ngramArr[15][14] = "RDS";
		ngramArr[4][15] = "PI";
		ngramArr[5][15] = "STE";
		ngramArr[14][15] = "AY";
		ngramArr[15][15] = "NTH";
		ngramArr[4][16] = "LE";
		ngramArr[5][16] = "ERS";
		ngramArr[14][16] = "CA";
		ngramArr[15][16] = "YOU";
		ngramArr[4][17] = "ST";
		ngramArr[5][17] = "LER";
		ngramArr[14][17] = "OR";
		ngramArr[15][17] = "STH";
		ngramArr[4][18] = "BE";
		ngramArr[5][18] = "ART";
		ngramArr[14][18] = "RD";
		ngramArr[15][18] = "OTH";
		ngramArr[4][19] = "RE";
		ngramArr[5][19] = "END";
		ngramArr[14][19] = "OU";
		ngramArr[15][19] = "ETH";
		ngramArr[4][20] = "AR";
		ngramArr[5][20] = "ICH";
		ngramArr[14][20] = "ED";
		ngramArr[15][20] = "FTH";
		ngramArr[4][21] = "NS";
		ngramArr[5][21] = "KAR";
		ngramArr[14][21] = "TO";
		ngramArr[15][21] = "ALL";
		ngramArr[4][22] = "UN";
		ngramArr[5][22] = "UND";
		ngramArr[14][22] = "ND";
		ngramArr[15][22] = "AME";
		ngramArr[4][23] = "DI";
		ngramArr[5][23] = "SCH";
		ngramArr[14][23] = "ET";
		ngramArr[15][23] = "ONT";
		ngramArr[4][24] = "AN";
		ngramArr[5][24] = "RDE";
		ngramArr[14][24] = "IS";
		ngramArr[15][24] = "ONE";
		ngramArr[4][25] = "RT";
		ngramArr[5][25] = "ERE";
		ngramArr[14][25] = "EN";
		ngramArr[15][25] = "FOR";
		ngramArr[4][26] = "RD";
		ngramArr[5][26] = "WER";
		ngramArr[14][26] = "AL";
		ngramArr[15][26] = "THA";
		ngramArr[4][27] = "SE";
		ngramArr[5][27] = "GEN";
		ngramArr[14][27] = "AC";
		ngramArr[15][27] = "HES";
		ngramArr[4][28] = "WE";
		ngramArr[5][28] = "REI";
		ngramArr[14][28] = "LE";
		ngramArr[15][28] = "OUR";
		ngramArr[4][29] = "KA";
		ngramArr[5][29] = "CHE";
		ngramArr[14][29] = "NG";
		ngramArr[15][29] = "EPL";
		ngramArr[4][30] = "AU";
		ngramArr[5][30] = "TEI";
		ngramArr[14][30] = "SA";
		ngramArr[15][30] = "ORE";
		ngramArr[4][31] = "SS";
		ngramArr[5][31] = "NSP";
		ngramArr[14][31] = "YE";
		ngramArr[15][31] = "OFT";
		ngramArr[4][32] = "IT";
		ngramArr[5][32] = "NEN";
		ngramArr[14][32] = "RS";
		ngramArr[15][32] = "HIS";
		ngramArr[4][33] = "DA";
		ngramArr[5][33] = "IES";
		ngramArr[14][33] = "HA";
		ngramArr[15][33] = "HEN";
		ngramArr[4][34] = "RS";
		ngramArr[5][34] = "HEN";
		ngramArr[14][34] = "EC";
		ngramArr[15][34] = "HEP";
		ngramArr[4][35] = "HA";
		ngramArr[5][35] = "AUF";
		ngramArr[14][35] = "OF";
		ngramArr[15][35] = "ECA";
		ngramArr[4][36] = "HE";
		ngramArr[5][36] = "IND";
		ngramArr[14][36] = "NE";
		ngramArr[15][36] = "GAM";
		ngramArr[4][37] = "UE";
		ngramArr[5][37] = "UER";
		ngramArr[14][37] = "IT";
		ngramArr[15][37] = "ERE";
		ngramArr[4][38] = "EG";
		ngramArr[5][38] = "DAS";
		ngramArr[14][38] = "AT";
		ngramArr[15][38] = "REA";
		ngramArr[4][39] = "IS";
		ngramArr[5][39] = "MIT";
		ngramArr[14][39] = "OT";
		ngramArr[15][39] = "ENT";
		ngramArr[4][40] = "NG";
		ngramArr[5][40] = "EIT";
		ngramArr[14][40] = "TI";
		ngramArr[15][40] = "ITH";
		ngramArr[4][41] = "IC";
		ngramArr[5][41] = "EGE";
		ngramArr[14][41] = "DE";
		ngramArr[15][41] = "ACE";
		ngramArr[4][42] = "NN";
		ngramArr[5][42] = "CHT";
		ngramArr[14][42] = "ME";
		ngramArr[15][42] = "SIN";
		ngramArr[4][43] = "ET";
		ngramArr[5][43] = "ELT";
		ngramArr[14][43] = "SE";
		ngramArr[15][43] = "HET";
		ngramArr[4][44] = "NA";
		ngramArr[5][44] = "NDI";
		ngramArr[14][44] = "AS";
		ngramArr[15][44] = "NOT";
		ngramArr[4][45] = "ED";
		ngramArr[5][45] = "EKA";
		ngramArr[14][45] = "DS";
		ngramArr[15][45] = "ART";
		ngramArr[4][46] = "AL";
		ngramArr[5][46] = "ENA";
		ngramArr[14][46] = "RO";
		ngramArr[15][46] = "EAC";
		ngramArr[4][47] = "EH";
		ngramArr[5][47] = "ERT";
		ngramArr[14][47] = "RT";
		ngramArr[15][47] = "EST";
		ngramArr[4][48] = "SC";
		ngramArr[5][48] = "ESP";
		ngramArr[14][48] = "TA";
		ngramArr[15][48] = "DTH";
		ngramArr[4][49] = "LT";
		ngramArr[5][49] = "NER";
		ngramArr[14][49] = "SI";
		ngramArr[15][49] = "HAN";
		ngramArr[4][50] = "EM";
		ngramArr[5][50] = "IST";
		ngramArr[14][50] = "RA";
		ngramArr[15][50] = "TRA";

		// POLITICS TEXT DEUTSCH ENGLISH
		ngramArr[6][1] = "ER";
		ngramArr[7][1] = "DER";
		ngramArr[16][1] = "TH";
		ngramArr[17][1] = "THE";
		ngramArr[6][2] = "EN";
		ngramArr[7][2] = "NDE";
		ngramArr[16][2] = "HE";
		ngramArr[17][2] = "ING";
		ngramArr[6][3] = "DE";
		ngramArr[7][3] = "EIN";
		ngramArr[16][3] = "IN";
		ngramArr[17][3] = "ENT";
		ngramArr[6][4] = "CH";
		ngramArr[7][4] = "DIE";
		ngramArr[16][4] = "RE";
		ngramArr[17][4] = "AND";
		ngramArr[6][5] = "TE";
		ngramArr[7][5] = "ICH";
		ngramArr[16][5] = "ER";
		ngramArr[17][5] = "THA";
		ngramArr[6][6] = "ND";
		ngramArr[7][6] = "SCH";
		ngramArr[16][6] = "AN";
		ngramArr[17][6] = "ION";
		ngramArr[6][7] = "EI";
		ngramArr[7][7] = "END";
		ngramArr[16][7] = "ON";
		ngramArr[17][7] = "HAT";
		ngramArr[6][8] = "IE";
		ngramArr[7][8] = "TEN";
		ngramArr[16][8] = "NT";
		ngramArr[17][8] = "FOR";
		ngramArr[6][9] = "IN";
		ngramArr[7][9] = "UNG";
		ngramArr[16][9] = "ES";
		ngramArr[17][9] = "ERE";
		ngramArr[6][10] = "ES";
		ngramArr[7][10] = "CHE";
		ngramArr[16][10] = "EN";
		ngramArr[17][10] = "TIO";
		ngramArr[6][11] = "GE";
		ngramArr[7][11] = "DEN";
		ngramArr[16][11] = "ST";
		ngramArr[17][11] = "ATE";
		ngramArr[6][12] = "UN";
		ngramArr[7][12] = "ERE";
		ngramArr[16][12] = "AT";
		ngramArr[17][12] = "EST";
		ngramArr[6][13] = "NE";
		ngramArr[7][13] = "UND";
		ngramArr[16][13] = "TI";
		ngramArr[17][13] = "NTH";
		ngramArr[6][14] = "ST";
		ngramArr[7][14] = "GEN";
		ngramArr[16][14] = "TO";
		ngramArr[17][14] = "HER";
		ngramArr[6][15] = "RE";
		ngramArr[7][15] = "ENS";
		ngramArr[16][15] = "OR";
		ngramArr[17][15] = "ICA";
		ngramArr[6][16] = "BE";
		ngramArr[7][16] = "INE";
		ngramArr[16][16] = "TE";
		ngramArr[17][16] = "RES";
		ngramArr[6][17] = "IT";
		ngramArr[7][17] = "NGE";
		ngramArr[16][17] = "ED";
		ngramArr[17][17] = "INT";
		ngramArr[6][18] = "NG";
		ngramArr[7][18] = "EIT";
		ngramArr[16][18] = "EA";
		ngramArr[17][18] = "ATI";
		ngramArr[6][19] = "DI";
		ngramArr[7][19] = "TER";
		ngramArr[16][19] = "AR";
		ngramArr[17][19] = "ARE";
		ngramArr[6][20] = "AN";
		ngramArr[7][20] = "ERD";
		ngramArr[16][20] = "HA";
		ngramArr[17][20] = "MEN";
		ngramArr[6][21] = "SE";
		ngramArr[7][21] = "VER";
		ngramArr[16][21] = "NG";
		ngramArr[17][21] = "ETH";
		ngramArr[6][22] = "UE";
		ngramArr[7][22] = "CHT";
		ngramArr[16][22] = "AL";
		ngramArr[17][22] = "VER";
		ngramArr[6][23] = "HE";
		ngramArr[7][23] = "RDE";
		ngramArr[16][23] = "IT";
		ngramArr[17][23] = "CON";
		ngramArr[6][24] = "NS";
		ngramArr[7][24] = "BER";
		ngramArr[16][24] = "IS";
		ngramArr[17][24] = "NGT";
		ngramArr[6][25] = "RA";
		ngramArr[7][25] = "DES";
		ngramArr[16][25] = "ND";
		ngramArr[17][25] = "TER";
		ngramArr[6][26] = "SI";
		ngramArr[7][26] = "STE";
		ngramArr[16][26] = "SE";
		ngramArr[17][26] = "HES";
		ngramArr[6][27] = "ON";
		ngramArr[7][27] = "ERS";
		ngramArr[16][27] = "OU";
		ngramArr[17][27] = "STA";
		ngramArr[6][28] = "AU";
		ngramArr[7][28] = "ENE";
		ngramArr[16][28] = "DE";
		ngramArr[17][28] = "TTH";
		ngramArr[6][29] = "IC";
		ngramArr[7][29] = "SSE";
		ngramArr[16][29] = "AS";
		ngramArr[17][29] = "ONT";
		ngramArr[6][30] = "RD";
		ngramArr[7][30] = "NDI";
		ngramArr[16][30] = "SA";
		ngramArr[17][30] = "ERS";
		ngramArr[6][31] = "TI";
		ngramArr[7][31] = "ION";
		ngramArr[16][31] = "RI";
		ngramArr[17][31] = "ALL";
		ngramArr[6][32] = "IS";
		ngramArr[7][32] = "HEN";
		ngramArr[16][32] = "ME";
		ngramArr[17][32] = "REA";
		ngramArr[6][33] = "SS";
		ngramArr[7][33] = "IER";
		ngramArr[16][33] = "ET";
		ngramArr[17][33] = "ITI";
		ngramArr[6][34] = "HA";
		ngramArr[7][34] = "ENT";
		ngramArr[16][34] = "LE";
		ngramArr[17][34] = "STH";
		ngramArr[6][35] = "SC";
		ngramArr[7][35] = "AND";
		ngramArr[16][35] = "LI";
		ngramArr[17][35] = "CAN";
		ngramArr[6][36] = "EL";
		ngramArr[7][36] = "IND";
		ngramArr[16][36] = "TA";
		ngramArr[17][36] = "DTH";
		ngramArr[6][37] = "LI";
		ngramArr[7][37] = "UER";
		ngramArr[16][37] = "VE";
		ngramArr[17][37] = "ORE";
		ngramArr[6][38] = "NI";
		ngramArr[7][38] = "ERT";
		ngramArr[16][38] = "NS";
		ngramArr[17][38] = "TED";
		ngramArr[6][39] = "TA";
		ngramArr[7][39] = "STA";
		ngramArr[16][39] = "CO";
		ngramArr[17][39] = "OVE";
		ngramArr[6][40] = "AL";
		ngramArr[7][40] = "EBE";
		ngramArr[16][40] = "IC";
		ngramArr[17][40] = "EAR";
		ngramArr[6][41] = "NA";
		ngramArr[7][41] = "IST";
		ngramArr[16][41] = "RA";
		ngramArr[17][41] = "ONS";
		ngramArr[6][42] = "LE";
		ngramArr[7][42] = "EST";
		ngramArr[16][42] = "CA";
		ngramArr[17][42] = "ART";
		ngramArr[6][43] = "RI";
		ngramArr[7][43] = "RUN";
		ngramArr[16][43] = "SI";
		ngramArr[17][43] = "TIN";
		ngramArr[6][44] = "ET";
		ngramArr[7][44] = "NEN";
		ngramArr[16][44] = "EC";
		ngramArr[17][44] = "STO";
		ngramArr[6][45] = "NT";
		ngramArr[7][45] = "MIT";
		ngramArr[16][45] = "OF";
		ngramArr[17][45] = "SIN";
		ngramArr[6][46] = "EH";
		ngramArr[7][46] = "AUS";
		ngramArr[16][46] = "RT";
		ngramArr[17][46] = "NCE";
		ngramArr[6][47] = "EM";
		ngramArr[7][47] = "FUE";
		ngramArr[16][47] = "HI";
		ngramArr[17][47] = "ORT";
		ngramArr[6][48] = "ME";
		ngramArr[7][48] = "SEN";
		ngramArr[16][48] = "NA";
		ngramArr[17][48] = "HIS";
		ngramArr[6][49] = "AE";
		ngramArr[7][49] = "ERI";
		ngramArr[16][49] = "NE";
		ngramArr[17][49] = "FTH";
		ngramArr[6][50] = "AR";
		ngramArr[7][50] = "EDE";
		ngramArr[16][50] = "TS";
		ngramArr[17][50] = "EDT";

		// FOOTBALL TEXT DEUTSCH ENGLISH
		ngramArr[8][1] = "ER";
		ngramArr[9][1] = "DER";
		ngramArr[18][1] = "TH";
		ngramArr[19][1] = "THE";
		ngramArr[8][2] = "EN";
		ngramArr[9][2] = "EIN";
		ngramArr[18][2] = "IN";
		ngramArr[19][2] = "AND";
		ngramArr[8][3] = "CH";
		ngramArr[9][3] = "ICH";
		ngramArr[18][3] = "HE";
		ngramArr[19][3] = "ING";
		ngramArr[8][4] = "DE";
		ngramArr[9][4] = "SCH";
		ngramArr[18][4] = "ER";
		ngramArr[19][4] = "INT";
		ngramArr[8][5] = "EI";
		ngramArr[9][5] = "NDE";
		ngramArr[18][5] = "AN";
		ngramArr[19][5] = "NTH";
		ngramArr[8][6] = "TE";
		ngramArr[9][6] = "DIE";
		ngramArr[18][6] = "ON";
		ngramArr[19][6] = "FOR";
		ngramArr[8][7] = "IE";
		ngramArr[9][7] = "END";
		ngramArr[18][7] = "RE";
		ngramArr[19][7] = "ION";
		ngramArr[8][8] = "IN";
		ngramArr[9][8] = "INE";
		ngramArr[18][8] = "AL";
		ngramArr[19][8] = "ALL";
		ngramArr[8][9] = "ND";
		ngramArr[9][9] = "CHE";
		ngramArr[18][9] = "ST";
		ngramArr[19][9] = "THA";
		ngramArr[8][10] = "GE";
		ngramArr[9][10] = "TEN";
		ngramArr[18][10] = "TO";
		ngramArr[19][10] = "TER";
		ngramArr[8][11] = "NE";
		ngramArr[9][11] = "DEN";
		ngramArr[18][11] = "OR";
		ngramArr[19][11] = "HIS";
		ngramArr[8][12] = "ST";
		ngramArr[9][12] = "UND";
		ngramArr[18][12] = "NT";
		ngramArr[19][12] = "ETH";
		ngramArr[8][13] = "ES";
		ngramArr[9][13] = "CHT";
		ngramArr[18][13] = "ES";
		ngramArr[19][13] = "VER";
		ngramArr[8][14] = "AN";
		ngramArr[9][14] = "ERS";
		ngramArr[18][14] = "EN";
		ngramArr[19][14] = "HES";
		ngramArr[8][15] = "RE";
		ngramArr[9][15] = "ERD";
		ngramArr[18][15] = "EA";
		ngramArr[19][15] = "ENT";
		ngramArr[8][16] = "EL";
		ngramArr[9][16] = "GEN";
		ngramArr[18][16] = "AT";
		ngramArr[19][16] = "ERE";
		ngramArr[8][17] = "BE";
		ngramArr[9][17] = "IEL";
		ngramArr[18][17] = "ED";
		ngramArr[19][17] = "EST";
		ngramArr[8][18] = "UN";
		ngramArr[9][18] = "TER";
		ngramArr[18][18] = "TE";
		ngramArr[19][18] = "THI";
		ngramArr[8][19] = "SE";
		ngramArr[9][19] = "ERE";
		ngramArr[18][19] = "ND";
		ngramArr[19][19] = "LAN";
		ngramArr[8][20] = "LE";
		ngramArr[9][20] = "ERT";
		ngramArr[18][20] = "HA";
		ngramArr[19][20] = "HER";
		ngramArr[8][21] = "UE";
		ngramArr[9][21] = "STE";
		ngramArr[18][21] = "AR";
		ngramArr[19][21] = "MAN";
		ngramArr[8][22] = "IC";
		ngramArr[9][22] = "ENS";
		ngramArr[18][22] = "TI";
		ngramArr[19][22] = "BAL";
		ngramArr[8][23] = "DI";
		ngramArr[9][23] = "ACH";
		ngramArr[18][23] = "NG";
		ngramArr[19][23] = "OOT";
		ngramArr[8][24] = "SS";
		ngramArr[9][24] = "REI";
		ngramArr[18][24] = "AS";
		ngramArr[19][24] = "PLA";
		ngramArr[8][25] = "HE";
		ngramArr[9][25] = "UER";
		ngramArr[18][25] = "IT";
		ngramArr[19][25] = "TED";
		ngramArr[8][26] = "AU";
		ngramArr[9][26] = "RDE";
		ngramArr[18][26] = "ET";
		ngramArr[19][26] = "ONT";
		ngramArr[8][27] = "NA";
		ngramArr[9][27] = "BER";
		ngramArr[18][27] = "IS";
		ngramArr[19][27] = "ERS";
		ngramArr[8][28] = "RA";
		ngramArr[9][28] = "EIT";
		ngramArr[18][28] = "LE";
		ngramArr[19][28] = "TIN";
		ngramArr[8][29] = "SC";
		ngramArr[9][29] = "VER";
		ngramArr[18][29] = "LA";
		ngramArr[19][29] = "ATI";
		ngramArr[8][30] = "NS";
		ngramArr[9][30] = "EDE";
		ngramArr[18][30] = "CO";
		ngramArr[19][30] = "TBA";
		ngramArr[8][31] = "IT";
		ngramArr[9][31] = "SPI";
		ngramArr[18][31] = "HI";
		ngramArr[19][31] = "ARE";
		ngramArr[8][32] = "NG";
		ngramArr[9][32] = "UNG";
		ngramArr[18][32] = "VE";
		ngramArr[19][32] = "ORT";
		ngramArr[8][33] = "RD";
		ngramArr[9][33] = "EGE";
		ngramArr[18][33] = "OT";
		ngramArr[19][33] = "AIN";
		ngramArr[8][34] = "AL";
		ngramArr[9][34] = "PIE";
		ngramArr[18][34] = "RO";
		ngramArr[19][34] = "TIO";
		ngramArr[8][35] = "HA";
		ngramArr[9][35] = "HEN";
		ngramArr[18][35] = "RA";
		ngramArr[19][35] = "EDT";
		ngramArr[8][36] = "NT";
		ngramArr[9][36] = "AUF";
		ngramArr[18][36] = "LL";
		ngramArr[19][36] = "FOO";
		ngramArr[8][37] = "LI";
		ngramArr[9][37] = "NER";
		ngramArr[18][37] = "NA";
		ngramArr[19][37] = "OTB";
		ngramArr[8][38] = "TA";
		ngramArr[9][38] = "DAS";
		ngramArr[18][38] = "FO";
		ngramArr[19][38] = "DTH";
		ngramArr[8][39] = "RT";
		ngramArr[9][39] = "ENT";
		ngramArr[18][39] = "MA";
		ngramArr[19][39] = "LEA";
		ngramArr[8][40] = "OR";
		ngramArr[9][40] = "NEN";
		ngramArr[18][40] = "DE";
		ngramArr[19][40] = "ILL";
		ngramArr[8][41] = "ON";
		ngramArr[9][41] = "IND";
		ngramArr[18][41] = "OU";
		ngramArr[19][41] = "ORE";
		ngramArr[8][42] = "AS";
		ngramArr[9][42] = "FUE";
		ngramArr[18][42] = "EL";
		ngramArr[19][42] = "IVE";
		ngramArr[8][43] = "EG";
		ngramArr[9][43] = "ENA";
		ngramArr[18][43] = "SA";
		ngramArr[19][43] = "OTH";
		ngramArr[8][44] = "DA";
		ngramArr[9][44] = "EBE";
		ngramArr[18][44] = "SE";
		ngramArr[19][44] = "ITH";
		ngramArr[8][45] = "ED";
		ngramArr[9][45] = "ENE";
		ngramArr[18][45] = "LI";
		ngramArr[19][45] = "ONS";
		ngramArr[8][46] = "RS";
		ngramArr[9][46] = "IES";
		ngramArr[18][46] = "BE";
		ngramArr[19][46] = "NAL";
		ngramArr[8][47] = "LA";
		ngramArr[9][47] = "AND";
		ngramArr[18][47] = "NS";
		ngramArr[19][47] = "STH";
		ngramArr[8][48] = "LL";
		ngramArr[9][48] = "NGE";
		ngramArr[18][48] = "CH";
		ngramArr[19][48] = "WIT";
		ngramArr[8][49] = "ET";
		ngramArr[9][49] = "IER";
		ngramArr[18][49] = "NE";
		ngramArr[19][49] = "HAT";
		ngramArr[8][50] = "EM";
		ngramArr[9][50] = "ABE";
		ngramArr[18][50] = "RI";
		ngramArr[19][50] = "ERT";

		// LAW TEXT DEUTSCH ENGLISH
		ngramArr[10][1] = "EN";
		ngramArr[11][1] = "UNG";
		ngramArr[20][1] = "TH";
		ngramArr[21][1] = "THE";
		ngramArr[10][2] = "ER";
		ngramArr[11][2] = "DER";
		ngramArr[20][2] = "ON";
		ngramArr[21][2] = "ION";
		ngramArr[10][3] = "DE";
		ngramArr[11][3] = "NDE";
		ngramArr[20][3] = "TI";
		ngramArr[21][3] = "TIO";
		ngramArr[10][4] = "UN";
		ngramArr[11][4] = "END";
		ngramArr[20][4] = "IN";
		ngramArr[21][4] = "ATI";
		ngramArr[10][5] = "CH";
		ngramArr[11][5] = "ICH";
		ngramArr[20][5] = "HE";
		ngramArr[21][5] = "ENT";
		ngramArr[10][6] = "ND";
		ngramArr[11][6] = "GEN";
		ngramArr[20][6] = "ER";
		ngramArr[21][6] = "FOR";
		ngramArr[10][7] = "GE";
		ngramArr[11][7] = "DIE";
		ngramArr[20][7] = "AT";
		ngramArr[21][7] = "ECT";
		ngramArr[10][8] = "EI";
		ngramArr[11][8] = "UND";
		ngramArr[20][8] = "RE";
		ngramArr[21][8] = "MEN";
		ngramArr[10][9] = "TE";
		ngramArr[11][9] = "SCH";
		ngramArr[20][9] = "IO";
		ngramArr[21][9] = "NOT";
		ngramArr[10][10] = "NG";
		ngramArr[11][10] = "TEN";
		ngramArr[20][10] = "NT";
		ngramArr[21][10] = "CTI";
		ngramArr[10][11] = "BE";
		ngramArr[11][11] = "DEN";
		ngramArr[20][11] = "OR";
		ngramArr[21][11] = "ONS";
		ngramArr[10][12] = "ES";
		ngramArr[11][12] = "ERD";
		ngramArr[20][12] = "AN";
		ngramArr[21][12] = "ING";
		ngramArr[10][13] = "IE";
		ngramArr[11][13] = "EIN";
		ngramArr[20][13] = "EN";
		ngramArr[21][13] = "AND";
		ngramArr[10][14] = "IN";
		ngramArr[11][14] = "CHE";
		ngramArr[20][14] = "ES";
		ngramArr[21][14] = "SEC";
		ngramArr[10][15] = "ST";
		ngramArr[11][15] = "NGE";
		ngramArr[20][15] = "ED";
		ngramArr[21][15] = "TAT";
		ngramArr[10][16] = "NE";
		ngramArr[11][16] = "EIT";
		ngramArr[20][16] = "TE";
		ngramArr[21][16] = "ONT";
		ngramArr[10][17] = "UE";
		ngramArr[11][17] = "RDE";
		ngramArr[20][17] = "SE";
		ngramArr[21][17] = "OFT";
		ngramArr[10][18] = "DI";
		ngramArr[11][18] = "VER";
		ngramArr[20][18] = "IS";
		ngramArr[21][18] = "ERE";
		ngramArr[10][19] = "AN";
		ngramArr[11][19] = "BER";
		ngramArr[20][19] = "TO";
		ngramArr[21][19] = "FTH";
		ngramArr[10][20] = "RE";
		ngramArr[11][20] = "NUN";
		ngramArr[20][20] = "EC";
		ngramArr[21][20] = "ERS";
		ngramArr[10][21] = "IC";
		ngramArr[11][21] = "STE";
		ngramArr[20][21] = "TA";
		ngramArr[21][21] = "NTH";
		ngramArr[10][22] = "NA";
		ngramArr[11][22] = "TER";
		ngramArr[20][22] = "AR";
		ngramArr[21][22] = "HER";
		ngramArr[10][23] = "SE";
		ngramArr[11][23] = "DES";
		ngramArr[20][23] = "ND";
		ngramArr[21][23] = "ATE";
		ngramArr[10][24] = "RD";
		ngramArr[11][24] = "NGS";
		ngramArr[20][24] = "ST";
		ngramArr[21][24] = "ONA";
		ngramArr[10][25] = "IT";
		ngramArr[11][25] = "ENS";
		ngramArr[20][25] = "IT";
		ngramArr[21][25] = "TED";
		ngramArr[10][26] = "LI";
		ngramArr[11][26] = "CHT";
		ngramArr[20][26] = "OT";
		ngramArr[21][26] = "CON";
		ngramArr[10][27] = "HE";
		ngramArr[11][27] = "ACH";
		ngramArr[20][27] = "CT";
		ngramArr[21][27] = "OTH";
		ngramArr[10][28] = "NS";
		ngramArr[11][28] = "LIC";
		ngramArr[20][28] = "EA";
		ngramArr[21][28] = "INT";
		ngramArr[10][29] = "SC";
		ngramArr[11][29] = "ERE";
		ngramArr[20][29] = "OF";
		ngramArr[21][29] = "DIN";
		ngramArr[10][30] = "ZU";
		ngramArr[11][30] = "ENA";
		ngramArr[20][30] = "NS";
		ngramArr[21][30] = "EDI";
		ngramArr[10][31] = "EH";
		ngramArr[11][31] = "EBE";
		ngramArr[20][31] = "NO";
		ngramArr[21][31] = "EDB";
		ngramArr[10][32] = "SS";
		ngramArr[11][32] = "ESE";
		ngramArr[20][32] = "AL";
		ngramArr[21][32] = "NCE";
		ngramArr[10][33] = "RU";
		ngramArr[11][33] = "ENE";
		ngramArr[20][33] = "SI";
		ngramArr[21][33] = "DBY";
		ngramArr[10][34] = "ET";
		ngramArr[11][34] = "HEN";
		ngramArr[20][34] = "ME";
		ngramArr[21][34] = "DER";
		ngramArr[10][35] = "AU";
		ngramArr[11][35] = "ENU";
		ngramArr[20][35] = "SA";
		ngramArr[21][35] = "CHA";
		ngramArr[10][36] = "NT";
		ngramArr[11][36] = "FUE";
		ngramArr[20][36] = "DE";
		ngramArr[21][36] = "ARE";
		ngramArr[10][37] = "HA";
		ngramArr[11][37] = "GUN";
		ngramArr[20][37] = "RT";
		ngramArr[21][37] = "ACT";
		ngramArr[10][38] = "EL";
		ngramArr[11][38] = "NDI";
		ngramArr[20][38] = "HA";
		ngramArr[21][38] = "NDE";
		ngramArr[10][39] = "IS";
		ngramArr[11][39] = "ERU";
		ngramArr[20][39] = "FO";
		ngramArr[21][39] = "ART";
		ngramArr[10][40] = "WE";
		ngramArr[11][40] = "CHA";
		ngramArr[20][40] = "AS";
		ngramArr[21][40] = "SAN";
		ngramArr[10][41] = "TS";
		ngramArr[11][41] = "NEN";
		ngramArr[20][41] = "CE";
		ngramArr[21][41] = "ORI";
		ngramArr[10][42] = "NU";
		ngramArr[11][42] = "RUN";
		ngramArr[20][42] = "CH";
		ngramArr[21][42] = "THA";
		ngramArr[10][43] = "EB";
		ngramArr[11][43] = "REN";
		ngramArr[20][43] = "LE";
		ngramArr[21][43] = "LAT";
		ngramArr[10][44] = "IG";
		ngramArr[11][44] = "RUE";
		ngramArr[20][44] = "NG";
		ngramArr[21][44] = "ERT";
		ngramArr[10][45] = "AE";
		ngramArr[11][45] = "RBE";
		ngramArr[20][45] = "NA";
		ngramArr[21][45] = "THI";
		ngramArr[10][46] = "ON";
		ngramArr[11][46] = "UER";
		ngramArr[20][46] = "DI";
		ngramArr[21][46] = "RTH";
		ngramArr[10][47] = "RA";
		ngramArr[11][47] = "AGE";
		ngramArr[20][47] = "RI";
		ngramArr[21][47] = "OTA";
		ngramArr[10][48] = "OR";
		ngramArr[11][48] = "GES";
		ngramArr[20][48] = "CO";
		ngramArr[21][48] = "PRO";
		ngramArr[10][49] = "LE";
		ngramArr[11][49] = "AUS";
		ngramArr[20][49] = "LA";
		ngramArr[21][49] = "SIN";
		ngramArr[10][50] = "TI";
		ngramArr[11][50] = "INE";
		ngramArr[20][50] = "LI";
		ngramArr[21][50] = "STA";

		// IF THERE IS A MANUAL REFERENCE

		if (!view.getReferenceTitle().equals("")) {
			String referenceText = view.getReferenceText().replaceAll("\\s+", "").toUpperCase();

			for (int n = 2; n <= 3; n++) {
				String[] gramArr = new String[referenceText.length()];

				for (int m = 0; m < referenceText.length(); m++)
					if (m < referenceText.length() - (n - 1))
						gramArr[m] = referenceText.substring(m, n + m);

				Map<String, Integer> freq = new HashMap<String, Integer>();

				for (String word : gramArr) {
					if (!freq.containsKey(word))
						freq.put(word, 0);
					freq.put(word, freq.get(word) + 1);
				}

				List<String> resultSorted = new ArrayList<String>();
				resultSorted = getWordInDescendingFreqOrder(freq);

				int e26 = 51;
				int count = 0;

				for (String p : resultSorted)
					if (++count < e26)
						ngramArr[20 + n][count] = p;
			}
		}

		// END IF THERE IS A MANUAL REFERENCE

		// Remove spaces and convert to Upper Case
		String ciphertext_l = view.getCipherText().replaceAll("\\s+", "").toUpperCase();
		System.out.println("Without spaces & Uppercase: " + ciphertext_l);

		for (int n = 2; n <= 3; n++) // *** For 2 and 3 grams ***
		{
			String[] gramArr = new String[ciphertext_l.length()];

			for (int m = 0; m < ciphertext_l.length(); m++)
				if (m < ciphertext_l.length() - (n - 1)) // avoid going beyond
															// the end of the
															// string
					gramArr[m] = ciphertext_l.substring(m, n + m);

			Map<String, Integer> freq = new HashMap<String, Integer>();

			for (String word : gramArr) {
				if (!freq.containsKey(word))
					freq.put(word, 0);
				freq.put(word, freq.get(word) + 1);
			}

			// SORT the N-grams according to their frequencies
			List<String> resultSorted = new ArrayList<String>();
			resultSorted = getWordInDescendingFreqOrder(freq);

			// Save only the first 51 to the ngramArray
			int e51 = 51;
			int count = 0;

			System.out.println("------");
			System.out.println("Sorted " + n + "-grams:");

			for (String p : resultSorted)
				if (++count < e51) {
					if (p != null && !p.isEmpty()) // check if the 2/3-gram not
													// equals null
					{
						ngramArr[n - 2][count] = p;
						System.out.println(count + ". " + ngramArr[n - 2][count]);
					} else
						count--;
				}
		}

		// An array of 2- and 3-gram distances between given txt and 5 other
		// txts
		int[] distance2gram = new int[8];
		int[] distance2gramQ = new int[8]; // distance for Least Squares
		int[] distance3gram = new int[8];
		int[] distance3gramQ = new int[8]; // distance for Least Squares

		/** 2-GRAM DISTANCES BETWEEN GIVEN TXT AND 5 OTHER TXTS */
		distance2gram[0] = 0;
		int w = 0; // flag

		int language = 10; // default language is English
		if (view.getCipherLanguage() == 1)
			language = 0; // else set to German

		for (int dis2 = 1; dis2 < 6; dis2++) {
			for (int i = 1; i < 51; i++) // compare each with each
			{
				for (int j = 1; j < 51; j++) // compare each with each
				{
					// if ENGLISH: language=10, if GERMAN: language=0
					if (ngramArr[0][i].equals(ngramArr[2 * dis2 + language][j])) // compare
																					// 2,4,6,8,10
					{
						// 0 - Euclidian, 1 - Least Squares
						if (view.getDistanceMethod() == 0)
							distance2gram[dis2] += Math.abs(i - j); // calc
																	// Euclidean
																	// dist.
																	// |i-j|
						else
							distance2gramQ[dis2] += Math.pow(Math.abs(i - j), 2); // square
																					// distance
						w = 1;
					}
				}

				if (w == 0) // if for two i and j there were NO 2 equals (e.g.ER
							// = ER)
				{
					if (view.getDistanceMethod() == 0)
						distance2gram[dis2] += 50 - i + 5;
					else
						distance2gramQ[dis2] += Math.pow((50 - i + 5), 2);
				}

				else
					w = 0;
			}
		}

		// IF THERE IS MANUAL REFERANCE, 2-GRAMMS ***

		if (!view.getReferenceTitle().equals("")) {
			for (int i = 1; i < 51; i++) {
				for (int j = 1; j < 51; j++) {
					if (ngramArr[0][i].equals(ngramArr[22][j])) {
						if (view.getDistanceMethod() == 0)
							distance2gram[6] += Math.abs(i - j);
						else
							distance2gramQ[6] += Math.pow(Math.abs(i - j), 2); // square
																				// distance
						w = 1;
					}
				}

				if (w == 0) {
					if (view.getDistanceMethod() == 0)
						distance2gram[6] += 50 - i + 5;
					else
						distance2gramQ[6] += Math.pow((50 - i + 5), 2);
				}

				else
					w = 0;
			}
		}
		// *** IF THERE IS MANUAL REFERANCE, 2-GRAMMS **/

		System.out.println("2-gram distance to Literature: " + distance2gram[1]);
		System.out.println("2-gram distance to Game rules: " + distance2gram[2]);
		System.out.println("2-gram distance to Politics: " + distance2gram[3]);
		System.out.println("2-gram distance to Football: " + distance2gram[4]);
		System.out.println("2-gram distance to Law: " + distance2gram[5]);

		System.out.println("Q 2-gram distance to Literature: " + Math.sqrt(distance2gramQ[1]));
		System.out.println("Q 2-gram distance to Games: " + Math.sqrt(distance2gramQ[2]));
		System.out.println("Q 2-gram distance to Politics: " + Math.sqrt(distance2gramQ[3]));
		System.out.println("Q 2-gram distance to Football: " + Math.sqrt(distance2gramQ[4]));
		System.out.println("Q 2-gram distance to Law: " + Math.sqrt(distance2gramQ[5]));

		if (!view.getReferenceTitle().equals(""))
			System.out.println("2-gram distance to " + view.getReferenceTitle() + ": " + distance2gram[6]);
		System.out.println("-------");

		/** 3-GRAM DISTANCES BETWEEN GIVEN TXT AND 5 OTHER TXTS */
		distance3gram[0] = 0;
		w = 0; // flag

		for (int dis3 = 1; dis3 < 6; dis3++) {
			for (int i = 1; i < 51; i++) {
				for (int j = 1; j < 51; j++) {
					if (ngramArr[1][i].equals(ngramArr[2 * dis3 + 1 + language][j])) // compare
																						// 3,5,7,9,11
					{
						if (view.getDistanceMethod() == 0)
							distance3gram[dis3] += Math.abs(i - j);
						else
							distance3gramQ[dis3] += Math.pow(Math.abs(i - j), 2); // square
																					// distance
						w = 1;
					}
				}

				if (w == 0) // if for two i and j there were NO 2 equals (e.g.ER
							// = ER)
				{
					if (view.getDistanceMethod() == 0)
						distance3gram[dis3] += 50 - i + 5;
					else
						distance3gramQ[dis3] += Math.pow((50 - i + 5), 2);
				} else
					w = 0;
			}
		}

		// IF THERE IS MANUAL REFERANCE, 3-GRAMMS ***
		if (!view.getReferenceTitle().equals("")) {
			for (int i = 1; i < 51; i++) {
				for (int j = 1; j < 51; j++) {
					if (ngramArr[1][i].equals(ngramArr[23][j])) {
						if (view.getDistanceMethod() == 0)
							distance3gram[6] += Math.abs(i - j);
						else
							distance3gramQ[6] += Math.pow(Math.abs(i - j), 2); // square
																				// distance
						w = 1;
					}
				}

				if (w == 0) {
					if (view.getDistanceMethod() == 0)
						distance3gram[6] += 50 - i + 5;
					else
						distance3gramQ[6] += Math.pow((50 - i + 5), 2);
				} else
					w = 0;
			}
		}
		// *** IF THERE IS MANUAL REFERANCE, 3-GRAMMS **/

		System.out.println("3-gram distance to Literature: " + distance3gram[1]);
		System.out.println("3-gram distance to Game rules: " + distance3gram[2]);
		System.out.println("3-gram distance to Politics: " + distance3gram[3]);
		System.out.println("3-gram distance to Football: " + distance3gram[4]);
		System.out.println("3-gram distance to Law: " + distance3gram[5]);

		System.out.println("Q 3-gram distance to Literature: " + Math.sqrt(distance3gramQ[1]));
		System.out.println("Q 3-gram distance to Games: " + Math.sqrt(distance3gramQ[2]));
		System.out.println("Q 3-gram distance to Politics: " + Math.sqrt(distance3gramQ[3]));
		System.out.println("Q 3-gram distance to Football: " + Math.sqrt(distance3gramQ[4]));
		System.out.println("Q 3-gram distance to Law: " + Math.sqrt(distance3gramQ[5]));

		if (!view.getReferenceTitle().equals(""))
			System.out.println("2-gram distance to " + view.getReferenceTitle() + ": " + distance3gram[6]);
		System.out.println("-------");

		/** COMBINED DISTANCES BETWEEN GIVEN TXT AND 5 OTHER TXTS */
		int[] distance;
		distance = new int[7];

		double[] distanceQ;
		distanceQ = new double[7];

		// ADD 2-gram and 3-gram distances
		// 0 - Euclidean
		if (view.getDistanceMethod() == 0) {
			for (int m = 1; m < 6; m++)
				distance[m] = distance2gram[m] + distance3gram[m];

			if (!view.getReferenceTitle().equals(""))
				distance[6] = distance2gram[6] + distance3gram[6]; // if there
																	// is manual
																	// reference

			System.out.println("Combined distance to Literature: " + distance[1]);
			System.out.println("Combined distance to Game rules: " + distance[2]);
			System.out.println("Combined distance to Politics: " + distance[3]);
			System.out.println("Combined distance to Football: " + distance[4]);
			System.out.println("Combined distance to Law: " + distance[5]);

			if (!view.getReferenceTitle().equals(""))
				System.out.println("Combined distance to " + view.getReferenceTitle() + ": " + distance[6]);
			System.out.println("-------");
		}
		// ADD 2-gram and 3-gram distances, Least Squares
		else {
			for (int m = 1; m < 6; m++)
				distanceQ[m] = Math.sqrt(distance2gramQ[m]) + Math.sqrt(distance3gramQ[m]);

			if (!view.getReferenceTitle().equals(""))
				distanceQ[6] = Math.sqrt(distance2gramQ[6]) + Math.sqrt(distance3gram[6]); // if
																							// there
																							// is
																							// manual
																							// reference

			System.out.println("Q Combined distance to Literature: " + distanceQ[1]);
			System.out.println("Q Combined distance to Game rules: " + distanceQ[2]);
			System.out.println("Q Combined distance to Politics: " + distanceQ[3]);
			System.out.println("Q Combined distance to Football: " + distanceQ[4]);
			System.out.println("Q Combined distance to Law: " + distanceQ[5]);

			if (!view.getReferenceTitle().equals(""))
				System.out.println("Q Combined distance to " + view.getReferenceTitle() + ": " + distanceQ[6]);
			System.out.println("-------");
		}

		/** SORT DISTANCES IN DESCENDING ORDER AND MAKE A GUESS */
		int totalNr = !view.getReferenceTitle().equals("") ? 7 : 6;

		// TESTING
		String[] namesStr = new String[totalNr];
		namesStr[1] = "Literature";
		namesStr[2] = "Game rules";
		namesStr[3] = "Politics";
		namesStr[4] = "Football";
		namesStr[5] = "Law";
		if (!view.getReferenceTitle().equals(""))
			namesStr[6] = view.getReferenceTitle();

		if (view.getDistanceMethod() == 0) {
			MyLinkedMap<String, Integer> nameDist = new MyLinkedMap<String, Integer>();

			for (int v = 1; v < totalNr; v++)
				nameDist.put(namesStr[v], distance[v]);

			List<String> sortedNameDist = new ArrayList<String>();
			sortedNameDist = getWordInDescendingFreqOrder(nameDist);

			MyLinkedMap<String, Integer> nameDistSorted = new MyLinkedMap<String, Integer>();

			int guess = 0;
			String star = "\u2605 \u2606 \u2606 \u2606 \u2606";
			String ResultStr = "";

			for (String word : sortedNameDist) {
				guess++;
				nameDistSorted.put(word, nameDist.get(word));

				if (nameDist.get(word) < 1050)
					star = "\u2605 \u2605 \u2605 \u2605 \u2605";
				else if (nameDist.get(word) < 1150)
					star = "\u2605 \u2605 \u2605 \u2605 \u2606";
				else if (nameDist.get(word) < 1250)
					star = "\u2605 \u2605 \u2605 \u2606 \u2606";
				else if (nameDist.get(word) < 1350)
					star = "\u2605 \u2605 \u2606 \u2606 \u2606";

				ResultStr = "(" + star + ") \t" + (totalNr - guess) + ". Guess: This text deals with the topic " + word
						+ "  [" + nameDist.get(word) + "]" + (guess == 1 ? "" : "\n") + ResultStr;

				// PRINT RESULT

				System.out.println(ResultStr);
				view.setResultText(ResultStr);
			}
		}

		else {
			MyLinkedMap<String, Integer> nameDist = new MyLinkedMap<String, Integer>();

			for (int v = 1; v < totalNr; v++)
				nameDist.put(namesStr[v], (int) distanceQ[v]);

			List<String> sortedNameDist = new ArrayList<String>();
			sortedNameDist = getWordInDescendingFreqOrder(nameDist);

			MyLinkedMap<String, Integer> nameDistSorted = new MyLinkedMap<String, Integer>();

			int guess = 0;
			String star = "\u2605 \u2606 \u2606 \u2606 \u2606";
			String ResultStr = "";

			for (String word : sortedNameDist) {
				guess++;
				nameDistSorted.put(word, nameDist.get(word));

				// factor 5 difference
				if ((nameDist.get(word)) < 200)
					star = "\u2605 \u2605 \u2605 \u2605 \u2605";
				else if (nameDist.get(word) < 220)
					star = "\u2605 \u2605 \u2605 \u2605 \u2606";
				else if (nameDist.get(word) < 240)
					star = "\u2605 \u2605 \u2605 \u2606 \u2606";
				else if (nameDist.get(word) < 260)
					star = "\u2605 \u2605 \u2606 \u2606 \u2606";

				ResultStr = "(" + star + ") \t" + (totalNr - guess) + ". Guess: This text deals with the topic " + word
						+ "  [" + nameDist.get(word) + "]" + (guess == 1 ? "" : "\n") + ResultStr;

				// PRINT RESULT

				System.out.println(ResultStr);
				view.setResultText(ResultStr);
			}
		}
	}

	// Functionality to access single elements of a Map
	class MyLinkedMap<K, V> extends LinkedHashMap<K, V> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public V getValue(int i) {

			Map.Entry<K, V> entry = this.getEntry(i);
			if (entry == null)
				return null;

			return entry.getValue();
		}

		public K getKey(int i) {

			Map.Entry<K, V> entry = this.getEntry(i);
			if (entry == null)
				return null;

			return entry.getKey();
		}

		public Map.Entry<K, V> getEntry(int i) {
			// check if negative index provided
			Set<Map.Entry<K, V>> entries = entrySet();
			int j = 0;

			for (Map.Entry<K, V> entry : entries)
				if (j++ == i)
					return entry;

			return null;

		}

	}

	// Function that returns words in descending frequency order
	private List<String> getWordInDescendingFreqOrder(Map<String, Integer> wordCount) {
		List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(wordCount.entrySet());

		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			@Override
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		List<String> result = new ArrayList<String>();

		for (Map.Entry<String, Integer> entry : list) {
			result.add(entry.getKey());
		}

		return result;
	}
}
