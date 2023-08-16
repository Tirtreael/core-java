package org.ignis.driver.minebench;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class InputUtils {

    public static int getFileLines(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            int numLines = 0;
            try {
                while (reader.readLine() != null) {
                    numLines += 1;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return numLines;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void forwardFileLines(BufferedReader reader, int numLines) {
        for (int i = 0; i < numLines; i++) {
            try {
                reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
