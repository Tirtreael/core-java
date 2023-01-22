/*
 * Copyright (C) 2018
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.ignis.executor.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * @author César Pomar
 */
public final class IThreads {

    private IThreads() {
    }

    private static int defaultThreads = 1;
    private static final Map<Long, Integer> ids = new HashMap<>();

    public static int getDefaultThreads() {
        return defaultThreads;
    }

    public static void setDefaultThreads(int defaultThreads) {
        IThreads.defaultThreads = defaultThreads;
    }

    public static int threadId() {
        return ids.getOrDefault(Thread.currentThread().threadId(), 0);
    }

    public static class IForBuilder {

        private final IRuntimeContext ctx;
        private boolean isStatic;
        private int chunk;
        private int start;

        private IForBuilder(IRuntimeContext ctx) {
            this.ctx = ctx;
            this.isStatic = true;
            this.chunk = -1;
            this.start = 0;
        }

        public IForBuilder _static() {
            isStatic = true;
            return this;
        }

        public IForBuilder dynamic() {
            isStatic = false;
            return this;
        }

        public IForBuilder chunk(int n) {
            chunk = n;
            return this;
        }

        public IForBuilder start(int n) {
            start = n;
            return this;
        }

        public void run(int end, Consumer<Integer> f) {
            if (ctx.loop) {
                throw new RuntimeException("parallel loop in parallel loop exception");
            }

            if (chunk == -1) {
                if (isStatic) {
                    chunk = Math.ceilDiv(end - this.start, this.ctx.threads);
                } else {
                    chunk = 1;
                }
            }


            List<Map.Entry<Integer, Integer>> data = null;
            int threadId = IThreads.threadId();
            if (threadId == 0 || isStatic) {
                if (isStatic) {
                    data = new ArrayList<>();
                } else {
                    data = ctx.data;
                    ctx.i_data.set(0);
                }

                data = isStatic ? new ArrayList<>() : ctx.data;

                int i = start;
                int id = 0;

                while (true) {
                    int next = i + chunk;
                    if (next > end) {
                        if (i < end) {
                            next = end;
                        } else {
                            break;
                        }
                    }
                    if (!isStatic || threadId == id) {
                        data.add(Map.entry(i, next));
                    }
                    i = next;
                    id++;
                    id = id % ctx.threads;
                }

            }
            ctx.barrier();
            ctx.loop = true;

            try {
                if (isStatic) {
                    for (Map.Entry<Integer, Integer> chunk : data) {
                        for (int i = chunk.getKey(); i < chunk.getValue(); i++) {
                            f.accept(i);
                        }
                    }
                } else {
                    data = ctx.data;
                    int c;
                    while ((c = ctx.i_data.getAndIncrement()) < data.size()) {
                        Map.Entry<Integer, Integer> chunk = data.get(c);
                        for (int i = chunk.getKey(); i < chunk.getValue(); i++) {
                            f.accept(i);
                        }
                    }
                }
            } finally {
                ctx.barrier();
                ctx.loop = false;
            }
        }
    }

    public static class IRuntimeContext {

        private final int threads;
        private volatile boolean loop;
        private volatile RuntimeException error;
        private final CyclicBarrier barrier;

        private final List<Map.Entry<Integer, Integer>> data;
        private final AtomicInteger i_data;

        private IRuntimeContext(int cores, Consumer<IRuntimeContext> f) {
            if (!ids.isEmpty()) {
                throw new RuntimeException("Already Running");
            }

            this.data = new ArrayList<>();
            this.i_data = new AtomicInteger();
            this.threads = cores;
            this.barrier = new CyclicBarrier(cores);

            Runnable work = () -> {
                try {
                    f.accept(this);
                } catch (RuntimeException ex) {
                    this.error = ex;
                }
            };

            List<Thread> workers = new ArrayList<>();
            IThreads.ids.put(Thread.currentThread().threadId(), 0);
            for (int i = 1; i < cores; i++) {
                Thread t = new Thread(work);
                IThreads.ids.put(t.threadId(), i);
                t.start();
                workers.add(t);
            }
            work.run();
            for (Thread worker : workers) {
                try{
                    worker.join();
                }catch (InterruptedException ex){
                }
            }
            IThreads.ids.clear();

            if (error != null) {
                throw error;
            }
        }

        public int threadId() {
            return IThreads.threadId();
        }

        public int threads() {
            return threads;
        }

        public void critical(Runnable f) {
            synchronized (barrier) {
                f.run();
            }
        }

        public void master(Runnable f) {
            if (threadId() == 0) {
                f.run();
            }
        }

        public void barrier() {
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }

        public IForBuilder ifor() {
            return new IForBuilder(this);
        }

    }

    public static void parallel(Consumer<IRuntimeContext> f) {
        parallel(getDefaultThreads(), f);
    }

    public static void parallel(int cores, Consumer<IRuntimeContext> f) {
        new IRuntimeContext(cores, f);
    }

}
