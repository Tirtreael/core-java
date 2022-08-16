package org.ignis.executor.core.modules.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ignis.executor.api.function.IFunction;
import org.ignis.executor.api.function.IFunction2;
import org.ignis.executor.core.IExecutorData;
import org.ignis.executor.core.modules.IGeneralModule;

public class GeneralModule extends Module implements IGeneralModule {

    private static final Logger LOGGER = LogManager.getLogger();
    IPipeImpl IPipe = new IPipeImpl(executorData);
    //    ISortImpl ISort
    IReduceImpl IReduce = new IReduceImpl(executorData);

    public GeneralModule(IExecutorData executorData) {
        super(executorData, LOGGER);
    }
//    IRepartitionImpl IRepartition

    @Override
    public void map(IFunction src) {
        try {
            IPipe.map(src);
        } catch (Exception e) {
            this.packException(e);
        }
    }

    @Override
    public void filter(IFunction src) {
        try {
            IPipe.filter(src);
        } catch (Exception e) {
            this.packException(e);
        }
    }

    @Override
    public void flatmap(IFunction src) {
        try {
            IPipe.flatmap(src);
        } catch (Exception e) {
            this.packException(e);
        }
    }

    @Override
    public void keyBy(IFunction src) {
        try {
            this.IPipe.keyBy(src);
        } catch (Exception e) {
            this.packException(e);
        }
    }

    @Override
    public void mapPartitions(IFunction src) {
        try {
            this.IPipe.mapPartitions(src);
        } catch (Exception e) {
            this.packException(e);
        }
    }

    @Override
    public void mapPartitionsWithIndex(IFunction2 src, boolean preservesPartitions) {
        try {
            this.IPipe.mapPartitionsWithIndex(src, preservesPartitions);
        } catch (Exception e) {
            this.packException(e);
        }
    }

    @Override
    public void mapExecutor(IFunction src) {
        try {
            this.IPipe.mapExecutor(src);
        } catch (Exception e) {
            this.packException(e);
        }
    }

    @Override
    public void mapExecutorTo(IFunction src) {
        try {
            this.IPipe.mapExecutorTo(src);
        } catch (Exception e) {
            this.packException(e);
        }
    }

    @Override
    public void groupBy(IFunction src, int numPartitions) {
        try {
            this.IPipe.keyBy(src);
            this.IReduce.groupByKey(numPartitions);

        } catch (Exception e) {
            this.packException(e);
        }
    }

    @Override
    public void sort(boolean ascending) {

    }

    @Override
    public void sort(boolean ascending, int numPartitions) {

    }

    @Override
    public void sortBy(IFunction src, boolean ascending) {

    }

    @Override
    public void sortBy(IFunction src, boolean ascending, int numPartitions) {

    }

    @Override
    public void flatMapValues(IFunction src) {

    }

    @Override
    public void mapValues(IFunction src) {

    }

    @Override
    public void groupByKey(int numPartitions) {

    }

    @Override
    public void groupByKey(int numPartitions, IFunction src) {

    }

    @Override
    public void reduceByKey(IFunction src, int numPartitions, boolean localReduce) {

    }

    @Override
    public void aggregateByKey(IFunction zero, IFunction seqOp, int numPartitions) {

    }

    @Override
    public void aggregateByKey(IFunction zero, IFunction seqOp, IFunction comb0p, int numPartitions) {

    }

    @Override
    public void foldByKey(IFunction zero, IFunction src, int numPartitions, boolean localFold) {

    }

    @Override
    public void sortByKey(boolean ascending) {

    }

    @Override
    public void sortByKey(boolean ascending, int numPartitions) {

    }

    @Override
    public void sortByKey(IFunction src, boolean ascending) {

    }

    @Override
    public void sortByKey(IFunction src, boolean ascending, int numPartitions) {

    }


}
