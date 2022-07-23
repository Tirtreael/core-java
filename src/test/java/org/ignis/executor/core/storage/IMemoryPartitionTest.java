package org.ignis.executor.core.storage;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TMemoryBuffer;
import org.apache.thrift.transport.TTransport;
import org.ignis.executor.api.IReadIterator;
import org.ignis.executor.api.IWriteIterator;
import org.ignis.executor.core.IElements;
import org.ignis.executor.core.protocol.IObjectProtocol;
import org.ignis.executor.core.transport.IZlibTransport;
import org.json.JSONObject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.NotSerializableException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IMemoryPartitionTest extends IElements {

//    @BeforeAll
//    void exampleData(){
//    }


    public static boolean compare(Object obj, Object obj2) {
        if (obj instanceof JSONObject && obj2 instanceof JSONObject)
            return obj.toString().equals(obj2.toString());
        else if (obj instanceof byte[] && obj2 instanceof byte[] && ((byte[]) obj).length == ((byte[]) obj2).length) {
            for (int i = 0; i < ((byte[]) obj).length; i++)
                if (((byte[]) obj)[i] != ((byte[]) obj2)[i])
                    return false;
            return true;
        } else return obj.equals(obj2);
    }

    @Disabled
    @ParameterizedTest
    @MethodSource({"createBoolean", "createByte", "createShort", "createInteger", "createLong", "createDouble",
            "createString", "createList", "createSet", "createMap", "createPair", "createBinary",
            "createPairList", "createJson"})
    void readWriteNativ(List<Object> elems) {
        boolean rNativ = false;
        IMemoryPartition partition1 = new IMemoryPartition();

        try {
            this.read(elems, partition1, rNativ);

            assertEquals(partition1.size(), 1);
            if (elems.get(0) instanceof JSONObject && partition1.getElements().get(0) instanceof JSONObject)
                assertEquals(elems.toString(), partition1.getElements().get(0).toString());
            else if (elems.get(0) instanceof byte[] && partition1.getElements().get(0) instanceof byte[]) {
                assertEquals(((byte[]) elems.get(0)).length, ((byte[]) partition1.getElements().get(0)).length);
                for (int i = 0; i < ((byte[]) elems.get(0)).length; i++)
                    assertEquals(((byte[]) elems.get(0))[i], ((byte[]) partition1.getElements().get(0))[i]);
            } else assertEquals(elems, partition1.getElements().get(0));

        } catch (TException | NotSerializableException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @ParameterizedTest
    @MethodSource({"createBoolean", "createByte", "createShort", "createInteger", "createLong", "createDouble",
            "createString", "createList", "createSet", "createMap", "createPair", "createBinary",
            "createPairList", "createJson"})
    void itWriteItRead(List<Object> elems) throws TException {
        IPartition partition = new IMemoryPartition();
        this.writeIterator(elems, partition);
        assertEquals(elems.size(), partition.size());
        List<Object> result = this.readIterator(partition);
        assertEquals(elems, result);
    }

    void itWriteTransRead(List<Object> elems, boolean wNative) throws TException, NotSerializableException {
        IMemoryPartition partition = new IMemoryPartition();
        this.writeIterator(elems, partition);
        assertEquals(elems.size(), partition.size());
        Object result = this.write(partition, wNative);
        List<?> resultList;
        if (result instanceof List) {
            resultList = (List<?>) result;
            compare(elems.get(0), resultList.get(0));
//            compare(elems.get(1), resultList.get(1));
        } else assertEquals(elems, result);
    }

    @ParameterizedTest
    @MethodSource({"createBoolean", "createByte", "createShort", "createInteger", "createLong", "createDouble",
            "createString", "createList", "createSet", "createMap", "createPair", "createBinary",
            "createPairList", "createJson"})
    void itWriteTransNativeRead(List<Object> elems) throws TException, NotSerializableException {
        itWriteTransRead(elems, true);
    }

    @ParameterizedTest
    @MethodSource({"createBoolean", "createByte", "createShort", "createInteger", "createLong", "createDouble",
            "createString", "createList", "createSet", "createMap", "createPair", "createBinary",
            "createPairList", "createJson"})
    void itWriteTransRead(List<Object> elems) throws TException, NotSerializableException {
        itWriteTransRead(elems, false);
    }

    void transWriteItRead(List<Object> elems, boolean rNative) throws TException, NotSerializableException {
        IMemoryPartition partition = new IMemoryPartition();
        this.read(elems, partition, rNative);
        assertEquals(elems.size(), partition.size());
        List<Object> result = this.readIterator(partition);
        compare(elems.get(0), result.get(0));
//        compare(elems.get(1), result.get(1));
    }

    @ParameterizedTest
    @MethodSource({"createBoolean", "createByte", "createShort", "createInteger", "createLong", "createDouble",
            "createString", "createList", "createSet", "createMap", "createPair", "createBinary",
            "createPairList", "createJson"})
    void transNativeWriteItRead(List<Object> elems) throws TException, NotSerializableException {
        transWriteItRead(elems, true);
    }

    @ParameterizedTest
    @MethodSource({"createBoolean", "createByte", "createShort", "createInteger", "createLong", "createDouble",
            "createString", "createList", "createSet", "createMap", "createPair", "createBinary",
            "createPairList", "createJson"})
    void transWriteItRead(List<Object> elems) throws TException, NotSerializableException {
        transWriteItRead(elems, false);
    }


    void transWriteTransRead(List<Object> elems, boolean wNative, boolean rNative) throws TException, NotSerializableException {
        IMemoryPartition partition = new IMemoryPartition();
        this.read(elems, partition, rNative);
        assertEquals(elems.size(), partition.size());
        Object result = this.write(partition, wNative);
        List<?> resultList;
        if (result instanceof List) {
            resultList = (List<?>) result;
            compare(elems.get(0), resultList.get(0));
//            compare(elems.get(1), resultList.get(1));
        } else assertEquals(elems, result);
    }

    @ParameterizedTest
    @MethodSource({"createBoolean", "createByte", "createShort", "createInteger", "createLong", "createDouble",
            "createString", "createList", "createSet", "createMap", "createPair", "createBinary",
            "createPairList", "createJson"})
    void transNativeWriteTransNativeRead(List<Object> elems) throws TException, NotSerializableException {
        transWriteTransRead(elems, true, true);
    }

    @ParameterizedTest
    @MethodSource({"createBoolean", "createByte", "createShort", "createInteger", "createLong", "createDouble",
            "createString", "createList", "createSet", "createMap", "createPair", "createBinary",
            "createPairList", "createJson"})
    void transNativeWriteTransRead(List<Object> elems) throws TException, NotSerializableException {
        transWriteTransRead(elems, true, false);
    }

    @ParameterizedTest
    @MethodSource({"createBoolean", "createByte", "createShort", "createInteger", "createLong", "createDouble",
            "createString", "createList", "createSet", "createMap", "createPair", "createBinary",
            "createPairList", "createJson"})
    void transWriteTransNativeRead(List<Object> elems) throws TException, NotSerializableException {
        transWriteTransRead(elems, false, true);
    }

    @ParameterizedTest
    @MethodSource({"createBoolean", "createByte", "createShort", "createInteger", "createLong", "createDouble",
            "createString", "createList", "createSet", "createMap", "createPair", "createBinary",
            "createPairList", "createJson"})
    void transWriteTransRead(List<Object> elems) throws TException, NotSerializableException {
        transWriteTransRead(elems, false, false);
    }


    /*
    UTILITY METHODS
     */
    /*
        Read from protocol to partitions
     */
    void read(Object elems, IMemoryPartition partition, boolean nativ) throws TException, NotSerializableException {
        TTransport memoryBuffer = new TMemoryBuffer(4096);
        TTransport zlib = new IZlibTransport(memoryBuffer);
        IObjectProtocol proto = new IObjectProtocol(zlib);

        proto.writeObject(elems, nativ, true);
        zlib.flush();
        partition.readAll(memoryBuffer);
    }

    Object write(IMemoryPartition partition, boolean nativ) throws TException, NotSerializableException {
        TTransport memoryBuffer = new TMemoryBuffer(4096);
        partition.write(memoryBuffer, 0, nativ);
        TTransport zlib = new IZlibTransport(memoryBuffer);
        IObjectProtocol proto = new IObjectProtocol(zlib);
        zlib.flush();
        return proto.readObject();
    }


    List<Object> readIterator(IPartition partition) throws TException {
        List<Object> elems = new ArrayList<>();
        IReadIterator it = partition.readIterator();
        while (it.hasNext()) {
            elems.add(it.next());
        }
        return elems;
    }

    void writeIterator(List<Object> elems, IPartition partition) throws TException {
        IWriteIterator it = partition.writeIterator();
        for (Object obj : elems)
            it.write(obj);
    }


    @Test
    void testClone() {
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

    @ParameterizedTest
    @MethodSource({"createBoolean", "createByte", "createShort", "createInteger", "createLong", "createDouble",
            "createString", "createList", "createSet", "createMap", "createPair", "createBinary",
            "createPairList", "createJson"})
    void clear(List<Object> elems) throws TException {
        IMemoryPartition partition = new IMemoryPartition();
        this.writeIterator(elems, partition);
        assertEquals(elems.size(), partition.size());
        partition.clear();
        assertEquals(0, partition.size());
        List<Object> result = this.readIterator(partition);
        assertEquals(0, result.size());
    }

    @Test
    void fit() {
    }

    @Test
    void type() {
    }

    @Test
    void iterator() {
    }


}