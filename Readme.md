#Implementing sort of big files (which may not fit in memory).
##Use of external sort methodology
The process is divided in 2 parts:

    1. Split the input File into chunks (ChunksSorter). Every chunk has a maximum size specified from ExternalSortUtils.chunkSizeFromFile. Those chunks will be read in memory as List<String>, then they will be sorted using standard libraries (Collections.sort, which uses quicksort or a modified version of mergesort). Afterwards they will be saved in a temp file.

    2. Merge the splited chunks. This process is like the merge process of the mergesort algorithm (ChunksMerger). Every two files form the output of the step 1 will generate a merged output file. This generated file will be sorted and will be concurrently send to this step again, until there is only one file left, which will be the final sorted output file. This part of the process is implemented using multiple threads (if supported and needed).

In summary. First, I split the input into several smaller sorted files and then I merge those sorted files (2:1) until I get the final output

##Testing:
An example test file which contains 5000 lines is provided. In order to force the system to use chunks, a very small chunk size has been specified manually . In this case, the small file will behave as a big one cause it is split into the small chunks.
