package com.didac.sort;

import com.didac.sort.utils.ExternalSortUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

/**
 * Created by dmontero on 26/07/16.
 */
public class ChuncksSorterTest {
    private final Charset charset = Charset.defaultCharset();

    @Test
    public void testChunksSortedLimitingChunckSize() throws URISyntaxException, IOException {
        File file = SortTestUtils.getFileFromResources("file_5000_lines.txt");

        checkOutputFileCorrectness(file, 10000);
    }
    @Test
    public void testChunksSorted() throws URISyntaxException, IOException {
        File file = SortTestUtils.getFileFromResources("file_5000_lines.txt");

        checkOutputFileCorrectness(file, ExternalSortUtils.chunkSizeFromFile(file));

    }

    private void checkOutputFileCorrectness(File file, long chunckSize) throws URISyntaxException, IOException {
        long expectedNumChunks = file.length() / chunckSize;
        BufferedReader buffer = ExternalSortUtils.bufferFromFile(file, charset);
        ChunksSorter chunksSorter = new ChunksSorter(buffer, chunckSize, charset);
        List<File> sortedChunks = chunksSorter.sort();
        Long sumLines=0L;
        long numChunks=0;
        for (File sortedChunk : sortedChunks) {
            Assert.assertTrue(SortTestUtils.isOutputSorted(sortedChunk, charset));
            sumLines+=SortTestUtils.countLines(sortedChunk, charset);
            Assert.assertTrue(SortTestUtils.containsSameHashes(file, sortedChunk, charset));
            numChunks++;
            sortedChunk.delete();
        }
        Long originalFileLines = SortTestUtils.countLines(file, charset);
        Assert.assertEquals(originalFileLines, sumLines);
        Assert.assertTrue(expectedNumChunks == numChunks || expectedNumChunks == numChunks-1);
    }

}
