// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool team and contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.modern.sha3.echo;

/**
 *
 * @author Felix Tenne
 */

public class ECHOAlgorithm {
    private int SUCCESS=0;
    private int FAIL=1;
    private int BAD_HASHBITLEN=2;
    private int STATE_NULL=3;

    private ECHOHashState state;
    private byte hashval[];
    private int status;

    private final short Logtable[] = {
      0,   0,  25,   1,  50,   2,  26, 198,  75, 199,  27, 104,  51, 238, 223,   3,
    100,   4, 224,  14,  52, 141, 129, 239,  76, 113,   8, 200, 248, 105,  28, 193,
    125, 194,  29, 181, 249, 185,  39, 106,  77, 228, 166, 114, 154, 201,   9, 120,
    101,  47, 138,   5,  33,  15, 225,  36,  18, 240, 130,  69,  53, 147, 218, 142,
    150, 143, 219, 189,  54, 208, 206, 148,  19,  92, 210, 241,  64,  70, 131,  56,
    102, 221, 253,  48, 191,   6, 139,  98, 179,  37, 226, 152,  34, 136, 145,  16,
    126, 110,  72, 195, 163, 182,  30,  66,  58, 107,  40,  84, 250, 133,  61, 186,
     43, 121,  10,  21, 155, 159,  94, 202,  78, 212, 172, 229, 243, 115, 167,  87,
    175,  88, 168,  80, 244, 234, 214, 116,  79, 174, 233, 213, 231, 230, 173, 232,
     44, 215, 117, 122, 235,  22,  11, 245,  89, 203,  95, 176, 156, 169,  81, 160,
    127,  12, 246, 111,  23, 196,  73, 236, 216,  67,  31,  45, 164, 118, 123, 183,
    204, 187,  62,  90, 251,  96, 177, 134,  59,  82, 161, 108, 170,  85,  41, 157,
    151, 178, 135, 144,  97, 190, 220, 252, 188, 149, 207, 205,  55,  63,  91, 209,
     83,  57, 132,  60,  65, 162, 109,  71,  20,  42, 158,  93,  86, 242, 211, 171,
     68,  17, 146, 217,  35,  32,  46, 137, 180, 124, 184,  38, 119, 153, 227, 165,
    103,  74, 237, 222, 197,  49, 254,  24,  13,  99, 140, 128, 192, 247, 112,   7,
    };

    private final short Alogtable[] = {
      1,   3,   5,  15,  17,  51,  85, 255,  26,  46, 114, 150, 161, 248,  19,  53,
     95, 225,  56,  72, 216, 115, 149, 164, 247,   2,   6,  10,  30,  34, 102, 170,
    229,  52,  92, 228,  55,  89, 235,  38, 106, 190, 217, 112, 144, 171, 230,  49,
     83, 245,   4,  12,  20,  60,  68, 204,  79, 209, 104, 184, 211, 110, 178, 205,
     76, 212, 103, 169, 224,  59,  77, 215,  98, 166, 241,   8,  24,  40, 120, 136,
    131, 158, 185, 208, 107, 189, 220, 127, 129, 152, 179, 206,  73, 219, 118, 154,
    181, 196,  87, 249,  16,  48,  80, 240,  11,  29,  39, 105, 187, 214,  97, 163,
    254,  25,  43, 125, 135, 146, 173, 236,  47, 113, 147, 174, 233,  32,  96, 160,
    251,  22,  58,  78, 210, 109, 183, 194,  93, 231,  50,  86, 250,  21,  63,  65,
    195,  94, 226,  61,  71, 201,  64, 192,  91, 237,  44, 116, 156, 191, 218, 117,
    159, 186, 213, 100, 172, 239,  42, 126, 130, 157, 188, 223, 122, 142, 137, 128,
    155, 182, 193,  88, 232,  35, 101, 175, 234,  37, 111, 177, 200,  67, 197,  84,
    252,  31,  33,  99, 165, 244,   7,   9,  27,  45, 119, 153, 176, 203,  70, 202,
     69, 207,  74, 222, 121, 139, 134, 145, 168, 227,  62,  66, 198,  81, 243,  14,
     18,  54,  90, 238,  41, 123, 141, 140, 143, 138, 133, 148, 167, 242,  13,  23,
     57,  75, 221, 124, 132, 151, 162, 253,  28,  36, 108, 180, 199,  82, 246,   1,
    };

    private final short S[] = {
     99, 124, 119, 123, 242, 107, 111, 197,  48,   1, 103,  43, 254, 215, 171, 118,
    202, 130, 201, 125, 250,  89,  71, 240, 173, 212, 162, 175, 156, 164, 114, 192,
    183, 253, 147,  38,  54,  63, 247, 204,  52, 165, 229, 241, 113, 216,  49,  21,
      4, 199,  35, 195,  24, 150,   5, 154,   7,  18, 128, 226, 235,  39, 178, 117,
      9, 131,  44,  26,  27, 110,  90, 160,  82,  59, 214, 179,  41, 227,  47, 132,
     83, 209,   0, 237,  32, 252, 177,  91, 106, 203, 190,  57,  74,  76,  88, 207,
    208, 239, 170, 251,  67,  77,  51, 133,  69, 249,   2, 127,  80,  60, 159, 168,
     81, 163,  64, 143, 146, 157,  56, 245, 188, 182, 218,  33,  16, 255, 243, 210,
    205,  12,  19, 236,  95, 151,  68,  23, 196, 167, 126,  61, 100,  93,  25, 115,
     96, 129,  79, 220,  34,  42, 144, 136,  70, 238, 184,  20, 222,  94,  11, 219,
    224,  50,  58,  10,  73,   6,  36,  92, 194, 211, 172,  98, 145, 149, 228, 121,
    231, 200,  55, 109, 141, 213,  78, 169, 108,  86, 244, 234, 101, 122, 174,   8,
    186, 120,  37,  46,  28, 166, 180, 198, 232, 221, 116,  31,  75, 189, 139, 138,
    112,  62, 181, 102,  72,   3, 246,  14,  97,  53,  87, 185, 134, 193,  29, 158,
    225, 248, 152,  17, 105, 217, 142, 148, 155,  30, 135, 233, 206,  85,  40, 223,
    140, 161, 137,  13, 191, 230,  66, 104,  65, 153,  45,  15, 176,  84, 187,  22,
    };

    ECHOAlgorithm(int hashbitlen, byte[] data){
        this.status = FAIL;
        this.hashval = new byte[hashbitlen/8];
        this.state = new ECHOHashState();

        status = Hash(hashbitlen, data);
    }

    ECHOAlgorithm(int hashbitlen, byte[] data, String salt){
        this.status = FAIL;
        this.hashval = new byte[hashbitlen/8];
        this.state = new ECHOHashState();

        setSalt(hexStrToByteField(salt));

        status = Hash(hashbitlen, data);
    }

    public int getStatus(){
        return this.status;
    }

    public byte[] getHash(){
        return this.hashval;
    }

    private short byteToShort(byte b){
        short s=0;

        if(b<0){
            s+=128;
            b+=128;
        }

        s+=b;
        return (short)s;
    }

    private short mul(short a, short b) {
       /* multiply two elements of GF(2^m)
        * needed for MixColumn and InvMixColumn
        */
        if(a!=0 && b!=0)
            return Alogtable[(Logtable[a] + Logtable[b])%255];

        return 0;
    }

    private byte[][] AddRoundKey(byte a[][], byte k[][]) {
        /* Exor corresponding text input and key input bytes
         */
        int i, j;

        for(i = 0; i < 4; i++)
            for(j = 0; j < 4; j++)
                a[i][j] = (byte)(byteToShort(a[i][j]) ^ byteToShort(k[i][j]));

        return a;
    }

    private byte[][] ShiftRows(byte a[][]) {
        /* Row 0 remains unchanged
         * The other three rows are shifted a variable amount
         */
        byte tmp[] = new byte[4];
        int i, j;

        for(i = 1; i < 4; i++) {
            for(j = 0; j < 4; j++)
                tmp[j] = a[i][(j + i) % 4];
            for(j = 0; j < 4; j++)
                a[i][j] = tmp[j];
        }

            return a;
    }

    private byte[][] SubByte(byte a[][]) {
        /* Replace every byte of the input by the byte at that place
         * in the nonlinear S-box.
         */
        int i, j;

        for(i = 0; i < 4; i++)
            for(j = 0; j < 4; j++){
                a[i][j] = (byte)S[byteToShort(a[i][j])] ;
            }

        return a;
    }

    private byte[][] MixColumns(byte a[][]) {
            /* Mix the four bytes of every column in a linear way	 */
        byte b[][] = new byte[4][4];
        int i, j;

        for(j = 0; j < 4; j++)
            for(i = 0; i < 4; i++)
                    b[i][j] = (byte)(
                        mul( (short)2, byteToShort(a[i][j]) )
                        ^ mul( (short)3, byteToShort( a[(i + 1) % 4][j] ) )
                        ^ byteToShort(a[(i + 2) % 4][j])
                        ^ byteToShort(a[(i + 3) % 4][j]));

        for(i = 0; i < 4; i++)
            for(j = 0; j < 4; j++)
                a[i][j] = b[i][j];

        return a;
    }

    private byte[][] aes(byte a[][], byte k[][]){
        a=SubByte(a);
        a=ShiftRows(a);
        a=MixColumns(a);
        a=AddRoundKey(a,k);

        return a;
    }

    private int Init(int hashbitlen){
        int i,j;

        if (state==null){
            return STATE_NULL;
        }

        if((hashbitlen >= 128) && (hashbitlen <= 512)){
            state.hashbitlen = (short)hashbitlen;
        }else{
            return BAD_HASHBITLEN;
        }

        if(hashbitlen > 256){
            state.cv_size = 1024;
            state.message_size = 1024;
            state.rounds = 10;
        }else{
            state.cv_size = 512;
            state.message_size = 1536;
            state.rounds = 8;
        }

        for(j=0; j<state.cv_size/512; j++){ //big col
            for(i=0; i<4; i++){ //big row
                state.tab[i][j][0][0] = (byte)hashbitlen;
                state.tab[i][j][1][0] = (byte)(hashbitlen>>8);
            }
        }

        state.index = (short)(state.cv_size/8);
        return SUCCESS;
    }

    private void Push(byte a){
        short help[] = state.getAddress(state.index++);
        state.tab[help[0]][help[1]][help[2]][help[3]] = a;
    }

    private byte Pop(){
        short help[] = state.getAddress(state.index++);
        return  state.tab[help[0]][help[1]][help[2]][help[3]];
    }

    private void Backup(){
        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                for(int k=0; k<4; k++){
                    for(int l=0; l<4; l++){
                        state.tab_backup[i][j][k][l] = state.tab[i][j][k][l];
                    }
                }
            }
        }
    }

    private void BigSubWords(){
        int i,j;
        state.k1[0][1] = (byte)(state.counter_hi >> 0);
        state.k1[1][1] = (byte)(state.counter_hi >> 8);
        state.k1[2][1] = (byte)(state.counter_hi >> 16);
        state.k1[3][1] = (byte)(state.counter_hi >> 24);

        for(j=0; j<4; j++){
            for(i=0; i<4; i++){
                state.k1[0][0] = (byte)(state.counter_lo >> 0);
                state.k1[1][0] = (byte)(state.counter_lo >> 8);
                state.k1[2][0] = (byte)(state.counter_lo >> 16);
                state.k1[3][0] = (byte)(state.counter_lo >> 24);
                state.tab[i][j] = aes(state.tab[i][j], state.k1);
                state.tab[i][j] = aes(state.tab[i][j], state.k2);
                state.counter_lo++;

                if (state.counter_lo == 0){
                    state.counter_hi++;
                    state.k1[0][1] = (byte)(state.counter_hi >> 0);
                    state.k1[1][1] = (byte)(state.counter_hi >> 8);
                    state.k1[2][1] = (byte)(state.counter_hi >> 16);
                    state.k1[3][1] = (byte)(state.counter_hi >> 24);
                }
            }
        }
    }

    private void BigShiftRows(){
        byte tmp[][][] = new byte[4][4][4];
        int m;

        for(int i = 1; i < 4; i++) {
            for(int j = 0; j < 4; j++){
                m = (j + i) % 4;
                for(int k = 0; k < 4; k++){
                    for(int l = 0; l < 4; l++){
                        tmp[j][k][l] = state.tab[i][m][k][l];
                    }
                }
            }

            for(int j = 0; j < 4; j++) {
                for(int k = 0; k < 4; k++){
                    for(int l = 0; l < 4; l++){
                        state.tab[i][j][k][l] = tmp[j][k][l];
                    }
                }
            }
        }
    }

    private byte[] Mix4bytes(short a, short b, short c, short d){
        /* Mix four bytes in a linear way */
        byte field[] = new byte[4];

        field[0] = (byte)(mul((short)2, a)^mul((short)3, b)^c^d);
        field[1] = (byte)(mul((short)2, b)^mul((short)3, c)^d^a);
        field[2] = (byte)(mul((short)2, c)^mul((short)3, d)^a^b);
        field[3] = (byte)(mul((short)2, d)^mul((short)3, a)^b^c);

        return field;
    }

    private void BigMixColumns(){
        int i,j,k;
        byte tmp[];

        for(i=0; i<4; i++){
            for(j=0; j<4; j++){
                for(k=0; k<4; k++){
                    tmp=Mix4bytes(byteToShort(state.tab[0][i][j][k])
                            , byteToShort(state.tab[1][i][j][k])
                            , byteToShort(state.tab[2][i][j][k])
                            , byteToShort(state.tab[3][i][j][k]));

                    state.tab[0][i][j][k]=tmp[0];
                    state.tab[1][i][j][k]=tmp[1];
                    state.tab[2][i][j][k]=tmp[2];
                    state.tab[3][i][j][k]=tmp[3];
                }
            }
        }
    }

    private void BigFinal(){
        int i,j,k;

        if (state.cv_size == 512){
            for(i=0; i<4; i++){
                for(k=0; k<4; k++){
                    for(j=0; j<4; j++){
                        state.tab[i][0][j][k] =
                            (byte)(byteToShort(state.tab_backup[i][0][j][k]) ^
                            byteToShort(state.tab_backup[i][1][j][k]) ^
                            byteToShort(state.tab_backup[i][2][j][k]) ^
                            byteToShort(state.tab_backup[i][3][j][k]) ^
                            byteToShort(state.tab[i][0][j][k]) ^
                            byteToShort(state.tab[i][1][j][k]) ^
                            byteToShort(state.tab[i][2][j][k]) ^
                            byteToShort(state.tab[i][3][j][k])) ;
                    }
                }
            }
        }else{
            for(i=0; i<4; i++){
                for(k=0; k<4; k++){
                    for(j=0; j<4; j++){
                        state.tab[i][0][j][k] =
                            (byte)(byteToShort(state.tab_backup[i][0][j][k]) ^
                            byteToShort(state.tab_backup[i][2][j][k]) ^
                            byteToShort(state.tab[i][0][j][k]) ^
                            byteToShort(state.tab[i][2][j][k]));
                    }
                }
            }

            for(i=0; i<4; i++){
                for(k=0; k<4; k++){
                    for(j=0; j<4; j++){
                        state.tab[i][1][j][k] =
                            (byte)(byteToShort(state.tab_backup[i][1][j][k]) ^
                            byteToShort(state.tab_backup[i][3][j][k]) ^
                            byteToShort(state.tab[i][1][j][k]) ^
                            byteToShort(state.tab[i][3][j][k]));
                    }
                }
            }
        }
    }

    private void Compress(){
        Backup();

        state.counter_hi = state.messlenhi;
        state.counter_lo = state.messlenlo;

        for(int i=0; i<state.rounds; i++){
            BigSubWords();
            BigShiftRows();
            BigMixColumns();
        }

        BigFinal();
    }

    private int Update(byte[] data){
        if (data.length == 0){
            return SUCCESS;
        }

        if (state == null){
            return STATE_NULL;
        }

        if (state.bit_index != 0){
            return FAIL;
        }

        if (state.computed != 0){
            return FAIL;
        }

        for(int i=0; i<data.length;i++ ){
                //read data byte
                Push(data[i]);

                state.messlenlo += 8;
                if(state.messlenlo == 0){
                    state.messlenhi ++;
                }


                if (state.index == 256){
                        //block completed
                        Compress();
                        state.index = (short)(state.cv_size/8);
                }
        }
        return SUCCESS;
    }

    private void Pad(){
        int nFinalPadding = 0;

        short MASK_AND[] = {
            // =  - (1<<(7-i))
            0x80, 0xC0, 0xE0, 0xF0, 0xF8, 0xFC, 0xFE, 0xFF
        };

        short MASK_OR[] = {
            // = 1 << (7-i)
            0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01
        };

        //first bit of padding
        short help[] = state.getAddress(state.index++);
        state.tab[help[0]][help[1]][help[2]][help[3]] = (byte)(byteToShort(state.tab[help[0]][help[1]][help[2]][help[3]]) & MASK_AND[state.bit_index]);
        state.tab[help[0]][help[1]][help[2]][help[3]] = (byte)(byteToShort(state.tab[help[0]][help[1]][help[2]][help[3]]) | MASK_OR[state.bit_index]);

        if ((state.index == state.cv_size/8) && (state.bit_index == 0)){
            //no message bit in this block
            nFinalPadding = 1;
        }

        state.index ++;

        if (state.index > 256 - 16 - 2)
        {
            //padding with "0"
            while (state.index < 256)
            {
                 Push((byte)0);
            }
            Compress();
            state.index = (short)(state.cv_size/8);
            //no message bit in next block
            nFinalPadding = 1;
        }
        //padding last block
        while (state.index < 256 - 16 - 2)
        {
             Push((byte)0);
        }
        //HSIZE (2 bytes)
        Push((byte)state.hashbitlen);
        Push((byte)(state.hashbitlen >> 8));
        //message length (8 bytes)
        Push((byte)(state.messlenlo >> 0));
        Push((byte)(state.messlenlo >> 8));
        Push((byte)(state.messlenlo >> 16));
        Push((byte)(state.messlenlo >> 24));
        Push((byte)(state.messlenhi >> 0));
        Push((byte)(state.messlenhi >> 8));
        Push((byte)(state.messlenhi >> 16));
        Push((byte)(state.messlenhi >> 24));

        //High 64 bits of counter set to 0
        while (state.index < 256)
        {
             Push((byte)0);
        }
        if (nFinalPadding != 0)
        {
            state.messlenhi = 0;
            state.messlenlo = 0;
        }
        Compress();
    }

    private int Final(){
        int i;

        int MASK_AND[] ={
            0xFF, 0x80, 0xC0, 0xE0, 0xF0, 0xF8, 0xFC, 0xFE
        };

        if (state == null){
            return STATE_NULL;
        }

        if (state.computed != 0){
            return FAIL;
        }

        Pad();

        /* output truncated hash value */
        state.index = 0;
        for(i=0; i<((state.hashbitlen + 7)/8); i++){
            hashval[i] = Pop();
        }

        //last byte truncation
        hashval[i-1] &= MASK_AND[state.hashbitlen % 8];

        //clean up
        state.index = 0;

        for (i=0; i<256; i++){
            Push((byte)0);
        }

        state.computed = 1;

        return SUCCESS;
    }

    private int Hash(int hashbitlen, byte[] data){
        int F;
        F = Init(hashbitlen);

        if(F != SUCCESS)
            return F;

        F = Update(data);

        if(F != SUCCESS)
            return F;

        return Final();
    }

    private void setSalt(byte[] salt){
        if(salt != null && salt.length == 16){
            //salt low 64 bits
            state.k2[0][0] = salt[0];
            state.k2[1][0] = salt[1];
            state.k2[2][0] = salt[2];
            state.k2[3][0] = salt[3];
            state.k2[0][1] = salt[4];
            state.k2[1][1] = salt[5];
            state.k2[2][1] = salt[6];
            state.k2[3][1] = salt[7];
            //salt high 64 bits
            state.k2[0][2] = salt[8];
            state.k2[1][2] = salt[9];
            state.k2[2][2] = salt[10];
            state.k2[3][2] = salt[11];
            state.k2[0][3] = salt[12];
            state.k2[1][3] = salt[13];
            state.k2[2][3] = salt[14];
            state.k2[3][3] = salt[15];
        }
    }

    private byte[] hexStrToByteField(String hexStr){
        if(hexStr.length()%2==0){
            byte [] bytes = new byte[hexStr.length()/2];
            int i,j;

            try{
                for(i=0,j=0;j<hexStr.length();i++, j+=2)
                       bytes[i] = (byte)Integer.parseInt(hexStr.substring(j,j+2), 16);
            }catch(Exception e){
                return null;
            }

            return bytes;
        }else
            return null;
    }
}
