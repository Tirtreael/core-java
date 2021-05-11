package org.ignis.executor.core.modules;

public interface IGeneralModule {

    void map(Runnable src);

    void filter(Runnable src);

    void flatmap(Runnable src);

    void keyBy(Runnable src);

    void mapPartitions(Runnable src);

    void mapPartitionsWithIndex(Runnable src, boolean preservesPartitions);

    void mapExecutor(Runnable src);

    void mapExecutorTo(Runnable src);

    void groupBy(Runnable src, int numPartitions);

    void sort(boolean ascending);

    void sort(boolean ascending, int numPartitions);

    void sortBy(Runnable src, boolean ascending);

    void sortBy(Runnable src, boolean ascending, int numPartitions);


    void flatMapValues(Runnable src);

    void mapValues(Runnable src);

    void groupByKey(int numPartitions);

    void groupByKey(int numPartitions, Runnable src);

    void reduceByKey(Runnable src, int numPartitions, boolean localReduce);

    void aggregateByKey(Runnable zero, Runnable seqOp, int numPartitions);

    void aggregateByKey(Runnable zero, Runnable seqOp, Runnable comb0p, int numPartitions);

    void foldByKey(Runnable zero, Runnable src, int numPartitions, boolean localFold);

    void sortByKey(boolean ascending);

    void sortByKey(boolean ascending, int numPartitions);

    void sortByKey(Runnable src, boolean ascending);

    void sortByKey(Runnable src, boolean ascending, int numPartitions);

}
