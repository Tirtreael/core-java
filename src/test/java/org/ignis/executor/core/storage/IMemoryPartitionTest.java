package org.ignis.executor.core.storage;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TMemoryBuffer;
import org.apache.thrift.transport.TTransport;
import org.ignis.executor.core.protocol.IObjectProtocol;
import org.ignis.executor.core.transport.IZlibTransport;
import org.junit.jupiter.api.Test;

import java.io.NotSerializableException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

class IMemoryPartitionTest {

//    @BeforeAll
//    void exampleData(){
//    }

    @Test
    void readWriteNativ(){

        Object result;
        boolean nativ = true;
        List<Map.Entry<Integer, String>> elements = List.of(new AbstractMap.SimpleEntry<>(1, "Mateo"),
                new AbstractMap.SimpleEntry<>(3, "Tomas"),
                new AbstractMap.SimpleEntry<>(17, "Berto"));

        IMemoryPartition partition = new IMemoryPartition(0);

        try {
            for(Map.Entry<Integer, String> element : elements)
                read(element, partition, nativ);
            assert elements.size() == partition.size();
            System.out.println(elements.size());
//            result = write(partition, nativ);
//            assert element.equals(result);
        } catch (TException | NotSerializableException e) {
            e.printStackTrace();
        }
    }

    @Test
    void iterator() {
    }

    @Test
    void testClone() {
    }

    /*
        Read from protocol to partitions
     */
    void read(Object elements, IMemoryPartition partition, boolean nativ) throws TException, NotSerializableException {
        TMemoryBuffer memoryBuffer = new TMemoryBuffer(0);
        TTransport zlib = new IZlibTransport(memoryBuffer);
        IObjectProtocol proto = new IObjectProtocol(zlib);

        proto.writeObject(elements, nativ);
        
        zlib.flush();

        partition.read(zlib);
        
        //proto.readByte();

    }

    Object write(IMemoryPartition partition, boolean nativ) throws TException, NotSerializableException {
        TMemoryBuffer memoryBuffer = new TMemoryBuffer(partition.size());
        partition.write(memoryBuffer, 6);
        TTransport zlib = new IZlibTransport(memoryBuffer);
        IObjectProtocol proto = new IObjectProtocol(zlib);
        return proto.readObject();
    }

    @Test
    void testWrite() {
    }

    @Test
    void testWrite1() {
    }

    @Test
    void readIterator() {
    }

    @Test
    void writeIterator() {
    }

    @Test
    void copyFrom() {
    }

    @Test
    void moveFrom() {
    }

    @Test
    void size() {
    }

    @Test
    void toBytes() {
    }

    @Test
    void clear() {
    }

    @Test
    void fit() {
    }

    @Test
    void type() {
    }
}