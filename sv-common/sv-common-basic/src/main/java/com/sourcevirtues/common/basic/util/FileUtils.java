package com.sourcevirtues.common.basic.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {

    /**
     * Generate a temporary file with given content.
     * 
     * @throws IllegalStateException When got IOException
     */
    public static File createTempFileWithContent(String content) {
        return createTempFileWithContent(content, System.currentTimeMillis() + "_", ".tmp");
    }

    /**
     * Generate a temporary file with given content. Use prefix and suffix for temporary file name.
     * 
     * @throws IllegalStateException When got IOException
     */
    public static File createTempFileWithContent(String content, String prefix, String suffix) {
        try {
            File f = File.createTempFile(prefix, suffix);
            FileWriter fileWriter = new FileWriter(f);
            fileWriter.write(content);
            fileWriter.flush();
            fileWriter.close();
            return f;
        } catch (IOException ioe) {
            throw new IllegalStateException(ioe);
        }
    }

    /**
     * Read a file and return its content as a single String.
     */
    public static String readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }
}
