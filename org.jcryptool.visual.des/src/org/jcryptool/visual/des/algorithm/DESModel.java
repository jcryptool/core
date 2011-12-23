package org.jcryptool.visual.des.algorithm;

import java.util.Random;

public class DESModel {                                                                       //march 2009
			
         public DESModel(){ }
		 int DES_action_type  = 0; //ENCRYPTION  = 0     DECRYPTION       = 1
         int DES_fixed_status = 0; //Fixed Point = 0     Anti-Fixed Point = 1
                 
         //Verschiebungs-Vektor (zum Erzeugen von (C[i],D[i])) aus (C[0],D[0]))
		 int[] v = { 1,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1 };
		
                 //Akkumuliertes v
		 int[] accu_v = new int[16];  // ={ 1,2,4,6,8,10,12,14,15,17,19,21,23,25,27,28 };  fuer obiges v
		
		 void evaluate_accu_v()
		{
			accu_v[0] = v[0];
			for (int i=1; i<v.length; i++) 
				accu_v[i] = accu_v[i-1] + v[i];
		}
		
		 int[][] C     = new int[17][28]; // C0,C1,...,C16
		 int[][] D     = new int[17][28]; // D0,D1,...,D16
		 int[][] CD    = new int[17][56]; // CD0,CD1,...,CD16
         int[][] CD_mix= new int[34][28]; // C0D0,...,C16D16
		 int[][] DES_K = new int[16][48]; // K1,...,K16 = alle Rundenkeys
		 int[][] DES_reversed_K = new int[17][32];
	
                // PC1 generiert (C0,D0)
		 int[] PC1 = {
			57,49,41,33,25,17,9,1,58,50,42,34,26,18,10,2,59,51,43,35,27,19,11,3,60,52,44,36,
                        63,55,47,39,31,23,15,7,62,54,46,38,30,22,14,6,61,53,45,37,29,21,13,5,28,20,12,4 };
		
		
                 // PC2 generiert die Rundenkeys K1,.....,K16
		 int[] PC2 = {
			14,17,11,24,1,5,3,28,15,6,21,10,23,19,12,4,26,8,16,7,27,20,13,2, // 1-28  fuer C_i
                        41,52,31,37,47,55,30,40,51,45,33,48,44,49,39,56,34,53,46,42,50,36,29,32 }; // 29-56 fuer D_i
		
		
                 // Semi-/Schwache Keys k0, ...., k15
                 // "DES_key" ist der DES-Masterkey, aus dem CD_Matrix und Rundenkeys K_i erzeugt werden
                 // "key_Schneier" = Testkey
		 int[] DES_key = new int[64]; 
		 int[] key_k0  = { 0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1 };
		 int[] key_k5  = { 0,0,0,1,1,1,1,1,0,0,0,1,1,1,1,1,0,0,0,1,1,1,1,1,0,0,0,1,1,1,1,1,0,0,0,0,1,1,1,0,0,0,0,0,1,1,1,0,0,0,0,0,1,1,1,0,0,0,0,0,1,1,1,0 };
		 int[] key_k10 = { 1,1,1,0,0,0,0,0,1,1,1,0,0,0,0,0,1,1,1,0,0,0,0,0,1,1,1,0,0,0,0,0,1,1,1,1,0,0,0,1,1,1,1,1,0,0,0,1,1,1,1,1,0,0,0,1,1,1,1,1,0,0,0,1 };
		 int[] key_k15 = { 1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,0 };
		 int[] key_k3  = { 0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,0 };
		 int[] key_k6  = { 0,0,0,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,1,1,1,0,1,1,1,1,0,0,0,1,0,0,0,0,1,1,1,0,1,1,1,1,0,0,0,1 };
		 int[] key_k9  = { 1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,1,0,0,0,0,1,1,1,0,1,1,1,1,0,0,0,1,0,0,0,0,1,1,1,0 };
		 int[] key_k12 = { 1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,1 };
         int[] key_user =     { 0,0,0,0,0,0,0,1,0,0,1,0,0,0,1,1,0,1,0,0,0,1,0,1,0,1,1,0,0,1,1,1,1,0,0,0,1,0,0,1,1,0,1,0,1,0,1,1,1,1,0,0,1,1,0,1,1,1,1,0,1,1,1,1 };
         int[] key_Schneier = { 0,0,0,0,0,0,0,1,0,0,1,0,0,0,1,1,0,1,0,0,0,1,0,1,0,1,1,0,0,1,1,1,1,0,0,0,1,0,0,1,1,0,1,0,1,0,1,1,1,1,0,0,1,1,0,1,1,1,1,0,1,1,1,1 };
		 //int[] key_Buchmann = { 0,0,0,1,0,0,1,1,0,0,1,1,0,1,0,0,0,1,0,1,0,1,1,1,0,1,1,1,1,0,0,1,1,0,0,1,1,0,1,1,1,0,1,1,1,1,0,0,1,1,0,1,1,1,1,1,1,1,1,1,0,0,0,1 }

		 // DES_plaintext ist der vom user eingegebene Text zum Chiffrieren		
		 int[] DES_plaintext               = new int[64];
         int[] DES_delta_Plaintext         = new int[64];
         int[] DES_m_Plaintext             = new int[64];
         int[] DES_m_oplus_Delta_Plaintext = new int[64];
         int[] DES_input_D    = { 1,1,0,1,1,1,0,1,1,1,0,1,1,1,0,1,1,1,0,1,1,1,0,1,1,1,0,1,1,1,0,1,1,1,0,1,1,1,0,1,1,1,0,1,1,1,0,1,1,1,0,1,1,1,0,1,1,1,0,1,1,1,0,1 };
		 int[] plaintext_NULL = { 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
//		 int[] plaintext_EINS = { 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
//		 int[] plaintext_Buchmann = { 0,0,0,0,0,0,0,1,0,0,1,0,0,0,1,1,0,1,0,0,0,1,0,1,0,1,1,0,0,1,1,1,1,0,0,0,1,0,0,1,1,0,1,0,1,0,1,1,1,1,0,0,1,1,0,1,1,1,1,0,1,1,1,1};		 
//		 int[] plaintext_Schneier   = { 0,0,0,0,0,0,0,1,0,0,1,0,0,0,1,1,0,1,0,0,0,1,0,1,0,1,1,0,0,1,1,1,1,0,0,0,1,0,0,1,1,0,1,0,1,0,1,1,1,1,0,0,1,1,0,1,1,1,1,0,0,1,1,1};
//		 int[] ciphertext_Schneier  = { 1,1,0,0,1,0,0,1,0,1,0,1,0,1,1,1,0,1,0,0,0,1,0,0,0,0,1,0,0,1,0,1,0,1,1,0,1,0,1,0,0,1,0,1,1,1,1,0,1,1,0,1,0,0,1,1,0,0,0,1,1,1,0,1};		
		
                 // "DES_cipher_sequence" = m[0], m[1], ..., m[17]  als Matrix
                 // "DES_ciphertext" = DES(k, DES_plaintext)
                 int[][] DES_cipher_sequence               = new int[17][32];
                 int[]   DES_ciphertext                    = new int[64]; 
                 
                 int[][] DES_m_cipher_sequence             = new int[18][32];//--> DES_m_active_SBoxes
                 int[][] DES_m_oplus_Delta_cipher_sequence = new int[18][32];//--> DES_m_oplus_Delta_active_SBoxes
		
                 // "DES_Plaintext_Matrix" = (DES_Plaintext + e_i)_i   als Matrix
                 // "DES_ciphertext_Matrix" = [ DES(k, DES_plaintext + e_i) ]_i   als Matrix
                 int[][] DES_Plaintext_Matrix         = new int[65][64]; //INPUT-Matrix hat 64 bits pro Zeile
                 int[][] DES_Ciphertext_Matrix        = new int[65][64]; //(row[i]):=DES_Ciphertext_Matrix
                 int[]   DES_dist_1_Ciphertext_Vector = new int[64];     //der String  dist(row[0],row[i])  als array
                 int[][] DES_dist_1_Ciphertext_Matrix = new int[8][8];   //der String  dist(row[0],row[i])  als Matrix
                 int[][] DES_dist_2_Ciphertext_Matrix = new int[8][8];   //der String  dist(row[i-1],row[i])  als Matrix
                 
                 int[][] DES_m_active_SBoxes             = new int[16][48];  //Matrix der aktiven S-Boxen
                 int[][] DES_m_oplus_Delta_active_SBoxes = new int[16][48];  //Matrix der aktiven S-Boxen                 
                 int[][] DES_active_SBoxes               = new int[16][48];  //Matrix der aktiven S-Boxen
                 
                 int[][] DES_active_SBoxes_in_panel         = new int[16][8];   //Farb-Matrix der aktiven S-Boxen
                 int[][] DES_active_SBoxes_in_panel_TEST    = {
                                                                {1,1,0,0,1,1,0}, {1,1,0,0,0,1,1}, {0,0,0,0,1,1,0}, {0,1,0,0,1,1,0},
                                                                {1,0,0,0,1,1,0}, {0,1,0,0,1,1,0}, {1,1,0,0,1,1,0}, {0,1,0,0,1,1,0},
                                                                {0,0,0,0,1,1,0}, {1,1,0,0,0,1,1}, {0,0,0,0,1,1,0}, {0,1,0,0,1,1,0},
                                                                {0,0,1,0,0,1,0}, {1,1,0,1,0,0,0}, {1,0,0,0,0,0,0}, {0,1,0,0,1,1,1}
                                                                };
                                                                		
         // "DES_m[8]" = Bei Selektion von "Fixed Point" die User-Eingabe im Panel "Anti-/ Fixed Points" 
		 int[] DES_m8 = new int[32];
/*		 int[] m8_0 = { 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0 };
		 int[] m8_1 = { 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1 };
		 int[] m8_10 = { 1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0 };
		 int[] m8_11 = { 1,1,0,0,1,0,1,0,1,0,0,1,1,1,0,0,0,1,0,1,0,0,0,1,0,0,0,1,1,1,0,1 };
*/
                 // "DES_fixedpoint_sequence" = Bei Selektion von "FIXED POINT" die Sequenz m[8],m[9], ..., m[16], m[17]
                 // DES_fixedpoint = DES(k, m[8])
		 int[][] DES_fixedpoint_sequence = new int[10][32];
		 int[] DES_fixedpoint            = new int[64];
		
                 // "DES_m[8]" = Bei Selektion von "ANTI-FIXED POINT" die User-Eingabe im Panel "Anti-/ Fixed Points"
                 // "DES_anti_fixedpoint_sequence" = Bei Selektion von "ANTI-FIXED POINT" die Sequenz m[8],m[9], ..., m[16], m[17]
                 // DES_anti_fixedpoint = DES(k, m[8])
		 int[] DES_anti_fixedpoint_m8         = new int[32];
		 int[][] DES_anti_fixedpoint_sequence = new int[10][32];
		 int[] DES_anti_fixedpoint            = new int[64];

                 //Initial Permutation IP
		 int[] IP = {
			58,50,42,34,26,18,10,2,60,52,44,36,28,20,12,4,62,54,46,38,30,22,14,6,64,56,48,40,32,24,16,8,
			57,49,41,33,25,17,9,1,59,51,43,35,27,19,11,3,61,53,45,37,29,21,13,5,63,55,47,39,31,23,15,7 };
		
                 //Final Permutation = Inverse of IP
		 int[] FP = {
			40,8,48,16,56,24,64,32,39,7,47,15,55,23,63,31,38,6,46,14,54,22,62,30,37,5,45,13,53,21,61,29,
			36,4,44,12,52,20,60,28,35,3,43,11,51,19,59,27,34,2,42,10,50,18,58,26,33,1,41,9,49,17,57,25 };
	
		//Falls man IP "ignorieren moechte" benutzt man dieses Setting (d.h. man setzt IP:= FP:= id)
/*		 int[] IP = {
			1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,
			33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64 };
		
		 int[] FP = {
			1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,
			33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64 };
*/			
		//Expansions-Funktion E
		 int[] E = {
			32,1,2,3,4,5,4,5,6,7,8,9,8,9,10,11,12,13,12,13,14,15,16,17,16,17,
                        18,19,20,21,20,21,22,23,24,25,24,25,26,27,28,29,28,29,30,31,32,1 };
	
		
                //Permutation P  (gebraucht in    f(R,K_i) := P( S( E(R)+K_i ) ) )
		 int[] P = { 16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5, 18, 31, 10,
                             2, 8, 24, 14, 32, 27, 3, 9, 19, 13, 30, 6, 22, 11, 4, 25 };
		
		
                // Alle 8 S-Boxen
		 int[][][] S = {
			{
			{1,1,1,0,0,1,0,0,1,1,0,1,0,0,0,1,0,0,1,0,1,1,1,1,1,0,1,1,1,0,0,0,0,0,1,1,1,0,1,0,0,1,1,0,1,1,0,0,0,1,0,1,1,0,0,1,0,0,0,0,0,1,1,1},
			{0,0,0,0,1,1,1,1,0,1,1,1,0,1,0,0,1,1,1,0,0,0,1,0,1,1,0,1,0,0,0,1,1,0,1,0,0,1,1,0,1,1,0,0,1,0,1,1,1,0,0,1,0,1,0,1,0,0,1,1,1,0,0,0},
			{0,1,0,0,0,0,0,1,1,1,1,0,1,0,0,0,1,1,0,1,0,1,1,0,0,0,1,0,1,0,1,1,1,1,1,1,1,1,0,0,1,0,0,1,0,1,1,1,0,0,1,1,1,0,1,0,0,1,0,1,0,0,0,0},
			{1,1,1,1,1,1,0,0,1,0,0,0,0,0,1,0,0,1,0,0,1,0,0,1,0,0,0,1,0,1,1,1,0,1,0,1,1,0,1,1,0,0,1,1,1,1,1,0,1,0,1,0,0,0,0,0,0,1,1,0,1,1,0,1},
			},
			{
			{1,1,1,1,0,0,0,1,1,0,0,0,1,1,1,0,0,1,1,0,1,0,1,1,0,0,1,1,0,1,0,0,1,0,0,1,0,1,1,1,0,0,1,0,1,1,0,1,1,1,0,0,0,0,0,0,0,1,0,1,1,0,1,0},
			{0,0,1,1,1,1,0,1,0,1,0,0,0,1,1,1,1,1,1,1,0,0,1,0,1,0,0,0,1,1,1,0,1,1,0,0,0,0,0,0,0,0,0,1,1,0,1,0,0,1,1,0,1,0,0,1,1,0,1,1,0,1,0,1},
			{0,0,0,0,1,1,1,0,0,1,1,1,1,0,1,1,1,0,1,0,0,1,0,0,1,1,0,1,0,0,0,1,0,1,0,1,1,0,0,0,1,1,0,0,0,1,1,0,1,0,0,1,0,0,1,1,0,0,1,0,1,1,1,1},
			{1,1,0,1,1,0,0,0,1,0,1,0,0,0,0,1,0,0,1,1,1,1,1,1,0,1,0,0,0,0,1,0,1,0,1,1,0,1,1,0,0,1,1,1,1,1,0,0,0,0,0,0,0,1,0,1,1,1,1,0,1,0,0,1}
			},
			{
			{1,0,1,0,0,0,0,0,1,0,0,1,1,1,1,0,0,1,1,0,0,0,1,1,1,1,1,1,0,1,0,1,0,0,0,1,1,1,0,1,1,1,0,0,0,1,1,1,1,0,1,1,0,1,0,0,0,0,1,0,1,0,0,0},
			{1,1,0,1,0,1,1,1,0,0,0,0,1,0,0,1,0,0,1,1,0,1,0,0,0,1,1,0,1,0,1,0,0,0,1,0,1,0,0,0,0,1,0,1,1,1,1,0,1,1,0,0,1,0,1,1,1,1,1,1,0,0,0,1},
			{1,1,0,1,0,1,1,0,0,1,0,0,1,0,0,1,1,0,0,0,1,1,1,1,0,0,1,1,0,0,0,0,1,0,1,1,0,0,0,1,0,0,1,0,1,1,0,0,0,1,0,1,1,0,1,0,1,1,1,0,0,1,1,1},
			{0,0,0,1,1,0,1,0,1,1,0,1,0,0,0,0,0,1,1,0,1,0,0,1,1,0,0,0,0,1,1,1,0,1,0,0,1,1,1,1,1,1,1,0,0,0,1,1,1,0,1,1,0,1,0,1,0,0,1,0,1,1,0,0}
			},
			{
			{0,1,1,1,1,1,0,1,1,1,1,0,0,0,1,1,0,0,0,0,0,1,1,0,1,0,0,1,1,0,1,0,0,0,0,1,0,0,1,0,1,0,0,0,0,1,0,1,1,0,1,1,1,1,0,0,0,1,0,0,1,1,1,1},
			{1,1,0,1,1,0,0,0,1,0,1,1,0,1,0,1,0,1,1,0,1,1,1,1,0,0,0,0,0,0,1,1,0,1,0,0,0,1,1,1,0,0,1,0,1,1,0,0,0,0,0,1,1,0,1,0,1,1,1,0,1,0,0,1},
			{1,0,1,0,0,1,1,0,1,0,0,1,0,0,0,0,1,1,0,0,1,0,1,1,0,1,1,1,1,1,0,1,1,1,1,1,0,0,0,1,0,0,1,1,1,1,1,0,0,1,0,1,0,0,1,0,1,0,0,0,0,1,0,0},
			{0,0,1,1,1,1,1,1,0,0,0,0,0,1,1,0,1,0,1,0,0,0,0,1,1,1,0,1,1,0,0,0,1,0,0,1,0,1,0,0,0,1,0,1,1,0,1,1,1,1,0,0,0,1,1,1,0,0,1,0,1,1,1,0}
			},
			{
			{0,0,1,0,1,1,0,0,0,1,0,0,0,0,0,1,0,1,1,1,1,0,1,0,1,0,1,1,0,1,1,0,1,0,0,0,0,1,0,1,0,0,1,1,1,1,1,1,1,1,0,1,0,0,0,0,1,1,1,0,1,0,0,1},
			{1,1,1,0,1,0,1,1,0,0,1,0,1,1,0,0,0,1,0,0,0,1,1,1,1,1,0,1,0,0,0,1,0,1,0,1,0,0,0,0,1,1,1,1,1,0,1,0,0,0,1,1,1,0,0,1,1,0,0,0,0,1,1,0},
			{0,1,0,0,0,0,1,0,0,0,0,1,1,0,1,1,1,0,1,0,1,1,0,1,0,1,1,1,1,0,0,0,1,1,1,1,1,0,0,1,1,1,0,0,0,1,0,1,0,1,1,0,0,0,1,1,0,0,0,0,1,1,1,0},
			{1,0,1,1,1,0,0,0,1,1,0,0,0,1,1,1,0,0,0,1,1,1,1,0,0,0,1,0,1,1,0,1,0,1,1,0,1,1,1,1,0,0,0,0,1,0,0,1,1,0,1,0,0,1,0,0,0,1,0,1,0,0,1,1}
			},
			{
			{1,1,0,0,0,0,0,1,1,0,1,0,1,1,1,1,1,0,0,1,0,0,1,0,0,1,1,0,1,0,0,0,0,0,0,0,1,1,0,1,0,0,1,1,0,1,0,0,1,1,1,0,0,1,1,1,0,1,0,1,1,0,1,1},
			{1,0,1,0,1,1,1,1,0,1,0,0,0,0,1,0,0,1,1,1,1,1,0,0,1,0,0,1,0,1,0,1,0,1,1,0,0,0,0,1,1,1,0,1,1,1,1,0,0,0,0,0,1,0,1,1,0,0,1,1,1,0,0,0},
			{1,0,0,1,1,1,1,0,1,1,1,1,0,1,0,1,0,0,1,0,1,0,0,0,1,1,0,0,0,0,1,1,0,1,1,1,0,0,0,0,0,1,0,0,1,0,1,0,0,0,0,1,1,1,0,1,1,0,1,1,0,1,1,0},
			{0,1,0,0,0,0,1,1,0,0,1,0,1,1,0,0,1,0,0,1,0,1,0,1,1,1,1,1,1,0,1,0,1,0,1,1,1,1,1,0,0,0,0,1,0,1,1,1,0,1,1,0,0,0,0,0,1,0,0,0,1,1,0,1}
			},			
			{
			{0,1,0,0,1,0,1,1,0,0,1,0,1,1,1,0,1,1,1,1,0,0,0,0,1,0,0,0,1,1,0,1,0,0,1,1,1,1,0,0,1,0,0,1,0,1,1,1,0,1,0,1,1,0,1,0,0,1,1,0,0,0,0,1},
			{1,1,0,1,0,0,0,0,1,0,1,1,0,1,1,1,0,1,0,0,1,0,0,1,0,0,0,1,1,0,1,0,1,1,1,0,0,0,1,1,0,1,0,1,1,1,0,0,0,0,1,0,1,1,1,1,1,0,0,0,0,1,1,0},
			{0,0,0,1,0,1,0,0,1,0,1,1,1,1,0,1,1,1,0,0,0,0,1,1,0,1,1,1,1,1,1,0,1,0,1,0,1,1,1,1,0,1,1,0,1,0,0,0,0,0,0,0,0,1,0,1,1,0,0,1,0,0,1,0},
			{0,1,1,0,1,0,1,1,1,1,0,1,1,0,0,0,0,0,0,1,0,1,0,0,1,0,1,0,0,1,1,1,1,0,0,1,0,1,0,1,0,0,0,0,1,1,1,1,1,1,1,0,0,0,1,0,0,0,1,1,1,1,0,0}
			},
			{
			{1,1,0,1,0,0,1,0,1,0,0,0,0,1,0,0,0,1,1,0,1,1,1,1,1,0,1,1,0,0,0,1,1,0,1,0,1,0,0,1,0,0,1,1,1,1,1,0,0,1,0,1,0,0,0,0,1,1,0,0,0,1,1,1},
			{0,0,0,1,1,1,1,1,1,1,0,1,1,0,0,0,1,0,1,0,0,0,1,1,0,1,1,1,0,1,0,0,1,1,0,0,0,1,0,1,0,1,1,0,1,0,1,1,0,0,0,0,1,1,1,0,1,0,0,1,0,0,1,0},
			{0,1,1,1,1,0,1,1,0,1,0,0,0,0,0,1,1,0,0,1,1,1,0,0,1,1,1,0,0,0,1,0,0,0,0,0,0,1,1,0,1,0,1,0,1,1,0,1,1,1,1,1,0,0,1,1,0,1,0,1,1,0,0,0},
			{0,0,1,0,0,0,0,1,1,1,1,0,0,1,1,1,0,1,0,0,1,0,1,0,1,0,0,0,1,1,0,1,1,1,1,1,1,1,0,0,1,0,0,1,0,0,0,0,0,0,1,1,0,1,0,1,0,1,1,0,1,0,1,1}
			}
		};
                             
                
		//Rotiert das array D zyklisch durch Links-Shift um i > 0 Einheiten
		public int[] cyclic_rot(int i, int[] D)
		{
			int L = D.length;
			
			int[] doubled_D = new int[2*L];
			int[] return_D  = new int[L];
			
			for(int j=0; j<L; j++)
				doubled_D[j]=D[j];
			
			for(int j=0; j<L; j++)
				doubled_D[j+L]=D[j];
			
			int shift = i%L;
			for(int j=0; j<L; j++)
 					return_D[j]=doubled_D[j+shift];
		
			return return_D;			
		}
		
	
		//Selektiert aus dem array "array" die Werte, dessen Position in "position" definiert ist
		public int[] select(int[] array, int[] position)
		{
			int L = array.length;
			int R = position.length;
			int[] r_vector = new int[R];
			
			//"-3" dient der Fehler-Erkennung
			for (int i=0; i<R; i++) 
				r_vector[i]= -3;
			
			if(L >= R)
			{	
				for (int j=0; j<R; j++)
					r_vector[j] = array[position[j]-1];
			}
			return r_vector;			
		}
	

		//Expands a 32 Bit-Vektor to a 48 Bit-Vektor using the Expansions-Funktion E
		public int[] expand_32bit(int[] original_32bit)
		{
			int[] expanded_48Bit = new int[48];
			
			for(int i=1; i<5; i++) //Inside tabular (ohne Ueberkreuz-Zuweisungen)
				for(int k=0; k<8; k++)
					expanded_48Bit[i+6*k] = original_32bit[i-1+4*k];

			expanded_48Bit[0] = original_32bit[31]; // li Rand
			expanded_48Bit[47] = original_32bit[0]; // re Rand
			
			for(int k=0; k<7; k++) //right column
				expanded_48Bit[5+6*k] = original_32bit[4+4*k];
			
			for(int k=1; k<8; k++) //left column
				expanded_48Bit[6*k] = original_32bit[-1+4*k];
			
			return expanded_48Bit;
		}

		//Berechnet "array1 XOR array2"
	        public	int[] exclusive_or(int[] array1, int[] array2) {
			int L = array1.length;
			int R = array2.length;
			int[] r_array = new int[L];
			int[] e_array = { -4,-4,-4,-4 };        // -4 dient der Fehlererkennung
			
			if(L == R && L > 0) {
				for (int i=0; i<L; i++) 
				{
					r_array [i] = array1[i] ^ array2[i];
				}
			        return r_array;
			}
			else 
                               return e_array;
		}
		
		//Berechnet den 32-BitVektor y:=(S1(x1),...,S8(x8)), wobei    input :=x1...x8     ein 48-Bitvektor ist
		public int[] lookup_SBoxes(int[] input) {
			int L = input.length;
			int[] r_array = new int[32];
			for (int j=0; j<32; j++) 
                                r_array[j] = -7;  // -7 fuer Fehlererkennung 
			
			if(L  == 48) {
				for (int j=0; j<8; j++) {
					int zeile = 2*input[6*j] + 1*input[6*j+5];
					int spalte = 8*input[6*j+1] + 4*input[6*j+2] + 2*input[6*j+3] + 1*input[6*j+4];
					
					for (int i=0; i<4; i++) {
						r_array[4*j+i] = S[j][zeile][4*spalte + i];
					}
				}
			}	
			return r_array;
		}
		
		//Setzt den DES_key durch die User-Eingabe, falls User einen key waehlt aus k0, ....., k15, manual key
		public void set_a_key(int selected_user_key)
		{			
			switch (selected_user_key)
			{
				case  0: DES_key = key_k0;  break;
                                case  3: DES_key = key_k3;  break;
				case  5: DES_key = key_k5;  break;
                                case  6: DES_key = key_k6;  break;
                                case  9: DES_key = key_k9;  break;
				case 10: DES_key = key_k10; break;
                                case 12: DES_key = key_k12; break;
				case 15: DES_key = key_k15; break;
                                case 16: DES_key = key_user; break;
                                //default: DES_key = key_Schneier;
			}
		}
		
		//Berechnet die CD-Matrix (Ci,Di)_i (i=1..16) zur Generierung der Rundenkeys K1...K16
		public void evaluate_CD()
		{
			int[] CD_init = new int[56]; //CD_init = conc(C_init, D_init)
			int[] C_init  = new int[28];
			int[] D_init  = new int[28];
			
			//definiert das Tupel (C0,D0)
			//"-1" dient der Fehler-Ekennung
			for (int i=0; i<56; i++) { CD_init[i]= -1; }
			CD_init = select(DES_key, PC1);
			
			for (int j=0; j<28; j++)	//definiert C0 and D0 using (C0,D0)
			{
				C_init[j] = CD_init[j];
				D_init[j] = CD_init[j+28];
			}
			
			for (int u=0; u<28; u++) 
			{
				C[0][u] = C_init[u]; 
				D[0][u] = D_init[u];
			}
			
			for (int i=1; i<17; i++)
			{
				C[i] = cyclic_rot(v[i-1], C[i-1]);
				D[i] = cyclic_rot(v[i-1], D[i-1]);
			}
			
			//definiert die Matrix (C[i],D[i]), notwendig um die Roundkeys K_i zu berechnen
			for (int k=0; k<17; k++)
			{
				for (int t=0; t<28; t++)
				{
					CD[k][t]    = C[k][t];
					CD[k][t+28] = D[k][t];
				}
			}	
                        
                        //definiert die 34x28 Matrix C0D0,....,C16D16 (displayed in "CD_Matrix")
                        for (int z=0; z<17; z++)
                                for (int s=0; s<28; s++)
                                {
                                        CD_mix[2*z][s]   = CD[z][s];
                                        CD_mix[2*z+1][s] = CD[z][28+s];
                                }
		}
                
		//Berechnet die Rundenkeys K1...K16 aus der CD-Matrix (+++ den reversed Rundenkey K16 ... K1)
	      public void evaluate_roundkeys()
		{
			for (int r=0; r<16; r++)
				{
					//"-2" dient der Fehler-Erkennung
					for(int y=0; y<48; y++)
						DES_K[r][y] = -2;					
				}
			
			for (int j=0; j<16; j++)
				DES_K[j] = select(CD[j+1],PC2);
			
			for (int j=0; j<16; j++)
				DES_reversed_K[j] = DES_K[15-j];
		}
		
		
		//Berechnet die Cipher-Sequenz m0...m17 bzgl. dem Plaintext und DES-key
                //abhaengig von der Auswahl ENCRYPT/DECRYPT
		public void evaluate_cipher_sequence() 
        {
			int[][] init_cipher_sequence = new int[18][32];
			//Initialisierung
			//for (int p=0; p<18; p++)
			//	for (int t=0; t<32; t++)
			//		init_cipher_sequence[p][t] = 0;
                        intialize_matrix(init_cipher_sequence);
			
                        //Initialisierung mit der Initial Permutation "IP"
			int[] m0_m1 = new int[64];
			m0_m1 = select(DES_plaintext,IP);
			
			DES_cipher_sequence = init_cipher_sequence;
			
			for (int j=0; j<32; j++) {
				DES_cipher_sequence[0][j] = m0_m1[j];
				DES_cipher_sequence[1][j] = m0_m1[32+j];
			}
			
			int[][] K = new int[16][48];
			if (DES_action_type == 0) 
				K = DES_K; 
			else 
				K = DES_reversed_K;
			
			//JOptionPane.showMessageDialog(null, "Evaluating m[0]...m[17] && Action-Type = " + DES_action_type);
                        
			for (int i=2; i<18; i++) {
				int[] input_SBoxes = exclusive_or(expand_32bit(DES_cipher_sequence[i-1]), K[i-2]);
                                //int J = i-1;
                                /*System.out.println("Input SBoxes in Round[" + J + "]");
                                for(int r=1; r<=8; r++)
                                {       System.out.print("SBox[" + r + "]: ");
                                        for(int s=0; s<6; s++)
                                                System.out.print(input_SBoxes[6*(r-1)+s] + " ");
                                        System.out.println("");
                                }
                                System.out.println("");*/
				int[] fmK = select(lookup_SBoxes(input_SBoxes),P);
				DES_cipher_sequence[i] = exclusive_or(DES_cipher_sequence[i-2],fmK);       
			}
		}

                //********* DKA *********** DKA ************ DKA *********** DKA **************
                //Berechnet die 16x8 Delta-Cipher-Matrix   [m0, ..., m_17] \oplus [(m+\Delta)0, ..., (m+\Delta)17]
                public void evaluate_active_SBoxes()
                {        
                       System.out.println("INSIDE evaluate_active_SBoxes();");
                       System.out.println("m_Plaintext =? 1101^16: ");
                       for(int y=0; y<8; y++)
                       {
                               for(int e=0; e<8; e++)
                                       System.out.print(DES_m_Plaintext[8*y+e] + " ");
                               System.out.println("");
                       }
                                       
                        int[] m0_m1 = select(DES_m_Plaintext,IP);						
                        for (int j=0; j<32; j++) {
                            DES_m_cipher_sequence[0][j] = m0_m1[j];
                            DES_m_cipher_sequence[1][j] = m0_m1[32+j];
                        }
			        
                        
                    for (int i=2; i<18; i++) 
                    {       
                        int[] input_SBoxes = exclusive_or(expand_32bit(DES_m_cipher_sequence[i-1]), DES_K[i-2]);
                                DES_m_active_SBoxes[i-2] = input_SBoxes;
                                /*int J = i-1;
                                System.out.println("m-Input SBoxes in Round[" + J + "]");
                                for(int r=1; r<=8; r++)
                                {       System.out.print("SBox[" + r + "]: ");
                                        for(int s=0; s<6; s++)
                                                System.out.print(input_SBoxes[6*(r-1)+s] + " ");
                                        System.out.println("");
                                }*/
                        int[] fmK = select(lookup_SBoxes(input_SBoxes),P);
                        DES_m_cipher_sequence[i] = exclusive_or(DES_m_cipher_sequence[i-2],fmK);
                    }
                        
                //******* SAME **** SAME ***** SAME ****** SAME *****
                       
                    m0_m1 = select(DES_m_oplus_Delta_Plaintext,IP);						
                    for (int j=0; j<32; j++) {
                        DES_m_oplus_Delta_cipher_sequence[0][j] = m0_m1[j];
                        DES_m_oplus_Delta_cipher_sequence[1][j] = m0_m1[32+j];
                    }
	                        
		for (int i=2; i<18; i++) 
        {
		        int[] input_SBoxes = exclusive_or(expand_32bit(DES_m_oplus_Delta_cipher_sequence[i-1]), DES_K[i-2]);
                DES_m_oplus_Delta_active_SBoxes[i-2] = input_SBoxes;
/*                        int J = i-1;
                                System.out.println("m_OPLUS DELTA-Input SBoxes in Round[" + J + "]");
                                for(int r=1; r<=8; r++)
                                {       System.out.print("SBox[" + r + "]: ");
                                        for(int s=0; s<6; s++)
                                                System.out.print(input_SBoxes[6*(r-1)+s] + " ");
                                        System.out.println("");
                                }
*/
			int[] fmK = select(lookup_SBoxes(input_SBoxes),P);
			DES_m_oplus_Delta_cipher_sequence[i] = exclusive_or(DES_m_oplus_Delta_cipher_sequence[i-2],fmK);
		} 

                for(int r=0; r<16; r++)
                        for(int c=0; c<48; c++)
                                DES_active_SBoxes[r][c] = DES_m_active_SBoxes[r][c]^DES_m_oplus_Delta_active_SBoxes[r][c];
                                
                System.out.println("BIG-Active S-Boxen:");
                for(int a=0; a<5; a++)
                {       //int A = a+1;
                        //System.out.print("Runde[" +A+ "]:  ");
                        for(int b=0; b<5; b++)
                        {
                                for(int c=0; c<6; c++)
                                        System.out.print(DES_active_SBoxes[a][6*b+c] + " ");
                                System.out.print("  ");
                        }
                        System.out.println("");
                }
                System.out.println("");               
                                
                                
                                
                                
                DES_active_SBoxes_in_panel = transform_big_to_small_Activity_Matrix(DES_active_SBoxes);
                
                System.out.println("Active S-Boxen sind:");
                for(int a=0; a<16; a++)
                {       int A = a+1;
                        System.out.print("Runde[" +A+ "]:  ");
                        for(int b=0; b<8; b++)
                                System.out.print(DES_active_SBoxes_in_panel[a][b] + " ");
                        System.out.println("");
                }
                System.out.println("");

                                
	}
	//********* DKA *********** DKA ************ DKA *********** DKA **************
	

		//Berechnet die Cipher-Matrix   [ DES(k, p+e_i) ]_i
		public void evaluate_ciphertext_matrix() {
			//DES_Plaintext_Matrix := Die 65x64 Matrix der Plaintexte "p + e_i"
			//Initialisierung
			for (int z=0; z<65; z++)
				for (int s=0; s<64; s++)
					DES_Plaintext_Matrix[z][s] = DES_plaintext[s];
				
			for (int z=1; z<65; z++)
				DES_Plaintext_Matrix[z][64-z] = 1^DES_Plaintext_Matrix[z][64-z];
			
			int[][] cipher_sequence_temporarily = new int[18][32];
			int[] m0_m1 = new int[64];
                        
			int[][] K = new int[16][48];
			if (DES_action_type == 0)
                    K = DES_K; 
                else
                {
                    K = DES_reversed_K;
                }
                                
                        //JOptionPane.showMessageDialog(null, "Evaluating DES(k,p+e_i) && Action-Type = " + DES_action_type);
		
			//Initialisierung
            intialize_matrix(cipher_sequence_temporarily);
			
			for (int k=0; k<65; k++) {	
				m0_m1 = select(DES_Plaintext_Matrix[k],IP);
				
				for (int j=0; j<32; j++) {
					cipher_sequence_temporarily[0][j] = m0_m1[j];
					cipher_sequence_temporarily[1][j] = m0_m1[32+j];
				}
				
				//Befuellung aus der Matrix "cipher_sequence_temporarily"
				for (int i=2; i<18; i++) {
					int[] input_SBoxes = exclusive_or(expand_32bit(cipher_sequence_temporarily[i-1]),K[i-2]);
					int[] fmK = select(lookup_SBoxes(input_SBoxes),P);
					cipher_sequence_temporarily[i] = exclusive_or(cipher_sequence_temporarily[i-2],fmK);
				}
				
				int[][] flipped_last2_cipher_rounds_matrix = new int[65][64];
				for (int a=0; a<32; a++)
					flipped_last2_cipher_rounds_matrix[k][a] = cipher_sequence_temporarily[17][a];
					
				for (int a=32; a<64; a++)
					flipped_last2_cipher_rounds_matrix[k][a] = cipher_sequence_temporarily[16][a-32];
					
				DES_Ciphertext_Matrix[k] = select(flipped_last2_cipher_rounds_matrix[k],FP);
			}	
		}
                
                //Berechnet die 8x8 Distanz-Matrix   [ distance( DES_Ciphertext_Matrix[row_0], DES_Ciphertext_Matrix[row_i]) ]_i
                // dist(INPUTS) = 1
                public void evaluate_dist_1_DES_Ciphertext_Matrix() {
                        //Initialisierung
                        for (int row=0; row<8; row++)
                                for (int col=0; col<8; col++) 
                                {
                                        DES_dist_1_Ciphertext_Matrix[row][col] = 0;
                                        DES_dist_1_Ciphertext_Vector[8*row + col] = 0;
                                }
                                
                        for (int row=0; row<8; row++)
                                for (int col=0; col<8; col++)
                                {
                                    for (int count=0; count<64; count++)
                                    {
                                         DES_dist_1_Ciphertext_Matrix[row][col] = DES_dist_1_Ciphertext_Matrix[row][col] +
                                                (DES_Ciphertext_Matrix[0][count]^DES_Ciphertext_Matrix[8*row + col + 1][count]); 
                                         DES_dist_1_Ciphertext_Vector[8*row + col] = DES_dist_1_Ciphertext_Matrix[row][col];
                                    }
                                }
                }
                
                //Berechnet die 8x8 Distanz-Matrix   [ distance(DES_Ciphertext_Matrix[row_i-1], DES_Ciphertext_Matrix[row_i]) ]_i
                // dist(INPUTS) = 2
                public void evaluate_dist_2_DES_Ciphertext_Matrix() {
                        //Initialisierung
                        //for (int row=0; row<8; row++)
                        //       for (int col=0; col<8; col++)
                        //               DES_dist_2_Ciphertext_Matrix[row][col] = 0;
                        intialize_matrix(DES_dist_2_Ciphertext_Matrix);
                        
                        for (int row=0; row<8; row++)
                                for (int col=0; col<8; col++)
                                {
                                    for (int count=0; count<64; count++)
                                         DES_dist_2_Ciphertext_Matrix[row][col] = DES_dist_2_Ciphertext_Matrix[row][col] +
                                                (DES_Ciphertext_Matrix[8*row + col][count]^DES_Ciphertext_Matrix[8*row + col + 1][count]);                                              
                                }
                }

            //Evaluates the Fixed Point-Sequence m[8]...m[17]
            public void evaluate_fixpoint_sequence() {
            //JOptionPane.showMessageDialog(null, "Evaluating the Fixed Point-Sequence m[8]...m[17]" );
			//DES_action_type = 0;
			int[][] init_fixedpoint_sequence = new int[10][32];
			//Initialisierung
			//for (int p=0; p<10; p++) 
			//	for (int t=0; t<32; t++)
			//		init_fixedpoint_sequence[p][t] = 0;
            intialize_matrix(init_fixedpoint_sequence);			
			DES_fixedpoint_sequence = init_fixedpoint_sequence;
			                     

			for (int j=0; j<32; j++) {
				DES_fixedpoint_sequence[0][j] = DES_m8[j];
				DES_fixedpoint_sequence[1][j] = DES_m8[j];
                }
			
			for (int i=2; i<10; i++) {
				int[] input_SBoxes = exclusive_or(expand_32bit(DES_fixedpoint_sequence[i-1]),DES_K[i+6]);
				int[] fmK = select(lookup_SBoxes(input_SBoxes),P);
				DES_fixedpoint_sequence[i] = exclusive_or(DES_fixedpoint_sequence[i-2],fmK);
                }
            }
		
           // Berechnet die Anti-Fixed Point Sequenz  m[8],m[9], ...., m[16],m[17]    wobei m[9] = 1^m[8]
	       public void evaluate_anti_fixedpoint_sequence() {
                       //JOptionPane.showMessageDialog(null, "Evaluating the Anti-FP sequence m[8]...m[17]" );
			//DES_action_type = 1; 
			int[][] init_anti_fixedpoint_sequence = new int[10][32];
			
			//Initialisierung
			//for (int p=0; p<10; p++)
			//	for (int t=0; t<32; t++)
			//		init_anti_fixedpoint_sequence[p][t] = 0;
			intialize_matrix(init_anti_fixedpoint_sequence);
			DES_anti_fixedpoint_sequence = init_anti_fixedpoint_sequence;
                        
			for (int j=0; j<32; j++) {
				DES_anti_fixedpoint_sequence[0][j] = DES_m8[j];
				DES_anti_fixedpoint_sequence[1][j] = 1^DES_m8[j];
			}
			
			for (int i=2; i<10; i++) {
				int[] input_SBoxes = exclusive_or(expand_32bit(DES_anti_fixedpoint_sequence[i-1]),DES_K[i+6]);
				int[] fmK = select(lookup_SBoxes(input_SBoxes),P);
				DES_anti_fixedpoint_sequence[i] = exclusive_or(DES_anti_fixedpoint_sequence[i-2],fmK);
			}
		}
		
        //Berechnet DES(k, m[8]), also den Fixed Point unter der Eingabe (k, m[8])
		public void evaluate_fixedpoint() {
			//DES_action_type = 0;
            //JOptionPane.showMessageDialog(null, "Evaluating FP && Action-Type = " + DES_action_type);
			int[] last2_fixedpoint_rounds = new int[64];
			for (int i=0; i<32; i++) {
				last2_fixedpoint_rounds[i] = DES_fixedpoint_sequence[9][i];
				last2_fixedpoint_rounds[32+i] = DES_fixedpoint_sequence[8][i];
			}
			DES_fixedpoint = select(last2_fixedpoint_rounds,FP);
		}
		
        //Berechnet DES(k, m[8], 1^m[8]), also den Anti-Fixed Point unter der Eingabe (k, m[8])
		public void evaluate_anti_fixedpoint() {
			//DES_action_type = 0;
            //JOptionPane.showMessageDialog(null, "Evaluating the Anti-FP && Action-Type = " + DES_action_type);
			int[] last2_anti_fixedpoint_rounds = new int[64];
			for (int i=0; i<32; i++) {
				last2_anti_fixedpoint_rounds[i] = DES_anti_fixedpoint_sequence[9][i];
				last2_anti_fixedpoint_rounds[32+i] = DES_anti_fixedpoint_sequence[8][i];
			}
			DES_anti_fixedpoint = select(last2_anti_fixedpoint_rounds,FP);
                        
                        for (int i=0; i<64; i++)        
                                DES_anti_fixedpoint[i] = 1^DES_anti_fixedpoint[i];
		}
		
                
        //"encrypt()" ist gut fuer ENCRYPT und DECRYPT   ("encrypt()" wird gesteuert durch "DES_action_type")
		public void encrypt() {
                        //JOptionPane.showMessageDialog(null, "Encrypting ... Action-Type = " + DES_action_type);
			int[] flipped_last2_cipher_rounds = new int[64];
			for (int i=0; i<32; i++) {
				flipped_last2_cipher_rounds[i]    = DES_cipher_sequence[17][i];
				flipped_last2_cipher_rounds[32+i] = DES_cipher_sequence[16][i];
			}
			DES_ciphertext = select(flipped_last2_cipher_rounds,FP);
		}
		
        // Diese Funktion wird benutzt, um den manuellen Key auf ungerade Paritaet zu testen
		public int eval_letter_parity(char s) {
			int r=0 ;
			
		        switch (s) {
			        case  '0': r=0; break;
			        case  '1': r=1; break;
			        case  '2': r=1; break;
			        case  '3': r=0; break;
			        case  '4': r=1; break;
			        case  '5': r=0; break;
			        case  '6': r=0; break;
			        case  '7': r=1; break;
			        case  '8': r=1; break;
			        case  '9': r=0; break;
			        case  'A': r=0; break;
			        case  'B': r=1; break;
			        case  'C': r=0; break;
			        case  'D': r=1; break;
			        case  'E': r=1; break;
			        case  'F': r=0; break;
			        }
			        return r;
		}
    

    //Entfernt alles aus einem String alles was kein Binary-Digit ist und gibt den reduzierten String zurueck
    public String cleanTheBinString(String input)
    {
        String result = "";
        if(input !=null && !input.isEmpty())
        {
            
            for(int k=0; k<input.length(); k++)
            {
                if(!(input.charAt(k)=='0' || input.charAt(k)=='1') || input.charAt(k)==' ')
                {}
                else
                    result = result + Character.toString(input.charAt(k));
            }
    
        }
        
        return result;  
    }
    
    
    
    
        //Entfernt alles aus einem String alles was kein Hex-Digit ist und gibt den reduzierten String zurueck  //ESSLINGER
        public String cleanTheString(String input)          
        {
            String result = "";
            if(input !=null && !input.isEmpty())
            {
            
                for(int k=0; k<input.length(); k++)
                {
                    if(!(input.charAt(k)=='0' || //unallowed characters
                         input.charAt(k)=='1' || 
                         input.charAt(k)=='2' || 
                         input.charAt(k)=='3' ||        
                         input.charAt(k)=='4' || 
                         input.charAt(k)=='5' || 
                         input.charAt(k)=='6' || 
                         input.charAt(k)=='7' ||
                         input.charAt(k)=='8' || 
                         input.charAt(k)=='9' ||
                         input.charAt(k)=='A' || 
                         input.charAt(k)=='B' ||
                         input.charAt(k)=='C' || 
                         input.charAt(k)=='D' || 
                         input.charAt(k)=='E' ||
                         input.charAt(k)=='F'))
                        {}
                    else
                        result = result + Character.toString(input.charAt(k));
                
                }
            
            }
        
            return result;  
        }
    
    
    
                
                // Wandelt die 16 Digit-Hex-Eingabe des Users um in einen 64 Bit integer-array 
                public void convert_Hex_To_Binary(String hex)
                {                             
                      String binary_string = hexToBinary(hex,true);  
                      int [] data = new int[64];
                      for(int k=0; k<data.length; k++)
                              data[k] = Character.getNumericValue( binary_string.charAt(k) );
                      key_user = data;      
                }
                
                // Wandelt die Binary-Eingabe um in Hex-Notation  (= char-array)
                public char[] convert_Binary_To_Hex(int[] bin)
                { 
                        int L = bin.length/4; //L = number of hex-digits in string "bin"
                        char[] hex_conversion = new char[L];                      

                        for (int z=0; z<L; z++) 
                        {
                                switch( bin[4*z]*8 + bin[4*z+1]*4 + bin[4*z+2]*2 + bin[4*z+3] )
                                {
                                     case 0:  hex_conversion[z] = '0'; break;    
                                     case 1:  hex_conversion[z] = '1'; break;   
                                     case 2:  hex_conversion[z] = '2'; break;
                                     case 3:  hex_conversion[z] = '3'; break;    
                                     case 4:  hex_conversion[z] = '4'; break;   
                                     case 5:  hex_conversion[z] = '5'; break;
                                     case 6:  hex_conversion[z] = '6'; break;    
                                     case 7:  hex_conversion[z] = '7'; break;   
                                     case 8:  hex_conversion[z] = '8'; break;
                                     case 9:  hex_conversion[z] = '9'; break;
                                     case 10: hex_conversion[z] = 'A'; break;    
                                     case 11: hex_conversion[z] = 'B'; break;   
                                     case 12: hex_conversion[z] = 'C'; break;
                                     case 13: hex_conversion[z] = 'D'; break;    
                                     case 14: hex_conversion[z] = 'E'; break;   
                                     case 15: hex_conversion[z] = 'F'; break;
                                }                               
                        } 
                        //assign the conversion
                        //DES_key = key_user;             ??????????????????????????????????????????   march 2009
                        
                        return hex_conversion;  
                }
                
                // Umwandlung von hex in Binary-Notation. Bei "true" hat die binary-Rueckgabe eine Laenge von 64, ansonsten may be != 64
                public String hexToBinary(String hex, boolean bool_is64bits)
                {
                   String binary_String = "";
                   for (int i = 0; i < hex.length(); i++)
                   {    //Umwandlung: String "hex"-->hex-digit at String-position_i-->integer-->binary
                       String temporary = Integer.toBinaryString(Character.digit(hex.charAt(i),16));// 16 = Basis
                       
                       //Erzeugung einer 4-bit Darstellung durch Hinzufuegen von leading Nullen +++ Concatenation (li->re)
                       binary_String    = binary_String + getLeadingZero(4 - temporary.length()) + temporary;
                   }
                   
                   if(!bool_is64bits)
                       return binary_String;    //false: "binary_String" hat Rueckgabe-Laenge evtl. verschieden von 64
                   else                         //true:  "binary_String" hat Rueckgabe-Laenge von 64
                       return getLeadingZero( 64 - binary_String.length() ) + binary_String;
                }
      
                //Erzeugt String von "num" Nullen 
                public String getLeadingZero(int num)
                {
                     String leading0s = "";
                     for(int k=0; k<num; k++)
                          leading0s = leading0s + "0";
                     return leading0s;
                }
		
                //"key" is written in hex-notation --> Check 2 aufeinenaderfolgende digits
                public boolean check_key_for_parity(char[] key) 
                {
                        boolean parity_check = false;
                        int i;
                        int L = key.length/2;
                        for (i=0; i< L; i++) 
                        {
                            int j = eval_letter_parity(key[2*i]);
                            int k = eval_letter_parity(key[2*i+1]);
				//int m = 2*i+1;
				//JOptionPane.showMessageDialog(null, "key[" + 2*i + "]=" +j+ " and " + "key[" + m + "]=" + k );
                            if (j==k) {
					//i=i+1;
					//JOptionPane.showMessageDialog(null, "INVALID DES-Key! Even parity in byte nr. " + i ); 
					//break;
                                        parity_check = false;
                                        break;                                        
                                        }
                                
                            if (i==L-1) {
					//JOptionPane.showMessageDialog(null, "VALID DES-Key !" );
                                        parity_check = true;
                                        }
                        }//end of for-loop
                        return parity_check;
                }
 
    
    
//******************************* DKA ************************ DKA ****************************************               
             public int [] generate_random_binary_array(int L)
             {
                        Random generator = new Random();
                        int [] random_array = new int[L];
                        for(int k=0; k<L; k++)
                                random_array[k] = generator.nextInt(2);
                         
                          System.out.println("RANDOM-INPUT");
                          for(int t=0; t<L; t++)
                                  //System.out.print(random_array[t] + " ");
                                    System.out.print(DES_input_D[t] + " "); 
                               
                        return random_array;
                        //return DES_input_D;
             }
             
             
             public int [] generate_random_key()
             {
                     int [] random_key = new int[64];
                     random_key = generate_random_binary_array(64);
                                                  
                     int [] xor_of_first_seven_bits = new int[8];
                     for(int j=0; j<8; j++)
                           xor_of_first_seven_bits[j] = 0;
                           
                     for(int k=0; k<8; k++)
                             for(int r=0; r<7; r++)
                                   xor_of_first_seven_bits[k] = xor_of_first_seven_bits[k]^random_key[k*8+r];
                           
                     for(int c=0; c<8; c++)
                         random_key[8*c+7] =  1^xor_of_first_seven_bits[c];
                 
                     return random_key;
                     //return key_k5;
             }
                          

             public int [][] transform_big_to_small_Activity_Matrix(int [][] big_matrix)
             {
                  int [][] small_matrix = new int[16][8];
                  intialize_matrix(small_matrix);                                        
                            
                  for(int R=0; R<16; R++) 
		          for(int C=0; C<8; C++)
                                for(int k=0; k<6; k++)
                                       small_matrix[R][C] = small_matrix[R][C] + big_matrix[R][6*C+k];
                                       
                  for(int row=0; row<16; row++) 
                  for(int col=0; col<8; col++)
                        {
                                if(small_matrix[row][col] == 0)
                                        small_matrix[row][col] = 0;
                                     else
                                        small_matrix[row][col] = 1;  
                        }
                                       
                   return small_matrix;
             }
             
             
             public void intialize_matrix(int [][] matrix)// ************* korrekt ??????
             {                     
                     for(int r=0; r<matrix.length; r++)
                             for(int c=0; c<matrix[0].length; c++)
                                     matrix[r][c] = 0; 
             }

//******************************* DKA ************************ DKA ****************************************		

		
		
		// get-methods *********************************************************************************************
		public int[] 	get_DES_key()                       { return DES_key; }                     //Panels 2+8
		public int[] 	get_DES_Plaintext()                 { return DES_plaintext; }               //Panel 2
		public int[] 	get_DES_Ciphertext()                { return DES_ciphertext; }              //Panel 2
		public int[][] 	get_m0_to_m17()                     { return DES_cipher_sequence; }         //Panel 3
        public int[][] 	get_DES_cipher_Matrix()             { return DES_Ciphertext_Matrix; }       //Panel 4
        public int[][]  get_DES_dist_1_Ciphertext_Matrix()  { return DES_dist_1_Ciphertext_Matrix; }//Panel 5
        public int[][]  get_DES_dist_2_Ciphertext_Matrix()  { return DES_dist_2_Ciphertext_Matrix; }//Panel 5
        public int[][] 	get_DES_Rundenkeys()                { if(DES_action_type == 0)              //Panel 6
                                                                    return DES_K;
                                                                else 
                                                                    return DES_reversed_K;
                                                            }                                     
        public int[][] 	get_DES_reversed_Rundenkeys()       { return DES_reversed_K; }              //Panel 6                                                                       
        public int[] 	get_DES_fixedpoint()                { return DES_fixedpoint; }              //Panel 8
		public int[] 	get_DES_anti_fixedpoint()           { return DES_anti_fixedpoint; }         //Panel 8
		public int[] 	get_DES_m8()                        { return DES_m8; }                      //Panel 8
        public int[][] 	get_m8_to_m17()                     { if(DES_fixed_status==0)               //Panel 8
                                                                    return DES_fixedpoint_sequence; 
                                                                else 
                                                                    return DES_anti_fixedpoint_sequence;
                                                            }  
        public int[][]	get_DES_active_SBoxes()   { return DES_active_SBoxes_in_panel; }            //Panel 9                                                       
		//*****************************************************************************************************8
	

       public void doOperation(int keySelection)
       {
		set_a_key(keySelection);
	
		evaluate_accu_v();
		evaluate_CD();
		evaluate_roundkeys();
		
		evaluate_fixpoint_sequence();
		evaluate_fixedpoint();
		evaluate_anti_fixedpoint_sequence();
		evaluate_anti_fixedpoint();
		
		evaluate_cipher_sequence();
		encrypt();
                //Key_Schneier       = 0123456789ABCDEF
                //plaintext_Schneier = 0123456789ABCDE7
                //cipher_Schreier = c957 4425 6a5e d31d
                //cipher_Schreier = 1100 1001 0101 0111 || 0100 0100 0010 0101 || 0110 1010 0101 1110 || 1101 0011 0001 1101
		evaluate_ciphertext_matrix();
                evaluate_dist_1_DES_Ciphertext_Matrix();  
                evaluate_dist_2_DES_Ciphertext_Matrix();               
                
                evaluate_active_SBoxes();   // MAKES A PROBLEM
                
                
       }

        //************* M A I N ******************************************************************************************
        
	public  static void main (String[] args) {   }     // end of main        
}