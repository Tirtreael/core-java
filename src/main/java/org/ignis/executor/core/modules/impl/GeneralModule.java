package org.ignis.executor.core.modules.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TException;
import org.ignis.executor.api.function.IFunction;
import org.ignis.executor.api.function.IFunction2;
import org.ignis.executor.core.IExecutorData;
import org.ignis.executor.core.modules.IGeneralModule;
import org.ignis.rpc.IExecutorException;
import org.ignis.rpc.ISource;

import static org.ignis.executor.core.ILibraryLoader.loadISource;
import static org.ignis.executor.core.ILibraryLoader.loadISource2;

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
    public void map_(ISource src) throws IExecutorException {
        this.map(loadISource(src));
    }

    @Override
    public void filter(ISource src) {
        this.filter(loadISource(src));
    }

    @Override
    public void flatmap(ISource src) throws TException {
        this.flatmap(loadISource(src));
    }

    @Override
    public void keyBy(ISource src) throws TException {
        this.keyBy(loadISource(src));
    }

    @Override
    public void mapPartitions(ISource src) throws TException {
        this.mapPartitions(loadISource(src));
    }

    @Override
    public void mapPartitionsWithIndex(ISource src, boolean preservesPartitioning) throws TException {
        this.mapPartitionsWithIndex(loadISource2(src), preservesPartitioning);
    }

    @Override
    public void mapExecutor(ISource src) throws TException {
        this.mapExecutor(loadISource(src));
    }

    @Override
    public void mapExecutorTo(ISource src) throws TException {
        this.mapExecutorTo(loadISource(src));
    }

    @Override
    public void groupBy(ISource src, long numPartitions) throws TException {
        this.groupBy(loadISource(src), (int) numPartitions);
    }

    @Override
    public void sort(boolean ascending) {

    }

    @Override
    public void sort2(boolean ascending, long numPartitions) throws TException {

    }

    @Override
    public void sortBy(ISource src, boolean ascending) throws TException {

    }

    @Override
    public void sortBy3(ISource src, boolean ascending, long numPartitions) throws TException {

    }

    @Override
    public void flatMapValues(ISource src) throws TException {

    }

    @Override
    public void mapValues(ISource src) throws TException {

    }

    @Override
    public void groupByKey(long numPartitions) throws TException {

    }

    @Override
    public void groupByKey2(long numPartitions, ISource src) throws TException {

    }

    @Override
    public void reduceByKey(ISource src, long numPartitions, boolean localReduce) throws TException {

    }

    @Override
    public void aggregateByKey(ISource zero, ISource seqOp, long numPartitions) throws TException {

    }

    @Override
    public void aggregateByKey4(ISource zero, ISource seqOp, ISource combOp, long numPartitions) throws TException {

    }

    @Override
    public void foldByKey(ISource zero, ISource src, long numPartitions, boolean localFold) throws TException {

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
    public void sortByKey2a(boolean ascending, long numPartitions) throws TException {

    }

    @Override
    public void sortByKey2b(ISource src, boolean ascending) throws TException {

    }

    @Override
    public void sortByKey3(ISource src, boolean ascending, long numPartitions) throws TException {

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
