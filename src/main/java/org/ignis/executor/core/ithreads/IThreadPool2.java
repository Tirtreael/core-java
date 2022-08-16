package org.ignis.executor.core.ithreads;


import org.ignis.executor.core.storage.IPartitionGroup;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class IThreadPool2 {
    private static final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static final List<Callable<Void>> taskQueue = new ArrayList<>();


    public static void parallel(BiConsumer<Integer, List<Object>> f, IPartitionGroup inputGroup) throws InterruptedException {
        // Create tasks
        for (int i = 0; i < inputGroup.size(); i++) {
            List<Object> data = inputGroup.get(i).getElements();
            int finalI = i;
            taskQueue.add(() -> {
                f.accept(finalI, data);
                return null;
            });
        }
        // Execute tasks
        executorService.invokeAll(taskQueue);
    }

    public static void parallel(Consumer<Integer> f, int end) throws InterruptedException {
        // Create tasks
        for (int i = 0; i < end; i++) {
            int finalI = i;
            taskQueue.add(() -> {
                f.accept(finalI);
                return null;
            });
        }
        // Execute tasks
        executorService.invokeAll(taskQueue);
    }


}
