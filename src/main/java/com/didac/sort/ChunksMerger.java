package com.didac.sort;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by dmontero on 24/07/16.
 */
public class ChunksMerger {
    private final List<File> chunkFiles;
    private final Charset charset;
    private final ExecutorService executor;

    public ChunksMerger(List<File> chunkFiles, Charset charset) {
        this.chunkFiles = chunkFiles;
        this.charset = charset;
        int maxThreadCount = Runtime.getRuntime().availableProcessors();
        //double of numThreads becase it is a CPU + I/O task
        executor = Executors.newFixedThreadPool(maxThreadCount*2);

    }
    public File merge(File output) throws IOException {
        return mergeRecursive(output, chunkFiles);

    }

    private File mergeRecursive(File output, List<File> files) throws IOException {

        if(files == null || files.isEmpty()){
            executor.shutdown();
            throw new RuntimeException("No files for the merge process is not expected");
        }
        if(files.size() == 1){
            Files.move(files.get(0).toPath(), output.toPath(), StandardCopyOption.REPLACE_EXISTING);
            executor.shutdown();
            return output;
        }


        List<Future<File>> futures = new ArrayList<>();


        Queue<File> queue = filesListToFilesQueue(files);
        List<File> outputFiles = new ArrayList<>();
        while (queue.size() > 1) {
            File file1 = queue.remove();
            File file2 = queue.remove();

            Callable<File> mergeStepWorker = new ChunkMerger(charset, file1, file2);
            Future<File> future = executor.submit(mergeStepWorker);
            futures.add(future);
        }

        if(!queue.isEmpty()){
            outputFiles.add(queue.remove());
        }

        for (Future<File> future : futures) {
            try {
                outputFiles.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                executor.shutdown();
                throw new ExternalSortException(e);
            }
        }

        return mergeRecursive(output, outputFiles);

    }

    private Queue<File> filesListToFilesQueue(List<File> chunkFiles){
        Queue<File> queue = new ConcurrentLinkedQueue<>();
        for (File chunkFile : chunkFiles) {
            queue.add(chunkFile);
        }
        return queue;
    }
}
