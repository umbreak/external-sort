package com.didac.sort;

import com.didac.sort.utils.ExternalSortUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by dmontero on 26/07/16.
 */
public class SortTestUtils {


    protected static File getFileFromResources(String name) throws URISyntaxException {
        URL resource = SortTestUtils.class.getResource("/" + name);
        return new File(resource.toURI());
    }

    protected static boolean isOutputSorted(File output, Charset charset) throws IOException {
        try (BufferedReader buffer = ExternalSortUtils.bufferFromFile(output, charset)) {
            String currentLine;
            String oldLine=buffer.readLine();
            while ((currentLine = buffer.readLine()) != null){
                if(oldLine.compareTo(currentLine) > 0)
                    return false;
                oldLine = currentLine;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    protected static Long countLines(File file, Charset charset){
        try (BufferedReader reader = Files.newBufferedReader(file.toPath(), charset)) {
            return reader.lines().count();
        } catch (IOException | UncheckedIOException e) {
            e.printStackTrace();
        }
        return null;
    }



    protected static boolean containsSameHashes(File input, File output, Charset charset) throws FileNotFoundException {
        Set<Integer> hashes = setOfHashesFromFile(input, charset);
        try (BufferedReader buffer = ExternalSortUtils.bufferFromFile(output, charset)) {
            String line;
            while ((line = buffer.readLine()) != null) {
                if(!hashes.contains(line.hashCode()))
                    return false;
            }
        }catch(IOException ex){
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    private static Set<Integer> setOfHashesFromFile(File file, Charset charset) throws FileNotFoundException {
        Set<Integer> hashes=new HashSet<>();
        try (BufferedReader buffer = ExternalSortUtils.bufferFromFile(file, charset)) {
            String line;
            while ((line = buffer.readLine()) != null) {
                hashes.add(line.hashCode());
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }
        return hashes;
    }
}
