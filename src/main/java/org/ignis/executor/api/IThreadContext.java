package org.ignis.executor.api;

public class IThreadContext {
    private IContext context;
    private int threadId;

    public IContext getContext() {
        return context;
    }

    public void setContext(IContext context) {
        this.context = context;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public IThreadContext(IContext context, int threadId) {
        this.context = context;
        this.threadId = threadId;
    }
}
