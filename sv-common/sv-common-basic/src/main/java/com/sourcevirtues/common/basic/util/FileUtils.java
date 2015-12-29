package com.sourcevirtues.common.basic.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

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
     * Read a file with given encoded and return its content as a single String.
     */
    public static String readFileUTF8(String path) throws IOException {
        return readFile(path, StandardCharsets.UTF_8);
    }

    /**
     * Read a file with given encoded and return its content as a single String.
     */
    public static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
