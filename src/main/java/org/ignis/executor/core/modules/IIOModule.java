package org.ignis.executor.core.modules;

public interface IIOModule extends IModule, org.ignis.rpc.executor.IIOModule.Iface {

    //void repartition(); //NO
    //void coalesce(); //NO

    void partitions(); //partitionCount ???

    void saveAsObjectFile(String path, String compression, int first);

}
