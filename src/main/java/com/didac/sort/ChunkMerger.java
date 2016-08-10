package com.didac.sort;

import com.didac.sort.utils.ExternalSortUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by dmontero on 24/07/16.
 */
public class ChunkMerger implements Callable<File> {
    private final Charset charset;
    private final File input1;
    private final File input2;


    public ChunkMerger(Charset charset, File input1, File input2) {
        this.charset = charset;
        this.input1 = input1;
        this.input2 = input2;

    }

    @Override
    public File call() throws Exception {
        File outputStep = mergeStep(
                ExternalSortUtils.bufferFromFile(input1, charset),
                ExternalSortUtils.bufferFromFile(input2, charset));
        input1.delete();
        input2.delete();
        return outputStep;
    }

    private File mergeStep(BufferedReader reader1, BufferedReader reader2) throws IOException {
        File output = File.createTempFile("merge", ".txt");
        FileOutputStream fos = new FileOutputStream(output);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos, charset));
        try {
            //init
            String currentLine1 = reader1.readLine();
            String currentLine2 = reader2.readLine();

            //loop
            while (currentLine1 != null || currentLine2 != null) {
                if (currentLine1 == null || (currentLine2 != null && (currentLine1.compareTo(currentLine2) > 0))) {
                    currentLine2 = writeOldLineAndReadNew(writer, reader2, currentLine2);
                } else {
                    currentLine1 = writeOldLineAndReadNew(writer, reader1, currentLine1);
                }
            }


        } finally {
            closeBuffer(reader1);
            closeBuffer(reader2);
        }
        return output;
    }


    private String writeOldLineAndReadNew(BufferedWriter writer, BufferedReader reader, String oldLine) throws IOException {
        writer.write(oldLine);
        writer.flush();
        writer.newLine();
        return reader.readLine();
    }

    private void closeBuffer(BufferedReader buffer) {
        try {
            buffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
