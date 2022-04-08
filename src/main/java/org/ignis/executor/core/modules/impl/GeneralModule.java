package org.ignis.executor.core.modules.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TException;
import org.ignis.executor.api.IContext;
import org.ignis.executor.api.IWriteIterator;
import org.ignis.executor.api.function.IFunction;
import org.ignis.executor.core.IExecutorData;
import org.ignis.executor.core.modules.IGeneralModule;
import org.ignis.executor.core.storage.IPartitionGroup;

public class GeneralModule extends Module implements IGeneralModule {

    private static final Logger LOGGER = LogManager.getLogger();

    public GeneralModule(IExecutorData executorData) {
        super(executorData, LOGGER);
    }

    @Override
    public void map(IFunction src) {
        try {
            IContext context = this.executorData.getContext();
            IPartitionGroup inputGroup = this.executorData.getAndDeletePartitions();
            src.before(context);
            IPartitionGroup outputGroup= this.executorData.getPartitionTools().newPartitionGroup(inputGroup);
            LOGGER.info("General: map " + inputGroup.size() + " partitions");
            for(int i=0; i< inputGroup.size(); i++) {
                IWriteIterator it = outputGroup.get(i).writeIterator();
                for(Object obj : inputGroup.get(i)){
                    it.write(src.call(obj, context));
                }
            }
//            inputGroup.clear();
            
            src.after(context);
            this.executorData.setPartitions(outputGroup);

        } catch (TException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void filter(IFunction src) {

    }

    @Override
    public void flatmap(IFunction src) {

    }

    @Override
    public void keyBy(IFunction src) {

    }

    @Override
    public void mapPartitions(IFunction src) {

    }

    @Override
    public void mapPartitionsWithIndex(IFunction src, boolean preservesPartitions) {

    }

    @Override
    public void mapExecutor(IFunction src) {

    }

    @Override
    public void mapExecutorTo(IFunction src) {

    }

    @Override
    public void groupBy(IFunction src, int numPartitions) {

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
