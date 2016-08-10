package com.didac.sort;

import com.didac.sort.utils.ExternalSortUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Created by dmontero on 24/07/16.
 */
public class ExternalSearch {

    public static File sort(File input, Optional<File> output) throws IOException {
        return sort(input, output, Charset.defaultCharset());
    }
    public static File sort(File input, Optional<File> output, Charset charset) throws IOException {
        MergeSortExternalSearch sorter = new MergeSortExternalSearch(input, output, charset, ExternalSortUtils.chunkSizeFromFile(input));
        return sorter.sort();
    }
    protected static File sort(File input, Optional<File> output, Charset charset, long chunckSize) throws IOException {
        MergeSortExternalSearch sorter = new MergeSortExternalSearch(input, output, charset, chunckSize);
        return sorter.sort();
    }

}
