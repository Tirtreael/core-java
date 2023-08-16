package org.ignis.driver.minebench;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FormatUtils {

    public static String swapHex(String hexString) {
        String swappedString = "";
        for (int i = 0; i < hexString.length(); i += 2) {
            swappedString += hexString.charAt(i + 1) + hexString.charAt(i);
        }
        return swappedString;
    }

    public static String sha256ToHexLittleEndian(String string) {
        return FormatUtils.swapHex(new StringBuilder(string).reverse().toString());
    }

    public static String uint32ToHexLittleEndian(int integer) {
        byte[] bytesInteger = ByteBuffer.allocate(32).order(ByteOrder.LITTLE_ENDIAN).putInt(integer).array();
        StringBuilder st = new StringBuilder();
        for (byte b : bytesInteger) {
            st.append(String.format("%032X", b));
        }
        return st.toString();
    }

    public static String decode(byte[] bytes) {
        StringBuilder st = new StringBuilder();
        for (byte b : bytes) {
            st.append(String.format("%032X", b));
        }
        return st.toString();
    }

    public static String uint32ToHexBigEndian(int integer) {
        byte[] bytesInteger = ByteBuffer.allocate(32).order(ByteOrder.BIG_ENDIAN).putInt(integer).array();
        return decode(bytesInteger);
    }

    public static String uint256ToHexBigEndian(BigInteger bigInteger) {
        byte[] bytesInteger = ByteBuffer.allocate(32).order(ByteOrder.BIG_ENDIAN).put(bigInteger.toByteArray()).array();
        return decode(bytesInteger);
    }

    public static byte[] hexToBin(String hexString) {
        return new BigInteger(hexString, 16).toByteArray();
//        return ByteBuffer.allocate(200).putLong//(Long.parseLong(hexString, 16)).array();
    }

    public static String binToHex(byte[] bytes) {
        StringBuilder st = new StringBuilder(32);
        for (byte b : bytes) {
            st.append(String.format("%032x", b));
        }
        return st.toString();
    }

    public static int hexToInt(String hexString) {
        return new BigInteger(hexString, 16).intValue();
//        return Integer.parseInt(hexString, 16);
    }

    public static long currentTimeStampSeconds() {
        return System.currentTimeMillis() / 1000;
    }

    public static long currentTimeStampMillis() {
        return System.currentTimeMillis();
    }

    public static byte[] binToSha256_Sha256Bin(byte[] headerBin) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] firstHashBin = digest.digest(headerBin);
        byte[] secondHashBin = digest.digest(firstHashBin);
//        return new StringBuilder(binToHex(secondHashBin)).reverse().toString();
        int len = secondHashBin.length;
        byte[] secondHashBinReverse = new byte[len];
        for (int i = 0; i < len; i++) {
            secondHashBinReverse[i] = secondHashBin[len - i - 1];
        }
        return secondHashBinReverse;
    }

    public static String binToSha256_Sha256(byte[] headerBin) {
        return String.format(binToHex(binToSha256_Sha256Bin(headerBin)), StandardCharsets.UTF_8);
    }

    public static String hexToSha256_Sha256(String string) {
        byte[] headerBin = hexToBin(string);
        return binToSha256_Sha256(headerBin);
    }


}
