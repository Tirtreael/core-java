package org.ignis.executor.core.modules;

public interface IGeneralActionModule {


    void reduce(Runnable src);

    void treeReduce(Runnable src);

    void collect();

    void aggregate(Runnable zero, Runnable seqOp, Runnable combOp);

    void treeAggregate(Runnable zero, Runnable seqOp, Runnable combOp);

    void fold(Runnable zero, Runnable src);

    void treeFold(Runnable zero, Runnable src);

    void take(Runnable src, int num);

    void foreach(Runnable src);

    void foreachPartition(Runnable src);

    void top(Runnable src);

    void top(Runnable src, Runnable comparator);

    void takeOrdered(Runnable src);

    void takeOrdered(Runnable src, Runnable comparator);


    void keys();

    void values();
}
