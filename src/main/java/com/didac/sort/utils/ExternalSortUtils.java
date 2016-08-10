package com.didac.sort.utils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by dmontero on 24/07/16.
 */
public class ExternalSortUtils {

    public static long stringToNumBytes(String string, Charset charset){
        final byte[] bytes = string.getBytes(charset);
        return bytes.length;
    }
    public static File createFileFromList(List<String> list, Charset charset, File output) throws IOException {
        Files.write(output.toPath(),list,charset);
        return output;
    }
    public static File createFileFromList(List<String> list, Charset charset) throws IOException {
        return createFileFromList(list, charset,File.createTempFile("fromList",".txt"));
    }

    public static long chunkSizeFromFile(File file){
        long fileLength = file.length();
        long freeMemory = Runtime.getRuntime().freeMemory();
        //Use .65 of the max Memory
        long lessThanFreeMem = (long)(0.65 * freeMemory);

        if(fileLength < lessThanFreeMem) return fileLength;
        return lessThanFreeMem;


    }
    public static BufferedReader bufferFromFile(File file, Charset charset) throws FileNotFoundException {
        return new BufferedReader(new InputStreamReader(
                new FileInputStream(file), charset));
    }
}
