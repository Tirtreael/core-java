package org.ignis.executor.core.modules.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ignis.executor.api.IContext;
import org.ignis.executor.api.IReadIterator;
import org.ignis.executor.api.IWriteIterator;
import org.ignis.executor.api.Pair;
import org.ignis.executor.api.function.IFunction;
import org.ignis.executor.api.function.IFunction2;
import org.ignis.executor.core.IExecutorData;
import org.ignis.executor.core.modules.IGeneralModule;
import org.ignis.executor.core.storage.IPartition;
import org.ignis.executor.core.storage.IPartitionGroup;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

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
            inputGroup.clear();
            
            src.after(context);
            this.executorData.setPartitions(outputGroup);

        } catch (Exception e) {
            this.packException(e);
        }
    }

    @Override
    public void filter(IFunction src) {
        try {
            IContext context = this.executorData.getContext();
            IPartitionGroup inputGroup = this.executorData.getAndDeletePartitions();
            src.before(context);
            IPartitionGroup outputGroup = this.executorData.getPartitionTools().newPartitionGroup(inputGroup);
            LOGGER.info("General: filter " + inputGroup.size() + " partitions");


            for (int i = 0; i < inputGroup.size(); i++) {
                IWriteIterator it = outputGroup.get(i).writeIterator();
                for (Object obj : inputGroup.get(i)) {
                    if(src.call(obj, context) == Boolean.TRUE) {
                        it.write(obj);
                    }
                }
            }
            inputGroup.clear();

            src.after(context);
            this.executorData.setPartitions(outputGroup);

        } catch (Exception e) {
            this.packException(e);
        }
    }

    @Override
    public void flatmap(IFunction src) {
        try {
            IContext context = this.executorData.getContext();
            IPartitionGroup inputGroup = this.executorData.getAndDeletePartitions();
            src.before(context);
            IPartitionGroup outputGroup= this.executorData.getPartitionTools().newPartitionGroup(inputGroup);
            LOGGER.info("General: flatmap " + inputGroup.size() + " partitions");
            for(int i=0; i< inputGroup.size(); i++) {
                IWriteIterator it = outputGroup.get(i).writeIterator();
                for(Object obj : inputGroup.get(i)){
                    for(Object obj2 : (Iterable<?>) src.call(obj, context)){
                        it.write(obj2);
                    }
                }
            }
            inputGroup.clear();

            src.after(context);
            this.executorData.setPartitions(outputGroup);

        } catch (Exception e) {
            this.packException(e);
        }
    }

    @Override
    public void keyBy(IFunction src) {
        try {
            IContext context = this.executorData.getContext();
            IPartitionGroup inputGroup = this.executorData.getAndDeletePartitions();
            src.before(context);
            IPartitionGroup outputGroup= this.executorData.getPartitionTools().newPartitionGroup(inputGroup);
            LOGGER.info("General: keyBy " + inputGroup.size() + " partitions");
            for(int i=0; i< inputGroup.size(); i++) {
                IWriteIterator it = outputGroup.get(i).writeIterator();
                for(Object obj : inputGroup.get(i)){
                    it.write(new Pair<>(src.call(obj, context), obj));
                }
            }
            inputGroup.clear();

            src.after(context);
            this.executorData.setPartitions(outputGroup);

        } catch (Exception e) {
            this.packException(e);
        }
    }

    @Override
    public void mapPartitions(IFunction src) {
        try {
            IContext context = this.executorData.getContext();
            IPartitionGroup inputGroup = this.executorData.getAndDeletePartitions();
            src.before(context);
            IPartitionGroup outputGroup= this.executorData.getPartitionTools().newPartitionGroup(inputGroup);
            LOGGER.info("General: mapPartitions " + inputGroup.size() + " partitions");
            for(int i=0; i< inputGroup.size(); i++) {
                IWriteIterator it = outputGroup.get(i).writeIterator();
                for (IReadIterator iter = (IReadIterator) src.call(inputGroup.get(i).readIterator(), context); iter.hasNext(); ) {
                    Object obj = iter.next();
                    it.write(obj);
                }
            }
            inputGroup.clear();

            src.after(context);
            this.executorData.setPartitions(outputGroup);

        } catch (Exception e) {
            this.packException(e);
        }
    }

    @Override
    public void mapPartitionsWithIndex(IFunction2 src, boolean preservesPartitions) {
        try {
            IContext context = this.executorData.getContext();
            IPartitionGroup inputGroup = this.executorData.getAndDeletePartitions();
            src.before(context);
            IPartitionGroup outputGroup= this.executorData.getPartitionTools().newPartitionGroup(inputGroup);
            LOGGER.info("General: mapPartitionsWithIndex " + inputGroup.size() + " partitions");
            for(int i=0; i< inputGroup.size(); i++) {
                IWriteIterator it = outputGroup.get(i).writeIterator();
                for (IReadIterator iter = (IReadIterator) src.call(i, inputGroup.get(i).readIterator(), context); iter.hasNext(); ) {
                    Object obj = iter.next();
                    it.write(obj);
                }
            }
            inputGroup.clear();

            src.after(context);
            this.executorData.setPartitions(outputGroup);

        } catch (Exception e) {
            this.packException(e);
        }
    }

    @Override
    public void mapExecutor(IFunction src) {
        try {
            IContext context = this.executorData.getContext();
            IPartitionGroup inputGroup = this.executorData.getPartitionGroup();
            boolean inMemory = this.executorData.getPartitionTools().isMemory(inputGroup);

            src.before(context);
            LOGGER.info("General: mapExecutor " + inputGroup.size() + " partitions");
            if (!inMemory || inputGroup.isCache()) {
                LOGGER.info("General: loading partitions in memory");
                IPartitionGroup aux = this.executorData.getPartitionTools().newPartitionGroup();
                for (IPartition part : inputGroup) {
                    IPartition memoryPart = this.executorData.getPartitionTools().newMemoryPartition(part.size());
                    part.copyTo(memoryPart);
                    aux.add(memoryPart);
                }
                inputGroup = aux;
            }
            ArrayList<Object> arg = new ArrayList<>();
            for (IPartition part : inputGroup)
                arg.addAll(part.getElements());

            src.call(arg, context);

            if (!inMemory) {
                LOGGER.info("General: saving partitions from memory");
                IPartitionGroup aux = this.executorData.getPartitionTools().newPartitionGroup();
                for (IPartition memoryPart : inputGroup) {
                    IPartition part = this.executorData.getPartitionTools().newPartition(memoryPart);
                    memoryPart.copyTo(part);
                    aux.add(part);
                }
                inputGroup = aux;
            }
            src.after(context);
            this.executorData.setPartitions(inputGroup);

        } catch (Exception e) {
            this.packException(e);
        }
    }

    @Override
    public void mapExecutorTo(IFunction src) {
        try {
            IContext context = this.executorData.getContext();
            IPartitionGroup inputGroup = this.executorData.getPartitionGroup();
            IPartitionGroup outputGroup = this.executorData.getPartitionTools().newPartitionGroup();
            boolean inMemory = this.executorData.getPartitionTools().isMemory(inputGroup);

            src.before(context);
            LOGGER.info("General: mapExecutorTo " + inputGroup.size() + " partitions");
            if (!inMemory || inputGroup.isCache()) {
                LOGGER.info("General: loading partitions in memory");
                IPartitionGroup aux = this.executorData.getPartitionTools().newPartitionGroup();
                for (IPartition part : inputGroup) {
                    IPartition memoryPart = this.executorData.getPartitionTools().newMemoryPartition(part.size());
                    part.copyTo(memoryPart);
                    aux.add(memoryPart);
                }
                inputGroup = aux;
            }
            ArrayList<Object> arg = new ArrayList<>();
            for (IPartition part : inputGroup)
                arg.addAll(part.getElements());

            List<List<Object>> newParts = (List<List<Object>>) src.call(arg, context);
            LOGGER.info("General: moving elements to partitions");
            for (List<Object> v : newParts) {
                IPartition part = this.executorData.getPartitionTools().newMemoryPartition(0);
                part.setElements(v);
                outputGroup.add(part);
            }

            if (!inMemory) {
                LOGGER.info("General: saving partitions from memory");
                IPartitionGroup aux = this.executorData.getPartitionTools().newPartitionGroup();
                for (IPartition memoryPart : outputGroup) {
                    IPartition part = this.executorData.getPartitionTools().newPartition(memoryPart);
                    memoryPart.copyTo(part);
                    aux.add(part);
                }
                outputGroup = aux;
            }
            src.after(context);
            this.executorData.setPartitions(outputGroup);

        } catch (Exception e) {
            this.packException(e);
        }
    }

    @Override
    public void groupBy(IFunction src, int numPartitions) {
        try{
             this.keyBy(src);
//             groupByKey

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
