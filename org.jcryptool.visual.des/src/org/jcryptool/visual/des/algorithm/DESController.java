package org.jcryptool.visual.des.algorithm;

import java.util.ArrayList;

import org.jcryptool.visual.des.view.Messages;

public class DESController {

    // General Variables
    private DESModel DESMod = null;
    public String[] errMsg = null;

    // Algorithm Variables
    public int Alg_In_Mode = 0;
    public int Alg_In_selectedKey = 0;
    public String Alg_In_manualKey = ""; //$NON-NLS-1$
    public String Alg_In_Data = ""; //$NON-NLS-1$
    public int[][] Alg_Out_M0M17 = null;
    public int[] Alg_Out_M0M17_Dist = null;
    public int[][] Alg_Out_cipherMatrix = null;
    public int[] Alg_Out_cipherMatrix_Dist = null;
    public int[][] Alg_Out_Roundkeys = null;
    public int[][] Alg_Out_CDMatrix = null;
    public String Alg_Out_EncDecResult = null;
    public int[][] Alg_Out_DistMatrix1 = null;
    public int[][] Alg_Out_DistMatrix2 = null;

    // Fixed Point Variables
    public boolean FPoints_In_FixedPoints = true;
    public int FPoints_In_selectedKey = 0;
    public String FPoints_In_M8 = ""; //$NON-NLS-1$
    public String FPoints_Out_AFpoints = ""; //$NON-NLS-1$
    public int[][] FPoints_Out_M8M17 = null;
    public int[] FPoints_Out_Distances = null;

    // SBox Variables
    public String SBox_In_Deltap = ""; //$NON-NLS-1$
    public String SBox_Out_randomm = null;
    public String SBox_Out_randomk = null;
    public int[][] SBox_Out_activeBoxes = null;;

    public DESController() {
        DESMod = new DESModel();
    }

    public int AlgorithmStudy() {
        String strData = ""; //$NON-NLS-1$
        int[] intData = null;
        int[] M0M17_Dist = null;
        int[] cipherM_Dist = null;

        if (checkAlgInput() != 0) {
            return 1;
        }

        strData = DESMod.hexToBinary(this.Alg_In_Data, true);
        intData = new int[64];
        for (int k = 0; k < strData.length(); k++) {
            intData[k] = Character.getNumericValue(strData.charAt(k));
        }
        DESMod.DES_plaintext = intData;

        DESMod.doOperation(this.Alg_In_selectedKey);
        this.Alg_Out_M0M17 = DESMod.get_m0_to_m17();
        M0M17_Dist = new int[this.Alg_Out_M0M17.length];
        for (int i = 1; i < this.Alg_Out_M0M17.length; i++) {
            for (int j = 0; j < this.Alg_Out_M0M17[i].length; j++) {
                if (this.Alg_Out_M0M17[i][j] != this.Alg_Out_M0M17[i - 1][j]) {
                    M0M17_Dist[i]++;
                }
            }
        }
        this.Alg_Out_M0M17_Dist = M0M17_Dist;

        this.Alg_Out_cipherMatrix = DESMod.get_DES_cipher_Matrix();
        cipherM_Dist = new int[this.Alg_Out_cipherMatrix.length];
        for (int i = 1; i < this.Alg_Out_cipherMatrix.length; i++) {
            for (int j = 0; j < this.Alg_Out_cipherMatrix[i].length; j++) {
                if (this.Alg_Out_cipherMatrix[i][j] != this.Alg_Out_cipherMatrix[i - 1][j]) {
                    cipherM_Dist[i]++;
                }
            }
        }
        this.Alg_Out_cipherMatrix_Dist = cipherM_Dist;

        this.Alg_Out_Roundkeys = DESMod.get_DES_Rundenkeys();
        this.Alg_Out_CDMatrix = DESMod.CD_mix;
        this.Alg_Out_EncDecResult = new String(DESMod.convert_Binary_To_Hex(DESMod.DES_ciphertext));
        this.Alg_Out_DistMatrix1 = DESMod.DES_dist_1_Ciphertext_Matrix;
        this.Alg_Out_DistMatrix2 = DESMod.DES_dist_2_Ciphertext_Matrix;

        return 0;
    }

    public int FPointsStudy() {
        String strData = ""; //$NON-NLS-1$
        int[] intData = null;
        int[] dist;

        if (FPoints_In_FixedPoints) {
            DESMod.DES_fixed_status = 0;
        } else {
            DESMod.DES_fixed_status = 1;
        }

        if (checkFPointsInput() != 0) {
            return 1;
        }

        strData = DESMod.hexToBinary(this.FPoints_In_M8, false);

        DESMod.DES_action_type = 0;

        intData = new int[32];
        for (int k = 0; k < intData.length; k++) {
            intData[k] = Character.getNumericValue(strData.charAt(k));
        }

        DESMod.DES_m8 = intData;

        DESMod.doOperation(FPoints_In_selectedKey);

        this.FPoints_Out_M8M17 = DESMod.get_m8_to_m17();

        if (this.FPoints_In_FixedPoints) {
            this.FPoints_Out_AFpoints = new String(DESMod.convert_Binary_To_Hex(DESMod.DES_fixedpoint));
        } else {
            this.FPoints_Out_AFpoints = new String(DESMod.convert_Binary_To_Hex(DESMod.DES_anti_fixedpoint));
        }

        dist = new int[10];
        for (int i = 1; i < this.FPoints_Out_M8M17.length; i++) {
            for (int j = 0; j < this.FPoints_Out_M8M17[i].length; j++) {
                if (this.FPoints_Out_M8M17[i][j] != this.FPoints_Out_M8M17[i - 1][j]) {
                    dist[i]++;
                }
            }
        }

        this.FPoints_Out_Distances = dist;

        return 0;
    }

    public int SBoxStudy() {
        String strData = ""; //$NON-NLS-1$
        int[] intData = null;

        if (this.checkSBoxInput() != 0) {
            return 1;
        }

        strData = DESMod.hexToBinary(this.SBox_In_Deltap, true);
        DESMod.DES_action_type = 0;

        intData = new int[64];
        for (int k = 0; k < intData.length; k++) {
            intData[k] = Character.getNumericValue(strData.charAt(k));
        }

        DESMod.DES_delta_Plaintext = intData;

        DESMod.key_user = DESMod.generate_random_key();

        this.SBox_Out_randomk = new String(DESMod.convert_Binary_To_Hex(DESMod.key_user));
        DESMod.DES_m_Plaintext = DESMod.generate_random_binary_array(64);
        this.SBox_Out_randomm = new String(DESMod.convert_Binary_To_Hex(DESMod.DES_m_Plaintext));

        for (int i = 0; i < intData.length; i++) {
            DESMod.DES_m_oplus_Delta_Plaintext[i] = DESMod.DES_m_Plaintext[i] ^ DESMod.DES_delta_Plaintext[i];
        }

        DESMod.doOperation(16);

        this.SBox_Out_activeBoxes = DESMod.get_DES_active_SBoxes();

        return 0;
    }

    private int checkAlgInput() {
        int err = 0;
        ArrayList<String> errList = new ArrayList<String>();

        // Check manual Key
        if (this.Alg_In_selectedKey == 16) {
            this.Alg_In_manualKey = DESMod.cleanTheString(this.Alg_In_manualKey);
            if (this.Alg_In_manualKey.length() != 16) {
                errList.add(Messages.DESController_12);
                err++;
            }
            if (DESMod.check_key_for_parity(this.Alg_In_manualKey.toCharArray()) == false) {
                errList.add(Messages.DESController_13);
                err++;
            }
            if (this.isHexDigit(this.Alg_In_manualKey) != 0) {
                errList.add(Messages.DESController_14);
                err++;
            }
        }

        // Check Input Data
        this.Alg_In_Data = DESMod.cleanTheString(this.Alg_In_Data);
        if (this.Alg_In_Data.length() != 16) {
            errList.add(Messages.DESController_15);
            err++;
        }
        if (this.isHexDigit(this.Alg_In_Data) != 0) {
            errList.add(Messages.DESController_16);
            err++;
        }

        errMsg = errList.toArray(new String[errList.size()]);

        return err;

    }

    private int checkFPointsInput() {
        int err = 0;
        ArrayList<String> errList = new ArrayList<String>();
        int len = 0;

        // Clean Spaces
        this.FPoints_In_M8 = DESMod.cleanTheString(this.FPoints_In_M8);
        // Check length
        len = this.FPoints_In_M8.length();
        if (len != 8) {
            errList.add(Messages.DESController_17);
            err++;
        }
        // Check Input Digits
        if (this.isHexDigit(this.FPoints_In_M8) != 0) {
            errList.add(Messages.DESController_18);
            err++;
        }

        errMsg = errList.toArray(new String[errList.size()]);

        return err;
    }

    private int checkSBoxInput() {
        int err = 0;
        ArrayList<String> errList = new ArrayList<String>();
        int len = 0;

        // Clean Spaces
        this.SBox_In_Deltap = DESMod.cleanTheString(this.SBox_In_Deltap);
        // Check Input Length
        len = this.SBox_In_Deltap.length();
        if (len != 16) {
            errList.add(Messages.DESController_19);
            err++;
        }
        // Check Input Digits
        if (this.isHexDigit(this.SBox_In_Deltap) != 0) {
            errList.add(Messages.DESController_20);
            err++;
        }

        errMsg = errList.toArray(new String[errList.size()]);

        return err;
    }

    // Helper Functions

    // Function is Based on isHexDigit in GUIMain.java of Wolfgang Baltes
    private int isHexDigit(String hexDigit) {
        char[] hexDigitArray = hexDigit.toCharArray();
        int hexDigitLength = hexDigitArray.length;

        boolean isNotHex = false;
        for (int i = 0; i < hexDigitLength; i++) {
            isNotHex = (Character.digit(hexDigitArray[i], 16) == -1);
            if (isNotHex) {
                return 1;
            }
        }

        return 0;
    }

}
