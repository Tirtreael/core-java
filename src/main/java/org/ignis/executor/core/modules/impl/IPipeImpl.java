package org.ignis.executor.core.modules.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TException;
import org.ignis.executor.api.IContext;
import org.ignis.executor.api.IReadIterator;
import org.ignis.executor.api.IWriteIterator;
import org.ignis.executor.api.Pair;
import org.ignis.executor.api.function.IFunction;
import org.ignis.executor.api.function.IFunction0;
import org.ignis.executor.api.function.IFunction2;
import org.ignis.executor.core.IExecutorData;
import org.ignis.executor.core.ithreads.IThreadPool;
import org.ignis.executor.core.ithreads.IThreadPool2;
import org.ignis.executor.core.storage.IMemoryPartition;
import org.ignis.executor.core.storage.IPartition;
import org.ignis.executor.core.storage.IPartitionGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IPipeImpl extends Module {

    private static final Logger LOGGER = LogManager.getLogger();

    public IPipeImpl(IExecutorData executorData) {
        super(executorData, LOGGER);
    }

    public void execute(IFunction0 src) {
        IContext context = this.executorData.getContext();

        src.before(context);
        LOGGER.info("General: Execute");
        src.call(context);
        src.after(context);
    }

    public void executeTo(IFunction0 src) {
        IContext context = this.executorData.getContext();
        src.before(context);
        IPartitionGroup outputGroup = this.executorData.getPartitionTools().newPartitionGroup();

        LOGGER.info("General: ExecuteTo");
        IPartitionGroup newParts = (IPartitionGroup) src.call(context);
        LOGGER.info("General: moving elements to partitions");
        for (IPartition v : newParts) {
            IPartition part = this.executorData.getPartitionTools().newMemoryPartition(0);
            part.setElements(v.getElements());
            outputGroup.add(part);
        }

        if (!Objects.equals(this.executorData.getPropertyParser().partitionType(), IMemoryPartition.TYPE)) {
            LOGGER.info("General: saving partitions from memory");
            IPartitionGroup aux = this.executorData.getPartitionTools().newPartitionGroup();
            for (IPartition men : outputGroup) {
                IPartition part = this.executorData.getPartitionTools().newPartition();
                men.copyTo(part);
                aux.add(part);
            }
            outputGroup = aux;
        }

        src.after(context);
        this.executorData.setPartitions(outputGroup);
    }

    public void map(IFunction src) {
        IContext context = this.executorData.getContext();
        IPartitionGroup inputGroup = this.executorData.getAndDeletePartitions();
        src.before(context);
        IPartitionGroup outputGroup = this.executorData.getPartitionTools().newPartitionGroup(inputGroup);
        LOGGER.info("General: map " + inputGroup.size() + " partitions");
        try {
            IThreadPool2.parallel((Integer i) -> {
                IWriteIterator it;
                try {
                    it = outputGroup.get(i).writeIterator();
                    for (Object obj : inputGroup.get(i).getElements()) {
                        it.write(src.call(obj, context));
                    }
                } catch (TException e) {
                    throw new RuntimeException(e);
                }
            }, inputGroup.size());

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

        try {
            IThreadPool2.parallel((Integer i) -> {
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
            }, inputGroup.size());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

        try {
            IThreadPool2.parallel((Integer i) -> {
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
            }, inputGroup.size());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

        try {
            IThreadPool2.parallel((Integer i) -> {
                IWriteIterator it;
                try {
                    it = outputGroup.get(i).writeIterator();
                    for (Object obj : inputGroup.get(i)) {
                        it.write(new Pair<>(src.call(obj, context), obj));
                    }
                } catch (TException e) {
                    this.packException(e);
                }
            }, inputGroup.size());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
        try {
            IThreadPool2.parallel((Integer i) -> {
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        inputGroup.clear();

        src.after(context);
        this.executorData.setPartitions(outputGroup);
    }

    public void mapPartitionsWithIndex(IFunction2 src, boolean preservesPartitions) {
        try {
            IContext context = this.executorData.getContext();
            IPartitionGroup inputGroup = this.executorData.getAndDeletePartitions();
            src.before(context);
            IPartitionGroup outputGroup = this.executorData.getPartitionTools().newPartitionGroup(inputGroup);
            LOGGER.info("General: mapPartitionsWithIndex " + inputGroup.size() + " partitions");
            IThreadPool.parallel((i) -> {
                IWriteIterator it;
                try {
                    it = outputGroup.get(i).writeIterator();
                    for (IReadIterator iter = (IReadIterator) src.call(i, inputGroup.get(i).readIterator(), context); iter.hasNext(); ) {
                        Object obj = iter.next();
                        it.write(obj);
                    }
                } catch (TException e) {
                    this.packException(e);
                }
            }, inputGroup.size());
            inputGroup.clear();

            src.after(context);
            this.executorData.setPartitions(outputGroup);

        } catch (Exception e) {
            this.packException(e);
        }
    }

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
            List<List<Object>> arg = new ArrayList<>();
            for (IPartition part : inputGroup) {
                arg.add(part.getElements());
            }

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
            List<List<Object>> arg = new ArrayList<>();
            for (IPartition part : inputGroup) {
                arg.add(part.getElements());
            }

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

    public void foreachPartition(IFunction src) throws TException, InterruptedException {
        IContext context = this.executorData.getContext();
        IPartitionGroup inputGroup = this.executorData.getAndDeletePartitions();

        src.before(context);
        LOGGER.info("General: foreachPartition " + inputGroup.size() + " partitions");
        IThreadPool2.parallel((Integer i) -> {
            IReadIterator reader;
            try {
                reader = inputGroup.get(i).readIterator();
            } catch (TException e) {
                throw new RuntimeException(e);
            }
            src.call(reader, context);
        }, inputGroup.size());

        inputGroup.clear();
        src.after(context);
        this.executorData.deletePartitions();
    }

    public void foreach(IFunction src) throws InterruptedException {
        IContext context = this.executorData.getContext();
        IPartitionGroup inputGroup = this.executorData.getAndDeletePartitions();

        src.before(context);
        LOGGER.info("General: foreach " + inputGroup.size() + " partitions");
        IThreadPool2.parallel((Integer i) -> {
            for (Object elem : inputGroup.get(i)) {
                src.call(elem, context);
            }
        }, inputGroup.size());
        inputGroup.clear();
        src.after(context);
        this.executorData.deletePartitions();
    }

    public void foreachExecutor(IFunction src) {
        IContext context = this.executorData.getContext();
        IPartitionGroup inputGroup = this.executorData.getAndDeletePartitions();
        boolean inMemory = this.executorData.getPartitionTools().isMemory(inputGroup);

        src.before(context);
        LOGGER.info("General: foreachExecutor " + inputGroup.size() + " partitions");
        if (!inMemory) {
            LOGGER.info("General: loading partitions in memory");
            IPartitionGroup aux = this.executorData.getPartitionTools().newPartitionGroup();
            for (IPartition part : inputGroup) {
                IMemoryPartition men = this.executorData.getPartitionTools().newMemoryPartition(part.size());
                part.copyTo(men);
                aux.add(men);
            }
            inputGroup = aux;
        }
        var arg = new ArrayList<>();
        for (IPartition part : inputGroup) {
            arg.addAll(part.getElements());
        }
        src.call(arg, context);
    }

    public void take(int num) throws TException {
        IPartitionGroup inputGroup = this.executorData.getAndDeletePartitions();
        IPartitionGroup outputGroup = this.executorData.getPartitionTools().newPartitionGroup(inputGroup);
        int count = 0;
        for (IPartition part : inputGroup) {
            if (part.size() + count > num) {
                IPartition cut = this.executorData.getPartitionTools().newPartition(part.type());
                IReadIterator reader = part.readIterator();
                IWriteIterator writer = cut.writeIterator();
                while (count != num && reader.hasNext()) {
                    count += 1;
                    writer.write(reader.next());
                }
                outputGroup.add(cut);
                break;
            }
            count += part.size();
            outputGroup.add(part);
        }
        this.executorData.setPartitions(outputGroup);
    }

    public void keys() throws TException, InterruptedException {
        IPartitionGroup inputGroup = this.executorData.getAndDeletePartitions();
        IPartitionGroup outputGroup = this.executorData.getPartitionTools().newPartitionGroup(inputGroup);
        LOGGER.info("General: keys " + inputGroup.size() + " partitions");
        IThreadPool2.parallel((Integer i) -> {
            try {
                IWriteIterator it = outputGroup.get(i).writeIterator();
                for (Object elem : inputGroup.get(i)) {
                    it.write(((Pair<?, ?>) elem).getKey());
                }
            } catch (TException e) {
                throw new RuntimeException(e);
            }
        }, inputGroup.size());

        this.executorData.setPartitions(outputGroup);
    }

    public void values() throws TException, InterruptedException {
        IPartitionGroup inputGroup = this.executorData.getAndDeletePartitions();
        IPartitionGroup outputGroup = this.executorData.getPartitionTools().newPartitionGroup(inputGroup);
        LOGGER.info("General: values " + inputGroup.size() + " partitions");
        IThreadPool2.parallel((Integer i) -> {
            try {
                IWriteIterator it = outputGroup.get(i).writeIterator();
                for (Object elem : inputGroup.get(i)) {
                    it.write(((Pair<?, ?>) elem).getValue());
                }
            } catch (TException e) {
                throw new RuntimeException(e);
            }
        }, inputGroup.size());

        this.executorData.setPartitions(outputGroup);
    }

    public void flatMapValues(IFunction src) throws TException, InterruptedException {
        IContext context = this.executorData.getContext();
        IPartitionGroup inputGroup = this.executorData.getAndDeletePartitions();
        IPartitionGroup outputGroup = this.executorData.getPartitionTools().newPartitionGroup(inputGroup);
        src.before(context);
        LOGGER.info("General: flatMapValues " + inputGroup.size() + " partitions");

        IThreadPool2.parallel((Integer i) -> {
            try {
                IWriteIterator it = outputGroup.get(i).writeIterator();
                for (Object elem : inputGroup.get(i)) {
                    for (Object value2 : (Iterable<?>) src.call(((Pair<?, ?>) elem).getValue(), context)) {
                        it.write(new Pair<>(((Pair<?, ?>) elem).getKey(), value2));
                    }
                }

            } catch (TException e) {
                throw new RuntimeException(e);
            }
        }, inputGroup.size());

        inputGroup.clear();
        src.after(context);
        this.executorData.setPartitions(outputGroup);
    }

    public void mapValues(IFunction src) throws TException, InterruptedException {
        IContext context = this.executorData.getContext();
        IPartitionGroup inputGroup = this.executorData.getAndDeletePartitions();
        IPartitionGroup outputGroup = this.executorData.getPartitionTools().newPartitionGroup(inputGroup);
        src.before(context);
        LOGGER.info("General: mapValues " + inputGroup.size() + " partitions");

        IThreadPool2.parallel((Integer i) -> {
            try {
                IWriteIterator it = outputGroup.get(i).writeIterator();
                for (Object elem : inputGroup.get(i)) {
                    it.write(new Pair<>(((Pair<?, ?>) elem).getKey(), src.call(((Pair<?, ?>) elem).getValue(), context)));
                }

            } catch (TException e) {
                throw new RuntimeException(e);
            }
        }, inputGroup.size());

        inputGroup.clear();
        src.after(context);
        this.executorData.setPartitions(outputGroup);
    }


}
