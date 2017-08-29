package com.aga.myapplication;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
public class ExampleHMAC {
    /**
     * table to convert a nibble to a hex char.
     */
    static final char[] hexChar = {
            '0', '1', '2', '3',
            '4', '5', '6', '7',
            '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f'};

    /**
     * Fast convert a byte array to a hex string
     * with possible leading zero.
     *
     * @param b array of bytes to convert to string
     * @return hex representation, two chars per byte.
     */
    public static String encodeHexString(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; i++) {
// look up high nibble char
            sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
// look up low nibble char
            sb.append(hexChar[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    /**
     * Computes the seal
     *
     * @param Data      the parameters to cipher
     * @param secretKey the secret key to append to the parameters
     * @return hex representation of the seal, two chars per byte.
     */
    public static String computeSeal(String Data, String secretKey) throws Exception {
        Mac hmacSHA256;
        hmacSHA256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        hmacSHA256.init(keySpec);
        return encodeHexString(hmacSHA256.doFinal(Data.getBytes()));
    }

}