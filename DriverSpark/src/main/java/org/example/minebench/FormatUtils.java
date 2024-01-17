package org.example.minebench;

import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FormatUtils {

    private static final Logger LOGGER = LogManager.getLogger();

    public static String swapHex(String hexString) {
        StringBuilder swappedString = new StringBuilder();
        for (int i = 0; i < hexString.length(); i += 2) {
            swappedString.append(hexString.charAt(i + 1));
            swappedString.append(hexString.charAt(i));
        }
        return swappedString.toString();
    }

    public static String sha256ToHexLittleEndian(String string) {
        return FormatUtils.swapHex(new StringBuilder(string).reverse().toString());
    }

    public static String uint32ToHexLittleEndian(int integer) {
        byte[] bytesInteger = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(integer).array();
        return new String(Hex.encodeHex(bytesInteger));
    }

    public static String decode(byte[] bytes) {
        return new String(Hex.encodeHex(bytes));
    }

    public static String uint32ToHexBigEndian(int integer) {
        byte[] bytesInteger = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(integer).array();
        return new String(Hex.encodeHex(bytesInteger));
    }

    public static String uint256ToHexBigEndian(BigInteger bigInteger) {
        byte[] bytesInteger = ByteBuffer.allocate(32).order(ByteOrder.BIG_ENDIAN).put(bigInteger.toByteArray()).array();
        return new String(Hex.encodeHex(bytesInteger));
    }

    public static byte[] hexToBin(String hexString) {
        return new BigInteger(hexString, 16).toByteArray();
//        return ByteBuffer.allocate(200).putLong//(Long.parseLong(hexString, 16)).array();
    }

    public static String binToHex(byte[] bytes) {
        return new String(Hex.encodeHex(bytes));
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
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] firstHashBin = digest.digest(headerBin);
        byte[] secondHashBin = digest.digest(firstHashBin);
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
