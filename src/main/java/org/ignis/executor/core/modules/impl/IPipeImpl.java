package org.ignis.executor.core.modules.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TException;
import org.ignis.executor.api.*;
import org.ignis.executor.api.function.IFunction;
import org.ignis.executor.core.IExecutorData;
import org.ignis.executor.core.ithreads.IThreadPool;
import org.ignis.executor.core.storage.IPartitionGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IPipeImpl extends Module {

    private static final Logger LOGGER = LogManager.getLogger();

    public IPipeImpl(IExecutorData executorData) {
        super(executorData, LOGGER);
    }


    public void map(IFunction src) {
        IContext context = this.executorData.getContext();
        IPartitionGroup inputGroup = this.executorData.getAndDeletePartitions();
        src.before(context);
        IPartitionGroup outputGroup = this.executorData.getPartitionTools().newPartitionGroup(inputGroup);
        LOGGER.info("General: map " + inputGroup.size() + " partitions");
        try {
            ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            List<Callable<Void>> taskQueue = new ArrayList<>();
            // Create tasks
            for (int i = 0; i < inputGroup.size(); i++) {
                ConcurrentLinkedQueue<Object> dataQueue = new ConcurrentLinkedQueue<>(inputGroup.get(i).getElements());
                int finalI = i;
                taskQueue.add(() -> {
                    IWriteIterator it;
                    try {
                        it = outputGroup.get(finalI).writeIterator();
                        Object obj;
                        while ((obj = dataQueue.poll()) != null) {
                            it.write(src.call(obj, context));
                        }
                    } catch (TException e) {
                        throw new RuntimeException(e);
                    }
                    return null;
                });
            }
            // Execute tasks
            executorService.invokeAll(taskQueue);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        inputGroup.clear();

        src.after(context);
        this.executorData.setPartitions(outputGroup);
    }

    public void filter(IFunction src) {
        IContext context = this.executorData.getContext();
        IPartitionGroup inputGroup = this.executorData.getAndDeletePartitions();
        src.before(context);
        IPartitionGroup outputGroup = this.executorData.getPartitionTools().newPartitionGroup(inputGroup);
        LOGGER.info("General: filter " + inputGroup.size() + " partitions");

        IThreadPool.parallel((i) -> {
//                for (int i = 0; i < inputGroup.size(); i++) {
            IWriteIterator it;
            try {
                it = outputGroup.get(i).writeIterator();
                for (Object obj : inputGroup.get(i)) {
                    if (src.call(obj, context) == Boolean.TRUE) {
                        it.write(obj);
                    }
                }
            } catch (TException e) {
                this.packException(e);
            }
//                }
        }, inputGroup.size());
        inputGroup.clear();

        src.after(context);
        this.executorData.setPartitions(outputGroup);
    }

    public void flatmap(IFunction src) {
        IContext context = this.executorData.getContext();
        IPartitionGroup inputGroup = this.executorData.getAndDeletePartitions();
        src.before(context);
        IPartitionGroup outputGroup = this.executorData.getPartitionTools().newPartitionGroup(inputGroup);
        LOGGER.info("General: flatmap " + inputGroup.size() + " partitions");
        IThreadPool.parallel((i) -> {
//                for (int i = 0; i < inputGroup.size(); i++) {
            IWriteIterator it;
            try {
                it = outputGroup.get(i).writeIterator();
                for (Object obj : inputGroup.get(i)) {
                    for (Object obj2 : (Iterable<?>) src.call(obj, context)) {
                        it.write(obj2);
                    }
                }
            } catch (TException e) {
                this.packException(e);
            }
//                }
        }, inputGroup.size());
        inputGroup.clear();

        src.after(context);
        this.executorData.setPartitions(outputGroup);
    }

    public void keyBy(IFunction src) {
        IContext context = this.executorData.getContext();
        IPartitionGroup inputGroup = this.executorData.getAndDeletePartitions();
        src.before(context);
        IPartitionGroup outputGroup = this.executorData.getPartitionTools().newPartitionGroup(inputGroup);
        LOGGER.info("General: keyBy " + inputGroup.size() + " partitions");
        IThreadPool.parallel((i) -> {
//                for (int i = 0; i < inputGroup.size(); i++) {
            IWriteIterator it;
            try {
                it = outputGroup.get(i).writeIterator();
                for (Object obj : inputGroup.get(i)) {
                    it.write(new Pair<>(src.call(obj, context), obj));
                }
            } catch (TException e) {
                this.packException(e);
            }
//                }
        }, inputGroup.size());
        inputGroup.clear();

        src.after(context);
        this.executorData.setPartitions(outputGroup);
    }

    public void mapPartitions(IFunction src) {
        IContext context = this.executorData.getContext();
        IPartitionGroup inputGroup = this.executorData.getAndDeletePartitions();
        src.before(context);
        IPartitionGroup outputGroup = this.executorData.getPartitionTools().newPartitionGroup(inputGroup);
        LOGGER.info("General: mapPartitions " + inputGroup.size() + " partitions");
        IThreadPool.parallel((i) -> {
            IWriteIterator it;
            try {
                it = outputGroup.get(i).writeIterator();
                IReadIterator iter = (IReadIterator) src.call(inputGroup.get(i).readIterator(), context);
                while (iter.hasNext()) {
                    it.write(iter.next());
                }
            } catch (TException e) {
                this.packException(e);
            }
        }, inputGroup.size());
        inputGroup.clear();

        src.after(context);
        this.executorData.setPartitions(outputGroup);
    }


}
