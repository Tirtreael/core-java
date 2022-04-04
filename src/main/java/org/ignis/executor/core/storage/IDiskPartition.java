package org.ignis.executor.core.storage;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TFileTransport;
import org.apache.thrift.transport.TTransport;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class IDiskPartition extends IRawPartition {

    private String path;
    private boolean destroy;

    IDiskPartition(String path, int compression, boolean nativ, boolean persist, boolean read, Class<?> clazz) throws IOException, TException {
        super(new TFileTransport(path, read), compression, nativ);
        this.path = path;
        this.setCompression(compression);
        this.destroy = !persist;

    }


    @Override
    public String getType() {
        return null;
    }

    @Override
    public IPartition clone() {
        String newPath = this.path;
        int i = 0;
        while(Files.exists(Path.of(path + "." + i)))
            i++;
        newPath += "." + i;
        IPartition newPartition = null;
        try {
            newPartition = new IDiskPartition(newPath, this.getCompression(), this.isNativ(), false, false, List.class);
            this.copyTo(newPartition);
        } catch (IOException | TException e) {
            e.printStackTrace();
        }
        return newPartition;
    }

    @Override
    public long bytes() {
        return 0;
    }

    @Override
    public void copyTo(IPartition target) {
        super.copyTo(target);
    }

    @Override
    public void moveTo(IPartition source, IPartition target) {
        super.moveTo(source, target);
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public byte[] toBytes() {
        return new byte[0];
    }

    @Override
    public void fit() {

    }

    @Override
    public String type() {
        return null;
    }

    @Override
    public TTransport readTransport() {
        return null;
    }

    @Override
    public void writeHeader() {

    }
}
