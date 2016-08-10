package com.didac.sort;

import com.didac.sort.utils.ExternalSortUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

/**
 * Created by dmontero on 24/07/16.
 */
public class MergeSortExternalSearch{
    private final File input;
    private final File output;
    private final Charset charset;
    private final long chunkSize;

    public MergeSortExternalSearch(File input, Optional<File> output, Charset charset, long chunkSize) throws IOException {
        this.input = input;
        this.output = output.isPresent() ? output.get() : File.createTempFile("sorted",".txt");
        this.charset = charset;
        this.chunkSize = chunkSize;
    }
    public File sort() throws IOException {
        List<File> chunks = sortInChunks(input);
        return mergeChunks(chunks,output);
    }

    private List<File> sortInChunks(File file) throws IOException {
        BufferedReader buffer = ExternalSortUtils.bufferFromFile(file, charset);
        ChunksSorter chunksSorter = new ChunksSorter(buffer, chunkSize, charset);
        return chunksSorter.sort();
    }

    private File mergeChunks(List<File> chunks, File output) throws IOException {
        ChunksMerger merger = new ChunksMerger(chunks, charset);
        return merger.merge(output);
    }
}
