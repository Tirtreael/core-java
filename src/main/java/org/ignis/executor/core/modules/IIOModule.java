package org.ignis.executor.core.modules;

public interface IIOModule {

    //void repartition(); //NO
    //void coalesce(); //NO

    void partitions(); //partitionCount ???

    void saveAsObjectFile(String path, String compression, int first);

    void saveAsTextFile(String path, int first);


    void saveAsJsonFile(String path, int first, boolean pretty);
}
