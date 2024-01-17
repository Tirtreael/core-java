package org.example.minebench;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class Minebench {


    private static final Logger LOGGER = LogManager.getLogger();
    private static int defaultBits = 0x1F111111;

    public static int getDefaultBits() {
        return defaultBits;
    }

    public static void setDefaultBits(int defaultBits) {
        Minebench.defaultBits = defaultBits;
    }

    public static void processJob(String fileName, int startRow, int rowsNum, int bits, boolean sequentialNonce, int processId) {
        Random random = new Random(1984);
        LOGGER.info("process " + processId + " started");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            InputUtils.forwardFileLines(reader, startRow);
            for (int i = 0; i < rowsNum; i++) {
                try {
                    String line = reader.readLine();
                    if (i % 100 == 99) LOGGER.info("process " + processId + ": " + i + " mined blocks");
                    Minebench.getBlockHeader(line).mine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public static String getMerkleRoot(String[] txs) {
        List<String> txsHashes = new ArrayList<>(txs.length);
        for (String tx : txs) {
            txsHashes.add(FormatUtils.hexToSha256_Sha256(tx));
        }

        List<String> merkleHashes = txsHashes;
        while (merkleHashes.size() > 1) {
            int merkleHashesSize = merkleHashes.size();
            List<String> newMerkleHashes = new ArrayList<>();
            for (int i = 0; i < merkleHashesSize; i += 2) {
                if (merkleHashesSize > i + 1) {
                    String h1 = FormatUtils.sha256ToHexLittleEndian(merkleHashes.get(i));
                    String h2 = FormatUtils.sha256ToHexLittleEndian(merkleHashes.get(i + 1));
                    newMerkleHashes.add(FormatUtils.hexToSha256_Sha256(h1 + h2));
                    continue;
                } else if (merkleHashesSize == i + 1) {
                    String h1 = FormatUtils.sha256ToHexLittleEndian(merkleHashes.get(i));
                    newMerkleHashes.add(FormatUtils.hexToSha256_Sha256(h1 + h1));
                    continue;
                }
                newMerkleHashes.add((FormatUtils.hexToSha256_Sha256(merkleHashes.get(i))));
            }
            merkleHashes = newMerkleHashes;
        }
        return merkleHashes.get(0);
    }

    public static int getPoints(int blocksNo, int millis) {
        return blocksNo * 1000000 / millis;
    }

    public static LineMap getDictFromFileLine(String line) {
        String[] lines = line.strip().split(",");
        return new LineMap(Integer.parseInt(lines[0]), lines[1], Integer.parseInt(lines[2]), lines[3]);
    }

    public static BlockHeader getBlockHeader(String row, int bits, boolean sequentialNonce) {
        LineMap lineMap = Minebench.getDictFromFileLine(row);

        String[] txs = lineMap.getTx().split(":");
        String merkleRoot = Minebench.getMerkleRoot(txs);
//        System.out.println(bits);
        return new BlockHeader(lineMap.getVer(), lineMap.getPrevBlock(), merkleRoot, lineMap.getTime(), bits, sequentialNonce, 0);
    }

    public static BlockHeader getBlockHeader(String row) {
        return getBlockHeader(row, defaultBits, true);
    }

    public static BlockHeader getBlockHeader(String row, int bits) {
        return getBlockHeader(row, bits, true);
    }

    public static void main(String[] args) {
        System.out.println("\nMinebench v0.1.5 (Python 3.6+)\n");
        LOGGER.atLevel(Level.INFO);
        int processesNum = 1; //Runtime.getRuntime().availableProcessors();
        LOGGER.info("Processors: " + processesNum);
        List<Integer> processId = IntStream.range(0, processesNum).boxed().toList();

        if (args.length < 1) {
            LOGGER.error("You must indicate an input csv file.");
            throw new RuntimeException();
        }
        String fileName = args[0];

        int numLines = InputUtils.getFileLines(fileName);
        int splitSize = numLines / processesNum;

//        Minebench.setDefaultBits(0x3FFFFFFF);
        int bits = defaultBits;
        List<Thread> processes = new ArrayList<>();
        long startTime = FormatUtils.currentTimeStampMillis();
        for (int i : processId) {
            if (numLines - (i) * splitSize < 0) {
                splitSize = numLines - i * splitSize;
            }
            int finalSplitSize = splitSize;
            System.out.println("Process " + i + ": " + finalSplitSize + " of " + numLines);
            Thread process = new Thread(() -> Minebench.processJob(fileName, finalSplitSize * i, finalSplitSize, bits, true, i));
            process.start();
            processes.add(process);
        }

        for (Thread process : processes) {
            try {
                process.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        long totalMillis = FormatUtils.currentTimeStampMillis() - startTime;
        System.out.printf("\n- Elapsed time: %.2f seconds", ((float) totalMillis / 1000));
        System.out.printf("\n- Points: %d", Minebench.getPoints(numLines, (int) totalMillis));
        System.out.println("\n- Bits: " + bits + "\n");
    }

    public class MapArgument {
        public String row;
        public Integer bits;
    }

    public static String call(String s) throws Exception {
        return Minebench.getBlockHeader(s).mine();
    }

}
