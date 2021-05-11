package org.ignis.executor.core.modules;

public interface IMathModule {

    void sample(boolean withReplacement, int num, int seed);

    void takeSample();

    void count();

    void max();

    void min();

    void max(Runnable cmp);

    void min(Runnable cmp);

    void sampleByKey();

    void countByKey();

    void countByValue();
}
