package com.didac.sort;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Optional;

/**
 * Created by dmontero on 26/07/16.
 */
public class ExternalSearchTest {
    private final Charset charset = Charset.defaultCharset();

    @Test
    public void testSortSpecifyingBytes() throws URISyntaxException, IOException {
        File file = SortTestUtils.getFileFromResources("file_5000_lines.txt");
        checkOutputFileFromInput(file, 10000L);
    }


    @Test
    public void testSort() throws URISyntaxException, IOException {
        File file = SortTestUtils.getFileFromResources("file_5000_lines.txt");
        checkOutputFileFromInput(file, null);
    }

    private void checkOutputFileFromInput(File input, Long chunkSize) throws URISyntaxException, IOException {

        File sortedFile = chunkSize == null ?
                ExternalSearch.sort(input, Optional.empty(), charset) :
                ExternalSearch.sort(input, Optional.empty(), charset, chunkSize);

        Assert.assertTrue(SortTestUtils.isOutputSorted(sortedFile, charset));

        Long linesOriginalFile = SortTestUtils.countLines(input, charset);
        Long linesSortedFile = SortTestUtils.countLines(sortedFile, charset);
        Assert.assertEquals(linesOriginalFile, linesSortedFile);

        Assert.assertTrue(SortTestUtils.containsSameHashes(input, sortedFile, charset));
        sortedFile.delete();
    }


}
