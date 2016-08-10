package com.didac.sort;

import com.didac.sort.utils.ExternalSortUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by dmontero on 24/07/16.
 */
public class ChunksSorter {
    private final BufferedReader buffer;
    private final long chunkSize;
    private final Charset charset;
    private List<File> chunkFiles;

    public ChunksSorter(BufferedReader buffer, long chunkSize, Charset charset) {
        this.buffer = buffer;
        this.chunkSize = chunkSize;
        this.charset = charset;
        chunkFiles = new ArrayList<>();
    }

    public List<File> sort() throws IOException {
        while (sortChunk() != null){
            continue;
        }
        return chunkFiles;
    }

    private File sortChunk() throws IOException {
        List<String> lines = readLines();
        if (lines.isEmpty()) return null;
        //n log(n) guaranteed
        Collections.sort(lines);
        return saveIntoFile(lines);
    }
    private File saveIntoFile(List<String> lines) throws IOException{
        File file = ExternalSortUtils.createFileFromList(lines, charset);
        chunkFiles.add(file);
        return file;
    }

    private List<String> readLines() throws IOException {
        int totalBytesRead=0;
        List<String> totalLines = new ArrayList<>();
        String currentLine;
        while (totalBytesRead < chunkSize &&
                (currentLine = buffer.readLine()) != null){
            totalLines.add(currentLine);
            totalBytesRead+=ExternalSortUtils.stringToNumBytes(currentLine, charset);
        }
        return totalLines;
    }
}
