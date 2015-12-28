package com.sourcevirtues.common.basic.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {

    /**
     * Generate a temporary file with given content.
     * 
     * @throws IllegalStateException When got IOException
     */
    public static File createTempFileWithContent(String content) {
        try {
            File f = File.createTempFile(System.currentTimeMillis() + "_", "_morphlines.conf");
            FileWriter fileWriter = new FileWriter(f);
            fileWriter.write(content);
            fileWriter.flush();
            fileWriter.close();
            return f;
        } catch (IOException ioe) {
            throw new IllegalStateException(ioe);
        }
    }
}
