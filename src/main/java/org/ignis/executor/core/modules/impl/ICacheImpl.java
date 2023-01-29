package org.ignis.executor.core.modules.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ignis.executor.core.IExecutorData;
import org.ignis.executor.core.storage.IMemoryPartition;
import org.ignis.executor.core.storage.IPartition;
import org.ignis.executor.core.storage.IPartitionGroup;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ICacheImpl extends Module {

    private static final Logger LOGGER = LogManager.getLogger();

    private int nextContextId;
    private Map<Long, IPartitionGroup> context;
    private Map<Long, IPartitionGroup> cache;

    public ICacheImpl(IExecutorData executorData) {
        super(executorData, LOGGER);
        this.nextContextId = 11;
        this.context = new HashMap<>();
        this.cache = new HashMap<>();
    }

    private String fileCache() {
        return this.executorData.infoDirectory() + "/cache" + this.executorData.getContext().executorId() + ".bak";
    }

    public long saveContext() {
        long id = 0;
        if (this.context.size() < 11) {
            for (int i = 0; i < 11; i++) {
                if (!this.context.containsKey(i)) {
                    id = i;
                    break;
                }
            }
        } else {
            id = this.nextContextId;
            this.nextContextId += 1;
        }
        LOGGER.info("CacheContext: saving context " + id);
        this.context.put(id, this.executorData.getPartitionGroup());

        return id;
    }

    public void clearContext() {
        this.executorData.deletePartitions();
        this.executorData.clearVariables();
        this.executorData.getContext().vars().clear();
    }

    public void loadContext(long id) {
        this.executorData.clearVariables();
        this.executorData.getContext().vars().clear();
        IPartitionGroup value = this.context.get(id);
        if (value != null && value == this.executorData.getPartitionGroup()) {
            this.context.remove(id);
            return;
        }
        LOGGER.info("CacheContext: loading context " + id);

        if (value == null) {
            throw new IllegalArgumentException("context " + id + " not found");
        }
        this.executorData.setPartitions(value);
        this.context.remove(id);
    }

    public void loadContextAsVariable(long id, String name) {
        IPartitionGroup value = this.context.get(id);
        LOGGER.info("CacheContext: loading context " + id + " as variable " + name);

        if (value == null) {
            throw new IllegalArgumentException("context " + id + " not found");
        }
        this.executorData.setVariable(name, value);
        this.context.remove(id);
    }

    public void cache(long id, int level) {
        if (level == 0) { // NO CACHE
            LOGGER.error("Not implemented");
            IPartitionGroup value = this.cache.get(id);
            if (value == null) {
                LOGGER.warn("CacheContext: removing non existent cache " + id);
                return;
            }
            this.cache.remove(id);
            if (this.executorData.getPartitionTools().isDisk(value)) {
                String cache = this.fileCache();
                boolean found = false;
                List<String> lines = new ArrayList<>();
                try (
                        FileInputStream fileIS = new FileInputStream(cache);
                        BufferedReader br = new BufferedReader(
                                new InputStreamReader(fileIS, StandardCharsets.UTF_8))
                ) {
                    String line = br.readLine();
                    while (line != null) {
                        if (!found && line.startsWith((String.valueOf(id)) + '\0')) {
                            found = true;
                            continue;
                        }
                        lines.add(line);
                        line = br.readLine();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                try (
                        FileOutputStream fileOS = new FileOutputStream(cache);
                        BufferedWriter bw = new BufferedWriter(
                                new OutputStreamWriter(fileOS, StandardCharsets.UTF_8))
                ) {
                    for (String line : lines) {
                        bw.write(line + "\n");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        IPartitionGroup groupCache = this.executorData.getPartitionGroup();
        boolean sameLevel = true;
        int levelSel = level;

        if (level == 1) { // PRESERVE
            if (this.executorData.getPartitionTools().isDisk(groupCache))
                levelSel = 4;
            else if (this.executorData.getPartitionTools().isRawMemory(groupCache))
                levelSel = 3;
            else level = 2;
        }

        if (level == 2) { // MEMORY
            LOGGER.info("CacheContext: saving partition in " + IMemoryPartition.TYPE + " cache");
            if (!this.executorData.getPartitionTools().isMemory(groupCache)) {
                sameLevel = false;
                IPartitionGroup group = new IPartitionGroup();
                for (IPartition partCache : groupCache) {
                    IPartition part = this.executorData.getPartitionTools().newMemoryPartition(partCache.size());
                    partCache.copyTo(part);
                    group.add(part);
                }
                groupCache = group;
            }
        } else if (levelSel == 3) { // RAW_MEMORY
            LOGGER.error("Not implemented");
        } else if (levelSel == 4) { // DISK
            LOGGER.error("Not implemented");
        }

        /*if(sameLevel){
            this.cache = groupCache;
        }*/

        this.cache.put(id, groupCache);

    }

    public void loadCacheFromDisk() {
        LOGGER.error("Not implemented");
    }

    public void loadCache(long id) {
        LOGGER.info("CacheContext: loading partition from cache");
        IPartitionGroup value = this.cache.get(id);
        if (value == null) {
            throw new IllegalArgumentException("context " + id + " not found");
        }
        this.executorData.setPartitions(value);
    }

}
