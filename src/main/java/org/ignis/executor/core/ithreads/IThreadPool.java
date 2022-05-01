package org.ignis.executor.core.ithreads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class IThreadPool {

    private static int defaultCores = 1;

    public static int getDefaultCores() {
        return defaultCores;
    }

    public static void setDefaultCores(int defaultCores) {
        IThreadPool.defaultCores = defaultCores;
    }

    public static void parallel(Consumer<Integer> f, int end) {
        parallel(defaultCores, f, end);
    }

    public static void parallel(int threads, Consumer<Integer> f, int end) {
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        int start = 0;
        int chunk = (int) (Math.ceil((double) (end - start) / (double) defaultCores));
        int chunkStart = 0;
        int next;
        for (int id = 0; id < threads; id++) {
            next = chunkStart + chunk;
            if(next > end)
                next = end;
            int finalNext = next;
            int finalChunkStart = chunkStart;
            executor.execute(() -> {
                for(int j = finalChunkStart; j < finalNext; j++){
                    f.accept(j);
                }
            });
            chunkStart = next;
        }
        executor.shutdown();
        try {
            if (!executor.awaitTermination(10, TimeUnit.MINUTES)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }

}
