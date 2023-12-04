package org.ignis.driver.minebench;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.ignis.driver.minebench.FormatUtils.binToHex;

public class BlockHeader {

    private static final Logger LOGGER = LogManager.getLogger();

    private final int ver;
    private final String prevBlock;
    private final String merkleRoot;
    private final int time;
    private final int bits;
    private final boolean sequentialNonce;
    private int nonce;
    private final Set<Integer> usedNonces;
    private byte[] headerBin;


    public BlockHeader(int ver, String prevBlock, String merkleRoot, int time, int bits, boolean sequentialNonce, int nonce) {
        this.ver = ver;
        this.prevBlock = prevBlock;
        this.merkleRoot = merkleRoot;
        this.time = time;
        this.bits = bits;
        this.sequentialNonce = sequentialNonce;
        this.nonce = 0;
        this.usedNonces = new HashSet<>();
        this.headerBin = this.getBin();
    }

    @Override
    public String toString() {
        return ver + "," + prevBlock + "," + time; //+ "," + HexFormat.of().formatHex(headerBin);
    }

    public String mine() {
        String networkTarget = this.getTarget();
        LOGGER.debug("Target: " + networkTarget);
        byte[] networkTargetBin = FormatUtils.hexToBin(networkTarget);
        long startTime = FormatUtils.currentTimeStampMillis();
        long blockSeconds = 0;
        int attemps = 1;

        byte[] currentHash = this.getHash(this.headerBin);
        while (binToHex(currentHash).compareTo(networkTarget) > 0) {
            this.setNewNonce();
            currentHash = this.getHash(this.headerBin);
            blockSeconds = FormatUtils.currentTimeStampMillis() - startTime;
            attemps++;
        }

        String currentHashStr = FormatUtils.binToHex(currentHash);
        LOGGER.debug("Block found: " + binToHex(currentHash));
        LOGGER.debug("Nonce: " + this.nonce);
        LOGGER.debug("Attempts: " + attemps);
        LOGGER.debug("Block elapsed seconds: " + blockSeconds / 1000);

        return currentHashStr;
    }

    public String getTarget() {
        String bitsBigEndianHex = FormatUtils.uint32ToHexBigEndian(this.bits);
        int exp = FormatUtils.hexToInt(bitsBigEndianHex.substring(0, 2));
        int coeff = FormatUtils.hexToInt(bitsBigEndianHex.substring(2));
        BigInteger target = BigInteger.TWO.pow(8 * (exp - 3));
        target = target.multiply(BigInteger.valueOf(coeff));
//        BigInteger target = BigDecimal.valueOf(coeff * Math.pow(2, 8 * (exp - 3))).toBigIntegerExact();
//        LOGGER.info("bitsBigEndianHex: " + this.bits);
//        LOGGER.info("bitsBigEndianHex: " + bitsBigEndianHex);
//        LOGGER.info("exp: " + exp);
//        LOGGER.info("coeff-hex: " + bitsBigEndianHex.substring(2));
//        LOGGER.info("coeff: " + coeff);
//        LOGGER.info("Target0: " + target);
        return FormatUtils.uint256ToHexBigEndian(target);
    }

    public byte[] getBin() {
        return FormatUtils.hexToBin(this.getHex());
    }

    public String getHex() {
        return FormatUtils.uint32ToHexLittleEndian(this.ver) + FormatUtils.sha256ToHexLittleEndian(this.prevBlock) + FormatUtils.sha256ToHexLittleEndian(this.merkleRoot) + FormatUtils.uint32ToHexLittleEndian(this.time) + FormatUtils.uint32ToHexLittleEndian(this.bits) + FormatUtils.uint32ToHexLittleEndian(this.nonce);
    }

    public byte[] getHash(byte[] headerBin) {
        return FormatUtils.binToSha256_Sha256Bin(headerBin);
    }

    public void setNewNonce() {
        if (this.sequentialNonce) {
            this.nonce++;
            this.headerBin = ByteBuffer.allocate(32).order(ByteOrder.LITTLE_ENDIAN).put(ByteBuffer.allocate(28).order(ByteOrder.LITTLE_ENDIAN).putInt(nonce).array()).array();
        }
        while (true) {
            int newNonce = new Random().nextInt();
            if (!this.usedNonces.contains(newNonce)) {
                this.usedNonces.add(newNonce);
                this.nonce = newNonce;
                this.headerBin = ByteBuffer.allocate(32).order(ByteOrder.LITTLE_ENDIAN).put(ByteBuffer.allocate(28).order(ByteOrder.LITTLE_ENDIAN).putInt(nonce).array()).array();
                break;
            }
        }
    }


}
