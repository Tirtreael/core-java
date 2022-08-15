package org.ignis.executor.core.ithreads;

import org.apache.thrift.TException;
import org.ignis.executor.api.IWriteIterator;
import org.ignis.executor.core.storage.IPartitionGroup;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class IThreadPool {

    public static int defaultCores = Runtime.getRuntime().availableProcessors();
    //    private CyclicBarrier cyclicBarrier = new CyclicBarrier();
//    private RuntimeContextData rctxData = new RuntimeContextData();
//    private IRuntimeContext rctxImpl = new RuntimeContext();

    public static int getDefaultCores() {
        return defaultCores;
    }

    public static void setDefaultCores(int defaultCores) {
        IThreadPool.defaultCores = defaultCores;
    }

    public interface IRuntimeContext {
        int getThreads();

        int getThreadId();

        void critical(Runnable f);

        void master(Runnable f);

        void barrier() throws BrokenBarrierException, InterruptedException;

        ForBuilder forLoop();
    }

    /*private class CyclicBarrier {
        private int it;
        private int count;
        private int parties;
        private Condition cond;

        private void await() throws InterruptedException {
            this.cond.wait();

            this.count--;
            if (this.count == 0) {
                this.cond.signalAll();
                this.it++;
                this.count = this.parties;
            } else {
                int it = this.it;
                for (int i = 0; i < it; i++) {
                    this.cond.wait();
                }
            }
        }
    }*/

    public class RuntimeContextData {
        public int threads;
        public LinkedList<Object> data;
        public boolean loop;
        public LinkedList<Object> error;
        public Consumer<IRuntimeContext> f;
        //        private Mutex mutes;
//        public IThreadPool.CyclicBarrier barrier;
        private java.util.concurrent.CyclicBarrier barrier;

        public RuntimeContextData(int threads, LinkedList<Object> data, boolean loop, LinkedList<Object> error, Consumer<IRuntimeContext> f, java.util.concurrent.CyclicBarrier barrier) {
            this.threads = threads;
            this.data = data;
            this.loop = loop;
            this.error = error;
            this.f = f;
            this.barrier = barrier;
        }
    }

    public class RuntimeContext implements IRuntimeContext {
        RuntimeContextData rctxData;
        int threadId;

        public RuntimeContext(RuntimeContextData rctxData, int threadId) {
            this.rctxData = rctxData;
            this.threadId = threadId;
        }


        public int getThreads() {
            return rctxData.threads;
        }

        public int getThreadId() {
            return this.threadId;
        }

        public synchronized void critical(Runnable f) {
            f.run();
        }

        public void master(Runnable f) {
            if (this.getThreadId() == 0) {
                f.run();
            }
        }

        public void barrier() throws BrokenBarrierException, InterruptedException {
            rctxData.barrier.await();
        }

        public ForBuilder forLoop() {
            return new ForBuilder(this, true, this.getThreads(), -1, 0);
//            return () -> {};
        }

    }

    public void parallel(Consumer<IRuntimeContext> f) {
        parallel(defaultCores, f);
    }

    public void parallel(int threads, Consumer<IRuntimeContext> f) {
        RuntimeContextData rctx = new RuntimeContextData(threads, null, false, new LinkedList<>(), f, new CyclicBarrier(1));
//        rctx.barrier.cond = ;
        for (int i = 1; i < threads; i++) {
            worker(new RuntimeContext(rctx, i));
        }
        worker(new RuntimeContext(rctx, 0));

        /*
        for i := 0; i < threads; i++ {
            if err := <-rctx.error; err != nil {
			    r = err
		    }
	    }*/
    }

    private void worker(RuntimeContext rctx) {
//        Runnable f = () -> {
//            if r := recover(); r != nil {
//                if err, ok := r.(error); ok {
//                    rctx.err <- err
//                } else {
//                    rctx.error <- errors.New(fmt.Sprint(r))
//                }
//            }
//        };
        rctx.rctxData.f.accept(rctx);
    }


    public interface IForBuilder {
        IForBuilder staticFor(RuntimeContext rctx);

        IForBuilder dynamicFor(RuntimeContext rctx);

        IForBuilder threads(int n);

        IForBuilder chunk(int n);

        IForBuilder start(int n);

        IForBuilder run(int n, Consumer<Integer> f) throws Exception;
    }

    public class ForBuilder implements IForBuilder {
        public static final int defaultThreads = 4;
        public static final int defaultChunk = -1;
        public static final int defaultStart = 0;
        public RuntimeContext rctxImpl; //= new RuntimeContext();
        private boolean staticFor;
        private int threads;
        private int chunk;
        private int start;

        public ForBuilder(RuntimeContext rctx, boolean staticFor) {
            this.rctxImpl = rctx;
            this.staticFor = staticFor;
            this.threads = defaultThreads;
            this.chunk = defaultChunk;
            this.start = defaultStart;
        }

        public ForBuilder(RuntimeContext rctx, boolean staticFor, int threads, int chunk, int start) {
            this.rctxImpl = rctx;
            this.staticFor = staticFor;
            this.threads = threads;
            this.chunk = chunk;
            this.start = start;
        }

        public IForBuilder staticFor(RuntimeContext rctx) {
            return new ForBuilder(rctx, true);
        }

        public IForBuilder dynamicFor(RuntimeContext rctx) {
            return new ForBuilder(rctx, false);
        }

        public IForBuilder threads(int n) {
            if (n > 0 && n <= this.threads) {
                this.threads = n;
            }
            return this;
        }

        @Override
        public IForBuilder chunk(int n) {
            this.chunk = n;
            return this;
        }

        @Override
        public IForBuilder start(int n) {
            this.start = n;
            return this;
        }

        @Override
        public IForBuilder run(int end, Consumer<Integer> f) throws Exception {
            if (this.rctxImpl.rctxData.loop) {
//                this.rc
                throw new Exception("parallel loop in parallel loop error");
            }
            if (this.chunk == -1) {
                if (this.staticFor)
                    this.chunk = (int) (Math.ceil(end - this.start) / this.threads);
                else this.chunk = 1;
            }

//            Object obj = this.rctxImpl.rctxData.data.pop();
//            this.rctxImpl.rctxData.data = ;
            LinkedList<Object> data = this.rctxImpl.rctxData.data;
            int threadId = this.rctxImpl.threadId;
            if (threadId == 0 || this.staticFor) {
//                data = new LinkedList<>();
//                if (!this.staticFor)
//                    this.rctxImpl.rctxData.data = data; //= new LinkedList<>();
                int i = this.start;
                int id = 0;
                while (true) {
                    int next = i + this.chunk;
                    if (next > end) {
                        if (i < end)
                            next = end;
                        else break;
                    }
                    if (!this.staticFor || threadId == id) {
                        this.rctxImpl.rctxData.data.add(new int[]{i, next});
                    }
                    i = next;
                    i++;
                    id = id % this.threads;
                }
//                data.close()
            }
            this.rctxImpl.barrier();
            this.rctxImpl.rctxData.loop = true;
//            if(!this.staticFor)
//                data = this.rctxImpl.rctxData.data;

//            for(var ch : data){
//                for(int i = ch[0]; i < ch[1]; i++){
////                    if err := f(i); err != nil {
//                    this.rctxImpl.barrier();
//                    this.rctxImpl.rctxData.loop = false;
////                        return err
////                    }
//                }
//            }
            this.rctxImpl.barrier();
            this.rctxImpl.rctxData.loop = false;
            return null;

        }
    }


    public static void parallel(Consumer<Integer> f, int end) {
        parallel(defaultCores, f, end);
    }

    public static void parallel(int threads, Consumer<Integer> f, int end) {
//        ExecutorService executorService = Executors.newFixedThreadPool(threads);
//        List<Runnable> taskQueue = new ArrayList<>();
//        // Create tasks
//        for (int i = 0; i < inputGroup.size(); i++) {
//            ConcurrentLinkedQueue<Object> dataQueue = new ConcurrentLinkedQueue<>(inputGroup.get(i).getElements());
//            int finalI = i;
//            taskQueue.add(f);
//        }
//        // Execute tasks
//        for(Runnable runnable : taskQueue){
//            executorService.execute(runnable);
//        }


        ExecutorService executor = Executors.newFixedThreadPool(threads);
        int start = 0;
        int chunk = (int) (Math.ceil((double) (end - start) / (double) defaultCores));
        int chunkStart = 0;
        int next;
        for (int id = 0; id < threads; id++) {
            next = chunkStart + chunk;
            if (next > end)
                next = end;
            int finalNext = next;
            int finalChunkStart = chunkStart;
            executor.execute(() -> {
                for (int j = finalChunkStart; j < finalNext; j++) {
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
