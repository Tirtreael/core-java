package org.ignis.executor.core.modules;

import org.ignis.executor.api.function.IFunction;

public interface IGeneralModule {

    void map(IFunction src);

    void filter(IFunction src);

    void flatmap(IFunction src);

    void keyBy(IFunction src);

    void mapPartitions(IFunction src);

    void mapPartitionsWithIndex(IFunction src, boolean preservesPartitions);

    void mapExecutor(IFunction src);

    void mapExecutorTo(IFunction src);

    void groupBy(IFunction src, int numPartitions);

    void sort(boolean ascending);

    void sort(boolean ascending, int numPartitions);

    void sortBy(IFunction src, boolean ascending);

    void sortBy(IFunction src, boolean ascending, int numPartitions);


    void flatMapValues(IFunction src);

    void mapValues(IFunction src);

    void groupByKey(int numPartitions);

    void groupByKey(int numPartitions, IFunction src);

    void reduceByKey(IFunction src, int numPartitions, boolean localReduce);

    void aggregateByKey(IFunction zero, IFunction seqOp, int numPartitions);

    void aggregateByKey(IFunction zero, IFunction seqOp, IFunction comb0p, int numPartitions);

    void foldByKey(IFunction zero, IFunction src, int numPartitions, boolean localFold);

    void sortByKey(boolean ascending);

    void sortByKey(boolean ascending, int numPartitions);

    void sortByKey(IFunction src, boolean ascending);

    void sortByKey(IFunction src, boolean ascending, int numPartitions);

}
